package com.example.food_label_scanner.text_recognition

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.ImageFormat
import android.graphics.Paint
import android.graphics.YuvImage
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.impl.utils.MatrixExt.postRotate
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Matrix

import kotlinx.coroutines.*
import java.io.File
import java.io.FileOutputStream
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import com.googlecode.tesseract.android.TessBaseAPI
import java.io.ByteArrayOutputStream


class TesseractOcrAnalyzer(
    private val context: Context,
    private val language: String = "ron",
    private val onDetectedTextUpdated: (String) -> Unit
) : ImageAnalysis.Analyzer {

    companion object {
        private const val TAG = "TesseractOcrAnalyzer"
        private const val TESSDATA_SUBDIR = "tessdata"
        private const val TRAINED_DATA_FILE = "eng.traineddata"
    }

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val tessBaseApi = TessBaseAPI()

    init {
        initializeTesseract()
    }

    private fun initializeTesseract() {
        val tessdataDir = File(context.filesDir, TESSDATA_SUBDIR)
        val trainedDataFile = File(tessdataDir, TRAINED_DATA_FILE)

        if (!trainedDataFile.exists()) {
            tessdataDir.mkdirs()
            copyTrainedData(trainedDataFile)
        }

        val dataPath = context.filesDir.absolutePath
        Log.d(TAG, "Initializing Tesseract with data path: $dataPath")
        Log.d(TAG, "Trained data file path: ${trainedDataFile.absolutePath}")
        val initSuccess = tessBaseApi.init(dataPath, language)
        if (!initSuccess) {
            Log.e(TAG, "Tesseract initialization failed")
            throw RuntimeException("Tesseract initialization failed")
        }
    }

    private fun copyTrainedData(destinationFile: File) {
        try {
            Log.d(TAG, "Copying trained data to: ${destinationFile.absolutePath}")
            context.assets.open("$TESSDATA_SUBDIR/$TRAINED_DATA_FILE").use { inputStream ->
                destinationFile.outputStream().use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
            Log.d(TAG, "Successfully copied trained data")
        } catch (e: Exception) {
            Log.e(TAG, "Error copying trained data: ${e.message}", e)
            throw RuntimeException("Failed to copy trained data", e)
        }
    }

    override fun analyze(imageProxy: ImageProxy) {
        scope.launch {
            try {
                val bitmap = imageProxy.toBitmap()
                val text = processBitmap(bitmap)
                if (text.isNotBlank()) {
                    withContext(Dispatchers.Main) {
                        onDetectedTextUpdated(text)
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error processing image: ${e.message}", e)
            } finally {
                imageProxy.close()
            }
        }
    }

    fun analyze(bitmap: Bitmap) {
        scope.launch {
            try {
                val text = processBitmap(bitmap)
                if (text.isNotBlank()) {
                    withContext(Dispatchers.Main) {
                        onDetectedTextUpdated(text)
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error processing bitmap: ${e.message}", e)
            }
        }
    }

    private suspend fun processBitmap(bitmap: Bitmap): String = withContext(Dispatchers.IO) {
        return@withContext suspendCoroutine { continuation ->
            try {
                if (bitmap.isRecycled || bitmap.width == 0 || bitmap.height == 0) {
                    Log.e(TAG, "Invalid bitmap: recycled or zero-sized")
                    continuation.resume("")
                    return@suspendCoroutine
                }
                val resizedBitmap = resizeBitmap(bitmap)
                tessBaseApi.setImage(resizedBitmap)
                val resultText = tessBaseApi.utF8Text ?: ""
                if (resizedBitmap != bitmap) resizedBitmap.recycle()
                continuation.resume(resultText)
            } catch (e: Exception) {
                Log.e(TAG, "Tesseract OCR failed: ${e.message}", e)
                continuation.resume("")
            }
        }
    }

    private fun resizeBitmap(bitmap: Bitmap, maxSize: Int = 1024): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val scale = maxSize.toFloat() / maxOf(width, height)
        if (scale >= 1) return bitmap
        val newWidth = (width * scale).toInt()
        val newHeight = (height * scale).toInt()
        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)
    }

    fun onDestroy() {
        tessBaseApi.end()
        scope.cancel()
    }

    private fun ImageProxy.toBitmap(): Bitmap {
        val yBuffer = planes[0].buffer
        val uBuffer = planes[1].buffer
        val vBuffer = planes[2].buffer

        val ySize = yBuffer.remaining()
        val uSize = uBuffer.remaining()
        val vSize = vBuffer.remaining()

        val nv21 = ByteArray(ySize + uSize + vSize)

        yBuffer.get(nv21, 0, ySize)
        vBuffer.get(nv21, ySize, vSize)
        uBuffer.get(nv21, ySize + vSize, uSize)

        val yuvImage = YuvImage(nv21, ImageFormat.NV21, width, height, null)
        val outputStream = ByteArrayOutputStream()
        try {
            // Use android.graphics.Rect explicitly
            yuvImage.compressToJpeg(android.graphics.Rect(0,0,width,height), 100, outputStream)
            val jpegBytes = outputStream.toByteArray()
            var bitmap = BitmapFactory.decodeByteArray(jpegBytes, 0, jpegBytes.size)
                ?: throw IllegalStateException("Failed to decode bitmap")

            val rotationDegrees = imageInfo.rotationDegrees
            if (rotationDegrees != 0) {
                bitmap = rotateBitmap(bitmap, rotationDegrees)
            }
            return bitmap
        } finally {
            outputStream.close()
        }
    }

    private fun rotateBitmap(source: Bitmap, rotationDegrees: Int): Bitmap {
        if (rotationDegrees == 0) return source

        val width = source.width
        val height = source.height
        val config = source.config ?: Bitmap.Config.ARGB_8888

        // Swap dimensions for 90 or 270 degrees rotation
        val (newWidth, newHeight) = when (rotationDegrees) {
            90, 270 -> height to width
            else -> width to height
        }

        val rotatedBitmap = Bitmap.createBitmap(newWidth, newHeight, config)
        val canvas = Canvas(rotatedBitmap)
        val paint = Paint().apply {
            isAntiAlias = true
            isFilterBitmap = true
        }

        canvas.rotate(rotationDegrees.toFloat(), newWidth / 2f, newHeight / 2f)
        canvas.drawBitmap(source, (newWidth - width) / 2f, (newHeight - height) / 2f, paint)

        if (source != rotatedBitmap) {
            source.recycle()
        }

        return rotatedBitmap
    }
}
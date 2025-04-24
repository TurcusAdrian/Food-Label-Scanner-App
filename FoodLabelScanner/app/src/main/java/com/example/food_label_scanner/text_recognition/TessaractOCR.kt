package com.example.food_label_scanner.text_recognition

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Matrix

import kotlinx.coroutines.*
import java.io.File
import java.io.FileOutputStream
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import com.googlecode.tesseract.android.TessBaseAPI


class TesseractOcrAnalyzer(
    private val context: Context,
    private val language: String = "ron",
    private val onDetectedTextUpdated: (String) -> Unit
) : ImageAnalysis.Analyzer {

    companion object {
        private const val TAG = "TesseractOcrAnalyzer"
        private const val DATA_PATH = "tesseract"
        private const val TESSDATA_SUBDIR = "tessdata"
        private const val TRAINED_DATA_FILE = "ron.traineddata"
    }

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val tessBaseApi = TessBaseAPI()

    init {
        initializeTesseract()
    }

    private fun initializeTesseract() {
        val tessdataDir = File(context.filesDir, "$DATA_PATH/$TESSDATA_SUBDIR")
        val trainedDataFile = File(tessdataDir, TRAINED_DATA_FILE)

        if (!trainedDataFile.exists()) {
            tessdataDir.mkdirs()
            copyTrainedData(trainedDataFile)
        }

        val dataPath = File(context.filesDir, DATA_PATH).absolutePath
        Log.d(TAG, "Initializing Tesseract with data path: $dataPath")
        Log.d(TAG, "Trained data file path: ${trainedDataFile.absolutePath}") // Add this line
        tessBaseApi.init(dataPath, language)
    }

    private fun copyTrainedData(destinationFile: File) {
        try {
            Log.d(TAG, "Copying trained data to: ${destinationFile.absolutePath}")
            context.assets.open("$TESSDATA_SUBDIR/$TRAINED_DATA_FILE").use { inputStream ->
                FileOutputStream(destinationFile).use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
            Log.d(TAG, "Successfully copied trained data")
        } catch (e: Exception) {
            Log.e(TAG, "Error copying trained data: ${e.message}")
        }
    }

    override fun analyze(imageProxy: ImageProxy) {
        scope.launch {
            val bitmap = imageProxy.toBitmap()
            imageProxy.close()
            val text = processBitmap(bitmap)
            if (text.isNotBlank()) {
                withContext(Dispatchers.Main) {
                    onDetectedTextUpdated(text)
                }
            }
        }
    }

    fun analyze(bitmap: Bitmap) { // Changed to accept Bitmap
        scope.launch {
            val text = processBitmap(bitmap)
            if (text.isNotBlank()) {
                withContext(Dispatchers.Main) {
                    onDetectedTextUpdated(text)
                }
            }
        }
    }

    private suspend fun processBitmap(bitmap: Bitmap): String = withContext(Dispatchers.IO) {
        return@withContext suspendCoroutine { continuation ->
            try {
                tessBaseApi.setImage(bitmap)
                val resultText = tessBaseApi.utF8Text
                continuation.resume(resultText ?: "")
            } catch (e: Exception) {
                Log.e(TAG, "Tesseract OCR failed: ${e.message}")
                continuation.resume("")
            }
        }
    }

    fun onDestroy() {
        tessBaseApi.end()
        scope.cancel()
    }
    // Efficient toBitmap conversion (moved inside the class)
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

        val yuvImage = android.graphics.YuvImage(nv21, android.graphics.ImageFormat.NV21, width, height, null)
        val outputStream = java.io.ByteArrayOutputStream()
        yuvImage.compressToJpeg(android.graphics.Rect(0, 0, width, height), 100, outputStream)
        val jpegBytes = outputStream.toByteArray()

        val bitmap = android.graphics.BitmapFactory.decodeByteArray(jpegBytes, 0, jpegBytes.size)

        // Apply rotation correction
        val rotationDegrees = imageInfo.rotationDegrees
        return if (rotationDegrees != 0) {
            val matrix = android.graphics.Matrix().apply {
                postRotate(rotationDegrees.toFloat())
            }
            Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        } else {
            bitmap
        }
    }
}
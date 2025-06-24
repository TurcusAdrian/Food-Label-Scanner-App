package com.example.food_label_scanner.text_recognition

import android.graphics.Bitmap
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import android.media.Image
import androidx.annotation.OptIn
import androidx.camera.core.ImageAnalysis
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class TextRecognitionAnalyzer(
    private val onDetectedTextUpdated: (String) -> Unit
) : ImageAnalysis.Analyzer {

    companion object {
        const val THROTTLE_TIMEOUT = 1_000L
    }

    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private val textRecognizer: TextRecognizer =
        TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    @OptIn(ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {
        scope.launch {
            val mediaImage: Image = imageProxy.image ?: run { imageProxy.close(); return@launch }
            val inputImage: InputImage =
                InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

            processTextFromImage(inputImage)
            imageProxy.close()
        }
    }

    fun analyzeFromBitmap(bitmap: Bitmap) {
        scope.launch {
            val inputImage = InputImage.fromBitmap(bitmap, 0)
            processTextFromImage(inputImage)
        }
    }

    private suspend fun processTextFromImage(inputImage: InputImage) {
        suspendCoroutine { continuation ->
            textRecognizer.process(inputImage)
                .addOnSuccessListener { visionText: Text ->
                    // Sort textBlocks by their average Y-coordinate (approximating section order)
                    val sortedBlocks = visionText.textBlocks.sortedBy { block ->
                        block.lines.mapNotNull { it.boundingBox?.top }.average().toInt()
                    }

                    // Process each block, sorting lines within it
                    val orderedText = sortedBlocks.joinToString("\n\n") { block ->
                        val sortedLines = block.lines.sortedWith(compareBy(
                            { it.boundingBox?.top ?: 0 }, // Primary sort by top Y-coordinate
                            { it.boundingBox?.left ?: 0 }  // Secondary sort by left X-coordinate
                        ))
                        sortedLines.joinToString("\n") { it.text }
                    }

                    if (orderedText.isNotBlank()) {
                         onDetectedTextUpdated(orderedText)
                    }
                }
                .addOnCompleteListener {
                    continuation.resume(Unit)
                }
        }
    }
}
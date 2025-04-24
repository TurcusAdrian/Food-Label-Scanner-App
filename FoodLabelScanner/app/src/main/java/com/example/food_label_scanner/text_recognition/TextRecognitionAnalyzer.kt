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

    // Method for analyzing text from an image proxy (live camera)
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

    // Method for analyzing text from a Bitmap (gallery image)
    fun analyzeFromBitmap(bitmap: Bitmap) {
        scope.launch {
            val inputImage = InputImage.fromBitmap(bitmap, 0)
            processTextFromImage(inputImage)
        }
    }

    // Shared method to process text from an InputImage
    private suspend fun processTextFromImage(inputImage: InputImage) {
        suspendCoroutine { continuation ->
            textRecognizer.process(inputImage)
                .addOnSuccessListener { visionText: Text ->
                    val detectedText: String = visionText.text
                    if (detectedText.isNotBlank()) {
                        onDetectedTextUpdated(detectedText)
                    }
                }
                .addOnCompleteListener {
                    continuation.resume(Unit)
                }
        }
    }
}
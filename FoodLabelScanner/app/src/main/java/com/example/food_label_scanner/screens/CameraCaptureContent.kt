package com.example.food_label_scanner.screens

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.food_label_scanner.R
import com.example.food_label_scanner.camera_functionality.CameraViewModel
import com.example.food_label_scanner.text_recognition.TextRecognitionAnalyzer


@Composable
fun CameraCaptureContent(
    onDetectedTextUpdated: (String) -> Unit,
    onPhotoCaptured: (Bitmap) -> Unit,
    sharedTextViewModel: SharedTextViewModel,
    navigationController: androidx.navigation.NavController
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val viewModel: CameraViewModel = hiltViewModel()
    var isCameraInitialized by remember { mutableStateOf(false) }
    var previewView by remember { mutableStateOf<PreviewView?>(null) }

    // Initialize camera preview
    LaunchedEffect(Unit) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build()
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                preview.setSurfaceProvider(previewView?.surfaceProvider)
                cameraProvider.bindToLifecycle(lifecycleOwner, cameraSelector, preview)
                isCameraInitialized = true
                Log.d("CameraContent", "Preview initialized and bound to lifecycle")
            } catch (e: Exception) {
                Log.e("CameraContent", "Failed to bind preview: ${e.message}", e)
            }
        }, ContextCompat.getMainExecutor(context))
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                PreviewView(context).apply {
                    layoutParams = android.widget.LinearLayout.LayoutParams(
                        android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                        android.view.ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    setBackgroundColor(android.graphics.Color.BLACK)
                    implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                    scaleType = PreviewView.ScaleType.FIT_START
                }.also { pv ->
                    previewView = pv
                }
            }
        )

        // Camera capture button
        FloatingActionButton(
            onClick = {
                if (isCameraInitialized) {
                    Log.d("CameraContent", "Capturing photo...")
                    viewModel.photoCapture(lifecycleOwner) { bitmap ->
                        Log.d("CameraContent", "Photo captured successfully, processing with TextRecognitionAnalyzer")
                        val textRecognitionAnalyzer = TextRecognitionAnalyzer { updatedText ->
                            Log.d("CameraContent", "Text detected: $updatedText")
                            onDetectedTextUpdated(updatedText)
                            if (updatedText.isNotBlank()){//updatedText.lowercase().contains("ingredients")) {
                                Log.d("CameraContent", "Ingrediente detected, navigating to TextDisplayScreen")
                                sharedTextViewModel.setDetectedText(updatedText)
                                navigationController.navigate(Screens.TextDisplay.routeWithoutArgs)
                            }
                        }
                        try {
                            textRecognitionAnalyzer.analyzeFromBitmap(bitmap)
                        } catch (e: Exception) {
                            Log.e("CameraContent", "Text recognition failed: ${e.message}", e)
                            onDetectedTextUpdated("Error detecting text: ${e.message}")
                        }
                        onPhotoCaptured(bitmap) // Pass the bitmap to the callback (though not used here)
                    }
                } else {
                    Log.w("CameraContent", "Camera not initialized, cannot capture photo")
                }
            },
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.camera_icon),
                contentDescription = "CameraIcon",
                modifier = Modifier.size(32.dp)
            )
        }
    }
}
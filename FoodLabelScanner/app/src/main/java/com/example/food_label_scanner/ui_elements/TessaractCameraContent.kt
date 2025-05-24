package com.example.food_label_scanner.ui_elements

import android.graphics.Bitmap
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner

@Composable
fun TesseractCameraContent(
    detectedText: String,
    onDetectedTextUpdated: (String) -> Unit,
    onPhotoCaptured: (Bitmap) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraController = remember { LifecycleCameraController(context) }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { ctx ->
                PreviewView(ctx).apply {
                    layoutParams = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    setBackgroundColor(4)
                    implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                    scaleType = PreviewView.ScaleType.FIT_START
                }.also { previewView ->
                    startTesseractRecognition(
                        context = ctx,
                        cameraController = cameraController,
                        lifecycleOwner = lifecycleOwner,
                        previewView = previewView,
                        onDetectedTextUpdated = onDetectedTextUpdated
                    )
                }
            }
        )

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(16.dp),
            text = detectedText
        )
    }
}
package com.example.food_label_scanner.ui_elements

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LifecycleOwner
import com.example.food_label_scanner.text_recognition.startTextRecognition

@Composable
fun CameraContent(
    detectedText : String,
    onDetectedTextUpdated: (String) -> Unit,
    onPhotoCaptured : (Bitmap) -> Unit
){
    val context : Context = LocalContext.current
    val lifecycleOwner : LifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
    val cameraController : LifecycleCameraController = remember { LifecycleCameraController(context) }


    Box(modifier = Modifier.fillMaxSize(),
        contentAlignment = androidx.compose.ui.Alignment.BottomCenter
    ){
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                PreviewView(context).apply {
                    layoutParams = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    setBackgroundColor(Color.BLACK)
                    implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                    scaleType = PreviewView.ScaleType.FIT_START
                }.also { previewView ->
                    startTextRecognition(
                        context = context,
                        cameraController = cameraController,
                        lifecycleOwner = lifecycleOwner,
                        previewView = previewView,
                        onDetectedTextUpdated = onDetectedTextUpdated
                    )
                }
            }
        )

        Text(
            modifier = Modifier.fillMaxWidth().background(androidx.compose.ui.graphics.Color.White)
                .padding(16.dp),
            text = detectedText,
        )

        // add last photo taken preview here if wanted
    }
}
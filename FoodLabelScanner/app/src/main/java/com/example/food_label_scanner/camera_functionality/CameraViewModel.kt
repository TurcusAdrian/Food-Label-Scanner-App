package com.example.food_label_scanner.camera_functionality

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.Executor

import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(
    private val savePhotoToGallery: SavePhotoToGallery,
    @ApplicationContext private val context: Context
) : ViewModel() {

    fun getCameraController(): LifecycleCameraController = LifecycleCameraController(context).apply {
        setEnabledUseCases(0) // Disable all use cases initially
    }

    fun photoCapture(
        lifecycleOwner: LifecycleOwner,
        onPhotoCaptured: (Bitmap) -> Unit
    ) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val imageCapture = ImageCapture.Builder()
                .setTargetRotation(android.view.Surface.ROTATION_0)
                .build()

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(lifecycleOwner, cameraSelector, imageCapture)

                imageCapture.takePicture(
                    ContextCompat.getMainExecutor(context),
                    object : ImageCapture.OnImageCapturedCallback() {
                        override fun onCaptureSuccess(image: ImageProxy) {
                            val correctedBitmap: Bitmap = image
                                .toBitmap()
                                .rotateBitmap(image.imageInfo.rotationDegrees)

                            // Magnify the bitmap (e.g., scale by 2x)
                            val scaleFactor = 4f
                            val magnifiedBitmap = Bitmap.createScaledBitmap(
                                correctedBitmap,
                                (correctedBitmap.width * scaleFactor).toInt(),
                                (correctedBitmap.height * scaleFactor).toInt(),
                                true // Filter for better quality
                            )

                            // Recycle the original bitmap to free memory
                            correctedBitmap.recycle()

                            onPhotoCaptured(magnifiedBitmap)
                            image.close()

                            cameraProvider.unbind(imageCapture)
                            Log.d("CameraViewModel", "Image captured, magnified, and unbound successfully")
                        }

                        override fun onError(exception: ImageCaptureException) {
                            Log.e("CameraViewModel", "Error capturing image: ${exception.message}", exception)
                        }
                    }
                )
            } catch (exc: Exception) {
                Log.e("CameraViewModel", "Use case binding failed: ${exc.message}", exc)
            }
        }, ContextCompat.getMainExecutor(context))
    }

    fun Bitmap.rotateBitmap(rotationDegrees: Int): Bitmap {
        val matrix = Matrix().apply {
            postRotate(rotationDegrees.toFloat())
        }
        return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
    }

    private val _capturedImage = MutableStateFlow<Bitmap?>(null)
    val capturedImage = _capturedImage.asStateFlow()

    fun storePhotoInGallery(bitmap: Bitmap) {
        viewModelScope.launch {
            savePhotoToGallery.call(bitmap)
            _capturedImage.value?.recycle()
            _capturedImage.value = bitmap
        }
    }

    override fun onCleared() {
        _capturedImage.value?.recycle()
        super.onCleared()
    }
}
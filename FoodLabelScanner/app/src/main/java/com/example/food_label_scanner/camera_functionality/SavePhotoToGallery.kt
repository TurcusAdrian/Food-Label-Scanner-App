package com.example.food_label_scanner.camera_functionality

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import com.google.android.datatransport.runtime.dagger.Component.Factory
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.OutputStream
import java.time.LocalDateTime
import javax.inject.Inject

//@Factory
class SavePhotoToGallery @Inject constructor( @ApplicationContext private val context: Context, ){

    //2025-03-16T16_20_56.937276 si trebe 20250316_162056
    val timeOfCreation = LocalDateTime.now()

    fun timeFormat(timeofCreation: LocalDateTime) : String{
        var timeForPhoto = timeofCreation.toString()

        if(timeForPhoto.length>15){
            timeForPhoto= timeForPhoto.substring(0,19)
        }

        timeForPhoto = timeForPhoto.replace("-", "")
        timeForPhoto = timeForPhoto.replace(":", "")
        timeForPhoto = timeForPhoto.replace("T", "_")
        timeForPhoto = timeForPhoto.replace("_", "")
        timeForPhoto = timeForPhoto.substring(0, 8) + "_" + timeForPhoto.substring(8) //copiaza partea cu data, pune _ si apoi concateneaza cu oraminutsecunda

        return timeForPhoto
    }

    suspend fun call(capturePhotoBitmap: Bitmap): Result<Unit> = withContext(Dispatchers.IO){

        val resolver : ContentResolver = context.applicationContext.contentResolver

        val imageCollection: Uri = when{
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
            else -> MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }

        val nowTimestamp: Long = System.currentTimeMillis()
        val imageContentValues: ContentValues = ContentValues().apply{

            put(MediaStore.Images.Media.DISPLAY_NAME, "IMG_" + timeFormat(timeOfCreation) + ".jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpg")

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                put(MediaStore.MediaColumns.DATE_TAKEN, nowTimestamp)
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                put(MediaStore.MediaColumns.IS_PENDING, 1)
            }


            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
                put(MediaStore.MediaColumns.DATE_TAKEN, nowTimestamp)
                put(MediaStore.MediaColumns.DATE_ADDED, nowTimestamp)
                put(MediaStore.MediaColumns.DATE_MODIFIED, nowTimestamp)
                put(MediaStore.Images.Media.AUTHOR, "adi")
                put(MediaStore.Images.Media.DESCRIPTION, "descriere")
            }
        }

        val imageMediaStoreUri: Uri? = resolver.insert(imageCollection, imageContentValues)

        val result: Result<Unit> = imageMediaStoreUri?.let { uri ->
            kotlin.runCatching {
                resolver.openOutputStream(uri).use { outputStream: OutputStream? ->
                    checkNotNull(outputStream) { "Couldn't create file for gallery, MediaStore output stream is null" }
                    capturePhotoBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    imageContentValues.clear()
                    imageContentValues.put(MediaStore.MediaColumns.IS_PENDING, 0)
                    resolver.update(uri, imageContentValues, null, null)
                }
                Result.success(Unit)
            }.getOrElse { exception: Throwable ->
                exception.message?.let(::println)
                resolver.delete(uri, null, null)
                Result.failure(exception)
            }
        } ?: run{
            Result.failure(Exception("Couldn't create file for gallery"))
        }

        return@withContext result
    }
}
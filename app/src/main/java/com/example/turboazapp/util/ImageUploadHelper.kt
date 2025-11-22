package com.example.turboazapp.util

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject

class ImageUploadHelper @Inject constructor(
    private val storage: FirebaseStorage
) {
    suspend fun uploadImages(imageUris: List<String>): List<String> {
        android.util.Log.d("ImageUpload", "Starting upload for ${imageUris.size} images")
        val uploadedUrls = mutableListOf<String>()

        imageUris.forEach { uriString ->
            try {
                // ✅ Əgər artıq Firebase URL-dirsə, birbaşa əlavə et
                if (uriString.startsWith("https://firebasestorage.googleapis.com")) {
                    android.util.Log.d(
                        "ImageUpload",
                        "Already uploaded, using existing URL: $uriString"
                    )
                    uploadedUrls.add(uriString)
                    return@forEach
                }

                android.util.Log.d("ImageUpload", "Uploading new image: $uriString")
                val uri = Uri.parse(uriString)
                val fileName = "car_images/${UUID.randomUUID()}.jpg"
                val storageRef = storage.reference.child(fileName)

                storageRef.putFile(uri).await()
                val downloadUrl = storageRef.downloadUrl.await()
                uploadedUrls.add(downloadUrl.toString())

                android.util.Log.d("ImageUpload", "SUCCESS: $downloadUrl")
            } catch (e: Exception) {
                android.util.Log.e("ImageUpload", "FAILED: ${e.message}", e)
            }
        }

        android.util.Log.d("ImageUpload", "Total processed: ${uploadedUrls.size}")
        return uploadedUrls
    }
}
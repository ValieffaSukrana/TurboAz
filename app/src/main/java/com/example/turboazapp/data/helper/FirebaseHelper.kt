package com.example.turboazapp.data.helper

import android.content.Context
import android.provider.Settings
import android.util.Log
import com.example.turboazapp.domain.model.Car
import com.example.turboazapp.domain.model.FavoriteCar
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseHelper @Inject constructor(
    private val firestore: FirebaseFirestore,
    @ApplicationContext private val context: Context
) {
    companion object {
        private const val CARS_COLLECTION = "cars"
        private const val FAVORITES_COLLECTION = "favorites"
    }

    private fun getDeviceId(): String {
        return Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ANDROID_ID
        )
    }

    // ==================== CARS ====================

    suspend fun saveCarsToFirebase(cars: List<Car>): Result<Unit> {
        return try {
            val batch = firestore.batch()

            cars.forEach { car ->
                val docRef = firestore.collection(CARS_COLLECTION).document(car.id)
                batch.set(docRef, hashMapOf(
                    "id" to car.id,
                    "brand" to car.brand,
                    "model" to car.model,
                    "price" to car.price,
                    "url" to car.url,
                    "engine" to car.engine,
                    "color" to car.color,
                    "transmission" to car.transmission,
                    "fuel" to car.fuel,
                    "year" to car.year,
                    "timestamp" to System.currentTimeMillis()
                ))
            }

            batch.commit().await()
            Log.d("FirebaseHelper", "✅ ${cars.size} maşın Firebase-ə yazıldı")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("FirebaseHelper", "❌ Xəta: ${e.message}")
            Result.failure(e)
        }
    }

    fun getAllCars(): Flow<List<Car>> = callbackFlow {
        val listener = firestore.collection(CARS_COLLECTION)
            .orderBy("brand")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val cars = snapshot?.documents?.mapNotNull { doc ->
                    Car(
                        id = doc.getString("id") ?: "",
                        brand = doc.getString("brand") ?: "",
                        model = doc.getString("model") ?: "",
                        price = doc.getDouble("price") ?: 0.0,
                        url = doc.getString("url") ?: "",
                        engine = doc.getString("engine") ?: "",
                        color = doc.getString("color") ?: "",
                        transmission = doc.getString("transmission") ?: "",
                        fuel = doc.getString("fuel") ?: "",
                        year = doc.getLong("year")?.toInt() ?: 0
                    )
                } ?: emptyList()

                trySend(cars)
            }

        awaitClose { listener.remove() }
    }

    suspend fun getCarsCount(): Int {
        return try {
            firestore.collection(CARS_COLLECTION).get().await().size()
        } catch (e: Exception) {
            0
        }
    }

    suspend fun getCarById(carId: String): Car? {
        return try {
            val doc = firestore.collection(CARS_COLLECTION)
                .document(carId)
                .get()
                .await()

            if (doc.exists()) {
                Car(
                    id = doc.getString("id") ?: "",
                    brand = doc.getString("brand") ?: "",
                    model = doc.getString("model") ?: "",
                    price = doc.getDouble("price") ?: 0.0,
                    url = doc.getString("url") ?: "",
                    engine = doc.getString("engine") ?: "",
                    color = doc.getString("color") ?: "",
                    transmission = doc.getString("transmission") ?: "",
                    fuel = doc.getString("fuel") ?: "",
                    year = doc.getLong("year")?.toInt() ?: 0
                )
            } else null
        } catch (e: Exception) {
            null
        }
    }

    // ==================== FAVORITES ====================

    suspend fun addFavorite(car: Car): Result<Unit> {
        return try {
            val deviceId = getDeviceId()
            val favoriteId = "${deviceId}_${car.id}"

            firestore.collection(FAVORITES_COLLECTION)
                .document(favoriteId)
                .set(hashMapOf(
                    "id" to favoriteId,
                    "carId" to car.id,
                    "brand" to car.brand,
                    "model" to car.model,
                    "price" to car.price,
                    "url" to car.url,
                    "engine" to car.engine,
                    "color" to car.color,
                    "transmission" to car.transmission,
                    "fuel" to car.fuel,
                    "year" to car.year,
                    "deviceId" to deviceId,
                    "timestamp" to System.currentTimeMillis()
                ))
                .await()

            Log.d("FirebaseHelper", "✅ ${car.brand} ${car.model} seçildi")
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun removeFavorite(carId: String): Result<Unit> {
        return try {
            val deviceId = getDeviceId()
            val favoriteId = "${deviceId}_${carId}"

            firestore.collection(FAVORITES_COLLECTION)
                .document(favoriteId)
                .delete()
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getFavorites(): Flow<List<FavoriteCar>> = callbackFlow {
        val deviceId = getDeviceId()

        val listener = firestore.collection(FAVORITES_COLLECTION)
            .whereEqualTo("deviceId", deviceId)
            .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val favorites = snapshot?.documents?.mapNotNull { doc ->
                    FavoriteCar(
                        id = doc.getString("id") ?: "",
                        carId = doc.getString("carId") ?: "",
                        brand = doc.getString("brand") ?: "",
                        model = doc.getString("model") ?: "",
                        price = doc.getDouble("price") ?: 0.0,
                        url = doc.getString("url") ?: "",
                        engine = doc.getString("engine") ?: "",
                        color = doc.getString("color") ?: "",
                        transmission = doc.getString("transmission") ?: "",
                        fuel = doc.getString("fuel") ?: "",
                        year = doc.getLong("year")?.toInt() ?: 0,
                        deviceId = doc.getString("deviceId") ?: "",
                        timestamp = doc.getLong("timestamp") ?: 0L
                    )
                } ?: emptyList()

                trySend(favorites)
            }

        awaitClose { listener.remove() }
    }

    suspend fun isFavorite(carId: String): Boolean {
        return try {
            val deviceId = getDeviceId()
            val favoriteId = "${deviceId}_${carId}"

            firestore.collection(FAVORITES_COLLECTION)
                .document(favoriteId)
                .get()
                .await()
                .exists()
        } catch (e: Exception) {
            false
        }
    }
}
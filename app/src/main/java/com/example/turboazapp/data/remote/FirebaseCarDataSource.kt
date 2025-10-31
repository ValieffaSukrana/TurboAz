package com.example.turboazapp.data.remote

import com.example.turboazapp.domain.model.Filter
import com.example.turboazapp.util.Constants
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class FirebaseCarDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    private val carsCollection = firestore.collection(Constants.CARS_COLLECTION)

    // Bütün elanları gətir
    fun getCars(): Flow<List<CarDto>> = callbackFlow {
        val listener = carsCollection
            .whereEqualTo("status", "ACTIVE")
            .orderBy("created_at", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val cars = snapshot?.documents?.mapNotNull {
                    it.toObject(CarDto::class.java)?.copy(id = it.id)
                } ?: emptyList()

                trySend(cars)
            }

        awaitClose { listener.remove() }
    }

    // ID-yə görə elan gətir
    suspend fun getCarById(carId: String): CarDto? {
        return try {
            val document = carsCollection.document(carId).get().await()
            document.toObject(CarDto::class.java)?.copy(id = document.id)
        } catch (e: Exception) {
            null
        }
    }

    // Filtrlə axtarış
    fun searchCars(filter: Filter): Flow<List<CarDto>> = callbackFlow {
        var query: Query = carsCollection.whereEqualTo("status", "ACTIVE")

        // Brand filter
        filter.brand?.let {
            query = query.whereEqualTo("brand", it)
        }

        // Model filter
        filter.model?.let {
            query = query.whereEqualTo("model", it)
        }

        // City filter
        filter.city?.let {
            query = query.whereEqualTo("city", it)
        }

        // Fuel type filter
        filter.fuelType?.let {
            query = query.whereEqualTo("fuel_type", it)
        }

        // Transmission filter
        filter.transmission?.let {
            query = query.whereEqualTo("transmission", it)
        }

        // Body type filter
        filter.bodyType?.let {
            query = query.whereEqualTo("body_type", it)
        }

        // isNew filter
        filter.isNew?.let {
            query = query.whereEqualTo("is_new", it)
        }

        // Year filter
        filter.minYear?.let {
            query = query.whereGreaterThanOrEqualTo("year", it)
        }
        filter.maxYear?.let {
            query = query.whereLessThanOrEqualTo("year", it)
        }

        // Sorting
        query = when (filter.sortBy) {
            com.example.turboazapp.domain.model.SortOption.DATE_DESC ->
                query.orderBy("created_at", Query.Direction.DESCENDING)
            com.example.turboazapp.domain.model.SortOption.DATE_ASC ->
                query.orderBy("created_at", Query.Direction.ASCENDING)
            com.example.turboazapp.domain.model.SortOption.PRICE_ASC ->
                query.orderBy("price", Query.Direction.ASCENDING)
            com.example.turboazapp.domain.model.SortOption.PRICE_DESC ->
                query.orderBy("price", Query.Direction.DESCENDING)
            com.example.turboazapp.domain.model.SortOption.MILEAGE_ASC ->
                query.orderBy("mileage", Query.Direction.ASCENDING)
            com.example.turboazapp.domain.model.SortOption.YEAR_DESC ->
                query.orderBy("year", Query.Direction.DESCENDING)
        }

        val listener = query.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }

            var cars = snapshot?.documents?.mapNotNull {
                it.toObject(CarDto::class.java)?.copy(id = it.id)
            } ?: emptyList()

            // Client-side filtering (Firestore-də mümkün olmayan filtrlər)
            filter.minPrice?.let { minPrice ->
                cars = cars.filter { it.price >= minPrice }
            }
            filter.maxPrice?.let { maxPrice ->
                cars = cars.filter { it.price <= maxPrice }
            }
            filter.minMileage?.let { minMileage ->
                cars = cars.filter { it.mileage >= minMileage }
            }
            filter.maxMileage?.let { maxMileage ->
                cars = cars.filter { it.mileage <= maxMileage }
            }
            filter.color?.let { color ->
                cars = cars.filter { it.color == color }
            }

            trySend(cars)
        }

        awaitClose { listener.remove() }
    }

    // Yeni elan əlavə et
    suspend fun addCar(car: CarDto): String {
        val docRef = carsCollection.document()
        val carWithId = car.copy(id = docRef.id)
        docRef.set(carWithId).await()
        return docRef.id
    }

    // Elanı yenilə
    suspend fun updateCar(car: CarDto) {
        carsCollection.document(car.id)
            .set(car.copy(updatedAt = System.currentTimeMillis()))
            .await()
    }

    // Elanı sil
    suspend fun deleteCar(carId: String) {
        carsCollection.document(carId).delete().await()
    }

    // Baxış sayını artır
    suspend fun incrementViewCount(carId: String) {
        firestore.runTransaction { transaction ->
            val carRef = carsCollection.document(carId)
            val snapshot = transaction.get(carRef)
            val currentCount = snapshot.getLong("view_count") ?: 0
            transaction.update(carRef, "view_count", currentCount + 1)
        }.await()
    }
}
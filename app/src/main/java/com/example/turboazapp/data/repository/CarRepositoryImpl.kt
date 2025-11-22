package com.example.turboazapp.data.repository

import android.util.Log
import com.example.turboazapp.data.mapper.toDomain
import com.example.turboazapp.data.mapper.toDto
import com.example.turboazapp.data.remote.FirebaseAuthDataSource
import com.example.turboazapp.data.remote.FirebaseCarDataSource
import com.example.turboazapp.domain.model.Car
import com.example.turboazapp.domain.model.Filter
import com.example.turboazapp.domain.repository.CarRepository
import com.example.turboazapp.util.ImageUploadHelper
import com.example.turboazapp.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CarRepositoryImpl @Inject constructor(
    private val carDataSource: FirebaseCarDataSource,
    private val authDataSource: FirebaseAuthDataSource,
    private val imageUploadHelper: ImageUploadHelper
) : CarRepository {

    companion object {
        private const val TAG = "CarRepositoryImpl"
    }

    // Cache üçün favoritləri yaddaşda saxla
    private val _favoritesCache = mutableSetOf<String>()

    override fun getCars(): Flow<Resource<List<Car>>> = flow {
        emit(Resource.Loading())

        Log.d(TAG, "getCars() called")

        // 1. Cars-ı gətir
        val carsFlow = carDataSource.getCars()

        carsFlow.collect { dtoList ->
            Log.d(TAG, "Received ${dtoList.size} cars from Firebase")

            // 2. Current user-i al
            val currentUserId = authDataSource.getCurrentUser()?.id
            Log.d(TAG, "Current user ID: $currentUserId")

            // 3. Favorites-i gətir
            val favorites = if (currentUserId != null) {
                try {
                    val freshFavorites = authDataSource.getFavorites(currentUserId)
                    Log.d(TAG, "Fetched favorites from Firebase: $freshFavorites")

                    // Cache-i yenilə
                    _favoritesCache.clear()
                    _favoritesCache.addAll(freshFavorites)

                    freshFavorites
                } catch (e: Exception) {
                    Log.e(TAG, "Error fetching favorites", e)
                    emptyList()
                }
            } else {
                Log.d(TAG, "No user logged in, no favorites")
                emptyList()
            }

            // 4. Cars-a favorite status əlavə et
            val cars = dtoList.map { dto ->
                val isFavorite = favorites.contains(dto.id)
                Log.d(TAG, "Car ${dto.id}: isFavorite=$isFavorite")
                dto.toDomain().copy(isFavorite = isFavorite)
            }

            Log.d(TAG, "Emitting ${cars.size} cars with favorites applied")
            emit(Resource.Success(cars))
        }
    }.catch { e ->
        Log.e(TAG, "Error in getCars()", e)
        emit(Resource.Error(e.message ?: "Xəta baş verdi"))
    }

    override fun getCarById(carId: String): Flow<Resource<Car>> = flow {
        emit(Resource.Loading())

        Log.d(TAG, "getCarById($carId) called")

        val carDto = carDataSource.getCarById(carId)
        if (carDto != null) {
            val currentUserId = authDataSource.getCurrentUser()?.id
            Log.d(TAG, "Current user ID: $currentUserId")

            val isFavorite = if (currentUserId != null) {
                // Cache-dən yoxla, boşdursa Firebase-dən gətir
                if (_favoritesCache.isEmpty()) {
                    try {
                        val favorites = authDataSource.getFavorites(currentUserId)
                        Log.d(TAG, "Fetched favorites: $favorites")
                        _favoritesCache.addAll(favorites)
                    } catch (e: Exception) {
                        Log.e(TAG, "Error fetching favorites", e)
                    }
                }
                val result = _favoritesCache.contains(carId)
                Log.d(TAG, "Car $carId isFavorite: $result")
                result
            } else {
                false
            }

            val car = carDto.toDomain().copy(isFavorite = isFavorite)
            emit(Resource.Success(car))
        } else {
            Log.e(TAG, "Car $carId not found")
            emit(Resource.Error("Elan tapılmadı"))
        }
    }.catch { e ->
        Log.e(TAG, "Error in getCarById($carId)", e)
        emit(Resource.Error(e.message ?: "Xəta baş verdi"))
    }

    override fun searchCars(filter: Filter): Flow<Resource<List<Car>>> = flow {
        emit(Resource.Loading())

        carDataSource.searchCars(filter)
            .map { dtoList ->
                val currentUserId = authDataSource.getCurrentUser()?.id
                val favorites = if (currentUserId != null) {
                    if (_favoritesCache.isEmpty()) {
                        val freshFavorites = authDataSource.getFavorites(currentUserId)
                        _favoritesCache.clear()
                        _favoritesCache.addAll(freshFavorites)
                        freshFavorites
                    } else {
                        _favoritesCache.toList()
                    }
                } else {
                    emptyList()
                }

                val cars = dtoList.map { dto ->
                    dto.toDomain().copy(isFavorite = favorites.contains(dto.id))
                }
                cars
            }
            .collect { cars ->
                emit(Resource.Success(cars))
            }
    }.catch { e ->
        emit(Resource.Error(e.message ?: "Xəta baş verdi"))
    }

    override suspend fun addCar(car: Car): Resource<String> {
        return try {
            // 1. Şəkilləri yüklə
            val uploadedImageUrls = if (car.images.isNotEmpty()) {
                imageUploadHelper.uploadImages(car.images)
            } else {
                emptyList()
            }

            // 2. Şəkil URL-ləri ilə car obyektini yenilə
            val carWithImages = car.copy(images = uploadedImageUrls)

            // 3. Firebase Firestore-a əlavə et
            val carId = carDataSource.addCar(carWithImages.toDto())

            Resource.Success(carId)
        } catch (e: Exception) {
            Log.e(TAG, "Error in addCar", e)
            Resource.Error(e.message ?: "Elan əlavə edilə bilmədi")
        }
    }

    override suspend fun updateCar(car: Car): Resource<Unit> {
        return try {
            carDataSource.updateCar(car.toDto())
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Elan yenilənə bilmədi")
        }
    }

    override suspend fun deleteCar(carId: String): Resource<Unit> {
        return try {
            carDataSource.deleteCar(carId)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Elan silinə bilmədi")
        }
    }

    override suspend fun addToFavorites(userId: String, carId: String): Resource<Unit> {
        return try {
            Log.d(TAG, "addToFavorites: userId=$userId, carId=$carId")

            authDataSource.addToFavorites(userId, carId)

            // ✅ Cache-i yenilə
            _favoritesCache.add(carId)
            Log.d(TAG, "Added to cache. Current cache: $_favoritesCache")

            Resource.Success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "Error in addToFavorites", e)
            Resource.Error(e.message ?: "Sevimlilərə əlavə edilə bilmədi")
        }
    }

    override suspend fun removeFromFavorites(userId: String, carId: String): Resource<Unit> {
        return try {
            Log.d(TAG, "removeFromFavorites: userId=$userId, carId=$carId")

            authDataSource.removeFromFavorites(userId, carId)

            // ✅ Cache-dən sil
            _favoritesCache.remove(carId)
            Log.d(TAG, "Removed from cache. Current cache: $_favoritesCache")

            Resource.Success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "Error in removeFromFavorites", e)
            Resource.Error(e.message ?: "Sevimlilərdən silinə bilmədi")
        }
    }

    override suspend fun incrementViewCount(carId: String): Resource<Unit> {
        return try {
            carDataSource.incrementViewCount(carId)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Baxış sayı artırıla bilmədi")
        }
    }

    override fun getCarsByUserId(userId: String): Flow<Resource<List<Car>>> = flow {
        emit(Resource.Loading())
        try {
            val favorites = authDataSource.getFavorites(userId)

            carDataSource.getCarsByUserId(userId).collect { carDtos ->
                val cars = carDtos.map { carDto ->
                    carDto.toDomain().copy(
                        isFavorite = favorites.contains(carDto.id)
                    )
                }
                emit(Resource.Success(cars))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Xəta"))
        }
    }
}
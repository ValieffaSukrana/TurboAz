package com.example.turboazapp.data.repository

import com.example.turboazapp.data.mapper.toDomain
import com.example.turboazapp.data.mapper.toDto
import com.example.turboazapp.data.remote.FirebaseAuthDataSource
import com.example.turboazapp.data.remote.FirebaseCarDataSource
import com.example.turboazapp.domain.model.Car
import com.example.turboazapp.domain.model.Filter
import com.example.turboazapp.domain.repository.CarRepository
import com.example.turboazapp.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CarRepositoryImpl @Inject constructor(
    private val carDataSource: FirebaseCarDataSource,
    private val authDataSource: FirebaseAuthDataSource
) : CarRepository {

    override fun getCars(): Flow<Resource<List<Car>>> = flow {
        emit(Resource.Loading())

        carDataSource.getCars()
            .map { dtoList ->
                val currentUserId = authDataSource.getCurrentUser()?.id
                val favorites = if (currentUserId != null) {
                    authDataSource.getFavorites(currentUserId)
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

    override fun getCarById(carId: String): Flow<Resource<Car>> = flow {
        emit(Resource.Loading())

        val carDto = carDataSource.getCarById(carId)
        if (carDto != null) {
            val currentUserId = authDataSource.getCurrentUser()?.id
            val favorites = if (currentUserId != null) {
                authDataSource.getFavorites(currentUserId)
            } else {
                emptyList()
            }

            val car = carDto.toDomain().copy(isFavorite = favorites.contains(carId))
            emit(Resource.Success(car))
        } else {
            emit(Resource.Error("Elan tapılmadı"))
        }
    }.catch { e ->
        emit(Resource.Error(e.message ?: "Xəta baş verdi"))
    }

    override fun searchCars(filter: Filter): Flow<Resource<List<Car>>> = flow {
        emit(Resource.Loading())

        carDataSource.searchCars(filter)
            .map { dtoList ->
                val currentUserId = authDataSource.getCurrentUser()?.id
                val favorites = if (currentUserId != null) {
                    authDataSource.getFavorites(currentUserId)
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
            val carId = carDataSource.addCar(car.toDto())
            Resource.Success(carId)
        } catch (e: Exception) {
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
            authDataSource.addToFavorites(userId, carId)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Sevimlilərə əlavə edilə bilmədi")
        }
    }

    override suspend fun removeFromFavorites(userId: String, carId: String): Resource<Unit> {
        return try {
            authDataSource.removeFromFavorites(userId, carId)
            Resource.Success(Unit)
        } catch (e: Exception) {
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
}
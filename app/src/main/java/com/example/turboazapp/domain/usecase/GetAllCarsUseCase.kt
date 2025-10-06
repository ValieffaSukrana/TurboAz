package com.example.turboazapp.domain.usecase

import com.example.turboazapp.domain.model.Car
import com.example.turboazapp.domain.repository.CarsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllCarsUseCase @Inject constructor(
    private val repository: CarsRepository
) {
    operator fun invoke(): Flow<List<Car>> {
        return repository.getAllCars()
    }
}
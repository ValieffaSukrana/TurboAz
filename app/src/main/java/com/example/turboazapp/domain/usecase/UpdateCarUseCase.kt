package com.example.turboazapp.domain.usecase

import com.example.turboazapp.domain.model.Car
import com.example.turboazapp.domain.repository.CarRepository
import com.example.turboazapp.util.Resource
import javax.inject.Inject

class UpdateCarUseCase @Inject constructor(
    private val repository: CarRepository
) {
    suspend operator fun invoke(car: Car): Resource<Unit> {
        return repository.updateCar(car)
    }
}
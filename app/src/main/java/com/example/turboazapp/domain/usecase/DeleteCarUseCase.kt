package com.example.turboazapp.domain.usecase

import com.example.turboazapp.domain.repository.CarRepository
import com.example.turboazapp.util.Resource
import javax.inject.Inject

class DeleteCarUseCase @Inject constructor(
    private val repository: CarRepository
) {
    suspend operator fun invoke(carId: String): Resource<Unit> {
        return repository.deleteCar(carId)
    }
}
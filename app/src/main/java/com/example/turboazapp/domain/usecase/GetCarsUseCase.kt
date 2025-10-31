package com.example.turboazapp.domain.usecase

import com.example.turboazapp.domain.model.Car
import com.example.turboazapp.domain.repository.CarRepository
import com.example.turboazapp.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCarsUseCase @Inject constructor(
private val repository: CarRepository
) {
    operator fun invoke(): Flow<Resource<List<Car>>> {
        return repository.getCars()
    }
}
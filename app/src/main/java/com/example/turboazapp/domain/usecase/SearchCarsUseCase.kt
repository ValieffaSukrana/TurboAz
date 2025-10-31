package com.example.turboazapp.domain.usecase

import com.example.turboazapp.domain.model.Car
import com.example.turboazapp.domain.model.Filter
import com.example.turboazapp.domain.repository.CarRepository
import com.example.turboazapp.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchCarsUseCase @Inject constructor(
    private val repository: CarRepository
) {
    operator fun invoke(filter: Filter): Flow<Resource<List<Car>>> {
        return repository.searchCars(filter)
    }
}
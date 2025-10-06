package com.example.turboazapp.domain.usecase

import com.example.turboazapp.domain.model.Car
import com.example.turboazapp.domain.repository.CarsRepository
import javax.inject.Inject

class ToggleFavoriteUseCase @Inject constructor(
    private val repository: CarsRepository
) {
    suspend operator fun invoke(car: Car): Result<Unit> {
        return if (repository.isFavorite(car.id)) {
            repository.removeFromFavorites(car.id)
        } else {
            repository.addToFavorites(car)
        }
    }
}
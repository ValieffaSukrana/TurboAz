package com.example.turboazapp.domain.usecase

import com.example.turboazapp.domain.model.FavoriteCar
import com.example.turboazapp.domain.repository.CarsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavoritesUseCase @Inject constructor(
    private val repository: CarsRepository
) {
    operator fun invoke(): Flow<List<FavoriteCar>> {
        return repository.getFavorites()
    }
}
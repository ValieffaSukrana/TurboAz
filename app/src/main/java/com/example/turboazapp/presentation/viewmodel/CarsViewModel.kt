package com.example.turboazapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.turboazapp.domain.model.Car
import com.example.turboazapp.domain.usecase.GetAllCarsUseCase
import com.example.turboazapp.domain.usecase.ToggleFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CarsViewModel @Inject constructor(
    private val getAllCarsUseCase: GetAllCarsUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : ViewModel() {

    private val _cars = MutableStateFlow<List<Car>>(emptyList())
    val cars: StateFlow<List<Car>> = _cars.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        loadCars()
    }

    private fun loadCars() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                getAllCarsUseCase().collect { carsList ->
                    _cars.value = carsList
                    _isLoading.value = false
                }
            } catch (e: Exception) {
                _error.value = "Xəta: ${e.message}"
                _isLoading.value = false
            }
        }
    }

    fun toggleFavorite(car: Car) {
        viewModelScope.launch {
            try {
                val result = toggleFavoriteUseCase(car)
                if (result.isFailure) {
                    _error.value = result.exceptionOrNull()?.message
                }
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun clearError() {
        _error.value = null
    }

    fun seedDummyCars(onDone: () -> Unit, onError: (Exception) -> Unit) {
        // Mövcud seed funksiyası - saxlayın
    }
}
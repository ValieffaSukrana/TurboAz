package com.example.turboazapp.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.turboazapp.domain.model.Car
import com.example.turboazapp.domain.usecase.GetCarDetailsUseCase
import com.example.turboazapp.domain.usecase.GetCurrentUserUseCase
import com.example.turboazapp.domain.usecase.IncrementViewCountUseCase
import com.example.turboazapp.domain.usecase.ToggleFavoriteUseCase
import com.example.turboazapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CarDetailsViewModel @Inject constructor(
    private val getCarDetailsUseCase: GetCarDetailsUseCase,
    private val incrementViewCountUseCase: IncrementViewCountUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val carId: String = savedStateHandle.get<String>("carId") ?: ""

    private val _carState = MutableStateFlow<Resource<Car>>(Resource.Loading())
    val carState: StateFlow<Resource<Car>> = _carState.asStateFlow()

    private val _favoriteState = MutableStateFlow<Resource<Unit>?>(null)
    val favoriteState: StateFlow<Resource<Unit>?> = _favoriteState.asStateFlow()

    init {
        loadCarDetails()
        incrementViewCount()
    }

    private fun loadCarDetails() {
        viewModelScope.launch {
            getCarDetailsUseCase(carId).collect { resource ->
                _carState.value = resource
            }
        }
    }

    private fun incrementViewCount() {
        viewModelScope.launch {
            incrementViewCountUseCase(carId)
        }
    }

    fun toggleFavorite(isFavorite: Boolean) {
        viewModelScope.launch {
            val userResult = getCurrentUserUseCase()

            when (userResult) {
                is Resource.Success -> {
                    val userId = userResult.data?.id
                    if (userId != null) {
                        _favoriteState.value = Resource.Loading()
                        val result = toggleFavoriteUseCase(userId, carId, isFavorite)
                        _favoriteState.value = result
                    } else {
                        _favoriteState.value = Resource.Error("Giriş edin")
                    }
                }
                is Resource.Error -> {
                    _favoriteState.value = Resource.Error("Giriş edin")
                }
                else -> {}
            }
        }
    }
}
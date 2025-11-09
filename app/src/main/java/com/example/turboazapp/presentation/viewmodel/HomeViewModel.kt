package com.example.turboazapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.turboazapp.domain.model.Car
import com.example.turboazapp.domain.usecase.GetCarsUseCase
import com.example.turboazapp.domain.usecase.GetCurrentUserUseCase
import com.example.turboazapp.domain.usecase.ToggleFavoriteUseCase
import com.example.turboazapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCarsUseCase: GetCarsUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {

    private val _carsState = MutableStateFlow<Resource<List<Car>>>(Resource.Loading())
    val carsState: StateFlow<Resource<List<Car>>> = _carsState.asStateFlow()

    private val _favoriteState = MutableStateFlow<Resource<Unit>?>(null)
    val favoriteState: StateFlow<Resource<Unit>?> = _favoriteState.asStateFlow()

    init {
        loadCars()
    }

    fun loadCars() {
        viewModelScope.launch {
            getCarsUseCase().collect { resource ->
                _carsState.value = resource
            }
        }
    }

    fun toggleFavorite(car: Car, isFavorite: Boolean) {
        viewModelScope.launch {
            // Cari istifadəçini al
            val userResult = getCurrentUserUseCase()

            when (userResult) {
                is Resource.Success -> {
                    val userId = userResult.data?.id
                    if (userId != null) {
                        _favoriteState.value = Resource.Loading()
                        val result = toggleFavoriteUseCase(userId, car.id, isFavorite)
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

    fun refresh() {
        loadCars()
    }
}
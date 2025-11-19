package com.example.turboazapp.presentation.viewmodel

import android.util.Log
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

    companion object {
        private const val TAG = "HomeViewModel"
    }

    private val _carsState = MutableStateFlow<Resource<List<Car>>>(Resource.Loading())
    val carsState: StateFlow<Resource<List<Car>>> = _carsState.asStateFlow()

    private val _favoriteState = MutableStateFlow<Resource<Unit>?>(null)
    val favoriteState: StateFlow<Resource<Unit>?> = _favoriteState.asStateFlow()

    init {
        Log.d(TAG, "HomeViewModel initialized")
        loadCars()
    }

    fun loadCars() {
        Log.d(TAG, "loadCars() called")
        viewModelScope.launch {
            getCarsUseCase().collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        Log.d(TAG, "Cars loading...")
                    }
                    is Resource.Success -> {
                        Log.d(TAG, "Cars loaded: ${resource.data?.size} cars")
                        resource.data?.forEach { car ->
                            Log.d(TAG, "Car: ${car.id}, isFavorite=${car.isFavorite}")
                        }
                    }
                    is Resource.Error -> {
                        Log.e(TAG, "Cars error: ${resource.message}")
                    }
                }
                _carsState.value = resource
            }
        }
    }

    fun toggleFavorite(car: Car, isFavorite: Boolean) {
        Log.d(TAG, "toggleFavorite called: carId=${car.id}, currentState=$isFavorite")

        viewModelScope.launch {
            val userResult = getCurrentUserUseCase()
            Log.d(TAG, "Current user result: $userResult")

            when (userResult) {
                is Resource.Success -> {
                    val userId = userResult.data?.id
                    Log.d(TAG, "UserId: $userId")

                    if (userId != null) {
                        _favoriteState.value = Resource.Loading()

                        val result = toggleFavoriteUseCase(userId, car.id, isFavorite)
                        Log.d(TAG, "Toggle favorite result: $result")

                        _favoriteState.value = result

                        if (result is Resource.Success) {
                            Log.d(TAG, "Favorite toggled successfully, reloading cars...")
                            loadCars()
                        } else if (result is Resource.Error) {
                            Log.e(TAG, "Toggle favorite error: ${result.message}")
                        }
                    } else {
                        Log.e(TAG, "UserId is null")
                        _favoriteState.value = Resource.Error("Giriş edin")
                    }
                }
                is Resource.Error -> {
                    Log.e(TAG, "User error: ${userResult.message}")
                    _favoriteState.value = Resource.Error("Giriş edin")
                }
                else -> {
                    Log.d(TAG, "User result is Loading")
                }
            }
        }
    }

    fun refresh() {
        Log.d(TAG, "refresh() called")
        loadCars()
    }
}
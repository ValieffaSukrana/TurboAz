package com.example.turboazapp.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.turboazapp.domain.model.Car
import com.example.turboazapp.domain.usecase.GetFavoriteCarsUseCase
import com.example.turboazapp.domain.usecase.ToggleFavoriteUseCase
import com.example.turboazapp.util.Resource
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectedViewModel @Inject constructor(
    private val getFavoriteCarsUseCase: GetFavoriteCarsUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val auth: FirebaseAuth
) : ViewModel() {

    companion object {
        private const val TAG = "SelectedViewModel"
    }

    private val _selectedState = MutableStateFlow<SelectedState>(SelectedState.Loading)
    val selectedState: StateFlow<SelectedState> = _selectedState.asStateFlow()

    init {
        Log.d(TAG, "SelectedViewModel initialized")
        Log.d(TAG, "Current user: ${auth.currentUser?.uid}")
        loadFavoriteCars()
    }

    fun loadFavoriteCars() {
        Log.d(TAG, "loadFavoriteCars() called")
        Log.d(TAG, "Firebase Auth User: ${auth.currentUser?.uid}")

        viewModelScope.launch {
            getFavoriteCarsUseCase().collect { result ->
                Log.d(TAG, "GetFavoriteCarsUseCase result: $result")

                when (result) {
                    is Resource.Loading -> {
                        Log.d(TAG, "Favorite cars loading...")
                        _selectedState.value = SelectedState.Loading
                    }
                    is Resource.Success -> {
                        val cars = result.data ?: emptyList()
                        Log.d(TAG, "Favorite cars loaded: ${cars.size} cars")
                        cars.forEach { car ->
                            Log.d(TAG, "Favorite car: ${car.id} - ${car.brand} ${car.model}")
                        }

                        _selectedState.value = if (cars.isEmpty()) {
                            Log.d(TAG, "No favorite cars, setting Empty state")
                            SelectedState.Empty
                        } else {
                            Log.d(TAG, "Setting Success state with ${cars.size} cars")
                            SelectedState.Success(cars)
                        }
                    }
                    is Resource.Error -> {
                        Log.e(TAG, "Favorite cars error: ${result.message}")
                        _selectedState.value = SelectedState.Error(
                            result.message ?: "Xəta baş verdi"
                        )
                    }
                }
            }
        }
    }

    fun toggleFavorite(car: Car, isFavorite: Boolean) {
        val userId = auth.currentUser?.uid
        Log.d(TAG, "toggleFavorite: carId=${car.id}, isFavorite=$isFavorite, userId=$userId")

        if (userId == null) {
            Log.e(TAG, "Cannot toggle favorite: user not logged in")
            return
        }

        viewModelScope.launch {
            val result = toggleFavoriteUseCase(userId, car.id, isFavorite)
            Log.d(TAG, "Toggle favorite result: $result")

            when (result) {
                is Resource.Success -> {
                    Log.d(TAG, "Favorite toggled successfully, reloading...")
                    loadFavoriteCars()
                }
                is Resource.Error -> {
                    Log.e(TAG, "Toggle error: ${result.message}")
                    _selectedState.value = SelectedState.Error(
                        result.message ?: "Əməliyyat uğursuz oldu"
                    )
                    loadFavoriteCars()
                }
                is Resource.Loading -> {
                    Log.d(TAG, "Toggle is loading...")
                }
            }
        }
    }

    sealed class SelectedState {
        object Loading : SelectedState()
        object Empty : SelectedState()
        data class Success(val cars: List<Car>) : SelectedState()
        data class Error(val message: String) : SelectedState()
    }
}
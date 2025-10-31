package com.example.turboazapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.turboazapp.domain.model.Car
import com.example.turboazapp.domain.usecase.AddCarUseCase
import com.example.turboazapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddCarViewModel @Inject constructor(
    private val addCarUseCase: AddCarUseCase
) : ViewModel() {

    private val _addCarState = MutableStateFlow<Resource<String>?>(null)
    val addCarState: StateFlow<Resource<String>?> = _addCarState.asStateFlow()

    fun addCar(car: Car) {
        viewModelScope.launch {
            _addCarState.value = Resource.Loading()
            val result = addCarUseCase(car)
            _addCarState.value = result
        }
    }

    fun resetState() {
        _addCarState.value = null
    }
}
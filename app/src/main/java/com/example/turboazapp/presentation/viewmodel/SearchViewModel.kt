package com.example.turboazapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.turboazapp.domain.model.Car
import com.example.turboazapp.domain.model.Filter
import com.example.turboazapp.domain.usecase.SearchCarsUseCase
import com.example.turboazapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchCarsUseCase: SearchCarsUseCase
) : ViewModel() {

    private val _searchState = MutableStateFlow<Resource<List<Car>>>(Resource.Success(emptyList()))
    val searchState: StateFlow<Resource<List<Car>>> = _searchState.asStateFlow()

    private val _currentFilter = MutableStateFlow(Filter())
    val currentFilter: StateFlow<Filter> = _currentFilter.asStateFlow()

    fun searchCars(filter: Filter) {
        viewModelScope.launch {
            _currentFilter.value = filter
            _searchState.value = Resource.Loading()
            searchCarsUseCase(filter).collect { resource ->
                _searchState.value = resource
            }
        }
    }

    fun clearFilter() {
        _currentFilter.value = Filter()
        searchCars(Filter())
    }
}
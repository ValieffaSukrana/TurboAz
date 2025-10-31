package com.example.turboazapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.turboazapp.domain.model.User
import com.example.turboazapp.domain.usecase.GetCurrentUserUseCase
import com.example.turboazapp.domain.usecase.LogoutUseCase
import com.example.turboazapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    private val _userState = MutableStateFlow<Resource<User?>>(Resource.Loading())
    val userState: StateFlow<Resource<User?>> = _userState.asStateFlow()

    private val _logoutState = MutableStateFlow<Resource<Unit>?>(null)
    val logoutState: StateFlow<Resource<Unit>?> = _logoutState.asStateFlow()

    init {
        loadCurrentUser()
    }

    fun loadCurrentUser() {
        viewModelScope.launch {
            _userState.value = Resource.Loading()
            val result = getCurrentUserUseCase()
            _userState.value = result
        }
    }

    fun logout() {
        viewModelScope.launch {
            _logoutState.value = Resource.Loading()
            val result = logoutUseCase()
            _logoutState.value = result
        }
    }

    fun resetLogoutState() {
        _logoutState.value = null
    }
}
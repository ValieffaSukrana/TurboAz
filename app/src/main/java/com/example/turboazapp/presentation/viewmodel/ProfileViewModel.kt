package com.example.turboazapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.turboazapp.domain.model.User
import com.example.turboazapp.domain.usecase.GetCurrentUserUseCase
import com.example.turboazapp.domain.usecase.LogoutUseCase
import com.example.turboazapp.util.Resource
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
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
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        if (firebaseUser == null) {
            _userState.value = Resource.Success(null)
            return
        }

        viewModelScope.launch {
            _userState.value = Resource.Loading()

            try {
                firebaseUser.reload().await() // 🔹 əlavə et: Firebase ilə sinxronlaşdır
                if (FirebaseAuth.getInstance().currentUser == null) {
                    _userState.value = Resource.Success(null)
                    return@launch
                }

                val result = getCurrentUserUseCase()
                _userState.value = result
            } catch (e: Exception) {
                _userState.value = Resource.Error("İstifadəçi məlumatı yenilənmədi: ${e.message}")
            }
        }
    }

    fun logout() {
        android.util.Log.d("ProfileViewModel", "===== LOGOUT BAŞLADI =====")
        viewModelScope.launch {
            _logoutState.value = Resource.Loading()
            android.util.Log.d("ProfileViewModel", "Logout Loading state set")

            val result = logoutUseCase()

            android.util.Log.d("ProfileViewModel", "Logout result: $result")
            _logoutState.value = result
            android.util.Log.d("ProfileViewModel", "===== LOGOUT TAMAMLANDI =====")
        }
    }

    fun resetLogoutState() {
        _logoutState.value = null
    }
}
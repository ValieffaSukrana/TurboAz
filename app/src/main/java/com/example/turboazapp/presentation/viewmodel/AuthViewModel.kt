package com.example.turboazapp.presentation.viewmodel

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.turboazapp.domain.model.User
import com.example.turboazapp.domain.usecase.VerifyCodeAndLoginUseCase
import com.example.turboazapp.util.Resource
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject


@HiltViewModel
class AuthViewModel @Inject constructor(
    private val verifyCodeAndLoginUseCase: VerifyCodeAndLoginUseCase,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _verificationState = MutableStateFlow<VerificationState>(VerificationState.Idle)
    val verificationState: StateFlow<VerificationState> = _verificationState.asStateFlow()

    private val _authState = MutableStateFlow<Resource<User>?>(null)
    val authState: StateFlow<Resource<User>?> = _authState.asStateFlow()

    private var verificationId: String? = null

    fun sendVerificationCode(phoneNumber: String, activity: Activity) {
        _verificationState.value = VerificationState.Loading

        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                _verificationState.value = VerificationState.AutoVerified(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                _verificationState.value = VerificationState.Error(
                    e.message ?: "SMS göndərilə bilmədi"
                )
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                this@AuthViewModel.verificationId = verificationId
                _verificationState.value = VerificationState.CodeSent(verificationId)
            }
        }

        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(callbacks)
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    fun verifyCode(code: String, name: String? = null) {
        val verifyId = verificationId
        if (verifyId == null) {
            _authState.value = Resource.Error("Verification ID tapılmadı")
            return
        }

        viewModelScope.launch {
            _authState.value = Resource.Loading()
            val result = verifyCodeAndLoginUseCase(verifyId, code, name)
            _authState.value = result
        }
    }

    fun resetState() {
        _verificationState.value = VerificationState.Idle
        _authState.value = null
        verificationId = null
    }
}

sealed class VerificationState {
    object Idle : VerificationState()
    object Loading : VerificationState()
    data class CodeSent(val verificationId: String) : VerificationState()
    data class AutoVerified(val credential: PhoneAuthCredential) : VerificationState()
    data class Error(val message: String) : VerificationState()
}
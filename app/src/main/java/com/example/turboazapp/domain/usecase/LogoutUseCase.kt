package com.example.turboazapp.domain.usecase

import com.example.turboazapp.domain.repository.AuthRepository
import com.example.turboazapp.util.Resource
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Resource<Unit> {
        return try {
            authRepository.logout()
            Resource.Success(Unit)
        } catch (e: Exception) {
            android.util.Log.e("LogoutUseCase", "Logout error", e)
            Resource.Error(e.message ?: "Çıxış zamanı xəta baş verdi")
        }
    }
}
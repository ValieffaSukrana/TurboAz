package com.example.turboazapp.domain.usecase

import com.example.turboazapp.domain.model.User
import com.example.turboazapp.domain.repository.AuthRepository
import com.example.turboazapp.util.Resource
import javax.inject.Inject

class VerifyCodeAndLoginUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(
        verificationId: String,
        code: String,
        name: String? = null
    ): Resource<User> {
        if (code.length != 6) {
            return Resource.Error("Kod 6 rəqəmli olmalıdır")
        }

        return repository.verifyCodeAndLogin(verificationId, code, name)
    }
}
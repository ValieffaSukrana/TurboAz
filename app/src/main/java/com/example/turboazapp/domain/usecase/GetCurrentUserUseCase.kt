package com.example.turboazapp.domain.usecase

import com.example.turboazapp.domain.model.User
import com.example.turboazapp.domain.repository.AuthRepository
import com.example.turboazapp.util.Resource
import javax.inject.Inject


class GetCurrentUserUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(): Resource<User?> {
        return repository.getCurrentUser()
    }
}
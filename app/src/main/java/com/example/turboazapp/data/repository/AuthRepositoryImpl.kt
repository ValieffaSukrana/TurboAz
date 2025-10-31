package com.example.turboazapp.data.repository

import com.example.turboazapp.data.mapper.toDomain
import com.example.turboazapp.data.mapper.toDto
import com.example.turboazapp.data.remote.FirebaseAuthDataSource
import com.example.turboazapp.domain.model.User
import com.example.turboazapp.domain.repository.AuthRepository
import com.example.turboazapp.util.Resource
import javax.inject.Inject


class AuthRepositoryImpl @Inject constructor(
    private val authDataSource: FirebaseAuthDataSource
) : AuthRepository {

    override suspend fun verifyCodeAndLogin(
        verificationId: String,
        code: String,
        name: String?
    ): Resource<User> {
        return try {
            val userDto = authDataSource.verifyCodeAndLogin(verificationId, code, name)
            Resource.Success(userDto.toDomain())
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Kod yanlışdır")
        }
    }

    override suspend fun logout(): Resource<Unit> {
        return try {
            authDataSource.logout()
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Çıxış uğursuz oldu")
        }
    }

    override suspend fun getCurrentUser(): Resource<User?> {
        return try {
            val userDto = authDataSource.getCurrentUser()
            Resource.Success(userDto?.toDomain())
        } catch (e: Exception) {
            Resource.Error(e.message ?: "İstifadəçi məlumatları alına bilmədi")
        }
    }

    override suspend fun updateProfile(user: User): Resource<Unit> {
        return try {
            authDataSource.updateProfile(user.toDto())
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Profil yenilənə bilmədi")
        }
    }

    override suspend fun isUserRegistered(phoneNumber: String): Resource<Boolean> {
        return try {
            val isRegistered = authDataSource.isPhoneRegistered(phoneNumber)
            Resource.Success(isRegistered)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Yoxlanıla bilmədi")
        }
    }
}
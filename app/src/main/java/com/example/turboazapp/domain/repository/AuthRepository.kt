package com.example.turboazapp.domain.repository

import com.example.turboazapp.domain.model.User
import com.example.turboazapp.util.Resource

interface AuthRepository {

    // SMS kodu təsdiqlə və giriş et
    suspend fun verifyCodeAndLogin(
        verificationId: String,
        code: String,
        name: String? = null
    ): Resource<User>

    // Çıxış
    suspend fun logout(): Resource<Unit>

    // Cari istifadəçi
    suspend fun getCurrentUser(): Resource<User?>

    // Profil yenilə
    suspend fun updateProfile(user: User): Resource<Unit>

    // İstifadəçi qeydiyyatdan keçibmi?
    suspend fun isUserRegistered(phoneNumber: String): Resource<Boolean>
}
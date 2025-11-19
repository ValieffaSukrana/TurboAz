package com.example.turboazapp.data.repository

import android.content.ContentValues.TAG
import android.util.Log
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
            android.util.Log.d("AuthRepositoryImpl", "Logout başladı")
            authDataSource.logout()
            android.util.Log.d("AuthRepositoryImpl", "Logout tamamlandı")
            Resource.Success(Unit)
        } catch (e: Exception) {
            android.util.Log.e("AuthRepositoryImpl", "Logout xətası", e)
            Resource.Error(e.message ?: "Çıxış uğursuz oldu")
        }
    }

    override suspend fun getCurrentUser(): Resource<User?> {
        return try {
            Log.d(TAG, "Getting current user...")
            val userDto = authDataSource.getCurrentUser()

            if (userDto == null) {
                Log.d(TAG, "No user logged in")
                Resource.Success(null)
            } else {
                Log.d(TAG, "User found: ${userDto.id}")
                Log.d(TAG, "User favorites: ${userDto.favorites}") // ✅ BUNU YOXLA!

                // ✅ Favorites-i də götür
                val favoritesFromFirestore = authDataSource.getFavorites(userDto.id)
                Log.d(TAG, "Favorites from Firestore: $favoritesFromFirestore")

                // UserDto-ya favorites əlavə et
                val updatedDto = userDto.copy(favorites = favoritesFromFirestore)

                Resource.Success(updatedDto.toDomain())
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error getting user", e)
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

    override suspend fun getFavorites(userId: String): Resource<List<String>> {
        return try {
            val favorites = authDataSource.getFavorites(userId)
            Resource.Success(favorites)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Sevimlilərə əlavə edilə bilmədi")
        }
    }
}
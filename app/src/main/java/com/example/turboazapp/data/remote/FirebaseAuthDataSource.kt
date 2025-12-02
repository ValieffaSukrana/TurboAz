package com.example.turboazapp.data.remote

import com.example.turboazapp.util.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
class FirebaseAuthDataSource @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {
    private val usersCollection = firestore.collection(Constants.USERS_COLLECTION)

    // SMS kodu təsdiqlə və login/register et
    suspend fun verifyCodeAndLogin(
        verificationId: String,
        code: String,
        name: String?
    ): UserDto {
        val credential = PhoneAuthProvider.getCredential(verificationId, code)
        val authResult = auth.signInWithCredential(credential).await()
        val userId = authResult.user?.uid ?: throw Exception("User ID tapılmadı")
        val phoneNumber = authResult.user?.phoneNumber ?: throw Exception("Telefon nömrəsi tapılmadı")

        // İstifadəçi artıq mövcuddurmu?
        val existingUser = usersCollection.document(userId).get().await()

        return if (existingUser.exists()) {
            // Mövcud istifadəçi
            existingUser.toObject(UserDto::class.java)?.copy(id = userId)
                ?: throw Exception("İstifadəçi məlumatları tapılmadı")
        } else {
            // Yeni istifadəçi - qeydiyyat
            val newUser = UserDto(
                id = userId,
                name = name ?: "",
                phone = phoneNumber,
                createdAt = System.currentTimeMillis(),
                isVerified = true
            )
            usersCollection.document(userId).set(newUser).await()
            newUser
        }
    }

    // Çıxış
    fun logout() {
        auth.signOut()
    }

    // Cari istifadəçini gətir
    suspend fun getCurrentUser(): UserDto? {
        val userId = auth.currentUser?.uid ?: return null
        val userDoc = usersCollection.document(userId).get().await()
        return userDoc.toObject(UserDto::class.java)?.copy(id = userId)
    }

    // Profili yenilə
    suspend fun updateProfile(user: UserDto) {
        usersCollection.document(user.id).set(user).await()
    }

    // Telefon nömrəsi qeydiyyatdadırmı?
    suspend fun isPhoneRegistered(phoneNumber: String): Boolean {
        val query = usersCollection
            .whereEqualTo("phone", phoneNumber)
            .limit(1)
            .get()
            .await()
        return !query.isEmpty
    }

    // Sevimlilərə əlavə et
    suspend fun addToFavorites(userId: String, carId: String) {
        val userRef = usersCollection.document(userId)
        firestore.runTransaction { transaction ->
            val snapshot = transaction.get(userRef)
            val currentFavorites = snapshot.get("favorites") as? List<*> ?: emptyList<String>()
            val updatedFavorites = currentFavorites.toMutableList().apply {
                if (!contains(carId)) add(carId)
            }
            transaction.update(userRef, "favorites", updatedFavorites)
        }.await()
    }

    // Sevimlilərdən çıxart
    suspend fun removeFromFavorites(userId: String, carId: String) {
        val userRef = usersCollection.document(userId)
        firestore.runTransaction { transaction ->
            val snapshot = transaction.get(userRef)
            val currentFavorites = snapshot.get("favorites") as? List<*> ?: emptyList<String>()
            val updatedFavorites = currentFavorites.toMutableList().apply {
                remove(carId)
            }
            transaction.update(userRef, "favorites", updatedFavorites)
        }.await()
    }

    // İstifadəçinin sevimli elanlarını gətir
    suspend fun getFavorites(userId: String): List<String> {
        val userDoc = usersCollection.document(userId).get().await()
        return userDoc.get("favorites") as? List<String> ?: emptyList()
    }
}
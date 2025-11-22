package com.example.turboazapp.data.di

import com.example.turboazapp.data.remote.FirebaseAuthDataSource
import com.example.turboazapp.data.remote.FirebaseCarDataSource
import com.example.turboazapp.data.repository.CarRepositoryImpl
import com.example.turboazapp.domain.repository.CarRepository
import com.example.turboazapp.util.ImageUploadHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseStorage(): FirebaseStorage {
        return FirebaseStorage.getInstance()
    }


    @Provides
    @Singleton
    fun provideImageUploadHelper(
        storage: FirebaseStorage
    ): ImageUploadHelper {
        return ImageUploadHelper(storage)
    }

    @Provides
    @Singleton
    fun provideCarRepository(
        carDataSource: FirebaseCarDataSource,
        authDataSource: FirebaseAuthDataSource,
        imageUploadHelper: ImageUploadHelper // ← Yeni əlavə et
    ): CarRepository {
        return CarRepositoryImpl(carDataSource, authDataSource, imageUploadHelper)
    }
}
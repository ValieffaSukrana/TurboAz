package com.example.turboazapp.data.di

import com.example.turboazapp.data.remote.FirebaseAuthDataSource
import com.example.turboazapp.data.remote.FirebaseCarDataSource
import com.example.turboazapp.data.repository.AuthRepositoryImpl
import com.example.turboazapp.data.repository.CarRepositoryImpl
import com.example.turboazapp.domain.repository.AuthRepository
import com.example.turboazapp.domain.repository.CarRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideFirebaseCarDataSource(
        firestore: FirebaseFirestore
    ): FirebaseCarDataSource {
        return FirebaseCarDataSource(firestore)
    }

    @Provides
    @Singleton
    fun provideFirebaseAuthDataSource(
        auth: FirebaseAuth,
        firestore: FirebaseFirestore
    ): FirebaseAuthDataSource {
        return FirebaseAuthDataSource(auth, firestore)
    }

    @Provides
    @Singleton
    fun provideCarRepository(
        carDataSource: FirebaseCarDataSource,
        authDataSource: FirebaseAuthDataSource
    ): CarRepository {
        return CarRepositoryImpl(carDataSource, authDataSource)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(
        authDataSource: FirebaseAuthDataSource
    ): AuthRepository {
        return AuthRepositoryImpl(authDataSource)
    }
}
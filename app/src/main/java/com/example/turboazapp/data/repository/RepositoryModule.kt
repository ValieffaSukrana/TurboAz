package com.example.turboazapp.data.repository

import com.example.turboazapp.domain.repository.CarsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindCarsRepository(
        impl: CarsRepositoryImpl
    ): CarsRepository
}

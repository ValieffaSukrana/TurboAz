package com.example.turboazapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.turboazapp.domain.model.BrandEntity

@Dao
interface BrandDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBrand(brand: BrandEntity)

    @Query("SELECT * FROM brands")
    suspend fun getAllBrands(): List<BrandEntity>

}
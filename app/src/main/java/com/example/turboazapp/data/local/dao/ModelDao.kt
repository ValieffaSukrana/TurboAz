package com.example.turboazapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.turboazapp.domain.model.ModelEntity

@Dao
interface ModelDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertModel(model: ModelEntity)

    @Query("SELECT * FROM models WHERE brandId = :brandId")
    suspend fun getAllModelsByBrandId(brandId: Int): List<ModelEntity>

}
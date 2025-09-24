package com.example.turboazapp.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "models")
data class ModelEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var modelName: String,
    val brandId: Int
)

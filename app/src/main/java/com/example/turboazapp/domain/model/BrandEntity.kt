package com.example.turboazapp.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "brands")
data class BrandEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var brandName: String,
    var brandLogo: String
)

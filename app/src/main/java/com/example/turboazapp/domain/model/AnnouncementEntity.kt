package com.example.turboazapp.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "announcements")
data class AnnouncementEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val currency: String,
    val city: String,
    val year: Int,
    val mileage: Int,
    val engineCapacity: String,
    val transmission: String,
    val fuelType: String,
    val driveTrain: String,
    val color: String,
    val seats: Int,
    var isNew: Boolean=true,
    val imageUrl: String,
    val createDate: LocalDate,
    var viewCount: Int=0,
    val userId: Int,
    val modelId: Int,
    val description: String,
    val price: Double,
    val brandId: Int,
)

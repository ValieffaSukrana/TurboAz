package com.example.turboazapp.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var name: String,
    val phoneNumber: String,
    val createDateTime: LocalDate
)

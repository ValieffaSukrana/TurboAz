package com.example.turboazapp.data.mapper

import com.example.turboazapp.R

object MakeImageMapper {
    private val makeImages = mapOf(
        "BMW" to R.drawable.e39,
        "Porsche" to R.drawable.porsche,
        "Mercedes-Benz" to R.drawable.benz,
        "Chevrolet" to R.drawable.cruze,
        "BYD" to R.drawable.byd,
        "Brabus" to R.drawable.brabus
    )
    fun getImageForMake(makeName: String): Int {
        return makeImages[makeName] ?: R.drawable.ic_launcher_background // default şəkil
    }
}
package com.example.turboazapp.util

import com.example.turboazapp.domain.model.Car
import java.text.Normalizer

object SearchHelper {

    /**
     * Maşını axtarış sorğusuna görə yoxlayır
     * Böyük/kiçik hərf həssaslığı yoxdur
     */
    fun matchesQuery(car: Car, query: String): Boolean {
        val normalizedQuery = normalizeText(query)

        return normalizeText(car.brand).contains(normalizedQuery) ||
                normalizeText(car.model).contains(normalizedQuery) ||
                normalizeText(car.price.toString()).contains(normalizedQuery)
    }

    private fun normalizeText(text: String): String {
        return text.lowercase()
            .replace('ə', 'e')
            .replace('ı', 'i')
            .replace('ö', 'o')
            .replace('ü', 'u')
            .replace('ş', 's')
            .replace('ç', 'c')
            .replace('ğ', 'g')
            .trim()
    }
}
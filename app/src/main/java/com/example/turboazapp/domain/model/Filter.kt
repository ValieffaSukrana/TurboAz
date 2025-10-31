package com.example.turboazapp.domain.model

data class Filter(
    val brand: String? = null,
    val model: String? = null,
    val minPrice: Double? = null,
    val maxPrice: Double? = null,
    val currency: String = "AZN",
    val minYear: Int? = null,
    val maxYear: Int? = null,
    val minMileage: Int? = null,
    val maxMileage: Int? = null,
    val city: String? = null,
    val fuelType: String? = null,
    val transmission: String? = null,
    val bodyType: String? = null,
    val driveType: String? = null,
    val color: String? = null,
    val minEnginVolume: Double? = null,
    val maxEngineVolume: Double? = null,
    val isNew: Boolean? = null,
    val onlyWithPhotos: Boolean = false,
    val sortBy: SortOption = SortOption.DATE_DESC
)

enum class SortOption {
    DATE_DESC,      // Ən yeni
    DATE_ASC,       // Ən köhnə
    PRICE_ASC,      // Ucuzdan bahaya
    PRICE_DESC,     // Bahadan ucuza
    MILEAGE_ASC,    // Az yürüşlü
    YEAR_DESC       // İl azalan
}

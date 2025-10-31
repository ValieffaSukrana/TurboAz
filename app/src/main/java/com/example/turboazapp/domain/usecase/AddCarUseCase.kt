package com.example.turboazapp.domain.usecase

import com.example.turboazapp.domain.model.Car
import com.example.turboazapp.domain.repository.CarRepository
import com.example.turboazapp.util.Resource
import javax.inject.Inject

class AddCarUseCase @Inject constructor(
    private val repository: CarRepository
) {
    suspend operator fun invoke(car: Car): Resource<String> {
        // Validasiya
        if (car.brand.isBlank()) {
            return Resource.Error("Marka seçin")
        }
        if (car.model.isBlank()) {
            return Resource.Error("Model daxil edin")
        }
        if (car.year < 1900 || car.year > 2025) {
            return Resource.Error("Düzgün il daxil edin")
        }
        if (car.price <= 0) {
            return Resource.Error("Qiymət daxil edin")
        }
        if (car.phone.isBlank()) {
            return Resource.Error("Telefon nömrəsi daxil edin")
        }
        if (car.images.isEmpty()) {
            return Resource.Error("Ən azı 1 şəkil əlavə edin")
        }

        return repository.addCar(car)
    }
}
package com.example.turboazapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.turboazapp.domain.model.Car
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CarsViewModel @Inject constructor(private val db: FirebaseFirestore) : ViewModel() {
    private val carsCol get() = db.collection("cars")

    fun seedDummyCars(onDone: () -> Unit = {}, onError: (Throwable) -> Unit = {}) {
        val dummy = listOf(
            Car(
                15000.0, brand = "Toyota", model = "Corolla", url = "https://picsum.photos/400?1",
                engine = "1.6", color = "White", transmission = "AT", fuel = "Petrol", year = 2018
            ),
            Car(
                23500.0, brand = "Hyundai", model = "Elantra", url = "https://picsum.photos/400?2",
                engine = "2.0", color = "Blue", transmission = "AT", fuel = "Petrol", year = 2020
            ),
            Car(
                42000.0, brand = "BMW", model = "X3", url = "https://picsum.photos/400?3",
                engine = "2.0", color = "Black", transmission = "AT", fuel = "Diesel", year = 2021
            ),
            Car(
                19500.0, brand = "Honda", model = "Civic", url = "https://picsum.photos/400?4",
                engine = "1.5T", color = "Red", transmission = "CVT", fuel = "Petrol", year = 2019
            )
        )

        val batch = db.batch()
        dummy.forEach { car ->
            val doc = carsCol.document()
            batch.set(doc, car.copy(id = doc.id))
        }
        batch.commit()
            .addOnSuccessListener { onDone() }
            .addOnFailureListener { onError(it) }
    }
}
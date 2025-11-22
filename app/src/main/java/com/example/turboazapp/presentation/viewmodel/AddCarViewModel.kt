package com.example.turboazapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.turboazapp.data.remote.FirebaseAuthDataSource
import com.example.turboazapp.domain.model.Car
import com.example.turboazapp.domain.usecase.AddCarUseCase
import com.example.turboazapp.util.ImageUploadHelper
import com.example.turboazapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddCarViewModel @Inject constructor(
    private val imageUploadHelper: ImageUploadHelper,
    private val addCarUseCase: AddCarUseCase,
    private val authDataSource: FirebaseAuthDataSource
) : ViewModel() {

    private val _addCarState = MutableStateFlow<AddCarState>(AddCarState.Idle)
    val addCarState: StateFlow<AddCarState> = _addCarState.asStateFlow()

    // Seçilmiş məlumatlar
    private val _selectedBrand = MutableStateFlow<String?>(null)
    val selectedBrand = _selectedBrand.asStateFlow()

    private val _selectedModel = MutableStateFlow<String?>(null)
    val selectedModel = _selectedModel.asStateFlow()

    private val _selectedYear = MutableStateFlow<Int?>(null)
    val selectedYear = _selectedYear.asStateFlow()

    private val _selectedBodyType = MutableStateFlow<String?>(null)
    val selectedBodyType = _selectedBodyType.asStateFlow()

    private val _selectedEngine = MutableStateFlow<String?>(null)
    val selectedEngine = _selectedEngine.asStateFlow()

    private val _selectedColor = MutableStateFlow<String?>(null)
    val selectedColor = _selectedColor.asStateFlow()

    private val _selectedMarket = MutableStateFlow<String?>(null)
    val selectedMarket = _selectedMarket.asStateFlow()

    private val _mileage = MutableStateFlow<Int?>(null)
    val mileage = _mileage.asStateFlow()

    private val _mileageUnit = MutableStateFlow("km")
    val mileageUnit = _mileageUnit.asStateFlow()

    private val _selectedImages = MutableStateFlow<List<String>>(emptyList())
    val selectedImages = _selectedImages.asStateFlow()

    private val _price = MutableStateFlow<Double?>(null)
    val price = _price.asStateFlow()

    private val _currency = MutableStateFlow("AZN")
    val currency = _currency.asStateFlow()

    private val _creditAllowed = MutableStateFlow(false)
    val creditAllowed = _creditAllowed.asStateFlow()

    private val _barterAllowed = MutableStateFlow(false)
    val barterAllowed = _barterAllowed.asStateFlow()

    private val _contactName = MutableStateFlow<String?>(null)
    private val _contactEmail = MutableStateFlow<String?>(null)
    private val _contactPhone = MutableStateFlow<String?>(null)


    // Seçimləri saxla
    fun selectBrand(brand: String) {
        _selectedBrand.value = brand
    }

    fun selectModel(model: String) {
        _selectedModel.value = model
    }

    fun selectYear(year: Int) {
        _selectedYear.value = year
    }

    fun selectBodyType(bodyType: String) {
        _selectedBodyType.value = bodyType
    }

    fun selectEngine(engine: String) {
        _selectedEngine.value = engine
    }

    fun selectColor(color: String) {
        _selectedColor.value = color
    }

    fun selectMarket(market: String) {
        _selectedMarket.value = market
    }

    fun setMileage(mileage: Int) {
        _mileage.value = mileage
    }

    fun setMileageUnit(unit: String) {
        _mileageUnit.value = unit
    }

    fun addImage(imageUrl: String) {
        _selectedImages.value = _selectedImages.value + imageUrl
    }

    fun removeImage(imageUrl: String) {
        _selectedImages.value = _selectedImages.value - imageUrl
    }



    fun addCar(
        price: Double,
        currency: String,
        city: String,
        fuelType: String,
        transmission: String,
        engineVolume: Double,
        horsePower: Int,
        driveType: String,
        ownerCount: Int,
        crashHistory: Boolean,
        painted: Boolean,
        description: String,
        phone: String,
        sellerId: String,
        sellerName: String,
        isNew: Boolean
    ) {
        viewModelScope.launch {
            _addCarState.value = AddCarState.Loading

            try {
                // 1️⃣ Şəkilləri Firebase Storage-ə yüklə
                val uploadedImageUrls = if (_selectedImages.value.isNotEmpty()) {
                    imageUploadHelper.uploadImages(_selectedImages.value)
                } else {
                    emptyList()
                }

                // 2️⃣ Car obyekti yarat (Firebase Storage URL-ləri ilə)
                val car = Car(
                    brand = _selectedBrand.value ?: "",
                    model = _selectedModel.value ?: "",
                    year = _selectedYear.value ?: 0,
                    bodyType = _selectedBodyType.value ?: "",
                    fuelType = fuelType,
                    color = _selectedColor.value ?: "",
                    mileage = _mileage.value ?: 0,
                    images = uploadedImageUrls, // ✅ Firebase Storage URLs
                    price = price,
                    currency = currency,
                    city = city,
                    transmission = transmission,
                    engineVolume = engineVolume,
                    horsePower = horsePower,
                    driveType = driveType,
                    ownerCount = ownerCount,
                    crashHistory = crashHistory,
                    painted = painted,
                    description = description,
                    phone = phone,
                    sellerId = authDataSource.getCurrentUser()?.id ?: sellerId,
                    sellerName = sellerName,
                    isNew = isNew
                )

                // 3️⃣ Firestore-a yaz
                when (val result = addCarUseCase(car)) {
                    is Resource.Success -> {
                        _addCarState.value = AddCarState.Success(result.data ?: "")
                    }

                    is Resource.Error -> {
                        _addCarState.value = AddCarState.Error(result.message ?: "Xəta baş verdi")
                    }

                    is Resource.Loading -> {
                        _addCarState.value = AddCarState.Loading
                    }
                }
            } catch (e: Exception) {
                _addCarState.value = AddCarState.Error("Şəkil yükləmə xətası: ${e.message}")
            }
        }
    }

    fun setPrice(price: Double, currency: String) {
        _price.value = price
        _currency.value = currency
    }

    fun setCreditAllowed(allowed: Boolean) {
        _creditAllowed.value = allowed
    }

    fun setBarterAllowed(allowed: Boolean) {
        _barterAllowed.value = allowed
    }

    fun setContactInfo(name: String, email: String, phone: String) {
        _contactName.value = name
        _contactEmail.value = email
        _contactPhone.value = phone
    }

    fun resetState() {
        _addCarState.value = AddCarState.Idle
    }
}

sealed class AddCarState {
    object Idle : AddCarState()
    object Loading : AddCarState()
    data class Success(val carId: String) : AddCarState()
    data class Error(val message: String) : AddCarState()
}
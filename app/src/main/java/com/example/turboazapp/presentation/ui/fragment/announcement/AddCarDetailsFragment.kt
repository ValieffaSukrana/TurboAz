package com.example.turboazapp.presentation.ui.fragment.announcement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.turboazapp.databinding.FragmentAddCarDetailsBinding
import com.example.turboazapp.presentation.viewmodel.AddCarState
import com.example.turboazapp.presentation.viewmodel.AddCarViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AddCarDetailsFragment : Fragment() {

    private var _binding: FragmentAddCarDetailsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AddCarViewModel by activityViewModels()

    @Inject
    lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddCarDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        setupSpinners()
        setupSubmitButton()
        observeViewModel()
    }

    private fun setupToolbar() {
        binding.titleText.text = "Əlavə məlumat"
        binding.subtitleText.text = "16-dan addım 10"

        binding.backButton.setOnClickListener {
            (activity as? AddCarActivity)?.navigateBack()
        }

        binding.cancelButton.setOnClickListener {
            activity?.finish()
        }
    }

    private fun setupSpinners() {
        // Şəhərlər
        val cities = arrayOf(
            "Bakı", "Sumqayıt", "Gəncə", "Mingəçevir",
            "Şirvan", "Lənkəran", "Naxçıvan", "Şəki",
            "Yevlax", "Quba", "Xaçmaz", "Ağdam"
        )
        binding.citySpinner.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            cities
        )

        // Yanacaq növü
        val fuelTypes = arrayOf("Benzin", "Dizel", "Qaz", "Elektrik", "Hibrid")
        binding.fuelTypeSpinner.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            fuelTypes
        )

        // Ötürücü
        val transmissions = arrayOf("Avtomatik", "Mexaniki")
        binding.transmissionSpinner.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            transmissions
        )

        // Ötürücü tipi
        val driveTypes = arrayOf("Ön", "Arxa", "Tam")
        binding.driveTypeSpinner.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            driveTypes
        )

        // Valyuta
        val currencies = arrayOf("AZN", "USD", "EUR")
        binding.currencySpinner.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            currencies
        )
    }

    private fun setupSubmitButton() {
        binding.btnSubmit.setOnClickListener {
            if (validateInputs()) {
                submitCar()
            }
        }
    }

    private fun validateInputs(): Boolean {
        val price = binding.priceEditText.text.toString().toDoubleOrNull()
        val phone = binding.phoneEditText.text.toString()

        if (price == null || price <= 0) {
            Toast.makeText(requireContext(), "Qiymət daxil edin", Toast.LENGTH_SHORT).show()
            return false
        }

        if (phone.isBlank()) {
            Toast.makeText(requireContext(), "Telefon nömrəsi daxil edin", Toast.LENGTH_SHORT)
                .show()
            return false
        }

        if (phone.length < 9) {
            Toast.makeText(
                requireContext(),
                "Düzgün telefon nömrəsi daxil edin",
                Toast.LENGTH_SHORT
            ).show()
            return false
        }

        return true
    }


    private fun submitCar() {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            Toast.makeText(requireContext(), "Xahiş edirik daxil olun", Toast.LENGTH_SHORT).show()
            return
        }

        val price = binding.priceEditText.text.toString().toDouble()
        val currency = binding.currencySpinner.selectedItem.toString()
        val city = binding.citySpinner.selectedItem.toString()
        val fuelType = binding.fuelTypeSpinner.selectedItem.toString()
        val transmission = binding.transmissionSpinner.selectedItem.toString()
        val engineVolume = binding.engineVolumeEditText.text.toString().toDoubleOrNull() ?: 0.0
        val horsePower = binding.horsePowerEditText.text.toString().toIntOrNull() ?: 0
        val driveType = binding.driveTypeSpinner.selectedItem.toString()
        val ownerCount = binding.ownerCountEditText.text.toString().toIntOrNull() ?: 1
        val crashHistory = binding.crashCheckbox.isChecked
        val painted = binding.paintedCheckbox.isChecked
        val description = binding.descriptionEditText.text.toString()
        val phone = binding.phoneEditText.text.toString()
        val isNew = binding.newCarCheckbox.isChecked

        viewModel.addCar(
            price = price,
            currency = currency,
            city = city,
            fuelType = fuelType,
            transmission = transmission,
            engineVolume = engineVolume,
            horsePower = horsePower,
            driveType = driveType,
            ownerCount = ownerCount,
            crashHistory = crashHistory,
            painted = painted,
            description = description,
            phone = phone,
            sellerId = currentUser.uid,
            sellerName = currentUser.displayName ?: "Anonim",
            isNew = isNew
        )
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.addCarState.collect { state ->
                when (state) {
                    is AddCarState.Loading -> {
                        binding.btnSubmit.isEnabled = false
                        binding.btnSubmit.text = "Yüklənir..."
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    is AddCarState.Success -> {
                        binding.btnSubmit.isEnabled = true
                        binding.btnSubmit.text = "Elanı yerləşdir"
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(
                            requireContext(),
                            "Elan uğurla əlavə edildi!",
                            Toast.LENGTH_LONG
                        ).show()

                        // Activity-ni bağla və əsas səhifəyə qayıt
                        activity?.finish()
                    }

                    is AddCarState.Error -> {
                        binding.btnSubmit.isEnabled = true
                        binding.btnSubmit.text = "Elanı yerləşdir"
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(
                            requireContext(),
                            "Xəta: ${state.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    AddCarState.Idle -> {
                        binding.btnSubmit.isEnabled = true
                        binding.btnSubmit.text = "Elanı yerləşdir"
                        binding.progressBar.visibility = View.GONE
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
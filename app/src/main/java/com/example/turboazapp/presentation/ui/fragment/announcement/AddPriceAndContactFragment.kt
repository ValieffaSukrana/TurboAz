package com.example.turboazapp.presentation.ui.fragment.announcement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.turboazapp.R
import com.example.turboazapp.databinding.FragmentAddPriceAndContactBinding
import com.example.turboazapp.presentation.viewmodel.AddCarViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AddPriceAndContactFragment : Fragment() {

    private var _binding: FragmentAddPriceAndContactBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AddCarViewModel by activityViewModels()

    @Inject
    lateinit var auth: FirebaseAuth

    private var selectedCurrency = "AZN"
    private var creditAllowed = false
    private var barterAllowed = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddPriceAndContactBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        setupCurrencyButtons()
        setupSwitches()
        setupPhoneField()
        setupContinueButton()
    }

    private fun setupToolbar() {
        lifecycleScope.launch {
            viewModel.selectedBrand.collect { brand ->
                binding.titleText.text = brand ?: "Qiymət və Əlaqə"
            }
        }

        binding.subtitleText.text = "16-dan addım 15"

        binding.backButton.setOnClickListener {
            (activity as? AddCarActivity)?.navigateBack()
        }

        binding.cancelButton.setOnClickListener {
            activity?.finish()
        }
    }

    private fun setupCurrencyButtons() {
        // Default AZN seçili
        selectCurrency(binding.btnAZN)

        binding.btnAZN.setOnClickListener {
            selectCurrency(binding.btnAZN)
            selectedCurrency = "AZN"
        }

        binding.btnUSD.setOnClickListener {
            selectCurrency(binding.btnUSD)
            selectedCurrency = "USD"
        }

        binding.btnEUR.setOnClickListener {
            selectCurrency(binding.btnEUR)
            selectedCurrency = "EUR"
        }
    }

    private fun selectCurrency(selectedButton: View) {
        // Hamısını deselect et
        binding.btnAZN.apply {
            backgroundTintList = ContextCompat.getColorStateList(requireContext(), android.R.color.transparent)
            strokeColor = ContextCompat.getColorStateList(requireContext(), android.R.color.darker_gray)
        }
        binding.btnUSD.apply {
            backgroundTintList = ContextCompat.getColorStateList(requireContext(), android.R.color.transparent)
            strokeColor = ContextCompat.getColorStateList(requireContext(), android.R.color.darker_gray)
        }
        binding.btnEUR.apply {
            backgroundTintList = ContextCompat.getColorStateList(requireContext(), android.R.color.transparent)
            strokeColor = ContextCompat.getColorStateList(requireContext(), android.R.color.darker_gray)
        }

        // Seçilən düyməni highlight et
        when (selectedButton) {
            binding.btnAZN -> binding.btnAZN.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.dark_blue)
            binding.btnUSD -> binding.btnUSD.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.dark_blue)
            binding.btnEUR -> binding.btnEUR.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.dark_blue)
        }
    }

    private fun setupSwitches() {
        binding.creditSwitch.setOnCheckedChangeListener { _, isChecked ->
            creditAllowed = isChecked
        }

        binding.barterSwitch.setOnCheckedChangeListener { _, isChecked ->
            barterAllowed = isChecked
        }
    }

    private fun setupPhoneField() {
        // Telefon nömrəsini Firebase-dən al
        auth.currentUser?.let { user ->
            val phone = user.phoneNumber?.removePrefix("+994") ?: ""
            binding.phoneEditText.setText(phone)
        }

        // Real-time format yoxlaması
        binding.phoneEditText.addTextChangedListener {
            val phone = it.toString()
            if (phone.length >= 9) {
                binding.phoneLayout.endIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_check)
                binding.phoneLayout.setEndIconTintList(ContextCompat.getColorStateList(requireContext(), R.color.dark_blue))
            } else {
                binding.phoneLayout.endIconDrawable = null
            }
        }
    }

    private fun setupContinueButton() {
        binding.btnDavamEt.setOnClickListener {
            if (validateInputs()) {
                proceedToNextStep()
            }
        }
    }

    private fun validateInputs(): Boolean {
        val price = binding.priceEditText.text.toString().toDoubleOrNull()
        val name = binding.nameEditText.text.toString()
        val email = binding.emailEditText.text.toString()
        val phone = binding.phoneEditText.text.toString()

        if (price == null || price <= 0) {
            Toast.makeText(requireContext(), "Qiymət daxil edin", Toast.LENGTH_SHORT).show()
            return false
        }

        if (name.isBlank()) {
            Toast.makeText(requireContext(), "Adınızı daxil edin", Toast.LENGTH_SHORT).show()
            return false
        }

        if (email.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(requireContext(), "Düzgün email daxil edin", Toast.LENGTH_SHORT).show()
            return false
        }

        if (phone.length < 9) {
            Toast.makeText(requireContext(), "Düzgün telefon nömrəsi daxil edin", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun proceedToNextStep() {
        val price = binding.priceEditText.text.toString().toDouble()
        val name = binding.nameEditText.text.toString()
        val email = binding.emailEditText.text.toString()
        val phone = "+994${binding.phoneEditText.text.toString()}"

        // ViewModel-ə məlumatları əlavə et
        viewModel.setPrice(price, selectedCurrency)
        viewModel.setCreditAllowed(creditAllowed)
        viewModel.setBarterAllowed(barterAllowed)
        viewModel.setContactInfo(name, email, phone)

        // Növbəti səhifəyə keç - Final submit
        (activity as? AddCarActivity)?.navigateToFragment(AddCarDetailsFragment())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
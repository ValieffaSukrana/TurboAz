package com.example.turboazapp.presentation.ui.fragment.announcement

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.turboazapp.R
import com.example.turboazapp.databinding.FragmentSelectMileageBinding
import com.example.turboazapp.presentation.viewmodel.AddCarViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SelectMileageFragment : Fragment() {

    private var _binding: FragmentSelectMileageBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AddCarViewModel by activityViewModels()

    private var selectedUnit = "km"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSelectMileageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        setupUnitButtons()
        setupMileageInput()
        setupContinueButton()
    }

    private fun setupToolbar() {
        lifecycleScope.launch {
            viewModel.selectedBrand.collect { brand ->
                binding.titleText.text = brand ?: "Yürüş"
            }
        }

        binding.subtitleText.text = "16-dan addım 8"

        binding.backButton.setOnClickListener {
            (activity as? AddCarActivity)?.navigateBack()
        }

        binding.cancelButton.setOnClickListener {
            activity?.finish()
        }
    }

    private fun setupUnitButtons() {
        // Default km seçili
        updateUnitButtons(binding.kmBtn)
        viewModel.setMileageUnit("km")

        binding.kmBtn.setOnClickListener {
            selectedUnit = "km"
            viewModel.setMileageUnit("km")
            updateUnitButtons(binding.kmBtn)
        }

        binding.mlBtn.setOnClickListener {
            selectedUnit = "mi"
            viewModel.setMileageUnit("mi")
            updateUnitButtons(binding.mlBtn)
        }
    }

    private fun updateUnitButtons(selectedButton: View) {
        if (selectedButton == binding.kmBtn) {
            binding.kmBtn.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.dark_blue)
            binding.kmBtn.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.white))

            binding.mlBtn.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.light_gray)
            binding.mlBtn.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.black))
        } else {
            binding.mlBtn.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.dark_blue)
            binding.mlBtn.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.white))

            binding.kmBtn.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.light_gray)
            binding.kmBtn.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.black))
        }
    }

    private fun setupMileageInput() {
        binding.yurusEditTxt.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val mileage = s.toString().toIntOrNull()
                binding.btnDavamEt.isEnabled = mileage != null && mileage > 0
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun setupContinueButton() {
        binding.btnDavamEt.isEnabled = false
        binding.btnDavamEt.setOnClickListener {
            val mileage = binding.yurusEditTxt.editText?.text.toString().toIntOrNull()
            if (mileage != null) {
                viewModel.setMileage(mileage)
                (activity as? AddCarActivity)?.navigateToFragment(SelectImagesFragment())
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
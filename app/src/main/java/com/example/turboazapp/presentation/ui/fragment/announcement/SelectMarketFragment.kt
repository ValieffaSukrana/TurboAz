package com.example.turboazapp.presentation.ui.fragment.announcement

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.turboazapp.R
import com.example.turboazapp.databinding.FragmentSelectMarketBinding
import com.example.turboazapp.presentation.viewmodel.AddCarViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SelectMarketFragment : Fragment() {

    private var _binding: FragmentSelectMarketBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AddCarViewModel by activityViewModels()

    private var selectedMarket: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSelectMarketBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        setupButtons()
        setupContinueButton()
    }

    private fun setupToolbar() {
        lifecycleScope.launch {
            viewModel.selectedBrand.collect { brand ->
                binding.titleText.text = brand ?: "Bazar"
            }
        }

        binding.subtitleText.text = "16-dan addım 7"

        binding.backButton.setOnClickListener {
            (activity as? AddCarActivity)?.navigateBack()
        }

        binding.cancelButton.setOnClickListener {
            activity?.finish()
        }
    }

    private fun setupButtons() {
        val buttons = listOf(
            binding.btnAmerika to "Amerika",
            binding.btnAvropa to "Avropa",
            binding.btnDiger to "Digər",
            binding.btnDubay to "Dubay",
            binding.btnKoreya to "Koreya",
            binding.btnRusiya to "Rusiya",
            binding.btnResmiDiler to "Rəsmi diler",
            binding.btnYaponiya to "Yaponiya",
            binding.btnCin to "Çin"
        )

        buttons.forEach { (button, market) ->
            button.setOnClickListener {
                selectMarket(market)
                updateButtonStates(button)
            }
        }
    }

    private fun selectMarket(market: String) {
        selectedMarket = market
        viewModel.selectMarket(market)
        binding.btnDavamEt.isEnabled = true
    }

    private fun updateButtonStates(selectedButton: View) {
        val allButtons = listOf(
            binding.btnAmerika,
            binding.btnAvropa,
            binding.btnDiger,
            binding.btnDubay,
            binding.btnKoreya,
            binding.btnRusiya,
            binding.btnResmiDiler,
            binding.btnYaponiya,
            binding.btnCin
        )

        allButtons.forEach { button ->
            if (button == selectedButton) {
                button.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.dark_blue)
                button.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.white))
            } else {
                button.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.light_gray)
                button.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.black))
            }
        }
    }

    private fun setupContinueButton() {
        binding.btnDavamEt.isEnabled = false
        binding.btnDavamEt.setOnClickListener {
            (activity as? AddCarActivity)?.navigateToFragment(SelectMileageFragment())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
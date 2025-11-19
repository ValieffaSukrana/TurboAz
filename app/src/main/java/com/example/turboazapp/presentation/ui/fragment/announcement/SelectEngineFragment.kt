package com.example.turboazapp.presentation.ui.fragment.announcement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.turboazapp.AddCarActivity
import com.example.turboazapp.R
import com.example.turboazapp.databinding.FragmentSelectEngineBinding
import com.example.turboazapp.presentation.viewmodel.AddCarViewModel
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SelectEngineFragment : Fragment() {

    private var _binding: FragmentSelectEngineBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AddCarViewModel by activityViewModels()

    private val engineTypes = listOf(
        "1.0", "1.2", "1.3", "1.4", "1.5", "1.6", "1.8",
        "2.0", "2.2", "2.4", "2.5", "2.7", "2.8", "3.0",
        "3.2", "3.5", "3.6", "4.0", "4.2", "4.4", "4.6",
        "5.0", "5.5", "5.7", "6.0", "6.2"
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSelectEngineBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        setupChips()
    }

    private fun setupToolbar() {
        lifecycleScope.launch {
            viewModel.selectedBrand.collect { brand ->
                binding.titleText.text = brand ?: "Mühərrik"
            }
        }

        binding.subtitleText.text = "16-dan addım 6"

        binding.backButton.setOnClickListener {
            (activity as? AddCarActivity)?.navigateBack()
        }

        binding.cancelButton.setOnClickListener {
            activity?.finish()
        }
    }

    private fun setupChips() {
        engineTypes.forEach { engineType ->
            val chip = Chip(requireContext()).apply {
                text = "$engineType L"
                isCheckable = true

                // Chip stilini təyin et
                val chipDrawable = ChipDrawable.createFromAttributes(
                    requireContext(),
                    null,
                    0,
                    com.google.android.material.R.style.Widget_MaterialComponents_Chip_Choice
                )
                setChipDrawable(chipDrawable)

                // Rənglər
                setChipBackgroundColorResource(R.color.light_gray)
                setTextColor(ContextCompat.getColor(requireContext(), android.R.color.black))

                // Seçiləndə rəng dəyişir
                setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        chipBackgroundColor = ContextCompat.getColorStateList(requireContext(), R.color.dark_blue)
                        setTextColor(ContextCompat.getColor(requireContext(), android.R.color.white))

                        viewModel.selectEngine(engineType)

                        // Bir qədər gözlə və növbəti səhifəyə keç
                        view?.postDelayed({
                            (activity as? AddCarActivity)?.navigateToFragment(SelectMarketFragment())
                        }, 300)
                    } else {
                        chipBackgroundColor = ContextCompat.getColorStateList(requireContext(), R.color.light_gray)
                        setTextColor(ContextCompat.getColor(requireContext(), android.R.color.black))
                    }
                }

                // Padding və margin
                setPadding(32, 16, 32, 16)
            }

            binding.chipEngine.addView(chip)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
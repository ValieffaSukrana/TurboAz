package com.example.turboazapp.presentation.ui.fragment.announcement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.turboazapp.databinding.FragmentSelectYearBinding
import com.example.turboazapp.presentation.ui.adapter.YearAdapter
import com.example.turboazapp.presentation.viewmodel.AddCarViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Calendar

@AndroidEntryPoint
class SelectYearFragment : Fragment() {

    private var _binding: FragmentSelectYearBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AddCarViewModel by activityViewModels()
    private lateinit var yearAdapter: YearAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSelectYearBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        setupRecyclerView()
    }

    private fun setupToolbar() {
        lifecycleScope.launch {
            viewModel.selectedBrand.collect { brand ->
                binding.titleText.text = brand ?: "Buraxış ili"
            }
        }

        binding.subtitleText.text = "16-dan addım 3"

        binding.backButton.setOnClickListener {
            (activity as? AddCarActivity)?.navigateBack()
        }

        binding.cancelButton.setOnClickListener {
            activity?.finish()
        }
    }

    private fun setupRecyclerView() {
        // İlləri tərs ardıcıllıqla yarat (2025-dən 1990-a)
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val years = (currentYear downTo 1990).toList()

        yearAdapter = YearAdapter(years) { selectedYear ->
            viewModel.selectYear(selectedYear)
            (activity as? AddCarActivity)?.navigateToFragment(SelectBodyTypeFragment())
        }

        binding.recyclerViewYear.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = yearAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
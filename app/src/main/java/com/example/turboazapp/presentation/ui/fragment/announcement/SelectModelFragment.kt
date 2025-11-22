package com.example.turboazapp.presentation.ui.fragment.announcement

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.turboazapp.databinding.FragmentSelectModelBinding
import com.example.turboazapp.presentation.ui.adapter.ModelAdapter
import com.example.turboazapp.presentation.viewmodel.AddCarViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SelectModelFragment : Fragment() {

    private var _binding: FragmentSelectModelBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AddCarViewModel by activityViewModels()
    private lateinit var modelAdapter: ModelAdapter

    // Müvəqqəti data - Firebase-dən gələcək
    private val models = listOf(
        "X1", "X3", "X5", "X6", "X7",
        "1 Series", "2 Series", "3 Series", "4 Series", "5 Series",
        "6 Series", "7 Series", "8 Series", "M3", "M5", "i3", "i8"
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSelectModelBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        setupRecyclerView()
        setupSearchView()
        observeViewModel()
    }

    private fun setupToolbar() {
        lifecycleScope.launch {
            viewModel.selectedBrand.collect { brand ->
                binding.titleText.text = brand ?: "Model"
            }
        }

        binding.subtitleText.text = "16-dan addım 2"

        binding.backButton.setOnClickListener {
            (activity as? AddCarActivity)?.navigateBack()
        }

        binding.cancelButton.setOnClickListener {
            activity?.finish()
        }
    }

    private fun setupRecyclerView() {
        modelAdapter = ModelAdapter(models) { selectedModel ->
            viewModel.selectModel(selectedModel)
            (activity as? AddCarActivity)?.navigateToFragment(SelectYearFragment())
        }

        binding.recyclerViewModels.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = modelAdapter
        }
    }

    private fun setupSearchView() {
        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                modelAdapter.filter(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.selectedBrand.collect { brand ->
                // Burada Firebase-dən brand-a görə modelləri yükləyə bilərsiz
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
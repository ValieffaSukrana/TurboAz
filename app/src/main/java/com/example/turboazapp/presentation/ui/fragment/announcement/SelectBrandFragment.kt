package com.example.turboazapp.presentation.ui.fragment.announcement

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.turboazapp.AddCarActivity
import com.example.turboazapp.databinding.FragmentSelectBrandBinding
import com.example.turboazapp.presentation.ui.adapter.BrandAdapter
import com.example.turboazapp.presentation.viewmodel.AddCarViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SelectBrandFragment : Fragment() {

    private var _binding: FragmentSelectBrandBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AddCarViewModel by activityViewModels()
    private lateinit var brandAdapter: BrandAdapter

    private val brands = listOf(
        "Audi", "BMW", "Mercedes-Benz", "Toyota", "Honda",
        "Hyundai", "Kia", "Nissan", "Volkswagen", "Ford",
        "Chevrolet", "Mazda", "Lexus", "Porsche", "Jaguar",
        "Land Rover", "Mitsubishi", "Subaru", "Volvo", "Peugeot"
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSelectBrandBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        setupRecyclerView()
        setupSearchView()
    }

    private fun setupToolbar() {
        binding.titleText.text = "Marka"
        binding.subtitleText.text = "16-dan addım 1"

        binding.backButton.setOnClickListener {
            activity?.finish()
        }
    }

    private fun setupRecyclerView() {
        brandAdapter = BrandAdapter(brands) { selectedBrand ->
            viewModel.selectBrand(selectedBrand)
            (activity as? AddCarActivity)?.navigateToFragment(SelectModelFragment())
        }

        binding.recyclerViewBrands.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = brandAdapter
        }
    }

    private fun setupSearchView() {
        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                brandAdapter.filter(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
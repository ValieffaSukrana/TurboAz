package com.example.turboazapp.presentation.ui.fragment.announcement

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
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
        Log.d("SelectBrandFragment", "onCreateView")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("SelectBrandFragment", "onViewCreated")

        setupToolbar()
        setupRecyclerView()
        setupSearchView()
    }

    private fun setupToolbar() {
        binding.titleText.text = "Marka"
        binding.subtitleText.text = "16-dan addım 1"

        binding.backButton.setOnClickListener {
            Log.d("SelectBrandFragment", "Back button clicked")
            activity?.finish()
        }

        binding.backButton.setOnClickListener {
            Log.d("SelectBrandFragment", "Cancel button clicked")
            activity?.finish()
        }
    }

    private fun setupRecyclerView() {
        brandAdapter = BrandAdapter(brands) { selectedBrand ->
            Log.d("SelectBrandFragment", "🎯 Brand callback received: $selectedBrand")

            Toast.makeText(
                requireContext(),
                "Seçildi: $selectedBrand",
                Toast.LENGTH_SHORT
            ).show()

            // ViewModel-ə yaz
            viewModel.selectBrand(selectedBrand)
            Log.d("SelectBrandFragment", "✅ ViewModel-ə yazıldı: $selectedBrand")

            // Fragment keçidi
            Log.d("SelectBrandFragment", "🚀 SelectModelFragment-ə keçid başlayır...")
            val activity = activity as? AddCarActivity
            if (activity != null) {
                activity.navigateToFragment(SelectModelFragment())
                Log.d("SelectBrandFragment", "✅ navigateToFragment çağırıldı")
            } else {
                Log.e("SelectBrandFragment", "❌ AddCarActivity tapılmadı!")
            }
        }

        binding.recyclerViewBrands.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = brandAdapter
        }

        Log.d("SelectBrandFragment", "RecyclerView setup: ${brandAdapter.itemCount} items")
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
        Log.d("SelectBrandFragment", "onDestroyView")
        _binding = null
    }
}
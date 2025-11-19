package com.example.turboazapp.presentation.ui.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.turboazapp.R
import com.example.turboazapp.databinding.FragmentSelectedBinding
import com.example.turboazapp.domain.model.Car
import com.example.turboazapp.presentation.ui.adapter.SelectedAdapter
import com.example.turboazapp.presentation.viewmodel.SelectedViewModel
import com.example.turboazapp.util.SearchHelper
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SelectedFragment : Fragment() {

    private var _binding: FragmentSelectedBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: SelectedAdapter
    private val viewModel: SelectedViewModel by viewModels()

    private var allCars: List<Car> = emptyList()
    private var searchChip: Chip? = null

    override fun onResume() {
        super.onResume()
        (requireActivity() as MainActivity).setToolbarVisible(false)
        (requireActivity() as MainActivity).setBottomNavVisible(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSelectedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupChips()
        setupRecyclerView()
        setupSearchView()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        adapter = SelectedAdapter(
            onItemClick = { car ->
                navigateToCarDetails(car.id)
            },
            onFavoriteClick = { car, isFavorite ->
                viewModel.toggleFavorite(car, isFavorite)

                if (isFavorite) {
                    Snackbar.make(
                        binding.root,
                        "Seçilmişlərdən silindi",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        )

        binding.SelectedRecyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = this@SelectedFragment.adapter
            setHasFixedSize(true)
        }
    }

    private fun setupSearchView() {
        binding.searchViewSelected.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterCars(newText ?: "")
                return true
            }
        })
    }

    private fun filterCars(query: String) {
        val filteredList = if (query.isEmpty()) {
            allCars
        } else {
            allCars.filter { car ->
                SearchHelper.matchesQuery(car, query)
            }
        }

        adapter.updateList(filteredList)

        if (filteredList.isEmpty() && query.isNotEmpty()) {
            Toast.makeText(
                requireContext(),
                "Nəticə tapılmadı",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun navigateToCarDetails(carId: String) {
        try {
            val bundle = bundleOf("carId" to carId)
            findNavController().navigate(
                R.id.action_selectedFragment_to_carDetailsFragment,
                bundle
            )
        } catch (e: Exception) {
            Toast.makeText(
                requireContext(),
                "Xəta: ${e.message}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.selectedState.collect { state ->
                    when (state) {
                        is SelectedViewModel.SelectedState.Loading -> {
                            // Loading göstər (opsional)
                        }
                        is SelectedViewModel.SelectedState.Empty -> {
                            allCars = emptyList()
                            adapter.updateList(emptyList())
                            Toast.makeText(
                                requireContext(),
                                "Seçilmiş elan yoxdur",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        is SelectedViewModel.SelectedState.Success -> {
                            allCars = state.cars
                            adapter.updateList(state.cars)

                            // Sevimliləri set et
                            val favoriteIds = state.cars.map { it.id }.toSet()
                            adapter.setFavoriteItems(favoriteIds)
                        }
                        is SelectedViewModel.SelectedState.Error -> {
                            Toast.makeText(
                                requireContext(),
                                "Xəta: ${state.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
    }

    private fun setupChips() {
        val categoriesSelected = listOf(
            "Elanlar" to R.drawable.notepad,
            "Axtarışlar" to R.drawable.search,
            "Müqayisə" to R.drawable.compare
        )

        for ((category, icon) in categoriesSelected) {
            val chip = Chip(requireContext()).apply {
                text = category
                isCheckable = true
                textSize = 14f
                setTextColor(Color.BLACK)
                setChipIconResource(icon)
                isChipIconVisible = true
                chipBackgroundColor = android.content.res.ColorStateList.valueOf(
                    ContextCompat.getColor(requireContext(), R.color.white)
                )

                // Axtarışlar chipinə click listener əlavə et
                if (category == "Axtarışlar") {
                    searchChip = this
                    setOnCheckedChangeListener { _, isChecked ->
                        binding.searchViewSelected.visibility = if (isChecked) View.VISIBLE else View.GONE

                        if (!isChecked) {
                            binding.searchViewSelected.setQuery("", false)
                            binding.searchViewSelected.clearFocus()
                            filterCars("")
                        }
                    }
                }
            }
            binding.chipCategoryInSelected.addView(chip)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
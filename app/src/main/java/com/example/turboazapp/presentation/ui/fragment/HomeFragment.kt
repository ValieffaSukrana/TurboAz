package com.example.turboazapp.presentation.ui.fragment

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import com.example.turboazapp.databinding.FragmentHomeBinding
import com.example.turboazapp.presentation.viewmodel.HomeViewModel
import com.example.turboazapp.presentation.ui.adapter.HomeAdapter
import com.example.turboazapp.presentation.viewmodel.SelectedViewModel
import com.example.turboazapp.util.Resource
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    companion object {
        private const val TAG = "HomeFragment"
    }

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: HomeAdapter
    private val viewModel: HomeViewModel by viewModels()
    private val selectedViewModel: SelectedViewModel by viewModels()

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume called - refreshing cars")

        (requireActivity() as MainActivity).setToolbarVisible(true)
        (requireActivity() as MainActivity).setBottomNavVisible(true)

        // ✅ Her defe geri donende yenile
        viewModel.loadCars()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupChips()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        adapter = HomeAdapter(
            onItemClick = { car ->
                navigateToCarDetails(car.id)
            },
            onFavoriteClick = { car, isFavorite ->
                viewModel.toggleFavorite(car, isFavorite)
            }
        )

        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = this@HomeFragment.adapter
        }
    }

    private fun navigateToCarDetails(carId: String) {
        try {
            val bundle = bundleOf("carId" to carId)
            findNavController().navigate(
                R.id.action_homeFragment_to_carDetailsFragment,
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
        // Cars state observe
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.carsState.collect { resource ->
                    when (resource) {
                        is Resource.Loading -> {
                            Log.d(TAG, "Cars loading...")
                        }

                        is Resource.Success -> {
                            resource.data?.let { cars ->
                                Log.d(TAG, "Updating adapter with ${cars.size} cars")
                                adapter.updateList(cars)
                            }
                        }

                        is Resource.Error -> {
                            Toast.makeText(
                                requireContext(),
                                "Xəta: ${resource.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }

        // Favorite state observe
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.favoriteState.collect { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            Log.d(TAG, "Favorite updated successfully")
                            selectedViewModel.loadFavoriteCars()
                        }

                        is Resource.Error -> {
                            Toast.makeText(
                                requireContext(),
                                resource.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        else -> {}
                    }
                }
            }
        }
    }

    private fun setupChips() {
        val categories_home = listOf(
            "Ən çox baxılanlar" to R.drawable.flame,
            "Kalkulyator" to R.drawable.calculator,
            "Avtokataloq" to R.drawable.catalog
        )

        for ((category, icon) in categories_home) {
            val chip = Chip(requireContext()).apply {
                text = category
                isCheckable = true
                textSize = 13f
                setTextColor(Color.BLACK)
                setChipIconResource(icon)
                isChipIconVisible = true
                chipBackgroundColor = ColorStateList.valueOf(
                    ContextCompat.getColor(requireContext(), R.color.white)
                )
            }
            binding.chipCategory.addView(chip)
        }

        val categories2 = listOf(
            "Bu gün:\n1781 yeni elan" to R.drawable.bmw_icon,
            "Dilerlər" to R.drawable.lamborghini
        )

        val screenWidth = resources.displayMetrics.widthPixels
        val margin = (16 * resources.displayMetrics.density).toInt()
        val chipWidth = (screenWidth / categories2.size) - margin * 2

        for ((category, icon) in categories2) {
            val chip = Chip(requireContext()).apply {
                text = category
                isCheckable = true
                textSize = 14f
                setTextColor(Color.BLACK)
                setChipIconResource(icon)
                isChipIconVisible = true
                chipBackgroundColor = ColorStateList.valueOf(
                    ContextCompat.getColor(requireContext(), R.color.white)
                )
                layoutParams = ViewGroup.MarginLayoutParams(
                    chipWidth,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                ).apply {
                    marginEnd = margin
                    marginStart = margin
                }
                setPadding(16, 8, 16, 8)
            }
            binding.chipCategory.addView(chip)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
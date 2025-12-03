package com.example.turboazapp.presentation.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import com.example.turboazapp.presentation.ui.fragment.MainActivity
import com.example.turboazapp.presentation.ui.adapter.HomeAdapter
import com.example.turboazapp.presentation.viewmodel.HomeViewModel
import com.example.turboazapp.presentation.viewmodel.SelectedViewModel
import com.example.turboazapp.util.Resource
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

        (requireActivity() as? MainActivity)?.setToolbarVisible(true)
        (requireActivity() as? MainActivity)?.setBottomNavVisible(true)

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
        setupButtons()
        observeViewModel()
    }

    private fun setupButtons() {
        binding.apply {
            // Sətir 1: 3 düymə
            btnMostViewed.setOnClickListener {
                Toast.makeText(requireContext(), "Ən çox baxılan", Toast.LENGTH_SHORT).show()
            }

            btnCalculator.setOnClickListener {
                Toast.makeText(requireContext(), "Kalkulyator", Toast.LENGTH_SHORT).show()
            }

            btnCatalog.setOnClickListener {
                Toast.makeText(requireContext(), "Avtokataloq", Toast.LENGTH_SHORT).show()
            }

            // Sətir 2: 2 düymə
            btnToday.setOnClickListener {
                Toast.makeText(requireContext(), "Bu gün", Toast.LENGTH_SHORT).show()
            }

            btnDealers.setOnClickListener {
                Toast.makeText(requireContext(), "Dilerlər", Toast.LENGTH_SHORT).show()
            }
        }
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
            Toast.makeText(requireContext(), "Xəta: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.carsState.collect { resource ->
                    when (resource) {
                        is Resource.Loading -> Log.d(TAG, "Cars loading...")
                        is Resource.Success -> {
                            resource.data?.let { cars ->
                                adapter.updateList(cars)
                            }
                        }
                        is Resource.Error -> {
                            Toast.makeText(requireContext(), "Xəta: ${resource.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.favoriteState.collect { resource ->
                    if (resource is Resource.Success) {
                        selectedViewModel.loadFavoriteCars()
                    } else if (resource is Resource.Error) {
                        Toast.makeText(requireContext(), resource.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
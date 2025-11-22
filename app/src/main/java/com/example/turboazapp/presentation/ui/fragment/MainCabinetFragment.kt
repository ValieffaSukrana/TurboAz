package com.example.turboazapp.presentation.profile

import android.os.Bundle
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
import coil.load
import com.example.turboazapp.R
import com.example.turboazapp.databinding.FragmentMainCabinetBinding
import com.example.turboazapp.domain.model.User
import com.example.turboazapp.presentation.ui.adapter.HomeAdapter
import com.example.turboazapp.presentation.ui.fragment.MainActivity
import com.example.turboazapp.presentation.viewmodel.HomeViewModel
import com.example.turboazapp.presentation.viewmodel.ProfileViewModel
import com.example.turboazapp.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainCabinetFragment : Fragment() {

    private var _binding: FragmentMainCabinetBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProfileViewModel by viewModels()
    private val homeViewModel: HomeViewModel by viewModels()

    private var currentUser: User? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainCabinetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as MainActivity).setToolbarVisible(false)

        setupClickListeners()
        observeViewModel()

        viewModel.loadCurrentUser()
    }

    private fun setupClickListeners() {
        binding.btnSettings.setOnClickListener {
            Toast.makeText(requireContext(), "Parametrlər", Toast.LENGTH_SHORT).show()
        }

        binding.btnAddBalance.setOnClickListener {
            Toast.makeText(requireContext(), "Balans artırma funksiyası tezliklə", Toast.LENGTH_SHORT).show()
        }
    }

    private fun observeViewModel() {
        // İstifadəçi məlumatı
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.userState.collect { resource ->
                    when (resource) {
                        is Resource.Loading -> {
                            // Loading
                        }

                        is Resource.Success -> {
                            resource.data?.let { user ->
                                currentUser = user
                                bindUserData(user)
                            } ?: navigateToCabinet()
                        }

                        is Resource.Error -> {
                            Toast.makeText(requireContext(), "Xəta: ${resource.message}", Toast.LENGTH_SHORT).show()
                            navigateToCabinet()
                        }
                    }
                }
            }
        }

        // İstifadəçinin elanları
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.userCarsState.collect { resource ->
                    when (resource) {
                        is Resource.Loading -> {
                            // Loading
                        }

                        is Resource.Success -> {
                            val cars = resource.data ?: emptyList()

                            if (cars.isEmpty()) {
                                binding.emptySection.visibility = View.VISIBLE
                                binding.recyclerViewMyCars.visibility = View.GONE
                            } else {
                                binding.emptySection.visibility = View.GONE
                                binding.recyclerViewMyCars.visibility = View.VISIBLE
                                setupRecyclerView(cars)
                            }
                        }

                        is Resource.Error -> {
                            Toast.makeText(requireContext(), "Elanlar yüklənmədi", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    private fun bindUserData(user: User) {
        binding.apply {
            tvPhone.text = formatPhoneNumber(user.phone)

            profileImage.load(user.profileImage.ifEmpty { R.drawable.account_circle }) {
                crossfade(true)
                placeholder(R.drawable.account_circle)
                error(R.drawable.account_circle)
            }
        }
    }

    private fun setupRecyclerView(cars: List<com.example.turboazapp.domain.model.Car>) {
        val adapter = HomeAdapter(
            onItemClick = { car ->
                navigateToCarDetails(car.id)
            },
            onFavoriteClick = { car, isFavorite ->
                homeViewModel.toggleFavorite(car, isFavorite)
            }
        )

        binding.recyclerViewMyCars.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            this.adapter = adapter
        }

        adapter.updateList(cars)
    }

    private fun navigateToCarDetails(carId: String) {
        try {
            val bundle = bundleOf("carId" to carId)
            findNavController().navigate(
                R.id.action_mainCabinetFragment_to_carDetailsFragment,
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

    private fun formatPhoneNumber(phone: String): String {
        val cleaned = phone.replace("+994", "")
        return if (cleaned.length == 9) {
            "(${cleaned.substring(0, 2)}) ${cleaned.substring(2, 5)}-${cleaned.substring(5, 7)}-${cleaned.substring(7,9)}"
        } else {
            phone
        }
    }

    private fun navigateToCabinet() {
        findNavController().navigate(R.id.action_mainCabinetFragment_to_cabinetFragment)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
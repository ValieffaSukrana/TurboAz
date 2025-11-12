package com.example.turboazapp.presentation.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.turboazapp.R
import com.example.turboazapp.databinding.FragmentMainCabinetBinding
import com.example.turboazapp.domain.model.User
import com.example.turboazapp.presentation.ui.fragment.MainActivity
import com.example.turboazapp.presentation.viewmodel.ProfileViewModel
import com.example.turboazapp.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainCabinetFragment : Fragment() {

    private var _binding: FragmentMainCabinetBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProfileViewModel by viewModels()

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

        // Cari istifadəçini yüklə
        viewModel.loadCurrentUser()
    }

    private fun setupClickListeners() {
        binding.btnSettings.setOnClickListener {
            // Settings səhifəsinə keç
            Toast.makeText(requireContext(), "Parametrlər", Toast.LENGTH_SHORT).show()
        }

        binding.btnAddBalance.setOnClickListener {
            Toast.makeText(
                requireContext(),
                "Balans artırma funksiyası tezliklə",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.userState.collect { resource ->
                    when (resource) {
                        is Resource.Loading -> {
                            // Loading göstər
                        }

                        is Resource.Success -> {
                            resource.data?.let { user ->
                                currentUser = user
                                bindUserData(user)
                            } ?: run {
                                // İstifadəçi giriş etməyib - Login-ə göndər
                                navigateToCabinet()
                            }
                        }

                        is Resource.Error -> {
                            Toast.makeText(
                                requireContext(),
                                "Xəta: ${resource.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                            navigateToCabinet()
                        }
                    }
                }
            }
        }
    }

    private fun bindUserData(user: User) {
        binding.apply {
            // Telefon nömrəsini göstər
            tvPhone.text = formatPhoneNumber(user.phone)

            // Profil şəklini yüklə
            profileImage.load(user.profileImage.ifEmpty { R.drawable.account_circle }) {
                crossfade(true)
                placeholder(R.drawable.account_circle)
                error(R.drawable.account_circle)
            }

            // Mənim elanlarımı yüklə
            // TODO: Load user's cars
        }
    }

    private fun formatPhoneNumber(phone: String): String {
        // +994501234567 → (050) 123-45-67
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


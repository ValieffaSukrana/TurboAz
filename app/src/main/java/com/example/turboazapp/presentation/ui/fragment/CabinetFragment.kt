package com.example.turboazapp.presentation.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.turboazapp.R
import com.example.turboazapp.databinding.FragmentCabinetBinding
import com.example.turboazapp.presentation.ui.fragment.MainActivity
import com.example.turboazapp.presentation.viewmodel.AuthViewModel
import com.example.turboazapp.presentation.viewmodel.VerificationState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CabinetFragment : Fragment() {

    private var _binding: FragmentCabinetBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCabinetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as? MainActivity)?.setBottomNavVisible(false)
        (requireActivity() as MainActivity).setToolbarVisible(false)

        setupUI()
        setupClickListeners()
        observeViewModel()
    }

    private fun setupUI() {
        // Telefon nömrəsi formatı
        binding.phoneNumber.editText?.addTextChangedListener { text ->
            val phone = text.toString()

            // Avtomatik +994 əlavə et
            if (phone.isEmpty()) {
                binding.phoneNumber.editText?.setText("+994")
                binding.phoneNumber.editText?.setSelection(4)
            }

            // Düymə aktivləşdirmə
            val isValid = isPhoneNumberValid(phone)
            binding.myButton.isEnabled = isValid
        }

        // Default olaraq +994 göstər
        if (binding.phoneNumber.editText?.text.isNullOrEmpty()) {
            binding.phoneNumber.editText?.setText("+994")
            binding.phoneNumber.editText?.setSelection(4)
        }

        binding.myButton.isEnabled = false
    }

    private fun setupClickListeners() {
        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.myButton.setOnClickListener {
            val phoneNumber = binding.phoneNumber.editText?.text.toString().trim()

            if (isPhoneNumberValid(phoneNumber)) {
                sendVerificationCode(phoneNumber)
            } else {
                Toast.makeText(
                    requireContext(),
                    "Düzgün telefon nömrəsi daxil edin",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun sendVerificationCode(phoneNumber: String) {
        binding.myButton.isEnabled = false
        binding.myButton.text = "Göndərilir..."

        viewModel.sendVerificationCode(phoneNumber, requireActivity())
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.verificationState.collect { state ->
                    when (state) {
                        is VerificationState.Idle -> {
                            binding.myButton.isEnabled = true
                            binding.myButton.text = getString(R.string.sms_kod_g_nd_rilsin)
                        }

                        is VerificationState.Loading -> {
                            binding.myButton.isEnabled = false
                            binding.myButton.text = "Göndərilir..."
                        }

                        is VerificationState.CodeSent -> {
                            binding.myButton.isEnabled = true
                            binding.myButton.text = getString(R.string.sms_kod_g_nd_rilsin)

                            Toast.makeText(
                                requireContext(),
                                "SMS kodu göndərildi",
                                Toast.LENGTH_SHORT
                            ).show()

                            // VerifyCodeFragment-ə keç
                            navigateToVerifyCode(
                                state.verificationId,
                                binding.phoneNumber.editText?.text.toString()
                            )
                        }

                        is VerificationState.AutoVerified -> {
                            Toast.makeText(
                                requireContext(),
                                "Avtomatik təsdiqləndi",
                                Toast.LENGTH_SHORT
                            ).show()

                            navigateToMainCabinet()
                        }

                        is VerificationState.Error -> {
                            binding.myButton.isEnabled = true
                            binding.myButton.text = getString(R.string.sms_kod_g_nd_rilsin)

                            Toast.makeText(
                                requireContext(),
                                "Xəta: ${state.message}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }
        }
    }

    private fun isPhoneNumberValid(phone: String): Boolean {
        // +994XXXXXXXXX formatı (13 simvol)
        val cleanPhone = phone.replace(" ", "").replace("-", "")
        return cleanPhone.startsWith("+994") && cleanPhone.length == 13
    }

    private fun navigateToVerifyCode(verificationId: String, phoneNumber: String) {
        val bundle = Bundle().apply {
            putString("verificationId", verificationId)
            putString("phoneNumber", phoneNumber)
        }
        findNavController().navigate(
            R.id.action_cabinetFragment_to_verifyCodeFragment,
            bundle
        )
    }

    private fun navigateToMainCabinet() {
        val navOptions = NavOptions.Builder()
            .setPopUpTo(R.id.cabinetFragment, true)
            .build()

        findNavController().navigate(
            R.id.action_cabinetFragment_to_mainCabinetFragment,
            null,
            navOptions
        )

    }
    override fun onDestroyView() {
        super.onDestroyView()
        (requireActivity() as? MainActivity)?.setBottomNavVisible(true)
        _binding = null
    }
}
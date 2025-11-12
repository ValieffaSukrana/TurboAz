package com.example.turboazapp.presentation.auth

import android.os.Bundle
import android.os.CountDownTimer
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
import com.example.turboazapp.databinding.FragmentVerifyCodeBinding
import com.example.turboazapp.presentation.ui.fragment.MainActivity
import com.example.turboazapp.presentation.viewmodel.AuthViewModel
import com.example.turboazapp.presentation.viewmodel.VerificationState
import com.example.turboazapp.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class VerifyCodeFragment : Fragment() {

    private var _binding: FragmentVerifyCodeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AuthViewModel by viewModels()

    private var verificationId: String = ""
    private var phoneNumber: String = ""

    private var countDownTimer: CountDownTimer? = null
    private var canResend = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            verificationId = it.getString("verificationId", "")
            phoneNumber = it.getString("phoneNumber", "")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVerifyCodeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as? MainActivity)?.setBottomNavVisible(false)

        setupUI()
        setupClickListeners()
        observeViewModel()
        startResendTimer()
    }

    private fun setupUI() {
        // Telefon nömrəsini format et və göstər
        binding.tvDescription.text =
            "${formatPhoneNumber(phoneNumber)} nömrəsinə SMS-kod göndərildi"

        // Auto-focus və kod daxil edilməsi
        binding.etSmsCode.addTextChangedListener { text ->
            val code = text.toString()

            // 6 rəqəm daxil edildikdə avtomatik verify et
            if (code.length == 6) {
                verifyCode(code)
            }
        }

        // Fokus ver
        binding.etSmsCode.requestFocus()
    }

    private fun formatPhoneNumber(phone: String): String {
        // +994501234567 → (050) 123-45-67
        val cleaned = phone.replace("+994", "").trim()
        val formatted = if (cleaned.length == 9) {
            val numberWithZero = "0$cleaned"
            "(${numberWithZero.substring(0, 3)}) ${
                numberWithZero.substring(
                    3,
                    6
                )
            }-${numberWithZero.substring(6, 8)}-${numberWithZero.substring(8)}"
        } else {
            phone
        }
        return formatted
    }


    private fun setupClickListeners() {
        binding.btnClose.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.tvResend.setOnClickListener {
            if (canResend) {
                resendCode()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Bir az gözləyin",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun verifyCode(code: String) {
        hideKeyboard()

        android.util.Log.d("VerifyCode", "=== VERIFY ===")
        android.util.Log.d("VerifyCode", "Code: $code")
        android.util.Log.d("VerifyCode", "Fragment VerificationId: $verificationId")
        android.util.Log.d("VerifyCode", "ViewModel VerificationId: ${viewModel.verificationId}")

        // ✅ Əgər Fragment-dəki ID varsa, onu göndər
        if (verificationId.isNotEmpty()) {
            viewModel.verifyCodeWithId(verificationId, code)
        }
        // ✅ Yoxdursa ViewModel-dəkini istifadə et
        else if (viewModel.verificationId != null) {
            viewModel.verifyCode(code)
        }
        // ❌ Heç biri yoxdursa xəta
        else {
            Toast.makeText(
                requireContext(),
                "Xəta: Doğrulama ID-si tapılmadı",
                Toast.LENGTH_LONG
            ).show()
            findNavController().navigateUp()
        }
    }

    private fun resendCode() {
        viewModel.sendVerificationCode(phoneNumber, requireActivity())
        startResendTimer()

        Toast.makeText(
            requireContext(),
            "Yeni kod göndərilir...",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun startResendTimer() {
        canResend = false
        binding.tvResend.isEnabled = false
        binding.tvResend.text = "SMS-kod yenidən göndərilsin (60s)"

        countDownTimer?.cancel()
        countDownTimer = object : CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val seconds = millisUntilFinished / 1000
                binding.tvResend.text = "SMS-kod yenidən göndərilsin (${seconds}s)"
            }

            override fun onFinish() {
                canResend = true
                binding.tvResend.isEnabled = true
                binding.tvResend.text = "SMS-kod yenidən göndərilsin"
            }
        }.start()
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.authState.collect { resource ->
                    when (resource) {
                        is Resource.Loading -> {
                            binding.etSmsCode.isEnabled = false
                        }

                        is Resource.Success -> {
                            binding.etSmsCode.isEnabled = true

                            resource.data?.let { user ->
                                Toast.makeText(
                                    requireContext(),
                                    "Xoş gəldiniz, ${user.name.ifEmpty { user.phone }}!",
                                    Toast.LENGTH_SHORT
                                ).show()

                                navigateToMainCabinet()
                            }
                        }

                        is Resource.Error -> {
                            binding.etSmsCode.isEnabled = true
                            binding.etSmsCode.text?.clear()

                            Toast.makeText(
                                requireContext(),
                                "Kod yanlışdır. Yenidən cəhd edin.",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                        else -> {
                            binding.etSmsCode.isEnabled = true
                        }
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.verificationState.collect { state ->
                    when (state) {
                        is VerificationState.CodeSent -> {
                            android.util.Log.d(
                                "VerifyCode",
                                "NEW VerificationId: ${state.verificationId}"
                            )
                            android.util.Log.d(
                                "VerifyCode",
                                "ViewModel VerificationId: ${viewModel.verificationId}"
                            )

                            Toast.makeText(
                                requireContext(),
                                "Yeni kod göndərildi",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        is VerificationState.Error -> {
                            Toast.makeText(
                                requireContext(),
                                "Xəta: ${state.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        else -> {}
                    }
                }
            }
        }
    }

    private fun navigateToMainCabinet() {
        val navOptions = NavOptions.Builder()
            .setPopUpTo(R.id.cabinetFragment, true)
            .build()

        findNavController().navigate(
            R.id.action_verifyCodeFragment_to_mainCabinetFragment,
            null,
            navOptions
        )
    }

    private fun hideKeyboard() {
        val imm = requireContext().getSystemService(android.content.Context.INPUT_METHOD_SERVICE)
                as android.view.inputmethod.InputMethodManager
        imm.hideSoftInputFromWindow(binding.etSmsCode.windowToken, 0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        countDownTimer?.cancel()
        (requireActivity() as? MainActivity)?.setBottomNavVisible(true)
        _binding = null
    }
}
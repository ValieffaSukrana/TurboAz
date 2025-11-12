package com.example.turboazapp.presentation.ui.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.turboazapp.R
import com.example.turboazapp.databinding.FragmentOtherBinding
import com.example.turboazapp.presentation.ui.LanguageHelper
import com.example.turboazapp.presentation.viewmodel.ProfileViewModel
import com.example.turboazapp.util.Resource
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OtherFragment : Fragment() {

    private var _binding: FragmentOtherBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProfileViewModel by viewModels()

    override fun onResume() {
        super.onResume()
        (requireActivity() as MainActivity).setToolbarVisible(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOtherBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupClickListeners()
        observeLogoutState()
        updateDarkModeIcon()
    }

    private fun setupClickListeners() {
        // Dil dəyişmə
        binding.changeLanguage.setOnClickListener {
            showLanguageDialog()
        }

        // Dark Mode toggle
        binding.darkModeLayout.setOnClickListener {
            toggleDarkMode()
        }

        // Çıxış
        binding.logoutLayout.setOnClickListener {
            showLogoutDialog()
        }
        binding.aboutAppLayout.setOnClickListener {
            findNavController().navigate(R.id.aboutAppFragment)
        }

    }

    private fun showLanguageDialog() {
        val currentLang = LanguageHelper.getSavedLanguage(requireContext())

        val languages = arrayOf("Azərbaycanca", "Русский")
        val languageCodes = arrayOf("az", "ru")

        val currentIndex = languageCodes.indexOf(currentLang)

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Dil seçin / Выберите язык")
            .setSingleChoiceItems(languages, currentIndex) { dialog, which ->
                val newLang = languageCodes[which]

                if (newLang != currentLang) {
                    LanguageHelper.saveLanguage(requireContext(), newLang)
                    LanguageHelper.setAppLocale(requireContext(), newLang)

                    Log.d("LANG_TEST", "Current: $currentLang, New: $newLang")

                    Toast.makeText(
                        requireContext(),
                        if (newLang == "az") "Dil dəyişdirildi" else "Язык изменен",
                        Toast.LENGTH_SHORT
                    ).show()

                    dialog.dismiss()
                    requireActivity().recreate()
                } else {
                    dialog.dismiss()
                }
            }
            .setNegativeButton("Ləğv et / Отмена") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun toggleDarkMode() {
        val currentStatus = getDarkModeStatus()
        val newStatus = !currentStatus

        saveDarkModeStatus(newStatus)
        applyDarkMode(newStatus)
        updateDarkModeIcon()

        Toast.makeText(
            requireContext(),
            if (newStatus) "Qaranlıq rejim aktivdir" else "İşıqlı rejim aktivdir",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun updateDarkModeIcon() {
        val isDarkMode = getDarkModeStatus()

        // Icon dəyişdir
        val iconRes = if (isDarkMode) {
            R.drawable.sun // qaranlıq rejim üçün ay ikonu
        } else {
            R.drawable.sun // işıqlı rejim üçün günəş ikonu
        }

        binding.darkModeIcon.setImageResource(iconRes)
    }

    private fun showLogoutDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Çıxış")
            .setMessage("Hesabdan çıxmaq istədiyinizdən əminsiniz?")
            .setPositiveButton("Bəli") { dialog, _ ->
                viewModel.logout()
                dialog.dismiss()
            }
            .setNegativeButton("Xeyr") { dialog, _ ->
                dialog.dismiss()
            }
            .setCancelable(true)
            .show()
    }

    private fun observeLogoutState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.logoutState.collect { resource ->
                    when (resource) {
                        is Resource.Loading -> {
                            // Loading state
                            binding.logoutLayout.isEnabled = false
                            binding.logoutLayout.alpha = 0.5f
                        }

                        is Resource.Success -> {
                            binding.logoutLayout.isEnabled = true
                            binding.logoutLayout.alpha = 1.0f

                            Toast.makeText(
                                requireContext(),
                                "Çıxış edildi",
                                Toast.LENGTH_SHORT
                            ).show()

                            navigateToLogin()
                            viewModel.resetLogoutState()
                        }

                        is Resource.Error -> {
                            binding.logoutLayout.isEnabled = true
                            binding.logoutLayout.alpha = 1.0f

                            Toast.makeText(
                                requireContext(),
                                "Xəta: ${resource.message}",
                                Toast.LENGTH_LONG
                            ).show()

                            viewModel.resetLogoutState()
                        }

                        null -> {
                            binding.logoutLayout.isEnabled = true
                            binding.logoutLayout.alpha = 1.0f
                        }
                    }
                }
            }
        }
    }

    private fun navigateToLogin() {
        try {
            // Bütün back stack-i təmizləyib login səhifəsinə yönləndir
            val navOptions = NavOptions.Builder()
                .setPopUpTo(R.id.nav_graph, true) // və ya findNavController().graph.id
                .setLaunchSingleTop(true)
                .build()

            findNavController().navigate(
                R.id.homeFragment, // Login fragment ID
                null,
                navOptions
            )
        } catch (e: Exception) {
            Log.e("OtherFragment", "Navigation error", e)

            // Alternativ: MainActivity-ni restart et
            requireActivity().finish()
            startActivity(requireActivity().intent)
        }
    }

    private fun getDarkModeStatus(): Boolean {
        val sharedPref =
            requireActivity().getSharedPreferences("app_settings", Context.MODE_PRIVATE)
        return sharedPref.getBoolean("dark_mode", false)
    }

    private fun saveDarkModeStatus(enabled: Boolean) {
        val sharedPref =
            requireActivity().getSharedPreferences("app_settings", Context.MODE_PRIVATE)
        sharedPref.edit().putBoolean("dark_mode", enabled).apply()
    }

    private fun applyDarkMode(enabled: Boolean) {
        if (enabled) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
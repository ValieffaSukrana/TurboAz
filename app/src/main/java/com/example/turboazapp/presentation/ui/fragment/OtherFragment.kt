package com.example.turboazapp.presentation.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.turboazapp.databinding.FragmentOtherBinding
import com.example.turboazapp.presentation.ui.LanguageHelper

class OtherFragment : Fragment() {

    private var _binding: FragmentOtherBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOtherBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.changeLanguage.setOnClickListener {
            val currentLang = LanguageHelper.getSavedLanguage(requireContext())
            val newLang = if (currentLang == "ru") "az" else "ru"

            LanguageHelper.saveLanguage(requireContext(), newLang)
            LanguageHelper.setAppLocale(requireContext(), newLang)
            Log.d("LANG_TEST", "Current language: $currentLang, New language: $newLang")
            requireActivity().recreate()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

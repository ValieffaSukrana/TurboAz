package com.example.turboazapp.presentation.ui.fragment

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.turboazapp.databinding.FragmentNewAnnouncement6Binding
import com.google.android.material.chip.Chip

class NewAnnouncementFragment6 : Fragment() {
    private var _binding: FragmentNewAnnouncement6Binding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNewAnnouncement6Binding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.backButton.setOnClickListener {
            requireActivity().onBackPressed()
        }
        binding.cancelButton.setOnClickListener {
            requireActivity().onBackPressed()
        }
        val categoriesEngine = listOf("Benzin", "Dizel", "Elektrik", "Hibrid")
        for (category in categoriesEngine) {
            val chip = Chip(requireContext()).apply {
                text = category
                isCheckable = true
                textSize = 14f
                setTextColor(Color.BLACK)
            }
            binding.chipEngine.addView(chip)
        }
    }
}
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
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.turboazapp.R
import com.example.turboazapp.databinding.FragmentHomeBinding
import com.example.turboazapp.presentation.ui.adapter.HomeAdapter
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: HomeAdapter


    override fun onResume() {
        super.onResume()
        (requireActivity() as MainActivity).setToolbarVisible(true)
        (requireActivity() as MainActivity).setBottomNavVisible(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 🔹 RecyclerView setup
        adapter = HomeAdapter()
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerView.adapter = adapter

        setupChips()
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


}

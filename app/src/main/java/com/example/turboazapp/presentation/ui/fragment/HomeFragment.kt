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
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.turboazapp.R
import com.example.turboazapp.data.di.RetrofitModule
import com.example.turboazapp.data.mapper.MakeImageMapper
import com.example.turboazapp.databinding.FragmentHomeBinding
import com.example.turboazapp.presentation.ui.adapter.HomeAdapter
import com.example.turboazapp.presentation.ui.model.MakeWithImage
import com.example.turboazapp.presentation.viewmodel.CarsViewModel
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: HomeAdapter
    private val viewModel: CarsViewModel by viewModels()

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

        // Seed dummy cars
        binding.image1.setOnClickListener {
            viewModel.seedDummyCars(
                onDone = {
                    Toast.makeText(requireContext(), "Seed OK", Toast.LENGTH_SHORT).show()
                },
                onError = { e ->
                    Toast.makeText(requireContext(), e.message ?: "Xəta", Toast.LENGTH_SHORT).show()
                }
            )
        }

        // RecyclerView setup - boş başlatırıq, API-dən data gələndə dolacaq
        adapter = HomeAdapter()
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerView.adapter = adapter

        // Chips
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

        // Fetch car makes from CarQuery API
        fetchCarMakes()
    }

    private fun fetchCarMakes() {
        lifecycleScope.launch {
            try {
                val response = RetrofitModule.api.getMakes()
                if (response.isSuccessful) {
                    val makes = response.body()?.Makes

                    // API-dən gələn markaları MakeWithImage modelinə çevir
                    val makeList = makes?.map { make ->
                        MakeWithImage(
                            name = make.make_display,
                            imageRes = MakeImageMapper.getImageForMake(make.make_display)
                        )
                    } ?: emptyList()

                    // RecyclerView-ı yenilə
                    adapter.updateList(makeList)

                    Log.d("CarMake", "Total makes loaded: ${makeList.size}")
                } else {
                    Log.e("API_ERROR", response.errorBody()?.string() ?: "Unknown error")
                    Toast.makeText(requireContext(), "API xətası", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("API_EXCEPTION", e.message ?: "Exception")
                Toast.makeText(requireContext(), "Şəbəkə xətası: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
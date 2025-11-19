package com.example.turboazapp.presentation.ui.fragment.announcement


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.turboazapp.AddCarActivity
import com.example.turboazapp.databinding.FragmentSelectColorBinding
import com.example.turboazapp.presentation.ui.adapter.ColorAdapter
import com.example.turboazapp.presentation.viewmodel.AddCarViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SelectColorFragment : Fragment() {

    private var _binding: FragmentSelectColorBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AddCarViewModel by activityViewModels()
    private lateinit var colorAdapter: ColorAdapter

    private val colors = listOf(
        ColorItem("Ağ", "#FFFFFF"),
        ColorItem("Qara", "#000000"),
        ColorItem("Boz", "#808080"),
        ColorItem("Gümüşü", "#C0C0C0"),
        ColorItem("Qırmızı", "#FF0000"),
        ColorItem("Mavi", "#0000FF"),
        ColorItem("Göy", "#87CEEB"),
        ColorItem("Yaşıl", "#008000"),
        ColorItem("Sarı", "#FFFF00"),
        ColorItem("Narıncı", "#FFA500"),
        ColorItem("Qəhvəyi", "#8B4513"),
        ColorItem("Bənövşəyi", "#800080"),
        ColorItem("Çəhrayı", "#FFC0CB"),
        ColorItem("Qızılı", "#FFD700"),
        ColorItem("Bej", "#F5F5DC"),
        ColorItem("Bordeaux", "#800020")
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSelectColorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        setupRecyclerView()
    }

    private fun setupToolbar() {
        lifecycleScope.launch {
            viewModel.selectedBrand.collect { brand ->
                binding.titleText.text = brand ?: "Rəng"
            }
        }

        binding.subtitleText.text = "16-dan addım 5"

        binding.backButton.setOnClickListener {
            (activity as? AddCarActivity)?.navigateBack()
        }

        binding.cancelButton.setOnClickListener {
            activity?.finish()
        }
    }

    private fun setupRecyclerView() {
        colorAdapter = ColorAdapter(colors) { selectedColor ->
            viewModel.selectColor(selectedColor.name)
            (activity as? AddCarActivity)?.navigateToFragment(SelectEngineFragment())
        }

        binding.recyclerViewColors.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = colorAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

data class ColorItem(val name: String, val hexCode: String)
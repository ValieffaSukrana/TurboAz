package com.example.turboazapp.presentation.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.turboazapp.presentation.ui.adapter.CarsBrandAdapter
import com.example.turboazapp.presentation.ui.adapter.ColorAdapter
import com.example.turboazapp.data.local.ColorModel
import com.example.turboazapp.databinding.FragmentNewAnnouncement5Binding

class NewAnnouncementFragment5 : Fragment() {
    private lateinit var adapter: CarsBrandAdapter
    private lateinit var colors: MutableList<ColorModel>
    private var _binding: FragmentNewAnnouncement5Binding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onResume() {
        super.onResume()
        (requireActivity() as MainActivity).setToolbarVisible(false)
        (requireActivity() as MainActivity).setBottomNavVisible(false)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNewAnnouncement5Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        colors= mutableListOf(
            ColorModel("Qara", "#000000"),
            ColorModel("Yaş Asfalt", "#555555"),
            ColorModel("Boz", "#888888"),
            ColorModel("Gümüşü", "#CCCCCC"),
            ColorModel("Ağ", "#FFFFFF"),
            ColorModel("Bej", "#FFFFCC"),
            ColorModel("Tünd qırmızı", "#990000"),
            ColorModel("Qırmızı", "#FF0000"),
            ColorModel("Çəhrayı", "#FFC0CB"),
            ColorModel("Narıncı", "#FFA500"),
            ColorModel("Qızılı", "#FFD700"),
            ColorModel("Sarı", "#FFFF00"),
            ColorModel("Yaşıl", "#00FF00"),
            ColorModel("Açıq yaşıl", "#B0C4B0"),
            ColorModel("Mavi", "#3399FF")
        )
        val adapter = ColorAdapter(colors) { selectedColor ->
            // Handle color selection here
        }
        binding.recyclerViewColors.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.recyclerViewColors.adapter = adapter


    }

}
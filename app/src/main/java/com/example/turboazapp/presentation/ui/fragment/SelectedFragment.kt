package com.example.turboazapp.presentation.ui.fragment

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.turboazapp.R
import com.example.turboazapp.databinding.FragmentSelectedBinding
import com.example.turboazapp.presentation.ui.adapter.SelectedAdapter
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SelectedFragment : Fragment() {
//    private var binding: FragmentSelectedBinding? = null
//    private lateinit var adapter: SelectedAdapter
//
//    override fun onResume() {
//        super.onResume()
//        (requireActivity() as MainActivity).setToolbarVisible(false)
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_selected, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentSelectedBinding.bind(view)

        //        this.binding = binding
//
//        setupChips(binding)
//        setupRecyclerView(binding)
//    }
//
//    private fun setupChips(binding: FragmentSelectedBinding) {
//        val categoriesSelected = listOf(
//            "Elanlar" to R.drawable.notepad,
//            "Axtarışlar" to R.drawable.search,
//            "Müqayisə" to R.drawable.compare
//        )
//
//        for ((category, icon) in categoriesSelected) {
//            val chip = Chip(requireContext()).apply {
//                text = category
//                isCheckable = true
//                textSize = 14f
//                setTextColor(Color.BLACK)
//                setChipIconResource(icon)
//                isChipIconVisible = true
//            }
//            binding.chipCategoryInSelected.addView(chip)
//        }
//    }
//
//    private fun setupRecyclerView(binding: FragmentSelectedBinding) {
//        adapter = SelectedAdapter()
//        binding.SelectedRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
//        binding.SelectedRecyclerView.adapter = adapter
//    }
//
//
//
        override fun onDestroyView() {
            super.onDestroyView()
            binding = null
        }
    }
    }

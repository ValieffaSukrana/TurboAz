package com.example.turboazapp.presentation.ui.fragment

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.turboazapp.R
import com.example.turboazapp.presentation.ui.adapter.SelectedAdapter
import com.example.turboazapp.databinding.FragmentSelectedBinding
import com.google.android.material.chip.Chip

class SelectedFragment : Fragment() {
    private var binding: FragmentSelectedBinding? = null
    private lateinit var adapter: SelectedAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onResume() {
        super.onResume()
        (requireActivity() as MainActivity).setToolbarVisible(false)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_selected, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentSelectedBinding.bind(view)
        this.binding = binding
        val categories_selected = listOf("Elanlar" to R.drawable.notepad
            , "Axtarışlar" to R.drawable.search
            , "Müqayisə" to R.drawable.compare
        )
        for ((category, icon) in categories_selected) {
            val chip = Chip(requireContext()).apply {
                text = category
                isCheckable = true
                textSize = 14f
                setTextColor(Color.BLACK)
                setChipIconResource(icon)
                isChipIconVisible = true
            }
            binding.chipCategoryInSelected.addView(chip)
        }
        val selectedList = listOf("Item 1", "Item 2", "Item 3", "Item 4", "Item 5", "Item 6", "Item 7", "Item 8", "Item 9", "Item 10")

        adapter = SelectedAdapter(selectedList)

        binding.SelectedRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.SelectedRecyclerView.adapter = adapter
    }

}

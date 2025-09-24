package com.example.turboazapp.presentation.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.turboazapp.presentation.ui.adapter.CarsModelAdapter
import com.example.turboazapp.databinding.FragmentModelListBinding


class ModelListFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return FragmentModelListBinding.inflate(inflater, container, false).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val host = requireParentFragment() as AddListingHostFragment
        val brand = host.getSelectedBrand() ?: return
        val binding = FragmentModelListBinding.bind(view)
        binding.recyclerViewModels.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewModels.adapter = CarsModelAdapter(brand.models) { model ->
            val model = brand.models.first { it.name == model.name }
            host.onModelSelected(model)
        }
    }


}
package com.example.turboazapp.presentation.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.navigateUp
import com.example.turboazapp.data.local.Brand
import com.example.turboazapp.data.local.Model
import com.example.turboazapp.R


class AddListingHostFragment : Fragment() {
    private var selectedBrand: Brand? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_listing_host, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState == null) {
            childFragmentManager.beginTransaction()
                .replace(R.id.add_host_container, BrandListFragment()).commit()
        }
    }

    fun onBrandSelected(brand: Brand) {
        selectedBrand = brand
        childFragmentManager.beginTransaction().replace(
            R.id.add_host_container,
            ModelListFragment()
        ).addToBackStack(null).commit()


    }

    fun onModelSelected(model: Model) {
        val brand = selectedBrand ?: return
        parentFragmentManager.setFragmentResult(
            "add_listing_result",
            bundleOf(
                "brandId" to brand.id,
                "brandName" to brand.name,
                "modelId" to model.id,
                "modelName" to model.name
            )
        )
        parentFragmentManager.popBackStack()
    }

    fun getSelectedBrand(): Brand? = selectedBrand

}
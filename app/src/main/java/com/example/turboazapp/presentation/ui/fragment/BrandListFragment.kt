package com.example.turboazapp.presentation.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.turboazapp.data.local.CarData
import com.example.turboazapp.presentation.ui.adapter.CarsBrandAdapter
import com.example.turboazapp.databinding.FragmentBrandListBinding


class BrandListFragment : Fragment() {
    private val brands = CarData.brands()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentBrandListBinding.inflate(inflater, container, false).root
    }


    override fun onResume() {
        super.onResume()
        (requireActivity() as MainActivity).setToolbarVisible(false)
        (requireActivity() as MainActivity).setBottomNavVisible(false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
       val binding = FragmentBrandListBinding.bind(view)
        binding.recyclerViewBrands.layoutManager=LinearLayoutManager(requireContext())
        binding.recyclerViewBrands.adapter= CarsBrandAdapter(brands){
            brandName-> val brand=brands.first{it.name==brandName.name}
            ((requireParentFragment() as AddListingHostFragment).onBrandSelected(brand))
        }

    }
}


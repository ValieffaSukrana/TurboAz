package com.example.turboazapp.presentation.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.turboazapp.presentation.ui.adapter.CarsBrandAdapter
import com.example.turboazapp.databinding.FragmentBrandListBinding


class BrandListFragment : Fragment() {

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

    }
}


package com.example.turboazapp.presentation.ui.fragment.announcement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.turboazapp.databinding.FragmentSelectBodyTypeBinding
import com.example.turboazapp.presentation.ui.adapter.BodyTypeAdapter
import com.example.turboazapp.presentation.viewmodel.AddCarViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SelectBodyTypeFragment : Fragment() {

    private var _binding: FragmentSelectBodyTypeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AddCarViewModel by activityViewModels()
    private lateinit var bodyTypeAdapter: BodyTypeAdapter

    private val bodyTypes = listOf(
        "Sedan",
        "Hetçbek",
        "Universal",
        "Offroad / SUV",
        "Kupe",
        "Minivan",
        "Pikap",
        "Liftbek",
        "Kabriolet",
        "Furqon",
        "Rodster",
        "Limuzin"
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSelectBodyTypeBinding.inflate(inflater, container, false)
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
                binding.titleText.text = brand ?: "Ban növü"
            }
        }

        binding.subtitleText.text = "16-dan addım 4"

        binding.backButton.setOnClickListener {
            (activity as? AddCarActivity)?.navigateBack()
        }

        binding.cancelButton.setOnClickListener {
            activity?.finish()
        }
    }

    private fun setupRecyclerView() {
        bodyTypeAdapter = BodyTypeAdapter(bodyTypes) { selectedBodyType ->
            viewModel.selectBodyType(selectedBodyType)
            (activity as? AddCarActivity)?.navigateToFragment(SelectColorFragment())
        }

        binding.recyclerViewYear.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = bodyTypeAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
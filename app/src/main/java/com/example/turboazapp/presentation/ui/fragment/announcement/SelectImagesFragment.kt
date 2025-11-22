package com.example.turboazapp.presentation.ui.fragment.announcement

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.turboazapp.databinding.FragmentSelectImageBinding
import com.example.turboazapp.presentation.ui.adapter.ImageAdapter
import com.example.turboazapp.presentation.viewmodel.AddCarViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SelectImagesFragment : Fragment() {

    private var _binding: FragmentSelectImageBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AddCarViewModel by activityViewModels()

    private lateinit var imageAdapter: ImageAdapter
    private val selectedImages = mutableListOf<Uri>()

    private val pickImagesLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.let { data ->
                if (data.clipData != null) {
                    val count = data.clipData!!.itemCount
                    for (i in 0 until count) {
                        val imageUri = data.clipData!!.getItemAt(i).uri
                        if (selectedImages.size < 20) {
                            selectedImages.add(imageUri)
                        }
                    }
                } else if (data.data != null) {
                    data.data?.let { uri ->
                        if (selectedImages.size < 20) {
                            selectedImages.add(uri)
                        }
                    }
                }
                updateUI()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSelectImageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        setupRecyclerView()
        setupAddImageButton()
        setupCardButtons()
        updateUI()
    }

    private fun setupToolbar() {
        lifecycleScope.launch {
            viewModel.selectedBrand.collect { brand ->
                binding.titleText.text = brand ?: "Şəkil"
            }
        }

        binding.subtitleText.text = "16-dan addım 9"

        binding.backButton.setOnClickListener {
            (activity as? AddCarActivity)?.navigateBack()
        }

        binding.cancelButton.setOnClickListener {
            activity?.finish()
        }
    }

    private fun setupRecyclerView() {
        imageAdapter = ImageAdapter(selectedImages) { position ->
            selectedImages.removeAt(position)
            updateUI()
        }


    }

    private fun setupAddImageButton() {
        binding.nextButton.setOnClickListener {
            if (selectedImages.size >= 3) {
                // ✅ Log əlavə et
                android.util.Log.d("SelectImages", "Adding ${selectedImages.size} images to ViewModel")

                selectedImages.forEach { uri ->
                    viewModel.addImage(uri.toString())
                    android.util.Log.d("SelectImages", "Added: $uri")
                }

                // ✅ ViewModel-də neçə şəkil var yoxla
                viewModel.selectedImages.value.let { images ->
                    android.util.Log.d("SelectImages", "ViewModel has ${images.size} images")
                }

                (activity as? AddCarActivity)?.navigateToFragment(AddCarDetailsFragment())
            } else {
                openImagePicker()
            }
        }
    }

    private fun setupCardButtons() {
        binding.cardFront.setOnClickListener {
            openImagePicker()
        }

        binding.cardRear.setOnClickListener {
            openImagePicker()
        }

        binding.cardInterior.setOnClickListener {
            openImagePicker()
        }
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply {
            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        }
        pickImagesLauncher.launch(intent)
    }

    private fun updateUI() {
        imageAdapter.notifyDataSetChanged()

        val imageCount = selectedImages.size
        binding.sertText2.text = "Şəkil sayı: $imageCount/20"

        if (imageCount >= 3) {
            binding.nextButton.text = "Davam et"
        } else {
            binding.nextButton.text = "Şəkil əlavə etmək (minimum 3)"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
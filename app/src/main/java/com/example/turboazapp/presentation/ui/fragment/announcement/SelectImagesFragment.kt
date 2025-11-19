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
import com.example.turboazapp.AddCarActivity
import com.example.turboazapp.databinding.FragmentSelectImageBinding
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
                    // Çoxlu şəkil seçilib
                    val count = data.clipData!!.itemCount
                    for (i in 0 until count) {
                        val imageUri = data.clipData!!.getItemAt(i).uri
                        if (selectedImages.size < 20) {
                            selectedImages.add(imageUri)
                        }
                    }
                } else if (data.data != null) {
                    // Tək şəkil seçilib
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
            // Şəkli sil
            selectedImages.removeAt(position)
            updateUI()
        }

        binding.recyclerViewImages.apply {
            layoutManager = GridLayoutManager(requireContext(), 3)
            adapter = imageAdapter
        }
    }

    private fun setupAddImageButton() {
        binding.nextButton.setOnClickListener {
            openImagePicker()
        }
    }

    private fun setupCardButtons() {
        binding.cardFront.setOnClickListener {
            // Ön görünüş üçün kamera
            openImagePicker()
        }

        binding.cardRear.setOnClickListener {
            // Arxa görünüş üçün kamera
            openImagePicker()
        }

        binding.cardInterior.setOnClickListener {
            // Salon üçün kamera
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

        // Minimum 3 şəkil lazımdır
        val imageCount = selectedImages.size
        binding.sertText2.text = "Şəkil sayı: $imageCount/20"

        // Continue button aktiv/deaktiv
        // Növbəti fragmentə keçid - burada elanın digər məlumatlarını daxil edəcəyik
        // Hələlik test üçün 3 şəkil yetərlidir
        if (imageCount >= 3) {
            binding.nextButton.text = "Davam et"
            binding.nextButton.setOnClickListener {
                // Şəkilləri ViewModel-ə əlavə et
                selectedImages.forEach { uri ->
                    viewModel.addImage(uri.toString())
                }
                // Növbəti fragmentə keç (Qiymət, təsvir və s.)
                (activity as? AddCarActivity)?.navigateToFragment(AddCarDetailsFragment())
            }
        } else {
            binding.nextButton.text = "Şəkil əlavə etmək"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
package com.example.turboazapp.presentation.ui.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.turboazapp.R
import com.example.turboazapp.databinding.FragmentCarDetailsBinding
import com.example.turboazapp.domain.model.Car
import com.example.turboazapp.presentation.ui.adapter.ImagePagerAdapter
import com.example.turboazapp.presentation.viewmodel.CarDetailsViewModel
import com.example.turboazapp.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CarDetailsFragment : Fragment() {

    private var _binding: FragmentCarDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CarDetailsViewModel by viewModels()
    private lateinit var imagePagerAdapter: ImagePagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCarDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Hide bottom navigation
        (requireActivity() as MainActivity).setToolbarVisible(false)
        (requireActivity() as MainActivity).setBottomNavVisible(false)

        setupClickListeners()
        observeViewModel()
    }

    private fun setupClickListeners() {
        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.favoriteButton.setOnClickListener {
            val currentCar = (viewModel.carState.value as? Resource.Success)?.data
            currentCar?.let { car ->
                viewModel.toggleFavorite(car.isFavorite)
            }
        }

        binding.callButton.setOnClickListener {
            val currentCar = (viewModel.carState.value as? Resource.Success)?.data
            currentCar?.let { car ->
                makePhoneCall(car.phone)
            }
        }

        binding.whatsappButton.setOnClickListener {
            val currentCar = (viewModel.carState.value as? Resource.Success)?.data
            currentCar?.let { car ->
                openWhatsApp(car.phone, car)
            }
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.carState.collect { resource ->
                    when (resource) {
                        is Resource.Loading -> {
                            // Show loading
                        }
                        is Resource.Success -> {
                            resource.data?.let { car ->
                                bindCarData(car)
                            }
                        }
                        is Resource.Error -> {
                            Toast.makeText(
                                requireContext(),
                                "Xəta: ${resource.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.favoriteState.collect { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            Toast.makeText(
                                requireContext(),
                                "Sevimlilərə əlavə olundu",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        is Resource.Error -> {
                            Toast.makeText(
                                requireContext(),
                                resource.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        else -> {}
                    }
                }
            }
        }
    }

    private fun bindCarData(car: Car) {
        binding.apply {
            // Title
            carTitle.text = "${car.brand} ${car.model}"
            collapsingToolbar.title = "${car.brand} ${car.model}"

            // Price
            carPrice.text = "${car.price.toInt().toString().replace(Regex("(\\d)(?=(\\d{3})+$)"), "$1,")} ${car.currency}"

            // Basic info
            carYear.text = car.year.toString()
            carMileage.text = "${car.mileage.toString().replace(Regex("(\\d)(?=(\\d{3})+$)"), "$1,")} km"
            carColor.text = car.color
            carFuelType.text = car.fuelType
            carTransmission.text = car.transmission
            carEngineVolume.text = "${car.engineVolume} L"
            carCity.text = car.city

            // Description
            carDescription.text = car.description

            // Seller info
            sellerName.text = car.sellerName
            sellerPhone.text = car.phone

            // Images
            setupImagePager(car.images)

            // Favorite button
            updateFavoriteButton(car.isFavorite)
        }
    }

    private fun setupImagePager(images: List<String>) {
        imagePagerAdapter = ImagePagerAdapter(images)
        binding.imageViewPager.adapter = imagePagerAdapter

        // Image counter
        binding.imageCounter.text = "1 / ${images.size}"

        binding.imageViewPager.registerOnPageChangeCallback(
            object : androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    binding.imageCounter.text = "${position + 1} / ${images.size}"
                }
            }
        )
    }

    private fun updateFavoriteButton(isFavorite: Boolean) {
        if (isFavorite) {
            binding.favoriteButton.setImageResource(R.drawable.filled_heart)
        } else {
            binding.favoriteButton.setImageResource(R.drawable.empty_heart)
        }
    }

    private fun makePhoneCall(phoneNumber: String) {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$phoneNumber")
        }
        startActivity(intent)
    }

    private fun openWhatsApp(phoneNumber: String, car: Car) {
        val message = "Salam! ${car.brand} ${car.model} (${car.year}) haqqında məlumat almaq istəyirəm."
        val url = "https://wa.me/${phoneNumber.replace("+", "")}?text=${Uri.encode(message)}"

        try {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(url)
            }
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(
                requireContext(),
                "WhatsApp yüklənməyib",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (requireActivity() as MainActivity).setToolbarVisible(true)
        (requireActivity() as MainActivity).setBottomNavVisible(true)
        _binding = null
    }
}
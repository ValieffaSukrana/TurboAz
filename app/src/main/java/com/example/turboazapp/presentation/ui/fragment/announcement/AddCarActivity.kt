package com.example.turboazapp.presentation.ui.fragment.announcement

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.turboazapp.R
import com.example.turboazapp.databinding.ActivityAddCarBinding
import com.example.turboazapp.presentation.viewmodel.AddCarViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddCarActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddCarBinding
    private val viewModel: AddCarViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            // İlk fragmenti yüklə - Marka seçimi
            navigateToFragment(SelectBrandFragment())
        }
    }

    fun navigateToFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .addToBackStack(null)
            .commit()
    }

    fun navigateBack() {
        if (supportFragmentManager.backStackEntryCount > 1) {
            supportFragmentManager.popBackStack()
        } else {
            finish()
        }
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 1) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }
}
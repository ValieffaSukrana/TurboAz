package com.example.turboazapp.presentation.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.turboazapp.R
import com.example.turboazapp.databinding.ActivityMainBinding
import com.example.turboazapp.presentation.ui.LanguageHelper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun attachBaseContext(newBase: Context) {
        val lang = LanguageHelper.getSavedLanguage(newBase)
        val context = LanguageHelper.setAppLocale(newBase, lang)
        super.attachBaseContext(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavigationView.setupWithNavController(navController)

        binding.add.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, AddListingHostFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    fun setToolbarVisible(visible: Boolean) {
        binding.topAppBar.visibility = if (visible) View.VISIBLE else View.GONE
    }

    fun setBottomNavVisible(visible: Boolean) {
        binding.bottomNavigationView.visibility = if (visible) View.VISIBLE else View.GONE
        binding.add.visibility = if (visible) View.VISIBLE else View.GONE
    }
}

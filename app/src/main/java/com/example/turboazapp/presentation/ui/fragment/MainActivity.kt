package com.example.turboazapp.presentation.ui.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.turboazapp.presentation.ui.fragment.announcement.AddCarActivity
import com.example.turboazapp.R
import com.example.turboazapp.databinding.ActivityMainBinding
import com.example.turboazapp.presentation.ui.LanguageHelper
import com.example.turboazapp.util.FirebaseDataSeeder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var dataSeeder: FirebaseDataSeeder

    override fun attachBaseContext(newBase: Context) {
        val lang = LanguageHelper.getSavedLanguage(newBase)
        val context = LanguageHelper.setAppLocale(newBase, lang)
        super.attachBaseContext(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        applyDarkModeFromSettings()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupNavigation()

        // Firebase data yoxla və lazım olsa yüklə
        checkAndSeedData()
    }

    private fun setupNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavigationView.setupWithNavController(navController)

        // ✅ FAB düyməsi - Yeni elan əlavə et
        binding.add.setOnClickListener {
            val intent = Intent(this, AddCarActivity::class.java)
            startActivity(intent)
        }
    }

    private fun checkAndSeedData() {
        lifecycleScope.launch {
            try {
                val hasData = dataSeeder.isDataSeeded()

                if (!hasData) {
                    // Data yoxdur, istifadəçidən soruş
                    showSeedDialog()
                }
            } catch (e: Exception) {
                // Xəta olsa da problem deyil, app açılsın
                e.printStackTrace()
            }
        }
    }

    private fun showSeedDialog() {
        AlertDialog.Builder(this)
            .setTitle("Test Məlumatları Yüklə")
            .setMessage("Firebase-də avtomobil elanları yoxdur.\n\n15 test elanı yükləmək istəyirsiniz?")
            .setPositiveButton("Bəli") { _, _ ->
                seedData()
            }
            .setNegativeButton("Xeyr") { dialog, _ ->
                dialog.dismiss()
                Toast.makeText(
                    this,
                    "Daha sonra Settings-dən yükləyə bilərsiniz",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .setCancelable(false)
            .show()
    }

    private fun seedData() {
        lifecycleScope.launch {
            try {
                // Loading göstər
                Toast.makeText(
                    this@MainActivity,
                    "Məlumatlar yüklənir...",
                    Toast.LENGTH_SHORT
                ).show()

                val result = dataSeeder.seedCarsData()

                result.onSuccess { message ->
                    Toast.makeText(
                        this@MainActivity,
                        "✅ $message",
                        Toast.LENGTH_LONG
                    ).show()
                }.onFailure { error ->
                    Toast.makeText(
                        this@MainActivity,
                        "❌ Xəta: ${error.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(
                    this@MainActivity,
                    "❌ Xəta baş verdi: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun applyDarkModeFromSettings() {
        val sharedPref = getSharedPreferences("app_settings", Context.MODE_PRIVATE)
        val isDarkMode = sharedPref.getBoolean("dark_mode", false)
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    fun setToolbarVisible(visible: Boolean) {
        binding.topAppBar.visibility = if (visible) View.VISIBLE else View.GONE
    }

    fun setBottomNavVisible(visible: Boolean) {
        binding.bottomNavigationView.visibility = if (visible) View.VISIBLE else View.GONE
        binding.add.visibility = if (visible) View.VISIBLE else View.GONE
    }

    // Bonus: Settings-dən manual seed etmək üçün
    fun manualSeedData() {
        showSeedDialog()
    }

    // Bonus: Bütün data-ları silmək üçün (test məqsədilə)
    fun clearAllData() {
        AlertDialog.Builder(this)
            .setTitle("Bütün məlumatları sil")
            .setMessage("Bütün avtomobil elanları silinəcək. Əminsiniz?")
            .setPositiveButton("Bəli") { _, _ ->
                lifecycleScope.launch {
                    val result = dataSeeder.clearAllData()
                    result.onSuccess { message ->
                        Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
                    }.onFailure { error ->
                        Toast.makeText(
                            this@MainActivity,
                            "Xəta: ${error.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
            .setNegativeButton("Xeyr", null)
            .show()
    }

    override fun onResume() {
        super.onResume()

        // ✅ Logout sonrası yoxlama
        val currentUser = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser

        if (currentUser == null) {
            android.util.Log.d("MainActivity", "⚠️ İstifadəçi logout edib")
            // Restart olandan sonra SplashFragment avtomatik login səhifəsinə göndərəcək
        } else {
            android.util.Log.d("MainActivity", "✅ İstifadəçi login olub: ${currentUser.phoneNumber}")
        }
    }
}
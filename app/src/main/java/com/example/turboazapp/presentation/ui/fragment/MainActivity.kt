package com.example.turboazapp.presentation.ui.fragment

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.turboazapp.worker.NotificationWorker
import java.util.concurrent.TimeUnit
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
        checkAndSeedData()

        // ✅ FCM Token al
        getFCMToken()

        // ✅ 30 saniyə sonra notification schedulə et
        scheduleNotification()

        // ✅ Android 13+ üçün notification permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestNotificationPermission()
        }
    }

    private fun setupNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavigationView.setupWithNavController(navController)

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
                    showSeedDialog()
                }
            } catch (e: Exception) {
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

    // ✅ FCM Token al
    private fun getFCMToken() {
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val token = task.result
                    android.util.Log.d("FCM_TOKEN", "Device Token: $token")

                    saveTokenToPreferences(token)
                    saveTokenToFirestore(token)
                } else {
                    android.util.Log.e("FCM_TOKEN", "Token alınmadı", task.exception)
                }
            }
    }

    private fun saveTokenToPreferences(token: String) {
        val sharedPref = getSharedPreferences("app_settings", Context.MODE_PRIVATE)
        sharedPref.edit().putString("fcm_token", token).apply()
    }

    private fun saveTokenToFirestore(token: String) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUser?.let { user ->
            val db = FirebaseFirestore.getInstance()
            db.collection("users").document(user.uid)
                .update("fcmToken", token)
                .addOnSuccessListener {
                    android.util.Log.d("FCM", "✅ Token Firestore-a saxlanıldı")
                }
                .addOnFailureListener { e ->
                    android.util.Log.e("FCM", "❌ Token saxlanılmadı: ${e.message}")
                }
        }
    }

    // ✅ 30 saniyə sonra notification schedulə et
    private fun scheduleNotification() {
        val notificationWork = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setInitialDelay(30, TimeUnit.SECONDS) // 30 saniyə gecikmə
            .build()

        WorkManager.getInstance(this).enqueue(notificationWork)

        android.util.Log.d("NOTIFICATION", "🔔 Notification 30 saniyə sonra schedulə edildi")
    }

    // ✅ Android 13+ notification permission
    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(
                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                    1001
                )
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1001) {
            if (grantResults.isNotEmpty() &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "✅ Bildirişlər aktiv edildi", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "⚠️ Bildirişlər üçün icazə verilmədi", Toast.LENGTH_SHORT).show()
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



    override fun onResume() {
        super.onResume()

        val currentUser = FirebaseAuth.getInstance().currentUser

        if (currentUser == null) {
            android.util.Log.d("MainActivity", "⚠️ İstifadəçi logout edib")
        } else {
            android.util.Log.d("MainActivity", "✅ İstifadəçi login olub: ${currentUser.phoneNumber}")
        }
    }
}
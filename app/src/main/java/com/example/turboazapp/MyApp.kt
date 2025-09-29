package com.example.turboazapp

import android.app.Application
import android.content.Context
import com.example.turboazapp.presentation.ui.LanguageHelper
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApp : Application() {
    override fun attachBaseContext(base: Context?) {
        val lang = LanguageHelper.getSavedLanguage(base!!)
        val context = LanguageHelper.setAppLocale(base, lang)
        super.attachBaseContext(context)
    }
}
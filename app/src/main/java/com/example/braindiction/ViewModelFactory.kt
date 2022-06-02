package com.example.braindiction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.braindiction.preference.SettingPreferences
import com.example.braindiction.ui.main.settings.ThemeViewModel

class ViewModelFactory(private val pref: SettingPreferences) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ThemeViewModel::class.java)) {
            return ThemeViewModel(pref) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}

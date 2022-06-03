package com.example.braindiction.ui.main.settings

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.braindiction.R
import com.example.braindiction.ViewModelFactory
import com.example.braindiction.databinding.ActivitySettingsBinding
import com.example.braindiction.preference.SettingPreferences
import com.example.braindiction.ui.main.home.HomeActivity
import com.example.braindiction.ui.main.notification.NotificationActivity
import com.example.braindiction.ui.main.profile.ProfileActivity
import com.example.braindiction.viewmodel.ThemeViewModel
import com.example.braindiction.viewmodel.UserViewModel

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class SettingsActivity : AppCompatActivity() {
    private lateinit var userViewModel: UserViewModel
    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Settings"

        setupNavigation()
        setupLogoutAction()
        switchTheme()
    }

    private fun setupNavigation() {
        val bottomNav = binding.navView

        bottomNav.selectedItemId = R.id.navigation_settings

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_settings -> {
                    return@setOnItemSelectedListener true
                }
                R.id.navigation_notification -> {
                    startActivity(Intent(applicationContext, NotificationActivity::class.java))
                    overridePendingTransition(0, 0)
                    return@setOnItemSelectedListener true
                }
                R.id.navigation_profile -> {
                    startActivity(Intent(applicationContext, ProfileActivity::class.java))
                    overridePendingTransition(0, 0)
                    return@setOnItemSelectedListener true
                }
                R.id.navigation_home -> {
                    startActivity(Intent(applicationContext, HomeActivity::class.java))
                    overridePendingTransition(0, 0)
                    return@setOnItemSelectedListener true
                }
            }
            return@setOnItemSelectedListener false
        }

        // navigation to language settings
        binding.language.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }
    }

    private fun switchTheme() {
        val pref = SettingPreferences.getInstance(dataStore)
        val themeViewModel =
            ViewModelProvider(this, ViewModelFactory(pref))[ThemeViewModel::class.java]
        val switch = binding.toggleThemeLayout

        themeViewModel.getThemeSettings().observe(
            this
        ) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                switch.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                switch.isChecked = false
            }
        }

        switch.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            themeViewModel.saveThemeSetting(isChecked)
        }
    }

    private fun setupLogoutAction() {
        binding.logoutButton.setOnClickListener {
            userViewModel.logout()
        }
    }
}
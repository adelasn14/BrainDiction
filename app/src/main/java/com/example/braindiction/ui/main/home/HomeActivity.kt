package com.example.braindiction.ui.main.home

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.braindiction.R
import com.example.braindiction.databinding.ActivityHomeBinding
import com.example.braindiction.ui.archive.ArchiveActivity
import com.example.braindiction.ui.main.notification.NotificationActivity
import com.example.braindiction.ui.main.profile.ProfileActivity
import com.example.braindiction.ui.main.settings.SettingsActivity
import com.example.braindiction.ui.patient.NewPredictionActivity


class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "BrainDiction"

        setupAction()
        setupNavigation()

    }

    private fun setupAction() {
        binding.newPredictionButton.setOnClickListener {
            val toRegisterPatient = Intent(this, NewPredictionActivity::class.java)
            startActivity(toRegisterPatient)
        }
        binding.archiveButton.setOnClickListener {
            val toArchive = Intent(this, ArchiveActivity::class.java)
            startActivity(toArchive)
        }
    }

    private fun setupNavigation() {
        val bottomNav = binding.navView

        bottomNav.selectedItemId = R.id.navigation_home

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
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
                R.id.navigation_settings -> {
                    startActivity(Intent(applicationContext, SettingsActivity::class.java))
                    overridePendingTransition(0, 0)
                    return@setOnItemSelectedListener true
                }
            }
            return@setOnItemSelectedListener false
        }
    }
}
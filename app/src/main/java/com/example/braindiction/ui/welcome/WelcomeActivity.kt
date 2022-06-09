package com.example.braindiction.ui.welcome

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.braindiction.databinding.ActivityWelcomeBinding
import com.example.braindiction.ui.login.LoginActivity

class WelcomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAction()
    }

    private fun setupAction(){
        binding.loginButton.setOnClickListener {
            val toLogin = Intent(this, LoginActivity::class.java)
            startActivity(toLogin)
        }

        binding.buttonGyuide.setOnClickListener {
            val toLogin = Intent(this, GuideActivity::class.java)
            startActivity(toLogin)
        }
    }

}
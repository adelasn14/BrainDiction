package com.example.braindiction.ui.signUp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.widget.addTextChangedListener
import com.example.braindiction.databinding.ActivitySignUpBinding
import com.example.braindiction.ui.login.LoginActivity

class signUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAction()
    }

    private fun setMyButtonEnable() {
        val nameSet = binding.nameEditText.text
        val emailSet = binding.emailEditText.text
        val passwordSet = binding.passwordEditText.text
        binding.signUpButton.isEnabled =
            nameSet != null && emailSet != null && passwordSet != null && nameSet.toString()
                .isNotEmpty() && emailSet.toString().isNotEmpty() && passwordSet.toString()
                .isNotEmpty()

    }

    private fun setupAction() {
        binding.nameEditText.addTextChangedListener {
            setMyButtonEnable()
        }

        binding.emailEditText.addTextChangedListener {
            setMyButtonEnable()
        }

        binding.passwordEditText.addTextChangedListener {
            setMyButtonEnable()
        }

        binding.signUpButton.setOnClickListener {
            val logIn = Intent (this, LoginActivity::class.java)
            startActivity(logIn)
        }
    }

}
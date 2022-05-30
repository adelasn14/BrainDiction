package com.example.braindiction.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.widget.addTextChangedListener
import com.example.braindiction.R
import com.example.braindiction.databinding.ActivityLoginBinding
import com.example.braindiction.ui.home.HomeActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAction()
    }

    private fun setMyButtonEnable() {
        val emailSet = binding.emailEditText.text
        val passwordSet = binding.passwordEditText.text
        binding.loginButton.isEnabled =
            emailSet != null && passwordSet != null && emailSet.toString()
                .isNotEmpty() && passwordSet.toString().isNotEmpty()

    }

    private fun setupAction(){
        binding.emailEditText.addTextChangedListener {
            setMyButtonEnable()
        }

        binding.passwordEditText.addTextChangedListener {
            setMyButtonEnable()
        }

        binding.loginButton.setOnClickListener {
            val toHome = Intent(this, HomeActivity::class.java)
            startActivity(toHome)
        }

        binding.signInButton.setOnClickListener {
//            val toSignUp = Intent(this, Sign::class.java)
//            startActivity(toSignUp)
        }
    }
}
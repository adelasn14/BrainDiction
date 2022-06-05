package com.example.braindiction.ui.login

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.braindiction.UserPreference
import com.example.braindiction.viewmodel.UserViewModelFactory
import com.example.braindiction.api.IsUserLogin
import com.example.braindiction.databinding.ActivityLoginBinding
import com.example.braindiction.preference.LoginSession
import com.example.braindiction.ui.main.home.HomeActivity
import com.example.braindiction.ui.signUp.SignUpActivity
import com.example.braindiction.viewmodel.LoginViewModel
import com.example.braindiction.viewmodel.UserViewModel

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "userLogin")
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var userViewModel: UserViewModel
    private lateinit var user: IsUserLogin
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAction()
        setupViewModel()
    }

    private fun setMyButtonEnable() {
        val emailSet = binding.emailEditText.text
        val passwordSet = binding.passwordEditText.text
        binding.loginButton.isEnabled =
            emailSet != null && passwordSet != null && emailSet.toString()
                .isNotEmpty() && passwordSet.toString().isNotEmpty()

    }

    private fun setupViewModel() {
        userViewModel = ViewModelProvider(
            this,
            UserViewModelFactory(UserPreference.getInstance(dataStore))
        )[UserViewModel::class.java]

        loginViewModel = ViewModelProvider(
            this,
            UserViewModelFactory(UserPreference.getInstance(dataStore))
        )[LoginViewModel::class.java]

        userViewModel.getUser().observe(this) { user ->
            this.user = user
        }
    }

    private fun setupAction(){
        binding.emailEditText.addTextChangedListener {
            setMyButtonEnable()
        }

        binding.passwordEditText.addTextChangedListener {
            setMyButtonEnable()
        }

        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            loginViewModel.doUserLogin(email, password)
            userViewModel.login()
            showLoading(true)
            loginViewModel.loginUser.observe(this) {
                val loginSession = LoginSession(this)
                loginSession.saveAuthToken(it.LoginResult?.token.toString())
                Log.d(
                    "LoginActivity",
                    "token : ${loginSession.passToken().toString()}"
                )
                showLoading(false)
                AlertDialog.Builder(this).apply {
                    setTitle("Congratulation!")
                    setMessage("You are now logging in. Let's connect!")
                    setPositiveButton("Continue") { _, _ ->
                        val intent = Intent(context, HomeActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                        finish()
                    }
                    create()
                    show()
                }
            }

            // will be deleted later, only act as navigator
            val toHome = Intent(this, HomeActivity::class.java)
            startActivity(toHome)
        }

        binding.signInButton.setOnClickListener {
            val toSignUp = Intent(this, SignUpActivity::class.java)
            startActivity(toSignUp)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
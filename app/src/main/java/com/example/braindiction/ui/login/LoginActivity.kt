package com.example.braindiction.ui.login

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.braindiction.preference.UserPreference
import com.example.braindiction.viewmodel.UserViewModelFactory
import com.example.braindiction.api.IsUserLogin
import com.example.braindiction.databinding.ActivityLoginBinding
import com.example.braindiction.preference.LoginSession
import com.example.braindiction.ui.main.home.HomeActivity
import com.example.braindiction.ui.signUp.SignUpActivity
import com.example.braindiction.viewmodel.LoginViewModel
import com.example.braindiction.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "userLogin")
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var userViewModel: UserViewModel
    private lateinit var user: IsUserLogin
    private lateinit var loginViewModel: LoginViewModel

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        setupAction()
        setupViewModel()
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            reload()
        }
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

            showLoading(true)
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success")
                        val user = auth.currentUser
                        updateUI(user)
                        showLoading(false)
                        AlertDialog.Builder(this).apply {
                            setTitle("Yeah!")
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

                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(baseContext, "Wrong password or email.",
                            Toast.LENGTH_SHORT).show()
                        updateUI(null)
                    }
                }
        }

        binding.signUpButton.setOnClickListener {
            val toSignUp = Intent(this, SignUpActivity::class.java)
            startActivity(toSignUp)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun updateUI(user: FirebaseUser?) {

    }

    private fun reload() {

    }

    companion object {
        private const val TAG = "EmailPassword"
    }
}
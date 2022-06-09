package com.example.braindiction.ui.signUp

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.braindiction.R
import com.example.braindiction.preference.UserPreference
import com.example.braindiction.viewmodel.UserViewModelFactory
import com.example.braindiction.api.UserRegister
import com.example.braindiction.databinding.ActivitySignUpBinding
import com.example.braindiction.ui.login.LoginActivity
import com.example.braindiction.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "userRegister")
class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var userViewModel: UserViewModel

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        setupViewModel()
        setupAction()
    }

    private fun setMyButtonEnable() {
        val nameSet = binding.nameEditText.text
        val usernameSet = binding.usernameEditText.text
        val emailSet = binding.emailEditText.text
        val passwordSet = binding.passwordEditText.text
        binding.signUpButton.isEnabled =
            nameSet != null && usernameSet != null && emailSet != null && passwordSet != null && nameSet.toString()
                .isNotEmpty() && usernameSet.toString().isNotEmpty() && emailSet.toString()
                .isNotEmpty() && passwordSet.toString()
                .isNotEmpty() && emailSet.contains("@")
                    && emailSet.contains(".com") && passwordSet.length >= 8

    }

    private fun setupViewModel() {
        userViewModel = ViewModelProvider(
            this,
            UserViewModelFactory(UserPreference.getInstance(dataStore))
        )[UserViewModel::class.java]
    }

    @SuppressLint("SimpleDateFormat")
    private fun setupAction() {
        binding.nameEditText.addTextChangedListener {
            setMyButtonEnable()
        }

        binding.usernameEditText.addTextChangedListener {
            setMyButtonEnable()
        }

        binding.emailEditText.addTextChangedListener {
            setMyButtonEnable()
        }

        binding.passwordEditText.addTextChangedListener {
            setMyButtonEnable()
        }

        val ddOccupation = binding.spinnerOccupation
        val occupation = resources.getStringArray(R.array.occupation)

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item, occupation
        )
        ddOccupation.adapter = adapter

        val birthEd = binding.textDateBirth
        val myCalendar = Calendar.getInstance()
        val date = DatePickerDialog.OnDateSetListener { _: DatePicker, year: Int, month: Int, day: Int ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH,month)
            myCalendar.set(Calendar.DAY_OF_MONTH,day)

            val sdfView = SimpleDateFormat("dd-MM-yyyy", Locale.US)
            birthEd.setText(sdfView.format(myCalendar.time))
        }

        birthEd.setOnClickListener {
            DatePickerDialog(this, date, myCalendar.get(Calendar.YEAR), myCalendar
                .get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        binding.signUpButton.setOnClickListener {
            binding.apply {
                val name = nameEditText.text.toString()
                val username = usernameEditText.text.toString()
                val email = emailEditText.text.toString()
                val password = passwordEditText.text.toString()
                val setGender =
                    (findViewById<View>(setGender.checkedRadioButtonId) as RadioButton).text.toString()
//
                // birth date
                val sdfConvert = SimpleDateFormat("yyyy-MM-dd")
                val dateToConvert = sdfConvert.format(myCalendar.time)
                val dateValue = sdfConvert.parse(dateToConvert) as Date
                val dobValue = sdfConvert.format(dateValue)
                Log.d("NewPatientActivity", "dobValue : $dobValue")

                val address =
                    binding.alamatEditText.text.toString()

                showLoading(true)
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { it ->
                        if (it.isSuccessful) {
                            val userRegister = UserRegister(name,username,email,password,setGender,dobValue, address)
                            FirebaseAuth.getInstance().currentUser?.let { it1 ->
                                FirebaseDatabase.getInstance().getReference("User Doctor Register").child(
                                    it1.uid).setValue(userRegister).addOnCompleteListener {
                                    if (it.isSuccessful) {
                                            // Sign in success, update UI with the signed-in user's information
                                            Log.d(TAG, "createUserWithEmail:success")
                                            val user = auth.currentUser
                                            updateUI(user)
                                            showLoading(false)
                                            AlertDialog.Builder(this@SignUpActivity).apply {
                                                setTitle("Yeah!")
                                                setMessage("Your account is created and ready to use. Let's log you in!")
                                                setPositiveButton("Log in") { _, _ ->
                                                    val intent = Intent(context, LoginActivity::class.java)
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
                                            showLoading(false)
                                            Log.w(TAG, "createUserWithEmail:failure", it.exception)
                                            Toast.makeText(
                                                baseContext, "Authentication failed.",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            updateUI(null)
                                        }
                                    }
                                }
                        }
                    }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun updateUI(user: FirebaseUser?) {

    }

    companion object {
        private const val TAG = "SignUpActivity"
    }
}

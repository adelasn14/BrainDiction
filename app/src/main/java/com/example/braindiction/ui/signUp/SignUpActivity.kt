package com.example.braindiction.ui.signUp

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
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
import com.example.braindiction.api.IsUserLogin
import com.example.braindiction.databinding.ActivitySignUpBinding
import com.example.braindiction.ui.login.LoginActivity
import com.example.braindiction.ui.main.home.HomeActivity
import com.example.braindiction.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
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

        // birth date
        val birthEd = binding.textDateBirth
        birthEd.transformIntoDatePicker(this@SignUpActivity, "dd/MM/yyyy")
        birthEd.transformIntoDatePicker(this@SignUpActivity, "dd/MM/yyyy", Date())
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
                .isNotEmpty()

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

        binding.signUpButton.setOnClickListener {
            binding.apply {
//                val name = nameEditText.text.toString()
//                val username = usernameEditText.text.toString()
                val email = emailEditText.text.toString()
                val password = passwordEditText.text.toString()
//                val gender = setChangeGender().toString()
//
//                // birth date
//                val dob = textDateBirth
//                val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
////                val myDate: Date? = df.parse(dob.text.toString())
//
//                val address =
//                    binding.alamatEditText.text.toString().toRequestBody("text/plain".toMediaType())

//                userViewModel.saveUser(IsUserLogin(name, email, password, false))
                showLoading(true)
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this@SignUpActivity) { task ->
                        if (task.isSuccessful) {
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
                            Log.w(TAG, "createUserWithEmail:failure", task.exception)
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

    //button set gender change
    private fun setChangeGender() {
        binding.setGender.setOnCheckedChangeListener { _, checkedId ->
            val text = "You selected " +
                    if (R.id.radioButton == checkedId)
                        "male"
                    else
                        "female"
            Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT).show()
        }
    }

    private fun EditText.transformIntoDatePicker(
        context: Context,
        format: String,
        maxDate: Date? = null
    ) {
        isFocusableInTouchMode = false
        isClickable = true
        isFocusable = false

        val myCalendar = Calendar.getInstance()
        val datePickerOnDataSetListener =
            DatePickerDialog.OnDateSetListener { _, year, month, day ->
                myCalendar.set(Calendar.YEAR, year)
                myCalendar.set(Calendar.MONTH, month)
                myCalendar.set(Calendar.DAY_OF_MONTH, day)
                val sdf = SimpleDateFormat(format, Locale.UK)
                setText(sdf.format(myCalendar.time))
            }

        setOnClickListener {
            DatePickerDialog(
                context, datePickerOnDataSetListener, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
            ).run {
                maxDate?.time?.also { datePicker.maxDate = it }
                show()
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun updateUI(user: FirebaseUser?) {

    }

    companion object {
        private const val TAG = "EmailPassword"
    }
}

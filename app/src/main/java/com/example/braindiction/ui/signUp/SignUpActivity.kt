package com.example.braindiction.ui.signUp

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
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
import com.example.braindiction.UserPreference
import com.example.braindiction.UserViewModelFactory
import com.example.braindiction.api.ApiConfig
import com.example.braindiction.api.IsUserLogin
import com.example.braindiction.api.RegisterResponse
import com.example.braindiction.databinding.ActivitySignUpBinding
import com.example.braindiction.ui.login.LoginActivity
import com.example.braindiction.viewmodel.UserViewModel
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "userRegister")
class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
                .isNotEmpty() && usernameSet.toString().isNotEmpty() && emailSet.toString().isNotEmpty() && passwordSet.toString()
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
                val name = nameEditText.text.toString()
                val username = usernameEditText.text.toString()
                val email = emailEditText.text.toString()
                val password = passwordEditText.text.toString()
                val gender = setChangeGender().toString()

                // birth date
                val dob = textDateBirth
                val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                val myDate: Date? = df.parse(dob.text.toString())

                val address =
                    binding.alamatEditText.text.toString().toRequestBody("text/plain".toMediaType())

                userViewModel.saveUser(IsUserLogin(name, email, password, false))
                val client = ApiConfig().getApiService()
                    .userRegister(name, username, email, password, gender, myDate, address)
                client.enqueue(object : Callback<RegisterResponse> {
                    override fun onResponse(
                        call: Call<RegisterResponse>,
                        response: Response<RegisterResponse>
                    ) {
                        if (response.isSuccessful) {
                            val responseBody = response.body()
                            if (responseBody != null && !responseBody.error) {
                                AlertDialog.Builder(this@SignUpActivity).apply {
                                    setTitle("Yeah!")
                                    setMessage("Your account is created and ready to use. Login and see what other people is up to!")
                                    setPositiveButton("Continue") { _, _ ->
                                        finish()
                                    }
                                    create()
                                    show()
                                }
                            }
                        } else {
                            val jsonObj =
                                JSONObject(response.errorBody()!!.charStream().readText())
                            Toast.makeText(
                                this@SignUpActivity,
                                jsonObj.getString("message"),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                        Toast.makeText(
                            this@SignUpActivity,
                            "Gagal mendaftarkan diri",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
            }
            val logIn = Intent(this, LoginActivity::class.java)
            startActivity(logIn)
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

    private fun EditText.transformIntoDatePicker(context: Context, format: String, maxDate: Date? = null) {
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

}
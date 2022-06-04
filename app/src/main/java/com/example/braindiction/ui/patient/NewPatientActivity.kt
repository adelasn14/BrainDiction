package com.example.braindiction.ui.patient

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.example.braindiction.R
import com.example.braindiction.api.AddNewPatientResponse
import com.example.braindiction.api.ApiConfig
import com.example.braindiction.databinding.ActivityNewPatientBinding
import com.example.braindiction.preference.LoginSession
import com.example.braindiction.ui.login.LoginActivity
import com.example.braindiction.ui.main.home.HomeActivity
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

@Suppress("NAME_SHADOWING")
class NewPatientActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewPatientBinding

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewPatientBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "New Patient"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setupAction()
        setChangeGender()

        val birthEd = binding.textDateBirth
        birthEd.transformIntoDatePicker(this, "dd/MM/yyyy")
        birthEd.transformIntoDatePicker(this, "dd/MM/yyyy", Date())
    }

    private fun setMyButtonEnable() {
        val rmNumberSet = binding.rmEditText.text
        val nameSet = binding.nameEditText.text
        /* val genderSet = binding.jenisKelaminTextView.text
        val dateOfBirthSet = binding.tanggalLahirTextView.text
        */
        val addressSet = binding.alamatEditText.text
        binding.continueButton.isEnabled =
            rmNumberSet != null && nameSet != null && addressSet != null && rmNumberSet.toString()
                .isNotEmpty() && nameSet.toString().isNotEmpty() && addressSet.toString()
                .isNotEmpty()

    }

    @SuppressLint("SimpleDateFormat")
    private fun setupAction() {
        binding.rmEditText.addTextChangedListener {
            setMyButtonEnable()
        }

        binding.nameEditText.addTextChangedListener {
            setMyButtonEnable()
        }

        binding.alamatEditText.addTextChangedListener {
            setMyButtonEnable()
        }

        binding.continueButton.setOnClickListener {
//            binding.apply {
//                val noRM = binding.rmEditText.text.toString()
//                val name = binding.nameEditText.text.toString()
//                val gender = setChangeGender().toString()
//
//                val df = SimpleDateFormat("dd-MM-yyyy")
//                val myDate: Date = df.parse(binding.textDateBirth.text.toString()) as Date
//
//                val address =
//                    binding.alamatEditText.text.toString().toRequestBody("text/plain".toMediaType())
//
//                val loginSession = LoginSession(this@NewPatientActivity)
//                val client = ApiConfig().getApiService().patientRegister(
//                    "Bearer ${loginSession.passToken().toString()}",
//                    noRM, name, gender, myDate, address
//                )
//                client.enqueue(object : Callback<AddNewPatientResponse> {
//                    override fun onResponse(
//                        call: Call<AddNewPatientResponse>,
//                        response: Response<AddNewPatientResponse>
//                    ) {
//                        if (response.isSuccessful) {
//                            val responseBody = response.body()
//                            if (responseBody != null && !responseBody.error) {
//                                Toast.makeText(
//                                    this@NewPatientActivity,
//                                    responseBody.message,
//                                    Toast.LENGTH_SHORT
//                                ).show()
//                                Log.d("AddStoryActivity", responseBody.toString())
//                                AlertDialog.Builder(this@NewPatientActivity).apply {
//                                    setTitle("Yeah!")
//                                    setMessage("Anda berhasil mengupload story. Sudah tidak sabar untuk belajar ya?")
//                                    setPositiveButton("Lihat story") { _, _ ->
//                                        val intent =
//                                            Intent(context, DetailPatientActivity::class.java)
//                                        intent.flags =
//                                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
//                                        startActivity(intent)
//                                        finish()
//                                    }
//                                    create()
//                                    show()
//                                }
//                            }
//                        } else {
//                            Toast.makeText(
//                                this@NewPatientActivity,
//                                response.message(),
//                                Toast.LENGTH_SHORT
//                            ).show()
//                        }
//                    }
//
//                    override fun onFailure(call: Call<AddNewPatientResponse>, t: Throwable) {
//                        Toast.makeText(
//                            this@NewPatientActivity,
//                            "Gagal instance Retrofit",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }
//                })
//            }
            val logIn = Intent(this, DetailPatientActivity::class.java)
            startActivity(logIn)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        val backTo = Intent(this, HomeActivity::class.java)
        startActivity(backTo)
        return true
    }

    //button set gender change
    private fun setChangeGender() {
        binding.setGender.setOnCheckedChangeListener { _, checkedId ->
            val text = "You selected " +
                    if (R.id.radioButton == checkedId)
                        "Male"
                    else
                        "Female"
            Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT).show()
        }
    }

    // date Picker
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

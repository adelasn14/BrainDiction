package com.example.braindiction.ui.patient

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.DatePicker
import android.widget.RadioButton
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.braindiction.api.AddNewPatientResponse
import com.example.braindiction.api.ApiConfig
import com.example.braindiction.databinding.ActivityNewPatientBinding
import com.example.braindiction.preference.LoginSession
import com.example.braindiction.ui.main.home.HomeActivity
import com.example.braindiction.ui.prediction.PredictionActivity
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
    }

    private fun setMyButtonEnable() {
        val nameSet = binding.nameEditText.text
        /* val genderSet = binding.jenisKelaminTextView.text
        val dateOfBirthSet = binding.tanggalLahirTextView.text
        */
        val addressSet = binding.alamatEditText.text
        binding.continueButton.isEnabled =
            nameSet != null && addressSet != null && nameSet.toString()
                .isNotEmpty() && addressSet.toString()
                .isNotEmpty()

    }

    @SuppressLint("SimpleDateFormat")
    private fun setupAction() {
        binding.nameEditText.addTextChangedListener {
            setMyButtonEnable()
        }

        binding.alamatEditText.addTextChangedListener {
            setMyButtonEnable()
        }

        val birthEd = binding.textDateBirth
        val myCalendar = Calendar.getInstance()
        val date = DatePickerDialog.OnDateSetListener { _: DatePicker, year: Int, month: Int, day: Int ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH,month)
            myCalendar.set(Calendar.DAY_OF_MONTH,day)

            val sdfView = SimpleDateFormat("dd-MMM-yyyy", Locale.US)
            birthEd.setText(sdfView.format(myCalendar.time))
        }

        birthEd.setOnClickListener {
            DatePickerDialog(this, date, myCalendar.get(Calendar.YEAR), myCalendar
                .get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        binding.continueButton.setOnClickListener {
            binding.apply {
                val name = nameEditText.text.toString()

                // set gender
                val setGender =
                    (findViewById<View>(setGender.checkedRadioButtonId) as RadioButton).text.toString()

                // set date
                val sdfConvert = SimpleDateFormat("yyyy-MM-dd")
                val dateToConvert = sdfConvert.format(myCalendar.time)
                val dateValue = sdfConvert.parse(dateToConvert) as Date
                val dobValue = sdfConvert.format(dateValue)
                Log.d("NewPatientActivity", "dobValue : $dobValue")

                // address
                val address =
                    alamatEditText.text.toString()

                val _addNewPatient = MutableLiveData<AddNewPatientResponse>()
                val addNewPatient: LiveData<AddNewPatientResponse> = _addNewPatient


                val loginSession = LoginSession(this@NewPatientActivity)
                val token = loginSession.passToken().toString()
                val client = ApiConfig().getApiService().patientRegister("Bearer $token",
                    AddNewPatientResponse(name, setGender,
                        dobValue, address)
                )
                client.enqueue(object : Callback<AddNewPatientResponse> {
                    override fun onResponse(
                        call: Call<AddNewPatientResponse>,
                        response: Response<AddNewPatientResponse>
                    ) {
                        if (response.isSuccessful) {
                            val responseBody = response.body()
                            if (responseBody != null) {
                                _addNewPatient.postValue(response.body())
                                Log.d("TAG", "response : $response")

                                AlertDialog.Builder(this@NewPatientActivity).apply {
                                    setTitle("Congratulations!")
                                    setMessage("Patient data has successfully been submitted.")
                                    setPositiveButton("Continue") { _, _ ->
                                        val intent = Intent(this@NewPatientActivity, DetailPatientActivity::class.java)
                                        intent.putExtra(DetailPatientActivity.EXTRA_NAME, responseBody.name)
                                        intent.flags =
                                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                        startActivity(intent)
                                        finish()
                                    }
                                    create()
                                    show()
                                }
                            }
                        }
                    }

                    override fun onFailure(call: Call<AddNewPatientResponse>, t: Throwable) {
                        Log.d(TAG, "onFailure: ${t.message.toString()}")
                    }
                })
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        val backTo = Intent(this, HomeActivity::class.java)
        startActivity(backTo)
        return true
    }
}

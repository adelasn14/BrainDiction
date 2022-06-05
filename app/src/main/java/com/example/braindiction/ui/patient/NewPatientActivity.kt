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
import com.example.braindiction.ui.main.home.HomeActivity
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

//        val birthEd = binding.textDateBirth
//        val myCalendar = Calendar.getInstance()
//        val datePickerListener =
//            DatePickerDialog.OnDateSetListener { _, year, month, day ->
//            myCalendar.set(Calendar.YEAR, year)
//            myCalendar.set(Calendar.MONTH, month)
//            myCalendar.set(Calendar.DAY_OF_MONTH, day)
//            val sdf = SimpleDateFormat(("dd-MM-yyyy"), Locale.UK)
//            birthEd.setText(sdf.format(myCalendar.time))
//        }
//
//        birthEd.setOnClickListener {
//             fun onClick(view: View) {
//                DatePickerDialog(
//                    this, datePickerListener, myCalendar
//                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
//                    myCalendar.get(Calendar.DAY_OF_MONTH)
//                ).show()
//             }
//            onClick(view)
//        }

        val birthEd = binding.textDateBirth
        val myCalendar = Calendar.getInstance()
        val date = DatePickerDialog.OnDateSetListener { _: DatePicker, year: Int, month: Int, day: Int ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH,month)
            myCalendar.set(Calendar.DAY_OF_MONTH,day)

            val myFormat = "dd-MM-yyyy"
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            birthEd.setText(sdf.format(myCalendar.time))
        }

        birthEd.setOnClickListener {
            val dobEd = DatePickerDialog(
                this@NewPatientActivity, date, myCalendar.get(Calendar.YEAR), myCalendar
                    .get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)
            ).show().toString()
            Log.d("TAG", "birthEd : ${dobEd}")
        }

        binding.continueButton.setOnClickListener {
            binding.apply {
                val name = binding.nameEditText.text.toString()

                // set gender
                val rg = setGender
                val radiovalue =
                    (findViewById<View>(rg.checkedRadioButtonId) as RadioButton).text.toString()

                // set date
                val sdf = SimpleDateFormat("yyyy-MM-dd")
                val dob: Date = sdf.parse(birthEd.text.toString()) as Date
                Log.d("TAG", "dob : $dob")

                // address
                val address =
                    binding.alamatEditText.text.toString()

                val _addNewPatient = MutableLiveData<AddNewPatientResponse>()
                val addNewPatient: LiveData<AddNewPatientResponse> = _addNewPatient

                val client = ApiConfig().getApiService().patientRegister(
                    name, radiovalue, dob, address
                )
                client.enqueue(object : Callback<AddNewPatientResponse> {
                    override fun onResponse(
                        call: Call<AddNewPatientResponse>,
                        response: Response<AddNewPatientResponse>
                    ) {
                        if (response.isSuccessful) {
                            val responseBody = response.body()
                            if (responseBody != null && !responseBody.error) {
                                _addNewPatient.postValue(response.body())
                                Log.d(TAG, "response : $response")
                            }
                        }
                    }

                    override fun onFailure(call: Call<AddNewPatientResponse>, t: Throwable) {
                        Log.d(TAG, "onFailure: ${t.message.toString()}")
                    }
                })
                AlertDialog.Builder(this@NewPatientActivity).apply {
                    setTitle("Yeah!")
                    setMessage("You are now logging in. Let's connect!")
                    setPositiveButton("Continue") { _, _ ->
                        val intent = Intent(context, DetailPatientActivity::class.java)
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

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        val backTo = Intent(this, HomeActivity::class.java)
        startActivity(backTo)
        return true
    }
}

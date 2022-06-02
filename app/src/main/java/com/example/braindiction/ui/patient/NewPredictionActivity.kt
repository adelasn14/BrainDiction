package com.example.braindiction.ui.patient

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.example.braindiction.R
import com.example.braindiction.databinding.ActivityNewPredictionBinding
import com.example.braindiction.ui.main.home.HomeActivity
import java.text.SimpleDateFormat
import java.util.*

@Suppress("NAME_SHADOWING")
class NewPredictionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewPredictionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewPredictionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "New Patient"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setupAction()
        setChangeGender()

        val birthEd = binding.textDateBirth
        birthEd.transformIntoDatePicker(this, "MM/dd/yyyy")
        birthEd.transformIntoDatePicker(this, "MM/dd/yyyy", Date())

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
            val next = Intent(this, DetailPatientActivity::class.java)
            startActivity(next)
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
        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            val text = "You selected " +
                    if (R.id.radioButton == checkedId)
                        "male"
                    else
                        "female"
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

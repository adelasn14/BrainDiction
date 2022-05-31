package com.example.braindiction.ui.patient

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.example.braindiction.R
import com.example.braindiction.databinding.ActivityNewPredictionBinding
import com.example.braindiction.ui.main.home.HomeActivity
import java.util.*

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
                .isNotEmpty() && nameSet.toString().isNotEmpty() && addressSet.toString().isNotEmpty()

    }

    private fun setupAction(){
        binding.rmEditText.addTextChangedListener {
            setMyButtonEnable()
        }

        binding.nameEditText.addTextChangedListener {
            setMyButtonEnable()
        }

        binding.alamatEditText.addTextChangedListener {
            setMyButtonEnable()
        }

        binding.continueButton.setOnClickListener{
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
        binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            val text = "You selected: " +
                    if (R.id.radioButton == checkedId)
                        "male"
                    else
                        "female"
            Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT).show()
        }
    }

    // datepicker
//    private fun setupDatePicker() {
//        val today = Calendar.getInstance()
//        binding.PickDate.(
//            today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH)
//
//        ) { view, year, monthOfYear, dayOfMonth ->
//            val month = monthOfYear + 1
//            val msg = "Selected Date is $dayOfMonth/$month/$year"
//            Toast.makeText(this@New, msg, Toast.LENGTH_SHORT).show()
//
//            binding.textDateSend.text = msg
//            binding.textDateSend.visibility = View.VISIBLE
//        }
//    }

}
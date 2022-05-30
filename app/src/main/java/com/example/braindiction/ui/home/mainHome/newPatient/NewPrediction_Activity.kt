package com.example.braindiction.ui.home.mainHome.newPatient

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.widget.addTextChangedListener
import com.example.braindiction.databinding.ActivityNewPredictionBinding

class NewPrediction_Activity : AppCompatActivity() {
    private lateinit var binding: ActivityNewPredictionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewPredictionBinding.inflate(layoutInflater)
        setContentView(binding.root)
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
}
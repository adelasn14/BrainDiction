package com.example.braindiction.ui.patient

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.braindiction.R
import com.example.braindiction.api.PatientData
import com.example.braindiction.databinding.ActivityDetailPatientBinding
import com.example.braindiction.ui.archive.ArchiveActivity
import com.example.braindiction.ui.prediction.PredictionActivity
import java.text.SimpleDateFormat
import java.util.*


class DetailPatientActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailPatientBinding
    private var patientid: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPatientBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Detail Patient"

        setupAction()
    }

    private fun setupAction(){
        val formatSQL = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")

        val sdfConvert = SimpleDateFormat("dd-MMM-yyyy", Locale.US)

        val detail = intent.getParcelableExtra<PatientData>(EXTRA_NAME)
        binding.apply {
            tvPatientId.text = detail?.patientid.toString()
            tvPatient.text = detail?.name

            val dateValue = formatSQL.parse(detail?.dateofbirth.toString()) as Date
            val dobPatient = sdfConvert.format(dateValue)
            Log.d("Detail Patient", "dobPatient : $dobPatient")

            tvDob.text = dobPatient

            tvGender.text = detail?.sex.toString()
            if (detail?.address?.isNotEmpty() == true) {
                tvAddress.text = detail.address
            } else tvAddress.text = getString(R.string.Empty)
        }

        binding.apply {
            fabPredict.setOnClickListener {
                binding.apply {
                    val toPrediction = Intent(this@DetailPatientActivity, PredictionActivity::class.java)
                    toPrediction.putExtra(PredictionActivity.EXTRA_ID, detail?.patientid)
                    startActivity(toPrediction)
                }
            }
        }
    }



    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        val backTo = Intent(this, ArchiveActivity::class.java)
        startActivity(backTo)
        return true
    }

    companion object {
        const val EXTRA_NAME = "extra_name"
        const val EXTRA_ID = "extra_id"

    }

}
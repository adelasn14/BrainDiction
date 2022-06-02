package com.example.braindiction.ui.signUp

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.example.braindiction.R
import com.example.braindiction.databinding.ActivitySignUpBinding
import com.example.braindiction.ui.login.LoginActivity
import java.text.SimpleDateFormat
import java.util.*


class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAction()

        // birth date
        val birthEd = binding.textDateBirth
        birthEd.transformIntoDatePicker(this, "dd/MM/yyyy")
        birthEd.transformIntoDatePicker(this, "dd/MM/yyyy", Date())

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
            val logIn = Intent (this, LoginActivity::class.java)
            startActivity(logIn)
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
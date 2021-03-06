package com.example.braindiction.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.braindiction.ui.patient.DetailPatientActivity
import com.example.braindiction.ui.prediction.PredictionActivity

class UserViewModelFactory() :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailPatientActivity::class.java)) {
            return DetailPatientActivity() as T
        }
        if (modelClass.isAssignableFrom(PredictionActivity::class.java)) {
            return PredictionActivity() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}

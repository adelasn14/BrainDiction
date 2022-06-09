package com.example.braindiction.paginglistpatient

import android.content.Context
import com.example.braindiction.api.ApiConfig

object Injection {
    fun provideRepository(context: Context): PatientRepository {
        val apiService = ApiConfig().getApiService()
        return PatientRepository(apiService, context)
    }
}
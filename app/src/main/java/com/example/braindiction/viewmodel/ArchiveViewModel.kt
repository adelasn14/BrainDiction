package com.example.braindiction.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.braindiction.api.ApiConfig
import com.example.braindiction.api.PatientData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ArchiveViewModel : ViewModel() {
    private val _searchPatient = MutableLiveData<ArrayList<PatientData>>()
    val searchPatient: LiveData<ArrayList<PatientData>> = _searchPatient

    fun setSearchPatient(patientid: String) {
        val client = ApiConfig().getApiService().searchPatient(patientid)
        client.enqueue(object : Callback<ArrayList<PatientData>> {
            override fun onResponse(call: Call<ArrayList<PatientData>>, response: Response<ArrayList<PatientData>>) {
                if (response.isSuccessful) {
                    _searchPatient.value = response.body()
                    Log.d("SearchPatient", response.toString())

                }
            }

            override fun onFailure(call: Call<ArrayList<PatientData>>, t: Throwable) {
                Log.e("SearchPatient", "OnFailure : ${t.message}")
            }
        })
    }
}
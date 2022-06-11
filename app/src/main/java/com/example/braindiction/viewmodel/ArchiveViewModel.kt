package com.example.braindiction.viewmodel

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.example.braindiction.api.ApiConfig
import com.example.braindiction.api.PatientData
import com.example.braindiction.api.UploadResponse
import com.example.braindiction.api.User
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class ArchiveViewModel : ViewModel() {
    private val _searchPatient = MutableLiveData<ArrayList<PatientData>>()
    val searchPatient: LiveData<ArrayList<PatientData>> = _searchPatient

    private val _listHistoryPrediction = MutableLiveData<ArrayList<User>>()
    val listHistoryPrediction: LiveData<ArrayList<User>> = _listHistoryPrediction

    fun setSearchPatient(token: String,patientid: String) {
        val client = ApiConfig().getApiService().searchPatient(token,patientid)
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

    fun setHistoryPrediction(token: String, patientid: Int) {
        val client = ApiConfig().getApiService().getHistoryPrediction(token, patientid)
        client.enqueue(object : Callback<ArrayList<User>> {
            override fun onResponse(
                call: Call<ArrayList<User>>,
                response: Response<ArrayList<User>>
            ) {
                if (response.isSuccessful) {
                    _listHistoryPrediction.value = response.body()
                }
            }

            override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }
}
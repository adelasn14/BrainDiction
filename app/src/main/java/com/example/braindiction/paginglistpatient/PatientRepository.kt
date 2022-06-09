package com.example.braindiction.paginglistpatient

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.braindiction.api.ApiService
import com.example.braindiction.api.PatientData

class PatientRepository(private val apiService: ApiService) {
    fun getPatient(): LiveData<PagingData<PatientData>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                PatientListPagingSource(apiService)
            }
        ).liveData
    }
}
package com.example.braindiction.paginglistpatient

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.braindiction.api.PatientData

class PagingPatientsViewModel(patientRepository: PatientRepository) : ViewModel() {
    val allPatient: LiveData<PagingData<PatientData>> =
        patientRepository.getPatient().cachedIn(viewModelScope)

    private val _listPatient = MutableLiveData<List<PatientData>>()
    val listPatient: LiveData<List<PatientData>> = _listPatient

    class PagingViewModelFactory() : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(PagingPatientsViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return PagingPatientsViewModel(Injection.provideRepository()) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
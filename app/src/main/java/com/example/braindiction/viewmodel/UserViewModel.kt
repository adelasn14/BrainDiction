package com.example.braindiction.viewmodel

import androidx.lifecycle.*
import com.example.braindiction.api.IsUserLogin
import com.example.braindiction.UserPreference
import kotlinx.coroutines.launch

class UserViewModel(private val pref: UserPreference) : ViewModel() {
    fun getUser(): LiveData<IsUserLogin> {
        return pref.getUser().asLiveData()
    }

    fun login() {
        viewModelScope.launch {
            pref.login()
        }
    }

}

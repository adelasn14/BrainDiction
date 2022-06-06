package com.example.braindiction.viewmodel

import androidx.lifecycle.*
import com.example.braindiction.api.IsUserLogin
import com.example.braindiction.preference.UserPreference
import kotlinx.coroutines.launch

class UserViewModel(private val pref: UserPreference) : ViewModel() {
    fun saveUser(user: IsUserLogin) {
        viewModelScope.launch {
            pref.saveUser(user)
        }
    }
    fun getUser(): LiveData<IsUserLogin> {
        return pref.getUser().asLiveData()
    }

    fun login() {
        viewModelScope.launch {
            pref.login()
        }
    }

    fun logout() {
        viewModelScope.launch {
            pref.logout()
        }
    }

}

package com.example.braindiction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.braindiction.viewmodel.LoginViewModel
import com.example.braindiction.viewmodel.UserViewModel

class UserViewModelFactory(private val pref: UserPreference) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            return UserViewModel(pref) as T
        }
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}

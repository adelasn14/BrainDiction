package com.example.braindiction.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.example.braindiction.api.ApiConfig
import com.example.braindiction.api.LoginResponse
import com.example.braindiction.UserPreference
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginViewModel() : ViewModel() {
    private val _loginUser = MutableLiveData<LoginResponse>()
    val loginUser: LiveData<LoginResponse> = _loginUser

    companion object {
        private const val TAG = "LoginViewModel"
    }

    fun doUserLogin(email: String, password: String) {
        val client = ApiConfig().getApiService().userLogin(email, password)
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && !responseBody.error) {
                        _loginUser.postValue(response.body())
                    }
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.d(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

}

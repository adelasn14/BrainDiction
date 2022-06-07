package com.example.braindiction.api

import com.google.gson.annotations.SerializedName
import java.util.*
import kotlin.collections.ArrayList

data class UploadResponse(
    val error: Boolean,
    val message: String
)

data class RegisterResponse(
    val error: Boolean,
    val message: String
)

data class LoginResponse(
    val error: Boolean,
    val message: String,
    val LoginResult: LoginResult?
)

data class LoginResult(
    var userId: String,
    var name: String,
    var token: String
)

data class ArchivePatientResponse(
    @SerializedName("JSON_OBJECT")
    val patient: List<PatientData>
)

data class AddNewPatientResponse(
    var name: String? = null,
    var sex: String? = null,
    var dateofbirth: String,
    var address: String? = null
)

data class PatientData(
    var patientid : Int,
    var name: String? = null,
    var dateofbirth: String,
    var sex: Char
)

data class IsUserLogin(
    val name: String,
    val email: String,
    val password: String,
    val isLogin: Boolean
)

data class User(
    val id: Int,
    val username: String,
    val history: Date
)
package com.example.braindiction.api

import android.os.Parcelable
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.util.*

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
    val error: Boolean,
    val message: String,
    val patient: List<PatientData>
)

data class AddNewPatientResponse(
    val error: Boolean,
    val message: String,
    val patient: List<NewPatientData>
)

data class NewPatientData(
    var noRM: String? = null,
    var name: String? = null,
    var tglLahir : Date,
    var jenisKelamin: String? = null,
    var avaUrl: String? = null,
    var alamat: String? = null
)

@Parcelize
data class PatientData(
    @PrimaryKey
    var name: String? = null,
    var tglLahir : Date,
    var jenisKelamin: String? = null,
    var avaUrl: String? = null,
    var alamat: String? = null,
    var predictImg: String? = null
) : Parcelable

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
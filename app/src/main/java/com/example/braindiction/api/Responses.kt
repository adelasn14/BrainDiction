package com.example.braindiction.api

import android.os.Parcelable
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.util.*

data class UploadResponse(
    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String
)

data class RegisterResponse(
    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String
)

data class LoginResponse(
    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("loginResult")
    val LoginResult: LoginResult?
)

data class LoginResult(
    @field:SerializedName("userId")
    var userId: String,

    @field:SerializedName("name")
    var name: String,

    @field:SerializedName("token")
    var token: String
)

data class ArchivePatientResponse(
    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("patient")
    val patient: List<PatientData>
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
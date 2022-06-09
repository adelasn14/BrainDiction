package com.example.braindiction.api

import android.os.Parcelable
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.util.*
import kotlin.collections.ArrayList

data class UploadResponse(
    val error: Boolean,
    val message: String
)

data class ArchivePatientResponse(
    val patient: List<PatientData>
)

data class AddNewPatientResponse(
    var name: String? = null,
    var sex: String? = null,
    var dateofbirth: String,
    var address: String? = null
)

@Parcelize
data class PatientData(
    @PrimaryKey
    var patientid : Int,
    var name: String? = null,
    var sex: Char,
    var dateofbirth: String
) : Parcelable

data class UserRegister(
    val name: String,
    val username: String,
    val email: String,
    val password: String,
    val gender : String,
    val dob : String,
    val address: String
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
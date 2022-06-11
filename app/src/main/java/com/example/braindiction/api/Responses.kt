package com.example.braindiction.api

import android.os.Parcelable
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.util.*
import kotlin.collections.ArrayList

data class UploadResponse(
    val persentage: Float,
    val prediction: String
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
    var dateofbirth: String,
    var address: String? = null
) : Parcelable

data class UserRegister(
    val name: String? = "",
    val username: String? = "",
    val email: String? = "",
    val password: String? = "",
    val gender : String? = "",
    val dob : String? = "",
    val address: String? = "",
    val occupation: String? = ""
)

@Parcelize
data class User(
    @PrimaryKey
    val prediction: String,
    val history: String
) : Parcelable
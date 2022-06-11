package com.example.braindiction.api

import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.*
import java.util.*
import kotlin.collections.ArrayList

interface ApiService {
    @Multipart
    @POST("predict")
    fun uploadXray(
        @Query("patientid") patientid: Int,
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part
    ) : Call<UploadResponse>

    @GET("predictionlist")
    fun getHistoryPrediction(
        @Header("Authorization") token: String,
        @Query("patientid") patientid: Int,
    ): Call<ArrayList<User>>

    @Headers(
        "Content-type:application/json; charset=utf-8"
    )
    @POST("registerpatient")
    fun patientRegister(
        @Header("Authorization") token: String,
        @Body user: AddNewPatientResponse
    ): Call<AddNewPatientResponse>

    @GET("patientlist")
    fun searchPatient(
        @Header("Authorization") token: String,
        @Query("patientid") patientid : String
    ): Call<ArrayList<PatientData>>

    @GET("patientlist")
    suspend fun displayAllPatient(
        @Header("Authorization") token: String,
        @Query("page") page: Int? = 0,
        @Query("size") size: Int? = 5
    ): List<PatientData>
}
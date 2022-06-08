package com.example.braindiction.api

import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.*
import java.util.*
import kotlin.collections.ArrayList

interface ApiService {
    @FormUrlEncoded
    @POST("registerdoc")
    fun userRegister(
        @Field("name") name: String,
        @Field("username") username: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("gender") gender: String,
        @Field("dateofbirth") dob: Date? = null,
        @Part("address") address: RequestBody
    ): Call<RegisterResponse>

    @FormUrlEncoded
    @POST("login")
    fun userLogin(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @Multipart
    @POST("imageupload")
    fun uploadImage(
        @Header("Authorization") authToken: String,
        @Part file: MultipartBody.Part
    ): Call<UploadResponse>

    @GET("predict")
    suspend fun getPredictPatient(
        @Header("Authorization") authToken: String,
        @Header("imageUrl") imgUrl: String,
        @Path("username") username: String
    ): ArchivePatientResponse

    @GET("predict/{username}/history")
    fun getHistoryPrediction(
        @Header("Authorization") token: String,
        @Path("username") username: String,
    ): Call<ArrayList<User>>

    @Headers(
        "Content-type:application/json; charset=utf-8"
    )
    @POST("registerpatient")
    fun patientRegister(
        @Body user: AddNewPatientResponse
    ): Call<AddNewPatientResponse>

    @GET("patientlist")
    fun searchPatient(
        @Query("patientid") patientid : String
    ): Call<ArrayList<PatientData>>

    @GET("patientlist")
    suspend fun displayAllPatient(
        @Query("page") page: Int? = 0,
        @Query("size") size: Int? = 5
    ): List<PatientData>
}
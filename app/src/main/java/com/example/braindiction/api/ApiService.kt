package com.example.capstone.api

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("registerdoc")
    fun userRegister(
        @Field("name") name: String,
        @Field("username") username: String,
        @Field("email") email: String,
        @Field("password") password: String
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

    @POST("registerpatient")
    fun patientRegister(
        @Header("Authorization") authToken: String,
        @Field("noRm") noRM: String,
        @Field("name") name: String,
        @Field("jenisKelamin") jenisKelamin: String,
        @Part("description") description: RequestBody
    ): Call<UploadResponse>
}
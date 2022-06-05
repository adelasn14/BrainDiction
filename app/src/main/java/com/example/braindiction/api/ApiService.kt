package com.example.braindiction.api

import okhttp3.MultipartBody
import okhttp3.RequestBody
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

    @POST("registerpatient")
    @FormUrlEncoded
    fun patientRegister(
        @Field("name") name: String,
        @Field("sex") gender: String,
        @Field("dateofbirth") dob: Date? = null,
        @Field("address") address: String
    ): Call<AddNewPatientResponse>
}
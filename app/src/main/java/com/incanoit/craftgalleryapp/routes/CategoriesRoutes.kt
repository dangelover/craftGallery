package com.incanoit.craftgalleryapp.routes

import com.incanoit.craftgalleryapp.models.Category
import com.incanoit.craftgalleryapp.models.ResponseHttp
import com.incanoit.craftgalleryapp.models.User
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface CategoriesRoutes {
    //vamos a crear la primera ruta que sera el post

    @GET("categories/getAll")
    fun getAll(
        @Header("Authorization") token: String
    ): Call<ArrayList<Category>>
    @Multipart
    @POST("categories/create")
    fun create(
        @Part image: MultipartBody.Part,
        @Part("category") category: RequestBody,
        @Header("Authorization") token: String
    ): Call<ResponseHttp>



}
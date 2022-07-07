package com.incanoit.craftgalleryapp.routes

import com.incanoit.craftgalleryapp.models.Address
import com.incanoit.craftgalleryapp.models.Category
import com.incanoit.craftgalleryapp.models.ResponseHttp
import com.incanoit.craftgalleryapp.models.User
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface AddressRoutes {
    //vamos a crear la primera ruta que sera el post

    @GET("address/findByUser/{id_user}")
    fun getAll(
        @Path("id_user") idUser: String,
        @Header("Authorization") token: String
    ): Call<ArrayList<Address>>
//    @Multipart
    @POST("address/create")
    fun create(
        @Body address: Address,
        @Header("Authorization") token: String
    ): Call<ResponseHttp>



}
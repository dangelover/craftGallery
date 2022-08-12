package com.incanoit.craftgalleryapp.routes

import com.incanoit.craftgalleryapp.models.Category
import com.incanoit.craftgalleryapp.models.Product
import com.incanoit.craftgalleryapp.models.ResponseHttp
import com.incanoit.craftgalleryapp.models.User
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ProductsRoutes {
    //vamos a crear la primera ruta que sera el post

    @GET("products/findByCategory/{id_category}")
    fun findByCategory(
        @Path("id_category") idCategory: String,
        @Header("Authorization") token: String,

    ): Call<ArrayList<Product>>
    @Multipart
    @POST("products/create")
    fun create(
        @Part images: Array<MultipartBody.Part?>,
        @Part("product") product: RequestBody,
        @Header("Authorization") token: String
    ): Call<ResponseHttp>
    @PUT("products/updateStatus/{id_producto}")
    fun updateStatus(
        @Path("id_producto") idProducto: String,
//        @Header("Authorization") token: String
    ): Call<ResponseHttp>



}
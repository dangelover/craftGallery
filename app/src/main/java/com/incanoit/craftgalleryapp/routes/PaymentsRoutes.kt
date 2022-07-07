package com.incanoit.craftgalleryapp.routes

import com.incanoit.craftgalleryapp.models.Category
import com.incanoit.craftgalleryapp.models.MercadoPagoPayment
import com.incanoit.craftgalleryapp.models.ResponseHttp
import com.incanoit.craftgalleryapp.models.User
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface PaymentsRoutes {
    //vamos a crear la primera ruta que sera el post
    @POST("payments/create")
    fun createPayment(
        @Body mercadoPagoPayment: MercadoPagoPayment,
        @Header("Authorization") token: String
    ): Call<ResponseHttp>



}
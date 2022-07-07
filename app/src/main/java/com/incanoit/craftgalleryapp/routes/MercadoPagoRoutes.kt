package com.incanoit.craftgalleryapp.routes

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.incanoit.craftgalleryapp.models.MercadoPagoCardTokenBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface MercadoPagoRoutes {
    @GET("v1/payment_methods/installments?access_token=TEST-7460314714765557-070214-e1b05214476fa3bfe5f0d0bf324868fa-543811213")
    fun getInstallments(@Query("bin") bin: String, @Query("amount") amount: String): Call<JsonArray>
    @POST("v1/card_tokens?public_key=TEST-d551379e-56a7-44a8-aaa9-2b82cfb4402c")
    fun createCardToken(@Body body: MercadoPagoCardTokenBody): Call<JsonObject>
}
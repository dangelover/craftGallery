package com.incanoit.craftgalleryapp.providers

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.incanoit.craftgalleryapp.api.MercadoPagoApiRoutes
import com.incanoit.craftgalleryapp.models.MercadoPagoCardTokenBody
import com.incanoit.craftgalleryapp.routes.MercadoPagoRoutes
import retrofit2.Call

class MercadoPagoProvider {
    var mercadoPagoRoutes:MercadoPagoRoutes?=null
    init {
        val api = MercadoPagoApiRoutes()
        mercadoPagoRoutes=api.getMercadoPagoRoutes()
    }
    fun getInstallment(bin: String, amount: String): Call<JsonArray>?{
        return mercadoPagoRoutes?.getInstallments(bin, amount)
    }
    fun createCardToken(mercadoPagoCardTokenBody: MercadoPagoCardTokenBody): Call<JsonObject>? {
        return mercadoPagoRoutes?.createCardToken(mercadoPagoCardTokenBody)
    }

}
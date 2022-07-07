package com.incanoit.craftgalleryapp.providers

import android.util.Log
import com.incanoit.craftgalleryapp.api.ApiRoutes
import com.incanoit.craftgalleryapp.models.Category
import com.incanoit.craftgalleryapp.models.MercadoPagoPayment
import com.incanoit.craftgalleryapp.models.ResponseHttp
import com.incanoit.craftgalleryapp.models.User
import com.incanoit.craftgalleryapp.routes.CategoriesRoutes
import com.incanoit.craftgalleryapp.routes.PaymentsRoutes
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import java.io.File

class PaymentsProvider(val token:String) {
    private var paymentsRoutes: PaymentsRoutes?=null
    init {
        val api = ApiRoutes()
        paymentsRoutes = api.getPaymentsRoutes(token)
    }
    fun create(mercadoPagoPayment: MercadoPagoPayment): Call<ResponseHttp>? {
        return paymentsRoutes?.createPayment(mercadoPagoPayment,token)

    }
}
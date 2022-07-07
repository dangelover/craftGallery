package com.incanoit.craftgalleryapp.providers

import android.util.Log
import com.incanoit.craftgalleryapp.api.ApiRoutes
import com.incanoit.craftgalleryapp.models.Address
import com.incanoit.craftgalleryapp.models.Category
import com.incanoit.craftgalleryapp.models.ResponseHttp
import com.incanoit.craftgalleryapp.models.User
import com.incanoit.craftgalleryapp.routes.AddressRoutes
import com.incanoit.craftgalleryapp.routes.CategoriesRoutes
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import java.io.File

class AddressProvider(val token:String) {
    private var addressRoutes: AddressRoutes?=null
    init {
        val api = ApiRoutes()
        addressRoutes = api.getAddressRoutes(token)
    }
    fun getAddress(idUser: String): Call<ArrayList<Address>>?{
        return addressRoutes?.getAll(idUser,token)
    }
    fun create(address: Address): Call<ResponseHttp>? {
        return addressRoutes?.create(address, token)

    }
}
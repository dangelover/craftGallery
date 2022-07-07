package com.incanoit.craftgalleryapp.providers

import android.util.Log
import com.incanoit.craftgalleryapp.api.ApiRoutes
import com.incanoit.craftgalleryapp.models.Category
import com.incanoit.craftgalleryapp.models.ResponseHttp
import com.incanoit.craftgalleryapp.models.User
import com.incanoit.craftgalleryapp.routes.CategoriesRoutes
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import java.io.File

class CategoriesProvider(val token:String) {
    private var categoriesRoute: CategoriesRoutes?=null
    init {
        val api = ApiRoutes()
        categoriesRoute = api.getCategoriesRoutes(token)
    }
    fun getAll(): Call<ArrayList<Category>>?{
        return categoriesRoute?.getAll(token)
    }
    fun create(file: File, category: Category): Call<ResponseHttp>? {
        Log.d("UsersProvider","$file")
        Log.d("UsersProvider","$category")
        val reqFile = RequestBody.create(MediaType.parse("image/*"), file)
        Log.d("UsersProvider","$reqFile")
        val image = MultipartBody.Part.createFormData("image", file.name, reqFile)
        Log.d("UsersProvider","$image")
        val requestBody = RequestBody.create(MediaType.parse("text/plain"),category.toJson())
        return categoriesRoute?.create(image,requestBody,token!!)
    }
}
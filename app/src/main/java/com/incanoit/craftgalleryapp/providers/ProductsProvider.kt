package com.incanoit.craftgalleryapp.providers

import android.util.Log
import com.incanoit.craftgalleryapp.api.ApiRoutes
import com.incanoit.craftgalleryapp.models.Category
import com.incanoit.craftgalleryapp.models.Product
import com.incanoit.craftgalleryapp.models.ResponseHttp
import com.incanoit.craftgalleryapp.models.User
import com.incanoit.craftgalleryapp.routes.CategoriesRoutes
import com.incanoit.craftgalleryapp.routes.ProductsRoutes
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import java.io.File

class ProductsProvider(val token:String? = null) {
    private var productsRoutes: ProductsRoutes?=null
    private var productsRoutesWithToken: ProductsRoutes?=null
    init {
        val api = ApiRoutes()
        productsRoutesWithToken=api.getProductsRoutesWithToken()
        if (token!=null){
            productsRoutes = api.getProductsRoutes(token)
        }
    }
    fun findByCategory(idCategory: String): Call<ArrayList<Product>>?{
        return productsRoutes?.findByCategory(idCategory,token!!)
    }
    //creamos una funcion y le pasamos como parametro la lista de archivos de images y el product
    fun create(files: List<File>, product: Product): Call<ResponseHttp>? {
        val images = arrayOfNulls<MultipartBody.Part>(files.size)
        //creamos una lista de imagenes y
    //recorremos cada elemento del arreglo
        for(i in 0 until files.size){
            val reqFile = RequestBody.create(MediaType.parse("image/*"), files[i])
            images[i] = MultipartBody.Part.createFormData("image", files[i].name, reqFile)
        }
        val requestBody = RequestBody.create(MediaType.parse("text/plain"),product.toJson())
        return productsRoutes?.create(images,requestBody,token!!)
    }
    fun updateStatus(idProducto:String):Call<ResponseHttp>?{
        Log.d("ProductProvider","${idProducto}")
        return productsRoutesWithToken?.updateStatus(idProducto)
    }
}
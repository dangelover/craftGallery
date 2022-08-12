package com.incanoit.craftgalleryapp.routes

import android.util.Log
import com.incanoit.craftgalleryapp.models.Category
import com.incanoit.craftgalleryapp.models.ResponseHttp
import com.incanoit.craftgalleryapp.models.User
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface UsersRoutes {
    @GET("users/getAllUser/{id_user}")
    fun getUserByID(
        @Path("id_user") idUsuario: String,
//        @Header("Authorization") token: String
    ): Call<ResponseHttp>
    @GET("users/getAll")
    fun getAll(
        @Header("Authorization") token: String
    ):Call<ArrayList<User>>
    @GET("users/findDeliveryMen")
    fun getDeliveryMen(
        @Header("Authorization") token: String
    ): Call<ArrayList<User>>
    //vamos a crear la primera ruta que sera el post
    @POST("users/create")
    //funcion para registrar un usuario
    //en el Body tenemos que enviar el users
    //ahora esta funcion recibe como parametro un user de tipo User
    //para registrar un usuario tenemos que enviar todos los datos del usuario
    //y vamos a mapear la respuesta
    fun register(@Body user:User): Call<ResponseHttp>
    @FormUrlEncoded
    @POST("users/login")
    fun login(@Field("email")email:String, @Field("password")password:String): Call<ResponseHttp>
    @Multipart
    @PUT("users/update")
    fun update(
        @Part image: MultipartBody.Part,
        @Part("user") user: RequestBody,
        @Header("Authorization") token: String
    ): Call<ResponseHttp>
    @PUT("users/updateStatus/{id_user}")
    fun updateStatus(
        @Path("id_user") idUsuario: String,
//        @Header("Authorization") token: String
    ): Call<ResponseHttp>
    @PUT("users/updateWithoutImage")
    fun updateWithoutImage(
        @Body user:User,
        @Header("Authorization") token: String
    ): Call<ResponseHttp>

}
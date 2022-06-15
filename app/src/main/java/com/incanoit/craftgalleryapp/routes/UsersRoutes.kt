package com.incanoit.craftgalleryapp.routes

import com.incanoit.craftgalleryapp.models.ResponseHttp
import com.incanoit.craftgalleryapp.models.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface UsersRoutes {
    //vamos a crear la primera ruta que sera el post
    @POST("users/create")
    //funcion para registrar un usuario
    //en el Body tenemos que enviar el users
    //ahora esta funcion recibe como parametro un user de tipo User
    //para registrar un usuario tenemos que enviar todos los datos del usuario
    //y vamos a mapear la respuesta
    fun register(@Body user:User): Call<ResponseHttp>

}
package com.incanoit.craftgalleryapp.api

import com.incanoit.craftgalleryapp.routes.UsersRoutes

class ApiRoutes {
    val API_URL = "http://192.168.87.2:8080/api/"
    val retrofit = RetrofitClient()
    fun getUsersRoutes(): UsersRoutes{
        //USAMOS EL METODO GETCLIENT Y VAMOS PASARLE LA URL
        //y ahora usamos el metodo create y el pasamos la clase UsersRoutes
        //esta va a ser la funcion que cree las rutas
        return retrofit.getClient(API_URL).create(UsersRoutes::class.java)
    }
}
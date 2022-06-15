package com.incanoit.craftgalleryapp.providers

import com.incanoit.craftgalleryapp.api.ApiRoutes
import com.incanoit.craftgalleryapp.models.ResponseHttp
import com.incanoit.craftgalleryapp.models.User
import com.incanoit.craftgalleryapp.routes.UsersRoutes
import retrofit2.Call

class UsersProvider {
    //vamos a crear una variable de tipo UserRoutes que sera nulo por el momento
    private var usersRoutes: UsersRoutes?=null
    //vamos a inicializarlo
    //este metodo sera el primero en inicializarse
    init {
        val api = ApiRoutes()
        //vamos a llamar nuestras rutas
        usersRoutes = api.getUsersRoutes()
    }
    //creamos una funcion que va a recibir una variable de tipo User de nuestro modelo
    //y va a retor
    //usamos el ? para indicar que este metodo puede retonar un nulo
    fun register(user: User): Call<ResponseHttp>?{
        return usersRoutes?.register(user)
    }

}
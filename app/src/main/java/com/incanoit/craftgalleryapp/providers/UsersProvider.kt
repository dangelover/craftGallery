package com.incanoit.craftgalleryapp.providers

import android.util.Log
import com.incanoit.craftgalleryapp.api.ApiRoutes
import com.incanoit.craftgalleryapp.models.Category
import com.incanoit.craftgalleryapp.models.ResponseHttp
import com.incanoit.craftgalleryapp.models.User
import com.incanoit.craftgalleryapp.routes.UsersRoutes
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import java.io.File

class UsersProvider(val token:String? = null) {
    //vamos a crear una variable de tipo UserRoutes que sera nulo por el momento
    private var usersRoutes: UsersRoutes?=null
    private var usersRoutesToken: UsersRoutes?=null
    //vamos a inicializarlo
    //este metodo sera el primero en inicializarse
    init {
        val api = ApiRoutes()
        //vamos a llamar nuestras rutas
        usersRoutes = api.getUsersRoutes()
        if (token!=null){
            Log.d("UsersProvider","$token")
            usersRoutesToken=api.getUsersRoutesWithToken(token!!)
        }
    }
    fun getAllUser(): Call<ArrayList<User>>?{
        return usersRoutesToken?.getAll(token!!)
    }
    fun getDeliveryMen(): Call<ArrayList<User>>?{
        return usersRoutesToken?.getDeliveryMen(token!!)
    }
    //creamos una funcion que va a recibir una variable de tipo User de nuestro modelo
    //y va a retor
    //usamos el ? para indicar que este metodo puede retonar un nulo
    fun register(user: User): Call<ResponseHttp>?{
        return usersRoutes?.register(user)
    }
    fun login(email: String, password:String): Call<ResponseHttp>?{
        return usersRoutes?.login(email, password)
    }
    fun updateWithoutImage(user: User): Call<ResponseHttp>?{
        return usersRoutesToken?.updateWithoutImage(user,token!!)
    }
    fun update(file: File, user: User): Call<ResponseHttp>? {
        Log.d("UsersProvider","$file")
        Log.d("UsersProvider","$user")
        val reqFile = RequestBody.create(MediaType.parse("image/*"), file)
        Log.d("UsersProvider","$reqFile")
        val image = MultipartBody.Part.createFormData("image", file.name, reqFile)
        Log.d("UsersProvider","$image")
        val requestBody = RequestBody.create(MediaType.parse("text/plain"),user.toJson())
        Log.d("UsersProvider","$requestBody")
        Log.d("UsersProvider","$token")
        return usersRoutesToken?.update(image,requestBody,token!!)
    }
    fun updateStatus(idUsuario:String): Call<ResponseHttp>?{
        Log.d("UsersProvider","${idUsuario}")
        return usersRoutes?.updateStatus(idUsuario)
    }
    fun getUserByID(idUsuario: String): Call<ResponseHttp>?{
        Log.d("UsersProvider","${idUsuario}")
        return usersRoutes?.getUserByID(idUsuario)

    }

}
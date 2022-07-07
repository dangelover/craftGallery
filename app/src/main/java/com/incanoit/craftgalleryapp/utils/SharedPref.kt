package com.incanoit.craftgalleryapp.utils

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import java.lang.Exception

//clase para almacenar la session del usuario
class SharedPref(activity: Activity) {
    private var prefs:SharedPreferences?=null
    //de esta forma vamos a inicializar la variable SharedPreferences
    init {
        //esta variable va a ser igual al activity y usamos este metodo para recibir como parametro
        //una referencia de nuestro paquete principal
        prefs=activity.getSharedPreferences("com.incanoit.craftgalleryapp", Context.MODE_PRIVATE)
    }
    //vamos a crear una funcion para guardar la informacion de session en nuestro dispositivo
    //este recibe dos parametros, el key y un objeto
    fun save(key:String,objeto:Any){
        try {
            val gson = Gson()
            //vamos a convertir el objeto que recibimos por parametro
            val json = gson.toJson(objeto)
            with(prefs?.edit()){
                //vamos a guardar un string y le pasamos el key y el json
                this?.putString(key,json)
                //ahora con el metodo commit vamos a guardar toda esta informacion
                this?.commit()

            }

        }catch (e: Exception){
            Log.d("Error","Err: ${e.message}")

        }

    }
    //el key es el id o tambien es el nombre
    fun getData(key:String):String?{
        val data = prefs?.getString(key,"")
        return data

    }
    fun remove(key:String){
        prefs?.edit()?.remove(key)?.apply()
    }


}
package com.incanoit.craftgalleryapp.models

import android.util.Log
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

class User(
    //mediante el SerializedName tenemos que especificar a que campo vamos a rellenar, y tiene que ser el mismo que el de la bd
    @SerializedName("id") var id: String? =null,
    @SerializedName("email") var email: String,
    @SerializedName("name") var name: String,
    @SerializedName("dni") var dni: String,
    @SerializedName("fecha_nacimiento") var fechaNacimiento: String,
    @SerializedName("lastname") var lastname: String,
    @SerializedName("phone") var phone: String,
    @SerializedName("image") var image: String? =null,
    @SerializedName("is_availabe") var isAvailabe: Boolean? = null,
    @SerializedName("session_token") var sessionToken: String? =null,
    @SerializedName("password") var password: String,
    @SerializedName("addres") var addres: String? =null,
    @SerializedName("years_market") var yearsMarket: String? =null,
    @SerializedName("id_rol") var idRol: String? =null,
    @SerializedName("code_rna") var codeRna: String? =null,
    @SerializedName("certificate") var certificate: String? =null,
    @SerializedName("roles") var roles: ArrayList<Rol>?=null
) {
    //creamos el metodo toString
    override fun toString(): String {
//        Log.d("User", dni)
        return "$name $dni  $lastname $fechaNacimiento $image "
    }
    fun toJson():String {
        return Gson().toJson(this)
    }
}
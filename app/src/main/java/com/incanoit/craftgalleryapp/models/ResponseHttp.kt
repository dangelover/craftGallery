package com.incanoit.craftgalleryapp.models

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
//esta clase permite va a devolver las respuestas desde el backend
class ResponseHttp (
    @SerializedName("message") val message: String,
    @SerializedName("success") val isSuccess: Boolean,
    @SerializedName("data") val data: JsonObject,
    @SerializedName("error") val error: String,
){
    //creamos el metodo toString
    override fun toString(): String {
        return "ResponseHttp(message='$message', isSuccess=$isSuccess, data=$data, error='$error')"
    }
}
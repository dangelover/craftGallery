package com.incanoit.craftgalleryapp.models

import com.google.gson.annotations.SerializedName

class User(
    //mediante el SerializedName tenemos que especificar a que campo vamos a rellenar, y tiene que ser el mismo que el de la bd
    @SerializedName("id") val id: String? =null,
    @SerializedName("email") val email: String,
    @SerializedName("name") val name: String,
    @SerializedName("DNI") val dni: String,
    @SerializedName("fecha_nacimiento") val fechaNacimiento: String,
    @SerializedName("lastname") val lastname: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("image") val image: String? =null,
    @SerializedName("is_availabe") val isAvailabe: Boolean? = null,
    @SerializedName("session_token") val sessionToken: String? =null,
    @SerializedName("password") val password: String,
    @SerializedName("addres") val addres: String? =null,
    @SerializedName("years_market") val yearsMarket: String? =null,
    @SerializedName("code_rna") val codeRna: String? =null,
    @SerializedName("certificate") val certificate: String? =null,
) {
    //creamos el metodo toString
    override fun toString(): String {
        return "User(id='$id', email='$email', name='$name', dni='$dni', fechaNacimiento='$fechaNacimiento', lastname='$lastname', phone='$phone', image='$image', isAvailabe=$isAvailabe, sessionToken='$sessionToken', password='$password', addres='$addres', yearsMarket='$yearsMarket', codeRna='$codeRna', certificate='$certificate')"
    }
}
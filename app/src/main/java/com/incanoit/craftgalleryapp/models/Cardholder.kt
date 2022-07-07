package com.incanoit.craftgalleryapp.models

import com.google.gson.annotations.SerializedName

class Cardholder(
    @SerializedName("name") val name: String
) {

    override fun toString(): String {
        return "CardHolder( name='$name')"
    }
}
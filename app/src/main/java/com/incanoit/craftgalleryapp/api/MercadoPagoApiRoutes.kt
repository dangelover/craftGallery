package com.incanoit.craftgalleryapp.api

import com.incanoit.craftgalleryapp.routes.*

class MercadoPagoApiRoutes {
    val API_URL = "https://api.mercadopago.com/"
    val retrofit = RetrofitClient()
    fun getMercadoPagoRoutes(): MercadoPagoRoutes{
        //USAMOS EL METODO GETCLIENT Y VAMOS PASARLE LA URL
        //y ahora usamos el metodo create y el pasamos la clase UsersRoutes
        //esta va a ser la funcion que cree las rutas
        return retrofit.getClient(API_URL).create(MercadoPagoRoutes::class.java)
    }

}
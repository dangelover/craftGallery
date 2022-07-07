package com.incanoit.craftgalleryapp.api

import com.incanoit.craftgalleryapp.routes.*

class ApiRoutes {
    val API_URL = "http://192.168.87.2:8080/api/"
    val retrofit = RetrofitClient()
    fun getUsersRoutes(): UsersRoutes{
        //USAMOS EL METODO GETCLIENT Y VAMOS PASARLE LA URL
        //y ahora usamos el metodo create y el pasamos la clase UsersRoutes
        //esta va a ser la funcion que cree las rutas
        return retrofit.getClient(API_URL).create(UsersRoutes::class.java)
    }
    fun getUsersRoutesWithToken(token: String): UsersRoutes{
        //USAMOS EL METODO GETCLIENT Y VAMOS PASARLE LA URL
        //y ahora usamos el metodo create y el pasamos la clase UsersRoutes
        //esta va a ser la funcion que cree las rutas
        return retrofit.getClientWithToken(API_URL,token).create(UsersRoutes::class.java)
    }
    fun getCategoriesRoutes(token: String): CategoriesRoutes{
        //USAMOS EL METODO GETCLIENT Y VAMOS PASARLE LA URL
        //y ahora usamos el metodo create y el pasamos la clase UsersRoutes
        //esta va a ser la funcion que cree las rutas
        return retrofit.getClientWithToken(API_URL,token).create(CategoriesRoutes::class.java)
    }
    fun getAddressRoutes(token: String): AddressRoutes{
        //USAMOS EL METODO GETCLIENT Y VAMOS PASARLE LA URL
        //y ahora usamos el metodo create y el pasamos la clase UsersRoutes
        //esta va a ser la funcion que cree las rutas
        return retrofit.getClientWithToken(API_URL,token).create(AddressRoutes::class.java)
    }
    fun getOrdersRoutes(token: String): OrdersRoutes{
        //USAMOS EL METODO GETCLIENT Y VAMOS PASARLE LA URL
        //y ahora usamos el metodo create y el pasamos la clase UsersRoutes
        //esta va a ser la funcion que cree las rutas
        return retrofit.getClientWithToken(API_URL,token).create(OrdersRoutes::class.java)
    }
    fun getProductsRoutes(token: String): ProductsRoutes{
        //USAMOS EL METODO GETCLIENT Y VAMOS PASARLE LA URL
        //y ahora usamos el metodo create y el pasamos la clase UsersRoutes
        //esta va a ser la funcion que cree las rutas
        return retrofit.getClientWithToken(API_URL,token).create(ProductsRoutes::class.java)
    }fun getPaymentsRoutes(token: String): PaymentsRoutes{
        //USAMOS EL METODO GETCLIENT Y VAMOS PASARLE LA URL
        //y ahora usamos el metodo create y el pasamos la clase UsersRoutes
        //esta va a ser la funcion que cree las rutas
        return retrofit.getClientWithToken(API_URL,token).create(PaymentsRoutes::class.java)
    }
}
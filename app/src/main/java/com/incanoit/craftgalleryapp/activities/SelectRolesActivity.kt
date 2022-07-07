package com.incanoit.craftgalleryapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.incanoit.craftgalleryapp.R
import com.incanoit.craftgalleryapp.adapters.RolesAdapater
import com.incanoit.craftgalleryapp.models.User
import com.incanoit.craftgalleryapp.utils.SharedPref

class SelectRolesActivity : AppCompatActivity() {
    var recyclerViewRoles:RecyclerView?=null
    var user:User?=null
    var adapter: RolesAdapater?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_roles)
        recyclerViewRoles=findViewById(R.id.recyclerview_roles)
        //usando el LinearLayoutManager debemos mostrar el cardViewRoles
        recyclerViewRoles?.layoutManager=LinearLayoutManager(this)
        getUsersFromSession()
        //usamos la claseRolesAdapter le pasamos el contexto y el arraylist de roles
        //le indicamos que puede ser nulo y con !! que debe ser no nula
        adapter= RolesAdapater(this,user?.roles!!)
        recyclerViewRoles?.adapter=adapter
    }
    private fun getUsersFromSession(){
        val sharedPref = SharedPref(this)
        val gson = Gson()
        //usamos el SharedPref y usando el metodo getData vamos a obtener informacion del usuario
        //le pasamos el key o el nombre con el que guardamos la informacion
        //si el usuario existe en sesion entonce lo manda a la pantalla del cliente
        if (!sharedPref.getData("user").isNullOrEmpty()){
            //SI EL USUARIO EXISTE EN SESION
            user = gson.fromJson(sharedPref.getData("user"), User::class.java)


        }
    }
}
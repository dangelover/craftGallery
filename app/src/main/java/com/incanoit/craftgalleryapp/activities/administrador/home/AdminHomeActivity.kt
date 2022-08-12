package com.incanoit.craftgalleryapp.activities.administrador.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import com.incanoit.craftgalleryapp.R
import com.incanoit.craftgalleryapp.activities.MainActivity
import com.incanoit.craftgalleryapp.fragments.admins.AdminHomeFragment
import com.incanoit.craftgalleryapp.fragments.admins.AdminUsersFragment
import com.incanoit.craftgalleryapp.fragments.client.ClientCategoriesFragment
import com.incanoit.craftgalleryapp.fragments.client.ClientOrdersFragment
import com.incanoit.craftgalleryapp.fragments.client.ClientProfileFragment
import com.incanoit.craftgalleryapp.models.User
import com.incanoit.craftgalleryapp.utils.SharedPref

class AdminHomeActivity : AppCompatActivity() {
    private val TAG = "ClientHomeActivity"
    //    var buttonLogout: Button?=null
    var sharedPref:SharedPref?=null
    var bottomNavigation: BottomNavigationView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_home)
        sharedPref= SharedPref(this )
//        buttonLogout = findViewById(R.id.btn_logout)
//        buttonLogout?.setOnClickListener {logout()}
        openFragment(ClientCategoriesFragment())
        bottomNavigation = findViewById(R.id.bottom_navigation)
        bottomNavigation?.setOnNavigationItemSelectedListener {
            when (it.itemId){
                //si el usuario selecciono el home
                R.id.item_home ->{
                    openFragment(AdminHomeFragment())
                    true
                }
                R.id.item_users ->{
                    openFragment(AdminUsersFragment())
                    true
                }
                R.id.item_profile ->{
                    openFragment(ClientProfileFragment())
                    true
                }
                else -> false
            }
        }

        getUsersFromSession()
    }
    private fun openFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        //ahora usando el metodo replace el pasamos el id del container que queremos reemplazar
        //y como segundo parametro le pasamos el fragment que queremos reemplazar
        transaction.replace(R.id.container,fragment)
        transaction.addToBackStack(null)
        transaction.commit()

    }
    private fun  logout(){
        //usamos el metodo remove de la clase sharedPref y el pasamos el key
        sharedPref?.remove("user")
        val i = Intent(this,MainActivity::class.java)
        startActivity(i)

    }
    private fun getUsersFromSession(){

        val gson = Gson()
        //usamos el SharedPref y usando el metodo getData vamos a obtener informacion del usuario
        //le pasamos el key o el nombre con el que guardamos la informacion
        if (!sharedPref?.getData("user").isNullOrEmpty()){
            //SI EL USUARIO EXISTE EN SESION
            val user = gson.fromJson(sharedPref?.getData("user"),User::class.java)
            Log.d(TAG,"Usuario $user")

        }
    }
}
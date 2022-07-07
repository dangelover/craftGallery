package com.incanoit.craftgalleryapp.activities.client.shoping_bag

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.incanoit.craftgalleryapp.R
import com.incanoit.craftgalleryapp.activities.client.addres.create.ClientAddressCreateActivity
import com.incanoit.craftgalleryapp.activities.client.addres.list.ClientAddressListActivity
import com.incanoit.craftgalleryapp.adapters.ShoppingBagAdapater
import com.incanoit.craftgalleryapp.models.Product
import com.incanoit.craftgalleryapp.utils.SharedPref

class ClientBagActivity : AppCompatActivity() {
    var recyclerViewShoppingBag: RecyclerView ?=null
    var textviewTotal: TextView?=null
    var buttonNext: Button?=null
    var toolbar: Toolbar?=null
    var adapter: ShoppingBagAdapater?=null
    var sharedPref: SharedPref? = null
    var gson = Gson()
    var selectProducts = ArrayList<Product>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_bag)
        sharedPref= SharedPref(this)
        recyclerViewShoppingBag = findViewById(R.id.recyclerview_shopping_barg)
        textviewTotal = findViewById(R.id.textview_total)
        buttonNext = findViewById((R.id.btn_next))
        toolbar = findViewById(R.id.toolbar)
        toolbar?.setTitleTextColor(ContextCompat.getColor(this,R.color.white))
        toolbar?.title="My Cart"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        recyclerViewShoppingBag?.layoutManager=LinearLayoutManager(this)
        buttonNext?.setOnClickListener {
            goToAddressList()
        }
        getProductsFromSharedPref()
    }
    private fun goToAddressList(){
        val i = Intent(this, ClientAddressListActivity::class.java)
        startActivity(i)
    }

    fun setTotal(total: Double){
        textviewTotal?.text = "S/. ${total}"
    }
    private fun getProductsFromSharedPref(){
        //si en el sharedPref existe un elemento guardado con la key order
        if (!sharedPref?.getData("order").isNullOrBlank()){ // EXISTE UNA ORDEN EN SHAREDPREF
            //vamos a transformar una lista de tipo json a una arreglo de tipo Product
            val type = object: TypeToken<ArrayList<Product>>() {}.type
            //usamos el metodo fromJson para transformar de json a arraylist
            selectProducts = gson.fromJson(sharedPref?.getData("order"),type)
            adapter= ShoppingBagAdapater(this,selectProducts)
            recyclerViewShoppingBag?.adapter=adapter


        }
    }
}
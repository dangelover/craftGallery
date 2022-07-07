package com.incanoit.craftgalleryapp.activities.client.products.list

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.incanoit.craftgalleryapp.R
import com.incanoit.craftgalleryapp.adapters.ProductsAdapater
import com.incanoit.craftgalleryapp.models.Product
import com.incanoit.craftgalleryapp.models.User
import com.incanoit.craftgalleryapp.providers.ProductsProvider
import com.incanoit.craftgalleryapp.utils.SharedPref
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ClientProductsListActivity : AppCompatActivity() {
    var TAG="ClientProducts"
    var recyclerViewProducts: RecyclerView?=null
    var adapter:ProductsAdapater?=null
    var user:User?=null
    var sharedPref: SharedPref?=null
    var productsProvider: ProductsProvider?=null
    var products: ArrayList<Product> = ArrayList()
    var idCategory: String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_products_list)
        sharedPref= SharedPref(this )
        //ahora optenemos el valor que nos pasaron desde el otro activity
        //mediante el getStringExtra y el pasamos el nombre de la variable
        idCategory=intent.getStringExtra("idCategory")
        getUsersFromSession()
        productsProvider = ProductsProvider(user?.sessionToken!!)
        recyclerViewProducts = findViewById(R.id.recyclerview_products)
        //usamos el metodo GridLayoutManager y le pasamos el contexto y cuantas columnas queremos que nos muestre
        recyclerViewProducts?.layoutManager = GridLayoutManager(this,2)
        getProducts()
    }
    private fun getUsersFromSession(){

        val gson = Gson()
        //usamos el SharedPref y usando el metodo getData vamos a obtener informacion del usuario
        //le pasamos el key o el nombre con el que guardamos la informacion
        if (!sharedPref?.getData("user").isNullOrEmpty()){
            //SI EL USUARIO EXISTE EN SESION
            user = gson.fromJson(sharedPref?.getData("user"), User::class.java)
            Log.d("ClientUpdateActivity","${user}")

        }
    }
    private fun getProducts(){
        productsProvider?.findByCategory(idCategory!!)?.enqueue(object: Callback<ArrayList<Product>>{
            override fun onResponse(
                call: Call<ArrayList<Product>>,
                response: Response<ArrayList<Product>>
            ) {
                if (response.body() != null){
                    //a la lista de productos le asigamos lo que trae el response.body()
                    products = response.body()!!
                    adapter= ProductsAdapater(this@ClientProductsListActivity,products)
                    recyclerViewProducts?.adapter=adapter
                }
            }

            override fun onFailure(call: Call<ArrayList<Product>>, t: Throwable) {
                Toast.makeText(this@ClientProductsListActivity,t.message ,Toast.LENGTH_SHORT).show()
                Log.d(TAG,"Error ${t.message}")
            }

        })
    }
}
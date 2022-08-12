package com.incanoit.craftgalleryapp.activities.ceramica.products.list

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.incanoit.craftgalleryapp.R
import com.incanoit.craftgalleryapp.adapters.ProductsAdapater
import com.incanoit.craftgalleryapp.adapters.ProductsArtezanoAdapater
import com.incanoit.craftgalleryapp.models.Product
import com.incanoit.craftgalleryapp.models.User
import com.incanoit.craftgalleryapp.providers.ProductsProvider
import com.incanoit.craftgalleryapp.utils.SharedPref
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CeramicaProductsListActivity : AppCompatActivity() {
    val TAG=" CeramicaProducts"
    var recyclerViewProductsArtezania:RecyclerView?=null
    var adapter:ProductsArtezanoAdapater?=null
    var user:User?=null
    var sharedPref:SharedPref?=null
    var productsProvider:ProductsProvider?=null
    var products: ArrayList<Product> = ArrayList()
    var idCategory:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ceramica_products_list)
        idCategory=intent.getStringExtra("idCategory")
        sharedPref= SharedPref(this)
        getUsersFromSession()
        productsProvider= ProductsProvider(user?.sessionToken!!)
        recyclerViewProductsArtezania=findViewById(R.id.recyclerview_products_ceramica)
        recyclerViewProductsArtezania?.layoutManager= GridLayoutManager(this,2)
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
                if (response.body()!==null){
                    products=response.body()!!
                    adapter=ProductsArtezanoAdapater(this@CeramicaProductsListActivity,products)
                    recyclerViewProductsArtezania?.adapter=adapter
                }
            }

            override fun onFailure(call: Call<ArrayList<Product>>, t: Throwable) {
                Log.d(TAG,"Error ${t.message}")
                Toast.makeText(this@CeramicaProductsListActivity, "${t.message}", Toast.LENGTH_LONG).show()
            }

        })
    }
}
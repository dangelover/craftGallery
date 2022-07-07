package com.incanoit.craftgalleryapp.activities.client.addres.list

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.incanoit.craftgalleryapp.R
import com.incanoit.craftgalleryapp.activities.client.addres.create.ClientAddressCreateActivity
import com.incanoit.craftgalleryapp.activities.client.payments.form.ClientPaymentFormActivity
import com.incanoit.craftgalleryapp.adapters.AddressAdapater
import com.incanoit.craftgalleryapp.adapters.ShoppingBagAdapater
import com.incanoit.craftgalleryapp.models.*
import com.incanoit.craftgalleryapp.providers.AddressProvider
import com.incanoit.craftgalleryapp.providers.OrdersProvider
import com.incanoit.craftgalleryapp.utils.SharedPref
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ClientAddressListActivity : AppCompatActivity() {
    var fabCreateAddress: FloatingActionButton?=null
    var toolbar:Toolbar?=null
    var recyclerViewAddress: RecyclerView?=null
    var adapter: AddressAdapater?=null
    var addressProvider:AddressProvider?=null
    var ordersProvider: OrdersProvider?=null
    var btnNextPayment: Button?=null
    var sharedPref: SharedPref?=null
    var user: User?=null
    var address = ArrayList<Address>()
    val gson = Gson()
    var selectProducts = ArrayList<Product>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_address_list)
        sharedPref = SharedPref(this)
        getProductsFromSharedPref()
        fabCreateAddress=findViewById(R.id.fab_address_create)
        toolbar = findViewById(R.id.toolbar)
        btnNextPayment = findViewById(R.id.btn_next_payment)
        btnNextPayment?.setOnClickListener { getAddressFromSession() }
        recyclerViewAddress = findViewById(R.id.recyclerview_address)
        recyclerViewAddress?.layoutManager = LinearLayoutManager(this)
        toolbar?.setTitleTextColor(ContextCompat.getColor(this ,R.color.black))
        toolbar?.title="Mis direcciones"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        getUsersFromSession()
        addressProvider= AddressProvider(user?.sessionToken!!)
        ordersProvider= OrdersProvider(user?.sessionToken!!)
        fabCreateAddress?.setOnClickListener { goToAddressCreate() }
        getAddress()
    }
    private fun getProductsFromSharedPref(){
        //si en el sharedPref existe un elemento guardado con la key order
        if (!sharedPref?.getData("order").isNullOrBlank()){ // EXISTE UNA ORDEN EN SHAREDPREF
            //vamos a transformar una lista de tipo json a una arreglo de tipo Product
            val type = object: TypeToken<ArrayList<Product>>() {}.type
            //usamos el metodo fromJson para transformar de json a arraylist
            selectProducts = gson.fromJson(sharedPref?.getData("order"),type)

        }
    }
    private fun createOrder(idAddress: String){
        goToPaymentsForm()
//        val order = Order(
//            products= selectProducts,
//            idClient = user?.id!!,
//            idAddress = idAddress
//        )
//        ordersProvider?.create(order)?.enqueue(object: Callback<ResponseHttp>{
//            override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>) {
//                if (response.body() != null){
//                    Toast.makeText(this@ClientAddressListActivity, "${response.body()?.message}", Toast.LENGTH_LONG).show()
//                }
//                else{
//                    Toast.makeText(this@ClientAddressListActivity, "Ocurrio un error en la peticion", Toast.LENGTH_LONG).show()
//                }
//            }
//            override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
//                Toast.makeText(this@ClientAddressListActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
//            }
//
//        })
    }
    private fun getAddressFromSession(){
        if (!sharedPref?.getData("address").isNullOrBlank()){
            val a = gson.fromJson(sharedPref?.getData("address"), Address::class.java) // SI EXISTE UNA DIRECCION
            createOrder(a.id!!)
//            goToPaymentsForm()
        }else{
            Toast.makeText(this, "Selecciona una direccion para continuar", Toast.LENGTH_SHORT).show()
        }
    }
    private fun goToPaymentsForm(){
        val i = Intent(this,ClientPaymentFormActivity::class.java)
        startActivity(i)
    }
    fun resetValue(position: Int){
        val viewHolder = recyclerViewAddress?.findViewHolderForAdapterPosition(position) // obtenemos una direccion en especifico
        val view = viewHolder?.itemView
        val imageViewCheck = view?.findViewById<ImageView>(R.id.imageview_check)
        imageViewCheck?.visibility= View.GONE
    }
    private fun getAddress(){
        addressProvider?.getAddress(user?.id!!)?.enqueue(object: Callback<ArrayList<Address>>{
            override fun onResponse(
                call: Call<ArrayList<Address>>,
                response: Response<ArrayList<Address>>
            ) {
                if (response.body()!=null){
                    address = response.body()!!
                    adapter = AddressAdapater(this@ClientAddressListActivity,address )
                    recyclerViewAddress?.adapter=adapter

                }

            }

            override fun onFailure(call: Call<ArrayList<Address>>, t: Throwable) {
                Toast.makeText(this@ClientAddressListActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
            }

        })
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
    private fun goToAddressCreate(){
        val i = Intent(this, ClientAddressCreateActivity::class.java)
        startActivity(i)
    }
}
package com.incanoit.craftgalleryapp.activities.delivery.orders.detail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.incanoit.craftgalleryapp.R
import com.incanoit.craftgalleryapp.activities.ceramica.home.CeramicaHomeActivity
import com.incanoit.craftgalleryapp.activities.delivery.home.DeliveryHomeActivity
import com.incanoit.craftgalleryapp.activities.delivery.orders.map.DeliveryOrdersMapActivity
import com.incanoit.craftgalleryapp.adapters.OrderProductsAdapater
import com.incanoit.craftgalleryapp.models.Category
import com.incanoit.craftgalleryapp.models.Order
import com.incanoit.craftgalleryapp.models.ResponseHttp
import com.incanoit.craftgalleryapp.models.User
import com.incanoit.craftgalleryapp.providers.OrdersProvider
import com.incanoit.craftgalleryapp.providers.UsersProvider
import com.incanoit.craftgalleryapp.utils.SharedPref
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DeliveryOrdersDetailActivity : AppCompatActivity() {
    val TAG = "DeliveryOrdersDetail"
    var order: Order?=null
    val gson = Gson()
    var toolbar: Toolbar?=null
    var textViewClient: TextView?=null
    var textViewAddress: TextView?=null
    var textViewDate: TextView?=null
    var textViewTotal: TextView?=null
    var textViewStatus: TextView?=null
    var recyclerViewProducts: RecyclerView?=null
    var buttonUpdate: Button?=null
    var buttonGoToMap: Button?=null
    var adapter: OrderProductsAdapater?=null
    var usersProvider: UsersProvider?=null
    var ordersProvider: OrdersProvider?=null
    var user: User?=null
    var sharedPref: SharedPref?=null
    var idDelivery = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delivery_orders_detail)
        sharedPref= SharedPref(this)
        order = gson.fromJson(intent.getStringExtra("order"),Order::class.java)
        textViewClient=findViewById(R.id.textview_client)
        textViewAddress=findViewById(R.id.textview_address_detail)
        textViewDate=findViewById(R.id.textview_date_detail)
        textViewStatus=findViewById(R.id.textview_status)
        textViewTotal=findViewById(R.id.textview_total_detail)
        recyclerViewProducts=findViewById(R.id.recyclerview_products)
        recyclerViewProducts?.layoutManager=LinearLayoutManager(this)
        buttonUpdate=findViewById(R.id.btn_update_status)
        buttonGoToMap=findViewById(R.id.btn_go_to_map)
        getUsersFromSession()
        usersProvider= UsersProvider(user?.sessionToken)
        ordersProvider= OrdersProvider(user?.sessionToken!!)
        adapter= OrderProductsAdapater(this,order?.products!!)
        recyclerViewProducts?.adapter=adapter
        toolbar=findViewById(R.id.toolbar)
        toolbar?.setTitleTextColor(ContextCompat.getColor(this,R.color.black))
        toolbar?.title="Orden #${order?.id}"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        textViewClient?.text="${order?.client?.name} ${order?.client?.lastname}"
        textViewAddress?.text=order?.address?.address
        textViewDate?.text="${order?.timestamp}"
        textViewStatus?.text=order?.status
        getTotal()
//        getDeliveryMen()
        if (order?.status=="DESPACHADO"){
            buttonUpdate?.visibility=View.VISIBLE
        }
        if (order?.status=="EN CAMINO"){
            buttonGoToMap?.visibility=View.VISIBLE
        }
        buttonUpdate?.setOnClickListener {
            updateOrder()
        }
        buttonGoToMap?.setOnClickListener { goToMap() }

        Log.d(TAG,"Orden: ${order.toString()}")
    }
    private fun updateOrder(){
        ordersProvider?.updateToOnTheWay(order!!)?.enqueue(object: Callback<ResponseHttp>{
            override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>) {
                if (response.body()!=null){

                    if (response.body()?.isSuccess==true){
                        Toast.makeText(this@DeliveryOrdersDetailActivity, "Entrega iniciada", Toast.LENGTH_LONG).show()
                        goToMap()
                    }else{
                        Toast.makeText(this@DeliveryOrdersDetailActivity, "No se pudo asignar repartidor", Toast.LENGTH_LONG).show()
                    }

                }else{
                    Toast.makeText(this@DeliveryOrdersDetailActivity, "No hubo respuesta del servidor", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                Log.d(TAG,"Error: ${t.message}")
                Toast.makeText(this@DeliveryOrdersDetailActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
            }

        })

    }
    private fun goToMap(){
        val i =Intent(this,DeliveryOrdersMapActivity::class.java)
        i.putExtra("order",order?.toJson())
        startActivity(i)
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
//    private fun getDeliveryMen(){
//        usersProvider?.getDeliveryMen()?.enqueue(object: Callback<ArrayList<User>>{
//            override fun onResponse(
//                call: Call<ArrayList<User>>,
//                response: Response<ArrayList<User>>
//            ) {
//                if (response.body()!=null){
//                    val deliveryMen = response.body()
//                    val arrayAdapter = ArrayAdapter<User>(this@DeliveryOrdersDetailActivity,android.R.layout.simple_dropdown_item_1line, deliveryMen!!)
//                    spinnerDeliveryMen?.adapter=arrayAdapter
//                    spinnerDeliveryMen?.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
//                        override fun onItemSelected(
//                            adapterView: AdapterView<*>?,
//                            view: View?,
//                            position: Int,
//                            l: Long
//                        ) {
//                            idDelivery = deliveryMen[position].id!! //seleccionando del spinner el id del delivery
//                            Log.d(TAG,"ID Delivery: $idDelivery")
//                        }
//
//                        override fun onNothingSelected(p0: AdapterView<*>?) {
//
//                        }
//
//                    }
//                }
//            }
//
//            override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {
//                Toast.makeText(this@DeliveryOrdersDetailActivity, "${t.message}", Toast.LENGTH_SHORT).show()
//            }
//
//        })
//    }
    private fun getTotal(){
        var total = 0.0
        for(p in order?.products!!){
            total = total + (p.price*p.quantity!!)
        }
        textViewTotal?.text="S/. ${total}"
    }
}
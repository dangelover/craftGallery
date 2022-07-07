package com.incanoit.craftgalleryapp.activities.client.orders.detail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.incanoit.craftgalleryapp.R
import com.incanoit.craftgalleryapp.activities.client.orders.map.ClientOrdersMapActivity
import com.incanoit.craftgalleryapp.activities.delivery.orders.map.DeliveryOrdersMapActivity
import com.incanoit.craftgalleryapp.adapters.OrderProductsAdapater
import com.incanoit.craftgalleryapp.models.Order

class ClientOrdersDetailActivity : AppCompatActivity() {
    val TAG = "ClientOrdersDetail"
    var order: Order?=null
    val gson = Gson()
    var toolbar: Toolbar?=null
    var textViewClient: TextView?=null
    var textViewAddress: TextView?=null
    var textViewDate: TextView?=null
    var textViewTotal: TextView?=null
    var textViewStatus: TextView?=null
    var recyclerViewProducts: RecyclerView?=null
    var adapter: OrderProductsAdapater?=null
    var buttonGoToMap: Button?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_orders_detail)
        textViewClient=findViewById(R.id.textview_client)
        textViewAddress=findViewById(R.id.textview_address_detail)
        textViewDate=findViewById(R.id.textview_date_detail)
        textViewStatus=findViewById(R.id.textview_status)
        textViewTotal=findViewById(R.id.textview_total_detail)
        recyclerViewProducts=findViewById(R.id.recyclerview_products)
        recyclerViewProducts?.layoutManager=LinearLayoutManager(this)
        order = gson.fromJson(intent.getStringExtra("order"),Order::class.java)
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
        buttonGoToMap=findViewById(R.id.btn_go_to_map_client)
        getTotal()
        if (order?.status=="EN CAMINO"){
            Log.d(TAG,"entra aqui")
            buttonGoToMap?.visibility=View.VISIBLE
        }
        buttonGoToMap?.setOnClickListener { goToMap() }

        Log.d(TAG,"Orden: ${order.toString()}")
    }
    private fun goToMap(){
        val i = Intent(this, ClientOrdersMapActivity::class.java)
        i.putExtra("order",order?.toJson())
        startActivity(i)
    }
    private fun getTotal(){
        var total = 0.0
        for(p in order?.products!!){
            total = total + (p.price*p.quantity!!)
        }
        textViewTotal?.text="S/. ${total}"
    }
}
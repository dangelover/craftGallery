package com.incanoit.craftgalleryapp.adapters

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.incanoit.craftgalleryapp.R
import com.incanoit.craftgalleryapp.activities.ceramica.home.CeramicaHomeActivity
import com.incanoit.craftgalleryapp.activities.ceramica.orders.detail.RestaurantOrdersDetailActivity
import com.incanoit.craftgalleryapp.activities.client.addres.list.ClientAddressListActivity
import com.incanoit.craftgalleryapp.activities.client.home.ClientHomeActivity
import com.incanoit.craftgalleryapp.activities.client.orders.detail.ClientOrdersDetailActivity
import com.incanoit.craftgalleryapp.activities.client.products.list.ClientProductsListActivity
import com.incanoit.craftgalleryapp.activities.delivery.home.DeliveryHomeActivity
import com.incanoit.craftgalleryapp.activities.delivery.orders.detail.DeliveryOrdersDetailActivity
import com.incanoit.craftgalleryapp.models.Address
import com.incanoit.craftgalleryapp.models.Category
import com.incanoit.craftgalleryapp.models.Order
import com.incanoit.craftgalleryapp.models.Rol
import com.incanoit.craftgalleryapp.utils.SharedPref

//creamos esta clase que va a recibir el activity y el array de roles
class OrdersDeliveryAdapater(val context:Activity, val orders:ArrayList<Order>):RecyclerView.Adapter<OrdersDeliveryAdapater.OrdersViewHolder>() {
    val sharedPref= SharedPref(context)
    val gson= Gson()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrdersViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cardview_orders_ceramica, parent,false)
        return OrdersViewHolder(view)
    }
    //este metodo va a retornar la cantidad de roles que tenemos
    override fun getItemCount(): Int {
        return orders.size
    }

    override fun onBindViewHolder(holder: OrdersViewHolder, position: Int) {
        val order = orders[position]//obtenemos cada una de las ordenes

//        Log.d("CategoriesAdapater","$a")
        holder.textViewOrderId.text="Orden #${order.id}"
        holder.textViewDate.text="${order.timestamp}"
        holder.textViewAddress.text="${order.address?.address}"
        holder.textViewClientName.text="${order.client?.name} ${order.client?.lastname}"
        holder.itemView.setOnClickListener {
            goToOrderDetail(order)

        }
    }
    private fun goToOrderDetail(order:Order){
        val i = Intent(context,DeliveryOrdersDetailActivity::class.java)
        i.putExtra("order",order.toJson())
        context.startActivity(i)

    }

    class OrdersViewHolder(view: View): RecyclerView.ViewHolder(view) {

        val textViewOrderId: TextView
        val textViewDate: TextView
        val textViewAddress: TextView
        val textViewClientName: TextView

        init {
            textViewOrderId= view.findViewById(R.id.textview_order_id)
            textViewDate= view.findViewById(R.id.textview_date_order)
            textViewAddress = view.findViewById(R.id.textview_address_order)
            textViewClientName = view.findViewById(R.id.textview_client_name)
        }

    }
}
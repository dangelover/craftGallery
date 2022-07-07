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
import com.incanoit.craftgalleryapp.activities.client.addres.list.ClientAddressListActivity
import com.incanoit.craftgalleryapp.activities.client.home.ClientHomeActivity
import com.incanoit.craftgalleryapp.activities.client.products.list.ClientProductsListActivity
import com.incanoit.craftgalleryapp.activities.delivery.home.DeliveryHomeActivity
import com.incanoit.craftgalleryapp.models.Address
import com.incanoit.craftgalleryapp.models.Category
import com.incanoit.craftgalleryapp.models.Rol
import com.incanoit.craftgalleryapp.utils.SharedPref

//creamos esta clase que va a recibir el activity y el array de roles
class AddressAdapater(val context:Activity, val address:ArrayList<Address>):RecyclerView.Adapter<AddressAdapater.AddressViewHolder>() {
    val sharedPref= SharedPref(context)
    val gson= Gson()
    var prev = 0
    var positionAddressSesion = 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cardview_address, parent,false)
        return AddressViewHolder(view)
    }
    //este metodo va a retornar la cantidad de roles que tenemos
    override fun getItemCount(): Int {
        return address.size
    }

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        val a = address[position]//obtenemos cada una de las categorias
        if (!sharedPref.getData("address").isNullOrBlank()){
            //si el usuario ya eligio una direccion de la lista
            val adr = gson.fromJson(sharedPref.getData("address"), Address::class.java)
            if (adr.id==a.id){
                positionAddressSesion=position
                holder.imageViewCheck.visibility = View.VISIBLE

            }
        }
        Log.d("CategoriesAdapater","$a")
        holder.textViewAddress.text=a.address
        holder.textViewNeighborhoord.text=a.neighborhood
        holder.itemView.setOnClickListener {
            (context as ClientAddressListActivity).resetValue(prev)
            (context as ClientAddressListActivity).resetValue(positionAddressSesion)
            prev=position
            holder.imageViewCheck.visibility=View.VISIBLE
            saveAddress(a.toJson())
        }
    }
    private fun saveAddress(data: String){
        val ad = gson.fromJson(data, Address::class.java)
        sharedPref.save("address",ad)

    }

    class AddressViewHolder(view: View): RecyclerView.ViewHolder(view) {

        val textViewAddress: TextView
        val textViewNeighborhoord: TextView
        val imageViewCheck: ImageView

        init {
            textViewAddress = view.findViewById(R.id.textview_address_list)
            textViewNeighborhoord = view.findViewById(R.id.textview_neighbordhood_list)
            imageViewCheck = view.findViewById(R.id.imageview_check)
        }

    }
}
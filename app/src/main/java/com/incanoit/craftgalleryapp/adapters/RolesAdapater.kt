package com.incanoit.craftgalleryapp.adapters

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.incanoit.craftgalleryapp.R
import com.incanoit.craftgalleryapp.activities.ceramica.home.CeramicaHomeActivity
import com.incanoit.craftgalleryapp.activities.client.home.ClientHomeActivity
import com.incanoit.craftgalleryapp.activities.delivery.home.DeliveryHomeActivity
import com.incanoit.craftgalleryapp.models.Rol
import com.incanoit.craftgalleryapp.utils.SharedPref

//creamos esta clase que va a recibir el activity y el array de roles
class RolesAdapater(val context:Activity, val roles:ArrayList<Rol>):RecyclerView.Adapter<RolesAdapater.RolesViewHolder>() {
    val sharedPref= SharedPref(context)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RolesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cardview_roles, parent,false)
        return RolesViewHolder(view)
    }
    //este metodo va a retornar la cantidad de roles que tenemos
    override fun getItemCount(): Int {
        return roles.size
    }

    override fun onBindViewHolder(holder: RolesViewHolder, position: Int) {
        val rol = roles[position]//nos va a devolver cada rol del arreglo roles
        holder.textViewRol.text=rol.name
        //cargamos la librerira glide y le pasamos el contexto y en el load le pasamos la imagen que queremos mostrar
        //y en el into en donde queremos mostrar
        Glide.with(context).load(rol.image).into(holder.imageViewRol)
        holder.itemView.setOnClickListener {
            goToRol(rol)

        }
    }
    private fun goToRol(rol:Rol){
        if (rol.name=="CERAMICA"){
            sharedPref.save("rol","CERAMICA")
            val i = Intent(context, CeramicaHomeActivity::class.java)
            context.startActivity(i)
        } else if (rol.name=="CLIENTE"){
            sharedPref.save("rol","CLIENTE")
            val i = Intent(context, ClientHomeActivity::class.java)
            context.startActivity(i)
        } else if (rol.name=="REPARTIDOR"){
            sharedPref.save("rol","REPARTIDOR")
            val i = Intent(context, DeliveryHomeActivity::class.java)
            context.startActivity(i)
        }

    }

    class RolesViewHolder(view: View): RecyclerView.ViewHolder(view){
        val textViewRol: TextView
        val imageViewRol: ImageView
        init {
            textViewRol=view.findViewById(R.id.textview_rol)
            imageViewRol=view.findViewById(R.id.imageview_rol)
        }
    }
}
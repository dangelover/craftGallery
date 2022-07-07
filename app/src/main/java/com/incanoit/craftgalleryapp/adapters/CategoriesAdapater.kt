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
import com.incanoit.craftgalleryapp.R
import com.incanoit.craftgalleryapp.activities.ceramica.home.CeramicaHomeActivity
import com.incanoit.craftgalleryapp.activities.client.home.ClientHomeActivity
import com.incanoit.craftgalleryapp.activities.client.products.list.ClientProductsListActivity
import com.incanoit.craftgalleryapp.activities.delivery.home.DeliveryHomeActivity
import com.incanoit.craftgalleryapp.models.Category
import com.incanoit.craftgalleryapp.models.Rol
import com.incanoit.craftgalleryapp.utils.SharedPref

//creamos esta clase que va a recibir el activity y el array de roles
class CategoriesAdapater(val context:Activity, val categories:ArrayList<Category>):RecyclerView.Adapter<CategoriesAdapater.CategoriesViewHolder>() {
    val sharedPref= SharedPref(context)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cardview_categories, parent,false)
        return CategoriesViewHolder(view)
    }
    //este metodo va a retornar la cantidad de roles que tenemos
    override fun getItemCount(): Int {
        return categories.size
    }

    override fun onBindViewHolder(holder: CategoriesViewHolder, position: Int) {
        val category = categories[position]//obtenemos cada una de las categorias
        Log.d("CategoriesAdapater","$category")
        holder.textViewCategory.text=category.name
        //cargamos la librerira glide y le pasamos el contexto y en el load le pasamos la imagen que queremos mostrar
        //y en el into en donde queremos mostrar
        Glide.with(context).load(category.image).into(holder.imageViewCategory)
        holder.itemView.setOnClickListener {
            goToProducts(category)

        }
    }
    private fun goToProducts(category:Category){
        val i = Intent(context, ClientProductsListActivity::class.java)
        //mediante el metodo putExtra le agregamos un nuevo parametro que queremos enviar
        //el primer parametro es el nombre de la variable y el segundo parametro es el valor de la variable
        i.putExtra("idCategory",category.id)
        context.startActivity(i)


    }
    class CategoriesViewHolder(view: View): RecyclerView.ViewHolder(view) {

        val textViewCategory: TextView
        val imageViewCategory: ImageView

        init {
            textViewCategory = view.findViewById(R.id.textview_category_list)
            imageViewCategory = view.findViewById(R.id.imageview_category_list)
        }

    }
}
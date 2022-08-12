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
import com.incanoit.craftgalleryapp.activities.administrador.home.AdminHomeActivity
import com.incanoit.craftgalleryapp.activities.ceramica.home.CeramicaHomeActivity
import com.incanoit.craftgalleryapp.activities.ceramica.products.list.CeramicaProductsListActivity
import com.incanoit.craftgalleryapp.activities.client.home.ClientHomeActivity
import com.incanoit.craftgalleryapp.activities.delivery.home.DeliveryHomeActivity
import com.incanoit.craftgalleryapp.fragments.ceramica.CeramicaListProductsFragment
import com.incanoit.craftgalleryapp.models.Category
import com.incanoit.craftgalleryapp.models.Rol
import com.incanoit.craftgalleryapp.utils.SharedPref

//creamos esta clase que va a recibir el activity y el array de roles
class CategoriesArtezanosAdapater(val context:Activity, val categories:ArrayList<Category>):RecyclerView.Adapter<CategoriesArtezanosAdapater.CategoriesViewHolder>() {
    val sharedPref= SharedPref(context)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cardview_categories_artezano, parent,false)
        return CategoriesViewHolder(view)
    }
    //este metodo va a retornar la cantidad de roles que tenemos
    override fun getItemCount(): Int {
        return categories.size
    }

    override fun onBindViewHolder(holder: CategoriesViewHolder, position: Int) {
        val category = categories[position]//nos va a devolver cada rol del arreglo roles
        holder.textViewCategoryArtezano.text=category.name
        //cargamos la librerira glide y le pasamos el contexto y en el load le pasamos la imagen que queremos mostrar
        //y en el into en donde queremos mostrar
        Glide.with(context).load(category.image).into(holder.imageViewCategoryArtezano)
        holder.itemView.setOnClickListener {
            goToProductArtezano(category)

        }
    }
    private fun goToProductArtezano(category: Category){
        val i = Intent(context, CeramicaProductsListActivity::class.java)
        i.putExtra("idCategory",category.id)
        context.startActivity(i)

    }

    class CategoriesViewHolder(view: View): RecyclerView.ViewHolder(view){
        val textViewCategoryArtezano: TextView
        val imageViewCategoryArtezano: ImageView
        init {
            textViewCategoryArtezano=view.findViewById(R.id.textview_categoy_ceramica)
            imageViewCategoryArtezano=view.findViewById(R.id.imageview_category_ceramica)
        }
    }
}
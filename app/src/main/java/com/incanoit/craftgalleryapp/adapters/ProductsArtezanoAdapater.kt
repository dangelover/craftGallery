package com.incanoit.craftgalleryapp.adapters

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.incanoit.craftgalleryapp.R
import com.incanoit.craftgalleryapp.activities.ceramica.home.CeramicaHomeActivity
import com.incanoit.craftgalleryapp.activities.client.home.ClientHomeActivity
import com.incanoit.craftgalleryapp.activities.client.products.list.ClientProductsListActivity
import com.incanoit.craftgalleryapp.activities.delivery.home.DeliveryHomeActivity
import com.incanoit.craftgalleryapp.models.Category
import com.incanoit.craftgalleryapp.models.Product
import com.incanoit.craftgalleryapp.models.ResponseHttp
import com.incanoit.craftgalleryapp.models.Rol
import com.incanoit.craftgalleryapp.providers.ProductsProvider
import com.incanoit.craftgalleryapp.utils.SharedPref
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

//creamos esta clase que va a recibir el activity y el array de roles
class ProductsArtezanoAdapater(val context:Activity, val products:ArrayList<Product>):RecyclerView.Adapter<ProductsArtezanoAdapater.ProductsArtezanoViewHolder>() {
    val sharedPref= SharedPref(context)
    val productsProvider = ProductsProvider()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsArtezanoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cardview_products_artezano, parent,false)
        return ProductsArtezanoViewHolder(view)
    }
    //este metodo va a retornar la cantidad de roles que tenemos
    override fun getItemCount(): Int {
        return products.size
    }

    override fun onBindViewHolder(holder: ProductsArtezanoViewHolder, position: Int) {
        val product = products[position]//obtenemos cada una de las categorias
        Log.d("ProductsAdapater","${product.price}")
        holder.textViewNameProductArtezano.text=product.name
        holder.textViewPriceProductArtezano.text="S/. ${product.price}"
        //cargamos la librerira glide y le pasamos el contexto y en el load le pasamos la imagen que queremos mostrar
        //y en el into en donde queremos mostrar
        Glide.with(context).load(product.image1).into(holder.imageViewProductArtezano)
        holder.imageViewProductDelete.setOnClickListener{
            deleteProduct(product)

        }
//        holder.itemView.setOnClickListener {
//            goToProducts(category)
//
//        }
    }
//    private fun goToProducts(category:Category){
//        val i = Intent(context, ClientProductsListActivity::class.java)
//        //mediante el metodo putExtra le agregamos un nuevo parametro que queremos enviar
//        //el primer parametro es el nombre de la variable y el segundo parametro es el valor de la variable
//        i.putExtra("idCategory",category.id)
//        context.startActivity(i)
//
//
//    }
    private fun deleteProduct(product: Product){
        Log.d("ProductsAdapater","${product.id}")
        productsProvider.updateStatus(product.id!!)?.enqueue(object: Callback<ResponseHttp>{
            override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>) {
                Log.d("ProductsAdapater","RESPONSE: $response")
                Log.d("ProductsAdapater","RESPONSE: ${response.body()}")
                if (response.body()?.isSuccess==true){
                    Toast.makeText(context, "El estado se actualizo", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                Log.d("ProductsAdapater","Error: ${t.message}")
            }

        })

    }
    class ProductsArtezanoViewHolder(view: View): RecyclerView.ViewHolder(view) {

        val textViewNameProductArtezano: TextView
        val textViewPriceProductArtezano: TextView
        val imageViewProductArtezano: ImageView
        val imageViewProductEdit: ImageView
        val imageViewProductDelete: ImageView

        init {
            textViewNameProductArtezano=view.findViewById(R.id.textview_name_product_art)
            textViewPriceProductArtezano=view.findViewById(R.id.textview_price_product_art)
            imageViewProductArtezano=view.findViewById(R.id.imageview_product_list)
            imageViewProductEdit=view.findViewById(R.id.image_view_edit_product)
            imageViewProductDelete=view.findViewById(R.id.image_view_delete_product)
        }

    }
}
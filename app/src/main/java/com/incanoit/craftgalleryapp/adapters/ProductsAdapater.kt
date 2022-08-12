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
import com.incanoit.craftgalleryapp.activities.client.home.ClientHomeActivity
import com.incanoit.craftgalleryapp.activities.client.products.detail.ClientProductsDetailActivity
import com.incanoit.craftgalleryapp.activities.delivery.home.DeliveryHomeActivity
import com.incanoit.craftgalleryapp.models.*
import com.incanoit.craftgalleryapp.providers.UsersProvider
import com.incanoit.craftgalleryapp.utils.SharedPref
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

//creamos esta clase que va a recibir el activity y el array de roles
class ProductsAdapater(val context:Activity, val products:ArrayList<Product>):RecyclerView.Adapter<ProductsAdapater.ProductsViewHolder>() {
    val sharedPref= SharedPref(context)
    var usersProvider = UsersProvider()
    var idUsuario:String?=null
    var user:User?=null
    var image:String?=null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cardview_product, parent,false)
        return ProductsViewHolder(view)
    }
    //este metodo va a retornar la cantidad de roles que tenemos
    override fun getItemCount(): Int {
        return products.size
    }

    override fun onBindViewHolder(holder: ProductsViewHolder, position: Int) {
        val product = products[position]//obtenemos cada una de las categorias
        Log.d("Products","${product.usuario}")
//        Log.d("Products","${user?.image}")
        holder.textViewName.text=product.name
        holder.textViewPrice.text="S/. ${product.price}"
        for(user in product.usuario!!){
            Log.d("Products","${user.image}")
            image=user.image
            Glide.with(context).load(image).into(holder.imageViewUser)
            holder.textViewArtezano.text=user.name
        }
        getUserId(product.idUsuario)
        //cargamos la librerira glide y le pasamos el contexto y en el load le pasamos la imagen que queremos mostrar
        //y en el into en donde queremos mostrar
        Glide.with(context).load(product.image1).into(holder.imageViewProduct)

        holder.itemView.setOnClickListener {
            goToDetail(product)

        }
    }
    private fun goToDetail(product:Product){
        val i = Intent(context, ClientProductsDetailActivity::class.java)
        i.putExtra("product",product.toJson())
        context.startActivity(i)


    }
    private fun getUserId(idUser: String){
        Log.d("Products","$idUser")
        usersProvider.getUserByID(idUser)?.enqueue(object: Callback<ResponseHttp>{
            override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>) {
//                Log.d("Products","Response: ${response.body()?.data}")
                artezanoUser(response.body()?.data.toString())

            }

            override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                Log.d("Products","Error: ${t.message}")
            }

        })


    }
    private fun artezanoUser(data:String){
        val gson = Gson()
        user= gson.fromJson(data,User::class.java)
        Log.d("Products","${user}")


    }
    class ProductsViewHolder(view: View): RecyclerView.ViewHolder(view) {

        val textViewName: TextView
        val textViewPrice: TextView
        val imageViewProduct: ImageView
        val imageViewUser: ImageView
        val textViewArtezano: TextView

        init {
            textViewName = view.findViewById(R.id.textview_name_product)
            textViewPrice = view.findViewById(R.id.textview_price_product)
            imageViewProduct = view.findViewById(R.id.imageview_product)
            imageViewUser=view.findViewById(R.id.imgview_user)
            textViewArtezano=view.findViewById(R.id.textview_artezano)
        }

    }
}
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
import com.incanoit.craftgalleryapp.activities.client.products.detail.ClientProductsDetailActivity
import com.incanoit.craftgalleryapp.activities.client.shoping_bag.ClientBagActivity
import com.incanoit.craftgalleryapp.activities.delivery.home.DeliveryHomeActivity
import com.incanoit.craftgalleryapp.models.Category
import com.incanoit.craftgalleryapp.models.Product
import com.incanoit.craftgalleryapp.models.Rol
import com.incanoit.craftgalleryapp.utils.SharedPref

//creamos esta clase que va a recibir el activity y el array de roles
class ShoppingBagAdapater(val context:Activity, val products:ArrayList<Product>):RecyclerView.Adapter<ShoppingBagAdapater.ShoppingBagViewHolder>() {
    val sharedPref= SharedPref(context)
    init {
        (context as ClientBagActivity).setTotal(getTotal())
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingBagViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cardview_shopping_bag, parent,false)
        return ShoppingBagViewHolder(view)
    }
    //este metodo va a retornar la cantidad de roles que tenemos
    override fun getItemCount(): Int {
        return products.size
    }

    override fun onBindViewHolder(holder: ShoppingBagViewHolder, position: Int) {
        val product = products[position]//obtenemos cada una de las categorias
        Log.d("CategoriesAdapater","$product")
        holder.textViewName.text=product.name
        holder.textViewCounter.text="${product.quantity}"
        if (product.quantity!=null){
            holder.textViewPrice.text="S/. ${product.price*product.quantity!!}"
        }
        //cargamos la librerira glide y le pasamos el contexto y en el load le pasamos la imagen que queremos mostrar
        //y en el into en donde queremos mostrar
        Glide.with(context).load(product.image1).into(holder.imageViewProduct)
        holder.imageViewAdd.setOnClickListener { addItem(product,holder) }
        holder.imageViewRemove.setOnClickListener { removeItem(product,holder) }
        holder.imageViewDelete.setOnClickListener { deleteItem(position) }
//        holder.itemView.setOnClickListener {
//            goToDetail(product)
//
//        }
    }
    private fun getTotal(): Double{
        var total = 0.0
        for (p in products){
            if (p.quantity != null){
                total=total+(p.quantity!!*p.price)
            }

        }
        return total
    }
    private fun getIndexOf(idProduct: String): Int{
        var pos = 0
        for(p in products){
            if (p.id==idProduct){
                return pos
            }
            pos++
        }
        return -1

    }
    private fun deleteItem(position: Int){
        products.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeRemoved(position, products.size)
        sharedPref.save("order",products)
        (context as ClientBagActivity).setTotal(getTotal())
    }
    private fun addItem(product: Product, holder:ShoppingBagViewHolder){
        val index = getIndexOf(product.id!!)
        product.quantity = product.quantity!!+1
        products[index].quantity=product.quantity

        holder.textViewCounter.text="${product?.quantity}"
        holder.textViewPrice.text = "S/. ${product.quantity!!*product.price}"
        sharedPref.save("order",products)
        (context as ClientBagActivity).setTotal(getTotal())
    }
    private fun removeItem(product: Product, holder: ShoppingBagViewHolder){
        if (product.quantity!! > 1){
            val index = getIndexOf(product.id!!)
            product.quantity = product.quantity!!-1
            products[index].quantity=product.quantity

            holder.textViewCounter.text="${product?.quantity}"
            holder.textViewPrice.text = "S/. ${product.quantity!!*product.price}"
            sharedPref.save("order",products)
            (context as ClientBagActivity).setTotal(getTotal())
        }


    }
    private fun goToDetail(product:Product){
        val i = Intent(context, ClientProductsDetailActivity::class.java)
        i.putExtra("product",product.toJson())
        context.startActivity(i)


    }
    class ShoppingBagViewHolder(view: View): RecyclerView.ViewHolder(view) {

        val textViewName: TextView
        val textViewPrice: TextView
        val textViewCounter: TextView
        val imageViewProduct: ImageView
        val imageViewAdd: ImageView
        val imageViewRemove: ImageView
        val imageViewDelete: ImageView

        init {
            textViewName = view.findViewById(R.id.textview_name)
            textViewPrice = view.findViewById(R.id.textview_price)
            textViewCounter = view.findViewById(R.id.textview_counter)
            imageViewProduct = view.findViewById(R.id.image_view_product)
            imageViewAdd = view.findViewById(R.id.image_view_add)
            imageViewRemove = view.findViewById(R.id.image_view_remove)
            imageViewDelete=view.findViewById(R.id.image_view_delete)
        }

    }
}
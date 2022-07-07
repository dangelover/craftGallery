package com.incanoit.craftgalleryapp.activities.client.products.detail

import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.incanoit.craftgalleryapp.R
import com.incanoit.craftgalleryapp.models.Product
import com.incanoit.craftgalleryapp.utils.SharedPref

class ClientProductsDetailActivity : AppCompatActivity() {
    val TAG = "ProductsDetail"
    var product: Product?=null
    val gson = Gson()
    var imageSlider: ImageSlider?=null
    var textViewName: TextView?=null
    var textViewDescription: TextView?=null
    var textViewPrice: TextView?=null
    var textViewCounter: TextView?=null
    var imageViewAdd: ImageView?=null
    var imageViewRemove: ImageView?=null
    var buttonAdd: Button?=null
    var counter = 1
    var productPrice = 0.0
    var sharedPref: SharedPref?=null
    var selectProducts = ArrayList<Product>()
    var cantidad = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_products_detail)
        product = gson.fromJson(intent.getStringExtra("product"), Product::class.java)
        sharedPref = SharedPref(this)
        imageSlider=findViewById(R.id.imageslider)
        textViewName=findViewById(R.id.textview_name_detail)
        textViewDescription=findViewById(R.id.textview_description_detail)
        textViewPrice=findViewById(R.id.textview_price_detail)
        textViewCounter=findViewById(R.id.textview_counter_detail)
        imageViewAdd=findViewById(R.id.imageview_add)
        imageViewRemove=findViewById(R.id.imageview_remove)
        buttonAdd = findViewById(R.id.btn_add_product)
        val imageList = ArrayList<SlideModel>()
        imageList.add(SlideModel(product?.image1, ScaleTypes.CENTER_CROP))
        imageList.add(SlideModel(product?.image2, ScaleTypes.CENTER_CROP))
        imageList.add(SlideModel(product?.image3, ScaleTypes.CENTER_CROP))
        imageSlider?.setImageList(imageList)
        textViewName?.text = product?.name
        textViewPrice?.text = "S/. ${product?.price}"
        textViewDescription?.text = product?.description
        imageViewAdd?.setOnClickListener {
            addItem()
        }
        imageViewRemove?.setOnClickListener {
            removeItem()
        }
        buttonAdd?.setOnClickListener { addToBag() }
        getProductsFromSharedPref()
    }
    private fun addToBag(){
        val index = getIndexOf(product?.id!!)//obtenemos el indice del producto si esque existe en sharedpref
        //si es que nos devuelve -1 es porque no encontro en sharedpref
        if (index==-1){ //ESTE PRODUCTO NO EXISTE AUN EN SHAREDPREF
            if (product?.quantity==null){
                product?.quantity=1
            }
            selectProducts.add(product!!)
        }else{ // YA EXISTE EN SHARED PREF EL PRODUCTO DEBEMOS EDITAR LA CANTIDAD LA CANTIDAD
            selectProducts[index].quantity = counter

        }
        //vamos a guardar en el sharedPref
        sharedPref?.save("order",selectProducts)
        Toast.makeText(this, "Producto agregado", Toast.LENGTH_LONG).show()

    }
    private fun getProductsFromSharedPref(){
        //si en el sharedPref existe un elemento guardado con la key order
        if (!sharedPref?.getData("order").isNullOrBlank()){ // EXISTE UNA ORDEN EN SHAREDPREF
            //vamos a transformar una lista de tipo json a una arreglo de tipo Product
            val type = object: TypeToken<ArrayList<Product>>() {}.type
            //usamos el metodo fromJson para transformar de json a arraylist
            selectProducts = gson.fromJson(sharedPref?.getData("order"),type)
            var index = getIndexOf(product?.id!!)
            //Desde la lista en sharedPref asignamos la cantidad a la BD
            if (index != -1){
                product?.quantity=selectProducts[index].quantity
                textViewCounter?.text="${product?.quantity}"
                cantidad = product?.quantity!!
                textViewCounter?.text = "$cantidad"
                productPrice=product?.price!!*product?.quantity!!
                counter = cantidad
                textViewPrice?.text = "S/. ${productPrice}"
                buttonAdd?.setText("AGREGAS MAS PRODUCTOS")
                buttonAdd?.backgroundTintList= ColorStateList.valueOf(Color.RED)
            }
            for (p in selectProducts){
                Log.d(TAG,"Shared pref: $p")
            }

        }
    }
    //metodo para comparar si un producto ya existe en sharedpref y asi poder editar la cantidad del producto seleccionado
    private fun getIndexOf(idProduct: String): Int{
        var pos = 0
        for(p in selectProducts){
            if (p.id==idProduct){
                return pos
            }
            pos++
        }
        return -1

    }
    private fun addItem(){
        counter++
        productPrice = product?.price!!*counter
        product?.quantity = counter
        textViewCounter?.text="${product?.quantity}"
        textViewPrice?.text = "S/.  ${productPrice}"
    }
    private fun removeItem(){
        if (counter>1){
            counter--
            productPrice = product?.price!!*counter
            product?.quantity = counter
            textViewCounter?.text="${product?.quantity}"
            textViewPrice?.text = "S/.  ${productPrice}"
        }

    }
}
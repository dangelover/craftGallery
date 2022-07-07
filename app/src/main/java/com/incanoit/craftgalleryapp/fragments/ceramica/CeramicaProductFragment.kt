package com.incanoit.craftgalleryapp.fragments.ceramica

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.gson.Gson
import com.incanoit.craftgalleryapp.R
import com.incanoit.craftgalleryapp.adapters.CategoriesAdapater
import com.incanoit.craftgalleryapp.models.Category
import com.incanoit.craftgalleryapp.models.Product
import com.incanoit.craftgalleryapp.models.ResponseHttp
import com.incanoit.craftgalleryapp.models.User
import com.incanoit.craftgalleryapp.providers.CategoriesProvider
import com.incanoit.craftgalleryapp.providers.ProductsProvider
import com.incanoit.craftgalleryapp.utils.SharedPref
import com.tommasoberlose.progressdialog.ProgressDialogFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class CeramicaProductFragment : Fragment() {
    val TAG = "ProductFragment"
    var myView: View?=null
    var editTextProductName: EditText?=null
    var editTextProductDescription: EditText?=null
    var editTextProductPrice: EditText?=null
    var imageViewProduct1: ImageView?=null
    var imageViewProduct2: ImageView?=null
    var imageViewProduct3: ImageView?=null
    var buttonCreateProduct: Button?=null
    var spinnerCategories:Spinner?=null
    var imageFile1: File?=null
    var imageFile2: File?=null
    var imageFile3: File?=null
    var user: User?=null
    var sharedPref: SharedPref?=null
    var categories = ArrayList<Category>()
    var categoriesProvider: CategoriesProvider?=null
    var productsProvider: ProductsProvider?=null
    var idCategory = ""



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        myView = inflater.inflate(R.layout.fragment_ceramica_product, container, false)
        editTextProductName=myView?.findViewById(R.id.edittext_product_name)
        editTextProductDescription=myView?.findViewById(R.id.edittext_product_description)
        editTextProductPrice=myView?.findViewById(R.id.edittext_product_price)
        imageViewProduct1=myView?.findViewById(R.id.imageview_image1)
        imageViewProduct2=myView?.findViewById(R.id.imageview_image2)
        imageViewProduct3=myView?.findViewById(R.id.imageview_image3)
        buttonCreateProduct=myView?.findViewById(R.id.btn_create_product)
        spinnerCategories=myView?.findViewById(R.id.spinner_categories)
        buttonCreateProduct?.setOnClickListener { createProduct() }
        imageViewProduct1?.setOnClickListener { selectImage(101) }
        imageViewProduct2?.setOnClickListener { selectImage(102) }
        imageViewProduct3?.setOnClickListener { selectImage(103) }
        sharedPref= SharedPref(requireActivity())
        getUsersFromSession()
        categoriesProvider = CategoriesProvider(user?.sessionToken!!)
        productsProvider = ProductsProvider(user?.sessionToken!!)
        getCategories()

        return myView
    }
    private fun isValidForm(name: String,description:String, price: String): Boolean {
        if (name.isNullOrBlank()){
            Toast.makeText(requireContext(), "Ingresa un valor en el nombre del producto", Toast.LENGTH_SHORT).show()
            return false
        }
        if (description.isNullOrBlank()){
            Toast.makeText(requireContext(), "Ingresa la descripcion del producto", Toast.LENGTH_SHORT).show()
            return false
        }
        if (price.isNullOrBlank()){
            Toast.makeText(requireContext(), "Ingresa el precio del producto", Toast.LENGTH_SHORT).show()
            return false
        }
        if (imageFile1==null){
            Toast.makeText(requireContext(), "Selecciona una imagen", Toast.LENGTH_SHORT).show()
            return false
        }
        if (imageFile2==null){
            Toast.makeText(requireContext(), "Selecciona una imagen", Toast.LENGTH_SHORT).show()
            return false
        }
        if (imageFile3==null){
            Toast.makeText(requireContext(), "Selecciona una imagen", Toast.LENGTH_SHORT).show()
            return false
        }
        if (idCategory.isNullOrBlank()){
            Toast.makeText(requireContext(), "Selecciona la categoria del producto", Toast.LENGTH_SHORT).show()
            return false
        }
        return true



    }
    private fun resetForm(){
        editTextProductName?.setText("")
        editTextProductDescription?.setText("")
        editTextProductPrice?.setText("")
        imageFile1=null
        imageFile2=null
        imageFile3=null
        imageViewProduct1?.setImageResource(R.drawable.ic_image)
        imageViewProduct2?.setImageResource(R.drawable.ic_image)
        imageViewProduct3?.setImageResource(R.drawable.ic_image)
    }
    private fun createProduct(){
        val name = editTextProductName?.text.toString()
        val descrption = editTextProductDescription?.text.toString()
        val priceText = editTextProductPrice?.text.toString()
        val files = ArrayList<File>()

        if (isValidForm(name,descrption,priceText )){
            //vamos a pasar al modelo Product el nombre, la descripcion y el precio
            val product = Product(
                name= name,
                description = descrption,
                //convertimos el precio de string a double
                price = priceText.toDouble(),
                idCategory = idCategory
            )
            files.add(imageFile1!!)
            files.add(imageFile2!!)
            files.add(imageFile3!!)
            ProgressDialogFragment.showProgressBar(requireActivity())
            Log.d(TAG,"Response: $product")
            Log.d(TAG,"Response: $files")
            productsProvider?.create(files, product)?.enqueue(object:Callback<ResponseHttp>{
                override fun onResponse(
                    call: Call<ResponseHttp>,
                    response: Response<ResponseHttp>
                ) {
                    ProgressDialogFragment.hideProgressBar(requireActivity())
                    Log.d(TAG,"Response: ${response}")
                    Log.d(TAG,"Response: ${response.body()}")
                    Toast.makeText(requireContext(),"Error: ${response.body()?.message}",Toast.LENGTH_LONG).show()
                    if (response.body()?.isSuccess==true){
                        resetForm()
                    }
                }

                override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                    ProgressDialogFragment.hideProgressBar(requireActivity())
                    Log.d(TAG,"Error: ${t.message}")
                    Toast.makeText(requireContext(),"Error: ${t.message}",Toast.LENGTH_LONG).show()
                }

            })

        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val fileUri = data?.data
            if(requestCode==101){
                //creamos un archivo y obtenemos la data de la data

                //al circleimage vamos a agregarle la imagen que el usuario esta agregando de galeria
                imageFile1= File(fileUri?.path)//el archivo que vamos a guardar como imagen en el servidor
                imageViewProduct1?.setImageURI(fileUri)
            }else if (requestCode==102){
                //creamos un archivo y obtenemos la data de la data

                //al circleimage vamos a agregarle la imagen que el usuario esta agregando de galeria
                imageFile2= File(fileUri?.path)//el archivo que vamos a guardar como imagen en el servidor
                imageViewProduct2?.setImageURI(fileUri)
            }else if(requestCode==103){
                //creamos un archivo y obtenemos la data de la data

                //al circleimage vamos a agregarle la imagen que el usuario esta agregando de galeria
                imageFile3= File(fileUri?.path)//el archivo que vamos a guardar como imagen en el servidor
                imageViewProduct3?.setImageURI(fileUri)
            }
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }
    private fun getCategories(){
        categoriesProvider?.getAll()?.enqueue(object: Callback<ArrayList<Category>> {
            override fun onResponse(
                call: Call<ArrayList<Category>>,
                response: Response<ArrayList<Category>>
            ) {
                if (response.body()!= null){
                    categories=response.body()!!
                    val arrayAdapter = ArrayAdapter<Category>(requireActivity(),android.R.layout.simple_dropdown_item_1line, categories)
                    spinnerCategories?.adapter=arrayAdapter
                    spinnerCategories?.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
                        override fun onItemSelected(
                            adapterView: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            l: Long
                        ) {
                            idCategory = categories[position].id!!
                            Log.d(TAG,"ID category: $idCategory")
                        }

                        override fun onNothingSelected(p0: AdapterView<*>?) {

                        }

                    }

                }
            }

            override fun onFailure(call: Call<ArrayList<Category>>, t: Throwable) {
                Log.d(TAG,"Error: ${t.message}")
                Toast.makeText(requireContext(),"Error: ${t.message}",Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun selectImage(requestCode: Int){
        //usamos la libreria ImagePicker y usamos el metodo with y el pasamos de parametro el contexto
        //usamos el metodo crop para cortar la imagen luego compres para que comprima la imagen hasta un ancho de 1024
        ImagePicker.with(this).crop().compress(1024).maxResultSize(1080,1080).start(requestCode)
    }
    private fun getUsersFromSession(){

        val gson = Gson()
        //usamos el SharedPref y usando el metodo getData vamos a obtener informacion del usuario
        //le pasamos el key o el nombre con el que guardamos la informacion
        if (!sharedPref?.getData("user").isNullOrEmpty()){
            //SI EL USUARIO EXISTE EN SESION
            user = gson.fromJson(sharedPref?.getData("user"), User::class.java)
            Log.d("ClientUpdateActivity","${user}")

        }
    }


}
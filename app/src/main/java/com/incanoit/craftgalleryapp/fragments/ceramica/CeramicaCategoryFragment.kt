package com.incanoit.craftgalleryapp.fragments.ceramica

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.gson.Gson
import com.incanoit.craftgalleryapp.R
import com.incanoit.craftgalleryapp.models.Category
import com.incanoit.craftgalleryapp.models.ResponseHttp
import com.incanoit.craftgalleryapp.models.User
import com.incanoit.craftgalleryapp.providers.CategoriesProvider
import com.incanoit.craftgalleryapp.utils.SharedPref
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


class CeramicaCategoryFragment : Fragment() {
    val TAG = "CategoryFragment"
    var myView: View?=null
    var imageViewCategory: ImageView?=null
    var editTextCategory: EditText? =null
    var buttonCreate: Button?=null
    private var imageFile: File?=null
    var categoriesProvider:CategoriesProvider?=null
    var sharedPref: SharedPref?=null
    var user: User?=null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        myView= inflater.inflate(R.layout.fragment_ceramica_category, container, false)
        sharedPref= SharedPref(requireActivity())
        imageViewCategory= myView?.findViewById(R.id.imageview_category)
        editTextCategory= myView?.findViewById(R.id.edittext_category)
        buttonCreate= myView?.findViewById(R.id.btn_create_category)
        getUsersFromSession()
        categoriesProvider= CategoriesProvider(user?.sessionToken!!)
        imageViewCategory?.setOnClickListener {
            selectImage()
        }
        buttonCreate?.setOnClickListener {
            createCategory()
        }
        return myView
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
    private fun createCategory(){
        val name = editTextCategory?.text.toString()
        if (imageFile !=null){
            val category = Category(name = name)
            categoriesProvider?.create(imageFile!!,category)?.enqueue(object: Callback<ResponseHttp> {
                override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>) {
                    Log.d(TAG,"${response}")
                    Log.d(TAG,"${response.body()}")
                    Toast.makeText(requireContext(),"La categoria se creo correctamente",Toast.LENGTH_LONG).show()
                    if(response.body()?.isSuccess==true){
                        clearForm()

                    }
                }


                override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                    Log.d(TAG,"${t.message}")
                    Toast.makeText(requireContext(),"Error ${t.message}",Toast.LENGTH_LONG).show()
                }

            })

        }else{
            Toast.makeText(requireContext(),"Selecciona una imagen",Toast.LENGTH_SHORT).show()
        }
    }
    private fun clearForm(){
        editTextCategory?.setText("")
        imageFile=null
        imageViewCategory?.setImageResource(R.drawable.ic_image)
    }
    private val startImageForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result: ActivityResult ->
        val resultCode = result.resultCode
        //recuperamos la data que nos devuelve el metodo el createIntent
        //es decir estamos recuperando la informacion de la imagen que el usuario capturo
        val data = result.data
        //si el usuario selecciono una imagen correctamente
        if (resultCode== Activity.RESULT_OK){
            //creamos un archivo y obtenemos la data de la data
            val fileUri = data?.data
            //al circleimage vamos a agregarle la imagen que el usuario esta agregando de galeria
            imageFile=
                File(fileUri?.path)//el archivo que vamos a guardar como imagen en el servidor
            imageViewCategory?.setImageURI(fileUri)
        }else if (resultCode== ImagePicker.RESULT_ERROR){
            Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_LONG).show()
        }else{
            Toast.makeText(requireContext(),"Tarea cancelada", Toast.LENGTH_LONG).show()
        }
    }
    private fun selectImage(){
        //usamos la libreria ImagePicker y usamos el metodo with y el pasamos de parametro el contexto
        //usamos el metodo crop para cortar la imagen luego compres para que comprima la imagen hasta un ancho de 1024
        ImagePicker.with(this).crop().compress(1024).maxResultSize(1080,1080).createIntent { intent -> startImageForResult.launch(intent) }
    }
}
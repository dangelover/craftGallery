package com.incanoit.craftgalleryapp.activities.client.update

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.gson.Gson
import com.incanoit.craftgalleryapp.R
import com.incanoit.craftgalleryapp.models.ResponseHttp
import com.incanoit.craftgalleryapp.models.User
import com.incanoit.craftgalleryapp.providers.UsersProvider
import com.incanoit.craftgalleryapp.utils.SharedPref
import de.hdodenhof.circleimageview.CircleImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class ClientUpdateActivity : AppCompatActivity() {
    val TAG = "ClientUpdateActivity"
    var circleImageUser: CircleImageView?=null
    var editTextName: EditText?=null
    var ediTextFechaNacimiento: EditText?=null
    var editTextLastName: EditText?=null
    var editTextDNI: EditText?=null
    var editTextPhone: EditText?=null
    var btnUpdate: Button?=null
    var sharedPref: SharedPref?=null
    var user:User?=null
    private var imageFile: File?=null
    var userProvider: UsersProvider?=null
    var toolbar: Toolbar?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_update)
        sharedPref = SharedPref(this,)
        toolbar=findViewById(R.id.toolbar)
        toolbar?.title="Editar Perfil"
        toolbar?.setTitleTextColor(ContextCompat.getColor(this,R.color.black))
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        circleImageUser=findViewById(R.id.circle_image_user)
        editTextName=findViewById(R.id.edittext_name_update)
        editTextLastName=findViewById(R.id.edittext_lastname_update)
        ediTextFechaNacimiento=findViewById(R.id.edittext_fecha_update)
        editTextDNI=findViewById(R.id.edittext_dni_update)
        editTextPhone=findViewById(R.id.edittext_phone_update)
        btnUpdate=findViewById(R.id.btn_update)
        getUsersFromSession()
        userProvider= UsersProvider(user?.sessionToken)
        editTextName?.setText(user?.name)
        editTextLastName?.setText(user?.lastname)
        ediTextFechaNacimiento?.setText(user?.fechaNacimiento)
        editTextDNI?.setText(user?.dni)
        editTextPhone?.setText(user?.phone)
        if (!user?.image.isNullOrBlank()){
            Glide.with(this).load(user?.image).into(circleImageUser!!)

        }
        circleImageUser?.setOnClickListener {
            selectImage()
        }
        btnUpdate?.setOnClickListener {
            updateData()
        }
    }
    private fun updateData(){
        val name = editTextName?.text.toString()
        val lastname = editTextLastName?.text.toString()
        val phone = editTextPhone?.text.toString()
        val dni = editTextDNI?.text.toString()
        val fecha_nac=ediTextFechaNacimiento?.text.toString()
        user?.name=name
        user?.lastname=lastname
        user?.phone=phone
        user?.dni=dni
        user?.fechaNacimiento=fecha_nac
        if (imageFile != null){
            userProvider?.update(imageFile!!,user!!)?.enqueue(object: Callback<ResponseHttp> {
                override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>) {
                    Log.d(TAG,"${response}")
                    Log.d(TAG,"${response.body()}")
                    if (response.body()?.isSuccess==true){
                        saveUserInSession(response.body()?.data.toString())
                    }
                    Toast.makeText(this@ClientUpdateActivity,response.body()?.message,Toast.LENGTH_SHORT).show()
                }

                override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                    Log.d(TAG,"${t.message}")
                    Toast.makeText(this@ClientUpdateActivity,"Error ${t.message}",Toast.LENGTH_LONG).show()
                }

            })

        }else{
            userProvider?.updateWithoutImage(user!!)?.enqueue(object: Callback<ResponseHttp> {
                override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>) {
                    Log.d(TAG,"${response}")
                    Log.d(TAG,"${response.body()}")
                    if (response.body()?.isSuccess==true){
                        saveUserInSession(response.body()?.data.toString())
                    }
                    Toast.makeText(this@ClientUpdateActivity,response.body()?.message,Toast.LENGTH_SHORT).show()
                }

                override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                    Log.d(TAG,"${t.message}")
                    Toast.makeText(this@ClientUpdateActivity,"Error ${t.message}",Toast.LENGTH_LONG).show()
                }

            })
        }

    }
    private fun saveUserInSession(data:String){
        //vamos a usar nuestro metodo SharedPref y le pasamos el activity

        val gson = Gson()
        //usando el metodo gson.fromJson vamos a transformar lo que traiga la data en un objeto de tipo usuario
        val user = gson.fromJson(data,User::class.java)
        //ahora ejecutamos el metodo save y le pasamos el key el cual es como queremos que se guarde o con que nombre
        sharedPref?.save("user",user)
        //si el usuario tiene mas de 1 rol
        //para llamar al metodo size siempre y cuando puede venir vacio el arreglo roles es necesario colocar !!
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
            circleImageUser?.setImageURI(fileUri)
        }else if (resultCode== ImagePicker.RESULT_ERROR){
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_LONG).show()
        }else{
            Toast.makeText(this,"Tarea cancelada", Toast.LENGTH_LONG).show()
        }
    }
    private fun selectImage(){
        //usamos la libreria ImagePicker y usamos el metodo with y el pasamos de parametro el contexto
        //usamos el metodo crop para cortar la imagen luego compres para que comprima la imagen hasta un ancho de 1024
        ImagePicker.with(this).crop().compress(1024).maxResultSize(1080,1080).createIntent { intent -> startImageForResult.launch(intent) }
    }
}
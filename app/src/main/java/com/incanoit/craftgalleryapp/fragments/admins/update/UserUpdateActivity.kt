package com.incanoit.craftgalleryapp.fragments.admins.update

import android.app.Activity
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.gson.Gson
import com.incanoit.craftgalleryapp.R
import com.incanoit.craftgalleryapp.models.ResponseHttp
import com.incanoit.craftgalleryapp.models.User
import com.incanoit.craftgalleryapp.providers.UsersProvider
import com.incanoit.craftgalleryapp.utils.SharedPref
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class UserUpdateActivity : AppCompatActivity() {
    val TAG ="UserUpdate"
    var circle_image_user: ImageView?=null //*
    var edittext_fecha_update: EditText?=null //*
    var imgview_fecha: ImageView?=null //*
    var editTextNameUser: EditText?=null //*
    var editText_lastname:EditText?=null //*
    var editTextDNIUser: EditText?=null //*
    var editTextPhone: EditText?=null //*
    var idUsuario: String?=null
    var nombreUsuario:String?=null
    var dateBirth:String?=null
    var lastname:String?=null
    var dni:String?=null
    var celular:String?=null
    var image:String?=null
    var btn_update: Button?=null
    var sharedPref:SharedPref?=null
    var user: User?=null
    private var imageFile: File?=null
    var userProvider: UsersProvider?=null
    var dpFecha: DatePicker?=null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_update)
        sharedPref= SharedPref(this)
        getUsersFromSession()
        userProvider= UsersProvider(user?.sessionToken)
        idUsuario=intent.getStringExtra("idUsuario")
        nombreUsuario=intent.getStringExtra("nombreUsuario")
        dateBirth=intent.getStringExtra("dateBirth")
        lastname=intent.getStringExtra("lastname")
        dni=intent.getStringExtra("dni")
        celular=intent.getStringExtra("celular")
        image=intent.getStringExtra("image")
        Log.d("UserUpdate","${dateBirth}")
        Log.d("UserUpdate","${lastname}")
        Log.d("UserUpdate","${dni}")
        Log.d("UserUpdate","${celular}")
        circle_image_user=findViewById(R.id.circle_image_user_update)
        editTextNameUser=findViewById(R.id.edittext_name_update_user)
        editText_lastname=findViewById(R.id.edittext_lastname_update_user)
        editTextDNIUser=findViewById(R.id.edittext_dni_update_user)
        editTextPhone=findViewById(R.id.edittext_phone_update_celular)
        edittext_fecha_update=findViewById(R.id.edittext_fecha_update_user)
        dpFecha=findViewById(R.id.dp_fecha)
        imgview_fecha=findViewById(R.id.imgview_fecha_user)
        btn_update=findViewById(R.id.btn_update_user)
        editTextNameUser?.setText(nombreUsuario)
        editText_lastname?.setText(lastname)
        editTextDNIUser?.setText(dni)
        editTextPhone?.setText(celular)
        edittext_fecha_update?.setText(getFechaDatePicker())
        if (!image.isNullOrBlank()){
            Glide.with(this).load(image).into(circle_image_user!!)
        }
        circle_image_user?.setOnClickListener {
            selectImage()
        }
        dpFecha?.setOnDateChangedListener{
            dpFecha,anio,mes,dia->
            edittext_fecha_update?.setText(getFechaDatePicker())
            dpFecha.visibility=View.GONE
        }
        btn_update?.setOnClickListener {
            updateData()
        }


    }
    fun getFechaDatePicker():String {
        var dia = dpFecha?.dayOfMonth.toString().padStart(2,'0')
        var mes = (dpFecha!!.month+1).toString().padStart(2,'0')
        var anio = dpFecha?.year.toString().padStart(4,'0')
        return dia + "/"+mes+"/"+anio
    }
    fun muestraCalendario(view: View){
        dpFecha?.visibility=View.VISIBLE
    }

    private fun updateData(){
        val name = editTextNameUser?.text.toString()
        val lastname = editText_lastname?.text.toString()
        val phone = editTextPhone?.text.toString()
        val dni = editTextDNIUser?.text.toString()
        val fecha_nac=edittext_fecha_update?.text.toString()
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
                    Toast.makeText(this@UserUpdateActivity,response.body()?.message,Toast.LENGTH_SHORT).show()
                }

                override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                    Log.d(TAG,"${t.message}")
                    Toast.makeText(this@UserUpdateActivity,"Error ${t.message}",Toast.LENGTH_LONG).show()
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
                    Toast.makeText(this@UserUpdateActivity,response.body()?.message,Toast.LENGTH_SHORT).show()
                }

                override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                    Log.d(TAG,"${t.message}")
                    Toast.makeText(this@UserUpdateActivity,"Error ${t.message}",Toast.LENGTH_LONG).show()
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
            Log.d("ClientProfileFragment","${user}")

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
            circle_image_user?.setImageURI(fileUri)
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
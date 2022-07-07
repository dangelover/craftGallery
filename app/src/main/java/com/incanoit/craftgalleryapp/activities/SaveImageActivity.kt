package com.incanoit.craftgalleryapp.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.gson.Gson
import com.incanoit.craftgalleryapp.R
import com.incanoit.craftgalleryapp.activities.ceramica.home.CeramicaHomeActivity
import com.incanoit.craftgalleryapp.activities.client.home.ClientHomeActivity
import com.incanoit.craftgalleryapp.activities.delivery.home.DeliveryHomeActivity
import com.incanoit.craftgalleryapp.models.ResponseHttp
import com.incanoit.craftgalleryapp.models.User
import com.incanoit.craftgalleryapp.providers.UsersProvider
import com.incanoit.craftgalleryapp.utils.SharedPref
import de.hdodenhof.circleimageview.CircleImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class SaveImageActivity : AppCompatActivity() {
    val TAG = "SaveImageActivity"
    var circleimageview: CircleImageView?=null
    var buttonNext: Button?=null
    var buttonConfirm: Button?=null
    var user: User?=null
    var sharedPref:SharedPref?=null
    private var imageFile: File?=null
    var userProvider: UsersProvider?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_save_image)
        sharedPref = SharedPref(this)
        getUsersFromSession()
        Log.d(TAG,"${user?.roles}")
        Log.d(TAG,"${user?.sessionToken}")
        userProvider= UsersProvider(user?.sessionToken)
        circleimageview=findViewById(R.id.circle_image_view)
        buttonNext=findViewById(R.id.btn_next)
        buttonConfirm=findViewById(R.id.btn_confirm)
        circleimageview?.setOnClickListener {
            selectImage()
        }
        buttonNext?.setOnClickListener {
            goToClientHome()
        }
        buttonConfirm?.setOnClickListener {
            saveImage()
        }
    }
    private fun saveImage(){
        Log.d(TAG,"${imageFile}")
        if (imageFile !=null && user !=null){
            Log.d(TAG,"${user?.sessionToken}")
            userProvider?.update(imageFile!!,user!!)?.enqueue(object: Callback<ResponseHttp>{
                override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>) {
                    Log.d(TAG,"DSADAD")
                    Log.d(TAG,"${response}")
                    Log.d(TAG,"${response.body()}")

                    saveUserInSession(response.body()?.data.toString())

                }

                override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                    Log.d(TAG,"${t.message}")
                    Toast.makeText(this@SaveImageActivity,"Error ${t.message}",Toast.LENGTH_LONG).show()
                }

            })
        }else{
            Toast.makeText(this, "La imagen no pueda ser vacia ni tampoco los datos de sesion del usuario", Toast.LENGTH_LONG).show()
        }

    }
    private fun saveUserInSession(data:String){
        //vamos a usar nuestro metodo SharedPref y le pasamos el activity
        val gson = Gson()
        //usando el metodo gson.fromJson vamos a transformar lo que traiga la data en un objeto de tipo usuario
        val usernew = gson.fromJson(data,User::class.java)
        //ahora ejecutamos el metodo save y le pasamos el key el cual es como queremos que se guarde o con que nombre
        sharedPref?.save("user",usernew)
        //si el usuario tiene mas de 1 rol
        //para llamar al metodo size siempre y cuando puede venir vacio el arreglo roles es necesario colocar !!
        goToClientHome()
    }
    private fun goToClientHome(){
        val i = Intent(this, ClientHomeActivity::class.java)
        i.flags=
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK //eliminar el historial de pantallas
        startActivity(i)
    }
    private fun goToCeramicaHome(){
        val i = Intent(this, CeramicaHomeActivity::class.java)
        i.flags=
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK //eliminar el historial de pantallas
        startActivity(i)
    }
    private fun goToDeliveryHome(){
        val i = Intent(this, DeliveryHomeActivity::class.java)
        i.flags=
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK //eliminar el historial de pantallas
        startActivity(i)
    }
    private fun getUsersFromSession(){

        val gson = Gson()
        //usamos el SharedPref y usando el metodo getData vamos a obtener informacion del usuario
        //le pasamos el key o el nombre con el que guardamos la informacion
        if (!sharedPref?.getData("user").isNullOrEmpty()){
            //SI EL USUARIO EXISTE EN SESION
            user = gson.fromJson(sharedPref?.getData("user"), User::class.java)

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
                imageFile=File(fileUri?.path)//el archivo que vamos a guardar como imagen en el servidor
                circleimageview?.setImageURI(fileUri)
            }else if (resultCode==ImagePicker.RESULT_ERROR){
                Toast.makeText(this,ImagePicker.getError(data),Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(this,"Tarea cancelada",Toast.LENGTH_LONG).show()
            }
    }
    private fun selectImage(){
        //usamos la libreria ImagePicker y usamos el metodo with y el pasamos de parametro el contexto
        //usamos el metodo crop para cortar la imagen luego compres para que comprima la imagen hasta un ancho de 1024
        ImagePicker.with(this).crop().compress(1024).maxResultSize(1080,1080).createIntent { intent -> startImageForResult.launch(intent) }
    }
}
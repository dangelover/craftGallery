package com.incanoit.craftgalleryapp.activities

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.gson.Gson
import com.incanoit.craftgalleryapp.R
import com.incanoit.craftgalleryapp.activities.administrador.home.AdminHomeActivity
import com.incanoit.craftgalleryapp.activities.ceramica.home.CeramicaHomeActivity
import com.incanoit.craftgalleryapp.activities.client.home.ClientHomeActivity
import com.incanoit.craftgalleryapp.activities.delivery.home.DeliveryHomeActivity
import com.incanoit.craftgalleryapp.models.ResponseHttp
import com.incanoit.craftgalleryapp.models.User
import com.incanoit.craftgalleryapp.providers.UsersProvider
import com.incanoit.craftgalleryapp.utils.SharedPref
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    var buttonGoToRegister:Button? = null
    var editTextEmail:EditText?=null
    var editTextPassword:EditText?=null
    var buttonLogin:Button?=null
    var buttonRegisterArtezano: Button?=null
    var buttonRegisterRepartidor: Button?=null
    var usersProviders = UsersProvider()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        buttonGoToRegister = findViewById(R.id.button_go_to_register)
        buttonRegisterArtezano=findViewById(R.id.button_go_to_register_artezano)
        buttonRegisterRepartidor=findViewById(R.id.button_go_to_register_repartidor)
        editTextEmail = findViewById(R.id.editext_email)
        editTextPassword = findViewById(R.id.edittext_password)
        buttonLogin=findViewById(R.id.btn_login)
        buttonRegisterRepartidor?.setOnClickListener {
            goToRegisterRepartidor()
        }
        buttonRegisterArtezano?.setOnClickListener {
            goToRegisterArtezano()
        }
        //? => validamos que la variable sea diferente de null
        buttonGoToRegister?.setOnClickListener{
            goToRegister()
        }
        buttonLogin?.setOnClickListener{
            login()
        }
        getUsersFromSession()
    }
    private fun login(){
        val email = editTextEmail?.text.toString()
        val password = editTextPassword?.text.toString()
        if (isValidForm(email,password)){
            usersProviders.login(email, password)?.enqueue(object: Callback<ResponseHttp>{
                override fun onResponse(
                    call: Call<ResponseHttp>,
                    response: Response<ResponseHttp>
                ) {
                    Log.d("MainActivity","Response: ${response.body()}")
                    if (response.body()?.isSuccess==true){
                        Toast.makeText(this@MainActivity,response.body()?.message,Toast.LENGTH_LONG).show()
                        saveUserInSession(response.body()?.data.toString())


                    }else{
                        Toast.makeText(this@MainActivity,"Datos incorrectos o su cuenta ha sido desactivada",Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                    Toast.makeText(this@MainActivity,"Hubo un error ${t.message}",Toast.LENGTH_LONG).show()
                    Log.d("MainActivity","Hubo un error con esta informacion")
                }

            })

        }else{
            Toast.makeText(this, "el formulario no es valido", Toast.LENGTH_LONG).show()
        }
//        Toast.makeText(this, "El password es: $password", Toast.LENGTH_LONG).show()
        //con el Log.d podemos imprimir en consola
        Log.d("MainActivity","El email es: $email")
//        Log.d("MainActivity","El password es: $password")
    }
    private fun goToClientHome(){
        val i = Intent(this,ClientHomeActivity::class.java)
        i.flags=FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK //eliminar el historial de pantallas
        startActivity(i)
    }
    private fun goToCeramicaHome(){
        val i = Intent(this,CeramicaHomeActivity::class.java)
        i.flags=FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK //eliminar el historial de pantallas
        startActivity(i)
    }
    private fun goToDeliveryHome(){
        val i = Intent(this,DeliveryHomeActivity::class.java)
        i.flags=FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK //eliminar el historial de pantallas
        startActivity(i)
    }
    private fun goToDeliveryAdmin(){
        val i = Intent(this,AdminHomeActivity::class.java)
        i.flags=FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK //eliminar el historial de pantallas
        startActivity(i)
    }
    private fun goToSelectRol(){
        val i = Intent(this,SelectRolesActivity::class.java)
        i.flags=FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK //eliminar el historial de pantallas
        startActivity(i)
    }
    private fun saveUserInSession(data:String){
        //vamos a usar nuestro metodo SharedPref y le pasamos el activity
        val sharedPref = SharedPref(this)
        val gson = Gson()
        //usando el metodo gson.fromJson vamos a transformar lo que traiga la data en un objeto de tipo usuario
        val user = gson.fromJson(data,User::class.java)
        //ahora ejecutamos el metodo save y le pasamos el key el cual es como queremos que se guarde o con que nombre
        sharedPref.save("user",user)
        //si el usuario tiene mas de 1 rol
        //para llamar al metodo size siempre y cuando puede venir vacio el arreglo roles es necesario colocar !!
        Log.d("MainActivity","$user")

        if (user.roles?.size!!>1){
            goToSelectRol()
        }else{
            for (roluser in user.roles!!){
                Log.d("MainActivity","${roluser.name}")
                val rolUser = roluser.name
                if (rolUser=="CERAMICA"){
                    goToCeramicaHome()
                }else if(rolUser=="CLIENTE"){
                    goToClientHome()
                }else if(rolUser=="REPARTIDOR"){
                    goToDeliveryHome()
                }
                else if(rolUser=="ADMINISTRADOR"){
                    goToDeliveryAdmin()
                }
            }
        }
    }
    // definimos esta funcion como String, a cualquier string podemos aplicar este metodo
    fun String.isEmailValid(): Boolean{
        //VAMOS A IMPORTAR ESTA CLASE
        //esta es la validacion que vamos a usar para validar si el usuario ingreso un email
        //RECORDAR : ESTA LINEA DE CODIGO VIENE DE LA DOCUMENTACION
        return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
    }
    private fun getUsersFromSession(){
        val sharedPref = SharedPref(this)
        val gson = Gson()
        //usamos el SharedPref y usando el metodo getData vamos a obtener informacion del usuario
        //le pasamos el key o el nombre con el que guardamos la informacion
        //si el usuario existe en sesion entonce lo manda a la pantalla del cliente
        if (!sharedPref.getData("user").isNullOrEmpty()){
            //SI EL USUARIO EXISTE EN SESION
            val user = gson.fromJson(sharedPref.getData("user"),User::class.java)
            if (!sharedPref.getData("rol").isNullOrBlank()){
                //si el usuario selecciono el rol
                //vamos a obtener el rol seleccionado por el usuario
                val rol = sharedPref.getData("rol")?.replace("\"","")
                Log.d("MainActivity","Rol $rol")
                if (rol=="CERAMICA"){
                    Log.d("MainActivity","entra aqui")
                    goToCeramicaHome()
                }else if(rol=="CLIENTE"){
                    goToClientHome()
                }else if(rol=="REPARTIDOR"){
                    goToDeliveryHome()
                }
            }else{
                goToClientHome()
            }

        }
    }
    //esta funcion va a retornar un booleano
    private fun isValidForm(email: String, password:String): Boolean{
        if (email.isBlank()){
            return false
        }
        if (password.isBlank()){
            return false
        }
        if (!email.isEmailValid()){
            return false
        }
        return true
    }
    private fun goToRegister(){
        //creamos el intent para indicarle a donde queremos ir
        //el this es donde nos encontramos y el siguiente argumento es la actividad
        val i = Intent(this, RegisterActivity::class.java)
        startActivity(i)
    }
    private fun goToRegisterArtezano(){
        //creamos el intent para indicarle a donde queremos ir
        //el this es donde nos encontramos y el siguiente argumento es la actividad
        val i = Intent(this, ArtezanoRegisterActivity::class.java)
        startActivity(i)
    }
    private fun goToRegisterRepartidor(){
        //creamos el intent para indicarle a donde queremos ir
        //el this es donde nos encontramos y el siguiente argumento es la actividad
        val i = Intent(this, RepartidorRegisterActivity::class.java)
        startActivity(i)
    }


}
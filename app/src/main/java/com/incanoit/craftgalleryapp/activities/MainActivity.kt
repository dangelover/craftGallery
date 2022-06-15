package com.incanoit.craftgalleryapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.incanoit.craftgalleryapp.R

class MainActivity : AppCompatActivity() {
    var buttonGoToRegister:Button? = null
    var editTextEmail:EditText?=null
    var editTextPassword:EditText?=null
    var buttonLogin:Button?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        buttonGoToRegister = findViewById(R.id.button_go_to_register)
        editTextEmail = findViewById(R.id.editext_email)
        editTextPassword = findViewById(R.id.edittext_password)
        buttonLogin=findViewById(R.id.btn_login)
        //? => validamos que la variable sea diferente de null
        buttonGoToRegister?.setOnClickListener{
            goToRegister()
        }
        buttonLogin?.setOnClickListener{
            login()
        }
    }
    private fun login(){
        val email = editTextEmail?.text.toString()
        val password = editTextPassword?.text.toString()
        if (isValidForm(email,password)){
            Toast.makeText(this,"El fomulario es valido",Toast.LENGTH_LONG).show()
        }else{
            Toast.makeText(this, "el formulario no es valido", Toast.LENGTH_LONG).show()
        }
//        Toast.makeText(this, "El password es: $password", Toast.LENGTH_LONG).show()
        //con el Log.d podemos imprimir en consola
        Log.d("MainActivity","El email es: $email")
        Log.d("MainActivity","El password es: $password")
    }
    // definimos esta funcion como String, a cualquier string podemos aplicar este metodo
    fun String.isEmailValid(): Boolean{
        //VAMOS A IMPORTAR ESTA CLASE
        //esta es la validacion que vamos a usar para validar si el usuario ingreso un email
        //RECORDAR : ESTA LINEA DE CODIGO VIENE DE LA DOCUMENTACION
        return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
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

}
package com.incanoit.craftgalleryapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.*
import com.incanoit.craftgalleryapp.R
import com.incanoit.craftgalleryapp.models.ResponseHttp
import com.incanoit.craftgalleryapp.models.User
import com.incanoit.craftgalleryapp.providers.UsersProvider
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    //en kotlin ninguna variable debe ser nulla
    var textViewGoToLogin: TextView?=null
    var editTextName: EditText? =null
    var editTextFecha: EditText?=null
    var editTextLastName: EditText?=null
    var editTextPhone: EditText?=null
    var editTextDNI: EditText?=null
    var imgViewFecha: ImageView?=null
    var editTextEmail: EditText?=null
    var editTextPassword: EditText?=null
    var editTextPasswordConfirm: EditText?=null
    var btnRegister: Button?=null
    var userProvider = UsersProvider()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        textViewGoToLogin = findViewById(R.id.textview_go_to_login)
        editTextName = findViewById(R.id.edittext_name)
        editTextLastName = findViewById(R.id.edittext_lastname)
        editTextDNI = findViewById(R.id.edittext_dni)
        editTextPhone = findViewById(R.id.edittext_phone)
        editTextFecha = findViewById(R.id.edittext_fecha)
        editTextEmail = findViewById(R.id.edittext_email_register)
        imgViewFecha = findViewById(R.id.imgview_fecha)
        editTextPassword = findViewById(R.id.edittext_password)
        editTextPasswordConfirm = findViewById(R.id.edittext_confir_password)
        btnRegister = findViewById(R.id.btn_register)
        //? SI LA VARIABLE VIENE NULLA NO SE EJECUTA EL EVENTO
        btnRegister?.setOnClickListener { goToRegister() }
        textViewGoToLogin?.setOnClickListener{
            goToLogin()
        }
    }
    private fun goToRegister(){
        val TAG = "RegisterActivity"
        val name = editTextName?.text.toString()
        val lastname = editTextLastName?.text.toString()
        val phone = editTextPhone?.text.toString()
        val dni = editTextDNI?.text.toString()
        val fecha_nac=editTextFecha?.text.toString()
        val email = editTextEmail?.text.toString()
        val password = editTextPassword?.text.toString()
        val confirm_password = editTextPasswordConfirm?.text.toString()
        if (isValidForm(name,lastname,dni,phone,fecha_nac,email,password,confirm_password)){
            val user = User(
                name = name,
                lastname = lastname,
                phone = phone,
                dni=dni,
                fechaNacimiento = fecha_nac,
                email = email,
                password = password,
            )
            //si este metodo nos devuelve nulo ya no ejecutaria el enqueue
            userProvider.register(user)?.enqueue(object: Callback<ResponseHttp>{
                //en caso el servidor nos retorne una respuesta
                override fun onResponse(
                    call: Call<ResponseHttp>,
                    response: Response<ResponseHttp>
                ) {
                    Toast.makeText(this@RegisterActivity, response.body()?.message, Toast.LENGTH_LONG).show()
                    Log.d(TAG,"Response: ${response}")
                    Log.d(TAG,"Response: ${response.body()}")
                }
                //en caso que haya un fallo en el servidor
                override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                    Log.d(TAG,"Se produjo un error ${t.message}")
                    Toast.makeText(this@RegisterActivity, "Se produjo un error ${t.message}", Toast.LENGTH_LONG).show()
                }

            })

        }
    }
    // definimos esta funcion como String, a cualquier string podemos aplicar este metodo
    fun String.isEmailValid(): Boolean{
        //VAMOS A IMPORTAR ESTA CLASE
        //esta es la validacion que vamos a usar para validar si el usuario ingreso un email
        //RECORDAR : ESTA LINEA DE CODIGO VIENE DE LA DOCUMENTACION
        return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
    }
    //esta funcion va a retornar un booleano
    private fun isValidForm(
        name:String,
        lastname:String,
        dni:String,
        phone:String,
        fecha_nac:String,
        email: String,
        password:String,
        confirm_password:String,
    ): Boolean{
        if (email.isBlank()){
            Toast.makeText(this, "debes ingresar el correo", Toast.LENGTH_SHORT).show()
            return false
        }
        if (!email.isEmailValid()){
            Toast.makeText(this, "el email no es correcto", Toast.LENGTH_SHORT).show()
            return false
        }
        if (lastname.isBlank()){
            Toast.makeText(this, "Debes ingresar tus apellidos", Toast.LENGTH_SHORT).show()
        }
        if (dni.isBlank()){
            Toast.makeText(this, "Debes ingresar tu DNI", Toast.LENGTH_SHORT).show()
        }
        if (phone.isBlank()){
            Toast.makeText(this, "Debes ingresar tu numero de celular", Toast.LENGTH_SHORT).show()
        }
        if (name.isBlank()){
            Toast.makeText(this, "debes ingresar el nombre", Toast.LENGTH_SHORT).show()
            return false
        }
        if (fecha_nac.isBlank()){
            Toast.makeText(this, "debes registar tu fecha de nacimiento", Toast.LENGTH_SHORT).show()
            return false
        }
        if (password.isBlank()){
            Toast.makeText(this, "debes registar tu contraseña", Toast.LENGTH_SHORT).show()
            return false
        }
        if (confirm_password.isBlank()){
            Toast.makeText(this, "debes registar tu contraseña", Toast.LENGTH_SHORT).show()
            return false
        }
        if (password != confirm_password){
            Toast.makeText(this, "las contraseñas deben ser iguales", Toast.LENGTH_SHORT).show()
            return  false
        }
        return true
    }
    private fun goToLogin(){
        val i = Intent(this, MainActivity::class.java)
        startActivity(i)

    }

}
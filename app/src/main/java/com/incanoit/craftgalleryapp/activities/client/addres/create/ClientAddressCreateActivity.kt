package com.incanoit.craftgalleryapp.activities.client.addres.create

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.incanoit.craftgalleryapp.R
import com.incanoit.craftgalleryapp.activities.client.addres.list.ClientAddressListActivity
import com.incanoit.craftgalleryapp.activities.client.addres.map.ClientAddressMapActivity
import com.incanoit.craftgalleryapp.models.Address
import com.incanoit.craftgalleryapp.models.ResponseHttp
import com.incanoit.craftgalleryapp.models.User
import com.incanoit.craftgalleryapp.providers.AddressProvider
import com.incanoit.craftgalleryapp.utils.SharedPref
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ClientAddressCreateActivity : AppCompatActivity() {
    val TAG = "ClientAddressCreate"
    var toolbar: Toolbar?=null
    var editTextRefPoint: EditText?=null
    var editTextAddress: EditText?=null
    var editTextNeighborhood: EditText?=null
    var buttonCreateAddress: Button?=null
    var addressLat = 0.0
    var addressLng = 0.0
    var addressProvider: AddressProvider?=null
    var sharedPref: SharedPref?=null
    var user: User?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_address_create)
        toolbar = findViewById(R.id.toolbar)
        editTextRefPoint=findViewById(R.id.edittext_ref_point)
        editTextAddress=findViewById(R.id.edittext_address)
        editTextNeighborhood=findViewById(R.id.edittext_neighborhood)
        buttonCreateAddress = findViewById(R.id.btn_create_address)
        toolbar?.setTitleTextColor(ContextCompat.getColor(this ,R.color.black))
        toolbar?.title="Nueva Direccion"
        sharedPref = SharedPref(this)
        getUsersFromSession()
        addressProvider= AddressProvider(user?.sessionToken!!)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        editTextRefPoint?.setOnClickListener { goToAddressMap() }
        buttonCreateAddress?.setOnClickListener { createAddress() }

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
    private fun createAddress(){
        val address = editTextAddress?.text.toString()
        val neirghborhood = editTextNeighborhood?.text.toString()
        if (isValidForm(address, neirghborhood)){
            //Lanzar la peticion
                val addressModel = Address(
                    address = address,
                    neighborhood = neirghborhood,
                    idUser = user?.id!!,
                    lat = addressLat,
                    lng = addressLng
                )
            addressProvider?.create(addressModel)?.enqueue(object: Callback<ResponseHttp>{
                override fun onResponse(
                    call: Call<ResponseHttp>,
                    response: Response<ResponseHttp>
                ) {
                    if (response.body() !=null){
                        Toast.makeText(this@ClientAddressCreateActivity, response.body()?.message, Toast.LENGTH_LONG).show()
                        goToAddressList()
                    }else{
                        Toast.makeText(this@ClientAddressCreateActivity, "Ocurrio un error", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                    Toast.makeText(this@ClientAddressCreateActivity, "Error ${t.message}", Toast.LENGTH_SHORT).show()
                }

            })
        }

    }
    private fun goToAddressList(){
        val i = Intent(this,ClientAddressListActivity::class.java)
        startActivity(i)
    }
    private fun isValidForm(address: String,neirghborhood:String ): Boolean {
        if (address.isNullOrBlank()){
            Toast.makeText(this, "Ingresa tu direccion", Toast.LENGTH_SHORT).show()
            return false
        }
        if (neirghborhood.isNullOrBlank()){
            Toast.makeText(this, "Ingresa el barrio o recidencia", Toast.LENGTH_SHORT).show()
            return false
        }
        if (addressLat==0.0){
            Toast.makeText(this, "Selecciona el punto de referencia", Toast.LENGTH_SHORT).show()
            return false
        }
        if (addressLng==0.0){
            Toast.makeText(this, "Selecciona el punto de referencia", Toast.LENGTH_SHORT).show()
            return false
        }
        return true


    }
    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
        if (result.resultCode==Activity.RESULT_OK){
            val data = result.data
            val city = data?.getStringExtra("city")
            val address = data?.getStringExtra("address")
            val country = data?.getStringExtra("country")
            addressLat = data?.getDoubleExtra("lat",0.0)!!
            addressLng = data?.getDoubleExtra("lng",0.0)!!
            editTextRefPoint?.setText("$address $city")
            Log.d(TAG,"City: ${city}")
            Log.d(TAG,"address: ${address}")
            Log.d(TAG,"country: ${country}")
            Log.d(TAG,"Latitud: ${addressLat}")
            Log.d(TAG,"Longitud: ${addressLng}")


        }
    }
    private fun goToAddressMap(){
        val i = Intent(this,ClientAddressMapActivity::class.java)
        resultLauncher.launch(i)
    }

}
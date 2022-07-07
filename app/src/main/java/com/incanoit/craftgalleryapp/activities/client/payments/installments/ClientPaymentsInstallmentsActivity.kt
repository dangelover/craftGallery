package com.incanoit.craftgalleryapp.activities.client.payments.installments

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.reflect.TypeToken
import com.incanoit.craftgalleryapp.R
import com.incanoit.craftgalleryapp.activities.client.payments.status.ClientPaymentsStatusActivity
import com.incanoit.craftgalleryapp.adapters.ShoppingBagAdapater
import com.incanoit.craftgalleryapp.models.*
import com.incanoit.craftgalleryapp.providers.MercadoPagoProvider
import com.incanoit.craftgalleryapp.providers.PaymentsProvider
import com.incanoit.craftgalleryapp.utils.SharedPref
import com.tommasoberlose.progressdialog.ProgressDialogFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ClientPaymentsInstallmentsActivity : AppCompatActivity() {
    val TAG ="ClientPaymentsInst"
    var textViewTotal: TextView?=null
    var textViewInstallmentDescription: TextView?=null
    var buttonPay: Button?=null
    var spinnerInstallment: Spinner?=null
    var mercadoPagoProvider= MercadoPagoProvider()
    var paymentsProvider:PaymentsProvider?=null
    var cardToken=""
    var firstSixDigits=""
    var sharedPref: SharedPref?=null
    var user: User?=null
    var selectProducts = ArrayList<Product>()
    var gson = Gson()
    var total = 0.0
    var installmentsSelected = "" //cuota seleccionada
    var address: Address?=null
    var paymentMethodId=""
    var paymentTypeId=""
    var issureId=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_payments_installments)
        sharedPref= SharedPref(this)
        getUsersFromSession()
        getAddressFromSession()
        paymentsProvider= PaymentsProvider(user?.sessionToken!!)
        cardToken=intent.getStringExtra("cardToken").toString()
        firstSixDigits=intent.getStringExtra("firstSixDigits").toString()
        textViewTotal=findViewById(R.id.textview_total_pay)
        textViewInstallmentDescription=findViewById(R.id.textview_installments_description)
        spinnerInstallment=findViewById(R.id.spinner_installments)
        buttonPay=findViewById(R.id.btn_pay)
        buttonPay?.setOnClickListener {
            if (!installmentsSelected.isNullOrBlank()){
                createPayment()
            }else{
                Toast.makeText(this, "Debe seleccionar el numero de  cuota", Toast.LENGTH_SHORT).show()
            }
        }
        getProductsFromSharedPref()
        getInstallments()
    }
    private fun createPayment(){
        val order = Order(
            products = selectProducts,
            idClient = user?.id!!,
            idAddress = address?.id!!
        )
        val payer =Payer(email = user?.email!!)
        val mercadoPagoPayment = MercadoPagoPayment(
            order=order,
            token = cardToken,
            description = "CrafGallery",
            paymentMethodId = paymentMethodId,
            paymentTypeId = paymentTypeId,
            issuerId = issureId,
            payer =payer,
            transactionAmount = total,
            installments = installmentsSelected.toInt()

        )
        ProgressDialogFragment.showProgressBar(this)
        paymentsProvider?.create(mercadoPagoPayment)?.enqueue(object: Callback<ResponseHttp>{
            override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>) {
                ProgressDialogFragment.hideProgressBar(this@ClientPaymentsInstallmentsActivity)
                if (response.body()!=null){
                    if (response.body()?.isSuccess==true){
                        sharedPref?.remove("order")
                    }
                    Log.d(TAG,"Response: ${response}")
                    Log.d(TAG,"Response: ${response.body()}")
                    Toast.makeText(this@ClientPaymentsInstallmentsActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    val status = response.body()?.data?.get("status")?.asString
                    var lastFour= response.body()?.data?.get("card")?.asJsonObject?.get("last_four_digits")?.asString
                    goToPaymentsStatus(paymentMethodId,status!!,lastFour!!)
                }else{
                    goToPaymentsStatus(paymentMethodId,"denied","")
                    Toast.makeText(this@ClientPaymentsInstallmentsActivity, "No se pudo realizar el pago", Toast.LENGTH_SHORT).show()
                }

            }

            override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                ProgressDialogFragment.hideProgressBar(this@ClientPaymentsInstallmentsActivity)
                Log.d(TAG,"${t.message}")
                Toast.makeText(this@ClientPaymentsInstallmentsActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }

        })
    }
    private fun goToPaymentsStatus(paymentMethodId: String, paymentStatus: String, lastFourDigits: String){
        val i = Intent(this,ClientPaymentsStatusActivity::class.java)
        i.putExtra("paymentMethodId",paymentMethodId)
        i.putExtra("paymentStatus",paymentStatus)
        i.putExtra("lastFourDigits",lastFourDigits)
        i.flags=Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i)



    }
    private fun getInstallments(){
        Log.d(TAG,"$firstSixDigits")
        Log.d(TAG,"$total")
        mercadoPagoProvider.getInstallment(firstSixDigits,"$total")?.enqueue(object: Callback<JsonArray>{
            override fun onResponse(call: Call<JsonArray>, response: Response<JsonArray>) {
                if (response.body()!=null){
                    val jsoninstallments = response.body()!!.get(0).asJsonObject.get("payer_costs").asJsonArray
                    var type = object: TypeToken<ArrayList<MercadoPagoInstallments>>(){}.type
                    val installments=gson.fromJson<ArrayList<MercadoPagoInstallments>>(jsoninstallments,type)
                    paymentMethodId=response.body()?.get(0)?.asJsonObject?.get("payment_method_id")?.asString!!
                    paymentTypeId=response.body()?.get(0)?.asJsonObject?.get("payment_type_id")?.asString!!
                    issureId=response.body()?.get(0)?.asJsonObject?.get("issuer")?.asJsonObject?.get("id")?.asString!!
                    val arrayAdapter = ArrayAdapter<MercadoPagoInstallments>(this@ClientPaymentsInstallmentsActivity,android.R.layout.simple_dropdown_item_1line, installments)
                    spinnerInstallment?.adapter=arrayAdapter
                    spinnerInstallment?.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
                        override fun onItemSelected(
                            adapterView: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            l: Long
                        ) {
                            installmentsSelected = "${installments[position].installments}"
                            Log.d(TAG,"Cuotas seleccionadas: $installmentsSelected")
                            textViewInstallmentDescription?.text=installments[position].recommendedMessage
                        }

                        override fun onNothingSelected(p0: AdapterView<*>?) {

                        }

                    }

                    Log.d(TAG,"Response $response")
                    Log.d(TAG,"installments $jsoninstallments")
                }
            }

            override fun onFailure(call: Call<JsonArray>, t: Throwable) {
                Log.d(TAG,"Error: ${t.message}")
            }

        })
    }
    private fun getProductsFromSharedPref(){
        //si en el sharedPref existe un elemento guardado con la key order
        if (!sharedPref?.getData("order").isNullOrBlank()){ // EXISTE UNA ORDEN EN SHAREDPREF
            //vamos a transformar una lista de tipo json a una arreglo de tipo Product
            val type = object: TypeToken<ArrayList<Product>>() {}.type
            //usamos el metodo fromJson para transformar de json a arraylist
            selectProducts = gson.fromJson(sharedPref?.getData("order"),type)
            for (p in selectProducts){
                total = total + (p.price * p.quantity!!)
            }
            textViewTotal?.text="S/. $total"
        }
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
    private fun getAddressFromSession(){
        if (!sharedPref?.getData("address").isNullOrBlank()){
            address = gson.fromJson(sharedPref?.getData("address"), Address::class.java) // SI EXISTE UNA DIRECCION
        }
    }
}
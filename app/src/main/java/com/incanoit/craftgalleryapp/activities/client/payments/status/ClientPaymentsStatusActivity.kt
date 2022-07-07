package com.incanoit.craftgalleryapp.activities.client.payments.status

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.incanoit.craftgalleryapp.R
import com.incanoit.craftgalleryapp.activities.client.home.ClientHomeActivity
import de.hdodenhof.circleimageview.CircleImageView

class ClientPaymentsStatusActivity : AppCompatActivity() {
    var textViewStatus: TextView?=null
    var circleImageStatus: CircleImageView?=null
    var buttonFinish: Button?=null
    var paymentMethodId = ""
    var paymentStatus=""
    var lastFourDigits = ""
    var textViewMessage:TextView?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_payments_status)
        textViewStatus= findViewById(R.id.textview_status_order)
        circleImageStatus=findViewById(R.id.circleimage_status)
        textViewMessage=findViewById(R.id.textview_message)
        buttonFinish=findViewById(R.id.btn_finish)
        paymentMethodId= intent.getStringExtra("paymentMethodId").toString()
        paymentStatus= intent.getStringExtra("paymentStatus").toString()
        lastFourDigits= intent.getStringExtra("lastFourDigits").toString()
        if (paymentStatus=="approved"){
            circleImageStatus?.setImageResource(R.drawable.ic_check)
            textViewMessage?.text="GRACIAS POR TU COMPRA"
            textViewStatus?.text="Tu orden fue procesada exitosamente usando ($paymentMethodId **** $lastFourDigits ) \n \nMira el estado de tu compra en la seccion mis pedidos"
        }else{
            circleImageStatus?.setImageResource(R.drawable.ic_cancel)
            textViewMessage?.text="SU COMPRA NO FUE EXITOSA"
            textViewStatus?.text="Hubo un error procesando el pago"
        }
        buttonFinish?.setOnClickListener { goToHome() }

    }
    private fun goToHome(){
        val i= Intent(this,ClientHomeActivity::class.java)
        i.flags=Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(i)
    }
}
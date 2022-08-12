package com.incanoit.craftgalleryapp.activities.ceramica.terminos

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.incanoit.craftgalleryapp.R
import com.incanoit.craftgalleryapp.fragments.ceramica.CeramicaProductFragment

class TerminosActivity : AppCompatActivity() {
    var btn_acept: Button?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terminos)
        btn_acept=findViewById(R.id.btn_term_acept)
//        btn_acept?.setOnClickListener {
//            goToBack()
//
//        }
    }
    private fun goToBack(){
        val i = Intent(this,CeramicaProductFragment::class.java)
        startActivity(i)
    }
}
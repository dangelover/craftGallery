package com.incanoit.craftgalleryapp.fragments.client

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.incanoit.craftgalleryapp.R
import com.incanoit.craftgalleryapp.activities.MainActivity
import com.incanoit.craftgalleryapp.activities.SelectRolesActivity
import com.incanoit.craftgalleryapp.activities.client.update.ClientUpdateActivity
import com.incanoit.craftgalleryapp.models.User
import com.incanoit.craftgalleryapp.utils.SharedPref
import de.hdodenhof.circleimageview.CircleImageView


class ClientProfileFragment : Fragment() {
    var myView: View? = null
    var button_select_rol: Button?= null
    var buttonUpdateProfile: Button?=null
    var circleImageUser: CircleImageView?= null
    var textViewName: TextView?=null
    var textViewEmail: TextView?=null
    var textViewDNI: TextView?=null
    var textViewPhone: TextView?=null
    var sharedPref:SharedPref?=null
    var imageViewLogout: ImageView?=null
    var user: User?=null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //dentro de la variable myView guardamos todo el fragment
        myView= inflater.inflate(R.layout.fragment_client_profile, container, false)
        sharedPref = SharedPref(requireActivity())
        button_select_rol = myView?.findViewById(R.id.btn_select_rol)
        buttonUpdateProfile = myView?.findViewById(R.id.btn_update_profile)
        circleImageUser = myView?.findViewById(R.id.circle_image_user)
        textViewName=myView?.findViewById(R.id.textview_name)
        textViewEmail=myView?.findViewById(R.id.textview_email)
        textViewDNI=myView?.findViewById(R.id.textview_dni)
        textViewPhone=myView?.findViewById(R.id.textview_phone)
        imageViewLogout = myView?.findViewById(R.id.imageview_logout)
        button_select_rol?.setOnClickListener { goToSelectRol() }
        imageViewLogout?.setOnClickListener { logout() }
        buttonUpdateProfile?.setOnClickListener {
            goToUpdate()
        }
        getUsersFromSession()
        textViewName?.text="${user?.name} ${user?.lastname}"
        textViewEmail?.text="${user?.email}"
        textViewDNI?.text="${user?.dni}"
        textViewPhone?.text="${user?.phone}"
        if (!user?.image.isNullOrBlank()){
            Glide.with(requireContext()).load(user?.image).into(circleImageUser!!)
        }
        return myView

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
    private fun goToUpdate(){
        val i = Intent(requireContext(), ClientUpdateActivity::class.java)
        startActivity(i)

    }
    private fun  logout(){
        //usamos el metodo remove de la clase sharedPref y el pasamos el key
        sharedPref?.remove("user")
        val i = Intent(requireContext(), MainActivity::class.java)
        startActivity(i)

    }
    private fun goToSelectRol(){
        val i = Intent(requireContext(), SelectRolesActivity::class.java)
        i.flags=
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK //eliminar el historial de pantallas
        startActivity(i)
    }

}
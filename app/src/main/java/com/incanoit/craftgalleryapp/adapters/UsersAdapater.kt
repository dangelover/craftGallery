package com.incanoit.craftgalleryapp.adapters

import android.app.Activity
import android.content.Intent
import android.media.Image
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.incanoit.craftgalleryapp.R
import com.incanoit.craftgalleryapp.activities.ceramica.home.CeramicaHomeActivity
import com.incanoit.craftgalleryapp.activities.client.home.ClientHomeActivity
import com.incanoit.craftgalleryapp.activities.client.products.list.ClientProductsListActivity
import com.incanoit.craftgalleryapp.activities.delivery.home.DeliveryHomeActivity
import com.incanoit.craftgalleryapp.fragments.admins.update.UserUpdateActivity
import com.incanoit.craftgalleryapp.models.Category
import com.incanoit.craftgalleryapp.models.ResponseHttp
import com.incanoit.craftgalleryapp.models.Rol
import com.incanoit.craftgalleryapp.models.User
import com.incanoit.craftgalleryapp.providers.UsersProvider
import com.incanoit.craftgalleryapp.utils.SharedPref
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

//creamos esta clase que va a recibir el activity y el array de roles
class UsersAdapater(val context:Activity, val usuarios:ArrayList<User>):RecyclerView.Adapter<UsersAdapater.UsuariosViewHolder>() {
    val sharedPref= SharedPref(context)
    var usersProvider = UsersProvider()
    var user:User?=null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsuariosViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cardview_usuarios, parent,false)
        return UsuariosViewHolder(view)
    }
    //este metodo va a retornar la cantidad de roles que tenemos
    override fun getItemCount(): Int {
        return usuarios.size
    }

    override fun onBindViewHolder(holder: UsuariosViewHolder, position: Int) {
        val usuario = usuarios[position]//obtenemos cada una de las categorias
        Log.d("UserAdapater","${usuario.dni}")
        holder.textViewNameUsuario.text=usuario.name
        holder.textViewDNIUser.text=usuario.dni
//        usersProvider = UsersProvider(user?.sessionToken!!)

        //cargamos la librerira glide y le pasamos el contexto y en el load le pasamos la imagen que queremos mostrar
        //y en el into en donde queremos mostrar
        Glide.with(context).load(usuario.image).into(holder.imageViewUser)
//        holder.itemView.setOnClickListener {
////            goToProducts(usuarios)
//
//        }
        holder.imageViewDelete.setOnClickListener {
            deleteUsuario(usuario)
        }
        holder.imageViewEdit.setOnClickListener {
            editUsuario(usuario)
        }
    }
//    private fun goToProducts(usuario:User){
//        val i = Intent(context, ClientProductsListActivity::class.java)
//        //mediante el metodo putExtra le agregamos un nuevo parametro que queremos enviar
//        //el primer parametro es el nombre de la variable y el segundo parametro es el valor de la variable
//        i.putExtra("idCategory",usuario.id)
//        context.startActivity(i)
//
//
//    }

    private fun deleteUsuario(usuario:User){
        Log.d("UserAdapater","${usuario.id}")
        usersProvider.updateStatus(usuario.id!!)?.enqueue(object: Callback<ResponseHttp>{
            override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>) {
                Log.d("UserAdapater","RESPONSE: $response")
                Log.d("UserAdapater","RESPONSE: ${response.body()}")
                if (response.body()?.isSuccess==true){
                    Toast.makeText(context, "El estado se actualizo", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                Log.d("UserAdapater","Error: ${t.message}")
//                Toast.makeText(this@UsersAdapater, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }

        })

    }
    private fun editUsuario(usuario: User){
        val i = Intent(context,UserUpdateActivity::class.java)
        Log.d("UserAdapater","${usuario.id}")
        Log.d("UserAdapater","${usuario.fechaNacimiento}")
        Log.d("UserAdapater","${usuario.lastname}")
        Log.d("UserAdapater","${usuario.dni}")
        i.putExtra("idUsuario",usuario.id)
        i.putExtra("nombreUsuario",usuario.name)
        i.putExtra("dateBirth",usuario.fechaNacimiento)
        i.putExtra("lastname",usuario.lastname)
        i.putExtra("dni",usuario.dni)
        i.putExtra("celular",usuario.phone)
        i.putExtra("image",usuario.image)
        context.startActivity(i)
    }
    class UsuariosViewHolder(view: View): RecyclerView.ViewHolder(view) {

        val textViewNameUsuario: TextView
        val imageViewUser: ImageView
        val textViewDNIUser: TextView
        val imageViewEdit: ImageView
        val imageViewDelete: ImageView

        init {
            textViewNameUsuario = view.findViewById(R.id.textview_name_user)
            imageViewUser = view.findViewById(R.id.imageview_user_list)
            textViewDNIUser = view.findViewById(R.id.textview_dni_usuario)
            imageViewEdit=view.findViewById(R.id.image_view_edit)
            imageViewDelete=view.findViewById(R.id.image_view_delete)

        }

    }
}
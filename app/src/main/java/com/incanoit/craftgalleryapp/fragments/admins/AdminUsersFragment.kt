package com.incanoit.craftgalleryapp.fragments.admins

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.incanoit.craftgalleryapp.R
import com.incanoit.craftgalleryapp.adapters.UsersAdapater
import com.incanoit.craftgalleryapp.models.User
import com.incanoit.craftgalleryapp.providers.UsersProvider
import com.incanoit.craftgalleryapp.utils.SharedPref
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AdminUsersFragment : Fragment() {
    val TAG ="AdminUsers"
    var myView: View?=null
    var recyclerViewUser:RecyclerView?=null
    var usersProvider:UsersProvider?=null
    var adapter: UsersAdapater?=null
    var user:User?=null
    var sharedPref:SharedPref?=null
    var users= ArrayList<User>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        myView= inflater.inflate(R.layout.fragment_admin_infor_list, container, false)
        recyclerViewUser=myView?.findViewById(R.id.recyclerview_usuarios_list)
        recyclerViewUser?.layoutManager=LinearLayoutManager(requireContext())
        sharedPref = SharedPref(requireActivity())
        getUsersFromSession()
        usersProvider = UsersProvider(user?.sessionToken!!)
        getUsuarios()

        return  myView
    }
    private fun getUsuarios(){
        usersProvider?.getAllUser()?.enqueue(object: Callback<ArrayList<User>>{
            override fun onResponse(
                call: Call<ArrayList<User>>,
                response: Response<ArrayList<User>>
            ) {
                if (response.body()!=null){
                    users=response.body()!!
                    Log.d(TAG,"$users")
                    adapter=UsersAdapater(requireActivity(),users)
                    recyclerViewUser?.adapter=adapter
                }
            }

            override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {
                Log.d(TAG,"Error: ${t.message}")
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_LONG).show()
            }

        })
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

}
package com.incanoit.craftgalleryapp.activities.administrador.users.lists

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.incanoit.craftgalleryapp.R
import com.incanoit.craftgalleryapp.adapters.UsersAdapater
import com.incanoit.craftgalleryapp.models.User
import com.incanoit.craftgalleryapp.providers.UsersProvider
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdminUsersListActivity : AppCompatActivity() {
    val TAG = "AdminUsers"
    var recyclerViewUsers: RecyclerView?=null
    var adapter: UsersAdapater?=null
    var user:User?=null
    var userProvider: UsersProvider?=null
    var usuarios:ArrayList<User> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_users_list)
        recyclerViewUsers=findViewById(R.id.recyclerview_usuarios)
        recyclerViewUsers?.layoutManager = GridLayoutManager(this,2)
        getUsers()

    }
    private fun getUsers(){
        userProvider?.getAllUser()?.enqueue(object: Callback<ArrayList<User>>{
            override fun onResponse(
                call: Call<ArrayList<User>>,
                response: Response<ArrayList<User>>
            ) {
                if (response.body()!=null){
                    usuarios=response.body()!!
                    adapter= UsersAdapater(this@AdminUsersListActivity,usuarios)
                    recyclerViewUsers?.adapter = adapter
                }
            }

            override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {
                Toast.makeText(this@AdminUsersListActivity, t.message, Toast.LENGTH_SHORT).show()
                Log.d(TAG,"Error: ${t.message}")
            }

        })
    }
}
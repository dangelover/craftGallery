package com.incanoit.craftgalleryapp.fragments.client

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.incanoit.craftgalleryapp.R
import com.incanoit.craftgalleryapp.activities.client.shoping_bag.ClientBagActivity
import com.incanoit.craftgalleryapp.adapters.CategoriesAdapater
import com.incanoit.craftgalleryapp.models.Category
import com.incanoit.craftgalleryapp.models.User
import com.incanoit.craftgalleryapp.providers.CategoriesProvider
import com.incanoit.craftgalleryapp.utils.SharedPref
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ClientCategoriesFragment : Fragment() {
    val TAG ="CategoriesFragment"
    var myView:View?=null
    var recyclerViewCategories: RecyclerView?=null
    var categoriesProvider: CategoriesProvider?=null
    var adapter: CategoriesAdapater?=null
    var user: User?=null
    var sharedPref: SharedPref?=null
    var categories = ArrayList<Category>()
    var toolbar: Toolbar?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        myView= inflater.inflate(R.layout.fragment_client_categories, container, false)
        setHasOptionsMenu(true)
        toolbar= myView?.findViewById(R.id.toolbar)
        toolbar?.title="Categorias"
        toolbar?.setTitleTextColor(ContextCompat.getColor(requireContext(),R.color.black))
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        recyclerViewCategories= myView?.findViewById(R.id.recyclerview_categories)
        recyclerViewCategories?.layoutManager=LinearLayoutManager(requireContext())
        sharedPref= SharedPref(requireActivity())
        getUsersFromSession()
        categoriesProvider = CategoriesProvider(user?.sessionToken!!)
        getCategories()
        return myView
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_shoping_bag,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId==R.id.item_shopping_bag){
            goToShopingBag()

        }
        return super.onOptionsItemSelected(item)
    }
    private fun goToShopingBag(){
        val i = Intent(requireContext(), ClientBagActivity::class.java)
        startActivity(i)
    }
    private fun getCategories(){
        categoriesProvider?.getAll()?.enqueue(object: Callback<ArrayList<Category>>{
            override fun onResponse(
                call: Call<ArrayList<Category>>,
                response: Response<ArrayList<Category>>
            ) {
                if (response.body()!= null){
                    categories=response.body()!!
                    adapter=CategoriesAdapater(requireActivity(),categories)
                    recyclerViewCategories?.adapter=adapter
                }
            }

            override fun onFailure(call: Call<ArrayList<Category>>, t: Throwable) {
                Log.d(TAG,"Error: ${t.message}")
                Toast.makeText(requireContext(),"Error: ${t.message}",Toast.LENGTH_LONG).show()
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
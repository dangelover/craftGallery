package com.incanoit.craftgalleryapp.fragments.ceramica

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.incanoit.craftgalleryapp.R
import com.incanoit.craftgalleryapp.adapters.CategoriesAdapater
import com.incanoit.craftgalleryapp.adapters.CategoriesArtezanosAdapater
import com.incanoit.craftgalleryapp.models.Category
import com.incanoit.craftgalleryapp.models.User
import com.incanoit.craftgalleryapp.providers.CategoriesProvider
import com.incanoit.craftgalleryapp.utils.SharedPref
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CeramicaListProductsFragment : Fragment() {
    val TAG="CeramicaListF"
    var myView:View?=null
    var recyclerViewCategoriesArtezanos:RecyclerView?=null
    var categoriesProvider:CategoriesProvider?=null
    var adapter:CategoriesArtezanosAdapater?=null
    var user:User?=null
    var sharedPref:SharedPref?=null
    var categories=ArrayList<Category>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        myView= inflater.inflate(R.layout.fragment_ceramica_list_products, container, false)
        recyclerViewCategoriesArtezanos=myView?.findViewById(R.id.recyclerview_categories_ceramica)
        recyclerViewCategoriesArtezanos?.layoutManager=LinearLayoutManager(requireContext())
        sharedPref= SharedPref(requireActivity())
        getUsersFromSession()
        categoriesProvider= CategoriesProvider(user?.sessionToken!!)
        getCategories()
        return myView
    }
    private fun getCategories(){
        categoriesProvider?.getAll()?.enqueue(object: Callback<ArrayList<Category>>{
            override fun onResponse(
                call: Call<ArrayList<Category>>,
                response: Response<ArrayList<Category>>
            ) {
                if (response.body()!=null){
                    categories=response.body()!!
                    adapter= CategoriesArtezanosAdapater(requireActivity(),categories)
                    recyclerViewCategoriesArtezanos?.adapter=adapter
                }
            }

            override fun onFailure(call: Call<ArrayList<Category>>, t: Throwable) {
                Log.d(TAG,"Error ${t.message}")
                Toast.makeText(requireContext(), "Error ${t.message}", Toast.LENGTH_LONG).show()
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
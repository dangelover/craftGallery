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
import com.incanoit.craftgalleryapp.adapters.OrdersCeramicsAdapater
import com.incanoit.craftgalleryapp.adapters.OrdersClientAdapater
import com.incanoit.craftgalleryapp.models.Order
import com.incanoit.craftgalleryapp.models.User
import com.incanoit.craftgalleryapp.providers.OrdersProvider
import com.incanoit.craftgalleryapp.utils.SharedPref
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CeramicaOrdersStatusFragment : Fragment() {
    var myView: View?=null
    var ordersProvider: OrdersProvider?=null
    var user: User?=null
    var sharedPref: SharedPref?=null
    var recyclerViewOrders: RecyclerView?=null
    var adapter: OrdersCeramicsAdapater?=null
    var status=""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        myView = inflater.inflate(R.layout.fragment_ceramica_orders_status, container, false)
        sharedPref = SharedPref(requireActivity())
        //obtenemos el estatus mediate el metodo getString desde el TabAdapter
        status= arguments?.getString("status")!!
        getUsersFromSession()
        ordersProvider= OrdersProvider(user?.sessionToken!!)
        recyclerViewOrders = myView?.findViewById(R.id.recyclerview_orders)
        recyclerViewOrders?.layoutManager=LinearLayoutManager(requireContext())
        getOrders()
        return myView
    }
    private fun getOrders(){
        ordersProvider?.getOrdersByStatus(status)?.enqueue(object: Callback<ArrayList<Order>>{
            override fun onResponse(
                call: Call<ArrayList<Order>>,
                response: Response<ArrayList<Order>>
            ) {
                if (response.body()!=null){
                    val orders = response.body()
                    adapter = OrdersCeramicsAdapater(requireActivity(),orders!!)
                    recyclerViewOrders?.adapter=adapter
                }
            }

            override fun onFailure(call: Call<ArrayList<Order>>, t: Throwable) {
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
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
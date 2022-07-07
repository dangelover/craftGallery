package com.incanoit.craftgalleryapp.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.incanoit.craftgalleryapp.fragments.ceramica.CeramicaOrdersStatusFragment
import com.incanoit.craftgalleryapp.fragments.client.ClientOrderStatusFragment
import com.incanoit.craftgalleryapp.fragments.delivery.DeliveryOrdersStatusFragment

class DeliveryTabsPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    var numberOfTabs: Int

):FragmentStateAdapter(fragmentManager,lifecycle) {
    override fun getItemCount(): Int {
        return numberOfTabs
    }
    override fun createFragment(position: Int): Fragment {
        when(position){
            0->{
                val bundle= Bundle()
                bundle.putString("status","DESPACHADO")
                val frament = DeliveryOrdersStatusFragment()
                frament.arguments = bundle
                return frament
            }
            1->{
                val bundle= Bundle()
                bundle.putString("status","EN CAMINO")
                val frament = DeliveryOrdersStatusFragment()
                frament.arguments = bundle
                return frament
            }
            2->{
                val bundle= Bundle()
                bundle.putString("status","ENTREGADO")
                val frament = DeliveryOrdersStatusFragment()
                frament.arguments = bundle
                return frament
            }
            else -> return DeliveryOrdersStatusFragment()
        }
    }
}
package com.incanoit.craftgalleryapp.fragments.admins

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.incanoit.craftgalleryapp.R
import com.incanoit.craftgalleryapp.providers.UsersProvider


class AdminInforListFragment : Fragment() {
    var myView: View?=null
    var recyclerViewUser:RecyclerView?=null
    var usersProvider:UsersProvider?=null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        myView= inflater.inflate(R.layout.fragment_admin_infor_list, container, false)
        return  myView
    }


}
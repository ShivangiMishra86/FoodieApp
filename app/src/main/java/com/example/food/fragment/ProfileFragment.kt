package com.example.food.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.food.R


class ProfileFragment : Fragment() {
    lateinit var sharedpreference: SharedPreferences
    lateinit var  nameProfile:TextView
    lateinit var  userProfile:TextView
    lateinit var  addProfile:TextView
    lateinit var  mobProfile:TextView
    lateinit var  emailProfile:TextView
    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view= inflater.inflate(R.layout.fragment_profile, container, false)
        sharedpreference= this.getActivity()!!.getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE)
        nameProfile=view.findViewById(R.id.profileName)
        userProfile=view.findViewById(R.id.profileUser)
        addProfile=view.findViewById(R.id.profileAdd)
        mobProfile=view.findViewById(R.id.profileMob)
        emailProfile=view.findViewById(R.id.profileEmail)



        nameProfile.text="Name:: ${sharedpreference.getString("name","User")}"
        userProfile.text="User_Id:: ${sharedpreference.getString("user_Id","User")}"
        mobProfile.text="Mobile_No.:: ${sharedpreference.getString("mobileNo","User")}"
        emailProfile.text="E-Mail:: ${sharedpreference.getString("EmailId","User")}"
        addProfile.text="Address:: ${sharedpreference.getString("address","User")}"



   return view }


    }


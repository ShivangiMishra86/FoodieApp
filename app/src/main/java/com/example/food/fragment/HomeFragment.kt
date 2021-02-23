package com.example.food.fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.*
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.food.util.ConnectionManager
import com.example.food.adapter.HomeRecyclerAdapter
import com.example.food.R
import com.example.food.model.Restaurant
import org.json.JSONException
import java.util.*

import kotlin.Comparator
import kotlin.collections.HashMap


//
class HomeFragment : Fragment() {

    lateinit var recyclerHome: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerAdapter: HomeRecyclerAdapter
    lateinit var progressLayout: RelativeLayout
    lateinit var progressBar: ProgressBar


    var resInfoList = arrayListOf<Restaurant>()
    var ratingComparator= Comparator<Restaurant>{ restaurant1, restaurant2 ->

    if(restaurant1.rating.compareTo(restaurant2.rating,true)==0){
            restaurant1.name.compareTo(restaurant2.name,true)
        }
        else{
    restaurant1.rating.compareTo(restaurant2.rating,true)
           }
    }

    var costLTHComparator= Comparator<Restaurant> {
        restaurant1, restaurant2 ->
        if (restaurant1.cost_for_one.compareTo(restaurant2.cost_for_one, true) == 0) {
            restaurant1.name.compareTo(restaurant2.name, true)
        } else {
            restaurant1.cost_for_one.compareTo(restaurant2.cost_for_one, true)
        }
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        recyclerHome = view.findViewById(R.id.recycleHomerView)
        progressBar = view.findViewById(R.id.progressBar)
        progressLayout = view.findViewById(R.id.progressLayout)
        progressLayout.visibility = View.VISIBLE
        setHasOptionsMenu(true)

        layoutManager = LinearLayoutManager(activity)
        val queue = Volley.newRequestQueue(activity as Context)
        val url = "http://13.235.250.119/v2/restaurants/fetch_result/"

        if (ConnectionManager().checkConnectivity(activity as Context)) {
            val jsonObjectRequestHome =
                object : JsonObjectRequest(Request.Method.GET, url, null, Response.Listener {
                    try {

                        progressLayout.visibility = View.GONE
                        println("*********************************************$it")
                        val iit = it.getJSONObject("data")
                        val success = iit.getBoolean("success")
                        println(success)
                        if (success) {
                            val data = iit.getJSONArray("data")

                            for (i in 0 until data.length()) {
                                val resJsonObject = data.getJSONObject(i)
                                val resObject = Restaurant(
                                        resJsonObject.getString("id"),
                                        resJsonObject.getString("name"),
                                        resJsonObject.getString("rating"),
                                        resJsonObject.getString("cost_for_one"),
                                        resJsonObject.getString("image_url")
                                )


                                resInfoList.add(resObject)

                                recyclerAdapter =
                                        HomeRecyclerAdapter(activity as Context, resInfoList)

                                recyclerHome.adapter = recyclerAdapter

                                recyclerHome.layoutManager = layoutManager

                                recyclerHome.addItemDecoration(
                                    DividerItemDecoration(
                                        recyclerHome.context,
                                        (layoutManager as LinearLayoutManager).orientation
                                    )
                                )
                            }
                        } else {
                            Toast.makeText(
                                activity as Context, "Some error occurred",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } catch (e: JSONException) {
                        Toast.makeText(
                            activity as Context, "Some unexpected error occurred",
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                }
                    , Response.ErrorListener {
                        println("response is $it")
                        Toast.makeText(
                            activity as Context, "Some Volley error occurred",
                            Toast.LENGTH_SHORT
                        ).show()
                    }) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Content-type"] = "application/json"
                        headers["token"] = "9bf534118365f1"
                        return headers
                    }

                }
            queue.add(jsonObjectRequestHome)
        } else {
            val dialog = AlertDialog.Builder(activity as Context)
            dialog.setTitle("error")
            dialog.setMessage("Internet connection not found")
            dialog.setPositiveButton("Open Settings") { text, listener ->
                val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                activity?.finish()
            }
            dialog.setNegativeButton("Exit") { text, listener ->
                ActivityCompat.finishAffinity(activity as Activity)
            }
            dialog.create()
            dialog.show()
        }



        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater?.inflate(R.menu.menu_home,menu)
    }
    var previousMenuItem: MenuItem?=null
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(previousMenuItem!=null){
            previousMenuItem?.isChecked=false
        }
        item.isCheckable=true
        item.isChecked=true
        previousMenuItem=item
        val id=item?.itemId
        when(id) {
            R.id.sort_high_to_low -> {

                Collections.sort(resInfoList, costLTHComparator)
                resInfoList.reverse()
            }
            R.id.sort_low_to_high -> {
                Collections.sort(resInfoList, costLTHComparator)
            }
            R.id.sortRating -> {
                Collections.sort(resInfoList, ratingComparator)
                resInfoList.reverse()
            }
        }
        recyclerAdapter.notifyDataSetChanged()
   return super.onOptionsItemSelected(item)
    }

}












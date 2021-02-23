package com.example.food.fragment


import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.food.*
import com.example.food.adapter.OrderHistoryParentRecyclerAdapter
import com.example.food.model.OrderHistoryChild
import com.example.food.model.OrderHistoryParent
import com.example.food.util.ConnectionManager
import org.json.JSONException

/**
 * A simple [Fragment] subclass.
 */
class OrderFragment : Fragment() {
    lateinit var progressLayoutOrder: RelativeLayout
    lateinit var progressBarOrder: ProgressBar
    lateinit var sharedpreference: SharedPreferences
    lateinit var recyclerOrderHis: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerAdapter: OrderHistoryParentRecyclerAdapter

    var orderInfoList = arrayListOf<OrderHistoryParent>()
    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view= inflater.inflate(R.layout.fragment_order, container, false)

        recyclerOrderHis = view.findViewById(R.id.recycleOrderView)
        progressBarOrder = view.findViewById(R.id.progressBarOrderHis)
        progressLayoutOrder = view.findViewById(R.id.progressLayoutOrderHis)
        progressLayoutOrder.visibility = View.VISIBLE
        layoutManager = LinearLayoutManager(activity)
        sharedpreference= this.getActivity()!!.getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE)

        val user= sharedpreference.getString("user_Id","User")

        // Inflate the layout for this fragment
        val queue = Volley.newRequestQueue(activity as Context)
        val url = "http://13.235.250.119/v2/orders/fetch_result/${user}"
        if (ConnectionManager().checkConnectivity(activity as Context)) {
            val jsonObjectRequestHome =
                object : JsonObjectRequest(Request.Method.GET, url, null, Response.Listener {
                    try {
                        progressLayoutOrder.visibility = View.GONE
                        val jObj = it.getJSONObject("data")
                        val success = jObj.getBoolean("success")
                        if (success) {
                            val data = jObj.getJSONArray("data")
                            for (i in 0 until data.length()) {

                                var listOH=arrayListOf<OrderHistoryChild>()
                                val resJsonObject = data.getJSONObject(i)
                                val foodItem=resJsonObject.getJSONArray("food_items")
                                for(j in 0 until foodItem.length())
                                {
                                    val obj=foodItem.getJSONObject(j)
                                  val listItem= OrderHistoryChild(obj.getString("food_item_id"), obj.getString("name"), obj.getString("cost"))
                                    listOH.add(listItem)
                                }
                                val resObject = OrderHistoryParent(
                                        resJsonObject.getString("order_id"),
                                        resJsonObject.getString("restaurant_name"),
                                        resJsonObject.getString("total_cost"),
                                        resJsonObject.getString("order_placed_at"),
                                        listOH


                                )

                               orderInfoList.add(resObject)

                                recyclerAdapter =
                                        OrderHistoryParentRecyclerAdapter(
                                                activity as Context
                                                , orderInfoList
                                        )

                                recyclerOrderHis.adapter = recyclerAdapter

                                recyclerOrderHis.layoutManager = layoutManager

                                recyclerOrderHis.addItemDecoration(
                                    DividerItemDecoration(
                                        recyclerOrderHis.context,
                                        (layoutManager as LinearLayoutManager).orientation
                                    )
                                )
                            }
                            if(orderInfoList.isEmpty())
                            {Toast.makeText(
                                    activity as Context, " Oops! you haven't placed any order yet.",
                                    Toast.LENGTH_SHORT
                            ).show()}
                        } else {
                            Toast.makeText(
                                activity as Context  , jObj.getString("errorMessage"),
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
                activity ?.finish()
            }
            dialog.setNegativeButton("Exit") { text, listener ->
                ActivityCompat.finishAffinity(activity as Activity)
            }
            dialog.create()
            dialog.show()
        }







     return view

    }


}

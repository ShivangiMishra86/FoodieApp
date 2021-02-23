package com.example.food.activity

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.room.Room
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.food.database.CartDatabase
import com.example.food.database.CartEntity
import com.example.food.util.ConnectionManager
import com.example.food.R
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class SuccessOrderActivity : AppCompatActivity() {
    lateinit var progressLayoutOrder: RelativeLayout
    lateinit var progressBarOrder: ProgressBar
    lateinit var  btnok: Button
    lateinit var sharedpreference: SharedPreferences
    var dbCartList=listOf<CartEntity>()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_success_order)

        progressBarOrder =findViewById(R.id.progressBarOrder)
        progressLayoutOrder =findViewById(R.id.progressLayoutOrder)
        btnok=findViewById(R.id.buttonOk)
        progressLayoutOrder.visibility = View.VISIBLE
        dbCartList = RetrieveCart(this@SuccessOrderActivity).execute().get()

        val bill=intent.getStringExtra("bill")

        sharedpreference=getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE)
        btnok.setOnClickListener { startActivity(Intent(this@SuccessOrderActivity,
            AppFrontActivity::class.java)) }

        val user= sharedpreference.getString("user_Id","User")

        val queue = Volley.newRequestQueue(this@SuccessOrderActivity)

        val url = "http://13.235.250.119/v2/place_order/fetch_result"
        val jsonParams = JSONObject()

        jsonParams.put("user_id",user)
        jsonParams.put("restaurant_id",dbCartList[0].RestaurantId )
        jsonParams.put("total_cost",bill)
       val data=JSONArray()
        for(i in 0 until dbCartList.size)
        { val jsonId = JSONObject()

            jsonId.put("food_item_id",dbCartList[i].ItemId)
            data.put(jsonId)
        }
        jsonParams.put("food",data)

        if (ConnectionManager().checkConnectivity(this@SuccessOrderActivity)) {
            val jsonObjectRequestHome =
                object :
                    JsonObjectRequest(Request.Method.POST, url, jsonParams, Response.Listener {
                        try {

                            val jObj = it.getJSONObject("data")
                            val success = jObj.getBoolean("success")
                            if (success) {

                                progressLayoutOrder.visibility = View.GONE

                                Toast.makeText(
                                    this@SuccessOrderActivity, "OrderPlaced",
                                    Toast.LENGTH_SHORT
                                ).show()


                            } else {
                                Toast.makeText(
                                    this@SuccessOrderActivity, "Some error occurred",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } catch (e: JSONException) {
                            Toast.makeText(
                                this@SuccessOrderActivity, "Some unexpected error occurred",
                                Toast.LENGTH_SHORT
                            ).show()

                        }
                    }
                        , Response.ErrorListener {
                            Toast.makeText(
                                this@SuccessOrderActivity, "Some Volley error occurred",
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
            val dialog = AlertDialog.Builder(this@SuccessOrderActivity)
            dialog.setTitle("error")
            dialog.setMessage("Internet connection not found")
            dialog.setPositiveButton("Open Settings") { text, listener ->
                val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                this@SuccessOrderActivity.finish()
            }
            dialog.setNegativeButton("Exit") { text, listener ->
                ActivityCompat.finishAffinity(this@SuccessOrderActivity)
            }
            dialog.create()
            dialog.show()
        }


    }
    class RetrieveCart(val context: Context): AsyncTask<Void, Void, List<CartEntity>>(){

        override fun doInBackground(vararg p0: Void?): List<CartEntity> {

            val db= Room.databaseBuilder(context, CartDatabase::class.java,"cart_db").build()
            return db.CartDao().getAllCart()
        }
    }

}

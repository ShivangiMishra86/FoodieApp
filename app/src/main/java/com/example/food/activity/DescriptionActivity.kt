package com.example.food.activity

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
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
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.food.*
import com.example.food.adapter.DescriptionRecyclerAdapter
import com.example.food.database.CartDatabase
import com.example.food.database.CartEntity
import com.example.food.model.RestaurantMenu
import com.example.food.util.ConnectionManager
import org.json.JSONException

class DescriptionActivity : AppCompatActivity() {
    lateinit var recyclerMenu: RecyclerView
    lateinit var layoutManagerMenu: RecyclerView.LayoutManager
    lateinit var toolbar: androidx.appcompat.widget.Toolbar
    lateinit var recyclerMenuAdapter: DescriptionRecyclerAdapter
    lateinit var progressLayoutMenu: RelativeLayout
    lateinit var progressBarMenu: ProgressBar
    public lateinit var  MenuCart: Button

    var menuInfoList = arrayListOf<RestaurantMenu>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_description)

        if (intent != null) {
            val id = intent.getStringExtra("id")
            val name=intent.getStringExtra("name")


            recyclerMenu = findViewById(R.id.recycleDesrView)
            progressBarMenu = findViewById(R.id.progressBarMenu)
            toolbar=findViewById(R.id.toolbarDes)
            setUpToolBar()
            progressLayoutMenu = findViewById(R.id.progressLayoutMenu)
            progressLayoutMenu.visibility = View.VISIBLE
            MenuCart = findViewById(R.id.MenuCart)

            MenuCart.visibility = View.GONE

           DeleteCart(this@DescriptionActivity).execute().get()


            MenuCart.setOnClickListener { val intent = Intent(this@DescriptionActivity, CartActivity::class.java)
                intent.putExtra("name",name)
                startActivity(intent)
                }
            layoutManagerMenu = LinearLayoutManager(this@DescriptionActivity)
            val queue = Volley.newRequestQueue(this@DescriptionActivity)
            val url = "http://13.235.250.119/v2/restaurants/fetch_result/${id}"
            if (ConnectionManager().checkConnectivity(this@DescriptionActivity)) {
                val jsonObjectRequestHome =
                    object : JsonObjectRequest(Request.Method.GET, url, null, Response.Listener {
                        try {
                            progressLayoutMenu.visibility = View.GONE
                            val jObj = it.getJSONObject("data")
                            val success = jObj.getBoolean("success")
                            if (success) {
                                val data = jObj.getJSONArray("data")
                                for (i in 0 until data.length()) {
                                    val resJsonObject = data.getJSONObject(i)
                                    val resObject = RestaurantMenu(
                                            resJsonObject.getString("id"),
                                            resJsonObject.getString("name"),
                                            resJsonObject.getString("cost_for_one"),
                                            resJsonObject.getString("restaurant_id")

                                    )

                                    menuInfoList.add(resObject)

                                    recyclerMenuAdapter =
                                            DescriptionRecyclerAdapter(
                                                    this@DescriptionActivity,
                                                    menuInfoList, MenuCart
                                            )

                                    recyclerMenu.adapter = recyclerMenuAdapter

                                    recyclerMenu.layoutManager = layoutManagerMenu

                                    recyclerMenu.addItemDecoration(
                                        DividerItemDecoration(
                                            recyclerMenu.context,
                                            (layoutManagerMenu as LinearLayoutManager).orientation
                                        )
                                    )
                                }
                            } else {
                                Toast.makeText(
                                    this@DescriptionActivity, "Some error occurred",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } catch (e: JSONException) {
                            Toast.makeText(
                                this@DescriptionActivity, "Some unexpected error occurred",
                                Toast.LENGTH_SHORT
                            ).show()

                        }
                    }
                        , Response.ErrorListener {
                            Toast.makeText(
                                this@DescriptionActivity, "Some Volley error occurred",
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
                val dialog = AlertDialog.Builder(this@DescriptionActivity)
                dialog.setTitle("error")
                dialog.setMessage("Internet connection not found")
                dialog.setPositiveButton("Open Settings") { text, listener ->
                    val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                    startActivity(settingsIntent)
                    this@DescriptionActivity.finish()
                }
                dialog.setNegativeButton("Exit") { text, listener ->
                    ActivityCompat.finishAffinity(this@DescriptionActivity)
                }
                dialog.create()
                dialog.show()
            }
        } else {
            finish()
            Toast.makeText(
                this@DescriptionActivity, "Some unexpected error occurred",
                Toast.LENGTH_SHORT
            ).show()


        }
    }




    private fun setUpToolBar(){
        setSupportActionBar(toolbar)

        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
    onBackPressed()
    return true}
    override fun onBackPressed() {
        val dialog = AlertDialog.Builder(this@DescriptionActivity)
        dialog.setTitle("Confirmation")
        dialog.setMessage("Going Back will RESET your cart items.Do you still want to Proceed?  ")
        dialog.setPositiveButton("yes") { text,listener->  Toast.makeText(
            this@DescriptionActivity, " Cart is Empty",
            Toast.LENGTH_SHORT
        ).show()
            super.onBackPressed()
            supportActionBar?.title="Home"
        }
        dialog.setNegativeButton("No") {text,listener->
            println("do Nothing")
            Toast.makeText(
                this@DescriptionActivity, "NoNo Deleted Deleted",
                Toast.LENGTH_SHORT
            ).show()

        }
        dialog.create()
        dialog.show()
    }
    class DeleteCart(val context: Context) : AsyncTask<Void, Void,Boolean>() {
        override fun doInBackground(vararg p0:Void?):Boolean
        { val db= Room.databaseBuilder(context, CartDatabase::class.java,"cart_db").build()
            val list=db.CartDao().getAllCart()
            for(element in list)
            {
                db.CartDao().deleteCart(element)
                db.close()
            }
            return true
        }

    }
}





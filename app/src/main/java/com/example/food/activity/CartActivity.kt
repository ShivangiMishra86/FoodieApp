package com.example.food.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.food.*
import com.example.food.adapter.CartRecyclerAdapter
import com.example.food.database.CartDatabase
import com.example.food.database.CartEntity

class CartActivity : AppCompatActivity() {
    lateinit var recyclerCart: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerCartAdapter: CartRecyclerAdapter
    lateinit var progressCartLayout: RelativeLayout
    lateinit var toolbar: androidx.appcompat.widget.Toolbar
    lateinit var progressCartBar: ProgressBar
    lateinit var placeorder: Button
    var dbCartList=listOf<CartEntity>()
    lateinit var restaurantName:TextView

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        recyclerCart = findViewById(R.id.recycleCartView)
        progressCartBar = findViewById(R.id.progressBarCart)
        progressCartLayout = findViewById(R.id.progressLayoutCart)
        progressCartLayout.visibility = View.VISIBLE
        placeorder = findViewById(R.id.PlaceOrderButton)
        layoutManager = LinearLayoutManager(this@CartActivity)
        restaurantName=findViewById(R.id.CartMenu)
        toolbar=findViewById(R.id.toolbarCart)
        setUpToolBar()

        dbCartList = RetrieveCart(this@CartActivity).execute().get()
        println("/n/n/n/n/n/n/n/n/n/n/n/n/n/n/n/n/n/n/n/n/n/n/")
        println(dbCartList)
        println("/n/n/n/n/n/n/n/n/n/n/n/n/n/n/n/n/n/n/n/n/n/n/")
        restaurantName.text="Ordering From:: ${intent.getStringExtra("name")}"
        var sum = 0
        for (element in dbCartList)
            sum += (element.ItemCost_for_one.toInt())
        placeorder.text = "place order (Total:$sum)"

        placeorder.setOnClickListener {
            val intent = Intent(this@CartActivity, SuccessOrderActivity::class.java)
           intent.putExtra("bill",sum.toString())
            startActivity(intent)
        }
        if (intent != null) {

            progressCartLayout.visibility = View.GONE
            recyclerCartAdapter = CartRecyclerAdapter(this@CartActivity, dbCartList)

            recyclerCart.adapter = recyclerCartAdapter

            recyclerCart.layoutManager = layoutManager


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

        class RetrieveCart(val context: Context) : AsyncTask<Void, Void, List<CartEntity>>() {

            override fun doInBackground(vararg p0: Void?): List<CartEntity> {

                val db = Room.databaseBuilder(context, CartDatabase::class.java, "cart_db").build()
                return db.CartDao().getAllCart()
            }
        }

    }





package com.example.food.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.food.database.CartDatabase
import com.example.food.database.CartEntity
import com.example.food.R
import com.example.food.model.RestaurantMenu

class DescriptionRecyclerAdapter (val context: Context, val itemList:ArrayList<RestaurantMenu>, val proceed_cart:Button): RecyclerView.Adapter<DescriptionRecyclerAdapter.MenuViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {

        val view=
            LayoutInflater.from(parent.context).inflate(R.layout.recycler_description_single,parent,false)
        return MenuViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }




    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {

        val res = itemList[position]
        holder.textMenuNo.text = "."
        holder.textResCost.text = res.cost_for_one
        holder.textMenuName.text = res.name

        val cartEntity= CartEntity(res.id?.toInt() as Int,
                res.restaurant_id,
                res.name,
                res.cost_for_one
        )

        val checkCart= DBCartAsyncTask(
                context,
                cartEntity,
                1
        ).execute()
        val isCart=checkCart.get()
        if(isCart){
            holder.add.text="Remove"
        }else{
            holder.add.text="Add"
        }


        holder.add.setOnClickListener {
            val cartEntity= CartEntity(res.id?.toInt() as Int,
                    res.restaurant_id,
                    res.name,
                    res.cost_for_one
            )


            val checkCart= DBCartAsyncTask(
                    context,
                    cartEntity,
                    1
            ).execute()
            val isCart=checkCart.get()
            if(isCart){

                holder.add.text="Remove"
            }else{
                holder.add.text="Add"
            }


            if(!DBCartAsyncTask(context, cartEntity, 1).execute().get()){
                val async= DBCartAsyncTask(context, cartEntity, 2).execute()
                val result=async.get()
                if(result){

                    Toast.makeText(
                        context, "  added to Cart",
                        Toast.LENGTH_SHORT
                    ).show()
                    holder.add.text="Remove"
                    proceed_cart.visibility=View.VISIBLE

                }
                else{ Toast.makeText(
                    context, "Try Again !!",
                    Toast.LENGTH_SHORT
                ).show()

                }
            }else{
                val async= DBCartAsyncTask(context, cartEntity, 3).execute()

                val result=async.get()
                if(result){
                    Toast.makeText(
                        context, " Removed From Cart",
                        Toast.LENGTH_SHORT
                    ).show()
                    holder.add.text="Add"

                    val check = RetrieveCart(context).execute().get()
                    if (check.isEmpty()) {
                        proceed_cart.visibility = View.GONE
                    }


                }
                else{ Toast.makeText(
                    context, "Try Again !!",
                    Toast.LENGTH_SHORT
                ).show()

                }


        }
    }}
    class MenuViewHolder(view: View): RecyclerView.ViewHolder(view){
        val textMenuNo: TextView =view.findViewById(R.id.menuNo)
        val textResCost: TextView =view.findViewById(R.id.resItemCost)
        val textMenuName: TextView =view.findViewById(R.id.resMenuItem)
        val add: Button =view.findViewById(R.id.cartAdd)



    }

    class DBCartAsyncTask(val context: Context, val CartEntity: CartEntity, val mode: Int): AsyncTask<Void, Void, Boolean>(){

        override fun doInBackground(vararg p0:Void?):Boolean{

            val db= Room.databaseBuilder(context, CartDatabase::class.java,"cart_db").build()
            when(mode)
            {
                1->{
                    val res: CartEntity?=db.CartDao().getCartById(CartEntity.ItemId.toString())
                    db.close()
                    return res!=null
                }
                2->{
                    db.CartDao().insertCart(CartEntity)
                    db.close()
                    return true

                }
                3->{
                    db.CartDao().deleteCart(CartEntity)
                    db.close()
                    return true
                }
            }



            return false
        }
    }
    class RetrieveCart(val context: Context): AsyncTask<Void, Void, List<CartEntity>>(){

        override fun doInBackground(vararg p0: Void?): List<CartEntity> {

            val db= Room.databaseBuilder(context, CartDatabase::class.java,"cart_db").build()
            return db.CartDao().getAllCart()
        }
    }



}


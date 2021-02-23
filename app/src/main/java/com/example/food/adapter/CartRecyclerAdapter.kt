package com.example.food.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.food.database.CartEntity
import com.example.food.R

class CartRecyclerAdapter (val context: Context, val itemList:List<CartEntity>): RecyclerView.Adapter<CartRecyclerAdapter.CartViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {

        val view=
            LayoutInflater.from(parent.context).inflate(R.layout.recycler_cart,parent,false)
        return CartViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }




    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {

        val res = itemList[position]
        holder.textCartNo.text = "."
        holder.textCartCost.text = res.ItemCost_for_one
        holder.textCartName.text = res.ItemCartName

    }
    class CartViewHolder(view: View): RecyclerView.ViewHolder(view){
        val textCartNo: TextView =view.findViewById(R.id.CartNo)
        val textCartCost: TextView =view.findViewById(R.id.resCartCost)
        val textCartName: TextView =view.findViewById(R.id.resCartItem)




    }


}


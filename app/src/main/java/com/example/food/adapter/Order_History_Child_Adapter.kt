package com.example.food.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.food.model.OrderHistoryChild
import com.example.food.R

class Order_History_Child_Adapter (val context: Context, val itemList:List<OrderHistoryChild>): RecyclerView.Adapter<Order_History_Child_Adapter.ChildOrderViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildOrderViewHolder {

        val view=
            LayoutInflater.from(parent.context).inflate(R.layout.order_history_child_single,parent,false)
        return ChildOrderViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }




    override fun onBindViewHolder(holder: ChildOrderViewHolder, position: Int) {

        val res = itemList[position]
        holder.textfoodNo.text = "."
        holder.textfoodName.text = res.name
        holder.textfoodCost.text = res.cost

    }
    class ChildOrderViewHolder(view: View): RecyclerView.ViewHolder(view){
        val textfoodNo: TextView =view.findViewById(R.id.foodNo)
        val textfoodName: TextView =view.findViewById(R.id.foodName)
        val textfoodCost: TextView =view.findViewById(R.id.foodCost)




    }


}



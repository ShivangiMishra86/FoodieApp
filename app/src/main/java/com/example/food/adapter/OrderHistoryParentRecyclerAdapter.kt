package com.example.food.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.food.model.OrderHistoryParent
import com.example.food.R

class OrderHistoryParentRecyclerAdapter (val context: Context, val itemList:List<OrderHistoryParent>): RecyclerView.Adapter<OrderHistoryParentRecyclerAdapter.ParentOrderViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParentOrderViewHolder {

        val view=
            LayoutInflater.from(parent.context).inflate(R.layout.order_history_parent_recycler,parent,false)
        return ParentOrderViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }




    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ParentOrderViewHolder, position: Int) {

        val res = itemList[position]
        var layoutManager: RecyclerView.LayoutManager
      var recyclerAdapter: Order_History_Child_Adapter
        holder.textName.text = res.restaurant_name
        holder.textDate.text = res.order_placed_at
        holder.texttime.text="Total:${res.total_cost}"

        recyclerAdapter =
                Order_History_Child_Adapter(context, res.food_items)
        var recyclerChild: RecyclerView = holder.recChild
        recyclerChild.adapter = recyclerAdapter
        layoutManager = LinearLayoutManager(context)
        recyclerChild.layoutManager = layoutManager

        recyclerChild.addItemDecoration(
            DividerItemDecoration(
                recyclerChild.context,
                (layoutManager as LinearLayoutManager).orientation
            )
        )

    }
    class ParentOrderViewHolder(view: View): RecyclerView.ViewHolder(view){
        val textDate: TextView =view.findViewById(R.id.date)
        val textName: TextView =view.findViewById(R.id.resnameOrder)
        val texttime: TextView =view.findViewById(R.id.time)

        val recChild:RecyclerView=view.findViewById(R.id.recycleOrderView)




    }


}



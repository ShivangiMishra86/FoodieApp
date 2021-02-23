package com.example.food.model

import com.example.food.model.OrderHistoryChild

data class OrderHistoryParent (

val order_id:String,
val restaurant_name:String,
val total_cost:String,
val order_placed_at:String,
val food_items:List<OrderHistoryChild>
)
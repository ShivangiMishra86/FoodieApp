package com.example.food.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Cart")
data class CartEntity (
    @PrimaryKey val ItemId:Int,
    @ColumnInfo(name = "Restaurant_Id")val RestaurantId:String,
    @ColumnInfo(name = "ItemCart_Name")val ItemCartName:String,
    @ColumnInfo(name = "ItemCost_for_one") val ItemCost_for_one:String

)
package com.example.food.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "restaurants")
data class RestaurantEntity (
    @PrimaryKey val resId:Int,
    @ColumnInfo(name = "res_Name")val resName:String,
    @ColumnInfo(name = "res_Rating") val resRating:String,
    @ColumnInfo(name = "resCost_for_one") val resCost_for_one:String,
    @ColumnInfo(name = "resImage_url") val resImage_url:String
)

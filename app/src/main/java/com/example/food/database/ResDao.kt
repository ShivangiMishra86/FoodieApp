package com.example.food.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ResDao {
    @Insert
fun insertRestaurant(restaurantEntity: RestaurantEntity)

@Delete
fun deleteRestaurant(restaurantEntity: RestaurantEntity)

@Query("SELECT*FROM restaurants")
fun getAllRes():List<RestaurantEntity>

@Query("SELECT * FROM restaurants WHERE resId=:resId")
fun getResById(resId:String): RestaurantEntity
}
package com.example.food.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CartDao {
    @Insert
    fun insertCart(cartEntity: CartEntity)

    @Delete
    fun deleteCart(cartEntity: CartEntity)

    @Query("SELECT*FROM Cart")
    fun getAllCart():List<CartEntity>

    @Query("SELECT * FROM Cart WHERE ItemId=:ItemId")
    fun getCartById(ItemId:String): CartEntity



}
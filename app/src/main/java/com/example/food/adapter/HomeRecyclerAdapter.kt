package com.example.food.adapter

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.food.R
import com.example.food.model.Restaurant
import com.example.food.database.RestaurantDatabase
import com.example.food.database.RestaurantEntity
import com.example.food.activity.DescriptionActivity
import com.squareup.picasso.Picasso

class HomeRecyclerAdapter (val context: Context, val itemList:ArrayList<Restaurant>): RecyclerView.Adapter<HomeRecyclerAdapter.HomeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {

        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.recycler_home_single, parent, false)
        return HomeViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }


    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {

        val res = itemList[position]
        val i =res.id
        holder.textResName.text = res.name
        holder.textResCost.text = res.cost_for_one
        holder.resRating.text = res.rating
        Picasso.get().load(res.image_url).error(R.drawable.img_food)
            .into(holder.imgRes)
        holder.llContent.setOnClickListener {
            Toast.makeText(
                context, "clicked on $i  ${holder.textResName.text}",
                Toast.LENGTH_SHORT
            ).show()

           val intent = Intent(context, DescriptionActivity::class.java)
            intent.putExtra("name",res.name)
            intent.putExtra("id", i)
            context.startActivity(intent)
        }


        val resEntity= RestaurantEntity(res.id?.toInt() as Int,
                res.name,
                res.rating,
                res.cost_for_one,
                res.image_url

        )
        val checkFav= DBFavAsyncTask(
                context,
                resEntity,
                1
        ).execute()
        val isFav=checkFav.get()
        if(isFav){
            val favColour= ContextCompat.getColor(context, R.color.colorFavourite)
            holder.imgFavRes.setColorFilter(favColour)
        }else{
            val favNoColour= ContextCompat.getColor(context, R.color.colorNoFavourite)
            holder.imgFavRes.setColorFilter(favNoColour)

        }

        holder.imgFavRes.setOnClickListener {

           val resEntity= RestaurantEntity(res.id?.toInt() as Int,
                   res.name,
                   res.rating,
                   res.cost_for_one,
                   res.image_url

           )
           val checkFav= DBFavAsyncTask(
                   context,
                   resEntity,
                   1
           ).execute()
           val isFav=checkFav.get()
           if(isFav){
               val favColour= ContextCompat.getColor(context, R.color.colorFavourite)
             holder.imgFavRes.setColorFilter(favColour)
           }else{
               val favNoColour= ContextCompat.getColor(context, R.color.colorNoFavourite)
               holder.imgFavRes.setColorFilter(favNoColour)

           }

           if(!DBFavAsyncTask(context, resEntity, 1).execute().get()){
               val async= DBFavAsyncTask(context, resEntity, 2).execute()
               val result=async.get()
               if(result){
                   Toast.makeText(
                       context, " ${holder.textResName.text } added to Favourites",
                       Toast.LENGTH_SHORT
                   ).show()
                   val favColour= ContextCompat.getColor(context, R.color.colorFavourite)
                   holder.imgFavRes.setColorFilter(favColour)
               }
               else{ Toast.makeText(
                   context, "Try Again !!",
                   Toast.LENGTH_SHORT
               ).show()

               }
           }else{
               val async= DBFavAsyncTask(context, resEntity, 3).execute()

               val result=async.get()
               if(result){
                   Toast.makeText(
                       context, " ${holder.textResName.text } removed from Favourites",
                       Toast.LENGTH_SHORT
                   ).show()
                   val favNoColour= ContextCompat.getColor(context, R.color.colorNoFavourite)
                   holder.imgFavRes.setColorFilter(favNoColour)
               }
               else{ Toast.makeText(
                   context, "Try Again !!",
                   Toast.LENGTH_SHORT
               ).show()

           }
           }
        }
    }

    class HomeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textResName: TextView = view.findViewById(R.id.resName)
        val textResCost: TextView = view.findViewById(R.id.rescost)
        val resRating: TextView = view.findViewById(R.id.rating)
       val imgFavRes: ImageView = view.findViewById(R.id.favRes)
        val imgRes: ImageView = view.findViewById(R.id.resImage)
        val llContent: LinearLayout = view.findViewById(R.id.llContent)
    }
    class DBFavAsyncTask(val context: Context, val resEntity: RestaurantEntity, val mode: Int): AsyncTask<Void, Void, Boolean>(){

        override fun doInBackground(vararg p0:Void?):Boolean{

            val db= Room.databaseBuilder(context, RestaurantDatabase::class.java,"res_db").build()
            when(mode)
            {
                1->{
                    val res: RestaurantEntity?=db.resDao().getResById(resEntity.resId.toString())
                    db.close()
                    return res!=null
                }
                2->{
                    db.resDao().insertRestaurant(resEntity)
                    db.close()
                    return true

                }
                3->{
                    db.resDao().deleteRestaurant(resEntity)
                    db.close()
                    return true
                }
            }



            return false
        }
    }



}
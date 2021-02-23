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
import com.example.food.database.RestaurantEntity
import com.example.food.activity.DescriptionActivity
import com.example.food.database.RestaurantDatabase
import com.squareup.picasso.Picasso

class FavouriteRecyclerAdapter  (val context: Context, val ResList:List<RestaurantEntity>): RecyclerView.Adapter<FavouriteRecyclerAdapter.FavouriteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteViewHolder {

        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.recycler_favourite_single, parent, false)
        return FavouriteViewHolder(view)
    }

    override fun getItemCount(): Int {
        return ResList.size
    }


    override fun onBindViewHolder(holder: FavouriteViewHolder, position: Int) {

        val res = ResList[position]
        val i = res.resId
        val s: String = i.toString()
        holder.textResFavName.text = res.resName
        holder.textResFavCost.text = res.resCost_for_one
        holder.resFavRating.text = res.resRating
        Picasso.get().load(res.resImage_url).error(R.drawable.ic_launcher_background)
                .into(holder.imgFavRes)
        holder.favContent.setOnClickListener {
            Toast.makeText(
                    context, "clicked on   ${holder.textResFavName.text}",
                    Toast.LENGTH_SHORT
            ).show()

            val intent = Intent(context, DescriptionActivity::class.java)
            intent.putExtra("id", s)
            context.startActivity(intent)
        }
        val resEntity = RestaurantEntity(res.resId,
                res.resName,
                res.resRating,
                res.resCost_for_one,
                res.resImage_url

        )
        val checkFav = DBFavAsyncTask(
                context,
                resEntity,
                1
        ).execute()
        val isFav = checkFav.get()
        if (isFav) {
            val favColour = ContextCompat.getColor(context, R.color.colorFavourite)
            holder.imgFav.setColorFilter(favColour)
        } else {
            val favNoColour = ContextCompat.getColor(context, R.color.colorNoFavourite)
            holder.imgFav.setColorFilter(favNoColour)

        }


        holder.imgFav.setOnClickListener {


            val resEntity = RestaurantEntity(res.resId,
                    res.resName,
                    res.resRating,
                    res.resCost_for_one,
                    res.resImage_url

            )
            val checkFav = DBFavAsyncTask(
                    context,
                    resEntity,
                    1
            ).execute()
            val isFav = checkFav.get()
            if (isFav) {
                val favColour = ContextCompat.getColor(context, R.color.colorFavourite)
                holder.imgFav.setColorFilter(favColour)
            } else {
                val favNoColour = ContextCompat.getColor(context, R.color.colorNoFavourite)
                holder.imgFav.setColorFilter(favNoColour)

            }

            if (!DBFavAsyncTask(context, resEntity, 1).execute().get()) {
                val async = DBFavAsyncTask(context, resEntity, 2).execute()
                val result = async.get()
                if (result) {
                    Toast.makeText(
                            context, " ${holder.textResFavName.text} added to Favourites",
                            Toast.LENGTH_SHORT
                    ).show()
                    val favColour = ContextCompat.getColor(context, R.color.colorFavourite)
                    holder.imgFav.setColorFilter(favColour)
                } else {
                    Toast.makeText(
                            context, "Try Again !!",
                            Toast.LENGTH_SHORT
                    ).show()

                }
            } else {
                val async = DBFavAsyncTask(context, resEntity, 3).execute()

                val result = async.get()
                if (result) {
                    Toast.makeText(
                            context, " ${holder.textResFavName.text} removed from Favourites",
                            Toast.LENGTH_SHORT
                    ).show()
                    val favNoColour = ContextCompat.getColor(context, R.color.colorNoFavourite)
                    holder.imgFav.setColorFilter(favNoColour)
                } else {
                    Toast.makeText(
                            context, "Try Again !!",
                            Toast.LENGTH_SHORT
                    ).show()

                }
            }
        }
    }


    class FavouriteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textResFavName: TextView = view.findViewById(R.id.resFavName)
        val textResFavCost: TextView = view.findViewById(R.id.resFavcost)
        val resFavRating: TextView = view.findViewById(R.id.FavRating)
        val imgFav: ImageView = view.findViewById(R.id.fav)
        val imgFavRes: ImageView = view.findViewById(R.id.resFavImage)
        val favContent: LinearLayout = view.findViewById(R.id.FavContent)
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
package com.example.food.fragment

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.food.adapter.FavouriteRecyclerAdapter
import com.example.food.R
import com.example.food.database.RestaurantDatabase
import com.example.food.database.RestaurantEntity


class FavFragment : Fragment() {
    lateinit var recyclerFav: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerFavAdapter: FavouriteRecyclerAdapter
    lateinit var progressFavLayout: RelativeLayout
    lateinit var progressFavBar: ProgressBar
    var dbResList=listOf<RestaurantEntity>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
   val view=inflater.inflate(R.layout.fragment_fav, container, false)
        recyclerFav = view.findViewById(R.id.recycleFavView)
        progressFavBar = view.findViewById(R.id.progressFavBar)
        progressFavLayout = view.findViewById(R.id.progressFavLayout)
        progressFavLayout.visibility = View.VISIBLE

        layoutManager =GridLayoutManager(activity as Context,2 )

        dbResList= RetrieveFavourite(activity as Context).execute().get()

if(dbResList.isEmpty())
{Toast.makeText(
        activity as Context, " Oops! you don't have any favourite restaurant",
        Toast.LENGTH_SHORT
).show()}

        if( activity!=null)
        {
            progressFavLayout.visibility = View.GONE
            recyclerFavAdapter = FavouriteRecyclerAdapter(activity as Context, dbResList)

            recyclerFav.adapter = recyclerFavAdapter

            recyclerFav.layoutManager = layoutManager



        }

        return view
    }
    class RetrieveFavourite(val context: Context):AsyncTask<Void,Void,List<RestaurantEntity>>(){

        override fun doInBackground(vararg p0: Void?): List<RestaurantEntity> {

            val db= Room.databaseBuilder(context, RestaurantDatabase::class.java,"res_db").build()
          return db.resDao().getAllRes()
        }
    }
}



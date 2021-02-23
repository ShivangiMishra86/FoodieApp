package com.example.food.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.food.*
import com.example.food.fragment.*
import com.google.android.material.navigation.NavigationView

class AppFrontActivity : AppCompatActivity() {
    lateinit var drawerLayout: DrawerLayout
    lateinit var coordinatorLayout: CoordinatorLayout
    lateinit var toolbar: androidx.appcompat.widget.Toolbar
    lateinit var frameLayout: FrameLayout
    lateinit var navigationView: NavigationView
    lateinit var sharedpreference: SharedPreferences
    var previousMenuItem: MenuItem?=null
    lateinit var header:View
    lateinit var imagedrawer:ImageView
    lateinit var textDrawer:TextView

    @SuppressLint("SetTextI18n")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_front)

        drawerLayout=findViewById(R.id.drawerLayout)
        coordinatorLayout=findViewById(R.id.coordinatorLayout)
        toolbar=findViewById(R.id.toolbar)

        frameLayout=findViewById(R.id.frame)
        navigationView=findViewById(R.id.navigationView)

        header=navigationView.getHeaderView(0)

        imagedrawer=header.findViewById(R.id.drawerHeaderImage)
        textDrawer=header.findViewById(R.id.drawerTitle)
        sharedpreference=getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE)
        setUpToolBar()
        textDrawer.text="Hi! ${sharedpreference.getString("name","Admin")}"
        openHome()
        val actionBarDrawerToggle= ActionBarDrawerToggle(this@AppFrontActivity,drawerLayout,
                R.string.open_drawer,
                R.string.close_drawer
        )

        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        imagedrawer.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(R.id.frame,
                    ProfileFragment()
            )
            .commit()
            supportActionBar?.title="Profile"
            drawerLayout.closeDrawers() }

        textDrawer.setOnClickListener {

            supportFragmentManager.beginTransaction().replace(R.id.frame,
                    ProfileFragment()
            )
            .commit()
            supportActionBar?.title="Profile"
            drawerLayout.closeDrawers()
        }


        navigationView.setNavigationItemSelectedListener {
            if(previousMenuItem!=null){
                previousMenuItem?.isChecked=false
            }
            it.isCheckable=true
            it.isChecked=true
            previousMenuItem=it

            when(it.itemId) {
               R.id.home -> {
                openHome()
                    drawerLayout.closeDrawers()
                }
                R.id.profile -> {
            supportFragmentManager.beginTransaction().replace(
                    R.id.frame,
                    ProfileFragment()
                    )
                        .commit()
                    supportActionBar?.title="Profile"
                    drawerLayout.closeDrawers()
                }
                R.id.favourite -> {
                supportFragmentManager.beginTransaction().replace(
                        R.id.frame,
                        FavFragment()
                )
                    .commit()
                supportActionBar?.title="Favourite"
                drawerLayout.closeDrawers()
            }

                R.id.Order_history -> {
                    supportFragmentManager.beginTransaction().replace(
                            R.id.frame,
                            OrderFragment()
                    )
                        .commit()
                    supportActionBar?.title="Order_History"
                    drawerLayout.closeDrawers()
                }
                R.id.Log_out -> {
                    val dialog = AlertDialog.Builder(this@AppFrontActivity)
                    dialog.setTitle("Confirmation")
                    dialog.setMessage("Are You Sure you want to logout?")
                    dialog.setPositiveButton("yes") { text, listener ->
                        sharedpreference.edit().clear().apply()
                        startActivity(Intent(this@AppFrontActivity,
                            MainActivity::class.java))

                    }
                    dialog.setNegativeButton("No") { text, listener ->

                    }
                    dialog.create()
                    dialog.show()

                  drawerLayout.closeDrawers()

                }
                R.id.FAQ -> {
                  supportFragmentManager.beginTransaction().replace(
                          R.id.frame,
                          FaqFragment()
                    )
                        .commit()
                    supportActionBar?.title="FAQ"
                    drawerLayout.closeDrawers()

                }
            }

            return@setNavigationItemSelectedListener true }



    }

    private fun setUpToolBar(){
        setSupportActionBar(toolbar)
        supportActionBar?.title="toolbar"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id=item.itemId
        if(id==android.R.id.home){
            drawerLayout.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)
    }
 private fun openHome(){
        val fragment= HomeFragment()
        val transaction=supportFragmentManager.beginTransaction()
        transaction.replace(
                R.id.frame,
            fragment
        )
        transaction.commit()
     navigationView.setCheckedItem(R.id.home)
        supportActionBar?.title="Home"
    }

    override fun onBackPressed() {

        when(supportFragmentManager.findFragmentById(R.id.frame)){
            !is HomeFragment ->
            {previousMenuItem?.isChecked=false

               navigationView.setCheckedItem(R.id.home)

                openHome()}
            else->{
                ActivityCompat.finishAffinity(this@AppFrontActivity)
                super.onBackPressed()}
        }

    }
}





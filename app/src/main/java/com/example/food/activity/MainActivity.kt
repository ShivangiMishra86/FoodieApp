package com.example.food.activity

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.food.util.ConnectionManager
import com.example.food.R
import org.json.JSONException
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    lateinit var Password: EditText
    lateinit var etMobileNumber: EditText
    lateinit var tForgot: TextView
    lateinit var tNoAccount: TextView
    lateinit var  btnLogin: Button

    lateinit var sharedpreference: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedpreference=getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE)
        var isLoggedIn= sharedpreference.getBoolean("isLoggedIn",false)

        if(isLoggedIn){

            startActivity(Intent(this@MainActivity,
                AppFrontActivity::class.java))
        }else{
            setContentView(R.layout.activity_main)
        }
        etMobileNumber=findViewById(R.id.editTextPhone)
       Password=findViewById(R.id.editTextPassword)

        btnLogin =findViewById(R.id.button)
        tForgot = findViewById(R.id.textViewForgot)
        tNoAccount=findViewById(R.id.textViewNoAccount)

        tForgot.setOnClickListener{


            startActivity( Intent(this@MainActivity,
                ForgotActivity::class.java)
            )
        }
        tNoAccount.setOnClickListener{

            startActivity(
                Intent(this@MainActivity,
                    NoAccountActivity::class.java)
            )
        }


        btnLogin.setOnClickListener {
            val MobileNo = etMobileNumber.text.toString()
            val password =Password.text.toString()
            val queue = Volley.newRequestQueue(this@MainActivity)
            val url = "http://13.235.250.119/v2/login/fetch_result/"
            val jsonParams = JSONObject()

            jsonParams.put("mobile_number", MobileNo)
            jsonParams.put("password", password)

            if (ConnectionManager().checkConnectivity(this@MainActivity)) {
                val jsonObjectRequestHome =
                    object :
                        JsonObjectRequest(Request.Method.POST, url, jsonParams, Response.Listener {
                            try {

                                val jObj = it.getJSONObject("data")
                                val success = jObj.getBoolean("success")
                                if (success) {


                                    val data = jObj.getJSONObject("data")

                                    val user_id=data.getString("user_id")
                                    val nam=data.getString("name")
                                    val email=data.getString("email")
                                    val mobile_no=data.getString("mobile_number")
                                    val add=data.getString("address")

                                    savePreference(mobile_no,email,nam,user_id,add)
                                    startActivity(Intent(this@MainActivity,
                                        AppFrontActivity::class.java))


                                } else {
                                    Toast.makeText(
                                        this@MainActivity, "${jObj.getString("errorMessage")}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } catch (e: JSONException) {
                                Toast.makeText(
                                    this@MainActivity, "Some unexpected error occurred",
                                    Toast.LENGTH_SHORT
                                ).show()

                            }
                        }
                            , Response.ErrorListener {
                                Toast.makeText(
                                    this@MainActivity, "Some Volley error occurred",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }) {
                        override fun getHeaders(): MutableMap<String, String> {
                            val headers = HashMap<String, String>()
                            headers["Content-type"] = "application/json"
                            headers["token"] = "9bf534118365f1"
                            return headers
                        }

                    }
                queue.add(jsonObjectRequestHome)
            } else {
                val dialog = AlertDialog.Builder(this@MainActivity)
                dialog.setTitle("error")
                dialog.setMessage("Internet connection not found")
                dialog.setPositiveButton("Open Settings") { text, listener ->
                    val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                    startActivity(settingsIntent)
                    this@MainActivity.finish()
                }
                dialog.setNegativeButton("Exit") { text, listener ->
                    ActivityCompat.finishAffinity(this@MainActivity)
                }
                dialog.create()
                dialog.show()
            }


        }



        }


    fun savePreference(mobileNo:String, EmailId:String,name:String,user_id:String,address:String){
        sharedpreference.edit().putBoolean("isLoggedIn",true).apply()
        sharedpreference.edit().putString("user_Id",user_id).apply()
        sharedpreference.edit().putString("name",name).apply()
        sharedpreference.edit().putString("mobileNo",mobileNo).apply()
        sharedpreference.edit().putString("EmailId",EmailId).apply()
        sharedpreference.edit().putString("address",address).apply()

    }

    override fun onBackPressed() {

        ActivityCompat.finishAffinity(this@MainActivity)
        super.onBackPressed()
    }

    }





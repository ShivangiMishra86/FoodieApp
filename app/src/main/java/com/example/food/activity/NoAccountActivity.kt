package com.example.food.activity

import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.*
import androidx.core.app.ActivityCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.food.util.ConnectionManager
import com.example.food.R
import org.json.JSONException
import org.json.JSONObject

class NoAccountActivity : AppCompatActivity() {
    lateinit var etDataname: EditText
    lateinit var etDataEmailId: EditText
    lateinit var etDataMobileNumber: EditText
    lateinit var etDataAddress: EditText
    lateinit var etDataPassword: EditText
    lateinit var etDataConfirmPassword: EditText
    lateinit var toolbar: androidx.appcompat.widget.Toolbar

    lateinit var btnRegister: Button
    lateinit var sharedpreference: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_no_account)

        etDataMobileNumber = findViewById(R.id.editTextDataMobile)
        etDataEmailId = findViewById(R.id.editTextDataEmailAddress)
        etDataname = findViewById(R.id.editTextDataName)
        etDataAddress = findViewById(R.id.editTextDataEmailAddress)
        etDataPassword = findViewById(R.id.editTextDataPassword)
        etDataConfirmPassword = findViewById(R.id.editTextDataConfirmPassword)
        btnRegister=findViewById(R.id.btnRegister)
        toolbar=findViewById(R.id.toolbarReg)
        setUpToolBar()

        btnRegister.setOnClickListener {
            val mobileNo = etDataMobileNumber.text.toString()
            val emailId = etDataEmailId.text.toString()
            val name = etDataname.text.toString()
            val address = etDataAddress.text.toString()
            val password = etDataPassword.text.toString()
            val confirmPassword = etDataConfirmPassword.text.toString()

            val queue = Volley.newRequestQueue(this@NoAccountActivity)
            val url = "http://13.235.250.119/v2/register/fetch_result"
            val jsonParams = JSONObject()
            jsonParams.put("name", name)
            jsonParams.put("mobile_number", mobileNo)
            jsonParams.put("password", password)
            jsonParams.put("address", address)
            jsonParams.put("email", emailId)
            if (ConnectionManager().checkConnectivity(this@NoAccountActivity)) {
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
                                    Toast.makeText(
                                            this@NoAccountActivity, "Registered Successfully",
                                            Toast.LENGTH_SHORT
                                    ).show()
                                    savePreference(mobile_no,email,nam,user_id,add)
                                    startActivity(Intent(this@NoAccountActivity,
                                        AppFrontActivity::class.java))


                                } else {

                                    Toast.makeText(
                                        this@NoAccountActivity, jObj.getString("errorMessage"),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } catch (e: JSONException) {
                                Toast.makeText(
                                    this@NoAccountActivity, "Some unexpected error occurred",
                                    Toast.LENGTH_SHORT
                                ).show()

                            }
                        }
                            , Response.ErrorListener {
                                Toast.makeText(
                                    this@NoAccountActivity, "Some Volley error occurred",
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
                val dialog = AlertDialog.Builder(this@NoAccountActivity)
                dialog.setTitle("error")
                dialog.setMessage("Internet connection not found")
                dialog.setPositiveButton("Open Settings") { text, listener ->
                    val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                    startActivity(settingsIntent)
                    this@NoAccountActivity.finish()
                }
                dialog.setNegativeButton("Exit") { text, listener ->
                    ActivityCompat.finishAffinity(this@NoAccountActivity)
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
    fun setUpToolBar(){
        setSupportActionBar(toolbar)

        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true}

}
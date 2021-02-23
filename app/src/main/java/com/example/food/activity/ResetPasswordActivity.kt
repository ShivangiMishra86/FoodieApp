package com.example.food.activity

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.food.util.ConnectionManager
import com.example.food.R
import kotlinx.android.synthetic.main.activity_reset_password.*
import org.json.JSONException
import org.json.JSONObject

class ResetPasswordActivity : AppCompatActivity() {
    lateinit var etPassword: EditText
    lateinit var etConfirmPassword: EditText
    lateinit var etotp: EditText
    lateinit var btnsubmit: Button
    lateinit var sharedpreference: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        etPassword = findViewById(R.id.editTextPass)
        etConfirmPassword = findViewById(R.id.editTextConf)
        btnsubmit = findViewById(R.id.buttonReset)
        sharedpreference=getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE)
        etotp = findViewById(R.id.Otp)


        val mob=intent.getStringExtra("MobileNo")

        btnsubmit.setOnClickListener {
            val Password = etPassword.text.toString()
            val ConfirmPass = etConfirmPassword.text.toString()
            val Otp = etotp.text.toString()
            if(Password==ConfirmPass){
                val queue = Volley.newRequestQueue(this@ResetPasswordActivity)

                val url = "http://13.235.250.119/v2/reset_password/fetch_result"
                val jsonParams = JSONObject()

                jsonParams.put("mobile_number", mob)
                jsonParams.put("password", Password)
                jsonParams.put("otp", Otp)

            println("*****************************************$jsonParams,$Password,$Otp")
                if (ConnectionManager().checkConnectivity(this@ResetPasswordActivity)) {
                    val jsonObjectRequestHome =
                        object :
                            JsonObjectRequest(
                                Request.Method.POST,
                                url,
                                jsonParams,
                                Response.Listener {
                                    try {
                                        println("*****************************************$it")

                                        val jObj = it.getJSONObject("data")
                                        val success = jObj.getBoolean("success")
                                        if (success) {
                                            sharedpreference.edit().clear().apply()
                                            Toast.makeText(
                                                this@ResetPasswordActivity,
                                                    jObj.getString("successMessage"),
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            startActivity(Intent(this@ResetPasswordActivity,
                                                MainActivity::class.java))


                                        } else {
                                            Toast.makeText(
                                                this@ResetPasswordActivity,
                                                    jObj.getString("errorMessage"),
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    } catch (e: JSONException) {
                                        Toast.makeText(
                                            this@ResetPasswordActivity,
                                            "Some unexpected error occurred",
                                            Toast.LENGTH_SHORT
                                        ).show()

                                    }
                                }
                                ,
                                Response.ErrorListener {
                                    Toast.makeText(
                                        this@ResetPasswordActivity, "Some Volley error occurred",
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
                    val dialog = AlertDialog.Builder(this@ResetPasswordActivity)
                    dialog.setTitle("error")
                    dialog.setMessage("Internet connection not found")
                    dialog.setPositiveButton("Open Settings") { text, listener ->
                        val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                        startActivity(settingsIntent)
                        this@ResetPasswordActivity.finish()
                    }
                    dialog.setNegativeButton("Exit") { text, listener ->
                        ActivityCompat.finishAffinity(this@ResetPasswordActivity)
                    }
                    dialog.create()
                    dialog.show()
                }



        }else{Toast.makeText(
                    this@ResetPasswordActivity, "Password not matched",
                    Toast.LENGTH_SHORT
            ).show()}

        }
    }
}
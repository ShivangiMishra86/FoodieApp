package com.example.food.activity

import android.app.AlertDialog
import android.content.Intent
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
import org.json.JSONException
import org.json.JSONObject

class ForgotActivity : AppCompatActivity() {
    lateinit var etEmailIdForgot: EditText
    lateinit var etMobileNumberForgot: EditText
    lateinit var  btnNext: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot)

        etMobileNumberForgot=findViewById(R.id.editTextPhoneForgot)
        etEmailIdForgot=findViewById(R.id.editTextEmailForgot)
        btnNext=findViewById(R.id.buttonNext)
        btnNext.setOnClickListener {

            val MobileNo=etMobileNumberForgot.text.toString()
            val EmailId=etEmailIdForgot.text.toString()
            val queue = Volley.newRequestQueue(this@ForgotActivity)

            val url = "http://13.235.250.119/v2/forgot_password/fetch_result"
            val jsonParams = JSONObject()

            jsonParams.put("mobile_number",MobileNo)
            jsonParams.put("email",EmailId)



            if (ConnectionManager().checkConnectivity(this@ForgotActivity)) {
                val jsonObjectRequestHome =
                    object :
                        JsonObjectRequest(Request.Method.POST, url, jsonParams, Response.Listener {
                            try {

                                val jObj = it.getJSONObject("data")
                                val success = jObj.getBoolean("success")
                                if (success) {
                                    val nxtActivity= Intent(this@ForgotActivity,
                                        ResetPasswordActivity::class.java)
                                    nxtActivity.putExtra("MobileNo",MobileNo)
                                    nxtActivity.putExtra("EmailId",EmailId)

                                    startActivity(nxtActivity)





                                } else {
                                    Toast.makeText(
                                        this@ForgotActivity, jObj.getString("errorMessage"),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } catch (e: JSONException) {
                                Toast.makeText(
                                    this@ForgotActivity, "Some unexpected error occurred",
                                    Toast.LENGTH_SHORT
                                ).show()

                            }
                        }
                            , Response.ErrorListener {
                                Toast.makeText(
                                    this@ForgotActivity, "Some Volley error occurred",
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
                val dialog = AlertDialog.Builder(this@ForgotActivity)
                dialog.setTitle("error")
                dialog.setMessage("Internet connection not found")
                dialog.setPositiveButton("Open Settings") { text, listener ->
                    val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                    startActivity(settingsIntent)
                    this@ForgotActivity.finish()
                }
                dialog.setNegativeButton("Exit") { text, listener ->
                    ActivityCompat.finishAffinity(this@ForgotActivity)
                }
                dialog.create()
                dialog.show()
            }



        }
    }
}

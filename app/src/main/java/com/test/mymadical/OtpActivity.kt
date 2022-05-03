package com.test.mymadical

import `in`.aabhasjindal.otptextview.OtpTextView
import `in`.aabhasjindal.otptextview.Utils
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.test.mymadical.Interface.GetUserInfoInterface
import com.test.mymadical.model.ModelgetUserDetailsInfo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OtpActivity : AppCompatActivity() {
    lateinit var otp_view: OtpTextView
    lateinit var btnverify: Button
    lateinit var tvsingup: TextView
    lateinit var tvDashBoard: TextView
    var fromloginotp: String = ""
    var mobileno: String = ""



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)
        otp_view = findViewById(R.id.otp_view)
        btnverify = findViewById(R.id.btnverify)
        tvsingup = findViewById(R.id.tvsingup)
        tvDashBoard = findViewById(R.id.tvDashBoard)


        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()?.setDisplayShowHomeEnabled(true)
        fromloginotp = intent.getStringExtra("otp").toString()
        mobileno = intent.getStringExtra("mobileno").toString()

            Toast.makeText(this, fromloginotp, Toast.LENGTH_SHORT).show()


        tvDashBoard.setOnClickListener {
            val intent = Intent(this, Dashboard::class.java)
            startActivity(intent)
        }

        tvsingup.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }



            btnverify.setOnClickListener {




                val Otpfromedt = otp_view.otp
                Log.e("otppp", Otpfromedt.toString() + "dd")


                if (Otpfromedt=="") {

                    Toast.makeText(this, "Enter Otp", Toast.LENGTH_SHORT).show()


                }else{

                    if (fromloginotp==Otpfromedt) {
                        ApiCall()

                        val inputMethodManager: InputMethodManager = getSystemService(
                            INPUT_METHOD_SERVICE
                        ) as InputMethodManager
                        inputMethodManager.hideSoftInputFromWindow(btnverify.getApplicationWindowToken(), 0)



                    } else {
                        Toast.makeText(this, "Worng Otp", Toast.LENGTH_SHORT).show()

                    }
                }
            }




    }

    private fun ApiCall() {
        val progress = LayoutInflater.from(this)
            .inflate(R.layout.custom_progress_dialog, null)


        val builder = AlertDialog.Builder(this)
            .setView(progress)
        val AlertDialog = builder.create()
        AlertDialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        AlertDialog.setCancelable(false) // disable dismiss by tapping outside of the dialog

        AlertDialog.show()
        val creation: GetUserInfoInterface =
            Retroclient.getSingleTonClient()!!.create(GetUserInfoInterface::class.java)
        val call = creation.getprodetaildata(mobileno)
        call.enqueue(object : Callback<ModelgetUserDetailsInfo>{
            override fun onResponse(
                call: Call<ModelgetUserDetailsInfo>,
                response: Response<ModelgetUserDetailsInfo>
            ) {
                if (response.isSuccessful) {

                    AlertDialog.dismiss()
                    val userdata = response.body()?.userDetailsMymedical

                    val name = userdata?.get(0)?.userName
                    val number = userdata?.get(0)?.userMobileno
                    val email = userdata?.get(0)?.userEmail
                    val image = userdata?.get(0)?.userImg
                    val userid = userdata?.get(0)?.userId
                    val cartitemtotal = userdata?.get(0)?.cart_total_item
                    Log.e("jbhhfg", image.toString())

                    com.test.mymadical.Utils.Utils().Logindata(this@OtpActivity, "is_login", "1")
                    com.test.mymadical.Utils.Utils()
                        .Logindata(this@OtpActivity, "uname", name.toString())
                    com.test.mymadical.Utils.Utils()
                        .Logindata(this@OtpActivity, "cartitemtotal", cartitemtotal.toString())
                    com.test.mymadical.Utils.Utils()
                        .Logindata(this@OtpActivity, "custID_key", userid.toString())
                    com.test.mymadical.Utils.Utils()
                        .Logindata(this@OtpActivity, "uemail", email.toString())
                    if (image !="") {
                        com.test.mymadical.Utils.Utils()
                            .Logindata(this@OtpActivity, "uimage", image.toString())
                        Log.e("dsddddsd",image.toString())
                    }
                    com.test.mymadical.Utils.Utils()
                        .Logindata(this@OtpActivity, "unumber", number.toString())
                    val intent =Intent(this@OtpActivity ,Dashboard::class.java)
                    startActivity(intent)
                    finish()

                }



            }

            override fun onFailure(call: Call<ModelgetUserDetailsInfo>, t: Throwable) {
                Toast.makeText(this@OtpActivity, t.message, Toast.LENGTH_SHORT).show()

            }
        })
    }



    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}
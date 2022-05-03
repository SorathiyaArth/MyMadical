package com.test.mymadical

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.NestedScrollView
import com.test.mymadical.Interface.SignupUserInterface
import com.test.mymadical.model.ModelLoginInfo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.String
import java.util.zip.CRC32

class SignupActivity : AppCompatActivity() {
    lateinit var tvlogin: TextView
    lateinit var etname: TextView
    lateinit var etnumber: TextView
    lateinit var scrolldashboard: NestedScrollView
    lateinit var etrefer: TextView
    lateinit var btnsignup: Button
    lateinit var cbterms: CheckBox
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        tvlogin = findViewById(R.id.tvlogin)
        etname = findViewById(R.id.etname)
        etnumber = findViewById(R.id.etnumber)
        etrefer = findViewById(R.id.etrefer)
        btnsignup = findViewById(R.id.btnsignup)
        scrolldashboard = findViewById(R.id.scrolldashboard)
        cbterms = findViewById(R.id.cbterms)

        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()?.setDisplayShowHomeEnabled(true)

        tvlogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

/*
        etrefer.setOnClickListener {
            scrolldashboard.scrollTo(0, scrolldashboard.getBottom());
        }*/


        btnsignup.setOnClickListener {
            if (checkname() && chackmobileno() && chackterms()) {
                Log.e("crc32", "encbtnsignup")

                SignupApi()

            }
        }
    }

    private fun signup() {
        Log.e("crc32", "encsignup")

        val otpPIN = (Math.random() * 9000).toInt() + 1000
        val intent = Intent(this, OtpActivity::class.java)
        intent.putExtra("otp", otpPIN.toString())
        intent.putExtra("mobileno", etnumber.text.toString())
        startActivity(intent)

        val str = etnumber.text.toString()
        val crc = CRC32()
        crc.update(str.toByteArray())
        val enc = String.format("%08X", crc.getValue())


    }

    private fun SignupApi() {
        val progress = LayoutInflater.from(this)
            .inflate(R.layout.custom_progress_dialog, null)


        val builder = AlertDialog.Builder(this)
            .setView(progress)
        val AlertDialog = builder.create()
        AlertDialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        AlertDialog.setCancelable(false) // disable dismiss by tapping outside of the dialog

        AlertDialog.show()

        val creation: SignupUserInterface =
            Retroclient.getSingleTonClient()!!.create(SignupUserInterface::class.java)

        val call = creation.postuserdetailsdetails(etname.text.toString(), etnumber.text.toString())
        call.enqueue(object : Callback<ModelLoginInfo> {
            override fun onResponse(
                call: Call<ModelLoginInfo>,
                response: Response<ModelLoginInfo>
            ) {
                if (response.isSuccessful) {
                    AlertDialog.dismiss()

                    Log.e("jjjjj4", response.body()?.message.toString())
                    if (response.body()?.flag!!.equals("1")) {
                        signup()
                        Log.e("crc32", "enc")

                    } else {
                        Toast.makeText(
                            this@SignupActivity,
                            response.body()?.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

            }

            override fun onFailure(call: Call<ModelLoginInfo>, t: Throwable) {
                Log.e("ddddddd", t.message.toString())
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }


    fun checkname(): Boolean {


        var isstudentnamevalid = false
        if (etname.text.toString().trim().length <= 0) {
            etname.error = "Enter Name"
        } else {
            isstudentnamevalid = true
        }
        return isstudentnamevalid
    }

    fun chackmobileno(): Boolean {


        var isstudentmobileno = false
        if (etnumber.text.toString().trim().length <= 0) {
            etnumber.error = "Enter Mobile Nomber"
        } else if (etnumber.text.toString().trim().length == 10) {
            isstudentmobileno = true
        } else {
            etnumber.error = "Enter valid number"
        }
        return isstudentmobileno
    }

    fun chackterms(): Boolean {
        var isstudentmobileno = false

        if (cbterms.isChecked) {
            isstudentmobileno = true
        } else {
            Toast.makeText(this, "Please accept term and condition!", Toast.LENGTH_SHORT).show()
        }
        return isstudentmobileno
    }


}
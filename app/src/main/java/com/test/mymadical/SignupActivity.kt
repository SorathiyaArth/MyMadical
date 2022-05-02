package com.test.mymadical

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import java.lang.String
import java.util.zip.CRC32

class SignupActivity : AppCompatActivity() {
    lateinit var tvlogin:TextView
    lateinit var etname:TextView
    lateinit var etnumber:TextView
    lateinit var scrolldashboard:NestedScrollView
    lateinit var etrefer:TextView
    lateinit var btnsignup:Button
    lateinit var cbterms:CheckBox
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
            val intent = Intent(this , LoginActivity::class.java)
            startActivity(intent)
        }

/*
        etrefer.setOnClickListener {
            scrolldashboard.scrollTo(0, scrolldashboard.getBottom());
        }*/


        btnsignup.setOnClickListener {
            if (checkname()&&chackmobileno()&&chackterms()){
                signup()
            }
        }
    }

    private fun signup() {
        val otpPIN = (Math.random() * 9000).toInt()+1000
        val intent = Intent(this, OtpActivity::class.java)
        intent.putExtra("otp",otpPIN.toString())
        intent.putExtra("mobileno",etnumber.text.toString())
        startActivity(intent)

        val str = etnumber.text.toString()
        val crc = CRC32()
        crc.update(str.toByteArray())
        val enc = String.format("%08X", crc.getValue())
        Log.e("crc32" , enc)

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
    fun chackmobileno():Boolean{


        var isstudentmobileno  = false
        if (etnumber.text.toString().trim().length<=0){
            etnumber.error = "Enter Mobile Nomber"
        }
        else if (etnumber.text.toString().trim().length==10){
            isstudentmobileno = true
        }else{
            etnumber.error ="Enter valid number"
        }
        return isstudentmobileno
    }
    fun chackterms():Boolean{
        var isstudentmobileno  = false

        if (cbterms.isChecked){
            isstudentmobileno = true
        }else{
            Toast.makeText(this , "Please accept term and condition!", Toast.LENGTH_SHORT).show()
        }
        return isstudentmobileno
    }


}
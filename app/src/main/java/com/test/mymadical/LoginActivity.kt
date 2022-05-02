package com.test.mymadical

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.test.mymadical.Interface.GetUserInfoInterface
import com.test.mymadical.Interface.GetUserLoginInterface
import com.test.mymadical.model.ModelLoginInfo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.zip.CRC32

class LoginActivity : AppCompatActivity() {
    lateinit var tvsingup: TextView
    lateinit var etnumber: EditText
    lateinit var btnlogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        tvsingup = findViewById(R.id.tvsingup)
        etnumber = findViewById(R.id.etnumber)
        btnlogin = findViewById(R.id.btnlogin)


        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()?.setDisplayShowHomeEnabled(true)


        tvsingup.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }

        btnlogin.setOnClickListener {


            if (chackstudenmobileno()) {

                ApiCall()


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
        val mobileno = etnumber.text.toString()

        val creation: GetUserLoginInterface =
            Retroclient.getSingleTonClient()!!.create(GetUserLoginInterface::class.java)
        val call = creation.getlogindata(mobileno)
        call.enqueue(object : Callback<ModelLoginInfo> {
            override fun onResponse(
                call: Call<ModelLoginInfo>,
                response: Response<ModelLoginInfo>
            ) {
                if (response.isSuccessful) {
                    AlertDialog.dismiss()
                    if (response.body()?.flag == "1") {
                        val otpPIN = (Math.random() * 9000).toInt() + 1000

                        val intent = Intent(this@LoginActivity, OtpActivity::class.java)
                        intent.putExtra("otp", otpPIN.toString())
                        intent.putExtra("mobileno", etnumber.text.toString())
                        startActivity(intent)
                    } else {
                        Toast.makeText(
                            this@LoginActivity,
                            response.body()?.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            override fun onFailure(call: Call<ModelLoginInfo>, t: Throwable) {
                Toast.makeText(
                    this@LoginActivity,
                    t.message,
                    Toast.LENGTH_SHORT
                ).show()

            }
        })


    }

    fun chackstudenmobileno(): Boolean {


        var isstudentmobileno = false
        if (etnumber.text.toString().trim().length <= 0) {
            etnumber.error = "Enter Mobile Nomber"
        } else if (etnumber.text.toString().trim().length == 10) {
            isstudentmobileno = true
        } else {
            etnumber.setError("Enter valid number")
        }
        return isstudentmobileno
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
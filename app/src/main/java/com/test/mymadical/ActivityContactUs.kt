package com.test.mymadical

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class ActivityContactUs : AppCompatActivity() {
    lateinit var txtAddress:TextView
    lateinit var txtEmail:TextView
    lateinit var txtContact1:TextView
    lateinit var txtContact2:TextView
    lateinit var txtContact3:TextView
    lateinit var txtWhats:TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_us)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()?.setDisplayShowHomeEnabled(true)
        ids()
        txtContact1.setText( "+91  9574290218")
        txtContact2.setText("+91 8511018192")
        txtContact3.setText("+91 8690290284")
        txtEmail.setText("arthsorathiya02@gmail.com")
        txtAddress.setText("My Medical Ahemdabad")
        txtWhats.setText("+91 7600898872")
        txtWhats.setOnClickListener {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://api.whatsapp.com/send?phone=" + txtWhats.text)
                )
            )
        }




    }

    private fun ids() {
        txtAddress =findViewById(R.id.txtAddress)
        txtEmail =findViewById(R.id.txtEmail)
        txtContact1 =findViewById(R.id.txtContact1)
        txtContact2 =findViewById(R.id.txtContact2)
        txtContact3 =findViewById(R.id.txtContact3)
        txtWhats =findViewById(R.id.txtWhats)
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
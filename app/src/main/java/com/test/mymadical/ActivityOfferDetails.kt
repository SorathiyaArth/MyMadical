package com.test.mymadical

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.razorpay.Payment
import com.razorpay.RazorpayClient
import org.json.JSONObject
// rzp_test_BRTY8CDXbNVQS9
//B0CwCJucOUANbDwQlhxdPCiH

class ActivityOfferDetails : AppCompatActivity() {
    lateinit var imgOffer: ImageView
    lateinit var txtTitle: TextView
    lateinit var txtDesc: TextView
    lateinit var btnAction: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_offer_details)
        val name = intent.getStringExtra("name")
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        supportActionBar!!.setTitle("New Comer")
        supportActionBar!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))




        btnAction = findViewById(R.id.btnAction)
        txtDesc = findViewById(R.id.txtDesc)
        txtTitle = findViewById(R.id.txtTitle)
        imgOffer = findViewById(R.id.imgOffer)



        val tital = intent.getStringExtra("tital")
        val dis =intent.getStringExtra("dis")
        val image = intent.getStringExtra("image")

        txtTitle.text = tital
        txtDesc.text = dis
        Glide.with(this).load(image).into(imgOffer)
        Log.e("LGF", image + "")


        btnAction.setOnClickListener {
            val intent = Intent(this, Produductdetails::class.java)
            intent.putExtra("pro_id", "1")

            startActivity(intent)
        }
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
package com.test.mymadical

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.bumptech.glide.Glide

class ActivityOffers : AppCompatActivity() {
    lateinit var imgOffer:ImageView
    lateinit var txtTitle:TextView
    lateinit var txtDesc:TextView
    lateinit var txtDate:TextView
    lateinit var Cv_Offer:CardView
    var image = ""
    var texttitle = ""
    var textdis = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_offers)
        details()

        imgOffer = findViewById(R.id.imgOffer)
        txtTitle = findViewById(R.id.txtTitle)
        Cv_Offer = findViewById(R.id.Cv_Offer)
        txtDesc = findViewById(R.id.txtDesc)
        txtDate = findViewById(R.id.txtDate)
        Glide.with(this).load(image).into(imgOffer)
        txtTitle.setText(texttitle)
        txtDesc.setText(textdis)
        txtDate.setText("18-03-2021")
        Cv_Offer.setOnClickListener {
            val intent =  Intent(this , ActivityOfferDetails::class.java)
            intent.putExtra("tital", texttitle)
            intent.putExtra("dis", textdis)
            intent.putExtra("image", image)
            startActivity(intent)
        }




    }

    private fun details() {
        image ="https://bestappforu.in.net/videotiktak/testapi/uploads/push/1627899533.png"
        texttitle ="the New in the list"
            textdis = "ITS A PUZZLE..\n" +
                    "    FIND THE NEW ITEM IN THE APP AND YOU WILL GET IT FREE OF COST IN YOUR NEXT ORDER.\n" +
                    "    YOU HAVE TO ADD THAT ITEM IN YOUR ORDER AND WHATSAPP ME ON 7600898872 THE NAME OF ITEM.\n" +
                    "    ONLY ONE LATEST ADDED ITEM WILL CONSIDER FROM ALL CATEGORIES."
    }
}
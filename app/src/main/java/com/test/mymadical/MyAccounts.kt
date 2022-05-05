package com.test.mymadical

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView

class MyAccounts : AppCompatActivity() {
    val main_key = "my_pref"
    val custID_key = "custID_key"
    val uname_key = "uname"
    val unumber_key = "unumber"
    val uemail_key = "uemail"
    val uimage_key = "uimage"
    val ucart_key = "cartitemtotal"
    var shareduname = ""
    var sherdnumber = ""
    var custId = ""


    lateinit var imgMyUpdateimage: CircleImageView
    lateinit var tvMyuname: TextView
    lateinit var tvMyuphone: TextView

    lateinit var ReletiveSubMyAccount: RelativeLayout
    lateinit var ReletiveSubMyOrders: RelativeLayout

    lateinit var linearMyAccount:LinearLayout
    lateinit var linearMyOrders:LinearLayout

    lateinit var lenearSubMyOrders:LinearLayout
    lateinit var lenearSubAccountInfo:LinearLayout
    lateinit var lenearSubMyAddress:LinearLayout
    lateinit var lenearSubWallet:LinearLayout
    lateinit var imgMyAccountRightArrow: ImageView
    lateinit var imgMyOrdersRightArrow: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_accounts)
        ids()
        val preferences: SharedPreferences = this.getSharedPreferences(main_key, MODE_PRIVATE)

         shareduname = preferences.getString(uname_key, "Guest User").toString()
         shareduname = preferences.getString(uname_key, "Guest User").toString()
         sherdnumber = preferences.getString(unumber_key, "**********").toString()
        custId = preferences.getString(custID_key, "").toString()

        tvMyuname.text = shareduname
        tvMyuphone.text = sherdnumber
        Glide.with(this).load(R.drawable.img_default_user_icon).into(imgMyUpdateimage)
        linearMyAccount.setOnClickListener {
            if( ReletiveSubMyAccount.visibility == View.VISIBLE) {
                imgMyAccountRightArrow.setImageResource(R.drawable.ac_right)
                ReletiveSubMyAccount.visibility = View.GONE
            }else{
                imgMyAccountRightArrow.setImageResource(R.drawable.ac_down)
                ReletiveSubMyAccount.visibility = View.VISIBLE
            }
        }
        linearMyOrders.setOnClickListener {
            if( ReletiveSubMyOrders.visibility == View.VISIBLE) {
                imgMyOrdersRightArrow.setImageResource(R.drawable.ac_right)
                ReletiveSubMyOrders.visibility = View.GONE
            }else{
                imgMyOrdersRightArrow.setImageResource(R.drawable.ac_down)
                ReletiveSubMyOrders.visibility = View.VISIBLE
            }
        }
        lenearSubMyOrders.setOnClickListener {
            val intent = Intent(this, Activity_order::class.java)
            startActivity(intent)
        }
        lenearSubMyAddress.setOnClickListener {
            val intent = Intent(this, ActivityMyAddress::class.java)
            intent.putExtra("fromAccount","fromAccount")
            startActivity(intent)
        }
        lenearSubAccountInfo.setOnClickListener {
            val intent = Intent(this, ActivityUserProfileEdit::class.java)

            startActivity(intent)
        }

    }

    private fun ids() {
        imgMyUpdateimage = findViewById(R.id.imgMyUpdateimage)
        tvMyuname = findViewById(R.id.tvMyuname)
        tvMyuphone = findViewById(R.id.tvMyuphone)

        ReletiveSubMyAccount = findViewById(R.id.ReletiveSubMyAccount)
        ReletiveSubMyOrders = findViewById(R.id.ReletiveSubMyOrders)

        linearMyAccount = findViewById(R.id.linearMyAccount)
        linearMyOrders = findViewById(R.id.linearMyOrders)

        lenearSubMyOrders = findViewById(R.id.lenearSubMyOrders)
        lenearSubAccountInfo = findViewById(R.id.lenearSubAccountInfo)
        lenearSubMyAddress = findViewById(R.id.lenearSubMyAddress)
        lenearSubWallet = findViewById(R.id.lenearSubWallet)
        imgMyAccountRightArrow = findViewById(R.id.imgMyAccountRightArrow)
        imgMyOrdersRightArrow = findViewById(R.id.imgMyOrdersRightArrow)

        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()?.setDisplayShowHomeEnabled(true)

    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
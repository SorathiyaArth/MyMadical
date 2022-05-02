package com.test.mymadical

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.core.text.HtmlCompat

class ActivityAboutUs : AppCompatActivity() {
    var text :String? = null
    lateinit var txtabout:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_us)
        txtabout = findViewById(R.id.txtabout)
        text= "<p>My Medical is an E-Commerce platform operate by <strong>GREEN FIELD</strong>. </p><p>Very dynamic platform for your day to day requirements of medicines.</p><p>We choose and pick best quality medicines direct from farm and make available for you at your door step. We help you to buy in the most comfortable environment.</p><p>We accept orders 24x7, and happy to deliver your valuable goods very next day.</p><p>We are also concern for your safety and hence, we strictly follow all safety measures starting from procurement to delivery destination. </p><p>You can choose our various payment option and enjoy your shopping."

        txtabout.setText(text?.let { HtmlCompat.fromHtml(it, 0) })
    }
}
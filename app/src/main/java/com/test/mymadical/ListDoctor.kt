package com.test.mymadical

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.widget.Toolbar

class ListDoctor : AppCompatActivity() {
    var toolbar: Toolbar? = null
    var city_name: String = ""
    var doc_type: String = ""
    var doc_img:Int = 0
    lateinit var tvtitle: TextView
    lateinit var Txt_nodata: TextView
    lateinit var Img_doctype: ImageView
    lateinit var imgsearch: ImageView
    lateinit var tvcart: RelativeLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listdoctor)

        tvtitle = findViewById(R.id.tvtitle)
        imgsearch = findViewById(R.id.imgsearch)
        tvcart = findViewById(R.id.tvcart)
        toolbar = findViewById(R.id.toolbar)
        Img_doctype = findViewById(R.id.Img_doctype)
        Txt_nodata = findViewById(R.id.Txt_nodata)


        setSupportActionBar(toolbar)

        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()?.setDisplayShowHomeEnabled(true)

        imgsearch.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }

        tvcart.setOnClickListener {
            val intent = Intent(this, ActivityMyCart::class.java)
            startActivity(intent)
        }


        city_name = intent.getStringExtra("city_name").toString()
        doc_type = intent.getStringExtra("doc_type").toString()
        doc_img = intent.getIntExtra("doc_img",0)
        tvtitle.setText(doc_type)
        Txt_nodata.text = "Ooops! No " + doc_type + " in " + city_name
        Img_doctype.setImageResource(doc_img)




    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
package com.test.mymadical

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager

class Spleshscreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        supportActionBar?.hide()
        setContentView(R.layout.activity_spleshsceen)
        var thread2 = object : Thread() {
            override fun run() {
                try {
                    sleep(3000)
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    val intent = Intent(this@Spleshscreen, Dashboard::class.java)
                    startActivity(intent)
                }

            }
        }
        thread2.start()
    }
    override fun onPause() {
        super.onPause()
        finish()
    }
}
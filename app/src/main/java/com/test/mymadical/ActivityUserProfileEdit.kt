package com.test.mymadical

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log.e
import android.widget.EditText
import android.widget.ImageView
import com.test.mymadical.Interface.UserprofileeditInterface
import com.test.mymadical.model.ModelLoginInfo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ActivityUserProfileEdit : AppCompatActivity() {
    lateinit var imgSend: ImageView
    lateinit var imgclose: ImageView
    lateinit var edtFirstName: EditText
    lateinit var edtEmail: EditText
    val main_key = "my_pref"
    val custID_key = "custID_key"

    val uname_key = "uname"
    val unumber_key = "unumber"
    val uemail_key = "uemail"
    val uimage_key = "uimage"
    var sherdiscustID = ""
    var shareduname = ""
    var sherdnumber = ""
    var sherdemail = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile_edit)
        ids()
        val preferences: SharedPreferences = this.getSharedPreferences(main_key, MODE_PRIVATE)

         sherdiscustID = preferences.getString(custID_key, "afd").toString()
         shareduname = preferences.getString(uname_key, "Guest User").toString()
         sherdnumber = preferences.getString(unumber_key, "**********").toString()
         sherdemail = preferences.getString(uemail_key, "").toString()
edtFirstName.setText(shareduname)
edtEmail.setText(sherdemail)
        imgclose.setOnClickListener {
            val intent = Intent(this, MyAccounts::class.java)
            startActivity(intent)
            finish()
        }
        imgSend.setOnClickListener {
            if (checkname()) apicall()
            else e("ddd","dddd")

        }
    }

    private fun apicall() {

        val creation =
            Retroclient.getSingleTonClient()?.create(UserprofileeditInterface::class.java)
        val call = creation?.Uapateuserdetailsdetails(
            edtFirstName.text.toString(),
            edtEmail.text.toString(),
            sherdiscustID,
            sherdnumber
        )
        call?.enqueue(object : Callback<ModelLoginInfo> {
            override fun onResponse(
                call: Call<ModelLoginInfo>,
                response: Response<ModelLoginInfo>
            ) {
                if (response.isSuccessful) {
                    com.test.mymadical.Utils.Utils()
                        .Logindata(this@ActivityUserProfileEdit, "uemail",  edtEmail.text.toString())
                    com.test.mymadical.Utils.Utils()
                        .Logindata(this@ActivityUserProfileEdit, "uname", edtFirstName.text.toString())
                    val intent = Intent(this@ActivityUserProfileEdit, MyAccounts::class.java)
                    startActivity(intent)
                    finish()
                }
            }

            override fun onFailure(call: Call<ModelLoginInfo>, t: Throwable) {
                t.message?.let { e("kfjdjf", it) }
            }
        })
    }

    private fun ids() {
        edtEmail = findViewById(R.id.edtEmail)
        edtFirstName = findViewById(R.id.edtFirstName)
        imgclose = findViewById(R.id.imgclose)
        imgSend = findViewById(R.id.imgSend)
    }
    fun checkname(): Boolean {


        var isstudentnamevalid = false
        if (edtFirstName.text.toString().trim().length <= 0) {
            edtFirstName.error = "Enter Name"
        } else {
            isstudentnamevalid = true
        }
        return isstudentnamevalid
    }
}
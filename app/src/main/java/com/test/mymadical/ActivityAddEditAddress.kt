package com.test.mymadical

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.test.mymadical.Interface.PostAddressInfoInterface
import com.test.mymadical.Utils.Utils
import com.test.mymadical.model.ModelAddEditAddressInfo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ActivityAddEditAddress : AppCompatActivity() {
    lateinit var etname: TextView
    lateinit var etnumber: TextView
    lateinit var etaddress: TextView
    lateinit var etArea: TextView
    lateinit var etPincode: TextView
    lateinit var etcity: TextView
    lateinit var etstate: TextView
    lateinit var txtAddAddress: TextView
    var addressid: String = ""
    val main_key = "my_pref"
    val custID_key = "custID_key"
    var custid: String = ""
    var types: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_address)
        Ids()
        addressid = intent.getStringExtra("addid").toString()
        val type = intent.getStringExtra("type").toString()
        Log.e("nvhgcfdxs", type)
        if (type.equals("update")) {

            etname.text = intent.getStringExtra("name").toString()
            etnumber.text = intent.getStringExtra("content").toString()
            etaddress.text = intent.getStringExtra("address").toString()
            etArea.text = intent.getStringExtra("area").toString()
            etPincode.text = intent.getStringExtra("pincode").toString()
            etcity.text = intent.getStringExtra("city").toString()
            etstate.text = intent.getStringExtra("state").toString()
            txtAddAddress.text = "Update Address"
            types = "update"

        } else {
            etname.text = ""
            etnumber.text = ""
            etaddress.text = ""
            etArea.text = ""
            etPincode.text = ""
            etcity.text = ""
            etstate.text = ""
            types = "add"
            txtAddAddress.text = "Add Address"

        }

        txtAddAddress.setOnClickListener {
            if (Utils().isNetworkAvailable(this)) {
                if (checkname() && chackmobileno() && checkaddress() && checkArea() &&
                    checkPincode() && checkcity() && checkstate()
                ) {

                    senddata()

                }

            } else {
                Utils().showToastShortForNoInternet(this)

            }


        }


    }

    private fun senddata() {
        val progress = LayoutInflater.from(this)
            .inflate(R.layout.custom_progress_dialog, null)


        val builder = AlertDialog.Builder(this)
            .setView(progress)
        val AlertDialog = builder.create()
        AlertDialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        AlertDialog.setCancelable(false) // disable dismiss by tapping outside of the dialog

        AlertDialog.show()
        val preferences: SharedPreferences = this.getSharedPreferences(main_key, MODE_PRIVATE)
        custid = preferences.getString(custID_key, "").toString()

        val name = etname.text.toString()
        val number = etnumber.text.toString()
        val address = etaddress.text.toString()
        val area = etArea.text.toString()
        val pincode = etPincode.text.toString()
        val city = etcity.text.toString()
        val state = etstate.text.toString()
        val creation: PostAddressInfoInterface =
            Retroclient.getSingleTonClient()!!.create(PostAddressInfoInterface::class.java)
        val call = creation.postaddressdetaildata(
            types,
            addressid,
            custid,
            name,
            number,
            address,
            area,
            pincode,
            city,
            state
        )
        call.enqueue(object : Callback<ModelAddEditAddressInfo> {
            override fun onResponse(
                call: Call<ModelAddEditAddressInfo>,
                response: Response<ModelAddEditAddressInfo>
            ) {
                if (response.isSuccessful) {

                    AlertDialog.dismiss()
                    if (response.body()?.flag == "1") {
                        val fromAccount = intent.getStringExtra("fromAccount")

                        val intent =
                            Intent(this@ActivityAddEditAddress, ActivityMyAddress::class.java)
                        if (fromAccount != null) {
                            intent.putExtra("fromAccount", fromAccount)

                        }
                        intent.putExtra("CustId", custid)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(
                            this@ActivityAddEditAddress, "Something went to Worng" +
                                    " please try again later", Toast.LENGTH_SHORT
                        ).show()
                    }
                    Log.e("onfailadd", response.message().toString())


                }

            }

            override fun onFailure(call: Call<ModelAddEditAddressInfo>, t: Throwable) {
                Log.e("onfailadd", t.message.toString())
            }

        })


    }

    private fun Ids() {
        etname = findViewById(R.id.etname)
        etnumber = findViewById(R.id.etnumber)
        etaddress = findViewById(R.id.etaddress)
        etArea = findViewById(R.id.etArea)
        etPincode = findViewById(R.id.etPincode)
        etcity = findViewById(R.id.etcity)
        etstate = findViewById(R.id.etstate)
        txtAddAddress = findViewById(R.id.txtAddAddress)
    }


    fun checkname(): Boolean {
        val st_name = findViewById<EditText>(R.id.etname)

        var isstudentnamevalid = false
        if (st_name.text.toString().trim().length <= 0) {
            st_name.error = "Enter Name"
        } else {
            isstudentnamevalid = true
        }
        return isstudentnamevalid
    }

    fun chackmobileno(): Boolean {
        val st_mobileno = findViewById<EditText>(R.id.etnumber)

        var isstudentmobileno = false
        if (st_mobileno.text.toString().trim().length <= 0) {
            st_mobileno.error = "Enter Mobile Nomber"
        } else if (st_mobileno.text.toString().trim().length == 10) {
            isstudentmobileno = true
        } else {
            st_mobileno.error = "Enter valid number"
        }
        return isstudentmobileno
    }

    fun checkaddress(): Boolean {
        val st_name = findViewById<EditText>(R.id.etaddress)

        var isstudentnamevalid = false
        if (st_name.text.toString().trim().length <= 0) {
            st_name.error = "Enter Name"
        } else {
            isstudentnamevalid = true
        }
        return isstudentnamevalid
    }

    fun checkArea(): Boolean {
        val st_name = findViewById<EditText>(R.id.etArea)

        var isstudentnamevalid = false
        if (st_name.text.toString().trim().length <= 0) {
            st_name.error = "Enter Area"
        } else {
            isstudentnamevalid = true
        }
        return isstudentnamevalid
    }

    fun checkPincode(): Boolean {
        val st_name = findViewById<EditText>(R.id.etPincode)

        var isstudentnamevalid = false
        if (st_name.text.toString().trim().length <= 0) {
            st_name.error = "Enter Pincode"
        } else {
            isstudentnamevalid = true
        }
        return isstudentnamevalid
    }

    fun checkcity(): Boolean {
        val st_name = findViewById<EditText>(R.id.etcity)

        var isstudentnamevalid = false
        if (st_name.text.toString().trim().length <= 0) {
            st_name.error = "Enter city"
        } else {
            isstudentnamevalid = true
        }
        return isstudentnamevalid
    }

    fun checkstate(): Boolean {
        val st_name = findViewById<EditText>(R.id.etstate)

        var isstudentnamevalid = false
        if (st_name.text.toString().trim().length <= 0) {
            st_name.error = "Enter state"
        } else {
            isstudentnamevalid = true
        }
        return isstudentnamevalid
    }
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
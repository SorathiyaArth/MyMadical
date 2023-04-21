package com.test.mymadical

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.test.mymadical.Interface.Adepter.AdepterMyOrder
import com.test.mymadical.Interface.ClickInterface
import com.test.mymadical.Interface.OrderDisplayInterface
import com.test.mymadical.Utils.Utils
import com.test.mymadical.model.ModelMyOrder
import com.test.mymadical.model.OrderTblItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Activity_order : AppCompatActivity() {
    lateinit var lyt_not_found: LinearLayout
    lateinit var RvMyOrder: RecyclerView
    var adepterOrder: AdepterMyOrder? = null

    val main_key = "my_pref"
    val custID_key = "custID_key"
    var sherdiscustID: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)
        lyt_not_found = findViewById(R.id.lyt_not_found)
        RvMyOrder = findViewById(R.id.RvMyOrder)
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        RvMyOrder.setLayoutManager(linearLayoutManager)
        // RvMyOrder.layoutManager = GridLayoutManager(this, 1)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()?.setDisplayShowHomeEnabled(true)
        val preferences: SharedPreferences = this.getSharedPreferences(main_key, MODE_PRIVATE)

        sherdiscustID = preferences.getString(custID_key, "afd")
        if (Utils().isNetworkAvailable(this@Activity_order)) {
            getoederlist()
        } else {
            Utils().showToastShortForNoInternet(this@Activity_order)
        }


    }


    private fun getoederlist() {
        val progress = LayoutInflater.from(this)
            .inflate(R.layout.custom_progress_dialog, null)


        val builder = AlertDialog.Builder(this)
            .setView(progress)
        val AlertDialog = builder.create()
        AlertDialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        AlertDialog.setCancelable(false) // disable dismiss by tapping outside of the dialog

        AlertDialog.show()
        val creation: OrderDisplayInterface =
            Retroclient.getSingleTonClient()!!.create(OrderDisplayInterface::class.java)

        val call = creation.getorderdata(sherdiscustID!!.toInt())
        call.enqueue(object : Callback<ModelMyOrder> {
            override fun onResponse(call: Call<ModelMyOrder>, response: Response<ModelMyOrder>) {
                if (response.isSuccessful) {
                    AlertDialog.dismiss()
                    if (response.body()!!.flag!!.equals(1)) {
                        val listorder = response.body()!!.orderTbl

                        adepterOrder = AdepterMyOrder(listorder as List<OrderTblItem>, baseContext)
                        RvMyOrder.adapter = adepterOrder

                        adepterOrder!!.setOnclickInterface(object : ClickInterface {
                            override fun onclicked12(position: Int, Types: Int) {
                                if (Types == 1) {
                                    val intent =
                                        Intent(
                                            this@Activity_order,
                                            Activity_order_details::class.java
                                        )
                                    intent.putExtra("orderid", listorder.get(position).orderId)
                                    startActivity(intent)
                                }
                            }
                        })

                    } else {
                        lyt_not_found.visibility = View.VISIBLE
                    }


                }
            }

            override fun onFailure(call: Call<ModelMyOrder>, t: Throwable) {
                Log.d("DSFSF", t.message + " hii")
            }
        })

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (intent.getStringExtra("from").equals("order")) {
            val intent = Intent(this@Activity_order, Dashboard::class.java)
            startActivity(intent)
            finishAffinity()
        } else {
            finish()

        }
    }
}
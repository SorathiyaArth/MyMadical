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
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidadvance.topsnackbar.TSnackbar
import com.test.mymadical.Adepter.AdepterMyCart
import com.test.mymadical.Interface.AddToCartInterface
import com.test.mymadical.Interface.ClickInterface
import com.test.mymadical.Interface.GetCartInfoInterface
import com.test.mymadical.Utils.Utils
import com.test.mymadical.model.CartItem
import com.test.mymadical.model.CartTblItem
import com.test.mymadical.model.ModelCartDisplayInfo
import com.test.mymadical.model.ModelCartaddInfo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ActivityMyCart : AppCompatActivity() {
    val main_key = "my_pref"
    val custID_key = "custID_key"
    val ucart_key = "ucart"
    var sherdiscustid = ""
    var total = ""

    var toolbar: Toolbar? = null
    lateinit var imgsearch: ImageView

    lateinit var rvcart: RecyclerView
    lateinit var rlemptycart: RelativeLayout
    lateinit var rlmycart: RelativeLayout
    lateinit var tvcart: RelativeLayout
    lateinit var Rv_mycart: RelativeLayout
    lateinit var tvhome: TextView
    lateinit var tvtotal: TextView
    lateinit var tvcontinue: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_cart)


        Ids()



        setSupportActionBar(toolbar)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()?.setDisplayShowHomeEnabled(true)


        tvcart.isVisible = false
        imgsearch.isVisible = false

        rvcart.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val preferences: SharedPreferences = this.getSharedPreferences(main_key, MODE_PRIVATE)
        sherdiscustid = preferences.getString(custID_key, "").toString()




        if (Utils().isNetworkAvailable(this)) {
            GetData()
        } else {
            Utils().showToastShortForNoInternet(this)
            val snackbar: TSnackbar = TSnackbar
                .make(
                    rvcart,
                    "Please Check your Internet Connection",
                    TSnackbar.LENGTH_INDEFINITE
                )
                .setAction("OK", object : View.OnClickListener {
                    override fun onClick(v: View?) {
                        finish();
                        startActivity(getIntent())
                    }

                })
            snackbar.setActionTextColor(Color.BLACK)
            val snackbarView: View = snackbar.view
            snackbarView.setBackgroundColor(Color.parseColor("#FFFFFFFF"))
            val textView =
                snackbarView.findViewById(R.id.snackbar_text) as TextView
            textView.setTextColor(Color.BLACK)
            snackbar.show()
        }


        tvhome.setOnClickListener {
            val intent = Intent(this, Dashboard::class.java)
            startActivity(intent)
        }
        tvcontinue.setOnClickListener {
            val intent = Intent(this, ActivityMyAddress::class.java)
            intent.putExtra("CustId", sherdiscustid)
            intent.putExtra("total", total)
            startActivity(intent)
        }


    }

    private fun Ids() {
        toolbar = findViewById(R.id.toolbar)
        rvcart = findViewById(R.id.rvcart)
        tvcontinue = findViewById(R.id.tvcontinue)
        tvcart = findViewById(R.id.tvcart)
        Rv_mycart = findViewById(R.id.Rv_mycart)
        imgsearch = findViewById(R.id.imgsearch)

        rlmycart = findViewById(R.id.rlmycart)
        rlemptycart = findViewById(R.id.rlemptycart)
        tvhome = findViewById(R.id.tvhome)
        tvtotal = findViewById(R.id.tvtotal)
    }

    private fun GetData() {


        val progress = LayoutInflater.from(this)
            .inflate(R.layout.custom_progress_dialog, null)


        val builder = AlertDialog.Builder(this)
            .setView(progress)
        val AlertDialog = builder.create()
        AlertDialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        AlertDialog.setCancelable(false) // disable dismiss by tapping outside of the dialog

        AlertDialog.show()
        val creation: GetCartInfoInterface =
            Retroclient.getSingleTonClient()!!.create(GetCartInfoInterface::class.java)
        val call = creation.getcartdetaildata(sherdiscustid.toInt())
        call.enqueue(object : Callback<ModelCartDisplayInfo> {
            override fun onResponse(
                call: Call<ModelCartDisplayInfo>,
                response: Response<ModelCartDisplayInfo>
            ) {
                if (response.isSuccessful) {
                    Rv_mycart.isVisible = true
                    AlertDialog.dismiss()
                     total = response.body()!!.cartItem?.carttotal.toString()
                    val item = response.body()!!.cartItem?.cartitem
                    tvtotal.text = "Total : ₹ " + total
                    val listcart = response.body()?.cartTbl
                    if (listcart != null) {

                        val mAdepterMyCart: AdepterMyCart
                        mAdepterMyCart =
                            AdepterMyCart(listcart as ArrayList<CartTblItem>, baseContext)
                        rvcart.adapter = mAdepterMyCart
                        mAdepterMyCart.setOnItemClickListener(object : ClickInterface {
                            override fun onclicked12(position: Int, Types: Int) {
                                if (Types == 1) {
                                    CallApi(
                                        "delete",
                                        listcart[position].productId!!)
                                }
                                if (Types == 2) {
                                    CallApi(
                                        "plus",
                                        listcart[position].productId!!)
                                }
                                if (Types == 3) {
                                    CallApi("minus", listcart[position].productId!!)
                                }
                            }
                        })
                    } else {
                        rlemptycart.isVisible = true
                        rlmycart.isVisible = false
                    }


                }
            }

            override fun onFailure(call: Call<ModelCartDisplayInfo>, t: Throwable) {
                Log.e("onfailcart", t.message.toString())
            }
        })



    }

    private fun CallApi(typ: String, id: String) {

        val progress = LayoutInflater.from(this)
            .inflate(R.layout.custom_progress_dialog, null)


        val builder = AlertDialog.Builder(this)
            .setView(progress)
        val AlertDialog = builder.create()
        AlertDialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        AlertDialog.setCancelable(false) // disable dismiss by tapping outside of the dialog

        AlertDialog.show()

        if (Utils().isNetworkAvailable(this)) {
            val creation: AddToCartInterface =
                Retroclient.getSingleTonClient()!!.create(
                    AddToCartInterface::class.java
                )
            val call = creation.insertdata(sherdiscustid, id, typ)
            call?.enqueue(object : Callback<ModelCartaddInfo> {
                override fun onResponse(
                    call: Call<ModelCartaddInfo>,
                    response: Response<ModelCartaddInfo>
                ) {
                    if (response.isSuccessful) {
                        AlertDialog.dismiss()
                        finish()
                        startActivity(getIntent())
                       Utils()
                            .Logindata(this@ActivityMyCart, "cartitemtotal", response.body()?.count.toString())
                        Toast.makeText(
                            this@ActivityMyCart,
                            response.body()?.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(
                    call: Call<ModelCartaddInfo>,
                    t: Throwable
                ) {
                    Log.e("onfailadd", t.message + "   l ")
                    Toast.makeText(
                        this@ActivityMyCart,
                        t.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })


        } else {
            Utils().showToastShortForNoInternet(this@ActivityMyCart)
            val snackbar: TSnackbar = TSnackbar
                .make(
                    rvcart,
                    "Please Check your Internet Connection",
                    TSnackbar.LENGTH_INDEFINITE
                )
                .setAction("OK", object : View.OnClickListener {
                    override fun onClick(v: View?) {
                        finish();
                        startActivity(getIntent())
                    }

                })
            snackbar.setActionTextColor(Color.BLACK)
            val snackbarView: View = snackbar.view
            snackbarView.setBackgroundColor(Color.parseColor("#FFFFFFFF"))
            val textView =
                snackbarView.findViewById(R.id.snackbar_text) as TextView
            textView.setTextColor(Color.BLACK)
            snackbar.show()
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}



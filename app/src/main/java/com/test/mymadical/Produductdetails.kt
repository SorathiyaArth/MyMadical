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
import androidx.appcompat.widget.Toolbar

import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import com.androidadvance.topsnackbar.TSnackbar
import com.bumptech.glide.Glide
import com.test.mymadical.Interface.AddToCartInterface
import com.test.mymadical.Interface.ProductdetailsInterface
import com.test.mymadical.Utils.Utils
import com.test.mymadical.model.ModelCartaddInfo
import com.test.mymadical.model.ProductInfo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Produductdetails : AppCompatActivity() {
    var toolbar: Toolbar? = null
    lateinit var tvtitle: TextView
    lateinit var tvcart: RelativeLayout
    lateinit var linearlayoutOutOfStock: LinearLayout
    lateinit var linearlayoutAddToCart: LinearLayout
    lateinit var Rv_details: RelativeLayout
    lateinit var imgsearch: ImageView
    val main_key = "my_pref"
    val custID_key = "custID_key"
    val ucart_key = "cartitemtotal"
    var sherdiscustID: String? = null
    var cartitemcount: String? = null
    lateinit var tvmytotalitems: TextView
    var pro_id: String? = null
    lateinit var ivlargeimage: ImageView
    lateinit var Img_flash: ImageView
    lateinit var ivsmallimage: ImageView
    lateinit var tvproudctname: TextView
    lateinit var tvproductmrp: TextView
    lateinit var tvproductdiscount: TextView
    lateinit var tvproductprice: TextView
    lateinit var Txt_inoutstock: TextView
    lateinit var tvdiscription: TextView
    lateinit var addtocart: TextView
    lateinit var buynow: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_produductdetails)
        toolbar = findViewById(R.id.toolbar)
        Ids()

        setSupportActionBar(toolbar)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()?.setDisplayShowHomeEnabled(true)

        val preferences: SharedPreferences = this.getSharedPreferences(main_key, MODE_PRIVATE)
        sherdiscustID = preferences.getString(custID_key, "")
        cartitemcount = preferences.getString(ucart_key, "")
        tvmytotalitems.text =cartitemcount
        pro_id = intent.getStringExtra("pro_id").toString()
        Log.e("khgf", pro_id + "       jkhgfhgxd");

        buynow.setOnClickListener {
            callApi()
            val intent = Intent(this, ActivityMyCart::class.java)
            startActivity(intent)

        }
        addtocart.setOnClickListener {
            callApi()
        }


        Img_flash.setOnClickListener {
            Utils().showToastShortForDrone(this)
        }
        tvcart.setOnClickListener {
            if (Utils().islogin(this)) {
                val intent = Intent(this, ActivityMyCart::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
        }
        imgsearch.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }

        if (Utils().isNetworkAvailable(this)) {
            getproductdetails()
        } else {
            Utils().showToastShortForNoInternet(this)
            val snackbar: TSnackbar = TSnackbar
                .make(
                    ivlargeimage,
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

    private fun callApi() {
        if (Utils().isNetworkAvailable(this@Produductdetails)) {
            val progress = LayoutInflater.from(this)
                .inflate(R.layout.custom_progress_dialog, null)


            val builder = AlertDialog.Builder(this)
                .setView(progress)
            val AlertDialog = builder.create()
            AlertDialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            AlertDialog.setCancelable(false) // disable dismiss by tapping outside of the dialog

            AlertDialog.show()

            val creation: AddToCartInterface =
                Retroclient.getSingleTonClient()!!.create(
                    AddToCartInterface::class.java)
            val call = creation.insertdata(sherdiscustID,pro_id,"plus")
            call?.enqueue(object :Callback<ModelCartaddInfo>{
                override fun onResponse(
                    call: Call<ModelCartaddInfo>,
                    response: Response<ModelCartaddInfo>
                ) {
                    AlertDialog.dismiss()
                    tvmytotalitems.text = response.body()?.count.toString()
                    Utils()
                        .Logindata(this@Produductdetails, "cartitemtotal", response.body()?.count.toString())
                    Toast.makeText(this@Produductdetails, response.body()?.message, Toast.LENGTH_SHORT).show()
                }

                override fun onFailure(
                    call: Call<ModelCartaddInfo>,
                    t: Throwable
                ) {
                    Log.e("onfailadd",t.message + "   l ")
                    Toast.makeText(
                        this@Produductdetails,
                        t.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })


        } else {
            Utils().showToastShortForNoInternet(this@Produductdetails)
            val snackbar: TSnackbar = TSnackbar
                .make(
                    ivlargeimage,
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


    private fun getproductdetails() {
        val progress = LayoutInflater.from(this)
            .inflate(R.layout.custom_progress_dialog, null)


        val builder = AlertDialog.Builder(this)
            .setView(progress)
        val AlertDialog = builder.create()
        AlertDialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        AlertDialog.setCancelable(false) // disable dismiss by tapping outside of the dialog

        AlertDialog.show()


        val creation: ProductdetailsInterface =
            Retroclient.getSingleTonClient()!!.create(ProductdetailsInterface::class.java)
        val call = creation.getprodetaildata(pro_id!!.toInt())
        call.enqueue(object : Callback<ProductInfo> {
            override fun onResponse(call: Call<ProductInfo>, response: Response<ProductInfo>) {
                if (response.isSuccessful) {
                    AlertDialog.dismiss()
                    Rv_details.visibility = View.VISIBLE
                    val listproduct = response.body()?.productTbl
                    if (listproduct != null) {
                        tvproudctname.text = listproduct.get(0)!!.productName
                        tvproductprice.text = "₹" + listproduct.get(0)!!.productPrice
                        tvproductmrp.text = "₹" + listproduct.get(0)!!.productMrp
                        tvproductdiscount.text = listproduct.get(0)!!.productOff
                        tvdiscription.text = listproduct.get(0)!!.productDis
                        Glide.with(this@Produductdetails).load(listproduct[0]?.productImage)
                            .into(ivlargeimage)
                        Glide.with(this@Produductdetails).load(listproduct[0]?.productImage)
                            .into(ivsmallimage)
                        if (listproduct[0]?.productInstock == "No") {
                            linearlayoutOutOfStock.isVisible = true
                            linearlayoutAddToCart.isVisible = false

                        }

                    }
                }
            }

            override fun onFailure(call: Call<ProductInfo>, t: Throwable) {
                Log.e("profail", t.message.toString())
            }
        })
    }
    private fun Ids() {
        tvtitle = findViewById(R.id.tvtitle)
        imgsearch = findViewById(R.id.imgsearch)
        tvcart = findViewById(R.id.tvcart)
        tvmytotalitems = findViewById(R.id.tvmytotalitems)
        Img_flash = findViewById(R.id.Img_flash)
        linearlayoutOutOfStock = findViewById(R.id.linearlayoutOutOfStock)
        linearlayoutAddToCart = findViewById(R.id.linearlayoutAddToCart)
        ivlargeimage = findViewById(R.id.ivlargeimage)
        Rv_details = findViewById(R.id.Rv_details)
        ivsmallimage = findViewById(R.id.ivsmallimage)
        tvproudctname = findViewById(R.id.tvproudctname)
        tvproductmrp = findViewById(R.id.tvproductmrp)
        tvproductprice = findViewById(R.id.tvproductprice)
        Txt_inoutstock = findViewById(R.id.Txt_inoutstock)
        tvdiscription = findViewById(R.id.tvdiscription)
        tvproductdiscount = findViewById(R.id.tvproductdiscount)
        addtocart = findViewById(R.id.addtocart)
        buynow = findViewById(R.id.buynow)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
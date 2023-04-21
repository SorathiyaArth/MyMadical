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
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidadvance.topsnackbar.TSnackbar
import com.test.mymadical.Interface.Adepter.AdepterProduct
import com.test.mymadical.Interface.AddToCartInterface
import com.test.mymadical.Interface.ClickInterface
import com.test.mymadical.Interface.ProductInterface
import com.test.mymadical.Utils.Utils
import com.test.mymadical.model.ModelCartaddInfo
import com.test.mymadical.model.ProductInfo
import com.test.mymadical.model.ProductTblItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductActivity : AppCompatActivity() {
    var toolbar: Toolbar? = null
    lateinit var tvtitle: TextView
    lateinit var tvmytotalitems: TextView
    lateinit var tvcart: RelativeLayout
    lateinit var imgsearch: ImageView
    val main_key = "my_pref"
    val custID_key = "custID_key"
    val ucart_key = "cartitemtotal"


    lateinit var RV_product: RecyclerView
    lateinit var Ll_nodata: LinearLayout
    var adepterproduct: AdepterProduct? = null
    var subcat_id: String? = null
    var sherdiscustID: String? = null
    var subcat_name: String? = null
    var cartitemcount: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)

        toolbar = findViewById(R.id.toolbar)


        setSupportActionBar(toolbar)

        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()?.setDisplayShowHomeEnabled(true)

        tvtitle = findViewById(R.id.tvtitle)
        imgsearch = findViewById(R.id.imgsearch)
        tvmytotalitems = findViewById(R.id.tvmytotalitems)
        tvcart = findViewById(R.id.tvcart)
        subcat_id = intent.getStringExtra("subcat_id").toString()
        subcat_name = intent.getStringExtra("subcat_name").toString()
        Ll_nodata = findViewById(R.id.Ll_nodata)

        tvtitle.setText(subcat_name)


        val preferences: SharedPreferences = this.getSharedPreferences(main_key, MODE_PRIVATE)
        sherdiscustID = preferences.getString(custID_key, "")
        cartitemcount = preferences.getString(ucart_key, "")
        tvmytotalitems.text = cartitemcount
        RV_product = findViewById(R.id.RV_product)
        RV_product.layoutManager = GridLayoutManager(this, 2)

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
            getproduct()
        } else {
            Utils().showToastShortForNoInternet(this)
            val snackbar: TSnackbar = TSnackbar
                .make(
                    RV_product,
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

    private fun getproduct() {

        val progress = LayoutInflater.from(this)
            .inflate(R.layout.custom_progress_dialog, null)


        val builder = AlertDialog.Builder(this)
            .setView(progress)
        val AlertDialog = builder.create()
        AlertDialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        AlertDialog.setCancelable(false) // disable dismiss by tapping outside of the dialog

        AlertDialog.show()


        val creation: ProductInterface =
            Retroclient.getSingleTonClient()!!.create(ProductInterface::class.java)
        val call = creation.getprodata(subcat_id!!.toInt())
        call.enqueue(object : Callback<ProductInfo> {
            override fun onResponse(call: Call<ProductInfo>, response: Response<ProductInfo>) {
                if (response.isSuccessful) {
                    AlertDialog.dismiss()
                    val listproduct = response.body()?.productTbl
                    if (listproduct != null) {
                        adepterproduct =
                            AdepterProduct(listproduct as ArrayList<ProductTblItem>, baseContext)

                        RV_product.adapter = adepterproduct

                        adepterproduct?.setOnClickListner(object : ClickInterface {
                            override fun onclicked12(position: Int, Types: Int) {
                                if (Types == 3) {
                                    val intent =
                                        Intent(this@ProductActivity, Produductdetails::class.java)
                                    intent.putExtra("pro_id", listproduct[position].productId)
                                    intent.putExtra("pro_name", listproduct[position].productName)
                                    startActivity(intent)
                                }
                                if (Types == 99) {
                                    if (Utils().islogin(this@ProductActivity)) {
                                        CallApi("plus", listproduct[position].productId)
                                    } else {
                                        val intent =
                                            Intent(this@ProductActivity, LoginActivity::class.java)
                                        startActivity(intent)
                                    }
                                }
                                if (Types == 0) {
                                    CallApi("minus", listproduct[position].productId)

                                }
                                if (Types == 1) {
                                    CallApi("plus", listproduct[position].productId)
                                }
                            }
                        })

                    } else {
                        Ll_nodata.visibility = View.VISIBLE
                    }
                }
            }

            override fun onFailure(call: Call<ProductInfo>, t: Throwable) {
                Log.e("profail", t.message.toString())
            }
        })
    }

    private fun CallApi(typ: String, productId: String?) {
        if (Utils().isNetworkAvailable(this@ProductActivity)) {
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
                    AddToCartInterface::class.java
                )
            val call = creation.insertdata(sherdiscustID, productId, typ)
            call?.enqueue(object : Callback<ModelCartaddInfo> {
                override fun onResponse(
                    call: Call<ModelCartaddInfo>,
                    response: Response<ModelCartaddInfo>
                ) {
                    AlertDialog.dismiss()
                    tvmytotalitems.text = response.body()?.count.toString()
                    Utils().Logindata(
                        this@ProductActivity,
                        "cartitemtotal",
                        response.body()?.count.toString()
                    )
                    Toast.makeText(
                        this@ProductActivity,
                        response.body()?.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onFailure(
                    call: Call<ModelCartaddInfo>,
                    t: Throwable
                ) {
                    Log.e("onfailadd", t.message + "   l ")
                    Toast.makeText(
                        this@ProductActivity,
                        t.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })


        } else {
            Utils().showToastShortForNoInternet(this@ProductActivity)
            val snackbar: TSnackbar = TSnackbar
                .make(
                    RV_product,
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

    override fun onResume() {
        super.onResume()
        if (Utils().islogin(this)) {
            val preferences: SharedPreferences = this.getSharedPreferences(main_key, MODE_PRIVATE)
            val cartitemcount = preferences.getString(ucart_key, "0")
            tvmytotalitems.text = cartitemcount

        }

    }
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}

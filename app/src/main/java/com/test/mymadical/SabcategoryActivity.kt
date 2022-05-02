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
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidadvance.topsnackbar.TSnackbar
import com.bumptech.glide.Glide
import com.test.mymadical.Adepter.AdepterSubcategory
import com.test.mymadical.Interface.ClickInterface
import com.test.mymadical.Interface.SubcategoryInterface
import com.test.mymadical.Utils.Utils
import com.test.mymadical.model.SubcategoryInfo
import com.test.mymadical.model.SubcategoryTblItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SabcategoryActivity : AppCompatActivity() {
    var toolbar: Toolbar? = null

    var adeptersabCategory: AdepterSubcategory? = null
    var cat_id: String = ""
    var cat_name: String = ""
    lateinit var RV_subcat: RecyclerView
    lateinit var Ll_nodata: LinearLayout
    lateinit var tvtitle: TextView
    lateinit var tvmytotalitems: TextView
    val main_key = "my_pref"
    val ucart_key = "cartitemtotal"
    lateinit var tvcart: RelativeLayout
    lateinit var imgsearch: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sabcategory)



        tvtitle = findViewById(R.id.tvtitle)
        imgsearch = findViewById(R.id.imgsearch)
        tvmytotalitems = findViewById(R.id.tvmytotalitems)
        tvcart = findViewById(R.id.tvcart)
        Ll_nodata = findViewById(R.id.Ll_nodata)
        RV_subcat = findViewById(R.id.RV_subcat)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()?.setDisplayShowHomeEnabled(true)

        val preferences: SharedPreferences = this.getSharedPreferences(main_key, MODE_PRIVATE)
        val cartitemcount = preferences.getString(ucart_key, "0")
        tvmytotalitems.text = cartitemcount

        cat_id = intent.getStringExtra("cat_id").toString()
        cat_name = intent.getStringExtra("cat_name").toString()
        RV_subcat.layoutManager = GridLayoutManager(this, 3)

        tvcart.setOnClickListener {
            if(Utils().islogin(this)){
                val intent = Intent(this, ActivityMyCart::class.java)
                startActivity(intent)
            } else{
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
        }
        imgsearch.setOnClickListener {
            val intent = Intent(this , SearchActivity::class.java)
            startActivity(intent)
        }


        tvtitle.setText(cat_name)


        if (Utils().isNetworkAvailable(this)) {
            getsubdata()
        } else {
            Utils().showToastShortForNoInternet(this)
            val snackbar: TSnackbar = TSnackbar
                .make(
                    RV_subcat,
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

    private fun getsubdata() {
        val progress = LayoutInflater.from(this)
            .inflate(R.layout.custom_progress_dialog, null)


        val builder = AlertDialog.Builder(this)
            .setView(progress)
        val AlertDialog = builder.create()
        AlertDialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        AlertDialog.setCancelable(false) // disable dismiss by tapping outside of the dialog

        AlertDialog.show()
        val creation: SubcategoryInterface =
            Retroclient.getSingleTonClient()!!.create(SubcategoryInterface::class.java)
        val call = creation.getsubdata(cat_id.toInt())

        call.enqueue(object : Callback<SubcategoryInfo> {
            override fun onResponse(
                call: Call<SubcategoryInfo>,
                response: Response<SubcategoryInfo>
            ) {
                if (response.isSuccessful) {

                    AlertDialog.dismiss()
                    val listofsubcat = response.body()?.subcategoryTbl
                    if (listofsubcat != null) {
                        adeptersabCategory =
                            AdepterSubcategory(
                                listofsubcat as List<SubcategoryTblItem>,
                                baseContext
                            )
                        RV_subcat.adapter = adeptersabCategory
                        Log.d("DSFSF", response.body()!!.msg + "")
                    } else {
                        Ll_nodata.visibility = View.VISIBLE

                    }

                    adeptersabCategory?.setOnItemClickListener(object : ClickInterface {
                        override fun onclicked12(position: Int, Types: Int) {
                            if (Types == 2) {
                                val intent =
                                    Intent(this@SabcategoryActivity, ProductActivity::class.java)
                                intent.putExtra("subcat_id", listofsubcat?.get(position)?.subcategoryId)
                                intent.putExtra("subcat_name", listofsubcat?.get(position)?.subcategoryName)
                                startActivity(intent)
                            }

                        }
                    })
                }
            }

            override fun onFailure(call: Call<SubcategoryInfo>, t: Throwable) {
                Log.d("DSFSF", t.message + " hii")

            }
        })
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

}
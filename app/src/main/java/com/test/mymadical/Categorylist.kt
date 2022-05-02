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
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidadvance.topsnackbar.TSnackbar
import com.test.mymadical.Adepter.AdepterCategorylist
import com.test.mymadical.Interface.CategoryInterface
import com.test.mymadical.Interface.ClickInterface
import com.test.mymadical.Utils.Utils
import com.test.mymadical.model.CategoryInfo
import com.test.mymadical.model.CategoryTblItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Categorylist : AppCompatActivity() {
    lateinit var RV_cat: RecyclerView
    var adeptercategorylist: AdepterCategorylist? = null
    val main_key = "my_pref"
    val custID_key = "custID_key"
    val ucart_key = "cartitemtotal"

    var toolbar: Toolbar? = null
    lateinit var tvtitle: TextView
    lateinit var tvmytotalitems: TextView
    lateinit var tvcart: RelativeLayout
    lateinit var imgsearch: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_categorylist)
        toolbar = findViewById(R.id.toolbar)


        setSupportActionBar(toolbar)

        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()?.setDisplayShowHomeEnabled(true)

        tvtitle = findViewById(R.id.tvtitle)
        imgsearch = findViewById(R.id.imgsearch)
        tvmytotalitems = findViewById(R.id.tvmytotalitems)
        tvcart = findViewById(R.id.tvcart)
        RV_cat = findViewById(R.id.RV_cat)
        RV_cat.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)


        val preferences: SharedPreferences = this.getSharedPreferences(main_key, MODE_PRIVATE)
        val cartitemcount = preferences.getString(ucart_key, "0")
        tvmytotalitems.text = cartitemcount



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


        tvtitle.setText("Categories")



        if (Utils().isNetworkAvailable(this)) {
            getcategory()
        } else {
            Utils().showToastShortForNoInternet(this)
            val snackbar: TSnackbar = TSnackbar
                .make(
                    RV_cat,
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

    private fun getcategory() {


        val progress = LayoutInflater.from(this)
            .inflate(R.layout.custom_progress_dialog, null)


        val builder = AlertDialog.Builder(this)
            .setView(progress)
        val AlertDialog = builder.create()
        AlertDialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        AlertDialog.setCancelable(false) // disable dismiss by tapping outside of the dialog

        AlertDialog.show()

        val creation: CategoryInterface =
            Retroclient.getSingleTonClient()!!.create(CategoryInterface::class.java)
        val call = creation.getdata()
        Log.d("DSFSF", "SFSLODJKSI1U")


        call.enqueue(object : Callback<CategoryInfo> {
            override fun onResponse(call: Call<CategoryInfo>, response: Response<CategoryInfo>) {
                if (response.isSuccessful) {
                    AlertDialog.dismiss()

                    val listofcat = response.body()?.categoryTbl

                    adeptercategorylist =
                        AdepterCategorylist(listofcat as List<CategoryTblItem>, baseContext)
                    RV_cat.adapter = adeptercategorylist
                    Log.d("DSFSF", response.body()!!.msg + "")

                    adeptercategorylist?.setOnItemClickListener(object : ClickInterface {
                        override fun onclicked12(position: Int, Types: Int) {
                            if (Types == 1) {
                                val intent =
                                    Intent(this@Categorylist, SabcategoryActivity::class.java)
                                intent.putExtra("cat_id", listofcat.get(position).categoryId)
                                intent.putExtra("cat_name", listofcat.get(position).categoryName)
                                startActivity(intent)
                            }
                        }
                    })
                }

            }

            override fun onFailure(call: Call<CategoryInfo>, t: Throwable) {
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
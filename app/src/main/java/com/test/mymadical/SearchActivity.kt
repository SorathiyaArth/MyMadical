package com.test.mymadical

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import android.widget.TextView.OnEditorActionListener
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.MenuItemCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.test.mymadical.Adepter.AdepterProduct
import com.test.mymadical.Interface.ClickInterface
import com.test.mymadical.Interface.SearchProductInterface
import com.test.mymadical.Utils.Utils
import com.test.mymadical.model.ProductInfo
import com.test.mymadical.model.ProductTblItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SearchActivity : AppCompatActivity() {
    var toolbar: Toolbar? = null
    lateinit var tvtitle: TextView
    lateinit var tvcart: RelativeLayout
    lateinit var RV_product: RecyclerView
    lateinit var Ll_nodata: LinearLayout
    lateinit var imgsearch: ImageView
    var adepterproduct: AdepterProduct? = null

    val main_key = "my_pref"
    val custID_key = "custID_key"
    val ucart_key = "cartitemtotal"
    var sherdiscustID: String? = null
    var cartitemcount: String? = null
    lateinit var tvmytotalitems: TextView
    lateinit var search: SearchView
    var searchtext: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        toolbar = findViewById(R.id.toolbar)
        RV_product = findViewById(R.id.RV_product)
        Ll_nodata = findViewById(R.id.Ll_nodata)
        tvtitle = findViewById(R.id.tvtitle)
        imgsearch = findViewById(R.id.imgsearch)
        tvcart = findViewById(R.id.tvcart)
        tvmytotalitems = findViewById(R.id.tvmytotalitems)
        imgsearch.isVisible = false
        RV_product = findViewById(R.id.RV_product)
        RV_product.layoutManager = GridLayoutManager(this, 2)


        setSupportActionBar(toolbar)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()?.setDisplayShowHomeEnabled(true)
        val preferences: SharedPreferences = this.getSharedPreferences(main_key, MODE_PRIVATE)
        sherdiscustID = preferences.getString(custID_key, "")
        cartitemcount = preferences.getString(ucart_key, "")
        tvmytotalitems.text =cartitemcount
        tvtitle.setText("My Medical")


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
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }



    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            1 -> {
                if (resultCode == Activity.RESULT_OK) {
                    /* if (data != null && data.extras != null) {

                         bitmap = data!!.extras!!["data"] as Bitmap
                         if (bitmap != null) {
                             imgUser.setImageBitmap(bitmap)
                             Log.wtf("DDD", "dddd")
                         }
                     }*/
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    Toast.makeText(this, "cancelled", Toast.LENGTH_SHORT).show()

                }
            }
            2 -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    // Glide.with(this).load(data.data).into(imgUser)


                    /* val file = File(getRealPathFromURI(data.data!!))
                     path = file.getPath()
                     Log.e("path", file.getPath() + "  111")


                     CheckImageSize(file.toString())
 */
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        menu.clear()
        menuInflater.inflate(R.menu.profile_menu, menu)

        val searchItem = menu.findItem(R.id.search)
        search = MenuItemCompat.getActionView(searchItem) as SearchView
        search.setQueryHint("Search Products...")
        searchItem.expandActionView()
        val searchEditId = androidx.appcompat.R.id.search_close_btn
        val searchTxtId = androidx.appcompat.R.id.search_src_text
        val et: ImageView = search.findViewById(searchEditId)
        val etXt: TextView = search.findViewById(searchTxtId)
        etXt.setTextColor(resources.getColor(R.color.white))
        et.setImageResource(R.drawable.ic_next)
// this code for search button from keyboard
        etXt.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                // Your piece of code on keyboard search click
                    if(searchtext!=null){
                        callapi()
                        Log.e("lkeed", searchtext.toString())
                        toolbar?.collapseActionView()
                    }
                else{
                        Toast.makeText(this, "Please Enter Your Search Text", Toast.LENGTH_SHORT).show()

                    }



                true
            } else false
        })


        et.setOnClickListener {
            RV_product.visibility=View.GONE

            callapi()
            toolbar?.collapseActionView()


        }

        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                menu.findItem(R.id.search).collapseActionView()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.length > 0) {
                    searchtext = newText
                    Log.e("lkd", newText)

                }
                return false
            }
        })

        return true
    }

    private fun callapi() {
        val progress = LayoutInflater.from(this)
            .inflate(R.layout.custom_progress_dialog, null)


        val builder = AlertDialog.Builder(this)
            .setView(progress)
        val AlertDialog = builder.create()
        AlertDialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        AlertDialog.setCancelable(false) // disable dismiss by tapping outside of the dialog

        AlertDialog.show()


        val creation: SearchProductInterface =
            Retroclient.getSingleTonClient()!!.create(SearchProductInterface::class.java)
        val call = creation.getsearchprodata(searchtext.toString())
        call.enqueue(object : Callback<ProductInfo> {
            override fun onResponse(call: Call<ProductInfo>, response: Response<ProductInfo>) {
                if (response.isSuccessful) {
                    AlertDialog.dismiss()
                    tvtitle.isVisible = true
                    tvtitle.text = "Result for " + searchtext
                    val listproduct = response.body()?.productTbl
                    if (listproduct != null) {
                        RV_product.visibility = View.VISIBLE
                        Ll_nodata.visibility = View.GONE

                        adepterproduct =
                            AdepterProduct(listproduct as ArrayList<ProductTblItem>, baseContext)

                        RV_product.adapter = adepterproduct

                        adepterproduct?.setOnClickListner(object : ClickInterface {
                            override fun onclicked12(position: Int, Types: Int) {
                                if (Types == 3) {
                                    val intent = Intent(
                                        this@SearchActivity,
                                        Produductdetails::class.java
                                    )
                                    intent.putExtra("pro_id", listproduct[position].productId)
                                    intent.putExtra("pro_name", listproduct[position].productName)
                                    startActivity(intent)
                                }
                                if (Types == 99) {
                                    if (Utils().islogin(this@SearchActivity)) {
                                        //CallApi("plus", listproduct[position].productId)
                                    } else {
                                        val intent = Intent(
                                            this@SearchActivity,
                                            LoginActivity::class.java
                                        )
                                        startActivity(intent)
                                    }
                                }
                                if (Types == 0) {
                                    // CallApi("minus", listproduct[position].productId)

                                }
                                if (Types == 1) {
                                    // CallApi("plus", listproduct[position].productId)
                                }
                            }
                        })

                    } else {
                        RV_product.visibility = View.GONE
                        Ll_nodata.visibility = View.VISIBLE
                    }
                }
            }

            override fun onFailure(call: Call<ProductInfo>, t: Throwable) {
                Log.e("profail", t.message.toString())
            }
        })
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
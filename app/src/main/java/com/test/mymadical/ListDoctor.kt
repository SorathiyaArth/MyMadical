package com.test.mymadical

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.util.Log.e
import android.view.LayoutInflater
import android.view.MenuItem
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.test.mymadical.Adepter.AdepterDoctorDetails
import com.test.mymadical.Interface.DoctorDetailsInterface
import com.test.mymadical.model.DoctorDetailsTblItem
import com.test.mymadical.model.ModelDoctorDetails
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class ListDoctor : AppCompatActivity() {
    var toolbar: Toolbar? = null
    var city_name: String = ""
    var doc_type: String = ""
    val main_key = "my_pref"
    val custID_key = "custID_key"
    val ucart_key = "cartitemtotal"

    var doc_img: Int = 0
    lateinit var listdoctor: List<DoctorDetailsTblItem>
    lateinit var filteredlistdoctor: List<DoctorDetailsTblItem>
    lateinit var tvtitle: TextView
    lateinit var tvmytotalitems: TextView
    lateinit var txtFilterBy: TextView
    lateinit var txtFilterByCost: TextView
    lateinit var txtFilterByRating: TextView
    lateinit var txtFilterByExperience: TextView
    lateinit var Txt_nodata: TextView
    lateinit var mAdepter: AdepterDoctorDetails
    lateinit var Img_doctype: ImageView
    lateinit var imgsearch: ImageView
    lateinit var tvcart: RelativeLayout
    lateinit var recycleview_doctor: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listdoctor)
        ids()

        val preferences: SharedPreferences = this.getSharedPreferences(main_key, MODE_PRIVATE)
        val cartitemcount = preferences.getString(ucart_key, "0")
        tvmytotalitems.text = cartitemcount


        setSupportActionBar(toolbar)

        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()?.setDisplayShowHomeEnabled(true)
        recycleview_doctor.layoutManager = LinearLayoutManager(this)
        imgsearch.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }

        tvcart.setOnClickListener {
            val intent = Intent(this, ActivityMyCart::class.java)
            startActivity(intent)
        }

        Apicall()
        txtFilterByRating.setOnClickListener {
            optionmenu(txtFilterByRating, "rating")


        }
        txtFilterByCost.setOnClickListener {
            optionmenu(txtFilterByCost,  "cost")



        }
        txtFilterByExperience.setOnClickListener {
            optionmenu(txtFilterByExperience, "experience")



        }

    }

    private fun optionmenu(s: TextView,Name: String) {
        val popupMenu = PopupMenu(this, s)
        popupMenu.inflate(R.menu.filter_menu)
        popupMenu.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
            override fun onMenuItemClick(item: MenuItem?): Boolean {
                when (item?.itemId) {
                    R.id.itemhightolow -> {
                        if (Name=="rating"){
                            Collections.sort(filteredlistdoctor,
                                Comparator<DoctorDetailsTblItem?> { lhs, rhs -> rhs.docStar!!.compareTo(lhs.docStar.toString()) })
                        }else if (Name=="cost"){
                            Collections.sort(filteredlistdoctor,
                                Comparator<DoctorDetailsTblItem?> { lhs, rhs -> rhs.docVisitFees!!.compareTo(lhs.docVisitFees.toString()) })
                        }else if (Name=="experience"){
                            Collections.sort(filteredlistdoctor,
                                Comparator<DoctorDetailsTblItem?> { lhs, rhs -> rhs.docExperience!!.compareTo(lhs.docExperience.toString()) })
                        }


                        mAdepter =
                            AdepterDoctorDetails(filteredlistdoctor as List<DoctorDetailsTblItem>, baseContext)
                        recycleview_doctor.adapter = mAdepter
                        txtFilterBy.setText(  " High"+Name+ " to " +"Low"+Name)

                    }
                    R.id.itemlowtohigh -> {
                        if (Name=="rating"){
                            Collections.sort(filteredlistdoctor,
                                Comparator<DoctorDetailsTblItem?> { lhs, rhs -> lhs.docStar!!.compareTo(rhs.docStar.toString()) })
                        }else if (Name=="cost"){
                            Collections.sort(filteredlistdoctor,
                                Comparator<DoctorDetailsTblItem?> { lhs, rhs -> lhs.docVisitFees!!.compareTo(rhs.docVisitFees.toString()) })
                        }else if (Name=="experience"){
                            Collections.sort(filteredlistdoctor,
                                Comparator<DoctorDetailsTblItem?> { lhs, rhs -> lhs.docExperience!!.compareTo(rhs.docExperience.toString()) })
                        }
                        mAdepter =
                            AdepterDoctorDetails(filteredlistdoctor as List<DoctorDetailsTblItem>, baseContext)
                        recycleview_doctor.adapter = mAdepter
                        txtFilterBy.setText(" Low"+Name+ " to " +" High"+Name)

                    }
                }
                return false
            }
        })
        popupMenu.show()
    }


    private fun ids() {
        tvtitle = findViewById(R.id.tvtitle)
        tvmytotalitems = findViewById(R.id.tvmytotalitems)
        txtFilterBy = findViewById(R.id.txtFilterBy)
        txtFilterByCost = findViewById(R.id.txtFilterByCost)
        txtFilterByRating = findViewById(R.id.txtFilterByRating)
        txtFilterByExperience = findViewById(R.id.txtFilterByExperience)
        imgsearch = findViewById(R.id.imgsearch)
        tvcart = findViewById(R.id.tvcart)
        toolbar = findViewById(R.id.toolbar)
        Img_doctype = findViewById(R.id.Img_doctype)
        recycleview_doctor = findViewById(R.id.recycleview_doctor)
        Txt_nodata = findViewById(R.id.Txt_nodata)
    }

    private fun Apicall() {
        val progress = LayoutInflater.from(this)
            .inflate(R.layout.custom_progress_dialog, null)


        val builder = AlertDialog.Builder(this)
            .setView(progress)
        val AlertDialog = builder.create()
        AlertDialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        AlertDialog.setCancelable(false) // disable dismiss by tapping outside of the dialog

        AlertDialog.show()
        val creation = Retroclient.getSingleTonClient()?.create(DoctorDetailsInterface::class.java)
        val call = creation?.getdoctordata()
        call?.enqueue(object : Callback<ModelDoctorDetails> {
            override fun onResponse(
                call: Call<ModelDoctorDetails>,
                response: Response<ModelDoctorDetails>
            ) {
                if (response.isSuccessful) {
                    AlertDialog.dismiss()
                    listdoctor = response.body()?.doctorDetailsTbl as List<DoctorDetailsTblItem>
                    Collections.shuffle(listdoctor);
                    filteredlistdoctor = listdoctor



                    mAdepter =
                        AdepterDoctorDetails(
                            filteredlistdoctor as List<DoctorDetailsTblItem>,
                            baseContext
                        )
                    recycleview_doctor.adapter = mAdepter


                }
            }

            override fun onFailure(call: Call<ModelDoctorDetails>, t: Throwable) {
                e("jbhhfg", t.message.toString())


            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
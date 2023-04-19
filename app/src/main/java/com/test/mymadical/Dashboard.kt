package com.test.mymadical

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.core.widget.NestedScrollView
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
 import com.androidadvance.topsnackbar.TSnackbar
import com.bumptech.glide.Glide
 import com.smarteist.autoimageslider.SliderView
import com.test.mymadical.Adepter.AdepterCategory
import com.test.mymadical.Adepter.BannerAdepter
import com.test.mymadical.Interface.CategoryInterface
import com.test.mymadical.Interface.ClickInterface
import com.test.mymadical.Utils.Utils
import com.test.mymadical.model.CategoryInfo
import com.test.mymadical.model.CategoryTblItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Dashboard : AppCompatActivity() {
    var toolbar: Toolbar? = null
    lateinit var Rv_Cat: RecyclerView
    lateinit var scrolldashboard: NestedScrollView
    lateinit var tvtitle: TextView
    lateinit var txtName: TextView
    lateinit var tvmytotalitems: TextView
    lateinit var txtMobile: TextView
    lateinit var tvcart: RelativeLayout
    lateinit var realtiveNaveHeader: RelativeLayout
    lateinit var relativeContactUs: RelativeLayout
    lateinit var relativeMyOrders: RelativeLayout
    lateinit var relativeShareApp: RelativeLayout
    lateinit var relativeRateApp: RelativeLayout
    lateinit var relativeOffer: RelativeLayout
    lateinit var relativeMyAccount: RelativeLayout
    lateinit var relativeAbout: RelativeLayout
    lateinit var relativeCategory: RelativeLayout
    lateinit var relativeLogOut: RelativeLayout
    lateinit var Ll_category: LinearLayout
    lateinit var Ll_mian: LinearLayout
    lateinit var Cv_FindDoctor: CardView
    lateinit var Btn_upload: Button
    lateinit var imgsearch: ImageView
    lateinit var imgUserProfile: ImageView
    val login_key = "is_login"

    val islogin = "1"
    var sherdlogin: String = ""
    var sherdcustID: String = ""
    val main_key = "my_pref"
    val custID_key = "custID_key"

    val uname_key = "uname"
    val unumber_key = "unumber"
    val uemail_key = "uemail"
    val uimage_key = "uimage"
    val ucart_key = "cartitemtotal"


    var adepterCategory: AdepterCategory? = null
    val bannerimage: ArrayList<String> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)


        Ids()



        setSupportActionBar(toolbar)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()?.setDisplayShowHomeEnabled(true)
        getSupportActionBar()?.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24)

        Rv_Cat.layoutManager = GridLayoutManager(this, 3)

        tvtitle.text = "My Medical"


        Ll_category.setOnClickListener {
            val intent = Intent(this, Categorylist::class.java)
            startActivity(intent)
        }
        relativeCategory.setOnClickListener {
            val intent = Intent(this, Categorylist::class.java)
            startActivity(intent)
        }
        imgsearch.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }
        relativeRateApp.setOnClickListener {
              val uri: Uri = Uri.parse("market://details?id=$packageName")
             val goToMarket = Intent(Intent.ACTION_VIEW, uri)
             // To count with Play market backstack, After pressing back button,
             // to taken back to our application, we need to add following flags to intent.
             goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY or
                     Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                     Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
             try {
                 startActivity(goToMarket)
             } catch (e: ActivityNotFoundException) {
                 startActivity(Intent(Intent.ACTION_VIEW,
                     Uri.parse("http://play.google.com/store/apps/details?id=$packageName")))
             }
        }
        relativeShareApp.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name")
            var shareMessage = "\nLet me recommend you this application\n\n"
            shareMessage =
                """
                ${shareMessage}https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}
                
                
                """.trimIndent()
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
            startActivity(Intent.createChooser(shareIntent, "choose one"))









        }
        val preferences: SharedPreferences = this.getSharedPreferences(main_key, MODE_PRIVATE)

        val sherdiscustID = preferences.getString(custID_key, "afd")
        val shareduname = preferences.getString(uname_key, "Guest User")
        val cartitemcount = preferences.getString(ucart_key, "0")
        val sherdnumber = preferences.getString(unumber_key, "**********")
        val sherdimage = preferences.getString(uimage_key, "https://www.kindpng.com/picc/m/495-4952535_create-digital-profile-icon-blue-user-profile-icon.png")


        Log.e("jbhhfg", sherdimage.toString() +"  dash")

        txtName.text = shareduname
        txtMobile.text = sherdnumber
        tvmytotalitems.text = cartitemcount
        Glide.with(this).load(R.drawable.img_default_user_icon)
            .placeholder(R.drawable.img_default_user_icon)
            .error(R.drawable.img_default_user_icon)
            .fallback(R.drawable.img_default_user_icon)
            .into(imgUserProfile)


        tvcart.setOnClickListener {
            if (Utils().islogin(this)) {
                val intent = Intent(this, ActivityMyCart::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }


        }
        if (!Utils().islogin(this)) {
            relativeLogOut.visibility = View.GONE
        }
        relativeLogOut.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Confirm")
            builder.setMessage("Are you sure?")
            builder.setPositiveButton("YES") { dialog, which ->

                preferences.edit().clear().apply()
                finishAffinity()
                startActivity(intent)
                dialog.dismiss()
            }

            builder.setNegativeButton(
                "NO"
            ) { dialog, which ->
                dialog.dismiss()
            }
            val alert = builder.create()
            alert.show()



        }
        realtiveNaveHeader.setOnClickListener {
            if (!Utils().islogin(this)) {

                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }


        }
        relativeMyOrders.setOnClickListener {
            if (!Utils().islogin(this)) {

                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            } else{
                val intent = Intent(this, Activity_order::class.java)
                startActivity(intent)
            }

        }
        relativeMyAccount.setOnClickListener {
            if (!Utils().islogin(this)) {

                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }else{
                val intent = Intent(this, MyAccounts::class.java)
                startActivity(intent)

            }


        }

        Cv_FindDoctor.setOnClickListener {
            val intent = Intent(this, FindDoctorActivity::class.java)
            startActivity(intent)
        }
        relativeAbout.setOnClickListener {
            val intent = Intent(this, ActivityAboutUs::class.java)
            startActivity(intent)
        }
        relativeContactUs.setOnClickListener {
            val intent = Intent(this, ActivityContactUs::class.java)
            startActivity(intent)
        }
        relativeOffer.setOnClickListener {
            val intent = Intent(this, ActivityOffers::class.java)
            startActivity(intent)
        }



        Btn_upload.setOnClickListener {


            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
          /*  val progress = LayoutInflater.from(this)
                .inflate(R.layout.upload_prescription, null)
            val builder = AlertDialog.Builder(this)
                .setView(progress)
            val AlertDialog = builder.create()
            AlertDialog.show()
            val Txt_camara = progress.findViewById<TextView>(R.id.Txt_camara)
            val Txt_Galary = progress.findViewById<TextView>(R.id.Txt_Galary)

            Txt_Galary.setOnClickListener {
                val pickPhoto = Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                )
                startActivityForResult(pickPhoto, 2)
                AlertDialog.dismiss()
            }
            Txt_camara.setOnClickListener {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent, 1)
                AlertDialog.dismiss()


            }
*/
        }

        if (Utils().isNetworkAvailable(this)) {
            getcategory()
            getbannerimage()
        } else {
            Utils().showToastShortForNoInternet(this)
            val snackbar: TSnackbar = TSnackbar
                .make(
                    Rv_Cat,
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

    private fun Ids() {
        toolbar = findViewById(R.id.toolbar)
        relativeShareApp = findViewById(R.id.relativeShareApp)
        relativeRateApp = findViewById(R.id.relativeRateApp)
        relativeOffer = findViewById(R.id.relativeOffer)
        relativeContactUs = findViewById(R.id.relativeContactUs)
        relativeAbout = findViewById(R.id.relativeAbout)
        txtMobile = findViewById(R.id.txtMobile)
        tvmytotalitems = findViewById(R.id.tvmytotalitems)
        txtName = findViewById(R.id.txtName)
        Ll_mian = findViewById(R.id.Ll_mian)
        scrolldashboard = findViewById(R.id.scrolldashboard)
        tvtitle = findViewById(R.id.tvtitle)
        imgsearch = findViewById(R.id.imgsearch)
        Ll_category = findViewById(R.id.Ll_category)
        relativeMyOrders = findViewById(R.id.relativeMyOrders)
        relativeMyAccount = findViewById(R.id.relativeMyAccount)
        realtiveNaveHeader = findViewById(R.id.realtiveNaveHeader)
        Cv_FindDoctor = findViewById(R.id.Cv_FindDoctor)
        Btn_upload = findViewById(R.id.Btn_upload)
        relativeCategory = findViewById(R.id.relativeCategory)
        tvcart = findViewById(R.id.tvcart)
        relativeLogOut = findViewById(R.id.relativeLogOut)
        imgUserProfile = findViewById(R.id.imgUserProfile)

        Rv_Cat = findViewById(R.id.Rv_Cat)
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

    private fun getbannerimage() {

        bannerimage.add("https://cdn.grabon.in/gograbon/images/web-images/uploads/1618566499779/medicines-offers.jpg")
        bannerimage.add("https://t4.ftcdn.net/jpg/02/72/37/73/360_F_272377344_JKCD4T13QUmIm6T0Yn109BnCJPt7MAHV.jpg")
        bannerimage.add("https://media.istockphoto.com/vectors/pharmacy-delivery-online-store-vector-banner-vector-id1223488226")
        bannerimage.add("https://media.istockphoto.com/vectors/medicine-and-pharmacy-background-drugstore-banner-design-with-pills-vector-id1282142100")

        val imageSlider = findViewById<SliderView>(R.id.imageSlider)

        imageSlider.setSliderAdapter(
            BannerAdepter(
                bannerimage,
                this

            )
        )
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

                    scrolldashboard.visibility = View.VISIBLE
                    Ll_mian.visibility = View.VISIBLE
                    val listofcat = response.body()?.categoryTbl
                    //  Collections.shuffle(listofcat)
                    adepterCategory =
                        AdepterCategory(listofcat as List<CategoryTblItem>, baseContext)
                    Rv_Cat.adapter = adepterCategory
                    Log.d("DSFSF", response.body()!!.msg + "")

                    adepterCategory?.setOnItemClickListener(object : ClickInterface {
                        override fun onclicked12(position: Int, Types: Int) {
                            if (Types == 1) {
                                val intent = Intent(this@Dashboard, SabcategoryActivity::class.java)
                                intent.putExtra("cat_id", listofcat.get(position).categoryId)
                                intent.putExtra("cat_name", listofcat.get(position).categoryName)
                                startActivity(intent)
                            }
                        }
                    }) } }
            override fun onFailure(call: Call<CategoryInfo>, t: Throwable) {
                Log.e("DSFSF", t.message + " hii") }
        }) }

    override fun onSupportNavigateUp(): Boolean {

        Log.e("dcjkdbch", "xljg")


        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        var toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        //toggle.isDrawerIndicatorEnabled = false
        toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        drawerLayout.post(Runnable { toggle.syncState() })
        return true
    }
    override fun onResume() {
        super.onResume()
        if (Utils().islogin(this)) {
            val preferences: SharedPreferences = this.getSharedPreferences(main_key, MODE_PRIVATE)

            val shareduname = preferences.getString(uname_key, "Guest User")
            val cartitemcount = preferences.getString(ucart_key, "0")
            val sherdnumber = preferences.getString(unumber_key, "**********")
            val image = preferences.getString(uimage_key, "")

            txtName.text = shareduname
            txtMobile.text = sherdnumber
            tvmytotalitems.text = cartitemcount
            Glide.with(this).load(R.drawable.img_default_user_icon).into(imgUserProfile)
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

}
package com.test.mymadical

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.test.mymadical.Interface.CouponcodeInfoInterface
import com.test.mymadical.Interface.orderplacedInfoInterface
import com.test.mymadical.Utils.Utils
import com.test.mymadical.model.ModelCouponcodeInfo
import com.test.mymadical.model.ModelOrderPlaced
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class ActivityOrderReview : AppCompatActivity() {
    var total: String = ""
    var shipping: String = ""
    var CustId: String = ""
    var addid: String = ""
    var name: String = ""
    var content: String = ""
    var address: String = ""
    var date: String = ""
    lateinit var paymentmode: String

    lateinit var imgeditorderaddress: ImageView
    lateinit var txtbillingname: TextView
    lateinit var tvSucessCoupon: TextView
    lateinit var txtSelectDate: TextView
    lateinit var txtbillingaddress: TextView
    lateinit var txtbillingmonumber: TextView
    lateinit var txtsubtotal: TextView
    lateinit var txtshipingcharg: TextView
    lateinit var txtpayableamnt: TextView
    lateinit var tvFailCoupon: TextView
    lateinit var txtgrndtotal: TextView
    lateinit var txtcoupondis: TextView
    lateinit var txtorderHideCoupon: TextView
    lateinit var etPaymentCoupencode: EditText
    lateinit var btnApplyCoupon: Button
    lateinit var btnplaceorder: Button
    lateinit var radioGRP: RadioGroup
    lateinit var radioCOD: RadioButton
    lateinit var radioOnline: RadioButton
    lateinit var couponLayout: LinearLayout
    lateinit var coupondiscount: LinearLayout
    lateinit var relDate: RelativeLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_review)
        Ids()
        total = intent.getStringExtra("total").toString()
        CustId = intent.getStringExtra("CustId").toString()
        addid = intent.getStringExtra("addid").toString()
        name = intent.getStringExtra("name").toString()
        content = intent.getStringExtra("content").toString()
        address = intent.getStringExtra("address").toString()
        txtbillingname.text = name
        txtbillingaddress.text = address
        txtbillingmonumber.text = content

        txtsubtotal.text = total

        if (total.toInt() <= 50) {
            txtshipingcharg.setText("49")
            shipping = "49"
        } else {
            txtshipingcharg.text = "0"
            shipping = "0"


        }
        val grandtotal = total.toInt() + shipping.toInt()
        txtgrndtotal.text = grandtotal.toString()
        txtpayableamnt.text = grandtotal.toString()



        imgeditorderaddress.setOnClickListener {
            val intent = Intent(this, ActivityMyAddress::class.java)
            intent.putExtra("CustId", CustId)
            intent.putExtra("total", total)
            startActivity(intent)
            finish()
        }


        relDate.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)
            val dpd = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->

                    date = dayOfMonth.toString() + "/" + (month + 1) + "/" + year
                    txtSelectDate.text = date
                }, year, month, day
            )
            val now = System.currentTimeMillis() - 1000
            dpd.getDatePicker().setMinDate(now+1000 * 60 * 60 *5)
            dpd.getDatePicker().setMaxDate(now + 1000 * 60 * 60 * 24 * 4)

            dpd.show()
        }
        txtorderHideCoupon.setOnClickListener {

            if (couponLayout.visibility == View.VISIBLE) {
                val img = ContextCompat.getDrawable(
                    this,
                    R.drawable.downarrow
                )
                txtorderHideCoupon.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null)
                couponLayout.visibility = View.GONE
            } else {
                val img = ContextCompat.getDrawable(
                    this,
                    R.drawable.ic_baseline_keyboard_arrow_up_24
                )
                txtorderHideCoupon.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null)
                couponLayout.visibility = View.VISIBLE
            }

        }
        btnApplyCoupon.setOnClickListener {
            etPaymentCoupencode
            if (Utils().isNetworkAvailable(this)) {
                iscouponvelid()

            } else {
                Utils().showToastShortForNoInternet(this)

            }
        }
        btnplaceorder.setOnClickListener {

            if (radioOnline.isChecked) {
                paymentmode = "online"
            } else {
                paymentmode = "cod"
            }
            if (Utils().isNetworkAvailable(this)) {
                if (checkdate()) {
                    if (paymentmode == "cod") {
                        placeorder()
                    } else {
                        Toast.makeText(
                            this@ActivityOrderReview,
                            "This Payment method currantly not working",
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                }
            } else {
                Utils().showToastShortForNoInternet(this)

            }

        }


    }

    private fun placeorder() {

        val main_key = "my_pref"
        val ucart_key = "cartitemtotal"
        val preferences: SharedPreferences = this.getSharedPreferences(main_key, MODE_PRIVATE)
        val cartitemcount = preferences.getString(ucart_key, "0")


        val sdf = SimpleDateFormat("dd/MM/yyyy")
        val currantdate: String = sdf.format(Date())
        val creation: orderplacedInfoInterface = Retroclient.getSingleTonClient()!!.create(
            orderplacedInfoInterface::class.java
        )
        val call = creation.orderplaceddetails(
            CustId,
            txtpayableamnt.text.toString(),
            "PANDIND", currantdate,
            date,
            addid, "PANDING", cartitemcount.toString()
        )

        call.enqueue(object : Callback<ModelOrderPlaced> {
            override fun onResponse(
                call: Call<ModelOrderPlaced>,
                response: Response<ModelOrderPlaced>
            ) {
                Log.d("Orderplacerd", response.body()!!.message + " hii")

                if (response.body()!!.flag.equals("1")) {
                    val intent = Intent(this@ActivityOrderReview, Activity_order::class.java)

                    startActivity(intent)
                    finish()
                    com.test.mymadical.Utils.Utils()
                        .Logindata(this@ActivityOrderReview, "cartitemtotal", "0")
                }
            }

            override fun onFailure(call: Call<ModelOrderPlaced>, t: Throwable) {
                Log.d("Orderplacerd", t.message + " hii")
                Toast.makeText(
                    this@ActivityOrderReview,
                    t.message + "Your Order placed Successfull",
                    Toast.LENGTH_SHORT
                ).show()

            }

        })
    }

    private fun iscouponvelid() {
        val progress = LayoutInflater.from(this)
            .inflate(R.layout.custom_progress_dialog, null)


        val builder = AlertDialog.Builder(this)
            .setView(progress)
        val AlertDialog = builder.create()
        AlertDialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        AlertDialog.setCancelable(false) // disable dismiss by tapping outside of the dialog

        AlertDialog.show()

        val coupon = etPaymentCoupencode.text.toString()
        val creation: CouponcodeInfoInterface =
            Retroclient.getSingleTonClient()!!.create(CouponcodeInfoInterface::class.java)
        val call = creation.couponcodedetaildata(CustId, coupon)
        call.enqueue(object : Callback<ModelCouponcodeInfo> {
            override fun onResponse(
                call: Call<ModelCouponcodeInfo>,
                response: Response<ModelCouponcodeInfo>
            ) {
                val msgcode = response.body()?.flag

                if (response.isSuccessful) {
                    AlertDialog.dismiss()


                    if (msgcode.equals("1")) {

                        if (response.body()?.discount != null) {
                            tvSucessCoupon.visibility = View.VISIBLE
                            tvSucessCoupon.text = response.body()?.message
                            coupondiscount.visibility = View.VISIBLE
                            tvFailCoupon.visibility = View.GONE

                            val persontage = response.body()?.discount?.toInt()
                            val discount = ((persontage!! * total.toInt()) / 100).toInt()
                            val dtotal = total?.toInt() - discount!!
                            txtcoupondis.text = "-" + discount.toString()
                            txtgrndtotal.text = "₹" + dtotal.toString()
                            txtpayableamnt.text = "₹" + dtotal.toString()
                        }


                    } else {
                        coupondiscount.visibility = View.GONE
                        tvSucessCoupon.visibility = View.GONE

                        tvFailCoupon.visibility = View.VISIBLE
                        tvFailCoupon.text = response.body()?.message
                    }

                }
            }

            override fun onFailure(call: Call<ModelCouponcodeInfo>, t: Throwable) {
                Toast.makeText(this@ActivityOrderReview, t.message + "", Toast.LENGTH_SHORT).show()
            }
        })

    }

    fun checkdate(): Boolean {

        var isstudentnamevalid = false
        if (date.isEmpty()) {

            Toast.makeText(this@ActivityOrderReview, "Pleace select Date", Toast.LENGTH_SHORT)
                .show()
        } else {
            isstudentnamevalid = true
        }
        return isstudentnamevalid
    }

    private fun Ids() {
        imgeditorderaddress = findViewById(R.id.imgeditorderaddress)
        tvFailCoupon = findViewById(R.id.tvFailCoupon)
        coupondiscount = findViewById(R.id.coupondiscount)
        tvSucessCoupon = findViewById(R.id.tvSucessCoupon)
        txtbillingname = findViewById(R.id.txtbillingname)
        txtcoupondis = findViewById(R.id.txtcoupondis)
        txtSelectDate = findViewById(R.id.txtSelectDate)
        txtbillingaddress = findViewById(R.id.txtbillingaddress)
        txtbillingmonumber = findViewById(R.id.txtbillingmonumber)
        btnplaceorder = findViewById(R.id.btnplaceorder)
        txtsubtotal = findViewById(R.id.txtsubtotal)
        txtshipingcharg = findViewById(R.id.txtshipingcharg)
        txtgrndtotal = findViewById(R.id.txtgrndtotal)
        txtorderHideCoupon = findViewById(R.id.txtorderHideCoupon)
        txtpayableamnt = findViewById(R.id.txtpayableamnt)
        etPaymentCoupencode = findViewById(R.id.etPaymentCoupencode)
        btnApplyCoupon = findViewById(R.id.btnApplyCoupon)
        radioGRP = findViewById(R.id.radioGRP)
        radioCOD = findViewById(R.id.radioCOD)
        radioOnline = findViewById(R.id.radioOnline)
        couponLayout = findViewById(R.id.couponLayout)
        relDate = findViewById(R.id.relDate)

    }
}
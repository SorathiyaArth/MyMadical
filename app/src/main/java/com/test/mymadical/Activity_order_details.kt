package com.test.mymadical

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidadvance.topsnackbar.TSnackbar
import com.razorpay.Payment
import com.razorpay.RazorpayClient
import com.razorpay.Refund
import com.test.mymadical.Interface.Adepter.AdepterOrderDetails
import com.test.mymadical.Interface.GetRefundedInterface
import com.test.mymadical.Interface.OrderDetailsInterface
import com.test.mymadical.Utils.Utils
import com.test.mymadical.model.ModelAddEditAddressInfo
import com.test.mymadical.model.ModelOrederDetails
import com.test.mymadical.model.ProductDetailsItem
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class Activity_order_details : AppCompatActivity() {
    lateinit var txtOrderId: TextView
    lateinit var txtDate: TextView
    lateinit var txtDelDate: TextView
    lateinit var txtStatus: TextView
    lateinit var txtrefund: TextView
    lateinit var txtPayStatusViaUPI: TextView
    lateinit var txtPayStatusViaCard_no: TextView
    lateinit var txtPayStatusViaCard_type: TextView
    lateinit var txtPayStatusViaCard_network: TextView
    lateinit var txtPayStatus: TextView
    lateinit var txtName1: TextView
    lateinit var txtAddress1: TextView
    lateinit var txtMobile1: TextView
    lateinit var txtNetAmount: TextView
    lateinit var txtCGst: TextView
    lateinit var txtSGst: TextView
    lateinit var txtTotalGst: TextView
    lateinit var txtFinalMrp: TextView
    lateinit var txtPayStatusVia: TextView
    lateinit var recyclerView: RecyclerView
    lateinit var llMain: LinearLayout
    lateinit var card_Payment_detais: CardView
    lateinit var relative_Card: RelativeLayout
    lateinit var relative_Upi: RelativeLayout
    var isrefunded: Boolean = false;
    var totalbill: Int = 0;

    var PaymentId: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_details)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()?.setDisplayShowHomeEnabled(true)
        ids()
        recyclerView.layoutManager = LinearLayoutManager(this)
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        recyclerView.setLayoutManager(linearLayoutManager)
        orderdetails()
    }

    private fun orderdetails() {
        val progress = LayoutInflater.from(this)
            .inflate(R.layout.custom_progress_dialog, null)


        val builder = AlertDialog.Builder(this)
            .setView(progress)
        val AlertDialog = builder.create()
        AlertDialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        AlertDialog.setCancelable(false) // disable dismiss by tapping outside of the dialog

        AlertDialog.show()
        val orderId = intent.getStringExtra("orderid")
        val creation: OrderDetailsInterface =
            Retroclient.getSingleTonClient()!!.create(OrderDetailsInterface::class.java)
        val call = creation.getorderdetails(orderId)
        call.enqueue(object : Callback<ModelOrederDetails> {

            @SuppressLint("SetTextI18n")
            override fun onResponse(
                call: Call<ModelOrederDetails>,
                response: Response<ModelOrederDetails>
            ) {
                AlertDialog.dismiss()

                if (response.isSuccessful) {
                    txtOrderId.text = "Order Id " + orderId
                    llMain.visibility = View.VISIBLE
                    val orderaddress = response.body()?.address
                    txtName1.text = orderaddress?.name.toString()
                    txtMobile1.text = orderaddress?.number.toString()
                    txtAddress1.text =
                        orderaddress?.address.toString() + "," + orderaddress?.area.toString() +
                                "," + orderaddress?.city.toString() + "," + orderaddress?.state.toString() +
                                "-" + orderaddress?.pincode.toString()
                    txtDate.text = response.body()?.orderDate
                    txtDelDate.text = response.body()?.delivaryDate

                    txtStatus.text = response.body()?.status
                    txtPayStatus.text = response.body()?.paymentStatus


                    if (response.body()?.status == "PANDIND") {
                        txtStatus.setTextColor(Color.parseColor("#FF8400"))
                    } else if (response.body()?.status == "Confirmed") {
                        txtStatus.setTextColor(Color.parseColor("#5398ec"))
                    } else if (response.body()?.status == "Canceled") {
                        txtStatus.setTextColor(Color.parseColor("#E85342"))
                    } else if (response.body()?.status == "On Delivery") {
                        txtStatus.setTextColor(Color.parseColor("#448FEA"))
                    } else if (response.body()?.status == "Delivered") {
                        txtStatus.setTextColor(Color.parseColor("#33AE10"))
                    }

                    if (response.body()?.paymentStatus == "PANDIND") {
                        txtPayStatus.setTextColor(Color.parseColor("#FF8400"))
                    } else if (response.body()?.paymentStatus == "SCCESSFULL") {
                        txtPayStatus.setTextColor(Color.parseColor("#33AE10"))
                    }



                    totalbill = response.body()?.valueTotal.toString().toInt()
                    txtNetAmount.text = "₹" + response.body()?.valueTotal
                    txtFinalMrp.text = "₹" + response.body()?.valueTotal
                    val mAdepter =
                        AdepterOrderDetails(
                            response.body()!!.productDetails as ArrayList<ProductDetailsItem>,
                            baseContext
                        )
                    recyclerView.adapter = mAdepter

                    PaymentId = response.body()?.tran_id.toString()
                    if (!PaymentId.equals("COD")) {
                        card_Payment_detais.visibility = View.VISIBLE
                        val job = SendfeedbackJob()
                        val p = job.execute(PaymentId).get()
                        if (p.get<Boolean?>("refund_status").equals("null")) {
                            isrefunded = true
                        } else {
                            txtrefund.text = "Refunded"
                        }

                        Log.e("asd", "p   " + p)
                        txtPayStatusVia.text = p.get("method")
                        if (p.get<Boolean?>("method").equals("upi")) {
                            relative_Upi.visibility = View.VISIBLE
                            txtPayStatusViaUPI.text = p.get("vpa")


                        } else if (p.get<Boolean?>("method").equals("card")) {
                            relative_Card.visibility = View.VISIBLE
                            val qwe = p.get<org.json.JSONObject>("card")

                            txtPayStatusViaCard_type.text = qwe.get("type") as CharSequence?
                            txtPayStatusViaCard_no.text = "**** **** **** " + qwe.get("last4")
                            txtPayStatusViaCard_network.text = qwe.get("network") as CharSequence?


                        }


                    }


                }

            }

            override fun onFailure(call: Call<ModelOrederDetails>, t: Throwable) {
                AlertDialog.dismiss()

                Log.e("onfailorderdetails", t.message.toString())

            }
        })
    }


    class SendfeedbackJob :
        AsyncTask<String?, Void?, Payment>() {
        override fun doInBackground(params: Array<String?>): Payment {

            val razorpay = RazorpayClient("rzp_test_BRTY8CDXbNVQS9", "B0CwCJucOUANbDwQlhxdPCiH")
            val payment: Payment = razorpay.payments.fetch(params[0])

            return payment
        }

        override fun onPostExecute(message: Payment) {
            super.onPostExecute(message)
        }


    }

    private fun ids() {
        txtOrderId = findViewById(R.id.txtOrderId)
        txtDate = findViewById(R.id.txtDate)
        txtDelDate = findViewById(R.id.txtDelDate)
        txtDelDate = findViewById(R.id.txtDelDate)
        txtStatus = findViewById(R.id.txtStatus)
        txtPayStatus = findViewById(R.id.txtPayStatus)
        txtPayStatus = findViewById(R.id.txtPayStatus)
        txtName1 = findViewById(R.id.txtName1)
        txtAddress1 = findViewById(R.id.txtAddress1)
        txtMobile1 = findViewById(R.id.txtMobile1)
        txtNetAmount = findViewById(R.id.txtNetAmount)
        txtCGst = findViewById(R.id.txtCGst)
        txtSGst = findViewById(R.id.txtSGst)
        txtTotalGst = findViewById(R.id.txtTotalGst)
        txtFinalMrp = findViewById(R.id.txtFinalMrp)
        recyclerView = findViewById(R.id.recyclerView)
        llMain = findViewById(R.id.llMain)
        txtPayStatusVia = findViewById(R.id.txtPayStatusVia)
        relative_Upi = findViewById(R.id.relative_Upi)
        relative_Card = findViewById(R.id.relative_Card)
        txtPayStatusViaCard_network = findViewById(R.id.txtPayStatusViaCard_network)
        txtPayStatusViaCard_type = findViewById(R.id.txtPayStatusViaCard_type)
        txtPayStatusViaCard_no = findViewById(R.id.txtPayStatusViaCard_no)
        txtPayStatusViaUPI = findViewById(R.id.txtPayStatusViaUPI)
        card_Payment_detais = findViewById(R.id.card_Payment_detais)
        txtrefund = findViewById(R.id.txtrefund)


        txtrefund.setOnClickListener {
            if (isrefunded) {


                if (Utils().isNetworkAvailable(this)) {
                    refundapicall()
                } else {
                    Utils().showToastShortForNoInternet(this)
                    val snackbar: TSnackbar = TSnackbar
                        .make(
                            txtName1,
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
        }

    }

    private fun refundapicall() {

        val job = Refund()
        val p = job.execute(PaymentId, totalbill.toString()).get()

        Log.e("asd", "refund    " + p)



        val progress = LayoutInflater.from(this)
            .inflate(R.layout.custom_progress_dialog, null)


        val builder = AlertDialog.Builder(this)
            .setView(progress)
        val AlertDialog = builder.create()
        AlertDialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        AlertDialog.setCancelable(false) // disable dismiss by tapping outside of the dialog

        AlertDialog.show()


        val orderId = intent.getStringExtra("orderid")
        val creation: GetRefundedInterface =
            Retroclient.getSingleTonClient()!!.create(GetRefundedInterface::class.java)
        val call = creation.getrefund(orderId!!)

        call.enqueue(object : Callback<ModelAddEditAddressInfo> {
            override fun onResponse(
                call: Call<ModelAddEditAddressInfo>,
                response: Response<ModelAddEditAddressInfo>
            ) {
                AlertDialog.dismiss()

                isrefunded = false
                txtrefund.text = "Refunded"
                finish()
            }

            override fun onFailure(call: Call<ModelAddEditAddressInfo>, t: Throwable) {

                AlertDialog.dismiss()

            }
        })


    }


    class Refund :
        AsyncTask<String?, Void?, com.razorpay.Refund?>() {
        override fun doInBackground(params: Array<String?>): com.razorpay.Refund? {
            val refundRequest = JSONObject()
            val amount = params[1].toString().toInt() * 100
            refundRequest.put("amount", amount)
            refundRequest.put("currency", "INR")
            val notes = JSONObject()
            notes.put("notes_key_1", "Tea, Earl Grey, Hot")
            notes.put("notes_key_2", "Tea, Earl Grey… decaf.")
            refundRequest.put("notes", notes)
            refundRequest.put("receipt", "Receipt No. #31")

            val razorpay = RazorpayClient("rzp_test_BRTY8CDXbNVQS9", "B0CwCJucOUANbDwQlhxdPCiH")
            val payment: com.razorpay.Refund? = razorpay.payments.refund(params[0], refundRequest)

            return payment
        }

        override fun onPostExecute(message: com.razorpay.Refund?) {
            super.onPostExecute(message)
        }


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
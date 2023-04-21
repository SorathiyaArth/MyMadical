package com.test.mymadical

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.razorpay.Payment
import com.razorpay.RazorpayClient
import com.test.mymadical.Interface.Adepter.AdepterOrderDetails
import com.test.mymadical.Interface.OrderDetailsInterface
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





                    txtNetAmount.text = "₹" + response.body()?.valueTotal
                    txtFinalMrp.text = "₹" + response.body()?.valueTotal
                    val mAdepter =
                        AdepterOrderDetails(
                            response.body()!!.productDetails as ArrayList<ProductDetailsItem>,
                            baseContext
                        )
                    recyclerView.adapter = mAdepter

                    val paymentId = response.body()?.tran_id
                    if (!paymentId.equals("COD")) {
                        val job = SendfeedbackJob()
                    val p =    job.execute(paymentId).get()
Log.e("asd","p   "+p)
                        txtPayStatusVia.text = p.get("method")
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
package com.test.mymadical

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.get
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidadvance.topsnackbar.TSnackbar
import com.test.mymadical.Adepter.AdepterMyAddress
import com.test.mymadical.Interface.ClickInterface
import com.test.mymadical.Interface.DeleteAddressInfoInterface
import com.test.mymadical.Interface.GetAddressInfoInterface
import com.test.mymadical.Utils.Utils
import com.test.mymadical.model.AddressTblItem
import com.test.mymadical.model.ModelAddEditAddressInfo
import com.test.mymadical.model.ModelAddressDisplayInfo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ActivityMyAddress : AppCompatActivity() {
var total :String = ""
    var sherdiscustid = ""
    val main_key = "my_pref"
    val custID_key = "custID_key"
    lateinit var recyclerviewMyAddresses:RecyclerView
    lateinit var rlNoAddress:RelativeLayout
    lateinit var txtAddAddress:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_address)



        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()?.setDisplayShowHomeEnabled(true)



        txtAddAddress = findViewById(R.id.txtAddAddress)
        rlNoAddress = findViewById(R.id.rlNoAddress)
        recyclerviewMyAddresses = findViewById(R.id.recyclerviewMyAddresses)

        recyclerviewMyAddresses.layoutManager = LinearLayoutManager(this ,LinearLayoutManager.VERTICAL , false)
        val preferences: SharedPreferences = this.getSharedPreferences(main_key, MODE_PRIVATE)
        sherdiscustid = preferences.getString(custID_key, "").toString()
        total = intent.getStringExtra("total").toString()

        txtAddAddress.setOnClickListener {
            val intent = Intent(this@ActivityMyAddress, ActivityAddEditAddress::class.java)
            startActivity(intent)
        }




        if (Utils().isNetworkAvailable(this)){
           getaddress()
       }
        else{
           Utils().showToastShortForNoInternet(this)
           val snackbar: TSnackbar = TSnackbar
               .make(
                   recyclerviewMyAddresses,
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
    private fun getaddress() {
        val progress = LayoutInflater.from(this)
            .inflate(R.layout.custom_progress_dialog, null)


        val builder = AlertDialog.Builder(this)
            .setView(progress)
        val AlertDialog = builder.create()
        AlertDialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        AlertDialog.setCancelable(false) // disable dismiss by tapping outside of the dialog

        AlertDialog.show()

        val creation: GetAddressInfoInterface =
            Retroclient.getSingleTonClient()!!.create(GetAddressInfoInterface::class.java)
        val call = creation.getaddressdetaildata(sherdiscustid)
        call.enqueue(object : Callback<ModelAddressDisplayInfo> {
            override fun onResponse(call: Call<ModelAddressDisplayInfo>, response: Response<ModelAddressDisplayInfo>) {

                val listaddress = response.body()?.addressTbl
                AlertDialog.dismiss()

                if (listaddress != null) {
                    if (response.isSuccessful) {
                    }

                    val myaddressadepter: AdepterMyAddress

                    myaddressadepter =
                        AdepterMyAddress(listaddress as ArrayList<AddressTblItem>, baseContext)
                    recyclerviewMyAddresses.adapter = myaddressadepter




                    myaddressadepter.setOnItemClickListener(object : ClickInterface {
                        override fun onclicked12(position: Int, Types: Int) {
                            if (Types == 1) {
                                optionmenu(listaddress, position)

                            }
                            if (Types == 2) {
                                val fromAccount = intent.getStringExtra("fromAccount")
                                if (fromAccount==null) {
                                    val address = listaddress[position]


                                    val address1 = address?.address
                                    val area = address?.area
                                    val city = address?.city
                                    val state = address?.state
                                    val pincode = address?.pincode
                                    val tvaddress =
                                        address1 + "," + area + ",\n" + city + "," + state + "-" + pincode
                                    Log.e("PSKSKKS", tvaddress)
                                    val intent =
                                        Intent(this@ActivityMyAddress, ActivityOrderReview::class.java)
                                    intent.putExtra("CustId",sherdiscustid )
                                    intent.putExtra("addid", listaddress.get(position)?.addressId)
                                    intent.putExtra("name", listaddress.get(position)?.name)
                                    intent.putExtra("content", listaddress.get(position)?.number)
                                    intent.putExtra("address", tvaddress)
                                    intent.putExtra("total", total)

                                    startActivity(intent)
                                }

                            }
                        }

                    })
                }else{
                    rlNoAddress.visibility = View.VISIBLE
                }
            }

            override fun onFailure(call: Call<ModelAddressDisplayInfo>, t: Throwable) {
                Toast.makeText(this@ActivityMyAddress, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun optionmenu(listaddress: List<AddressTblItem?>?, position: Int) {
        val popupMenu = PopupMenu(this, recyclerviewMyAddresses[position].findViewById(R.id.imgMore))
        popupMenu.inflate(R.menu.menu_address_editdetele)
        popupMenu.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
            override fun onMenuItemClick(item: MenuItem?): Boolean {
                when (item?.itemId) {
                    R.id.itemdelete -> {
                        val addid = listaddress?.get(position)?.addressId
                        Log.e("dhkbcksd", addid.toString())
                        val creation:DeleteAddressInfoInterface  =
                            Retroclient.getSingleTonClient()!!.create(DeleteAddressInfoInterface::class.java)
                        val call =
                            creation.deleteaddressdetaildata(addid.toString())
                        call.enqueue(object : Callback<ModelAddEditAddressInfo> {
                            override fun onResponse(
                                call: Call<ModelAddEditAddressInfo>,
                                response: Response<ModelAddEditAddressInfo>
                            ) {
                                Toast.makeText(
                                    this@ActivityMyAddress,
                                    response.body()!!.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                                startActivity(getIntent())
                                finish()


                            }

                            override fun onFailure(call: Call<ModelAddEditAddressInfo>, t: Throwable) {
                                Toast.makeText(
                                    this@ActivityMyAddress,
                                    t.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        })
                    }
                    R.id.itemedit -> {

                        val intent = Intent(this@ActivityMyAddress, ActivityAddEditAddress::class.java)
                        intent.putExtra("CustId", sherdiscustid)
                        intent.putExtra("addid", listaddress?.get(position)?.addressId)
                        intent.putExtra("name", listaddress?.get(position)?.name)
                        intent.putExtra("content", listaddress?.get(position)?.number)
                        intent.putExtra("address", listaddress?.get(position)?.address)
                        intent.putExtra("area", listaddress?.get(position)?.area)
                        intent.putExtra("pincode", listaddress?.get(position)?.pincode)
                        intent.putExtra("city", listaddress?.get(position)?.city)
                        intent.putExtra("state", listaddress?.get(position)?.state)
                        startActivity(intent)

                    }

                }
                return false
            }
        })
        popupMenu.show()

    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
package com.test.mymadical

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.helper.widget.MotionEffect
import androidx.core.widget.NestedScrollView
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import com.test.mymadical.Interface.SignupUserInterface
import com.test.mymadical.model.ModelLoginInfo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.TimeUnit


class SignupActivity : AppCompatActivity() {
    lateinit var tvlogin: TextView
    lateinit var tvterms: TextView
    lateinit var etname: TextView
    lateinit var etnumber: TextView
    lateinit var scrolldashboard: NestedScrollView
    lateinit var etrefer: TextView
    lateinit var btnsignup: Button
    lateinit var cbterms: CheckBox
    private var mAuth: FirebaseAuth? = null
    var mCallbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)


        mAuth = FirebaseAuth.getInstance()

        tvlogin = findViewById(R.id.tvlogin)
        etname = findViewById(R.id.etname)
        etnumber = findViewById(R.id.etnumber)
        tvterms = findViewById(R.id.tvterms)
        etrefer = findViewById(R.id.etrefer)
        btnsignup = findViewById(R.id.btnsignup)
        scrolldashboard = findViewById(R.id.scrolldashboard)
        cbterms = findViewById(R.id.cbterms)

        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()?.setDisplayShowHomeEnabled(true)

        tvlogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        tvterms.setOnClickListener {
            try {
                val myIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://mymedicalshop.com/terms-and-conditions"))
                startActivity(myIntent)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(
                    this, "No application can handle this request."
                            + " Please install a webbrowser", Toast.LENGTH_LONG
                ).show()
                e.printStackTrace()
            }
        }




        btnsignup.setOnClickListener {
            if (checkname() && chackmobileno() && chackterms()) {
                Log.e("crc32", "encbtnsignup")

                SignupApi()

            }
        }
    }

    private fun signup() {
        Log.e("crc32", "encsignup")


        val intent1 = Intent(this@SignupActivity, OtpActivity::class.java)
        intent1.putExtra("otp", "0000")
        intent1.putExtra("mobileno", etnumber.text.toString())
        startActivity(intent1)





    }

    private fun SignupApi() {
        val progress = LayoutInflater.from(this)
            .inflate(R.layout.custom_progress_dialog, null)


        val builder = AlertDialog.Builder(this)
            .setView(progress)
        val AlertDialog = builder.create()
        AlertDialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        AlertDialog.setCancelable(false) // disable dismiss by tapping outside of the dialog

        AlertDialog.show()

        val creation: SignupUserInterface =
            Retroclient.getSingleTonClient()!!.create(SignupUserInterface::class.java)

        val call = creation.postuserdetailsdetails(etname.text.toString(), etnumber.text.toString())
        call.enqueue(object : Callback<ModelLoginInfo> {
            override fun onResponse(
                call: Call<ModelLoginInfo>,
                response: Response<ModelLoginInfo>
            ) {
                if (response.isSuccessful) {
                    AlertDialog.dismiss()

                    Log.e("jjjjj4", response.body()?.message.toString())
                    if (response.body()?.flag!!.equals("1")) {
                       // signup()
                        asd()
                        Log.e("crc32", "enc")

                    } else {
                        Toast.makeText(
                            this@SignupActivity,
                            response.body()?.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

            }

            override fun onFailure(call: Call<ModelLoginInfo>, t: Throwable) {
                Log.e("ddddddd", t.message.toString())
                AlertDialog.dismiss()

            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }


    fun checkname(): Boolean {


        var isstudentnamevalid = false
        if (etname.text.toString().trim().length <= 0) {
            etname.error = "Enter Name"
        } else {
            isstudentnamevalid = true
        }
        return isstudentnamevalid
    }

    fun chackmobileno(): Boolean {


        var isstudentmobileno = false
        if (etnumber.text.toString().trim().length <= 0) {
            etnumber.error = "Enter Mobile Number"
        } else if (etnumber.text.toString().trim().length == 10) {
            isstudentmobileno = true
        } else {
            etnumber.error = "Enter valid number"
        }
        return isstudentmobileno
    }

    fun chackterms(): Boolean {
        var isstudentmobileno = false

        if (cbterms.isChecked) {
            isstudentmobileno = true
        } else {
            Toast.makeText(this, "Please accept term and condition!", Toast.LENGTH_SHORT).show()
        }
        return isstudentmobileno
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }


    private fun asd() {
        mCallbacks = object : OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                Log.d(MotionEffect.TAG, "onVerificationCompleted:$credential")
                val code = credential.smsCode
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Log.w(MotionEffect.TAG, "onVerificationFailed", e)
                if (e is FirebaseAuthInvalidCredentialsException) {
                } else if (e is FirebaseTooManyRequestsException) {
                }
            }

            override fun onCodeSent(
                verificationId: String,
                token: ForceResendingToken
            ) {
                var verificationId = verificationId
                 Log.d(MotionEffect.TAG, "onCodeSent:$verificationId")
                val intent = Intent(this@SignupActivity, OtpActivity::class.java)
                val mBundle = Bundle()
                mBundle.putParcelable("key", token)
                mBundle.putString("verificationId", verificationId)
                mBundle.putString("phonenumber", etnumber.getText().toString())
                intent.putExtras(mBundle)
                startActivity(intent)
                verificationId = verificationId
            }
        }
        val options = PhoneAuthOptions.newBuilder(mAuth!!)
            .setPhoneNumber("+91" + etnumber.getText().toString()) // Phone number to verify
            .setTimeout(120L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this@SignupActivity) // Activity (for callback binding)
            .setCallbacks(mCallbacks as OnVerificationStateChangedCallbacks) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

}
package com.test.mymadical

import `in`.aabhasjindal.otptextview.OtpTextView
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.helper.widget.MotionEffect
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*


import com.test.mymadical.Interface.GetUserInfoInterface
import com.test.mymadical.model.ModelgetUserDetailsInfo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.TimeUnit

class OtpActivity : AppCompatActivity() {
    lateinit var otp_view: OtpTextView
    lateinit var btnverify: Button
    lateinit var tvsingup: TextView
    lateinit var tvDashBoard: TextView
    lateinit var txt_resend: TextView
    var fromloginotp: String = ""
    var mobileno: String = ""
    var verificationId: String = ""
    private var mAuth: FirebaseAuth? = null
var resendclick:Boolean = false
   var token: PhoneAuthProvider.ForceResendingToken? = null
    var mCallbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)
        otp_view = findViewById(R.id.otp_view)
        btnverify = findViewById(R.id.btnverify)
        tvsingup = findViewById(R.id.tvsingup)
        tvDashBoard = findViewById(R.id.tvDashBoard)
        txt_resend = findViewById(R.id.txt_resend)

        mAuth = FirebaseAuth.getInstance()

        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()?.setDisplayShowHomeEnabled(true)
        fromloginotp = intent.getStringExtra("otp").toString()
        mobileno = intent.getStringExtra("phonenumber").toString()
        verificationId = intent.getStringExtra("verificationId")!!
        token = intent.getParcelableExtra("key")
         object : CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val dsf = millisUntilFinished / 1000
                val text1 = "<font color=#000000>Resend code in </font>"
                val text = "<font color=#000000>s</font>"
                val qwe = text1 + dsf + text
                txt_resend.text = Html.fromHtml(qwe)
            }

            override fun onFinish() {
                val resrnd = "<font color=#000000>Click to resend</font>"
                txt_resend.text = Html.fromHtml(resrnd)
                resendclick = true
            }
        }.start()

        tvDashBoard.setOnClickListener {
            val intent = Intent(this, Dashboard::class.java)
            startActivity(intent)
        }
        txt_resend.setOnClickListener {
            if (resendclick){
                resendclick = false
                token?.let { it1 -> resendVerificationCode("", it1) }
            }
        }


        tvsingup.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }



            btnverify.setOnClickListener {
                val Otpfromedt = otp_view.otp
                if (Otpfromedt!!.length == 6) {
                    val otpbyuser: Int = Otpfromedt!!.toInt()
                    verifyCode(otpbyuser.toString())
                   // ApiCall()
                }else{
                    Toast.makeText(this@OtpActivity, "Please Enter OTP", Toast.LENGTH_SHORT).show()

                }


            }




    }

    private fun resendVerificationCode(phoneNumber: String, token: PhoneAuthProvider.ForceResendingToken) {
        mCallbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                Log.d(MotionEffect.TAG, "onVerificationCompleted:$credential")
                val code = credential.smsCode
                val intent = Intent(this@OtpActivity, OtpActivity::class.java)
                intent.putExtra("code", code)
                startActivity(intent)
                Log.e("zsdf", "sdf  $code")
                Log.e("zsdf", "asd ")
                verifyCode(code!!)
                //  signInWithPhoneAuthCredential(credential);
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Log.w(MotionEffect.TAG, "onVerificationFailed", e)
                if (e is FirebaseAuthInvalidCredentialsException) {
                } else if (e is FirebaseTooManyRequestsException) {
                }
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                var verificationId = verificationId
                Log.d(MotionEffect.TAG, "onCodeSent:$verificationId")
                val intent = Intent(this@OtpActivity, OtpActivity::class.java)
                val mBundle = Bundle()
                mBundle.putParcelable("key", token)
                mBundle.putString("verificationId", verificationId)

                mBundle.putString("phonenumber", mobileno)
                intent.putExtras(mBundle)
                startActivity(intent)
                // Save verification ID and resending token so we can use them later
                verificationId = verificationId
                // mResendToken = token;
            }
        }
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            "+917600898872",  // Phone number to verify
            60,  // Timeout duration
            TimeUnit.SECONDS,  // Unit of timeout
            this,  // Activity (for callback binding)
            mCallbacks as PhoneAuthProvider.OnVerificationStateChangedCallbacks,  // OnVerificationStateChangedCallbacks
            token
        ) // ForceResendingToken from callbacks
    }


    private fun verifyCode(code: String) {
        val credential = PhoneAuthProvider.getCredential(verificationId, code)
        signInWithPhoneAuthCredential(credential)

    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        mAuth!!.signInWithCredential(credential)
            .addOnCompleteListener(this,
                OnCompleteListener<AuthResult?> { task ->
                    if (task.isSuccessful) {
                        ApiCall()
                    } else {
                        otp_view.showError()
                        otp_view.setOTP("")
                        Toast.makeText(this@OtpActivity, "Wrong OTP", Toast.LENGTH_SHORT).show()

                    }
                })
    }

    private fun ApiCall() {
        val progress = LayoutInflater.from(this)
            .inflate(R.layout.custom_progress_dialog, null)


        val builder = AlertDialog.Builder(this)
            .setView(progress)
        val AlertDialog = builder.create()
        AlertDialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        AlertDialog.setCancelable(false) // disable dismiss by tapping outside of the dialog

        AlertDialog.show()
        val creation: GetUserInfoInterface =
            Retroclient.getSingleTonClient()!!.create(GetUserInfoInterface::class.java)
        val call = creation.getprodetaildata(mobileno)
        call.enqueue(object : Callback<ModelgetUserDetailsInfo>{
            override fun onResponse(
                call: Call<ModelgetUserDetailsInfo>,
                response: Response<ModelgetUserDetailsInfo>
            ) {
                if (response.isSuccessful) {

                    AlertDialog.dismiss()
                    val userdata = response.body()?.userDetailsMymedical

                    val name = userdata?.get(0)?.userName
                    val number = userdata?.get(0)?.userMobileno
                    val email = userdata?.get(0)?.userEmail
                    val image = userdata?.get(0)?.userImg
                    val userid = userdata?.get(0)?.userId
                    val cartitemtotal = userdata?.get(0)?.cart_total_item
                    Log.e("jbhhfg", image.toString())

                    com.test.mymadical.Utils.Utils().Logindata(this@OtpActivity, "is_login", "1")
                    com.test.mymadical.Utils.Utils()
                        .Logindata(this@OtpActivity, "uname", name.toString())
                    com.test.mymadical.Utils.Utils()
                        .Logindata(this@OtpActivity, "cartitemtotal", cartitemtotal.toString())
                    com.test.mymadical.Utils.Utils()
                        .Logindata(this@OtpActivity, "custID_key", userid.toString())
                    com.test.mymadical.Utils.Utils()
                        .Logindata(this@OtpActivity, "uemail", email.toString())
                    if (image !="") {
                        com.test.mymadical.Utils.Utils()
                            .Logindata(this@OtpActivity, "uimage", image.toString())
                        Log.e("dsddddsd",image.toString())
                    }
                    com.test.mymadical.Utils.Utils()
                        .Logindata(this@OtpActivity, "unumber", number.toString())
                    val intent =Intent(this@OtpActivity ,Dashboard::class.java)
                    startActivity(intent)
                    finishAffinity()

                }



            }

            override fun onFailure(call: Call<ModelgetUserDetailsInfo>, t: Throwable) {
                Toast.makeText(this@OtpActivity, t.message, Toast.LENGTH_SHORT).show()

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
package com.test.mymadical

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.helper.widget.MotionEffect


import com.test.mymadical.Interface.GetUserLoginInterface
import com.test.mymadical.model.ModelLoginInfo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.TimeUnit

class LoginActivity : AppCompatActivity() {
    lateinit var tvsingup: TextView
    lateinit var etnumber: EditText
    lateinit var btnlogin: Button
  //  private var mAuth: FirebaseAuth? = null
    //var mCallbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        tvsingup = findViewById(R.id.tvsingup)
        etnumber = findViewById(R.id.etnumber)
        btnlogin = findViewById(R.id.btnlogin)

//        mAuth = FirebaseAuth.getInstance()

        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()?.setDisplayShowHomeEnabled(true)


        tvsingup.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }

        btnlogin.setOnClickListener {


            if (chackstudenmobileno()) {
                val inputManager: InputMethodManager =
                    this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputManager.hideSoftInputFromWindow(
                    this.currentFocus!!.windowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS
                )
                ApiCall()


            }


        }


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
        val mobileno = etnumber.text.toString()

        val creation: GetUserLoginInterface =
            Retroclient.getSingleTonClient()!!.create(GetUserLoginInterface::class.java)
        val call = creation.getlogindata(mobileno)
        call.enqueue(object : Callback<ModelLoginInfo> {
            override fun onResponse(
                call: Call<ModelLoginInfo>,
                response: Response<ModelLoginInfo>
            ) {
                if (response.isSuccessful) {
                    AlertDialog.dismiss()
                    if (response.body()?.flag == "1") {
//asd()


                        val otpPIN = (Math.random() * 9000).toInt() + 1000

                        val intent = Intent(this@LoginActivity, OtpActivity::class.java)
                        intent.putExtra("otp", otpPIN.toString())
                        intent.putExtra("mobileno", etnumber.text.toString())
                        startActivity(intent)
                    } else {
                        Toast.makeText(
                            this@LoginActivity,
                            response.body()?.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            override fun onFailure(call: Call<ModelLoginInfo>, t: Throwable) {
                Toast.makeText(
                    this@LoginActivity,
                    t.message,
                    Toast.LENGTH_SHORT
                ).show()

            }
        })


    }
  /*  private fun asd() {
        mCallbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
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
                token: PhoneAuthProvider.ForceResendingToken
            ) {

                var verificationId = verificationId
                Log.d(MotionEffect.TAG, "onCodeSent:$verificationId")
                val intent = Intent(this@LoginActivity, OtpActivity::class.java)
                val mBundle = Bundle()
                mBundle.putParcelable("key", token)
                mBundle.putString("verificationId", verificationId)
                    mBundle.putString("phonenumber", etnumber.text.toString())
                intent.putExtras(mBundle)
                startActivity(intent)

            }
        }
        val options = PhoneAuthOptions.newBuilder(mAuth!!)
                .setPhoneNumber("+91" + etnumber.text.toString()) // Phone number to verify
                .setTimeout(120L, TimeUnit.SECONDS) // Timeout and unit
                .setActivity(this@LoginActivity) // Activity (for callback binding)
                .setCallbacks(mCallbacks as PhoneAuthProvider.OnVerificationStateChangedCallbacks) // OnVerificationStateChangedCallbacks
                .build()

        if (options != null) {
            PhoneAuthProvider.verifyPhoneNumber(options)
        }
    }

*/
    fun chackstudenmobileno(): Boolean {


        var isstudentmobileno = false
        if (etnumber.text.toString().trim().length <= 0) {
            etnumber.error = "Enter Mobile Nomber"
        } else if (etnumber.text.toString().trim().length == 10) {
            isstudentmobileno = true
        } else {
            etnumber.setError("Enter valid number")
        }
        return isstudentmobileno
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
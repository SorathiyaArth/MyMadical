package com.test.mymadical.Utils

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast

import com.test.mymadical.R

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

class Utils {
    fun isNetworkAvailable(activity: Context): Boolean {
        val connectivity =
            activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivity == null) {
            return false
        } else {
            val info = connectivity.allNetworkInfo
            if (info != null) {
                for (anInfo in info) {
                    if (anInfo.state == NetworkInfo.State.CONNECTED) {
                        return true
                    }
                }
            }
        }
        return false
    }

    fun showToastShortForNoInternet(_activity: Activity?) {
        if (_activity != null) {
            val mylf = _activity.layoutInflater
            val myview: View = mylf.inflate(R.layout.toast_layout, null)
            val text_data = myview.findViewById<TextView>(R.id.toast_text)
            text_data.text = "Please Check your Internet Connection"
            val mytoast = Toast(_activity)
            mytoast.duration = Toast.LENGTH_SHORT
            mytoast.setView(myview)
            if (!text_data.text.toString().isEmpty()) {
                mytoast.show()
            }
        }
    }

    fun showToastShortForDrone(_activity: Activity?) {
        if (_activity != null) {
            val mylf = _activity.layoutInflater
            val myview: View = mylf.inflate(R.layout.toast_layout, null)
            val text_data = myview.findViewById<TextView>(R.id.toast_text)
            text_data.text = "Use Drone To Fast Delivery"
            val mytoast = Toast(_activity)
            mytoast.duration = Toast.LENGTH_SHORT
            mytoast.setView(myview)
            mytoast.setGravity(Gravity.TOP or Gravity.LEFT, 420, 500)
            if (!text_data.text.toString().isEmpty()) {
                mytoast.show()
            }
        }
    }

    fun showToastShortselectcity(_activity: Activity?) {

        if (_activity != null) {
            val mylf = _activity.layoutInflater
            val myview: View = mylf.inflate(R.layout.toast_layout, null)
            val text_data = myview.findViewById<TextView>(R.id.toast_text)
            text_data.text = "Please select your city"
            val mytoast = Toast(_activity)
            mytoast.duration = Toast.LENGTH_SHORT
            mytoast.setView(myview)
            //mytoast.setGravity(Gravity.TOP or Gravity.LEFT, 420, 500)
            if (!text_data.text.toString().isEmpty()) {
                mytoast.show()
            }
        }
    }

    public fun Logindata(_activity: Activity?, key: String, value: String) {
        val main_key = "my_pref"
        val preferences: SharedPreferences = _activity!!.getSharedPreferences(
            main_key, Context.MODE_PRIVATE
        )

        val editor: SharedPreferences.Editor = preferences.edit()
        editor.putString(key, value)
        editor.apply()


    }

    fun islogin(_activity: Activity?): Boolean {
        val main_key = "my_pref"
        val login_key = "is_login"
        val preferences: SharedPreferences = _activity!!.getSharedPreferences(
            main_key,
            Context.MODE_PRIVATE
        )
        val sherdislogin = preferences.getString(login_key, "afd")

        if (sherdislogin == "1") {
            return true

        }
        return false

    }

    fun hideKeyboard(activity: Activity) {
        val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = activity.currentFocus
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }




}
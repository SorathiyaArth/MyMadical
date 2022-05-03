package com.test.mymadical.Adepter

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.test.mymadical.Interface.ClickInterface
import com.test.mymadical.R
import com.test.mymadical.model.OrderTblItem

class AdepterMyOrder(var listorder: List<OrderTblItem>, val context: Context) :
    RecyclerView.Adapter<AdepterMyOrder.ViewHolder>() {
    var mListner: ClickInterface? = null
    fun setOnclickInterface(clickInterface: ClickInterface) {
        mListner = clickInterface
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtOrderId = view.findViewById<TextView>(R.id.txtOrderId)
        val txtDate = view.findViewById<TextView>(R.id.txtDate)
        val txtStatus = view.findViewById<TextView>(R.id.txtStatus)
        val txtQuntityCount = view.findViewById<TextView>(R.id.txtQuntityCount)
        val txtDeliveryDate = view.findViewById<TextView>(R.id.txtDeliveryDate)
        val txtAmount = view.findViewById<TextView>(R.id.txtAmount)
        val txtViewdetails = view.findViewById<TextView>(R.id.txtViewdetails)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val mView = LayoutInflater.from(context).inflate(R.layout.row_myorder, parent, false)
        return ViewHolder(mView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val order = listorder[position]
        val id = order?.orderId
        holder.txtOrderId.text = "Order ID : " + id
        val date = order?.orderDate
        holder.txtDate.text = date
        val quntity = order?.totalItem
        holder.txtQuntityCount.text = quntity
        val deldate = order?.delivaryDate
        holder.txtDeliveryDate.text = deldate
        val amount = order?.valueTotal
        holder.txtAmount.text ="₹"+ amount
        val status = order?.status
        holder.txtStatus.text = status
        if (status == "PANDIND") {
            holder.txtStatus.setTextColor(Color.parseColor("#FF8400"))
        } else if (status == "Confirmed") {
            holder.txtStatus.setTextColor(Color.parseColor("#5398ec"))
        } else if (status == "Canceled") {
            holder.txtStatus.setTextColor(Color.parseColor("#E85342"))
        } else if (status == "On Delivery") {
            holder.txtStatus.setTextColor(Color.parseColor("#448FEA"))
        } else if (status == "Delivered") {
            holder.txtStatus.setTextColor(Color.parseColor("#33AE10"))
        }
        holder.txtViewdetails.setOnClickListener {
            mListner?.onclicked12(holder.adapterPosition, 1)
        }

    }

    override fun getItemCount(): Int {
        Log.d("DFGSVDFG", listorder.size.toString() + " hi121i")

        return listorder.size
    }
}
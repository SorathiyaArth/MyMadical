package com.test.mymadical.Interface.Adepter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.test.mymadical.Interface.ClickInterface
import com.test.mymadical.R
import com.test.mymadical.model.AddressTblItem

class AdepterMyAddress(var listaddress: ArrayList<AddressTblItem>, var context: Context) :
    RecyclerView.Adapter<AdepterMyAddress.ViewHolder>() {
    var mListener: ClickInterface? = null
    fun setOnItemClickListener(clickListener: ClickInterface) {
        mListener = clickListener
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtName = view.findViewById<TextView>(R.id.txtName)
        val txtNumber = view.findViewById<TextView>(R.id.txtNumber)
        val txtAddress = view.findViewById<TextView>(R.id.txtAddress)
        val imgMore = view.findViewById<ImageView>(R.id.imgMore)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val mview = LayoutInflater.from(context).inflate(R.layout.raw_my_addresses, parent, false)
        return ViewHolder(mview)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val address = listaddress[position]
        holder.txtName.text = address.name
        holder.txtNumber.text = address.number
        holder.txtAddress.text =
            address.address + "," + address.area + "," + address.city + "," + address.state + "-" + address.pincode
        holder.imgMore.setOnClickListener {
            mListener?.onclicked12(holder.adapterPosition, 1)
        }
        holder.itemView.setOnClickListener {
            mListener?.onclicked12(holder.adapterPosition, 2)
        }
    }


    override fun getItemCount(): Int {
        return listaddress.size
    }
}
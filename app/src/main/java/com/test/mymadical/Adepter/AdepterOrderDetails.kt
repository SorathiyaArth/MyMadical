package com.test.mymadical.Adepter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.test.mymadical.R
import com.test.mymadical.model.CartTblItem
import com.test.mymadical.model.ProductDetailsItem

class AdepterOrderDetails(var listproducts: ArrayList<ProductDetailsItem>, var context: Context) :
    RecyclerView.Adapter<AdepterOrderDetails.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        context = parent.context
        val mView = LayoutInflater.from(context).inflate(R.layout.row_orderdetails, parent, false)
        return ViewHolder(mView)
    }

    override fun onBindViewHolder(holder: AdepterOrderDetails.ViewHolder, position: Int) {
        val orderdetails = listproducts[position]
        Glide.with(context).load(orderdetails.productImage).error(R.drawable.img_default_user_icon).into(holder.imgProductImage)
        holder.txtProductName.text = orderdetails.productName
        holder.txtUnitPrice.text = "₹" +orderdetails.price
        holder.txtQuntityCount.text = orderdetails.quntity
        val totalptice =   orderdetails.price!!.toInt()*orderdetails.quntity!!.toInt()
        holder.txtTotalPrice.text = "₹"+totalptice
    }

    override fun getItemCount(): Int {
return listproducts.size
    }
    class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        val imgProductImage = view.findViewById<ImageView>(R.id.imgProductImage)
        val txtProductName = view.findViewById<TextView>(R.id.txtProductName)
        val txtUnitPrice = view.findViewById<TextView>(R.id.txtUnitPrice)
        val txtQuntityCount = view.findViewById<TextView>(R.id.txtQuntityCount)
        val txtTotalPrice = view.findViewById<TextView>(R.id.txtTotalPrice)
    }
}
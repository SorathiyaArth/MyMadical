package com.test.mymadical.Adepter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.test.mymadical.Interface.ClickInterface
import com.test.mymadical.R
import com.test.mymadical.model.CartTblItem

class AdepterMyCart(var listcart: ArrayList<CartTblItem>, var context: Context) :
    RecyclerView.Adapter<AdepterMyCart.ViewHolder>() {
    var mListener: ClickInterface? = null

    fun setOnItemClickListener(clickListener: ClickInterface) {
        mListener = clickListener
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivcartimage = view.findViewById<ImageView>(R.id.ivcartimage)
        val delete = view.findViewById<ImageView>(R.id.delete)
        val tvminus = view.findViewById<ImageView>(R.id.tvminus)
        val tvplus = view.findViewById<ImageView>(R.id.tvplus)
        val tvcartname = view.findViewById<TextView>(R.id.tvcartname)
        val tvcartprice = view.findViewById<TextView>(R.id.tvcartprice)
        val tvcarttmrp = view.findViewById<TextView>(R.id.tvcarttmrp)
        val tvquantity = view.findViewById<TextView>(R.id.tvquantity)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val mView = LayoutInflater.from(context).inflate(R.layout.raw_mycart, parent, false)
        return ViewHolder(mView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cartitem = listcart[position]
        Glide.with(context).load(cartitem.productImage).into(holder.ivcartimage)
        holder.tvcartname.text = cartitem.productName
        holder.tvcartprice.text = "₹"+ cartitem.productPrice
        holder.tvcarttmrp.text = "₹"+cartitem.productMrp
        holder.tvquantity.text = cartitem.quantity
        holder.delete.setOnClickListener {
            mListener?.onclicked12(holder.adapterPosition, 1)
        }
        holder.tvplus.setOnClickListener {
            mListener?.onclicked12(holder.adapterPosition, 2)
        }
        holder.tvminus.setOnClickListener {
            mListener?.onclicked12(holder.adapterPosition, 3)
        }


    }

    override fun getItemCount(): Int {
        return listcart.size
    }
}
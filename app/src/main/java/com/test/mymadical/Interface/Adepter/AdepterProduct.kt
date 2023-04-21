package com.test.mymadical.Interface.Adepter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.test.mymadical.Interface.ClickInterface
import com.test.mymadical.R
import com.test.mymadical.model.ProductTblItem

class AdepterProduct(val listproduct: ArrayList<ProductTblItem>, val context: Context) :
    RecyclerView.Adapter<AdepterProduct.ViewHolder>() {

    var mListner:ClickInterface?=null
    fun setOnClickListner(clicklistener:ClickInterface){
        mListner=clicklistener
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val Img_product = view.findViewById<ImageView>(R.id.Img_product)
        val Txt_off = view.findViewById<TextView>(R.id.Txt_off)
        val imgMinus = view.findViewById<ImageView>(R.id.imgMinus)
        val tvquantity = view.findViewById<TextView>(R.id.tvquantity)
        val imgPlus = view.findViewById<ImageView>(R.id.imgPlus)
        val Txt_Product_nam = view.findViewById<TextView>(R.id.Txt_Product_nam)
        val Txt_Product_dis = view.findViewById<TextView>(R.id.Txt_Product_dis)
        val Pro_mrp = view.findViewById<TextView>(R.id.Pro_mrp)
        val Pro_price = view.findViewById<TextView>(R.id.Pro_price)
        val Txt_add = view.findViewById<TextView>(R.id.Txt_add)
        val Rl_Product = view.findViewById<RelativeLayout>(R.id.Rl_Product)
        val RL_Outofstock = view.findViewById<RelativeLayout>(R.id.RL_Outofstock)
        val rladdtocart = view.findViewById<RelativeLayout>(R.id.rladdtocart)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val mview = LayoutInflater.from(context).inflate(R.layout.raw_product, parent, false)
        return ViewHolder(mview)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
   val product = listproduct[position]
        Glide.with(context).load(product.productImage).into(holder.Img_product)
        holder.Txt_off.text = product.productOff
        holder.Txt_Product_nam.text = product.productName
        holder.Txt_Product_dis.text = product.productDis
        holder.Pro_mrp.text = "₹" + product.productMrp
        holder.Pro_price.text ="₹" +  product.productPrice
        holder.rladdtocart.visibility= View.GONE
        holder.Rl_Product.setOnClickListener {
            mListner?.onclicked12(holder.adapterPosition ,3 )
        }
        holder.Txt_add.setOnClickListener {
            mListner?.onclicked12(holder.adapterPosition ,99 )
            holder.Txt_add.visibility =View.GONE
            holder.rladdtocart.visibility= View.VISIBLE

            Log.e("ddddd","dddd")
        }

        val stock = product.productInstock
        if (stock=="No"){
            holder.RL_Outofstock.visibility = View.VISIBLE
            holder.Txt_add.isClickable = false
            holder.Txt_add.visibility = View.GONE
        }
        var quntity: Int = 1
        holder.imgMinus.setOnClickListener {
            mListner?.onclicked12(holder.adapterPosition ,0 )
            if(quntity==1){
                holder.Txt_add.visibility =View.VISIBLE
                holder.rladdtocart.visibility= View.GONE
            }else{
                quntity = quntity-1
                holder.tvquantity.text = quntity.toString()

            }

        }
        holder.imgPlus.setOnClickListener {
            mListner?.onclicked12(holder.adapterPosition ,1 )
            quntity = quntity+1
            holder.tvquantity.text = quntity.toString()

        }




    }

    override fun getItemCount(): Int {
        return listproduct.size
    }
}
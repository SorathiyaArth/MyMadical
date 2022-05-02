package com.test.mymadical.Adepter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.test.mymadical.Interface.ClickInterface
import com.test.mymadical.R

class AdepterFindDoctor(
    val listdoctor: ArrayList<String>,
    val listimage: ArrayList<Int>,
    val context: Context
) : RecyclerView.Adapter<AdepterFindDoctor.ViewHolder>() {


    var mListener: ClickInterface? = null

    fun setOnItemClickListener(clickListener: ClickInterface) {
        mListener = clickListener
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivcatecory = view.findViewById<ImageView>(R.id.ivcatecory)
        val tvcatecory = view.findViewById<TextView>(R.id.tvcatecory)
        val llcategory = view.findViewById<LinearLayout>(R.id.llcategory)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val mView = LayoutInflater.from(context).inflate(R.layout.raw_category,parent,false)
        return ViewHolder(mView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.ivcatecory.setImageResource(listimage[position])
        holder.tvcatecory.text = listdoctor.get(position)

        holder.llcategory.setOnClickListener {
            mListener?.onclicked12(holder.adapterPosition , 1)
        }

    }

    override fun getItemCount(): Int {
        return listdoctor.size
    }

    override fun getItemViewType(position: Int): Int {
        return listdoctor.size

    }
}
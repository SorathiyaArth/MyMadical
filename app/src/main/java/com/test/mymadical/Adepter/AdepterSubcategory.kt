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
import com.test.mymadical.model.CategoryTblItem
import com.test.mymadical.model.SubcategoryTblItem

class AdepterSubcategory (var listsubcategory: List<SubcategoryTblItem>, var context: Context) :
    RecyclerView.Adapter<AdepterSubcategory.ViewHolder>() {

    var mListener: ClickInterface? = null

    fun setOnItemClickListener(clickListener: ClickInterface) {
        mListener = clickListener
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivsubcategory = view.findViewById<ImageView>(R.id.ivsubcategory)
        val tvsubcategory = view.findViewById<TextView>(R.id.tvsubcategory)
        val llsubcategory = view.findViewById<LinearLayout>(R.id.llsubcategory)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context =parent.context
        val mView = LayoutInflater.from(context).inflate(R.layout.raw_subcategory,parent,false)
        return ViewHolder(mView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category =listsubcategory[position]
        val name = category.subcategoryName
        holder.tvsubcategory.text = name
        Glide.with(context).load(category.subcategoryImage).into(holder.ivsubcategory).waitForLayout()
        holder.llsubcategory.setOnClickListener {
            mListener?.onclicked12(holder.adapterPosition , 2)
        }

    }

    override fun getItemCount(): Int {
        return listsubcategory.size
    }
}
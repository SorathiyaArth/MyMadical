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
import com.test.mymadical.model.CategoryInfo
import com.test.mymadical.model.CategoryTblItem

class AdepterCategory(var listcategory: List<CategoryTblItem>, var context: Context) :
    RecyclerView.Adapter<AdepterCategory.ViewHolder>() {
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
        context =parent.context
        val mView = LayoutInflater.from(context).inflate(R.layout.raw_category,parent,false)
        return ViewHolder(mView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category =listcategory[position]
        val name = category.categoryName
        holder.tvcatecory.text = name
        Glide.with(context).load(category.categoryImage).into(holder.ivcatecory).waitForLayout()
        holder.llcategory.setOnClickListener {
        mListener?.onclicked12(holder.adapterPosition , 1)
        }


    }

    override fun getItemCount(): Int {
        return listcategory.size
    }
}
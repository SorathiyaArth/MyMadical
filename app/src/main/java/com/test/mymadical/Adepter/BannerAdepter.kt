package com.test.mymadical.Adepter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.smarteist.autoimageslider.SliderViewAdapter
import com.test.mymadical.R

class BannerAdepter (
    var BannerImage: ArrayList<String>,
    var context: Context
) :
    SliderViewAdapter<BannerAdepter.Sliderviewholder>() {
    override fun getCount(): Int {
        return  BannerImage.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?): Sliderviewholder {
        val mview: View = LayoutInflater.from(context).inflate(R.layout.raw_banner,parent,false)
        return Sliderviewholder(mview)
    }

    override fun onBindViewHolder(viewHolder: Sliderviewholder?, position: Int) {
        val slideimage  = BannerImage[position]

        Glide.with(context).load(slideimage).into(viewHolder!!.ivbanner)
    }

    class Sliderviewholder(view: View): SliderViewAdapter.ViewHolder(view){
        val ivbanner = view.findViewById<ImageView>(R.id.ivbanner)
    }
}

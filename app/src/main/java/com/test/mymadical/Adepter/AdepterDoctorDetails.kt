package com.test.mymadical.Adepter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.test.mymadical.R
import com.test.mymadical.model.DoctorDetailsTblItem
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class AdepterDoctorDetails(val listdoctor: List<DoctorDetailsTblItem>, var context: Context) :
    RecyclerView.Adapter<AdepterDoctorDetails.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtDoctorName = view.findViewById<TextView>(R.id.txtDoctorName)
        val txtDoctorFees = view.findViewById<TextView>(R.id.txtDoctorFees)
        val ratingbardocrtor = view.findViewById<RatingBar>(R.id.ratingbardocrtor)
        val txttotalreview = view.findViewById<TextView>(R.id.txttotalreview)
        val txtDoctorCity = view.findViewById<TextView>(R.id.txtDoctorCity)
        val txtDoctorExperience = view.findViewById<TextView>(R.id.txtDoctorExperience)
        val txtToday = view.findViewById<TextView>(R.id.txtToday)
        val txtNextday = view.findViewById<TextView>(R.id.txtNextday)
        val imgDoc_pic = view.findViewById<ImageView>(R.id.imgDoc_pic)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val mview = LayoutInflater.from(context).inflate(R.layout.row_doc_list, parent, false)
        return ViewHolder(mview)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val doctor = listdoctor[position]
        holder.txtDoctorName.text = doctor.docName
        holder.txtDoctorFees.text = "From: " + "\u20B9" + doctor.docVisitFees
        holder.txttotalreview.text = "("+doctor.docTotalReview + ")"
        holder.txtDoctorCity.text = doctor.docCity
        holder.txtDoctorExperience.text = doctor.docExperience + " Years of Experience"
        val star = doctor.docStar!!.toFloat()
        holder.ratingbardocrtor.setRating(star)
        val calendar: Calendar = Calendar.getInstance()

        calendar.add(Calendar.DAY_OF_YEAR, 1)
        val tomorrow: Date = calendar.getTime()
        val dateFormat: DateFormat = SimpleDateFormat("dd/MM")

        val tomorrowAsString: String = dateFormat.format(tomorrow)
        holder.txtNextday.text = tomorrowAsString
        Glide.with(context).load(doctor.docImg).into(holder.imgDoc_pic)


    }

    override fun getItemCount(): Int {
        return listdoctor.size
    }


}
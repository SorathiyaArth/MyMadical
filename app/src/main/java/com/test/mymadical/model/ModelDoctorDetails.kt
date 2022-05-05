package com.test.mymadical.model

import com.google.gson.annotations.SerializedName

data class ModelDoctorDetails(

	@field:SerializedName("msg")
	val msg: String? = null,

	@field:SerializedName("flag")
	val flag: Int? = null,

	@field:SerializedName("DoctorDetails_tbl")
	val doctorDetailsTbl: List<DoctorDetailsTblItem?>? = null
)

data class DoctorDetailsTblItem(

	@field:SerializedName("doc_city")
	val docCity: String? = null,

	@field:SerializedName("doc_specialist")
	val docSpecialist: String? = null,

	@field:SerializedName("doc_img")
	val docImg: String? = null,

	@field:SerializedName("doc_name")
	val docName: String? = null,

	@field:SerializedName("doc_total_review")
	val docTotalReview: String? = null,

	@field:SerializedName("doc_bio")
	val docBio: String? = null,

	@field:SerializedName("doc_visit_fees")
	val docVisitFees: String? = null,

	@field:SerializedName("doc_university")
	val docUniversity: String? = null,

	@field:SerializedName("doc_experience")
	val docExperience: String? = null,

	@field:SerializedName("doc_total_star")
	val docStar: String? = null,

	@field:SerializedName("doc_id")
	val docId: String? = null,

	@field:SerializedName("doc_time")
	val docTime: String? = null
)

package com.test.mymadical.model

import com.google.gson.annotations.SerializedName

data class ModelgetUserDetailsInfo(

	@field:SerializedName("msg")
	val msg: String? = null,

	@field:SerializedName("flag")
	val flag: Int? = null,

	@field:SerializedName("user_details_mymedical")
	val userDetailsMymedical: List<UserDetailsMymedicalItem?>? = null
)

data class UserDetailsMymedicalItem(

	@field:SerializedName("user_email")
	val userEmail: String? = null,

	@field:SerializedName("user_id")
	val userId: String? = null,

	@field:SerializedName("user_name")
	val userName: String? = null,

	@field:SerializedName("cart_total_item")
	val cart_total_item: String? = null,

	@field:SerializedName("user_mobileno")
	val userMobileno: String? = null,

	@field:SerializedName("user_img")
	val userImg: String? = null
)

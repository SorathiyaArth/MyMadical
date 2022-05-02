package com.test.mymadical.model

import com.google.gson.annotations.SerializedName

data class ModelCartaddInfo(

	@field:SerializedName("flag")
	val flag: String? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("count")
	val count: String? = null
)

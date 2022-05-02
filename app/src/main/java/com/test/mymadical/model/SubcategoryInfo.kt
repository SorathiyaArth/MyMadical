package com.test.mymadical.model

import com.google.gson.annotations.SerializedName

data class SubcategoryInfo(

	@field:SerializedName("msg")
	val msg: String? = null,

	@field:SerializedName("flag")
	val flag: Int? = null,

	@field:SerializedName("subcategory_tbl")
	val subcategoryTbl: List<SubcategoryTblItem?>? = null
)

data class SubcategoryTblItem(

	@field:SerializedName("subcategory_id")
	val subcategoryId: String? = null,

	@field:SerializedName("subcategory_name")
	val subcategoryName: String? = null,

	@field:SerializedName("subcategory_image")
	val subcategoryImage: String? = null,

	@field:SerializedName("subcategory_title")
	val subcategoryTitle: Any? = null
)

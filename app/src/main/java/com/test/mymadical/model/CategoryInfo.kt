package com.test.mymadical.model

import com.google.gson.annotations.SerializedName

data class CategoryInfo(


    @field:SerializedName("category_tbl")
    val categoryTbl: List<CategoryTblItem?>? = null,
	@field:SerializedName("flag")
    val flag: Int? = null,
	@field:SerializedName("msg")
    val msg: String? = null

)

data class CategoryTblItem(

    @field:SerializedName("category_image")
    val categoryImage: String? = null,

    @field:SerializedName("category_name")
    val categoryName: String? = null,

    @field:SerializedName("category_id")
    val categoryId: String? = null
)

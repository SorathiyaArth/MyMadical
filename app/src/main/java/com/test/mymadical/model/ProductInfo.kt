package com.test.mymadical.model

import com.google.gson.annotations.SerializedName

data class ProductInfo(

	@field:SerializedName("msg")
	val msg: String? = null,

	@field:SerializedName("product_tbl")
	val productTbl: List<ProductTblItem?>? = null,

	@field:SerializedName("flag")
	val flag: Int? = null
)

data class ProductTblItem(

	@field:SerializedName("product_dis")
	val productDis: String? = null,

	@field:SerializedName("product_instock")
	val productInstock: String? = null,

	@field:SerializedName("product_off")
	val productOff: String? = null,

	@field:SerializedName("product_image")
	val productImage: String? = null,

	@field:SerializedName("product_id")
	val productId: String? = null,

	@field:SerializedName("product_mrp")
	val productMrp: String? = null,

	@field:SerializedName("product_price")
	val productPrice: String? = null,

	@field:SerializedName("product_name")
	val productName: String? = null
)

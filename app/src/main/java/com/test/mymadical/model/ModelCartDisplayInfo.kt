package com.test.mymadical.model

import com.google.gson.annotations.SerializedName

data class ModelCartDisplayInfo(

	@field:SerializedName("msg")
	val msg: String? = null,

	@field:SerializedName("flag")
	val flag: Int? = null,

	@field:SerializedName("cart_tbl")
	val cartTbl: List<CartTblItem?>? = null,

	@field:SerializedName("CartItem")
	val cartItem: CartItem? = null
)

data class CartItem(

	@field:SerializedName("carttotal")
	val carttotal: Int? = null,

	@field:SerializedName("cartitem")
	val cartitem: Int? = null
)

data class CartTblItem(

	@field:SerializedName("product_dis")
	val productDis: String? = null,

	@field:SerializedName("product_instock")
	val productInstock: String? = null,

	@field:SerializedName("product_off")
	val productOff: String? = null,

	@field:SerializedName("quantity")
	val quantity: String? = null,

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

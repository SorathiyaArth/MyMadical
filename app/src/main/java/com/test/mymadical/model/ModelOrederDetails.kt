package com.test.mymadical.model

import com.google.gson.annotations.SerializedName

data class ModelOrederDetails(

	@field:SerializedName("msg")
	val msg: String? = null,

	@field:SerializedName("order_date")
	val orderDate: String? = null,

	@field:SerializedName("total_item")
	val totalItem: String? = null,

	@field:SerializedName("address")
	val address: Address? = null,

	@field:SerializedName("flag")
	val flag: Int? = null,

	@field:SerializedName("payment_status")
	val paymentStatus: String? = null,

	@field:SerializedName("address_id")
	val addressId: String? = null,

	@field:SerializedName("value_total")
	val valueTotal: String? = null,

	@field:SerializedName("delivary_date")
	val delivaryDate: String? = null,

	@field:SerializedName("product_details")
	val productDetails: List<ProductDetailsItem?>? = null,

	@field:SerializedName("order_id")
	val orderId: String? = null,

	@field:SerializedName("status")
	val status: String? = null,

	@field:SerializedName("tran_id")
	val tran_id: String? = null
)

data class Address(

	@field:SerializedName("area")
	val area: String? = null,

	@field:SerializedName("number")
	val number: String? = null,

	@field:SerializedName("pincode")
	val pincode: String? = null,

	@field:SerializedName("address")
	val address: String? = null,

	@field:SerializedName("city")
	val city: String? = null,

	@field:SerializedName("address_id")
	val addressId: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("state")
	val state: String? = null
)

data class ProductDetailsItem(

	@field:SerializedName("price")
	val price: String? = null,

	@field:SerializedName("product_image")
	val productImage: String? = null,

	@field:SerializedName("product_id")
	val productId: String? = null,

	@field:SerializedName("mrp")
	val mrp: String? = null,

	@field:SerializedName("product_name")
	val productName: String? = null,

	@field:SerializedName("quntity")
	val quntity: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

package com.test.mymadical.model

import com.google.gson.annotations.SerializedName

data class ModelMyOrder(

	@field:SerializedName("msg")
	val msg: String? = null,

	@field:SerializedName("order_tbl")
	val orderTbl: List<OrderTblItem?>? = null,

	@field:SerializedName("flag")
	val flag: Int? = null
)

data class OrderTblItem(

	@field:SerializedName("order_date")
	val orderDate: String? = null,

	@field:SerializedName("total_item")
	val totalItem: String? = null,

	@field:SerializedName("payment_status")
	val paymentStatus: String? = null,

	@field:SerializedName("address_id")
	val addressId: String? = null,

	@field:SerializedName("value_total")
	val valueTotal: String? = null,

	@field:SerializedName("delivary_date")
	val delivaryDate: String? = null,

	@field:SerializedName("order_id")
	val orderId: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

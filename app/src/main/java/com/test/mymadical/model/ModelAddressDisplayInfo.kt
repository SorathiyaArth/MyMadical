package com.test.mymadical.model

import com.google.gson.annotations.SerializedName

data class ModelAddressDisplayInfo(

	@field:SerializedName("msg")
	val msg: String? = null,

	@field:SerializedName("flag")
	val flag: Int? = null,

	@field:SerializedName("address_tbl")
	val addressTbl: List<AddressTblItem?>? = null
)

data class AddressTblItem(

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

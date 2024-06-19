package com.example.hical.response

import com.google.gson.annotations.SerializedName

data class DashboardItemResponse(

	@field:SerializedName("Item")
	val item: List<DashboardItem>? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class DashboardItem(

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("meal")
	val meal: String? = null,

	@field:SerializedName("amount")
	val amount: Int? = null
)

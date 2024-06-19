package com.example.hical.response

import com.google.gson.annotations.SerializedName

data class AddItemResponse(

	@field:SerializedName("Item")
	val item: List<AddItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class AddItem(

	@field:SerializedName("meal")
	val meal: String? = null,

	@field:SerializedName("date")
	val date: String? = null,

	@field:SerializedName("amount")
	val amount: Int? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("user_id")
	val userId: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: String? = null
)

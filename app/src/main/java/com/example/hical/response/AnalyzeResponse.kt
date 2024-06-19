package com.example.hical.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class AnalyzeResponse(

	@field:SerializedName("information")
	val information: String? = null,

	@field:SerializedName("label")
	val label: String? = null
) : Parcelable

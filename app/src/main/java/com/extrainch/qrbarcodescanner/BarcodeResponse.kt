package com.extrainch.qrbarcodescanner

import com.google.gson.annotations.SerializedName

data class BarcodeResponse(

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
)

package com.apitap.views.fragments.specials.data

import com.google.gson.annotations.SerializedName

data class InventoryResponse(

	@field:SerializedName("tblProductinventoryQuantity")
	val tblProductinventoryQuantity: Int? = null,

	@field:SerializedName("tblProductinventorySku")
	val tblProductinventorySku: String? = null,

	@field:SerializedName("tblProductinventoryUpc")
	val tblProductinventoryUpc: String? = null,

	@field:SerializedName("tblLocationId")
	val tblLocationId: Int? = null,

	@field:SerializedName("tblProductinventoryChoices")
	val tblProductinventoryChoices: String? = null,

	@field:SerializedName("tblProductId")
	val tblProductId: Int? = null,

	@field:SerializedName("tblProductinventoryId")
	val tblProductinventoryId: Int? = null
)
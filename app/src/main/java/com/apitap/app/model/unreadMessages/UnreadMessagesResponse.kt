package com.apitap.app.model.unreadMessages

import com.google.gson.annotations.SerializedName

data class UnreadMessagesResponse(

	@field:SerializedName("_192")
	val jsonMember192: String? = null,

	@field:SerializedName("_122_18")
	val jsonMember12218: String? = null,

	@field:SerializedName("_122_17")
	val jsonMember12217: Boolean? = null,

	@field:SerializedName("RESULT")
	val rESULT: List<RESULTItem?>? = null,

	@field:SerializedName("_11")
	val jsonMember11: String? = null
)

data class RESULTItem(

	@field:SerializedName("_39")
	val jsonMember39: String? = null,

	@field:SerializedName("_101")
	val jsonMember101: String? = null,

	@field:SerializedName("_127_41")
	val jsonMember12741: Int? = null,

	@field:SerializedName("RESULT")
	val rESULT: List<RESULTItem?>? = null,

	@field:SerializedName("_44")
	val jsonMember44: String? = null,

	@field:SerializedName("_114_121")
	val jsonMember114121: String? = null
)

package com.apitap.app.model.reservation

import com.google.gson.annotations.SerializedName

data class MerchantByCatResponse(

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

data class MEItem(

	@field:SerializedName("_114_1")
	val jsonMember1141: String? = null,

	@field:SerializedName("_122_19")
	val jsonMember12219: String? = null,

	@field:SerializedName("_121_80")
	val jsonMember12180: String? = null,

	@field:SerializedName("_114_70")
	val jsonMember11470: String? = null,

	@field:SerializedName("_114_9")
	val jsonMember1149: String? = null,

	@field:SerializedName("_53")
	val jsonMember53: String? = null,

	@field:SerializedName("_121_170")
	val jsonMember121170: String? = null,

	@field:SerializedName("_127_86")
	val jsonMember12786: String? = null
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

	@field:SerializedName("_120_45")
	val jsonMember12045: String? = null,

	@field:SerializedName("ME")
	val mE: List<MEItem?>? = null,

	@field:SerializedName("_120_44")
	val jsonMember12044: String? = null,

	@field:SerializedName("_122_21")
	val jsonMember12221: String? = null,

	@field:SerializedName("_114_93")
	val jsonMember11493: String? = null
)

package com.apitap.model.reservation

import com.google.gson.annotations.SerializedName

data class ReservationLocationResponse(

	@field:SerializedName("address")
	val address: String? = null,

	@field:SerializedName("phone")
	val phone: String? = null,

	@field:SerializedName("city")
	val city: Any? = null,

	@field:SerializedName("contact")
	val contact: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("state")
	val state: Any? = null,

	@field:SerializedName("email")
	val email: String? = null
)

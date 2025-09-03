	package com.apitap.views.fragments.specials.data

	import com.google.gson.annotations.SerializedName

	data class OrderByIdResponse(

		@field:SerializedName("orderType")
		val orderType: String? = null,

		@field:SerializedName("userCreatorId")
		val userCreatorId: Int? = null,

		@field:SerializedName("tblOrderCustomerFirstname")
		val tblOrderCustomerFirstname: String? = null,

		@field:SerializedName("orderId")
		val orderId: Int? = null,

		@field:SerializedName("orderDueDate")
		val orderDueDate: String? = null,

		@field:SerializedName("orderNote")
		val orderNote: String? = null,

		@field:SerializedName("orderTypeId")
		val orderTypeId: Int? = null,

		@field:SerializedName("lastProductDate")
		val lastProductDate: String? = null,

		@field:SerializedName("locationTableName")
		val locationTableName: String? = null,

		@field:SerializedName("orderTotal")
		val orderTotal: Double? = null,

		@field:SerializedName("products")
		val products: List<OrderIdProductsItem>? = null,

		@field:SerializedName("companyId")
		val companyId: Int? = null,

		@field:SerializedName("orderSubtotal")
		val orderSubtotal: Double? = null,

		@field:SerializedName("orderStatusId")
		val orderStatusId: Int? = null,

		@field:SerializedName("locationSeatingId")
		val locationSeatingId: Int? = null,

		@field:SerializedName("tblOrderCustomerLastname")
		val tblOrderCustomerLastname: Any? = null,

		@field:SerializedName("locationId")
		val locationId: Int? = null,

		@field:SerializedName("orderTaxes")
		val orderTaxes: Double? = null,

		@field:SerializedName("locationSeatingName")
		val locationSeatingName: String? = null,

		@field:SerializedName("locationTableId")
		val locationTableId: Int? = null,

		@field:SerializedName("orderTip")
		val orderTip: Double? = null,

		@field:SerializedName("billNumber")
		val billNumber: Any? = null,

		@field:SerializedName("orderDate")
		val orderDate: String? = null,

		@field:SerializedName("tblOrderCustomerPhone")
		val tblOrderCustomerPhone: Any? = null
	)

	data class OrderIdOptionsItem(

		@field:SerializedName("valueId")
		val valueId: Int? = null,

		@field:SerializedName("valueName")
		val valueName: String? = null,

		@field:SerializedName("extraPrice")
		val extraPrice: Double? = null,

		@field:SerializedName("optionId")
		val optionId: Int? = null,

		@field:SerializedName("optionName")
		val optionName: String? = null
	)

	data class OrderIdProductsItem(

		@field:SerializedName("productQuantity")
		val productQuantity: Int? = null,

		@field:SerializedName("productId")
		val productId: Int? = null,

		@field:SerializedName("productRegularPrice")
		val productRegularPrice: Double? = null,

		@field:SerializedName("details")
		val details: List<OrderIdDetailsItem>? = null,

		@field:SerializedName("productDelivered")
		val productDelivered: Int? = null,

		@field:SerializedName("productName")
		val productName: String? = null,

		@field:SerializedName("productReady")
		val productReady: Int? = null,

		@field:SerializedName("categoryId")
		val categoryId: Any? = null
	)

	data class OrderIdDetailsItem(

		@field:SerializedName("detailStatus")
		val detailStatus: Int? = null,

		@field:SerializedName("note")
		val note: String? = null,

		@field:SerializedName("options")
		val options: List<OrderIdOptionsItem>? = null,

		@field:SerializedName("detailId")
		val detailId: Int? = null,

		@field:SerializedName("productDate")
		val productDate: String? = null
	)

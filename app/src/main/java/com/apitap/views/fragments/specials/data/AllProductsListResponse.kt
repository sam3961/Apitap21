package com.apitap.views.fragments.specials.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class AllProductsListResponse(

	@field:SerializedName("productImage")
	val productImage: String? = null,

	@field:SerializedName("productId")
	val productId: Int? = null,

	@field:SerializedName("productStatusId")
	val productStatusId: Int? = null,

	@field:SerializedName("productRegularPrice")
	val productRegularPrice: Double? = null,

	@field:SerializedName("options")
	val options: List<OptionsProductPromoItem>? = null,

	@field:SerializedName("productName")
	val productName: String? = null,

	@field:SerializedName("productDescription")
	val productDescription: String? = null,

	@field:SerializedName("productPriceCurrencyName")
	val productPriceCurrencyName: String? = null,

	@field:SerializedName("productPriceCurrency")
	val productPriceCurrency: Int? = null,

): Parcelable

@Parcelize
data class PromoChoicesItem(

	@field:SerializedName("valueId")
	val valueId: Int? = null,

	@field:SerializedName("valueName")
	val valueName: String? = null,

	@field:SerializedName("extraPrice")
	val extraPrice: Double? = null,

	@field:SerializedName("byDefault")
	val byDefault: Boolean? = null,

	@field:SerializedName("optionId")
	val optionId: Int? = null,

	@field:SerializedName("optionName")
	val optionName: String? = null,

	var selectedItem: Boolean? = false
): Parcelable

@Parcelize
data class OptionsProductPromoItem(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("choices")
	val choices: List<PromoChoicesItem>? = null,

	var selectedItem: Boolean? = false
): Parcelable

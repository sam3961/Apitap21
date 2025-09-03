package com.apitap.views.fragments.specials.data

import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class PromoScheduledItem(
	val hourClose: String? = null,
	val scheduleEndDate: String? = null,
	val dayOfTheWeek: Int? = null,
	val timezone: String? = null,
	val scheduleStartDate: String? = null,
	val closedFlag: Int? = null,
	val discountId: Int? = null,
	val hour24: Int? = null,
	val hourOpen: String? = null
) : Parcelable

@Parcelize
data class PromotionByIdResponseItem(
	val promoDescription: String? = null,
	val itemsaplied: String? = null,
	val discountTypeId: Int? = null,
	val productUserId: Int? = null,
	val requiredAllItemsFlag: Int? = null,
	val locationAvailableId: Int? = null,
	val requiredList: List<AppliedListItems?>? = null,
	val appliedList: List<AppliedListItems?>? = null,
	val promoScheduled: List<PromoScheduledItem?>? = null,
	val locationAvailableName: String? = null,
	val discountConditionAppliesName: String? = null,
	val promotionKindId: Int? = null,
	val discountTypeName: String? = null,
	val discountConditionAppliesId: Int? = null,
	val promoName: String? = null,
	val companyId: Int? = null,
	val productImages: List<ProductImagesItems?>? = null,
	val discountConditionValue: Int? = null,
	val promotionKindName: String? = null,
	val locations: String? = null,
	val discountId: Int? = null,
	val discountValue: String? = null
) : Parcelable

@Parcelize
data class ProductImagesItems(
	val productImagesId: Int? = null,
	val imageTypeId: Int? = null,
	val productImage: String? = null,
	val productId: Int? = null,
	val productImageLandscapeF: Boolean? = null
) : Parcelable

@Parcelize
data class AppliedListItems(
	val inventoryBelowQty: Int? = null,
	val productId: Int? = null,
	val productStatusId: Int? = null,
	val showMessageInventoryBelow: Boolean? = null,
	val continueSellingOutOfSchedule: Boolean? = null,
	val productName: String? = null,
	val productImage: String? = null,
	val productDescription: String? = null,
	val productPriceCurrencyName: String? = null,
	val productPriceCurrency: Int? = null
) : Parcelable

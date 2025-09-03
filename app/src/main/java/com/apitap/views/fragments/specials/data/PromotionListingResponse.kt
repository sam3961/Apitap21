package com.apitap.views.fragments.specials.data

import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class PromotionListingResponse(
    val shippingCompany: String? = null,
    val promoDescription: String? = null,
    val itemsaplied: String? = null,
    val discountTypeId: Int? = null,
    val productUserId: Int? = null,
    val locationSeatingId: Int? = null,
    val requiredAllItemsFlag: Int? = null,
    val itemsRequired: Boolean? = null,
    val locationAvailableId: Int? = null,
    val appliedList: List<AppliedListItem> = ArrayList(),
    val requiredList: List<AppliedListItem> = ArrayList(),
    val locationAvailableName: String? = null,
    val discountConditionAppliesName: String? = null,
    val promotionKindId: Int? = null,
    val shippingService: String? = null,
    val discountTypeName: String? = null,
    val discountConditionAppliesId: Int? = null,
    val promoName: String? = null,
    val companyId: Int? = null,
    val productImages: List<ProductImagesItem?>? = null,
    val discountConditionValue: Double? = null,
    val promotionKindName: String? = null,
    val locations: String? = null,
    val discountId: Int? = null,
    val discountValue: Double? = null
) : Parcelable {
    fun totalSelectedQty(): Int {
        val required = requiredList ?: emptyList()
        val applied = appliedList ?: emptyList()
        return (required + applied).sumOf { it.quantity ?: 0 }
    }

}

@Parcelize
data class AppliedListItem(
    val productImage: String? = null,
    val productId: Int? = null,
    var isRequiredItem: Boolean? = null,
    var isSelected: Boolean? = null,
    var isEnabled: Boolean = true,
    var quantity: Int? = null,
    val productStatusId: Int? = null,
    val productRegularPrice: Double? = null,
    val productName: String? = null,
    var productActualPrice: String? = null,
    var productDiscountPrice: String? = null,
    val productDescription: String? = null,
    val productPriceCurrencyName: String? = null,
    val productPriceCurrency: Int? = null,
    var options: List<OptionsProductPromoItem>? = null
) : Parcelable

@Parcelize
data class ProductImagesItem(
    val productImagesId: Int? = null,
    val imageTypeId: Int? = null,
    val productImage: String? = null,
    val productId: Int? = null,
    val productImageLandscapeF: Boolean? = null
) : Parcelable

@Parcelize
data class RequiredListItem(
    val productImage: String? = null,
    val productId: Int? = null,
    val productStatusId: Int? = null,
    val productRegularPrice: Double? = null,
    val productName: String? = null,
    val productDescription: String? = null,
    val productPriceCurrencyName: String? = null,
    val productPriceCurrency: Int? = null,
) : Parcelable

@Parcelize
data class ProductItemWrapper(
    val item: AppliedListItem,
    val isApplied: Boolean
): Parcelable




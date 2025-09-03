package com.apitap.views.fragments.specials.data

import com.apitap.views.fragments.specials.utils.CommonFunctions
import com.google.gson.annotations.SerializedName

data class ProductByIdResponse(

    @field:SerializedName("productImage")
    val productImage: String? = null,

    @field:SerializedName("productId")
    val productId: Int? = null,

    @field:SerializedName("productQuantity")
    val productQuantity: Int? = null,

    @field:SerializedName("productReady")
    val productReady: Int? = null,

    @field:SerializedName("productDelivered")
    val productDelivered: Int? = null,

    val isOldOrderItem: Boolean = false,

    @field:SerializedName("productStatusId")
    val productStatusId: Int? = null,

    @field:SerializedName("orderStatusId")
    val orderStatusId: Int? = null,
    @field:SerializedName("shoppingCartId")
    val shoppingCartId: Int? = null,

    @field:SerializedName("orderTypeId")
    val orderTypeId: Int? = null,

    @field:SerializedName("orderType")
    val orderType: String? = null,
    @field:SerializedName("orderDate")
    val orderDate: String? = null,
    @field:SerializedName("preparedUserDate")
    val preparedUserDate: String? = null,
    @field:SerializedName("preparedUserId")
    val preparedUserId: Int? = null,
    @field:SerializedName("orderDueDate")
    val orderDueDate: String? = null,

    @field:SerializedName("lastProductDate")
    var lastProductDate: String? = null,

    @field:SerializedName("printDate")
    var printDate: String? = null,
    @field:SerializedName("productRegularPrice")
    val productRegularPrice: Double = 0.0,

    @field:SerializedName("options")
    val options: List<OptionsItem>? = null,

    @field:SerializedName("productName")
    val productName: String? = null,

    @field:SerializedName("productDescription")
    val productDescription: String? = null,

    @field:SerializedName("productPriceCurrencyName")
    val productPriceCurrencyName: String? = null,

    @field:SerializedName("elapsedTime")
    val elapsedTime: String? = null,

    @field:SerializedName("momentum")
    val momentum: String? = null,

    @SerializedName("promoProductList")
    val promoProductList: List<PromoProductItem>? = null,

    @field:SerializedName("details")
    val details: List<DetailsItem>? = null,

    @field:SerializedName("productPriceCurrency")
    val productPriceCurrency: Int? = null,

    @field:SerializedName("recommended")
    val recommended: List<RecommendedItem>? = null,


    val locationSeatingId: Int? = 0,
    val tableOrderTypeId: Int? = 0,
    val tableId: Int? = null,
    val tableLocationName: String? = "",
    val tblUserId: Int? = 0,
    val orderId: Int? = 0,
    val orderSubtotal: Double? = 0.0,
    val productNotes: String? = "",
    val detailId: Int? = null,
    val detailStatus: Int? = null,
    val orderNotes: String? = "",
    val offlineAdded: Boolean? = false,
    val isSpecialPriceTotal: Boolean? = false,

    )

data class DataDetailsItem(

    @field:SerializedName("detailStatus")
    val detailStatus: Int? = null,

    @field:SerializedName("note")
    val note: String? = null,

    @field:SerializedName("detailId")
    val detailId: Int? = null,

    @field:SerializedName("elapsedTime")
    val elapsedTime: String? = null,

    @field:SerializedName("preparedUserId")
    val preparedUserId: Int? = null,

    @field:SerializedName("productDate")
    val productDate: String? = null,
    @field:SerializedName("userLast")
    val userLast: String? = null,
    @field:SerializedName("userFirst")
    val userFirst: String? = null,
    @field:SerializedName("preparedUserDate")
    val preparedUserDate: String? = null,
    @field:SerializedName("detailCreationDate")
    val detailCreationDate: String? = null,
    @field:SerializedName("detailDeliveredDate")
    val detailDeliveredDate: String? = null,
)


data class ChoicesItem(

    @field:SerializedName("valueId")
    val valueId: Int? = null,

    @field:SerializedName("valueName")
    val valueName: String? = null,

    @field:SerializedName("byDefault")
    var byDefault: Boolean = false,

    @field:SerializedName("extraPrice")
    val extraPrice: Double = 0.0,

    @field:SerializedName("optionId")
    val optionId: Int? = null,

    @field:SerializedName("optionName")
    val optionName: String? = null,

    var selectedItem: Boolean = false
)

data class OptionsItem(

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("price")
    val price: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("choices")
    val choices: List<ChoicesItem>? = null
)

data class RecommendedItem(

    @field:SerializedName("productImage")
    val productImage: String? = null,

    @field:SerializedName("productId")
    val productId: Int? = null,

    @field:SerializedName("productStatusId")
    val productStatusId: Int? = null,

    @field:SerializedName("productRegularPrice")
    val productRegularPrice: Double? = null,

    @field:SerializedName("options")
    val options: List<Any?>? = null,

    @field:SerializedName("productRaiting")
    val productRaiting: Any? = null,

    @field:SerializedName("productName")
    val productName: String? = null,

    @field:SerializedName("productDescription")
    val productDescription: String? = null,

    @field:SerializedName("productPriceCurrencyName")
    val productPriceCurrencyName: String? = null,

    @field:SerializedName("productPriceCurrency")
    val productPriceCurrency: Int? = null,

    @field:SerializedName("recommended")
    val recommended: List<Any?>? = null
)

data class PromoProductItem(
    @SerializedName("promotionId")
    val promotionId: Int? = null,

    @SerializedName("productId")
    val productId: Int? = null,

    @SerializedName("productName")
    val productName: String? = null,

    @SerializedName("productImage")
    val productImage: String? = null,

    @SerializedName("productPriceCurrencyName")
    val productPriceCurrencyName: String? = null,

    @SerializedName("orderId")
    val orderId: Int? = null,

    @SerializedName("shoppingcartId")
    val shoppingCartId: Int? = null,

    @SerializedName("shoppingcartdetailId")
    val shoppingCartDetailId: Int? = null,

    @SerializedName("orderdetailId")
    val orderDetailId: Int? = null,

    @SerializedName("detailChoices")
    val detailChoices: String? = null,

    val promoActualPrice: String? = null,
    val promoDiscountPrice: String? = null,

    @SerializedName("detailQty")
    val detailQty: Int? = null,

    @SerializedName("detailSalePrice")
    val detailSalePrice: Double? = null,

    @SerializedName("productRegularPrice")
    val productRegularPrice: Double? = null,

    @SerializedName("orderPromotionTotalDiscount")
    val orderPromotionTotalDiscount: Double? = null,

    @SerializedName("createdAt")
    val createdAt: String? = null,

    @SerializedName("details")
    val details: List<PromoDetailItem>? = null
) {
    fun getNameOfProduct(): String? {
        return CommonFunctions.promotionActiveProductResponse.find { it.productId == productId }?.productName
    }

    fun getImageOfProduct(): String? {
        return if (productImage.isNullOrEmpty())
            CommonFunctions.promotionActiveProductResponse.find { it.productId == productId }?.productImage
        else
            productImage
    }
}

data class PromoDetailItem(
    @SerializedName("options")
    val options: List<PromoChoiceOption>? = null
)

data class PromoChoiceOption(
    @SerializedName("optionId")
    val optionId: Int? = null,

    @SerializedName("optionName")
    val optionName: String? = null,

    @SerializedName("valueId")
    val valueId: Int? = null,

    @SerializedName("valueName")
    val valueName: String? = null,

    @SerializedName("extraPrice")
    val extraPrice: Double? = null
)




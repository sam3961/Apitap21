package com.apitap.views.fragments.specials.data

import com.google.gson.annotations.SerializedName

data class OrderByLocationTable(

    @field:SerializedName("orderType")
    val orderType: String? = null,

    @field:SerializedName("userCreatorName")
    val userCreatorName: String? = null,

    @field:SerializedName("userCreatorRoleName")
    val userCreatorRoleName: String? = null,

    @field:SerializedName("deliveredByLastName")
    val deliveredByLastName: String? = null,

    @field:SerializedName("deliveredByJobTitle")
    val deliveredByJobTitle: String? = null,

    @field:SerializedName("deliveredByFirstName")
    val deliveredByFirstName: String? = null,

    @field:SerializedName("orderDeliveredBy")
    val orderDeliveredBy: String? = null,

    @field:SerializedName("userCreatorId")
    val userCreatorId: Int? = null,

    @field:SerializedName("userCreatorRoleId")
    val userCreatorRoleId: Int? = null,

    @field:SerializedName("tblOrderCustomerFirstname")
    val tblOrderCustomerFirstname: Any? = null,

    @field:SerializedName("orderId")
    val orderId: Int? = null,

    @field:SerializedName("orderDueDate")
    val orderDueDate: String? = null,
  @field:SerializedName("printDate")
    val printDate: String? = null,

    @field:SerializedName("orderCanceledDate")
    val orderCanceledDate: String? = null,
    @field:SerializedName("orderCanceledReason")
    val orderCanceledReason: String? = null,
    @field:SerializedName("orderDeliveryInstructions")
    val orderDeliveryInstructions: String? = null,

    @field:SerializedName("orderNote")
    val orderNote: String? = null,

    @field:SerializedName("orderTypeId")
    val orderTypeId: Int? = null,

    @field:SerializedName("lastProductDate")
    val lastProductDate: String? = null,

    @field:SerializedName("momentum")
    val momentum: String? = null,
    @field:SerializedName("elapsedTime")
    val elapsedTime: String? = null,

    @field:SerializedName("locationTableName")
    val locationTableName: String? = null,

    @field:SerializedName("orderTotal")
    val orderTotal: Double? = null,

    @field:SerializedName("products")
    val products: ArrayList<OrderProductsItem>? = null,

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
    val billNumber: String? = null,

    @field:SerializedName("orderDate")
    val orderDate: String? = null,

    @field:SerializedName("tblOrderCustomerPhone")
    val tblOrderCustomerPhone: Any? = null
)

data class  DetailsItem(

    @field:SerializedName("detailStatus")
    val detailStatus: Int? = null,

    @field:SerializedName("note")
    val note: String? = null,

    @field:SerializedName("options")
    val options: List<OrderOptionsItem>? = null,

    @field:SerializedName("detailId")
    val detailId: Int? = null,

    @field:SerializedName("elapsedTime")
    val elapsedTime: String? = null,

    @field:SerializedName("preparedUserId")
    val preparedUserId: Int? = null,

    @field:SerializedName("detailPickupLocationId")
    val detailPickupLocationId: Int? = null,

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

data class OrderProductsItem(

    @field:SerializedName("productQuantity")
    val productQuantity: Int? = null,

    @field:SerializedName("productId")
    val productId: Int? = null,

    @field:SerializedName("productRegularPrice")
    val productRegularPrice: Double = 0.0,

    @field:SerializedName("details")
    val details: List<DetailsItem>? = null,

    @field:SerializedName("productDelivered")
    val productDelivered: Int? = null,

    @field:SerializedName("productName")
    val productName: String? = null,

    @field:SerializedName("productImage")
    val productImage: String? = null,

    @field:SerializedName("productReady")
    val productReady: Int? = null,

    @field:SerializedName("categoryId")
    val categoryId: Int? = null,

    @field:SerializedName("promoProductList")
    val promoProductList: List<PromoProductItem>? = null
)

data class OrderPromoProductItem(

    @field:SerializedName("promotionId")
    val promotionId: Int? = null,

    @field:SerializedName("productId")
    val productId: Int? = null,

    @field:SerializedName("orderId")
    val orderId: Int? = null,

    @field:SerializedName("shoppingcartId")
    val shoppingcartId: Int? = null,

    @field:SerializedName("shoppingcartdetailId")
    val shoppingcartdetailId: Int? = null,

    @field:SerializedName("orderdetailId")
    val orderdetailId: Int? = null,

    @field:SerializedName("detailChoices")
    val detailChoices: String? = null,

    @field:SerializedName("detailQty")
    val detailQty: Int? = null,

    @field:SerializedName("detailSalePrice")
    val detailSalePrice: Double? = null,

    @field:SerializedName("productRegularPrice")
    val productRegularPrice: Double? = null,

    @field:SerializedName("orderPromotionTotalDiscount")
    val orderPromotionTotalDiscount: Double? = null,

    @field:SerializedName("createdAt")
    val createdAt: String? = null,

    @field:SerializedName("details")
    val details: List<OrderPromoDetailItem>? = null
)
data class OrderPromoDetailItem(

    @field:SerializedName("options")
    val options: List<OrderOptionsItem>? = null
)


data class OrderOptionsItem(

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

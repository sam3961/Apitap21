package com.apitap.views.fragments.specials.utils

import com.apitap.views.fragments.specials.data.AppliedListItem
import java.text.DecimalFormat

object PromotionTypeRuleEvaluator {

    enum class DiscountConditionType(val id: Int?) {
        QTY_ALL(39001),
        QTY_EACH(39002),
        TOTAL_PURCHASE(39003),
        NONE(null);

        companion object {
            fun from(id: Int?): DiscountConditionType {
                return values().firstOrNull { it.id == id } ?: NONE
            }
        }
    }

    enum class DiscountType(val id: Int) {
        PRICE_OFF(38000),
        PERCENT_OFF(38001),
        SPECIAL_PRICE(38002),
        NO_FREE_ITEMS(38003),
        SPECIAL_PRICE_TOTAL(38004);

        companion object {
            fun from(id: Int): DiscountType {
                return values().firstOrNull { it.id == id } ?: PRICE_OFF
            }
        }
    }

    fun getDiscountTitle(discountTypeId: Int, discountValue: Double): String {
        val discountType = DiscountType.from(discountTypeId)

        return when (discountType) {
            DiscountType.PRICE_OFF -> "- $${"%.2f".format(discountValue)}"
            DiscountType.PERCENT_OFF -> "${"%.0f".format(discountValue)}%"
            DiscountType.SPECIAL_PRICE -> "$${"%.2f".format(discountValue)} each"
            DiscountType.NO_FREE_ITEMS -> "" // empty string
            DiscountType.SPECIAL_PRICE_TOTAL -> "$${"%.2f".format(discountValue)} total"
        }
    }

    fun shouldEnableAppliedList(
        conditionType: DiscountConditionType,
        requiredQty: Int,
        appliedQty: Int,
        totalQty: Int,
        requiredAmount: Double,
        totalCartAmount: Double,
        hasRequiredList: Boolean
    ): Boolean {
        return when (conditionType) {
            DiscountConditionType.QTY_ALL -> totalQty >= requiredQty
            DiscountConditionType.QTY_EACH -> if (hasRequiredList) totalQty >= requiredQty else appliedQty >= requiredQty
            DiscountConditionType.TOTAL_PURCHASE -> if (hasRequiredList) (totalCartAmount + requiredAmount) >= requiredQty else totalCartAmount >= requiredQty
            DiscountConditionType.NONE -> true
        }
    }

    fun getMinQtyForItem(conditionType: DiscountConditionType, requiredQty: Int): Int {
        return if (conditionType == DiscountConditionType.QTY_EACH) requiredQty else 1
    }

    fun isAddToOrderEnabled(
        appliedQty: Int,
        conditionType: DiscountConditionType,
        totalQty: Int,
        requiredQty: Int,
        hasRequiredList: Boolean,
        cartAmount: Double
    ): Boolean {
        return when (conditionType) {
            DiscountConditionType.QTY_ALL -> totalQty >= requiredQty
            DiscountConditionType.QTY_EACH -> appliedQty >= requiredQty
            DiscountConditionType.TOTAL_PURCHASE -> cartAmount >= requiredQty
            DiscountConditionType.NONE -> appliedQty > 0
        }
    }

    // Add this function to your PromotionListingResponse class or the relevant class.
    fun isPromotionValid(
        cartAmount: Double,
        selectedItems: List<AppliedListItem>,
        requiredItems: List<AppliedListItem>,
        discountConditionAppliesId: Int?,
        discountConditionValue: Double?,
        hasRequired: Boolean
    ): Boolean {
        // Retrieve the promotion condition ID and condition value
        val conditionId = PromotionConditionId.fromId(discountConditionAppliesId)
        val conditionValue = discountConditionValue ?: 0.0

        // Check for the promotion condition type
        return when (conditionId) {
            // If the promotion condition is based on the total quantity of all items
            PromotionConditionId.QuantityTotalAllItems -> {
                // Calculate the total quantity of selected items
                val totalQuantity = selectedItems.sumOf { it.quantity ?: 0 }

                // Check if the total quantity is greater than or equal to the required condition value
                val conditionMet = totalQuantity >= conditionValue.toInt()

                // If the condition is met, check if required items are selected (if necessary)
                if (conditionMet && !hasRequired) {
                    return true
                }

                val hasRequiredItemsSelected = requiredItems.any {
                    it.isSelected == true || (it.quantity ?: 0) > 0
                }

                if (conditionMet && hasRequiredItemsSelected)
                    true
                else
                    false
            }

            // If the promotion condition is based on quantity per individual item
            PromotionConditionId.QuantityEachItem -> {
                // Check if the required quantity for each item is met
                selectedItems.all { item ->
                    val requiredQty = conditionValue.toInt()
                    (item.quantity ?: 0) >= requiredQty
                }
            }

            // If the promotion condition is based on the total cart amount
            PromotionConditionId.CartTotalAmount -> {
                cartAmount >= conditionValue
            }

            // If no valid condition is found, return false
            else -> false
        }
    }

    fun getPromotionConditionLabel(
        discountTypeId: Int?,
        discountConditionId: Int?,
        hasRequired: Boolean,
        requiredQty: Int?,
        requiredAmount: Double?,
        cartAmount: Double?,
        currentPromoAmount: Double?,
        currentQuantity: Int?,
        selectedListItem: AppliedListItem? = null
    ): String {
        val type = DiscountType.from(discountTypeId ?: 38000)
        val condition = DiscountConditionType.from(discountConditionId)

        return when (type) {
            DiscountType.PRICE_OFF -> {
                when (condition) {
                    DiscountConditionType.QTY_EACH -> {
                        val finalQty = ((requiredQty ?: 0) - (currentQuantity ?: 0))
                            .coerceAtLeast(0)

                        if (finalQty == 0) {
                            "Promotion enabled"
                        } else {
                            if (hasRequired) {
                                "You need to add at least $finalQty of each selected item to enable the promotion."
                            } else {
                                "You need to add at least $finalQty of each item to enable the promotion."
                            }
                        }


                    }

                    DiscountConditionType.QTY_ALL -> {
                        val finalQty = ((requiredQty ?: 0) - (currentQuantity ?: 0))
                            .coerceAtLeast(0)

                        if (finalQty == 0) {
                            "Promotion enabled"
                        } else {
                            "You need to add $finalQty items in total to enable the promotion."
                        }
                    }

                    DiscountConditionType.TOTAL_PURCHASE -> {
                        val amount: Double = if (hasRequired)
                            currentPromoAmount ?: 0.0
                        else
                            cartAmount ?: 0.0

                        val finalAmount = ((requiredAmount ?: 0.0) - (amount ?: 0.0))
                            .coerceAtLeast(0.0)

                        if (finalAmount <= 0.0) {
                            "Promotion enabled"
                        } else {
                            "You need to reach a minimum purchase of $${"%.2f".format(finalAmount)} in the order subtotal to enable the promotion."
                        }
                    }

                    else -> ""
                }
            }

            DiscountType.PERCENT_OFF -> {
                when (condition) {
                    DiscountConditionType.QTY_ALL -> {
                        val finalQty = ((requiredQty ?: 0) - (currentQuantity ?: 0))
                            .coerceAtLeast(0)

                        if (finalQty == 0) {
                            "Promotion enabled"
                        } else {
                            "You need to add $finalQty items in total to enable the promotion."
                        }
                    }

                    DiscountConditionType.QTY_EACH -> {
                        val finalQty = ((requiredQty ?: 0) - (currentQuantity ?: 0))
                            .coerceAtLeast(0)

                        if (finalQty == 0) {
                            "Promotion enabled"
                        } else {
                            "You need to add at least $finalQty of each selected item to enable the promotion."
                        }
                    }

                    DiscountConditionType.TOTAL_PURCHASE -> {
                        val amount: Double = if (hasRequired)
                            currentPromoAmount ?: 0.0
                        else
                            cartAmount ?: 0.0

                        val finalAmount = ((requiredAmount ?: 0.0) - (amount ?: 0.0))
                            .coerceAtLeast(0.0)

                        if (finalAmount <= 0.0) {
                            "Promotion enabled"
                        } else {
                            "You need to reach a minimum purchase of $${"%.2f".format(finalAmount)} in the order subtotal to enable the promotion."
                        }
                    }

                    else -> "" // % Off - No condition = no label
                }
            }

            DiscountType.SPECIAL_PRICE -> {
                when (condition) {
                    DiscountConditionType.QTY_ALL -> {
                        val finalQty = ((requiredQty ?: 0) - (currentQuantity ?: 0))
                            .coerceAtLeast(0)

                        if (finalQty == 0) {
                            "Promotion enabled"
                        } else {
                            "You need to add $finalQty items in total to enable the promotion."
                        }
                    }

                    DiscountConditionType.QTY_EACH -> {
                        val finalQty = ((requiredQty ?: 0) - (currentQuantity ?: 0))
                            .coerceAtLeast(0)


                        if (finalQty == 0) {
                            "Promotion enabled"
                        } else {
                            "You need to add $finalQty items in total to enable the promotion."
                        }
                    }

                    DiscountConditionType.TOTAL_PURCHASE -> {
                        var amount = 0.0
                        if (hasRequired) {
                            amount = currentPromoAmount ?: 0.0
                        } else
                            amount = cartAmount ?: 0.0

                        val finalAmount = ((requiredAmount ?: 0.0) - (amount ?: 0.0))
                            .coerceAtLeast(0.0)

                        if (finalAmount <= 0.0) {
                            "Promotion enabled"
                        } else {
                            "You need to reach a minimum purchase of $${"%.2f".format(finalAmount)} to unlock the special price."
                        }
                    }

                    else -> ""
                }
            }

            DiscountType.NO_FREE_ITEMS -> {
                when (condition) {
                    DiscountConditionType.QTY_EACH -> {
                        val finalQty = ((requiredQty ?: 0) - (currentQuantity ?: 0))
                            .coerceAtLeast(0)

                        if (finalQty == 0) {
                            "Promotion enabled"
                        } else {
                            "You need to add $finalQty items in total to enable the promotion."
                        }
                    }

                    DiscountConditionType.QTY_ALL -> {
                        val finalQty = ((requiredQty ?: 0) - (currentQuantity ?: 0))
                            .coerceAtLeast(0)

                        if (finalQty == 0) {
                            "Promotion enabled"
                        } else {
                            "You need to add $finalQty items in total to enable the promotion."
                        }
                    }

                    DiscountConditionType.TOTAL_PURCHASE -> {
                        val amount: Double = if (hasRequired)
                            currentPromoAmount ?: 0.0
                        else
                            cartAmount ?: 0.0

                        val finalAmount = ((requiredAmount ?: 0.0) - (amount ?: 0.0))
                            .coerceAtLeast(0.0)

                        if (finalAmount <= 0.0) {
                            "Promotion enabled"
                        } else {
                            "You need to reach a minimum purchase of $${"%.2f".format(finalAmount)} in the order subtotal to enable the promotion."
                        }
                    }

                    else -> ""
                }
            }

            DiscountType.SPECIAL_PRICE_TOTAL -> {
                when (condition) {
                    DiscountConditionType.QTY_ALL -> {
                        val finalQty = ((requiredQty ?: 0) - (currentQuantity ?: 0))
                            .coerceAtLeast(0)

                        if (finalQty == 0) {
                            "Promotion enabled"
                        } else {
                            "You need to add $finalQty items in total to enable the promotion."
                        }
                    }

                    DiscountConditionType.QTY_EACH -> {
                        val finalQty = ((requiredQty ?: 0) - (currentQuantity ?: 0))
                            .coerceAtLeast(0)

                        if (finalQty == 0) {
                            "Promotion enabled"
                        } else {
                            "You need to add $finalQty items in total to enable the promotion."
                        }
                    }

                    DiscountConditionType.TOTAL_PURCHASE -> {
                        val amount: Double = if (hasRequired)
                            currentPromoAmount ?: 0.0
                        else
                            cartAmount ?: 0.0

                        val finalAmount = ((requiredAmount ?: 0.0) - amount)
                            .coerceAtLeast(0.0)

                        if (finalAmount <= 0.0) {
                            "Promotion enabled"
                        } else {
                            "You need to reach a minimum purchase of $${"%.2f".format(finalAmount)} in the order subtotal to enable the promotion."
                        }
                    }

                    else -> ""
                }
            }


            else -> ""
        }
    }

    fun calculatePricesFormatted(
        detailQty: Int,
        detailSalePrice: Double,
        productRegularPrice: Double
    ): Pair<String, String> {
        val df = DecimalFormat("$#,##0.00")

        val discountPrice = if (detailQty > 0) {
            detailSalePrice / detailQty
        } else {
            0.0
        }

        return Pair(
            df.format(productRegularPrice), // Actual Price
            df.format(discountPrice)        // Discount Price
        )
    }

    fun calculatePrices(
        detailQty: Int,
        detailSalePrice: Double,
        productRegularPrice: Double
    ): Pair<Double, Double> {
        val discountPrice = if (detailQty > 0) {
            detailSalePrice / detailQty
        } else {
            0.0
        }

        return Pair(
            productRegularPrice, // Actual Price
            discountPrice        // Discount Price
        )
    }


}
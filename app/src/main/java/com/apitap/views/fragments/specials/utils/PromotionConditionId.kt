package com.apitap.views.fragments.specials.utils


enum class PromotionConditionId(val id: Int) {
    QuantityTotalAllItems(39001),
    QuantityEachItem(39002),
    CartTotalAmount(39003);

    companion object {
        fun fromId(id: Int?): PromotionConditionId? {
            return values().firstOrNull { it.id == id }
        }
    }
}

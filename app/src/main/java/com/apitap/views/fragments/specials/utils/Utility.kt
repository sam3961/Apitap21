package com.apitap.views.fragments.specials.utils

import com.apitap.views.fragments.specials.network.RetrofitClient


object Utility {
    suspend fun isCombinationInStock(productId: Int?, selectedChoices: List<Int>): Boolean {
//        val currentLocationId = SharedPreferenceHelper.getLoginUserResponse().data?.location?.tblLocationId
        val currentLocationId = CommonFunctions.promotionMerchantId

        return try {
            val inventoryList = RetrofitClient.instance.getInventoryByProductId(productId)
                .filter { it.tblLocationId == currentLocationId } // ✅ filter by locationId

            val normalizedSelected = selectedChoices.sorted()

            inventoryList.any { inv ->
                val invChoices = inv.tblProductinventoryChoices
                    ?.split(",")
                    ?.mapNotNull { it.toIntOrNull() }
                    ?.sorted()

                // ✅ check if all selected choices exist in this inventory combination
                normalizedSelected.all { invChoices?.contains(it) == true } &&
                        (inv.tblProductinventoryQuantity ?: 0) > 0
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun isInStock(productId: Int?): Boolean {
//        val currentLocationId = SharedPreferenceHelper.getLoginUserResponse().data?.location?.tblLocationId
        val currentLocationId = CommonFunctions.promotionMerchantId

        return try {
            val inventoryList = RetrofitClient.instance.getInventoryByProductId(productId)
                .filter { it.tblLocationId == currentLocationId } // ✅ filter by locationId

            // check if any entry in list has quantity > 0
            inventoryList.any { (it.tblProductinventoryQuantity ?: 0) > 0 }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
package com.apitap.views.fragments.specials.utils

import com.apitap.views.fragments.specials.AddPromoToOrderDialog
import com.apitap.views.fragments.specials.data.InventoryResponse
import com.apitap.views.fragments.specials.network.RetrofitClient


object Utility {
    suspend fun isCombinationInStock(
        productId: Int?,
        selectedChoices: List<Int>
    ): Boolean {

        return try {
            val inventoryList =
                RetrofitClient.instance.getInventoryByProductId(productId)
            // ❌ NO location filter for promotions

            val normalizedSelected = selectedChoices.sorted()

            inventoryList.any { inv ->
                val invChoices = inv.tblProductinventoryChoices
                    ?.split(",")
                    ?.mapNotNull { it.toIntOrNull() }
                    ?.sorted()

                // Check combination match + quantity
                normalizedSelected.all { invChoices?.contains(it) == true } &&
                        (inv.tblProductinventoryQuantity ?: 0) > 0
            }

        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun isInStock(productId: Int?): Boolean {

        val data = try {
            val inventoryList =
                RetrofitClient.instance.getInventoryByProductId(productId)
            // ❌ NO location filter

            inventoryList.any { (it.tblProductinventoryQuantity ?: 0) > 0 }

        } catch (e: Exception) {
            e.printStackTrace()
            false
        }

        return data
    }


    fun isProductInStock(
        productId: Int,
        inventoryMap: Map<Int, List<InventoryResponse>>?
    ): Boolean {
        val list = inventoryMap?.get(productId) ?: return false
        return list.any { (it.tblProductinventoryQuantity ?: 0) > 0 }
    }

    fun isCombinationInStock(
        productId: Int,
        selectedChoices: Set<Int>,
        inventoryIndexMap: Map<Int, List<AddPromoToOrderDialog.InventoryIndex>>?
    ): Boolean {
        return inventoryIndexMap?.get(productId)
            ?.any { it.qty > 0 && it.choiceSet.containsAll(selectedChoices) }
            ?: false
    }

    fun isChoiceAvailable(
        productId: Int,
        testChoiceId: Int,
        selectedChoices: Set<Int>,
        inventoryIndexMap: Map<Int, List<AddPromoToOrderDialog.InventoryIndex>>?
    ): Boolean {
        val testSet = selectedChoices + testChoiceId
        return isCombinationInStock(productId, testSet, inventoryIndexMap)
    }

    fun isChoiceGloballyInStock(
        productId: Int,
        choiceId: Int,
        inventoryIndexMap: Map<Int, List<AddPromoToOrderDialog.InventoryIndex>>?
    ): Boolean {
        val inventory = inventoryIndexMap?.get(productId) ?: return false

        return inventory.any {
            it.qty > 0 && it.choiceSet.contains(choiceId)
        }
    }

}
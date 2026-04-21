package com.apitap.app.views.fragments.specials

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.apitap.app.R
import com.apitap.app.controller.ModelManager
import com.apitap.app.databinding.DialogAddPromoOrderBinding
import com.apitap.app.model.Client
import com.apitap.app.model.Constants
import com.apitap.app.model.Operations
import com.apitap.app.model.Utils
import com.apitap.app.model.preferences.ATPreferences
import com.apitap.app.views.fragments.specials.adapter.PromotionsItemsAdapter
import com.apitap.app.views.fragments.specials.data.AppliedListItem
import com.apitap.app.views.fragments.specials.data.InventoryResponse
import com.apitap.app.views.fragments.specials.data.ProductByIdResponse
import com.apitap.app.views.fragments.specials.data.PromoChoiceOption
import com.apitap.app.views.fragments.specials.data.PromoDetailItem
import com.apitap.app.views.fragments.specials.data.PromoProductItem
import com.apitap.app.views.fragments.specials.data.PromotionListingResponse
import com.apitap.app.views.fragments.specials.utils.CommonFunctions
import com.apitap.app.views.fragments.specials.utils.PromotionTypeRuleEvaluator
import com.apitap.app.views.fragments.specials.utils.PromotionTypeRuleEvaluator.getPromotionConditionLabel
import com.bumptech.glide.Glide
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONObject
import kotlin.math.round


object AddPromoToOrderDialog {

    private var finalAmount: Double = 0.0
    private var dialog: Dialog? = null
    private var optionId = 0
    private var quantityCount = 1
    var tableLocationName = ""
    var tableId = 0
    var locationSeatingId = 0
    var promotionsItemsAdapter: PromotionsItemsAdapter? = null

    fun show(
        context: Activity,
        selectedPromotionId: String,
        merchantId: String,
        product: PromotionListingResponse,
        showAddToOrder: Boolean = true,
        selectedProduct: PromotionListingResponse? = null,
        inventoryMap: Map<Int, List<InventoryResponse>>? = HashMap(),
        listener: OnPromoCartListener? = null
    ): Dialog {

        if (dialog?.isShowing == true && (dialog?.context as? Activity)?.isFinishing == false) {
            try {
                dialog?.dismiss()
            } catch (_: IllegalArgumentException) {
                // Already detached, ignore
            }
        }
        dialog = null
//        dismissDialog(orderMenuViewModel,dashboardMenuViewModel)


        val binding = DialogAddPromoOrderBinding.inflate(LayoutInflater.from(context))
        dialog = Dialog(context).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(binding.root)
            setCancelable(false)
            setCanceledOnTouchOutside(false)

            window?.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT
            )
            window?.setBackgroundDrawableResource(R.color.transparent)
            window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)

            binding.rootLayout.setOnTouchListener { v, event ->
                if (event.action == MotionEvent.ACTION_DOWN) {
                    val imm =
                        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    currentFocus?.let { focusedView ->
                        imm.hideSoftInputFromWindow(focusedView.windowToken, 0)
                    }
                    v.clearFocus()
                }
                false
            }


            dialog?.setOnDismissListener {
                CommonFunctions.unlockOrientation(context)
            }

            dialog?.setOnShowListener { d: DialogInterface? ->
                CommonFunctions.lockOrientation(context)
            }

            CommonFunctions.lockOrientation(context)

            product?.let { data ->
                binding.apply {
                    val title = PromotionTypeRuleEvaluator.getDiscountTitle(
                        data.discountTypeId ?: 0, data.discountValue ?: 0.0
                    )
                    textViewItemPrice.text = title

                    binding.linearLayoutCount.isVisible = false

                    textViewItemName.text = data.promoName

                    if (data.discountTypeId == PromotionTypeRuleEvaluator.DiscountType.NO_FREE_ITEMS.id) {
                        binding.textViewFreeItem.isVisible = true
                        binding.textViewFreeItem.text =
                            "You can get up to " + data.discountValue?.toInt() + " free items."
                    }






                    imageViewItem.let { image ->
                        Glide.with(image.context).load(
                            ATPreferences.readString(
                                context, Constants.KEY_IMAGE_URL
                            ).toString().plus(data.productImages?.firstOrNull()?.productImage)
                        ).placeholder(R.drawable.loading).into(image);
                    }

                    buttonAddToOrder.isVisible = showAddToOrder
//                    viewDividerBottom.isVisible = showAddToOrder
//                    viewDivider.isVisible = showAddToOrder


                    textViewItemDescription.text = data.promoDescription

                    textViewPlus.setOnClickListener {
                        quantityCount++
                        textViewCount.text = quantityCount.toString()
                    }

                    textViewMinus.setOnClickListener {
                        if (quantityCount > 1) {
                            quantityCount--
                            textViewCount.text = quantityCount.toString()
                        }
                    }


                    val hasRequired = product?.requiredList?.isNotEmpty() == true
                    val requiresAll = product?.requiredAllItemsFlag == 1
                    val conditionName = product?.discountConditionAppliesName?.lowercase() ?: ""

                    var subTotal = 0.0/*
                                        val currentOrderList = SharedPreferenceHelper.getCartItems()
                                        currentOrderList.forEach {
                                            subTotal += if (!it.promoProductList.isNullOrEmpty()) {
                                                // Sum up promo product prices if promoProductList is not empty
                                                it.promoProductList.sumOf { promo ->
                                                    promo.detailSalePrice ?: 0.0
                                                }
                                            } else {
                                                // Sum up regular prices and options if promoProductList is empty
                                                it.productRegularPrice +
                                                        (it.options?.sumOf { option ->
                                                            option.choices?.filter { it.selectedItem }
                                                                ?.sumOf { it.extraPrice }
                                                                ?: 0.0
                                                        } ?: 0.0)
                                            }
                                        }
                    */


                    val labelText =
                        if (selectedProduct != null) "Promotion enabled" else getPromotionConditionLabel(
                            discountTypeId = product?.discountTypeId,
                            discountConditionId = product?.discountConditionAppliesId,
                            hasRequired = product?.requiredList?.isNotEmpty() == true,
                            requiredQty = product?.discountConditionValue?.toInt(),
                            requiredAmount = product?.discountConditionValue,
                            cartAmount = subTotal,
                            currentPromoAmount = 0.0,
                            currentQuantity = 0
                        )

                    binding.textViewRequiredItems.isVisible = labelText.isNotEmpty()
                    binding.textViewRequiredItems.text = labelText
                    when (labelText) {
                        "Promotion enabled" -> {
                            binding.textViewRequiredItems.setTextColor(context.getColor(R.color.colorWhite))
                            binding.textViewRequiredItems.backgroundTintList =
                                ContextCompat.getColorStateList(
                                    context, R.color.colorGreenLogo
                                )
                        }

                        else -> {
                            binding.textViewRequiredItems.setTextColor(context.getColor(R.color.colorText))
                            binding.textViewRequiredItems.backgroundTintList =
                                ContextCompat.getColorStateList(
                                    context, R.color.colorShiftGrey
                                )
                        }
                    }


                    val allowAppliedItemSelection =
                        !hasRequired && (requiresAll || conditionName.contains("quantity"))


                    val productListing: ArrayList<Int> = arrayListOf()
                    val mapOfProductStock: HashMap<Int, Boolean> = hashMapOf()
                    CommonFunctions.promotionCombinedProductList.forEach {
                        productListing.add(it.item.productId ?: 0)
                    }

                    /*                    CoroutineScope(Dispatchers.Main).launch {

                                            val uniqueProducts = productListing.distinct()

                                            val results = uniqueProducts.map { productId ->
                                                async(Dispatchers.IO) {
                                                    productId to isInStock(productId)
                                                }
                                            }.awaitAll()

                                            results.forEach { (id, inStock) ->
                                                mapOfProductStock[id] = inStock
                                            }*/
                    // Now assign the adapter after declaring the var
                    val inventoryIndexMap = buildInventoryIndex(inventoryMap)
                    promotionsItemsAdapter = PromotionsItemsAdapter(
                        itemList = CommonFunctions.promotionCombinedProductList,
                        allProductsList = CommonFunctions.promotionActiveProductResponse,
                        discountValue = data.discountValue,
                        discountConditionAppliesId = data.discountConditionAppliesId,
                        discountConditionValue = data.discountConditionValue,
                        allowAppliedSelection = allowAppliedItemSelection,
                        textViewRequiredItems = binding.textViewRequiredItems,
                        hasRequired = hasRequired,
                        totalCartAmount = subTotal,
                        discountTypeId = data.discountTypeId,
                        selectedProduct = selectedProduct,
                        mapOfProductStock = mapOfProductStock,
                        inventoryIndexMap = inventoryIndexMap,
                        onItemClick = { appliedItem ->
//                            recalculatePromoUI(binding,product, hasRequired, appliedItem)
                            finalAmount = 0.0
                            finalAmount = fetchCurrentPromoTotalAmount(
                                product = product,
                                hasRequired = hasRequired,
                                selectedListItem = appliedItem
                            )

                            recalculatePromoUI(
                                binding,
                                product,
                                hasRequired,
                                appliedItem,
                                finalAmount,
                                promotionsItemsAdapter
                            )

                            Log.d("TAG", "fetchCurrentPromoTotalAmount: " + finalAmount)


//                            textViewTotalPrice.text = "$$subTotal"

                            val labelText = getPromotionConditionLabel(
                                discountTypeId = product?.discountTypeId,
                                discountConditionId = product?.discountConditionAppliesId,
                                hasRequired = product?.requiredList?.isNotEmpty() == true,
                                requiredQty = product?.discountConditionValue?.toInt(),
                                requiredAmount = product?.discountConditionValue,
                                cartAmount = subTotal,
                                currentPromoAmount = finalAmount,
                                currentQuantity = currentQuantity(hasRequired),
                                selectedListItem = appliedItem
                            )

                            val requiredTotal =
                                data?.discountConditionValue?.toInt()?.toDouble() ?: 0.0

                            val matchedWrapper =
                                promotionsItemsAdapter?.itemList?.find { it.item.productId == appliedItem?.productId }

                            val isClickedItemFromRequired = matchedWrapper?.isApplied == false

                            var totalSelectedQty = 0

                            if (hasRequired) {
                                var qnty = 0
                                promotionsItemsAdapter?.itemList?.forEach {
                                    if (!it.isApplied) {
                                        qnty = qnty + (it.item.quantity ?: 0)
                                    }
                                }
                                totalSelectedQty = qnty
                            } else {
                                totalSelectedQty = promotionsItemsAdapter?.itemList?.sumOf {
                                    it.item.quantity ?: 0
                                } ?: 0
                            }

//                            val isRequirementMet = totalSelectedQty >= requiredTotal
                            /*                            val isRequirementMet = binding.textViewRequiredItems.text == "Promotion enabled"



                                                    promotionsItemsAdapter?.updatePromotionState(
                                                        isRequirementMet,
                                                        hasRequired
                                                    )*/

                            /*                         if (isClickedItemFromRequired && totalSelectedQty >= requiredTotal && (appliedItem?.isSelected != true || (appliedItem.quantity
                                                             ?: 0) == 0)
                                                     ) {
                                                         Toast.makeText(
                                                             context,
                                                             "You can only select "+requiredTotal.toInt() +" required items.",
                                                             Toast.LENGTH_SHORT
                                                         ).show()
                                                         return@PromotionsItemsAdapter
                                                     }*/

                            val anyItemSelected = promotionsItemsAdapter?.itemList?.any {
                                it.item.isSelected == true || (it.item.quantity ?: 0) > 0
                            } ?: false

                            binding.textViewSelectItem.isVisible = !anyItemSelected

                            /*   binding.textViewRequiredItems.text = getPromotionRequirementMessage(
                               remaining = remaining,
                               isAmount = data.discountConditionAppliesId == PromotionTypeRuleEvaluator.DiscountConditionType.TOTAL_PURCHASE.id
                           )*/



                            binding.textViewRequiredItems.text = labelText


                            when (labelText) {
                                "Promotion enabled" -> {
                                    binding.textViewRequiredItems.setTextColor(
                                        context.getColor(
                                            R.color.colorWhite
                                        )
                                    )
                                    binding.textViewRequiredItems.backgroundTintList =
                                        ContextCompat.getColorStateList(
                                            context, R.color.colorGreenLogo
                                        )
                                }

                                else -> {
                                    binding.textViewRequiredItems.setTextColor(
                                        context.getColor(
                                            R.color.colorText
                                        )
                                    )
                                    binding.textViewRequiredItems.backgroundTintList =
                                        ContextCompat.getColorStateList(
                                            context, R.color.colorShiftGrey
                                        )
                                }
                            }
                            val isRequirementMet =
                                binding.textViewRequiredItems.text == "Promotion enabled"

                            promotionsItemsAdapter?.updatePromotionState(
                                isRequirementMet, hasRequired, selectedProduct
                            )
                        }

                    )

                    recyclerViewPromotionsItems.adapter = promotionsItemsAdapter
                    promotionsItemsAdapter?.recyclerView = recyclerViewPromotionsItems

//                    }

                    if (selectedProduct == null) {
                        promotionsItemsAdapter?.itemList?.forEach {
                            it.item.quantity = null
                            it.item.isSelected = false
                        }
                    }

                    Handler(Looper.getMainLooper()).postDelayed({
                        val isRequirementMet =
                            binding.textViewRequiredItems.text == "Promotion enabled"

                        promotionsItemsAdapter?.updatePromotionState(
                            isRequirementMet, hasRequired, selectedProduct
                        )
                    }, 500)


//                    buttonAddToOrder.isEnabled = true
                    buttonAddToOrder.setOnClickListener {
                        promotionsItemsAdapter?.itemList?.forEach {
                            Log.d(
                                "TAGHEED",
                                "show: " + it.item.productId + "   " + CommonFunctions.hexToASCII(it.item.productName) + "   " + it.item.quantity
                            )
                        }

                        val hasRequiredItems =
                            promotionsItemsAdapter?.itemList?.any { !it.isApplied } == true


                        val isRequirementMet =
                            binding.textViewRequiredItems.text == "Promotion enabled" || labelText == ""

                        val anyItemSelected = promotionsItemsAdapter?.itemList?.any {
                            it.item.isSelected == true || (it.item.quantity ?: 0) > 0
                        } ?: false

//                        if (labelText != "Promotion enabled") {
                        // ❌ Block: Nothing selected
                        if (!anyItemSelected) {
                            Toast.makeText(
                                context, "Please select an item to start.", Toast.LENGTH_SHORT
                            ).show()
                            return@setOnClickListener
                        }

                        // ❌ Block: Quantity condition not satisfied
                        if (!isRequirementMet) {
                            Toast.makeText(
                                context, labelText, Toast.LENGTH_SHORT
                            ).show()
                            return@setOnClickListener
                        }

                        // ❌ Block: Required items present but none selected
                        if (hasRequiredItems) {
                            val selectedRequiredItem = promotionsItemsAdapter?.itemList?.any {
                                it.isApplied && (it.item.quantity ?: 0) > 0
                            } == true


                            if (!selectedRequiredItem) {
                                Toast.makeText(
                                    context,
                                    "You must select at least one required item.",
                                    Toast.LENGTH_SHORT
                                ).show()
                                return@setOnClickListener
                            }
                        }

                        // ❌ Block: Missing choices
                        val hasMissingChoices = promotionsItemsAdapter?.itemList?.any { wrapper ->
                            val item = wrapper.item
                            val quantity = item.quantity ?: 0

                            if (quantity > 0 && !item.options.isNullOrEmpty()) {
                                item.options?.any { option ->
                                    option.choices?.none { it.selectedItem == true } == true
                                } ?: false
                            } else false
                        } == true

                        if (hasMissingChoices) {
                            Toast.makeText(
                                context,
                                "Please select choices for all product options.",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@setOnClickListener
                        }

                        val selectedProducts = promotionsItemsAdapter?.itemList?.filter {
                            it.item.quantity != null && (it.item.isSelected == true || (it.item.quantity
                                ?: 0) > 0)
                        }?.map { it.item } ?: emptyList()


                        val promoResponse = selectedProducts.toPromoProductList(product)

                        /*
                                                SharedPreferenceHelper.getCartItems().apply {
                                                    val existingIndex =
                                                        indexOfFirst { it.productId == selectedProduct?.discountId } // or use productId depending on your model

                                                    if (existingIndex >= 0) {
                                                        // Update existing
                                                        this[existingIndex] = promoResponse
                                                    } else {
                                                        // Add new
                                                        add(promoResponse)
                                                    }

                                                    SharedPreferenceHelper.saveCartItems(this)
                                                }
                        */

                        /* SharedPreferenceHelper.getCartItems().apply {
                             add(promoResponse)
                             SharedPreferenceHelper.saveCartItems(this)
                         }*/

                        Log.d("PROMO_DEBUG", "Added Promo: ${Gson().toJson(promoResponse)}")

                        ModelManager.getInstance().getShoppingCartManager().addItemTOCart(
                            context,
                            Operations.makeJsonAddToCartItems(
                                context,
                                "1",
                                Utils.lengtT(11, selectedPromotionId),
                                Utils.lengtT(11, merchantId),
                                "",
                                "",
                                ""
                            )
                        )


                        val optListArray = JSONArray()

                        promoResponse.promoProductList?.forEach { promoItem ->
                            val childProductId = promoItem.productId?.toString() ?: return@forEach
                            val detailQty = promoItem.detailQty?.toString() ?: "1"

                            // collect options (valueId) from details
                            val optionIds = promoItem.details?.flatMap {
                                it.options?.mapNotNull { opt -> opt.valueId?.toString() }
                                    ?: emptyList()
                            } ?: emptyList()

                            val optionId = optionIds.getOrNull(0) ?: ""
                            val optionId2 = optionIds.getOrNull(1) ?: ""

                            // decide role (R = required, A = applied)
                            val role = if (promoItem.isRequiredItem == true) "R" else "A"

                            // build PARAM object
                            val paramObj = JSONObject().apply {
                                put(
                                    "53",
                                    Utils.lengtT(
                                        11,
                                        ATPreferences.readString(context, Constants.KEY_USERID)
                                    )
                                )
                                put(
                                    "114.144",
                                    Utils.lengtT(11, childProductId)
                                )                 // child product
                                put(
                                    "600.6",
                                    Utils.lengtT(11, promoResponse.productId.toString())
                                ) // parent = promotionId
                                put(
                                    "600.7",
                                    detailQty
                                )                                          // quantity
                                put(
                                    "114.179",
                                    Utils.lengtT(11, merchantId)
                                )
                                put(
                                    "114.121",
                                    detailQty
                                )  // parent qty
                                put("121.55", promoResponse.productNotes ?: "")
                                put("600.2", role)

                                // options array
                                val chArray = JSONArray()
                                if (optionId.isNotEmpty()) chArray.put(
                                    JSONObject().put(
                                        "121.104",
                                        Utils.lengtT(11, optionId)
                                    )
                                )
                                if (optionId2.isNotEmpty()) chArray.put(
                                    JSONObject().put(
                                        "121.104",
                                        Utils.lengtT(11, optionId2)
                                    )
                                )
                                put("CH", chArray)
                            }

                            // wrap with 101 + PARAM
                            val itemObj = JSONObject().apply {
                                put("101", "030400198") // API code
                                put("PARAM", paramObj)
                            }

                            optListArray.put(itemObj)
                        }

// now build final request
                        val finalRequest = JSONObject().apply {
                            put("11", Client.getTimeStamp())
                            put(
                                "192",
                                ATPreferences.readString(context, Constants.KEY_USER_DEFAULT)
                            )
                            put("122.45", "en")
                            put("OPTLST", optListArray)
                        }

                        listener?.onAddToPromoCart(finalRequest)

                        dismissDialog(context)
                    }


                }
            }


            binding.imageViewClose.setOnClickListener {
                optionId = 0
                dismissDialog(context)
            }


            binding.buttonCancel.setOnClickListener {
                optionId = 0
                dismissDialog(context)
            }

            show()
        }
        return dialog!!
    }

    private fun currentQuantity(hasRequired: Boolean): Int {
        var totalSelectedQty = 0

        if (hasRequired) {
            var qnty = 0
            promotionsItemsAdapter?.itemList?.forEach {
                if (!it.isApplied) {
                    qnty = qnty + (it.item.quantity ?: 0)
                }
            }
            totalSelectedQty = qnty
        } else {
            totalSelectedQty =
                promotionsItemsAdapter?.itemList?.sumOf { it.item.quantity ?: 0 } ?: 0

        }
        return totalSelectedQty
    }


    private fun fetchCurrentPromoTotalAmount(
        product: PromotionListingResponse,
        hasRequired: Boolean?,
        selectedListItem: AppliedListItem? = null
    ): Double {
        val type = product.discountTypeId
        val condition =
            PromotionTypeRuleEvaluator.DiscountConditionType.from(product.discountConditionAppliesId)

        var requiredItemsTotal = 0.0
        var appliedItemsTotal = 0.0

        promotionsItemsAdapter?.itemList?.forEach { productItemWrapper ->
            val item = productItemWrapper.item
            val quantity = item.quantity ?: 0
            if (quantity <= 0) return@forEach

            // Determine base price depending on promo type + condition + hasRequired
            val basePrice: Double = when (type) {
                PromotionTypeRuleEvaluator.DiscountType.SPECIAL_PRICE.id -> {
                    if (condition == PromotionTypeRuleEvaluator.DiscountConditionType.QTY_ALL || condition == PromotionTypeRuleEvaluator.DiscountConditionType.QTY_EACH) {
                        // FIX: handle "with required" vs "no required"
                        if (hasRequired == true) {
                            if (item.isRequiredItem == true) item.productRegularPrice ?: 0.0
                            else product.discountValue ?: 0.0
                        } else {
                            product.discountValue ?: 0.0
                        }
                    } else {
                        // For other conditions (e.g. Total Purchase)
                        if (hasRequired == true) {
                            if (item.isRequiredItem == true) item.productRegularPrice ?: 0.0
                            else product.discountValue ?: 0.0
                        } else {
                            if (item.isRequiredItem == true) item.productRegularPrice ?: 0.0
                            else product.discountValue ?: 0.0
                        }
                    }
                }

                PromotionTypeRuleEvaluator.DiscountType.SPECIAL_PRICE_TOTAL.id -> {
                    return product.discountValue ?: 0.0
                }


                PromotionTypeRuleEvaluator.DiscountType.NO_FREE_ITEMS.id -> {
                    // If required list exists → required items full price, applied items free
                    if (hasRequired == true) {
                        if (item.isRequiredItem == true) item.productRegularPrice ?: 0.0
                        else 0.0
                    } else {
                        // If somehow no required list, keep regular price for all
                        item.productRegularPrice ?: 0.0
                    }
                }

                PromotionTypeRuleEvaluator.DiscountType.PERCENT_OFF.id -> {
                    // Percent Off – apply to applied list only, required items at full price
                    val pct = (product.discountValue ?: 0.0) / 100.0
                    if (hasRequired == true) {
                        if (item.isRequiredItem == true) {
                            item.productRegularPrice ?: 0.0
                        } else {
                            val regularPrice = item.productRegularPrice ?: 0.0
                            val discountAmount = regularPrice * pct
                            (regularPrice - discountAmount).coerceAtLeast(0.0)
                        }
                    } else {
                        // No required list → all applied items get the percent off
                        val regularPrice = item.productRegularPrice ?: 0.0
                        val discountAmount = regularPrice * pct
                        (regularPrice - discountAmount).coerceAtLeast(0.0)
                    }
                }


                else -> {
                    // Default: use regular product price for other discount types
                    item.productRegularPrice ?: 0.0
                }
            }

            // Add extra choice prices (options extras are added on top of the base)
            val extraChoicePrice = item.options?.sumOf { option ->
                option.choices?.filter { it.selectedItem == true }?.sumOf { it.extraPrice ?: 0.0 }
                    ?: 0.0
            } ?: 0.0

            val unitPrice = (basePrice + extraChoicePrice).coerceAtLeast(0.0)
            val total = unitPrice * quantity

            // Use the flag on the item itself to split totals
            if (item.isRequiredItem == true) {
                requiredItemsTotal += total
            } else {
                appliedItemsTotal += total
            }
        }

        // Final applied total adjustments by discount type
        val finalAppliedTotal = when (type) {
            PromotionTypeRuleEvaluator.DiscountType.SPECIAL_PRICE.id -> {
                // For special price each, appliedItemsTotal already uses discountValue where appropriate
                appliedItemsTotal
            }

            PromotionTypeRuleEvaluator.DiscountType.PRICE_OFF.id -> {
                // $ off — (current logic subtracts discountValue from applied total)
                (appliedItemsTotal - (product.discountValue ?: 0.0)).coerceAtLeast(0.0)
            }

            PromotionTypeRuleEvaluator.DiscountType.PERCENT_OFF.id -> {
                val pct = (product.discountValue ?: 0.0) / 100.0
                val discount = appliedItemsTotal * pct
                (appliedItemsTotal - discount).coerceAtLeast(0.0)
            }

            else -> appliedItemsTotal
        }

        return requiredItemsTotal + finalAppliedTotal
    }

    fun dismissDialog(
        activity: Activity,
    ) {
        CommonFunctions.unlockOrientation(activity)
        optionId = 0

        val adapter = promotionsItemsAdapter ?: return
        val items = adapter.itemList ?: return

        items.forEach { wrapper ->
            wrapper.item.quantity = 1
            wrapper.item.isSelected = false

            wrapper.item.options?.forEach { option ->
                option.selectedItem = false
                option.choices?.forEach { choice ->
                    choice.selectedItem = false
                }
            }
        }

        adapter.itemList = arrayListOf() // Clear list for GC
        promotionsItemsAdapter = null
        dialog?.dismiss()
        dialog = null
    }

    fun List<AppliedListItem>.toPromoProductList(
        parentProduct: PromotionListingResponse
    ): ProductByIdResponse {
        val discountValue = parentProduct.discountValue ?: 0.0
        val requiredIds = parentProduct.requiredList?.mapNotNull { it.productId } ?: emptyList()
        val condition =
            PromotionTypeRuleEvaluator.DiscountConditionType.from(parentProduct.discountConditionAppliesId)
        val hasRequiredItems = parentProduct.requiredList?.isNotEmpty() == true
        val type = parentProduct.discountTypeId
        var isSpecialPriceTotal = false

        val promoProductItems = this.mapNotNull { item ->
            val quantity = item.quantity ?: 0
            if ((item.isSelected == true || quantity > 0)) {

//                val isRequiredItem = requiredIds.contains(item.productId)

                val isRequiredItem = item.isRequiredItem == true


                // per-item extra from selected choices
                val perItemExtra = item.options?.sumOf { option ->
                    option.choices?.filter { it.selectedItem == true }
                        ?.sumOf { it.extraPrice ?: 0.0 } ?: 0.0
                } ?: 0.0

                // base price per unit (NOTE: for PERCENT_OFF we keep base = regular price,
                // discount will be applied later in discountTotal)
                val basePrice = when (type) {

                    PromotionTypeRuleEvaluator.DiscountType.SPECIAL_PRICE.id -> {
                        if (condition == PromotionTypeRuleEvaluator.DiscountConditionType.QTY_ALL || condition == PromotionTypeRuleEvaluator.DiscountConditionType.QTY_EACH) {
                            // Special Price each - Qty based
                            if (hasRequiredItems) {
                                if (isRequiredItem) item.productRegularPrice ?: 0.0
                                else discountValue // applied items at promo fixed price
                            } else {
                                // No required list → all applied items at discountValue
                                discountValue
                            }
                        } else {
                            // Total Purchase / other conditions
                            if (hasRequiredItems) {
                                if (isRequiredItem) item.productRegularPrice ?: 0.0
                                else discountValue
                            } else {
                                if (isRequiredItem) item.productRegularPrice ?: 0.0
                                else discountValue
                            }
                        }
                    }

                    PromotionTypeRuleEvaluator.DiscountType.SPECIAL_PRICE_TOTAL.id -> {
                        // total handled separately (finalAmount)
                        item.productRegularPrice ?: 0.0
                    }

                    PromotionTypeRuleEvaluator.DiscountType.NO_FREE_ITEMS.id -> {
                        // Required items full price, applied items free
                        if (hasRequiredItems) {
                            if (isRequiredItem) item.productRegularPrice ?: 0.0 else 0.0
                        } else {
                            item.productRegularPrice ?: 0.0
                        }
                    }

                    PromotionTypeRuleEvaluator.DiscountType.PERCENT_OFF.id -> {
                        // DON'T apply percent here — keep regular price and compute percent as discountTotal below
                        item.productRegularPrice ?: 0.0
                    }

                    else -> {
                        // PRICE_OFF and other types default to regular price here
                        item.productRegularPrice ?: 0.0
                    }
                }

                // per-item price (base + extras)
//                val perItemPrice = (basePrice + perItemExtra).coerceAtLeast(0.0)
                val perItemPrice = (basePrice + perItemExtra).coerceAtLeast(0.0)

                // total for this line (before any discountTotal subtraction)
                var totalRegular = perItemPrice * quantity

                if (type == PromotionTypeRuleEvaluator.DiscountType.PERCENT_OFF.id && !isRequiredItem) {
                    totalRegular = (applyPercentDiscount(
                        basePrice, discountValue
                    ) * quantity) + (perItemExtra * quantity)
                }

                // total discount calculation:
                val discountTotal = when (type) {
                    PromotionTypeRuleEvaluator.DiscountType.PRICE_OFF.id -> {
                        // $ OFF per item (only applied items)
                        if (!isRequiredItem) {
                            (discountValue * quantity).coerceAtMost(totalRegular)
                        } else 0.0
                    }

                    else -> 0.0
                }

                var actualPrice = item.productActualPrice
                var discountPrice = item.productDiscountPrice
                var productRegularPrice = item.productRegularPrice ?: 0.0


                // final sale total after discount (handles special branches like SPECIAL_PRICE, NO_FREE_ITEMS, SPECIAL_PRICE_TOTAL)
                var finalSaleTotal = when (type) {
                    PromotionTypeRuleEvaluator.DiscountType.SPECIAL_PRICE.id -> totalRegular
                    PromotionTypeRuleEvaluator.DiscountType.SPECIAL_PRICE_TOTAL.id -> totalRegular
                    PromotionTypeRuleEvaluator.DiscountType.NO_FREE_ITEMS.id -> if (isRequiredItem) totalRegular else 0.0
                    else -> (totalRegular - discountTotal).coerceAtLeast(0.0)
                }
                if (type == PromotionTypeRuleEvaluator.DiscountType.SPECIAL_PRICE_TOTAL.id) {
//                    if (condition == DiscountConditionType.QTY_ALL)
//                        finalSaleTotal = discountValue

                    productRegularPrice = discountValue
                    discountPrice = actualPrice
                    isSpecialPriceTotal = true
                }


                PromoProductItem(
                    promotionId = parentProduct.discountId,
                    productId = item.productId,
                    productName = CommonFunctions.hexToASCII(item.productName),
                    productPriceCurrencyName = item.productPriceCurrencyName,
                    productImage = item.productImage,
                    isRequiredItem = isRequiredItem,
                    detailQty = quantity,
                    promoActualPrice = actualPrice,
                    promoDiscountPrice = discountPrice,
                    // store total for the promo line (qty included)
                    detailSalePrice = finalSaleTotal,
                    productRegularPrice = productRegularPrice,
                    orderPromotionTotalDiscount = discountTotal,
                    detailChoices = "",
                    details = listOf(
                        PromoDetailItem(options = item.options?.flatMap { option ->
                            option.choices?.filter { it.selectedItem == true }?.map { choice ->
                                PromoChoiceOption(
                                    optionId = choice.optionId,
                                    optionName = choice.optionName,
                                    valueId = choice.valueId,
                                    valueName = choice.valueName,
                                    extraPrice = choice.extraPrice
                                )
                            } ?: emptyList()
                        } ?: emptyList())))
            } else null
        }

        return ProductByIdResponse(
            offlineAdded = true,
            isSpecialPriceTotal = isSpecialPriceTotal,
            productId = parentProduct.discountId,
            productName = CommonFunctions.ASCIIToHex(parentProduct.promoName),
            productImage = parentProduct.productImages?.firstOrNull()?.productImage,
            productQuantity = quantityCount,
            productRegularPrice = finalAmount,
            locationSeatingId = locationSeatingId,
            tableLocationName = tableLocationName,
            tableId = tableId,
            productDelivered = 0,
            productReady = 0,
            promoProductList = promoProductItems
        )
    }

    fun applyPercentDiscount(originalPrice: Double, percent: Double): Double {
        val discountAmount = originalPrice * (percent / 100)
        val discountedPrice = originalPrice - discountAmount
        return round(discountedPrice * 100) / 100  // Round to 2 decimal places
    }

    interface OnPromoCartListener {
        fun onAddToPromoCart(request: JSONObject)
    }

    data class InventoryIndex(
        val productId: Int,
        val choiceSet: Set<Int>,
        val qty: Int
    )

    fun buildInventoryIndex(
        inventoryMap: Map<Int, List<InventoryResponse>>?
    ): Map<Int, List<InventoryIndex>>? {

        return inventoryMap?.mapValues { (_, list) ->
            list.map {
                InventoryIndex(
                    productId = it.tblProductId ?: 0,
                    choiceSet = it.tblProductinventoryChoices
                        ?.split(",")
                        ?.mapNotNull { id -> id.toIntOrNull() }
                        ?.toSet()
                        ?: emptySet(),
                    qty = it.tblProductinventoryQuantity ?: 0
                )
            }
        }
    }

    private fun recalculatePromoUI(
        binding: DialogAddPromoOrderBinding,
        product: PromotionListingResponse,
        hasRequired: Boolean,
        appliedItem: AppliedListItem? = null,
        total: Double? = null,
        promotionsItemsAdapter: PromotionsItemsAdapter?
    ) {
        /*      val total = fetchCurrentPromoTotalAmount(
                  product = product,
                  hasRequired = hasRequired,
                  selectedListItem = appliedItem
              )*/

//        finalAmount = total

        var amount = 0.0

        promotionsItemsAdapter?.itemList?.forEach { wrapper ->
            val item = wrapper.item

            if (item.isSelected != true) return@forEach

            val quantity = item.quantity ?: 1

            // 1️⃣ Base price (discount price)
            val basePrice = if (item.productDiscountPrice?.replace("$", "")
                    .equals("0.0")
            ) item.productActualPrice?.replace("$", "")?.toDoubleOrNull() ?: 0.0
            else item.productDiscountPrice?.replace("$", "")?.toDoubleOrNull() ?: 0.0


            amount += basePrice * quantity

            if (item.options?.isEmpty() == true) {
                Log.d(
                    "TAGEED",
                    "recalculatePromoUI: productRegularPrice=" + wrapper.item.productRegularPrice + "   productActualPrice=" +
                            wrapper.item.productActualPrice + "   productDiscountPrice=" + wrapper.item.productDiscountPrice
                            + "    extraPrice=nill"
                )
            }
            // 2️⃣ Options extra price
            item.options?.forEach { option ->
                if (option.selectedItem == true) {
                    option.choices?.forEach { choice ->
                        if (choice.selectedItem == true) {
                            val extraPrice = choice.extraPrice ?: 0.0
                            amount += extraPrice * quantity

                            Log.d(
                                "TAGEED",
                                "recalculatePromoUI: productRegularPrice=" + wrapper.item.productRegularPrice + "   productActualPrice=" +
                                        wrapper.item.productActualPrice + "   productDiscountPrice=" + wrapper.item.productDiscountPrice
                                        + "    extraPrice=" + choice.extraPrice
                            )
                        }
                    }
                }
            }
        }
        binding.textViewTotalPrice.text = "$${"%.2f".format(amount)}"


        val label = getPromotionConditionLabel(
            discountTypeId = product.discountTypeId,
            discountConditionId = product.discountConditionAppliesId,
            hasRequired = hasRequired,
            requiredQty = product.discountConditionValue?.toInt(),
            requiredAmount = product.discountConditionValue,
            cartAmount = 0.0,
            currentPromoAmount = total,
            currentQuantity = currentQuantity(hasRequired),
            selectedListItem = appliedItem
        )

        binding.textViewRequiredItems.text = label
        val enabled = label == "Promotion enabled" || label.isEmpty()
        updateBottomBarState(binding, enabled)
    }

    private fun updateBottomBarState(binding: DialogAddPromoOrderBinding, isEnabled: Boolean) {
        binding.buttonAddToOrder.isEnabled = isEnabled
        binding.buttonAddToOrder.alpha = if (isEnabled) 1f else 0.5f
    }


}


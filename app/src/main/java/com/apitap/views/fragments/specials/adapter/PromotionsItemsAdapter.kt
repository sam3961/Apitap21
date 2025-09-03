package com.apitap.views.fragments.specials.adapter

import android.content.Context
import android.graphics.Paint
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.apitap.R
import com.apitap.views.fragments.specials.data.AllProductsListResponse
import com.apitap.views.fragments.specials.data.AppliedListItem
import com.apitap.views.fragments.specials.data.ProductItemWrapper
import com.apitap.views.fragments.specials.data.PromotionListingResponse
import com.apitap.views.fragments.specials.utils.CommonFunctions
import com.apitap.views.fragments.specials.utils.PromotionTypeRuleEvaluator
import com.apitap.views.fragments.specials.utils.Utility.isInStock
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PromotionsItemsAdapter(
    var itemList: List<ProductItemWrapper>?,
    private var allProductsList: ArrayList<AllProductsListResponse>?,
    private var discountValue: Double?,
    private var discountConditionValue: Double?,
    private var discountConditionAppliesId: Int?,
    private var discountTypeId: Int?,
    private var selectedProduct: PromotionListingResponse?,
    private var allowAppliedSelection: Boolean,
    private var textViewRequiredItems: AppCompatTextView,
    private var hasRequired: Boolean,
    private var totalCartAmount: Double,
    private val onItemClick: (AppliedListItem?) -> Unit
) : RecyclerView.Adapter<PromotionsItemsAdapter.ViewHolder>() {

    var recyclerView: RecyclerView? = null
    val optionsAdapterMap = mutableMapOf<Int, PromotionOptionsAdapter>()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val linearLayoutName: LinearLayout = itemView.findViewById(R.id.linearLayoutName)
        val textViewName: AppCompatTextView = itemView.findViewById(R.id.textViewName)
        val textViewOutOfStock: AppCompatTextView = itemView.findViewById(R.id.textViewRequired)
        val checkBox: AppCompatCheckBox = itemView.findViewById(R.id.checkBox)
        val checkBoxName: AppCompatCheckBox = itemView.findViewById(R.id.checkBoxName)
        val linearLayoutRoot: LinearLayout = itemView.findViewById(R.id.linearLayoutRoot)
        val linearAddItem: LinearLayout = itemView.findViewById(R.id.linearAddItem)
        val textViewDiscountPrice: AppCompatTextView =
            itemView.findViewById(R.id.textViewDiscountPrice)
        val textViewActualPrice: AppCompatTextView = itemView.findViewById(R.id.textViewActualPrice)
        val textViewPlus: AppCompatTextView = itemView.findViewById(R.id.textViewPlus)
        val textViewMinus: AppCompatTextView = itemView.findViewById(R.id.textViewMinus)
        val textViewCount: AppCompatTextView = itemView.findViewById(R.id.textViewCount)
        val recyclerViewPromotionsOptions: RecyclerView =
            itemView.findViewById(R.id.recyclerViewPromotionsOptions)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_promotion_items, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = itemList?.get(position) ?: return
        val productId = data.item.productId ?: return

        val isPriceOff = discountTypeId == PromotionTypeRuleEvaluator.DiscountType.PRICE_OFF.id
        val isPercentOff = discountTypeId == PromotionTypeRuleEvaluator.DiscountType.PERCENT_OFF.id
        val isSpecialPrice =
            discountTypeId == PromotionTypeRuleEvaluator.DiscountType.SPECIAL_PRICE.id
        val isNumberOfFreeItems =
            discountTypeId == PromotionTypeRuleEvaluator.DiscountType.NO_FREE_ITEMS.id
        val isSpecialPriceTotal =
            discountTypeId == PromotionTypeRuleEvaluator.DiscountType.SPECIAL_PRICE_TOTAL.id

        val isQtyEach =
            discountConditionAppliesId == PromotionTypeRuleEvaluator.DiscountConditionType.QTY_EACH.id
        val isQtyAll =
            discountConditionAppliesId == PromotionTypeRuleEvaluator.DiscountConditionType.QTY_ALL.id
        val isTotalPurchase =
            discountConditionAppliesId == PromotionTypeRuleEvaluator.DiscountConditionType.TOTAL_PURCHASE.id

        val conditionQty = (discountConditionValue ?: 1.0).toInt()

        val existingAdapter = optionsAdapterMap[productId]


        val promotionsOptionAdapter =
            existingAdapter ?: PromotionOptionsAdapter(
                this,
                data.item.productId,
                data.item.options, selectedProduct
            ) {

                //call api

                //when get response then run below code if error then show message and returns from it no below code execute
                var initialQty = 1
                if (isQtyEach && !hasRequired) {
                    initialQty = conditionQty
                } else if (isQtyEach && data.item.isRequiredItem == true)
                    initialQty = conditionQty

                data.item.quantity = initialQty
                holder.textViewCount.text = initialQty.toString()
                data.item.isSelected = true
                holder.linearAddItem.isVisible = true

                holder.checkBoxName.isVisible = it?.optionId != null
                if (holder.checkBoxName.isVisible) {
                    holder.checkBoxName.isChecked = true
                }

                onItemClick.invoke(data.item)
            }.also { optionsAdapterMap[productId] = it }
        holder.recyclerViewPromotionsOptions.adapter = promotionsOptionAdapter

        holder.checkBoxName.setOnCheckedChangeListener { _, isChecked ->
            if (!isChecked) {
                data.item.options?.forEach {
                    it.selectedItem = false
                }
                data.item.quantity = null
                data.item.isSelected = false
                holder.checkBoxName.isVisible = false
                holder.linearAddItem.isVisible = false
                holder.recyclerViewPromotionsOptions.isVisible = false
                optionsAdapterMap[productId]?.clearAllChoicesSelections()
                onItemClick.invoke(data.item)
            }
        }


        val productName = CommonFunctions.hexToASCII(data.item.productName)
        val regularPrice = data.item.productRegularPrice ?: 0.0
        val quantity = data.item.quantity ?: 0
        val isSelected = data.item.isSelected == true

        val styledName = getStyledProductName(
            holder.itemView.context,
            productName ?: "",
            data.item.isRequiredItem ?: false
        )


        if (data.item.options?.isNotEmpty() == true) {
            holder.linearLayoutName.isVisible = true
            holder.textViewName.text = styledName
            holder.checkBox.isVisible = false
        } else {
            holder.linearLayoutName.isVisible = false
            holder.checkBox.isVisible = true
            holder.checkBox.text = styledName
        }

        holder.linearAddItem.isVisible = isSelected && quantity > 0
        holder.textViewCount.text = if (quantity > 0) quantity.toString() else "1"

        setupCheckbox(holder, data, isQtyEach, conditionQty, isQtyAll)

        //chat gpt
        holder.textViewPlus.setOnClickListener {
            val updated = (data.item.quantity ?: 0) + 1
            val conditionQty = (discountConditionValue ?: 1.0).toInt()

            // ✅ NO FREE ITEMS limit check
            if (discountTypeId == PromotionTypeRuleEvaluator.DiscountType.NO_FREE_ITEMS.id) {
                val maxFreeItems = (discountValue ?: 0.0).toInt()
                var totalAppliedQty = 0

                itemList?.forEach {
                    if (it.isApplied) {
                        totalAppliedQty += (it.item.quantity ?: 0)
                    }
                }

                if (totalAppliedQty >= maxFreeItems && data.isApplied) {
                    Toast.makeText(
                        holder.itemView.context,
                        "You can only have $maxFreeItems free item(s) in this promotion.",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }
            }

            if (isQtyAll) {

                var checkCondition = false
                var totalQtyInTargetList = 0
                itemList?.forEach {
                    if (hasRequired && !it.isApplied) {
                        checkCondition = true
                        totalQtyInTargetList = totalQtyInTargetList + (it.item.quantity ?: 0)
                    } else if (!hasRequired && it.isApplied) {
                        checkCondition = true
                        totalQtyInTargetList = totalQtyInTargetList + (it.item.quantity ?: 0)
                    } else if (hasRequired && data.isApplied) {
                        checkCondition = false
                    }
                }

                if (totalQtyInTargetList >= conditionQty && checkCondition) {
                    Toast.makeText(
                        holder.itemView.context,
                        "You can only add $conditionQty item(s) in this promotion.",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }
            }

            data.item.quantity = updated
            data.item.isSelected = true
            holder.textViewCount.text = updated.toString()
            holder.linearAddItem.isVisible = true
            onItemClick.invoke(data.item)
        }


        holder.textViewMinus.setOnClickListener {
            val current = data.item.quantity ?: 1
//            val minAllowedQty = if (isQtyEach) conditionQty else 1
            val minAllowedQty =
                if (isQtyEach && data.item.isRequiredItem == true) conditionQty else 1
            if (current > minAllowedQty) {
                data.item.quantity = current - 1
                holder.textViewCount.text = (current - 1).toString()
            } else {
                if (!isQtyEach) {
                    holder.checkBox.isChecked = false
                    holder.checkBoxName.isChecked = false
                    data.item.quantity = 0
                    data.item.isSelected = false
                    holder.linearAddItem.isVisible = false
                    holder.recyclerViewPromotionsOptions.isVisible = false
                    holder.textViewCount.text = "1"
                    optionsAdapterMap[productId]?.clearAllChoicesSelections()
                } else {
                    Toast.makeText(
                        holder.itemView.context,
                        "Minimum quantity is $minAllowedQty",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            onItemClick.invoke(data.item)
        }

        holder.itemView.setOnClickListener {
            if (!holder.linearLayoutRoot.isEnabled) return@setOnClickListener

            if (data.item.options?.isNotEmpty() == true && data.item.isSelected == true) {
                holder.recyclerViewPromotionsOptions.isVisible =
                    !holder.recyclerViewPromotionsOptions.isVisible
            } else {

                if (discountTypeId == PromotionTypeRuleEvaluator.DiscountType.NO_FREE_ITEMS.id) {
                    val maxFreeItems = (discountValue ?: 0.0).toInt()
                    var totalAppliedQty = 0
                    itemList?.forEach {
                        if (it.isApplied) {
                            totalAppliedQty += (it.item.quantity ?: 0)
                        }
                    }
                    if (totalAppliedQty >= maxFreeItems && data.isApplied) {
                        Toast.makeText(
                            holder.itemView.context,
                            "You can only have $maxFreeItems free item(s) in this promotion.",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@setOnClickListener
                    }
                }


                var checkCondition = false
                var totalQtyInTargetList = 0
                itemList?.forEach {
                    if (hasRequired && !it.isApplied) {
                        checkCondition = true
                        totalQtyInTargetList = totalQtyInTargetList + (it.item.quantity ?: 0)
                    } else if (!hasRequired && it.isApplied) {
                        checkCondition = true
                        totalQtyInTargetList = totalQtyInTargetList + (it.item.quantity ?: 0)
                    } else if (hasRequired && data.isApplied) {
                        checkCondition = false
                    }
                }

                if (totalQtyInTargetList >= conditionQty && checkCondition && isQtyAll) {
                    Toast.makeText(
                        holder.itemView.context,
                        "You can only add $conditionQty item(s) in this promotion.",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }

                if (data.item.options?.isNotEmpty() == true) {
                    holder.recyclerViewPromotionsOptions.isVisible =
                        !holder.recyclerViewPromotionsOptions.isVisible
                }
            }
        }

        // end of item click listener


        // check with prices
        if (isPriceOff) {
            val hasDiscount = data.isApplied && (discountValue ?: 0.0) > 0.0
            val discountedPrice =
                CommonFunctions.subtractAndFormat(regularPrice, discountValue ?: 0.0)
            holder.textViewDiscountPrice.text = if (hasDiscount) discountedPrice else ""
            holder.textViewDiscountPrice.isVisible = hasDiscount

            holder.textViewActualPrice.text =
                CommonFunctions.formatPriceWithDollarSymbol(regularPrice)
            holder.textViewActualPrice.paintFlags = if (hasDiscount)
                holder.textViewActualPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            else
                holder.textViewActualPrice.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()

            data.item.productActualPrice = holder.textViewActualPrice.text.toString()

            if (holder.textViewDiscountPrice.isVisible) {
                data.item.productDiscountPrice = holder.textViewDiscountPrice.text.toString()
            } else {
                data.item.productDiscountPrice = "$0.0"
            }

        } else if (isPercentOff) {
            val hasDiscount = data.isApplied && (discountValue ?: 0.0) > 0.0
            val percentage = discountValue ?: 0.0

            // Calculate discounted price using percentage
            val discountAmount = (regularPrice * percentage) / 100
            val discountedPrice = CommonFunctions.subtractAndFormat(regularPrice, discountAmount)

            holder.textViewDiscountPrice.text = if (hasDiscount) discountedPrice else ""
            holder.textViewDiscountPrice.isVisible = hasDiscount

            holder.textViewActualPrice.text =
                CommonFunctions.formatPriceWithDollarSymbol(regularPrice)
            holder.textViewActualPrice.paintFlags = if (hasDiscount)
                holder.textViewActualPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            else
                holder.textViewActualPrice.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()

            data.item.productActualPrice = holder.textViewActualPrice.text.toString()
            if (holder.textViewDiscountPrice.isVisible) {
                data.item.productDiscountPrice = holder.textViewDiscountPrice.text.toString()
            } else {
                data.item.productDiscountPrice = "$0.0"
            }
        } else if (isSpecialPrice) {
            val hasDiscount = data.isApplied && (discountValue ?: 0.0) > 0.0
            val specialPrice = discountValue ?: 0.0
            val isRequiredItem = !data.isApplied

            if (isRequiredItem) {
                // Required item: show regular price only, no discount
                holder.textViewDiscountPrice.text = ""
                holder.textViewDiscountPrice.isVisible = false

                holder.textViewActualPrice.text =
                    CommonFunctions.formatPriceWithDollarSymbol(regularPrice)
                holder.textViewActualPrice.isVisible = true
                holder.textViewActualPrice.paintFlags =
                    holder.textViewActualPrice.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            } else {
                // Applied item: show special price and strikethrough if applicable
                val showStrikethrough = regularPrice > specialPrice

                holder.textViewDiscountPrice.text = if (hasDiscount)
                    CommonFunctions.formatPriceWithDollarSymbol(specialPrice)
                else
                    ""
                holder.textViewDiscountPrice.isVisible = hasDiscount

                holder.textViewActualPrice.text =
                    CommonFunctions.formatPriceWithDollarSymbol(regularPrice)
                holder.textViewActualPrice.isVisible = showStrikethrough
                holder.textViewActualPrice.paintFlags = if (showStrikethrough)
                    holder.textViewActualPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                else
                    holder.textViewActualPrice.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }

            data.item.productActualPrice = holder.textViewActualPrice.text.toString()
            if (holder.textViewDiscountPrice.isVisible) {
                data.item.productDiscountPrice = holder.textViewDiscountPrice.text.toString()
            } else {
                data.item.productDiscountPrice = "$0.0"
            }
        } else if (isNumberOfFreeItems) {
            val isRequiredItem = !data.isApplied // requiredList items are not marked as applied
            val isFreeItem = data.isApplied      // free items are marked as applied

            if (isRequiredItem) {
                // Required item: Show regular price only
                holder.textViewDiscountPrice.text = ""
                holder.textViewDiscountPrice.isVisible = false

                holder.textViewActualPrice.text =
                    CommonFunctions.formatPriceWithDollarSymbol(regularPrice)
                holder.textViewActualPrice.isVisible = true
                holder.textViewActualPrice.paintFlags =
                    holder.textViewActualPrice.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()

            } else if (isFreeItem) {
                // Free item: Show "Free", no original price
                holder.textViewDiscountPrice.text = "Free"
                holder.textViewDiscountPrice.isVisible = true

                holder.textViewActualPrice.text = ""
                holder.textViewActualPrice.isVisible = false
                holder.textViewActualPrice.paintFlags =
                    holder.textViewActualPrice.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }
            data.item.productActualPrice = holder.textViewActualPrice.text.toString()
            if (holder.textViewDiscountPrice.isVisible) {
                data.item.productDiscountPrice = holder.textViewDiscountPrice.text.toString()
            } else {
                data.item.productDiscountPrice = "$0.0"
            }
        } else if (isSpecialPriceTotal) {
            // Always show the regular price
            holder.textViewDiscountPrice.text = ""
            holder.textViewDiscountPrice.isVisible = false

            holder.textViewActualPrice.text =
                CommonFunctions.formatPriceWithDollarSymbol(regularPrice)
            holder.textViewActualPrice.isVisible = true

            // Ensure no strike-through
            holder.textViewActualPrice.paintFlags =
                holder.textViewActualPrice.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()

            data.item.productActualPrice = holder.textViewActualPrice.text.toString()
            if (holder.textViewDiscountPrice.isVisible) {
                data.item.productDiscountPrice = holder.textViewDiscountPrice.text.toString()
            } else {
                data.item.productDiscountPrice = "$0.0"
            }

            if (isQtyAll) {
                holder.textViewDiscountPrice.paintFlags =
                    holder.textViewDiscountPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                holder.textViewActualPrice.paintFlags =
                    holder.textViewActualPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

            }
        }

        if (selectedProduct != null && data.item.isSelected == true) {
            holder.recyclerViewPromotionsOptions.isVisible =
                data.item.options.isNullOrEmpty() == false
        }

//        if (data.item.options.isNullOrEmpty()) {
            CoroutineScope(Dispatchers.Main).launch {
                val inStock = withContext(Dispatchers.IO) {
                    isInStock(productId)
                }
                if (!inStock) {
                    holder.textViewOutOfStock.isVisible = true
                    holder.linearLayoutRoot.alpha = 0.5f
                    holder.linearLayoutRoot.isEnabled = false
                    holder.checkBox.isEnabled = false
                    holder.checkBoxName.isEnabled = false
                    holder.linearAddItem.isVisible = false
                } else {
                    holder.textViewOutOfStock.isVisible = false
                }
//            }

        }
    }

    fun updatePromotionState(
        isRequirementMet: Boolean,
        hasRequiredItems: Boolean,
        selectedProduct: PromotionListingResponse?
    ) {
        itemList?.forEachIndexed { index, wrapper ->
            if ((wrapper.isApplied && hasRequiredItems) ||
                (wrapper.isApplied && discountTypeId == PromotionTypeRuleEvaluator.DiscountType.SPECIAL_PRICE.id &&
                        discountConditionAppliesId == PromotionTypeRuleEvaluator.DiscountConditionType.TOTAL_PURCHASE.id) ||
                (wrapper.isApplied && discountTypeId == PromotionTypeRuleEvaluator.DiscountType.PERCENT_OFF.id &&
                        discountConditionAppliesId == PromotionTypeRuleEvaluator.DiscountConditionType.TOTAL_PURCHASE.id)
            ) {
                val viewHolder =
                    recyclerView?.findViewHolderForAdapterPosition(index) as? ViewHolder
                viewHolder?.let { holder ->
                    holder.linearLayoutRoot.alpha = if (isRequirementMet) 1.0f else 0.4f
                    holder.linearLayoutRoot.isEnabled = isRequirementMet
                    holder.checkBox.isClickable = isRequirementMet
                    if (!isRequirementMet) {
                        holder.linearAddItem.isVisible = false
                        holder.checkBox.isChecked = false
                        holder.checkBoxName.isChecked = false
                        wrapper.item.quantity = null
                    }
                }
            }
        }
    }

    private fun setupCheckbox(
        holder: ViewHolder,
        data: ProductItemWrapper,
        isQtyEach: Boolean,
        conditionQty: Int,
        isQtyAll: Boolean
    ) {
        holder.checkBox.setOnCheckedChangeListener(null)
        holder.checkBox.isChecked = data.item.isSelected == true
        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {

                //call api

                CoroutineScope(Dispatchers.Main).launch {
                    val inStock = withContext(Dispatchers.IO) {
                        isInStock(
                            data.item.productId
                        )
                    }

                    if (!inStock) {
                        Toast.makeText(
                            holder.itemView.context,
                            "Oops! Looks like this combination is out of stock.",
                            Toast.LENGTH_SHORT
                        ).show()
                        holder.checkBox.isChecked = false
                        holder.checkBoxName.isChecked = false
                        holder.linearAddItem.isVisible = false
                        return@launch // 🚫 stop here, don’t run the below code
                    }

                    // if error check false again and returns from it

                    // ✅ NO FREE ITEMS check
                    if (discountTypeId == PromotionTypeRuleEvaluator.DiscountType.NO_FREE_ITEMS.id) {
                        val maxFreeItems = (discountValue ?: 0.0).toInt()
                        var totalAppliedQty = 0
                        itemList?.forEach {
                            if (it.isApplied) {
                                totalAppliedQty += (it.item.quantity ?: 0)
                            }
                        }
                        if (totalAppliedQty >= maxFreeItems && data.isApplied) {
                            Toast.makeText(
                                holder.itemView.context,
                                "You can only have $maxFreeItems free item(s) in this promotion.",
                                Toast.LENGTH_SHORT
                            ).show()
                            holder.checkBox.isChecked = false
                            holder.checkBoxName.isChecked = false
                            return@launch
                        }
                    }


                    var checkCondition = false
                    var totalQtyInTargetList = 0
                    itemList?.forEach {
                        if (hasRequired && !it.isApplied) {
                            checkCondition = true
                            totalQtyInTargetList = totalQtyInTargetList + (it.item.quantity ?: 0)
                        } else if (!hasRequired && it.isApplied) {
                            checkCondition = true
                            totalQtyInTargetList = totalQtyInTargetList + (it.item.quantity ?: 0)
                        } else if (hasRequired && data.isApplied) {
                            checkCondition = false
                        }
                    }

                    if (totalQtyInTargetList >= conditionQty && checkCondition && isQtyAll) {
                        Toast.makeText(
                            holder.itemView.context,
                            "You can only add $conditionQty item(s) in this promotion.",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@launch
                    }
                }

                data.item.isSelected = isChecked
//            val initialQty = if (isQtyEach && (data.item.quantity ?: 0) == 0) conditionQty else 1
                var initialQty = 1
                if (isQtyEach && !hasRequired) {
                    initialQty = conditionQty
                } else if (isQtyEach && data.item.isRequiredItem == true)
                    initialQty = conditionQty

                data.item.quantity = initialQty
                holder.textViewCount.text = initialQty.toString()
                holder.linearAddItem.isVisible = isChecked
                if (!isChecked) {
                    data.item.quantity = null
                    holder.recyclerViewPromotionsOptions.isVisible = false
                    optionsAdapterMap[data.item.productId]?.clearAllChoicesSelections()
                }
                onItemClick.invoke(data.item)
            }
        }
    }

    override fun getItemCount(): Int = itemList?.size ?: 0

    override fun getItemId(position: Int): Long {
        return itemList?.get(position)?.item?.productId?.toLong() ?: position.toLong()
    }

    override fun getItemViewType(position: Int): Int = super.getItemViewType(position)


    fun getStyledProductName(
        context: Context,
        productName: String?,
        isRequired: Boolean
    ): SpannableString {
        val baseName = productName ?: ""
        val requiredText = "  required"
        val requiredSizeScale = 0.85f // 45% of original size

        return if (isRequired) {
            SpannableString("$baseName$requiredText").apply {
                val start = baseName.length
                val end = start + requiredText.length

                setSpan(
                    ForegroundColorSpan(ContextCompat.getColor(context, R.color.colorOrange)),
                    start,
                    end,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                setSpan(
                    RelativeSizeSpan(requiredSizeScale),
                    start,
                    end,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
        } else {
            SpannableString(baseName)
        }
    }
}

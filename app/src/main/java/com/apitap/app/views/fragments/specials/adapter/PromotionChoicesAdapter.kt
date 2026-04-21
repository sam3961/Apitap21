package com.apitap.app.views.fragments.specials.adapter

import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatRadioButton
import androidx.recyclerview.widget.RecyclerView
import com.apitap.app.R
import com.apitap.app.views.fragments.specials.AddPromoToOrderDialog
import com.apitap.app.views.fragments.specials.data.PromoChoicesItem
import com.apitap.app.views.fragments.specials.data.PromotionListingResponse
import com.apitap.app.views.fragments.specials.utils.CommonFunctions
import com.apitap.app.views.fragments.specials.utils.Utility.isChoiceAvailable
import com.apitap.app.views.fragments.specials.utils.Utility.isChoiceGloballyInStock

class PromotionChoicesAdapter(
    private val promoItemsAdapter: PromotionsItemsAdapter,
    private val productId: Int?,
    private val optionId: Int?,
    private val choicesItems: MutableList<PromoChoicesItem>?,
    private val selectedProduct: PromotionListingResponse?,
    private var inventoryIndexMap: Map<Int, List<AddPromoToOrderDialog.InventoryIndex>>? = HashMap(),
    private val onItemClick: (PromoChoicesItem?) -> Unit
) : RecyclerView.Adapter<PromotionChoicesAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val radioButtonChoices: AppCompatRadioButton = itemView.findViewById(R.id.radioButtonOption)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_item_options, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = choicesItems?.get(position) ?: return
        val pid = productId ?: return

        val selectedChoices = getSelectedChoices(productId, optionId).toSet()

  /*          val isAvailable = isChoiceAvailable(
            pid,
            item.valueId ?: return,
            selectedChoices,
            inventoryIndexMap
        )
*/
        val isAvailableWithSelection = isChoiceAvailable(
            pid,
            item.valueId ?: return,
            selectedChoices,
            inventoryIndexMap
        )

        val isGloballyInStock = isChoiceGloballyInStock(
            pid,
            item.valueId ?: return,
            inventoryIndexMap
        )


        holder.radioButtonChoices.text = when {
            isAvailableWithSelection -> {
                "${item.valueName}: $${CommonFunctions.formatPrice(item.extraPrice ?: 0.0)}"
            }

            !isGloballyInStock -> {
                buildRedSuffixText(
                    holder.itemView.context,
                    "${item.valueName}: $${CommonFunctions.formatPrice(item.extraPrice ?: 0.0)}",
                    "(Out of stock)"
                )
            }

            else -> {
                buildYellowSuffixText(
                    holder.itemView.context,
                    "${item.valueName}: $${CommonFunctions.formatPrice(item.extraPrice ?: 0.0)}",
                    "(Not available with selection)"
                )
            }
        }


        holder.radioButtonChoices.isEnabled = isAvailableWithSelection
        holder.radioButtonChoices.alpha =
            if (isAvailableWithSelection) 1f else 0.4f

        holder.radioButtonChoices.isChecked = item.selectedItem == true

        holder.radioButtonChoices.setOnClickListener {
            if (!isAvailableWithSelection) {
                Toast.makeText(
                    holder.itemView.context,
                    if (!isGloballyInStock)
                        "This option is out of stock"
                    else
                        "Not available with current selection",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            selectItem(position)
            onItemClick(item)

            // 🔥 Force re-evaluation of all choices (web behavior)
            promoItemsAdapter.notifyOptionsChanged(productId)
        }

    }

    private fun buildRedSuffixText(
        context: Context,
        base: String,
        suffix: String
    ): SpannableString {
        val text = "$base $suffix"
        return SpannableString(text).apply {
            val start = text.indexOf(suffix)
            val end = start + suffix.length

            setSpan(
                ForegroundColorSpan(context.getColor(R.color.colorOrangeRed)), // better yellow
                start,
                end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            setSpan(
                RelativeSizeSpan(0.85f), // 👈 smaller text
                start,
                end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
    }

    private fun buildYellowSuffixText(
        context: Context,
        base: String,
        suffix: String
    ): SpannableString {
        val text = "$base $suffix"
        return SpannableString(text).apply {
            val start = text.indexOf(suffix)
            val end = start + suffix.length

            setSpan(
                ForegroundColorSpan(context.getColor(R.color.colorOrangeNew)), // better yellow
                start,
                end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            setSpan(
                RelativeSizeSpan(0.85f), // 👈 smaller text
                start,
                end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
    }


    private fun selectItem(position: Int) {
        val list = choicesItems ?: return

        val prevSelectedIndex = list.indexOfFirst { it.selectedItem == true }

        if (prevSelectedIndex != -1 && prevSelectedIndex != position) {
            list[prevSelectedIndex].selectedItem = false
            notifyItemChanged(prevSelectedIndex)
        }

        list[position].selectedItem = true
        notifyItemChanged(position)

        // 🔥 IMPORTANT: CLEAR DOWNSTREAM OPTIONS (WEB BEHAVIOR)
        promoItemsAdapter.clearSelectionsAfterOption(
            productId = productId,
            optionId = optionId
        )
    }

    fun clearSelection() {
        choicesItems?.forEachIndexed { index, item ->
            if (item.selectedItem == true) {
                item.selectedItem = false
                notifyItemChanged(index)
            }
        }
    }

    fun getSelectedChoices(productId: Int?, optionId: Int?): ArrayList<Int> {
        val listOfSelectedChoices = ArrayList<Int>()

        promoItemsAdapter.itemList
            ?.firstOrNull { it.item.productId == productId }
            ?.item?.options
            ?.forEach { option ->
                option.choices?.forEach { choice ->
                    // ✅ Keep selections from other options
                    if (choice.selectedItem == true) {
                        if (option.id == optionId) {
                            // skip old selections of THIS option
                            return@forEach
                        }
                        listOfSelectedChoices.add(choice.valueId ?: 0)
                    }
                }
            }

        return listOfSelectedChoices
    }

    fun getSelectedChoicesForStock(): List<Int> {
        return choicesItems
            ?.filter { it.selectedItem == true }
            ?.mapNotNull { it.valueId }
            ?: emptyList()
    }

    fun updateChoices(newChoices: List<PromoChoicesItem>?) {
        choicesItems?.clear()
        choicesItems?.addAll(newChoices ?: emptyList())
        notifyDataSetChanged()
    }


    override fun getItemCount(): Int = choicesItems?.size ?: 0
}

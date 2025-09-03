package com.apitap.views.fragments.specials.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatRadioButton
import androidx.recyclerview.widget.RecyclerView
import com.apitap.R
import com.apitap.views.fragments.specials.data.PromoChoicesItem
import com.apitap.views.fragments.specials.data.PromotionListingResponse
import com.apitap.views.fragments.specials.utils.CommonFunctions
import com.apitap.views.fragments.specials.utils.Utility.isCombinationInStock
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PromotionChoicesAdapter(
    private val promoItemsAdapter: PromotionsItemsAdapter,
    private val productId: Int?,
    private val optionId: Int?,
    private val choicesItems: MutableList<PromoChoicesItem>?,
    private val selectedProduct: PromotionListingResponse?,
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
        choicesItems?.get(position)?.let { item ->
            holder.radioButtonChoices.text =
                "${item.valueName}: $${CommonFunctions.formatPrice(item.extraPrice ?: 0.0)}"

            holder.radioButtonChoices.isChecked = item.selectedItem == true

            holder.radioButtonChoices.setOnClickListener {

                CoroutineScope(Dispatchers.Main).launch {
                    val inStock = withContext(Dispatchers.IO) {
                        val listOfOldChoice = getSelectedChoices(productId, optionId)
                        if (!listOfOldChoice.contains(item.valueId)) {
                            listOfOldChoice.add(item.valueId ?: 0)
                        }
                        isCombinationInStock(
                            productId,
                            listOfOldChoice
                        )
                    }

                    if (!inStock) {
                        Toast.makeText(
                            holder.itemView.context,
                            "Oops! Looks like this combination is out of stock.",
                            Toast.LENGTH_SHORT
                        ).show()
                        holder.radioButtonChoices.isChecked = false
                        return@launch // 🚫 stop here, don’t run the below code
                    }

                    selectItem(position)
                    onItemClick(item)
                }
            }

            holder.itemView.setOnClickListener {
                CoroutineScope(Dispatchers.Main).launch {
                    val inStock = withContext(Dispatchers.IO) {
                        val listOfOldChoice = getSelectedChoices(productId, optionId)
                        if (!listOfOldChoice.contains(item.valueId)) {
                            listOfOldChoice.add(item.valueId ?: 0)
                        }

                        isCombinationInStock(
                            productId,
                            listOfOldChoice
                        )
                    }

                    if (!inStock) {
                        Toast.makeText(
                            holder.itemView.context,
                            "Oops! Looks like this combination is out of stock.",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@launch // 🚫 stop here, don’t run the below code
                    }

                    selectItem(position)
                    onItemClick(item)
                }
            }
        }
    }

    private fun selectItem(position: Int) {
        choicesItems?.let { list ->
            val prevSelectedIndex = list.indexOfFirst { it.selectedItem == true }

            if (prevSelectedIndex != -1 && prevSelectedIndex != position) {
                list[prevSelectedIndex].selectedItem = false
                notifyItemChanged(prevSelectedIndex)
            }

            list[position].selectedItem = true
            notifyItemChanged(position)
        }
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


    override fun getItemCount(): Int = choicesItems?.size ?: 0
}

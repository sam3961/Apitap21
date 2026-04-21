package com.apitap.app.views.fragments.specials.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.apitap.app.R
import com.apitap.app.views.fragments.specials.AddPromoToOrderDialog
import com.apitap.app.views.fragments.specials.data.OptionsProductPromoItem
import com.apitap.app.views.fragments.specials.data.PromoChoicesItem
import com.apitap.app.views.fragments.specials.data.PromotionListingResponse
import com.apitap.app.views.fragments.specials.utils.Utility.isChoiceAvailable

class PromotionOptionsAdapter(
    private val promoItemsAdapter: PromotionsItemsAdapter,
    private val productId: Int?,
    private val choicesItems: List<OptionsProductPromoItem>?,
    private val selectedProduct: PromotionListingResponse?,
    private var inventoryIndexMap: Map<Int, List<AddPromoToOrderDialog.InventoryIndex>>? = HashMap(),
    private val onItemClick: (PromoChoicesItem?) -> Unit
) :

    RecyclerView.Adapter<PromotionOptionsAdapter.ViewHolder>() {

    private val choicesAdapters = mutableListOf<PromotionChoicesAdapter>()

    // ViewHolder class to hold the reference to the UI components
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewOptions: AppCompatTextView = itemView.findViewById(R.id.textViewOptions)
        val recyclerViewOptions: RecyclerView = itemView.findViewById(R.id.recyclerViewOptions)
        var choicesAdapter: PromotionChoicesAdapter? = null
    }

    // Inflate the item layout and create the ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_item_options_title, parent, false)
        return ViewHolder(view)
    }

    // Bind data to the UI components in the ViewHolder

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = choicesItems?.get(position) ?: return

        val hasAnyStock = item.choices?.any { choice ->
            isChoiceAvailable(
                productId ?: return@any false,
                choice.valueId ?: return@any false,
                emptySet(),
                inventoryIndexMap
            )
        } == true

        holder.recyclerViewOptions.isEnabled = hasAnyStock
        holder.textViewOptions.alpha = if (hasAnyStock) 1f else 0.4f
        holder.textViewOptions.text = item.name

        if (holder.choicesAdapter == null) {
            val adapter = PromotionChoicesAdapter(
                promoItemsAdapter,
                productId,
                item.id,
                item.choices?.toMutableList(),
                selectedProduct,
                inventoryIndexMap
            ) {
                item.selectedItem = true
                onItemClick.invoke(it)
            }

            holder.choicesAdapter = adapter
            holder.recyclerViewOptions.adapter = adapter

            // ✅ REGISTER ONCE (NO DUPLICATES)
            choicesAdapters.add(adapter)

        } else {
            holder.choicesAdapter?.updateChoices(item.choices)
        }
    }

    override fun getItemCount(): Int = choicesItems?.size ?: 0

    fun clearAllChoicesSelections() {
        choicesAdapters.forEach { it.clearSelection() }
    }

    fun notifyAllChoicesChanged() {
        choicesAdapters.forEach { it.notifyDataSetChanged() }
    }

    fun getAllSelectedChoices(): Set<Int> {
        val result = mutableSetOf<Int>()
        choicesAdapters.forEach { adapter ->
            adapter.getSelectedChoicesForStock().let { result.addAll(it) }
        }
        return result
    }



}
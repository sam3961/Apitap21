package com.apitap.views.fragments.specials.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.apitap.R
import com.apitap.views.fragments.specials.data.OptionsProductPromoItem
import com.apitap.views.fragments.specials.data.PromoChoicesItem
import com.apitap.views.fragments.specials.data.PromotionListingResponse

class PromotionOptionsAdapter(
    private val promoItemsAdapter: PromotionsItemsAdapter,
    private val productId: Int?,
    private val choicesItems: List<OptionsProductPromoItem>?,
    private val selectedProduct: PromotionListingResponse?,
    private val onItemClick: (PromoChoicesItem?) -> Unit
) :

    RecyclerView.Adapter<PromotionOptionsAdapter.ViewHolder>() {

    private val choicesAdapters = mutableListOf<PromotionChoicesAdapter>()

    // ViewHolder class to hold the reference to the UI components
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewOptions: AppCompatTextView = itemView.findViewById(R.id.textViewOptions)
        val recyclerViewOptions: RecyclerView = itemView.findViewById(R.id.recyclerViewOptions)
    }

    // Inflate the item layout and create the ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_item_options_title, parent, false)
        return ViewHolder(view)
    }

    // Bind data to the UI components in the ViewHolder
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        choicesItems?.get(position)?.let { item ->
            holder.textViewOptions.text = item.name

            val promotionChoicesAdapter = PromotionChoicesAdapter(
                promoItemsAdapter,
                productId,
                item.id,
                item.choices?.toMutableList(), selectedProduct
            ) {
                item.selectedItem = true
                onItemClick.invoke(it)
            }
            choicesAdapters.add(promotionChoicesAdapter) // Store reference

            holder.recyclerViewOptions.adapter = promotionChoicesAdapter

        }
    }

    override fun getItemCount(): Int = choicesItems?.size ?: 0

    fun clearAllChoicesSelections() {
        choicesAdapters.forEach { it.clearSelection() }
    }

    fun customNotify(options: List<OptionsProductPromoItem>?) {

    }

}
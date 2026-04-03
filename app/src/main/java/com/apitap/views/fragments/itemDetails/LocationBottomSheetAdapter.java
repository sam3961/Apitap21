package com.apitap.views.fragments.itemDetails;

import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apitap.R;
import com.apitap.model.bean.items.LocationBean;

import java.util.ArrayList;

public class LocationBottomSheetAdapter
        extends RecyclerView.Adapter<LocationBottomSheetAdapter.ViewHolder> {

    public interface OnLocationClick {
        void onClick(LocationBean location);
    }

    private String selectedLocationId;
    private ArrayList<LocationBean> list;
    private String choiceKey;
    private OnLocationClick listener;

    public LocationBottomSheetAdapter(ArrayList<LocationBean> list,
                                      String choiceKey,
                                      String selectedLocationId,
                                      OnLocationClick listener) {
        this.list = list;
        this.choiceKey = choiceKey;
        this.selectedLocationId = selectedLocationId;
        this.listener = listener;
    }

    public void updateChoiceKey(String newKey) {
        this.choiceKey = newKey;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_bottomsheet_locations, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        LocationBean loc = list.get(position);
        String locationId = loc.getLocationId();

        if (locationId != null && locationId.equals(selectedLocationId)) {
            holder.imgCheck.setImageDrawable(holder.itemView.getContext().getDrawable(R.drawable.ic_icon_radio_button));
        } else {
            holder.imgCheck.setImageDrawable(holder.itemView.getContext().getDrawable(R.drawable.ic_icon_blank_radio_button));
        }

        Log.d("INV_DEBUG", "Adapter Key = " + choiceKey);
        int qty = loc.getQuantityForChoices(choiceKey);
        Log.d("INV_DEBUG", "Returned Qty = " + qty);
        Log.d("INV_DEBUG", "Location Map = " + loc.getChoiceQuantityMap());

        String baseText = loc.getLocationName()
                + " " + loc.getAddress();
//                + "\nQty: " + qty;

        if (qty <= 0) {
            baseText += " (Out of stock)";
        }

      /*  SpannableString spannable = new SpannableString(baseText);

// Find start index of Qty number
        String qtyText = "Qty: " + qty;
        int start = baseText.indexOf(qtyText);
        int end = start + qtyText.length();

// Make Qty bold
        spannable.setSpan(
                new StyleSpan(Typeface.BOLD),
                start,
                end,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );*/

        holder.tv.setText(baseText);

        holder.tv.setTextColor(qty > 0 ? Color.BLACK : Color.GRAY);
        holder.itemView.setEnabled(qty > 0);
        holder.itemView.setAlpha(qty > 0 ? 1f : 0.5f);

        holder.itemView.setOnClickListener(v -> {
            if (qty > 0) {
                listener.onClick(loc);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv, tvPrice;
        ImageView imgCheck;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgCheck = itemView.findViewById(R.id.imgCheck);
            tv = itemView.findViewById(R.id.txt_type);
            tvPrice = itemView.findViewById(R.id.txt_price);
            tvPrice.setVisibility(View.GONE
            );
        }
    }
}
package com.apitap.views.fragments.home.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.apitap.R;
import com.apitap.model.storeCategories.RESULTItem;

import java.util.List;

/**
 * Created by Shami on 1/9/16.
 */

public class AdapterStoreCategories extends RecyclerView.Adapter<AdapterStoreCategories.ViewHolder> {
    private Context context;
    private List<RESULTItem> arrayListStoresCategory;
    private boolean firstTimeAdapterSet = false;
    private int selectedPosition = -1;
    private StoreItemClick storeItemClick;

    public AdapterStoreCategories(Context context, List<RESULTItem> arrayListStores,StoreItemClick storeItemClick ) {
        this.arrayListStoresCategory = arrayListStores;
        this.context = context;
        this.storeItemClick = storeItemClick;
        firstTimeAdapterSet = true;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_home_store_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        if (firstTimeAdapterSet) {
            if (holder.getAdapterPosition() == 0 || holder.getAdapterPosition() == 1) {
                holder.imageViewArrow.setImageResource(R.drawable.ic_icon_uparrow);
                holder.recyclerViewStores.setVisibility(View.VISIBLE);
            } else {
                holder.imageViewArrow.setImageResource(R.drawable.ic_icon_downarrow);
                holder.recyclerViewStores.setVisibility(View.GONE);
            }
        }

        if (selectedPosition == holder.getAdapterPosition()) {
            if (holder.recyclerViewStores.getVisibility() == View.VISIBLE) {
                holder.recyclerViewStores.setVisibility(View.GONE);
                holder.imageViewArrow.setImageResource(R.drawable.ic_icon_downarrow);
            } else {
                holder.imageViewArrow.setImageResource(R.drawable.ic_icon_uparrow);
                holder.recyclerViewStores.setVisibility(View.VISIBLE);
            }

        }

        holder.textViewBusiness.setText(arrayListStoresCategory.get(holder.getAdapterPosition()).getJsonMember12045());


        holder.llHeader.setOnClickListener(view -> {
            selectedPosition = holder.getAdapterPosition();
            firstTimeAdapterSet = false;
            notifyDataSetChanged();
        });

        setStoresAdapter(holder.recyclerViewStores, holder.getAdapterPosition());
    }

    private void setStoresAdapter(RecyclerView recyclerViewStores, int position) {
        AdapterStores adapterStores = new AdapterStores(context, arrayListStoresCategory.get(position).getME(),storeItemClick,
                arrayListStoresCategory.get(position).getJsonMember11493());
        recyclerViewStores.setAdapter(adapterStores);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return arrayListStoresCategory.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewBusiness;
        private ImageView imageViewArrow;
        private RecyclerView recyclerViewStores;
        private RelativeLayout llHeader;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewBusiness = itemView.findViewById(R.id.textViewBusiness);
            imageViewArrow = itemView.findViewById(R.id.imageViewArrow);
            recyclerViewStores = itemView.findViewById(R.id.recyclerViewStores);
            llHeader = itemView.findViewById(R.id.lin_Ads);
        }
    }

    public interface StoreItemClick {
        void onCategoryStoreClick(Bundle bundle);
        void onViewMoreStoreClick(Bundle bundle);
    }


}

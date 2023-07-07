package com.apitap.views.fragments.home.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.apitap.R;
import com.apitap.model.Constants;
import com.apitap.model.Utils;
import com.apitap.model.home.stores.RESULTItem;
import com.apitap.model.preferences.ATPreferences;
import com.apitap.model.storeCategories.MEItem;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

/**
 * Created by Shami on 1/9/16.
 */

public class AdapterStores extends RecyclerView.Adapter<AdapterStores.ViewHolder> {
    private Context context;
    private String  categoryId;
    private List<MEItem> arrayListStores;
    private AdapterStoreCategories.StoreItemClick storeItemClick;

    public AdapterStores(Context context, List<MEItem> arrayListStores, AdapterStoreCategories.StoreItemClick storeItemClick,
                         String categoryId) {
        this.arrayListStores = arrayListStores;
        this.context = context;
        this.storeItemClick = storeItemClick;
        this.categoryId = categoryId;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_stores, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.imageViewSeen.setVisibility(View.GONE);

        if (arrayListStores.size() > 9 && holder.getAdapterPosition() == 9) {
            holder.linearLayoutViewAll.setVisibility(View.VISIBLE);
        } else {
            holder.linearLayoutViewAll.setVisibility(View.GONE);
        }


        holder.textViewStoreName.setText(Utils.hexToASCII(arrayListStores.get(holder.getAdapterPosition()).getJsonMember11470()));

        Glide.with(context).load(ATPreferences.readString(context, Constants.KEY_IMAGE_URL)
                + "_t_" + arrayListStores.get(holder.getAdapterPosition()).getJsonMember121170())
                .placeholder(R.drawable.ic_gallery_placeholder)
                .error(R.drawable.no_photo_placeholder)
                .fitCenter().centerInside()
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).into(holder.imageViewStore);

        holder.imageViewStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String merchantID = arrayListStores.get(holder.getAdapterPosition()).getJsonMember53();
                String merchantName = arrayListStores.get(holder.getAdapterPosition()).getJsonMember11470();
                Bundle b = new Bundle();
                b.putBoolean(Constants.HEADER_STORE, true);
                b.putString(Constants.MERCHANT_ID, merchantID);

                ATPreferences.putString(context, Constants.HEADER_IMG,
                        arrayListStores.get(holder.getAdapterPosition()).getJsonMember121170());
                ATPreferences.putBoolean(context, Constants.HEADER_STORE, true);
                ATPreferences.putString(context, Constants.MERCHANT_ID, merchantID);
                ATPreferences.putString(context, Constants.MERCHANT_CATEGORY, "");

                storeItemClick.onCategoryStoreClick(b);
            }
        });

        holder.linearLayoutViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("categoryId",categoryId);
                storeItemClick.onViewMoreStoreClick(bundle);
            }
        });
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        if (arrayListStores.size() > 9)
            return 10;
        else
            return arrayListStores.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewStoreName;
        private ImageView imageViewStore;
        private ImageView imageViewSeen;
        private LinearLayout linearLayoutViewAll;


        public ViewHolder(View itemView) {
            super(itemView);
            textViewStoreName = itemView.findViewById(R.id.textViewStoreName);
            imageViewStore = itemView.findViewById(R.id.imageViewStore);
            imageViewSeen = itemView.findViewById(R.id.eye);
            linearLayoutViewAll = itemView.findViewById(R.id.view_all);

        }
    }

}

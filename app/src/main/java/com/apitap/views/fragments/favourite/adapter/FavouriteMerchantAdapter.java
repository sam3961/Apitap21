package com.apitap.views.fragments.favourite.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.apitap.R;
import com.apitap.model.Constants;
import com.apitap.model.Utils;
import com.apitap.model.bean.FavMerchantBean;
import com.apitap.model.preferences.ATPreferences;
import com.apitap.views.HomeActivity;
import com.apitap.views.fragments.storefront.FragmentStoreFront;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sahil on 1/9/16.
 */

public class FavouriteMerchantAdapter extends RecyclerView.Adapter<FavouriteMerchantAdapter.ViewHolder> {

    private final Activity activity;

    LayoutInflater inflater;
    List<FavMerchantBean.RESULT> list = new ArrayList<>();
    public List<FavMerchantBean.CU> allImages;
    String selected_Sort;

    public FavouriteMerchantAdapter(Activity activity, List<FavMerchantBean.RESULT> list, String selected_Sort) {
        this.list = list;
        this.activity = activity;
        this.selected_Sort = selected_Sort;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView mLogo;
        TextView storeName, stores;
        RelativeLayout relativeLayout_main;

        public ViewHolder(View itemView) {
            super(itemView);
            mLogo = (ImageView) itemView.findViewById(R.id.logo);
            storeName = (TextView) itemView.findViewById(R.id.storeName);
            stores = (TextView) itemView.findViewById(R.id.storename);
            relativeLayout_main = (RelativeLayout) itemView.findViewById(R.id.mainlayout_rl);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, final int position) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_fav_store_merchant, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // there is second list which contains list of songs array
        allImages = list.get(position).getCU();


        for (int i = 0; i < list.get(position).getCU().size(); i++) {
            if (selected_Sort.equals("All")){
                holder.relativeLayout_main.setVisibility(View.VISIBLE);
                holder.stores.setText(allImages.get(i).get_12083());
            }else {
            if (allImages.get(i).get_12083().equals(selected_Sort)) {
                holder.relativeLayout_main.setVisibility(View.VISIBLE);
                holder.stores.setText(allImages.get(i).get_12083());
            }}
        }

        holder.storeName.setText(Utils.hexToASCII(list.get(position).get11470()));
        Picasso.get()
                .load(ATPreferences.readString(activity, Constants.KEY_IMAGE_URL) + list.get(position).get121170())
                .placeholder(R.drawable.ic_gallery_placeholder)
                .error(R.drawable.no_photo_placeholder)
                .into(holder.mLogo);

        holder.mLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String merchantID = list.get(position).get53();
                Bundle b = new Bundle();
                b.putBoolean(Constants.HEADER_STORE, true);
                b.putString(Constants.MERCHANT_ID, merchantID);
                ATPreferences.putBoolean(activity, Constants.HEADER_STORE, true);
                ATPreferences.putString(activity, Constants.MERCHANT_ID, merchantID);
                ATPreferences.putString(activity, Constants.MERCHANT_CATEGORY, "");

                ((HomeActivity) activity).displayView(new FragmentStoreFront(), Constants.TAG_STORESFRONTPAGE, b);
            }
        });
    }

    @Override
    public int getItemCount() {

        return list.size();
    }

    public void tabContainer2Visible() {
        //homeTab2.setBackgroundColor(activity.getResources().getColor(R.color.colorWhite));
        //imageViewHomeTab2.setImageResource(R.drawable.ic_icon_home);

        HomeActivity.tabContainer2.setVisibility(View.GONE);
        HomeActivity.tabContainer2.setVisibility(View.GONE);
    }
}

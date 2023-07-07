package com.apitap.views.adapters;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.apitap.R;
import com.apitap.model.Constants;
import com.apitap.model.Utils;
import com.apitap.model.bean.FavMerchantBean;
import com.apitap.model.preferences.ATPreferences;
import com.apitap.model.storeAbout.RESULTItem;
import com.apitap.views.HomeActivity;
import com.apitap.views.fragments.storefront.FragmentStoreFront;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sahil on 1/9/16.
 */

public class StoreAboutImagesAdapter extends RecyclerView.Adapter<StoreAboutImagesAdapter.ViewHolder> {

    private final Activity activity;

    LayoutInflater inflater;
    List<RESULTItem> result = new ArrayList<>();
    public List<FavMerchantBean.CU> allImages;
    String selected_Sort;
    onPhotoClick onPhotoClick;


    public StoreAboutImagesAdapter(Activity activity, List<RESULTItem> result,  onPhotoClick onPhotoClick) {
        this.result = result;
        this.onPhotoClick = onPhotoClick;
        this.activity = activity;
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
            relativeLayout_main = (RelativeLayout) itemView.findViewById(R.id.mainlayout_rl);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, final int position) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_store_about, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // there is second list which contains list of songs array

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPhotoClick.onPhotoClick(position);
            }
        });

        Picasso.get()
                .load(ATPreferences.readString(activity, Constants.KEY_IMAGE_URL) + result.get(position).getJsonMember121170())
                .placeholder(R.drawable.ic_gallery_placeholder)
                .error(R.drawable.no_photo_placeholder)
                .into(holder.mLogo);

    }

    @Override
    public int getItemCount() {

        return result.size();
    }

    public void tabContainer2Visible() {
        //homeTab2.setBackgroundColor(activity.getResources().getColor(R.color.colorWhite));
        //imageViewHomeTab2.setImageResource(R.drawable.ic_icon_home);

        HomeActivity.tabContainer2.setVisibility(View.GONE);
        HomeActivity.tabContainer2.setVisibility(View.GONE);
    }

    public interface onPhotoClick{
        void onPhotoClick(int positon);
    }
}

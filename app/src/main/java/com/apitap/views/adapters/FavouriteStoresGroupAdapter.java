package com.apitap.views.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apitap.R;
import com.apitap.model.Constants;
import com.apitap.model.Utils;
import com.apitap.model.bean.FavBeanOld;
import com.apitap.model.bean.FavMerchantBean;
import com.apitap.model.bean.Favdetailsbean;
import com.apitap.model.preferences.ATPreferences;
import com.apitap.views.HomeActivity;
import com.apitap.views.fragments.itemDetails.FragmentItemDetails;
import com.apitap.views.fragments.storefront.FragmentStoreFront;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Sahil on 1/9/16.
 */

public class FavouriteStoresGroupAdapter extends RecyclerView.Adapter<FavouriteStoresGroupAdapter.ViewHolder> {

    private final Activity activity;

    LayoutInflater inflater;
    List<FavMerchantBean.RESULT>  list;

    public FavouriteStoresGroupAdapter(Activity activity,  List<FavMerchantBean.RESULT> result) {
        this.list = result;
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

        private final ImageView imageView;
        private final ImageView eye;

        private final TextView description;
        LinearLayout llPrice;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView =  itemView.findViewById(R.id.image);
            eye =  itemView.findViewById(R.id.eye);
            llPrice =  itemView.findViewById(R.id.llPrice);
            description =  itemView.findViewById(R.id.description);
        }
    }

    int currentIndex = 0;


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, final int position) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_horizontal_test, null);
        //view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
            holder.llPrice.setVisibility(View.GONE);
            holder.eye.setVisibility(View.GONE);

        holder.description.setText(Utils.hexToASCII(list.get(position).get11470()));
        Picasso.get()
                .load(ATPreferences.readString(activity, Constants.KEY_IMAGE_URL) + list.get(position).get121170())
                .placeholder(R.drawable.ic_gallery_placeholder)
                .error(R.drawable.no_photo_placeholder)
                .into(holder.imageView);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
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



    }

package com.apitap.views.fragments.favourite.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apitap.R;
import com.apitap.model.Constants;
import com.apitap.model.Utils;
import com.apitap.model.bean.FavBeanOld;
import com.apitap.model.bean.Favdetailsbean;
import com.apitap.model.bean.FavouriteItemBean;
import com.apitap.model.preferences.ATPreferences;
import com.apitap.views.HomeActivity;
import com.apitap.views.fragments.itemDetails.FragmentItemDetails;
import com.apitap.views.fragments.FragmentFavoriteItems;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Sahil on 1/9/16.
 */

public class FavouriteItemAdapter extends RecyclerView.Adapter<FavouriteItemAdapter.ViewHolder> {

    private final Activity activity;
    int count;
    String selectedFilter = "";

    public List<FavBeanOld.PC> allImages;
    LayoutInflater inflater;
    List<FavouriteItemBean> list;
    HashMap<Integer, ArrayList<Favdetailsbean>> map;
    String selected_Sort;

    public FavouriteItemAdapter(Activity activity, List<FavouriteItemBean> favouriteItemBeans, String selected_sort) {
        this.selected_Sort = selected_sort;
        this.activity = activity;
        this.list = favouriteItemBeans;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (list.size() > 9)
            count = 10;
        else
            count = list.size();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_horizontal_test, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        setData(holder, position);
    }

    public void setData(ViewHolder holder, final int position) {
        if (list.size() > 9 && count - 1 == position)
            holder.view_all.setVisibility(View.VISIBLE);
        else
            holder.view_all.setVisibility(View.GONE);

        String isSeen = list.get(position).getSeen();
        if (isSeen.equalsIgnoreCase("false")) {
            holder.eye.setBackgroundResource(R.drawable.green_seen);
        } else {
            holder.eye.setVisibility(View.GONE);
        }
        // if (allImagesItems.get(position).getSellerName() != null)
        holder.description.setText(list.get(position).getDescription());

        String ActualPrice = String.format("%.2f", Double.parseDouble(list.get(position).getActualPrice()));
        String DiscountPrice = String.format("%.2f", Double.parseDouble(list.get(position).getDiscountedPrice()));

        if ((Double.parseDouble(ActualPrice) == Double.parseDouble(DiscountPrice)) || DiscountPrice.equals("0") || DiscountPrice.equals("0.00")
                || Double.parseDouble(DiscountPrice) > Double.parseDouble(ActualPrice) || ActualPrice.equals(DiscountPrice)) {
            holder.rlSinglePrice.setVisibility(View.VISIBLE);
            holder.rlTwoPrice.setVisibility(View.GONE);
            holder.actualPrice.setText("$" + Utils.getFormatAmount(ActualPrice));
        } else if (ActualPrice.equals("0") || ActualPrice.equals("0.00")) {
            holder.rlTwoPrice.setVisibility(View.VISIBLE);
            holder.rlSinglePrice.setVisibility(View.GONE);
            holder.priceAfterDiscount.setText("$" + Utils.getFormatAmount(DiscountPrice));
            holder.actualPrice.setVisibility(View.GONE);
        } else if (Double.parseDouble(ActualPrice) > Double.parseDouble(DiscountPrice)) {
            holder.rlTwoPrice.setVisibility(View.VISIBLE);
            holder.rlSinglePrice.setVisibility(View.VISIBLE);
            holder.priceAfterDiscount.setText("$" + Utils.getFormatAmount(DiscountPrice));
            holder.priceAfterDiscount.setGravity(Gravity.END);
            holder.actualPrice.setGravity(Gravity.START);
            holder.actualPrice.setText("$" + Utils.getFormatAmount(ActualPrice) + "   ");
            holder.actualPrice.setTextColor(activity.getResources().getColor(R.color.colorRed));
            holder.actualPrice.setPaintFlags(holder.actualPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String productId = list.get(position).getId();
                String productType = list.get(position).getProduct_type();
                Bundle bundle = new Bundle();
                bundle.putString("productId", productId);
                bundle.putString("productType", productType);
                bundle.putString("flag", "home");
                FragmentItemDetails fragment = new FragmentItemDetails();
                fragment.setArguments(bundle);
                ((HomeActivity) activity).displayView(fragment, Constants.TAG_DETAILSPAGE, bundle);
            }
        });

        holder.view_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((HomeActivity) activity).displayView(new FragmentFavoriteItems(), "FAV_ITEMS", new Bundle());
            }
        });

        Picasso.get()
                .load(ATPreferences.readString(activity, Constants.KEY_IMAGE_URL) + list.get(position).getImageUrl())
                .placeholder(R.drawable.ic_gallery_placeholder)
                .error(R.drawable.no_photo_placeholder)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        if (list.size() > 9)
            return 10;
        else
            return list.size();
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView imageView, eye;
        private final TextView description, actualPrice, priceAfterDiscount;
        private final LinearLayout rlSinglePrice, rlTwoPrice, view_all;
        private final CardView parentCard;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.image);
            description = (TextView) itemView.findViewById(R.id.description);
            eye = (ImageView) itemView.findViewById(R.id.eye);
            rlSinglePrice = (LinearLayout) itemView.findViewById(R.id.rel_single_price);
            rlTwoPrice = (LinearLayout) itemView.findViewById(R.id.rl_two_price);
            actualPrice = (TextView) itemView.findViewById(R.id.actual_price);
            priceAfterDiscount = (TextView) itemView.findViewById(R.id.price_after_discount);
            view_all = (LinearLayout) itemView.findViewById(R.id.view_all);
            parentCard = itemView.findViewById(R.id.parentCard);
        }
    }
}
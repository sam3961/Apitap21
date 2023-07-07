
package com.apitap.views.fragments.favourite.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apitap.R;
import com.apitap.model.Constants;
import com.apitap.model.bean.FavouriteSpecialBean;
import com.apitap.model.bean.Favspecialbean;
import com.apitap.model.preferences.ATPreferences;
import com.apitap.views.HomeActivity;
import com.apitap.views.fragments.itemDetails.FragmentItemDetails;
import com.apitap.views.fragments.FragmentFavoriteSpecials;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Sahil on 1/9/16.
 */

public class FavouriteSpecialAdapter extends RecyclerView.Adapter<FavouriteSpecialAdapter.ViewHolder> implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {

    private final Activity activity;
    private final int count;

    LayoutInflater inflater;
    ArrayList<FavouriteSpecialBean> list;
    HashMap<Integer, ArrayList<Favspecialbean>> map;
    String selected_Sort;

    public FavouriteSpecialAdapter(Activity activity, ArrayList<FavouriteSpecialBean> favouriteSpecialBeans, String selected_sort) {

        this.list = favouriteSpecialBeans;
        this.selected_Sort = selected_sort;
        this.activity = activity;
        if (list.size() > 9)
            count = 10;
        else
            count = list.size();
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

        private final TextView description, actualPrice, priceAfterDiscount;
        LinearLayout rlSinglePrice, rlTwoPrice,view_all;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.image);
            description = (TextView) itemView.findViewById(R.id.description);
            view_all =  itemView.findViewById(R.id.view_all);
            eye = (ImageView) itemView.findViewById(R.id.eye);

            rlSinglePrice = (LinearLayout) itemView.findViewById(R.id.rel_single_price);
            rlTwoPrice = (LinearLayout) itemView.findViewById(R.id.rl_two_price);
            actualPrice = (TextView) itemView.findViewById(R.id.actual_price);
            priceAfterDiscount = (TextView) itemView.findViewById(R.id.price_after_discount);
        }
    }




    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, final int position) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_horizontal_test, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        if (list.size() > 9 && count - 1 == position)
            holder.view_all.setVisibility(View.VISIBLE);
        else
            holder.view_all.setVisibility(View.GONE);

        String ActualPrice = list.get(position).getActualPrice();
        String DiscountPrice = list.get(position).getDiscountedPrice();

        if (ActualPrice.equals(DiscountPrice) || DiscountPrice.equals("") || DiscountPrice.equals("0") ||
                DiscountPrice.equals("0.00")) {
            holder.rlSinglePrice.setVisibility(View.VISIBLE);
            holder.rlTwoPrice.setVisibility(View.GONE);
            holder.actualPrice.setText(/*"$"+*/ActualPrice);
        } else {
            holder.rlSinglePrice.setVisibility(View.GONE);
            holder.rlTwoPrice.setVisibility(View.VISIBLE);
            holder.actualPrice.setTextColor(Color.GRAY);
            holder.actualPrice.setText(/*"$" + */ActualPrice);
            holder.priceAfterDiscount.setText(/*"$" +*/ DiscountPrice);
            holder.actualPrice.setPaintFlags(holder.actualPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        }

        holder.eye.setVisibility(View.GONE);
        holder.description.setText(list.get(position).getDescription());
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String productId =  list.get(position).getId();
                ;
                Log.d("ProductsID", productId);
                String productType = list.get(position).getProduct_type();
                Log.d("productTypes", productType);
                String ActualPrice = list.get(position).getActualPrice();
                String PriceAfterDiscount = list.get(position).getDiscountedPrice();
                Bundle bundle = new Bundle();

                bundle.putString("productId", productId);
                bundle.putString("productType", productType);
                bundle.putString("actualprice", ActualPrice);
                bundle.putString("discountprice", PriceAfterDiscount);
                bundle.putString("isFavorite", "1");
                FragmentItemDetails fragment = new FragmentItemDetails();
                fragment.setArguments(bundle);
                ((HomeActivity) activity).displayView(fragment, Constants.TAG_DETAILSPAGE, bundle);
                tabContainer2Visible();

            }
        });
        Picasso.get().load(ATPreferences.readString(activity, Constants.KEY_IMAGE_URL) +
                list.get(position).getImageUrl())
                .placeholder(R.drawable.ic_gallery_placeholder)
                .error(R.drawable.no_photo_placeholder)
                .into(holder.imageView);

        holder.view_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((HomeActivity) activity).displayView(new FragmentFavoriteSpecials(), "FAV_SPECIALS", new Bundle());
            }
        });

    }

    @Override
    public int getItemCount() {
        if (list.size() > 9)
            return 10;
        else
            return list.size();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    ;


    public void tabContainer2Visible() {
        //homeTab2.setBackgroundColor(activity.getResources().getColor(R.color.colorWhite));
        //imageViewHomeTab2.setImageResource(R.drawable.ic_icon_home);

        HomeActivity.tabContainer2.setVisibility(View.GONE);
        HomeActivity.tabContainer2.setVisibility(View.GONE);
    }
}

package com.apitap.views.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import com.apitap.model.Utils;
import com.apitap.model.bean.FavBeanSpecial;
import com.apitap.model.bean.Favspecialbean;
import com.apitap.model.preferences.ATPreferences;
import com.apitap.views.HomeActivity;
import com.apitap.views.fragments.itemDetails.FragmentItemDetails;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Sahil on 1/9/16.
 */

public class FavouriteSpecialGroupAdapter extends RecyclerView.Adapter<FavouriteSpecialGroupAdapter.ViewHolder> {

    private final Activity activity;

    public List<FavBeanSpecial.PC> allImages;
    LayoutInflater inflater;
    List<FavBeanSpecial.RESULT> list;
    HashMap<Integer, ArrayList<Favspecialbean>> map;
    String selected_Sort;

    public FavouriteSpecialGroupAdapter(Activity activity, HashMap<Integer, ArrayList<Favspecialbean>> map, List<FavBeanSpecial.RESULT> list, String selected_Sort) {
        this.map = map;
        this.list = list;
        this.selected_Sort = selected_Sort;
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

        TextView mCategoryName, mViewAll;
        RecyclerView mTwoWayView;
        LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            mTwoWayView = (RecyclerView) itemView.findViewById(R.id.my_gallery);
            mCategoryName = (TextView) itemView.findViewById(R.id.category_name);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.mainlayout);
        }
    }

    int currentIndex = 0;


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, final int position) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vertical_row_fav, null);
        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        // there is second list which contains list of songs array
        allImages = list.get(position).getPC();
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
        //  holder.mTwoWayView.setAdapter(mAdapter);
        holder.mTwoWayView.setTag(position);
        holder.mCategoryName.setText(list.get(position).get12045());
        holder.mTwoWayView.setLayoutManager(layoutManager);
        Baseadap baseadap = new Baseadap(allImages);
        if (selected_Sort.equals("All")){
            holder.mTwoWayView.setAdapter(baseadap);
        }else{
        if (list.get(position).get12045().contains(selected_Sort)) {
            holder.mTwoWayView.setAdapter(baseadap);
        } else {
            holder.linearLayout.setVisibility(View.GONE);

        }}
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class Baseadap extends RecyclerView.Adapter<ViewHolders> {
        public List<FavBeanSpecial.PC> allImages;

        public Baseadap(List<FavBeanSpecial.PC> allImages) {
            this.allImages = allImages;
        }


        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemCount() {
            return allImages.size();
        }

        @Override
        public ViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_horizontal_fav, null);

            return new ViewHolders(view);
        }

        @Override
        public void onBindViewHolder(ViewHolders holder, final int position) {
         /*   if (allImages.get(position).get11498().equals(allImages.get(position).get122158()) || allImages.get(position).get122158().equals("") || allImages.get(position).get122158().equals("0") || allImages.get(position).get122158().equals("0.00")) {
                holder.rlSinglePrice.setVisibility(View.VISIBLE);
                holder.rlTwoPrice.setVisibility(View.GONE);
                holder.actualPrice.setText("$" + allImages.get(position).get11498());
            } else {
                holder.rlSinglePrice.setVisibility(View.GONE);
                holder.rlTwoPrice.setVisibility(View.VISIBLE);
                holder.actualPrice.setText("$" + allImages.get(position).get11498());
                holder.priceAfterDiscount.setText("$" + allImages.get(position).get122158());
                holder.actualPrice.setPaintFlags(holder.actualPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }*/


            String ActualPrice = allImages.get(position).get11498();
            String DiscountPrice = allImages.get(position).get122162();

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
            holder.description.setText(Utils.hexToASCII(allImages.get(position).get12083()));
            Log.d("specialadapter", Utils.hexToASCII(allImages.get(position).get12083()));
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String productId = Utils.lengtT(11, allImages.get(position).get114144());
                    ;
                    Log.d("ProductsID", productId);
                    String productType = allImages.get(position).get114112();
                    Log.d("productTypes", productType);
                    String ActualPrice = allImages.get(position).get11498();
                    String PriceAfterDiscount = allImages.get(position).get122158();
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
            Picasso.get().load(ATPreferences.readString(activity, Constants.KEY_IMAGE_URL) + allImages.get(position).get121170()).into(holder.imageView);


        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }
    }

    ;

    public class ViewHolders extends RecyclerView.ViewHolder {

        private final ImageView imageView;
        private final ImageView eye;

        private final TextView description, price, actualPrice, priceAfterDiscount;
        LinearLayout rlSinglePrice, rlTwoPrice;

        public ViewHolders(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.image);
            description = (TextView) itemView.findViewById(R.id.description);
            eye = (ImageView) itemView.findViewById(R.id.eye);

            rlSinglePrice = (LinearLayout) itemView.findViewById(R.id.rel_single_price);
            rlTwoPrice = (LinearLayout) itemView.findViewById(R.id.rl_two_price);
            price = (TextView) itemView.findViewById(R.id.price);
            actualPrice = (TextView) itemView.findViewById(R.id.actual_price);
            priceAfterDiscount = (TextView) itemView.findViewById(R.id.price_after_discount);
        }
    }

    public void tabContainer2Visible() {
        //homeTab2.setBackgroundColor(activity.getResources().getColor(R.color.colorWhite));
        //imageViewHomeTab2.setImageResource(R.drawable.ic_icon_home);

        HomeActivity.tabContainer2.setVisibility(View.GONE);
        HomeActivity.tabContainer2.setVisibility(View.GONE);
    }
}

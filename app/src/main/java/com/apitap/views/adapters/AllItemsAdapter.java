/*
package com.apitap.views.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apitap.R;
import com.apitap.model.Constants;
import com.apitap.model.Utils;
import com.apitap.model.bean.ItemsBean;
import com.apitap.model.preferences.ATPreferences;
import com.apitap.views.HomeActivity;
import com.apitap.views.fragments.itemDetails.FragmentItemDetails;
import com.linearlistview.LinearListView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.apitap.controller.HomeManager.itemsBean;

*/
/**
 * Created by Sahil on 1/9/16.
 *//*


public class AllItemsAdapter extends BaseAdapter {

    private final Activity activity;
    LayoutInflater inflater;
    List<ItemsBean.PC> allImages;
    List<ItemsBean.PC> pcList = new ArrayList<>();

    public AllItemsAdapter(FragmentActivity activity, List<ItemsBean.PC> allImages) {


        this.activity = activity;
        this.pcList = allImages;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }


    @Override
    public int getCount() {
        return pcList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    class ViewHolders {
        TextView mCategoryName, textViewCategoryTitle,mViewAll;
        LinearListView mTwoWayView;
        LinearLayout title_ll;
        LinearLayout view_ll, view_all;
        LinearLayout parent_ll;
        CardView card_ll;

    }

    int currentIndex = 0;

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolders holder;
        // if (convertView == null) {
        holder = new ViewHolders();
        convertView = inflater.inflate(R.layout.vertical_row, parent, false);
           //   view_ll.setVisibility(View.GONE);
        // title_ll.setVisibility(View.VISIBLE);
        //img_Items.setVisibility(View.VISIBLE);
        holder.mTwoWayView = convertView.findViewById(R.id.my_gallery);
        holder.mCategoryName = convertView.findViewById(R.id.category_name);
        holder.mViewAll = convertView.findViewById(R.id.view);
        holder.title_ll = convertView.findViewById(R.id.title_ll);
        holder.textViewCategoryTitle = convertView.findViewById(R.id.title);
        holder.view_ll = convertView.findViewById(R.id.view_ll);
        holder.parent_ll = convertView.findViewById(R.id.parentll);
        holder.card_ll = convertView.findViewById(R.id.cardView);
        holder.view_all = convertView.findViewById(R.id.view_all);

        if (itemsBean.getRESULT().get(0).getRESULT().size()>position&&itemsBean.getRESULT().get(0).getRESULT().get(position) != null) {
            Log.d("posutuins", position + "  d " + position);
            allImages = itemsBean.getRESULT().get(0).getRESULT().get(position).getPC();
            //mCategoryName.setText(filterCategoryBean1.getRESULT().get(0).getRESULT().get(i).getCA().get(i1).get12045());

            holder.mViewAll.setVisibility(View.GONE);
            holder.mTwoWayView.setAdapter(mAdapter);

            holder.mTwoWayView.setTag(position);

            holder.view_all.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.mViewAll.performClick();
                }
            });

        }
            return convertView;
    }



    public BaseAdapter mAdapter = new BaseAdapter() {
        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            if (convertView == null) {
                convertView = activity.getLayoutInflater().inflate(R.layout.row_horizontal_test, parent, false);
            }

            ImageView imageView = convertView.findViewById(R.id.image);
            TextView actualPrice = convertView.findViewById(R.id.actual_price);
            TextView priceAfterDiscount = convertView.findViewById(R.id.price_after_discount);
            TextView price = convertView.findViewById(R.id.price);

            ImageView eye = convertView.findViewById(R.id.eye);
            LinearLayout rlSinglePrice = convertView.findViewById(R.id.rel_single_price);
            LinearLayout rlTwoPrice = convertView.findViewById(R.id.rl_two_price);
            TextView description = convertView.findViewById(R.id.description);
            String isSeen = allImages.get(position).get1149();
            if (isSeen.equalsIgnoreCase("false")) {
                eye.setBackgroundResource(R.drawable.green_seen);
            } else {
                //   eye.setBackgroundResource(R.drawable.green_seen);
                eye.setVisibility(View.GONE);
            }

            String ActualPrice = String.format("%.2f", Double.parseDouble(allImages.get(position).get11498()));
            String DiscountPrice = String.format("%.2f", Double.parseDouble(allImages.get(position).get122158()));

            if (Double.parseDouble(ActualPrice) == Double.parseDouble(DiscountPrice) || DiscountPrice.equals("0") || DiscountPrice.equals("0.00") || Double.parseDouble(DiscountPrice) > Double.parseDouble(ActualPrice)) {
                rlSinglePrice.setVisibility(View.VISIBLE);
                rlTwoPrice.setVisibility(View.GONE);
                actualPrice.setText("$" + Utils.getFormatAmount(ActualPrice));

            } else if (ActualPrice.equals("0") || ActualPrice.equals("0.00")) {
                rlTwoPrice.setVisibility(View.VISIBLE);
                rlSinglePrice.setVisibility(View.GONE);
                priceAfterDiscount.setText("$" + Utils.getFormatAmount(DiscountPrice));
                actualPrice.setVisibility(View.GONE);

            } else if (Double.parseDouble(ActualPrice) > Double.parseDouble(DiscountPrice)) {
                rlTwoPrice.setVisibility(View.VISIBLE);
                rlSinglePrice.setVisibility(View.VISIBLE);
                priceAfterDiscount.setText("$" + Utils.getFormatAmount(DiscountPrice));
                actualPrice.setText("$" + Utils.getFormatAmount(ActualPrice));
                priceAfterDiscount.setGravity(Gravity.END);
                actualPrice.setGravity(Gravity.START);
                actualPrice.setTextColor(actualPrice.getResources().getColor(R.color.colorRed));
                actualPrice.setPaintFlags(actualPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }

            description.setText(Utils.hexToASCII(allImages.get(position).get12083()));

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String productId = Utils.lengtT(11, allImages.get(position).get114144());
                    String productType = allImages.get(position).get114112();
                    String actualPrice = allImages.get(position).get11498();
                    String priceAfterDiscount = allImages.get(position).get122158();
                    Bundle bundle = new Bundle();
                    bundle.putString("productId", productId);
                    bundle.putString("productType", productType);
                    bundle.putString("actualprice", actualPrice);
                    bundle.putString("discountprice", priceAfterDiscount);
                    FragmentItemDetails fragment = new FragmentItemDetails();
                    fragment.setArguments(bundle);
                    ((HomeActivity) activity).displayView(fragment, Constants.TAG_DETAILSPAGE, bundle);
                }
            });

            Picasso.get().load(ATPreferences.readString(activity,
                    Constants.KEY_IMAGE_URL) + "_t_" + allImages.get(position).get121170()).into(imageView);




            return convertView;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public int getCount() {
            if (allImages.size() > 9)
                return 10;
            else
                return allImages.size();
        }
    };
}
*/

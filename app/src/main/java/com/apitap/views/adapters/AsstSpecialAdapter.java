package com.apitap.views.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import com.google.android.material.tabs.TabLayout;
import androidx.cardview.widget.CardView;

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
import com.apitap.model.bean.SearchSpecialBean;
import com.apitap.model.bean.Storebean;
import com.apitap.model.preferences.ATPreferences;
import com.linearlistview.LinearListView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Sahil on 1/9/16.
 */

public class AsstSpecialAdapter extends BaseAdapter {

    private Activity activity;
    LayoutInflater inflater;
    HashMap<Integer, ArrayList<Storebean>> map;
    ArrayList<Storebean> allImages;
    TabLayout tabLayout;
    String selectedName;
    boolean viewAdded = false;
    ArrayList<Integer> add_positions = new ArrayList<>();
    ArrayList<String> favoritemerchants = new ArrayList<>();
    List<SearchSpecialBean.RESULT> arrayList = new ArrayList<>();


    public AsstSpecialAdapter(Activity activity, List<SearchSpecialBean.RESULT> arrayList, TabLayout tabLayout) {
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.tabLayout = tabLayout;
        this.arrayList = arrayList;
        this.activity = activity;

    }

    @Override
    public int getCount() {
        return arrayList.size();
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
        TextView mCategoryName;
        LinearListView mTwoWayView;
        TextView view_all, title, item_name, price, merchant_name;
        LinearLayout rlSinglePrice;
        LinearLayout rlTwoPrice;
        TextView actualPrice;
        TextView priceAfterDiscount;
        LinearLayout view_ll;
        LinearLayout parent_ll;
        CardView card_ll;
        ImageView main_img, eye, merchant_img;
        LinearLayout title_ll;
    }

    int currentIndex = 0;

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolders holder;
        // if (convertView == null) {
        holder = new ViewHolders();
        convertView = inflater.inflate(R.layout.vertical_row_search, parent, false);
        holder.main_img = (ImageView) convertView.findViewById(R.id.image);
        holder.merchant_img = (ImageView) convertView.findViewById(R.id.image2);
        holder.item_name = (TextView) convertView.findViewById(R.id.item1);
        holder.price = (TextView) convertView.findViewById(R.id.item3);
        holder.merchant_name = (TextView) convertView.findViewById(R.id.description2);
        holder.title = (TextView) convertView.findViewById(R.id.description);
        holder.eye = (ImageView) convertView.findViewById(R.id.eye);
        holder.rlSinglePrice = (LinearLayout) convertView.findViewById(R.id.rel_single_price);
        holder.rlTwoPrice = (LinearLayout) convertView.findViewById(R.id.rl_two_price);
        holder.actualPrice = (TextView) convertView.findViewById(R.id.actual_price);
        holder.priceAfterDiscount = (TextView) convertView.findViewById(R.id.price_after_discount);
        holder.parent_ll = (LinearLayout) convertView.findViewById(R.id.mainlin);

        setData(position, holder);

        return convertView;
    }

    private void setData(final int position, ViewHolders holder) {
       holder.merchant_name.setText(Utils.hexToASCII(arrayList.get(position).getPC().get(0).get_114170()));
        holder.title.setText((arrayList.get(position).getPC().get(0).get12083()));
        String isSeen = arrayList.get(position).getPC().get(0).get1149();
        if (isSeen.equalsIgnoreCase("false")) {
            holder.eye.setBackgroundResource(R.drawable.green_seen);
        } else {
            holder.eye.setVisibility(View.GONE);
        }
        Picasso.get().load(ATPreferences.readString(activity, Constants.KEY_IMAGE_URL) + arrayList.get(position)
                .getPC().get(0).get_12177()).into(holder.merchant_img);

        Picasso.get().load(ATPreferences.readString(activity, Constants.KEY_IMAGE_URL) + arrayList.get(position)
                .getPC().get(0).get121170()).into(holder.main_img);

        String ActualPrice = arrayList.get(position).getPC().get(0).get11498();
        String DiscountPrice = arrayList.get(position).getPC().get(0).get122162();

        if (ActualPrice.equals(DiscountPrice) || DiscountPrice.equals("") || DiscountPrice.equals("0") || DiscountPrice.equals("0.00")) {
            holder.rlSinglePrice.setVisibility(View.VISIBLE);
            holder.rlTwoPrice.setVisibility(View.GONE);
            holder.actualPrice.setText(/*"$"+*/allImages.get(position).getActualPrice());
        } else {
            holder.rlSinglePrice.setVisibility(View.GONE);
            holder.rlTwoPrice.setVisibility(View.VISIBLE);
            holder.actualPrice.setTextColor(Color.GRAY);
            holder.actualPrice.setText(ActualPrice);
            holder.priceAfterDiscount.setText(DiscountPrice);
            holder.actualPrice.setPaintFlags(holder.actualPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        }

    }


}

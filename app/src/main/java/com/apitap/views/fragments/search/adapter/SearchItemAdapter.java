package com.apitap.views.fragments.search.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apitap.R;
import com.apitap.model.Utils;
import com.apitap.model.bean.SearchBean;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ashok-kumar on 16/6/16.
 */

public class SearchItemAdapter extends RecyclerView.Adapter<SearchItemAdapter.ViewHolder> {
    HashMap<Integer, ArrayList<SearchBean>> map;
    Context context;
    private AdapterClick adapterClick;
    public SearchItemAdapter(Context context, HashMap<Integer, ArrayList<SearchBean>> array) {
        this.map = array;
        this.context = context;
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_horizontal_test, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.description.setText(Utils.hexToASCII( map.get(position).get(0).getProductName()));
        String isSeen = map.get(position).get(0).getIsSeen();
        if (isSeen.equalsIgnoreCase("false")) {
            holder.eye.setBackgroundResource(R.drawable.green_seen);
        } else {
            holder.eye.setVisibility(View.GONE);
        }
        String ActualPrice = String.format("%.2f", Double.parseDouble(map.get(position).get(0).getActualPrice()));
        String DiscountPrice = String.format("%.2f", Double.parseDouble(map.get(position).get(0).getPriceAfterDiscount()));

        if (Double.parseDouble(ActualPrice) == Double.parseDouble(DiscountPrice) || DiscountPrice.equals("0") || DiscountPrice.equals("0.00") || Double.parseDouble(DiscountPrice) > Double.parseDouble(ActualPrice)) {
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
            holder.actualPrice.setText("$" + Utils.getFormatAmount(ActualPrice));
            holder. priceAfterDiscount.setGravity(Gravity.END);
            holder.actualPrice.setGravity(Gravity.START);
            holder.actualPrice.setTextColor(context.getResources().getColor(R.color.colorRed));
            holder.actualPrice.setPaintFlags(holder.actualPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }

        Picasso.get().load(map.get(position).get(0).getImageUrls())
                .placeholder(R.drawable.ic_gallery_placeholder)
                .error(R.drawable.no_photo_placeholder)
                .into(holder.imageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterClick.onItemClick(v, position);
            }
        });

    }



    @Override
    public int getItemCount() {
        return map.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView imageView;
        private final ImageView eye;
        LinearLayout rlSinglePrice,rlTwoPrice;
        TextView description,price,priceAfterDiscount,actualPrice;
        public ViewHolder(View itemView) {
            super(itemView);

             imageView = itemView.findViewById(R.id.image);
             actualPrice = itemView.findViewById(R.id.actual_price);
             priceAfterDiscount = itemView.findViewById(R.id.price_after_discount);
             price = itemView.findViewById(R.id.price);

             eye = itemView.findViewById(R.id.eye);
             rlSinglePrice = itemView.findViewById(R.id.rel_single_price);
             rlTwoPrice = itemView.findViewById(R.id.rl_two_price);
             description = itemView.findViewById(R.id.description);
        }
    }
    public void setOnItemClickListner(AdapterClick adapterClick) {
        this.adapterClick = adapterClick;
    }

    public interface AdapterClick {
        void onItemClick(View v, int position);
    }
}

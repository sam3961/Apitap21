package com.apitap.views.fragments.checkinTv.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.apitap.R;

import java.util.ArrayList;

/**
 * Created by Shami on 1/9/16.
 */

public class AdapterGameDeals extends RecyclerView.Adapter<AdapterGameDeals.ViewHolder> {
    private Context context;
    private ArrayList<String> requestList;

    public AdapterGameDeals(Context context, ArrayList<String> requestList) {
        this.requestList = requestList;
        this.context = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_game_deals, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.textViewActualPrice.setPaintFlags(holder.textViewActualPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        // return requestList.size();
        return 8;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewActualPrice;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewActualPrice = itemView.findViewById(R.id.actual_price);


        }
    }

}

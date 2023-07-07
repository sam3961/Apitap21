package com.apitap.views.fragments.checkinTv.adapter;

import android.content.Context;
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

public class AdapterNoticesList extends RecyclerView.Adapter<AdapterNoticesList.ViewHolder> {
    private Context context;
    private ArrayList<String> requestList;
    private ClickListener clickListener;

    public AdapterNoticesList(Context context, ArrayList<String> requestList,
                              ClickListener clickListener) {
        this.requestList = requestList;
        this.context = context;
        this.clickListener = clickListener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_notices, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

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

        private TextView textViewTvName;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewTvName = itemView.findViewById(R.id.textViewTvName);
        }
    }

    public interface ClickListener {
        void onClick(View view, int position);
    }

}

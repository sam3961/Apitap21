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

public class AdapterStreamDirectory extends RecyclerView.Adapter<AdapterStreamDirectory.ViewHolder> {
    private Context context;
    private OnItemClickListener adapterClick;
    private ArrayList<String> requestList;

    public AdapterStreamDirectory(Context context, ArrayList<String> requestList,OnItemClickListener adapterClick) {
        this.requestList = requestList;
        this.context = context;
        this.adapterClick = adapterClick;
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_stream_directory, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {


        holder.itemView.setOnClickListener(v -> adapterClick.onItemClick(holder.getAdapterPosition()));

    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
       // return requestList.size();
        return 6;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewChannelName;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewChannelName = itemView.findViewById(R.id.textViewChannelName);


        }
    }

    public interface OnItemClickListener {
        public void onItemClick(int position);
    }
}

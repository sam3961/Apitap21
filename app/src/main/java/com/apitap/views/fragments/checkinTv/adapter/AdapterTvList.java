package com.apitap.views.fragments.checkinTv.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.apitap.R;
import com.apitap.model.Utils;
import com.apitap.views.fragments.checkinTv.FragmentCheckIn;
import com.apitap.views.streaming.ConnectionsFragment;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Shami on 1/9/16.
 */

public class AdapterTvList extends RecyclerView.Adapter<AdapterTvList.HostViewHolder> {
    private ArrayList<ConnectionsFragment.Endpoint> hostList;
    private ClickListener clickListener;
    FragmentCheckIn clientActivity;
    Context context;
    HashMap<String, String> activeTvListMap = new HashMap<>();
    HashMap<String, String> activeTvMediaName = new HashMap<>();
    HashMap<String, Long> activeTvMediaDuration = new HashMap<>();

    public AdapterTvList(FragmentCheckIn clientActivity, ArrayList<ConnectionsFragment.Endpoint> requestList,
                         ClickListener clickListener) {
        this.clientActivity = clientActivity;
        this.hostList = requestList;
        this.clickListener = clickListener;
    }


    @NonNull
    @Override
    public HostViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        View view = inflater.inflate(R.layout.adapter_tv_list, viewGroup, false);
        context = viewGroup.getContext();
        HostViewHolder viewHolder = new HostViewHolder(view);
        return viewHolder;
    }


    public static class HostViewHolder extends RecyclerView.ViewHolder {
        public ConstraintLayout row;
        public TextView textViewChannelName;
        public TextView textViewTvName;
        public TextView textViewShowTitle;

        public HostViewHolder(@NonNull View itemView) {
            super(itemView);
            row = itemView.findViewById(R.id.host_list_row);
            textViewChannelName = itemView.findViewById(R.id.textViewChannelName);
            textViewTvName = itemView.findViewById(R.id.textViewTvName);
            textViewShowTitle = itemView.findViewById(R.id.textViewShowTitle);
        }
    }


    @SuppressLint("SuspiciousIndentation")
    @Override
    public void onBindViewHolder(@NonNull HostViewHolder holder, final int position) {
        holder.textViewTvName.setText(hostList.get(holder.getAdapterPosition()).getName());

//        if (activeTvListMap.size() > 0 && activeTvListMap.containsKey(hostList.get(holder.getAdapterPosition()).getName()))
//            holder.textViewChannelName.setText(activeTvListMap.get(
//                    hostList.get(holder.getAdapterPosition()).getName()));


//        if (holder.textViewChannelName.getText().toString().isEmpty())
//            holder.textViewChannelName.setText("Currently Playing");

        if (hostList.get(holder.getAdapterPosition()).isActive())
            holder.textViewTvName.setBackgroundColor(ContextCompat.getColor(context, R.color.colorGreenLogo));
        else
            holder.textViewTvName.setBackgroundColor(ContextCompat.getColor(context, R.color.colorGrey1));

            holder.textViewShowTitle.setText(
                    activeTvMediaName.get(
                    hostList.get(holder.getAdapterPosition()).getName()));

//            if (holder.textViewShowTitle.getText().toString().isEmpty())
//                holder.row.setVisibility(View.GONE);
//            else
//                holder.row.setVisibility(View.VISIBLE);


        holder.itemView.setOnClickListener(v -> {
          //  Log.d("tagss", activeTvMediaName.get(hostList.get(holder.getAdapterPosition()).getName()));
            if (hostList.get(holder.getAdapterPosition()).isActive())
                clickListener.onClick(holder.getAdapterPosition(),
                        activeTvMediaName.get(
                                hostList.get(holder.getAdapterPosition()).getName())
                        ,
                        activeTvMediaDuration.get(hostList.get(holder.getAdapterPosition()).getName()));
        });
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return hostList.size();
    }


    public void updateList(ArrayList<ConnectionsFragment.Endpoint> temp) {
        this.hostList = temp;
        notifyDataSetChanged();

    }


    public interface ClickListener {
        void onClick(int host,String mediaName,Long duration);
    }

    public void setAPITvList(HashMap<String, String> activeTvListName, HashMap<String, String> activeTvMediaName, HashMap<String, Long> tvMapMediaDuration) {
        this.activeTvListMap = activeTvListName;
        this.activeTvMediaName = activeTvMediaName;
        this.activeTvMediaDuration = tvMapMediaDuration;
        notifyDataSetChanged();
    }

}

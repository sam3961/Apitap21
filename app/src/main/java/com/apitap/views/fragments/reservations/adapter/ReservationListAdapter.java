package com.apitap.views.fragments.reservations.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.apitap.R;
import com.apitap.model.Utils;
import com.apitap.model.bean.MessageListBean;
import com.apitap.model.getReservation.RESULTItem;

import java.util.List;

/**
 * Created by appzorro on 1/9/16.
 */

public class ReservationListAdapter extends RecyclerView.Adapter<ReservationListAdapter.ViewHolder> {

    private AdapterClick adapterClick;
    private Context context;
    private final List<RESULTItem> list;

    public ReservationListAdapter(List<RESULTItem> list,AdapterClick adapterClick) {
        this.list = list;
        this.adapterClick = adapterClick;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_reservation, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.tvName.setText(Utils.hexToASCII(list.get(holder.getAdapterPosition()).getJsonMember11453()));
        holder.tvStartEndTime.setText(
                Utils.removeLastChar(list.get(holder.getAdapterPosition()).getJsonMember116202(), 3)
                        + "-" +
                        Utils.removeLastChar(list.get(holder.getAdapterPosition()).getJsonMember116203(), 3)
        );

        holder.tvDate.setText(list.get(position).getJsonMember116201());
        holder.tvOrderId.setText("#"+list.get(position).getJsonMember116200());


        holder.linearLayoutRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterClick.onItemClick(v, list.get(holder.getAdapterPosition()).getJsonMember116200());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvName, tvStartEndTime, tvDate,tvOrderId;
        private final LinearLayout linearLayoutRoot;


        public ViewHolder(View itemView) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            tvName = itemView.findViewById(R.id.tvName);
            tvStartEndTime = itemView.findViewById(R.id.tvStartEndTime);
            tvDate = itemView.findViewById(R.id.tvDate);
            linearLayoutRoot = itemView.findViewById(R.id.linearLayoutRoot);


        }
    }

    public void setOnItemClickListner(AdapterClick adapterClick) {
        this.adapterClick = adapterClick;
    }

    public interface AdapterClick {
        void onItemClick(View v, String  id);
    }

}

package com.apitap.views.adapters;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apitap.R;

import java.util.List;

/**
 * Created by appzorro on 1/9/16.
 */

public class AdapterBusinessSelect extends RecyclerView.Adapter<AdapterBusinessSelect.ViewHolder> {

    private List<String> businessList;
    private BusinessClick adapterClick;
    private Context mContext;
    private int row_index=-1;

    public AdapterBusinessSelect(List<String> response,BusinessClick businessClick) {
        this.businessList = response;
        this.adapterClick = businessClick;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_select_business, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.textViewBusiness.setText(businessList.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                row_index=position;
                adapterClick.onBusinessSelect(position);
                notifyDataSetChanged();
            }
        });


        if (row_index == position) {
            holder.linearLayout.setBackgroundColor(mContext.getResources().getColor(R.color.gray));
        } else {
            holder.linearLayout.setBackgroundColor(mContext.getResources().getColor(R.color.colorWhite));
        }


    }

    @Override
    public int getItemCount() {
        return businessList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView textViewBusiness;
        private final LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewBusiness = itemView.findViewById(R.id.textViewBusiness);
            linearLayout = itemView.findViewById(R.id.linearLayout);
        }
    }

    public void setOnItemClick(BusinessClick adapterClick) {
        this.adapterClick = adapterClick;
    }

    public interface BusinessClick {
        public void onBusinessSelect(int position);
    }

}

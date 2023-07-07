package com.apitap.views.adapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apitap.R;

import java.util.ArrayList;

/**
 * Created by Shami on 1/9/16.
 */

public class AdapterSortBy extends RecyclerView.Adapter<AdapterSortBy.ViewHolder> {
    private Context context;
    private SortByItemClick adapterClick;
    private ArrayList<String> arrayListSortBy;
    private int row_index=0;


    public AdapterSortBy(Context context, ArrayList<String> arrayListSortBy,SortByItemClick adapterClick) {
        this.arrayListSortBy = arrayListSortBy;
        this.context = context;
        this.adapterClick = adapterClick;
    }

    public void customNotify(int position) {
        this.row_index=position;
        notifyDataSetChanged();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_sort_by, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.textViewTitle.setText(arrayListSortBy.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterClick.onSortClick(position);
            }
        });

        if (row_index == position) {
            holder.linearLayoutParent.setBackground(context.getResources().getDrawable(R.drawable.rounded_filled_green));
        } else {
            holder.linearLayoutParent.setBackground(context.getResources().getDrawable(R.drawable.back_round_black_border));
        }



    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return arrayListSortBy.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewTitle;
        private LinearLayout linearLayoutParent;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            linearLayoutParent = itemView.findViewById(R.id.linearLayoutParent);
        }
    }

    public void setOnItemClickListner(SortByItemClick adapterClick) {
        this.adapterClick = adapterClick;
    }

    public interface SortByItemClick {
        public void onSortClick(int position);
    }
}

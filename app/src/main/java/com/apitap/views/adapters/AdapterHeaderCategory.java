package com.apitap.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.apitap.R;

import java.util.List;

/**
 * Created by appzorro on 1/9/16.
 */

public class AdapterHeaderCategory extends RecyclerView.Adapter<AdapterHeaderCategory.ViewHolder> {

    public List<String> categoryList;
    private CategoryHeaderClick adapterClick;
    private Context mContext;
    private int row_index=-1;

    public AdapterHeaderCategory(List<String> response, CategoryHeaderClick businessClick) {
        this.categoryList = response;
        this.adapterClick = businessClick;
    }

    public void customNotifyRowClick(int row_index){
        this.row_index =row_index;
        notifyDataSetChanged();
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_header_category, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        if(row_index==position){
            holder.linearLayout.setBackground(mContext.getResources().getDrawable(R.drawable.blue_single_line));
        }
        else
        {
            holder.linearLayout.setBackground(mContext.getResources().getDrawable(R.drawable.linearlayout_white_selector));
        }

        holder.textViewCategoryName.setText(categoryList.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                row_index=position;
                adapterClick.onCategoryHeaderClick(position);
                notifyDataSetChanged();
            }
        });


    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public void customNotify(List<String> arrayListHeaderCategory) {
        this.categoryList = arrayListHeaderCategory;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView textViewCategoryName;
        private final LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.linearLayout);
            textViewCategoryName = itemView.findViewById(R.id.textViewCategoryName);
        }
    }

    public void setOnItemClick(CategoryHeaderClick adapterClick) {
        this.adapterClick = adapterClick;
    }

    public interface CategoryHeaderClick {
        public void onCategoryHeaderClick(int position);
    }

}

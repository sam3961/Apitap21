package com.apitap.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.apitap.R;
import com.apitap.model.bean.SelectedParentModel;
import com.google.android.gms.vision.L;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shami on 1/9/16.
 */

public class AdapterParentCategories extends RecyclerView.Adapter<AdapterParentCategories.ViewHolder> {
    private Context context;
    private ParentCategoryClick adapterClick;
    private List<SelectedParentModel> arrayListCategories;

    public AdapterParentCategories(Context context, List<SelectedParentModel> arrayListCategories, ParentCategoryClick adapterClick) {
        this.arrayListCategories = arrayListCategories;
        this.context = context;
        this.adapterClick = adapterClick;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_parent_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.textViewTitle.setText(arrayListCategories.get(position).getTitle());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterClick.onParentCategoryClick(position);
            }
        });

    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return arrayListCategories.size();
    }

    public void customNotify(List<SelectedParentModel> arrayListSelectParent) {
        this.arrayListCategories=arrayListSelectParent;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewParentTitle);
        }
    }

    public void setOnItemClickListner(ParentCategoryClick adapterClick) {
        this.adapterClick = adapterClick;
    }

    public interface ParentCategoryClick {
        public void onParentCategoryClick(int position);
    }
}

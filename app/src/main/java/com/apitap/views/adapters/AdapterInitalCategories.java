package com.apitap.views.adapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.apitap.R;

import java.util.ArrayList;

/**
 * Created by Shami on 1/9/16.
 */

public class AdapterInitalCategories extends RecyclerView.Adapter<AdapterInitalCategories.ViewHolder> {
    private Context context;
    private CategoriesItemClick adapterClick;
    private ArrayList<String> arrayListCategories;

    public AdapterInitalCategories(Context context, ArrayList<String> arrayListCategories, CategoriesItemClick adapterClick) {
        this.arrayListCategories = arrayListCategories;
        this.context = context;
        this.adapterClick = adapterClick;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_categories, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.textViewTitle.setText(arrayListCategories.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterClick.onInitialCategoryClick(position);
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



    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
        }
    }

    public void setOnItemClickListner(CategoriesItemClick adapterClick) {
        this.adapterClick = adapterClick;
    }

    public interface CategoriesItemClick {
        public void onInitialCategoryClick(int position);
    }
}

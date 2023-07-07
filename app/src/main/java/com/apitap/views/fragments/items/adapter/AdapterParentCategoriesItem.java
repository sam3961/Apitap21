package com.apitap.views.fragments.items.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.apitap.R;
import com.apitap.model.bean.SelectedParentModel;

import java.util.List;

/**
 * Created by Shami on 1/9/16.
 */

public class AdapterParentCategoriesItem extends RecyclerView.Adapter<AdapterParentCategoriesItem.ViewHolder> {
    private Context context;
    private ParentCategoryClick adapterClick;
    private List<SelectedParentModel> arrayListCategories;

    public AdapterParentCategoriesItem(Context context, List<SelectedParentModel> arrayListCategories, ParentCategoryClick adapterClick) {
        this.arrayListCategories = arrayListCategories;
        this.context = context;
        this.adapterClick = adapterClick;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_parent_category_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.textViewTitle.setText(arrayListCategories.get(position).getTitle());

        holder.itemView.setOnClickListener(v -> adapterClick.onParentCategoryClick(position));
        holder.imageViewBack.setOnClickListener(v -> adapterClick.onParentCategoryClick(position));

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
        private ImageView imageViewBack;

        public ViewHolder(View itemView) {
            super(itemView);
            imageViewBack = itemView.findViewById(R.id.imageViewBack);
            textViewTitle = itemView.findViewById(R.id.textViewParentTitle);

            imageViewBack.setVisibility(View.VISIBLE);
        }


    }

    public void setOnItemClickListner(ParentCategoryClick adapterClick) {
        this.adapterClick = adapterClick;
    }

    public interface ParentCategoryClick {
        public void onParentCategoryClick(int position);
    }
}

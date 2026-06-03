package com.apitap.app.views.fragments.search.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.apitap.app.App;
import com.apitap.app.R;
import com.apitap.app.model.bean.SearchBusinessBean;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SearchBusinessAdapter extends RecyclerView.Adapter<SearchBusinessAdapter.ViewHolder> {

    private final ArrayList<SearchBusinessBean> businesses;
    private final Context context;
    private AdapterClick adapterClick;

    public SearchBusinessAdapter(Context context, ArrayList<SearchBusinessBean> businesses) {
        this.context = context;
        this.businesses = businesses;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_search_item_promotion, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        SearchBusinessBean business = businesses.get(position);
        holder.textViewStoreName.setText(business.getBusinessName());

        if (App.getInstance().listOfSeenMerchants.contains(business.getBusinessId())){
            holder.eye.setBackgroundResource(R.drawable.green_seen);
        } else {
            holder.eye.setBackgroundResource(R.drawable.grey_seen);
        }


        Picasso.get().load(business.getImageUrl())
                .placeholder(R.drawable.ic_gallery_placeholder)
                .error(R.drawable.no_photo_placeholder)
                .into(holder.imageViewStore);

        holder.itemView.setOnClickListener(v -> {
            if (adapterClick != null) {
                adapterClick.onItemClick(v, position);
                App.getInstance().listOfSeenMerchants.add(business.getBusinessId());
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return businesses.size();
    }

    public void setOnItemClickListner(AdapterClick adapterClick) {
        this.adapterClick = adapterClick;
    }

    public interface AdapterClick {
        void onItemClick(View v, int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewStoreName;
        private final ImageView imageViewStore;
        private final ImageView eye;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewStoreName = itemView.findViewById(R.id.textViewStoreName);
            imageViewStore = itemView.findViewById(R.id.imageViewStore);
            eye = itemView.findViewById(R.id.eye);
        }
    }
}

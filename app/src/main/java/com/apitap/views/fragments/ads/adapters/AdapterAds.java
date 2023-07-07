package com.apitap.views.fragments.ads.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.apitap.R;
import com.apitap.model.Constants;
import com.apitap.model.Utils;
import com.apitap.model.bean.ads.RESULT;
import com.apitap.model.preferences.ATPreferences;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

/**
 * Created by Shami on 1/9/16.
 */

public class AdapterAds extends RecyclerView.Adapter<AdapterAds.ViewHolder> {
    private Context context;
    private ItemListClick adapterClick;
    private List<RESULT> arrayListItems;

    public AdapterAds(Context context, List<RESULT> arrayListStores, ItemListClick adapterClick) {
        this.arrayListItems = arrayListStores;
        this.context = context;
        this.adapterClick = adapterClick;
    }


    public void customNotify(List<RESULT> arrayListItems) {
        this.arrayListItems = arrayListItems;
        notifyDataSetChanged();

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_ads, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.textViewAdName.setText(Utils.hexToASCII(arrayListItems.get(position).get_12083()));
        holder.textViewStoreName.setText(Utils.hexToASCII(arrayListItems.get(position).get_11470()));

        if (arrayListItems.get(position).get_1149().equals("true"))
            holder.imageViewSeen.setImageDrawable(context.getResources().getDrawable(R.drawable.grey_seen));
        else
            holder.imageViewSeen.setImageDrawable(context.getResources().getDrawable(R.drawable.green_seen));


        Glide.with(context).load(ATPreferences.readString(context, Constants.KEY_IMAGE_URL)
                + arrayListItems.get(position).get_121170()).placeholder
                (R.drawable.loading_no_border).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).into(holder.imageViewAd);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterClick.onItemsClick(position);
            }
        });

    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return arrayListItems.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewAdName;
        private TextView textViewStoreName;
        private ImageView imageViewAd;
        private ImageView imageViewSeen;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewAdName = itemView.findViewById(R.id.adName);
            textViewStoreName = itemView.findViewById(R.id.store_name);
            imageViewSeen = itemView.findViewById(R.id.eye);
            imageViewAd = itemView.findViewById(R.id.imageView);

        }
    }

    public void setOnItemClickListner(ItemListClick adapterClick) {
        this.adapterClick = adapterClick;
    }

    public interface ItemListClick {
        public void onItemsClick(int position);
    }
}

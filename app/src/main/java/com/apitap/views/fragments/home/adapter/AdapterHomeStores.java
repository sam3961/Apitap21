package com.apitap.views.fragments.home.adapter;

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
import com.apitap.model.home.stores.RESULTItem;
import com.apitap.model.preferences.ATPreferences;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Shami on 1/9/16.
 */

public class AdapterHomeStores extends RecyclerView.Adapter<AdapterHomeStores.ViewHolder> {
    private Context context;
    private StoreItemClick adapterClick;
    private List<RESULTItem> arrayListStores;

    public AdapterHomeStores(Context context, List<RESULTItem> arrayListStores, StoreItemClick adapterClick) {
        this.arrayListStores = arrayListStores;
        this.context = context;
        this.adapterClick = adapterClick;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_stores, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.textViewStoreName.setText(Utils.hexToASCII(arrayListStores.get(position).getJsonMember11470()));
        holder.imageViewSeen.setVisibility(View.GONE);

/*
        Picasso.get().load(ATPreferences.readString(context, Constants.KEY_IMAGE_URL)
                + "_t_" + arrayListStores.get(position).getJsonMember121170()).placeholder(R.drawable.splash_screen_new)
                .error(R.drawable.splash_screen_new).into(holder.imageViewStore);
*/

        Glide.with(context).load(ATPreferences.readString(context, Constants.KEY_IMAGE_URL)
                + "_t_" + arrayListStores.get(position).getJsonMember121170())
                .placeholder(R.drawable.ic_gallery_placeholder)
                .error(R.drawable.no_photo_placeholder)
                .fitCenter().centerInside()
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).into(holder.imageViewStore);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterClick.onStoreClick(position);
            }
        });

    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return arrayListStores.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewStoreName;
        private ImageView imageViewStore;
        private ImageView imageViewSeen;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewStoreName = itemView.findViewById(R.id.textViewStoreName);
            imageViewStore = itemView.findViewById(R.id.imageViewStore);
            imageViewSeen = itemView.findViewById(R.id.eye);
        }
    }

    public void setOnItemClickListner(StoreItemClick adapterClick) {
        this.adapterClick = adapterClick;
    }

    public interface StoreItemClick {
        public void onStoreClick(int position);
    }
}

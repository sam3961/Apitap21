package com.apitap.views.fragments.stores.adapters;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.apitap.R;
import com.apitap.model.Constants;
import com.apitap.model.Utils;
import com.apitap.model.bean.merchantStores.RESULT;
import com.apitap.model.preferences.ATPreferences;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Shami on 1/9/16.
 */

public class AdapterStores extends RecyclerView.Adapter<AdapterStores.ViewHolder> {
    private Context context;
    private StoreItemClick adapterClick;
    private List<RESULT> arrayListStores;

    public AdapterStores(Context context, List<RESULT> arrayListStores, StoreItemClick adapterClick) {
        this.arrayListStores = arrayListStores;
        this.context = context;
        this.adapterClick = adapterClick;
    }

    public void clear() {
        int size = arrayListStores.size();
        arrayListStores.clear();
        notifyItemRangeRemoved(0, size);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_stores, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.textViewStoreName.setText(Utils.hexToASCII(arrayListStores.get(position).get_11470()));

        if (arrayListStores.get(position).get_1149().equals("true"))
            holder.imageViewSeen.setImageDrawable(context.getResources().getDrawable(R.drawable.grey_seen));
        else
            holder.imageViewSeen.setImageDrawable(context.getResources().getDrawable(R.drawable.green_seen));

        Picasso.get().load(ATPreferences.readString(context, Constants.KEY_IMAGE_URL)
                + "_t_" + arrayListStores.get(position).get_121170())
                .placeholder(R.drawable.ic_gallery_placeholder)
                .error(R.drawable.no_photo_placeholder)
                .into(holder.imageViewStore);


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

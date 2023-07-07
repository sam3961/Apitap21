package com.apitap.views.fragments.ads.storeFront;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.apitap.R;
import com.apitap.model.Constants;
import com.apitap.model.Utils;
import com.apitap.model.preferences.ATPreferences;
import com.apitap.model.storeFrontAds.ADItem;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

/**
 * Created by Shami on 1/9/16.
 */

public class AdapterChildAds extends RecyclerView.Adapter<AdapterChildAds.ViewHolder> {
    private Context context;
    private AdsClick adapterClick;
    private List<ADItem> arrayListItems;

    public AdapterChildAds(Context context, List<ADItem> arrayListStores, AdsClick adapterClick) {
        this.arrayListItems = arrayListStores;
        this.context = context;
        this.adapterClick = adapterClick;
    }

    public void customNotify(List<ADItem> arrayListItems) {
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
        holder.textViewAdName.setText(Utils.hexToASCII(arrayListItems.get(position).getJsonMember12083()));
        holder.textViewStoreName.setText(Utils.hexToASCII(arrayListItems.get(position).getJsonMember11470()));

        if (arrayListItems.get(position).getJsonMember1149().equals("true"))
            holder.imageViewSeen.setImageDrawable(context.getResources().getDrawable(R.drawable.grey_seen));
        else
            holder.imageViewSeen.setImageDrawable(context.getResources().getDrawable(R.drawable.green_seen));


        Glide.with(context).load(ATPreferences.readString(context, Constants.KEY_IMAGE_URL)
                + arrayListItems.get(position).getJsonMember121170()).placeholder
                (R.drawable.loading_no_border).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).into(holder.imageViewAd);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String imageId = arrayListItems.get(position).getJsonMember12086();
                String imageUrl = "";
                String videoUrl = arrayListItems.get(position).getJsonMember12115();
                if (!imageId.isEmpty()) {
                    imageUrl = ATPreferences.readString(context,
                            Constants.KEY_IMAGE_URL) + arrayListItems.get(position).getJsonMember121170();
                    videoUrl = "";
                }
                String merchant= Utils.hexToASCII(arrayListItems.get(position).getJsonMember11470());
                String adName= Utils.hexToASCII(arrayListItems.get(position).getJsonMember12083());
                String desc= Utils.hexToASCII(arrayListItems.get(position).getJsonMember120157());
                String id= arrayListItems.get(position).getJsonMember12118();

                adapterClick.onAdsClick(imageId,imageUrl,videoUrl,merchant,adName,desc,id,position);
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

    public void setOnItemClickListner(AdsClick adapterClick) {
        this.adapterClick = adapterClick;
    }

    public interface AdsClick {
        public void onAdsClick(String imageId,
                               String imageUrl, String videoUrl,
                               String merchant, String adName, String desc, String id,int position);
    }
}

package com.apitap.views.adapters;

import android.app.Activity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.apitap.R;
import com.apitap.model.Constants;
import com.apitap.model.bean.AdsListBean;
import com.apitap.model.preferences.ATPreferences;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by appzorro on 1/9/16.
 */

public class SimilarAdsListingAdapter extends RecyclerView.Adapter<SimilarAdsListingAdapter.ViewHolder> {

    private AdapterClick adapterClick;
    List<AdsListBean.RESULT.AdsData.IR> list;
    Activity activity;

    public SimilarAdsListingAdapter(Activity activity, List<AdsListBean.RESULT.AdsData.IR> list) {
        this.activity = activity;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_ad_similarlisting, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {


        Picasso.get().load(ATPreferences.readString(activity, Constants.KEY_IMAGE_URL) +
                list.get(position).getImageName()).into(holder.img);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterClick.onItemClick(v, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView img;

        public ViewHolder(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.img);

        }
    }

    public void setOnItemClickListner(AdapterClick adapterClick) {
        this.adapterClick = adapterClick;
    }

    public interface AdapterClick {
        public void onItemClick(View v, int position);
    }

}

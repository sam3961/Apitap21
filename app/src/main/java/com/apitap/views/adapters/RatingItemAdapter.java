package com.apitap.views.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.apitap.R;
import com.apitap.model.Utils;
import com.apitap.model.bean.RatingBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vijay on 26/2/18.
 */

public class RatingItemAdapter extends RecyclerView.Adapter<RatingItemAdapter.MyViewHolder> {
    Context context;
    List<RatingBean.RESULT_> arrayList = new ArrayList<>();

    public RatingItemAdapter(Activity rateMerchant, List<RatingBean.RESULT_> arrayList) {
        this.arrayList=arrayList;
        this.context= rateMerchant;
    }

    public void cutomNotify(List<RatingBean.RESULT_> arrayList) {
        this.arrayList=arrayList;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView mDate,mName,mReview;
        RatingBar ratingBar;
        public MyViewHolder(View itemView) {
            super(itemView);
            mDate = (TextView)itemView.findViewById(R.id.date);
            mName = (TextView)itemView.findViewById(R.id.name);
            mReview = (TextView)itemView.findViewById(R.id.comment);
            ratingBar = (RatingBar) itemView.findViewById(R.id.ratingBar);
        }
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ratingxml, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.mReview.setText(Utils.hexToASCII(arrayList.get(position).get12083()));
        LayerDrawable stars = (LayerDrawable) holder.ratingBar.getProgressDrawable();

        String dateTime[]= arrayList.get(position).get_11438().split(" ");
        String date = dateTime[0];

        holder.mDate.setText(date);

        String FullName= Utils.hexToASCII(arrayList.get(position).get_1143())+" "+Utils.hexToASCII(arrayList.get(position).get_1145());
        holder.mName.setText(" "+ FullName);

        String rate = RatingfromServer(arrayList.get(position).get_122129());
        if (arrayList.get(position).get12180().equals("0")) {
            stars.getDrawable(0).setColorFilter(Color.parseColor("#fcb74d"), PorterDuff.Mode.SRC_ATOP);

            holder.ratingBar.setProgress((int) Double.parseDouble(rate));
        } else {
            stars.getDrawable(2).setColorFilter(Color.parseColor("#fcb74d"), PorterDuff.Mode.SRC_ATOP);
            stars.getDrawable(0).setColorFilter(Color.parseColor("#fcb74d"), PorterDuff.Mode.SRC_ATOP);
            stars.getDrawable(1).setColorFilter(Color.parseColor("#fcb74d"), PorterDuff.Mode.SRC_ATOP);
            holder.ratingBar.setRating(Float.parseFloat(rate));
            //ratingNo.setText("(" +data.getReview_count()+ ")");
        }

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    private String RatingfromServer(String s) {
        String RatingServerNo = "";
        switch (s) {
            case "2101":
                RatingServerNo = "1.0";
                break;
            case "2102":
                RatingServerNo = "2.0";
                break;
            case "2103":
                RatingServerNo = "3.0";
                break;
            case "2104":
                RatingServerNo = "4.0";
                break;
            case "2105":
                RatingServerNo = "5.0";
                break;
        }
        return RatingServerNo;
    }


}


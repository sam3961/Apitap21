package com.apitap.views.fragments.storefront.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.apitap.R;
import com.apitap.model.Constants;
import com.apitap.model.Utils;
import com.apitap.model.preferences.ATPreferences;
import com.apitap.model.storeFrontItems.specials.RESULTItem;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Shami on 1/9/16.
 */

public class AdapterStoreSpecials extends RecyclerView.Adapter<AdapterStoreSpecials.ViewHolder> {
    private Context context;
    private SpecialListClick adapterClick;
    private List<RESULTItem> arrayListSpecials;

    public AdapterStoreSpecials(Context context, List<RESULTItem> arrayListStores, SpecialListClick adapterClick) {
        this.arrayListSpecials = arrayListStores;
        this.context = context;
        this.adapterClick = adapterClick;
    }


    public void customNotify(List<RESULTItem> arrayListItems) {
        this.arrayListSpecials = arrayListItems;
        notifyDataSetChanged();

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_specials, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (arrayListSpecials.size() > 9 && position == 9) {
            holder.linearLayoutViewAll.setVisibility(View.VISIBLE);
        } else {
            holder.linearLayoutViewAll.setVisibility(View.GONE);
        }
        holder.linearLayoutSinglePrice.setVisibility(View.VISIBLE);
        holder.linearLayoutTwoPrices.setVisibility(View.GONE);
        holder.textViewActualPrice.setText(arrayListSpecials.get(position).getJsonMember122162());


        holder.textViewItemName.setText(Utils.hexToASCII(arrayListSpecials.get(position).getJsonMember12083()));

        if (arrayListSpecials.get(position).getJsonMember1149().equals("true"))
            holder.imageViewSeen.setImageDrawable(context.getResources().getDrawable(R.drawable.grey_seen));
        else
            holder.imageViewSeen.setImageDrawable(context.getResources().getDrawable(R.drawable.green_seen));


        Picasso.get().load(ATPreferences.readString(context, Constants.KEY_IMAGE_URL)
                + "_t_" + arrayListSpecials.get(position).getJsonMember121170())
                .placeholder(R.drawable.ic_gallery_placeholder)
                .error(R.drawable.no_photo_placeholder)
                .fit().centerInside().into(holder.imageViewItem);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterClick.onSpecialsClick(position);
            }
        });
        holder.linearLayoutViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterClick.onSpecialsViewMore();
            }
        });

    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        if (arrayListSpecials.size() > 9)
            return 10;
        else
            return arrayListSpecials.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewItemName;
        private TextView textViewActualPrice;
        private TextView textViewDiscountPrice;
        private ImageView imageViewItem;
        private ImageView imageViewSeen;
        private LinearLayout linearLayoutSinglePrice;
        private LinearLayout linearLayoutTwoPrices;
        private LinearLayout linearLayoutViewAll;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewItemName = itemView.findViewById(R.id.textViewItemName);
            imageViewItem = itemView.findViewById(R.id.imageViewItem);
            imageViewSeen = itemView.findViewById(R.id.eye);
            textViewActualPrice = itemView.findViewById(R.id.actual_price);
            textViewDiscountPrice = itemView.findViewById(R.id.price_after_discount);
            linearLayoutSinglePrice = itemView.findViewById(R.id.rel_single_price);
            linearLayoutTwoPrices = itemView.findViewById(R.id.rl_two_price);
            linearLayoutViewAll = itemView.findViewById(R.id.view_all);

        }
    }

    public void setOnItemClickListner(SpecialListClick adapterClick) {
        this.adapterClick = adapterClick;
    }

    public interface SpecialListClick {
        public void onSpecialsClick(int position);

        public void onSpecialsViewMore();
    }
}

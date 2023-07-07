package com.apitap.views.fragments.storefront.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.Gravity;
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
import com.apitap.model.showCase.RESULTItem;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Shami on 1/9/16.
 */

public class AdapterShowCase extends RecyclerView.Adapter<AdapterShowCase.ViewHolder> {
    private Context context;
    private ShowCaseItemClick adapterClick;
    private List<RESULTItem> arrayListShowCase;

    public AdapterShowCase(Context context, List<RESULTItem> arrayListStores, ShowCaseItemClick adapterClick) {
        this.arrayListShowCase = arrayListStores;
        this.context = context;
        this.adapterClick = adapterClick;
    }


    public void customNotify(List<RESULTItem> arrayListItems) {
        this.arrayListShowCase = arrayListItems;
        notifyDataSetChanged();

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_showcase, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {




        String ActualPrice = String.format("%.2f", Double.parseDouble(arrayListShowCase.get(position).getJsonMember11498()));
        String DiscountPrice = String.format("%.2f", Double.parseDouble(arrayListShowCase.get(position).getJsonMember122158()));

        if (Double.parseDouble(ActualPrice) == Double.parseDouble(DiscountPrice) || DiscountPrice.equals("0") || DiscountPrice.equals("0.00") || Double.parseDouble(DiscountPrice) > Double.parseDouble(ActualPrice)) {
            holder.linearLayoutSinglePrice.setVisibility(View.VISIBLE);
            holder.linearLayoutTwoPrices.setVisibility(View.GONE);
            holder.textViewActualPrice.setText("$" + Utils.getFormatAmount(ActualPrice));

        } else if (ActualPrice.equals("0") || ActualPrice.equals("0.00")) {
            holder.linearLayoutTwoPrices.setVisibility(View.VISIBLE);
            holder.linearLayoutSinglePrice.setVisibility(View.GONE);
            holder.textViewDiscountPrice.setText("$" + Utils.getFormatAmount(DiscountPrice));
            holder.textViewActualPrice.setVisibility(View.GONE);

        } else if (Double.parseDouble(ActualPrice) > Double.parseDouble(DiscountPrice)) {
            holder.linearLayoutTwoPrices.setVisibility(View.VISIBLE);
            holder.linearLayoutSinglePrice.setVisibility(View.VISIBLE);
            holder.textViewDiscountPrice.setText("$" + Utils.getFormatAmount(DiscountPrice));
            holder.textViewActualPrice.setText("$" + Utils.getFormatAmount(ActualPrice));
            holder.textViewDiscountPrice.setGravity(Gravity.END);
            holder.textViewActualPrice.setGravity(Gravity.START);
            holder.textViewActualPrice.setTextColor(context.getResources().getColor(R.color.colorRed));
            holder.textViewActualPrice.setPaintFlags(holder.textViewActualPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }

        if (arrayListShowCase.get(position).getJsonMember114112().equals("23")){ //specials
            holder.linearLayoutTwoPrices.setVisibility(View.GONE);
            holder.linearLayoutSinglePrice.setVisibility(View.GONE);
        }

        holder.textViewItemName.setText(Utils.hexToASCII(arrayListShowCase.get(position).getJsonMember12083()));


        Picasso.get().load(ATPreferences.readString(context, Constants.KEY_IMAGE_URL)
                + "_t_" + arrayListShowCase.get(position).getJsonMember121170())
                .placeholder(R.drawable.ic_gallery_placeholder)
                .error(R.drawable.no_photo_placeholder)
                .fit().centerInside().into(holder.imageViewItem);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterClick.onShowCaseClick(position);
            }
        });

    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return arrayListShowCase.size();
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

    public void setOnItemClickListner(ShowCaseItemClick adapterClick) {
        this.adapterClick = adapterClick;
    }

    public interface ShowCaseItemClick {
        public void onShowCaseClick(int position);
    }
}

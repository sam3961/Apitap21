package com.apitap.views.fragments.specials.storefront;

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
import com.apitap.model.bean.itemStoreFront.PCItem;
import com.apitap.model.preferences.ATPreferences;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Shami on 1/9/16.
 */

public class AdapterChildSpecials extends RecyclerView.Adapter<AdapterChildSpecials.ViewHolder> {
    private Context context;
    private SpecialListClick adapterClick;
    private List<com.apitap.model.storeFrontSpecials.PCItem> arrayListItems;


    public AdapterChildSpecials(Context context, List<com.apitap.model.storeFrontSpecials.PCItem> pc,
                                SpecialListClick adapterClick) {
        this.arrayListItems = pc;
        this.context = context;
        this.adapterClick = adapterClick;
    }

    public void customNotify(List<com.apitap.model.storeFrontSpecials.PCItem> arrayListItems){
        this.arrayListItems = arrayListItems;
        notifyDataSetChanged();

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.linearLayoutSinglePrice.setVisibility(View.VISIBLE);
        holder.linearLayoutTwoPrices.setVisibility(View.GONE);
        holder.textViewActualPrice.setText(arrayListItems.get(position).getJsonMember122162());


        holder.textViewItemName.setText(Utils.hexToASCII(arrayListItems.get(position).getJsonMember12083()));

        if (arrayListItems.get(position).getJsonMember1149().equals("true"))
            holder.imageViewSeen.setImageDrawable(context.getResources().getDrawable(R.drawable.grey_seen));
        else
            holder.imageViewSeen.setImageDrawable(context.getResources().getDrawable(R.drawable.green_seen));

        Picasso.get().load(ATPreferences.readString(context, Constants.KEY_IMAGE_URL)
                + "_t_" + arrayListItems.get(position).getJsonMember121170())
                .placeholder(R.drawable.splash_screen_new)
                .fit().centerInside().into(holder.imageViewItem);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterClick.onSpecialsClick(
                        arrayListItems.get(position).getJsonMember114144(),
//                        Utils.lengtT(11,arrayListItems.get(position).getJsonMember114144()),
                        arrayListItems.get(position).getJsonMember114112(),
                        arrayListItems.get(position).getJsonMember114179());
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

        private TextView textViewItemName;
        private TextView textViewActualPrice;
        private TextView textViewDiscountPrice;
        private ImageView imageViewItem;
        private ImageView imageViewSeen;
        private LinearLayout linearLayoutSinglePrice;
        private LinearLayout linearLayoutTwoPrices;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewItemName = itemView.findViewById(R.id.textViewItemName);
            imageViewItem = itemView.findViewById(R.id.imageViewItem);
            imageViewSeen = itemView.findViewById(R.id.eye);
            textViewActualPrice = itemView.findViewById(R.id.actual_price);
            textViewDiscountPrice = itemView.findViewById(R.id.price_after_discount);
            linearLayoutSinglePrice = itemView.findViewById(R.id.rel_single_price);
            linearLayoutTwoPrices = itemView.findViewById(R.id.rl_two_price);
        }
    }

    public void setOnItemClickListner(SpecialListClick adapterClick) {
        this.adapterClick = adapterClick;
    }

    public interface SpecialListClick {
        public void onSpecialsClick(String productId,String productType,String merchantId);
    }
}

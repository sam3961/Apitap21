package com.apitap.views.fragments.items.storeFront;

import android.content.Context;
import android.graphics.Paint;
import android.os.Build;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
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

public class AdapterChildItems extends RecyclerView.Adapter<AdapterChildItems.ViewHolder> {
    private Context context;
    private ItemListClick adapterClick;
    private List<PCItem> arrayListItems;

    public AdapterChildItems(Context context, List<PCItem> arrayListStores, ItemListClick adapterClick) {
        this.arrayListItems = arrayListStores;
        this.context = context;
        this.adapterClick = adapterClick;
    }

    public void customNotify(List<PCItem> arrayListItems) {
        this.arrayListItems = arrayListItems;
        notifyDataSetChanged();

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_child_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        String ActualPrice = String.format("%.2f", Double.parseDouble(arrayListItems.get(position).getJsonMember11498()));
        String DiscountPrice = String.format("%.2f", Double.parseDouble(arrayListItems.get(position).getJsonMember122158()));

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

        holder.textViewItemName.setText(Utils.hexToASCII(arrayListItems.get(position).getJsonMember12083()));

        String resultString = Utils.hexToASCII(arrayListItems.get(position).getJsonMember120157());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.textViewItemDesc.setText(Html.fromHtml(resultString, Html.FROM_HTML_MODE_LEGACY));
        } else {
            holder.textViewItemDesc.setText(Html.fromHtml(resultString));
        }

        if (arrayListItems.get(position).getJsonMember1149().equals("true"))
            holder.imageViewSeen.setImageDrawable(context.getResources().getDrawable(R.drawable.grey_seen));
        else
            holder.imageViewSeen.setImageDrawable(context.getResources().getDrawable(R.drawable.green_seen));

        if (arrayListItems.get(position).getJsonMember121170() != null && !arrayListItems.get(position).getJsonMember121170().isEmpty())
            holder.frameLayoutImage.setVisibility(View.VISIBLE);
        else
            holder.frameLayoutImage.setVisibility(View.GONE);

        if (arrayListItems.get(position).getJsonMember12139()!=null&&!Utils.checkAvailability(arrayListItems.get(position).getJsonMember12139()).isEmpty()) {
            holder.tvAvailability.setVisibility(View.VISIBLE);
            holder.tvAvailability.setText(Utils.checkAvailability(arrayListItems.get(position).getJsonMember12139()));
        }else
            holder.tvAvailability.setVisibility(View.GONE);

//        Log.d("ItemImageUrl", ATPreferences.readString(context, Constants.KEY_IMAGE_URL)
//                + "_t_" + arrayListItems.get(position).getJsonMember121170());
        Picasso.get().load(ATPreferences.readString(context, Constants.KEY_IMAGE_URL)
                + "_t_" + arrayListItems.get(position).getJsonMember121170())
                .placeholder(R.drawable.ic_gallery_placeholder)
                .error(R.drawable.no_photo_placeholder)
                .fit().centerInside().into(holder.imageViewItem);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterClick.onItemsClick(Utils.lengtT(11, arrayListItems.get(position).getJsonMember114144())
                        , arrayListItems.get(position).getJsonMember114112());
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
        private TextView textViewItemDesc;
        private TextView textViewActualPrice;
        private TextView tvAvailability;
        private TextView textViewDiscountPrice;
        private ImageView imageViewItem;
        private ImageView imageViewSeen;
        private FrameLayout frameLayoutImage;
        private LinearLayout linearLayoutSinglePrice;
        private LinearLayout linearLayoutTwoPrices;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewItemName = itemView.findViewById(R.id.textViewItemName);
            textViewItemDesc = itemView.findViewById(R.id.textViewItemDesc);
            imageViewItem = itemView.findViewById(R.id.imageViewItem);
            imageViewSeen = itemView.findViewById(R.id.eye);
            textViewActualPrice = itemView.findViewById(R.id.actual_price);
            tvAvailability = itemView.findViewById(R.id.tvAvailability);
            textViewDiscountPrice = itemView.findViewById(R.id.price_after_discount);
            linearLayoutSinglePrice = itemView.findViewById(R.id.rel_single_price);
            linearLayoutTwoPrices = itemView.findViewById(R.id.rl_two_price);
            frameLayoutImage = itemView.findViewById(R.id.frame_layout);
        }
    }

    public void setOnItemClickListner(ItemListClick adapterClick) {
        this.adapterClick = adapterClick;
    }

    public interface ItemListClick {
        public void onItemsClick(String productId, String productType);
    }
}

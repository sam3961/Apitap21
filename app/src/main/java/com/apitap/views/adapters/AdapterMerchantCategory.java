package com.apitap.views.adapters;

import java.util.List;
 
import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apitap.R;
import com.apitap.model.merchantCategoryList.RESULT;

public class AdapterMerchantCategory extends RecyclerView.Adapter<AdapterMerchantCategory.ViewHolder> {

    private List<RESULT> businessList;
    private MerchantCategoryClick adapterClick;
    private Context mContext;
    private int row_index=-1;

    public AdapterMerchantCategory(Context context,List<RESULT> response, MerchantCategoryClick businessClick) {
        this.mContext = context;
        this.businessList = response;
        this.adapterClick = businessClick;
    }

    public void customNotify(int position) {
        row_index=position;
        notifyDataSetChanged();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_child_category, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.textViewTitle.setText(businessList.get(position).get_12045());

        if (row_index == position) {
            holder.textViewTitle.setTextColor(mContext.getResources().getColor(R.color.colorWhite));
            holder.linearLayoutParent.setBackground(mContext.getResources().getDrawable(R.drawable.rounded_filled_green));
        } else {
            holder.textViewTitle.setTextColor(mContext.getResources().getColor(R.color.colorBlack));
            holder.linearLayoutParent.setBackground(mContext.getResources().getDrawable(R.drawable.back_round_black_border));
        }

        holder.linearLayoutParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterClick.onMerchantCategoryClick(position);
                notifyDataSetChanged();
            }
        });



    }

    @Override
    public int getItemCount() {
        return businessList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView textViewTitle;
        private final LinearLayout linearLayoutParent;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            linearLayoutParent = itemView.findViewById(R.id.linearLayoutParent);
        }
    }

    public void setOnItemClick(MerchantCategoryClick adapterClick) {
        this.adapterClick = adapterClick;
    }

    public interface MerchantCategoryClick {
        public void onMerchantCategoryClick(int position);
    }

}

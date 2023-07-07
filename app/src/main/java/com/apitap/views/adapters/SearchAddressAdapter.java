package com.apitap.views.adapters;

import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.apitap.R;

import java.util.ArrayList;

/**
 * Created by rishav on 10/11/16.
 */

public class SearchAddressAdapter extends RecyclerView.Adapter<SearchAddressAdapter.ViewHolder> {

    private ArrayList<String> list;
    private HistoryAdapter.AdapterClick adapterClick;

    public SearchAddressAdapter(ArrayList<String> list) {
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_searchllisting, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        Log.e("ITEMS:", ""+list.get(position));
        holder.items.setText(list.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterClick.onClick(v, holder.getAdapterPosition());
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView items;

        public ViewHolder(View itemView) {
            super(itemView);

            items = (TextView)itemView.findViewById(R.id.searchAddresses);

        }
    }

    public void setOnItemClickListner(HistoryAdapter.AdapterClick adapterClick) {
        this.adapterClick = adapterClick;
    }

    public interface AdapterClick {
        public void onClick(View v, int position);
    }
}

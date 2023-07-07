package com.apitap.views.adapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.apitap.R;
import com.apitap.model.bean.SearchAddressBeans;

import java.util.ArrayList;

/*
 * Created by rishav on 19/1/17.
 */

public class SearchNearByAdapter extends RecyclerView.Adapter<SearchNearByAdapter.ViewHolder> {

    private Context context;
    private ArrayList<SearchAddressBeans> list;

    public SearchNearByAdapter(Context context, ArrayList<SearchAddressBeans> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public SearchNearByAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_places_search, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchNearByAdapter.ViewHolder holder, int position) {
        SearchAddressBeans searchAddressBeans = list.get(position);


        holder.textAddress.setText(searchAddressBeans.getAddress());
        holder.textArea.setText(searchAddressBeans.getArea());

        /*list.set(position, searchAddressBeans);
        notifyItemInserted(position);
        notifyItemRangeChanged(position, list.size());*/
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

     class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textAddress, textArea;

         ViewHolder(View itemView) {
            super(itemView);

            textAddress = (TextView)itemView.findViewById(R.id.textAddress);
            textArea = (TextView)itemView.findViewById(R.id.textArea);
        }
    }
}

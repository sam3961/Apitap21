package com.apitap.views.adapters;

import android.content.Context;
import android.graphics.Color;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.apitap.R;
import com.apitap.model.Utils;
import com.apitap.model.bean.HistoryInvoiceBean;

import java.util.ArrayList;
import java.util.List;

import android.widget.Filter;
import android.widget.Filterable;

/**
 * Created by appzorro on 1/9/16.
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> implements Filterable {

    private AdapterClick adapterClick;
    List<HistoryInvoiceBean.RESULT.Invoicedata> itemList;
    List<HistoryInvoiceBean.RESULT.Invoicedata> mFilteredList;
    Context context;


    public HistoryAdapter(List<HistoryInvoiceBean.RESULT.Invoicedata> historyInvoiceBean) {
        itemList = historyInvoiceBean;
        mFilteredList = historyInvoiceBean;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context=parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_row, parent, false);
        return new ViewHolder(view);
    }

    public void updateList(List<HistoryInvoiceBean.RESULT.Invoicedata> list) {
        itemList = list;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.txt_date.setText(Utils.changeInvoiceDateFormat(itemList.get(position).getInvoiceDate()));

        holder.txt_store.setText(Utils.hexToASCII(itemList.get(position).getCompanyName()));
        holder.txt_invoice.setText(itemList.get(position).getInvoiceNumber());
        holder.txt_amount.setText("$ " + itemList.get(position).getAmount());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterClick.onClick(v, position);
            }
        });

        if (itemList.get(position).getStatus().equals("102")) {
            holder.txt_date.setTextColor(Color.parseColor("#cc0000"));
            holder.txt_store.setTextColor(Color.parseColor("#cc0000"));
            holder.txt_invoice.setTextColor(Color.parseColor("#cc0000"));
            holder.txt_amount.setTextColor(Color.parseColor("#cc0000"));

            holder.txt_status.setText("Return");
        } else  if (itemList.get(position).getStatus().equals("105")) {
            holder.txt_date.setTextColor(context.getResources().getColor(R.color.colorGreen));
            holder.txt_store.setTextColor(context.getResources().getColor(R.color.colorGreen));
            holder.txt_invoice.setTextColor(context.getResources().getColor(R.color.colorGreen));
            holder.txt_amount.setTextColor(context.getResources().getColor(R.color.colorGreen));

            holder.txt_status.setText("Refund");
        } else {
            holder.txt_date.setTextColor(Color.parseColor("#707070"));
            holder.txt_store.setTextColor(Color.parseColor("#707070"));
            holder.txt_invoice.setTextColor(Color.parseColor("#707070"));
            holder.txt_amount.setTextColor(Color.parseColor("#707070"));
            holder.txt_status.setText("Success");
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {

                    mFilteredList = itemList;
                } else {


                    ArrayList<HistoryInvoiceBean.RESULT.Invoicedata> filteredList = new ArrayList<>();

                    for (HistoryInvoiceBean.RESULT.Invoicedata androidVersion : itemList) {

                        if (androidVersion.getCompanyName().toLowerCase().contains(charString) || androidVersion.getStatus().toLowerCase().contains(charString) || androidVersion.getInvoiceNumber().toLowerCase().contains(charString) || androidVersion.getInvoiceDate().toLowerCase().contains(charString)) {

                            filteredList.add(androidVersion);
                        }
                    }

                    mFilteredList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilteredList = (ArrayList<HistoryInvoiceBean.RESULT.Invoicedata>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView txt_date, txt_store, txt_invoice, txt_amount, txt_status;

        public ViewHolder(View itemView) {
            super(itemView);
            txt_date = (TextView) itemView.findViewById(R.id.txt_date);
            txt_store = (TextView) itemView.findViewById(R.id.txt_store);
            txt_invoice = (TextView) itemView.findViewById(R.id.txt_invoice);
            txt_amount = (TextView) itemView.findViewById(R.id.txt_amount);
            txt_status = (TextView) itemView.findViewById(R.id.txtStatus);

        }
    }

    public void setOnItemClickListner(AdapterClick adapterClick) {
        this.adapterClick = adapterClick;
    }

    public interface AdapterClick {
        public void onClick(View v, int position);
    }


}

package com.apitap.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.apitap.R;
import com.apitap.model.bean.ShoppingCartDetailBean;
import com.apitap.model.bean.SizeBean;
import com.apitap.model.bean.Sizedata;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ashok-kumar on 16/6/16.
 */

public class SpinnerAdapter extends ArrayAdapter<String> {
    List<ShoppingCartDetailBean.RESULT.DetailData.DE> array;
    Context context;
    int quantity = 1;
    AdapterClick adapterClick;

    public SpinnerAdapter(Context context, int resource, List<ShoppingCartDetailBean.RESULT.DetailData.DE> array) {
        super(context, resource);
        this.array = array;
        this.context = context;
    }

    @Override
    public int getCount() {
        return array.size();
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_spinner_divider, null);
        TextView label = (TextView) rowView.findViewById(R.id.text);
      //  label.setText(array.get(position).getExpectedDays());
        return rowView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_spinner_divider, null);
        TextView label = (TextView) rowView.findViewById(R.id.text);
       // label.setText(array.get(position).getExpectedDays());
        return rowView;
    }

    public void setOnTextClickListener(AdapterClick adapterClick) {
        this.adapterClick = adapterClick;
    }

    public interface AdapterClick {
        public void onTextClick(View v, String quantity);
    }
}

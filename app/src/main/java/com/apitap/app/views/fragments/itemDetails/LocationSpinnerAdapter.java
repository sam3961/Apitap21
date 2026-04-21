package com.apitap.app.views.fragments.itemDetails;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.apitap.app.R;
import com.apitap.app.model.bean.items.LocationBean;

import java.util.ArrayList;

public class LocationSpinnerAdapter extends ArrayAdapter<LocationBean> {

    private Context context;
    private ArrayList<LocationBean> list;
    private String choiceKey; // 🔥 "3153_3155"

    public LocationSpinnerAdapter(Context context,
                                  ArrayList<LocationBean> list,
                                  String choiceOne,
                                  String choiceTwo) {
        super(context, 0, list);
        this.context = context;
        this.list = list;
        this.choiceKey = buildKey(choiceOne, choiceTwo);
    }

    private String buildKey(String c1, String c2) {
        if (c1 == null) return "";
        if (c2 == null || c2.isEmpty()) return c1;
        return c1 + "_" + c2;
    }

    public void updateChoices(String c1, String c2) {
        this.choiceKey = buildKey(c1, c2);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = View.inflate(context,
                    R.layout.spinner_location_selected, null);
        }

        TextView tv = convertView.findViewById(R.id.tvSpinnerText);
        LocationBean loc = list.get(position);

        int qty = loc.getQuantityForChoices(choiceKey);

        String text = loc.getLocationName()
                + "\n" + loc.getAddress()
                + "\nQty: " + qty;

        tv.setTextColor(qty > 0 ? Color.BLACK : Color.GRAY);
        tv.setText(qty > 0 ? text : text + " (Out of stock)");

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = View.inflate(context,
                    R.layout.spinner_location_dropdown, null);
        }

        TextView tv = convertView.findViewById(R.id.tvSpinnerText);
        LocationBean loc = list.get(position);

        int qty = loc.getQuantityForChoices(choiceKey);

        String text = loc.getLocationName()
                + "\n" + loc.getAddress()
                + "\nQty: " + qty;

        tv.setTextColor(qty > 0 ? Color.BLACK : Color.GRAY);
        tv.setText(qty > 0 ? text : text + " (Out of stock)");

        return convertView;
    }

    @Override
    public boolean isEnabled(int position) {
        return list.get(position)
                .isInStockForChoices(choiceKey);
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }
}

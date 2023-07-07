package com.apitap.views.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.apitap.R;
import com.apitap.model.bean.ShoppingCartDetailBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ashok-kumar on 16/6/16.
 */

public class SpinnerAdapter2 extends ArrayAdapter<String> {
    List<String> nickNameList = new ArrayList<>();
    List<String> addressList = new ArrayList<>();
    Context context;
    int quantity = 1;
    AdapterClick adapterClick;

    public SpinnerAdapter2(Activity context, int resource, ArrayList<String> nickname_list, ArrayList<String> address_list) {
        super(context, resource);
        this.context = context;
        this.nickNameList = nickname_list;
        this.addressList = address_list;
    }

    @Override
    public int getCount() {
        return addressList.size();
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.spinner_textview_two, null);
        TextView nickname = (TextView) rowView.findViewById(R.id.text2);
        TextView address = (TextView) rowView.findViewById(R.id.text);
        nickname.setText(nickNameList.get(position));
        address.setText(addressList.get(position));
        return rowView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.spinner_textview_two, null);
        TextView nickname = (TextView) rowView.findViewById(R.id.text2);
        TextView address = (TextView) rowView.findViewById(R.id.text);
        nickname.setText(nickNameList.get(position));
        address.setText(addressList.get(position));
        return rowView;
    }

    public void setOnTextClickListener(AdapterClick adapterClick) {
        this.adapterClick = adapterClick;
    }

    public interface AdapterClick {
        public void onTextClick(View v, String quantity);
    }
}

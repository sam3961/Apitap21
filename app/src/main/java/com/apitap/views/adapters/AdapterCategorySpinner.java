package com.apitap.views.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.apitap.R;

import java.util.List;

public class AdapterCategorySpinner extends BaseAdapter {

    private LayoutInflater layoutInflater;
    private List<String> list;
    private Context context;

    public AdapterCategorySpinner(Context applicationContext, List<String> list ) {
        this.context = applicationContext;
        this.list = list;
        layoutInflater = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rowview = layoutInflater.inflate(R.layout.adapter_category_spinner, null);

        TextView textViewTitle = rowview.findViewById(R.id.textView);

        textViewTitle.setText(list.get(position));

        return rowview;
    }
}
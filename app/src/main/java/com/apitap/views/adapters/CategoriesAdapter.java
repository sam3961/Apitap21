package com.apitap.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

import com.apitap.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ashok-kumar on 16/6/16.
 */

public class CategoriesAdapter extends ArrayAdapter<String> {
    HashMap<Integer,ArrayList<String>> map;
    Context context;

    public CategoriesAdapter(Context context, int resource, HashMap<Integer,ArrayList<String>> map) {
        super(context, resource);
        this.map = map;
        this.context = context;
    }

    @Override
    public int getCount() {
        return map.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_categories, null);
        HorizontalScrollView scrollView = (HorizontalScrollView) rowView.findViewById(R.id.scrollView);
        ImagesAdapter imagesAdapter=new ImagesAdapter(context,0,map.get(position));
        //scrollView.
        return rowView;
    }
}

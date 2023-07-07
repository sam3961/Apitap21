package com.apitap.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;

import com.apitap.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ashok-kumar on 16/6/16.
 */

public class ImagesAdapter extends ArrayAdapter<String> {
    ArrayList<String> array;
    Context context;

    public ImagesAdapter(Context context, int resource, ArrayList<String> array) {
        super(context, resource);
        this.array = array;
        this.context = context;
    }

    @Override
    public int getCount() {
        return array.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_categories, null);
        ImageView image = (ImageView) rowView.findViewById(R.id.image);
        Picasso.get().load(array.get(position)).into(image);
        return rowView;
    }
}

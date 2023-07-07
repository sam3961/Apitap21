package com.apitap.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.apitap.R;
import com.apitap.model.Constants;
import com.apitap.model.bean.ProductDetailsBean;
import com.apitap.model.bean.SizeBean;
import com.apitap.model.bean.Sizedata;
import com.apitap.model.customclasses.SquareImageView;
import com.apitap.model.preferences.ATPreferences;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by ashok-kumar on 16/6/16.
 */

public class SubImagesAdapter extends ArrayAdapter<String> {
    ArrayList<String> array;
    Context context;
    String  frontImage;

    public SubImagesAdapter(Context context, int resource, ArrayList<String> array,String frontImage) {
        super(context, resource);
        this.array = array;
        this.frontImage = frontImage;
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
        View rowView = inflater.inflate(R.layout.row_subimage, null);
        ImageView image =  rowView.findViewById(R.id.image);
        ImageView playImage =  rowView.findViewById(R.id.playImage);
        if (array.get(position).endsWith("mp4")||
                array.get(position).endsWith("mpeg")
                ||array.get(position).endsWith("avi")||array.get(position).endsWith("mkv")){

            playImage.setVisibility(View.VISIBLE);
            Picasso.get().load(frontImage).into(image);

        }else {
            playImage.setVisibility(View.GONE);
            Picasso.get().load(array.get(position)).into(image);
        }
        return rowView;
    }
}

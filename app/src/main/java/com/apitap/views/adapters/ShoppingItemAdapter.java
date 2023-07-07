package com.apitap.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.apitap.R;
import com.apitap.model.Constants;
import com.apitap.model.bean.ShoppingCompBean;
import com.apitap.model.preferences.ATPreferences;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by ashok-kumar on 16/6/16.
 */

public class ShoppingItemAdapter extends ArrayAdapter<String> {
    ArrayList<ShoppingCompBean> array;
    Context context;
    String url;

    public ShoppingItemAdapter(Context context, int resource, ArrayList<ShoppingCompBean> array) {
        super(context, resource);
        this.array = array;
        this.context = context;
        this.url = ATPreferences.readString(context, Constants.KEY_IMAGE_URL);
    }

    @Override
    public int getCount() {
        return array.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_shoppingcart_item, null);
        TextView companyName = (TextView) rowView.findViewById(R.id.name);
        TextView delete = (TextView) rowView.findViewById(R.id.by_company);
        ImageView image = (ImageView) rowView.findViewById(R.id.image);
        ShoppingCompBean bean = array.get(position);
        companyName.setText(bean.getCompanyName());
        Picasso.get().load(url + bean.getCompanyImage()).into(image);

        return rowView;
    }
}

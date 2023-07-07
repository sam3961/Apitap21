package com.apitap.views.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.apitap.R;
import com.apitap.model.Utils;
import com.apitap.model.bean.ShoppingAsstListBean;
import com.apitap.model.customclasses.SelectedItem;
import com.apitap.model.preferences.ATPreferences;
import com.apitap.views.fragments.FragmentShoppingAsst;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ashok-kumar on 16/6/16.
 */

public class SavedCatAdapter extends BaseAdapter {
    List<ShoppingAsstListBean.RESULT_> array = new ArrayList<>();
    Context context;
    boolean b;
    int defaultPosition;
    int defaultCatPosition;
    boolean isMainList = true;
    SelectedItem selectedItem;
    List<ShoppingAsstListBean.RESULT_> shoppingAsstListBean = new ArrayList<>();


    public SavedCatAdapter(Context activity, List<ShoppingAsstListBean.RESULT_> shoppingAsstListBeen, int defaultPosition, int defaultCatPosition, boolean b) {
        this.context = activity;
        this.shoppingAsstListBean = shoppingAsstListBeen;
        this.defaultCatPosition = defaultCatPosition;
        this.defaultPosition = defaultPosition;
        this.isMainList = b;
    }


    public void customNotify(List<ShoppingAsstListBean.RESULT_> array, int defaultPosition, int defaultCatPosition, boolean b) {
        new SelectedItem(array);
        this.shoppingAsstListBean = array;
        this.defaultCatPosition = defaultCatPosition;
        this.defaultPosition = defaultPosition;
        this.b = b;
        this.isMainList = b;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return shoppingAsstListBean.get(defaultPosition).getIL().size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_saved, null);
        TextView name = (TextView) rowView.findViewById(R.id.name);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.iv_next);
        ImageView imageView1 = (ImageView) rowView.findViewById(R.id.iv_radio_btn);

        imageView.setVisibility(View.GONE);
        name.setText(Utils.hexToASCII(shoppingAsstListBean.get(defaultPosition).getIL().get(position).get12083()));
        if (defaultCatPosition != 404 && position == defaultCatPosition) {
            imageView1.setImageResource(R.drawable.ic_icon_circle_fill);
        }


        return rowView;
    }
}

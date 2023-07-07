package com.apitap.views.adapters;

import androidx.fragment.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apitap.R;
import com.apitap.model.Constants;
import com.apitap.model.preferences.ATPreferences;

import java.util.ArrayList;

/**
 * Created by Thakur on 3/5/2018.
 */

public class SortAdapter extends BaseAdapter {
    FragmentActivity fragmentItems;
    ArrayList<String> arraylist;
    LayoutInflater layoutInflater;
    int i;

    public SortAdapter(FragmentActivity fragmentItems, ArrayList<String> arraylist, int i) {
        this.fragmentItems = fragmentItems;
        this.arraylist = arraylist;
        this.i = i;
        layoutInflater = LayoutInflater.from(fragmentItems);
    }

    @Override
    public int getCount() {
        return arraylist.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = layoutInflater.inflate(R.layout.sortby_row, null);
        TextView name = (TextView) convertView.findViewById(R.id.name);
        LinearLayout item_view = (LinearLayout) convertView.findViewById(R.id.root_view);
        name.setText(arraylist.get(position).toString());
        final CheckBox itemcheck = (CheckBox) convertView.findViewById(R.id.checkimg);

        if (i == 0) {
            if (!ATPreferences.getString(fragmentItems, Constants.SAVED_SORT, "").isEmpty() && ATPreferences.getString(fragmentItems, Constants.SAVED_SORT, "").equals(position + "")) {
                itemcheck.setChecked(true);
            }
        } else if (i == 1) {
            if (!ATPreferences.getString(fragmentItems, Constants.SPECIAL_SORT, "").isEmpty() && ATPreferences.getString(fragmentItems, Constants.SPECIAL_SORT, "").equals(position + "")) {
                itemcheck.setChecked(true);
            }
        }


        return convertView;
    }

}

package com.apitap.views.fragments.items.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.apitap.R;
import com.apitap.model.merchantCategoryList.RESULT;
import com.apitap.views.fragments.items.FragmentItems;

import java.util.List;

public class AdapterCategoryListSpinner extends BaseAdapter {

    private LayoutInflater layoutInflater;
    private List<RESULT> list;
    private Context context;


    public AdapterCategoryListSpinner(Context activity, List<RESULT> result) {
        this.context = activity;
        this.list = result;
        layoutInflater = (LayoutInflater.from(context));
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

        View rowview = layoutInflater.inflate(R.layout.adapter_category_list_spinner, null);

        TextView textViewTitle = rowview.findViewById(R.id.textView);

        textViewTitle.setText(list.get(position).get_12045());

        return rowview;
    }
}
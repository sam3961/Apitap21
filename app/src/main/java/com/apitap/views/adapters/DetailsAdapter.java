package com.apitap.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.apitap.R;
import com.apitap.model.bean.SizeBean;
import com.apitap.model.bean.Sizedata;

import java.util.ArrayList;

/**
 * Created by ashok-kumar on 16/6/16.
 */

public class DetailsAdapter extends ArrayAdapter<String> {
    ArrayList<Sizedata> array;
    Context context;
    int quantity = 1;
    AdapterClick adapterClick;

    public DetailsAdapter(Context context, int resource, ArrayList<Sizedata> array) {
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
        View rowView = inflater.inflate(R.layout.row_details, null);
        TextView label = (TextView) rowView.findViewById(R.id.size_label);
        Spinner spinner = (Spinner) rowView.findViewById(R.id.spinner);

        TextView minus = (TextView)rowView.findViewById(R.id.tv_minus);
        TextView plus = (TextView)rowView.findViewById(R.id.tv_plus);
        final TextView tv_quantity = (TextView)rowView.findViewById(R.id.tv_quantity);

        quantity = Integer.parseInt(tv_quantity.getText().toString());

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantity > 1)
                    quantity--;

                tv_quantity.setText(String.valueOf(quantity));
                adapterClick.onTextClick(v, String.valueOf(quantity));

            }
        });

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                quantity++;
                tv_quantity.setText(String.valueOf(quantity));
                adapterClick.onTextClick(v, String.valueOf(quantity));
            }
        });

        label.setText(array.get(position).getName());

        ArrayList<SizeBean> arrData = array.get(position).getSizeArray();

        ArrayList<String> nameArray = new ArrayList<>();
        int size = arrData.size();
        for (int i = 0; i < size; i++) {
            nameArray.add(arrData.get(i).getSize());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, nameArray);
        spinner.setAdapter(adapter); // this will set list of values to spinner
        return rowView;
    }

    public void setOnTextClickListener(AdapterClick adapterClick) {
        this.adapterClick = adapterClick;
    }

    public interface AdapterClick {
        public void onTextClick(View v, String quantity);
    }
}

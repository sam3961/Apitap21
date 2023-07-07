package com.apitap.views.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.apitap.R;
import com.apitap.model.Constants;
import com.apitap.model.Utils;
import com.apitap.model.bean.SearchFavoritesBean;
import com.apitap.views.HomeActivity;
import com.apitap.views.fragments.itemDetails.FragmentItemDetails;

import java.util.List;

/**
 * Created by ashok-kumar on 16/6/16.
 */

public class SearchFavoritesAdapter extends ArrayAdapter<String> {
    List<SearchFavoritesBean.RESULT.RESULTDATA>  array;
    Context context;

    public SearchFavoritesAdapter(Context context, int resource, List<SearchFavoritesBean.RESULT.RESULTDATA> array) {
        super(context, resource);
        this.array = array;
        this.context = context;
    }

    @Override
    public int getCount() {
        return array.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_search_item, null);
        ImageView image = (ImageView) rowView.findViewById(R.id.ic_icon);
        TextView textName=(TextView)rowView.findViewById(R.id.txt_name);
        TextView textCompany=(TextView)rowView.findViewById(R.id.txt_store);
        TextView textCost=(TextView)rowView.findViewById(R.id.cost);

        textName.setText(Utils.hexToASCII(array.get(position).getName()));
        textCompany.setText("By "+array.get(position).getStoreName());
        textCost.setText("$ "+array.get(position).getActualPrice());
        //Picasso.get().load(ATPreferences.readString(context, Constants.KEY_IMAGE_URL) +array.get(position).getImagesData().get(0).getImageUrl()).into(image);
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String productId = Utils.lengtT(11, array.get(position).getProductId());
                String productType = array.get(position).getProductType();
                Bundle bundle = new Bundle();
                bundle.putString("productId", productId);
                bundle.putString("productType", productType);
                bundle.putString("isFavorite", "0");
                FragmentItemDetails fragment = new FragmentItemDetails();
                fragment.setArguments(bundle);
                ((HomeActivity) context).displayView(fragment, Constants.TAG_DETAILSPAGE, bundle);
            }
        });

        return rowView;
    }
}
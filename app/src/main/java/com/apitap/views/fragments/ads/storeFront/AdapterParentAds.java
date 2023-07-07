package com.apitap.views.fragments.ads.storeFront;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apitap.R;
import com.apitap.model.bean.itemStoreFront.PCItem;
import com.apitap.model.bean.itemStoreFront.RESULTItem;
import com.apitap.views.fragments.items.storeFront.AdapterChildItems;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shami on 1/9/16.
 */

public class AdapterParentAds extends BaseExpandableListAdapter {

    private AdapterChildAds.AdsClick adapterClick;
    private Context context;
    private List<com.apitap.model.storeFrontAds.RESULTItem> listParent;
    private List<PCItem> listChild;


    public AdapterParentAds(Context activity, List<com.apitap.model.storeFrontAds.RESULTItem> listItems,
                            AdapterChildAds.AdsClick itemListClick) {

        this.listParent =listItems;
        this.context=activity;
        this.adapterClick=itemListClick;

    }


    @Override
    public int getGroupCount() {
        return listParent.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return 1;
    }

    @Override
    public Object getGroup(int i) {
        return listParent.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return listParent.get(i).getAD().get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(final int position, boolean isExpanded, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.vertical_row, viewGroup, false);
            ExpandableListView mExpandableListView = (ExpandableListView) viewGroup;
            mExpandableListView.expandGroup(position);
            convertView.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,
                    RecyclerView.LayoutParams.MATCH_PARENT));

        }
        LinearLayout view_ll = convertView.findViewById(R.id.view_ll);
        LinearLayout title_ll = convertView.findViewById(R.id.title_ll);
        CardView rootLayout = convertView.findViewById(R.id.cardView);
        ImageView img_Items = convertView.findViewById(R.id.img_Items);
        TextView title = convertView.findViewById(R.id.title);
        TextView category_name = convertView.findViewById(R.id.category_name);
        HorizontalScrollView horizontalScrollView = convertView.findViewById(R.id.horizontal_scrollview);
        horizontalScrollView.setVisibility(View.GONE);
        title.setText(listParent.get(position).getJsonMember12045());
        category_name.setText(listParent.get(position).getJsonMember12045());

        if (isExpanded) {
            img_Items.setImageResource(R.drawable.ic_icon_uparrow);
        } else {
            img_Items.setImageResource(R.drawable.ic_icon_downarrow);
        }
        //   if (strSubCat_SortSelected.equals("Showing All") && str_subCatFilter.equalsIgnoreCase("Showing All")) {
        title_ll.setVisibility(View.VISIBLE);
        rootLayout.setVisibility(View.VISIBLE);
        title.setVisibility(View.VISIBLE);
        view_ll.setVisibility(View.GONE);
        img_Items.setVisibility(View.VISIBLE);
        return convertView;

    }

    @Override
    public View getChildView(final int i, final int i1, boolean b, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.vertical_row_items, viewGroup, false);

        }
        RecyclerView mTwoWayView = convertView.findViewById(R.id.my_gallery);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        mTwoWayView.setLayoutManager(layoutManager);
        AdapterChildAds adapterChildItems =new AdapterChildAds(context,listParent.get(i).getAD(),adapterClick);
        mTwoWayView.setAdapter(adapterChildItems);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    public void customNotify(ArrayList<com.apitap.model.storeFrontAds.RESULTItem> resultItems) {
        this.listParent =resultItems;
        notifyDataSetChanged();
    }

}

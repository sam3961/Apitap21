package com.apitap.views.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apitap.R;
import com.apitap.controller.ModelManager;
import com.apitap.model.Constants;
import com.apitap.model.Operations;
import com.apitap.model.Utils;
import com.apitap.model.bean.ImagesBean;
import com.apitap.model.bean.NewItemBean;
import com.apitap.views.HomeActivity;
import com.apitap.views.fragments.itemDetails.FragmentItemDetails;
import com.linearlistview.LinearListView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Shami on 10/5/2018.
 */

public class MyAdapter extends BaseExpandableListAdapter {
    LayoutInflater inflater;
    HashMap<Integer, ArrayList<ImagesBean>> map;
    ArrayList<NewItemBean> newItemBeen;
    ArrayList<String> stringArrayList;
    ArrayList<NewItemBean.ChildBean> parentArraylist= new ArrayList<>();
    Activity activity;
    List<NewItemBean.ChildBean> beanArrayList = new ArrayList<>();
    ExpandableListView mList;
    HashMap<Integer, ArrayList<NewItemBean.ChildBean>> listHashMap;
    ArrayList<NewItemBean.ParentBean> parentBeen;
    ArrayList<ImagesBean> allImages;

    public MyAdapter(Activity activity, HashMap<Integer, ArrayList<ImagesBean>> map, HashMap<Integer, ArrayList<NewItemBean.ChildBean>> listHashMap, ArrayList<String> stringArrayList,
                     ArrayList<NewItemBean.ParentBean> parentBeen,ExpandableListView mList) {
        this.activity = activity;
        this.mList = mList;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.map = map;
        this.listHashMap = listHashMap;
        this.parentBeen = parentBeen;
        this.stringArrayList = stringArrayList;

    }



    @Override
    public int getGroupCount() {
        return stringArrayList.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return listHashMap.size();
    }

    @Override
    public Object getGroup(int i) {
        return stringArrayList.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return listHashMap.get(i).get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }


    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }


    class ViewHolder {
        TextView mCategoryName, mViewAll;
        LinearListView mTwoWayView;
        LinearLayout title_ll;
        //  MyTwoWayAdapter adapter;
    }

    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        if (convertView == null) {
            System.out.println("---getChildView --convertView == null");
            LayoutInflater inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.vertical_row, parent, false);
        }

        TextView mCategoryName = (TextView) convertView.findViewById(R.id.category_name);
        LinearListView mTwoWayView = (LinearListView) convertView.findViewById(R.id.my_gallery);
        LinearLayout title_ll = (LinearLayout) convertView.findViewById(R.id.title_ll);
        title_ll.setVisibility(View.GONE);
       // parentArraylist = parentBeen.get(groupPosition).getChildBeen();

        allImages = map.get(groupPosition);
        mCategoryName.setText(listHashMap.get(groupPosition).get(childPosition).getCategoryName());

        mTwoWayView.setAdapter(mAdapter);


        return convertView;
    }

    @Override
    public View getGroupView(final int groupPosition, final boolean isExpanded, View convertView,
                             ViewGroup parent) {
        System.out.println("---getGroupView --");
        String group = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.vertical_row, parent, false);
        }
        TextView title = (TextView) convertView.findViewById(R.id.title);
        LinearLayout view_ll = (LinearLayout) convertView.findViewById(R.id.view_ll);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.img_Items);
        final LinearListView linearListView = (LinearListView) convertView.findViewById(R.id.my_gallery);
        view_ll.setVisibility(View.GONE);

        title.setText(stringArrayList.get(groupPosition));

        if (isExpanded)
            imageView.setImageResource(R.drawable.ic_icon_downarrow);
        else
            imageView.setImageResource(R.drawable.ic_icon_uparrow);

        //TextView tv = (TextView) convertView.findViewById(R.id.tvGroup);
        // tv.setText(group);

        mList.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                Log.d("groupClick", "group1");
            }
        });

        mList.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                Log.d("groupClick", "group2");

            }
        });

        mList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Log.d("groupClick", "group3");
                return false;
            }
        });


        // expandableListView.expandGroup(groupPosition);  //used to Expand the child list automatically at the time of displaying
        return convertView;
    }


    // other adapter

    private BaseAdapter mAdapter = new BaseAdapter() {

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            if (convertView == null) {
                convertView = activity.getLayoutInflater().inflate(R.layout.row_horizontal_test, parent, false);
            }

            ImageView imageView = (ImageView) convertView.findViewById(R.id.image);
            TextView description = (TextView) convertView.findViewById(R.id.description);
            ImageView eye = (ImageView) convertView.findViewById(R.id.eye);
            String isSeen = allImages.get(position).getIsSeen();
            LinearLayout rlSinglePrice = (LinearLayout) convertView.findViewById(R.id.rel_single_price);
            LinearLayout rlTwoPrice = (LinearLayout) convertView.findViewById(R.id.rl_two_price);
            TextView price = (TextView) convertView.findViewById(R.id.price);
            TextView actualPrice = (TextView) convertView.findViewById(R.id.actual_price);
            TextView priceAfterDiscount = (TextView) convertView.findViewById(R.id.price_after_discount);
            if (isSeen.equalsIgnoreCase("false")) {
                eye.setBackgroundResource(R.drawable.green_seen);
            } else {
                eye.setVisibility(View.GONE);
            }
            description.setText(Utils.hexToASCII(allImages.get(position).getDescription()));

            String ActualPrice = allImages.get(position).getActualPrice();
            String DiscountPrice = allImages.get(position).getPriceAfterDiscount();

            if (ActualPrice.equals(DiscountPrice) || DiscountPrice.equals("0") || DiscountPrice.equals("0.00")) {
                rlSinglePrice.setVisibility(View.VISIBLE);
                rlTwoPrice.setVisibility(View.GONE);
                actualPrice.setText("$" + ActualPrice);
            } else if (ActualPrice.equals("0") || ActualPrice.equals("0.00")) {
                rlTwoPrice.setVisibility(View.VISIBLE);
                rlSinglePrice.setVisibility(View.GONE);
                priceAfterDiscount.setText("$" + DiscountPrice);
                actualPrice.setVisibility(View.GONE);
            } else if (Double.parseDouble(ActualPrice) > Double.parseDouble(DiscountPrice)) {
                rlTwoPrice.setVisibility(View.VISIBLE);
                rlSinglePrice.setVisibility(View.VISIBLE);
                priceAfterDiscount.setText("$" + (String.format("%.2f", Double.parseDouble(DiscountPrice))));
                actualPrice.setText("$" + ActualPrice);
                priceAfterDiscount.setGravity(Gravity.END);
                actualPrice.setGravity(Gravity.START);
                actualPrice.setTextColor(activity.getResources().getColor(R.color.colorRed));
                actualPrice.setPaintFlags(actualPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }


            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String productId = Utils.lengtT(11, allImages.get(position).getProductId());

                    ModelManager.getInstance().setProductSeen().setProductSeen(activity, Operations.makeProductSeen(activity, productId));


                    String productType = parentArraylist.get(position).getProductType();
                    Bundle bundle = new Bundle();
                    bundle.putString("productId", productId);
                    bundle.putString("productType", productType);
                    bundle.putString("isFavorite", map.get(position).get(position).getIsFavorite());
                    bundle.putString("actualprice", map.get(position).get(position).getActualPrice());
                    bundle.putString("discountprice", map.get(position).get(position).getPriceAfterDiscount());
                    FragmentItemDetails fragment = new FragmentItemDetails();
                    fragment.setArguments(bundle);
                    ((HomeActivity) activity).displayView(fragment, Constants.TAG_DETAILSPAGE, bundle);
                    tabContainer2Visible();
                }
            });

            Picasso.get().load(allImages.get(position).getImageUrls()).into(imageView);
            return convertView;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public int getCount() {
            return allImages.size();
        }
    };

    public void tabContainer2Visible() {
        //homeTab2.setBackgroundColor(activity.getResources().getColor(R.color.colorWhite));
        //imageViewHomeTab2.setImageResource(R.drawable.ic_icon_home);

        HomeActivity.tabContainer2.setVisibility(View.GONE);
        HomeActivity.tabContainer2.setVisibility(View.GONE);
    }

}


package com.apitap.views.adapters;

import android.app.Activity;
import android.content.Context;

import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apitap.R;
import com.apitap.model.Constants;
import com.apitap.model.Utils;
import com.apitap.model.bean.NewAdBean;
import com.apitap.model.customclasses.CustomImageView;
import com.apitap.views.HomeActivity;
import com.apitap.views.fragments.adDetails.FragmentAdDetail;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by appzorro on 1/9/16.
 */

public class AdsListingAdapter extends BaseExpandableListAdapter {

    ExpandableListView _list;
    private AdapterClick adapterClick;
    List<NewAdBean> newAdBeen;
    List<NewAdBean.ChildBean> childBeen;
    ArrayList<String> stringArrayList = new ArrayList<>();
    Activity activity;

    public AdsListingAdapter(Activity activity, List<NewAdBean> newAdBeen, ArrayList<String> stringArrayList, ExpandableListView _list) {
        this.activity = activity;
        this.newAdBeen = newAdBeen;
        this.stringArrayList = stringArrayList;
        this._list = _list;


    }
    public void customNotify(ArrayList<String> stringArrayList, ArrayList<NewAdBean> tempAdSendList){
        this.stringArrayList = stringArrayList;
        this.newAdBeen = tempAdSendList;
        notifyDataSetChanged();
    }

    public Object getChild(int groupPosition, int childPosition) {
        return newAdBeen.get(groupPosition).getChildBeen().get(childPosition);
        // return null;
    }

    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }


    public View getChildView(int groupPosition, final int position,
                             boolean isLastChild, View itemView, ViewGroup parent) {

        if (itemView == null) {
            System.out.println("---getChildView --convertView == null");
            LayoutInflater inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            itemView = inflater.inflate(R.layout.view_adslisting, parent, false);
        }
        CustomImageView img = (CustomImageView) itemView.findViewById(R.id.img);
        ImageView eye = (ImageView) itemView.findViewById(R.id.eye);
        final CardView mainView = (CardView) itemView.findViewById(R.id.card_view_set_priority);
        TextView storeName = (TextView) itemView.findViewById(R.id.store_name);
        TextView adName = (TextView) itemView.findViewById(R.id.adName);
        ImageView play = (ImageView) itemView.findViewById(R.id.play);

        childBeen = newAdBeen.get(groupPosition).getChildBeen();

        Log.d("ImageUrlss", childBeen.get(position).getName() + " ");

        Glide.with(activity).load(childBeen.get(position).getImageUrl()).placeholder
                (R.drawable.ic_icon_loading).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).into(img);

        adName.setText(Utils.hexToASCII(childBeen.get(position).getName()));
        storeName.setText(childBeen.get(position).getMerchantname());
        String isSeen = childBeen.get(position).getIsSeen();
        if (isSeen.equalsIgnoreCase("false")) {
            eye.setBackgroundResource(R.drawable.green_seen);
        } else {
            eye.setVisibility(View.GONE);
        }

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("clickedUrl", childBeen.get(position).getImageUrl());
                Bundle bundle = new Bundle();
                bundle.putString("videoUrl",childBeen.get(position).getVideoUrl());
                bundle.putString("image",childBeen.get(position).getImageUrl());
                bundle.putString("merchant",childBeen.get(position).getMerchantname());
                bundle.putString("adName", Utils.hexToASCII(childBeen.get(position).getName()));
                bundle.putString("desc", Utils.hexToASCII(childBeen.get(position).getDesc()));
                bundle.putString("id",childBeen.get(position).getId());
                bundle.putString("ad_id", childBeen.get(position).getAddId());
                bundle.putString("merchantid",childBeen.get(position).getMerchantId());
                bundle.putInt("adpos",position);
                ((HomeActivity) activity).displayView(new FragmentAdDetail(), Constants.TAG_AD_DETAIL, bundle);
            }
        });



        if (childBeen.get(position).getVideoUrl().endsWith("mp4") || childBeen.get(position).getVideoUrl().endsWith("avi")) {
            play.setVisibility(View.VISIBLE);
        } else {
            play.setVisibility(View.GONE);
        }


//        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
//            int lastExpandedGroupPosition = -1;
//            @Override
//            public void onGroupExpand(int groupPosition) {
//                Log.d("groupClick", "group1");
//                if(groupPosition != lastExpandedGroupPosition){
//                    expandableListView.collapseGroup(lastExpandedGroupPosition);
//                }
//
//                lastExpandedGroupPosition = groupPosition;
//
//            }
//        });

        _list.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                Log.d("groupClick", "group2");

            }
        });

        _list.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Log.d("groupClick", "group3");
                return false;
            }
        });

        //expandableListView.expandGroup(1);
        return itemView;
    }


    public int getChildrenCount(int groupPosition) {
        return newAdBeen.get(groupPosition).getChildBeen().size();
    }

    public Object getGroup(int groupPosition) {
        return stringArrayList.get(groupPosition);
    }

    public int getGroupCount() {
        return stringArrayList.size();
    }

    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    public boolean hasStableIds() {
        return false;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    // Return a group view. You can load your custom layout here.
    @Override
    public View getGroupView(final int groupPosition, final boolean isExpanded, View convertView,
                             final ViewGroup parent) {
        System.out.println("---getGroupView --");
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.vertical_row, parent, false);
        }
        LinearLayout linearLayout = (LinearLayout) convertView.findViewById(R.id.view_ll);
        TextView title_tv = (TextView) convertView.findViewById(R.id.title);
        LinearLayout title_ll = (LinearLayout) convertView.findViewById(R.id.title_ll);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.img_Items);
        imageView.setVisibility(View.VISIBLE);
        title_ll.setVisibility(View.VISIBLE);
        linearLayout.setVisibility(View.GONE);
        if (isExpanded)
            imageView.setImageResource(R.drawable.ic_icon_uparrow);
        else
            imageView.setImageResource(R.drawable.ic_icon_downarrow);



        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("click_happens", "yes");
//                adapterClick.onItemClick(v, position);
                if (isExpanded)
                    _list.collapseGroup(groupPosition);
                else
                    _list.expandGroup(groupPosition, true);
            }
        });
        title_tv.setText(stringArrayList.get(groupPosition));
        // expandableListView.expandGroup(groupPosition);  //used to Expand the child list automatically at the time of displaying
        return convertView;
    }

    public void setOnItemClickListner(AdapterClick adapterClick) {
        this.adapterClick = adapterClick;
    }

    public interface AdapterClick {
        public void onItemClick(View v, int position);
    }

}

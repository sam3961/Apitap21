package com.apitap.views.adapters;

import android.app.Activity;
import android.content.Context;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.apitap.R;
import com.apitap.model.Constants;
import com.apitap.model.bean.AdsListBean;
import com.apitap.model.preferences.ATPreferences;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Sahil on 10/5/2016.
 */
public class AdsPagerAdapter extends PagerAdapter {

    Activity activity;
    List<AdsListBean.RESULT.AdsData> list;

    public AdsPagerAdapter(Activity activity, List<AdsListBean.RESULT.AdsData> data) {
        this.activity = activity;
        list = data;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inf.inflate(R.layout.view_ads_pager, null);

        ImageView img_main = (ImageView) view.findViewById(R.id.img_main);
        Picasso.get().load(ATPreferences.readString(activity, Constants.KEY_IMAGE_URL) +
                list.get(position).getImageName()).into(img_main);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }

    @Override
    public float getPageWidth(int position) {
        return (float) 0.9;
    }
}

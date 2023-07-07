package com.apitap.views.adapters;

import android.content.Context;
import android.os.Parcelable;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.apitap.R;


public class TourPagerAdapter extends PagerAdapter {
    private Context context;
    private LayoutInflater inflater;
    private int[] mResources;


    public TourPagerAdapter(Context context, int[] mResources) {
        this.context = context;
        this.mResources = mResources;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {

        return mResources.length;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View image = inflater.inflate(R.layout.tour_adapter, container, false);
        assert image != null;
        final ImageView imageView = (ImageView) image.findViewById(R.id.image);
        imageView.setImageResource(mResources[position]);

        container.addView(image, 0);
        return image;
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

}
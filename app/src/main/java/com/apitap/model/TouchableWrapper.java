package com.apitap.model;

/**
 * Created by Shami on 17/4/2018.
 */

import android.content.Context;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import com.apitap.views.MerchantStoreMap;

public class TouchableWrapper extends FrameLayout {

    public TouchableWrapper(Context context) {
        super(context);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                MerchantStoreMap.mMapIsTouched = true;
                break;

            case MotionEvent.ACTION_UP:
                MerchantStoreMap.mMapIsTouched = false;
                break;
        }
        return super.dispatchTouchEvent(event);
    }
}
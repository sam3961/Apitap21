package com.apitap.model.customclasses;

import android.content.Context;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by Shami on 27/2/2018.
 */

public class CustomLinearLayout extends LinearLayout {
    public CustomLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    // some other necessary things

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getMeasuredWidth();

        //force a 16:9 aspect ratio
        int height = Math.round(width * .5625f);
        setMeasuredDimension(width, height);
    }
}
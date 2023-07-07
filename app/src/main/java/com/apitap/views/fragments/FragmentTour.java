package com.apitap.views.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.apitap.R;
import com.apitap.model.ViewPagerCustomDuration;
import com.apitap.views.HomeActivity;
import com.apitap.views.adapters.TourPagerAdapter;

import me.relex.circleindicator.CircleIndicator;

public class FragmentTour extends Activity implements View.OnClickListener {

    private TourPagerAdapter tourPagerAdapter;
    int[] mResources = {R.drawable.welcome_screen,R.drawable.tour_1, R.drawable.tour_2,
            R.drawable.tour_3, R.drawable.tour_4, R.drawable.tour_5, R.drawable.tour_6,
            /*R.drawable.tour_7,*/ R.drawable.tour_8
    };
    private ImageView[] dots;
    private int state = 0, dotscount = 0;
    LinearLayout ll_back, welcomeScreen_ll;
    ViewPagerCustomDuration viewPager;
    CircleIndicator circleIndicator;
    LinearLayout SliderDots;
    RelativeLayout parent_rl;
    ImageView done_img, next_img, back_img;
    ImageView welcome_Skip_btn, welcome_start_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_tour);
        tabContainer2Visible();
        initViews();
    }

    private void initViews() {
        ll_back = findViewById(R.id.back_ll);
        viewPager = findViewById(R.id.viewpager);
        circleIndicator = findViewById(R.id.indicator_default);
        done_img = findViewById(R.id.done_btn);
        next_img = findViewById(R.id.nxt_btn);
        back_img = findViewById(R.id.back_btn);
        SliderDots = findViewById(R.id.SliderDots);
        parent_rl = findViewById(R.id.parent_rl);
        welcomeScreen_ll = findViewById(R.id.welcome_screen);
        welcome_Skip_btn = findViewById(R.id.skipbtn);
        welcome_start_btn = findViewById(R.id.startbtn);


        setAdapter();
        ll_back.setOnClickListener(this);
        next_img.setOnClickListener(this);
        done_img.setOnClickListener(this);
        back_img.setOnClickListener(this);
        welcome_Skip_btn.setOnClickListener(this);
        welcomeScreen_ll.setOnClickListener(this);
        welcome_start_btn.setOnClickListener(this);


        pagerChangeListener();

        welcomeScreen_ll.setVisibility(View.GONE);
        parent_rl.setVisibility(View.VISIBLE);
    }

    private void setAdapter() {
        viewPager.setAdapter(new TourPagerAdapter(this, mResources));
        circleIndicator.setViewPager(viewPager);

        dotscount = mResources.length-1;
        dots = new ImageView[dotscount];

        for (int i = 0; i < dotscount; i++) {

            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.white_circle));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(55, 55);
            params.setMargins(6, 0, 6, 0);
            SliderDots.addView(dots[i], params);

        }
       // dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.green_circle));

    }

    private void pagerChangeListener() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                state = position;
                if (position == mResources.length - 1) {
                    done_img.setVisibility(View.VISIBLE);
                    next_img.setVisibility(View.GONE);
                } else {
                    done_img.setVisibility(View.GONE);
                    next_img.setVisibility(View.VISIBLE);
                }
                for (int i = position-1; i < dotscount; i++) {
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.white_circle));
                }
                for (int i = 0; i < position-1; i++) {
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.blue_circle));
                }

                dots[position-1].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.green_circle));

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_ll:
                startActivity(new Intent(this, HomeActivity.class));
                break;
            case R.id.done_btn:
                startActivity(new Intent(this, HomeActivity.class));
                break;
            case R.id.back_btn:
                if (state == 1) {
                    //state = mResources.length;
                    welcome_Skip_btn.setVisibility(View.VISIBLE);
                    welcome_start_btn.setVisibility(View.VISIBLE);
                   // parent_rl.setVisibility(View.GONE);
                    back_img.setVisibility(View.GONE);
                    next_img.setVisibility(View.GONE);

                } else
                    viewPager.setCurrentItem(state - 1);
                break;
            case R.id.nxt_btn:
                viewPager.setCurrentItem(state + 1);
                break;
            case R.id.skipbtn:
                startActivity(new Intent(this, HomeActivity.class));
                finish();
                break;
            case R.id.startbtn:
                //   ((HomeActivity) getActivity()).displayView(new FragmentTour(), Constants.TAG_TOUR, new Bundle());
               /* welcomeScreen_ll.setVisibility(View.GONE);
                parent_rl.setVisibility(View.VISIBLE);*/
                welcome_Skip_btn.setVisibility(View.GONE);
                welcome_start_btn.setVisibility(View.GONE);
                back_img.setVisibility(View.VISIBLE);
                next_img.setVisibility(View.VISIBLE);
                viewPager.setCurrentItem(state + 1);

                break;


        }

    }


    @Override
    public void onStart() {
        super.onStart();

    }

    public void tabContainer2Visible() {
        //homeTab2.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        //imageViewHomeTab2.setImageResource(R.drawable.ic_icon_home);

        HomeActivity.tabContainer2.setVisibility(View.GONE);
        HomeActivity.tabContainer2.setVisibility(View.GONE);
    }
}

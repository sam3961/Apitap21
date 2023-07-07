package com.apitap.views;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.apitap.R;
import com.apitap.controller.ModelManager;
import com.apitap.model.Constants;
import com.apitap.model.Operations;
import com.apitap.model.customclasses.Event;
import com.apitap.model.preferences.ATPreferences;
import com.apitap.views.fragments.login.FragmentLogin;
import com.apitap.views.fragments.FragmentSignup;
import com.github.rahatarmanahmed.cpv.CircularProgressView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    TabLayout mTabs;
    ViewPager mViewPager;
    Button btnGuest;
    public CircularProgressView mPocketBar;
    private static final String TAG = LoginActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorWhite));
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        setContentView(R.layout.activity_login);

        initViews();

        setupViewPager();
    }

    private void initViews() {
        mTabs = findViewById(R.id.custom_tabs);
        mViewPager = findViewById(R.id.viewpager_home);
        btnGuest = findViewById(R.id.btnGuest);
        btnGuest.setOnClickListener(this);
        mPocketBar = findViewById(R.id.pocket);
        ATPreferences.putBoolean(this, Constants.HEADER_STORE, false);

    }

    private void setupViewPager() {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(new FragmentLogin(), "Category 1");
        adapter.addFragment(new FragmentSignup(), "Category 2");
        mViewPager.setAdapter(adapter);
        mTabs.setupWithViewPager(mViewPager);
        mTabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        setupTabs();
        mViewPager.setCurrentItem(0);

    }

    private void setupTabs() {
        ArrayList<String> tabs = new ArrayList<>();
        tabs.add("Log In");
        tabs.add("Sign Up");
        for (int i = 0; i < tabs.size(); i++) {

            TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
            tabOne.setText(tabs.get(i));
            mTabs.getTabAt(i).setCustomView(tabOne);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnGuest:
                //    ATPreferences.putBoolean(LoginActivity.this, Constants.TOUR_GUEST, true);
             /*   mPocketBar.setVisibility(View.VISIBLE);
                ATPreferences.putString(LoginActivity.this, Constants.KEY_USERID,
                        "00011010000000000004");
                ATPreferences.putString(LoginActivity.this, Constants.KEY_USERNAME, "Guest");

                startActivity(new Intent(LoginActivity.this, HomeActivity.class).putExtra("Guest", "Login"));
                mPocketBar.setVisibility(View.GONE);
                finish();*/
                ATPreferences.putBoolean(this, Constants.GUEST,true);
                showProgress();
                ModelManager.getInstance().getLoginManager().guestLogin(this, Operations.makeJsonGuestLogin(this));
                break;

            default:
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(Event event) {
        switch (event.getKey()) {

            case Constants.GUEST_LOGIN_SUCCESS:
                hideProgress();
                ATPreferences.putString(LoginActivity.this, Constants.KEY_USERNAME, "Guest");
                startActivity(new Intent(LoginActivity.this, HomeActivity.class).putExtra("Guest", "Login"));
                mPocketBar.setVisibility(View.GONE);
                finish();
                break;


        }
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }
}

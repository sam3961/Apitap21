package com.apitap.model;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;

import com.apitap.views.fragments.specials.FragmentSpecial;
import com.google.android.material.tabs.TabLayout;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apitap.R;
import com.apitap.model.preferences.ATPreferences;
import com.apitap.views.HomeActivity;
import com.apitap.views.NavigationMenu.FragmentDrawer;
import com.apitap.views.fragments.items.FragmentItems;
import com.apitap.views.fragments.ads.FragmentAds;

/**
 * Created by Shami on 20/2/2018.
 */

public class AddTabBar {

    private Toolbar mToolbar;
    private TabLayout tabLayout;
    private FragmentDrawer drawerFragment;
    private TextView tabOne, tabTwo, tabThree, tabFour, tabFive, tabSix;


    public static AddTabBar mInstance;

    public AddTabBar() {
        mInstance = this;
    }

    public static AddTabBar getmInstance() {
        return mInstance;
    }


    public void setUpDrawer(AppCompatActivity context, Toolbar mToolbar, FragmentDrawer drawerFragment) {
        this.drawerFragment = drawerFragment;
        this.mToolbar = mToolbar;
        context.getSupportActionBar().setDisplayShowTitleEnabled(false);

    }

    public void setupViewPager(TabLayout tabLayout) {

       // tabLayout.addTab(tabLayout.newTab().setText("Home"));
        tabLayout.addTab(tabLayout.newTab().setText(" Businesses"), false);
        tabLayout.addTab(tabLayout.newTab().setText(" Ads"), false);
        tabLayout.addTab(tabLayout.newTab().setText(" Promotions"), false);

        tabLayout.getTabAt(0).setIcon(R.drawable.store_ico_selctor);
        tabLayout.getTabAt(1).setIcon(R.drawable.ads_selector);
        tabLayout.getTabAt(2).setIcon(R.drawable.special_selector);

        //   tabLayout.addTab(tabLayout.newTab().setText("Items"));
       // tabLayout.addTab(tabLayout.newTab().setText("Favorites"));

    }

    public void setupTabIcons(TabLayout tabLayout, final Activity context, TextView tabOne, TextView tabTwo,
                              TextView tabThree, TextView tabFour, TextView tabFive, TextView tabSix,
                              ImageView homeTab2) {
        this.tabOne = tabOne;
        this.tabTwo = tabTwo;
        this.tabThree = tabThree;

       tabTwo = (TextView) LayoutInflater.from(context).inflate(R.layout.custom_tab, null);
        tabTwo.setText(" Businesses");
        //tabTwo.setTextColor(context.getResources().getColor(R.color.colorWhite));
        tabTwo.setTextSize(11);
        tabTwo.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_icon_store, 0, 0, 0);
        tabLayout.getTabAt(0).setCustomView(tabTwo);

        tabThree = (TextView) LayoutInflater.from(context).inflate(R.layout.custom_tab, null);
        //tabThree.setTextColor(context.getResources().getColor(R.color.colorWhite));
        tabThree.setText(" Ads");
        tabThree.setTextSize(11);
        tabThree.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ads_selector, 0, 0, 0);
        tabLayout.getTabAt(1).setCustomView(tabThree);

        tabFour = (TextView) LayoutInflater.from(context).inflate(R.layout.custom_tab, null);
        tabFour.setText(" Promotions");
       // tabFour.setTextColor(context.getResources().getColor(R.color.colorWhite));
        tabFour.setTextSize(11);
        tabFour.setCompoundDrawablesWithIntrinsicBounds(R.drawable.special_selector, 0, 0, 0);
        tabLayout.getTabAt(2).setCustomView(tabFour);

        homeTab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCurrentTabFragment(context, 99);

            }
        });
    }

    public void displayView(AppCompatActivity context, Fragment fragment, String tag, Bundle bundle, int id) {
        //  if (fragment != null) {
        FragmentManager fragmentManager = context.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (bundle != null)
            fragment.setArguments(bundle);
        //  if (fragB == null) {
        fragmentTransaction.replace(id, fragment);
        if (fragment instanceof FragmentAds || fragment instanceof FragmentSpecial || fragment instanceof FragmentItems) {

        } else
            fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.commit();
//            } else
//                fragmentTransaction.show(fragB);
        //  getSupportActionBar().setTitle(tag);
        // }
    }

    public void bindWidgetsWithAnEvent(final LinearLayout linearLayout, final TabLayout tabLayout, final AppCompatActivity activity, final int id) {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.d("tabSelect",tab.getPosition()+"  pos");

               // frameLayout.setVisibility(View.VISIBLE);
                setCurrentTabFragment(activity, tab.getPosition());

            /*    View view = tab.getCustomView();
                TextView selectedText = view.findViewById(R.id.tab);
                selectedText.setTextColor(ContextCompat.getColor(activity, R.color.colorWhite));
*/
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                Log.d("onTabUnselected",tab.getPosition()+"  pos");
            /*    View view = tab.getCustomView();
                TextView selectedText = view.findViewById(R.id.tab);
                selectedText.setTextColor(ContextCompat.getColor(activity, R.color.simple_grey));
*/
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                if (tab.getPosition()==0){
                    setCurrentTabFragment(activity, tab.getPosition());
                    tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                        @Override
                        public void onTabSelected(TabLayout.Tab tab) {
                            setCurrentTabFragment( activity, tab.getPosition());

                    /*        View view = tab.getCustomView();
                            TextView selectedText = view.findViewById(R.id.tab);
                            selectedText.setTextColor(ContextCompat.getColor(activity, R.color.colorWhite));
*/
                        }

                        @Override
                        public void onTabUnselected(TabLayout.Tab tab) {
                            Log.d("onTabUnselected",tab.getPosition()+"  pos");

                        }

                        @Override
                        public void onTabReselected(TabLayout.Tab tab) {
                            Log.d("onTabReselected",tab.getPosition()+"  pos");
                   /*         View view = tab.getCustomView();
                            TextView selectedText = view.findViewById(R.id.tab);
                            selectedText.setTextColor(ContextCompat.getColor(activity, R.color.simple_grey));
*/
                        }
                    });
                }
            }
        });
    }

        public static void setCurrentTabFragment(Activity activity, int tabPosition) {
        ATPreferences.putBoolean(activity,Constants.HEADER_STORE,false);
        ATPreferences.putString(activity, Constants.MERCHANT_ID, "");

        switch (tabPosition) {

            case 0:
                ATPreferences.putBoolean(activity,Constants.HEADER_STORE,false);
                ATPreferences.putString(activity, Constants.MERCHANT_ID, "");
               activity.startActivity(new Intent(activity, HomeActivity.class).putExtra("Tab", tabPosition));
               // activity.finish();
                //displayView(activity, new FragmentHome(), "home", null, id);
                break;
            case 1:
                activity.startActivity(new Intent(activity, HomeActivity.class).putExtra("Tab", tabPosition));
               // displayView(activity, new Fragment_Store(), "Store", null, id);
                break;
            case 2:
                activity.startActivity(new Intent(activity, HomeActivity.class).putExtra("Tab", tabPosition));

                //displayView(activity, new Fragment_Ads(), "Ads", null, id);
                break;
            case 3:
                activity.startActivity(new Intent(activity, HomeActivity.class).putExtra("Tab", tabPosition));
                //displayView(activity, new FragmentSpecial(), "Specials", null, id);
                break;
            case 4:
                activity.startActivity(new Intent(activity, HomeActivity.class).putExtra("Tab", tabPosition));
                //displayView(activity, new FragmentItems(), "Items", null, id);
                break;
            case 5:
                activity.startActivity(new Intent(activity, HomeActivity.class).putExtra("Tab", tabPosition));
                // displayView(activity, new FragmentFavourite(), "Favorites", null, id);
                break;
            case 99:
                ATPreferences.putBoolean(activity,Constants.HEADER_STORE,false);
                ATPreferences.putString(activity, Constants.MERCHANT_ID, "");
                activity.startActivity(new Intent(activity, HomeActivity.class).putExtra("Tab", 99));
                //displayView(activity, new FragmentHome(), "home", null, id);
                break;

        }
    }

}

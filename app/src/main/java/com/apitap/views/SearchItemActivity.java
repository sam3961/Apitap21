package com.apitap.views;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;

import com.apitap.views.fragments.favourite.FragmentFavourite;
import com.apitap.views.fragments.specials.FragmentSpecial;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.apitap.R;
import com.apitap.controller.ModelManager;
import com.apitap.model.AddTabBar;
import com.apitap.model.Constants;
import com.apitap.model.Operations;
import com.apitap.model.Utils;
import com.apitap.model.ViewPagerCustomDuration;
import com.apitap.model.bean.AdsDetailWithMerchant;
import com.apitap.model.bean.SearchBean;
import com.apitap.model.bean.SearchSpecialBean;
import com.apitap.model.customclasses.Event;
import com.apitap.model.preferences.ATPreferences;
import com.apitap.views.NavigationMenu.FragmentDrawer;
import com.apitap.views.adapters.SamplePagerAdapter;
import com.apitap.views.fragments.search.adapter.SearchItemAdapter;
import com.apitap.views.customviews.HorizontalSpaceItemDecoration;
import com.apitap.views.fragments.itemDetails.FragmentItemDetails;
import com.apitap.views.fragments.items.FragmentItems;
import com.apitap.views.fragments.messages.FragmentMessages;
import com.apitap.views.fragments.FragmentScanner;
import com.apitap.views.fragments.ads.FragmentAds;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.linearlistview.LinearListView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;

import static com.apitap.App.isGuest;

/**
 * Created by sourcefuse on 25/11/16.
 */

public class SearchItemActivity extends BaseActivity implements View.OnClickListener, FragmentDrawer.FragmentDrawerListener {
    RecyclerView listView;
    LinearListView listView2;
    private static int toolint = 0;
    ArrayList<String> mernchantfavlist = new ArrayList<>();
    ViewPagerCustomDuration viewPager;
    CircleIndicator circleIndicator;
    LinearLayout listlayout1, listlayout2, noItemFound, noSpecialFound, adLayout, noadLayout;
    public static LinearLayout viewMain;
    public static FrameLayout frameLayout;
    List<SearchSpecialBean.RESULT> allImagesItems;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    static Activity mActivity;
    List<SearchSpecialBean.PC> pcList;
    LinearLayout scan_tool;
    LinearLayout msg_tool;
    LinearLayout search_tool;
    TextView searchedtv;
    TextView location_tv;
    LinearLayout back_tool;
    Button searchAgain, cancel_search;
    private FragmentDrawer drawerFragment;
    private Toolbar mToolbar;
    public static TabLayout tabLayout;
    private TextView tabOne, tabTwo, tabThree, tabFour, tabFive, tabSix;
    private ImageView  homeTab2;
    LinearLayout tabConatiner;
    FrameLayout ff_back;
    ScrollView scrollView;
    LinearLayout buttons_ll;
    TextView tv_back;
    ArrayList<String> removeDuplicacy = new ArrayList<>();
    SamplePagerAdapter SamplePagerAdapter;
    String searchKey = "", sort_by = Constants.Alphabetical, isFav = "false";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_item);
        mActivity = this;
        viewPager = findViewById(R.id.viewpager);
        circleIndicator = findViewById(R.id.indicator_default);
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        listlayout1 = findViewById(R.id.lin1);
       // homeTab2 = findViewById(R.id.homeTab2);
        listlayout2 = findViewById(R.id.lin2);
        noItemFound = findViewById(R.id.noItemll);
        noSpecialFound = findViewById(R.id.noSpecial_ll);
        adLayout = findViewById(R.id.ad_ll);
        noadLayout = findViewById(R.id.no_Ads);
        tv_back = findViewById(R.id.ic_back);
        viewMain = findViewById(R.id.mainview);
        frameLayout = findViewById(R.id.container_body2);
        tabConatiner = findViewById(R.id.tab_container);
        tabLayout = findViewById(R.id.tabs);
        scan_tool = findViewById(R.id.ll_scan);
        msg_tool = findViewById(R.id.ll_message);
        search_tool = findViewById(R.id.ll_search);
        searchedtv = findViewById(R.id.searched_key);
        location_tv = findViewById(R.id.location_key);
        back_tool = findViewById(R.id.iv_back);
        ff_back = findViewById(R.id.backff);
        scrollView = findViewById(R.id.scroll_view);
        buttons_ll = findViewById(R.id.button_ll);

        searchAgain = findViewById(R.id.searchAgain);
        cancel_search = findViewById(R.id.cancelbtn);


        searchAgain.setOnClickListener(this);
        tv_back.setOnClickListener(this);
        cancel_search.setOnClickListener(this);
        scan_tool.setOnClickListener(this);
        msg_tool.setOnClickListener(this);
        search_tool.setOnClickListener(this);
        //back_tool.setOnClickListener(this);

        searchKey = getIntent().getExtras().getString("key");
        String location = getIntent().getExtras().getString("location");
        searchedtv.setText("Searched For: " + searchKey);
        if (location != null)
            location_tv.setText("Location: " + location);
        else
            location_tv.setText("Location: " + "Current Location");

        if (location_tv.getText().toString().contains("Current Location"))
            sort_by = Constants.NEAR_ME;

        showProgress();
        if (getIntent().hasExtra("isFav")) {
            isFav = "true";
            ModelManager.getInstance().getMerchantFavouriteManager().getFavourites(mActivity,
                    Operations.makeJsonGetMerchantFavourite(mActivity));
        } else {
            //  ModelManager.getInstance().getSearchItemsManager().getAllSearchItems(this, Operations.makeJsonSearchItem(SearchItemActivity.this, searchKey));
//            ModelManager.getInstance().getSearchItemsManager().getAllSearchProduct(this,
//                    Operations.makeJsonSearchProduct(SearchItemActivity.this, Utils.convertStringToHex(searchKey), sort_by,"",""));
        }


        listView = findViewById(R.id.list);
        listView2 = findViewById(R.id.list2);
        listView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        listView.addItemDecoration(new HorizontalSpaceItemDecoration(20));

        AddTabBar.getmInstance().setupViewPager(tabLayout);
        AddTabBar.getmInstance().setupTabIcons(tabLayout, mActivity, tabOne, tabTwo,
                tabThree, tabFour, tabFive, tabSix,homeTab2);
        AddTabBar.getmInstance().bindWidgetsWithAnEvent(tabConatiner, tabLayout, SearchItemActivity.this, R.id.container_body2);

    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Subscribe
    public void onEvent(Event event) {
        switch (event.getKey()) {
            case Constants.SEARCH_ITEM_SUCCESS:
                boolean setAdap = false;
                //   final List<SearchItemBean.Result.ResultData> arrayList = ModelManager.getInstance().getSearchItemsManager().searchItemBean.getResult().get(0).getResult();
                final HashMap<Integer, ArrayList<SearchBean>> map = ModelManager.getInstance().getSearchItemsManager().itemsData;

                if (isFav.equals("true"))
                    for (int i = 0; i < map.size(); i++) {
                        if (mernchantfavlist.contains(map.get(i).get(0).getSellerName())) {
                            setAdap = true;
                        }
                    }

                if (setAdap || !isFav.equals("true") && map.size() > 0) {
                    SearchItemAdapter searchItemAdapter = new SearchItemAdapter(this, map);
                    listView.setAdapter(searchItemAdapter);
                    searchItemAdapter.setOnItemClickListner(new SearchItemAdapter.AdapterClick() {
                        @Override
                        public void onItemClick(View v, int position) {
                            String productId = Utils.lengtT(11, map.get(position).get(0).getProductId());
                            String productType = map.get(position).get(0).getProdcutType();
                            Bundle bundle = new Bundle();
                            bundle.putString("productId", productId);
                            bundle.putString("productType", productType);
                            bundle.putString("flag", "search");
                            clearBackstack();
                            displayView(new FragmentItemDetails(), Constants.TAG_DETAILSPAGE, bundle);


                        }
                    });

                } else {
                    noItemFound.setVisibility(View.GONE);
                    listlayout1.setVisibility(View.GONE);
                }


                //circularProgressView.setVisibility(View.GONE);

                break;
            case Constants.ADDRESS_NEARBY_SUCCESS:
                Utils.setRecyclerNearByAdapter(getApplicationContext());
                break;
            case Constants.SEARCH_ITEM_SUCCESS_Empty:

                hideProgress();
                noItemFound.setVisibility(View.VISIBLE);
                listlayout1.setVisibility(View.GONE);
                break;

            case Constants.SEARCH_ITEM_SUCCESS_List2:
                //   circularProgressView.setVisibility(View.GONE);
                setDataSpecial();
                break;
            case Constants.SEARCH_ITEM_SUCCESS_List2_Empty:
                //   circularProgressView.setVisibility(View.GONE);
                listlayout2.setVisibility(View.GONE);
                noSpecialFound.setVisibility(View.VISIBLE);
                break;

            case Constants.GET_MERCHANT_FAVORITES:
                //    if (isFavorite.equals("1"))
                //   ModelManager.getInstance().getSearchItemsManager().getAllSearchItems(this, Operations.makeJsonSearchItem(SearchItemActivity.this, searchKey));
/*
                ModelManager.getInstance().getSearchItemsManager().getAllSearchProduct(this, Operations.
                        makeJsonSearchProduct(SearchItemActivity.this, searchKey, sort_by,"",""));
*/
                mernchantfavlist = ModelManager.getInstance().getMerchantFavouriteManager().mernchantfavlist;
                break;
            case Constants.ADS_LISTING_SUCCESS:
                hideProgress();
                scrollView.setVisibility(View.VISIBLE);
                buttons_ll.setVisibility(View.VISIBLE);
                showAdsWithAnimation();
                break;
            case Constants.ADS_LISTING_EMPTY:
                hideProgress();
                scrollView.setVisibility(View.VISIBLE);
                buttons_ll.setVisibility(View.VISIBLE);
                adLayout.setVisibility(View.GONE);
                noadLayout.setVisibility(View.VISIBLE);
                break;
            case -1:
                hideProgress();
                Toast.makeText(getApplicationContext(), "Problem occurred, please try again.", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void clearBackstack() {
        FragmentManager fm = getSupportFragmentManager();
        for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
    }

    Runnable r;
    Handler h;
    int count;

    private void showAdsWithAnimation() {

        if (ModelManager.getInstance().getSearchItemsManager().ads != null)
            if (ModelManager.getInstance().getSearchItemsManager().ads.size() > 0) {

                count = 0;
                final ArrayList<AdsDetailWithMerchant> adsDetailWithMerchants = ModelManager.getInstance().getSearchItemsManager().url_maps1;

                SamplePagerAdapter = new SamplePagerAdapter(this, ModelManager.getInstance()
                        .getSearchItemsManager().ads, adsDetailWithMerchants, false, "search");
                viewPager.setAdapter(SamplePagerAdapter);
                circleIndicator.setViewPager(viewPager);
                viewPager.setCurrentItem(count);
                // viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
                h = new Handler();
                r = new Runnable() {
                    @Override
                    public void run() {
                        h.removeMessages(0);
                        ++count;
                        if ((count + 1) > ModelManager.getInstance().getSearchItemsManager().ads.size())
                            count = 0;

                        if (SamplePagerAdapter != null) {
                            SamplePagerAdapter.notifyDataSetChanged();
                        }
                        viewPager.setCurrentItem(count);
                        h.postDelayed(r, 10000);
                    }
                };

                viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        count = position;
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });
                h.postDelayed(r, 1000);
            } else {

                //  mLlAds.setVisibility(View.GONE);
                //noAdsll.setVisibility(View.VISIBLE);
            }
    }

    private void setDataSpecial() {
        removeDuplicacy = new ArrayList<>();
        List<SearchSpecialBean.RESULT> specialBeanArrayList = ModelManager.getInstance().getSearchItemsManager().searchSpecialBean.getRESULT();
        this.allImagesItems = specialBeanArrayList;
        Log.d("SizeSpecial", specialBeanArrayList.size() + "");
        if (allImagesItems.size() > 0)
            listView2.setAdapter(mAdapterItems);
        else {
            listlayout2.setVisibility(View.GONE);
            noSpecialFound.setVisibility(View.GONE);
        }

    }

    @Override
    protected void onPause() {
        if (SamplePagerAdapter != null) {
            SamplePagerAdapter.notifyDataSetChanged();
        }
        super.onPause();
    }

    private BaseAdapter mAdapterItems = new BaseAdapter() {

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.row_horizontal_test, parent, false);
            }
         //   pcList = allImagesItems.get(position).getPC();

            ImageView imageView = convertView.findViewById(R.id.image);
            CardView cardViewParent = convertView.findViewById(R.id.parentCard);
            TextView description = convertView.findViewById(R.id.description);
            ImageView eye = convertView.findViewById(R.id.eye);
            String isSeen = allImagesItems.get(0).getPC().get(position).get1149();
            LinearLayout rlSinglePrice = convertView.findViewById(R.id.rel_single_price);
            LinearLayout rlTwoPrice = convertView.findViewById(R.id.rl_two_price);
            TextView actualPrice = convertView.findViewById(R.id.actual_price);

            if ((mernchantfavlist != null && mernchantfavlist.size() > 0 &&
                    mernchantfavlist.contains(Utils.hexToASCII(allImagesItems.get(0).getPC().get(position).get_114170())))
                    || (!isFav.equals("true"))) {
                if (!removeDuplicacy.contains(Utils.hexToASCII(allImagesItems.get(0).getPC().get(position).get120157()))) {
                    removeDuplicacy.add(Utils.hexToASCII(allImagesItems.get(0).getPC().get(position).get120157()));
                    if (isSeen.equalsIgnoreCase("false")) {
                        eye.setBackgroundResource(R.drawable.green_seen);
                    } else {
                        eye.setVisibility(View.GONE);
                    }

                    description.setText(Utils.hexToASCII(allImagesItems.get(0).getPC().get(position).get120157()));
                    rlSinglePrice.setVisibility(View.VISIBLE);
                    rlTwoPrice.setVisibility(View.GONE);
                    actualPrice.setText(allImagesItems.get(0).getPC().get(position).get122162());
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String productId = Utils.lengtT(11, allImagesItems.get(0).getPC().get(position).get114144());
                            String productType = allImagesItems.get(0).getPC().get(position).get114112();
                            Bundle bundle = new Bundle();
                            bundle.putString("productId", productId);
                            bundle.putString("productType", productType);
                            bundle.putString("flag", "search");

                            clearBackstack();
                            displayView(new FragmentItemDetails(), Constants.TAG_DETAILSPAGE, bundle);

                        }
                    });
                    Glide.with(mActivity).load(ATPreferences.readString(mActivity, Constants.KEY_IMAGE_URL)
                            + allImagesItems.get(0).getPC().get(position).get121170())
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(imageView);

                }
                else
                    cardViewParent.setVisibility(View.GONE);
            } else {
                cardViewParent.setVisibility(View.GONE);
            }
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
            return allImagesItems.get(0).getPC().size();
        }
    };

    public void displayView(Fragment fragment, String tag, Bundle bundle) {
        //  if (fragment != null) {
        frameLayout.setVisibility(View.VISIBLE);
        viewMain.setVisibility(View.GONE);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragB = fragmentManager.findFragmentByTag(tag);
        if (bundle != null)
            fragment.setArguments(bundle);
        //  if (fragB == null) {
        fragmentTransaction.replace(R.id.container_body2, fragment);
        if (fragment instanceof FragmentAds || fragment instanceof FragmentSpecial || fragment instanceof FragmentItems) {

        } else
            fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.commit();
//            } else
//                fragmentTransaction.show(fragB);
        //  getSupportActionBar().setTitle(tag);
        // }
    }


    public void onResume() {
        super.onResume();
        showAdsWithAnimation();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //backPress();
        if (frameLayout.getVisibility() == View.VISIBLE) {
            frameLayout.setVisibility(View.GONE);
            viewMain.setVisibility(View.VISIBLE);
        } else
            mActivity.finish();

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_message:
                displayView(new FragmentMessages(), Constants.TAG_MESSAGEPAGE, new Bundle());
                break;
            case R.id.ic_back:
                finish();
                break;
            case R.id.ll_scan:
                frameLayout.setVisibility(View.VISIBLE);
                viewMain.setVisibility(View.GONE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            requestPermissions(new String[]{Manifest.permission.CAMERA},
                                    MY_CAMERA_REQUEST_CODE);
                        }
                    } else {
                        //mTxtHeading.setText("Scan");
                        //mlogo.setVisibility(View.GONE);
                        //mTxtHeading.setVisibility(View.VISIBLE);
                        displayView(new FragmentScanner(), Constants.TAG_SCANNER, new Bundle());
                    }
                }

                break;
            case R.id.ll_search:
                //Utils.showSearchDialog(this, "SearchItem");
                if (isGuest)
                    Utils.showGuestDialog(this);
                else {
                    displayView(new FragmentFavourite(), Constants.TAG_FAVOURITEPAGE, null);
                }
                break;

            case R.id.cancelbtn:
                finish();
                break;

            case R.id.searchAgain:
                search_tool.performClick();
                break;

            case R.id.iv_back:

                if (frameLayout.getVisibility() == View.VISIBLE) {
                    frameLayout.setVisibility(View.GONE);
                    viewMain.setVisibility(View.VISIBLE);
                } else
                    finish();

                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_CAMERA_REQUEST_CODE) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (toolint == 0) {

                    //  Toast.makeText(this, "Camera permission granted", Toast.LENGTH_LONG).show();
                    // mTxtHeading.setText("Scan");
                    //mlogo.setVisibility(View.GONE);
                    //mTxtHeading.setVisibility(View.VISIBLE);
                    displayView(new FragmentScanner(), Constants.TAG_SCANNER, new Bundle());

                } else {
                    displayView(new FragmentScanner(), Constants.TAG_SCANNER, null);
                }
            } else {

                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_LONG).show();

            }

        }
    }


    @Override
    public void onDrawerItemSelected(View view, int position) {
        startActivity(new Intent(this, HomeActivity.class).putExtra("Drawer", position));

    }

    public static void viewFrame() {
        viewMain.setVisibility(View.VISIBLE);
        frameLayout.setVisibility(View.GONE);
    }

}
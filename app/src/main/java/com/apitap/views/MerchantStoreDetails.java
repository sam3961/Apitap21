package com.apitap.views;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;

import androidx.annotation.NonNull;

import com.apitap.controller.MerchantFavouriteManager;
import com.apitap.model.storeFrontItems.details.RESULTItem;
import com.apitap.views.fragments.favourite.FragmentFavourite;
import com.apitap.views.fragments.specials.FragmentSpecial;
import com.google.android.material.tabs.TabLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.apitap.App;
import com.apitap.R;
import com.apitap.controller.ModelManager;
import com.apitap.model.AddTabBar;
import com.apitap.model.Constants;
import com.apitap.model.Operations;
import com.apitap.model.Utils;
import com.apitap.model.ViewPagerCustomDuration;
import com.apitap.model.bean.AdsDetailWithMerchant;
import com.apitap.model.bean.LocationListBean;
import com.apitap.model.bean.RatingBean;
import com.apitap.model.customclasses.Event;
import com.apitap.model.preferences.ATPreferences;
import com.apitap.views.NavigationMenu.FragmentDrawer;
import com.apitap.views.adapters.LocationListAdpater;
import com.apitap.views.adapters.SamplePagerAdapter;
import com.apitap.views.fragments.items.FragmentItems;
import com.apitap.views.fragments.messages.FragmentMessages;
import com.apitap.views.fragments.FragmentScanner;
import com.apitap.views.fragments.ads.FragmentAds;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import me.relex.circleindicator.CircleIndicator;

import static com.apitap.App.isGuest;
import static com.apitap.controller.MerchantManager.merchantCategoryList;
import static com.apitap.model.Utils.MY_PERMISSIONS_REQUEST_LOCATION;
import static com.apitap.views.fragments.home.FragmentHome.setFavouriteMerchantView;

/**
 * Created by Shami on 20/9/2017.
 */

public class MerchantStoreDetails extends AppCompatActivity implements View.OnClickListener, FragmentDrawer.FragmentDrawerListener, LocationListAdpater.AdapterClick {

    private TextView storeName, storeDetails, showAll, ratingNo, tv_termsAndCondition;
    private ImageView inbox, favourite, share, img_main, storeImg;
    private RecyclerView recyclerView;
    ViewPagerCustomDuration viewPager;
    CircleIndicator circleIndicator;
    private RatingBar ratingBar;
    private FragmentDrawer drawerFragment;
    private Toolbar mToolbar;
    private String merchantId, image_str, merchantName, searchkey;
    private LinearLayout giveRating, ratingNumbers;
    private Activity mActivity;
    private static int toolint = 0;
    FrameLayout frameLayout;
    SmoothProgressBar smoothProgressBar;
    Uri uri;
    CardView noAdsCard, adsCard;
    Button storeBrowse;
    LocationListAdpater.AdapterClick adapterClick;
    LinearLayout scan_tool;
    LinearLayout msg_tool;
    LinearLayout search_tool;
    LinearLayout back_tool;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    LocationListAdpater locationListAdpater;
    CircularProgressView mPocketBar;
    CardView nolocationTxt, nolocationLinear;
    LinearLayout browseStore, header;
    ImageView iv_back;
    ImageView noStoreLogo;
    int position = 0;
    String current_lat = "", current_long = "";
    public static TabLayout tabLayout;
    private TextView tabOne, tabTwo, tabThree, tabFour, tabFive, tabSix;
    private ImageView  homeTab2;
    LinearLayout tabConatiner;
    DrawerLayout rootLayout;
    boolean isFavorite = false;
    private Spinner sotre_categorySpinner;
    private LinearLayout ll_storeMessages, linearLayoutRating,linearLayoutStoreTab;
    private RESULTItem data;


    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.store_review_screen);
        mActivity = this;
        adapterClick = this;
        if (getIntent() != null) {
            merchantId = getIntent().getStringExtra("merchantId");
            Log.d("MerchantID", merchantId + "   s");
        }
        getloc();
        initViews();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (frameLayout.getVisibility() == View.VISIBLE)
            frameLayout.setVisibility(View.GONE);
        else
            finish();
    }

    private void initViews() {

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        sotre_categorySpinner = findViewById(R.id.category_dropdown);
        tabConatiner = findViewById(R.id.tab_container);
        rootLayout = findViewById(R.id.drawer_layout);
        noAdsCard = findViewById(R.id.noAdsll);
        homeTab2 = findViewById(R.id.tab_one_image);
        storeBrowse = findViewById(R.id.details_store);
        tabLayout = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.viewpager);
        iv_back = findViewById(R.id.tv_back);
        circleIndicator = findViewById(R.id.indicator_default);
        frameLayout = findViewById(R.id.container_body);
        frameLayout.setVisibility(View.GONE);
        storeName = findViewById(R.id.storeName);
        storeDetails = findViewById(R.id.storeDetails);
        showAll = findViewById(R.id.showall);
        ratingNo = findViewById(R.id.ratingNo);
        ll_storeMessages = findViewById(R.id.message_store);
        linearLayoutRating = findViewById(R.id.linearLayoutRating);
        linearLayoutStoreTab = findViewById(R.id.view_store_tabs);
        tv_termsAndCondition = findViewById(R.id.termsConditions);
        inbox = findViewById(R.id.messages);
        favourite = findViewById(R.id.favourite);
        img_main = findViewById(R.id.img_main);
        storeImg = findViewById(R.id.adstoreImg);
        noStoreLogo = findViewById(R.id.noStoreLogo);
        share = findViewById(R.id.share);
        recyclerView = findViewById(R.id.recycler);
        ratingBar = findViewById(R.id.ratingBar);
        ratingNumbers = findViewById(R.id.ratingnumbers);
        smoothProgressBar = findViewById(R.id.pocketa);

        scan_tool = findViewById(R.id.ll_scan);
        header = findViewById(R.id.header);
        msg_tool = findViewById(R.id.ll_message);
        search_tool = findViewById(R.id.ll_search);
        back_tool = findViewById(R.id.iv_back);
        mPocketBar = findViewById(R.id.pocket);
        nolocationTxt = findViewById(R.id.nolocation);
        nolocationLinear = findViewById(R.id.locationlinear);


        ratingBar.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        storeName.setOnClickListener(this);
        storeBrowse.setOnClickListener(this);
        linearLayoutRating.setOnClickListener(this);
        //inbox.setOnClickListener(this);
        share.setOnClickListener(this);
        scan_tool.setOnClickListener(this);
        msg_tool.setOnClickListener(this);
        search_tool.setOnClickListener(this);
        showAll.setOnClickListener(this);
        ll_storeMessages.setOnClickListener(this);
        getfocus();
        mPocketBar.setVisibility(View.VISIBLE);

//        ModelManager.getInstance().getMerchantManager().getMerchantDetail(this,
//                Operations.makeJsonGetMerchantDetail(this, merchantId), Constants.GET_MERCHANT_SUCCESS);
//
//        ModelManager.getInstance().getMerchantFavouriteManager().getFavourites(mActivity,
//                Operations.makeJsonGetMerchantFavourite(mActivity));
//


/*
        ModelManager.getInstance().getAdsManager().getAllAds(this,
                Operations.makeJsonGetAdsListing(this, merchantId), Constants.ADS_LISTING_SUCCESS);
*/

        ModelManager.getInstance().getAddMerchantRating().getMerchantRating(MerchantStoreDetails.this,
                Operations.GetMerchantRating(mActivity, ATPreferences.readString(this, Constants.KEY_USERID), merchantId));


        AddTabBar.getmInstance().setupViewPager(tabLayout);
        AddTabBar.getmInstance().setupTabIcons(tabLayout, mActivity, tabOne, tabTwo, tabThree, tabFour, tabFive,
                tabSix, homeTab2);
        AddTabBar.getmInstance().bindWidgetsWithAnEvent(tabConatiner, tabLayout, MerchantStoreDetails.this, R.id.container_body);
    }

    private void setData() {
        String terms = "";
        String policy = "";
        if (ModelManager.getInstance().getMerchantStoresManager().storeDetailsModel.getRESULT().size() > 0) {
            data = ModelManager.getInstance().getMerchantStoresManager().storeDetailsModel.getRESULT().get(0);
//            if (data.getName().length() > 15)
//                storeName.setText(data.getName().substring(0,14) + "...");
//            else
            storeName.setText(Utils.hexToASCII(data.getJsonMember11470()));
            //storeName.setText(data.getName());
            merchantName = Utils.hexToASCII(data.getJsonMember11470());
            if (!data.getJsonMember11915().isEmpty())
                terms = Utils.hexToASCII(data.getJsonMember11915());
            if (!data.getJsonMember11916().isEmpty())
                policy = Utils.hexToASCII(data.getJsonMember11916());
            if (terms.isEmpty() && policy.isEmpty())
                tv_termsAndCondition.setText("Currently Not Available");
            else {
                String subjectString = terms + " " + policy;
                subjectString = Normalizer.normalize(subjectString, Normalizer.Form.NFD);
                String resultString = subjectString.replaceAll("[^\\x00-\\x7F]", "");
                // tv_termsAndCondition.setText(resultString);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    tv_termsAndCondition.setText(Html.fromHtml(resultString, Html.FROM_HTML_MODE_LEGACY));
                } else {
                    tv_termsAndCondition.setText(Html.fromHtml(resultString));
                }
            }
            LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();

            if (data.getJsonMember12219().equals("0")) {
                ratingNo.setText("No Ratings");
                stars.getDrawable(0).setColorFilter(Color.parseColor("#fcb74d"), PorterDuff.Mode.SRC_ATOP);

                ratingBar.setRating((Float.parseFloat(data.getJsonMember12219())));
            } else {
                stars.getDrawable(2).setColorFilter(Color.parseColor("#fcb74d"), PorterDuff.Mode.SRC_ATOP);
                stars.getDrawable(0).setColorFilter(Color.parseColor("#fcb74d"), PorterDuff.Mode.SRC_ATOP);
                stars.getDrawable(1).setColorFilter(Color.parseColor("#fcb74d"), PorterDuff.Mode.SRC_ATOP);
                ratingBar.setRating(Float.parseFloat(data.getJsonMember12219()));
                Log.d("ratingIs2", (int) Double.parseDouble(data.getJsonMember12219()) + "");

            }
            try {
                String subjectString = Utils.getStringHexaDecimal(data.getJsonMember120157());
                if (!data.getJsonMember120157().isEmpty())
                    storeDetails.setText(subjectString);
                else
                    storeDetails.setText("No Description Available Currently");
            } catch (Exception e) {
                e.printStackTrace();
            }
            Picasso.get().load(ATPreferences.readString(this, Constants.KEY_IMAGE_URL) + data.getJsonMember121170()).into(img_main);
            Picasso.get().load(ATPreferences.readString(this, Constants.KEY_IMAGE_URL) + data.getJsonMember121170()).into(storeImg);
            //Picasso.get().load(ATPreferences.readString(this, Constants.KEY_IMAGE_URL) + data.getImage()).into(noStoreLogo);
            Picasso.get().load(ATPreferences.readString(this, Constants.KEY_IMAGE_URL) + data.getJsonMember121170()).fit().centerInside().into(noStoreLogo);

            image_str = data.getJsonMember121170();
            // uri = getLocalBitmapUri(img_main);
            setCategorySpinnerAdapter();
        }
        if (MerchantFavouriteManager.isCurrentMerchantFav)
            isFavorite = true;
        setFavouriteMerchantView(storeName);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
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


    @Subscribe
    public void onEvent(Event event) {
        switch (event.getKey()) {
            case Constants.GET_MERCHANT_SUCCESS:
                break;
            case Constants.ADS_LISTING_SUCCESS:
                showAdsWithAnimation();
                break;
            case Constants.ADDRESS_NEARBY_SUCCESS:
                Utils.setRecyclerNearByAdapter(getApplicationContext());
                break;

            case Constants.REMOVE_MERCHANT_FAVORITES:
                smoothProgressBar.setVisibility(View.GONE);
                smoothProgressBar.progressiveStop();
                favourite.setBackgroundResource(R.drawable.ic_icon_fav_gray);
                MerchantFavouriteManager.isCurrentMerchantFav = false;
                isFavorite = false;
                setFavouriteMerchantView(storeName);
                break;

            case Constants.GET_RATING_SUCCESS:
                clearfocus();
                mPocketBar.setVisibility(View.GONE);
                List<RatingBean.RESULT_> ratingBeen = ModelManager.getInstance().getAddMerchantRating().ratingBean.getRESULT().get(0).getRESULT();
                ratingNo.setText("(" + ratingBeen.size() + ")");
                if (ratingBeen.size() == 0) {
                    ratingNo.setText("No Ratings");
                }

                break;

            case Constants.GET_MERCHANT_DISTANCE_SUCCESS:
                List<LocationListBean.RESULT_> arrayList = new ArrayList<LocationListBean.RESULT_>();
                arrayList = ModelManager.getInstance().getMerchantManager().locationListBean.getRESULT().get(0).getRESULT();
                Log.d("listContains", arrayList.get(0).get478() + "");
                if (arrayList.get(0).get478() == null) {
                    nolocationTxt.setVisibility(View.VISIBLE);
                    nolocationLinear.setVisibility(View.GONE);
                } else {
                    locationListAdpater = new LocationListAdpater(adapterClick, mActivity, arrayList, inbox);
                    recyclerView.setHasFixedSize(true);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(locationListAdpater);
                }

                break;

            case Constants.ADD_MERCHANT_FAVORITE_SUCCESS:
                //    if (isFavorite.equals("1"))
                smoothProgressBar.setVisibility(View.GONE);
                smoothProgressBar.progressiveStop();
                favourite.setBackgroundResource(R.drawable.ic_icon_fav);
                MerchantFavouriteManager.isCurrentMerchantFav = true;
                isFavorite = true;
                setFavouriteMerchantView(storeName);
                break;
            case Constants.GET_MERCHANT_LOCATION_SUCCESS:
                break;
            case Constants.GET_MERCHANT_FAVORITES:
                //    if (isFavorite.equals("1"))
                ArrayList<String> mername = ModelManager.getInstance().getMerchantFavouriteManager().mernchantfavlist;
                Log.d("MernMae", mername + "  " + merchantName);
                try {
                    if (mername.contains(merchantName)) {
                        isFavorite = true;
                        favourite.setBackgroundResource(R.drawable.ic_icon_fav);
                    }

                } catch (Exception e) {
                }
                break;
        }
    }

    Runnable r;
    Handler h;
    int count;

    private void showAdsWithAnimation() {

        if (ModelManager.getInstance().getHomeManager().ads != null)
            if (ModelManager.getInstance().getHomeManager().ads.size() > 0) {
                count = 0;
                final ArrayList<AdsDetailWithMerchant> adsDetailWithMerchants = ModelManager.getInstance().getAdsManager().url_maps;
                viewPager.setAdapter(new SamplePagerAdapter(mActivity, ModelManager.getInstance().getHomeManager().ads, adsDetailWithMerchants, false, ""));
                circleIndicator.setViewPager(viewPager);
                viewPager.setCurrentItem(count);
                h = new Handler();
                r = new Runnable() {
                    @Override
                    public void run() {
                        h.removeMessages(0);
                        ++count;
                        if ((count + 1) > ModelManager.getInstance().getHomeManager().ads.size())
                            count = 0;

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
                // noAdsCard.setVisibility(View.VISIBLE);
                // adsCard.setVisibility(View.GONE);
            }
    }

    public void onResume() {
        super.onResume();
        setData();
        showAdsWithAnimation();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.linearLayoutRating:

                Intent i = new Intent(this, RateMerchant.class);
                i.putExtra("merchantId", merchantId);
                i.putExtra("storeName", storeName.getText().toString());
                i.putExtra("storeImage", image_str);
                i.putExtra("storeRateString", ratingNo.getText().toString());
                i.putExtra("rateProgress", (data.getJsonMember12219()));
                startActivity(i);
                break;
            case R.id.tv_back:
                finish();
                break;
            case R.id.message_store:
                Bundle bundle = new Bundle();
                bundle.putString("merchantId", merchantId);
                bundle.putString("className", "StoreDetails");
                bundle.putString("storeName", storeName.getText().toString());
                displayView(new FragmentMessages(), Constants.TAG_MESSAGEPAGE, bundle);
                break;

            case R.id.storeName:
                smoothProgressBar.setVisibility(View.VISIBLE);
                smoothProgressBar.progressiveStart();
                if (!isFavorite) {
                    ModelManager.getInstance().getAddMerchantFavorite().addMerchantToFavorite(mActivity, Operations.makeJsonMerchantAddToFavorite(mActivity, merchantId));
                } else
                    ModelManager.getInstance().getMerchantFavouriteManager().removeFavourite(mActivity,
                            Operations.makeJsonRemoveMerchantFavourite(mActivity, merchantId));
                break;
            case R.id.messages:
                displayView(new FragmentMessages(), Constants.TAG_MESSAGEPAGE, new Bundle());
                break;
            case R.id.share:
                shareImage();
                break;
            case R.id.ll_message:
                displayView(new FragmentMessages(), Constants.TAG_MESSAGEPAGE, new Bundle());
                break;
            case R.id.ll_scan:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.CAMERA},
                                MY_CAMERA_REQUEST_CODE);
                    } else {
                        //mTxtHeading.setText("Scan");
                        //mlogo.setVisibility(View.GONE);
                        //mTxtHeading.setVisibility(View.VISIBLE);
                        displayView(new FragmentScanner(), Constants.TAG_SCANNER, new Bundle());
                    }
                }

                break;
            case R.id.ll_search:
                //Utils.showSearchDialog(mActivity, "MerchantStoreDetails");
                if (isGuest)
                    Utils.showGuestDialog(this);
                else {
                    displayView(new FragmentFavourite(), Constants.TAG_FAVOURITEPAGE, null);
                }
                break;
            case R.id.details_store:
                Bundle bundle1 = new Bundle();
                bundle1.putString(Constants.MERCHANT_ID, merchantId);
                ATPreferences.putBoolean(mActivity, Constants.HEADER_STORE, true);
                ATPreferences.putString(mActivity, Constants.MERCHANT_ID, merchantId);
                startActivity(new Intent(mActivity, HomeActivity.class)
                        .putExtras(bundle1));
                finish();
                //  displayView(new FragmentStoreFront(), Constants.TAG_STORESFRONTPAGE, bundle1);

                break;


            case R.id.showall:
                Bundle bundle2 = new Bundle();
                bundle2.putString("merchantId",merchantId);
                bundle2.putInt("merchantId",99);
                Intent intent = new Intent(mActivity, MerchantStoreMap.class);
                intent.putExtra("merchantId", merchantId);

                startActivity(intent);
                break;

            case R.id.iv_back:

                if (frameLayout.getVisibility() == View.VISIBLE)
                    frameLayout.setVisibility(View.GONE);
                else
                    finish();

                break;
        }
    }

    public void displayView(Fragment fragment, String tag, Bundle bundle) {
        //  if (fragment != null) {
        frameLayout.setVisibility(View.VISIBLE);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragB = fragmentManager.findFragmentByTag(tag);
        if (bundle != null)
            fragment.setArguments(bundle);
        //  if (fragB == null) {
        fragmentTransaction.replace(R.id.container_body, fragment);
        if (fragment instanceof FragmentAds || fragment instanceof FragmentSpecial || fragment instanceof FragmentItems) {

        } else
            fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.commit();
//            } else
//                fragmentTransaction.show(fragB);
        //  getSupportActionBar().setTitle(tag);
        // }
    }

    private void shareImage() {
        Log.d("ImageUri", image_str + "  notnull");
        Bitmap loadedImage = getBitmapFromURL(ATPreferences.readString(this, Constants.KEY_IMAGE_URL) + image_str);
        if (loadedImage == null)
            return;
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), loadedImage, "", null);
        if (path == null)
            return;
        Uri screenshotUri = Uri.parse(path);
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Nice Merchant on ApiTap\n" + merchantName
                + "\n" + "http://aiodc.com");
        shareIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
        shareIntent.setType("image/*");
        //shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(shareIntent, "Share Via"));
    }


    // Returns the URI path to the Bitmap displayed in specified ImageView
    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }

    public Uri getLocalBitmapUri(ImageView imageView) {
        // Extract Bitmap from ImageView drawable
        Drawable drawable = imageView.getDrawable();
        Bitmap bmp = null;
        if (drawable instanceof BitmapDrawable) {
            bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        } else {
            return null;
        }
        // Store image to default external storage directory
        Uri bmpUri = null;
        try {
            File file = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS), "share_image_" + System.currentTimeMillis() + ".png");
            file.getParentFile().mkdirs();
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }

    public void showSearchDialog() {
        final ArrayList<String> list = ModelManager.getInstance().getSearchManager().listAddresses;
        final Dialog dialog = new Dialog(mActivity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.setContentView(R.layout.quick_search_test);

        Button submit = dialog.findViewById(R.id.submit);
        Button cancel = dialog.findViewById(R.id.cancel);
        final EditText search = dialog.findViewById(R.id.search);
        Spinner spinner = dialog.findViewById(R.id.spinner);
        ArrayAdapter arrayAdapter = new ArrayAdapter(getApplicationContext(), R.layout.spinner_item, list);
        spinner.setAdapter(arrayAdapter);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchkey = search.getText().toString();
                startActivity(new Intent(mActivity, SearchItemActivity.class).putExtra("key", searchkey));
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        dialog.show();
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        //getDialogView(dialog);
        //viewsVisibility(dialog);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_CAMERA_REQUEST_CODE) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (toolint == 0) {

                    displayView(new FragmentScanner(), Constants.TAG_SCANNER, new Bundle());

                } else {
                    displayView(new FragmentScanner(), Constants.TAG_SCANNER, null);
                }
            } else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_LONG).show();
            }

        }
        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // permission was granted, yay! Do the
                // location-related task you need to do.
                getloc();
                //items.setText(address);
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {

                    //Request location updates:

                }

            } else {

                // permission denied, boo! Disable the
                // functionality that depends on this permission.


            }
            return;
        }
    }

    public void clearfocus() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    public void getfocus() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        startActivity(new Intent(this, HomeActivity.class).putExtra("Drawer", position));
    }

    private boolean getloc() {
        boolean isReturn = false;
        boolean b = Utils.checkLocationPermission(MerchantStoreDetails.this);
        if (!b) {
            Toast.makeText(getApplicationContext(), "Your location is not available, please try again.", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            current_lat = String.valueOf(App.latitude);
            current_long = String.valueOf(App.longitude);
            ModelManager.getInstance().getMerchantManager().getMerchantDistance(this,
                    Operations.makeJsonGetMerchantDistance(this, merchantId, current_lat, current_long), Constants.GET_MERCHANT_DISTANCE_SUCCESS);

//            Utils.baseshowFeedbackMessage(this,rootLayout,current_lat+","+current_long);
        }

       /* if (gps.canGetLocation()) {
            current_lat = String.valueOf(gps.getLatitude());
            current_long = String.valueOf(gps.getLongitude());
            isReturn = true;
        } else {
            gps.showSettingsAlertLocation();
            current_lat = "41.881832";
            current_long = "-87.623177";

        }*/
        return isReturn;
    }

    @Override
    public void onItemClick(View v, int position) {
        Intent intent = new Intent(mActivity, MerchantStoreMap.class);
        intent.putExtra("merchantId", merchantId);
        intent.putExtra("position", position);
        startActivity(intent);
    }

    private void setCategorySpinnerAdapter() {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MerchantStoreDetails.this,
                R.layout.simple_item_blue, merchantCategoryList);
        arrayAdapter.setDropDownViewResource(R.layout.spinner_item_txt);
        sotre_categorySpinner.setAdapter(arrayAdapter);

        sotre_categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0)
                    checkingSpecialorItem(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void checkingSpecialorItem(int i) {
        Bundle bundle = new Bundle();
        ATPreferences.putString(this, Constants.MERCHANT_CATEGORY, merchantCategoryList.get(i));
        displayView(new FragmentItems(), Constants.TAG_ITEMS, bundle);

    }


}

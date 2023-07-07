package com.apitap.views.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.apitap.App;
import com.apitap.R;
import com.apitap.controller.MerchantFavouriteManager;
import com.apitap.controller.ModelManager;
import com.apitap.model.Constants;
import com.apitap.model.Operations;
import com.apitap.model.Utils;
import com.apitap.model.ViewPagerCustomDuration;
import com.apitap.model.bean.AdsDetailWithMerchant;
import com.apitap.model.bean.LocationListBean;
import com.apitap.model.bean.RatingBean;
import com.apitap.model.customclasses.Event;
import com.apitap.model.preferences.ATPreferences;
import com.apitap.model.storeFrontItems.details.RESULTItem;
import com.apitap.views.HomeActivity;
import com.apitap.views.NavigationMenu.FragmentDrawer;
import com.apitap.views.adapters.LocationListAdpater;
import com.apitap.views.adapters.SamplePagerAdapter;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.google.android.material.tabs.TabLayout;
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

import static com.apitap.controller.MerchantManager.merchantCategoryList;
import static com.apitap.views.fragments.home.FragmentHome.setFavouriteMerchantView;

/**
 * Created by Shami on 20/9/2017.
 */

public class FragmentStoreDetails extends BaseFragment implements View.OnClickListener,
        LocationListAdpater.AdapterClick {

    private TextView storeName, textViewReturnReturnPolicy,textViewReturnPrivacyPolicy, showAll, ratingNo, tv_termsAndCondition;
    private ImageView inbox, share, img_main, storeImg;
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
    private LinearLayout linearLayoutHeaderStoreFront;
    private LinearLayout linearLayoutReturnPolicyRoot;
    private LinearLayout linearLayoutPrivacyPolicyRoot;
    private LinearLayout linearLayoutTermsRoot;
    private LinearLayout view_store_detail_header;
    private LinearLayout view_store_tabs;
    private View view_terms;
    private View view_return;
    private View view_privacy;

    private RelativeLayout relativeLayoutSearchBarStoreFront;
    private RelativeLayout linearLayoutTerms;
    private RelativeLayout linearLayoutReturn;
    private RelativeLayout linearLayoutPrivacy;
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
    private ImageView homeTab2;
    LinearLayout tabConatiner;
    DrawerLayout rootLayout;
    boolean isFavorite = false;
    private Spinner sotre_categorySpinner;
    private LinearLayout ll_storeMessages, linearLayoutRating;
    private RESULTItem data;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.store_review_screen, container, false);
        mActivity = getActivity();
        adapterClick = this;
        if (getArguments() != null && getArguments().getString("merchantId") != null) {
            merchantId = getArguments().getString("merchantId");
            Log.d("MerchantID", merchantId + "   s");
        }
        merchantId = ATPreferences.readString(mActivity,Constants.MERCHANT_ID);

        initViews(v);
        getloc();

        return v;
    }

    private void initViews(View v) {
        relativeLayoutSearchBarStoreFront = getActivity().findViewById(R.id.search_storefront);
        linearLayoutReturn = v.findViewById(R.id.linearLayoutPolicy);
        linearLayoutPrivacy = v.findViewById(R.id.linearLayoutPrivacy);
        linearLayoutTerms = v.findViewById(R.id.linearLayoutTerms);
        linearLayoutReturnPolicyRoot = v.findViewById(R.id.linearLayoutReturnPolicy);
        linearLayoutPrivacyPolicyRoot = v.findViewById(R.id.linearLayoutPrivacyRoot);
        linearLayoutTermsRoot = v.findViewById(R.id.linearLayoutTermsRoot);
        view_store_tabs = getActivity().findViewById(R.id.view_store_tabs);
        view_store_detail_header = getActivity().findViewById(R.id.view_store_detail_header);
        view_store_tabs.setVisibility(View.VISIBLE);
        view_store_detail_header.setVisibility(View.VISIBLE);

        view_terms = v.findViewById(R.id.view_terms);
        view_return = v.findViewById(R.id.view_policy);
        view_privacy = v.findViewById(R.id.view_privacy);
        linearLayoutHeaderStoreFront = getActivity().findViewById(R.id.header);

        sotre_categorySpinner = v.findViewById(R.id.category_dropdown);
        tabConatiner = v.findViewById(R.id.tab_container);
        rootLayout = v.findViewById(R.id.drawer_layout);
        noAdsCard = v.findViewById(R.id.noAdsll);
        homeTab2 = v.findViewById(R.id.tab_one_image);
        storeBrowse = v.findViewById(R.id.details_store);
        tabLayout = v.findViewById(R.id.tabs);
        viewPager = v.findViewById(R.id.viewpager);
        iv_back = v.findViewById(R.id.tv_back);
        circleIndicator = v.findViewById(R.id.indicator_default);
        frameLayout = v.findViewById(R.id.container_body);
        frameLayout.setVisibility(View.GONE);
        storeName = v.findViewById(R.id.storeName);
        textViewReturnReturnPolicy = v.findViewById(R.id.storeDetails);
        textViewReturnPrivacyPolicy = v.findViewById(R.id.privacyPolicy);
        showAll = v.findViewById(R.id.showall);
        ratingNo = v.findViewById(R.id.ratingNo);
        ll_storeMessages = v.findViewById(R.id.message_store);
        linearLayoutRating = v.findViewById(R.id.linearLayoutRating);
        tv_termsAndCondition = v.findViewById(R.id.termsConditions);
        inbox = v.findViewById(R.id.messages);
        img_main = v.findViewById(R.id.img_main);
        storeImg = v.findViewById(R.id.adstoreImg);
        noStoreLogo = v.findViewById(R.id.noStoreLogo);
        share = v.findViewById(R.id.share);
        recyclerView = v.findViewById(R.id.recycler);
        ratingBar = v.findViewById(R.id.ratingBar);
        ratingNumbers = v.findViewById(R.id.ratingnumbers);
        smoothProgressBar = v.findViewById(R.id.pocketa);

        scan_tool = v.findViewById(R.id.ll_scan);
        header = v.findViewById(R.id.header);
        msg_tool = v.findViewById(R.id.ll_message);
        search_tool = v.findViewById(R.id.ll_search);
        back_tool = v.findViewById(R.id.iv_back);
        mPocketBar = v.findViewById(R.id.pocket);
        nolocationTxt = v.findViewById(R.id.nolocation);
        nolocationLinear = v.findViewById(R.id.locationlinear);

        ratingBar.setOnClickListener(this);
        linearLayoutReturn.setOnClickListener(this);
        linearLayoutPrivacy.setOnClickListener(this);
        linearLayoutTerms.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        storeName.setOnClickListener(this);
        storeBrowse.setOnClickListener(this);
        linearLayoutRating.setOnClickListener(this);
        //inbox.setOnClickListener(this);
        share.setOnClickListener(this);
        showAll.setOnClickListener(this);
        ll_storeMessages.setOnClickListener(this);
        //  mPocketBar.setVisibility(View.VISIBLE);

        showProgress();
        ModelManager.getInstance().getAddMerchantRating().getMerchantRating(mActivity,
                Operations.GetMerchantRating(mActivity, ATPreferences.readString(mActivity,
                        Constants.KEY_USERID), merchantId));
    }

    private void setData() {
        String terms = "";
        String return_policy = "";
        String privacy_policy = "";
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
                return_policy = Utils.hexToASCII(data.getJsonMember11916());
            if (!data.getJsonMember11917().isEmpty())
                privacy_policy = Utils.hexToASCII(data.getJsonMember11917());
            if (terms.isEmpty() && return_policy.isEmpty())
                tv_termsAndCondition.setText("Currently Not Available");
            else {
                String subjectStringTerms = terms;
                String subjectStringPolicy = return_policy;
                String subjectStringPrivacy= privacy_policy;

                subjectStringTerms = Normalizer.normalize(subjectStringTerms, Normalizer.Form.NFD);
                String resultStringTerms = subjectStringTerms.replaceAll("[^\\x00-\\x7F]", "");

                subjectStringPolicy = Normalizer.normalize(subjectStringPolicy, Normalizer.Form.NFD);
                String resultStringPolicy = subjectStringPolicy.replaceAll("[^\\x00-\\x7F]", "");

                subjectStringPrivacy = Normalizer.normalize(subjectStringPrivacy, Normalizer.Form.NFD);
                String resultStringPrivacy = subjectStringPrivacy.replaceAll("[^\\x00-\\x7F]", "");

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    tv_termsAndCondition.setText(Html.fromHtml(resultStringTerms, Html.FROM_HTML_MODE_LEGACY));
                    textViewReturnReturnPolicy.setText(Html.fromHtml(resultStringPolicy, Html.FROM_HTML_MODE_LEGACY));
                    textViewReturnPrivacyPolicy.setText(Html.fromHtml(resultStringPrivacy, Html.FROM_HTML_MODE_LEGACY));
                } else {
                    tv_termsAndCondition.setText(Html.fromHtml(resultStringTerms));
                    textViewReturnReturnPolicy.setText(Html.fromHtml(resultStringPolicy));
                    textViewReturnPrivacyPolicy.setText(Html.fromHtml(resultStringPrivacy));

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
//            try {
//                String subjectString = Utils.getStringHexaDecimal(data.getJsonMember120157());
//                if (!data.getJsonMember120157().isEmpty())
//                    textViewReturnPolicy.setText(subjectString);
//                else
//                    textViewReturnPolicy.setText("No Description Available Currently");
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
            Picasso.get().load(ATPreferences.readString(mActivity, Constants.KEY_IMAGE_URL) + data.getJsonMember121170()).into(img_main);
            Picasso.get().load(ATPreferences.readString(mActivity, Constants.KEY_IMAGE_URL) + data.getJsonMember121170()).into(storeImg);
           // Picasso.get().load(ATPreferences.readString(mActivity, Constants.KEY_IMAGE_URL) + data.getImage()).into(noStoreLogo);
            Picasso.get().load(ATPreferences.readString(mActivity, Constants.KEY_IMAGE_URL) + data.getJsonMember121170()).fit().centerInside().into(noStoreLogo);

            image_str = data.getJsonMember121170();
            // uri = getLocalBitmapUri(img_main);
            setCategorySpinnerAdapter();
        }
        if (MerchantFavouriteManager.isCurrentMerchantFav)
            isFavorite = true;
        setFavouriteMerchantView(storeName);

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
                //     hideProgress();
                Utils.setRecyclerNearByAdapter(mActivity);
                break;

            case Constants.REMOVE_MERCHANT_FAVORITES:
                hideProgress();
                //smoothProgressBar.setVisibility(View.GONE);
                //smoothProgressBar.progressiveStop();
                //favourite.setBackgroundResource(R.drawable.ic_icon_fav_gray);
                MerchantFavouriteManager.isCurrentMerchantFav = false;
                isFavorite = false;
                setFavouriteMerchantView(storeName);
                break;

            case Constants.GET_RATING_SUCCESS:
                List<RatingBean.RESULT_> ratingBeen = ModelManager.getInstance().getAddMerchantRating().
                        ratingBean.getRESULT().get(0).getRESULT();
                ratingNo.setText("(" + ratingBeen.size() + ")");
                if (ratingBeen.size() == 0) {
                    ratingNo.setText("No Ratings");
                }

                break;

            case Constants.GET_MERCHANT_DISTANCE_SUCCESS:
                hideProgress();
                List<LocationListBean.RESULT_> arrayList = new ArrayList<LocationListBean.RESULT_>();
                arrayList = ModelManager.getInstance().getMerchantManager().locationListBean.getRESULT().get(0).getRESULT();
                Log.d("listContains", arrayList.get(0).get478() + "");
                if (arrayList.get(0).get478() == null) {
                    nolocationTxt.setVisibility(View.VISIBLE);
                    nolocationLinear.setVisibility(View.GONE);
                } else {
                    locationListAdpater = new LocationListAdpater(adapterClick, mActivity, arrayList, inbox);
                    recyclerView.setHasFixedSize(true);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(locationListAdpater);
                }

                break;

            case Constants.ADD_MERCHANT_FAVORITE_SUCCESS:
                hideProgress();
                //    if (isFavorite.equals("1"))
                //smoothProgressBar.setVisibility(View.GONE);
                //smoothProgressBar.progressiveStop();
                //  favourite.setBackgroundResource(R.drawable.ic_icon_fav);
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
                        // favourite.setBackgroundResource(R.drawable.ic_icon_fav);
                    }

                } catch (Exception e) {
                }
                break;

            case -1:
                hideProgress();
                baseshowFeedbackMessage(getActivity(), rootLayout, "Something went wrong");
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

                Bundle bundle = new Bundle();
                bundle.putString("merchantId", merchantId);
                bundle.putString("storeName", storeName.getText().toString());
                bundle.putString("storeImage", image_str);
                bundle.putString("storeRateString", ratingNo.getText().toString());
                ((HomeActivity) getActivity()).displayView(new FragmentStoreRate(), Constants.TAG_STORE_RATE, bundle);
                break;
            case R.id.linearLayoutPolicy:
                linearLayoutReturnPolicyRoot.setVisibility(View.VISIBLE);
                view_return.setVisibility(View.VISIBLE);
                view_terms.setVisibility(View.INVISIBLE);
                view_privacy.setVisibility(View.INVISIBLE);
                linearLayoutTermsRoot.setVisibility(View.GONE);
                linearLayoutPrivacyPolicyRoot.setVisibility(View.GONE);
                break;
            case R.id.linearLayoutTerms:
                linearLayoutReturnPolicyRoot.setVisibility(View.GONE);
                linearLayoutPrivacyPolicyRoot.setVisibility(View.GONE);
                view_return.setVisibility(View.INVISIBLE);
                view_privacy.setVisibility(View.INVISIBLE);
                view_terms.setVisibility(View.VISIBLE);
                linearLayoutTermsRoot.setVisibility(View.VISIBLE);
                break;
                case R.id.linearLayoutPrivacy:
                linearLayoutReturnPolicyRoot.setVisibility(View.GONE);
                linearLayoutPrivacyPolicyRoot.setVisibility(View.VISIBLE);
                view_return.setVisibility(View.INVISIBLE);
                view_privacy.setVisibility(View.VISIBLE);
                view_terms.setVisibility(View.INVISIBLE);
                linearLayoutTermsRoot.setVisibility(View.GONE);
                break;
            case R.id.iv_back:
            case R.id.tv_back:
                linearLayoutHeaderStoreFront.setVisibility(View.VISIBLE);
                relativeLayoutSearchBarStoreFront.setVisibility(View.VISIBLE);
                onBackPress();
                break;

            case R.id.storeName:
                // smoothProgressBar.setVisibility(View.VISIBLE);
                // smoothProgressBar.progressiveStart();
                showProgress();
                if (!isFavorite) {
                    ModelManager.getInstance().getAddMerchantFavorite().addMerchantToFavorite(mActivity,
                            Operations.makeJsonMerchantAddToFavorite(mActivity, merchantId));
                } else
                    ModelManager.getInstance().getMerchantFavouriteManager().removeFavourite(mActivity,
                            Operations.makeJsonRemoveMerchantFavourite(mActivity, merchantId));
                break;
            case R.id.share:
                shareImage();
                break;
            case R.id.details_store:
                Bundle bundle1 = new Bundle();
                bundle1.putString(Constants.MERCHANT_ID, merchantId);
                ATPreferences.putBoolean(mActivity, Constants.HEADER_STORE, true);
                ATPreferences.putString(mActivity, Constants.MERCHANT_ID, merchantId);
                startActivity(new Intent(mActivity, HomeActivity.class)
                        .putExtras(bundle1));
                mActivity.finish();
                //  displayView(new FragmentStoreFront(), Constants.TAG_STORESFRONTPAGE, bundle1);

                break;


            case R.id.showall:
                Bundle bundle2 = new Bundle();
                bundle2.putString("merchantId", merchantId);
                bundle2.putInt("position", 99);
                ((HomeActivity) getActivity()).displayView(new FragmentStoreMap(), Constants.TAG_STORE_MAP, bundle2);
                break;

        }
    }

    private void shareImage() {
        Log.d("ImageUri", image_str + "  notnull");
        Bitmap loadedImage = getBitmapFromURL(ATPreferences.readString(mActivity, Constants.KEY_IMAGE_URL) + image_str);
        if (loadedImage == null)
            return;
        String path = MediaStore.Images.Media.insertImage(mActivity.getContentResolver(), loadedImage, "", null);
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


    private boolean getloc() {
        boolean isReturn = false;
        boolean b = Utils.checkLocationPermission(mActivity);
        if (!b) {
            Toast.makeText(mActivity, "Your location is not available, please try again.", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            showProgress();
            current_lat = String.valueOf(App.latitude);
            current_long = String.valueOf(App.longitude);
            ModelManager.getInstance().getMerchantManager().getMerchantDistance(mActivity,
                    Operations.makeJsonGetMerchantDistance(mActivity, merchantId, current_lat, current_long), Constants.GET_MERCHANT_DISTANCE_SUCCESS);

        }
        return isReturn;
    }

    @Override
    public void onItemClick(View v, int position) {
        Bundle bundle2 = new Bundle();
        bundle2.putString("merchantId", merchantId);
        bundle2.putInt("position", 99);
        ((HomeActivity) getActivity()).displayView(new FragmentStoreMap(), Constants.TAG_STORE_MAP, bundle2);
    }

    private void setCategorySpinnerAdapter() {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mActivity,
                R.layout.simple_item_blue, merchantCategoryList);
        arrayAdapter.setDropDownViewResource(R.layout.spinner_item_txt);
        sotre_categorySpinner.setAdapter(arrayAdapter);

        sotre_categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
      //  view_store_detail_header.setVisibility(View.GONE);
       // view_store_tabs.setVisibility(View.GONE);

    }
}

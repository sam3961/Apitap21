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
import android.webkit.WebView;
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
import androidx.fragment.app.FragmentActivity;
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
import com.apitap.views.adapters.StoreAboutImagesAdapter;
import com.apitap.views.fragments.home.FragmentHome;
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

public class FragmentStoreAbout extends BaseFragment implements StoreAboutImagesAdapter.onPhotoClick {

    private TextView storeAbout;
    private RESULTItem data;
    private Activity mActivity;
    private LinearLayout view_store_detail_header;
    private RecyclerView recyclerViewStoreAbout;
    private LinearLayout view_store_tabs;
    private String merchantId = "";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_store_about, container, false);
        mActivity = getActivity();
        if (getArguments() != null && getArguments().getString("merchantId") != null) {
            merchantId = getArguments().getString("merchantId");
            Log.d("MerchantID", merchantId + "   s");
        }

        merchantId = ATPreferences.readString(mActivity, Constants.MERCHANT_ID);

        initViews(v);
        fetchStoreImages();

        return v;
    }

    private void fetchStoreImages() {
        ModelManager.getInstance().getMerchantStoresManager().getStoreImages(getActivity(),
                Operations.makeJsonGetStoreImages(getActivity(), merchantId));
    }

    private void initViews(View v) {
        storeAbout = v.findViewById(R.id.storeDetails);
        view_store_tabs = getActivity().findViewById(R.id.view_store_tabs);
        view_store_detail_header = getActivity().findViewById(R.id.view_store_detail_header);
        recyclerViewStoreAbout = v.findViewById(R.id.recyclerViewStoreImages);
        view_store_tabs.setVisibility(View.VISIBLE);
        view_store_detail_header.setVisibility(View.VISIBLE);

        view_store_detail_header.setVisibility(View.VISIBLE);
    }

    private void setData() {
        String terms = "";
        String policy = "";
        if (ModelManager.getInstance().getMerchantStoresManager().storeDetailsModel.getRESULT().size() > 0) {
            data = ModelManager.getInstance().getMerchantStoresManager().storeDetailsModel.getRESULT().get(0);
            try {
                String subjectString = Utils.getStringHexaDecimal(data.getJsonMember120157());
                if (!data.getJsonMember120157().isEmpty()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        storeAbout.setText(Html.fromHtml(subjectString, Html.FROM_HTML_MODE_COMPACT));
                    } else {
                        storeAbout.setText(Html.fromHtml(subjectString));
                    }
                } else {
                    storeAbout.setText("No description available.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
            case Constants.STORE_ABOUT_IMAGES:
                setAdapter();
                break;
            case -1:
                break;
        }
    }

    private void setAdapter() {
        StoreAboutImagesAdapter storeAboutImagesAdapter = new StoreAboutImagesAdapter(getActivity(),
                ModelManager.getInstance().getMerchantStoresManager().storeAboutResponse.getRESULT().get(0).getRESULT(), this);
        recyclerViewStoreAbout.setAdapter(storeAboutImagesAdapter);
    }


    public void onResume() {
        super.onResume();
        setData();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //  view_store_detail_header.setVisibility(View.GONE);
        //  view_store_tabs.setVisibility(View.GONE);
    }

    @Override
    public void onPhotoClick(int positon) {
        String image = ATPreferences.readString(getActivity(), Constants.KEY_IMAGE_URL) +
                ModelManager.getInstance().getMerchantStoresManager().storeAboutResponse.getRESULT().get(0).getRESULT().get(positon).getJsonMember121170();
        Bundle extras = new Bundle();
        extras.putString("imagebitmap", image);
        extras.putString("video", "");
        extras.putString("previousClass", "");
        extras.putString("merchant", "");
        extras.putString("adName", "");
        extras.putString("desc", "");
        extras.putString("id", "");
        extras.putString("ad_id", "");
        extras.putInt("adpos", 0);
        extras.putLong("vidpos", 0);


        ((HomeActivity) getActivity()).displayView(new FragmentFullScreenImage(), Constants.TAG_FULL_SCREEN, extras);
    }
}

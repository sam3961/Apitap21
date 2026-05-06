package com.apitap.app.views.fragments;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.apitap.app.R;
import com.apitap.app.controller.ModelManager;
import com.apitap.app.model.Constants;
import com.apitap.app.model.Operations;
import com.apitap.app.model.Utils;
import com.apitap.app.model.customclasses.Event;
import com.apitap.app.model.preferences.ATPreferences;
import com.apitap.app.model.storeFrontItems.details.RESULTItem;
import com.apitap.app.views.HomeActivity;
import com.apitap.app.views.adapters.StoreAboutImagesAdapter;

import org.apache.commons.lang.StringEscapeUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

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
        if (ModelManager.getInstance().getMerchantStoresManager().storeDetailsModel.getRESULT().size() > 0) {
            data = ModelManager.getInstance().getMerchantStoresManager().storeDetailsModel.getRESULT().get(0);
            try {
                String subjectString = Utils.getStringHexaDecimal(data.getJsonMember120157());
                if (!data.getJsonMember120157().isEmpty()) {
                    storeAbout.setText(getPlainTextFromHtml(subjectString));
                } else {
                    storeAbout.setText("No description available.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String getPlainTextFromHtml(String htmlText) {
        if (htmlText == null || htmlText.trim().isEmpty()) {
            return "";
        }

        String unescapedHtml = StringEscapeUtils.unescapeHtml(htmlText);
        Spanned parsedText;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            parsedText = Html.fromHtml(unescapedHtml, Html.FROM_HTML_MODE_LEGACY);
        } else {
            parsedText = Html.fromHtml(unescapedHtml);
        }

        return parsedText.toString().replace('\u00A0', ' ').trim();
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

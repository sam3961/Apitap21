package com.apitap.views.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apitap.R;
import com.apitap.controller.MerchantFavouriteManager;
import com.apitap.controller.ModelManager;
import com.apitap.model.Client;
import com.apitap.model.Constants;
import com.apitap.model.Operations;
import com.apitap.model.customclasses.Event;
import com.apitap.model.customclasses.KeyboardHeightObserver;
import com.apitap.model.customclasses.KeyboardHeightProvider;
import com.apitap.model.preferences.ATPreferences;
import com.apitap.views.HomeActivity;
import com.apitap.views.fragments.home.FragmentHome;
import com.apitap.views.fragments.storefront.FragmentStoreFront;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import im.delight.android.webview.AdvancedWebView;

public class FragmentCheckout extends BaseFragment implements View.OnClickListener, KeyboardHeightObserver {


    private WebView webView;
    private LinearLayout backll;
    private LinearLayout linearLayoutHeader;
    String url = "";
    private Context mContext;
    private boolean isCurrentMerchantFav;
    private String companyName;
    private TextView textViewStoreName;
    private ImageView imageViewStoreHeader;

    private String merchantId, companyLogo, deliveryId = "", cartId = "";
    private Button buttonStoreInfo;
  //  private KeyboardHeightProvider keyboardHeightProvider;
    private LinearLayout keyBoardView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cart_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContext = getActivity();
      //  keyboardHeightProvider = new KeyboardHeightProvider(getActivity());
        View activitylayout = view.findViewById(R.id.activitylayout);
//        activitylayout.post(new Runnable() {
//            public void run() {
//                keyboardHeightProvider.start();
//            }
//        });
        backll = (LinearLayout) view.findViewById(R.id.back_ll);
        linearLayoutHeader = (LinearLayout) view.findViewById(R.id.header);
        keyBoardView = view.findViewById(R.id.keyboard);
        Bundle bundle = getArguments();
        companyName = bundle.getString("companyName");
        merchantId = bundle.getString("merchantId");
        companyLogo = bundle.getString("compnyimg");
        deliveryId = bundle.getString("deliveryId");
        cartId = bundle.getString("cartId");

        if (!ATPreferences.readBoolean(getActivity(), Constants.HEADER_STORE)) {
            linearLayoutHeader.setVisibility(View.VISIBLE);
        }


        webView = view.findViewById(R.id.webView);
        imageViewStoreHeader = view.findViewById(R.id.adstoreImg);
        textViewStoreName = view.findViewById(R.id.storeName);
        buttonStoreInfo = view.findViewById(R.id.details_store);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        //webSettings.setLoadWithOverviewMode(true);
//        webSettings.setUseWideViewPort(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setSupportZoom(true);
        webSettings.setDefaultTextEncodingName("utf-8");


        //webView.getSettings().setLoadWithOverviewMode(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        textViewStoreName.setText(companyName);
        Picasso.get().load(ATPreferences.readString(getActivity(), Constants.KEY_IMAGE_URL) +
                companyLogo)
                .placeholder(R.drawable.loading).into(imageViewStoreHeader);


//        webView.getSettings().setUseWideViewPort(true);
//        webView.getSettings().setLoadWithOverviewMode(true);
//        webView.getSettings().setBuiltInZoomControls(true);
        String id = ATPreferences.readString(getActivity(), Constants.KEY_USERID);
        String token = ATPreferences.readString(getActivity(), Constants.TOKEN);
      /*  url = Client.BASE_URL_CART_QA + id + "&scId=" + bundle.getString("cartId") + "&merId=" +
                bundle.getString("merchantId");
*/
        if (deliveryId.isEmpty())
            url = Client.BASE_URL_CART + id +
                    "&scId=" + cartId +
                    "&merId=" + merchantId +
                    "&t=" + token;
        else
            url = Client.BASE_URL_CART + id +
                    "&scId=" + cartId +
                    "&merId=" + merchantId +
                    "&delId=" + deliveryId +
                    "&t=" + token;

        showProgress();
        ModelManager.getInstance().getMerchantFavouriteManager().getFavourites(getActivity(),
                Operations.makeJsonGetMerchantFavourite(getActivity()));


        backll.setOnClickListener(this);
        buttonStoreInfo.setOnClickListener(this);
        textViewStoreName.setOnClickListener(this);

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                try {
                    if (url.equalsIgnoreCase(Constants.SHOPPING_SUCCESS_URL)) {
                        webView.stopLoading();
                        ModelManager.getInstance().getShoppingManager().updateGPSCoordinatesByCartId(getActivity()
                                , Operations.updateGPSCoordinatesByCartId(getActivity(), cartId));
                        successPurchaseDialog();
                    } else if (url.equalsIgnoreCase(Constants.SHOPPING_FAILURE_URL)) {
                        webView.stopLoading();
                        failureTransactionDialog();
                    } else {
                        view.loadUrl(url);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return super.shouldOverrideUrlLoading(view, url);

            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                showProgress();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                hideProgress();

            }

        });

        try {
            webView.loadUrl(url);
            webView.setVerticalScrollBarEnabled(true);
            webView.setHorizontalScrollBarEnabled(true);
            Log.d("LoadUrls", url);
        } catch (Exception e) {
            e.printStackTrace();
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

    @SuppressLint("NewApi")
    @Override
    public void onResume() {
        super.onResume();
        webView.onResume();
        // ...
    }

    @SuppressLint("NewApi")
    @Override
    public void onPause() {
        webView.onPause();
        // ...
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Subscribe
    public void onEvent(final Event event) {
        switch (event.getKey()) {
            case Constants.GET_MERCHANT_FAVORITES:
                //ArrayList<String> merchantFavList = ModelManager.getInstance().getMerchantFavouriteManager().mernchantfavlist;
                ArrayList<String> merchantFavList = ModelManager.getInstance().getMerchantFavouriteManager().mernchantfavlist;
                if (merchantFavList.contains(companyName)) {
                    isCurrentMerchantFav = true;
                    setFavouriteMerchantView(textViewStoreName);

                }
                break;

            case Constants.ADD_MERCHANT_FAVORITE_SUCCESS:
                //    if (isFavorite.equals("1"))
                hideProgress();
                isCurrentMerchantFav = true;
                setFavouriteMerchantView(textViewStoreName);
                break;
            case Constants.REMOVE_MERCHANT_FAVORITES:
                hideProgress();
                isCurrentMerchantFav = false;
                setFavouriteMerchantView(textViewStoreName);
                break;

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_ll:
                getActivity().onBackPressed();
                break;

            case R.id.storeName:
                showProgress();
                if (!isCurrentMerchantFav) {
                    ModelManager.getInstance().getAddMerchantFavorite().addMerchantToFavorite(getActivity(),
                            Operations.makeJsonMerchantAddToFavorite(getActivity(), merchantId));
                } else
                    ModelManager.getInstance().getMerchantFavouriteManager().removeFavourite(getActivity(),
                            Operations.makeJsonRemoveMerchantFavourite(getActivity(), merchantId));
                break;

            case R.id.details_store:
                // ATPreferences.putBoolean(getActivity(), Constants.HEADER_STORE, true);
                //ATPreferences.putString(getActivity(), Constants.MERCHANT_ID, merchantId);
                //startActivity(new Intent(getActivity(), HomeActivity.class));

                Bundle b = new Bundle();
                b.putBoolean(Constants.HEADER_STORE, true);
                b.putString(Constants.MERCHANT_ID, merchantId);

                ATPreferences.putBoolean(getActivity(), Constants.HEADER_STORE, true);
                ATPreferences.putString(getActivity(), Constants.MERCHANT_ID, merchantId);
                ATPreferences.putString(getActivity(), Constants.MERCHANT_CATEGORY, "");

                ((HomeActivity) getActivity()).displayView(new FragmentStoreFront(), Constants.TAG_STORESFRONTPAGE, b);


                break;


        }

    }

    public void setFavouriteMerchantView(TextView tvStoreName) {
        if (isCurrentMerchantFav) {
            tvStoreName.setBackground(getActivity().getResources().getDrawable(R.drawable.back_round_green_border));
            tvStoreName.setTextColor(getActivity().getResources().getColor(R.color.colorGreen));
            tvStoreName.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_icon_fav_red, 0, 0, 0);
        } else {
            tvStoreName.setBackground(getActivity().getResources().getDrawable(R.drawable.back_round_blue_border));
            tvStoreName.setTextColor(getActivity().getResources().getColor(R.color.colorBlue));
            tvStoreName.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_icon_fav_gray, 0, 0, 0);
        }
    }


    @Override
    public void onKeyboardHeightChanged(int height, int orientation) {
        if (height > 0) {
            keyBoardView.setVisibility(View.VISIBLE);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) keyBoardView.getLayoutParams();
            params.height = height;
            keyBoardView.setLayoutParams(params);
        } else
            keyBoardView.setVisibility(View.GONE);

    }

    public void successPurchaseDialog() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.gravity = Gravity.CENTER;
        dialog.setContentView(R.layout.dialog_order_sucess);
        Button done = (Button) dialog.findViewById(R.id.btn_ok);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                getFragmentManager().popBackStack();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

    }

    public void failureTransactionDialog() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.gravity = Gravity.CENTER;
        dialog.setContentView(R.layout.dialog_order_failure);
        Button done = (Button) dialog.findViewById(R.id.btn_ok);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                getFragmentManager().popBackStack();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

    }

}


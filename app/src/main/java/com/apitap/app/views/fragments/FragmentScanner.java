package com.apitap.app.views.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;

import com.apitap.app.R;
import com.apitap.app.model.Constants;
import com.apitap.app.model.Utils;
import com.apitap.app.model.customclasses.FlashlightProvider;
import com.apitap.app.model.preferences.ATPreferences;
import com.apitap.app.views.HomeActivity;
import com.apitap.app.views.fragments.itemDetails.FragmentItemDetails;
import com.apitap.app.views.fragments.storefront.FragmentStoreFront;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;

import java.util.List;


/**
 * Created by ashok-kumar on 9/6/16.
 */

public class FragmentScanner extends BaseFragment implements View.OnClickListener {
    private Activity mActivity;
    private RelativeLayout relativeLayout1, relativeLayout2;
    private LinearLayout cancel_ll, flash_ll, rootLayout;
    private ImageView flash_img;
    private int state = 0;
    private CodeScannerView scannerView;
    private FlashlightProvider flashlightProvider;
    private CodeScanner mCodeScanner;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_scanner, container, false);
        mActivity = getActivity();
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //flashlightProvider = new FlashlightProvider(getActivity());
        initViews(view);
        clickListeners();
    }


    private void initViews(View v) {
        cancel_ll = v.findViewById(R.id.cancel_ll);
        relativeLayout1 = v.findViewById(R.id.rl1);
        relativeLayout2 = v.findViewById(R.id.rl2);
        flash_ll = v.findViewById(R.id.flash_ll);
        rootLayout = v.findViewById(R.id.rootLayout);
        flash_img = v.findViewById(R.id.flash_light);
        scannerView = v.findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(getActivity(), scannerView);

        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                requireActivity().runOnUiThread(() -> handleScannedQr(result.getText()));
            }
        });
        mCodeScanner.startPreview();
        tabContainer2Visible();

    }

    private void handleScannedQr(String qrText) {
        Log.d("QR_SCAN", "Scanned: " + qrText);

        if (qrText == null || qrText.trim().isEmpty()) {
            Log.d("QR_SCAN", "Empty QR result");
            return;
        }

        try {
            Uri uri = Uri.parse(qrText);

            String scheme = uri.getScheme();      // http / https
            String host = uri.getHost();          // www.apitap.com / aiodctesting.org
            int port = uri.getPort();             // 8443 / 5443 / -1
            List<String> segments = uri.getPathSegments();

            Log.d("QR_SCAN", "scheme=" + scheme + ", host=" + host + ", port=" + port + ", segments=" + segments);

            if (scheme == null || host == null || segments == null || segments.isEmpty()) {
                Log.d("QR_SCAN", "Invalid URL format");
                return;
            }

            boolean isSupportedHost =
                    "www.apitap.com".equalsIgnoreCase(host) ||
                            "aiodctesting.org".equalsIgnoreCase(host);

            if (!isSupportedHost) {
                Log.d("QR_SCAN", "Unsupported host: " + host);
                return;
            }

            String firstSegment = segments.get(0);

            if ("adv".equalsIgnoreCase(firstSegment)) {
                if (segments.size() >= 2) {
                    navigateToItemDetail(segments.get(1), "23");
                } else {
                    Log.d("QR_SCAN", "Missing adv id");
                }
            } else if ("products".equalsIgnoreCase(firstSegment)) {
                if (segments.size() >= 2) {
                    navigateToItemDetail(segments.get(1), "21");
                } else {
                    Log.d("QR_SCAN", "Missing product id");
                }
            } else {
                if (segments.size() >= 2) {
                    navigateToStoreFront(Utils.hexToASCII(segments.get(1)));
                } else if (segments.size() >= 1) {
                    navigateToStoreFront(Utils.hexToASCII(segments.get(0)));
                } else {
                    Log.d("QR_SCAN", "Missing merchant id");
                }
            }

        } catch (Exception e) {
            Log.e("QR_SCAN", "Failed to parse QR", e);
        }
    }

    private void clickListeners() {
        cancel_ll.setOnClickListener(this);
        flash_ll.setOnClickListener(new  FlashClickListener());
    }

    @Override
    public void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    public void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.flash_ll:
                if (state == 0) {
                    state = 1;
                    flash_img.setImageResource(R.drawable.flash_off);

                    //  relativeLayout1.setVisibility(View.GONE);
                    //relativeLayout2.setVisibility(View.VISIBLE);


                } else if (state == 1) {
                    state = 0;
                    flash_img.setImageResource(R.drawable.flash_on);
                    // relativeLayout1.setVisibility(View.VISIBLE);
                    //relativeLayout2.setVisibility(View.GONE);
                    reloadFragment();
                }

                break;
            case R.id.cancel_ll:
               // flashlightProvider.turnFlashlightOff();
                startActivity(new Intent(getActivity(), HomeActivity.class));
                break;
        }
    }

    public final class FlashClickListener implements View.OnClickListener {
        @Override
        public void onClick(final View view) {
            final CodeScanner scanner = mCodeScanner;
            if (scanner == null || !scanner.isFlashSupportedOrUnknown()) {
                return;
            }
            final boolean enabled = !scanner.isFlashEnabled();
            scanner.setFlashEnabled(enabled);
            mCodeScanner.setFlashEnabled(enabled);
        }
    }


    public void navigateToItemDetail(final String id, String type) {
        Bundle bundle = new Bundle();
        bundle.putString("productId", Utils.getElevenDigitId(id));
        bundle.putString("productType", type);
        FragmentItemDetails fragment = new FragmentItemDetails();
        fragment.setArguments(bundle);
        ((HomeActivity) getActivity()).displayView(fragment, Constants.TAG_DETAILSPAGE, bundle);
    }

    public void tabContainer2Visible() {
        HomeActivity.tabContainer2.setVisibility(View.GONE);
        HomeActivity.tabContainer2.setVisibility(View.GONE);
    }

    public void tabContainer1Visible() {
        HomeActivity.tabContainer2.setVisibility(View.GONE);
        HomeActivity.tabContainer1.setVisibility(View.VISIBLE);
    }

    private void navigateToStoreFront(String merchantID) {
        Bundle b = new Bundle();
        b.putBoolean(Constants.HEADER_STORE, true);
        b.putString(Constants.MERCHANT_ID, merchantID);

        ATPreferences.putBoolean(getActivity(), Constants.HEADER_STORE, true);
        ATPreferences.putString(getActivity(), Constants.MERCHANT_ID, merchantID);
        ATPreferences.putString(getActivity(), Constants.MERCHANT_CATEGORY, "");

        ((HomeActivity) getActivity()).displayView(new FragmentStoreFront(), Constants.TAG_STORESFRONTPAGE, b);

        storeFrontTabsView();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void reloadFragment() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
    }

}

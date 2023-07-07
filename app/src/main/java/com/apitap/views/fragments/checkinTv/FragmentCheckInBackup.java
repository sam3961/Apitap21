package com.apitap.views.fragments.checkinTv;


import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.apitap.R;
import com.apitap.model.Constants;
import com.apitap.model.Utils;
import com.apitap.model.preferences.ATPreferences;
import com.apitap.model.syncPlayer.FileReceiver;
import com.apitap.model.syncPlayer.Host;
import com.apitap.model.syncPlayer.NsdClient;
import com.apitap.views.HomeActivity;
import com.apitap.views.fragments.BaseFragment;
import com.apitap.views.fragments.checkinTv.FragmentStreamDetail;
import com.apitap.views.fragments.checkinTv.adapter.AdapterGameDeals;
import com.apitap.views.fragments.checkinTv.adapter.AdapterTvList;

import java.io.File;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentCheckInBackup extends BaseFragment implements View.OnClickListener, AdapterTvList.ClickListener {

    private View rootView;
    private RecyclerView recyclerViewGameDeals;
    private RecyclerView receiverViewTVList;
    private ImageView imageViewBack;
    private TextView textViewTitle, textViewStoreName;
    private LinearLayout linearLayoutHeaderCheckin;
    private LinearLayout linearLayoutStoreReservation;
    private RelativeLayout relativeLayoutSearchBarStoreFront;
    private LinearLayout linearLayoutStoreDetailHeader;
    private LinearLayout linearLayoutHeaderStoreFront;
    private LinearLayout linearLayoutHeaderCategory;
    private NsdClient nsdClient;
    private AdapterTvList adapterTvList;
    private FileReceiver fileReceiver;
    public String password = "123456";
    private Host host;
    private Dialog dialogConnectTv;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_checkin, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        dialogConnectTv = Utils.createLoadingDialog(requireContext(),false);
        initViews();
        clickListeners();
        setData();

        setTvListAdapter();
        setGameDealsAdapter();

        nsdClient = new NsdClient(requireContext(), adapterTvList);
        nsdClient.discoverServices();
    }


    private void setTvListAdapter() {
//        adapterTvList = new AdapterTvList(this,
//                new ArrayList(), this);
        receiverViewTVList.setAdapter(adapterTvList);
    }

    private void setGameDealsAdapter() {
        AdapterGameDeals adapterGameDeals = new AdapterGameDeals(requireContext(),
                new ArrayList());
        recyclerViewGameDeals.setAdapter(adapterGameDeals);
    }


    private void initViews() {
        recyclerViewGameDeals = rootView.findViewById(R.id.recyclerViewGameDeals);
        receiverViewTVList = rootView.findViewById(R.id.receiverViewTVList);
        imageViewBack = rootView.findViewById(R.id.imageViewBack);
        textViewTitle = rootView.findViewById(R.id.textViewTitle);
        textViewStoreName = rootView.findViewById(R.id.textViewMerchantName);
        linearLayoutHeaderCheckin = getActivity().findViewById(R.id.view_checkin);
        linearLayoutStoreReservation = getActivity().findViewById(R.id.view_store_reservation);
        relativeLayoutSearchBarStoreFront = getActivity().findViewById(R.id.search_storefront);
        linearLayoutStoreDetailHeader = getActivity().findViewById(R.id.view_store_detail_header);
        linearLayoutHeaderStoreFront = getActivity().findViewById(R.id.header);
        linearLayoutHeaderCategory = getActivity().findViewById(R.id.header_browse_category);

        linearLayoutStoreDetailHeader.setVisibility(View.VISIBLE);
        relativeLayoutSearchBarStoreFront.setVisibility(View.GONE);
        linearLayoutHeaderCheckin.setVisibility(View.GONE);
        linearLayoutHeaderStoreFront.setVisibility(View.GONE);
        linearLayoutHeaderCategory.setVisibility(View.GONE);
    }

    private void setData() {
        textViewStoreName.setText("Welcome to\n" + ATPreferences.readString(getActivity(), Constants.STORE_NAME));
        textViewTitle.setText(getArguments().getString(Constants.HEADER_TITLE));
    }

    private void clickListeners() {
        imageViewBack.setOnClickListener(this);
    }


    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageViewBack:
                onBackPress();
                break;
        }
    }

/*
    @Subscribe
    public void onEvent(final Event event) {
        switch (event.getKey()) {
            case Constants.REMOVE_MERCHANT_FAVORITES:

                hideProgress();
                MerchantFavouriteManager.isCurrentMerchantFav = false;
                FragmentHome.setFavouriteMerchantView(textViewHeaderStoreName);
                FragmentHome.setFavouriteMerchantView(textViewStoreNameDetails);

                break;

            case Constants.ADD_MERCHANT_FAVORITE_SUCCESS:

                hideProgress();
                MerchantFavouriteManager.isCurrentMerchantFav = true;
                FragmentHome.setFavouriteMerchantView(textViewHeaderStoreName);
                FragmentHome.setFavouriteMerchantView(textViewStoreNameDetails);

                break;
        }
    }
*/

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        dialogConnectTv.dismiss();
        linearLayoutHeaderCheckin.setVisibility(View.VISIBLE);
        relativeLayoutSearchBarStoreFront.setVisibility(View.VISIBLE);
        linearLayoutStoreDetailHeader.setVisibility(View.GONE);
        linearLayoutHeaderStoreFront.setVisibility(View.VISIBLE);
        linearLayoutHeaderCategory.setVisibility(View.VISIBLE);
        nsdClient.stopDiscovery();
    }

/*
    @Override
    public void onClick(Host host) {
        this.host = host;
        fileReceiver = new FileReceiver(host.hostAddress,3078,
                this);
        dialogConnectTv.show();
    }
*/

    public void setProgress(int i, long l, long l1) {
    //showProgress();
    }

    public void onFinishDownload(String filepath, File file) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialogConnectTv.dismiss();
                Bundle bundle = new Bundle();
                bundle.putString(Constants.HEADER_TITLE, getArguments().getString(Constants.HEADER_TITLE));
                bundle.putSerializable(Constants.TV_HOST_ADDRESS, host.hostAddress);
                bundle.putString(Constants.TV_HOST_NAME, host.hostName);
                bundle.putString(getString(R.string.mediaSelectPathExtra), filepath);
                bundle.putSerializable(getString(R.string.mediaSelectFileExtra), file);
                ((HomeActivity) requireContext()).displayView(new FragmentStreamDetail(), Constants.TAG_STREAM_DIRECTORY_DETAIL, bundle);

            }
        });

    }

    public void onFailure(String s) {
        getActivity().runOnUiThread(() -> {
            baseshowFeedbackMessage(requireActivity(),rootView,s);
            dialogConnectTv.dismiss();
        });
    }


    @Override
    public void onClick(int host,String mediaName,Long mediaDuration) {

    }
}

package com.apitap.views.fragments.home;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.apitap.R;
import com.apitap.controller.MerchantFavouriteManager;
import com.apitap.controller.ModelManager;
import com.apitap.model.Constants;
import com.apitap.model.Operations;
import com.apitap.model.Utils;
import com.apitap.model.customclasses.Event;
import com.apitap.model.home.address.HomeAddressResponse;
import com.apitap.model.home.ads.HomeAdsResponse;
import com.apitap.model.home.specials.HomeSpecialsResponse;
import com.apitap.model.home.stores.HomeStoresResponse;
import com.apitap.model.preferences.ATPreferences;
import com.apitap.model.storeCategories.StoreCategoryResponse;
import com.apitap.model.storeFrontItems.ads.AdsData;
import com.apitap.views.HomeActivity;
import com.apitap.views.fragments.home.adapter.AdapterHomeSpecials;
import com.apitap.views.fragments.home.adapter.AdapterHomeStores;
import com.apitap.views.fragments.home.adapter.AdapterStoreCategories;
import com.apitap.views.fragments.storefront.adapter.AdapterStoreAdsPager;
import com.apitap.views.fragments.BaseFragment;
import com.apitap.views.fragments.adDetails.FragmentAdDetail;
import com.apitap.views.fragments.itemDetails.FragmentItemDetails;
import com.apitap.views.fragments.storefront.FragmentStoreFront;
import com.apitap.views.fragments.stores.FragmentStore;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.google.android.material.tabs.TabLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;

public class FragmentHome extends BaseFragment implements View.OnClickListener, AdapterStoreAdsPager.AdsItemClick, AdapterHomeSpecials.SpecialListClick,
        AdapterHomeStores.StoreItemClick, AdapterStoreCategories.StoreItemClick {

    private View rootView;

    private TextView textViewShowAllAds;
    private TextView textViewShowAllStores;
    private TextView textViewShowAllSpecials;

    private LinearLayout linearLayoutParent;
    private LinearLayout linearLayoutHeaderStoreFront;

    private RelativeLayout relativeLayoutSearchBarStoreFront;

    private CardView cardViewNoAdsContainer;
    private CardView cardViewAdsContainer;
    private CardView cardViewStoresContainer;
    private CardView cardViewNoStoresContainer;
    private CardView cardViewSpecialsContainer;
    private CardView cardViewNoSpecialsContainer;
    private CardView cardViewStoresByCategory;

    private ViewPager viewPagerAds;
    private Timer timer;

    private int currentAdPage = 0;
    private int adsPageDelay = 13000;

    private CircleIndicator circleIndicator;

    private ShimmerRecyclerView recyclerViewStores;
    private ShimmerRecyclerView recyclerViewSpecials;
    private RecyclerView receiverViewStoreCategory;

    private TabLayout tabLayout;

    private ArrayList<AdsData> adsDataArrayList;

    private AdapterHomeStores adapterHomeStores;
    private AdapterHomeSpecials adapterHomeSpecials;
    private AdapterStoreAdsPager adapterStorePager;

    private HomeAdsResponse homeAdsResponse;
    private HomeStoresResponse homeStoresResponse;
    private HomeSpecialsResponse homeSpecialsResponse;
    private HomeAddressResponse homeAddressResponse;
    private StoreCategoryResponse storeCategoryResponse;

    public static Activity mActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        return rootView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mActivity = getActivity();

        initViews();

        clickListeners();

        fetchHomeScreenData();

        // setStoreCategoryAdapter();

        //fetchMerchantsByLocation();
    }


    private void initViews() {
        tabLayout = getActivity().findViewById(R.id.tabs);
        relativeLayoutSearchBarStoreFront = getActivity().findViewById(R.id.search_storefront);
        linearLayoutHeaderStoreFront = getActivity().findViewById(R.id.header);

        textViewShowAllAds = rootView.findViewById(R.id.textViewShowAllAds);
        textViewShowAllStores = rootView.findViewById(R.id.textViewShowAllStores);
        textViewShowAllSpecials = rootView.findViewById(R.id.textViewShowAllSpecials);
        linearLayoutParent = rootView.findViewById(R.id.parentLayout);
        cardViewNoAdsContainer = rootView.findViewById(R.id.noAdsll);
        cardViewAdsContainer = rootView.findViewById(R.id.ll_ads);
        cardViewStoresContainer = rootView.findViewById(R.id.ll_stores);
        cardViewNoStoresContainer = rootView.findViewById(R.id.ll_no_stores);
        cardViewSpecialsContainer = rootView.findViewById(R.id.ll_specials);
        cardViewNoSpecialsContainer = rootView.findViewById(R.id.noSpecials);
        cardViewStoresByCategory = rootView.findViewById(R.id.ll_stores_by_cat);
        viewPagerAds = rootView.findViewById(R.id.viewpager);
        circleIndicator = rootView.findViewById(R.id.indicator_default);
        recyclerViewStores = rootView.findViewById(R.id.receiverViewStores);
        recyclerViewSpecials = rootView.findViewById(R.id.recyclerViewSpecial);
        receiverViewStoreCategory = rootView.findViewById(R.id.receiverViewStoreCategory);

        recyclerViewStores.showShimmerAdapter();
        recyclerViewSpecials.showShimmerAdapter();

        homeTabsView();
    }


    private void clickListeners() {
        textViewShowAllAds.setOnClickListener(this);
        textViewShowAllStores.setOnClickListener(this);
        textViewShowAllSpecials.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (ATPreferences.readBoolean(getActivity(), Constants.HEADER_STORE)) {
            linearLayoutHeaderStoreFront.setVisibility(View.VISIBLE);
            relativeLayoutSearchBarStoreFront.setVisibility(View.VISIBLE);
        } else {
            linearLayoutHeaderStoreFront.setVisibility(View.GONE);
            relativeLayoutSearchBarStoreFront.setVisibility(View.GONE);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (timer != null) {
            timer.cancel();
        }
        releaseExoPlayer();
    }

    public void releaseExoPlayer() {
        if (adapterStorePager != null) {
            adapterStorePager.releasePlayer();
        }

    }


    @Override
    public void onPause() {
        super.onPause();
    }

    @Subscribe
    public void onEvent(final Event event) {
        switch (event.getKey()) {

            case -1:
                hideProgress();
                baseshowFeedbackMessage(getActivity(), linearLayoutParent, "Something went wrong.");
                break;

            case Constants.HOME_ADS:
                hideProgress();
                homeAdsResponse = ModelManager.getInstance().getHomeManager().homeAdsResponse;
                if (!event.hasData()) {
                    cardViewNoAdsContainer.setVisibility(View.GONE);
                    cardViewAdsContainer.setVisibility(View.GONE);
                } else {
                    cardViewNoAdsContainer.setVisibility(View.GONE);
                    cardViewAdsContainer.setVisibility(View.VISIBLE);

                    setAdsAdapter();

                }
                break;
            case Constants.HOME_STORES_BY_CATEGORY:
                hideProgress();
                storeCategoryResponse = ModelManager.getInstance().getHomeManager().storeCategoryResponse;
                if (!event.hasData()) {
                    // cardViewNoStoresContainer.setVisibility(View.GONE);
                    cardViewStoresByCategory.setVisibility(View.GONE);
                } else {
                    cardViewStoresByCategory.setVisibility(View.VISIBLE);
                    setStoreCategoryAdapter();
                }
                break;
            case Constants.HOME_STORES:
                hideProgress();
                homeStoresResponse = ModelManager.getInstance().getHomeManager().homeStoresResponse;
                if (!event.hasData()) {
                    cardViewNoStoresContainer.setVisibility(View.GONE);
                    // cardViewStoresContainer.setVisibility(View.GONE);
                } else {
                    cardViewNoStoresContainer.setVisibility(View.GONE);
                    //cardViewStoresContainer.setVisibility(View.VISIBLE);

                    setStoreAdapter();

                }
                break;
            case Constants.HOME_SPECIALS:
                hideProgress();
                homeSpecialsResponse = ModelManager.getInstance().getHomeManager().homeSpecialsResponse;
                if (!event.hasData()) {
                    cardViewNoSpecialsContainer.setVisibility(View.GONE);
                    cardViewSpecialsContainer.setVisibility(View.GONE);
                } else {
                    cardViewNoSpecialsContainer.setVisibility(View.GONE);
                    //cardViewSpecialsContainer.setVisibility(View.VISIBLE);

                    setSpecialsAdapter();

                }
                break;

            case Constants.HOME_ADDRESS:
                homeAddressResponse = ModelManager.getInstance().getHomeManager().homeAddressResponse;
                saveAddresses();
                break;


        }

    }

    private void saveAddresses() {
        ModelManager.getInstance().getHomeManager().listAddresses = new ArrayList<>();
        ModelManager.getInstance().getHomeManager().addressNickName = new ArrayList<>();

        ModelManager.getInstance().getHomeManager().addressNickName.add("GPS -");
        ModelManager.getInstance().getHomeManager().listAddresses.add("Use Current Location");

        if (homeAddressResponse != null && homeAddressResponse.getRESULT() != null && homeAddressResponse.getRESULT().size() > 0) {

            for (int j = 0; j < homeAddressResponse.getRESULT().size(); j++) {
                for (int k = 0; k < homeAddressResponse.getRESULT().get(j).getAD().size(); k++) {
                    String location1 = homeAddressResponse.getRESULT().get(j).getAD().get(k).getCI().getJsonMember4715();
                    String address2 = homeAddressResponse.getRESULT().get(j).getAD().get(k).getST().getJsonMember1234();
                    String zpCode = homeAddressResponse.getRESULT().get(j).getAD().get(k).getZP().getJsonMember4717();
                    String address1 = homeAddressResponse.getRESULT().get(j).getAD().get(k).getCO().getJsonMember11417();

                    ModelManager.getInstance().getHomeManager().addressNickName.add(
                            Utils.hexToASCII(homeAddressResponse.getRESULT().get(j).getAD().get(k).getJsonMember11453())
                                    + " -");
                    ModelManager.getInstance().getHomeManager().listAddresses.add(location1
                            + " " + address2 + " " + zpCode + "(" + address1 + ")");

                }
            }
        }
    }

    private void setSpecialsAdapter() {
        recyclerViewSpecials.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        adapterHomeSpecials = new AdapterHomeSpecials(getActivity(), homeSpecialsResponse.getRESULT(), this);
        recyclerViewSpecials.setAdapter(adapterHomeSpecials);
    }

    private void setStoreAdapter() {
        recyclerViewStores.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        adapterHomeStores = new AdapterHomeStores(getActivity(), homeStoresResponse.getRESULT(), this);
        recyclerViewStores.setAdapter(adapterHomeStores);
    }

    private void setAdsAdapter() {
        adsDataArrayList = new ArrayList<>();
        for (int i = 0; i < homeAdsResponse.getRESULT().size(); i++) {
            AdsData adsData = new AdsData();
            adsData.setAdName(Utils.hexToASCII(homeAdsResponse.getRESULT().get(i).getJsonMember12083()));
            adsData.setMerchantId(homeAdsResponse.getRESULT().get(i).getJsonMember53());
            adsData.setStoreName(Utils.hexToASCII(homeAdsResponse.getRESULT().get(i).getJsonMember11470()));
            adsData.setIsSeen(homeAdsResponse.getRESULT().get(i).getJsonMember1149());
            adsData.setImageUrl(homeAdsResponse.getRESULT().get(i).getJsonMember121170());
            adsData.setAdId(homeAdsResponse.getRESULT().get(i).getJsonMember12321());
            adsData.setImageId(homeAdsResponse.getRESULT().get(i).getJsonMember12086());
            adsData.setVideoUrl(homeAdsResponse.getRESULT().get(i).getJsonMember12115());
            Log.d("imagessurl", ATPreferences.readString(getActivity(), Constants.KEY_IMAGE_URL) + "" + homeAdsResponse.getRESULT().get(i).getJsonMember121170());
            adsData.setAdDescription(Utils.hexToASCII(homeAdsResponse.getRESULT().get(i).getJsonMember120157()));
            adsDataArrayList.add(adsData);
        }

        adapterStorePager = new AdapterStoreAdsPager(getActivity(), adsDataArrayList, this);
        viewPagerAds.setAdapter(adapterStorePager);


        circleIndicator.setViewPager(viewPagerAds);

        viewPagerAds.setOffscreenPageLimit(1);
        viewPagerAds.setCurrentItem(currentAdPage);

        viewPagerAds.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentAdPage = position;
                //adapterStorePager.pauseResumePlayer(true);
                adapterStorePager.seekToStart();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        pageSwitcher();
    }

    public void pageSwitcher() {
        timer = new Timer(); // At this line a new Thread will be created
        timer.scheduleAtFixedRate(new RemindTask(), 0, adsPageDelay); // delay
    }

    class RemindTask extends TimerTask {

        @Override
        public void run() {
            getActivity().runOnUiThread(() -> {

                if (currentAdPage == adapterStorePager.getCount()) { // In my case the number of pages are 5
                    currentAdPage = 0;
                } else {
                    viewPagerAds.setCurrentItem(currentAdPage++);
                }
            });
        }
    }

    private void setStoreCategoryAdapter() {
        AdapterStoreCategories adapterStoreCategories = new AdapterStoreCategories(requireContext(), storeCategoryResponse.getRESULT(), this);
        receiverViewStoreCategory.setAdapter(adapterStoreCategories);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.textViewShowAllAds:

                tabLayout.getTabAt(1).select();

                break;
            case R.id.textViewShowAllSpecials:


                tabLayout.getTabAt(2).select();
                break;

            case R.id.textViewShowAllStores:

                tabLayout.getTabAt(0).select();
                break;

        }
    }

    private void fetchHomeScreenData() {
        showProgress();
        ModelManager.getInstance().getHomeManager().getHomeScreesData(getActivity(), Operations
                .makeJsonForHome(getActivity(), Constants.NewToOld));
    }

    private void fetchMerchantsByLocation() {
        showProgress();
        ModelManager.getInstance().getMerchantStoresManager()
                .getLocationByMerchant(requireActivity(),
                        Operations.getLocationByMerchant(requireActivity()));
    }


    @Override
    public void onAdClick(int position) {
        releaseExoPlayer();

        ModelManager.getInstance().setProductSeen().setProductSeen(getActivity(), Operations.makeProductSeen(getActivity(),
                adsDataArrayList.get(position).getAdId()));

        String imageId = adsDataArrayList.get(position).getImageId();
        String merchantId = adsDataArrayList.get(position).getMerchantId();
        String adId = adsDataArrayList.get(position).getAdId();
        String adName = adsDataArrayList.get(position).getAdName();
        String adDesc = adsDataArrayList.get(position).getAdDescription();
        String storeName = adsDataArrayList.get(position).getStoreName();
        String imageUrl = "";
        String videoUrl = adsDataArrayList.get(position).getVideoUrl();

        if (!imageId.isEmpty()) {
            imageUrl = ATPreferences.readString(getActivity(), Constants.KEY_IMAGE_URL) +
                    adsDataArrayList.get(position).getImageUrl();
            videoUrl = "";
        }

        Bundle bundle = new Bundle();
        bundle.putString("videoUrl", videoUrl);
        bundle.putString("image", imageUrl);
        bundle.putString("merchant", storeName);
        bundle.putString("adName", adName);
        bundle.putString("desc", adDesc);
        bundle.putString("id", adId);
        bundle.putString("ad_id", adId);
        bundle.putString("merchantid", merchantId);
        bundle.putInt("adpos", position);
        ((HomeActivity) getActivity()).displayView(new FragmentAdDetail(), Constants.TAG_FULL_SCREEN, bundle);

        storeFrontTabsView();
    }

    @Override
    public void onSpecialsClick(int position) {
        releaseExoPlayer();

        String productId = Utils.lengtT(11, homeSpecialsResponse.getRESULT().get(position).getJsonMember114144());
        String productType = homeSpecialsResponse.getRESULT().get(position).getJsonMember114112();

        ModelManager.getInstance().setProductSeen().setProductSeen(getActivity(), Operations.makeProductSeen(getActivity(),
                productId));

        Bundle bundle = new Bundle();
        bundle.putString("productId", productId);
        bundle.putString("productType", productType);
        FragmentItemDetails fragment = new FragmentItemDetails();
        fragment.setArguments(bundle);

        ((HomeActivity) getActivity()).displayAddView(fragment, Constants.TAG_SPECIAL, Constants.TAG_DETAILSPAGE, bundle);

        storeFrontTabsView();
    }

    @Override
    public void onSpecialsViewMore() {
        textViewShowAllSpecials.performClick();
    }

    @Override
    public void onStoreClick(int position) {
        releaseExoPlayer();

        String merchantID = homeStoresResponse.getRESULT().get(position).getJsonMember53();
        String merchantName = homeStoresResponse.getRESULT().get(position).getJsonMember11470();
        Bundle b = new Bundle();
        b.putBoolean(Constants.HEADER_STORE, true);
        b.putString(Constants.MERCHANT_ID, merchantID);

        ATPreferences.putString(getActivity(), Constants.HEADER_IMG,
                homeStoresResponse.getRESULT().get(position).getJsonMember121170());
        ATPreferences.putBoolean(getActivity(), Constants.HEADER_STORE, true);
        ATPreferences.putString(getActivity(), Constants.MERCHANT_ID, merchantID);
        ATPreferences.putString(getActivity(), Constants.MERCHANT_CATEGORY, "");

        ((HomeActivity) getActivity()).displayViewAddHide(new FragmentStoreFront(), Constants.TAG_STORESFRONTPAGE, Constants.TAG_HOMEPAGE, b);

        storeFrontTabsView();
    }

    public static void setFavouriteMerchantView(TextView tvStoreName) {
        if (MerchantFavouriteManager.isCurrentMerchantFav) {
            //  tvStoreName.setBackground(mActivity.getResources().getDrawable(R.drawable.back_round_green_border));
            tvStoreName.setTextColor(mActivity.getResources().getColor(R.color.colorGreen));
            tvStoreName.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_icon_fav_red, 0, 0, 0);
        } else {
            //    tvStoreName.setBackground(mActivity.getResources().getDrawable(R.drawable.back_round_blue_border));
            tvStoreName.setTextColor(mActivity.getResources().getColor(R.color.colorBlue));
            tvStoreName.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_icon_fav_gray, 0, 0, 0);
        }
    }

    @Override
    public void onCategoryStoreClick(Bundle bundle) {
        releaseExoPlayer();

        ((HomeActivity) getActivity()).displayViewAddHide(new FragmentStoreFront(),
                Constants.TAG_STORESFRONTPAGE, Constants.TAG_HOMEPAGE, bundle);
        storeFrontTabsView();
    }

    @Override
    public void onViewMoreStoreClick(Bundle bundle) {
        releaseExoPlayer();

        ((HomeActivity) getActivity()).displayViewReplace(new FragmentStore(), Constants.TAG_STORESPAGE, bundle);
    }
}

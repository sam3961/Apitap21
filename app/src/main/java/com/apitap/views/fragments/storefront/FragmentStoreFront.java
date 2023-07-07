package com.apitap.views.fragments.storefront;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;

import com.apitap.R;
import com.apitap.controller.MerchantFavouriteManager;
import com.apitap.controller.ModelManager;
import com.apitap.model.Constants;
import com.apitap.model.Operations;
import com.apitap.model.Utils;
import com.apitap.model.bean.itemStoreFront.ItemStoreFrontResponse;
import com.apitap.model.customclasses.Event;
import com.apitap.model.preferences.ATPreferences;
import com.apitap.model.showCase.ShowCaseResponse;
import com.apitap.model.storeFrontItems.ads.AdsData;
import com.apitap.model.storeFrontItems.ads.StoreAdsResponse;
import com.apitap.model.storeFrontItems.browseCategory.BrowseCategoryResponse;
import com.apitap.model.storeFrontItems.details.StoreDetailsResponse;
import com.apitap.model.storeFrontItems.favourites.StoreFavouriteResponse;
import com.apitap.model.storeFrontItems.items.StoreItemsResponse;
import com.apitap.model.storeFrontItems.specials.StoreSpecialsResponse;
import com.apitap.views.HomeActivity;
import com.apitap.views.MerchantStoreDetails;
import com.apitap.views.adapters.AdapterCategorySpinner;
import com.apitap.views.adapters.AdapterHeaderCategory;
import com.apitap.views.fragments.items.FragmentItemsHomeStoreFront;
import com.apitap.views.fragments.storefront.adapter.AdapterShowCase;
import com.apitap.views.fragments.storefront.adapter.AdapterStoreItems;
import com.apitap.views.fragments.storefront.adapter.AdapterStoreAdsPager;
import com.apitap.views.fragments.storefront.adapter.AdapterStoreSpecials;
import com.apitap.views.fragments.BaseFragment;
import com.apitap.views.fragments.adDetails.FragmentAdDetail;
import com.apitap.views.fragments.itemDetails.FragmentItemDetails;
import com.apitap.views.fragments.messages.FragmentMessages;
import com.apitap.views.fragments.ads.storeFront.FragmentAdsStoreFront;
import com.apitap.views.fragments.home.FragmentHome;
import com.apitap.views.fragments.items.storeFront.FragmentItemsStoreFront;
import com.apitap.views.fragments.specials.storefront.FragmentSpecialStoreFront;
import com.apitap.views.fragments.checkinTv.FragmentStreamDirectory;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;

import static com.apitap.controller.MerchantManager.merchantCategoryList;

public class FragmentStoreFront extends BaseFragment implements View.OnClickListener, AdapterStoreItems.ItemListClick,
        AdapterStoreSpecials.SpecialListClick, AdapterStoreAdsPager.AdsItemClick, AdapterShowCase.ShowCaseItemClick, View.OnTouchListener {

    private View rootView;

    private ImageView imageViewStoreHeader;
    private ImageView imageViewStoreNoAds;
    private ImageView imageViewTv;

    private TextView textViewStoreName;
    private TextView textViewStoreNameDetails;
    private TextView textViewShowAllAds;
    private TextView textViewShowAllItems;
    private TextView textViewShowAllSpecials;
    private TextView textViewMenuTitle;
    private TextView textViewShowCaseTitle;

    private LinearLayout linearLayoutBackHeader;
    private LinearLayout linearLayoutStoreDetails;
    private LinearLayout linearLayoutStoreName;
    private LinearLayout linearLayoutStoreMessages;
    private ConstraintLayout linearLayoutParent;
    private LinearLayout linearLayoutStoreFavourite;
    private LinearLayout linearLayoutHeaderStoreFront;
    private LinearLayout linearLayoutHeaderCheckin;
    private LinearLayout linearLayoutStoreReservation;
    private LinearLayout linearLayoutHeaderCategory;

    private RelativeLayout relativeLayoutSearchBarStoreFront;
    float dX;
    float dY;
    int lastAction;

    private Spinner spinnerCategories;

    private Button buttonStoreDetails;

    private CardView cardViewNoAdsContainer;
    private CardView cardViewAdsContainer;
    private CardView cardViewItemsContainer;
    private CardView cardViewNoItemsContainer;
    private CardView cardViewSpecialsContainer;
    private CardView cardViewShowCaseContainer;
    private CardView cardViewNoSpecialsContainer;
    private CardView cardViewNoShowCaseContainer;

    private ViewPager viewPagerAds;
    private Timer timer;
    private int currentAdPage = 0;
    private int adsPageDelay = 13000;

    private CircleIndicator circleIndicator;

    private ShimmerRecyclerView recyclerViewItems;
    private ShimmerRecyclerView recyclerViewSpecials;
    private ShimmerRecyclerView recyclerViewShowCase;

    private TabLayout tabLayout;

    private String merchantId = "";
    private String merchantCategory = "";

    private StoreItemsResponse storeFrontItemResponse;
    private StoreSpecialsResponse storeFrontSpecialsResponse;
    private ShowCaseResponse showCaseResponse;
    private StoreAdsResponse storeFrontAdsResponse;

    private StoreDetailsResponse storeDetailsResponse;
    private BrowseCategoryResponse browseCategoryResponse;
    private StoreFavouriteResponse storeFavouriteResponse;
    public ItemStoreFrontResponse itemListResponse;

    private AdapterStoreItems adapterStoreItems;
    private AdapterStoreSpecials adapterStoreSpecials;
    private AdapterShowCase adapterShowCase;
    private AdapterStoreAdsPager adapterStorePager;
    private AdapterHeaderCategory adapterHeaderCategory;

    private ArrayList<String> arrayListCategories;
    private ArrayList<AdsData> adsDataArrayList;
    private List<String> arrayListHeaderCategory = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_store_front, container, false);
        return rootView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        merchantId = getArguments().getString(Constants.MERCHANT_ID);


        initViews();

        clickListeners();

        fetchStoreFrontItems();

        storeFrontTabsView();
    }


    private void initViews() {
        tabLayout = getActivity().findViewById(R.id.tabs);
        linearLayoutStoreFavourite = getActivity().findViewById(R.id.view_store_favourite);
        linearLayoutHeaderStoreFront = getActivity().findViewById(R.id.header);
        linearLayoutHeaderCheckin = getActivity().findViewById(R.id.view_checkin);
        linearLayoutStoreReservation = getActivity().findViewById(R.id.view_store_reservation);
        linearLayoutHeaderCategory = getActivity().findViewById(R.id.header_browse_category);
        imageViewStoreHeader = rootView.findViewById(R.id.adstoreImg);
        imageViewStoreNoAds = rootView.findViewById(R.id.noStoreLogo);
        imageViewTv = rootView.findViewById(R.id.imageViewTv);
        linearLayoutBackHeader = rootView.findViewById(R.id.linearLayoutBack);
        linearLayoutStoreDetails = rootView.findViewById(R.id.linearLayoutStoreDetails);
        buttonStoreDetails = rootView.findViewById(R.id.details_store);
        linearLayoutStoreName = rootView.findViewById(R.id.linearLayoutStoreName);
        textViewStoreName = rootView.findViewById(R.id.storeName);
        textViewStoreNameDetails = getActivity().findViewById(R.id.textViewStoreNameDetails);
        textViewShowAllAds = rootView.findViewById(R.id.textViewShowAllAds);
        textViewShowAllItems = rootView.findViewById(R.id.textViewShowAllItems);
        textViewShowAllSpecials = rootView.findViewById(R.id.textViewShowAllSpecials);
        textViewMenuTitle = rootView.findViewById(R.id.textViewMenuTitle);
        textViewShowCaseTitle = rootView.findViewById(R.id.textViewShowCaseTitle);
        linearLayoutStoreMessages = rootView.findViewById(R.id.linearLayoutStoreMessages);
        linearLayoutParent = rootView.findViewById(R.id.parentLayout);
        spinnerCategories = rootView.findViewById(R.id.category_dropdown);
        cardViewNoAdsContainer = rootView.findViewById(R.id.noAdsll);
        cardViewAdsContainer = rootView.findViewById(R.id.ll_ads);
        cardViewItemsContainer = rootView.findViewById(R.id.ll_items);
        cardViewNoItemsContainer = rootView.findViewById(R.id.noItems);
        cardViewSpecialsContainer = rootView.findViewById(R.id.ll_specials);
        cardViewShowCaseContainer = rootView.findViewById(R.id.ll_showcase);
        cardViewNoSpecialsContainer = rootView.findViewById(R.id.noSpecials);
        cardViewNoShowCaseContainer = rootView.findViewById(R.id.noShowcase);
        viewPagerAds = rootView.findViewById(R.id.viewpager);
        circleIndicator = rootView.findViewById(R.id.indicator_default);
        recyclerViewItems = rootView.findViewById(R.id.receiverViewSpecials);
        recyclerViewSpecials = rootView.findViewById(R.id.recyclerViewItems);
        recyclerViewShowCase = rootView.findViewById(R.id.recyclerViewShowCase);
        relativeLayoutSearchBarStoreFront = getActivity().findViewById(R.id.search_storefront);

        linearLayoutStoreFavourite.setVisibility(View.GONE);
        linearLayoutHeaderStoreFront.setVisibility(View.VISIBLE);
       // linearLayoutHeaderCheckin.setVisibility(View.VISIBLE);
       // linearLayoutStoreReservation.setVisibility(View.VISIBLE);
        linearLayoutHeaderCategory.setVisibility(View.VISIBLE);
        relativeLayoutSearchBarStoreFront.setVisibility(View.VISIBLE);

        final View relativeLayoutTv = rootView.findViewById(R.id.relativeLayoutTv);
        relativeLayoutTv.setOnTouchListener(this);

        recyclerViewItems.showShimmerAdapter();
        recyclerViewSpecials.showShimmerAdapter();
        recyclerViewShowCase.showShimmerAdapter();

        clearPreviousStoreFrontData();
        storeFrontTabsView();
    }

    private void clearPreviousStoreFrontData() {
        itemListResponse = null;
        arrayListHeaderCategory = new ArrayList<>();
    }


    private void clickListeners() {
        linearLayoutStoreName.setOnClickListener(this);
        linearLayoutBackHeader.setOnClickListener(this);
        linearLayoutStoreDetails.setOnClickListener(this);
        buttonStoreDetails.setOnClickListener(this);
        linearLayoutStoreMessages.setOnClickListener(this);
        textViewShowAllAds.setOnClickListener(this);
        textViewShowAllItems.setOnClickListener(this);
        textViewShowAllSpecials.setOnClickListener(this);
        imageViewTv.setOnClickListener(this);
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

        linearLayoutHeaderCheckin.setVisibility(View.GONE);
        linearLayoutStoreReservation.setVisibility(View.GONE);

    }

    public void releaseExoPlayer() {
        if (adapterStorePager != null) {
            adapterStorePager.releasePlayer();
        }

    }


    @Subscribe
    public void onEvent(final Event event) {
        switch (event.getKey()) {
            case Constants.SHOWCASE_TITLE_RESPONSE:
                textViewShowCaseTitle.setText(event.getResponse());
                break;
            case Constants.SHOWCASE_RESPONSE:
                showCaseResponse = ModelManager.getInstance().getMerchantStoresManager().showCaseModel;
                if (!event.hasData()) {
                    cardViewNoShowCaseContainer.setVisibility(View.GONE);
                    cardViewShowCaseContainer.setVisibility(View.GONE);
                } else {
                    cardViewNoShowCaseContainer.setVisibility(View.GONE);
                    cardViewShowCaseContainer.setVisibility(View.VISIBLE);

                    setShowCaseAdapter();
                }

                break;
            case Constants.STORE_FRONT_ITEMS:
                // hideProgress();
                storeFrontItemResponse = ModelManager.getInstance().getMerchantStoresManager().storeItemsModel;
                if (!event.hasData()) {
                    cardViewNoItemsContainer.setVisibility(View.GONE);
                    cardViewItemsContainer.setVisibility(View.GONE);
                } else {
                    cardViewNoItemsContainer.setVisibility(View.GONE);
                    cardViewItemsContainer.setVisibility(View.GONE);

                    setItemsAdapter();
                }

                //fetchStoreDetails();
                break;

            case Constants.STORE_FRONT_SPECIALS:
                storeFrontSpecialsResponse = ModelManager.getInstance().getMerchantStoresManager().storeSpecialsModel;
                if (!event.hasData()) {
                    cardViewNoSpecialsContainer.setVisibility(View.GONE);
                    cardViewSpecialsContainer.setVisibility(View.GONE);
                } else {
                    cardViewNoSpecialsContainer.setVisibility(View.GONE);
                    cardViewSpecialsContainer.setVisibility(View.GONE);

                    setSpecialsAdapter();
                }

                break;

            case Constants.STORE_FRONT_ADS:
                storeFrontAdsResponse = ModelManager.getInstance().getMerchantStoresManager().storeAdsModel;
                if (!event.hasData()) {
                    cardViewNoAdsContainer.setVisibility(View.GONE);
                    cardViewAdsContainer.setVisibility(View.GONE);
                } else {
                    cardViewNoAdsContainer.setVisibility(View.GONE);
                    cardViewAdsContainer.setVisibility(View.VISIBLE);
                    setAdsAdapter();
                }
                //  fetchStoreDetails();
                break;

            case Constants.STORE_FRONT_DETAILS:
                hideProgress();
                storeDetailsResponse = ModelManager.getInstance().getMerchantStoresManager().storeDetailsModel;
                textViewStoreName.setText(Utils.hexToASCII(storeDetailsResponse.getRESULT().get(0).getJsonMember11470()));
                textViewStoreNameDetails.setText(Utils.hexToASCII(storeDetailsResponse.getRESULT().get(0).getJsonMember11470()));

                storeName = Utils.hexToASCII(storeDetailsResponse.getRESULT().get(0).getJsonMember11470());
                isBroadcasting = Boolean.valueOf(storeDetailsResponse.getRESULT().get(0).getIsBroadcasting());
                storeImageUrl = storeDetailsResponse.getRESULT().get(0).getJsonMember121170();
                ATPreferences.putString(getActivity(), Constants.HEADER_IMG, storeImageUrl);
                ATPreferences.putString(getActivity(), Constants.STORE_NAME, storeName);
                ATPreferences.putString(getActivity(), Constants.STORE_RATE, storeDetailsResponse.getRESULT().get(0).getJsonMember12219());

                if (isBroadcasting)
                    linearLayoutHeaderCheckin.setVisibility(View.VISIBLE);
                else
                    linearLayoutHeaderCheckin.setVisibility(View.GONE);

                Picasso.get().load(ATPreferences.readString(getActivity(), Constants.KEY_IMAGE_URL) +
                                storeImageUrl)
                        .placeholder(R.drawable.loading).into(imageViewStoreHeader);

                Picasso.get().load(ATPreferences.readString(getActivity(), Constants.KEY_IMAGE_URL) +
                                storeImageUrl)
                        .placeholder(R.drawable.loading).into(imageViewStoreNoAds);


                break;

            case Constants.STORE_FRONT_FAVOURITE:
                hideProgress();
                storeFavouriteResponse = ModelManager.getInstance().getMerchantStoresManager().storeFavouriteModel;
                if (storeFavouriteResponse == null)
                    return;
                setListOfFavouriteMerchants();
                checkIsFavourite();

                break;

            case Constants.STORE_FRONT_BROWSE_CAT:
                hideProgress();
                browseCategoryResponse = ModelManager.getInstance().getMerchantStoresManager().browseCategoryModel;
                merchantCategory = browseCategoryResponse.getRESULT().get(0).getJsonMember12045();
                if (merchantCategory.equals("Restaurant/Bar")) {
                    textViewMenuTitle.setText("Our Menu");
                    linearLayoutStoreReservation.setVisibility(View.VISIBLE);
                } else {
                    textViewMenuTitle.setText("Our Products");
                }

                Bundle bundle = new Bundle();
                if (getArguments() != null)
                    bundle = getArguments();
                setBrowseSpinnerAdapter();
                spinnerListener();
                // if (!ATPreferences.readString(getActivity(), Constants.MERCHANT_CATEGORY).isEmpty())
//                    ATPreferences.putString(getActivity(), Constants.MERCHANT_CATEGORY, arrayListCategories.get(1));
                displayViewStore(new FragmentItemsHomeStoreFront(), Constants.TAG_ITEMS, bundle);
                break;

            case Constants.REMOVE_MERCHANT_FAVORITES:

                hideProgress();
                MerchantFavouriteManager.isCurrentMerchantFav = false;
                FragmentHome.setFavouriteMerchantView(textViewStoreName);
                FragmentHome.setFavouriteMerchantView(textViewStoreNameDetails);

                break;

            case Constants.ADD_MERCHANT_FAVORITE_SUCCESS:

                hideProgress();
                MerchantFavouriteManager.isCurrentMerchantFav = true;
                FragmentHome.setFavouriteMerchantView(textViewStoreName);
                FragmentHome.setFavouriteMerchantView(textViewStoreNameDetails);

                break;

            case -1:
                hideProgress();
                baseshowFeedbackMessage(getActivity(), linearLayoutParent, "Something went wrong.");
                break;


        }

    }

    private void spinnerListener() {
        spinnerCategories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    ATPreferences.putString(getActivity(), Constants.MERCHANT_CATEGORY, arrayListCategories.get(position));
                    ((HomeActivity) getActivity()).displayView(new FragmentItemsStoreFront(), Constants.TAG_ITEMS_STOREFRONT, new Bundle());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setBrowseSpinnerAdapter() {
        arrayListCategories = new ArrayList<>();
        arrayListCategories.add("Select Category");
        merchantCategoryList.add("Select Category");

        for (int i = 0; i < browseCategoryResponse.getRESULT().size(); i++) {
            arrayListCategories.add(browseCategoryResponse.getRESULT().get(i).getJsonMember12045());
            merchantCategoryList.add(browseCategoryResponse.getRESULT().get(i).getJsonMember12045());
        }
        AdapterCategorySpinner arrayAdapter = new AdapterCategorySpinner(getActivity(), arrayListCategories);
        spinnerCategories.setAdapter(arrayAdapter);
    }

    private void checkIsFavourite() {
        for (int i = 0; i < MerchantFavouriteManager.mernchantfavlist.size(); i++) {
            if (merchantId.equals(MerchantFavouriteManager.mernchantfavlist.get(i))) {
                MerchantFavouriteManager.isCurrentMerchantFav = true;
                isFavouriteStore = true;
                FragmentHome.setFavouriteMerchantView(textViewStoreName);
                FragmentHome.setFavouriteMerchantView(textViewStoreNameDetails);
                break;
            } else {
                MerchantFavouriteManager.isCurrentMerchantFav = false;
                isFavouriteStore = false;
            }
        }

    }

    private void setListOfFavouriteMerchants() {
        if (storeFavouriteResponse.getRESULT() != null && storeFavouriteResponse.getRESULT().size() > 0) {
            for (int i = 0; i < storeFavouriteResponse.getRESULT().size(); i++) {
                MerchantFavouriteManager.mernchantfavlist.add(storeFavouriteResponse.getRESULT().get(i).getJsonMember114179());
            }
        }
    }

    private void setAdsAdapter() {
        adsDataArrayList = new ArrayList<>();
        for (int i = 0; i < storeFrontAdsResponse.getRESULT().size(); i++) {
            for (int j = 0; j < storeFrontAdsResponse.getRESULT().get(i).getAD().size(); j++) {
                AdsData adsData = new AdsData();
                adsData.setAdName(Utils.hexToASCII(storeFrontAdsResponse.getRESULT().get(i).getAD().get(j).get12083()));
                adsData.setStoreName(Utils.hexToASCII(storeFrontAdsResponse.getRESULT().get(i).getAD().get(j).get11470()));
                adsData.setIsSeen(storeFrontAdsResponse.getRESULT().get(i).getAD().get(j).get1149());
                adsData.setMerchantId(storeFrontAdsResponse.getRESULT().get(i).getAD().get(j).get53());
                adsData.setImageUrl(storeFrontAdsResponse.getRESULT().get(i).getAD().get(j).get121170());
                adsData.setAdId(storeFrontAdsResponse.getRESULT().get(i).getAD().get(j).get12321());
                adsData.setImageId(storeFrontAdsResponse.getRESULT().get(i).getAD().get(j).get12086());
                adsData.setVideoUrl(storeFrontAdsResponse.getRESULT().get(i).getAD().get(j).get12115());
                adsData.setAdDescription(storeFrontAdsResponse.getRESULT().get(i).getAD().get(j).get120157());
                adsDataArrayList.add(adsData);
            }
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


    private void setItemsAdapter() {
        recyclerViewItems.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        adapterStoreItems = new AdapterStoreItems(getActivity(), storeFrontItemResponse.getRESULT(), this);
        recyclerViewItems.setAdapter(adapterStoreItems);
    }

    private void setSpecialsAdapter() {
        recyclerViewSpecials.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        adapterStoreSpecials = new AdapterStoreSpecials(getActivity(), storeFrontSpecialsResponse.getRESULT(), this);
        recyclerViewSpecials.setAdapter(adapterStoreSpecials);
    }

    private void setShowCaseAdapter() {
        recyclerViewShowCase.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        adapterShowCase = new AdapterShowCase(getActivity(), showCaseResponse.getRESULT(), this);
        recyclerViewShowCase.setAdapter(adapterShowCase);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageViewTv:
                ((HomeActivity) getActivity()).displayView(new FragmentStreamDirectory(), Constants.TAG_STREAM_DIRECTORY, new Bundle());
                break;
            case R.id.linearLayoutBack:
                /*startActivity(new Intent(getActivity(), HomeActivity.class));
                getActivity().finish();*/
                onBackPress();
                break;

            case R.id.linearLayoutStoreDetails:
            case R.id.details_store:
                startActivity(new Intent(getActivity(), MerchantStoreDetails.class)
                        .putExtra("merchantId", merchantId));

                break;

            case R.id.linearLayoutStoreMessages:
                Bundle bundle = new Bundle();
                bundle.putString("merchantId", merchantId);
                bundle.putString("className", "Home");
                bundle.putString("storeName", textViewStoreName.getText().toString());
                ((HomeActivity) getActivity()).displayView(new FragmentMessages(), Constants.TAG_MESSAGEPAGE, bundle);

                break;

            case R.id.textViewShowAllAds:
                ATPreferences.putString(getActivity(), Constants.MERCHANT_CATEGORY, arrayListCategories.get(1));
                ((HomeActivity) getActivity()).displayView(new FragmentAdsStoreFront(), Constants.TAG_ADS, new Bundle());
                break;

            case R.id.textViewShowAllSpecials:
                ATPreferences.putString(getActivity(), Constants.MERCHANT_CATEGORY, arrayListCategories.get(1));
                ((HomeActivity) getActivity()).displayView(new FragmentSpecialStoreFront(), Constants.TAG_SPECIAL, new Bundle());
                //tabLayout.getTabAt(2).select();
                break;

            case R.id.textViewShowAllItems:
                ATPreferences.putString(getActivity(), Constants.MERCHANT_CATEGORY, arrayListCategories.get(1));
                ((HomeActivity) getActivity()).displayView(new FragmentItemsStoreFront(), Constants.TAG_ITEMS_STOREFRONT, new Bundle());
                // tabLayout.getTabAt(3).select();
                break;

            case R.id.textViewStoreNameDetails:
            case R.id.linearLayoutStoreName:
                if (!MerchantFavouriteManager.isCurrentMerchantFav) {
                    fetchAddFavouriteResponse();
                } else
                    fetchRemoveFavouriteResponse();
                break;

        }
    }

    private void fetchStoreFrontItems() {
        showProgress();
        ModelManager.getInstance().getMerchantStoresManager().getMerchantStoreItems(getActivity(),
                Operations.makeJsonForStoreFrontItems(getActivity(), merchantId, Constants.NewToOld));
    }


    private void fetchStoreDetails() {
        //   showProgress();
        ModelManager.getInstance().getMerchantStoresManager().getMerchantStoreDetails(getActivity(),
                Operations.makeJsonGetMerchantDetail(getActivity(), merchantId));
    }

    private void fetchRemoveFavouriteResponse() {
        showProgress();
        ModelManager.getInstance().getMerchantFavouriteManager().removeFavourite(getActivity(),
                Operations.makeJsonRemoveMerchantFavourite(getActivity(), merchantId));

    }

    private void fetchAddFavouriteResponse() {
        showProgress();
        ModelManager.getInstance().getAddMerchantFavorite().addMerchantToFavorite(getActivity(),
                Operations.makeJsonMerchantAddToFavorite(getActivity(), merchantId));
    }


    @Override
    public void onItemsClick(int position) {
        releaseExoPlayer();
        String productId = Utils.lengtT(11, storeFrontItemResponse.getRESULT().get(position).getJsonMember114144());
        String productType = storeFrontItemResponse.getRESULT().get(position).getJsonMember114112();

        ModelManager.getInstance().setProductSeen().setProductSeen(getActivity(), Operations.makeProductSeen(getActivity(),
                productId));

        Bundle bundle = new Bundle();
        bundle.putString("productId", productId);
        bundle.putString("productType", productType);
        FragmentItemDetails fragment = new FragmentItemDetails();
        fragment.setArguments(bundle);

        ((HomeActivity) getActivity()).displayAddView(fragment, Constants.TAG_SPECIAL, Constants.TAG_DETAILSPAGE, bundle);
        // ((HomeActivity) getActivity()).displayView(fragment, Constants.TAG_DETAILSPAGE, bundle);

        storeFrontTabsView();
    }

    @Override
    public void onItemsViewMore() {
        textViewShowAllItems.performClick();
    }

    @Override
    public void onSpecialsClick(int position) {
        releaseExoPlayer();
        String productId = Utils.lengtT(11, storeFrontSpecialsResponse.getRESULT().get(position).getJsonMember114144());
        String productType = storeFrontSpecialsResponse.getRESULT().get(position).getJsonMember114112();

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
    public void onAdClick(int position) {
        releaseExoPlayer();

        ModelManager.getInstance().setProductSeen().setProductSeen(getActivity(), Operations.makeProductSeen(getActivity(),
                adsDataArrayList.get(position).getAdId()));

        String imageId = adsDataArrayList.get(position).getImageId();
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
        ((HomeActivity) getActivity()).displayView(new FragmentAdDetail(), Constants.TAG_AD_DETAIL, bundle);


        storeFrontTabsView();
    }

    public void displayViewStore(Fragment fragment, String tag, Bundle bundle) {
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (bundle != null)
            fragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.container_for_store, fragment);
        fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.commit();

    }

    @Override
    public void onShowCaseClick(int position) {
        releaseExoPlayer();

        String productId = Utils.lengtT(11, showCaseResponse.getRESULT().get(position).getJsonMember114144());
        String productType = showCaseResponse.getRESULT().get(position).getJsonMember114112();

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
    public boolean onTouch(View view, MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                dX = view.getX() - event.getRawX();
                dY = view.getY() - event.getRawY();
                lastAction = MotionEvent.ACTION_DOWN;
                break;

            case MotionEvent.ACTION_MOVE:
                view.setY(event.getRawY() + dY);
                view.setX(event.getRawX() + dX);
                lastAction = MotionEvent.ACTION_MOVE;
                break;

            case MotionEvent.ACTION_UP:
                if (lastAction == MotionEvent.ACTION_DOWN) {
                    //   Toast.makeText(requireContext(), "Clicked!", Toast.LENGTH_SHORT).show();
                    ((HomeActivity) getActivity()).displayView(new FragmentStreamDirectory(), Constants.TAG_STREAM_DIRECTORY, new Bundle());
                }
                break;

            default:
                return false;
        }
        return true;
    }
}

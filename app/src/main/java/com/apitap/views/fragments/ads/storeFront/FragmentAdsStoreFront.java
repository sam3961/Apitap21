package com.apitap.views.fragments.ads.storeFront;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.apitap.R;
import com.apitap.controller.ModelManager;
import com.apitap.model.Constants;
import com.apitap.model.Operations;
import com.apitap.model.Utils;
import com.apitap.model.bean.SelectedParentModel;
import com.apitap.model.bean.levelOneCategories.LevelOneCategory;
import com.apitap.model.customclasses.Event;
import com.apitap.model.deliveryServices.DeliveryServiceModel;
import com.apitap.model.merchantCategoryList.MerchantCategoryListModel;
import com.apitap.model.preferences.ATPreferences;
import com.apitap.model.storeFrontAds.RESULTItem;
import com.apitap.model.storeFrontAds.StoreFrontAdsResponse;
import com.apitap.model.storeFrontItems.browseCategory.BrowseCategoryResponse;
import com.apitap.views.HomeActivity;
import com.apitap.views.MerchantStoreDetails;
import com.apitap.views.adapters.AdapterCategorySpinner;
import com.apitap.views.adapters.AdapterInitalCategories;
import com.apitap.views.fragments.BaseFragment;
import com.apitap.views.fragments.interfaces.SearchStoreClickListener;
import com.apitap.views.fragments.adDetails.FragmentAdDetail;
import com.apitap.views.fragments.messages.FragmentMessages;
import com.apitap.views.fragments.items.adapter.AdapterMerchantCategoryItem;
import com.apitap.views.fragments.items.adapter.AdapterParentCategoriesItem;
import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import static com.apitap.App.isGuest;

public class FragmentAdsStoreFront extends BaseFragment implements View.OnClickListener,
        AdapterInitalCategories.CategoriesItemClick,
        AdapterMerchantCategoryItem.MerchantCategoryClick,
        AdapterParentCategoriesItem.ParentCategoryClick, AdapterChildAds.AdsClick, SearchStoreClickListener {

    private View rootView;

    private ImageView imageViewFilter;
    private ImageView imageViewStoreImage;

    private RelativeLayout relativeLayoutAfterSelection;
    private RelativeLayout relativeLayoutFilter;

    private LinearLayout linearLayoutSelectionCategory;
    private LinearLayout linearLayoutViewResults;
    private LinearLayout linearLayoutGoBack;
    private LinearLayout linearLayoutStoreHeader;
    private LinearLayout linearLayoutSearchHeader;
    private LinearLayout linearLayoutStoreMessages;

    private Button buttonStoreDetails;

    private TextView textViewFilter;
    private TextView textViewSearch;
    private TextView textViewCategoryName;
    private TextView textViewCategorySelect;


    private EditText editTextSearchWord;
    private EditText editTextSearchZip;

    private RelativeLayout viewLeftPanel;
    private RelativeLayout parentLayout;
    private RelativeLayout relativeLayoutHomeToolbar;
    private RelativeLayout relativeLayoutStoreToolbar;


    private Spinner spinnerCategory;
    private Spinner spinnerDeliveryMethod;
    private Spinner spinnerSortBy;
    private Spinner spinnerLocation;
    private Spinner spinnerCategoryFilter;

    private RecyclerView recycelerViewParentCategory;
    private ExpandableListView recyclerViewAds;
    private RecyclerView recyclerViewInitialCategory;
    private RecyclerView rvMerchantCategory;

    private AdapterMerchantCategoryItem adapterMerchantCategory;
    private AdapterParentAds adapterAds;
    private AdapterInitalCategories adapterCategories;
    private AdapterParentCategoriesItem adapterParentCategories;

    private ArrayList<String> arrayListSpinnerCategory = new ArrayList<>();
    private ArrayList<String> arrayListSearchFilter = new ArrayList<>();
    private List<SelectedParentModel> arrayListSelectParent = new ArrayList<>();

    private LevelOneCategory levelOneCategoryResponse;
    private DeliveryServiceModel deliveryServiceResponse;
    private MerchantCategoryListModel merchantCategoryListResponse;
    private StoreFrontAdsResponse adsListResponse;
    private BrowseCategoryResponse browseCategoryResponse;

    private String searchKey = "";
    private String selectedCategoryId = "";
    private String selectedLevelOneCategoryId = "";
    private String selectedDeliveryId = "";
    private String locationName = "";
    private String selectedParentId = "";
    private String selectedGrandParentId = "";
    private String merchantId = "";
    private String parentTitle = "";
    private String selectedUpdatedCategoryId = "";
    private String sortByID = "";
    private String storeFrontCategory = "";

    private boolean isFromStoreFront;

    private int categorySelectedPosition;
    public boolean isFirstTimeLoaded = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_ad_storefront, container, false);
        return rootView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        isFromStoreFront = ATPreferences.readBoolean(getActivity(), Constants.HEADER_STORE);
        HomeActivity.setActiveFragment(this);

        initViews();

        clickListeners();
        touchListener();

        sortBySpinnerListener();
        setSortByAdapter();

        inflateSearchSpinnerList();
        setSearchFilterSpinnerAdapter();
        setSearchFilterSpinnerListener();

        fetchCategoryLvlOne();

        setParentListAdapter();


        if (isGuest) {
            ModelManager.getInstance().getLoginManager().guestLastActivity(getActivity(), Operations.makeJsonLastActivityByGuest(getActivity()));
        }
    }

    private void initViews() {
        TabLayout tabLayout = getActivity().findViewById(R.id.tabs);
        imageViewFilter = rootView.findViewById(R.id.imageViewFilter);
        textViewFilter = rootView.findViewById(R.id.textViewFilter);
        recycelerViewParentCategory = rootView.findViewById(R.id.recyclerViewParentCategory);
        textViewSearch = rootView.findViewById(R.id.textViewSearch);
        editTextSearchZip = rootView.findViewById(R.id.editTextSearchZip);
        editTextSearchWord = rootView.findViewById(R.id.editTextSearchWord);
        viewLeftPanel = rootView.findViewById(R.id.scrollViewLeftPanel);
        parentLayout = rootView.findViewById(R.id.parentLayout);
        rvMerchantCategory = rootView.findViewById(R.id.recyclerViewMerchantCategory);
        spinnerCategory = rootView.findViewById(R.id.spinnerCategory);
        spinnerDeliveryMethod = rootView.findViewById(R.id.spinnerDeliveryMethod);
        spinnerSortBy = rootView.findViewById(R.id.spinnerSortBy);
        spinnerLocation = rootView.findViewById(R.id.spinnerSearchFilter);
        spinnerCategoryFilter = rootView.findViewById(R.id.spinnerCategoryFilter);
        recyclerViewAds = rootView.findViewById(R.id.expandableListAds);
        recyclerViewInitialCategory = rootView.findViewById(R.id.recyclerViewCategory);
        relativeLayoutAfterSelection = rootView.findViewById(R.id.relativeLayoutAfterSelection);
        relativeLayoutFilter = rootView.findViewById(R.id.rlFilter);
        linearLayoutSelectionCategory = rootView.findViewById(R.id.linearLayoutSelectionCategory);
        linearLayoutViewResults = rootView.findViewById(R.id.ll_view_result);
        linearLayoutGoBack = rootView.findViewById(R.id.linearLayoutBack);
        linearLayoutStoreHeader = rootView.findViewById(R.id.storeHeader);
        linearLayoutSearchHeader = rootView.findViewById(R.id.storeSearch);
        linearLayoutStoreMessages = rootView.findViewById(R.id.linearLayoutStoreMessages);
        buttonStoreDetails = rootView.findViewById(R.id.details_store);
        imageViewStoreImage = rootView.findViewById(R.id.adstoreImg);
        textViewCategoryName = rootView.findViewById(R.id.textViewCategoryName);
        textViewCategorySelect = rootView.findViewById(R.id.textViewCategory);

        relativeLayoutHomeToolbar = getActivity().findViewById(R.id.view_search);
        relativeLayoutStoreToolbar = getActivity().findViewById(R.id.search_storefront);

        //if (!searchKey.isEmpty())
            //tabLayout.setVisibility(View.VISIBLE);
        // relativeLayoutHomeToolbar.setVisibility(View.GONE);

    }

    @Override
    public void onResume() {
        super.onResume();
        checkFromStoreFront();
    }

    private void inflateSearchSpinnerList() {
        arrayListSearchFilter.add("All Locations");
        arrayListSearchFilter.add("Near by Locations");
        arrayListSearchFilter.add("Other");
    }


    private void setSearchFilterSpinnerAdapter() {
        spinnerLocation.setAdapter(new AdapterCategorySpinner(getActivity(), arrayListSearchFilter));
    }

    private void setParentListAdapter() {
        adapterParentCategories = new AdapterParentCategoriesItem(getActivity(), arrayListSelectParent, this);
        recycelerViewParentCategory.setAdapter(adapterParentCategories);
    }

    private void checkFromStoreFront() {
        storeFrontCategory = ATPreferences.readString(getActivity(), Constants.MERCHANT_CATEGORY);
        if (isFromStoreFront) {
            Picasso.get().load(ATPreferences.readString(getActivity(), Constants.KEY_IMAGE_URL) +
                    ATPreferences.readString(getActivity(), Constants.HEADER_IMG))
                    .placeholder(R.drawable.loading).into(imageViewStoreImage);

            storeFrontTabsView();
            merchantId = ATPreferences.readString(getActivity(), Constants.MERCHANT_ID);
            relativeLayoutAfterSelection.setVisibility(View.VISIBLE);
            //linearLayoutStoreHeader.setVisibility(View.VISIBLE);
            linearLayoutSelectionCategory.setVisibility(View.GONE);
        } else {
            defaultTabsView();
        }
        if (getArguments() != null && getArguments().getString("key") != null) {
            searchKey = Utils.convertStringToHex(getArguments().getString("key"));
            selectedCategoryId = getArguments().getString("categoryId");
            selectedUpdatedCategoryId = getArguments().getString("categoryId");
            selectedDeliveryId = getArguments().getString("deliveryId");
            locationName = getArguments().getString("location");
            String condition = getArguments().getString("condition");
            sortByID = getArguments().getString("sortby");
            editTextSearchZip.setText(getArguments().getString("zip"));
            selectedGrandParentId = getArguments().getString("grandParentId");
            selectedParentId = getArguments().getString("parentId");
            parentTitle = getArguments().getString("parentTitle");
            if (!isFromStoreFront)
                hideTabsView();
        }
        if (getArguments() != null && getArguments().containsKey(Constants.MERCHANT_CATEGORY_ID)) {
            selectedCategoryId = getArguments().getString(Constants.MERCHANT_CATEGORY_ID);
            selectedUpdatedCategoryId = getArguments().getString(Constants.MERCHANT_CATEGORY_ID);
        }

/*
        if (!searchKey.isEmpty()){
            selectedCategoryId = selectedLevelOneCategoryId;
            selectedUpdatedCategoryId = selectedLevelOneCategoryId;
        }
*/

/*
        if (!searchKey.isEmpty()){
            selectedCategoryId = selectedLevelOneCategoryId;
            ((HomeActivity) getActivity()).fetchAdsCategoryList(selectedCategoryId,false);
        }
*/

    }


    private void categorySpinnerListener() {
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                categorySelected(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void categorySelected(int position) {
        if (isFromStoreFront) {
            selectedCategoryId = browseCategoryResponse.getRESULT().get(position).getJsonMember11493();
            selectedLevelOneCategoryId = browseCategoryResponse.getRESULT().get(position).getJsonMember11493();
        }
        else {
            selectedCategoryId = levelOneCategoryResponse.getRESULT().get(0).getRESULT().get(position).get_11493();
            selectedLevelOneCategoryId = levelOneCategoryResponse.getRESULT().get(0).getRESULT().get(position).get_11493();
        }

        if (!isFirstTimeLoaded) {
            selectedGrandParentId = "";
            selectedParentId = "";
            parentTitle = "";
        }else{
            isFirstTimeLoaded =false;
            checkFromStoreFront();
        }


        notifyParentList(new ArrayList<SelectedParentModel>());
        if (adapterMerchantCategory != null)
            adapterMerchantCategory.customNotify(-1);
        if (adapterAds != null)
            adapterAds.customNotify(new ArrayList<RESULTItem>());

        fetchAdsList();
    }

    private void setSearchFilterSpinnerListener() {
        // SpinnerInteractionListener listener = new SpinnerInteractionListener();
        //spinnerSearchFilter.setOnTouchListener(listener);
        //spinnerSearchFilter.setOnItemSelectedListener(listener);

        spinnerLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 2)
                    editTextSearchZip.setVisibility(View.VISIBLE);
                else
                    editTextSearchZip.setVisibility(View.GONE);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void sortBySpinnerListener() {
        spinnerSortBy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sortByID = sortByListId().get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void inflateCategorySpinnerList() {
        for (int i = 0; i < levelOneCategoryResponse.getRESULT().get(0).getRESULT().size(); i++) {
            String name = levelOneCategoryResponse.getRESULT().get(0).getRESULT().get(i).get_12045();
            arrayListSpinnerCategory.add(name);

        }
    }

    private void setCategorySpinnerAdapter() {
        spinnerCategory.setAdapter(new AdapterCategorySpinner(getActivity(), arrayListSpinnerCategory));
    }

    private void setSortByAdapter() {
        spinnerSortBy.setAdapter(new AdapterCategorySpinner(getActivity(), sortByListNames()));
        sortByID = sortByListId().get(0);

    }

    private void setAdsAdapter() {
        adapterAds = new AdapterParentAds(getActivity(), adsListResponse.getRESULT().get(0).getRESULT(), this);
        recyclerViewAds.setAdapter(adapterAds);
    }

    private void setInitialCategoryListAdapter() {
        adapterCategories = new AdapterInitalCategories(getActivity(), arrayListSpinnerCategory, this);
        recyclerViewInitialCategory.setAdapter(adapterCategories);
    }


    private void touchListener() {
        relativeLayoutAfterSelection.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                setLeftPanelVisibility(false);
                return false;
            }
        });
    }

    private void clickListeners() {
        textViewFilter.setOnClickListener(this);
        imageViewFilter.setOnClickListener(this);
        linearLayoutStoreMessages.setOnClickListener(this);
        textViewSearch.setOnClickListener(this);
        textViewCategorySelect.setOnClickListener(this);
        linearLayoutViewResults.setOnClickListener(this);
        linearLayoutGoBack.setOnClickListener(this);
        buttonStoreDetails.setOnClickListener(this);
        viewLeftPanel.setOnClickListener(this);

    }


    private void setCategoryListAdapter() {
        adapterMerchantCategory = new AdapterMerchantCategoryItem(getActivity(), merchantCategoryListResponse.getRESULT().get(0).getRESULT(),
                this);
        rvMerchantCategory.setAdapter(adapterMerchantCategory);
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
    public void onEvent(final Event event) {
        switch (event.getKey()) {

            case Constants.GUEST_ACTIVITY_SUCCESS:
            case Constants.GUEST_ACTIVITY_TIMEOUT:
                break;
            case Constants.FCM_NOTIFICATION:
                fetchAdsList();
                break;
            case Constants.LEVEL_ONE_CATEGORY:
                hideProgress();
                levelOneCategoryResponse = ModelManager.getInstance().getMerchantStoresManager().levelOneCategoryModel;
                if (event.hasData()) {
                    inflateCategorySpinnerList();
                    categorySpinnerListener();
                    setCategorySpinnerAdapter();
                    setInitialCategoryListAdapter();
                    if (isFromStoreFront)
                        selectStoreSelected();
                } else {
                    Utils.baseshowFeedbackMessage(getActivity(), parentLayout, "No Level One Category Found");
                }

                break;

            case Constants.MERCHANT_CATEGORY_LIST:
                hideProgress();
                merchantCategoryListResponse = ModelManager.getInstance().getMerchantStoresManager().merchantCategoryListModel;
                if (event.hasData()) {
                    setCategoryListAdapter();
                    setParentList();
                } else {
                    adapterMerchantCategory.customNotify(categorySelectedPosition);
                    //  Utils.baseshowFeedbackMessage(getActivity(), parentLayout, "No Category Found");
                }

                break;
            case Constants.ADS_LIST_DATA:
                hideProgress();
                setLeftPanelVisibility(false);
                adsListResponse = ModelManager.getInstance().getAdsManager().storeFrontAdsModel;
                if (event.hasData()) {
                    if (getArguments()!=null&&getArguments().containsKey("key"))
                        ((HomeActivity) getActivity()).fetchAdsCategoryList(Utils.hexToASCII(searchKey),false);

                    setAdsAdapter();
                    checkForFilterHeader();

                } else {
                    Utils.baseshowFeedbackMessage(getActivity(), parentLayout, "No Ads Found");
                }

                break;


            case -1:
                hideProgress();
                break;


        }

    }

    private void checkForFilterHeader() {
        if (getArguments() != null && getArguments().containsKey("key")) {
            linearLayoutSearchHeader.setVisibility(View.GONE);
        }
        relativeLayoutStoreToolbar.setVisibility(View.GONE);
    }

    @Override
    public void storeHeaderVisibility(boolean isVisible) {
        if (isVisible)
            linearLayoutSearchHeader.setVisibility(View.GONE);
        else
            linearLayoutSearchHeader.setVisibility(View.VISIBLE);
    }


    private void selectStoreSelected() {
        if (isFromStoreFront) {
            for (int i = 0; i < browseCategoryResponse.getRESULT().size(); i++) {
                if (browseCategoryResponse.getRESULT().get(i).getJsonMember12045().equals(storeFrontCategory)) {
                    spinnerCategory.setSelection(i);
                    intialListCategoryClick(i);
                    break;
                }
            }
        } else {
            for (int i = 0; i < levelOneCategoryResponse.getRESULT().get(0).getRESULT().size(); i++) {
                if (levelOneCategoryResponse.getRESULT().get(0).getRESULT().get(i).get_12045().equals(storeFrontCategory)) {
                    spinnerCategory.setSelection(i);
                    intialListCategoryClick(i);
                    break;
                }
            }
        }
    }

    private void setParentList() {
        if (!parentTitle.isEmpty()) {
            SelectedParentModel selectedParentModel = new SelectedParentModel();
            selectedParentModel.setTitle(parentTitle);
            selectedParentModel.setParentId(selectedGrandParentId);
            selectedParentModel.setId(selectedParentId);
            arrayListSelectParent.add(selectedParentModel);
            notifyParentList(arrayListSelectParent);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textViewFilter:
            case R.id.imageViewFilter:
                if (viewLeftPanel.getVisibility() == View.VISIBLE)
                    setLeftPanelVisibility(false);
                else
                    setLeftPanelVisibility(true);
                break;
            case R.id.linearLayoutStoreMessages:

                Bundle bundle = new Bundle();
                bundle.putString("merchantId", merchantId);
                bundle.putString("className", "Home");
                bundle.putString("storeName", ATPreferences.readString(getActivity(), Constants.STORE_NAME));
                ((HomeActivity) getActivity()).displayView(new FragmentMessages(), Constants.TAG_MESSAGEPAGE, bundle);

                break;
            case R.id.ll_view_result:
                searchKey = "";
                fetchAdsList();
                break;
            case R.id.textViewCategory:
                if (rvMerchantCategory.getVisibility() == View.VISIBLE) {
                    rvMerchantCategory.setVisibility(View.GONE);
                    recycelerViewParentCategory.setVisibility(View.GONE);
                } else {
                    rvMerchantCategory.setVisibility(View.VISIBLE);
                    recycelerViewParentCategory.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.textViewSearch:
                searchKey = Utils.convertStringToHex(editTextSearchWord.getText().toString());
                fetchAdsList();
                break;
            case R.id.details_store:
                startActivity(new Intent(getActivity(), MerchantStoreDetails.class)
                        .putExtra("merchantId", merchantId));
                break;
            case R.id.linearLayoutBack:
                startActivity(new Intent(getActivity(), HomeActivity.class)
                        .putExtra(Constants.MERCHANT_ID, merchantId));
                getActivity().finish();
                break;
        }
    }


    @Override
    public void onInitialCategoryClick(int position) {
        intialListCategoryClick(position);
    }

    private void intialListCategoryClick(int position) {
        relativeLayoutAfterSelection.setVisibility(View.VISIBLE);
        linearLayoutSelectionCategory.setVisibility(View.GONE);

        linearLayoutSearchHeader.setVisibility(View.VISIBLE);
        spinnerCategory.setSelection(position);
        textViewCategoryName.setText(arrayListSpinnerCategory.get(position));
        categorySelected(position);
    }


    @Override
    public void onMerchantCategoryClick(int position) {
        categorySelectedPosition = position;
        selectedUpdatedCategoryId=merchantCategoryListResponse.getRESULT().get(0).getRESULT().get(position).get_11493();
        selectedParentId = merchantCategoryListResponse.getRESULT().get(0).getRESULT().get(position).get_11493();
        selectedGrandParentId = merchantCategoryListResponse.getRESULT().get(0).getRESULT().get(position).get_12221();
        parentTitle = merchantCategoryListResponse.getRESULT().get(0).getRESULT().get(position).get_12045();


        fetchAdsCategoryList();
        adapterMerchantCategory.customNotify(position);

        textViewCategorySelect.setText(parentTitle);
        rvMerchantCategory.setVisibility(View.GONE);
        recycelerViewParentCategory.setVisibility(View.GONE);

    }

    @Override
    public void onParentCategoryClick(final int position) {
        selectedParentId = arrayListSelectParent.get(position).getParentId();
        textViewCategorySelect.setText(arrayListSelectParent.get(position).getTitle());
        if (selectedParentId.equals(selectedCategoryId)) { //that means position selected is 0 position
            selectedParentId = "";
            selectedGrandParentId = "";
            selectedUpdatedCategoryId = selectedCategoryId;
            parentTitle = "";
            textViewCategorySelect.setText("Select Category");
        }
        selectedUpdatedCategoryId =  arrayListSelectParent.get(position).getParentId();
        rvMerchantCategory.setVisibility(View.GONE);
        recycelerViewParentCategory.setVisibility(View.GONE);
        fetchAdsCategoryList();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                arrayListSelectParent = removeItemsFromParentList(position);
                notifyParentList(arrayListSelectParent);
            }
        }, 1000);


    }


    private void notifyParentList(List<SelectedParentModel> arrayListSelectParent) {
        if (adapterParentCategories != null)
            adapterParentCategories.customNotify(arrayListSelectParent);
    }

    @SuppressLint("NewApi")
    private void setLeftPanelVisibility(boolean visibile) {
        if (!visibile) {
            viewLeftPanel.setVisibility(View.GONE);
            recyclerViewAds.setNestedScrollingEnabled(true);
            textViewFilter.setTextColor(getResources().getColor(R.color.darkBlue));
            relativeLayoutFilter.setBackground(getResources().getDrawable(R.drawable.rounded_blue_transparent_));
            imageViewFilter.setColorFilter(ContextCompat.getColor(getActivity(), R.color.dark_blue),
                    android.graphics.PorterDuff.Mode.MULTIPLY);
            imageViewFilter.setImageDrawable(getResources().getDrawable(R.drawable.ic_icon_downarrow));
        } else {
            // imageViewFilter.setImageDrawable(getResources().getDrawable(R.drawable.ic_icon_uparrow));
            viewLeftPanel.setVisibility(View.VISIBLE);
            recyclerViewAds.setNestedScrollingEnabled(false);
            if (merchantCategoryListResponse == null) {
                fetchAdsCategoryList();
            }
        }
    }


    private void fetchCategoryLvlOne() {
        if (isFromStoreFront) {
            browseCategoryResponse = ModelManager.getInstance().getMerchantStoresManager().browseCategoryModel;
            inflateStoreCategorySpinnerList();
            categorySpinnerListener();
            setCategorySpinnerAdapter();
            setInitialCategoryListAdapter();
            selectStoreSelected();

        } else {
            showProgress();
            ModelManager.getInstance().getMerchantStoresManager().getCategoryLvlOne
                    (getActivity(), Operations.getCategoriesLvlOne(getActivity()));
        }
    }

    private void inflateStoreCategorySpinnerList() {
        arrayListSpinnerCategory = new ArrayList<>();
        for (int i = 0; i < browseCategoryResponse.getRESULT().size(); i++) {
            arrayListSpinnerCategory.add(browseCategoryResponse.getRESULT().get(i).getJsonMember12045());
        }
    }

    private void fetchAdsCategoryList() {
        showProgress();
        String searchWord= "";
        if (getArguments()!=null&&getArguments().containsKey("key"))
            searchWord = getArguments().getString("key");


        ModelManager.getInstance().getMerchantStoresManager()
                .getMerchantCategoryList(getActivity(), Operations.getAdsCategoryList(getActivity(),
                        selectedUpdatedCategoryId, searchWord, selectedDeliveryId, selectedParentId, merchantId));
    }


    private void fetchAdsList() {
        showProgress();
        ModelManager.getInstance().getAdsManager().getAdsByCategoryList
                (getActivity(), Operations.makeJsonAdsByBusiness(getActivity(), selectedUpdatedCategoryId, searchKey, sortByID,
                        selectedParentId, merchantId, editTextSearchZip.getText().toString()));

    }


    private List<SelectedParentModel> removeItemsFromParentList(int position) {
        if (position == 0) {
            return new ArrayList<>();
        } else {
            return arrayListSelectParent.subList(0, position);
        }

    }

    @Override
    public void onAdsClick(String imageId, String imageUrl, String videoUrl, String
            merchant, String adName,
                           String description, String id, int position) {
        ModelManager.getInstance().setProductSeen().setProductSeen(getActivity(), Operations.makeProductSeen(getActivity(),
                id));

        Bundle bundle = new Bundle();
        bundle.putString("videoUrl", videoUrl);
        bundle.putString("image", imageUrl);
        bundle.putString("merchant", merchant);
        bundle.putString("adName", adName);
        bundle.putString("desc", description);
        bundle.putString("id", id);
        bundle.putString("ad_id", id);
        bundle.putString("merchantid", merchantId);
        bundle.putInt("adpos", position);
        ((HomeActivity) getActivity()).displayView(new FragmentAdDetail(), Constants.TAG_AD_DETAIL, bundle);

        storeFrontTabsView();
    }

    public class SpinnerInteractionListener implements AdapterView.OnItemSelectedListener, View.OnTouchListener {

        boolean userSelect = false;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (merchantCategoryListResponse == null) {
                if (waitCode())
                    fetchAdsCategoryList();
            }
            setLeftPanelVisibility(true);
            userSelect = true;
            return false;
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (userSelect) {
                if (position == 2)
                    editTextSearchZip.setVisibility(View.VISIBLE);
                else
                    editTextSearchZip.setVisibility(View.GONE);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }

    }

}

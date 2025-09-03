package com.apitap.views.fragments.specials.storefront;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
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
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.apitap.R;
import com.apitap.controller.ModelManager;
import com.apitap.model.Constants;
import com.apitap.model.Operations;
import com.apitap.model.Utils;
import com.apitap.model.bean.MerchantLocationBean;
import com.apitap.model.bean.SelectedParentModel;
import com.apitap.model.bean.levelOneCategories.LevelOneCategory;
import com.apitap.model.customclasses.Event;
import com.apitap.model.deliveryServices.DeliveryServiceModel;
import com.apitap.model.merchantCategoryList.MerchantCategoryListModel;
import com.apitap.model.preferences.ATPreferences;
import com.apitap.model.storeFrontItems.browseCategory.BrowseCategoryResponse;
import com.apitap.model.storeFrontSpecials.RESULTItem;
import com.apitap.model.storeFrontSpecials.StoreFrontSpecialsResponse;
import com.apitap.views.HomeActivity;
import com.apitap.views.MerchantStoreDetails;
import com.apitap.views.adapters.AdapterCategorySpinner;
import com.apitap.views.adapters.AdapterInitalCategories;
import com.apitap.views.fragments.BaseFragment;
import com.apitap.views.fragments.interfaces.SearchStoreClickListener;
import com.apitap.views.fragments.messages.FragmentMessages;
import com.apitap.views.fragments.items.adapter.AdapterCategoryListSpinner;
import com.apitap.views.fragments.items.adapter.AdapterMerchantCategoryItem;
import com.apitap.views.fragments.items.adapter.AdapterParentCategoriesItem;
import com.apitap.views.fragments.shoppingCart.ShoppingCartFragment;
import com.apitap.views.fragments.specials.AddPromoToOrderDialog;
import com.apitap.views.fragments.specials.data.AllProductsListResponse;
import com.apitap.views.fragments.specials.data.AppliedListItem;
import com.apitap.views.fragments.specials.data.OptionsProductPromoItem;
import com.apitap.views.fragments.specials.data.ProductItemWrapper;
import com.apitap.views.fragments.specials.data.PromotionListingResponse;
import com.apitap.views.fragments.specials.viewModel.SpecialsViewModel;
import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.apitap.App.isGuest;
import static com.apitap.views.fragments.specials.utils.CommonFunctions.promotionActiveProductResponse;
import static com.apitap.views.fragments.specials.utils.CommonFunctions.promotionCombinedProductList;
import static com.apitap.views.fragments.specials.utils.CommonFunctions.promotionByIdResponse;
import static com.apitap.views.fragments.specials.utils.CommonFunctions.promotionMerchantId;

public class FragmentSpecialStoreFront extends BaseFragment implements View.OnClickListener,
        AdapterInitalCategories.CategoriesItemClick,
        AdapterMerchantCategoryItem.MerchantCategoryClick,
        AdapterParentCategoriesItem.ParentCategoryClick, AdapterChildSpecials.SpecialListClick, SearchStoreClickListener {

    private View rootView;

    private ImageView imageViewFilter;
    private ImageView imageViewStoreImage;

    private RelativeLayout relativeLayoutAfterSelection;
    private RelativeLayout relativeLayoutHomeToolbar;
    private RelativeLayout relativeLayoutStoreToolbar;
    private RelativeLayout relativeLayoutFilter;

    private Button buttonStoreDetails;

    private LinearLayout linearLayoutSelectionCategory;
    private LinearLayout linearLayoutViewResults;
    private LinearLayout linearLayoutGoBack;
    private LinearLayout linearLayoutStoreHeader;
    private LinearLayout linearLayoutSearchHeader;
    private LinearLayout linearLayoutStoreMessages;

    private TextView textViewFilter;
    private TextView textViewSearch;
    private TextView textViewCategoryName;
    private TextView textViewCategorySelect;

    private EditText editTextSearchWord;
    private EditText editTextSearchZip;

    private RelativeLayout viewLeftPanel;
    private RelativeLayout parentLayout;

    private Spinner spinnerSortBy;
    private Spinner spinnerCategory;
    private Spinner spinnerDeliveryMethod;
    private Spinner spinnerSearchFilter;
    private Spinner spinnerCategoryFilter;

    private RecyclerView recycelerViewParentCategory;
    private ExpandableListView recyclerViewSpecial;
    private RecyclerView recyclerViewInitialCategory;
    private RecyclerView rvMerchantCategory;

    private AdapterMerchantCategoryItem adapterMerchantCategory;
    private AdapterParentSpecials adapterSpecials;
    private AdapterInitalCategories adapterCategories;
    private AdapterParentCategoriesItem adapterParentCategories;

    private ArrayList<String> arrayListSearchFilter = new ArrayList<>();
    private ArrayList<String> arrayListSpinnerCategory = new ArrayList<>();
    private ArrayList<String> arrayListSpinnerDelivery = new ArrayList<>();
    private List<SelectedParentModel> arrayListSelectParent = new ArrayList<>();

    private LevelOneCategory levelOneCategoryResponse;
    private DeliveryServiceModel deliveryServiceResponse;
    private MerchantCategoryListModel merchantCategoryListResponse;
    private StoreFrontSpecialsResponse specialListResponse;

    private String selectedPromotionId = "";
    private String searchKey = "";
    private String selectedCategoryId = "";
    private String selectedLevelOneCategoryId = "";
    private String selectedDeliveryId = "";
    private String selectedUpdatedCategoryId = "";
    private String selectedParentId = "";
    private String selectedGrandParentId = "";
    private String merchantId = "";
    private String parentTitle = "";
    private String sortByID = "";
    private String storeFrontCategory = "";

    private int categorySelectedPosition;
    public boolean isFirstTimeLoaded = true;
    private boolean isFromStoreFront;
    private String locationName = "";
    private BrowseCategoryResponse browseCategoryResponse;
    private SpecialsViewModel specialsViewModel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_specials_store_front, container, false);
        return rootView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        specialsViewModel = new ViewModelProvider(this).get(SpecialsViewModel.class);

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

        initObserver();
    }

    private void initObserver() {
        specialsViewModel.getPromotionsByIdResponse().observe(getViewLifecycleOwner(), event -> {
            if (event != null) {
                List<PromotionListingResponse> promoListings = event.getContentIfNotHandled();
                if (promoListings != null) {
                    promotionByIdResponse = new ArrayList<>(promoListings);
                    // Use the full products map (change to getActiveProductsMap() if you prefer that source)
                    Set<String> allProductIds = new HashSet<>();
                    for (int i = 0; i < promoListings.size(); i++) {
                        PromotionListingResponse promo = promoListings.get(i);
                        // requiredList
                        for (int k = 0; k < promo.getRequiredList().size(); k++) {
                            AppliedListItem item = promo.getRequiredList().get(k);
                            allProductIds.add(String.valueOf(item.getProductId()));
                        }

                        // appliedList
                        for (int j = 0; j < promo.getAppliedList().size(); j++) {
                            AppliedListItem item = promo.getAppliedList().get(j);
                            allProductIds.add(String.valueOf(item.getProductId()));
                        }
                    }

                    // Join IDs once and call API
                    String ids = TextUtils.join(",", allProductIds);
                    specialsViewModel.getActiveItemsByIds(ids);

                }

            }
        });
        specialsViewModel.getActiveProductResponse().observe(getViewLifecycleOwner(), event -> {
            if (event != null) {
                hideProgress();
                List<AllProductsListResponse> response = event.getContentIfNotHandled();
                if (response != null) {
                    promotionActiveProductResponse = new ArrayList<>(response);
                    Map<Integer, AllProductsListResponse> allProductsMap = getActiveProductsMap();
                    List<ProductItemWrapper> requiredItems = new ArrayList<>();
                    List<ProductItemWrapper> appliedItems = new ArrayList<>();

                    for (int i = 0; i < promotionByIdResponse.size(); i++) {
                        PromotionListingResponse promo = promotionByIdResponse.get(i);

                        // requiredList
                        for (int k = 0; k < promo.getRequiredList().size(); k++) {
                            AppliedListItem item = promo.getRequiredList().get(k);

                            List<OptionsProductPromoItem> matchingOptions = null;
                            AllProductsListResponse prod = allProductsMap.get(item.getProductId());
                            if (prod != null) matchingOptions = prod.getOptions();

                            item.setOptions(matchingOptions);
                            item.setRequiredItem(true);

                            requiredItems.add(new ProductItemWrapper(item, false));
                        }

                        // appliedList
                        for (int j = 0; j < promo.getAppliedList().size(); j++) {
                            AppliedListItem item = promo.getAppliedList().get(j);

                            List<OptionsProductPromoItem> matchingOptions = null;
                            AllProductsListResponse prod = allProductsMap.get(item.getProductId());
                            if (prod != null) matchingOptions = prod.getOptions();

                            item.setOptions(matchingOptions);
                            item.setRequiredItem(false);

                            appliedItems.add(new ProductItemWrapper(item, true));
                        }
                    }

                    promotionCombinedProductList = new ArrayList<>(requiredItems.size() + appliedItems.size());
                    promotionCombinedProductList.addAll(requiredItems);
                    promotionCombinedProductList.addAll(appliedItems);


                    if (getActivity() != null) {
                        AddPromoToOrderDialog.INSTANCE.show(
                                getActivity(),
                                selectedPromotionId,
                                promotionByIdResponse.get(0),
                                true,
                                null,
                                () -> showProgress()
                        );
                    }

                }
            }
        });

    }

    public Map<Integer, AllProductsListResponse> getActiveProductsMap() {
        Map<Integer, AllProductsListResponse> activeProductsMap = new HashMap<>();
        for (AllProductsListResponse product : promotionActiveProductResponse) {
            if (product.getProductId() != null) {
                activeProductsMap.put(product.getProductId(), product);
            }
        }
        return activeProductsMap;
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
        spinnerSearchFilter.setAdapter(new AdapterCategorySpinner(getActivity(), arrayListSearchFilter));
    }


    private void setParentListAdapter() {
        adapterParentCategories = new AdapterParentCategoriesItem(getActivity(), arrayListSelectParent, this);
        recycelerViewParentCategory.setAdapter(adapterParentCategories);
    }

    private void checkFromStoreFront() {
        storeFrontCategory = ATPreferences.readString(getActivity(), Constants.MERCHANT_CATEGORY);
        if (isFromStoreFront) {
            storeFrontTabsView();
            Picasso.get().load(ATPreferences.readString(getActivity(), Constants.KEY_IMAGE_URL) +
                            ATPreferences.readString(getActivity(), Constants.HEADER_IMG))
                    .placeholder(R.drawable.loading).into(imageViewStoreImage);
            merchantId = ATPreferences.readString(getActivity(), Constants.MERCHANT_ID);
        } else {
            defaultTabsView();
        }

        if (getArguments() != null && getArguments().getString("key") != null) {
            searchKey = getArguments().getString("key");
            selectedCategoryId = getArguments().getString("categoryId");
            selectedUpdatedCategoryId = getArguments().getString("categoryId");
            selectedDeliveryId = getArguments().getString("deliveryId");
            locationName = getArguments().getString("location");
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

    }

    private void setSearchFilterSpinnerListener() {
        //SpinnerInteractionListener listener = new SpinnerInteractionListener();
        //spinnerSearchFilter.setOnTouchListener(listener);
        // spinnerSearchFilter.setOnItemSelectedListener(listener);
        spinnerSearchFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        } else {
            selectedLevelOneCategoryId = levelOneCategoryResponse.getRESULT().get(0).getRESULT().get(position).get_11493();
            selectedCategoryId = levelOneCategoryResponse.getRESULT().get(0).getRESULT().get(position).get_11493();
        }
        if (!isFirstTimeLoaded) {
            selectedGrandParentId = "";
            selectedParentId = "";
            parentTitle = "";
            selectedDeliveryId = "";
        } else {
            isFirstTimeLoaded = false;
            checkFromStoreFront();
        }


        notifyParentList(new ArrayList<SelectedParentModel>());
        if (adapterMerchantCategory != null)
            adapterMerchantCategory.customNotify(-1);
        if (adapterSpecials != null)
            adapterSpecials.customNotify(new ArrayList<RESULTItem>());

        fetchSpecialsList();
    }


    private void deliverySpinnerListener() {
        spinnerDeliveryMethod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0)
                    selectedDeliveryId = deliveryServiceResponse.getRESULT().get(0).getRESULT().get(position).get_12239();
                else
                    selectedDeliveryId = "";

                notifyParentList(new ArrayList<SelectedParentModel>());
                fetchSpecialsCategoryList();
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


    private void initViews() {
        TabLayout tabLayout = getActivity().findViewById(R.id.tabs);
        imageViewFilter = rootView.findViewById(R.id.imageViewFilter);
        imageViewStoreImage = rootView.findViewById(R.id.adstoreImg);
        textViewFilter = rootView.findViewById(R.id.textViewFilter);
        recycelerViewParentCategory = rootView.findViewById(R.id.recyclerViewParentCategory);
        textViewSearch = rootView.findViewById(R.id.textViewSearch);
        editTextSearchZip = rootView.findViewById(R.id.editTextSearchZip);
        editTextSearchWord = rootView.findViewById(R.id.editTextSearchWord);
        viewLeftPanel = rootView.findViewById(R.id.scrollViewLeftPanel);
        parentLayout = rootView.findViewById(R.id.parentLayout);
        rvMerchantCategory = rootView.findViewById(R.id.recyclerViewMerchantCategory);
        spinnerCategory = rootView.findViewById(R.id.spinnerCategory);
        spinnerSortBy = rootView.findViewById(R.id.spinnerSortBy);
        spinnerSearchFilter = rootView.findViewById(R.id.spinnerSearchFilter);
        spinnerCategoryFilter = rootView.findViewById(R.id.spinnerCategoryFilter);
        spinnerDeliveryMethod = rootView.findViewById(R.id.spinnerDeliveryMethod);
        recyclerViewSpecial = rootView.findViewById(R.id.recyclerViewSpecial);
        recyclerViewInitialCategory = rootView.findViewById(R.id.recyclerViewCategory);
        relativeLayoutAfterSelection = rootView.findViewById(R.id.relativeLayoutAfterSelection);
        buttonStoreDetails = rootView.findViewById(R.id.details_store);
        linearLayoutSearchHeader = rootView.findViewById(R.id.storeSearch);
        linearLayoutStoreMessages = rootView.findViewById(R.id.linearLayoutStoreMessages);
        linearLayoutSelectionCategory = rootView.findViewById(R.id.linearLayoutSelectionCategory);
        linearLayoutViewResults = rootView.findViewById(R.id.ll_view_result);
        linearLayoutGoBack = rootView.findViewById(R.id.linearLayoutBack);
        linearLayoutStoreHeader = rootView.findViewById(R.id.storeHeader);
        textViewCategoryName = rootView.findViewById(R.id.textViewCategoryName);
        textViewCategorySelect = rootView.findViewById(R.id.textViewCategory);
        relativeLayoutFilter = rootView.findViewById(R.id.rlFilter);
        relativeLayoutHomeToolbar = getActivity().findViewById(R.id.view_search);
        relativeLayoutStoreToolbar = getActivity().findViewById(R.id.search_storefront);

        //tabLayout.setVisibility(View.VISIBLE);

        //relativeLayoutHomeToolbar.setVisibility(View.GONE);
    }


    private void inflateDeliveryList() {
        arrayListSpinnerDelivery = new ArrayList<>();
        arrayListSpinnerDelivery.add("All");
        for (int i = 0; i < deliveryServiceResponse.getRESULT().get(0).getRESULT().size(); i++) {
            String name = Utils.hexToASCII(deliveryServiceResponse.getRESULT().get(0).getRESULT().get(i).get_12239());
            arrayListSpinnerDelivery.add(name);
        }

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

    private void setDeliveryMethodSpinnerAdapter() {
        spinnerDeliveryMethod.setAdapter(new AdapterCategorySpinner(getActivity(), arrayListSpinnerDelivery));

        // spinnerDeliveryMethod.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, arrayListSpinnerDelivery));
    }

    private void setSortByAdapter() {
        spinnerSortBy.setAdapter(new AdapterCategorySpinner(getActivity(), sortByListNames()));
        sortByID = sortByListId().get(0);

    }

    private void setSpecialAdapter() {
        adapterSpecials = new AdapterParentSpecials(getActivity(),
                specialListResponse.getRESULT().get(0).getRESULT(), this);
        recyclerViewSpecial.setAdapter(adapterSpecials);

        for (int i = 0; i < adapterSpecials.getGroupCount(); i++) {
            recyclerViewSpecial.expandGroup(i);
        }


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
        textViewSearch.setOnClickListener(this);
        linearLayoutViewResults.setOnClickListener(this);
        textViewCategorySelect.setOnClickListener(this);
        linearLayoutGoBack.setOnClickListener(this);
        buttonStoreDetails.setOnClickListener(this);
        linearLayoutStoreMessages.setOnClickListener(this);
        viewLeftPanel.setOnClickListener(this);

    }


    private void setCategorySpinnerListAdapter() {
        AdapterCategoryListSpinner adapterCategoryListSpinner = new AdapterCategoryListSpinner(getActivity(), merchantCategoryListResponse.getRESULT().get(0).getRESULT());
        spinnerCategoryFilter.setAdapter(adapterCategoryListSpinner);
    }

    private void setCategoryListAdapter() {
        adapterMerchantCategory = new AdapterMerchantCategoryItem(getActivity(), merchantCategoryListResponse.getRESULT().get(0).getRESULT(), this);
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

            case Constants.SHOPPING_SUCCESS:
                hideProgress();
                showSuccessdialog();
                break;
            case Constants.GUEST_ACTIVITY_SUCCESS:
            case Constants.GUEST_ACTIVITY_TIMEOUT:
                break;
            case Constants.FCM_NOTIFICATION:
                fetchSpecialsList();
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

            case Constants.DELIVERY_SERVICES:
                hideProgress();
                deliveryServiceResponse = ModelManager.getInstance().getMerchantStoresManager().deliveryServiceModel;
                if (event.hasData()) {
                    inflateDeliveryList();
                    deliverySpinnerListener();
                    setDeliveryMethodSpinnerAdapter();
                } else {
                    //  Utils.baseshowFeedbackMessage(getActivity(), parentLayout, "No Delivery Data Found");
                }

                break;
            case Constants.MERCHANT_CATEGORY_LIST:
                hideProgress();
                merchantCategoryListResponse = ModelManager.getInstance().getMerchantStoresManager().merchantCategoryListModel;
                if (event.hasData()) {
                    setCategoryListAdapter();
                    setCategorySpinnerListAdapter();
                    setParentList();
                } else {
                    adapterMerchantCategory.customNotify(categorySelectedPosition);
                }

                break;
            case Constants.SPECIAL_LIST_DATA:
                hideProgress();
                setLeftPanelVisibility(false);
                specialListResponse = ModelManager.getInstance().getSpecialsManager().storeSpecialsModel;
                if (event.hasData()) {
                    if (getArguments() != null && getArguments().containsKey("key"))
                        ((HomeActivity) getActivity()).fetchSpecialsCategoryList(searchKey, false);

                    setSpecialAdapter();
                    checkForFilterHeader();
                    fetchDeliveryServices();
                } else {
                    if (adapterSpecials != null)
                        adapterSpecials.customNotify(new ArrayList<>());
                    Utils.baseshowFeedbackMessage(getActivity(), parentLayout, "No Item Found");
                }

                break;


            case Constants.GET_MERCHANT_LOCATION_SUCCESS:
                List<MerchantLocationBean.RESULT.MerchantLocationData> data = ModelManager.getInstance().getMerchantManager().merchantLocationBean.getRESULT().get(0).getRESULT();
                if (!data.isEmpty()) {
                    merchantId = data.get(0).locationID;
                    promotionMerchantId = Integer.parseInt(merchantId);
                }

                specialsViewModel.promotionById(Integer.parseInt(selectedPromotionId));

                break;
            case -1:
                hideProgress();
                break;


        }

    }

    public void showSuccessdialog() {
        final Dialog dialog = new Dialog(requireActivity(), R.style.AppTheme_Dialog_MyDialogTheme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.setContentView(R.layout.customdialogcart);
        Button btncontinue = dialog.findViewById(R.id.continueshoping);
        Button btncheckout = dialog.findViewById(R.id.checkout);

        btncontinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btncheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((HomeActivity) getActivity()).displayView(new ShoppingCartFragment(), Constants.TAG_SHOPPING, new Bundle());
                dialog.dismiss();

            }
        });

        dialog.show();
        // dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setDimAmount(0.5f);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

    }

    private void checkForFilterHeader() {
        if (getArguments() != null && getArguments().containsKey("key")) {
            linearLayoutSearchHeader.setVisibility(View.GONE);
        }
        relativeLayoutStoreToolbar.setVisibility(View.GONE);
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

    private void intialListCategoryClick(int position) {
        relativeLayoutAfterSelection.setVisibility(View.VISIBLE);
        linearLayoutSearchHeader.setVisibility(View.VISIBLE);
        linearLayoutSelectionCategory.setVisibility(View.GONE);
        spinnerCategory.setSelection(position);
        textViewCategoryName.setText(arrayListSpinnerCategory.get(position));
        categorySelected(position);
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
            case R.id.textViewCategory:
                if (rvMerchantCategory.getVisibility() == View.VISIBLE) {
                    rvMerchantCategory.setVisibility(View.GONE);
                    recycelerViewParentCategory.setVisibility(View.GONE);
                } else {
                    rvMerchantCategory.setVisibility(View.VISIBLE);
                    recycelerViewParentCategory.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.ll_view_result:
                searchKey = editTextSearchWord.getText().toString();
                fetchSpecialsList();
                break;
            case R.id.textViewSearch:
                searchKey = Utils.convertStringToHex(editTextSearchWord.getText().toString());
                fetchSpecialsList();
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
        relativeLayoutAfterSelection.setVisibility(View.VISIBLE);
        linearLayoutSearchHeader.setVisibility(View.VISIBLE);
        linearLayoutSelectionCategory.setVisibility(View.GONE);
        spinnerCategory.setSelection(position);
        textViewCategoryName.setText(arrayListSpinnerCategory.get(position));
        categorySelected(position);
    }


    private void notifyParentList(List<SelectedParentModel> arrayListSelectParent) {
        if (adapterParentCategories != null)
            adapterParentCategories.customNotify(arrayListSelectParent);
    }

    @SuppressLint("NewApi")
    private void setLeftPanelVisibility(boolean visibile) {
        if (!visibile) {
            viewLeftPanel.setVisibility(View.GONE);
            recyclerViewSpecial.setNestedScrollingEnabled(true);
            textViewFilter.setTextColor(getResources().getColor(R.color.darkBlue));
            relativeLayoutFilter.setBackground(getResources().getDrawable(R.drawable.rounded_blue_transparent_));
            imageViewFilter.setColorFilter(ContextCompat.getColor(getActivity(), R.color.dark_blue),
                    android.graphics.PorterDuff.Mode.MULTIPLY);
            imageViewFilter.setImageDrawable(getResources().getDrawable(R.drawable.ic_icon_downarrow));
        } else {
            viewLeftPanel.setVisibility(View.VISIBLE);
            recyclerViewSpecial.setNestedScrollingEnabled(false);
            textViewFilter.setTextColor(getResources().getColor(R.color.colorWhite));
            relativeLayoutFilter.setBackground(getResources().getDrawable(R.drawable.round_dark_blue_white_outline_));
            imageViewFilter.setColorFilter(ContextCompat.getColor(getActivity(), R.color.colorWhite),
                    android.graphics.PorterDuff.Mode.MULTIPLY);
            imageViewFilter.setImageDrawable(getResources().getDrawable(R.drawable.ic_icon_uparrow));

//            if (deliveryServiceResponse == null) {
//                fetchDeliveryServices();
//            }

        }
    }

    @Override
    public void onMerchantCategoryClick(int position) {
        categorySelectedPosition = position;
        selectedUpdatedCategoryId = merchantCategoryListResponse.getRESULT().get(0).getRESULT().get(position).get_11493();
        selectedParentId = merchantCategoryListResponse.getRESULT().get(0).getRESULT().get(position).get_11493();
        selectedGrandParentId = merchantCategoryListResponse.getRESULT().get(0).getRESULT().get(position).get_12221();
        parentTitle = merchantCategoryListResponse.getRESULT().get(0).getRESULT().get(position).get_12045();
        fetchSpecialsCategoryList();
        adapterMerchantCategory.customNotify(position);

        textViewCategorySelect.setText(parentTitle);
        rvMerchantCategory.setVisibility(View.GONE);
        recycelerViewParentCategory.setVisibility(View.GONE);

        // adapterMerchantCategory.customNotify(position);
    }

    private void fetchCategoryLvlOne() {
        if (isFromStoreFront) {
            browseCategoryResponse = ModelManager.getInstance().getMerchantStoresManager().browseCategoryModel;
            inflateStoreCategorySpinnerList();
            categorySpinnerListener();
            setCategorySpinnerAdapter();
            setInitialCategoryListAdapter();
            if (isFromStoreFront)
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


    private void fetchDeliveryServices() {
        showProgress();
        ModelManager.getInstance().getMerchantStoresManager()
                .getDeliveryServices(getActivity(), Operations.getDeliveryServices(getActivity(),
                        selectedUpdatedCategoryId, searchKey));

    }

    private void fetchSpecialsCategoryList() {
        showProgress();
        String searchWord = "";
        if (getArguments() != null && getArguments().containsKey("key"))
            searchWord = getArguments().getString("key");

        ModelManager.getInstance().getMerchantStoresManager()
                .getMerchantCategoryList(getActivity(), Operations.getSpecialsCategoryList(getActivity(),
                        selectedUpdatedCategoryId, searchWord, selectedDeliveryId, selectedParentId, merchantId));
    }


    private void fetchSpecialsList() {
        showProgress();
        ModelManager.getInstance().getSpecialsManager().getCategorySpecialsList
                (getActivity(), Operations.getCategoriesSpecialsList(getActivity(), selectedUpdatedCategoryId, searchKey, sortByID,
                        selectedParentId, selectedDeliveryId, merchantId, editTextSearchZip.getText().toString()));

    }

    @Override
    public void onSpecialsClick(String productId, String productType, String merchantId) {
        showProgress();
        selectedPromotionId = productId;
        ModelManager.getInstance().setProductSeen().setProductSeen(getActivity(), Operations.makeProductSeen(getActivity(),
                Utils.getElevenDigitId(productId)));

        ModelManager.getInstance().getMerchantManager().getMerchantLocation(requireContext(),
                Operations.makeJsonGetMerchantLocation(requireActivity(), merchantId), Constants.GET_MERCHANT_LOCATION_SUCCESS);


        Bundle bundle = new Bundle();
        bundle.putString("productId", productId);
        bundle.putString("productType", productType);


//        FragmentItemDetails fragment = new FragmentItemDetails();
//        fragment.setArguments(bundle);

//        ((HomeActivity) getActivity()).displayAddView(fragment, Constants.TAG_SPECIAL, Constants.TAG_DETAILSPAGE, bundle);

//        storeFrontTabsView();
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
        selectedUpdatedCategoryId = arrayListSelectParent.get(position).getParentId();
        rvMerchantCategory.setVisibility(View.GONE);
        recycelerViewParentCategory.setVisibility(View.GONE);
        fetchSpecialsCategoryList();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                arrayListSelectParent = removeItemsFromParentList(position);
                notifyParentList(arrayListSelectParent);
            }
        }, 1000);


    }

    private List<SelectedParentModel> removeItemsFromParentList(int position) {
        if (position == 0) {
            return new ArrayList<>();
        } else {
            return arrayListSelectParent.subList(0, position);
        }

    }

    @Override
    public void storeHeaderVisibility(boolean isVisible) {
        if (isVisible)
            linearLayoutSearchHeader.setVisibility(View.GONE);
        else
            linearLayoutSearchHeader.setVisibility(View.VISIBLE);
    }

    public class SpinnerInteractionListener implements AdapterView.OnItemSelectedListener, View.OnTouchListener {

        boolean userSelect = false;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (deliveryServiceResponse == null) {
//                if (waitCode())
//                    fetchDeliveryServices();
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

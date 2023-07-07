package com.apitap.views.fragments.stores;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.apitap.model.bean.SelectedParentModel;
import com.apitap.model.bean.merchantStores.MerchantStoreBean;
import com.apitap.model.bean.merchantStores.RESULT;
import com.apitap.model.preferences.ATPreferences;
import com.apitap.model.storeCategories.StoreCategoryResponse;
import com.apitap.views.HomeActivity;
import com.apitap.views.fragments.BaseFragment;
import com.apitap.views.fragments.storefront.FragmentStoreFront;
import com.apitap.views.fragments.items.adapter.AdapterMerchantCategoryItem;
import com.apitap.views.fragments.items.adapter.AdapterParentCategoriesItem;
import com.apitap.views.fragments.stores.adapters.AdapterParentStore;
import com.apitap.views.fragments.stores.adapters.AdapterStoreByCategory;
import com.apitap.views.fragments.stores.adapters.AdapterStores;
import com.google.android.material.tabs.TabLayout;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.apitap.R;
import com.apitap.controller.ModelManager;
import com.apitap.model.Constants;
import com.apitap.model.Operations;
import com.apitap.model.Utils;
import com.apitap.model.bean.levelOneCategories.LevelOneCategory;
import com.apitap.model.customclasses.Event;
import com.apitap.model.merchantCategoryList.MerchantCategoryListModel;
import com.apitap.views.adapters.AdapterInitalCategories;
import com.apitap.views.adapters.AdapterCategorySpinner;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static com.apitap.App.isGuest;

public class FragmentStore extends BaseFragment implements View.OnClickListener, AdapterStoreByCategory.StoreItemClick,
        AdapterInitalCategories.CategoriesItemClick,
        AdapterParentCategoriesItem.ParentCategoryClick,
        AdapterMerchantCategoryItem.MerchantCategoryClick, AdapterStores.StoreItemClick {

    private View rootView;

    private ImageView imageViewFilter;
    private ImageView imageViewExpand;

    private RelativeLayout relativeLayoutAfterSelection;
    private RelativeLayout relativeLayoutHomeToolbar;
    private RelativeLayout relativeLayoutStoreToolbar;

    private LinearLayout linearLayoutSelectionCategory;
    private LinearLayout linearLayoutViewResults;
    private LinearLayout linearLayoutSearchFilter;
    private LinearLayout linearLayoutExpandCollapse;
    private LinearLayout linearLayoutTitle;
    private LinearLayout linearLayoutStoreHeader;

    private TextView textViewExpand;
    private TextView textViewCategorySelect;
    private TextView textViewFilter;
    private TextView textViewTopTitle;

    private EditText editTextZipCode;

    private AutoCompleteTextView editTextSearch;

    private RelativeLayout viewLeftPanel;
    private RelativeLayout parentLayout;
    private RelativeLayout relativeLayoutFilter;

    private Spinner spinnerLocations;
    private Spinner spinnerCategory;
    private Spinner spinnerSortBy;

    private ExpandableListView recyclerViewStores;
    //private RecyclerView recyclerViewStores;
    private RecyclerView recyclerViewInitialCategory;
    private RecyclerView recycelerViewParentCategory;
    private RecyclerView rvMerchantCategory;

    private AdapterMerchantCategoryItem adapterMerchantCategory;
    private AdapterParentStore adapterCategoryStores;
    private AdapterInitalCategories adapterInitalCategories;
    private AdapterParentCategoriesItem adapterParentCategories;

    private AdapterStoreByCategory.StoreItemClick storeItemClick;

    private ArrayList<String> arrayListSpinnerCategory = new ArrayList<>();
    private List<SelectedParentModel> arrayListSelectParent = new ArrayList<>();
    private ArrayList<String> arrayListLocations = new ArrayList<>();
    private List<RESULT> arrayListStores = new ArrayList<>();

    private LevelOneCategory levelOneCategoryResponse;
    private MerchantCategoryListModel merchantCategoryListResponse;
    private MerchantCategoryListModel levelOneCategory;
    private StoreCategoryResponse merchantStoreCategoryListResponse;

    private int categorySelectedPosition;
    private String searchKey = "";
    private String searchZip = "";
    private String selectedCategoryId = "";
    private String categoryIdForStore = "";
    private String topTitle = "";
    private String selectedCategoryName = "";
    private String selectedDeliveryId = "";
    private String selectedParentId = "";
    private String parentTitle = "";
    private String sortByID = "";
    private String selectedGrandParentId = "";
    private String location = "";
    private boolean isMerchantCategoryLoading;
    private TabLayout tabLayout;
    private MerchantStoreBean merchantStoreListResponse;
    private AdapterStores adapterStores;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_stores, container, false);
        return rootView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        emptyValues();
        storeItemClick = this;
        initViews();

        clickListeners();

        inflateLocationSpinnerList();
        setSpinnerLocationsAdapter();
        locationSpinnerListener();


        sortBySpinnerListener();
        setSortByAdapter();

        setParentListAdapter();


        fetchCategoryLvlOne();
        if (isGuest) {
            ModelManager.getInstance().getLoginManager().guestLastActivity(getActivity(), Operations.makeJsonLastActivityByGuest(getActivity()));
        }

        if (getArguments() != null && getArguments().containsKey("key")) {
            relativeLayoutAfterSelection.setVisibility(View.VISIBLE);
            linearLayoutSearchFilter.setVisibility(View.VISIBLE);
            linearLayoutSelectionCategory.setVisibility(View.GONE);
        }
    }

    private void emptyValues() {
        selectedCategoryId = "";
        searchKey = "";
        selectedCategoryName = "";
        selectedCategoryId = "";
        categoryIdForStore = "";
        topTitle = "";
        selectedCategoryName = "";
        selectedDeliveryId = "";
        selectedParentId = "";
        String parentTitle = "";
        String sortByID = "";
        String selectedGrandParentId = "";
        String location = "";
    }

    private void initViews() {
        tabLayout = getActivity().findViewById(R.id.tabs);
        imageViewFilter = rootView.findViewById(R.id.imageViewFilter);
        imageViewExpand = rootView.findViewById(R.id.ivExpand);
        textViewFilter = rootView.findViewById(R.id.textViewFilter);
        textViewTopTitle = rootView.findViewById(R.id.title);
        editTextZipCode = rootView.findViewById(R.id.editTextSearchZip);
        editTextSearch = rootView.findViewById(R.id.editTextSearch);
        viewLeftPanel = rootView.findViewById(R.id.scrollViewLeftPanel);
        relativeLayoutFilter = rootView.findViewById(R.id.rlFilter);
        parentLayout = rootView.findViewById(R.id.parentLayout);
        rvMerchantCategory = rootView.findViewById(R.id.recycelerViewMerchantCategory);
        recycelerViewParentCategory = rootView.findViewById(R.id.recyclerViewParentCategory);
        spinnerCategory = rootView.findViewById(R.id.spinnerCategory);
        spinnerLocations = rootView.findViewById(R.id.spinnerLocation);
        spinnerSortBy = rootView.findViewById(R.id.spinnerSortBy);
        recyclerViewStores = rootView.findViewById(R.id.recyclerViewStores);
        recyclerViewInitialCategory = rootView.findViewById(R.id.recyclerViewCategory);
        relativeLayoutAfterSelection = rootView.findViewById(R.id.relativeLayoutAfterSelection);
        linearLayoutSelectionCategory = rootView.findViewById(R.id.linearLayoutSelectionCategory);
        linearLayoutViewResults = rootView.findViewById(R.id.ll_view_result);
        linearLayoutViewResults = rootView.findViewById(R.id.ll_view_result);
        linearLayoutTitle = rootView.findViewById(R.id.title_ll);
        linearLayoutSearchFilter = rootView.findViewById(R.id.storeSearch);
        linearLayoutExpandCollapse = rootView.findViewById(R.id.expand_ll);
        textViewCategorySelect = rootView.findViewById(R.id.textViewCategory);
        textViewExpand = rootView.findViewById(R.id.tvExpand);
        relativeLayoutHomeToolbar = getActivity().findViewById(R.id.view_search);
        relativeLayoutStoreToolbar = getActivity().findViewById(R.id.search_storefront);
        linearLayoutStoreHeader = getActivity().findViewById(R.id.header);

        ////tabLayout.setVisibility(View.VISIBLE);
        linearLayoutStoreHeader.setVisibility(View.GONE);
        //defaultTabsView();
    }


    private void categorySpinnerListener() {
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                categorySelected(position);
                fetchMerchantStoresList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void categorySelected(int position) {
        if (getArguments() == null || !getArguments().containsKey("key")) {
            selectedCategoryId = levelOneCategory.getRESULT().get(0).getRESULT().get(position).get_11493();
            categoryIdForStore = levelOneCategory.getRESULT().get(0).getRESULT().get(position).get_11493();
            selectedCategoryName = levelOneCategory.getRESULT().get(0).getRESULT().get(position).get_12045();
        } else {

        }
        selectedGrandParentId = "";
        selectedParentId = "";
        parentTitle = "";
        textViewCategorySelect.setText("Select Category");
        linearLayoutTitle.setVisibility(View.GONE);
        topTitle = "";

        notifyParentList(new ArrayList<SelectedParentModel>());
        notifyMerchantCategoryList();


        setBusinessTabText(selectedCategoryName);
    }

    private void setSpinnerLocationsAdapter() {
        spinnerLocations.setAdapter(new AdapterCategorySpinner(getActivity(), arrayListLocations));
    }

    private void locationSpinnerListener() {
        spinnerLocations.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 2)
                    editTextZipCode.setVisibility(View.VISIBLE);
                else
                    editTextZipCode.setVisibility(View.GONE);

                location = arrayListLocations.get(position);
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
                sortByID = sortByListStoresId().get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void notifyMerchantCategoryList() {
        if (adapterMerchantCategory != null)
            adapterMerchantCategory.customNotify(-1);
    }

    private void inflateLocationSpinnerList() {
        arrayListLocations = new ArrayList<>();
        arrayListLocations.add("All Locations");
        arrayListLocations.add("Near by Locations");
        arrayListLocations.add("Other");
    }

    private void inflateCategorySpinnerList() {
        // arrayListSpinnerCategory.add("All");
        arrayListSpinnerCategory = new ArrayList<>();
        for (int i = 0; i < levelOneCategory.getRESULT().get(0).getRESULT().size(); i++) {
            String name = levelOneCategory.getRESULT().get(0).getRESULT().get(i).get_12045();
            arrayListSpinnerCategory.add(name);

        }
    }

    private void setParentListAdapter() {
        adapterParentCategories = new AdapterParentCategoriesItem(getActivity(), arrayListSelectParent, this);
        recycelerViewParentCategory.setAdapter(adapterParentCategories);
    }

    private void notifyParentList(List<SelectedParentModel> arrayListSelectParent) {
        if (adapterParentCategories != null)
            adapterParentCategories.customNotify(arrayListSelectParent);
    }


    private void setCategorySpinnerAdapter() {
        spinnerCategory.setAdapter(new AdapterCategorySpinner(getActivity(), arrayListSpinnerCategory));
    }


    private void setSortByAdapter() {
        spinnerSortBy.setAdapter(new AdapterCategorySpinner(getActivity(), sortByListStoresNames()));
        sortByID = sortByListStoresId().get(0);

    }

    private void setStoreAdapter() {
//        adapterStores = new AdapterStores(getActivity(), arrayListStores, this);
//        recyclerViewStores.setAdapter(adapterStores);

        adapterCategoryStores = new AdapterParentStore(getActivity(),
                merchantStoreCategoryListResponse.getRESULT().get(0).getRESULT(), storeItemClick);
        recyclerViewStores.setAdapter(adapterCategoryStores);

        for (int i = 0; i < adapterCategoryStores.getGroupCount(); i++) {
            recyclerViewStores.expandGroup(i);
        }

    }

    private void setInitialCategoryListAdapter() {
        adapterInitalCategories = new AdapterInitalCategories(getActivity(), arrayListSpinnerCategory, this);
        recyclerViewInitialCategory.setAdapter(adapterInitalCategories);
    }


    private void clickListeners() {
        textViewFilter.setOnClickListener(this);
        imageViewFilter.setOnClickListener(this);
        //textViewSearch.setOnClickListener(this);
        linearLayoutViewResults.setOnClickListener(this);
        textViewCategorySelect.setOnClickListener(this);
        linearLayoutExpandCollapse.setOnClickListener(this);
        viewLeftPanel.setOnClickListener(this);

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

            case Constants.GUEST_ACTIVITY_SUCCESS:
            case Constants.GUEST_ACTIVITY_TIMEOUT:
                break;
            case Constants.MERCHANT_CATEGORY_LIST_HOME:
                hideProgress();
                linearLayoutSelectionCategory.setVisibility(View.VISIBLE);
                levelOneCategory = ModelManager.getInstance().getMerchantStoresManager().merchantCategoryListModel;
                if (event.hasData()) {
                    inflateCategorySpinnerList();
                    categorySpinnerListener();
                    setCategorySpinnerAdapter();
                    setInitialCategoryListAdapter();
                    checkIfFromSearch();
                    checkIfFromHome();
                } else {
                    //  Utils.baseshowFeedbackMessage(getActivity(), parentLayout, "No Level One Category Found");
                }
                break;
            case Constants.LEVEL_ONE_CATEGORY:
                hideProgress();
                levelOneCategoryResponse = ModelManager.getInstance().getMerchantStoresManager().levelOneCategoryModel;
                if (event.hasData()) {
                    inflateCategorySpinnerList();
                    categorySpinnerListener();
                    setCategorySpinnerAdapter();
                    setInitialCategoryListAdapter();
                    checkIfFromSearch();
                } else {
                    Utils.baseshowFeedbackMessage(getActivity(), parentLayout, "No Level One Category Found");
                }

                break;


            case Constants.MERCHANT_CATEGORY_LIST:
                hideProgress();
                isMerchantCategoryLoading = false;
                merchantCategoryListResponse = ModelManager.getInstance().getMerchantStoresManager().merchantCategoryListModel;
                if (event.hasData()) {
                    setCategoryListAdapter();
                    setParentList();
                } else {
                    if (adapterMerchantCategory != null)
                        adapterMerchantCategory.customNotify(categorySelectedPosition);
                    //Utils.baseshowFeedbackMessage(getActivity(), parentLayout, "No children categories found related");
                }

                break;
            case Constants.CATEGORY_STORES:
                hideProgress();
                setLeftPanelVisibility(false);
                fetchMerchantCategoryList(false);
                if (!topTitle.isEmpty())
                    linearLayoutTitle.setVisibility(View.VISIBLE);
                textViewTopTitle.setText(topTitle);

                merchantStoreCategoryListResponse = ModelManager.getInstance().getMerchantStoresManager().storeCategoryResponse;
                if (event.hasData()) {
                    if (getArguments()!=null&&getArguments().containsKey("key"))
                        ((HomeActivity) getActivity()).fetchStoreCategoryList(Utils.hexToASCII(searchKey),false);

                    setStoreAdapter();
                } else {
                    if (adapterStores != null)
                        adapterStores.clear();
                    Utils.baseshowFeedbackMessage(getActivity(), parentLayout, "No Stores Found");
                }

                break;

            case Constants.MERCHANT_STORES_LIST:
                hideProgress();
                setLeftPanelVisibility(false);
                fetchMerchantCategoryList(false);
                if (!topTitle.isEmpty())
                    linearLayoutTitle.setVisibility(View.VISIBLE);
                textViewTopTitle.setText(topTitle);
                merchantStoreListResponse = ModelManager.getInstance().getMerchantStoresManager().merchantStoreBeanModel;
                arrayListStores = merchantStoreListResponse.getRESULT().get(0).getRESULT();
                if (event.hasData()) {
                    setStoreAdapter();
                    checkForFilterHeader();
                } else {
                    if (adapterStores != null)
                        adapterStores.clear();
                    Utils.baseshowFeedbackMessage(getActivity(), parentLayout, "No Stores Found");
                }

                break;

            case -1:
                hideProgress();
                break;


        }

    }

    private void checkIfFromHome() {
        if (getArguments()!=null&&getArguments().containsKey("categoryId")) {
            selectedCategoryId = getArguments().getString("categoryId");
            for (int i = 0; i < levelOneCategory.getRESULT().get(0).getRESULT().size(); i++) {
                if (selectedCategoryId.equals(levelOneCategory.getRESULT().get(0).getRESULT().get(i).get_11493())) {
                    initialCategoryClick(i);
                    break;
                }
            }
        }
    }

    private void checkForFilterHeader() {
        if (getArguments() != null && getArguments().containsKey("key")) {
            linearLayoutSearchFilter.setVisibility(View.GONE);
        } else {
            relativeLayoutStoreToolbar.setVisibility(View.GONE);
            relativeLayoutHomeToolbar.setVisibility(View.GONE);
            linearLayoutStoreHeader.setVisibility(View.GONE);
        }
    }


    private void checkIfFromSearch() {
        if (getArguments() != null && getArguments().containsKey("key")) {
            searchKey = Utils.convertStringToHex(getArguments().getString("key"));
            sortByID = getArguments().getString("sortby");
            categoryIdForStore = getArguments().getString("categoryId");
            selectedCategoryId = getArguments().getString("categoryId");
            selectedDeliveryId = getArguments().getString("deliveryId");
            searchZip = getArguments().getString("zip");
            selectedGrandParentId = getArguments().getString("grandParentId");
            selectedCategoryName = getArguments().getString(Constants.MERCHANT_CATEGORY);
            selectedParentId = getArguments().getString("parentId");
            parentTitle = getArguments().getString("parentTitle");
            editTextZipCode.setText(searchZip);
            for (int i = 0; i < levelOneCategory.getRESULT().get(0).getRESULT().size(); i++) {
                if (selectedCategoryId.equals(levelOneCategory.getRESULT().get(0).getRESULT().get(i).get_11493())) {
                    initialCategoryClick(i);
                    break;
                }
            }
            //if (!searchKey.isEmpty()){
            relativeLayoutAfterSelection.setVisibility(View.VISIBLE);
            linearLayoutSearchFilter.setVisibility(View.VISIBLE);
            linearLayoutSelectionCategory.setVisibility(View.GONE);

            new Handler().postDelayed(() -> linearLayoutSearchFilter.setVisibility(View.GONE), 500);
//                ((HomeActivity) getActivity()).fetchStoreCategoryList(selectedCategoryId,false);

            //}
        }
    }

    private void setParentList() {
        if (!parentTitle.isEmpty()) {
            SelectedParentModel selectedParentModel = new SelectedParentModel();
            selectedParentModel.setTitle(parentTitle);
            selectedParentModel.setParentId(selectedGrandParentId);
            selectedParentModel.setId(selectedParentId);
            arrayListSelectParent.add(selectedParentModel);

            arrayListSelectParent = removeDuplicates(arrayListSelectParent);
            notifyParentList(arrayListSelectParent);
        }
    }

    public List<SelectedParentModel> removeDuplicates(List<SelectedParentModel> list) {
        Set set = new TreeSet(new Comparator() {

            @Override
            public int compare(Object o1, Object o2) {
                if (((SelectedParentModel) o1).getId().equalsIgnoreCase(((SelectedParentModel) o2).getId())) {
                    return 0;
                }
                return 1;
            }
        });
        set.addAll(list);

        final List newList = new ArrayList(set);
        return newList;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.expand_ll:
                if (textViewExpand.getText().toString().equalsIgnoreCase(getResources().getString(R.string.expand_all))) {
                    for (int i = 0; i < adapterCategoryStores.getGroupCount(); i++) {
                        recyclerViewStores.expandGroup(i);
                    }
                    textViewExpand.setText(getResources().getString(R.string.collapse_all));
                    imageViewExpand.setImageResource(R.drawable.ic_collapse_all);
                } else {
                    for (int i = 0; i < adapterCategoryStores.getGroupCount(); i++) {
                        recyclerViewStores.collapseGroup(i);
                    }
                    textViewExpand.setText(getResources().getString(R.string.expand_all));
                    imageViewExpand.setImageResource(R.drawable.ic_expand_all);

                }
                break;
            case R.id.textViewFilter:
            case R.id.imageViewFilter:
                if (viewLeftPanel.getVisibility() == View.VISIBLE)
                    setLeftPanelVisibility(false);
                else
                    setLeftPanelVisibility(true);
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
                searchKey = "";
                fetchMerchantStoresList();
                setLeftPanelVisibility(false);
                break;
            case R.id.textViewSearch:
                searchKey = Utils.convertStringToHex(editTextSearch.getText().toString());
                fetchMerchantStoresList();
                break;

        }
    }


    // in used right nowwwwww
    @Override
    public void onStoreClick(String merchantId) {
        Bundle b = new Bundle();
        b.putBoolean(Constants.HEADER_STORE, true);
        b.putString(Constants.MERCHANT_ID, merchantId);
        b.putString(Constants.MERCHANT_CATEGORY_ID, categoryIdForStore);
        b.putString(Constants.MERCHANT_CATEGORY, selectedCategoryName);

        ATPreferences.putBoolean(getActivity(), Constants.HEADER_STORE, true);
        ATPreferences.putString(getActivity(), Constants.MERCHANT_ID, merchantId);
        ATPreferences.putString(getActivity(), Constants.MERCHANT_CATEGORY, selectedCategoryName);
        ATPreferences.putString(getActivity(), Constants.MERCHANT_CATEGORY_ID, categoryIdForStore);

        ((HomeActivity) getActivity()).displayView(new FragmentStoreFront(), Constants.TAG_STORESFRONTPAGE, b);

//        ((HomeActivity) getActivity()).displayAddView(new FragmentStoreFront(), Constants.TAG_STORESFRONTPAGE,
//                Constants.TAG_STORESPAGE, b);

        storeFrontTabsView();
    }


    @Override
    public void onInitialCategoryClick(int position) {
        initialCategoryClick(position);
    }

    private void initialCategoryClick(int position) {
        relativeLayoutAfterSelection.setVisibility(View.VISIBLE);
        linearLayoutSearchFilter.setVisibility(View.VISIBLE);
        linearLayoutSelectionCategory.setVisibility(View.GONE);
        spinnerCategory.setSelection(position);
        categorySelected(position);
    }


    @SuppressLint("NewApi")
    private void setLeftPanelVisibility(boolean visibile) {
        if (!visibile) {
            viewLeftPanel.setVisibility(View.GONE);
            linearLayoutExpandCollapse.setVisibility(View.VISIBLE);
            recyclerViewStores.setNestedScrollingEnabled(true);
            textViewFilter.setTextColor(getResources().getColor(R.color.colorGreenDarkLogo));
            relativeLayoutFilter.setBackground(getResources().getDrawable(R.drawable.rounded_blue_transparent_));
            imageViewFilter.setColorFilter(ContextCompat.getColor(getActivity(), R.color.colorGreenLogo),
                    android.graphics.PorterDuff.Mode.MULTIPLY);
            imageViewFilter.setImageDrawable(getResources().getDrawable(R.drawable.ic_icon_downarrow));
        } else {
            viewLeftPanel.setVisibility(View.VISIBLE);
            linearLayoutExpandCollapse.setVisibility(View.GONE);
            recyclerViewStores.setNestedScrollingEnabled(false);
            textViewFilter.setTextColor(getResources().getColor(R.color.colorWhite));
            relativeLayoutFilter.setBackground(getResources().getDrawable(R.drawable.round_dark_blue_white_outline_));
            imageViewFilter.setColorFilter(ContextCompat.getColor(getActivity(), R.color.colorWhite),
                    android.graphics.PorterDuff.Mode.MULTIPLY);
            imageViewFilter.setImageDrawable(getResources().getDrawable(R.drawable.ic_icon_uparrow));

            if (isMerchantCategoryLoading)
                showProgress();
        }
    }


    private void fetchCategoryLvlOne() {
        showProgress();
//        ModelManager.getInstance().getMerchantStoresManager().getCategoryLvlOne
//                (getActivity(), Operations.getCategoriesLvlOne(getActivity()));

        if (getArguments() != null && getArguments().containsKey("key")) {
            String catId = "";
            if (!getArguments().getString("categoryId").isEmpty())
                catId = getArguments().getString("categoryId");
            ModelManager.getInstance().getMerchantStoresManager()
                    .getMerchantCategoryListHome(getActivity(), Operations.getMerchantCategoryList(getActivity(),
                            catId, "", "", ""));
        } else {
            ModelManager.getInstance().getMerchantStoresManager()
                    .getMerchantCategoryListHome(getActivity(), Operations.getMerchantCategoryList(getActivity(),
                            "", "", "", ""));

        }
    }

    private void fetchMerchantCategoryList(boolean showProgress) {
        if (showProgress || viewLeftPanel.getVisibility() == View.VISIBLE)
            showProgress();
        isMerchantCategoryLoading = true;
        String searchWord= "";
        if (getArguments()!=null&&getArguments().containsKey("key"))
            searchWord = getArguments().getString("key");

        ModelManager.getInstance().getMerchantStoresManager()
                .getMerchantCategoryList(getActivity(), Operations.getMerchantCategoryList(getActivity(),
                        selectedCategoryId, searchWord, selectedDeliveryId, selectedParentId));
    }


    private void fetchMerchantStoresList() {
        showProgress();
        ModelManager.getInstance().getMerchantStoresManager().getStoreByCategory(getActivity(),
                Operations.getStoreByCategory(getActivity(), categoryIdForStore, searchKey,
                        sortByID.replaceAll("120.83","114.70")));

/*
        ModelManager.getInstance().getMerchantStoresManager().getMerchantStoresList
                (getActivity(), Operations.getSearchMerchantList(getActivity(), selectedCategoryId, searchKey, sortByID,
                        selectedParentId, selectedDeliveryId, editTextZipCode.getText().toString()));
*/

    }

    @Override
    public void onParentCategoryClick(final int position) {
        selectedParentId = arrayListSelectParent.get(position).getParentId();
        textViewCategorySelect.setText(arrayListSelectParent.get(position).getTitle());
        topTitle = arrayListSelectParent.get(position).getTitle();
        if (selectedParentId.equals(selectedCategoryId)) { //that means position selected is 0 position
            selectedParentId = "";
            selectedGrandParentId = "";
            categoryIdForStore = selectedCategoryId;
            parentTitle = "";
            textViewCategorySelect.setText("Select Category");
            linearLayoutTitle.setVisibility(View.GONE);
            topTitle = "";
        }
        fetchMerchantCategoryList(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                arrayListSelectParent = removeItemsFromParentList(position);
                notifyParentList(arrayListSelectParent);
            }
        }, 1000);


    }

    @Override
    public void onMerchantCategoryClick(int position) {
        categorySelectedPosition = position;
        selectedParentId = merchantCategoryListResponse.getRESULT().get(0).getRESULT().get(position).get_11493();
        categoryIdForStore = merchantCategoryListResponse.getRESULT().get(0).getRESULT().get(position).get_11493();

        parentTitle = merchantCategoryListResponse.getRESULT().get(0).getRESULT().get(position).get_12045();
        selectedGrandParentId = merchantCategoryListResponse.getRESULT().get(0).getRESULT().get(position).get_12221();
        fetchMerchantCategoryList(true);

        textViewCategorySelect.setText(parentTitle);
        topTitle = parentTitle;

        rvMerchantCategory.setVisibility(View.GONE);
        recycelerViewParentCategory.setVisibility(View.GONE);
    }


    private List<SelectedParentModel> removeItemsFromParentList(int position) {
        if (position == 0) {
            return new ArrayList<>();
        } else {
            return arrayListSelectParent.subList(0, position);
        }

    }

    //no categorised
    @Override
    public void onStoreClick(int position) {
        String merchantID = merchantStoreListResponse.getRESULT().get(0).getRESULT().get(position).get_114179();
        Bundle b = new Bundle();
        b.putBoolean(Constants.HEADER_STORE, true);
        b.putString(Constants.MERCHANT_ID, merchantID);

        ATPreferences.putString(getActivity(), Constants.MERCHANT_CATEGORY, selectedCategoryName);
        ATPreferences.putString(getActivity(), Constants.HEADER_IMG,
                merchantStoreListResponse.getRESULT().get(0).getRESULT().get(position).get_121170());
        ATPreferences.putBoolean(getActivity(), Constants.HEADER_STORE, true);
        ATPreferences.putString(getActivity(), Constants.MERCHANT_ID, merchantID);


        //   ((HomeActivity) getActivity()).displayView(new FragmentStoreFront(), Constants.TAG_STORESFRONTPAGE, b);
        ((HomeActivity) getActivity()).displayViewAddHide(new FragmentStoreFront(), Constants.TAG_STORESFRONTPAGE, Constants.TAG_STORESPAGE, b);

        storeFrontTabsView();
    }


}
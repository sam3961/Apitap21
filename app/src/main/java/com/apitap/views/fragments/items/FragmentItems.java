package com.apitap.views.fragments.items;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
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
import com.apitap.model.bean.itemStoreFront.ItemStoreFrontResponse;
import com.apitap.model.bean.itemStoreFront.RESULTItem;
import com.apitap.model.bean.levelOneCategories.LevelOneCategory;
import com.apitap.model.brandNames.BrandNamesResponse;
import com.apitap.model.customclasses.Event;
import com.apitap.model.deliveryServices.DeliveryServiceModel;
import com.apitap.model.merchantCategoryList.MerchantCategoryListModel;
import com.apitap.model.preferences.ATPreferences;
import com.apitap.model.ratings.RatingResponse;
import com.apitap.model.storeFrontItems.browseCategory.BrowseCategoryResponse;
import com.apitap.views.HomeActivity;
import com.apitap.views.MerchantStoreDetails;
import com.apitap.views.adapters.AdapterCategorySpinner;
import com.apitap.views.adapters.AdapterInitalCategories;
import com.apitap.views.fragments.BaseFragment;
import com.apitap.views.fragments.interfaces.SearchStoreClickListener;
import com.apitap.views.fragments.itemDetails.FragmentItemDetails;
import com.apitap.views.fragments.items.adapter.AdapterCategoryListSpinner;
import com.apitap.views.fragments.items.adapter.AdapterMerchantCategoryItem;
import com.apitap.views.fragments.items.adapter.AdapterParentCategoriesItem;
import com.apitap.views.fragments.items.storeFront.AdapterChildItems;
import com.apitap.views.fragments.items.storeFront.AdapterParentItem;
import com.apitap.views.fragments.items.storeFront.FragmentItemsStoreFront;
import com.apitap.views.fragments.messages.FragmentMessages;
import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static com.apitap.App.isGuest;

public class FragmentItems extends BaseFragment implements View.OnClickListener,
        AdapterInitalCategories.CategoriesItemClick,
        AdapterMerchantCategoryItem.MerchantCategoryClick, AdapterChildItems.ItemListClick,
        AdapterParentCategoriesItem.ParentCategoryClick, SearchStoreClickListener {

    private View rootView;

    private ImageView imageViewFilter;
    private ImageView imageViewStoreImage;
    private ImageView imageViewExpand;

    private Button buttonStoreDetails;

    private RelativeLayout relativeLayoutFilter;
    private RelativeLayout relativeLayoutAfterSelection;
    private RelativeLayout relativeLayoutHomeToolbar;
    private RelativeLayout relativeLayoutStoreToolbar;

    private LinearLayout linearLayoutSelectionCategory;
    private LinearLayout linearLayoutSearchHeader;
    private LinearLayout linearLayoutExpandCollapse;
    private LinearLayout linearLayoutViewResults;
    private LinearLayout linearLayoutGoBack;
    private LinearLayout linearLayoutStoreHeader;
    private LinearLayout linearLayoutStoreMessages;
    private ScrollView scrollView;

    private TextView textViewFilter;
    private TextView textViewCategoryName;
    private TextView textViewSearch;
    private TextView textViewCategorySelect;
    private TextView textViewExpand;

    private AutoCompleteTextView editTextSearch;
    private EditText editTextSearchWord;
    private EditText editTextSearchZip;

    private RelativeLayout viewLeftPanel;
    private RelativeLayout parentLayout;


    private Spinner spinnerCondition;
    private Spinner spinnerCategory;
    private Spinner spinnerCategoryFilter;
    private Spinner spinnerSortBy;
    private Spinner spinnerDeliveryMethod;
    private Spinner spinnerLocations;
    private Spinner spinnerBrandNames;
    private Spinner spinnerRating;

    private RecyclerView recycelerViewParentCategory;
    private RecyclerView recyclerViewInitialCategory;
    private RecyclerView rvMerchantCategory;
    private ExpandableListView expandableListViewItems;

    private AdapterMerchantCategoryItem adapterMerchantCategory;
    private AdapterParentItem adapterItems;
    private AdapterInitalCategories adapterCategories;
    private AdapterParentCategoriesItem adapterParentCategories;

    private List<String> arrayListSpinnerCondition = new ArrayList<>();
    private List<String> arrayListRatings = new ArrayList<>();
    private ArrayList<String> arrayListSpinnerCategory = new ArrayList<>();
    private ArrayList<String> arrayListSearchFilter = new ArrayList<>();
    private ArrayList<String> arrayListSpinnerDelivery = new ArrayList<>();
    private List<SelectedParentModel> arrayListSelectParent = new ArrayList<>();
    private ArrayList<String> arrayListBrandName = new ArrayList<>();

    private MerchantCategoryListModel levelOneCategoryResponse;
    private DeliveryServiceModel deliveryServiceResponse;
    private MerchantCategoryListModel merchantCategoryListResponse;
    private ItemStoreFrontResponse itemListResponse;
    private RatingResponse ratingsResponse;

    private String searchKey = "";
    private String selectedCategoryId = "";
    private String selectedDeliveryId = "";
    private String locationName = "";
    private String selectedParentId = "";
    private String selectedGrandParentId = "";
    private String merchantId = "";
    private String parentTitle = "";
    private String sortByID = "";
    private String storeFrontCategory = "";
    private String selectedConditionId = "";
    private String selectedRatingId = "";
    private String selectedBrandName = "";
    private boolean isFromStoreFront =false;

    private int categorySelectedPosition;
    private BrowseCategoryResponse browseCategoryResponse;
    private BrandNamesResponse brandNamesResponse;

    public static boolean isActive = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_item, container, false);
        return rootView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //isFromStoreFront = ATPreferences.readBoolean(getActivity(), Constants.HEADER_STORE);
        HomeActivity.setActiveFragment(this);

        initViews();

        clickListeners();
        touchListener();

        sortBySpinnerListener();
        setSortByAdapter();


        inflateSearchSpinnerList();
        setSearchFilterSpinnerAdapter();
        setSearchFilterSpinnerListener();

        inflateConditionSpinnerList();
        setConditionSpinnerAdapter();
        setConditionSpinnerListener();

        fetchCategoryLvlOne();

        setParentListAdapter();

        if (isGuest) {
            ModelManager.getInstance().getLoginManager().guestLastActivity(getActivity(), Operations.makeJsonLastActivityByGuest(getActivity()));
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        checkFromStoreFront();
    }

    private void initViews() {
        TabLayout tabLayout = getActivity().findViewById(R.id.tabs);
        imageViewFilter = rootView.findViewById(R.id.imageViewFilter);
        imageViewExpand = rootView.findViewById(R.id.ivExpand);
        imageViewStoreImage = rootView.findViewById(R.id.adstoreImg);
        linearLayoutGoBack = rootView.findViewById(R.id.linearLayoutBack);
        linearLayoutStoreHeader = rootView.findViewById(R.id.storeHeader);
        linearLayoutStoreMessages = rootView.findViewById(R.id.linearLayoutStoreMessages);
        buttonStoreDetails = rootView.findViewById(R.id.details_store);
        textViewFilter = rootView.findViewById(R.id.textViewFilter);
        scrollView = rootView.findViewById(R.id.scrollView);
        textViewCategoryName = rootView.findViewById(R.id.textViewCategoryName);
        recycelerViewParentCategory = rootView.findViewById(R.id.recyclerViewParentCategory);
        textViewSearch = rootView.findViewById(R.id.textViewSearch);
        editTextSearch = rootView.findViewById(R.id.editTextSearch);
        editTextSearchZip = rootView.findViewById(R.id.editTextSearchZip);
        editTextSearchWord = rootView.findViewById(R.id.editTextSearchWord);
        viewLeftPanel = rootView.findViewById(R.id.scrollViewLeftPanel);
        parentLayout = rootView.findViewById(R.id.parentLayout);
        rvMerchantCategory = rootView.findViewById(R.id.recyclerViewMerchantCategory);
        expandableListViewItems = rootView.findViewById(R.id.expandableListItems);
        spinnerCategory = rootView.findViewById(R.id.spinnerCategory);
        spinnerCondition = rootView.findViewById(R.id.spinnerCondition);
        spinnerCategoryFilter = rootView.findViewById(R.id.spinnerCategoryFilter);
        spinnerSortBy = rootView.findViewById(R.id.spinnerSortBy);
        spinnerDeliveryMethod = rootView.findViewById(R.id.spinnerDeliveryMethod);
        spinnerLocations = rootView.findViewById(R.id.spinnerSearchFilter);
        spinnerBrandNames = rootView.findViewById(R.id.spinnerBrand);
        spinnerRating = rootView.findViewById(R.id.spinnerRating);
        recyclerViewInitialCategory = rootView.findViewById(R.id.recyclerViewCategory);
        relativeLayoutFilter = rootView.findViewById(R.id.rlFilter);
        relativeLayoutAfterSelection = rootView.findViewById(R.id.relativeLayoutAfterSelection);
        linearLayoutSelectionCategory = rootView.findViewById(R.id.linearLayoutSelectionCategory);
        linearLayoutSearchHeader = rootView.findViewById(R.id.storeSearch);
        linearLayoutExpandCollapse = rootView.findViewById(R.id.expand_ll);
        linearLayoutViewResults = rootView.findViewById(R.id.ll_view_result);
        textViewCategorySelect = rootView.findViewById(R.id.textViewCategory);
        textViewExpand = rootView.findViewById(R.id.tvExpand);
        relativeLayoutHomeToolbar = getActivity().findViewById(R.id.view_search);
        relativeLayoutStoreToolbar = getActivity().findViewById(R.id.search_storefront);

        editTextSearchWord.setText(ATPreferences.readString(getActivity(), Constants.SEARCH_KEY));

        //  relativeLayoutHomeToolbar.setVisibility(View.GONE);

        //if (!searchKey.isEmpty())
            //tabLayout.setVisibility(View.VISIBLE);

    }

    private void setParentListAdapter() {
        adapterParentCategories = new AdapterParentCategoriesItem(getActivity(), arrayListSelectParent, this);
        recycelerViewParentCategory.setAdapter(adapterParentCategories);
    }

    private void checkFromStoreFront() {
        storeFrontCategory = ATPreferences.readString(getActivity(),Constants.MERCHANT_CATEGORY);
        if (isFromStoreFront) {
            Picasso.get().load(ATPreferences.readString(getActivity(), Constants.KEY_IMAGE_URL) +
                    ATPreferences.readString(getActivity(), Constants.HEADER_IMG))
                    .placeholder(R.drawable.loading).into(imageViewStoreImage);

            storeFrontTabsView();
            merchantId = ATPreferences.readString(getActivity(), Constants.MERCHANT_ID);

        } else {
            defaultTabsView();
        }
        storeFrontTabsView();

        if (getArguments() != null && getArguments().getString("key") != null) {
            searchKey = getArguments().getString("key");
            selectedCategoryId = getArguments().getString("categoryId");
            selectedDeliveryId = getArguments().getString("deliveryId");
            locationName = getArguments().getString("location");
            selectedConditionId = getArguments().getString("condition");
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
        }
    }

    private void setConditionSpinnerListener() {
        spinnerCondition.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        selectedConditionId = "2701";
                        break;
                    case 2:
                        selectedConditionId = "2702";
                        break;
                    case 3:
                        selectedConditionId = "2703";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setSearchFilterSpinnerListener() {
        //  SpinnerInteractionListener listener = new SpinnerInteractionListener();
        // spinnerSearchFilter.setOnTouchListener(listener);
        // spinnerSearchFilter.setOnItemSelectedListener(listener);
        spinnerLocations.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
            // selectedCategoryId = ATPreferences.readString(getActivity(), Constants.MERCHANT_CATEGORY_ID);//browseCategoryResponse.getRESULT().get(position).getJsonMember11493();
            //selectedCategoryId = browseCategoryResponse.getRESULT().get(position).getJsonMember11493();
            linearLayoutSearchHeader.setVisibility(View.VISIBLE);
         //   relativeLayoutStoreToolbar.setVisibility(View.GONE);
            Log.d("selectedCategoryId", selectedCategoryId);

        } else {
            selectedCategoryId = levelOneCategoryResponse.getRESULT().get(0).getRESULT().get(position).get_11493();
            relativeLayoutStoreToolbar.setVisibility(View.GONE);
        }

        selectedGrandParentId = "";
        selectedParentId = "";
        parentTitle = "";
        selectedDeliveryId = "";
        searchKey = "";
        selectedBrandName = "";
        selectedRatingId = "";

        notifyParentList(new ArrayList<SelectedParentModel>());
        if (adapterMerchantCategory != null)
            adapterMerchantCategory.customNotify(-1);
        if (adapterItems != null)
            adapterItems.customNotify(new ArrayList<RESULTItem>());
        fetchItemsList();
        // fetchDeliveryServices();

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

    private void ratingSpinnerListener() {
        spinnerRating.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0)
                    selectedRatingId = ratingsResponse.getRESULT().get(0).getRESULT().get(position - 1).getJsonMember12180();
                else
                    selectedRatingId = "";

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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
                fetchItemCategoryList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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
        arrayListSpinnerCategory = new ArrayList<>();
        for (int i = 0; i < levelOneCategoryResponse.getRESULT().get(0).getRESULT().size(); i++) {
            String name = levelOneCategoryResponse.getRESULT().get(0).getRESULT().get(i).get_12045();
            arrayListSpinnerCategory.add(name);

        }
    }


    private void inflateSearchSpinnerList() {
        arrayListSearchFilter.add("All Locations");
        arrayListSearchFilter.add("Near by Locations");
        arrayListSearchFilter.add("Other");
    }

    private void inflateConditionSpinnerList() {
        arrayListSpinnerCondition.add("New");
        arrayListSpinnerCondition.add("Used");
        arrayListSpinnerCondition.add("Refurbished");
    }

    private void inflateRatingList() {
        arrayListRatings = new ArrayList<>();
        arrayListRatings.add("Select Rating");
        for (int i = 0; i < ratingsResponse.getRESULT().get(0).getRESULT().size(); i++) {
            if (ratingsResponse.getRESULT().get(0).getRESULT().get(i).getJsonMember12180() != null) {
                String name = ratingsResponse.getRESULT().get(0).getRESULT().get(i).getJsonMember12180();
                String lastIndex = name.substring(name.length() - 1);
                arrayListRatings.add(lastIndex + " Star");
            }
        }

    }


    private void setCategorySpinnerAdapter() {
        spinnerCategory.setAdapter(new AdapterCategorySpinner(getActivity(), arrayListSpinnerCategory));
        if (isFromStoreFront) {
            for (int i = 0; i < arrayListSpinnerCategory.size(); i++) {
                if (ATPreferences.readString(getActivity(), Constants.MERCHANT_CATEGORY).equalsIgnoreCase(arrayListSpinnerCategory.get(i))) {
                    spinnerCategory.setSelection(i);
                    break;
                }
            }
        }
    }

    private void setConditionSpinnerAdapter() {
        spinnerCondition.setAdapter(new AdapterCategorySpinner(getActivity(), arrayListSpinnerCondition));
    }

    private void setSearchFilterSpinnerAdapter() {
        spinnerLocations.setAdapter(new AdapterCategorySpinner(getActivity(), arrayListSearchFilter));
    }

    private void setDeliveryMethodSpinnerAdapter() {
        spinnerDeliveryMethod.setAdapter(new AdapterCategorySpinner(getActivity(), arrayListSpinnerDelivery));
    }

    private void setSortByAdapter() {
        spinnerSortBy.setAdapter(new AdapterCategorySpinner(getActivity(), sortByListNames()));
        sortByID = sortByListId().get(0);
    }

    private void setRatingAdapter() {
        spinnerRating.setAdapter(new AdapterCategorySpinner(getActivity(), arrayListRatings));
    }


    private void setItemListAdapter() {
        adapterItems = new AdapterParentItem(getActivity(), itemListResponse.getRESULT().get(0).getRESULT(), this);
        expandableListViewItems.setAdapter(adapterItems);

        for ( int i = 0; i < adapterItems.getGroupCount(); i++ ) {
            expandableListViewItems.expandGroup(i);
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
        textViewCategorySelect.setOnClickListener(this);
        textViewSearch.setOnClickListener(this);
        linearLayoutViewResults.setOnClickListener(this);
        linearLayoutGoBack.setOnClickListener(this);
        buttonStoreDetails.setOnClickListener(this);
        linearLayoutExpandCollapse.setOnClickListener(this);
        linearLayoutStoreMessages.setOnClickListener(this);

    }


    private void setCategoryListAdapter() {
        adapterMerchantCategory = new AdapterMerchantCategoryItem(getActivity(),
                merchantCategoryListResponse.getRESULT().get(0).getRESULT(), this);
        rvMerchantCategory.setAdapter(adapterMerchantCategory);
    }

    private void setCategorySpinnerListAdapter() {
        AdapterCategoryListSpinner adapterCategoryListSpinner = new AdapterCategoryListSpinner(getActivity(), merchantCategoryListResponse.getRESULT().get(0).getRESULT());
        spinnerCategoryFilter.setAdapter(adapterCategoryListSpinner);
    }


    @Override
    public void onStart() {
        super.onStart();
        isActive = true;
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        isActive = false;
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(final Event event) {
        switch (event.getKey()) {

            case Constants.ITEMS_REFRESH:
                selectedCategoryId = event.getResponse();
                fetchItemsList();
                break;
            case Constants.GUEST_ACTIVITY_SUCCESS:
            case Constants.GUEST_ACTIVITY_TIMEOUT:
                break;
            case Constants.FCM_NOTIFICATION:
                fetchItemsList();
                break;

            case Constants.MERCHANT_CATEGORY_LIST_HOME:
                hideProgress();
                levelOneCategoryResponse = ModelManager.getInstance().getMerchantStoresManager().merchantCategoryListModel;
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

          /*  case Constants.LEVEL_ONE_CATEGORY:
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

                break;*/

            case Constants.DELIVERY_SERVICES:
                hideProgress();
                deliveryServiceResponse = ModelManager.getInstance().getMerchantStoresManager().deliveryServiceModel;
                if (event.hasData()) {
                    inflateDeliveryList();
                    deliverySpinnerListener();
                    setDeliveryMethodSpinnerAdapter();
                    fetchBrandNames();
                } else {
                   // Utils.baseshowFeedbackMessage(getActivity(), parentLayout, "No Delivery Data Found");
                }

                break;

            case Constants.BRAND_NAMES:
                hideProgress();
                brandNamesResponse = ModelManager.getInstance().getMerchantStoresManager().brandNamesModel;
                inflateBrandName();
                setBrandNameSpinnerAdapter();
                spinnerBrandNameListener();
                fetchRatingList();
                break;

            case Constants.RATINGS:
                hideProgress();
                ratingsResponse = ModelManager.getInstance().getMerchantStoresManager().ratingModel;
                inflateRatingList();
                setRatingAdapter();
                ratingSpinnerListener();
                break;


            case Constants.MERCHANT_CATEGORY_LIST:
                hideProgress();
                //setCategorySpinnerListener();
                merchantCategoryListResponse = ModelManager.getInstance().getMerchantStoresManager().merchantCategoryListModel;
                if (event.hasData()) {
                    setCategoryListAdapter();
                    setCategorySpinnerListAdapter();
                    setParentList();
                } else {
                    adapterMerchantCategory.customNotify(categorySelectedPosition);
                }

                break;
            case Constants.ITEM_LIST_DATA:
                hideProgress();
                setLeftPanelVisibility(false);
                itemListResponse = ModelManager.getInstance().getItemManager().itemStoreFrontListModel;
                if (event.hasData()) {
                    //setItemListAdapter();
                    setItemListAdapter();
                    checkForFilterHeader();
                    fetchDeliveryServices();
                } else {
                    Utils.baseshowFeedbackMessage(getActivity(), parentLayout, "No Item Found");
                }

                break;


            case -1:
                hideProgress();
                break;

        }

    }

    private void spinnerBrandNameListener() {
        spinnerBrandNames.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0)
                    selectedBrandName = brandNamesResponse.getRESULT().get(0).getRESULT().get(position).getJsonMember114149();
                else
                    selectedBrandName = "";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void inflateBrandName() {
        arrayListBrandName = new ArrayList<>();
        arrayListBrandName.add("Select Brand Name");
        for (int i = 0; i < brandNamesResponse.getRESULT().get(0).getRESULT().size(); i++) {
            if (brandNamesResponse.getRESULT().get(0).getRESULT().get(i).getJsonMember114149() != null) {
                String name = Utils.hexToASCII(brandNamesResponse.getRESULT().get(0).getRESULT().get(i).getJsonMember114149());
                arrayListBrandName.add(name);
            }
        }

    }

    private void setBrandNameSpinnerAdapter() {
        spinnerBrandNames.setAdapter(new AdapterCategorySpinner(getActivity(), arrayListBrandName));
    }


    private void checkForFilterHeader() {
        if (getArguments() != null && getArguments().containsKey("key")) {
            linearLayoutSearchHeader.setVisibility(View.GONE);
        }
        // relativeLayoutStoreToolbar.setVisibility(View.GONE);
    }

    private void setCategorySpinnerListener() {
        SpinnerInteractionListener listener = new SpinnerInteractionListener();
        spinnerCategoryFilter.setOnTouchListener(listener);
        spinnerCategoryFilter.setOnItemSelectedListener(listener);

        categorySelectedPosition = 0;
        selectedParentId = merchantCategoryListResponse.getRESULT().get(0).getRESULT().get(0).get_11493();
        selectedGrandParentId = merchantCategoryListResponse.getRESULT().get(0).getRESULT().get(0).get_12221();
        parentTitle = merchantCategoryListResponse.getRESULT().get(0).getRESULT().get(0).get_12045();

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

            arrayListSelectParent = removeDuplicates(arrayListSelectParent);

//            Set<SelectedParentModel> s= new HashSet<SelectedParentModel>();
//            s.addAll(arrayListSelectParent);
//            arrayListSelectParent = new ArrayList<SelectedParentModel>();
//            arrayListSelectParent.addAll(s);

            notifyParentList(arrayListSelectParent);
        }
    }

    public List<SelectedParentModel> removeDuplicates(List<SelectedParentModel> list) {
        // Set set1 = new LinkedHashSet(list);
        Set set = new TreeSet(new Comparator() {

            @Override
            public int compare(Object o1, Object o2) {
                if (((SelectedParentModel) o1).getId().equalsIgnoreCase(((SelectedParentModel) o2).getId()) /*&&
                    ((Blog)o1).getName().equalsIgnoreCase(((Blog)o2).getName())*/) {
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
                    for (int i = 0; i < adapterItems.getGroupCount(); i++) {
                        expandableListViewItems.expandGroup(i);
                    }
                    textViewExpand.setText(getResources().getString(R.string.collapse_all));
                    imageViewExpand.setImageResource(R.drawable.ic_collapse_all);
                } else {
                    for (int i = 0; i < adapterItems.getGroupCount(); i++) {
                        expandableListViewItems.collapseGroup(i);
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
//                    scrollView.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            scrollView.fullScroll(ScrollView.FOCUS_DOWN);
//                        }
//                    });
                }
                break;

            case R.id.ll_view_result:
                searchKey = editTextSearchWord.getText().toString();
                fetchItemsList();
                setLeftPanelVisibility(false);
                ATPreferences.putString(getActivity(), Constants.SEARCH_KEY, "");
                break;
            case R.id.textViewSearch:
                searchKey = Utils.convertStringToHex(editTextSearchWord.getText().toString());
                fetchItemsList();
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
       // intialListCategoryClick(position);
        selectedCategoryId = levelOneCategoryResponse.getRESULT().get(0).getRESULT().get(position).get_11493();
        Bundle bundle =new Bundle();
        bundle.putString(Constants.MERCHANT_CATEGORY_ID,selectedCategoryId);
            ((HomeActivity) getActivity()).displayView(new FragmentItemsStoreFront(), Constants.TAG_ITEMS_STOREFRONT, bundle);
    }

    private void intialListCategoryClick(int position) {
        relativeLayoutAfterSelection.setVisibility(View.VISIBLE);
        linearLayoutSearchHeader.setVisibility(View.VISIBLE);
        linearLayoutSelectionCategory.setVisibility(View.GONE);
        spinnerCategory.setSelection(position);
        textViewCategoryName.setText(arrayListSpinnerCategory.get(position));
        categorySelected(position);
    }

    @Override
    public void onMerchantCategoryClick(int position) {
        categorySelectedPosition = position;
        selectedParentId = merchantCategoryListResponse.getRESULT().get(0).getRESULT().get(position).get_11493();
        selectedGrandParentId = merchantCategoryListResponse.getRESULT().get(0).getRESULT().get(position).get_12221();
        parentTitle = merchantCategoryListResponse.getRESULT().get(0).getRESULT().get(position).get_12045();
        fetchItemCategoryList();
        adapterMerchantCategory.customNotify(position);

        textViewCategorySelect.setText(parentTitle);
        rvMerchantCategory.setVisibility(View.GONE);
        recycelerViewParentCategory.setVisibility(View.GONE);

    }

    @Override
    public void onItemsClick(String productId, String productType) {
        isActive = false;

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
    public void onParentCategoryClick(final int position) {
        selectedParentId = arrayListSelectParent.get(position).getParentId();
        textViewCategorySelect.setText(arrayListSelectParent.get(position).getTitle());
        if (selectedParentId.equals(selectedCategoryId)) { //that means position selected is 0 position
            selectedParentId = "";
            selectedGrandParentId = "";
            parentTitle = "";
            textViewCategorySelect.setText("Select Category");
        }
        rvMerchantCategory.setVisibility(View.GONE);
        recycelerViewParentCategory.setVisibility(View.GONE);
        fetchItemCategoryList();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                arrayListSelectParent = removeItemsFromParentList(position);
                notifyParentList(arrayListSelectParent);
            }
        }, 3000);


    }


    private void notifyParentList(List<SelectedParentModel> arrayListSelectParent) {
        if (adapterParentCategories != null)
            adapterParentCategories.customNotify(arrayListSelectParent);
    }

    private void setLeftPanelVisibility(boolean visibile) {
        if (!visibile) {
            viewLeftPanel.setVisibility(View.GONE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                expandableListViewItems.setNestedScrollingEnabled(true);
            }
            textViewFilter.setTextColor(getResources().getColor(R.color.darkBlue));
            relativeLayoutFilter.setBackground(getResources().getDrawable(R.drawable.rounded_blue_transparent_));
            imageViewFilter.setColorFilter(ContextCompat.getColor(getActivity(), R.color.dark_blue),
                    android.graphics.PorterDuff.Mode.MULTIPLY);
            imageViewFilter.setImageDrawable(getResources().getDrawable(R.drawable.ic_icon_downarrow));
        } else {
            //imageViewFilter.setImageDrawable(getResources().getDrawable(R.drawable.ic_icon_uparrow));
            viewLeftPanel.setVisibility(View.VISIBLE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                expandableListViewItems.setNestedScrollingEnabled(false);
            }
            textViewFilter.setTextColor(getResources().getColor(R.color.colorWhite));
            relativeLayoutFilter.setBackground(getResources().getDrawable(R.drawable.round_dark_blue_white_outline_));
            imageViewFilter.setColorFilter(ContextCompat.getColor(getActivity(), R.color.colorWhite),
                    android.graphics.PorterDuff.Mode.MULTIPLY);
            imageViewFilter.setImageDrawable(getResources().getDrawable(R.drawable.ic_icon_uparrow));

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
            ModelManager.getInstance().getMerchantStoresManager()
                    .getMerchantCategoryListHome(getActivity(), Operations.getItemCategoryList(getActivity(),
                            "", "", "", "", ""));
        }
    }

    private void inflateStoreCategorySpinnerList() {
        arrayListSpinnerCategory = new ArrayList<>();
        for (int i = 0; i < browseCategoryResponse.getRESULT().size(); i++) {
            arrayListSpinnerCategory.add(browseCategoryResponse.getRESULT().get(i).getJsonMember12045());
        }
    }


    private void fetchBrandNames() {
        showProgress();
        ModelManager.getInstance().getMerchantStoresManager()
                .getBrandNames(getActivity(),
                        Operations.getBrandNames(getActivity(), selectedCategoryId, searchKey, merchantId));

    }

    private void fetchRatingList() {
        showProgress();
        ModelManager.getInstance().getMerchantStoresManager()
                .getRatings(getActivity(),
                        Operations.getRatingList(getActivity(), selectedCategoryId, searchKey,
                                ATPreferences.readString(getActivity(), Constants.MERCHANT_ID)));

    }


    private void fetchDeliveryServices() {
        if (isProgressShowing())
            return;
        showProgress();
        ModelManager.getInstance().getMerchantStoresManager()
                .getDeliveryServices(getActivity(), Operations.getDeliveryServices(getActivity(), selectedCategoryId, searchKey));

    }

    private void fetchItemCategoryList() {
        showProgress();
        ModelManager.getInstance().getMerchantStoresManager()
                .getMerchantCategoryList(getActivity(), Operations.getItemCategoryList(getActivity(),
                        selectedCategoryId, searchKey, selectedDeliveryId, selectedParentId, merchantId));
    }


    private void fetchItemsList() {
        showProgress();
        ModelManager.getInstance().getItemManager().getItemCategoryItemsList
                (getActivity(), Operations.getCategoriesItemsList(getActivity(), selectedCategoryId, searchKey, sortByID,
                        selectedParentId, selectedDeliveryId, merchantId, editTextSearchZip.getText().toString()
                        , selectedBrandName, selectedRatingId));

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
                //  if (waitCode())
                //  fetchDeliveryServices();
            }
            setLeftPanelVisibility(true);
            userSelect = true;
            return false;
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (userSelect) {
             /*   userSelect = false;
                categorySelectedPosition = position;
                selectedParentId = merchantCategoryListResponse.getRESULT().get(0).getRESULT().get(position).get_11493();
                selectedGrandParentId = merchantCategoryListResponse.getRESULT().get(0).getRESULT().get(position).get_12221();
                parentTitle = merchantCategoryListResponse.getRESULT().get(0).getRESULT().get(position).get_12045();
                fetchItemCategoryList();*/

                if (position == 2)
                    editTextSearchZip.setVisibility(View.VISIBLE);
                else
                    editTextSearchZip.setVisibility(View.GONE);

//                setLeftPanelVisibility(true);
//                if (deliveryServiceResponse == null)
//                    fetchDeliveryServices();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }

    }
}

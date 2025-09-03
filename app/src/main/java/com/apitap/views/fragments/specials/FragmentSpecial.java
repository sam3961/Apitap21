package com.apitap.views.fragments.specials;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
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
import com.apitap.model.specialModel.RESULT;
import com.apitap.model.specialModel.SpecialModel;
import com.apitap.views.HomeActivity;
import com.apitap.views.MerchantStoreDetails;
import com.apitap.views.adapters.AdapterCategorySpinner;
import com.apitap.views.adapters.AdapterInitalCategories;
import com.apitap.views.fragments.BaseFragment;
import com.apitap.views.fragments.itemDetails.FragmentItemDetails;
import com.apitap.views.fragments.messages.FragmentMessages;
import com.apitap.views.fragments.items.adapter.AdapterCategoryListSpinner;
import com.apitap.views.fragments.items.adapter.AdapterMerchantCategoryItem;
import com.apitap.views.fragments.items.adapter.AdapterParentCategoriesItem;
import com.apitap.views.fragments.specials.data.AllProductsListResponse;
import com.apitap.views.fragments.specials.data.PromotionListingResponse;
import com.apitap.views.fragments.specials.storefront.FragmentSpecialStoreFront;
import com.apitap.views.fragments.specials.utils.CommonFunctions;
import com.apitap.views.fragments.specials.utils.SingleEvent;
import com.apitap.views.fragments.specials.viewModel.SpecialsViewModel;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import static com.apitap.App.isGuest;

public class FragmentSpecial extends BaseFragment implements View.OnClickListener,
        AdapterInitalCategories.CategoriesItemClick,
        AdapterMerchantCategoryItem.MerchantCategoryClick, AdapterSpecials.SpecialListClick, AdapterParentCategoriesItem.ParentCategoryClick {

    private View rootView;

    private TabLayout tabLayout;

    private ImageView imageViewFilter;
    private ImageView imageViewStoreImage;

    private RelativeLayout relativeLayoutFilter;
    private RelativeLayout relativeLayoutAfterSelection;

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
    private RecyclerView recyclerViewSpecial;
    private RecyclerView recyclerViewInitialCategory;
    private RecyclerView rvMerchantCategory;

    private AdapterMerchantCategoryItem adapterMerchantCategory;
    private AdapterSpecials adapterSpecials;
    private AdapterInitalCategories adapterCategories;
    private AdapterParentCategoriesItem adapterParentCategories;

    private ArrayList<String> arrayListSearchFilter = new ArrayList<>();
    private ArrayList<String> arrayListSpinnerCategory = new ArrayList<>();
    private ArrayList<String> arrayListSpinnerDelivery = new ArrayList<>();
    private List<SelectedParentModel> arrayListSelectParent = new ArrayList<>();

    private MerchantCategoryListModel levelOneCategory;
    private LevelOneCategory levelOneCategoryResponse;
    private DeliveryServiceModel deliveryServiceResponse;
    private MerchantCategoryListModel merchantCategoryListResponse;
    private SpecialModel specialListResponse;

    private String searchKey = "";
    private String selectedCategoryId = "";
    private String selectedDeliveryId = "";
    private String selectedParentId = "";
    private String selectedGrandParentId = "";
    private String merchantId = "";
    private String parentTitle = "";
    private String sortByID = "";
    private String storeFrontCategory = "";

    private int categorySelectedPosition;

    private boolean isFromStoreFront;
    private SpecialsViewModel specialsViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_specials, container, false);
        return rootView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        specialsViewModel = new ViewModelProvider(this).get(SpecialsViewModel.class);

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


//        specialsViewModel.activeProductsByCompanyId();
//        specialsViewModel.getPromotionsByCompanyId();

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
        isFromStoreFront = ATPreferences.readBoolean(getActivity(), Constants.HEADER_STORE);
        storeFrontCategory = ATPreferences.readString(getActivity(), Constants.MERCHANT_CATEGORY);
        if (isFromStoreFront) {
            storeFrontTabsView();
            Picasso.get().load(ATPreferences.readString(getActivity(), Constants.KEY_IMAGE_URL) +
                            ATPreferences.readString(getActivity(), Constants.HEADER_IMG))
                    .placeholder(R.drawable.loading).into(imageViewStoreImage);
            merchantId = ATPreferences.readString(getActivity(), Constants.MERCHANT_ID);
            relativeLayoutAfterSelection.setVisibility(View.VISIBLE);
            linearLayoutStoreHeader.setVisibility(View.VISIBLE);
            linearLayoutSelectionCategory.setVisibility(View.GONE);
        } else {
            defaultTabsView();
        }

    }

    private void setSearchFilterSpinnerListener() {
        //SpinnerInteractionListener listener = new SpinnerInteractionListener();
        //spinnerSearchFilter.setOnTouchListener(listener);
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
        selectedCategoryId = levelOneCategory.getRESULT().get(0).getRESULT().get(position).get_11493();
        selectedGrandParentId = "";
        selectedParentId = "";
        parentTitle = "";

        notifyParentList(new ArrayList<SelectedParentModel>());
        if (adapterMerchantCategory != null)
            adapterMerchantCategory.customNotify(-1);
        if (adapterSpecials != null)
            adapterSpecials.customNotify(new ArrayList<RESULT>());

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
        tabLayout = getActivity().findViewById(R.id.tabs);
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
        relativeLayoutFilter = rootView.findViewById(R.id.rlFilter);
        buttonStoreDetails = rootView.findViewById(R.id.details_store);
        linearLayoutSearchHeader = rootView.findViewById(R.id.storeSearch);
        linearLayoutSelectionCategory = rootView.findViewById(R.id.linearLayoutSelectionCategory);
        linearLayoutViewResults = rootView.findViewById(R.id.ll_view_result);
        linearLayoutStoreMessages = rootView.findViewById(R.id.linearLayoutStoreMessages);
        linearLayoutGoBack = rootView.findViewById(R.id.linearLayoutBack);
        linearLayoutStoreHeader = rootView.findViewById(R.id.storeHeader);
        textViewCategoryName = rootView.findViewById(R.id.textViewCategoryName);
        textViewCategorySelect = rootView.findViewById(R.id.textViewCategory);

        //tabLayout.setVisibility(View.VISIBLE);
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
        for (int i = 0; i < levelOneCategory.getRESULT().get(0).getRESULT().size(); i++) {
            String name = levelOneCategory.getRESULT().get(0).getRESULT().get(i).get_12045();
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
        adapterSpecials = new AdapterSpecials(getActivity(), specialListResponse.getRESULT().get(0).getRESULT(), this);
        recyclerViewSpecial.setAdapter(adapterSpecials);
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
        linearLayoutStoreMessages.setOnClickListener(this);
        imageViewFilter.setOnClickListener(this);
        textViewSearch.setOnClickListener(this);
        linearLayoutViewResults.setOnClickListener(this);
        textViewCategorySelect.setOnClickListener(this);
        linearLayoutGoBack.setOnClickListener(this);
        buttonStoreDetails.setOnClickListener(this);

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

            case Constants.GUEST_ACTIVITY_SUCCESS:
            case Constants.GUEST_ACTIVITY_TIMEOUT:
                break;
            case Constants.FCM_NOTIFICATION:
                fetchSpecialsList();
                break;
            case Constants.MERCHANT_CATEGORY_LIST_HOME:
                hideProgress();
                levelOneCategory = ModelManager.getInstance().getMerchantStoresManager().merchantCategoryListModel;
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
                    // Utils.baseshowFeedbackMessage(getActivity(), parentLayout, "No Delivery Data Found");
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
                specialListResponse = ModelManager.getInstance().getSpecialsManager().specialModel;
                Log.d("TAG", "onEventss: " + new Gson().toJson(specialListResponse));
                if (event.hasData()) {
                    setSpecialAdapter();
                } else {
                    Utils.baseshowFeedbackMessage(getActivity(), parentLayout, "No Item Found");
                }

                break;

            case -1:
                hideProgress();
                break;


        }

    }

    private void selectStoreSelected() {
        for (int i = 0; i < levelOneCategory.getRESULT().get(0).getRESULT().size(); i++) {
            if (levelOneCategory.getRESULT().get(0).getRESULT().get(i).get_12045().equals(storeFrontCategory)) {
                spinnerCategory.setSelection(i);
                break;
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
                ((HomeActivity) getActivity()).displayView(new FragmentMessages(), Constants.TAG_MESSAGEPAGE, null);
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
 /*       relativeLayoutAfterSelection.setVisibility(View.VISIBLE);
        linearLayoutSearchHeader.setVisibility(View.VISIBLE);
        linearLayoutSelectionCategory.setVisibility(View.GONE);
        spinnerCategory.setSelection(position);
        textViewCategoryName.setText(arrayListSpinnerCategory.get(position));
        categorySelected(position);*/

        selectedCategoryId = levelOneCategory.getRESULT().get(0).getRESULT().get(position).get_11493();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.MERCHANT_CATEGORY_ID, selectedCategoryId);
        ((HomeActivity) getActivity()).displayView(new FragmentSpecialStoreFront(), Constants.TAG_SPECIALS_STOREFRONT, bundle);

    }


    private void notifyParentList(List<SelectedParentModel> arrayListSelectParent) {
        if (adapterParentCategories != null)
            adapterParentCategories.customNotify(arrayListSelectParent);
    }

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

            if (deliveryServiceResponse == null) {
                fetchDeliveryServices();
            }
        }
    }

    @Override
    public void onMerchantCategoryClick(int position) {
        categorySelectedPosition = position;
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
        showProgress();
//        ModelManager.getInstance().getMerchantStoresManager().getCategoryLvlOne
//                (getActivity(), Operations.getCategoriesLvlOne(getActivity()));

        ModelManager.getInstance().getMerchantStoresManager()
                .getMerchantCategoryListHome(getActivity(), Operations.getSpecialsCategoryList(getActivity(),
                        "", "", "", "", ""));


    }


    private void fetchDeliveryServices() {
        showProgress();
        ModelManager.getInstance().getMerchantStoresManager()
                .getDeliveryServices(getActivity(), Operations.getDeliveryServices(getActivity(), selectedCategoryId, searchKey));

    }

    private void fetchSpecialsCategoryList() {
        showProgress();
        ModelManager.getInstance().getMerchantStoresManager()
                .getMerchantCategoryList(getActivity(), Operations.getSpecialsCategoryList(getActivity(),
                        selectedCategoryId, searchKey, selectedDeliveryId, selectedParentId, ""));
    }


    private void fetchSpecialsList() {
        showProgress();
        ModelManager.getInstance().getSpecialsManager().getSpecialsList
                (getActivity(), Operations.getSpecialsList(getActivity(), selectedCategoryId, searchKey, sortByID,
                        selectedParentId, selectedDeliveryId, merchantId, editTextSearchZip.getText().toString()));

    }

    @Override
    public void onSpecialsClick(int position) {
        String productId = Utils.lengtT(11, specialListResponse.getRESULT().get(0).getRESULT().get(position).get_114144());
        String productType = specialListResponse.getRESULT().get(0).getRESULT().get(position).get_114112();

        ModelManager.getInstance().setProductSeen().setProductSeen(getActivity(), Operations.makeProductSeen(getActivity(),
                productId));

        Bundle bundle = new Bundle();
        bundle.putString("productId", productId);
        bundle.putString("productType", productType);
        bundle.putString(Constants.TAB_SELECTED, Constants.TAG_SPECIAL);
        FragmentItemDetails fragment = new FragmentItemDetails();
        fragment.setArguments(bundle);

        ((HomeActivity) getActivity()).displayView(fragment, Constants.TAG_DETAILSPAGE, bundle);

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
        fetchSpecialsCategoryList();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                arrayListSelectParent = removeItemsFromParentList(position);
                notifyParentList(arrayListSelectParent);
            }
        }, 3000);


    }

    private List<SelectedParentModel> removeItemsFromParentList(int position) {
        if (position == 0) {
            return new ArrayList<>();
        } else {
            return arrayListSelectParent.subList(0, position);
        }

    }

    public class SpinnerInteractionListener implements AdapterView.OnItemSelectedListener, View.OnTouchListener {

        boolean userSelect = false;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (deliveryServiceResponse == null) {
                if (waitCode())
                    fetchDeliveryServices();
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
/*        FragmentManager fragmentManager = getFragmentManager();
        final Fragment fragment = fragmentManager.findFragmentById(R.id.container_body);
        if (fragment instanceof FragmentSpecial) {
            TabLayout.Tab tab = tabLayout.getTabAt(2);
            ((HomeActivity) getActivity()).removeTabListener();
            tab.select();
            ((HomeActivity) getActivity()).addTabListener();

        }*/
    }
}

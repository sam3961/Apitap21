package com.apitap.views.fragments.ads;

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
import com.apitap.model.bean.ads.AdsModel;
import com.apitap.model.bean.ads.RESULT;
import com.apitap.model.bean.levelOneCategories.LevelOneCategory;
import com.apitap.model.customclasses.Event;
import com.apitap.model.deliveryServices.DeliveryServiceModel;
import com.apitap.model.merchantCategoryList.MerchantCategoryListModel;
import com.apitap.model.preferences.ATPreferences;
import com.apitap.views.HomeActivity;
import com.apitap.views.MerchantStoreDetails;
import com.apitap.views.adapters.AdapterCategorySpinner;
import com.apitap.views.adapters.AdapterInitalCategories;
import com.apitap.views.fragments.BaseFragment;
import com.apitap.views.fragments.adDetails.FragmentAdDetail;
import com.apitap.views.fragments.ads.storeFront.FragmentAdsStoreFront;
import com.apitap.views.fragments.items.storeFront.FragmentItemsStoreFront;
import com.apitap.views.fragments.messages.FragmentMessages;
import com.apitap.views.fragments.ads.adapters.AdapterAds;
import com.apitap.views.fragments.items.adapter.AdapterCategoryListSpinner;
import com.apitap.views.fragments.items.adapter.AdapterMerchantCategoryItem;
import com.apitap.views.fragments.items.adapter.AdapterParentCategoriesItem;
import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import static com.apitap.App.isGuest;

public class FragmentAds extends BaseFragment implements View.OnClickListener,
        AdapterInitalCategories.CategoriesItemClick,
        AdapterMerchantCategoryItem.MerchantCategoryClick, AdapterAds.ItemListClick,
        AdapterParentCategoriesItem.ParentCategoryClick {

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


    private Spinner spinnerCategory;
    private Spinner spinnerDeliveryMethod;
    private Spinner spinnerSortBy;
    private Spinner spinnerSearchFilter;
    private Spinner spinnerCategoryFilter;

    private RecyclerView recycelerViewParentCategory;
    private RecyclerView recyclerViewAds;
    private RecyclerView recyclerViewInitialCategory;
    private RecyclerView rvMerchantCategory;

    private AdapterMerchantCategoryItem adapterMerchantCategory;
    private AdapterAds adapterAds;
    private AdapterInitalCategories adapterCategories;
    private AdapterParentCategoriesItem adapterParentCategories;

    private ArrayList<String> arrayListSpinnerCategory = new ArrayList<>();
    private ArrayList<String> arrayListSearchFilter = new ArrayList<>();
    private List<SelectedParentModel> arrayListSelectParent = new ArrayList<>();

    private LevelOneCategory levelOneCategoryResponse;
    private MerchantCategoryListModel levelOneCategory;
    private DeliveryServiceModel deliveryServiceResponse;
    private MerchantCategoryListModel merchantCategoryListResponse;
    private AdsModel adsListResponse;

    private String searchKey = "";
    private String selectedCategoryId = "";
    private String selectedDeliveryId = "";
    private String selectedParentId = "";
    private String selectedGrandParentId = "";
    private String merchantId = "";
    private String parentTitle = "";
    private String sortByID = "";
    private String storeFrontCategory = "";

    private boolean isFromStoreFront;

    private int categorySelectedPosition;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_ad, container, false);
        return rootView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

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
        spinnerSearchFilter = rootView.findViewById(R.id.spinnerSearchFilter);
        spinnerCategoryFilter = rootView.findViewById(R.id.spinnerCategoryFilter);
        recyclerViewAds = rootView.findViewById(R.id.recyclerViewAds);
        recyclerViewInitialCategory = rootView.findViewById(R.id.recyclerViewCategory);
        relativeLayoutFilter = rootView.findViewById(R.id.rlFilter);
        relativeLayoutAfterSelection = rootView.findViewById(R.id.relativeLayoutAfterSelection);
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

        //tabLayout.setVisibility(View.VISIBLE);
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


    private void setCategorySpinnerListAdapter() {
        AdapterCategoryListSpinner adapterCategoryListSpinner = new AdapterCategoryListSpinner(getActivity(), merchantCategoryListResponse.getRESULT().get(0).getRESULT());
        spinnerCategoryFilter.setAdapter(adapterCategoryListSpinner);
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
            Picasso.get().load(ATPreferences.readString(getActivity(), Constants.KEY_IMAGE_URL) +
                    ATPreferences.readString(getActivity(),Constants.HEADER_IMG))
                    .placeholder(R.drawable.loading).into(imageViewStoreImage);

            storeFrontTabsView();
            merchantId = ATPreferences.readString(getActivity(), Constants.MERCHANT_ID);
            relativeLayoutAfterSelection.setVisibility(View.VISIBLE);
            linearLayoutStoreHeader.setVisibility(View.VISIBLE);
            linearLayoutSelectionCategory.setVisibility(View.GONE);
        } else {
            defaultTabsView();
        }

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
        selectedGrandParentId="";
        selectedParentId="";
        parentTitle="";

        notifyParentList(new ArrayList<SelectedParentModel>());
        if (adapterMerchantCategory != null)
            adapterMerchantCategory.customNotify(-1);
        if (adapterAds != null)
            adapterAds.customNotify(new ArrayList<RESULT>());

        fetchAdsList();
    }

    private void setSearchFilterSpinnerListener() {
       // SpinnerInteractionListener listener = new SpinnerInteractionListener();
       // spinnerSearchFilter.setOnTouchListener(listener);
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
        arrayListSpinnerCategory= new ArrayList<>();
        for (int i = 0; i < levelOneCategory.getRESULT().get(0).getRESULT().size(); i++) {
            String name = levelOneCategory.getRESULT().get(0).getRESULT().get(i).get_12045();
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
        adapterAds = new AdapterAds(getActivity(), adsListResponse.getRESULT().get(0).getRESULT(), this);
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
        textViewSearch.setOnClickListener(this);
        textViewCategorySelect.setOnClickListener(this);
        linearLayoutViewResults.setOnClickListener(this);
        linearLayoutGoBack.setOnClickListener(this);
        linearLayoutStoreMessages.setOnClickListener(this);
        buttonStoreDetails.setOnClickListener(this);

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

            case Constants.MERCHANT_CATEGORY_LIST:
                hideProgress();
                merchantCategoryListResponse = ModelManager.getInstance().getMerchantStoresManager().merchantCategoryListModel;
                if (event.hasData()) {
                    setCategoryListAdapter();
                    setCategorySpinnerListAdapter();
                    setParentList();
                } else {
                    adapterMerchantCategory.customNotify(categorySelectedPosition);
                    //  Utils.baseshowFeedbackMessage(getActivity(), parentLayout, "No Category Found");
                }

                break;
            case Constants.ADS_LIST_DATA:
                hideProgress();
                setLeftPanelVisibility(false);
                adsListResponse = ModelManager.getInstance().getAdsManager().adsModel;
                if (event.hasData()) {
                    setAdsAdapter();
                } else {
                    Utils.baseshowFeedbackMessage(getActivity(), parentLayout, "No Ads Found");
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
        setLeftPanelVisibility(true);
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
            case R.id.ll_view_result:
                searchKey = "";
                fetchAdsList();
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
 /*       relativeLayoutAfterSelection.setVisibility(View.VISIBLE);
        linearLayoutSelectionCategory.setVisibility(View.GONE);

        linearLayoutSearchHeader.setVisibility(View.VISIBLE);
        spinnerCategory.setSelection(position);
        textViewCategoryName.setText(arrayListSpinnerCategory.get(position));
        categorySelected(position);*/
        selectedCategoryId = levelOneCategory.getRESULT().get(0).getRESULT().get(position).get_11493();
        Bundle bundle =new Bundle();
        bundle.putString(Constants.MERCHANT_CATEGORY_ID,selectedCategoryId);
        ((HomeActivity) getActivity()).displayView(new FragmentAdsStoreFront(), Constants.TAG_ADS_STOREFRONT, bundle);

    }

    @Override
    public void onMerchantCategoryClick(int position) {
        categorySelectedPosition = position;
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
    public void onItemsClick(int position) {
        List<RESULT> adsResultList = adsListResponse.getRESULT().get(0).getRESULT();

        ModelManager.getInstance().setProductSeen().setProductSeen(getActivity(), Operations.makeProductSeen(getActivity(),
                adsResultList.get(position).get_12118()));

        String imageId = adsResultList.get(position).get_12086();
        String imageUrl = "";
        String videoUrl = adsResultList.get(position).get_12115();
        if (!imageId.isEmpty()) {
            imageUrl = ATPreferences.readString(getActivity(), Constants.KEY_IMAGE_URL) + adsResultList.get(position).get_121170();
            videoUrl = "";
        }


        Bundle bundle = new Bundle();
        bundle.putString("videoUrl",videoUrl);
        bundle.putString("image",imageUrl);
        bundle.putString("merchant",Utils.hexToASCII(adsResultList.get(position).get_11470()));
        bundle.putString("adName", Utils.hexToASCII(adsResultList.get(position).get_12083()));
        bundle.putString("desc",adsResultList.get(position).get_120157());
        bundle.putString("id",adsResultList.get(position).get_12321());
        bundle.putString("ad_id",adsResultList.get(position).get_12321());
        bundle.putString("merchantid",merchantId);
        bundle.putInt("adpos",position);
        bundle.putString(Constants.TAB_SELECTED, Constants.TAG_ADS);
        ((HomeActivity) getActivity()).displayView(new FragmentAdDetail(), Constants.TAG_AD_DETAIL, bundle);

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
        fetchAdsCategoryList();
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
            recyclerViewAds.setNestedScrollingEnabled(true);
            textViewFilter.setTextColor(getResources().getColor(R.color.darkBlue));
            relativeLayoutFilter.setBackground(getResources().getDrawable(R.drawable.rounded_blue_transparent_));
            imageViewFilter.setColorFilter(ContextCompat.getColor(getActivity(),R.color.dark_blue),
                    android.graphics.PorterDuff.Mode.MULTIPLY);
            imageViewFilter.setImageDrawable(getResources().getDrawable(R.drawable.ic_icon_downarrow));
        } else {
            viewLeftPanel.setVisibility(View.VISIBLE);
            recyclerViewAds.setNestedScrollingEnabled(false);
            textViewFilter.setTextColor(getResources().getColor(R.color.colorWhite));
            relativeLayoutFilter.setBackground(getResources().getDrawable(R.drawable.round_dark_blue_white_outline_));
            imageViewFilter.setColorFilter(ContextCompat.getColor(getActivity(),R.color.colorWhite),
                    android.graphics.PorterDuff.Mode.MULTIPLY);
            imageViewFilter.setImageDrawable(getResources().getDrawable(R.drawable.ic_icon_uparrow));

            if (merchantCategoryListResponse == null) {
                fetchAdsCategoryList();
            }

        }
    }


    private void fetchCategoryLvlOne() {
        showProgress();
//        ModelManager.getInstance().getMerchantStoresManager().getCategoryLvlOne
//                (getActivity(), Operations.getCategoriesLvlOne(getActivity()));
        ModelManager.getInstance().getMerchantStoresManager()
                .getMerchantCategoryListHome(getActivity(), Operations.getAdsCategoryList(getActivity(),
                        "", "", "", "", ""));

    }


    private void fetchAdsCategoryList() {
        showProgress();
        ModelManager.getInstance().getMerchantStoresManager()
                .getMerchantCategoryList(getActivity(), Operations.getAdsCategoryList(getActivity(),
                        selectedCategoryId, searchKey, selectedDeliveryId, selectedParentId,""));
    }


    private void fetchAdsList() {
        showProgress();
        ModelManager.getInstance().getAdsManager().getAdsList
                (getActivity(), Operations.getAdsList(getActivity(), selectedCategoryId, searchKey, sortByID,
                        selectedParentId, selectedDeliveryId, merchantId,editTextSearchZip.getText().toString()));

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

package com.apitap.views;

import static com.apitap.App.isGuest;
import static com.apitap.controller.MerchantManager.merchantCategoryList;
import static com.apitap.model.MyFirebaseMessagingService.adId;
import static com.apitap.model.MyFirebaseMessagingService.generalMessageId;
import static com.apitap.model.MyFirebaseMessagingService.invoiceId;
import static com.apitap.model.MyFirebaseMessagingService.productId;
import static com.apitap.model.MyFirebaseMessagingService.productName;
import static com.apitap.model.MyFirebaseMessagingService.storeName;
import static com.apitap.model.Utils.MY_PERMISSIONS_REQUEST_LOCATION;
import static com.apitap.model.Utils.showToast;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.apitap.R;
import com.apitap.controller.MerchantFavouriteManager;
import com.apitap.controller.ModelManager;
import com.apitap.model.AddTabBar;
import com.apitap.model.Constants;
import com.apitap.model.GPSService;
import com.apitap.model.GlobalData;
import com.apitap.model.MyFirebaseMessagingService;
import com.apitap.model.Operations;
import com.apitap.model.Utils;
import com.apitap.model.bean.SelectedParentModel;
import com.apitap.model.bean.itemStoreFront.ItemStoreFrontResponse;
import com.apitap.model.bean.levelOneCategories.LevelOneCategory;
import com.apitap.model.brandNames.BrandNamesResponse;
import com.apitap.model.customclasses.Event;
import com.apitap.model.customclasses.ScaleImageView;
import com.apitap.model.deliveryServices.DeliveryServiceModel;
import com.apitap.model.merchantCategoryList.MerchantCategoryListModel;
import com.apitap.model.preferences.ATPreferences;
import com.apitap.model.ratings.RatingResponse;
import com.apitap.model.storeFrontItems.browseCategory.BrowseCategoryResponse;
import com.apitap.model.storeFrontItems.details.StoreDetailsResponse;
import com.apitap.views.NavigationMenu.FragmentDrawer;
import com.apitap.views.adapters.AdapterCategorySpinner;
import com.apitap.views.adapters.AdapterHeaderCategory;
import com.apitap.views.fragments.FragmentAbout;
import com.apitap.views.fragments.FragmentGotQuestions;
import com.apitap.views.fragments.FragmentHistory;
import com.apitap.views.fragments.FragmentNotification;
import com.apitap.views.fragments.FragmentScanner;
import com.apitap.views.fragments.FragmentSettings;
import com.apitap.views.fragments.FragmentShoppingAsst;
import com.apitap.views.fragments.FragmentStoreAbout;
import com.apitap.views.fragments.FragmentStoreDetails;
import com.apitap.views.fragments.FragmentStoreMap;
import com.apitap.views.fragments.FragmentStoreRate;
import com.apitap.views.fragments.FragmentTour;
import com.apitap.views.fragments.ads.FragmentAds;
import com.apitap.views.fragments.ads.storeFront.FragmentAdsStoreFront;
import com.apitap.views.fragments.checkinTv.FragmentCheckIn;
import com.apitap.views.fragments.favourite.FragmentFavourite;
import com.apitap.views.fragments.home.FragmentHome;
import com.apitap.views.fragments.interfaces.SearchStoreClickListener;
import com.apitap.views.fragments.itemDetails.FragmentItemDetails;
import com.apitap.views.fragments.items.FragmentItems;
import com.apitap.views.fragments.items.adapter.AdapterMerchantCategoryItem;
import com.apitap.views.fragments.items.adapter.AdapterParentCategoriesItem;
import com.apitap.views.fragments.items.storeFront.FragmentItemsStoreFront;
import com.apitap.views.fragments.messageDetails.FragmentMessageDetail;
import com.apitap.views.fragments.messages.FragmentMessages;
import com.apitap.views.fragments.reservations.FragmentAddReservation;
import com.apitap.views.fragments.reservations.FragmentViewReservation;
import com.apitap.views.fragments.search.FragmentSearch;
import com.apitap.views.fragments.shoppingCart.ShoppingCartFragment;
import com.apitap.views.fragments.specials.FragmentSpecial;
import com.apitap.views.fragments.specials.storefront.FragmentSpecialStoreFront;
import com.apitap.views.fragments.storefront.FragmentStoreFront;
import com.apitap.views.fragments.stores.FragmentStore;
import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by apple on 10/08/16.
 */
public class HomeActivity extends BaseActivity implements FragmentDrawer.FragmentDrawerListener, View.OnClickListener, FragmentManager.OnBackStackChangedListener, AdapterParentCategoriesItem.ParentCategoryClick, AdapterMerchantCategoryItem.MerchantCategoryClick, AdapterHeaderCategory.CategoryHeaderClick {

    private MerchantCategoryListModel merchantCategoryListResponse;
    private RatingResponse ratingsResponse;
    private AdapterMerchantCategoryItem adapterMerchantCategory;
    private AdapterHeaderCategory adapterHeaderCategory;
    private Spinner spinnerLocations;
    private Spinner spinnerLookingFor;
    private Spinner spinnerRating;
    private Spinner spinnerCondition;
    private Spinner spinnerSortBy;
    private Spinner spinnerDelivery;
    private Spinner spinnerBrandNames;
    private Spinner spinnerCategory;
    private LinearLayout linearLayoutGoBack;
    private LinearLayout linearLayoutCategory;
    private LinearLayout linearLayoutDelivery;
    private LinearLayout linearLayoutCondition;
    private LinearLayout linearLayoutBusiness;
    private LinearLayout linearLayoutStoreMessage;
    private LinearLayout linearLayoutBrand;
    private LinearLayout linearLayoutRating;
    private LinearLayout linearLayoutLocations, linearLayoutReviews, linearLayoutAbout, linearLayoutPolicies;
    private View viewLocations, viewReviews, viewAbout, viewPolicies;
    private Button buttonStoreDetails, buttonViewStore;
    private Button buttonCheckinHome, buttonListenLive, buttonViewReservation, buttonAddReservation;
    private RelativeLayout relativeLayoutSearchBar;
    private LinearLayout linearLayoutStoreFavourite, linearLayoutTabs;
    private LinearLayout linearLayoutCheckin;
    private LinearLayout linearLayoutStoreReservation;
    private ScaleImageView imageViewStoreHeader;
    private int categorySelectedPosition;
    private RelativeLayout leftPanel;
    private LinearLayout linearLayoutHeaderStoreFront;
    private LinearLayout linearLayoutHeaderCategory;
    private LinearLayout linearLayoutStoreTabs;
    private LinearLayout linearLayoutStoreDetailHeader;
    private RelativeLayout relativeLayoutSearchBarStoreFront;
    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;
    private TextView mTxtHeading;
    private LinearLayout ll_msgCount;
    private TextView tv_msgCount;
    private TextView textViewBusiness1, textViewAds1, textViewPromotions1;
    private LinearLayout linearLayoutBusinessTab, linearLayoutAdsTab, linearLayoutPromotionsTab;
    private ImageView imageViewBusiness, imageViewAds, imageViewPromotions;
    private static int toolint = 0;
    private ImageView mlogo;
    private ImageView imageViewSearch;
    private ImageView imageViewMessageStore;
    private ImageView imageViewSearchBarHome;
    private ImageView imageViewSearch2;

    private LinearLayout llScan, llMessage, llFavourites, ll_cart;
    private static final int ZXING_CAMERA_PERMISSION = 1;
    private Class<?> mClss;
    private static final int MY_SCAN_CAMERA_REQUEST_CODE = 100;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 101;
    private static final int CAMERA_REQUEST_CODE = 102;
    private Activity context;
    String searchkey = "";
    String selectedUpdatedCategoryId = "";
    public static TabLayout tabLayout;
    boolean doubleBackToExitPressedOnce = false;
    public static TabLayout tabLayout2;
    public EditText editTextZipCode;
    public AutoCompleteTextView editTextSearch, editTextSearchStoreFront;
    public TextView textViewSearch, textViewSearchStoreFront, textViewCategorySelect;
    public LinearLayout linearLayoutSearchFilter;
    public TextView textViewFilter, textViewFilterStoreFront;
    public ImageView imageViewFilter, imageViewFilterStoreFront;
    public ImageView imageViewBackHeaderCat;
    public static LinearLayout tabContainer2;
    public static LinearLayout tabContainer1;
    private TextView tabOne, tabTwo, tabThree, tabFour, tabFive, tabSix;
    String locationSearch = "";
    private boolean isDrawerItemSelected = false;
    public ImageView homeTab1, homeTab2, imageViewCollapseSearch, imageViewScan, imageViewCamera;

    FragmentManager manager;
    private Dialog dialogReload;
    private DrawerLayout rootLayout;
    private TextView textViewUserName;
    private TextView textViewStoreName;
    private TextView textViewStoreNameDetails;
    private RecyclerView recyclerViewCategoryHeader;

    private BrandNamesResponse brandNamesResponse;
    private DeliveryServiceModel deliveryServiceResponse;
    private LevelOneCategory levelOneCategoryResponse;
    private BrowseCategoryResponse browseCategoryResponse;
    private StoreDetailsResponse storeDetailsResponse;

    private ArrayList<String> arrayListLocations = new ArrayList<>();
    private ArrayList<String> arrayListLookingFor = new ArrayList<>();
    private List<String> arrayListSpinnerCondition = new ArrayList<>();
    private List<String> arrayListHeaderCategory = new ArrayList<>();
    private List<String> arrayListSpinnerCategory = new ArrayList<>();
    private ArrayList<String> arrayListSpinnerDelivery = new ArrayList<>();
    private ArrayList<String> arrayListBrandName = new ArrayList<>();
    private ArrayList<String> arrayListRatings = new ArrayList<>();
    private List<SelectedParentModel> arrayListSelectParent = new ArrayList<>();

    private AdapterParentCategoriesItem adapterParentCategories;

    private String selectedGrandParentId = "";
    private String selectedParentId = "";
    private String parentTitle = "";
    private String sortByID = "";
    private String lookingFor = "";
    private String selectedConditionId = "";
    private String location = "";
    private String categoryId = "";
    private String categoryName = "";
    private String deliveryId = "";
    private String selectedRatingId = "";
    private String selectedBrandName = "";

    private boolean storeFrontBackPressed = false;

    private RecyclerView recycelerViewParentCategory;
    private RecyclerView rvMerchantCategory;
    private static SearchStoreClickListener searchStoreClickListener;
    public ItemStoreFrontResponse itemListResponse;


    public static void setActiveFragment(SearchStoreClickListener searchClickListener) {
        searchStoreClickListener = searchClickListener;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorWhite));
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        setContentView(R.layout.homepage_activity);

        manager = getSupportFragmentManager();
        context = this;
        isGuest = ATPreferences.readBoolean(this, Constants.GUEST);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        findToolbarViews();

        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);

        mToolbar.setNavigationIcon(null);

        if (getIntent() != null) {
            if (getIntent().hasExtra("Guest")) {
                Log.d("itsTrue", Constants.GUEST);
            } else if (getIntent().hasExtra("Drawer")) {
                isDrawerItemSelected = true;
                int position = getIntent().getIntExtra("Drawer", 99);
                drawerItemSelected(position);
            } else if (getIntent().hasExtra("Tab")) {
                int tabposition = getIntent().getIntExtra("Tab", 99);
                isDrawerItemSelected = true;
                setCurrentTabFragment(tabposition);
            }

        }

        getSupportFragmentManager().addOnBackStackChangedListener(this);

        if (!isDrawerItemSelected) {
            displayView(new FragmentHome(), Constants.TAG_HOMEPAGE, new Bundle());
        }
        Uri data = getIntent().getData();
        if (data != null && data.getPathSegments().size() >= 1) {
            List<String> params = data.getPathSegments();
            Bundle bundle = new Bundle();
            bundle.putString("productId", params.get(1));
            bundle.putString("productType", params.get(0));
            bundle.putString("flag", "home");
            FragmentItemDetails fragment = new FragmentItemDetails();
            fragment.setArguments(bundle);
            displayView(fragment, Constants.TAG_DETAILSPAGE, bundle);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    tabContainer2Visible();
                }
            }, 3000);

        }
        if (getIntent() != null && getIntent().hasExtra("productId")) {
            Bundle bundle = new Bundle();
            bundle.putString("productId", getIntent().getStringExtra("productId"));
            bundle.putString("productType", "21");
            bundle.putString("flag", "home");
            FragmentItemDetails fragment = new FragmentItemDetails();
            fragment.setArguments(bundle);
            displayView(fragment, Constants.TAG_DETAILSPAGE, bundle);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    tabContainer2Visible();
                }
            }, 3000);

        }

        if (getIntent() != null && getIntent().hasExtra(Constants.MERCHANT_ID)) {
            Bundle bundle = new Bundle();
            bundle.putString(Constants.MERCHANT_ID, getIntent().getExtras().getString(Constants.MERCHANT_ID));
            ATPreferences.putString(context, Constants.MERCHANT_ID, getIntent().getExtras().getString(Constants.MERCHANT_ID));
            ATPreferences.putString(context, Constants.MERCHANT_CATEGORY, "");
            displayView(new FragmentStoreFront(), Constants.TAG_STORESFRONTPAGE, bundle);
        }

        if (getIntent() != null && (getIntent().hasExtra("productId") ||
                getIntent().hasExtra("adID") || getIntent().hasExtra("invoice") ||
                getIntent().hasExtra("generalId"))) {
            Bundle bundle = new Bundle();
            if (!getIntent().hasExtra("productId")) {
                bundle.putString("productId", productId);
                bundle.putString("productName", productName);
            } else if (!getIntent().hasExtra("adID")) {
                bundle.putString("adID", adId);
                bundle.putString("adName", "");
            } else if (!getIntent().hasExtra("invoice")) {
                bundle.putString("invoice", invoiceId);
            } else if (!getIntent().hasExtra("generalId")) {
                bundle.putString("generalId", generalMessageId);
                bundle.putString("merchantId", "");
                bundle.putString("merchantName", storeName);
                displayView(new FragmentMessageDetail(), Constants.MessageDetailPage, bundle);
            }
        }
        addTabListener();

    }

    private void findToolbarViews() {
        tabLayout = findViewById(R.id.tabs);
        tabLayout2 = findViewById(R.id.tabs2);
        tabContainer2 = findViewById(R.id.tab_container2);
        tabContainer1 = findViewById(R.id.tab_container);


        llScan = mToolbar.findViewById(R.id.ll_scan);
        llMessage = mToolbar.findViewById(R.id.ll_message);
        llFavourites = mToolbar.findViewById(R.id.ll_search);
        ll_cart = mToolbar.findViewById(R.id.ll_cart);
        mTxtHeading = mToolbar.findViewById(R.id.txt_heading);
        mlogo = mToolbar.findViewById(R.id.img_logo);
        rootLayout = findViewById(R.id.drawer_layout);
        textViewUserName = findViewById(R.id.textViewUserName);
        textViewStoreName = findViewById(R.id.storeName);
        textViewStoreNameDetails = findViewById(R.id.textViewStoreNameDetails);
        recyclerViewCategoryHeader = findViewById(R.id.recyclerViewCategoryHeader);
        imageViewSearch = findViewById(R.id.search);
        imageViewSearchBarHome = findViewById(R.id.search_home);
        imageViewSearch2 = findViewById(R.id.search_two);
        homeTab1 = findViewById(R.id.tab_one_image);
        homeTab2 = findViewById(R.id.tab_two_image);
        imageViewCollapseSearch = findViewById(R.id.imageViewCollapseSearch);
        imageViewScan = findViewById(R.id.imageViewScan);
        imageViewCamera = findViewById(R.id.imageViewCamera);
        ll_msgCount = findViewById(R.id.new_msgsll);
        tv_msgCount = findViewById(R.id.new_msgstv);
        textViewBusiness1 = findViewById(R.id.textViewBusiness1);
        textViewAds1 = findViewById(R.id.textViewAds1);
        textViewPromotions1 = findViewById(R.id.textViewPromotions1);
        linearLayoutBusinessTab = findViewById(R.id.linearLayoutBusiness);
        imageViewBusiness = findViewById(R.id.imageViewBusiness);
        imageViewAds = findViewById(R.id.imageViewAds);
        imageViewPromotions = findViewById(R.id.imageViewPromotions);
        linearLayoutAdsTab = findViewById(R.id.linearLayoutAds);
        linearLayoutPromotionsTab = findViewById(R.id.linearLayoutPromotions);
        spinnerLookingFor = findViewById(R.id.spinnerLookingFor);
        spinnerRating = findViewById(R.id.spinnerRating);
        spinnerCondition = findViewById(R.id.spinnerCondition);
        spinnerLocations = findViewById(R.id.spinnerLocation);
        spinnerSortBy = findViewById(R.id.spinnerSortBy);
        spinnerCategory = findViewById(R.id.spinnerCategoryHome);
        spinnerDelivery = findViewById(R.id.spinnerDeliveryMethod);
        spinnerBrandNames = findViewById(R.id.spinnerBrand);

        editTextSearch = findViewById(R.id.editTextSearch);
        editTextSearchStoreFront = findViewById(R.id.editTextSearchStoreFront);
        editTextZipCode = findViewById(R.id.editTextSearchZip);
        textViewSearch = findViewById(R.id.textViewSearch);
        linearLayoutSearchFilter = findViewById(R.id.linearLayoutSearchFilter);
        textViewSearchStoreFront = findViewById(R.id.textViewSearchStoreFront);
        textViewCategorySelect = findViewById(R.id.textViewCategory);
        textViewFilter = findViewById(R.id.textViewFilter);
        textViewFilterStoreFront = findViewById(R.id.textViewFilterStoreFront);
        imageViewFilter = findViewById(R.id.imageViewFilter);
        imageViewBackHeaderCat = findViewById(R.id.imageViewBackHeaderCat);
        imageViewFilterStoreFront = findViewById(R.id.imageViewFilterStoreFront);

        relativeLayoutSearchBar = findViewById(R.id.view_search);
        linearLayoutStoreFavourite = findViewById(R.id.view_store_favourite);
        linearLayoutTabs = findViewById(R.id.llTabs);
        linearLayoutGoBack = findViewById(R.id.linearLayoutBack);
        linearLayoutCategory = findViewById(R.id.linearLayoutCategory);
        linearLayoutDelivery = findViewById(R.id.linearLayoutDelivery);
        linearLayoutCondition = findViewById(R.id.linearLayoutCondition);
        imageViewMessageStore = findViewById(R.id.message);
        linearLayoutBusiness = findViewById(R.id.linearLayoutCategories);
        linearLayoutStoreMessage = findViewById(R.id.message_store);
        linearLayoutBrand = findViewById(R.id.llBrand);
        linearLayoutRating = findViewById(R.id.llRating);
        linearLayoutLocations = findViewById(R.id.linearLayoutLocations);
        linearLayoutReviews = findViewById(R.id.linearLayoutReviews);
        linearLayoutAbout = findViewById(R.id.linearLayoutAbout);
        linearLayoutPolicies = findViewById(R.id.linearLayoutPolicies);
        viewLocations = findViewById(R.id.view_location);
        viewReviews = findViewById(R.id.view_reviews);
        viewAbout = findViewById(R.id.view_about);
        viewPolicies = findViewById(R.id.view_policies);
        imageViewStoreHeader = findViewById(R.id.adstoreImg);
        relativeLayoutSearchBarStoreFront = findViewById(R.id.search_storefront);
        linearLayoutHeaderStoreFront = findViewById(R.id.header);
        linearLayoutCheckin = findViewById(R.id.view_checkin);
        linearLayoutStoreReservation = findViewById(R.id.view_store_reservation);
        linearLayoutHeaderCategory = findViewById(R.id.header_browse_category);
        linearLayoutStoreTabs = findViewById(R.id.view_store_tabs);
        linearLayoutStoreDetailHeader = findViewById(R.id.view_store_detail_header);
        leftPanel = findViewById(R.id.view_filter);
        buttonStoreDetails = findViewById(R.id.details_store);
        buttonListenLive = findViewById(R.id.buttonListenLive);
        buttonCheckinHome = findViewById(R.id.buttonCheckinHome);
        buttonAddReservation = findViewById(R.id.buttonAddReservation);
        buttonViewReservation = findViewById(R.id.buttonViewReservation);
        buttonViewStore = findViewById(R.id.buttonViewStore);
        rvMerchantCategory = findViewById(R.id.recycelerViewMerchantCategory);
        recycelerViewParentCategory = findViewById(R.id.recyclerViewParentCategory);

        dialogReload = Utils.showReloadDialog(this);

        buttonCheckinHome.setOnClickListener(this);
        buttonAddReservation.setOnClickListener(this);
        buttonViewReservation.setOnClickListener(this);
        buttonListenLive.setOnClickListener(this);
        linearLayoutBusinessTab.setOnClickListener(this);
        linearLayoutAdsTab.setOnClickListener(this);
        linearLayoutPromotionsTab.setOnClickListener(this);
        linearLayoutLocations.setOnClickListener(this);
        linearLayoutReviews.setOnClickListener(this);
        linearLayoutAbout.setOnClickListener(this);
        linearLayoutPolicies.setOnClickListener(this);
        textViewStoreName.setOnClickListener(this);
        textViewStoreNameDetails.setOnClickListener(this);
        linearLayoutGoBack.setOnClickListener(this);
        linearLayoutSearchFilter.setOnClickListener(this);
        buttonStoreDetails.setOnClickListener(this);
        buttonViewStore.setOnClickListener(this);
        imageViewSearchBarHome.setOnClickListener(this);
        imageViewMessageStore.setOnClickListener(this);
        imageViewCamera.setOnClickListener(this);
        textViewSearchStoreFront.setOnClickListener(this);
        leftPanel.setOnClickListener(this);
        textViewFilter.setOnClickListener(this);
        textViewFilterStoreFront.setOnClickListener(this);
        imageViewFilter.setOnClickListener(this);
        imageViewFilterStoreFront.setOnClickListener(this);
        textViewSearch.setOnClickListener(this);
        imageViewScan.setOnClickListener(this);
        llScan.setOnClickListener(this);
        llMessage.setOnClickListener(this);
        llFavourites.setOnClickListener(this);
        imageViewSearch.setOnClickListener(this);
        findViewById(R.id.imageViewCollapseSearch).setOnClickListener(this);
        findViewById(R.id.search_bar_two).setOnClickListener(this);
        findViewById(R.id.search_bar).setOnClickListener(this);
        findViewById(R.id.imageViewCloseFilter).setOnClickListener(this);
        imageViewSearch2.setOnClickListener(this);
        textViewCategorySelect.setOnClickListener(this);
        linearLayoutStoreMessage.setOnClickListener(this);
        imageViewBackHeaderCat.setOnClickListener(this);
        ll_cart.setOnClickListener(this);

        editTextSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    textViewSearch.performClick();
                    return true;
                }
                return false;
            }
        });
        editTextSearchStoreFront.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    textViewSearchStoreFront.performClick();
                    return true;
                }
                return false;
            }
        });

        setupViewPager();
        //setupTabIcons();
        bindWidgetsWithAnEvent();
        setUpHandlerForTour();
        setParentListAdapter();

        AddTabBar.getmInstance().setupViewPager(tabLayout2);
/*
        AddTabBar.getmInstance().setupTabIcons(tabLayout2, context, tabOne, tabTwo, tabThree, tabFour, tabFive,
                tabSix, homeTab2);
*/
        //      AddTabBar.getmInstance().bindWidgetsWithAnEvent(tabContainer2, tabLayout2, HomeActivity.this, R.id.container_body);

        tabContainer2.setVisibility(View.GONE);
        tabContainer2.setVisibility(View.GONE);
        if (!isGuest)
            textViewUserName.setText("Welcome, " + Utils.hexToASCII(ATPreferences.getString(this, Constants.KEY_USERNAME, "")));

        GlobalData.nick = Utils.hexToASCII(ATPreferences.getString(this, Constants.KEY_USERNAME, ""));
        ATPreferences.putString(this, getString(R.string.nickSharedPrefsKey), Utils.hexToASCII(ATPreferences.getString(this, Constants.KEY_USERNAME, "")));
        GlobalData.deviceRole = GlobalData.DeviceRole.CLIENT;


        inflateLookingForList();
        inflateConditionSpinnerList();
        inflateSearchSpinnerList();
        setSearchFilterSpinnerAdapter();
        setSortByAdapter();
        setLookingForSpinnerAdapter();
        setConditionSpinnerAdapter();
        spinnerListeners();
    }


    public void tabContainer2Visible() {
        tabContainer2.setVisibility(View.GONE);
        tabContainer2.setVisibility(View.GONE);
    }

    public void tabContainer1Visible() {
        tabContainer2.setVisibility(View.GONE);
        tabContainer1.setVisibility(View.VISIBLE);
    }

    public void inActiveTabs() {
        HomeActivity.tabContainer2.setVisibility(View.GONE);
        HomeActivity.tabContainer2.setVisibility(View.GONE);
    }


    private void inflateDeliveryList() {
        arrayListSpinnerDelivery = new ArrayList<>();
        arrayListSpinnerDelivery.add("All");
        for (int i = 0; i < deliveryServiceResponse.getRESULT().get(0).getRESULT().size(); i++) {
            String name = Utils.hexToASCII(deliveryServiceResponse.getRESULT().get(0).getRESULT().get(i).get_12239());
            arrayListSpinnerDelivery.add(name);
        }

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

    private void inflateSearchSpinnerList() {
        arrayListLocations.add("All Locations");
        arrayListLocations.add("Near by Locations");
        arrayListLocations.add("Other");
    }

    private void inflateLookingForList() {
        arrayListLookingFor.add("All");
        arrayListLookingFor.add("Stores");
        arrayListLookingFor.add("Promotions");
        arrayListLookingFor.add("Products or Services");
        arrayListLookingFor.add("Ads");
    }

    private void inflateConditionSpinnerList() {
        arrayListSpinnerCondition.add("New");
        arrayListSpinnerCondition.add("Used");
        arrayListSpinnerCondition.add("Refurbished");
    }

    private void inflateCategorySpinnerList() {
        arrayListSpinnerCategory = new ArrayList<>();
        arrayListSpinnerCategory.add("Select Business");
        for (int i = 0; i < levelOneCategoryResponse.getRESULT().get(0).getRESULT().size(); i++) {
            String name = levelOneCategoryResponse.getRESULT().get(0).getRESULT().get(i).get_12045();
            arrayListSpinnerCategory.add(name);

        }
    }

    private void inflateStoreCategorySpinnerList() {
        arrayListSpinnerCategory = new ArrayList<>();
        arrayListSpinnerCategory.add("Select Business");
        for (int i = 0; i < browseCategoryResponse.getRESULT().size(); i++) {
            arrayListSpinnerCategory.add(browseCategoryResponse.getRESULT().get(i).getJsonMember12045());
        }
    }


    private void setDeliveryMethodSpinnerAdapter() {
        spinnerDelivery.setAdapter(new AdapterCategorySpinner(this, arrayListSpinnerDelivery));
    }

    private void setBrandNameSpinnerAdapter() {
        spinnerBrandNames.setAdapter(new AdapterCategorySpinner(this, arrayListBrandName));
    }

    private void setSearchFilterSpinnerAdapter() {
        spinnerLocations.setAdapter(new AdapterCategorySpinner(this, arrayListLocations));
    }

    private void setRatingAdapter() {
        spinnerRating.setAdapter(new AdapterCategorySpinner(this, arrayListRatings));
    }

    private void setLookingForSpinnerAdapter() {
        spinnerLookingFor.setAdapter(new AdapterCategorySpinner(this, arrayListLookingFor));
    }

    private void setConditionSpinnerAdapter() {
        spinnerCondition.setAdapter(new AdapterCategorySpinner(this, arrayListSpinnerCondition));
    }

    private void setCategorySpinnerAdapter() {
        Log.d("categoryy", ATPreferences.readString(context, Constants.MERCHANT_CATEGORY) + "  categoryy");
        spinnerCategory.setAdapter(new AdapterCategorySpinner(this, arrayListSpinnerCategory));

        if (ATPreferences.readBoolean(this, Constants.HEADER_STORE)) {
            if (!ATPreferences.readString(context, Constants.MERCHANT_CATEGORY).isEmpty()) {
                for (int i = 0; i < arrayListSpinnerCategory.size(); i++) {
                    if (arrayListSpinnerCategory.get(i).equals(ATPreferences.readString(context, Constants.MERCHANT_CATEGORY))) {
                        spinnerCategory.setSelection(i);
                    }
                }
            } else {
                try {
                    spinnerCategory.setSelection(1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void setStoreHeaderCategoryListAdapter() {
        // if (adapterHeaderCategory != null)
        //   adapterHeaderCategory.customNotify(arrayListHeaderCategory);
        //else {
        //   adapterHeaderCategory = new AdapterHeaderCategory(arrayListHeaderCategory, this);
        //  recyclerViewCategoryHeader.setAdapter(adapterHeaderCategory);
        //}
    }

    private void setCategoryListAdapter() {
        adapterMerchantCategory = new AdapterMerchantCategoryItem(this, merchantCategoryListResponse.getRESULT().get(0).getRESULT(), this);
        rvMerchantCategory.setAdapter(adapterMerchantCategory);
    }


    private void setSortByAdapter() {
        spinnerSortBy.setAdapter(new AdapterCategorySpinner(this, sortByListNames()));
        sortByID = sortByListId().get(0);

    }


    private void setUpHandlerForTour() {
        final android.os.Handler handler = new android.os.Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (ATPreferences.readBoolean(context, Constants.LOGIN_FROM_TEMP_PASS))
                    showConfirmTempPassDialog(context);
                else
                    checkForFirstTimeInstalled();
            }
        }, 500);
    }

    private void checkForFirstTimeInstalled() {
        boolean checkFirstTime = ATPreferences.readBoolean(this, Constants.FIRST_TIME);
        boolean loginAsGuest = ATPreferences.readBoolean(this, Constants.TOUR_GUEST);
        boolean loginAsUser = ATPreferences.readBoolean(this, Constants.TOUR_USER);
        if (!loginAsUser && !checkFirstTime) {
            ATPreferences.putBoolean(this, Constants.TOUR_USER, true);
            ATPreferences.putBoolean(this, Constants.FIRST_TIME, true);
            startActivity(new Intent(this, FragmentTour.class));
        } else if (!loginAsGuest && !checkFirstTime) {
            ATPreferences.putBoolean(this, Constants.TOUR_GUEST, true);
            ATPreferences.putBoolean(this, Constants.FIRST_TIME, true);
            startActivity(new Intent(this, FragmentTour.class));
        }

    }


    private void bindWidgetsWithAnEvent() {
        homeTab2.setOnClickListener(v -> homeTab1.performClick());
        homeTab1.setOnClickListener(view -> {
            ATPreferences.putBoolean(context, Constants.HEADER_STORE, false);
            ATPreferences.putString(getApplicationContext(), Constants.MERCHANT_ID, "");
            //displayView(new FragmentHome(), Constants.TAG_HOMEPAGE, null);
            startActivity(new Intent(context, HomeActivity.class));
            finish();
        });

    }

    TabLayout.OnTabSelectedListener tabSelectedListener = new TabLayout.OnTabSelectedListener() {

        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            setCurrentTabFragment(tab.getPosition());

            //   View view = tab.getCustomView();
            // TextView selectedText = view.findViewById(R.id.tab);
            //  selectedText.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));

        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
            Log.d("onTabUnselected", tab.getPosition() + "  pos");
            //View view = tab.getCustomView();
            // TextView selectedText = view.findViewById(R.id.tab);
            //   selectedText.setTextColor(ContextCompat.getColor(context, R.color.simple_grey));
        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {
            Log.d("onTabReselected", tab.getPosition() + "  pos");
            setCurrentTabFragment(tab.getPosition());
        }
    };

    private void setCurrentTabFragment(int tabPosition) {
        clearStoreFront();
        resetSearch();
        relativeLayoutSearchBar.setVisibility(View.GONE);
        relativeLayoutSearchBarStoreFront.setVisibility(View.GONE);
        switch (tabPosition) {
            case 0:
                displayViewReplace(new FragmentStore(), Constants.TAG_STORESPAGE, null);
                tabLayout.setScrollPosition(0, 0f, true);
                break;
            case 1:
                displayView(new FragmentAds(), Constants.TAG_ADS, null);
                tabLayout.setScrollPosition(1, 0f, true);
                break;
            case 2:
                displayView(new FragmentSpecial(), Constants.TAG_SPECIAL, null);
                tabLayout.setScrollPosition(2, 0f, true);
                break;
            case 3:
                displayView(new FragmentItems(), Constants.TAG_ITEMS, null);
                tabLayout.setScrollPosition(3, 0f, true);
                break;
            case 4:
                if (isGuest)
                    showGuestDialog();
                else {
                    displayView(new FragmentFavourite(), Constants.TAG_FAVOURITEPAGE, null);
                    tabLayout.setScrollPosition(4, 0f, true);
                }
                break;
            case 99:
                ATPreferences.putBoolean(context, Constants.HEADER_STORE, false);
                ATPreferences.putString(context, Constants.MERCHANT_ID, "");
                displayView(new FragmentHome(), Constants.TAG_HOMEPAGE, null);
                break;

        }
    }


    public void setupTabIcons() {
        tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabTwo.setText(" Businesses");
        // tabTwo.setTextColor(getResources().getColor(R.color.colorWhite));
        tabTwo.setTextSize(11);
        tabTwo.setCompoundDrawablesWithIntrinsicBounds(R.drawable.store_ico_selctor, 0, 0, 0);
        tabLayout.getTabAt(0).setCustomView(tabTwo);

        tabThree = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        // tabThree.setTextColor(getResources().getColor(R.color.colorWhite));
        tabThree.setText(" Ads");
        tabThree.setTextSize(11);
        tabThree.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ads_selector, 0, 0, 0);
        tabLayout.getTabAt(1).setCustomView(tabThree);

        tabFour = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabFour.setText(" Promotions");
        // tabFour.setTextColor(getResources().getColor(R.color.colorWhite));
        tabFour.setTextSize(11);
        tabFour.setCompoundDrawablesWithIntrinsicBounds(R.drawable.special_selector, 0, 0, 0);
        tabLayout.getTabAt(2).setCustomView(tabFour);

    }

    private void setupViewPager() {
        tabLayout.addTab(tabLayout.newTab().setText(" Businesses"), false);
        tabLayout.addTab(tabLayout.newTab().setText(" Ads"), false);
        tabLayout.addTab(tabLayout.newTab().setText(" Promotions"), false);

        tabLayout.getTabAt(0).setIcon(R.drawable.store_ico_selctor);
        tabLayout.getTabAt(1).setIcon(R.drawable.ads_selector);
        tabLayout.getTabAt(2).setIcon(R.drawable.special_selector);

    }


    @Override
    public void onBackPressed() {
        final FragmentManager fm = this.getSupportFragmentManager();
        final Fragment fragment = fm.findFragmentById(R.id.container_body);
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            if (fragment instanceof FragmentStoreFront) {
                ATPreferences.putString(context, Constants.MERCHANT_ID, "");
                ATPreferences.putBoolean(context, Constants.HEADER_STORE, false);
                linearLayoutCheckin.setVisibility(View.GONE);
                linearLayoutStoreReservation.setVisibility(View.GONE);
                linearLayoutHeaderStoreFront.setVisibility(View.GONE);
                linearLayoutHeaderCategory.setVisibility(View.GONE);
                linearLayoutStoreFavourite.setVisibility(View.GONE);
                relativeLayoutSearchBarStoreFront.setVisibility(View.GONE);
                getSupportFragmentManager().popBackStack();
                return;
            } else if (fragment instanceof FragmentHome) {
                if (doubleBackToExitPressedOnce) {
                    super.onBackPressed();
                    finishAffinity();
                    return;
                }

                this.doubleBackToExitPressedOnce = true;

                Utils.baseshowFeedbackMessage(HomeActivity.this, rootLayout, "Please click BACK again to exit");
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce = false;
                    }
                }, 2000);
            } else {
                getSupportFragmentManager().popBackStack();
                // linearLayoutStoreDetailHeader.setVisibility(View.GONE);
            }
        } else {
            if (fragment instanceof FragmentStore) {
                ATPreferences.putString(context, Constants.MERCHANT_ID, "");
                ATPreferences.putBoolean(context, Constants.HEADER_STORE, false);
                linearLayoutHeaderStoreFront.setVisibility(View.GONE);
                linearLayoutCheckin.setVisibility(View.GONE);
                linearLayoutStoreReservation.setVisibility(View.GONE);
                linearLayoutHeaderCategory.setVisibility(View.GONE);
                linearLayoutStoreFavourite.setVisibility(View.GONE);
                relativeLayoutSearchBarStoreFront.setVisibility(View.GONE);
                startActivity(new Intent(this, HomeActivity.class));
                finish();
                return;
            } else if (fragment instanceof FragmentStoreFront) {
                super.onBackPressed();
                ATPreferences.putString(context, Constants.MERCHANT_ID, "");
                ATPreferences.putBoolean(context, Constants.HEADER_STORE, false);
                linearLayoutHeaderStoreFront.setVisibility(View.GONE);
                linearLayoutCheckin.setVisibility(View.GONE);
                linearLayoutStoreReservation.setVisibility(View.GONE);
                linearLayoutHeaderCategory.setVisibility(View.GONE);
                linearLayoutStoreFavourite.setVisibility(View.GONE);
                relativeLayoutSearchBarStoreFront.setVisibility(View.GONE);
                tabContainer1.setVisibility(View.GONE);
                tabContainer2.setVisibility(View.GONE);
                setBusinessTabText(ATPreferences.readString(context, Constants.MERCHANT_CATEGORY));
                return;
            } else if (!(fragment instanceof FragmentHome)) {
                //   linearLayoutStoreDetailHeader.setVisibility(View.GONE);
                super.onBackPressed();
                //commenting this code 2 feb 21
                //   startActivity(new Intent(this, HomeActivity.class));
                //  finish();
                return;
            } else if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                finishAffinity();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Utils.baseshowFeedbackMessage(HomeActivity.this, rootLayout, "Please click BACK again to exit");

            new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
        }

    }


    @Override
    public void onDrawerItemSelected(View view, int position) {
        drawerItemSelected(position);
    }

    private void drawerItemSelected(int position) {
        resetSearch();
        clearStoreFront();
        switch (position) {
            case 0:
                if (isGuest) {
                    showGuestDialog();
                } else {
                    mlogo.setVisibility(View.VISIBLE);
                    mTxtHeading.setVisibility(View.GONE);
                    mTxtHeading.setText("Notification History");
                    inActiveTabs();
                    displayView(new FragmentNotification(), Constants.TAG_HISTORYPAGE, new Bundle());
                }
                break;
            case 1:
                mTxtHeading.setText("Got Questions?");
                mlogo.setVisibility(View.VISIBLE);
                mTxtHeading.setVisibility(View.GONE);
                inActiveTabs();
                displayView(new FragmentGotQuestions(), Constants.TAG_QUESTIONS, new Bundle());
                break;
            case 2:
                mTxtHeading.setText("Take the tour.");
                mlogo.setVisibility(View.VISIBLE);
                mTxtHeading.setVisibility(View.GONE);
                inActiveTabs();
                startActivity(new Intent(this, FragmentTour.class));
                break;
            case 3:
                mlogo.setVisibility(View.VISIBLE);
                mTxtHeading.setVisibility(View.GONE);
                displayView(new FragmentHome(), Constants.TAG_HOMEPAGE, new Bundle());
                ATPreferences.putBoolean(context, Constants.HEADER_STORE, false);
                ATPreferences.putString(context, Constants.MERCHANT_ID, "");
                break;
            case 4:
                mlogo.setVisibility(View.VISIBLE);
                mTxtHeading.setVisibility(View.GONE);
                tabLayout.getTabAt(0).select();
                break;
            case 5:
                mTxtHeading.setText("Apitap");
                mlogo.setVisibility(View.VISIBLE);
                mTxtHeading.setVisibility(View.GONE);
                tabLayout.getTabAt(1).select();
                break;
            case 6:
                mTxtHeading.setText("Apitap");
                mlogo.setVisibility(View.VISIBLE);
                mTxtHeading.setVisibility(View.GONE);
                tabLayout.getTabAt(2).select();
                break;
            case 7:
                mTxtHeading.setText("Apitap");
                mlogo.setVisibility(View.VISIBLE);
                mTxtHeading.setVisibility(View.GONE);
                displayView(new FragmentItems(), Constants.TAG_ITEMS, new Bundle());
                inActiveTabs();
                break;
            case 8:
                toolint = 0;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.CAMERA},
                                MY_SCAN_CAMERA_REQUEST_CODE);
                    } else {
                        mlogo.setVisibility(View.VISIBLE);
                        mTxtHeading.setVisibility(View.GONE);
                        inActiveTabs();
                        displayView(new FragmentScanner(), Constants.TAG_SCANNER, new Bundle());
                    }
                }
                break;
            case 9:
                if (isGuest) {
                    showGuestDialog();
                } else {
                    mTxtHeading.setText("Carts");
                    mlogo.setVisibility(View.VISIBLE);
                    mTxtHeading.setVisibility(View.GONE);
                    inActiveTabs();
                    displayView(new ShoppingCartFragment(), Constants.TAG_SHOPPING, new Bundle());
                }
                break;
            case 10:
                if (isGuest) {
                    showGuestDialog();
                } else {
                    mlogo.setVisibility(View.VISIBLE);
                    mTxtHeading.setVisibility(View.GONE);
                    mTxtHeading.setText("Messages");
                    inActiveTabs();
                    displayView(new FragmentMessages(), Constants.TAG_MESSAGEPAGE, new Bundle());
                }
                break;
            case 11:
                if (isGuest) {
                    showGuestDialog();
                } else {
                    mlogo.setVisibility(View.VISIBLE);
                    mTxtHeading.setVisibility(View.GONE);
                    mTxtHeading.setText("Apitap");
                    inActiveTabs();
                    displayView(new FragmentShoppingAsst(), Constants.TAG_SHOPPING_ASST, new Bundle());
                }
                break;
            case 12:
                if (isGuest) {
                    showGuestDialog();
                } else {
                    mTxtHeading.setText("Apitap");
                    displayView(new FragmentFavourite(), Constants.TAG_FAVOURITEPAGE, new Bundle());
                }
                break;
            case 13:
                if (isGuest) {
                    showGuestDialog();
                } else {
                    mlogo.setVisibility(View.VISIBLE);
                    mTxtHeading.setVisibility(View.GONE);
                    mTxtHeading.setText("History");
                    inActiveTabs();
                    displayView(new FragmentHistory(), Constants.TAG_HISTORYPAGE, new Bundle());
                }
                break;

            case 14:
                mlogo.setVisibility(View.VISIBLE);
                mTxtHeading.setVisibility(View.GONE);
                mTxtHeading.setText("Settings");
                inActiveTabs();
                displayView(new FragmentSettings(), Constants.TAG_SETTINGSPAGE, new Bundle());
                break;
            case 15:
                mlogo.setVisibility(View.VISIBLE);
                mTxtHeading.setVisibility(View.GONE);
                inActiveTabs();
                displayView(new FragmentAbout(), Constants.ALL_ABOUT_APITAP, new Bundle());
                break;

            case 16:
                if (isGuest) {
                    startActivity(new Intent(this, LoginActivity.class));
                } else {
                    ATPreferences.putString(context, Constants.StaySignedIn, "false");
                    ATPreferences.putString(context, Constants.KEY_USERNAME, "");
                    ATPreferences.putString(context, Constants.KEY_USERID, "");
                    ATPreferences.putString(context, Constants.LOCATION_ID, "");
                    ATPreferences.putString(context, Constants.TOKEN, "");
                    mlogo.setVisibility(View.GONE);
                    mTxtHeading.setVisibility(View.VISIBLE);
                    mTxtHeading.setText("Logout");

                    startActivity(new Intent(this, LoginActivity.class));
                    finishAffinity();

                    break;
                }
        }
    }

    private boolean checkAndRequestPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
//                || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
        ) {


            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA
                            //        , Manifest.permission.WRITE_EXTERNAL_STORAGE
                    },
                    CAMERA_PERMISSION_REQUEST_CODE);
            return false;
        }
        return true;
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, CAMERA_REQUEST_CODE);
        }
    }

    public void displayAddView(Fragment fragment, String Tag, String replaceWithTag, Bundle
            bundle) {

        // FragmentManager fragmentManager = getSupportFragmentManager();

        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        if (bundle != null)
            fragment.setArguments(bundle);
        //   FragmentUtil.printActivityFragmentList(fragmentManager);

        // Get fragment two if exist.
        Fragment fragmentTwo = Utils.getFragmentByTagName(manager, Tag);
        if (fragmentTwo != null) {
            Log.d(Utils.TAG_NAME_FRAGMENT, "Fragment Two exist in back stack, will hide it now.");
            // Hide fragment two. Only hide not destroy.
            // When user type back menu in Fragment three,
            // this hidden Fragment will be shown again with input text saved.
            fragmentTransaction.hide(fragmentTwo);
        }
        // Add Fragment with special tag name.
        fragmentTransaction.add(R.id.container_body, fragment, replaceWithTag);
        // Add fragment two in back stack.

        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        Utils.printActivityFragmentList(manager);
        removeEventBus(Tag);
    }

    public void removeTabListener() {
        tabLayout.removeOnTabSelectedListener(tabSelectedListener);
    }

    public void addTabListener() {
        tabLayout.addOnTabSelectedListener(tabSelectedListener);
        tabLayout2.addOnTabSelectedListener(tabSelectedListener);
    }

    public void displayView(Fragment fragment, String tag, Bundle bundle) {
//        if (!tag.equals(Constants.TAG_SEARCH)||!tag.equals(Constants.TAG_ITEMS)
//                ||!tag.equals(Constants.TAG_SPECIAL)||!tag.equals(Constants.TAG_ADS)) {
//            relativeLayoutSearchBar.setVisibility(View.GONE);
//        }

        //  if (fragment != null) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        //  fragmentManager.addOnBackStackChangedListener(this);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (bundle != null)
            fragment.setArguments(bundle);
        //  if (fragB == null) {
        fragmentTransaction.replace(R.id.container_body, fragment);
        if (fragment instanceof FragmentAds || fragment instanceof FragmentSpecial
                || fragment instanceof FragmentItems || fragment instanceof FragmentFavourite || fragment instanceof FragmentItemsStoreFront) {
            // fragmentTransaction.addToBackStack(null);
        } else
            fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.commit();
        checkIfStoreFront(tag);
        removeEventBus(tag);

        if (fragment instanceof FragmentAds || fragment instanceof FragmentSpecial
                || fragment instanceof FragmentStore) {
        } else {
            makeTextHighlighted(3);
        }
    }


    public void displayViewReplace(Fragment fragment, String tag, Bundle bundle) {
        if (bundle != null)
            fragment.setArguments(bundle);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.container_body, fragment, tag);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
        checkIfStoreFront(tag);
        removeEventBus(tag);
        if (fragment instanceof FragmentAds || fragment instanceof FragmentSpecial
                || fragment instanceof FragmentStore) {
        } else {
            makeTextHighlighted(3);
        }
    }

    public void displayViewAddHide(Fragment fragment, String tag, String hideTag, Bundle bundle) {
        if (bundle != null)
            fragment.setArguments(bundle);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment fragmenthide = getSupportFragmentManager().findFragmentByTag(hideTag);
        if (fragmenthide != null)
            ft.hide(getSupportFragmentManager().findFragmentByTag(hideTag));
        ft.add(R.id.container_body, fragment, tag);
        ft.addToBackStack(null);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
        checkIfStoreFront(tag);
        removeEventBus(tag);
        if (tag.equalsIgnoreCase(Constants.TAG_STORESFRONTPAGE)) {
            resetStoreFrontDetail();
        }
        if (fragment instanceof FragmentAds || fragment instanceof FragmentSpecial
                || fragment instanceof FragmentStore) {
        } else {
            makeTextHighlighted(3);
        }
    }

    private void resetStoreFrontDetail() {
        arrayListHeaderCategory = new ArrayList<>();
        itemListResponse = null;
        setStoreHeaderCategoryListAdapter();
        Picasso.get().load(R.drawable.loading)
                .placeholder(R.drawable.loading).into(imageViewStoreHeader);

    }

    private void removeEventBus(String tag) {
        if (!tag.equals(Constants.TAG_STORESFRONTPAGE)) {
            //  EventBus.getDefault().unregister(this);
        } else {
            if (!EventBus.getDefault().isRegistered(this))
                EventBus.getDefault().register(this);
        }
    }


    private void checkIfStoreFront(String tag) {
        if (ATPreferences.readBoolean(this, Constants.HEADER_STORE)) {
            if (tag.equalsIgnoreCase(Constants.TAG_STORE_DETAILS) ||
                    tag.equalsIgnoreCase(Constants.TAG_STORE_RATE) ||
                    tag.equalsIgnoreCase(Constants.TAG_STORE_MAP) ||
                    tag.equalsIgnoreCase(Constants.TAG_ABOUT_STORE)) {
                linearLayoutStoreDetailHeader.setVisibility(View.VISIBLE);
                linearLayoutStoreTabs.setVisibility(View.VISIBLE);
            } else {
                linearLayoutStoreDetailHeader.setVisibility(View.GONE);
                linearLayoutStoreTabs.setVisibility(View.GONE);
            }

            if (tag.equals(Constants.TAG_STORESFRONTPAGE) || tag.equals(Constants.TAG_SEARCH) ||
                    tag.equals(Constants.TAG_ITEMS) ||
                    tag.equals(Constants.TAG_SPECIAL) ||
                    tag.equals(Constants.TAG_ADS)) {
                //relativeLayoutSearchBarStoreFront.setVisibility(View.VISIBLE);
                linearLayoutHeaderStoreFront.setVisibility(View.VISIBLE);
                //  linearLayoutCheckin.setVisibility(View.VISIBLE); //uncomment later
                //  linearLayoutStoreReservation.setVisibility(View.VISIBLE); //uncomment later
                linearLayoutHeaderCategory.setVisibility(View.VISIBLE);
                linearLayoutStoreFavourite.setVisibility(View.GONE);
                relativeLayoutSearchBar.setVisibility(View.GONE);
/*
                Picasso.get()
                        .load(ATPreferences.readString(this, Constants.KEY_IMAGE_URL) + ATPreferences.readString(this, Constants.HEADER_IMG))
                        .placeholder(R.drawable.loading).into(imageViewStoreHeader);
*/

            } else {
                // relativeLayoutSearchBarStoreFront.setVisibility(View.GONE);
                // linearLayoutHeaderStoreFront.setVisibility(View.GONE);
            }
        }
        leftPanel.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonAddReservation:
                displayView(new FragmentAddReservation(),
                        Constants.TAG_ADD_RESERVATION, new Bundle());

                break;
            case R.id.buttonViewReservation:
                displayView(new FragmentViewReservation(),
                        Constants.TAG_VIEW_RESERVATION, new Bundle());

                break;
            case R.id.buttonCheckinHome:
                Bundle bundle8 = new Bundle();
                bundle8.putString(Constants.HEADER_TITLE, "You're Now Checked In");
                displayView(new FragmentCheckIn(),
                        Constants.TAG_CHECK_IN_TV, bundle8);
                break;

            case R.id.buttonListenLive:
                Bundle bundle9 = new Bundle();
                bundle9.putString(Constants.HEADER_TITLE, "You're Now Listening");
                displayView(new FragmentCheckIn(),
                        Constants.TAG_CHECK_IN_TV, bundle9);
                break;

            case R.id.linearLayoutBusiness:
                makeTextHighlighted(0);
                setCurrentTabFragment(0);
                break;

            case R.id.linearLayoutAds:
                makeTextHighlighted(1);
                setCurrentTabFragment(1);
                break;


            case R.id.linearLayoutPromotions:
                makeTextHighlighted(2);
                setCurrentTabFragment(2);

                break;


            case R.id.imageViewBackHeaderCat:
                if (!EventBus.getDefault().isRegistered(this))
                    EventBus.getDefault().register(this);
                onBackPressed();
                //  isCategoryHeaderSecondLevel = false;
                imageViewBackHeaderCat.setVisibility(View.GONE);
                // manageHeaderCategoryListing();
                setStoreHeaderCategoryListAdapter();
                EventBus.getDefault().post(new Event(Constants.RELOAD_ITEMS, ""));

                break;

            case R.id.buttonViewStore:
                Bundle bundle3 = new Bundle();
                bundle3.putString(Constants.MERCHANT_ID, ATPreferences.readString(context, Constants.MERCHANT_ID));
                ATPreferences.putBoolean(context, Constants.HEADER_STORE, true);
                ATPreferences.putString(context, Constants.MERCHANT_CATEGORY, "");
                ATPreferences.putString(context, Constants.MERCHANT_CATEGORY_ID, "");
                ATPreferences.putString(context, Constants.MERCHANT_ID, ATPreferences.readString(context, Constants.MERCHANT_ID));
                startActivity(new Intent(context, HomeActivity.class)
                        .putExtras(bundle3));
                finish();
                break;
            case R.id.linearLayoutLocations:
                Bundle bundle1 = new Bundle();
                bundle1.putInt("position", 99);
                bundle1.putString("merchantId", ATPreferences.readString(context, Constants.MERCHANT_ID));
                displayView(new FragmentStoreMap(), Constants.TAG_STORE_MAP, bundle1);
                viewLocations.setVisibility(View.VISIBLE);
                viewAbout.setVisibility(View.GONE);
                viewPolicies.setVisibility(View.GONE);
                viewReviews.setVisibility(View.GONE);
                break;
            case R.id.linearLayoutReviews:
                Bundle bundle4 = new Bundle();
                bundle4.putString("merchantId", ATPreferences.readString(context, Constants.MERCHANT_ID));
                bundle4.putString("storeName", ATPreferences.readString(this, Constants.STORE_NAME));
                bundle4.putString("storeImage", ATPreferences.readString(context, Constants.HEADER_IMG));
                bundle4.putString("storeRateString", storeDetailsResponse.getRESULT().get(0).getJsonMember12219());
                displayView(new FragmentStoreRate(), Constants.TAG_STORE_RATE, bundle4);
                viewReviews.setVisibility(View.VISIBLE);
                viewLocations.setVisibility(View.GONE);
                viewAbout.setVisibility(View.GONE);
                viewPolicies.setVisibility(View.GONE);
                break;
            case R.id.linearLayoutAbout:
                Bundle bundle7 = new Bundle();
                bundle7.putString("merchantId", ATPreferences.readString(context, Constants.MERCHANT_ID));
                displayView(new FragmentStoreAbout(), Constants.TAG_ABOUT_STORE, bundle7);
                viewAbout.setVisibility(View.VISIBLE);
                viewReviews.setVisibility(View.GONE);
                viewLocations.setVisibility(View.GONE);
                viewPolicies.setVisibility(View.GONE);
                break;
            case R.id.linearLayoutPolicies:
                Bundle bundle6 = new Bundle();
                bundle6.putString("merchantId", ATPreferences.readString(context, Constants.MERCHANT_ID));
                displayView(new FragmentStoreDetails(), Constants.TAG_STORE_DETAILS, bundle6);
                viewPolicies.setVisibility(View.VISIBLE);
                viewAbout.setVisibility(View.GONE);
                viewReviews.setVisibility(View.GONE);
                viewLocations.setVisibility(View.GONE);
                break;
            case R.id.details_store:
                linearLayoutHeaderStoreFront.setVisibility(View.GONE);
                linearLayoutHeaderCategory.setVisibility(View.GONE);
                linearLayoutStoreFavourite.setVisibility(View.GONE);
                relativeLayoutSearchBarStoreFront.setVisibility(View.GONE);
                Bundle bundle5 = new Bundle();
                bundle5.putInt("position", 99);
                bundle5.putString("merchantId", ATPreferences.readString(context, Constants.MERCHANT_ID));
                //   displayView(new FragmentStoreDetails(), Constants.TAG_STORE_DETAILS, bundle1);
                displayView(new FragmentStoreAbout(), Constants.TAG_ABOUT_STORE, bundle5);
                break;
            case R.id.textViewStoreNameDetails:
            case R.id.storeName:
                if (!MerchantFavouriteManager.isCurrentMerchantFav) {
                    fetchAddFavouriteResponse();
                } else
                    fetchRemoveFavouriteResponse();
                break;
            case R.id.imageViewCloseFilter:
                leftPanel.setVisibility(View.GONE);
                break;
            case R.id.message_store:
                Bundle bundle2 = new Bundle();
                bundle2.putString("merchantId", ATPreferences.readString(this, Constants.MERCHANT_ID));
                bundle2.putString("className", "Home");
                bundle2.putString("storeName", ATPreferences.readString(context, Constants.STORE_NAME));
                displayView(new FragmentMessages(), Constants.TAG_MESSAGEPAGE, bundle2);
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
            case R.id.message:
                Bundle bundle = new Bundle();
                bundle.putString("merchantId", ATPreferences.readString(this, Constants.MERCHANT_ID));
                bundle.putString("className", "Home");
                bundle.putString("storeName", ATPreferences.readString(this, Constants.STORE_NAME));
                displayView(new FragmentMessages(), Constants.TAG_MESSAGEPAGE, bundle);
                break;
            case R.id.textViewFilter:
            case R.id.textViewFilterStoreFront:
            case R.id.imageViewFilterStoreFront:
            case R.id.imageViewFilter:
                if (leftPanel.getVisibility() == View.VISIBLE)
                    leftPanel.setVisibility(View.GONE);
                else {
                    // resetSearch();
                    leftPanel.setVisibility(View.VISIBLE);
                    if (!ATPreferences.readBoolean(this, Constants.HEADER_STORE)) {
                        if (levelOneCategoryResponse == null) {
                            fetchCategoryLvlOne();
                        }
                    } else {

                    }
                }
                break;


            case R.id.startbtn:
                //   ((HomeActivity) getActivity()).displayView(new FragmentTour(), Constants.TAG_TOUR, new Bundle());
                startActivity(new Intent(this, FragmentTour.class));
                break;

            case R.id.ll_message:
                if (isGuest) {
                    showGuestDialog();
                } else {
                    // tabLayout.setVisibility(View.GONE);
                    displayView(new FragmentMessages(), Constants.TAG_MESSAGEPAGE, null);
                }
                break;
            case R.id.imageViewScan:
            case R.id.ll_scan:
                toolint = 1;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.CAMERA},
                                MY_SCAN_CAMERA_REQUEST_CODE);
                    } else {
                        //tabLayout.setVisibility(View.GONE);
                        displayView(new FragmentScanner(), Constants.TAG_SCANNER, null);
                    }
                }

                break;
            case R.id.ll_cart:
                if (isGuest) {
                    showGuestDialog();
                } else {
                    mTxtHeading.setText("Carts");
                    mlogo.setVisibility(View.VISIBLE);
                    mTxtHeading.setVisibility(View.GONE);
                    inActiveTabs();
                    displayView(new ShoppingCartFragment(), Constants.TAG_SHOPPING, new Bundle());
                }
                break;
            case R.id.ll_search:
                if (isGuest)
                    showGuestDialog();
                else {
                    displayView(new FragmentFavourite(), Constants.TAG_FAVOURITEPAGE, null);
                }
                // showSearchDialog();
                break;

            case R.id.linearLayoutBack:
                Fragment f = getSupportFragmentManager().findFragmentById(R.id.container_body);
                if (f instanceof FragmentItemsStoreFront) {
                    storeFrontBackPressed = true;
                    startActivity(new Intent(context, HomeActivity.class)
                            .putExtra(Constants.MERCHANT_ID, ATPreferences.readString(this, Constants.MERCHANT_ID)));
                    finish();
                } else
                    onBackPressed();
                break;
            case R.id.search_home:
                ATPreferences.putBoolean(this, Constants.HEADER_STORE, false);
                ATPreferences.putString(this, Constants.MERCHANT_ID, "");
                startActivity(new Intent(this, HomeActivity.class));
                finish();
                break;
            case R.id.imageViewCamera:
                if (checkAndRequestPermissions()) {
                    openCamera();
                }
                break;
            case R.id.search:
                editTextSearch.requestFocus();
            case R.id.imageViewCollapseSearch:
            case R.id.search_bar:
            case R.id.search_bar_two:
            case R.id.search_two:
                //showSearchDialog();
                resetSearch();
                if (imageViewCollapseSearch.getVisibility() == View.GONE) {
                    imageViewCollapseSearch.setVisibility(View.VISIBLE);
                    imageViewSearch.setVisibility(View.GONE);
                    imageViewScan.setVisibility(View.VISIBLE);
                    relativeLayoutSearchBar.setVisibility(View.VISIBLE);
                    linearLayoutTabs.setVisibility(View.GONE);
                } else {
                    imageViewCollapseSearch.setVisibility(View.GONE);
                    imageViewScan.setVisibility(View.GONE);
                    relativeLayoutSearchBar.setVisibility(View.GONE);
                    linearLayoutTabs.setVisibility(View.VISIBLE);
                    imageViewSearch.setVisibility(View.VISIBLE);
                }

/*                if (ATPreferences.readBoolean(this, Constants.HEADER_STORE)) {
                    if (relativeLayoutSearchBarStoreFront.getVisibility() == View.VISIBLE) {
                        relativeLayoutSearchBarStoreFront.setVisibility(View.GONE);
                        leftPanel.setVisibility(View.GONE);
                        if (searchStoreClickListener != null)
                            searchStoreClickListener.storeHeaderVisibility(false);
                    } else {
                        if (searchStoreClickListener != null)
                            searchStoreClickListener.storeHeaderVisibility(true);
                        relativeLayoutSearchBarStoreFront.setVisibility(View.VISIBLE);
                    }
                    return;
                }
                if (relativeLayoutSearchBar.getVisibility() == View.VISIBLE) {
                    if (ATPreferences.readBoolean(context, Constants.TAB_1_ACTIVE))
                        tabContainer1.setVisibility(View.VISIBLE);
                    else
                        tabContainer2.setVisibility(View.GONE);

                    relativeLayoutSearchBar.setVisibility(View.GONE);
                } else {
                    if (tabContainer1.getVisibility() == View.VISIBLE) {
                        ATPreferences.putBoolean(context, Constants.TAB_1_ACTIVE, true);
                    } else
                        ATPreferences.putBoolean(context, Constants.TAB_1_ACTIVE, false);
                    tabContainer2.setVisibility(View.GONE);
                    tabContainer2.setVisibility(View.GONE);
                    relativeLayoutSearchBar.setVisibility(View.VISIBLE);
                }*/
                break;


            case R.id.textViewSearch:
                checkAndNavigate(editTextSearch.getText().toString(), false);
                imageViewCollapseSearch.setVisibility(View.VISIBLE);
                relativeLayoutSearchBar.setVisibility(View.VISIBLE);
                linearLayoutHeaderStoreFront.setVisibility(View.GONE);
                linearLayoutCheckin.setVisibility(View.GONE);
                linearLayoutStoreReservation.setVisibility(View.GONE);
                linearLayoutHeaderCategory.setVisibility(View.GONE);
                relativeLayoutSearchBarStoreFront.setVisibility(View.GONE);

//                ATPreferences.putBoolean(this, Constants.HEADER_STORE, false);
//                ATPreferences.putString(this, Constants.MERCHANT_ID, "");

                break;

            case R.id.linearLayoutSearchFilter:
                if (imageViewCollapseSearch.getVisibility() == View.VISIBLE) {
                    checkAndNavigate(editTextSearch.getText().toString(), false);

                    imageViewCollapseSearch.setVisibility(View.VISIBLE);
                    relativeLayoutSearchBar.setVisibility(View.VISIBLE);
                    linearLayoutHeaderStoreFront.setVisibility(View.GONE);
                    linearLayoutCheckin.setVisibility(View.GONE);
                    linearLayoutStoreReservation.setVisibility(View.GONE);
                    linearLayoutHeaderCategory.setVisibility(View.GONE);
                    relativeLayoutSearchBarStoreFront.setVisibility(View.GONE);

//                    ATPreferences.putBoolean(this, Constants.HEADER_STORE, false);
//                    ATPreferences.putString(this, Constants.MERCHANT_ID, "");


                } else {
                    checkAndNavigate(editTextSearchStoreFront.getText().toString(), true);
                }
                break;

            case R.id.textViewSearchStoreFront:
                String search = "";
                if (ATPreferences.readBoolean(this, Constants.HEADER_STORE))
                    search = editTextSearchStoreFront.getText().toString();
                else
                    search = editTextSearch.getText().toString();

                checkAndNavigate(search, true);

                break;


        }
    }

    public void makeTextHighlighted(int i) {
        switch (i) {
            case 0:
                linearLayoutBusinessTab.setBackground(ContextCompat.getDrawable(this, R.drawable.rounded_inside));
                linearLayoutAdsTab.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                linearLayoutPromotionsTab.setBackgroundColor(getResources().getColor(R.color.colorWhite));

                textViewBusiness1.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                textViewAds1.setTextColor(ContextCompat.getColor(this, R.color.simple_grey));
                textViewPromotions1.setTextColor(ContextCompat.getColor(this, R.color.simple_grey));

                imageViewBusiness.setImageResource(R.drawable.ic_icon_store_white);
                imageViewAds.setImageResource(R.drawable.ic_icon_ads);
                imageViewPromotions.setImageResource(R.drawable.ic_icon_specials);
                break;

            case 1:
                linearLayoutAdsTab.setBackground(ContextCompat.getDrawable(this, R.drawable.rounded_inside));
                linearLayoutBusinessTab.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                linearLayoutPromotionsTab.setBackgroundColor(getResources().getColor(R.color.colorWhite));

                textViewBusiness1.setTextColor(ContextCompat.getColor(this, R.color.simple_grey));
                textViewAds1.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                textViewPromotions1.setTextColor(ContextCompat.getColor(this, R.color.simple_grey));

                imageViewBusiness.setImageResource(R.drawable.ic_icon_store);
                imageViewAds.setImageResource(R.drawable.ic_icon_ads_white);
                imageViewPromotions.setImageResource(R.drawable.ic_icon_specials);

                break;

            case 2:
                linearLayoutPromotionsTab.setBackground(ContextCompat.getDrawable(this, R.drawable.rounded_inside));
                linearLayoutAdsTab.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                linearLayoutBusinessTab.setBackgroundColor(getResources().getColor(R.color.colorWhite));

                textViewPromotions1.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                textViewAds1.setTextColor(ContextCompat.getColor(this, R.color.simple_grey));
                textViewBusiness1.setTextColor(ContextCompat.getColor(this, R.color.simple_grey));

                imageViewBusiness.setImageResource(R.drawable.ic_icon_store);
                imageViewAds.setImageResource(R.drawable.ic_icon_ads);
                imageViewPromotions.setImageResource(R.drawable.ic_icon_specials_white);

                break;


            case 3:
                linearLayoutPromotionsTab.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                linearLayoutAdsTab.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                linearLayoutBusinessTab.setBackgroundColor(getResources().getColor(R.color.colorWhite));

                textViewPromotions1.setTextColor(ContextCompat.getColor(this, R.color.simple_grey));
                textViewAds1.setTextColor(ContextCompat.getColor(this, R.color.simple_grey));
                textViewBusiness1.setTextColor(ContextCompat.getColor(this, R.color.simple_grey));

                imageViewBusiness.setImageResource(R.drawable.ic_icon_store);
                imageViewAds.setImageResource(R.drawable.ic_icon_ads);
                imageViewPromotions.setImageResource(R.drawable.ic_icon_specials);

                break;


        }
    }


    private void checkAndNavigate(String search, Boolean isStoreFront) {
        String categorySelect = "";
        if (textViewCategorySelect.getText().toString().isEmpty()
                || textViewCategorySelect.getText().toString().equalsIgnoreCase("Select Category"))
            categorySelect = categoryName;
        else
            categorySelect = textViewCategorySelect.getText().toString();

        Bundle bundle = new Bundle();
        if (!selectedParentId.isEmpty() && categoryId.isEmpty())
            categoryId = selectedParentId;
        bundle.putString("key", search);
        bundle.putString("categoryId", categoryId);
        bundle.putString("parentTitle", parentTitle);
        bundle.putString("parentId", selectedParentId);
        bundle.putString("grandParentId", selectedGrandParentId);
        bundle.putString("deliveryId", deliveryId);
        bundle.putString("location", location);
        bundle.putString("sortby", sortByID);
        bundle.putString("brandName", selectedBrandName);

        bundle.putString(Constants.MERCHANT_CATEGORY, categorySelect);
        bundle.putString("rating", selectedRatingId);
        if (isStoreFront)
            bundle.putString("merchantId", ATPreferences.readString(context, Constants.MERCHANT_ID));
        bundle.putString("zip", editTextZipCode.getText().toString());
        bundle.putString("condition", selectedConditionId);
        switch (lookingFor) {
            case Constants.TAG_ALL:
                displayView(new FragmentSearch(), Constants.TAG_SEARCH, bundle);
                break;
            case Constants.TAG_ADS:
                if (!selectedUpdatedCategoryId.isEmpty())
                    bundle.putString("categoryId", selectedUpdatedCategoryId);
                displayView(new FragmentAdsStoreFront(), Constants.TAG_ADS, bundle);
                break;
            case Constants.TAG_ITEMS:
                if (!selectedUpdatedCategoryId.isEmpty())
                    bundle.putString("categoryId", selectedUpdatedCategoryId);
                displayView(new FragmentItemsStoreFront(), Constants.TAG_ITEMS_STOREFRONT, bundle);
                break;
            case Constants.TAG_SPECIAL:
                if (!selectedUpdatedCategoryId.isEmpty())
                    bundle.putString("categoryId", selectedUpdatedCategoryId);
                displayView(new FragmentSpecialStoreFront(), Constants.TAG_SPECIAL, bundle);
                break;
            case Constants.TAG_STORESPAGE:
                if (!selectedUpdatedCategoryId.isEmpty())
                    bundle.putString("categoryId", selectedUpdatedCategoryId);
                if (!selectedUpdatedCategoryId.isEmpty())
                    bundle.putString("categoryId", categoryName);
                //displayView(new FragmentStore(), Constants.TAG_STORESPAGE, bundle);
                displayViewReplace(new FragmentStore(), Constants.TAG_STORESPAGE, bundle);
                break;

            default:
                displayView(new FragmentSearch(), Constants.TAG_SEARCH, bundle);
                break;
        }
        leftPanel.setVisibility(View.GONE);
    }

    private void fetchRemoveFavouriteResponse() {
        showProgress();
        ModelManager.getInstance().getMerchantFavouriteManager().removeFavourite(context,
                Operations.makeJsonRemoveMerchantFavourite(context, ATPreferences.readString(context, Constants.MERCHANT_ID)));

    }

    private void fetchAddFavouriteResponse() {
        showProgress();
        ModelManager.getInstance().getAddMerchantFavorite().addMerchantToFavorite(context,
                Operations.makeJsonMerchantAddToFavorite(context, ATPreferences.readString(context, Constants.MERCHANT_ID)));
    }


    public void showSearchDialog() {
        final Dialog dialog = Utils.showSearchDialog(HomeActivity.this, "Home");
        final AutoCompleteTextView editTextSearch = dialog.findViewById(R.id.editTextSearch);

        dialog.findViewById(R.id.textViewFilter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.findViewById(R.id.relativeLayoutFilter).performClick();
            }
        });
        dialog.findViewById(R.id.imageViewFilter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.findViewById(R.id.relativeLayoutFilter).performClick();
            }
        });
        dialog.findViewById(R.id.relativeLayoutFilter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ATPreferences.putString(context, Constants.SEARCH_KEY,
                        editTextSearch.getText().toString()
                );
                if (ATPreferences.readBoolean(context, Constants.HEADER_STORE)) {
                    ATPreferences.putString(context, Constants.MERCHANT_CATEGORY, merchantCategoryList.get(1));
                    displayView(new FragmentItemsStoreFront(), Constants.TAG_ITEMS_STOREFRONT, null);
                } else {
                    displayView(new FragmentItems(), Constants.TAG_ITEMS, null);
                    tabLayout.setScrollPosition(3, 0f, true);
                }
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
//        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(final Event event) {
        switch (event.getKey()) {
            case Constants.FETCH_IMAGE_SEARCH_RESULT:
                hideProgress();
                imageViewCollapseSearch.performClick();
                break;
            case Constants.MERCHANT_CATEGORY_LIST_HOME_ONLY:
                hideProgressAsync();
                merchantCategoryListResponse = ModelManager.getInstance().getMerchantStoresManager().merchantCategoryListModel;
                if (event.hasData()) {
                    setCategoryListAdapter();
                    setParentList();
                    if (lookingFor.equals(Constants.TAG_ITEMS))
                        fetchDeliveryServices();

//                    if (!isCategoryHeaderSecondLevel) {
//                        for (int i = 0; i < merchantCategoryListResponse.getRESULT().get(0).getRESULT().size(); i++) {
//                            arrayListHeaderCategory.add(
//                                    merchantCategoryListResponse.getRESULT().get(0).getRESULT().get(i).get_12045());
//                        }
//
//                            isCategoryHeaderSecondLevel = true;
//                        setStoreHeaderCategoryListAdapter();
//                    }

                } else {
                    if (adapterMerchantCategory != null)
                        adapterMerchantCategory.customNotify(categorySelectedPosition);
                    //Utils.baseshowFeedbackMessage(getActivity(), parentLayout, "No children categories found related");
                }


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

            case Constants.STORE_FRONT_DETAILS:
                storeDetailsResponse = ModelManager.getInstance().getMerchantStoresManager().storeDetailsModel;
                String storeImageUrl = storeDetailsResponse.getRESULT().get(0).getJsonMember121170();
                storeImageUrl = storeDetailsResponse.getRESULT().get(0).getJsonMember121170();
                //   textViewStoreName.setText(Utils.hexToASCII(storeDetailsResponse.getRESULT().get(0).getJsonMember11470()));
                Picasso.get().load(ATPreferences.readString(context, Constants.KEY_IMAGE_URL) +
                                storeImageUrl)
                        .placeholder(R.drawable.loading).into(imageViewStoreHeader);
                break;

            case Constants.STORE_FRONT_FAVOURITE:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        checkIsFavourite();
                    }
                }, 1000);
                break;

            case Constants.ADDRESS_NEARBY_SUCCESS:
                // Utils.setRecyclerNearByAdapter(getApplicationContext());
                break;

            case Constants.STORE_FRONT_BROWSE_CAT:
                browseCategoryResponse = ModelManager.getInstance().getMerchantStoresManager().browseCategoryModel;
                inflateStoreCategorySpinnerList();
                setCategorySpinnerAdapter();
                //  manageHeaderCategoryListing();
                break;

          /*  case Constants.ITEM_LIST_DATA:
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        if (itemListResponse == null || (browseCategoryResponse != null && browseCategoryResponse.getRESULT().size() > 1)) {
                            itemListResponse = ModelManager.getInstance().getItemManager().itemStoreFrontListModel;
                            if (event.hasData()) {
                                if (itemListResponse != null && itemListResponse.getRESULT() != null && itemListResponse.getRESULT().size() > 0) {
                                    arrayListHeaderCategory = new ArrayList<>();
                                    for (int i = 0; i < itemListResponse.getRESULT().get(0).getRESULT().size(); i++) {
                                        arrayListHeaderCategory.add(itemListResponse.getRESULT().get(0).getRESULT().get(i).getJsonMember11453());
                                    }
                                    setStoreHeaderCategoryListAdapter();
                                }
                            }
                        }
                    }
                }, 1000);
                break;
*/
            case Constants.LEVEL_ONE_CATEGORY:
                hideProgress();

                levelOneCategoryResponse = ModelManager.getInstance().getMerchantStoresManager().levelOneCategoryModel;
                if (event.hasData() && !ATPreferences.readBoolean(context, Constants.HEADER_STORE)) {
                    inflateCategorySpinnerList();
                    setCategorySpinnerAdapter();
                } else {
                    Utils.baseshowFeedbackMessage(this, rootLayout, "No Level One Category Found");
                }

                break;


            case Constants.DELIVERY_SERVICES:
                hideProgress();
                //checkLookingForHitApi();
                deliveryServiceResponse = ModelManager.getInstance().getMerchantStoresManager().deliveryServiceModel;
                if (lookingFor.equals(Constants.TAG_ITEMS) && brandNamesResponse == null)
                    fetchBrandNames();
                if (event.hasData()) {
                    inflateDeliveryList();
                    setDeliveryMethodSpinnerAdapter();
                } else {
                    //Utils.baseshowFeedbackMessage(this, rootLayout, "No Delivery Data Found");
                }

                break;
            case Constants.BRAND_NAMES:
                hideProgress();
                brandNamesResponse = ModelManager.getInstance().getMerchantStoresManager().brandNamesModel;
                inflateBrandName();
                setBrandNameSpinnerAdapter();
                fetchRatingList();
                break;

            case Constants.RATINGS:
                hideProgress();
                hideProgressAsync();
                ratingsResponse = ModelManager.getInstance().getMerchantStoresManager().ratingModel;
                inflateRatingList();
                setRatingAdapter();
                break;

            case Constants.FCM_MSG_NOTIFICATION:
                ll_msgCount.setVisibility(View.VISIBLE);
                tv_msgCount.setText(MyFirebaseMessagingService.msgCount + "");
                reloadDialog("Message");
                break;

            case Constants.NOTIFICATION_ARRIVED:
                reloadDialog("Items");
                break;

            case Constants.UPDATE_PASSWORD_SUCCESS:
                hideProgress();
                if (dialogTempPassword != null && dialogTempPassword.isShowing()) {
                    dialogTempPassword.dismiss();

                    Utils.baseshowFeedbackMessage(this,
                            rootLayout, "Password Updated. Please login with new password");
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ATPreferences.putString(context, Constants.StaySignedIn, "false");
                            ATPreferences.putString(context, Constants.KEY_USERNAME, "");
                            ATPreferences.putString(context, Constants.KEY_USERID, "");
                            ATPreferences.putString(context, Constants.LOCATION_ID, "");
                            ATPreferences.putString(context, Constants.TOKEN, "");
                            startActivity(new Intent(context, LoginActivity.class));
                            finishAffinity();
                        }
                    }, 800);
                }
                break;

            case -1:
                hideProgress();
                Utils.baseshowFeedbackMessage(this,
                        rootLayout,
                        event.getResponse());
                break;
        }
    }


    private void checkIsFavourite() {
        for (int i = 0; i < MerchantFavouriteManager.mernchantfavlist.size(); i++) {
            if (ATPreferences.readString(context, Constants.MERCHANT_ID)
                    .equals(MerchantFavouriteManager.mernchantfavlist.get(i))) {
                MerchantFavouriteManager.isCurrentMerchantFav = true;
                //isFavouriteStore = true;
                FragmentHome.setFavouriteMerchantView(textViewStoreName);
                FragmentHome.setFavouriteMerchantView(textViewStoreNameDetails);
                break;
            } else {
                MerchantFavouriteManager.isCurrentMerchantFav = false;
                //isFavouriteStore = false;
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
            notifyParentList(arrayListSelectParent);
        }
    }

    private void setParentListAdapter() {
        adapterParentCategories = new AdapterParentCategoriesItem(context, arrayListSelectParent, this);
        recycelerViewParentCategory.setAdapter(adapterParentCategories);
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

    private void notifyParentList(List<SelectedParentModel> arrayListSelectParent) {
        if (adapterParentCategories != null)
            adapterParentCategories.customNotify(arrayListSelectParent);
    }


    private void reloadDialog(final String key) {
        TextView textView_yes = dialogReload.findViewById(R.id.txtok);
        TextView textView_no = dialogReload.findViewById(R.id.txtcancel);
        TextView textView_title = dialogReload.findViewById(R.id.txtmessage);

        textView_title.setText("You have received a new notification. Would you like to view it now?");
        textView_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // reloadFragment();
                if (key.equals("Items")) {
                    ModelManager.getInstance().setProductSeen().setProductSeen(HomeActivity.this, Operations.makeProductSeen
                            (HomeActivity.this, productId));
                    Bundle bundle = new Bundle();
                    bundle.putString("productId", productId);
                    bundle.putString("productType", "21");
                    FragmentItemDetails fragment = new FragmentItemDetails();
                    fragment.setArguments(bundle);
                    displayAddView(fragment, Constants.TAG_ITEMS, Constants.TAG_DETAILSPAGE, bundle);
                    dialogReload.dismiss();
                    tabContainer2.setVisibility(View.GONE);
                    tabContainer2.setVisibility(View.GONE);

                } else {
                    Bundle bundle2 = new Bundle();
                    if (!productId.isEmpty()) {
                        bundle2.putString("productId", productId);
                        bundle2.putString("productName", productName);
                    } else if (!adId.isEmpty()) {
                        bundle2.putString("adID", adId);
                        bundle2.putString("adName", "");
                    } else if (!invoiceId.isEmpty())
                        bundle2.putString("invoice", invoiceId);
                    else if (!generalMessageId.isEmpty())
                        bundle2.putString("generalId", generalMessageId);
                    bundle2.putString("merchantId", "");
                    bundle2.putString("merchantName", storeName);
                    dialogReload.dismiss();
                    tabContainer2.setVisibility(View.GONE);
                    tabContainer2.setVisibility(View.GONE);

                    displayView(new FragmentMessageDetail(),
                            Constants.MessageDetailPage, bundle2);

                }
            }
        });

        textView_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogReload.dismiss();

            }
        });

        dialogReload.show();

    }

    public void showGuestDialog() {
        Utils.showGuestDialog(HomeActivity.this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (storeFrontBackPressed) {
            storeFrontBackPressed = false;
        } else {
            ATPreferences.putBoolean(getApplicationContext(), Constants.HEADER_STORE, false);

        }
        // ATPreferences.putString(context, Constants.MERCHANT_ID, "");
        EventBus.getDefault().unregister(this);

    }

    public String getLocations() {
        String address = "";
        GPSService mGPSService = new GPSService(context);
        mGPSService.getLocation();
        boolean b = Utils.checkLocationPermission(HomeActivity.this);
        if (!b) {

            // Here you can ask the user to try again, using return; for that
            //Toast.makeText(context, "Your location is not available, please try again.", Toast.LENGTH_SHORT).show();
            Utils.baseshowFeedbackMessage(HomeActivity.this, rootLayout, "Your location is not available, please try again.");

            return "";

            // Or you can continue without getting the location, remove the return; above and uncomment the line given below
            // address = "Location not available";
        } else {

            // Getting location co-ordinates
            double latitude = mGPSService.getLatitude();
            double longitude = mGPSService.getLongitude();


            //Toast.makeText(context, "Latitude:" + latitude + " | Longitude: " + longitude, Toast.LENGTH_LONG).show();


            address = mGPSService.getLocationAddress();

        }

        //Toast.makeText(context, "Your address is: " + address, Toast.LENGTH_SHORT).show();

        // make sure you close the gps after using it. Save user's battery power
        mGPSService.closeGPS();
        return address;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_SCAN_CAMERA_REQUEST_CODE) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (toolint == 0) {

                    //  Toast.makeText(this, "Camera permission granted", Toast.LENGTH_LONG).show();
                    mTxtHeading.setText("Scan");
                    mlogo.setVisibility(View.VISIBLE);
                    mTxtHeading.setVisibility(View.GONE);
                    // tabLayout.setVisibility(View.GONE);
                    displayView(new FragmentScanner(), Constants.TAG_SCANNER, new Bundle());

                } else {
                    displayView(new FragmentScanner(), Constants.TAG_SCANNER, null);
                }
            } else {
                Utils.baseshowFeedbackMessage(HomeActivity.this, rootLayout, "Camera permission denied");

            }

        } else if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // permission was granted, yay! Do the
                // location-related task you need to do.
                String address = getLocations();
                //items.setText(address);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        //Request location updates:

                    }
                }

            }
        } else if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            }
        } else {
            showToast(getApplicationContext(), "Permissions Denied.");
        }
    }


    @Override
    public void onBackStackChanged() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        final Fragment fragment = fragmentManager.findFragmentById(R.id.container_body);
//        if (fragment instanceof FragmentSpecial) {
//            TabLayout.Tab tab = tabLayout.getTabAt(2);
//            removeTabListener();
//            tab.select();
//            addTabListener();
//
//        }else if(fragment instanceof FragmentStore){
//            TabLayout.Tab tab = tabLayout.getTabAt(0);
//            removeTabListener();
//            tab.select();
//            addTabListener();
//        }else if(fragment instanceof FragmentAds){
//            TabLayout.Tab tab = tabLayout.getTabAt(1);
//            removeTabListener();
//            tab.select();
//            addTabListener();
//        }
        //   Toast.makeText(context, fragment.getTag() + "", Toast.LENGTH_SHORT).show();

//        if (fragmentManager!=null)
//        {
//            FragmentItems fragmentHome = (FragmentItems) fragmentManager.findFragmentByTag(Constants.TAG_ITEMS);
//            fragmentHome.onResume();
//        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
/*
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
*/
    }

    private void fetchDeliveryServices() {
        // showProgress();
        ModelManager.getInstance().getMerchantStoresManager()
                .getDeliveryServices(this,
                        Operations.getDeliveryServices(this, categoryId, ""));

    }

    private void fetchBrandNames() {
        //`     showProgress();
        ModelManager.getInstance().getMerchantStoresManager()
                .getBrandNames(this,
                        Operations.getBrandNames(this, categoryId, searchkey,
                                ATPreferences.readString(context, Constants.MERCHANT_ID)));
    }

    private void fetchRatingList() {
        // showProgress();
        ModelManager.getInstance().getMerchantStoresManager()
                .getRatings(this,
                        Operations.getRatingList(this, categoryId, searchkey,
                                ATPreferences.readString(context, Constants.MERCHANT_ID)));

    }


    private void fetchCategoryLvlOne() {
        showProgress();
        ModelManager.getInstance().getMerchantStoresManager().getCategoryLvlOne
                (this, Operations.getCategoriesLvlOne(this));
    }


    private void spinnerListeners() {
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
        spinnerDelivery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (deliveryServiceResponse == null)
                    return;
                if (position != 0)
                    deliveryId = deliveryServiceResponse.getRESULT().get(0).getRESULT().get(position).get_12239();
                else
                    deliveryId = "";

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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

        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    categoryId = "";
                    return;
                }
                ATPreferences.putString(context, Constants.MERCHANT_CATEGORY, arrayListSpinnerCategory.get(position));
                if (ATPreferences.readBoolean(context, Constants.HEADER_STORE)) {
                    if (browseCategoryResponse == null)
                        return;
                    categoryId = browseCategoryResponse.getRESULT().get(position - 1).getJsonMember11493();
                    categoryName = arrayListSpinnerCategory.get(position);

//                    if (lookingFor.equals(Constants.TAG_ITEMS))
//                        fetchDeliveryServices();
                } else {
                    if (levelOneCategoryResponse == null)
                        return;
                    categoryId = levelOneCategoryResponse.getRESULT().get(0).getRESULT().get(position - 1).get_11493();
                    categoryName = arrayListSpinnerCategory.get(position);


                 /*   if (lookingFor.equals(Constants.TAG_ITEMS))
                        fetchDeliveryServices();
                    else {
                        checkLookingForHitApi();
                    }*/
                }
                checkLookingForHitApi();
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerLookingFor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    lookingFor = Constants.TAG_ALL;
                    linearLayoutDelivery.setVisibility(View.GONE);
                    linearLayoutBusiness.setVisibility(View.VISIBLE);
                    linearLayoutCondition.setVisibility(View.GONE);
                    linearLayoutCategory.setVisibility(View.GONE);
                    linearLayoutRating.setVisibility(View.GONE);
                    linearLayoutBrand.setVisibility(View.GONE);

                } else if (position == 3) {
                    lookingFor = Constants.TAG_ITEMS;
                    linearLayoutDelivery.setVisibility(View.VISIBLE);
                    linearLayoutCategory.setVisibility(View.VISIBLE);
                    linearLayoutCondition.setVisibility(View.VISIBLE);
                    linearLayoutBusiness.setVisibility(View.VISIBLE);
                    linearLayoutRating.setVisibility(View.VISIBLE);
                    linearLayoutBrand.setVisibility(View.VISIBLE);
                    if (brandNamesResponse == null)
                        fetchBrandNames();
                } else if (position == 1) {
                    lookingFor = Constants.TAG_STORESPAGE;
                    linearLayoutBusiness.setVisibility(View.VISIBLE);
                    linearLayoutDelivery.setVisibility(View.GONE);
                    linearLayoutCategory.setVisibility(View.VISIBLE);
                    linearLayoutCondition.setVisibility(View.GONE);
                    linearLayoutRating.setVisibility(View.GONE);
                    linearLayoutBrand.setVisibility(View.GONE);
                } else if (position == 2) {
                    lookingFor = Constants.TAG_SPECIAL;
                    linearLayoutDelivery.setVisibility(View.GONE);
                    linearLayoutBusiness.setVisibility(View.VISIBLE);
                    linearLayoutCategory.setVisibility(View.VISIBLE);
                    linearLayoutCondition.setVisibility(View.GONE);
                    linearLayoutRating.setVisibility(View.GONE);
                    linearLayoutBrand.setVisibility(View.GONE);
                } else if (position == 4) {
                    lookingFor = Constants.TAG_ADS;
                    linearLayoutDelivery.setVisibility(View.GONE);
                    linearLayoutBusiness.setVisibility(View.VISIBLE);
                    linearLayoutCategory.setVisibility(View.VISIBLE);
                    linearLayoutCondition.setVisibility(View.GONE);
                    linearLayoutRating.setVisibility(View.GONE);
                    linearLayoutBrand.setVisibility(View.GONE);
                }

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        checkLookingForHitApi();
                    }
                }, 1000);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerSortBy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sortByID = sortByListId().get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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

    private void resetSearch() {
        levelOneCategoryResponse = null;
        editTextSearch.setText("");
        editTextSearchStoreFront.setText("");
        editTextZipCode.setText("");
        setConditionSpinnerAdapter();
        setSortByAdapter();
        setSearchFilterSpinnerAdapter();
        setCategorySpinnerAdapter();
        setDeliveryMethodSpinnerAdapter();
        setLookingForSpinnerAdapter();
        setRatingAdapter();
        setBrandNameSpinnerAdapter();
    }

    private void clearStoreFront() {
        ATPreferences.putString(context, Constants.MERCHANT_CATEGORY, "");
        ATPreferences.putString(context, Constants.HEADER_IMG, "");
        ATPreferences.putBoolean(context, Constants.HEADER_STORE, false);
        ATPreferences.putString(context, Constants.MERCHANT_ID, "");
        linearLayoutHeaderStoreFront.setVisibility(View.GONE);
        linearLayoutCheckin.setVisibility(View.GONE);
        linearLayoutStoreReservation.setVisibility(View.GONE);
        linearLayoutHeaderCategory.setVisibility(View.GONE);
        linearLayoutStoreFavourite.setVisibility(View.GONE);
        relativeLayoutSearchBarStoreFront.setVisibility(View.GONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getExtras() != null) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            String base64String = Utils.bitmapToBase64(imageBitmap);
            Log.d("TAG", "onActivityResult: " + base64String);

            fetchImageSearchResult(base64String);
            // You can display or save the bitmap image
        } else {
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container_body);
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onParentCategoryClick(final int position) {
        selectedParentId = arrayListSelectParent.get(position).getParentId();
        textViewCategorySelect.setText(arrayListSelectParent.get(position).getTitle());
        if (selectedParentId.equals(categoryId)) { //that means position selected is 0 position
            selectedParentId = "";
            selectedGrandParentId = "";
            //selectedUpdatedCategoryId = categoryId;
            parentTitle = "";
            textViewCategorySelect.setText("Select Category");
        }
        selectedUpdatedCategoryId = arrayListSelectParent.get(position).getParentId();
        checkLookingForHitApi();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                arrayListSelectParent = removeItemsFromParentList(position);
                notifyParentList(arrayListSelectParent);
            }
        }, 1000);

    }

    private void checkLookingForHitApi() {
        switch (lookingFor) {
            case Constants.TAG_ALL:
                break;
            case Constants.TAG_ADS:
                fetchAdsCategoryList(searchkey, true);
                break;
            case Constants.TAG_ITEMS:
                fetchItemCategoryList(searchkey, true);
                break;
            case Constants.TAG_SPECIAL:
                fetchSpecialsCategoryList(searchkey, true);
                break;
            case Constants.TAG_STORESPAGE:
                fetchStoreCategoryList(searchkey, true);
                break;
            default:
                break;
        }
    }

    private List<SelectedParentModel> removeItemsFromParentList(int position) {
        if (position == 0) {
            return new ArrayList<>();
        } else {
            return arrayListSelectParent.subList(0, position);
        }
    }

    public void fetchStoreCategoryList(String searchkey, boolean showLoader) {
        if (showLoader)
            showProgressAsync();
        ModelManager.getInstance().getMerchantStoresManager()
                .getMerchantCategoryListHomeOnly(this, Operations.getMerchantCategoryList(this,
                        selectedUpdatedCategoryId, searchkey, deliveryId, selectedParentId));
    }

    public void fetchItemCategoryList(String searchkey, boolean showLoader) {
        if (showLoader)
            showProgressAsync();
        String search = "";

        ModelManager.getInstance().getMerchantStoresManager()
                .getMerchantCategoryListHomeOnly(this, Operations.getItemCategoryList(this,
                        selectedUpdatedCategoryId, searchkey, deliveryId, selectedParentId,
                        ATPreferences.readString(context, Constants.MERCHANT_ID)));
    }

    public void fetchAdsCategoryList(String searchkey, boolean showLoader) {
        if (showLoader)
            showProgressAsync();
        ModelManager.getInstance().getMerchantStoresManager()
                .getMerchantCategoryListHomeOnly(this, Operations.getAdsCategoryList(this,
                        selectedUpdatedCategoryId, searchkey, deliveryId, selectedParentId, ""));
    }

    public void fetchSpecialsCategoryList(String searchkey, boolean showLoader) {
        if (showLoader)
            showProgressAsync();
        ModelManager.getInstance().getMerchantStoresManager()
                .getMerchantCategoryListHomeOnly(this, Operations.getSpecialsCategoryList(this,
                        selectedUpdatedCategoryId, searchkey, deliveryId, selectedParentId, ""));
    }


    private void fetchImageSearchResult(String imageBase64) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.IMAGE_BASE_64, imageBase64);
        displayView(new FragmentSearch(),
                Constants.TAG_SEARCH, bundle);
    }

    @Override
    public void onMerchantCategoryClick(int position) {
        categorySelectedPosition = position;
        //if (categoryId.isEmpty())
        categoryId = merchantCategoryListResponse.getRESULT().get(0).getRESULT().get(position).get_11493();
        selectedUpdatedCategoryId = merchantCategoryListResponse.getRESULT().get(0).getRESULT().get(position).get_11493();
        //  else
        selectedUpdatedCategoryId = merchantCategoryListResponse.getRESULT().get(0).getRESULT().get(position).get_11493();
        selectedParentId = merchantCategoryListResponse.getRESULT().get(0).getRESULT().get(position).get_11493();
        parentTitle = merchantCategoryListResponse.getRESULT().get(0).getRESULT().get(position).get_12045();
        selectedGrandParentId = merchantCategoryListResponse.getRESULT().get(0).getRESULT().get(position).get_12221();
        checkLookingForHitApi();

        textViewCategorySelect.setText(parentTitle);
        rvMerchantCategory.setVisibility(View.GONE);
        recycelerViewParentCategory.setVisibility(View.GONE);
    }

    @Override
    public void onCategoryHeaderClick(int position) {
        // headerClick(position, itemListResponse, true);
    }

    public void headerClick(int position, ItemStoreFrontResponse itemListResponse,
                            boolean isDisplayView) {
      /*  recyclerViewCategoryHeader = findViewById(R.id.recyclerViewCategoryHeader);
        adapterHeaderCategory = null;
        if (browseCategoryResponse.getRESULT().size() == 1) {
            String categoryName = itemListResponse.getRESULT().get(0).getRESULT().get(position).getJsonMember11453();
            ATPreferences.putString(context, Constants.MERCHANT_CATEGORY_ID, itemListResponse.getRESULT().get(0).getRESULT().get(position).getJsonMember11493());
            ATPreferences.putString(context, Constants.MERCHANT_CATEGORY, categoryName);
            Bundle bundler = new Bundle();
            bundler.putString(Constants.MERCHANT_CATEGORY_ID, itemListResponse.getRESULT().get(0).getRESULT().get(position).getJsonMember11493());
            // displayViewAddHide(new FragmentItemsStoreFront(), Constants.TAG_ITEMS_STOREFRONT, Constants.TAG_STORESFRONTPAGE, bundler);
            if (isDisplayView)
                displayView(new FragmentItemsStoreFront(), Constants.TAG_ITEMS_STOREFRONT, bundler);
            // imageViewBackHeaderCat.setVisibility(View.VISIBLE);
            return;
        }
        if (imageViewBackHeaderCat.getVisibility() == View.GONE) {
            String categoryName = itemListResponse.getRESULT().get(0).getRESULT().get(position).getJsonMember11453();
            ATPreferences.putString(context, Constants.MERCHANT_CATEGORY_ID, itemListResponse.getRESULT().get(0).getRESULT().get(position).getJsonMember11493());
            ATPreferences.putString(context, Constants.MERCHANT_CATEGORY, categoryName);
            Bundle bundler = new Bundle();
            bundler.putString(Constants.MERCHANT_CATEGORY_ID, itemListResponse.getRESULT().get(0).getRESULT().get(position).getJsonMember11493());
            //  displayViewAddHide(new FragmentItemsStoreFront(), Constants.TAG_ITEMS_STOREFRONT, Constants.TAG_STORESFRONTPAGE, bundler);
            if (isDisplayView) {
                displayView(new FragmentItemsStoreFront(), Constants.TAG_ITEMS_STOREFRONT, bundler);
                imageViewBackHeaderCat.setVisibility(View.VISIBLE);
            }
        } else {
            //  FragmentItemsStoreFront myFragment = (FragmentItemsStoreFront)getSupportFragmentManager().findFragmentByTag(Constants.TAG_ITEMS_STOREFRONT);
            if (FragmentItemsStoreFront.isActive) {
                //  Toast.makeText(context, "Visible", Toast.LENGTH_SHORT).show();
                if (isDisplayView) {
                    EventBus.getDefault().post(new Event(Constants.ITEMS_REFRESH, itemListResponse.getRESULT().get(0).getRESULT().get(position).getJsonMember11493()));
                    EventBus.getDefault().unregister(this);
                }
            } else {
                String categoryName = itemListResponse.getRESULT().get(0).getRESULT().get(position).getJsonMember11453();
                ATPreferences.putString(context, Constants.MERCHANT_CATEGORY_ID, itemListResponse.getRESULT().get(0).getRESULT().get(position).getJsonMember11493());
                ATPreferences.putString(context, Constants.MERCHANT_CATEGORY, categoryName);
                Bundle bundler = new Bundle();
                bundler.putString(Constants.MERCHANT_CATEGORY_ID, itemListResponse.getRESULT().get(0).getRESULT().get(position).getJsonMember11493());
                //  displayViewAddHide(new FragmentItemsStoreFront(), Constants.TAG_ITEMS_STOREFRONT, Constants.TAG_STORESFRONTPAGE, bundler);
                if (isDisplayView) {
                    displayView(new FragmentItemsStoreFront(), Constants.TAG_ITEMS_STOREFRONT, bundler);
                    imageViewBackHeaderCat.setVisibility(View.VISIBLE);
                }
            }
        }*/
    }


}
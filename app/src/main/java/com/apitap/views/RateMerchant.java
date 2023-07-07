package com.apitap.views;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.annotation.NonNull;

import com.apitap.controller.MerchantFavouriteManager;
import com.apitap.model.storeFrontItems.details.RESULTItem;
import com.apitap.views.fragments.favourite.FragmentFavourite;
import com.apitap.views.fragments.specials.FragmentSpecial;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.apitap.R;
import com.apitap.controller.ModelManager;
import com.apitap.model.AddTabBar;
import com.apitap.model.Constants;
import com.apitap.model.Operations;
import com.apitap.model.Utils;
import com.apitap.model.bean.RatingBean;
import com.apitap.model.customclasses.Event;
import com.apitap.model.preferences.ATPreferences;
import com.apitap.views.NavigationMenu.FragmentDrawer;
import com.apitap.views.fragments.items.FragmentItems;
import com.apitap.views.fragments.messages.FragmentMessages;
import com.apitap.views.fragments.FragmentScanner;
import com.apitap.views.adapters.RatingItemAdapter;
import com.apitap.views.fragments.ads.FragmentAds;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.apitap.views.fragments.home.FragmentHome.setFavouriteMerchantView;

public class RateMerchant extends AppCompatActivity implements View.OnClickListener, FragmentDrawer.FragmentDrawerListener {

    RecyclerView mRecycler;
    RatingItemAdapter ratingItemAdapter;
    ArrayList<Integer> arrayList;
    private RatingBar userRating;
    private FragmentDrawer drawerFragment;
    private Toolbar mToolbar;
    private DrawerLayout drawer_layout;
    private EditText comments;
    private TextView storereviews;
    ImageView storeImage, tv_back;
    private boolean isGuest = false;
    CircularProgressView mPocketBar;
    private RESULTItem data;
    private String UserRating = "1.0", merchantId, RatingServerNo, storeimage, storeName;
    private TextView currentRatingTv, storeNametv, submit;
    Activity mActivity;
    private static int toolint = 0;
    FrameLayout frameLayout;
    TextView ratingNo;
    List<RatingBean.RESULT_> ratingBeen;
    private RatingBar ratingBar;
    public static TabLayout tabLayout;
    EditText mComment;
    private ImageView inbox, favourite, share, img_main;
    LinearLayout scan_tool;
    LinearLayout msg_tool;
    LinearLayout tabConatiner;
    LinearLayout search_tool;
    Button storeBrowse;
    String rating_number;
    int rating_prog;
    ScrollView scroll_view;
    LinearLayout browse_store;
    private TextView tabOne, tabTwo, tabThree, tabFour, tabFive, tabSix;
    private ImageView  homeTab2;
    LinearLayout back_tool;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    boolean isFavorite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_merchant2);
        mActivity = this;
        arrayList = new ArrayList<Integer>(10);
        isGuest = ATPreferences.readBoolean(this, Constants.GUEST);
        mRecycler = findViewById(R.id.mRecyclerRev);
        initViews();
        if (getIntent() != null) {
            merchantId = getIntent().getStringExtra("merchantId");
            storeimage = getIntent().getStringExtra("storeImage");
            storeName = getIntent().getStringExtra("storeName");
            rating_number = getIntent().getStringExtra("storeRateString");
            rating_prog = getIntent().getIntExtra("rateProgress", 0);
//            if (storeName.length() > 15)
//                storeNametv.setText(storeName.substring(0,14) + "...");
//            else
            storeNametv.setText(storeName);
            //   storeNametv.setText(storeName);
            Picasso.get().load(ATPreferences.readString(this, Constants.KEY_IMAGE_URL) + storeimage).into(storeImage);

            Log.d("MerchantIDimg", rating_prog + "   s");
        }
        add();
    }

    private void initViews() {
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        tabLayout = findViewById(R.id.tabs);
        tabConatiner = findViewById(R.id.tab_container);
        homeTab2 = findViewById(R.id.tab_one_image);
        drawer_layout = findViewById(R.id.drawer_layout);
        userRating = findViewById(R.id.myratingBar);
        storeImage = findViewById(R.id.store_img);
        storeNametv = findViewById(R.id.store_name);
        mComment = findViewById(R.id.comment);
        scroll_view = findViewById(R.id.scroll_view);
        tv_back = findViewById(R.id.tv_back);
        storereviews = findViewById(R.id.storereviews);
        submit = findViewById(R.id.submit);
        comments = findViewById(R.id.comment);
        storeBrowse = findViewById(R.id.details_store);
        currentRatingTv = findViewById(R.id.currentRating);
        mPocketBar = findViewById(R.id.pocket);
        inbox = findViewById(R.id.messages);
        favourite = findViewById(R.id.favourite);
        share = findViewById(R.id.share);
        ratingBar = findViewById(R.id.ratingBarcurrent);
        frameLayout = findViewById(R.id.container_body);
        ratingNo = findViewById(R.id.ratingNo);
        browse_store = findViewById(R.id.browse_store);
        frameLayout.setVisibility(View.GONE);

        scan_tool = findViewById(R.id.ll_scan);
        msg_tool = findViewById(R.id.ll_message);
        search_tool = findViewById(R.id.ll_search);
        back_tool = findViewById(R.id.iv_back);

        scan_tool.setOnClickListener(this);
        msg_tool.setOnClickListener(this);
        search_tool.setOnClickListener(this);
        storeBrowse.setOnClickListener(this);
        submit.setOnClickListener(this);
        storeNametv.setOnClickListener(this);
        inbox.setOnClickListener(this);
        share.setOnClickListener(this);
        browse_store.setOnClickListener(this);
        tv_back.setOnClickListener(this);


        //mPocketBar.setVisibility(View.VISIBLE);
//        ModelManager.getInstance().getMerchantFavouriteManager().getFavourites(mActivity,
//                Operations.makeJsonGetMerchantFavourite(mActivity));


        ratingBeen = ModelManager.getInstance().getAddMerchantRating().ratingBean.getRESULT().get(0).getRESULT();
        ratingNo.setText("(" + ratingBeen.size() + ")");
        if (ratingBeen.size() == 0) {
            ratingNo.setText("No Ratings");
        }
        storereviews.setText("Current Reviews-" + ratingBeen.size());

        LayerDrawable stars = (LayerDrawable) userRating.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.parseColor("#fcb74d"), PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(0).setColorFilter(Color.parseColor("#fcb74d"), PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(1).setColorFilter(Color.parseColor("#fcb74d"), PorterDuff.Mode.SRC_ATOP);
        userRating.setEnabled(true);
        userRating.setFocusable(true);
        userRating.bringToFront();
        userRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                // This event is fired when rating is changed

                currentRatingTv.setText(String.valueOf(rating));
                UserRating = currentRatingTv.getText().toString();
                Log.d("RatingLogss", rating + "");
            }
        });


        LayerDrawable stars1 = (LayerDrawable) ratingBar.getProgressDrawable();
        data =  ModelManager.getInstance().getMerchantStoresManager().storeDetailsModel.getRESULT().get(0);
        if (data.getJsonMember12219().equals("0")) {
            ratingNo.setText("No Ratings");
            stars1.getDrawable(0).setColorFilter(Color.parseColor("#fcb74d"), PorterDuff.Mode.SRC_ATOP);
            ratingBar.setRating(Float.parseFloat(data.getJsonMember12219()));
        } else {
            stars1.getDrawable(2).setColorFilter(Color.parseColor("#fcb74d"), PorterDuff.Mode.SRC_ATOP);
            stars1.getDrawable(0).setColorFilter(Color.parseColor("#fcb74d"), PorterDuff.Mode.SRC_ATOP);
            stars1.getDrawable(1).setColorFilter(Color.parseColor("#fcb74d"), PorterDuff.Mode.SRC_ATOP);
            ratingBar.setRating(Float.parseFloat(data.getJsonMember12219()));
        }

        if (MerchantFavouriteManager.isCurrentMerchantFav)
            isFavorite = true;
        setFavouriteMerchantView(storeNametv);

        AddTabBar.getmInstance().setupViewPager(tabLayout);
        AddTabBar.getmInstance().setupTabIcons(tabLayout, mActivity, tabOne, tabTwo, tabThree, tabFour,
                tabFive, tabSix,homeTab2);
        AddTabBar.getmInstance().bindWidgetsWithAnEvent(tabConatiner, tabLayout, RateMerchant.this, R.id.container_body2);

    }


    private void add() {
        ratingItemAdapter = new RatingItemAdapter(RateMerchant.this, ratingBeen);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecycler.setLayoutManager(mLayoutManager);
        mRecycler.setItemAnimator(new DefaultItemAnimator());
        mRecycler.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mRecycler.setAdapter(ratingItemAdapter);
    }

    public String RatingfromServer() {
        switch (UserRating) {
            case "1.0":
                RatingServerNo = "2101";
                break;
            case "2.0":
                RatingServerNo = "2102";
                break;
            case "3.0":
                RatingServerNo = "2103";
                break;
            case "4.0":
                RatingServerNo = "2104";
                break;
            case "5.0":
                RatingServerNo = "2105";
                break;
        }
        return RatingServerNo;
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
            case Constants.ADD_TO_RATING_SUCCESS:
                mPocketBar.setVisibility(View.GONE);
                comments.setText("");
              //  Toast.makeText(mActivity, "Rating Success", Toast.LENGTH_SHORT).show();
                Utils.baseshowFeedbackMessage(this,drawer_layout,"Rating Success");
                ModelManager.getInstance().getAddMerchantRating().getMerchantRating(RateMerchant.this,
                        Operations.GetMerchantRating(mActivity, ATPreferences.readString(this, Constants.KEY_USERID), merchantId));

                break;

            case Constants.ADDRESS_NEARBY_SUCCESS:
                Utils.setRecyclerNearByAdapter(getApplicationContext());
                break;
            case Constants.ADD_MERCHANT_FAVORITE_SUCCESS:
                mPocketBar.setVisibility(View.GONE);
                favourite.setImageResource(R.drawable.ic_icon_fav);
                MerchantFavouriteManager.isCurrentMerchantFav = true;
                isFavorite = true;
                setFavouriteMerchantView(storeNametv);
                break;

            case Constants.GET_MERCHANT_FAVORITES:
                //    if (isFavorite.equals("1"))
                mPocketBar.setVisibility(View.GONE);
                ArrayList<String> mername = ModelManager.getInstance().getMerchantFavouriteManager().mernchantfavlist;
                try {
                    if (mername.contains(storeNametv.getText().toString())) {
                        favourite.setImageResource(R.drawable.ic_icon_fav);
                        isFavorite = true;
                    }
                } catch (Exception e) {
                }

                break;
            case Constants.REMOVE_MERCHANT_FAVORITES:

                mPocketBar.setVisibility(View.GONE);
                favourite.setBackgroundResource(R.drawable.ic_icon_fav_gray);
                MerchantFavouriteManager.isCurrentMerchantFav = false;
                isFavorite = false;
                setFavouriteMerchantView(storeNametv);
                break;

            case Constants.GET_RATING_SUCCESS:
                mPocketBar.setVisibility(View.GONE);
                List<RatingBean.RESULT_> ratingBeen = ModelManager.getInstance().getAddMerchantRating().ratingBean.getRESULT().get(0).getRESULT();
                ratingNo.setText("(" + ratingBeen.size() + ")");
                storereviews.setText("Current Reviews-" + ratingBeen.size());
                if (ratingBeen.size() == 0) {
                    ratingNo.setText("No Ratings");
                }
                ratingItemAdapter.cutomNotify(ratingBeen);
                ModelManager.getInstance().getMerchantManager().getMerchantDetail(this,
                        Operations.makeJsonGetMerchantDetail(this, merchantId), Constants.GET_MERCHANT_SUCCESS);

                break;

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.store_name:

                mPocketBar.setVisibility(View.VISIBLE);
                if (!isFavorite) {
                    ModelManager.getInstance().getAddMerchantFavorite().addMerchantToFavorite(mActivity, Operations.makeJsonMerchantAddToFavorite(mActivity, merchantId));
                } else
                    ModelManager.getInstance().getMerchantFavouriteManager().removeFavourite(mActivity,
                            Operations.makeJsonRemoveMerchantFavourite(mActivity, merchantId));

                break;
            case R.id.tv_back:
                finish();
                break;
            case R.id.messages:
                displayView(new FragmentMessages(), Constants.TAG_MESSAGEPAGE, new Bundle());
                break;
            case R.id.share:
                shareImage();
                break;
            case R.id.browse_store:
                Bundle bundle=new Bundle();
                bundle.putString(Constants.MERCHANT_ID,merchantId);
                ATPreferences.putBoolean(mActivity, Constants.HEADER_STORE, true);
                ATPreferences.putString(mActivity, Constants.MERCHANT_ID, merchantId);
                startActivity(new Intent(mActivity, HomeActivity.class).putExtras(bundle));
                finish();
                break;
            case R.id.ll_message:
                displayView(new FragmentMessages(), Constants.TAG_MESSAGEPAGE, new Bundle());
                break;
            case R.id.ll_scan:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.CAMERA},
                                MY_CAMERA_REQUEST_CODE);
                    } else {
                        //mTxtHeading.setText("Scan");
                        //mlogo.setVisibility(View.GONE);
                        //mTxtHeading.setVisibility(View.VISIBLE);
                        displayView(new FragmentScanner(), Constants.TAG_SCANNER, new Bundle());
                    }
                }

                break;
            case R.id.ll_search:
                //Utils.showSearchDialog(this, "RateMerchant");
                if (isGuest)
                    Utils.showGuestDialog(this);
                else {
                    displayView(new FragmentFavourite(), Constants.TAG_FAVOURITEPAGE, null);
                }
                break;
            case R.id.details_store:
                ATPreferences.putBoolean(mActivity, Constants.HEADER_STORE, true);
                ATPreferences.putString(mActivity, Constants.MERCHANT_ID, merchantId);
                startActivity(new Intent(mActivity, HomeActivity.class));
                break;
            case R.id.iv_back:

                if (frameLayout.getVisibility() == View.VISIBLE) {
                    frameLayout.setVisibility(View.GONE);
                    scroll_view.setVisibility(View.VISIBLE);
                } else
                    finish();

                break;
            case R.id.submit:
                if (isGuest) {
                    Utils.showGuestDialog(RateMerchant.this);
                } else {
                    String desc = comments.getText().toString();
                    String rating = RatingfromServer();
                    Log.d("RatingStr", rating + "");
                    mPocketBar.setVisibility(View.VISIBLE);
                    ModelManager.getInstance().getAddMerchantRating().addMerchantRating(RateMerchant.this,
                            Operations.AddMerchantRating(mActivity, ATPreferences.readString(this, Constants.KEY_USERID),
                                    merchantId, rating, desc));

                }
                break;
        }
    }

    public void displayView(Fragment fragment, String tag, Bundle bundle) {
        scroll_view.setVisibility(View.GONE);
        frameLayout.setVisibility(View.VISIBLE);
        //  if (fragment != null) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragB = fragmentManager.findFragmentByTag(tag);
        if (bundle != null)
            fragment.setArguments(bundle);
        //  if (fragB == null) {
        fragmentTransaction.replace(R.id.container_body, fragment);
        if (fragment instanceof FragmentAds || fragment instanceof FragmentSpecial || fragment instanceof FragmentItems) {

        } else
            fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.commit();
//            } else
//                fragmentTransaction.show(fragB);
        //  getSupportActionBar().setTitle(tag);
        // }
    }

//    private void shareImage() {
//        Intent shareIntent = new Intent();
//        shareIntent.setAction(Intent.ACTION_SEND);
//        shareIntent.putExtra(Intent.EXTRA_TEXT, "Nice item on ApiTap by \n" + storeName);
//        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
//        shareIntent.setType("image/*");
//        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//        startActivity(Intent.createChooser(shareIntent, "send"));
//    }
    private void shareImage() {
        Bitmap loadedImage = getBitmapFromURL((Constants.KEY_IMAGE_URL) + storeimage);
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), loadedImage, "", null);
        Uri screenshotUri = Uri.parse(path);
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Nice Merchant on ApiTap\n" + storeName
                +"\n"+ "http://aiodc.com");
        shareIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
        shareIntent.setType("image/*");
        //shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(shareIntent, "Share Via"));
    }

    // Returns the URI path to the Bitmap displayed in specified ImageView
    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (frameLayout.getVisibility() == View.VISIBLE) {
            frameLayout.setVisibility(View.GONE);
            scroll_view.setVisibility(View.VISIBLE);
        } else
            finish();
    }

    public Uri getLocalBitmapUri(ImageView imageView) {
        // Extract Bitmap from ImageView drawable
        Drawable drawable = imageView.getDrawable();
        Bitmap bmp = null;
        if (drawable instanceof BitmapDrawable) {
            bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        } else {
            return null;
        }
        // Store image to default external storage directory
        Uri bmpUri = null;
        try {
            File file = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS), "share_image_" + System.currentTimeMillis() + ".png");
            file.getParentFile().mkdirs();
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }

    @Override

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_CAMERA_REQUEST_CODE) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (toolint == 0) {

                    //  Toast.makeText(this, "Camera permission granted", Toast.LENGTH_LONG).show();
                    // mTxtHeading.setText("Scan");
                    //mlogo.setVisibility(View.GONE);
                    //mTxtHeading.setVisibility(View.VISIBLE);
                    displayView(new FragmentScanner(), Constants.TAG_SCANNER, new Bundle());

                } else {
                    displayView(new FragmentScanner(), Constants.TAG_SCANNER, null);
                }
            } else {

                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_LONG).show();

            }

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        startActivity(new Intent(this, HomeActivity.class).putExtra("Drawer", position));
    }
}

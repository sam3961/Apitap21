package com.apitap.views.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apitap.R;
import com.apitap.controller.MerchantFavouriteManager;
import com.apitap.controller.ModelManager;
import com.apitap.model.Constants;
import com.apitap.model.Operations;
import com.apitap.model.Utils;
import com.apitap.model.bean.RatingBean;
import com.apitap.model.customclasses.Event;
import com.apitap.model.preferences.ATPreferences;
import com.apitap.model.storeFrontItems.details.RESULTItem;
import com.apitap.model.storeFrontItems.details.StoreDetailsResponse;
import com.apitap.views.HomeActivity;
import com.apitap.views.NavigationMenu.FragmentDrawer;
import com.apitap.views.adapters.RatingItemAdapter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.google.android.material.tabs.TabLayout;
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
import java.util.Collections;
import java.util.List;

import static com.apitap.views.fragments.home.FragmentHome.setFavouriteMerchantView;

public class FragmentStoreRate extends BaseFragment implements View.OnClickListener {

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
    LinearLayout view_store_detail_header, view_store_tabs;
    LinearLayout search_tool;
    Button storeBrowse;
    String rating_number;
    float rating_prog;
    ScrollView scroll_view;
    LinearLayout browse_store;
    private TextView tabOne, tabTwo, tabThree, tabFour, tabFive, tabSix;
    private ImageView homeTab2;
    LinearLayout back_tool;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    boolean isFavorite = false;
    private View rootView;
    private boolean isFirstLoad = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_merchant_rating, container, false);
        mActivity = getActivity();
        arrayList = new ArrayList<Integer>(10);
        isGuest = ATPreferences.readBoolean(mActivity, Constants.GUEST);
        initViews();
        if (getArguments() != null) {
            merchantId = getArguments().getString("merchantId");
            storeimage = getArguments().getString("storeImage");
            storeName = getArguments().getString("storeName");
            rating_number = getArguments().getString("storeRateString");
//            if (storeName.length() > 15)
//                storeNametv.setText(storeName.substring(0,14) + "...");
//            else
            storeNametv.setText(storeName);
            //   storeNametv.setText(storeName);
            Picasso.get().load(ATPreferences.readString(mActivity, Constants.KEY_IMAGE_URL) + storeimage).into(storeImage);

        }
        merchantId = ATPreferences.readString(mActivity, Constants.MERCHANT_ID);


        return rootView;
    }

    private void initViews() {
        mRecycler = rootView.findViewById(R.id.mRecyclerRev);
        userRating = rootView.findViewById(R.id.myratingBar);
        storeImage = rootView.findViewById(R.id.store_img);
        storeNametv = rootView.findViewById(R.id.store_name);
        mComment = rootView.findViewById(R.id.comment);
        scroll_view = rootView.findViewById(R.id.scroll_view);
        tv_back = rootView.findViewById(R.id.tv_back);
        storereviews = rootView.findViewById(R.id.storereviews);
        submit = rootView.findViewById(R.id.submit);
        comments = rootView.findViewById(R.id.comment);
        storeBrowse = rootView.findViewById(R.id.details_store);
        currentRatingTv = rootView.findViewById(R.id.currentRating);
        mPocketBar = rootView.findViewById(R.id.pocket);
        inbox = rootView.findViewById(R.id.messages);
        favourite = rootView.findViewById(R.id.favourite);
        share = rootView.findViewById(R.id.share);
        ratingBar = rootView.findViewById(R.id.ratingBarcurrent);
        frameLayout = rootView.findViewById(R.id.container_body);
        ratingNo = rootView.findViewById(R.id.ratingNo);
        browse_store = rootView.findViewById(R.id.browse_store);
        frameLayout.setVisibility(View.GONE);

        scan_tool = rootView.findViewById(R.id.ll_scan);
        view_store_tabs = getActivity().findViewById(R.id.view_store_tabs);
        view_store_detail_header = getActivity().findViewById(R.id.view_store_detail_header);
        view_store_tabs.setVisibility(View.VISIBLE);
        view_store_detail_header.setVisibility(View.VISIBLE);

        msg_tool = rootView.findViewById(R.id.ll_message);
        search_tool = rootView.findViewById(R.id.ll_search);
        back_tool = rootView.findViewById(R.id.iv_back);

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


        //  ratingBeen = ModelManager.getInstance().getAddMerchantRating().ratingBean.getRESULT().get(0).getRESULT();

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
                // mActivity event is fired when rating is changed

                currentRatingTv.setText(String.valueOf(rating));
                UserRating = currentRatingTv.getText().toString();
                Log.d("RatingLogss", rating + "");
            }
        });


        setAverageRating();
        if (MerchantFavouriteManager.isCurrentMerchantFav)
            isFavorite = true;
        setFavouriteMerchantView(storeNametv);

        fetchRatingDetails();
    }

    private void setAverageRating() {
        LayerDrawable stars1 = (LayerDrawable) ratingBar.getProgressDrawable();
        data = ModelManager.getInstance().getMerchantStoresManager().storeDetailsModel.getRESULT().get(0);
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

    }


    private void fetchRatingDetails() {
        showProgress();
        ModelManager.getInstance().getAddMerchantRating().getMerchantRating(getActivity(),
                Operations.GetMerchantRating(mActivity, ATPreferences.readString(getActivity(), Constants.KEY_USERID),
                        ATPreferences.readString(mActivity, Constants.MERCHANT_ID)));

    }

    private void setAdapter() {
        Collections.reverse(ratingBeen);
        ratingItemAdapter = new RatingItemAdapter(mActivity, ratingBeen);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mActivity);
        mRecycler.setLayoutManager(mLayoutManager);
        mRecycler.setItemAnimator(new DefaultItemAnimator());
        mRecycler.addItemDecoration(new DividerItemDecoration(mActivity, DividerItemDecoration.VERTICAL));
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
                hideProgress();
                comments.setText("");
                //  Toast.makeText(mActivity, "Rating Success", Toast.LENGTH_SHORT).show();
                Utils.baseshowFeedbackMessage(mActivity, rootView, "Rating Success");
                isFirstLoad = true;
                fetchRatingDetails();
                break;

            case Constants.ADDRESS_NEARBY_SUCCESS:
                Utils.setRecyclerNearByAdapter(getActivity());
                break;
            case Constants.ADD_MERCHANT_FAVORITE_SUCCESS:
                hideProgress();
                favourite.setImageResource(R.drawable.ic_icon_fav);
                MerchantFavouriteManager.isCurrentMerchantFav = true;
                isFavorite = true;
                setFavouriteMerchantView(storeNametv);
                break;

            case Constants.STORE_FRONT_DETAILS:
                hideProgress();
                data = ModelManager.getInstance().getMerchantStoresManager().storeDetailsModel.getRESULT().get(0);
                setAverageRating();

                break;
            case Constants.GET_MERCHANT_FAVORITES:
                //    if (isFavorite.equals("1"))
                hideProgress();
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

                hideProgress();
                favourite.setBackgroundResource(R.drawable.ic_icon_fav_gray);
                MerchantFavouriteManager.isCurrentMerchantFav = false;
                isFavorite = false;
                setFavouriteMerchantView(storeNametv);
                break;

            case Constants.GET_RATING_SUCCESS:
                hideProgress();
                ratingBeen = ModelManager.getInstance().getAddMerchantRating().ratingBean.getRESULT().get(0).getRESULT();
                ratingNo.setText("(" + ratingBeen.size() + ")");
                storereviews.setText("Current Reviews-" + ratingBeen.size());
                if (ratingBeen.size() == 0) {
                    ratingNo.setText("No Ratings");
                }
                //ratingItemAdapter.cutomNotify(ratingBeen);
                setAdapter();

                if (isFirstLoad) {
                    isFirstLoad = false;
                    ModelManager.getInstance().getMerchantManager().getMerchantDetail(mActivity,
                            Operations.makeJsonGetMerchantDetail(mActivity, merchantId), Constants.GET_MERCHANT_SUCCESS);
                }

                break;
            case -1:
                hideProgress();
                baseshowFeedbackMessage(getActivity(), rootView, "Something went wrong");
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.store_name:
                showProgress();
                //mPocketBar.setVisibility(View.VISIBLE);
                if (!isFavorite) {
                    ModelManager.getInstance().getAddMerchantFavorite().addMerchantToFavorite(mActivity, Operations.makeJsonMerchantAddToFavorite(mActivity, merchantId));
                } else
                    ModelManager.getInstance().getMerchantFavouriteManager().removeFavourite(mActivity,
                            Operations.makeJsonRemoveMerchantFavourite(mActivity, merchantId));

                break;
            case R.id.tv_back:
                onBackPress();
                break;
            case R.id.share:
                shareImage();
                break;
            case R.id.browse_store:
                Bundle bundle = new Bundle();
                bundle.putString(Constants.MERCHANT_ID, merchantId);
                ATPreferences.putBoolean(mActivity, Constants.HEADER_STORE, true);
                ATPreferences.putString(mActivity, Constants.MERCHANT_ID, merchantId);
                startActivity(new Intent(mActivity, HomeActivity.class).putExtras(bundle));
                getActivity().finish();
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
                    onBackPress();

                break;
            case R.id.submit:
                if (isGuest) {
                    Utils.showGuestDialog(mActivity);
                } else {
                    String desc = Utils.convertStringToHex(comments.getText().toString());
                    String rating = RatingfromServer();
                    Log.d("RatingStr", rating + "");
                    //mPocketBar.setVisibility(View.VISIBLE);
                    showProgress();
                    ModelManager.getInstance().getAddMerchantRating().addMerchantRating(mActivity,
                            Operations.AddMerchantRating(mActivity, ATPreferences.readString(mActivity, Constants.KEY_USERID),
                                    merchantId, rating, desc));

                }
                break;
        }
    }

    private void shareImage() {
        // Bitmap loadedImage = getBitmapFromURL((Constants.KEY_IMAGE_URL) + storeimage);
        showProgress();
        Glide.with(this)
                .asBitmap()
                .load(ATPreferences.readString(getActivity(), Constants.KEY_IMAGE_URL) +
                        ATPreferences.readString(getActivity(), Constants.HEADER_IMG))
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        String path = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), resource, "", null);
                        Uri screenshotUri = Uri.parse(path);
                        Intent shareIntent = new Intent();
                        shareIntent.setAction(Intent.ACTION_SEND);
                        shareIntent.setType("image/*");
                        shareIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
                        shareIntent.putExtra(Intent.EXTRA_TEXT, "Nice Merchant on ApiTap\n" + storeName
                                + "\n" + "http://aiodc.com");
                        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        //shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        startActivity(Intent.createChooser(shareIntent, "Share Via"));
                        hideProgress();
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });


    }

    private void saveBitmapFile(Bitmap loadedImage) {
        try (FileOutputStream out = new FileOutputStream(getActivity().getCacheDir().getAbsolutePath() + storeimage + ".jpeg")) {
            loadedImage.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
            // PNG is a lossless format, the compression factor (100) is ignored
        } catch (IOException e) {
            e.printStackTrace();
        }
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


    private void fetchStoreDetails() {
        //   showProgress();
        ModelManager.getInstance().getMerchantStoresManager().getMerchantStoreDetails(getActivity(),
                Operations.makeJsonGetMerchantDetail(getActivity(), merchantId));
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isFirstLoad = true;
      //  view_store_detail_header.setVisibility(View.GONE);
       // view_store_tabs.setVisibility(View.GONE);
    }
}

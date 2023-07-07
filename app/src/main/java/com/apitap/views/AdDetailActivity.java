package com.apitap.views;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.apitap.views.fragments.favourite.FragmentFavourite;
import com.apitap.views.fragments.specials.FragmentSpecial;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.appcompat.widget.Toolbar;

import com.apitap.controller.AdsManager;
import com.apitap.model.bean.AdsIRbean;
import com.apitap.model.bean.RelatedAdBean;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultRenderersFactory;

import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.apitap.App;
import com.apitap.R;
import com.apitap.controller.ModelManager;
import com.apitap.model.AddTabBar;
import com.apitap.model.Constants;
import com.apitap.model.Operations;
import com.apitap.model.Utils;
import com.apitap.model.bean.AdsListBean;
import com.apitap.model.customclasses.CustomImageView;
import com.apitap.model.customclasses.Event;
import com.apitap.model.preferences.ATPreferences;
import com.apitap.views.NavigationMenu.FragmentDrawer;
import com.apitap.views.fragments.itemDetails.FragmentItemDetails;
import com.apitap.views.fragments.items.FragmentItems;
import com.apitap.views.fragments.messages.FragmentMessages;
import com.apitap.views.fragments.FragmentScanner;
import com.apitap.views.fragments.ads.FragmentAds;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackPreparer;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.decoder.DecoderCounters;
import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.MediaSourceEventListener;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.source.smoothstreaming.DefaultSsChunkSource;
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.FileDataSource;
import com.google.android.exoplayer2.upstream.cache.CacheDataSink;
import com.google.android.exoplayer2.upstream.cache.CacheDataSource;
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor;
import com.google.android.exoplayer2.upstream.cache.SimpleCache;
import com.google.android.exoplayer2.util.EventLogger;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.video.VideoRendererEventListener;
import com.linearlistview.LinearListView;
import com.squareup.picasso.Picasso;
import com.wang.avi.AVLoadingIndicatorView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import static com.apitap.App.isGuest;

/**
 * Created by sourcefuse on 12/12/16.
 */

public class AdDetailActivity extends AppCompatActivity implements PlaybackPreparer, VideoRendererEventListener, FragmentDrawer.FragmentDrawerListener, View.OnClickListener {

    long currentvidPosition = 0;
    private FragmentDrawer drawerFragment;
    private Toolbar mToolbar;
    LinearListView list;
    String imageUrl = "", videoUrl = "";
    LinearLayout llImages;
    ImageView inbox;
    TextView merchantName, Adname, description;
    CustomImageView ivImage;
    ImageView back_tv;
    LinearLayout linearheader;
    CardView cardDesc;
    LinearLayout backlinear;
    String adname, merchantname, desc, id, adId;
    Activity mActivity;
    CircularProgressView mPocketBar;
    Uri uri;
    String merchantid = "";
    ImageView backbtn;
    static int state = 0;
    ImageView ivFav, ivExpand;
    LinearLayout layoutFavorite;
    LinearLayout scan_tool;
    LinearLayout msg_tool;
    LinearLayout search_tool;
    static ScrollView viewMain;
    static FrameLayout frameLayout;
    private static int toolint = 0;
    String searchkey = "";
    int ad_position;
    TextView more_details;
    Button detailStore;
    TextView txt_related;
    CardView related_items;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private View view;
    public static TabLayout tabLayout;
    private TextView tabOne, tabTwo, tabThree, tabFour, tabFive, tabSix;
    private ImageView  homeTab2;
    LinearLayout tabConatiner;
    FrameLayout ff_back;
    TextView buffering;
    AVLoadingIndicatorView avLoadingIndicatorView;
    boolean isFavorite = false;
    boolean isBusiness = false;
    long lastSeekPostion = 0;
    private SimpleExoPlayer player;
    private static final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();
    private Handler mainHandler;
    private CacheDataSourceFactory mediaDataSourceFactory;
    private DefaultTrackSelector trackSelector;
    private String[] extensions;
    private EventLogger eventLogger;
    private PlayerView videoPlayerView;
    private boolean isPause = false;
    private Dialog mDialog;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inflate the layout for this fragment
        // view = inflater.inflate(R.layout.activity_ads_details, container, false);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        setContentView(R.layout.activity_ads_details);
        initViews();
        mActivity = this;


        Uri data = getIntent().getData();
        if (data != null && data.getPathSegments().size() >= 1) {
            List<String> params = data.getPathSegments();
            id = params.get(3);
            adId = params.get(2);
            merchantname = params.get(1);
            try {
                merchantName.setText(URLDecoder.decode(merchantname, "utf-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            ModelManager.getInstance().getAdsManager().adsRelatedItem(mActivity,
                    Operations.makeJsonGetAdsRelatedItemsOnly(mActivity, adId));
            ModelManager.getInstance().getFavouriteManager().getAdFavourites(mActivity,
                    Operations.makeJsonGetAdFavourites(mActivity));
            ModelManager.getInstance().getDetailsManager().getDetails(mActivity, Operations.makeJsonGetRelatedItems(mActivity,
                    Utils.getElevenDigitId(id)), true);
            ModelManager.getInstance().setProductSeen().setAdSeen(mActivity, Operations.makeJsonAdwatched(mActivity, Utils.getElevenDigitId(id)));

        }

        Intent i = mActivity.getIntent();

        if (i != null && i.hasExtra("merchant")) {
            merchantname = i.getExtras().getString("merchant");
            adname = i.getExtras().getString("adName");
            desc = i.getExtras().getString("desc");
            videoUrl = i.getExtras().getString("videoUrl");
            id = i.getExtras().getString("id");
            adId = i.getExtras().getString("ad_id");
            ad_position = i.getExtras().getInt("adpos");
            merchantid = i.getExtras().getString("merchantid");
            imageUrl = i.getExtras().getString("image");
            Log.d("adposition", id + "");
            merchantName.setText((merchantname));
            ModelManager.getInstance().getAdsManager().adsRelatedItem(mActivity,
                    Operations.makeJsonGetAdsRelatedItemsOnly(mActivity, adId));
            ModelManager.getInstance().getFavouriteManager().getAdFavourites(mActivity,
                    Operations.makeJsonGetAdFavourites(mActivity));
            ModelManager.getInstance().getDetailsManager().getDetails(mActivity, Operations.makeJsonGetRelatedItems(mActivity, Utils.getElevenDigitId(id)), true);
            ModelManager.getInstance().setProductSeen().setAdSeen(mActivity, Operations.makeJsonAdwatched(mActivity, Utils.getElevenDigitId(id)));
        }


    }

    public void setAdapter() {
        String business_ads = ATPreferences.readString(mActivity, Constants.BUSINESS_ADS);

        ArrayList<AdsIRbean> array2 = new ArrayList<>();
        //MyAdapter myAdapter = null;
        AdsListBean.RESULT array = null;
        if (business_ads.equals("yes")) {
            isBusiness = true;
            array2 = ModelManager.getInstance().getAdsManager().AdsarrayIR;
            try {
                if (array2.size() == 0)
                    txt_related.setVisibility(View.GONE);
            } catch (IndexOutOfBoundsException e) {

            }
        } else {
            isBusiness = false;
            array = ModelManager.getInstance().getAdsManager().adsListBean.getRESULT().get(0);
            if (array.getRESULT().get(ad_position).getiR().size() == 0)
                txt_related.setVisibility(View.GONE);
        }

        if (isBusiness) {
            assert array2 != null;
//            Log.d("sizearray1  ", array2.getRESULT().size() + " p ");
//            Log.d("sizearray2  ", array2.getRESULT().get(ad_position).getAD().size() + " o");
            //  if (array2.size() > 0)
            //    myAdapter = new MyAdapter(array2, "");
        } else {
            //  myAdapter = new MyAdapter(array.getRESULT().get(ad_position).getiR());
        }

        //  list.setAdapter(myAdapter);


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

    private void initViews() {


        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        mainHandler = new Handler();
        //  mediaDataSourceFactory = CacheDataSourceFactory(true);
        avLoadingIndicatorView = (AVLoadingIndicatorView) findViewById(R.id.anim);
        detailStore = (Button) findViewById(R.id.details_store);
        tabConatiner = (LinearLayout) findViewById(R.id.tab_container);
        txt_related = (TextView) findViewById(R.id.txt_related);
        buffering = (TextView) findViewById(R.id.buffering);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        scan_tool = (LinearLayout) findViewById(R.id.ll_scan);
        viewMain = (ScrollView) findViewById(R.id.scroll_view);
        frameLayout = (FrameLayout) findViewById(R.id.container_body);
        msg_tool = (LinearLayout) findViewById(R.id.ll_message);
        search_tool = (LinearLayout) findViewById(R.id.ll_search);
        videoPlayerView = (PlayerView) findViewById(R.id.video_view);
        list = (LinearListView) findViewById(R.id.list);
        llImages = (LinearLayout) findViewById(R.id.ll_images);
        ff_back = (FrameLayout) findViewById(R.id.backff);

        inbox = (ImageView) findViewById(R.id.iv_inbox);

        ivImage = (CustomImageView) findViewById(R.id.iv_ad);
        merchantName = (TextView) findViewById(R.id.merchant);
        more_details = (TextView) findViewById(R.id.more_Details);
        Adname = (TextView) findViewById(R.id.adName);
        description = (TextView) findViewById(R.id.product_desc);
        back_tv = (ImageView) findViewById(R.id.ic_back);
        layoutFavorite = (LinearLayout) findViewById(R.id.layout_favorite);
        ivFav = (ImageView) findViewById(R.id.iv_fav);
        ivExpand = (ImageView) findViewById(R.id.iv_expand);
        mPocketBar = (CircularProgressView) findViewById(R.id.pocket);
        cardDesc = (CardView) findViewById(R.id.descriptions);
        homeTab2 = findViewById(R.id.tab_one_image);

        playerReadyIntialization();

        backlinear = (LinearLayout) findViewById(R.id.back);
        detailStore.setOnClickListener(this);
        layoutFavorite.setOnClickListener(this);
        ivExpand.setOnClickListener(this);
        ivFav.setOnClickListener(this);
        scan_tool.setOnClickListener(this);
        msg_tool.setOnClickListener(this);
        search_tool.setOnClickListener(this);
        more_details.setOnClickListener(this);
        // merchantName.setOnClickListener(this);
        back_tv.setOnClickListener(this);

        AddTabBar.getmInstance().setupViewPager(tabLayout);
        AddTabBar.getmInstance().setupTabIcons(tabLayout, AdDetailActivity.this, tabOne, tabTwo,
                tabThree, tabFour, tabFive, tabSix, homeTab2);
        AddTabBar.getmInstance().bindWidgetsWithAnEvent(tabConatiner, tabLayout, AdDetailActivity.this, R.id.container_body);

        findViewById(R.id.iv_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ExecuteApi executeApi = new ExecuteApi();
                executeApi.execute(imageUrl);

            }
        });
        inbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent obji = new Intent(getApplicationContext(), MessageDetailActivity.class);
                obji.putExtra("adID", Utils.getElevenDigitId(adId));
                obji.putExtra("merchantName", merchantName.getText().toString());
                obji.putExtra("adName", adname);
                obji.putExtra("merchantId", merchantid);
                obji.putExtra("image", imageUrl);
                obji.putExtra("videoUrl", videoUrl);
                startActivity(obji);
//                viewMain.setVisibility(View.GONE);
//                frameLayout.setVisibility(View.VISIBLE);
//                displayView(new FragmentMessages(), Constants.TAG_MESSAGEPAGE, new Bundle());
            }
        });
    }

    private void playerReadyIntialization() {

        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

// 2. Create a default LoadControl
        LoadControl loadControl = new DefaultLoadControl();

        DrmSessionManager<FrameworkMediaCrypto> drmSessionManager = null;

        @DefaultRenderersFactory.ExtensionRendererMode int extensionRendererMode =
                App.getInstance().useExtensionRenderers()
                        ? (true ? DefaultRenderersFactory.EXTENSION_RENDERER_MODE_PREFER
                        : DefaultRenderersFactory.EXTENSION_RENDERER_MODE_ON)
                        : DefaultRenderersFactory.EXTENSION_RENDERER_MODE_OFF;
        DefaultRenderersFactory renderersFactory = new DefaultRenderersFactory(this,
                drmSessionManager, extensionRendererMode);

// 3. Create the player
        //    player = ExoPlayerFactory.newSimpleInstance(this, trackSelector, loadControl);
        player = ExoPlayerFactory.newSimpleInstance(renderersFactory, trackSelector, loadControl);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (frameLayout.getVisibility() == View.VISIBLE) {
            viewMain.setVisibility(View.VISIBLE);
            frameLayout.setVisibility(View.GONE);
            if (lastSeekPostion != 0) {
                currentvidPosition = lastSeekPostion;
                initializePlayer();
            }
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

    @Subscribe
    public void onEvent(Event event) {
        switch (event.getKey()) {

            case Constants.ADD_Ad_TO_FAVORITE_SUCCESS:

                mPocketBar.setVisibility(View.GONE);
                if (!isFavorite) {
                    isFavorite = true;
                    ivFav.setBackgroundResource(R.drawable.ic_icon_fav);
                } else {
                    isFavorite = false;
                    ivFav.setBackgroundResource(R.drawable.ic_icon_fav_gray);

                }
                break;
            case Constants.ADDRESS_NEARBY_SUCCESS:
                Utils.setRecyclerNearByAdapter(getApplicationContext());
                break;
            case Constants.RELATED_ADITEM_SUCCESS:
                if (AdsManager.relatedAdBean.getRESULT().get(0).getRESULT().size() > 0) {
                    desc = AdsManager.relatedAdBean.getRESULT().get(0).getRESULT().get(0).get120157();
                    imageUrl = ATPreferences.readString(this, Constants.KEY_IMAGE_URL) + AdsManager.relatedAdBean.getRESULT().get(0).getRESULT().get(0).get121170();
                    videoUrl = AdsManager.relatedAdBean.getRESULT().get(0).getRESULT().get(0).get12115();
                    merchantname = AdsManager.relatedAdBean.getRESULT().get(0).getRESULT().get(0).get53();
                }
                Adname.setText(adname);
                description.setText(desc);

                // videoUrl = mActivity.getIntent().getExtras().getString("videoUrl");
                if (mActivity.getIntent().hasExtra("vidpos")) {
                    currentvidPosition = (Long) mActivity.getIntent().getExtras().getLong("vidpos");
                }


                if (!videoUrl.equals("")) {
                    ivImage.setVisibility(View.GONE);
                    videoPlayerView.setVisibility(View.VISIBLE);
                    initializePlayer();
                    //setAdapter();

                } else {
                    try {
                        ivImage.setBackgroundColor(getResources().getColor(R.color.colorBlack));
                        Glide.with(this).load(imageUrl)
                                .placeholder(R.drawable.loading_no_border).diskCacheStrategy(DiskCacheStrategy.ALL).into(ivImage);
                        uri = getLocalBitmapUri(ivImage);
                        videoPlayerView.setVisibility(View.GONE);
                        ivImage.setVisibility(View.VISIBLE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                if (AdsManager.relatedAdBean!=null&&AdsManager.relatedAdBean.getRESULT().size()>0&&
                        AdsManager.relatedAdBean.getRESULT().get(0).getRESULT().get(0).getIR() != null && AdsManager.relatedAdBean.getRESULT().get(0).getRESULT().get(0).getIR().size() > 0) {
                    txt_related.setVisibility(View.VISIBLE);
                    MyAdapter myAdapter = new MyAdapter(AdsManager.relatedAdBean.getRESULT().get(0).getRESULT().get(0).getIR());
                    list.setAdapter(myAdapter);
                } else
                    txt_related.setVisibility(View.GONE);

                break;

            case Constants.GET_Ad_FAVOURITE_SUCCESS:
                final ArrayList<String> favdetailsbeanArrayList = ModelManager.getInstance().getFavouriteManager().favIds;

                if (favdetailsbeanArrayList.contains(id)) {
                    isFavorite = true;
                    ivFav.setBackgroundResource(R.drawable.ic_icon_fav);
                }
                break;

            /*case Constants.RELATED_DETAILS:
                relatedArray = ModelManager.getInstance().getDetailsManager().relatedDetailsBean.getResult().get(0).getResult();
                if (relatedArray != null && relatedArray.size() > 0) {
                    listRelatedItems.setAdapter(mAdapter);
                    txtRelatedItems.setVisibility(View.VISIBLE);
                } else
                    layRelatedItems.setVisibility(View.GONE);
                break;*/

        }
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
        if (!videoUrl.isEmpty()) {
            player.setVolume(0f);
            player.release();
        }
    }

    private void shareImage(Bitmap loadedImage) {
        // Bitmap loadedImage = getBitmapFromURL(imageUrl);

        String path = MediaStore.Images.Media.insertImage(getContentResolver(), loadedImage, "", null);
        Uri screenshotUri = Uri.parse(path);
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        try {
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Nice item on ApiTap\n" + merchantName.getText().toString() + "\n" + description.getText().toString()
                    + "\n" + "Open in the ApiTap" + "\n" + "http://adaiodc.com/ad/" + URLEncoder.encode(merchantName.getText().toString(), "utf-8") + "/" + adId + "/" + id);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
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

    @SuppressLint("StaticFieldLeak")
    private class ExecuteApi extends AsyncTask<String, String, Bitmap> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mPocketBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Bitmap doInBackground(String... param) {
            try {
                URL url = new URL(param[0]);
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
        protected void onPostExecute(Bitmap s) {
            super.onPostExecute(s);
            mPocketBar.setVisibility(View.GONE);
            if (s != null)
                shareImage(s);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.iv_fav:
                mPocketBar.setVisibility(View.VISIBLE);
                if (!isFavorite)
                    ModelManager.getInstance().getAddToFavoriteManager().adToFavorite(mActivity, Operations.makeJsonAdToFavorite(mActivity, Utils.getElevenDigitId(id)));
                else
                    ModelManager.getInstance().getAddToFavoriteManager().adToFavorite(mActivity, Operations.makeJsonAdToFavorite(mActivity, Utils.getElevenDigitId(id)));

                break;

            case R.id.iv_expand:
                expnadView();
                break;
            case R.id.ic_back:
                if (frameLayout.getVisibility() == View.VISIBLE) {
                    viewMain.setVisibility(View.VISIBLE);
                    frameLayout.setVisibility(View.GONE);
                } else
                    finish();
                break;
            case R.id.merchant:
                ATPreferences.putBoolean(mActivity, Constants.HEADER_STORE, true);
                ATPreferences.putString(mActivity, Constants.MERCHANT_ID, merchantid);
                startActivity(new Intent(mActivity, HomeActivity.class));
                break;

            case R.id.details_store:
                ATPreferences.putBoolean(mActivity, Constants.HEADER_STORE, true);
                ATPreferences.putString(mActivity, Constants.MERCHANT_ID, merchantid);
                startActivity(new Intent(mActivity, HomeActivity.class));
                break;
            case R.id.more_Details:
                Intent intent = new Intent(mActivity, MerchantStoreDetails.class);
                intent.putExtra("merchantId", merchantid);
                startActivity(intent);
                break;
            case R.id.ll_message:
                displayView(new FragmentMessages(), Constants.TAG_MESSAGEPAGE, new Bundle());
                break;
            case R.id.ll_scan:
                if (mActivity.checkSelfPermission(Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA},
                            MY_CAMERA_REQUEST_CODE);
                } else {
                    //mTxtHeading.setText("Scan");
                    //mlogo.setVisibility(View.GONE);
                    //mTxtHeading.setVisibility(View.VISIBLE);
                    displayView(new FragmentScanner(), Constants.TAG_SCANNER, new Bundle());
                }

                break;
            case R.id.ll_search:
                //Utils.showSearchDialog(this, "AdDetail");
                if (isGuest)
                    Utils.showGuestDialog(this);
                else {
                    displayView(new FragmentFavourite(), Constants.TAG_FAVOURITEPAGE, null);
                }
                break;
        }
    }

    public void showSearchDialog() {
        final ArrayList<String> list = ModelManager.getInstance().getSearchManager().listAddresses;
        final Dialog dialog = new Dialog(mActivity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.setContentView(R.layout.quick_search_test);

        Button submit = (Button) dialog.findViewById(R.id.submit);
        Button cancel = (Button) dialog.findViewById(R.id.cancel);
        final EditText search = (EditText) dialog.findViewById(R.id.search);
        Spinner spinner = (Spinner) dialog.findViewById(R.id.spinner);
        ArrayAdapter arrayAdapter = new ArrayAdapter(mActivity, R.layout.spinner_item, list);
        spinner.setAdapter(arrayAdapter);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchkey = search.getText().toString();
                startActivity(new Intent(mActivity, SearchItemActivity.class).putExtra("key", searchkey));
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        dialog.show();
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        //getDialogView(dialog);
        //viewsVisibility(dialog);

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

                Toast.makeText(mActivity, "Camera permission denied", Toast.LENGTH_LONG).show();

            }

        }
    }

    public void displayView(Fragment fragment, String tag, Bundle bundle) {
        //  if (fragment != null) {
//        if (!videoUrl.isEmpty()) {
//            player.release();
//        }
        frameLayout.setVisibility(View.VISIBLE);
        viewMain.setVisibility(View.GONE);
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


    public void expnadView() {

        if (!videoUrl.isEmpty()) {
            currentvidPosition = Math.max(0, player.getContentPosition());
            player.release();

        }
        Intent intent = new Intent(mActivity, FullScreenImage.class);
        Bundle extras = new Bundle();
        extras.putString("imagebitmap", imageUrl);
        extras.putString("video", videoUrl);
        extras.putString("previousClass", "AdDetail");
        extras.putString("merchant", merchantName.getText().toString());
        extras.putString("adName", adname);
        extras.putString("desc", desc);
        extras.putString("id", id);
        extras.putString("ad_id", adId);
        extras.putInt("adpos", ad_position);
        extras.putLong("vidpos", currentvidPosition);
        intent.putExtras(extras);
        startActivity(intent);
        if (!videoUrl.isEmpty())
            finish();

     /*   if (state==0){
            state=1;
            Resources r = getResources();
            float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 450, r.getDisplayMetrics());
            ivImage.requestLayout();

            ivImage.getLayoutParams().height = (int) px;

            cardDesc.setVisibility(View.GONE);
            linearheader.setVisibility(View.GONE);

        }else if (state==1){
            state=0;
            Resources r = getResources();
            float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300, r.getDisplayMetrics());
            ivImage.requestLayout();

            ivImage.getLayoutParams().height = (int) px;

            cardDesc.setVisibility(View.VISIBLE);
            linearheader.setVisibility(View.VISIBLE);
        }*/
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {

        startActivity(new Intent(this, HomeActivity.class).putExtra("Drawer", position));
    }


    class MyAdapter extends BaseAdapter {
        LayoutInflater inflater;

        // List<AdsListBean.RESULT.AdsData.IR> array;
        List<RelatedAdBean.IR> array2;

        public MyAdapter(List<RelatedAdBean.IR> array) {
            inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.array2 = array;
        }


        @Override
        public int getCount() {
            return array2.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        class ViewHolder {
            TextView mCategoryName;
            LinearListView mTwoWayView;
            //  MyTwoWayAdapter adapter;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.row_horizontal_test, parent, false);
            ImageView imageView = (ImageView) convertView.findViewById(R.id.image);
            ImageView eye = (ImageView) convertView.findViewById(R.id.eye);
            LinearLayout rlSinglePrice = (LinearLayout) convertView.findViewById(R.id.rel_single_price);
            LinearLayout rlTwoPrice = (LinearLayout) convertView.findViewById(R.id.rl_two_price);
            TextView price = (TextView) convertView.findViewById(R.id.price);
            TextView actualPrice = (TextView) convertView.findViewById(R.id.actual_price);
            TextView priceAfterDiscount = (TextView) convertView.findViewById(R.id.price_after_discount);
            TextView description = (TextView) convertView.findViewById(R.id.description);

            String isSeen = "";
            String ActualPrice;
            String DiscountPrice;

            isSeen = array2.get(position).get1149();
            description.setText(array2.get(position).get12083());
            //Glide.with(AdDetailActivity.this).load(ATPreferences.readString(mActivity, Constants.KEY_IMAGE_URL) + array2.get(position).get121170()).diskCacheStrategy(DiskCacheStrategy.SOURCE).placeholder(R.drawable.splash_screen_new).into(imageView);
            Picasso.get().load(ATPreferences.readString(mActivity, Constants.KEY_IMAGE_URL) + "_t_" + array2.get(position).get121170()).into(imageView);


            if (array2.get(position).get114112().equalsIgnoreCase("21")) {
                ActualPrice = array2.get(position).get11498();
                DiscountPrice = array2.get(position).get122158();
                if (Double.parseDouble(ActualPrice) == Double.parseDouble(DiscountPrice) || DiscountPrice.equals("0") || DiscountPrice.equals("0.00") || Double.parseDouble(DiscountPrice) > Double.parseDouble(ActualPrice)) {
                    rlSinglePrice.setVisibility(View.VISIBLE);
                    rlTwoPrice.setVisibility(View.GONE);
                    actualPrice.setText("$" + ActualPrice);
                } else if (ActualPrice.equals("0") || ActualPrice.equals("0.00")) {
                    rlTwoPrice.setVisibility(View.VISIBLE);
                    rlSinglePrice.setVisibility(View.GONE);
                    priceAfterDiscount.setText("$" + DiscountPrice);
                    actualPrice.setVisibility(View.GONE);
                } else if (Double.parseDouble(ActualPrice) > Double.parseDouble(DiscountPrice)) {
                    rlTwoPrice.setVisibility(View.VISIBLE);
                    rlSinglePrice.setVisibility(View.VISIBLE);
                    priceAfterDiscount.setText("$" + (String.format("%.2f", Double.parseDouble(DiscountPrice))));
                    priceAfterDiscount.setGravity(Gravity.END);
                    actualPrice.setGravity(Gravity.START);
                    actualPrice.setText("$" + ActualPrice + "   ");
                    actualPrice.setTextColor(getResources().getColor(R.color.colorRed));
                    actualPrice.setPaintFlags(actualPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                }
            } else {
                rlSinglePrice.setVisibility(View.VISIBLE);
                rlTwoPrice.setVisibility(View.GONE);
                actualPrice.setText(array2.get(position).get_122_162());
            }

            if (isSeen.equalsIgnoreCase("false")) {
                eye.setBackgroundResource(R.drawable.green_seen);
            } else {
                eye.setVisibility(View.GONE);
            }


            convertView.setTag(holder);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String productType = "";
                    String productId = "";
                    productId = Utils.lengtT(11, array2.get(position).get114144());
                    productType = array2.get(position).get114112();

                    ModelManager.getInstance().setProductSeen().setProductSeen(getApplicationContext(), Operations.makeProductSeen(AdDetailActivity.this, productId));

                    Bundle bundle = new Bundle();
                    bundle.putString("productId", productId);
                    bundle.putString("productType", productType);
                    bundle.putString("flag", "adDetail");
                    FragmentItemDetails fragment = new FragmentItemDetails();
                    fragment.setArguments(bundle);
                    FragmentManager fm = getSupportFragmentManager();
                    for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                        fm.popBackStack();
                    }
                    if (!videoUrl.isEmpty()) {
                        lastSeekPostion = Math.max(0, player.getContentPosition());
                        player.stop();
                    }

                    displayView(fragment, Constants.TAG_DETAILSPAGE, bundle);
                }
            });

            return convertView;
        }
    }


    public static void viewFrame() {
        viewMain.setVisibility(View.VISIBLE);
        frameLayout.setVisibility(View.GONE);
    }


    void stopAnim() {
        avLoadingIndicatorView.setVisibility(View.GONE);
        avLoadingIndicatorView.smoothToHide();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isPause) {
            playerReadyIntialization();
            initializePlayer();
        }
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    protected void onPause() {
        super.onPause();
        isPause = true;
        if (!videoUrl.isEmpty())
            player.release();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }


    public void initializePlayer() {

        final Uri videoUri = Uri.parse(ATPreferences.readString(mActivity, Constants.KEY_VIDEO_URL) + videoUrl);
        //  final Uri videoUri = Uri.parse("http://djjohalhd.video/get/17016/1080/Sauda%20Khara%20Khara%20(Good%20Newwz)%20(DJJOhAL.Com).mp4");
        TrackSelection.Factory adaptiveTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(BANDWIDTH_METER);
        trackSelector = new DefaultTrackSelector(adaptiveTrackSelectionFactory);
        eventLogger = new EventLogger(trackSelector);
        extensions = new String[1];
        //  com.google.android.exoplayer2.source.MediaSource[] mediaSources = new com.google.android.exoplayer2.source.MediaSource[1];
        //   mediaSources[i] = buildMediaSource(uris[i], extensions[i], mainHandler, eventLogger);

        MediaSource mediaSources = new ExtractorMediaSource(videoUri,
                new CacheDataSourceFactory(mActivity, 100 * 1024 * 1024, 5 * 1024 * 1024), new DefaultExtractorsFactory(), null, null);

        //mediaSources[0] = buildMediaSource(videoUri, extensions[0], mainHandler, eventLogger);

        //   mediaSources[i] = buildMediaSource(uri, extensions[i], mainHandler, eventLogger);

        Log.d("lengths", extensions.length + "");
/*
        com.google.android.exoplayer2.source.MediaSource mediaSource = mediaSources.length == 1 ? mediaSources[0]
                : new ConcatenatingMediaSource(mediaSources);
*/


        videoPlayerView.setUseController(true);
        videoPlayerView.setPlaybackPreparer(this);
        videoPlayerView.setPlayer(player);
        player.addListener(new PlayerEventListener());
        player.addListener(eventLogger);
        player.setRepeatMode(Player.REPEAT_MODE_ALL);
        player.setPlayWhenReady(true); //run file/link when ready to play.
        player.addVideoDebugListener(this);

        player.prepare(mediaSources, false, false);
        if (currentvidPosition != 0) {
            player.seekTo(currentvidPosition);
        }
    }

    @Override
    public void preparePlayback() {
        initializePlayer();
    }

    @Override
    public void onVideoEnabled(DecoderCounters counters) {

    }

    @Override
    public void onVideoDecoderInitialized(String decoderName, long initializedTimestampMs, long initializationDurationMs) {

    }

    @Override
    public void onVideoInputFormatChanged(Format format) {

    }

    @Override
    public void onDroppedFrames(int count, long elapsedMs) {

    }

    @Override
    public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {

    }

    @Override
    public void onRenderedFirstFrame(Surface surface) {

    }

    @Override
    public void onVideoDisabled(DecoderCounters counters) {

    }

    private DataSource.Factory buildDataSourceFactory(boolean useBandwidthMeter) {
        return App.getInstance()
                .buildDataSourceFactory(useBandwidthMeter ? BANDWIDTH_METER : null);
    }

    private com.google.android.exoplayer2.source.MediaSource buildMediaSource(
            Uri uri,
            String overrideExtension,
            @Nullable Handler handler,
            @Nullable MediaSourceEventListener listener) {
        @C.ContentType int type = TextUtils.isEmpty(overrideExtension) ? Util.inferContentType(uri)
                : Util.inferContentType("." + overrideExtension);
        switch (type) {
            case C.TYPE_DASH:
                return new DashMediaSource.Factory(
                        new DefaultDashChunkSource.Factory(mediaDataSourceFactory),
                        buildDataSourceFactory(false))
                        .createMediaSource(uri, handler, listener);
            case C.TYPE_SS:
                return new SsMediaSource.Factory(
                        new DefaultSsChunkSource.Factory(mediaDataSourceFactory),
                        buildDataSourceFactory(false))
                        .createMediaSource(uri, handler, listener);
            case C.TYPE_HLS:
                return new HlsMediaSource.Factory(mediaDataSourceFactory)
                        .createMediaSource(uri, handler, listener);
            case C.TYPE_OTHER:
                return new ExtractorMediaSource.Factory(mediaDataSourceFactory)
                        .createMediaSource(uri, handler, listener);
            default: {
                throw new IllegalStateException("Unsupported type: " + type);
            }
        }
    }

    private class PlayerEventListener extends Player.DefaultEventListener {

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            if (playbackState == Player.STATE_ENDED) {

            }
            if (playbackState == Player.STATE_BUFFERING) {
                showProgress();
            } else
                hideProgress();

            //updateButtonVisibilities();
        }

        @Override
        public void onPositionDiscontinuity(@Player.DiscontinuityReason int reason) {
        }

        @Override
        public void onPlayerError(ExoPlaybackException e) {
            String errorString = null;

        }

        @Override
        @SuppressWarnings("ReferenceEquality")
        public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {


        }

    }

    public void showProgress() {
        mDialog = new Dialog(this);
        // no tile for the dialog
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.prograss_bar_dialog);
        ProgressBar mProgressBar = mDialog.findViewById(R.id.progress_bar);
        TextView textView = mDialog.findViewById(R.id.progress_text);
        textView.setText("Buffering...");
        //  mProgressBar.getIndeterminateDrawable().setColorFilter(context.getResources()
        // .getColor(R.color.material_blue_gray_500), PorterDuff.Mode.SRC_IN);
        mProgressBar.setVisibility(View.VISIBLE);
        // you can change or add this line according to your need
        mProgressBar.setIndeterminate(true);
        mDialog.setCancelable(false);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();
    }

    public void hideProgress() {
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
    }

    class CacheDataSourceFactory implements DataSource.Factory {
        private final Context context;
        private final DefaultDataSourceFactory defaultDatasourceFactory;
        private final long maxFileSize, maxCacheSize;

        CacheDataSourceFactory(Context context, long maxCacheSize, long maxFileSize) {
            super();
            this.context = context;
            this.maxCacheSize = maxCacheSize;
            this.maxFileSize = maxFileSize;
            String userAgent = Util.getUserAgent(context, context.getString(R.string.app_name));
            DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            defaultDatasourceFactory = new DefaultDataSourceFactory(this.context,
                    bandwidthMeter,
                    new DefaultHttpDataSourceFactory(userAgent, bandwidthMeter));
        }

        @Override
        public DataSource createDataSource() {
            LeastRecentlyUsedCacheEvictor evictor = new LeastRecentlyUsedCacheEvictor(maxCacheSize);
            SimpleCache simpleCache = new SimpleCache(new File(context.getCacheDir(), "media"), evictor);
            return new CacheDataSource(simpleCache, defaultDatasourceFactory.createDataSource(),
                    new FileDataSource(), new CacheDataSink(simpleCache, maxFileSize),
                    CacheDataSource.FLAG_BLOCK_ON_CACHE | CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR, null);
        }
    }
}

package com.apitap.views.fragments.adDetails;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.apitap.App;
import com.apitap.R;
import com.apitap.controller.AdsManager;
import com.apitap.controller.ModelManager;
import com.apitap.model.Constants;
import com.apitap.model.Operations;
import com.apitap.model.Utils;
import com.apitap.model.bean.AdsIRbean;
import com.apitap.model.bean.AdsListBean;
import com.apitap.model.bean.RelatedAdBean;
import com.apitap.model.customclasses.CustomImageView;
import com.apitap.model.customclasses.Event;
import com.apitap.model.preferences.ATPreferences;
import com.apitap.views.HomeActivity;
import com.apitap.views.NavigationMenu.FragmentDrawer;
import com.apitap.views.fragments.BaseFragment;
import com.apitap.views.fragments.FragmentFullScreenImage;
import com.apitap.views.fragments.SendMessage;
import com.apitap.views.fragments.itemDetails.FragmentItemDetails;
import com.apitap.views.fragments.FragmentStoreDetails;
import com.apitap.views.fragments.messageDetails.FragmentMessageDetail;
import com.apitap.views.fragments.storefront.FragmentStoreFront;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
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
import com.google.android.material.tabs.TabLayout;
import com.linearlistview.LinearListView;
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

/**
 * Created by sourcefuse on 12/12/16.
 */

public class FragmentAdDetail extends BaseFragment implements PlaybackPreparer, VideoRendererEventListener,
        View.OnClickListener {

    long currentvidPosition = 0;
    private FragmentDrawer drawerFragment;
    private Toolbar mToolbar;
    LinearListView list;
    String imageUrl = "", videoUrl = "";
    LinearLayout llImages;
    ImageView inbox, imageViewZoomOut, imageViewZoomIn;
    TextView merchantName, Adname;
    TextView description;
    CustomImageView ivImage;
    ImageView back_tv;
    boolean isAdDetailResponse;
    boolean isMessagedClicked;
    CardView cardDesc;
    LinearLayout backlinear;
    LinearLayout linearLayoutZoomIn, linearLayoutZoomOut;
    String adname, merchantname, desc, id, adId;
    Activity mActivity;
    CircularProgressView mPocketBar;
    Uri uri;
    String merchantid = "";
    ImageView backbtn;
    static int state = 0;
    private float fontsize = 14;
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
    public TabLayout tabLayout;
    private TextView tabOne, tabTwo, tabThree, tabFour, tabFive, tabSix;
    private ImageView homeTab2;
    LinearLayout tabConatiner;
    FrameLayout ff_back;
    TextView buffering;
    AVLoadingIndicatorView avLoadingIndicatorView;
    boolean isFavorite = false;
    private LinearLayout linearLayoutHeaderStoreFront;
    private RelativeLayout relativeLayoutSearchBarStoreFront;
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
    private View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_ad_detail, container, false);
        mActivity = getActivity();
        initViews();


        Uri data = getActivity().getIntent().getData();
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
            showProgress();
            ModelManager.getInstance().getAdsManager().adsRelatedItem(mActivity,
                    Operations.makeJsonGetAdsRelatedItemsOnly(mActivity, adId));


            ModelManager.getInstance().getFavouriteManager().getAdFavourites(mActivity,

                    Operations.makeJsonGetAdFavourites(mActivity));
            ModelManager.getInstance().getDetailsManager().getDetails(mActivity, Operations.makeJsonGetRelatedItems(mActivity,
                    Utils.getElevenDigitId(id)), true);
            ModelManager.getInstance().setProductSeen().setAdSeen(mActivity, Operations.makeJsonAdwatched(mActivity, Utils.getElevenDigitId(id)));

        }

        Bundle i = getArguments();

        if (i != null && i.getString("merchant") != null) {
            merchantname = i.getString("merchant");
            adname = i.getString("adName");
            desc = i.getString("desc");
            videoUrl = i.getString("videoUrl");
            //videoUrl = "https://videos-usw22.streaming.media.azure.net/65527d24-ecbf-44fb-a05d-3eb985623610/2012-12-03%2007.43.28.ism/manifest(format=mpd-time-csf)";
            id = i.getString("id");
            adId = i.getString("ad_id");
            ad_position = i.getInt("adpos");
            merchantid = i.getString("merchantid");
            imageUrl = i.getString("image");
            Log.d("adposition", id + "");
            merchantName.setText((merchantname));

            showProgress();
            ModelManager.getInstance().getAdsManager().adsRelatedItem(mActivity,
                    Operations.makeJsonGetAdsRelatedItemsOnly(mActivity, adId));
            ModelManager.getInstance().getFavouriteManager().getAdFavourites(mActivity,
                    Operations.makeJsonGetAdFavourites(mActivity));
            //    ModelManager.getInstance().getDetailsManager().getDetails(mActivity, Operations.makeJsonGetRelatedItems(mActivity, Utils.getElevenDigitId(id)), true);
            ModelManager.getInstance().setProductSeen().setAdSeen(mActivity, Operations.makeJsonAdwatched(mActivity, Utils.getElevenDigitId(id)));


        }

        return rootView;

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


    private void initViews() {


        mainHandler = new Handler();
        relativeLayoutSearchBarStoreFront = getActivity().findViewById(R.id.search_storefront);
        linearLayoutHeaderStoreFront = getActivity().findViewById(R.id.header);
        avLoadingIndicatorView = rootView.findViewById(R.id.anim);
        detailStore = rootView.findViewById(R.id.details_store);
        tabConatiner = rootView.findViewById(R.id.tab_container);
        txt_related = rootView.findViewById(R.id.txt_related);
        buffering = rootView.findViewById(R.id.buffering);
        tabLayout = getActivity().findViewById(R.id.tabs);
        scan_tool = rootView.findViewById(R.id.ll_scan);
        viewMain = rootView.findViewById(R.id.scroll_view);
        frameLayout = rootView.findViewById(R.id.container_body);
        msg_tool = rootView.findViewById(R.id.ll_message);
        search_tool = rootView.findViewById(R.id.ll_search);
        videoPlayerView = rootView.findViewById(R.id.video_view);
        list = rootView.findViewById(R.id.list);
        llImages = rootView.findViewById(R.id.ll_images);
        ff_back = rootView.findViewById(R.id.backff);

        inbox = rootView.findViewById(R.id.iv_inbox);

        ivImage = rootView.findViewById(R.id.iv_ad);
        merchantName = rootView.findViewById(R.id.merchant);
        more_details = rootView.findViewById(R.id.more_Details);
        Adname = rootView.findViewById(R.id.adName);
        description = rootView.findViewById(R.id.product_desc);
        imageViewZoomIn = rootView.findViewById(R.id.imageViewZoomIn);
        imageViewZoomOut = rootView.findViewById(R.id.imageViewZoomOut);
        linearLayoutZoomIn = rootView.findViewById(R.id.linearLayoutZoomIn);
        linearLayoutZoomOut = rootView.findViewById(R.id.linearLayoutZoomOut);
        back_tv = rootView.findViewById(R.id.ic_back);
        layoutFavorite = rootView.findViewById(R.id.layout_favorite);
        ivFav = rootView.findViewById(R.id.iv_fav);
        ivExpand = rootView.findViewById(R.id.iv_expand);
        mPocketBar = rootView.findViewById(R.id.pocket);
        cardDesc = rootView.findViewById(R.id.descriptions);
        homeTab2 = rootView.findViewById(R.id.tab_one_image);
        backlinear = rootView.findViewById(R.id.back);

        playerReadyIntialization();

        linearLayoutZoomOut.setOnClickListener(this);
        linearLayoutZoomIn.setOnClickListener(this);
        imageViewZoomOut.setOnClickListener(this);
        imageViewZoomIn.setOnClickListener(this);
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
        rootView.findViewById(R.id.iv_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ExecuteApi executeApi = new ExecuteApi();
                executeApi.execute(imageUrl);

            }
        });
        inbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAdDetailResponse) {
                    navigateToSendMessage();
                } else {
                    showProgress();
                    isMessagedClicked = true;
                }
//                ((HomeActivity) getActivity()).displayView(new FragmentMessageDetail(),
//                        Constants.MessageDetailPage, bundle);

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
        DefaultRenderersFactory renderersFactory = new DefaultRenderersFactory(mActivity,
                drmSessionManager, extensionRendererMode);

// 3. Create the player
        //    player = ExoPlayerFactory.newSimpleInstance(this, trackSelector, loadControl);
        player = ExoPlayerFactory.newSimpleInstance(renderersFactory, trackSelector, loadControl);

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

            case Constants.AD_DETAIL_SUCCESS:
                isAdDetailResponse = true;
                hideProgress();
                // navigateToSendMessage();
                break;

            case Constants.ADD_Ad_TO_FAVORITE_SUCCESS:


                hideProgress();
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
                Utils.setRecyclerNearByAdapter(mActivity);
                break;
            case Constants.RELATED_ADITEM_SUCCESS:
                hideProgress();
                if (AdsManager.relatedAdBean.getRESULT().get(0).getRESULT().size() > 0) {
                    desc = Utils.hexToASCII(AdsManager.relatedAdBean.getRESULT().get(0).getRESULT().get(0).get120157());
                    imageUrl = ATPreferences.readString(mActivity, Constants.KEY_IMAGE_URL) + AdsManager.relatedAdBean.getRESULT().get(0).getRESULT().get(0).get121170();
                    videoUrl = AdsManager.relatedAdBean.getRESULT().get(0).getRESULT().get(0).get12115();
                    merchantname = AdsManager.relatedAdBean.getRESULT().get(0).getRESULT().get(0).get53();
                }
                Adname.setText(adname);
                description.setText(desc);

                // videoUrl = mActivity.getIntent().getExtras().getString("videoUrl");
                if (mActivity.getIntent().hasExtra("vidpos")) {
                    currentvidPosition = mActivity.getIntent().getExtras().getLong("vidpos");
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

                        //uri = getLocalBitmapUri(ivImage);

                        videoPlayerView.setVisibility(View.GONE);
                        ivImage.setVisibility(View.VISIBLE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                if (AdsManager.relatedAdBean != null && AdsManager.relatedAdBean.getRESULT().size() > 0 &&
                        AdsManager.relatedAdBean.getRESULT().get(0).getRESULT().get(0).getIR() != null && AdsManager.relatedAdBean.getRESULT().get(0).getRESULT().get(0).getIR().size() > 0) {
                    txt_related.setVisibility(View.VISIBLE);
                    MyAdapter myAdapter = new MyAdapter(AdsManager.relatedAdBean.getRESULT().get(0).getRESULT().get(0).getIR());
                    list.setAdapter(myAdapter);
                } else
                    txt_related.setVisibility(View.GONE);

                ModelManager.getInstance().getAdsManager().adDetail(mActivity,
                        Operations.makeJsonGetAdDetailById(mActivity, id));
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

    private void navigateToSendMessage() {
        Bundle bundle = new Bundle();
        bundle.putString("adID", Utils.getElevenDigitId(adId));
        bundle.putString("productId", Utils.getElevenDigitId(adId));
        bundle.putString("merchantName", merchantName.getText().toString());
        bundle.putString("storeName", merchantName.getText().toString());
        bundle.putString("adName", adname);
        bundle.putString("merchantId", merchantid);
        bundle.putString("image", imageUrl);
        bundle.putString("videoUrl", videoUrl);
        bundle.putStringArrayList("locationList", ModelManager.getInstance().getAdsManager().arrayListLocation);
        bundle.putStringArrayList("locationIdList", ModelManager.getInstance().getAdsManager().arrayListLocationId);
        ((HomeActivity) getActivity()).displayView(new SendMessage(), "Send Message", bundle);

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

        String path =
                MediaStore.Images.Media.insertImage(mActivity.getContentResolver(),
                        loadedImage, "", null);
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

            case R.id.imageViewZoomIn:
            case R.id.linearLayoutZoomIn:
                if (fontsize < 20) {
                    fontsize++;
                    description.setTextSize(fontsize);
                }
                break;
            case R.id.imageViewZoomOut:
            case R.id.linearLayoutZoomOut:
                if (fontsize > 14) {
                    fontsize--;
                    description.setTextSize(fontsize);
                }
                break;

            case R.id.iv_fav:
                showProgress();
                if (!isFavorite)
                    ModelManager.getInstance().getAddToFavoriteManager().adToFavorite(mActivity, Operations.makeJsonAdToFavorite(mActivity, Utils.getElevenDigitId(id)));
                else
                    ModelManager.getInstance().getAddToFavoriteManager().adToFavorite(mActivity, Operations.makeJsonAdToFavorite(mActivity, Utils.getElevenDigitId(id)));

                break;

            case R.id.iv_expand:
                expnadView();
                break;
            case R.id.ic_back:
                onBackPress();
                break;
            case R.id.merchant:
            case R.id.details_store:
                Bundle b = new Bundle();
                b.putBoolean(Constants.HEADER_STORE, true);
                b.putString(Constants.MERCHANT_ID, merchantid);

                ATPreferences.putBoolean(getActivity(), Constants.HEADER_STORE, true);
                ATPreferences.putString(getActivity(), Constants.MERCHANT_ID, merchantid);
                ATPreferences.putString(getActivity(), Constants.MERCHANT_CATEGORY, "");

                ((HomeActivity) getActivity()).displayView(new FragmentStoreFront(), Constants.TAG_STORESFRONTPAGE, b);


                break;
            case R.id.more_Details:

                linearLayoutHeaderStoreFront.setVisibility(View.GONE);
                relativeLayoutSearchBarStoreFront.setVisibility(View.GONE);
                Bundle bundle1 = new Bundle();
                bundle1.putString("merchantId", merchantid);
                ((HomeActivity) getActivity()).displayView(new FragmentStoreDetails(), Constants.TAG_STORE_DETAILS, bundle1);

                break;


        }
    }


    public void expnadView() {

        if (!videoUrl.isEmpty()) {
            currentvidPosition = Math.max(0, player.getContentPosition());
            player.release();

        }
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
        ((HomeActivity) getActivity()).displayView(new FragmentFullScreenImage(), Constants.TAG_FULL_SCREEN, extras);

        if (!videoUrl.isEmpty())
            onBackPress();

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
            // Picasso.get().load(ATPreferences.readString(mActivity, Constants.KEY_IMAGE_URL) + "_t_" + array2.get(position).get121170()).into(imageView);

            Glide.with(mActivity).load(ATPreferences.readString(mActivity, Constants.KEY_IMAGE_URL)
                            + "_t_" + array2.get(position).get121170())
                    .placeholder(R.drawable.ic_gallery_placeholder)
                    .error(R.drawable.no_photo_placeholder)
                    .fitCenter().centerInside()
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).into(imageView);


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

            imageView.setOnClickListener(view -> {
                String productType = "";
                String productId = "";
                productId = Utils.lengtT(11, array2.get(position).get114144());
                productType = array2.get(position).get114112();

                ModelManager.getInstance().setProductSeen().setProductSeen(mActivity, Operations.makeProductSeen(mActivity, productId));

                Bundle bundle = new Bundle();
                bundle.putString("productId", productId);
                bundle.putString("productType", productType);
                bundle.putString("flag", "adDetail");
                FragmentItemDetails fragment = new FragmentItemDetails();
                if (!videoUrl.isEmpty()) {
                    lastSeekPostion = Math.max(0, player.getContentPosition());
                    player.stop();
                }

                ((HomeActivity) getActivity()).displayView(fragment, Constants.TAG_DETAILSPAGE, bundle);
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
    public void onResume() {
        super.onResume();
        if (isPause && !videoUrl.isEmpty()) {
            player.setPlayWhenReady(true);
        }
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    public void onPause() {
        super.onPause();
        isPause = true;
        if (!videoUrl.isEmpty())
            player.setPlayWhenReady(false);
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

    private void releasePlayer() {
        if (player != null) {
            player.stop();
            player.release();
            player = null;
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        releasePlayer();
        if (getArguments().containsKey(Constants.TAB_SELECTED)) {
            TabLayout.Tab tab = tabLayout.getTabAt(1);
            ((HomeActivity) getActivity()).removeTabListener();
            tab.select();
            ((HomeActivity) getActivity()).addTabListener();
        }
    }
}

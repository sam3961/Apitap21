package com.apitap.views.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.apitap.App;
import com.apitap.R;
import com.apitap.model.Constants;
import com.apitap.model.Utils;
import com.apitap.model.bean.ProductDetailsBean;
import com.apitap.model.customclasses.Event;
import com.apitap.model.preferences.ATPreferences;
import com.apitap.views.HomeActivity;
import com.apitap.views.fragments.adDetails.FragmentAdDetail;
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
import com.jsibbold.zoomage.ZoomageView;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Shami on 11/10/2017.
 */

public class FragmentFullScreenImage extends BaseFragment implements PlaybackPreparer, VideoRendererEventListener,
        View.OnClickListener {


    @SuppressLint("NewApi")

    ZoomageView imgDisplay;
    Button btnClose;
    FrameLayout collapse;
    ImageView collapse_img;
    LinearLayout collapse_white;
    LinearLayout ll_menu;
    RelativeLayout main_rel;
    String bmp, video = "", merchant, adName, previousClass, desc, id, adId, merchantid;
    int adpos;
    long currentvidPosition = 0;
    int height = 0, width = 0;
    CircularProgressView circleIndicator;
    private static int state = 0;

    private SimpleExoPlayer player;
    private static final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();
    private Handler mainHandler;
    private CacheDataSourceFactory mediaDataSourceFactory;
    private DefaultTrackSelector trackSelector;
    private String[] extensions;
    private EventLogger eventLogger;
    private PlayerView videoPlayerView;

    LinearLayout scan_tool;
    LinearLayout msg_tool;
    LinearLayout search_tool;
    RelativeLayout viewMain;
    FrameLayout frameLayout;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    ArrayList<ProductDetailsBean> detailsArray;
    int img_selcted_postion;
    ImageView back_img, next_img;
    int preview_state = 0;
    boolean hasListImages = false;
    private Dialog mDialog;
    private View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_full_screen, container, false);
        

        imgDisplay = rootView.findViewById(R.id.imgDisplay);
        circleIndicator = rootView.findViewById(R.id.pocket);
        collapse = rootView.findViewById(R.id.collapse);
        collapse_img = rootView.findViewById(R.id.collapse_img);
        collapse_white = rootView.findViewById(R.id.collapse_white);
        ll_menu = rootView.findViewById(R.id.ll_menu);
        btnClose = rootView.findViewById(R.id.btnClose);
        videoPlayerView = rootView.findViewById(R.id.video_view);
        viewMain = rootView.findViewById(R.id.linear);
        frameLayout = rootView.findViewById(R.id.container_body);
        scan_tool = rootView.findViewById(R.id.ll_scan);
        msg_tool = rootView.findViewById(R.id.ll_message);
        search_tool = rootView.findViewById(R.id.ll_search);
        back_img = rootView.findViewById(R.id.back_arrow);
        next_img = rootView.findViewById(R.id.next_arrow);

        ll_menu.setVisibility(View.GONE);
        msg_tool.setOnClickListener(this);
        search_tool.setOnClickListener(this);
        scan_tool.setOnClickListener(this);
        back_img.setOnClickListener(this);
        next_img.setOnClickListener(this);


        Bundle extras = getArguments();
        bmp = extras.getString("imagebitmap");
        video = extras.getString("video");
        merchant = extras.getString("merchant");
        adName = extras.getString("adName");
        desc = extras.getString("desc");
        id = extras.getString("id");
        adId = extras.getString("ad_id");
        adpos = extras.getInt("adpos");
        previousClass = extras.getString("previousClass");
        merchantid = extras.getString("merchantid");
        currentvidPosition = extras.getLong("vidpos");
        if (extras.containsKey("detailsArray")) {
            detailsArray = (ArrayList<ProductDetailsBean>) getArguments().getSerializable("detailsArray");
            img_selcted_postion = extras.getInt("img_position");
            if (detailsArray != null && detailsArray.size() > 0) {
                back_img.setVisibility(View.VISIBLE);
                next_img.setVisibility(View.VISIBLE);
                hasListImages = true;
                preview_state = img_selcted_postion;
                Picasso.get().load(ATPreferences.readString(getActivity(), Constants.KEY_IMAGE_URL) +
                        detailsArray.get(img_selcted_postion).getProductImage()).fit().centerInside().into(imgDisplay);


                try {
                    URL url = new URL(ATPreferences.readString(getActivity(), Constants.KEY_IMAGE_URL) +
                            detailsArray.get(img_selcted_postion).getProductImage());
                    Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    Log.d("Widthss", bmp.getWidth() + " " + bmp.getHeight());
//                    if (bmp.getHeight() >= bmp.getWidth()) {
//                        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//                    } else {
//                        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        mainHandler = new Handler();

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
        DefaultRenderersFactory renderersFactory = new DefaultRenderersFactory(getActivity(),
                drmSessionManager, extensionRendererMode);

// 3. Create the player
        //    player = ExoPlayerFactory.newSimpleInstance(this, trackSelector, loadControl);
        player = ExoPlayerFactory.newSimpleInstance(renderersFactory, trackSelector, loadControl);

        if (!video.equals("")) {
            if (!previousClass.equals("ProductDetail"))
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            imgDisplay.setVisibility(View.GONE);
            collapse.setVisibility(View.GONE);
            collapse_white.setVisibility(View.VISIBLE);
            videoPlayerView.setVisibility(View.VISIBLE);
            initializePlayer();

        } else {
            circleIndicator.setVisibility(View.VISIBLE);
            imgDisplay.setVisibility(View.VISIBLE);
            Log.d("imgsheight", imgDisplay.getHeight() + "  " + imgDisplay.getWidth());

            if (!hasListImages) {
                if (bmp.endsWith("gif") || bmp.endsWith("GIF"))
                    Glide.with(this).load(bmp).placeholder(R.drawable.loading).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                            .into(imgDisplay);
                else
                    Picasso.get().load(bmp).fit().centerInside().into(imgDisplay);
            }

        }

        collapse_white.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                collapse_img.performClick();
            }
        });


        collapse_img.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (previousClass.equalsIgnoreCase("ProductDetail")) {
                    onBackPress();
                    return;
                }
                if (!video.equals("")) {
                    currentvidPosition = Math.max(0, player.getContentPosition());
                    player.release();


                    Bundle bundle = new Bundle();
                    bundle.putString("videoUrl",video);
                    bundle.putString("image",bmp);
                    bundle.putString("merchant",merchant);
                    bundle.putString("adName",adName);
                    bundle.putString("desc",desc);
                    bundle.putString("id",id);
                    bundle.putString("ad_id",adId);
                    bundle.putString("merchantid","");
                    bundle.putInt("adpos",adpos);
                    bundle.putLong("vidpos",currentvidPosition);

                    ((HomeActivity) getActivity()).displayView(new FragmentAdDetail(), Constants.TAG_AD_DETAIL, bundle);

                } else {
                    state = 0;
                    onBackPress();

                }
            }
        });

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }


    @Override
    public void onResume() {
        super.onResume();
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Subscribe
    public void onEvent(Event event) {
        switch (event.getKey()) {
            case Constants.ADDRESS_NEARBY_SUCCESS:
                Utils.setRecyclerNearByAdapter(getActivity());
                break;
        }
    }



    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
        if (!video.isEmpty()) {
            player.setVolume(0f);
            player.release();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (!video.isEmpty())
            player.release();
    }


    public void initializePlayer() {
        Uri videoUri = Uri.parse(ATPreferences.readString(getActivity(), Constants.KEY_VIDEO_URL) + video);
     //   final Uri videoUri = Uri.parse("http://djjohalhd.video/get/17016/1080/Sauda%20Khara%20Khara%20(Good%20Newwz)%20(DJJOhAL.Com).mp4");

        TrackSelection.Factory adaptiveTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(BANDWIDTH_METER);
        trackSelector = new DefaultTrackSelector(adaptiveTrackSelectionFactory);
        eventLogger = new EventLogger(trackSelector);
        extensions = new String[1];
      //  com.google.android.exoplayer2.source.MediaSource[] mediaSources = new com.google.android.exoplayer2.source.MediaSource[1];
        //   mediaSources[i] = buildMediaSource(uris[i], extensions[i], mainHandler, eventLogger);

        MediaSource mediaSources = new ExtractorMediaSource(videoUri,
                new CacheDataSourceFactory(getActivity(),
                        100 * 1024 * 1024, 5 * 1024 * 1024), new DefaultExtractorsFactory(), null, null);


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
        player.seekTo(currentvidPosition);
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

    private MediaSource buildMediaSource(
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

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.back_arrow:
                if (preview_state == 0) {
                    preview_state = detailsArray.size() - 1;
                } else {
                    preview_state = preview_state - 1;
                }
                Picasso.get().load(ATPreferences.readString(getActivity(),
                        Constants.KEY_IMAGE_URL) + detailsArray.get(preview_state).getProductImage()).fit().centerInside().placeholder(R.drawable.loading).into(imgDisplay);

                break;
            case R.id.next_arrow:
                if (preview_state == detailsArray.size() - 1) {
                    preview_state = 0;
                } else {
                    preview_state = preview_state + 1;
                }
                Picasso.get().load(ATPreferences.readString(getActivity(), Constants.KEY_IMAGE_URL) + detailsArray.get(preview_state).getProductImage()).fit().centerInside().placeholder(R.drawable.loading).into(imgDisplay);

                break;


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


package com.apitap.views.adapters;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apitap.App;
import com.apitap.R;
import com.apitap.controller.ModelManager;
import com.apitap.model.Constants;
import com.apitap.model.Utils;
import com.apitap.model.bean.AdsBean;
import com.apitap.model.bean.AdsDetailWithMerchant;
import com.apitap.model.customclasses.CustomImageView;
import com.apitap.views.HomeActivity;
import com.apitap.views.fragments.adDetails.FragmentAdDetail;
import com.apitap.views.fragments.ads.FragmentAds;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.PlaybackPreparer;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.decoder.DecoderCounters;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.source.smoothstreaming.DefaultSsChunkSource;
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.video.VideoRendererEventListener;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;


public class SamplePagerAdapter extends PagerAdapter implements PlaybackPreparer, VideoRendererEventListener {

    private final Random random = new Random();
    private int mSize;
    ArrayList<String> business_list = new ArrayList<>();
    private Context context;
    private HashMap<Integer, AdsBean> ads;
    private LayoutInflater inflater;
    private boolean doNotifyDataSetChangedOnce = false;
    private boolean visible_header = true;
    private String isFav;
    private String selected_sort = "no";
    PlayerEventListener playerEventListener;
    private ArrayList<AdsDetailWithMerchant> adsDetailWithMerchants;
    private boolean isFirstVideoPlayed = false;

    //ExoPlayerViews
    private String[] extensions;
    /* private Handler mainHandler;
     private DataSource.Factory mediaDataSourceFactory;
     private DefaultTrackSelector trackSelector;
     public SimpleExoPlayer player;
     private static final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();

     private EventLogger eventLogger;
     private PlayerView videoPlayerView;*/
    private String videoUrl = "";

    long lastSeekPostion = 0;
    private SimpleExoPlayer player;
    private static final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();
    private Handler mainHandler;
    private DataSource.Factory mediaDataSourceFactory;
    private DefaultTrackSelector trackSelector;
    private EventListener eventLogger;
    private PlayerView videoPlayerView;
    private CircularProgressView progressBar;


    public SamplePagerAdapter() {
    }

    public SamplePagerAdapter(Context context, HashMap<Integer, AdsBean> ads, ArrayList<AdsDetailWithMerchant>
            adsDetailWithMerchants, boolean visible_header, String isFav) {
        this.ads = ads;
        this.adsDetailWithMerchants = adsDetailWithMerchants;
        this.context = context;
        this.visible_header = visible_header;
        this.isFav = isFav;
        inflater = LayoutInflater.from(context);


    }

    public SamplePagerAdapter(Context context, HashMap<Integer, AdsBean> ads, ArrayList<AdsDetailWithMerchant> adsDetailWithMerchants, boolean visible_header, String isFav, String selected_Sort) {
        this.ads = ads;
        this.adsDetailWithMerchants = adsDetailWithMerchants;
        this.context = context;
        this.visible_header = visible_header;
        this.isFav = isFav;
        this.selected_sort = selected_Sort;
        inflater = LayoutInflater.from(context);


    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (player != null) {
            player.setVolume(0f);
            // player.stop();
            //player.release();
            // player.removeListener(playerEventListener);
        }
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        if (doNotifyDataSetChangedOnce) {
            doNotifyDataSetChangedOnce = false;
            notifyDataSetChanged();
        }
        return ads.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View image = inflater.inflate(R.layout.row_customize_image, container, false);
        assert image != null;
        final CustomImageView imageView = image.findViewById(R.id.image);
        final ImageView storeImage = (ImageView) image.findViewById(R.id.adstoreImg);
        final TextView storeName = (TextView) image.findViewById(R.id.storeName);
        final TextView storeTitle = (TextView) image.findViewById(R.id.storeTitle);
        final ImageView eye = (ImageView) image.findViewById(R.id.seen);
        final ImageView play = (ImageView) image.findViewById(R.id.play);
        final TextView showAll = (TextView) image.findViewById(R.id.showallads);
        final TextView textViewStoreName = (TextView) image.findViewById(R.id.textViewStoreName);
        final TextView textViewAdTitle = (TextView) image.findViewById(R.id.textViewAdTitle);
        final LinearLayout mainLayout = (LinearLayout) image.findViewById(R.id.mainlayout);
        progressBar = image.findViewById(R.id.progressBar);
        videoPlayerView = (PlayerView) image.findViewById(R.id.video_view);
        //videoPlayerView = (VideoView) image.findViewById(R.id.video_view);

        final FrameLayout frameLayout = (FrameLayout) image.findViewById(R.id.frame_layout);
        playerEventListener = new PlayerEventListener();
        //ExoPlayer

        //    mainHandler = new Handler();
        //  mediaDataSourceFactory = buildDataSourceFactory(true);

        //initializeMethod();
        final TextView storeDetails = (TextView) image.findViewById(R.id.details_store);
        final LinearLayout header = (LinearLayout) image.findViewById(R.id.header);
        //   Glide.with(context).load(ads.get(position).getImageUrl()).placeholder(R.drawable.ic_icon_loading).into(imageView);
        if (visible_header)
            //  header.setVisibility(View.VISIBLE);
            storeName.setText(Utils.hexToASCII(ads.get(position).getMerchantName()));

//        if (isFav.equals("fav"))
//            storeTitle.setText(ads.get(position).getMerchantName());

        textViewStoreName.setText(Utils.hexToASCII(ads.get(position).getMerchantName()));
        if (isFav.equals("fav") || isFav.equals("search"))
            textViewAdTitle.setText(Utils.hexToASCII(ads.get(position).getAdName()));
        else
            textViewAdTitle.setText(Utils.hexToASCII(ads.get(position).getAdName()));
        //storeName.setText(adsDetailWithMerchants.get(position).getImageUrl());

//        Glide.with(context)
//                .load(ads.get(position).getImageUrl())
//                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                .preload(500, 500);
        if (ads.get(position).getImageUrl().endsWith("gif"))
            Glide.with(context)
                    .load(ads.get(position).getImageUrl())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.ic_icon_loading)
                    .dontTransform()
                    .into(imageView);
        else
            Glide.with(context)
                    .load(ads.get(position).getImageUrl())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.ic_icon_loading)
                    .dontTransform()
                    .into(imageView);
        //Log.d("AdsUrls1", ads.get(position).getImageUrl() + " vid " + ads.get(position).getVideoUrl());


//        Glide.with(context)
//                .load(ATPreferences.readString(context, Constants.KEY_IMAGE_URL) + ads.get(position).getMerchantLogo())
//                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                .placeholder(R.drawable.loading)
//                .into(storeImage);

        String isSeen = ads.get(position).getSeen();
        //Log.d("isSeenss", ads.get(position).getSeen() + "");
        if (isSeen.equalsIgnoreCase("false")) {
            eye.setBackgroundResource(R.drawable.green_seen);
        } else {
            eye.setVisibility(View.GONE);
        }

        if (Objects.requireNonNull(ads.get(position)).getVideoUrl().contains("mp4") ||
                Objects.requireNonNull(ads.get(position)).getVideoUrl().contains("avi") ||
                ads.get(position).getVideoUrl().contains("mkv")) {
            play.setVisibility(View.VISIBLE);
        } else
            play.setVisibility(View.GONE);

/*        if (ads.get(position).getVideoUrl().contains("mp4") || ads.get(position).getVideoUrl().contains("avi") ||
                ads.get(position).getVideoUrl().contains("mkv")) {
            if (player!=null) {
                player.setVolume(0f);
                player.stop();
                player.release();
                player.removeListener(playerEventListener);
            }
            //videoUrl = ads.get(position).getVideoUrl();
             videoUrl = "http://d1.funnymp4.net/files/sfd8/3972/Anushka%20Sharma%20Talk%20With%20Reporters%20Mom%20-%20YouTube%20Viral%20Video(funnymp4.net).mp4";
            //playVideo();
            mainHandler = new Handler();
            mediaDataSourceFactory = buildDataSourceFactory(true);
            playerReadyIntialization();
            initializePlayer();
            Uri uri = Uri.parse(ATPreferences.readString(context, Constants.KEY_VIDEO_URL) +videoUrl);
            imageView.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            videoPlayerView.setVisibility(View.VISIBLE);
           // videoPlayerView.start();

//
//            if (!isFirstVideoPlayed)
//                isFirstVideoPlayed = true;
//            else
            //player.release();
            //initializePlayer();
            // play.setVisibility(View.VISIBLE);
        }*/// else {
        videoUrl = "";
        //  videoPlayerView.setVisibility(View.GONE);
        imageView.setVisibility(View.VISIBLE);
/*
            if (player!=null) {
                player.setVolume(0f);
                player.stop();
                player.release();
                player.removeListener(playerEventListener);
            }
*/
        //  player.release();
        // }

        showAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((HomeActivity) context).displayView(new FragmentAds(), "Ads", new Bundle());
            }
        });

        if (!selected_sort.equals("no")) {
            business_list = ModelManager.getInstance().getFavouriteManager().business_typeList;
        }
        if (selected_sort.equals("All")) {
            mainLayout.setVisibility(View.VISIBLE);
        } else {
            if (!selected_sort.equals("no") && business_list.contains(selected_sort)) {
                mainLayout.setVisibility(View.VISIBLE);
            } else {
                if (!selected_sort.equals("no")) {
                    mainLayout.setVisibility(View.GONE);
                }
            }
        }
/*
        storeDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, MerchantStoreDetails.class)
                        .putExtra("merchantId", adsDetailWithMerchants.get(position).getMerchantId()));
            }
        });
*/
        mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageView.performClick();
            }
        });
/*
        videoPlayerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                imageView.performClick();
                return false;
            }
        });
*/

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!videoUrl.isEmpty()) {
                    if (player != null) {
                        player.setVolume(0f);
                        player.stop();
                        player.release();
                        player.removeListener(playerEventListener);
                    }
                }
                String merchantName = "";
                String AdName = "";
                String description = "";
                String id = "";
                String Adid = "";
                String Merchantid = "";
                if (isFav.equals("home")) {
                    merchantName = ads.get(position).getMerchantName();
                    description = Utils.hexToASCII(ads.get(position).getDescription());
                    id = ads.get(position).getId();
                    Adid = ads.get(position).getAdId();
                    AdName = Utils.hexToASCII(ads.get(position).getAdName());
                    Merchantid = ads.get(position).getMerchantId();
                } else if (isFav.equals("fav") || isFav.equals("search")) {
                    merchantName = adsDetailWithMerchants.get(position).getMerchantname();
                    description = adsDetailWithMerchants.get(position).getDesc();
                    id = adsDetailWithMerchants.get(position).getId();
                    Adid = adsDetailWithMerchants.get(position).getAdId();
                    AdName = adsDetailWithMerchants.get(position).getName();
                    Merchantid = adsDetailWithMerchants.get(position).getMerchantId();
                } else {
                    merchantName = Utils.hexToASCII(adsDetailWithMerchants.get(position).getMerchantname());
                    description = adsDetailWithMerchants.get(position).getDesc();
                    id = adsDetailWithMerchants.get(position).getId();
                    Adid = adsDetailWithMerchants.get(position).getAdId();
                    AdName = adsDetailWithMerchants.get(position).getName();
                    Merchantid = adsDetailWithMerchants.get(position).getMerchantId();
                }

                Bundle bundle = new Bundle();
                bundle.putString("videoUrl",ads.get(position).getVideoUrl());
                bundle.putString("image",ads.get(position).getImageUrl());
                bundle.putString("merchant",merchantName);
                bundle.putString("adName",AdName);
                bundle.putString("desc",description);
                bundle.putString("id",id);
                bundle.putString("ad_id",Adid);
                bundle.putString("merchantid","");
                bundle.putInt("adpos",position);
                ((HomeActivity) context).displayView(new FragmentAdDetail(), Constants.TAG_AD_DETAIL, bundle);

            }
        });

        container.addView(image, 0);
        return image;
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

/*
    private DataSource.Factory buildDataSourceFactory(boolean useBandwidthMeter) {
        return App.getInstance()
                .buildDataSourceFactory(useBandwidthMeter ? BANDWIDTH_METER : null);
    }
*/

/*
    public void initializePlayer() {

        // releasePlayer();
        final Uri videoUri = Uri.parse(ATPreferences.readString(context, Constants.KEY_VIDEO_URL) + videoUrl);
        TrackSelection.Factory adaptiveTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(BANDWIDTH_METER);
        trackSelector = new DefaultTrackSelector(adaptiveTrackSelectionFactory);
        eventLogger = new EventLogger(trackSelector);
        extensions = new String[1];
        com.google.android.exoplayer2.source.MediaSource[] mediaSources = new com.google.android.exoplayer2.source.MediaSource[1];
        //   mediaSources[i] = buildMediaSource(uris[i], extensions[i], mainHandler, eventLogger);


        mediaSources[0] = buildMediaSource(videoUri, extensions[0], mainHandler, eventLogger);

        //   mediaSources[i] = buildMediaSource(uri, extensions[i], mainHandler, eventLogger);

        Log.d("lengths", extensions.length + "");
        com.google.android.exoplayer2.source.MediaSource mediaSource = mediaSources.length == 1 ? mediaSources[0]
                : new ConcatenatingMediaSource(mediaSources);


        videoPlayerView.setUseController(false);
        videoPlayerView.setControllerAutoShow(false);
        videoPlayerView.setUseController(false);
        videoPlayerView.setPlaybackPreparer(this);
        videoPlayerView.setPlayer(player);
        player.addListener(new PlayerEventListener());
        player.setVolume(0f);
        player.setRepeatMode(Player.REPEAT_MODE_ALL);
        player.setPlayWhenReady(true); //run file/link when ready to play.
        player.addVideoDebugListener(this);

        player.prepare(mediaSource, false, false);
    }
*/

/*
    public void setVolumeMute() {
        if (player != null)
            player.setVolume(0f);
    }
*/

/*
    private void initializeMethod() {
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
        DefaultRenderersFactory renderersFactory = new DefaultRenderersFactory(context,
                drmSessionManager, extensionRendererMode);

// 3. Create the player
        //    player = ExoPlayerFactory.newSimpleInstance(this, trackSelector, loadControl);
        player = ExoPlayerFactory.newSimpleInstance(renderersFactory, trackSelector, loadControl);

    }
*/


/*
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
*/

 /*   @Override
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

    @Override
    public void preparePlayback() {
        initializePlayer();
    }

    private class PlayerEventListener extends Player.DefaultEventListener {

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            if (playbackState == Player.STATE_READY) {
                if (player != null)
                    player.setVolume(0f);
            }
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

    public void releasePlayer() {
        if (player != null*//* && (player.getPlaybackState() == 2 || player.getPlaybackState() == 3)*//*) {
            player.removeListener(new PlayerEventListener());
            player.stop();
            player.seekTo(0);
            player.release();
            player = null;
            trackSelector = null;
            eventLogger = null;
        }
    }*/

/*
    public void playVideo() {
        //    Uri uri = Uri.parse(videoUrl);
        final Uri videoUri = Uri.parse(ATPreferences.readString(context, Constants.KEY_VIDEO_URL) + videoUrl);
        videoPlayerView.setVideoSource(new UriSource(context, videoUri));

        if (videoPlayerView.isPlaying())
            videoPlayerView.stopPlayback();
        videoPlayerView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                try {

                    mp.start();
                    mp.setVolume(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // relativeLayoutmain.setVisibility(View.VISIBLE);
            }
        });
        videoPlayerView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                return false;
            }
        });
        videoPlayerView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                playVideo();
            }
        });
        videoPlayerView.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                Log.d("buffering", "" + percent);
            }
        });

        // itemName = items.get(0).getItemName();
    }
*/

/*
    public void releasePlayer() {
        if (videoPlayerView.isPlaying())
            videoPlayerView.stopPlayback();


    }
*/


    public void initializePlayer() {

        final Uri videoUri = Uri.parse(/*ATPreferences.readString(context, Constants.KEY_VIDEO_URL) +*/ videoUrl);
        AdaptiveTrackSelection.Factory adaptiveTrackSelectionFactory = new AdaptiveTrackSelection.Factory();
        trackSelector = new DefaultTrackSelector(context, adaptiveTrackSelectionFactory);
//        eventLogger = new EventLogger(trackSelector);
        extensions = new String[1];
        com.google.android.exoplayer2.source.MediaSource[] mediaSources = new com.google.android.exoplayer2.source.MediaSource[1];
        //   mediaSources[i] = buildMediaSource(uris[i], extensions[i], mainHandler, eventLogger);


        mediaSources[0] = buildMediaSource(videoUri, extensions[0], mainHandler);

        //   mediaSources[i] = buildMediaSource(uri, extensions[i], mainHandler, eventLogger);

        Log.d("lengths", extensions.length + "");
        com.google.android.exoplayer2.source.MediaSource mediaSource = mediaSources.length == 1 ? mediaSources[0]
                : new ConcatenatingMediaSource(mediaSources);


        videoPlayerView.setUseController(false);
        videoPlayerView.setPlaybackPreparer(this);
        videoPlayerView.setPlayer(player);
        player.addListener(playerEventListener);
//        player.addListener(eventLogger);
        player.setRepeatMode(Player.REPEAT_MODE_OFF);
        player.setVolume(0f);
        player.setPlayWhenReady(true); //run file/link when ready to play.

        player.prepare(mediaSource, false, false);
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
    public void onVideoDisabled(DecoderCounters counters) {

    }

    private DataSource.Factory buildDataSourceFactory(boolean useBandwidthMeter) {
        return App.getInstance()
                .buildDataSourceFactory(useBandwidthMeter ? BANDWIDTH_METER : null);
    }

    private com.google.android.exoplayer2.source.MediaSource buildMediaSource(
            Uri uri,
            String overrideExtension,
            @Nullable Handler handler) {
        @C.ContentType int type = TextUtils.isEmpty(overrideExtension) ? Util.inferContentType(uri)
                : Util.inferContentType("." + overrideExtension);
        MediaItem mediaItem = MediaItem.fromUri(uri);
        switch (type) {
            case C.TYPE_DASH:
                return new DashMediaSource.Factory(
                        new DefaultDashChunkSource.Factory(mediaDataSourceFactory),
                        buildDataSourceFactory(false))
                        .createMediaSource(mediaItem);
            case C.TYPE_SS:
                return new SsMediaSource.Factory(
                        new DefaultSsChunkSource.Factory(mediaDataSourceFactory),
                        buildDataSourceFactory(false))
                        .createMediaSource(mediaItem);
            case C.TYPE_HLS:
                return new HlsMediaSource.Factory(mediaDataSourceFactory)
                        .createMediaSource(mediaItem);
            case C.TYPE_OTHER:
                return new ProgressiveMediaSource.Factory(mediaDataSourceFactory)
                        .createMediaSource(mediaItem);
            default: {
                throw new IllegalStateException("Unsupported type: " + type);
            }
        }
    }

    private class PlayerEventListener implements Player.EventListener {

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            if (playbackState == Player.STATE_ENDED) {
                progressBar.setVisibility(View.GONE);
            }
            if (playbackState == Player.STATE_BUFFERING) {
                progressBar.setVisibility(View.VISIBLE);
            }
            if (playbackState == Player.STATE_READY) {
                progressBar.setVisibility(View.GONE);
                videoPlayerView.setVisibility(View.VISIBLE);
            }
            if (playbackState == Player.STATE_IDLE) {
                progressBar.setVisibility(View.GONE);
            }
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

}


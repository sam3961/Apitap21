package com.apitap.views.fragments.storefront.adapter;

import android.content.Context;
import android.net.Uri;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;

import com.apitap.App;
import com.apitap.R;
import com.apitap.model.Constants;
import com.apitap.model.Utils;
import com.apitap.model.customclasses.CustomImageView;
import com.apitap.model.preferences.ATPreferences;
import com.apitap.model.storeFrontItems.ads.AdsData;
import com.apitap.views.fragments.adDetails.FragmentAdDetail;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackPreparer;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
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
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory;
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor;
import com.google.android.exoplayer2.upstream.cache.SimpleCache;
import com.google.android.exoplayer2.util.EventLogger;
import com.google.android.exoplayer2.util.Util;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class AdapterStoreAdsPager extends PagerAdapter implements PlaybackPreparer {


    private final LayoutInflater inflater;
    private final Context context;
    private List<AdsData> adsList;
    private PlayerView playerView;
    private CircleProgressBar progressBar;
    private AdsItemClick adapterClick;
    private SimpleExoPlayer player;
    private DefaultTrackSelector trackSelector;
    private String[] extensions;
    private EventLogger eventLogger;
    private String videoUrl = "";
    private static final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();

    public AdapterStoreAdsPager(Context context, ArrayList<AdsData> result, AdsItemClick adapterClick) {

        this.context = context;
        this.adsList = result;
        this.adapterClick = adapterClick;
        inflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return adsList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = inflater.inflate(R.layout.adapter_store_pager, container, false);
        assert view != null;
        final CustomImageView imageView = view.findViewById(R.id.image);
        final ImageView imageViewSeen = view.findViewById(R.id.seen);
        final TextView textViewStoreName = view.findViewById(R.id.textViewStoreName);
        final TextView textViewAdTitle = view.findViewById(R.id.textViewAdTitle);
        playerView = view.findViewById(R.id.video_view);
        progressBar = view.findViewById(R.id.progressBar);

        if (adsList.get(position).getIsSeen().equals("true"))
            imageViewSeen.setImageDrawable(context.getResources().getDrawable(R.drawable.grey_seen));
        else
            imageViewSeen.setImageDrawable(context.getResources().getDrawable(R.drawable.green_seen));

        Glide.with(context).load(ATPreferences.readString(context, Constants.KEY_IMAGE_URL)
                + adsList.get(position).getImageUrl()).placeholder
                (R.drawable.loading_no_border).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).into(imageView);


        textViewAdTitle.setText(adsList.get(position).getAdName());
        textViewStoreName.setText(adsList.get(position).getStoreName());

        videoUrl = adsList.get(position).getVideoUrl();

        if (!adsList.get(position).getVideoUrl().isEmpty()) {
            playerView.setVisibility(View.VISIBLE);
            playerReadyInitialization();
            initializePlayer(videoUrl);
        } else {
            playerView.setVisibility(View.GONE);
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                releasePlayer();
                adapterClick.onAdClick(position);
            }
        });


        container.addView(view, 0);
        return view;
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


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public interface AdsItemClick {
        public void onAdClick(int position);
    }


    @Override
    public void preparePlayback() {
        if (!videoUrl.isEmpty())
            initializePlayer(videoUrl);
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

    private void playerReadyInitialization() {
        releasePlayer();

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

        player = ExoPlayerFactory.newSimpleInstance(renderersFactory, trackSelector, loadControl);

    }

    public void initializePlayer(String videoUrl) {

        final Uri videoUri = Uri.parse(ATPreferences.readString(context, Constants.KEY_VIDEO_URL) + videoUrl);

        TrackSelection.Factory adaptiveTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(BANDWIDTH_METER);
        trackSelector = new DefaultTrackSelector(adaptiveTrackSelectionFactory);
        eventLogger = new EventLogger(trackSelector);
        extensions = new String[1];


        MediaSource mediaSources = new ExtractorMediaSource(videoUri,
                new CacheDataSourceFactory(context, 100 * 1024 * 1024, 5 * 1024 * 1024), new DefaultExtractorsFactory(), null, null);

        playerView.setPlaybackPreparer(this);
        playerView.setPlayer(player);
        player.addListener(eventLogger);
        player.setRepeatMode(Player.REPEAT_MODE_ALL);
        player.setPlayWhenReady(true); //run file/link when ready to play.
        player.setVolume(0f);
        player.prepare(mediaSources, false, false);
    }


    public void releasePlayer() {
        if (player != null) {
            player.stop();
            player.release();
            player = null;
        }
    }

    public void pauseResumePlayer(boolean isPause) {
        if (player != null) {
            player.setPlayWhenReady(!player.getPlayWhenReady());

        }
    }

    public void seekToStart() {
        if (player != null&&!videoUrl.isEmpty()) {
            player.seekTo(0);
        }
    }

}


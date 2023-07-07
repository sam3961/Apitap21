package com.apitap.views.fragments.checkinTv;


import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioDeviceInfo;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.UiThread;
import androidx.annotation.WorkerThread;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.apitap.R;
import com.apitap.model.Constants;
import com.apitap.model.preferences.ATPreferences;
import com.apitap.views.fragments.checkinTv.adapter.AdapterGameDeals;
import com.apitap.views.fragments.checkinTv.adapter.AdapterNoticesList;
import com.apitap.views.streaming.AudioPlayer;
import com.apitap.views.streaming.AudioRecorder;
import com.apitap.views.streaming.ConnectionsFragment;
import com.google.android.gms.nearby.connection.ConnectionInfo;
import com.google.android.gms.nearby.connection.Payload;
import com.google.android.gms.nearby.connection.Strategy;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import pl.droidsonroids.gif.GifImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentStreamDetail extends ConnectionsFragment implements View.OnClickListener,
        AdapterNoticesList.ClickListener {

    private View rootView;

    private RecyclerView recyclerViewGameDeals;
    private RecyclerView receiverViewNoticesList;
    private ImageView imageViewBack, imageViewLowVolume, imageViewMute, imageViewViewVolumeUp, imageViewHeadset;
    private GifImageView imageViewGif;
    private TextView textViewTitle, textViewStoreName, textViewShowDuration,
            textViewTvName, textViewNowPlaying, textViewShowTitle;
    private LinearLayout linearLayoutHeaderCheckin;
    private LinearLayout linearLayoutStoreReservation;
    private RelativeLayout relativeLayoutSearchBarStoreFront;
    private LinearLayout linearLayoutStoreDetailHeader;
    private LinearLayout linearLayoutHeaderStoreFront;
    private LinearLayout linearLayoutHeaderCategory;
    private SeekBar seekBarOval, seekBar;
    private CircularProgressBar circularProgressBar;
    private NestedScrollView nestedScrollView;


    //streaming
    private ArrayList<ConnectionsFragment.Endpoint> endpointArrayList = new ArrayList<>();
    private ArrayList<String> endpointNameArrayList = new ArrayList<>();
    private final Handler mUiHandler = new Handler(Looper.getMainLooper());
    private FragmentCheckIn.State mState = FragmentCheckIn.State.UNKNOWN;
    private long mediaDuration = 360000;
    private final Set<AudioPlayer> mAudioPlayers = new HashSet<>();
    @Nullable
    private AudioRecorder mRecorder;
    private boolean isMute = false;
    private static final String SERVICE_ID =
            "com.google.location.nearby.apps.walkietalkie.manual.SERVICE_ID";
    private static final Strategy STRATEGY = Strategy.P2P_STAR;
    private String mName;

    private final Runnable mDiscoverRunnable =
            new Runnable() {
                @Override
                public void run() {
                    setState(FragmentCheckIn.State.DISCOVERING);
                }
            };


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_stream_detail, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Constants.USER = Constants.HOST;
        mediaDuration = getArguments().getLong(Constants.TV_MEDIA_DURATION);
        Log.d("TAG", "Media Duration: "+mediaDuration);

        initViews();
        clickListeners();
        seekBarListener();
        setData();

        setNotificationListAdapter();
        setGameDealsAdapter();


        manageHomeTopViews();

        focusOnView(textViewNowPlaying);

        if (isHeadphonesPlugged()){
            imageViewHeadset.setImageResource(R.drawable.ic_baseline_headset_24);
        }

    }


    private void initViews() {
        recyclerViewGameDeals = rootView.findViewById(R.id.recyclerViewGameDeals);
        receiverViewNoticesList = rootView.findViewById(R.id.receiverViewNotices);
        imageViewBack = rootView.findViewById(R.id.imageViewBack);
        imageViewGif = rootView.findViewById(R.id.imageViewGif);
        textViewTitle = rootView.findViewById(R.id.textViewTitle);
        textViewStoreName = rootView.findViewById(R.id.textViewMerchantName);
        textViewShowDuration = rootView.findViewById(R.id.textViewShowDuration);
        seekBarOval = rootView.findViewById(R.id.seek_bar);
        circularProgressBar = rootView.findViewById(R.id.circularProgressBar);
        nestedScrollView = rootView.findViewById(R.id.nestedScrollView);
        textViewTvName = rootView.findViewById(R.id.textViewTvName);
        textViewNowPlaying = rootView.findViewById(R.id.textViewNowPlaying);
        textViewShowTitle = rootView.findViewById(R.id.textViewShowTitle);
        seekBar = rootView.findViewById(R.id.seekBar);
        imageViewLowVolume = rootView.findViewById(R.id.imageViewLowVolume);
        imageViewMute = rootView.findViewById(R.id.imageViewMute);
        imageViewViewVolumeUp = rootView.findViewById(R.id.imageViewViewVolumeUp);
        imageViewHeadset = rootView.findViewById(R.id.imageViewHeadset);

        linearLayoutHeaderCheckin = getActivity().findViewById(R.id.view_checkin);
        linearLayoutStoreReservation = getActivity().findViewById(R.id.view_store_reservation);
        relativeLayoutSearchBarStoreFront = getActivity().findViewById(R.id.search_storefront);
        linearLayoutStoreDetailHeader = getActivity().findViewById(R.id.view_store_detail_header);
        linearLayoutHeaderStoreFront = getActivity().findViewById(R.id.header);
        linearLayoutHeaderCategory = getActivity().findViewById(R.id.header_browse_category);
    }


    private void seekBarListener() {
        AudioManager audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        if (audioManager.isWiredHeadsetOn())

        seekBar.setMax(audioManager
                .getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        seekBar.setProgress(audioManager
                .getStreamVolume(AudioManager.STREAM_MUSIC));

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                        progress, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void setData() {
        textViewStoreName.setText("Welcome to\n" + ATPreferences.readString(getActivity(), Constants.STORE_NAME));
        textViewTitle.setText(getArguments().getString(Constants.HEADER_TITLE));
        textViewTvName.setText(getArguments().getString(Constants.TV_HOST_NAME));
        textViewShowTitle.setText(getArguments().getString(Constants.TV_MEDIA_NAME));

        imageViewGif.setImageResource(R.drawable.playing);

//        Glide.with(this).load(R.raw.playing)
//                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
//                .into(imageViewGif);
    }

    private void setNotificationListAdapter() {
        AdapterNoticesList adapterTvList = new AdapterNoticesList(requireContext(),
                new ArrayList(), this);
        receiverViewNoticesList.setAdapter(adapterTvList);
    }

    private void setGameDealsAdapter() {
        AdapterGameDeals adapterGameDeals = new AdapterGameDeals(requireContext(),
                new ArrayList());
        recyclerViewGameDeals.setAdapter(adapterGameDeals);
    }


    private void manageHomeTopViews() {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            linearLayoutStoreDetailHeader.setVisibility(View.VISIBLE);
            relativeLayoutSearchBarStoreFront.setVisibility(View.GONE);
            linearLayoutHeaderCheckin.setVisibility(View.GONE);
            linearLayoutHeaderStoreFront.setVisibility(View.GONE);
            linearLayoutHeaderCategory.setVisibility(View.GONE);
        }, 200);
    }

    private void focusOnView(final View view) {
        long delay = 100; //delay to let finish with possible modifications to ScrollView
        nestedScrollView.postDelayed(new Runnable() {
            public void run() {
                nestedScrollView.smoothScrollTo(0, view.getTop());
            }
        }, delay);
    }

    private void clickListeners() {
        imageViewBack.setOnClickListener(this);
        imageViewMute.setOnClickListener(this);
        imageViewViewVolumeUp.setOnClickListener(this);
        imageViewLowVolume.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageViewMute:

                if (isMute) {
                    isMute = false;
                    imageViewMute.setImageResource(R.drawable.volume_mute_white);
                    AudioManager amanager = (AudioManager) requireActivity().getSystemService(Context.AUDIO_SERVICE);
                    amanager.setStreamMute(AudioManager.STREAM_MUSIC, false);

                } else {
                    isMute = true;
                    imageViewMute.setImageResource(R.drawable.volume_mute_red);
                    AudioManager amanager = (AudioManager) requireActivity().getSystemService(Context.AUDIO_SERVICE);
                    amanager.setStreamMute(AudioManager.STREAM_MUSIC, true);
                }


                break;
            case R.id.imageViewViewVolumeUp:

                break;
            case R.id.imageViewLowVolume:

                break;
            case R.id.imageViewBack:
                onBackPress();
                break;
        }
    }

    @Override
    public void onClick(View view, int position) {
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        linearLayoutHeaderCheckin.setVisibility(View.VISIBLE);
        relativeLayoutSearchBarStoreFront.setVisibility(View.VISIBLE);
        linearLayoutStoreDetailHeader.setVisibility(View.GONE);
        linearLayoutHeaderStoreFront.setVisibility(View.VISIBLE);
        linearLayoutHeaderCategory.setVisibility(View.VISIBLE);
    }


    //streaming code

    @Override
    public void onStart() {
        super.onStart();
        setState(FragmentCheckIn.State.DISCOVERING);
    }

    @Override
    public void onStop() {
        if (isPlaying()) {
            stopPlaying();
        }

        setState(FragmentCheckIn.State.UNKNOWN);

        mUiHandler.removeCallbacksAndMessages(null);

        super.onStop();
    }

    @Override
    protected void onEndpointDiscovered(ConnectionsFragment.Endpoint endpoint) {
        // We found an advertiser!
        if (!isConnecting()) {
            if (endpointArrayList.size() > 0) {
                for (int i = 0; i < endpointArrayList.size(); i++) {
                    if (endpointArrayList.get(i).getName().equalsIgnoreCase(endpoint.getName())) {
                        if (!endpointArrayList.get(i).getId().equalsIgnoreCase(endpoint.getId())) {
                            endpointArrayList.add(endpoint);
                            endpointNameArrayList.add(endpoint.getName());
                        } else {
                            endpointArrayList.remove(i);
                            endpointNameArrayList.remove(i);
                        }
                    } else {
                        endpointArrayList.add(endpoint);
                        endpointNameArrayList.add(endpoint.getName());
                    }
                }
            } else {
                endpointArrayList.add(endpoint);
                endpointNameArrayList.add(endpoint.getName());

            }

            Set<ConnectionsFragment.Endpoint> s = new HashSet<ConnectionsFragment.Endpoint>();
            s.addAll(endpointArrayList);
            endpointArrayList = new ArrayList<ConnectionsFragment.Endpoint>();
            endpointArrayList.addAll(s);


            Set<String> s1 = new HashSet<String>();
            s1.addAll(endpointNameArrayList);
            endpointNameArrayList = new ArrayList<String>();
            endpointNameArrayList.addAll(s1);


            // mCurrentStateView.setText(endpointArrayList.size()+ " Connection found click on item for establish connection.");
            for (int i = 0; i < endpointArrayList.size(); i++) {
                if (endpointArrayList.get(i).getName().equals(getArguments().getString(Constants.TV_HOST_NAME)))
                    connectToEndpoint(endpointArrayList.get(i));

            }
        }
    }

    @Override
    protected void onConnectionInitiated(ConnectionsFragment.Endpoint endpoint, ConnectionInfo connectionInfo) {
        // A connection to another device has been initiated! We'll accept the connection immediately.
        acceptConnection(endpoint);


    }

    @Override
    protected void onEndpointConnected(ConnectionsFragment.Endpoint endpoint) {
        setState(FragmentCheckIn.State.CONNECTED);
    }

    @Override
    protected void onEndpointDisconnected(ConnectionsFragment.Endpoint endpoint) {
        setState(FragmentCheckIn.State.DISCOVERING);
    }

    @Override
    protected void onConnectionFailed(ConnectionsFragment.Endpoint endpoint) {
        // Let's try someone else.
        if (getState() == FragmentCheckIn.State.DISCOVERING && !getDiscoveredEndpoints().isEmpty()) {
            connectToEndpoint(pickRandomElem(getDiscoveredEndpoints()));
        }
    }

    /**
     * The state has changed. I wonder what we'll be doing now.
     *
     * @param state The new state.
     */
    private void setState(FragmentCheckIn.State state) {
        if (mState == state) {
            logW("State set to " + state + " but already in that state");
            return;
        }

        logD("State set to " + state);
        FragmentCheckIn.State oldState = mState;
        mState = state;
        onStateChanged(oldState, state);
    }

    /**
     * @return The current state.
     */
    private FragmentCheckIn.State getState() {
        return mState;
    }

    /**
     * State has changed.
     *
     * @param oldState The previous state we were in. Clean up anything related to this state.
     * @param newState The new state we're now in. Prepare the UI for this state.
     */
    private void onStateChanged(FragmentCheckIn.State oldState, FragmentCheckIn.State newState) {
        // Update Nearby Connections to the new state.
        switch (newState) {
            case DISCOVERING:
                if (isAdvertising()) {
                    stopAdvertising();
                }
                disconnectFromAllEndpoints();
                startDiscovering();
                break;
            case ADVERTISING:
                if (isDiscovering()) {
                    stopDiscovering();
                }
                disconnectFromAllEndpoints();
                startAdvertising();
                break;
            case CONNECTED:
                if (isDiscovering()) {
                    stopDiscovering();
                } else if (isAdvertising()) {
                    // Continue to advertise, so others can still connect,
                    // but clear the discover runnable.
                    removeCallbacks(mDiscoverRunnable);
                }
                baseshowFeedbackMessage(requireActivity(), rootView, "Connected");
                runTimer();
                break;
            case UNKNOWN:
                stopAllEndpoints();
                break;
            default:
                // no-op
                break;
        }

        // Update the UI.
        switch (oldState) {
            case UNKNOWN:
                // Unknown is our initial state. Whatever state we move to,
                // we're transitioning forwards.
                transitionForward(oldState, newState);
                break;
            case DISCOVERING:
                switch (newState) {
                    case UNKNOWN:
                        transitionBackward(oldState, newState);
                        break;
                    case ADVERTISING:
                    case CONNECTED:
                        transitionForward(oldState, newState);
                        break;
                    default:
                        // no-op
                        break;
                }
                break;
            case ADVERTISING:
                switch (newState) {
                    case UNKNOWN:
                    case DISCOVERING:
                        transitionBackward(oldState, newState);
                        break;
                    case CONNECTED:
                        transitionForward(oldState, newState);
                        break;
                    default:
                        // no-op
                        break;
                }
                break;
            case CONNECTED:
                // Connected is our final state. Whatever new state we move to,
                // we're transitioning backwards.
                transitionBackward(oldState, newState);
                break;
            default:
                // no-op
                break;
        }
    }

    private void runTimer() {
        circularProgressBar.setEnabled(false);
        seekBarOval.setEnabled(false);
        circularProgressBar.setProgressMax(mediaDuration);
        seekBarOval.setMax(Math.toIntExact(mediaDuration));
        new CountDownTimer(mediaDuration, 1000) {
            @SuppressLint("DefaultLocale")
            public void onTick(long millisUntilFinished) {
                int barVal = (int) millisUntilFinished / 1000;
                seekBarOval.setProgress(Math.toIntExact(3600000 - millisUntilFinished));
                circularProgressBar.setProgress(Math.toIntExact(3600000 - millisUntilFinished));

                textViewShowDuration.setText(String.format("%02d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))
                ));
            }

            public void onFinish() {
            }
        }.start();
    }

    /**
     * Transitions from the old state to the new state with an animation implying moving forward.
     */
    @UiThread
    private void transitionForward(FragmentCheckIn.State oldState, final FragmentCheckIn.State newState) {
        //   mPreviousStateView.setVisibility(View.VISIBLE);
        // mCurrentStateView.setVisibility(View.VISIBLE);

    }

    /**
     * Transitions from the old state to the new state with an animation implying moving backward.
     */
    @UiThread
    private void transitionBackward(FragmentCheckIn.State oldState, final FragmentCheckIn.State newState) {
        //mPreviousStateView.setVisibility(View.VISIBLE);
        //  mCurrentStateView.setVisibility(View.VISIBLE);

    }


    @Override
    protected void onReceive(ConnectionsFragment.Endpoint endpoint, Payload payload) {
        Log.d("onReceive", "onReceive" + "payload");
        if (payload.getType() == Payload.Type.STREAM) {
            AudioPlayer player =
                    new AudioPlayer(payload.asStream().asInputStream()) {
                        @WorkerThread
                        @Override
                        protected void onFinish() {
                            final AudioPlayer audioPlayer = this;
                            post(
                                    new Runnable() {
                                        @UiThread
                                        @Override
                                        public void run() {
                                            mAudioPlayers.remove(audioPlayer);
                                        }
                                    });
                        }
                    };
            mAudioPlayers.add(player);
            player.start();
        }
    }

    /**
     * Stops all currently streaming audio tracks.
     */
    private void stopPlaying() {
        logV("stopPlaying()");
        for (AudioPlayer player : mAudioPlayers) {
            player.stop();
        }
        mAudioPlayers.clear();
    }

    /**
     * @return True if currently playing.
     */
    private boolean isPlaying() {
        return !mAudioPlayers.isEmpty();
    }


    /**
     * @return True if currently streaming from the microphone.
     */
    private boolean isRecording() {
        return mRecorder != null && mRecorder.isRecording();
    }

    /**
     * {@see ConnectionsActivity#getRequiredPermissions()}
     */
    @Override
    protected String[] getRequiredPermissions() {
        return super.getRequiredPermissions();

    }

    /**
     * Joins 2 arrays together.
     */
    private static String[] join(String[] a, String... b) {
        String[] join = new String[a.length + b.length];
        System.arraycopy(a, 0, join, 0, a.length);
        System.arraycopy(b, 0, join, a.length, b.length);
        return join;
    }

    /**
     * Queries the phone's contacts for their own profile, and returns their name. Used when
     * connecting to another device.
     */
    @Override
    protected String getName() {
        return mName;
    }

    /**
     * {@see ConnectionsActivity#getServiceId()}
     */
    @Override
    public String getServiceId() {
        return SERVICE_ID;
    }

    /**
     * {@see ConnectionsActivity#getStrategy()}
     */
    @Override
    public Strategy getStrategy() {
        return STRATEGY;
    }

    /**
     * {@see Handler#post()}
     */
    protected void post(Runnable r) {
        mUiHandler.post(r);
    }

    /**
     * {@see Handler#postDelayed(Runnable, long)}
     */
    protected void postDelayed(Runnable r, long duration) {
        mUiHandler.postDelayed(r, duration);
    }

    /**
     * {@see Handler#removeCallbacks(Runnable)}
     */
    protected void removeCallbacks(Runnable r) {
        mUiHandler.removeCallbacks(r);
    }

    @Override
    protected void logV(String msg) {
        super.logV(msg);
    }

    @Override
    protected void logD(String msg) {
        super.logD(msg);
    }

    @Override
    protected void logW(String msg) {
        super.logW(msg);
    }

    @Override
    protected void logW(String msg, Throwable e) {
        super.logW(msg, e);
    }

    @Override
    protected void logE(String msg, Throwable e) {
        super.logE(msg, e);
    }


    private static CharSequence toColor(String msg, int color) {
        SpannableString spannable = new SpannableString(msg);
        spannable.setSpan(new ForegroundColorSpan(color), 0, msg.length(), 0);
        return spannable;
    }

    private static String generateRandomName() {
        String name = "";
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            name += random.nextInt(10);
        }
        return name;
    }

    @SuppressWarnings("unchecked")
    private static <T> T pickRandomElem(Collection<T> collection) {
        return (T) collection.toArray()[new Random().nextInt(collection.size())];
    }


    /**
     * Provides an implementation of Animator.AnimatorListener so that we only have to override the
     * method(s) we're interested in.
     */
    private abstract static class AnimatorListener implements Animator.AnimatorListener {
        @Override
        public void onAnimationStart(Animator animator) {
        }

        @Override
        public void onAnimationEnd(Animator animator) {
        }

        @Override
        public void onAnimationCancel(Animator animator) {
        }

        @Override
        public void onAnimationRepeat(Animator animator) {
        }
    }


    /**
     * States that the UI goes through.
     */
    public enum State {
        UNKNOWN,
        DISCOVERING,
        ADVERTISING,
        CONNECTED

    }

    private boolean isHeadphonesPlugged(){
        AudioManager audioManager = (AudioManager)getActivity().getSystemService(Context.AUDIO_SERVICE);
        AudioDeviceInfo[] audioDevices = new AudioDeviceInfo[0];
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            audioDevices = audioManager.getDevices(AudioManager.GET_DEVICES_INPUTS);
            for(AudioDeviceInfo deviceInfo : audioDevices){
                if(deviceInfo.getType()==AudioDeviceInfo.TYPE_WIRED_HEADPHONES
                        || deviceInfo.getType()== AudioDeviceInfo.TYPE_WIRED_HEADSET){
                    return true;
                }
            }
        }
        return false;
    }
}

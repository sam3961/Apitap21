package com.apitap.views.fragments.checkinTv;


import android.animation.Animator;
import android.app.Dialog;
import android.os.Bundle;
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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.UiThread;
import androidx.annotation.WorkerThread;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.apitap.R;
import com.apitap.controller.ModelManager;
import com.apitap.model.Constants;
import com.apitap.model.Operations;
import com.apitap.model.Utils;
import com.apitap.model.activeTvInfo.ActiveTvInfoResponse;
import com.apitap.model.activeTvInfo.RESULTItem;
import com.apitap.model.customclasses.Event;
import com.apitap.model.preferences.ATPreferences;
import com.apitap.views.HomeActivity;
import com.apitap.views.fragments.checkinTv.adapter.AdapterGameDeals;
import com.apitap.views.fragments.checkinTv.adapter.AdapterTvList;
import com.apitap.views.streaming.AudioPlayer;
import com.apitap.views.streaming.AudioRecorder;
import com.apitap.views.streaming.ConnectionsFragment;
import com.google.android.gms.nearby.connection.ConnectionInfo;
import com.google.android.gms.nearby.connection.Payload;
import com.google.android.gms.nearby.connection.Strategy;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentCheckIn extends ConnectionsFragment implements View.OnClickListener, AdapterTvList.ClickListener {

    private View rootView;
    private RecyclerView recyclerViewGameDeals;
    private RecyclerView receiverViewTVList;
    private ImageView imageViewBack;
    private TextView textViewTitle, textViewStoreName;
    private LinearLayout linearLayoutHeaderCheckin;
    private LinearLayout linearLayoutStoreReservation;
    private RelativeLayout relativeLayoutSearchBarStoreFront;
    private LinearLayout linearLayoutStoreDetailHeader;
    private LinearLayout linearLayoutHeaderStoreFront;
    private LinearLayout linearLayoutHeaderCategory;
    private TextView textViewNoData;
    private AdapterTvList adapterTvList;
    private Dialog dialogConnectTv;

    //streaming
    private ArrayList<ConnectionsFragment.Endpoint> endpointArrayList = new ArrayList<>();
    private ArrayList<String> endpointArrayListName = new ArrayList<>();
    private final Handler mUiHandler = new Handler(Looper.getMainLooper());
    private State mState = State.UNKNOWN;
    private boolean isBroadCasting;
    private final Set<AudioPlayer> mAudioPlayers = new HashSet<>();
    @Nullable
    private AudioRecorder mRecorder;
    private static final String SERVICE_ID =
            "com.google.location.nearby.apps.walkietalkie.manual.SERVICE_ID";
    private static final Strategy STRATEGY = Strategy.P2P_STAR;
    private boolean isApiResponse = false;
    private String mName = "";

    private final Runnable mDiscoverRunnable =
            new Runnable() {
                @Override
                public void run() {
                    setState(State.DISCOVERING);
                }
            };


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_checkin, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Constants.USER = Constants.HOST;

        dialogConnectTv = Utils.createLoadingDialog(requireContext(), false);
        initViews();
        clickListeners();
        setData();

        setTvListAdapter();
        setGameDealsAdapter();
        showProgress();


    }


    private void setTvListAdapter() {
        adapterTvList = new AdapterTvList(this,
                endpointArrayList, this);
        receiverViewTVList.setAdapter(adapterTvList);

        checkIfListEmpty();
    }

    private void checkIfListEmpty() {
        if (adapterTvList.getItemCount() == 0) {
            textViewNoData.setVisibility(View.VISIBLE);
            receiverViewTVList.setVisibility(View.GONE);
        } else {
            textViewNoData.setVisibility(View.GONE);
            receiverViewTVList.setVisibility(View.VISIBLE);
        }

    }

    private void setGameDealsAdapter() {
        AdapterGameDeals adapterGameDeals = new AdapterGameDeals(requireContext(),
                new ArrayList());
        recyclerViewGameDeals.setAdapter(adapterGameDeals);
    }


    private void initViews() {
        recyclerViewGameDeals = rootView.findViewById(R.id.recyclerViewGameDeals);
        receiverViewTVList = rootView.findViewById(R.id.receiverViewTVList);
        imageViewBack = rootView.findViewById(R.id.imageViewBack);
        textViewTitle = rootView.findViewById(R.id.textViewTitle);
        textViewStoreName = rootView.findViewById(R.id.textViewMerchantName);
        textViewNoData = rootView.findViewById(R.id.textViewNoData);

        linearLayoutHeaderCheckin = getActivity().findViewById(R.id.view_checkin);
        linearLayoutStoreReservation = getActivity().findViewById(R.id.view_store_reservation);

        relativeLayoutSearchBarStoreFront = getActivity().findViewById(R.id.search_storefront);
        linearLayoutStoreDetailHeader = getActivity().findViewById(R.id.view_store_detail_header);
        linearLayoutHeaderStoreFront = getActivity().findViewById(R.id.header);
        linearLayoutHeaderCategory = getActivity().findViewById(R.id.header_browse_category);

        linearLayoutStoreDetailHeader.setVisibility(View.VISIBLE);
        relativeLayoutSearchBarStoreFront.setVisibility(View.GONE);
        linearLayoutHeaderCheckin.setVisibility(View.GONE);
        linearLayoutHeaderStoreFront.setVisibility(View.GONE);
        linearLayoutHeaderCategory.setVisibility(View.GONE);
    }

    private void setData() {
        textViewStoreName.setText("Welcome to\n" + ATPreferences.readString(getActivity(), Constants.STORE_NAME));
        textViewTitle.setText(getArguments().getString(Constants.HEADER_TITLE));
    }

    private void clickListeners() {
        imageViewBack.setOnClickListener(this);
    }


    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageViewBack:
                onBackPress();
                break;
        }
    }

    @Subscribe
    public void onEvent(final Event event) {
        switch (event.getKey()) {
            case Constants.ACTIVE_TV_INFO:
                hideProgress();
                ActiveTvInfoResponse activeTvInfoResponse = ModelManager.getInstance().getMerchantStoresManager().activeTvInfoResponse;
                if (activeTvInfoResponse.getRESULT().get(0).getRESULT().size()>0) {
                    ArrayList<String> activeTvListName = new ArrayList<>();
                    HashMap<String, String> tvMap = new HashMap<>();
                    HashMap<String, String> tvMapMediaName = new HashMap<>();
                    HashMap<String, Long> tvMapMediaDuration = new HashMap<>();
                    for (int i = 0; i < activeTvInfoResponse.getRESULT().get(0).getRESULT().size(); i++) {
                        String locationName = activeTvInfoResponse.getRESULT().get(0).getRESULT().get(i).getLocationName();
                        String mediaName = Utils.hexToASCII(activeTvInfoResponse.getRESULT().get(0).getRESULT().get(i).getJsonMember127111());
                        String mediaDuration = activeTvInfoResponse.getRESULT().get(0).getRESULT().get(i).getJsonMember124152();
                        if (!locationName.isEmpty())
                            locationName = Utils.hexToASCII(activeTvInfoResponse.getRESULT().get(0).getRESULT().get(i).getLocationName());
                        activeTvListName.add(
                                activeTvInfoResponse.getRESULT().get(0).getRESULT().get(i).getTerminalName());

                        tvMap.put(activeTvInfoResponse.getRESULT().get(0).getRESULT().get(i).getTerminalName(), locationName);
                        tvMapMediaName.put(activeTvInfoResponse.getRESULT().get(0).getRESULT().get(i).getTerminalName(), mediaName);
                        tvMapMediaDuration.put(activeTvInfoResponse.getRESULT().get(0).getRESULT().get(i).getTerminalName(),
                                Long.valueOf(mediaDuration));

                        Log.d("CurrentActiveTVName",
                                activeTvInfoResponse.getRESULT().get(0).getRESULT().get(i).getTerminalName());
                    }

                    if (!isApiResponse && ModelManager.getInstance().getMerchantStoresManager().activeTvInfoResponse != null &&
                            ModelManager.getInstance().getMerchantStoresManager().activeTvInfoResponse.getRESULT() != null) {
                        List<RESULTItem> apiList = ModelManager.getInstance().getMerchantStoresManager().activeTvInfoResponse.getRESULT().get(0).getRESULT();
                        for (int i = 0; i < apiList.size(); i++) {
                            if (!endpointArrayListName.contains(apiList.get(i).getTerminalName())) {
                                endpointArrayList.add(new Endpoint(apiList.get(i).getJsonMember12069(),
                                        apiList.get(i).getTerminalName(), false));
                            }
                        }
                    }

                    adapterTvList.updateList(endpointArrayList);
                    adapterTvList.setAPITvList(tvMap, tvMapMediaName, tvMapMediaDuration);
                    isApiResponse = true;

                }
                else {
                    adapterTvList.updateList(new ArrayList<>());
                    checkIfListEmpty();
                }


                break;

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        dialogConnectTv.dismiss();
//        linearLayoutHeaderCheckin.setVisibility(View.VISIBLE);
//        relativeLayoutSearchBarStoreFront.setVisibility(View.VISIBLE);
//        linearLayoutStoreDetailHeader.setVisibility(View.GONE);
//        linearLayoutHeaderStoreFront.setVisibility(View.VISIBLE);
//        linearLayoutHeaderCategory.setVisibility(View.VISIBLE);
    }


    @Override
    public void onClick(int position, String mediaName, Long mediaDuration) {
        //dialogConnectTv.show();
        //connectToEndpoint(endpointArrayList.get(position));

        Bundle bundle = new Bundle();
        bundle.putString(Constants.HEADER_TITLE, getArguments().getString(Constants.HEADER_TITLE));
        bundle.putString(Constants.TV_HOST_NAME, endpointArrayList.get(position).getName());
        bundle.putInt(Constants.TV_HOST_POSITION, position);
        bundle.putString(Constants.TV_MEDIA_NAME, mediaName);
        if (mediaDuration==null)
            mediaDuration=1000L;
        bundle.putLong(Constants.TV_MEDIA_DURATION, mediaDuration);
        ((HomeActivity) requireContext()).displayView(new FragmentStreamDetail(), Constants.TAG_STREAM_DIRECTORY_DETAIL, bundle);


    }


    //streaming code

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        setState(State.DISCOVERING);
    }

    @Override
    public void onStop() {
        if (isPlaying()) {
            stopPlaying();
        }

        setState(State.UNKNOWN);

        mUiHandler.removeCallbacksAndMessages(null);

        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onEndpointDiscovered(Endpoint endpoint) {
        // We found an advertiser!
        if (!isConnecting()) {
            if (endpointArrayList.size() > 0) {
                for (int i = 0; i < endpointArrayList.size(); i++) {
                    if (endpointArrayList.get(i).getName().equalsIgnoreCase(endpoint.getName())) {
                        if (!endpointArrayList.get(i).getId().equalsIgnoreCase(endpoint.getId())) {
                            endpointArrayList.add(endpoint);
                            endpointArrayListName.add(endpoint.getName());
                        } else {
                            try {
                                endpointArrayList.remove(i);
                                endpointArrayListName.remove(i);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    } else {
                        endpointArrayList.add(endpoint);
                        endpointArrayListName.add(endpoint.getName());
                    }
                }
            } else {
                endpointArrayList.add(endpoint);
                endpointArrayListName.add(endpoint.getName());
            }


            Set<ConnectionsFragment.Endpoint> s = new HashSet<ConnectionsFragment.Endpoint>();
            s.addAll(endpointArrayList);
            endpointArrayList = new ArrayList<>();
            endpointArrayList.addAll(s);


            adapterTvList.updateList(endpointArrayList);
            checkIfListEmpty();

            ModelManager.getInstance().getMerchantStoresManager().getActivePlayersInfo(requireContext(),
                    Operations.makeJsonGetActivePlayersInfo(requireContext()));

            // mCurrentStateView.setText(endpointArrayList.size()+ " Connection found click on item for establish connection.");
            // connectToEndpoint(endpoint);
        }
    }

    @Override
    protected void onConnectionInitiated(ConnectionsFragment.Endpoint endpoint, ConnectionInfo connectionInfo) {
        // A connection to another device has been initiated! We'll accept the connection immediately.
        acceptConnection(endpoint);


    }

    @Override
    protected void onEndpointConnected(ConnectionsFragment.Endpoint endpoint) {
        setState(State.CONNECTED);
    }

    @Override
    protected void onEndpointDisconnected(Endpoint endpoint) {
        setState(State.DISCOVERING);
    }

    @Override
    protected void onConnectionFailed(Endpoint endpoint) {
        // Let's try someone else.
        if (getState() == State.DISCOVERING && !getDiscoveredEndpoints().isEmpty()) {
            connectToEndpoint(pickRandomElem(getDiscoveredEndpoints()));
        }
    }

    /**
     * The state has changed. I wonder what we'll be doing now.
     *
     * @param state The new state.
     */
    private void setState(State state) {
        if (mState == state) {
            logW("State set to " + state + " but already in that state");
            return;
        }

        logD("State set to " + state);
        State oldState = mState;
        mState = state;
        onStateChanged(oldState, state);
    }

    /**
     * @return The current state.
     */
    private State getState() {
        return mState;
    }

    /**
     * State has changed.
     *
     * @param oldState The previous state we were in. Clean up anything related to this state.
     * @param newState The new state we're now in. Prepare the UI for this state.
     */
    private void onStateChanged(State oldState, State newState) {
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

    /**
     * Transitions from the old state to the new state with an animation implying moving forward.
     */
    @UiThread
    private void transitionForward(State oldState, final State newState) {
        //   mPreviousStateView.setVisibility(View.VISIBLE);
        // mCurrentStateView.setVisibility(View.VISIBLE);

    }

    /**
     * Transitions from the old state to the new state with an animation implying moving backward.
     */
    @UiThread
    private void transitionBackward(State oldState, final State newState) {
        //mPreviousStateView.setVisibility(View.VISIBLE);
        //  mCurrentStateView.setVisibility(View.VISIBLE);

    }


    @Override
    protected void onReceive(Endpoint endpoint, Payload payload) {
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
}

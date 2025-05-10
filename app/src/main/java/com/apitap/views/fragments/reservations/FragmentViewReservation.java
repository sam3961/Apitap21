package com.apitap.views.fragments.reservations;


import static android.media.CamcorderProfile.get;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.apitap.R;
import com.apitap.controller.ModelManager;
import com.apitap.model.Constants;
import com.apitap.model.DrawableUtils;
import com.apitap.model.Operations;
import com.apitap.model.customclasses.Event;
import com.apitap.model.getReservation.GetReservationResponse;
import com.apitap.model.getReservation.RESULTItem;
import com.apitap.views.HomeActivity;
import com.apitap.views.fragments.BaseFragment;
import com.apitap.views.fragments.reservations.adapter.ReservationListAdapter;
import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnCalendarPageChangeListener;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentViewReservation extends BaseFragment implements View.OnClickListener, ReservationListAdapter.AdapterClick {

    private View rootView;
    private ImageView imageViewBack;
    private LinearLayout linearLayoutHeaderCheckin;
    private LinearLayout linearLayoutStoreReservation;
    private RelativeLayout relativeLayoutSearchBarStoreFront;
    private LinearLayout linearLayoutStoreDetailHeader;
    private LinearLayout linearLayoutHeaderStoreFront;
    private LinearLayout linearLayoutHeaderCategory;

    private CalendarView calendarView;
    private RecyclerView recyclerViewReservationList;
    private ArrayList<EventDay> eventsList = new ArrayList<EventDay>();
    private List<RESULTItem> resultItemList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_view_reservation, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        initViews();
        clickListeners();

        showProgress();
        ModelManager.getInstance().getReservationManager().addReservationDetails(getActivity(),
                Operations.makeJsonGetAllReservations(requireActivity()),
                Constants.TAG_GET_RESERVATION);

    }

    private void initViews() {
        imageViewBack = rootView.findViewById(R.id.imageViewBack);
        calendarView = rootView.findViewById(R.id.calendarView);
        recyclerViewReservationList = rootView.findViewById(R.id.rv_events_list);

        linearLayoutHeaderCheckin = getActivity().findViewById(R.id.view_checkin);
        linearLayoutStoreReservation = getActivity().findViewById(R.id.view_store_reservation);

        relativeLayoutSearchBarStoreFront = getActivity().findViewById(R.id.search_storefront);
        linearLayoutStoreDetailHeader = getActivity().findViewById(R.id.view_store_detail_header);
        linearLayoutHeaderStoreFront = getActivity().findViewById(R.id.header);
        linearLayoutHeaderCategory = getActivity().findViewById(R.id.header_browse_category);

        linearLayoutStoreDetailHeader.setVisibility(View.VISIBLE);
        relativeLayoutSearchBarStoreFront.setVisibility(View.GONE);
        linearLayoutHeaderCheckin.setVisibility(View.GONE);
        linearLayoutStoreReservation.setVisibility(View.GONE);
        linearLayoutHeaderStoreFront.setVisibility(View.GONE);
        linearLayoutHeaderCategory.setVisibility(View.GONE);
    }

    private void clickListeners() {
        imageViewBack.setOnClickListener(this);
    }


    private void eventDayClickListener(List<RESULTItem> result) {
        calendarView.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(EventDay eventDay) {
                resultItemList.clear();
                for (int i = 0; i < result.size(); i++) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(getTimeStampFromDate(result.get(i).getJsonMember116201()));
                    String eventTime = dateFormat.format(eventDay.getCalendar().getTime()) + "";
                    String apiTime = dateFormat.format(calendar.getTime()) + "";
                    if (eventTime.equals(apiTime)) {
                        resultItemList.add(result.get(i));
                    }
                }

                if (resultItemList.size() == 1) {
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.RESERVATION_ID, resultItemList.get(0).getJsonMember116200());
                    ((HomeActivity) requireContext()).displayView(new FragmentAddReservation(), Constants.TAG_ADD_RESERVATION, bundle);

                } else if (resultItemList.size() > 1) {
                    setAdapter(resultItemList);

                }

            }
        });


    }

    private void setAdapter(List<RESULTItem> resultItemList) {
        recyclerViewReservationList.setVisibility(View.VISIBLE);
        calendarView.setVisibility(View.GONE);

        ReservationListAdapter reservationListAdapter = new ReservationListAdapter(resultItemList, this);
        recyclerViewReservationList.setAdapter(reservationListAdapter);

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
                if (recyclerViewReservationList.getVisibility() == View.VISIBLE) {
                    calendarView.setVisibility(View.VISIBLE);
                    recyclerViewReservationList.setVisibility(View.GONE);

                } else
                    onBackPress();
                break;
        }
    }

    @Subscribe
    public void onEvent(final Event event) {
        switch (event.getKey()) {
            case Constants.GET_RESERVATION_SUCCESS:
                hideProgress();
                GetReservationResponse getReservationResponse = ModelManager.getInstance().getReservationManager().getReservationResponse;
                setEventsOnCalendar(getReservationResponse.getRESULT().get(0).getRESULT());
                eventDayClickListener(getReservationResponse.getRESULT().get(0).getRESULT());
                break;

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        linearLayoutHeaderCheckin.setVisibility(View.VISIBLE);
//        relativeLayoutSearchBarStoreFront.setVisibility(View.VISIBLE);
//        linearLayoutStoreDetailHeader.setVisibility(View.GONE);
//        linearLayoutHeaderStoreFront.setVisibility(View.VISIBLE);
//        linearLayoutHeaderCategory.setVisibility(View.VISIBLE);
    }


    //streaming code

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

    private void setEventsOnCalendar(List<RESULTItem> resultItem) {
        eventsList = new ArrayList();
        for (int i = 0; i < resultItem.size(); i++) {
            Calendar calendar = Calendar.getInstance();

            calendar.setTimeInMillis(getTimeStampFromDate(
                    resultItem.get(i).getJsonMember116201()));

            eventsList.add(new EventDay(calendar, DrawableUtils.getThreeDotsDrawableResId(), Color.RED));
        }
        calendarView.setEvents(eventsList);
    }

    private long getTimeStampFromDate(String str_date) {
        Date date = null;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date = formatter.parse(str_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();

    }

    @Override
    public void onItemClick(View v, String id) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.RESERVATION_ID, id);
        ((HomeActivity) requireContext()).displayView(new FragmentAddReservation(), Constants.TAG_ADD_RESERVATION, bundle);
    }
}
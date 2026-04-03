package com.apitap.views.fragments.reservations;


import static android.media.CamcorderProfile.get;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.apitap.App;
import com.apitap.R;
import com.apitap.controller.ReservationApi;
import com.apitap.controller.RetrofitClient;
import com.apitap.model.Constants;
import com.apitap.model.DrawableUtils;
import com.apitap.model.Utils;
import com.apitap.model.customclasses.Event;
import com.apitap.model.preferences.ATPreferences;
import com.apitap.model.reservation.AddEditReservationRequest;
import com.apitap.model.reservation.AddEditReservationResponse;
import com.apitap.model.reservation.NearbyStoreRequest;
import com.apitap.model.reservation.ViewReservationRequest;
import com.apitap.model.reservation.ViewReservationResponseItem;
import com.apitap.views.HomeActivity;
import com.apitap.views.fragments.BaseFragment;
import com.apitap.views.fragments.reservations.adapter.ReservationListAdapter;
import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentViewReservation extends BaseFragment implements View.OnClickListener, ReservationListAdapter.AdapterClick {

    private View rootView;
    private ImageView imageViewBack;
    private TextView textViewAddReservation;
    private LinearLayout linearLayoutHeaderCheckin;
    private LinearLayout linearLayoutStoreReservation;
    private RelativeLayout relativeLayoutSearchBarStoreFront;
    private LinearLayout linearLayoutStoreDetailHeader;
    private LinearLayout linearLayoutHeaderStoreFront;
    private LinearLayout linearLayoutHeaderCategory;

    private String selectedStartDate = "";
    private String selectedEndDate = "";
    private CalendarView calendarView;
    private RecyclerView recyclerViewReservationList;
    private ArrayList<EventDay> eventsList = new ArrayList<EventDay>();
    private List<ViewReservationResponseItem> resultItemList = new ArrayList<>();
    private List<ViewReservationResponseItem> initalList = new ArrayList<>();

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
        eventDayClickListener();
    }


    private void initViews() {
        imageViewBack = rootView.findViewById(R.id.imageViewBack);
        calendarView = rootView.findViewById(R.id.calendarView);
        textViewAddReservation = rootView.findViewById(R.id.textViewAddReservation);
        recyclerViewReservationList = rootView.findViewById(R.id.rv_events_list);

        linearLayoutHeaderCheckin = getActivity().findViewById(R.id.view_checkin);
        linearLayoutStoreReservation = getActivity().findViewById(R.id.view_store_reservation);

        relativeLayoutSearchBarStoreFront = getActivity().findViewById(R.id.search_storefront);
        linearLayoutStoreDetailHeader = getActivity().findViewById(R.id.view_store_detail_header);
        linearLayoutHeaderStoreFront = getActivity().findViewById(R.id.header);
        linearLayoutHeaderCategory = getActivity().findViewById(R.id.header_browse_category);


        if (getArguments() != null && getArguments().containsKey(Constants.FROM_MENU)) {
            linearLayoutStoreDetailHeader.setVisibility(View.GONE);
        } else {
            linearLayoutStoreDetailHeader.setVisibility(View.VISIBLE);
        }

        relativeLayoutSearchBarStoreFront.setVisibility(View.GONE);
        linearLayoutHeaderCheckin.setVisibility(View.GONE);
        linearLayoutStoreReservation.setVisibility(View.GONE);
        linearLayoutHeaderStoreFront.setVisibility(View.GONE);
        linearLayoutHeaderCategory.setVisibility(View.GONE);
    }

    private void clickListeners() {
        imageViewBack.setOnClickListener(this);
        textViewAddReservation.setOnClickListener(this);
    }


    private void eventDayClickListener() {
        calendarView.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(EventDay eventDay) {

                Calendar selectedCalendar = eventDay.getCalendar();

                SimpleDateFormat apiFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

                // Start Date
                selectedStartDate = apiFormat.format(selectedCalendar.getTime());

                // End Date = +1 Day
                Calendar endCalendar = (Calendar) selectedCalendar.clone();
                endCalendar.add(Calendar.DAY_OF_MONTH, 1);

                selectedEndDate = apiFormat.format(endCalendar.getTime());

                callReservationApi(selectedStartDate, selectedStartDate);

            }
        });


    }

    private void loadCurrentMonthReservations() {

        Calendar calendar = Calendar.getInstance();

        // First day of month
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date firstDay = calendar.getTime();

        // Last day of month
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date lastDay = calendar.getTime();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        String startDate = sdf.format(firstDay);
        String endDate = sdf.format(lastDay);

        callReservationApi(startDate, endDate);
    }

    private void callReservationApi(String startDate, String endDate) {

        showProgress();

        ViewReservationRequest request = new ViewReservationRequest();
        request.setStartDate(startDate);
        request.setEndDate(endDate);
        request.setPage(0);
        request.setUserCreatorId("0");
        request.setSort("tbl_reservation_date asc;tbl_reservation_startHour asc;");

        ReservationApi api = RetrofitClient.getClient().create(ReservationApi.class);

        Call<List<ViewReservationResponseItem>> call = api.getReservations(request);

        call.enqueue(new Callback<List<ViewReservationResponseItem>>() {
            @Override
            public void onResponse(Call<List<ViewReservationResponseItem>> call,
                                   Response<List<ViewReservationResponseItem>> response) {

                hideProgress();

                if (response.isSuccessful() && response.body() != null) {

                    if (!selectedStartDate.isEmpty()) {
                        resultItemList = response.body();

                     /*   if (resultItemList.size() == 1) {
                            Bundle bundle = new Bundle();
                            bundle.putSerializable(Constants.RESERVATION_DATA, resultItemList.get(0));
                            ((HomeActivity) requireContext()).displayView(new FragmentAddReservation(), Constants.TAG_ADD_RESERVATION, bundle);
                            setEventsOnCalendar(resultItemList);
                        } else*/
                        if (!resultItemList.isEmpty()) {
                            setAdapter(resultItemList);
                            // setEventsOnCalendar(resultItemList);
                        } else {
                            baseshowFeedbackMessage(requireActivity(), rootView, "No reservation on selected date.");
                        }
                    } else {
                        if (!response.body().isEmpty()) {
                            initalList = response.body();
                            setEventsOnCalendar(initalList);
                        }
                    }


                } else {
                    Log.e("API", "Error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<ViewReservationResponseItem>> call, Throwable t) {
                hideProgress();
                Log.e("API", "Failure: " + t.getMessage());
            }
        });
    }


    private void cancelReservation(ViewReservationResponseItem data) {

        showProgress();

        AddEditReservationRequest request = new AddEditReservationRequest();
        request.setReservationId(data.getReservationId());
        request.setReservationDate(data.getReservationDate());
        request.setStartHour(data.getStartHour());
        request.setConsumerName(data.getConsumerName());
        request.setPeopleQty(data.getPeopleQty());
        request.setLocationId(data.getLocationId());
        request.setAreaId(data.getAreaId());
        request.setCompanyId(String.valueOf(data.getCompanyId()));
        request.setConsumerEmail(data.getConsumerEmail());
        request.setConsumerPhone(data.getConsumerPhone());
        request.setNote(data.getNote());
        request.setStatusId(5);

        ReservationApi api = RetrofitClient.getClient().create(ReservationApi.class);

        Call<AddEditReservationResponse> call = api.addEditReservation(request);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<AddEditReservationResponse> call, Response<AddEditReservationResponse> response) {

                hideProgress();

                if (response.isSuccessful()) {
                    baseshowFeedbackMessage(requireActivity(), rootView, "Reservation cancelled successfully.");

                    callReservationApi(selectedStartDate, selectedEndDate);
                }

            }

            @Override
            public void onFailure(Call<AddEditReservationResponse> call, Throwable t) {

                hideProgress();
                baseshowFeedbackMessage(requireActivity(), rootView, "API Failed: " + t.getMessage());

            }
        });
    }

    private void setAdapter(List<ViewReservationResponseItem> resultItemList) {
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
        loadCurrentMonthReservations();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textViewAddReservation:
                Bundle bundle = new Bundle();

                if (getArguments() != null && getArguments().containsKey(Constants.FROM_MENU)) {
                    bundle.putBoolean(Constants.FROM_MENU, getArguments().getBoolean(Constants.FROM_MENU));
                }

                ((HomeActivity) requireContext()).displayView(new FragmentAddReservation(), Constants.TAG_ADD_RESERVATION, bundle);
                break;
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
//                GetReservationResponse getReservationResponse = ModelManager.getInstance().getReservationManager().getReservationResponse;
//                setEventsOnCalendar(getReservationResponse.getRESULT().get(0).getRESULT());
//                eventDayClickListener(getReservationResponse.getRESULT().get(0).getRESULT());
                break;

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        selectedStartDate = "";
        selectedEndDate = "";
        recyclerViewReservationList.setVisibility(View.GONE);
        calendarView.setVisibility(View.VISIBLE);
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

    private void setEventsOnCalendar(List<ViewReservationResponseItem> resultItem) {
        eventsList = new ArrayList();
        for (int i = 0; i < resultItem.size(); i++) {
            Calendar calendar = Calendar.getInstance();

            calendar.setTimeInMillis(getTimeStampFromDate(
                    resultItem.get(i).getReservationDate()));

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
    public void onItemClick(ViewReservationResponseItem responseItem) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.RESERVATION_DATA, responseItem);
        ((HomeActivity) requireContext()).displayView(new FragmentAddReservation(), Constants.TAG_ADD_RESERVATION, bundle);
    }

    @Override
    public void onCancelClick(ViewReservationResponseItem id) {
        cancelReservation(id);
    }


}
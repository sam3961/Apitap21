package com.apitap.app.views.fragments.reservations;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.apitap.app.App;
import com.apitap.app.R;
import com.apitap.app.controller.ModelManager;
import com.apitap.app.controller.ReservationApi;
import com.apitap.app.controller.RetrofitClient;
import com.apitap.app.model.Constants;
import com.apitap.app.model.Operations;
import com.apitap.app.model.Triple;
import com.apitap.app.model.Utils;
import com.apitap.app.model.address.ADItem;
import com.apitap.app.model.address.GetAddressResponse;
import com.apitap.app.model.assignedToUser.AssignUserLocationResponse;
import com.apitap.app.model.bean.LocationListBean;
import com.apitap.app.model.customclasses.Event;
import com.apitap.app.model.getReservation.GetReservationResponse;
import com.apitap.app.model.preferences.ATPreferences;
import com.apitap.app.model.promoByLocation.PromoByLocationResponse;
import com.apitap.app.model.reservation.AddEditReservationRequest;
import com.apitap.app.model.reservation.AddEditReservationResponse;
import com.apitap.app.model.reservation.MEItem;
import com.apitap.app.model.reservation.MerchantByCatResponse;
import com.apitap.app.model.reservation.NearbyStoreRequest;
import com.apitap.app.model.reservation.ReservationLocationResponse;
import com.apitap.app.model.reservation.StoreItem;
import com.apitap.app.model.reservation.ViewReservationResponseItem;
import com.apitap.app.model.seatingAreaByLocation.SeatingAreaLocationResponse;
import com.apitap.app.model.tablesBySeatingArea.TablesBySeatingAreaResponse;
import com.apitap.app.views.HomeActivity;
import com.apitap.app.views.fragments.BaseFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentAddReservation extends BaseFragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private View rootView;
    private ImageView imageViewBack;
    private CheckBox checkBoxWheelChairAccess, checkBoxChildSeating;
    private LinearLayout linearLayoutHeaderCheckin;
    private LinearLayout linearLayoutStoreReservation;
    private RelativeLayout relativeLayoutSearchBarStoreFront;
    private LinearLayout linearLayoutStoreDetailHeader;
    private LinearLayout linearLayoutHeaderStoreFront;
    private LinearLayout linearLayoutArea;
    private LinearLayout linearLayoutHeaderCategory;
    private RelativeLayout relativeLayoutSave;
    private Spinner spinnerDining, spinnerArea, spinnerTable, spinnerAssign, spinnerPromo, spinnerLocation, spinnerSmokingArea, spinnerStore;
    private EditText editTextStartDate, editTextStartTime, editTextEndTime, editTextNumberOfPeople;
    private EditText editTextNumberOfChildren, editTextName, editTextPhone, editTextEmail, editTextSecondEmail;
    private EditText editTextSpecialRequest, editTextNotes, editTextCustomerHistory;
    private Switch switchNearMe;
    private LinearLayout linearLayoutNearMe;
    private LinearLayout linearLayoutStore;
    private View viewStoreDivider, viewKeyboard;
    private TextView textViewAreaLabel, textViewStore;
    private String reservationId = "";
    private ViewReservationResponseItem responseItem = null;
    private String selectedLocationId = "";
    private String selectedSeatingAreaId = "";
    private String selectedTableId = "";
    private String selectedPromoId = "";
    private String selectedAssignId = "";
    private String storeId = "";
    private String merchantId = "";
    private String categoryId = "726";
    private Boolean smokingFlag = null;
    private Boolean fromMenu = false;
    private Boolean primaryAddressChecked = false;
    private Boolean gpsAddressChecked = false;

    private String[] selectedDiningMethodArray;
    private ArrayList<StoreItem> fullStoreList = new ArrayList<>();
    private ArrayList<StoreItem> filteredStoreList = new ArrayList<>();
    private ArrayList<String> locationIdList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_add_reservation, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getArguments() != null && getArguments().containsKey(Constants.FROM_MENU)) {
            fromMenu = getArguments().getBoolean(Constants.FROM_MENU);
        }

        selectedDiningMethodArray = getResources().getStringArray(R.array.dining_method_id);
        initViews();
        clickListeners();


        if (getArguments() != null && getArguments().containsKey(Constants.RESERVATION_DATA)) {
            responseItem = (ViewReservationResponseItem)
                    getArguments().getSerializable(Constants.RESERVATION_DATA);

            reservationId = String.valueOf(responseItem.getId());
            setData(responseItem);

        }


        if (reservationId.isEmpty()) {
            showProgress();
            if (fromMenu) {
                textViewStore.setVisibility(View.GONE);
                spinnerStore.setVisibility(View.VISIBLE);
                ModelManager.getInstance().getReservationManager().addReservationDetails(getActivity(),
                        Operations.makeJsonGetMerchantByCategory(requireActivity(),
                                categoryId),
                        Constants.TAG_MERCHANT_BY_CATEGORY);
            } else {
                textViewStore.setVisibility(View.VISIBLE);
                linearLayoutStore.setBackgroundResource(R.drawable.back_round_grey_border);
                textViewStore.setText(ATPreferences.readString(requireContext(), Constants.STORE_NAME));
                spinnerStore.setVisibility(View.GONE);

                callStoreLocation(ATPreferences.getInt(requireContext(), Constants.STORE_ID, 0).toString());

              /*  ModelManager.getInstance().getMerchantManager().getMerchantDistance(requireContext(),
                        Operations.makeJsonGetMerchantDistance(requireContext(),
                                ATPreferences.readString(requireContext(), Constants.MERCHANT_ID),
                                String.valueOf(App.latitude),
                                String.valueOf(App.longitude)), Constants.GET_MERCHANT_DISTANCE_SUCCESS);
*/

            }
        } else {
            showProgress();
            linearLayoutStore.setBackgroundResource(R.drawable.back_round_grey_border);
            textViewStore.setVisibility(View.VISIBLE);
            spinnerStore.setVisibility(View.GONE);
            linearLayoutNearMe.setVisibility(View.GONE);
            viewStoreDivider.setVisibility(View.VISIBLE);

            ModelManager.getInstance().getReservationManager().addReservationDetails(getActivity(),
                    Operations.makeJsonGetReservationById(requireActivity(),
                            reservationId),
                    Constants.TAG_GET_RESERVATION);

//            disableViews();

        }

        if (ATPreferences.readBoolean(requireActivity(), Constants.HEADER_STORE)) {
            linearLayoutNearMe.setVisibility(View.GONE);
        }

        ModelManager.getInstance().getReservationManager().getAddress(requireActivity(),
                Operations.makeJsonGetSavedAddress(requireActivity()));


        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            private boolean isKeyboardVisible = false;

            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                rootView.getWindowVisibleDisplayFrame(rect);

                int screenHeight = rootView.getRootView().getHeight();
                int keypadHeight = screenHeight - rect.bottom;

                // Threshold: if more than 15% of screen, keyboard is open
                boolean isNowVisible = keypadHeight > screenHeight * 0.15;

                if (isNowVisible != isKeyboardVisible) {
                    isKeyboardVisible = isNowVisible;

                    if (isKeyboardVisible) {
                        // Keyboard OPEN → Show view
                        viewKeyboard.setVisibility(View.VISIBLE);
                    } else {
                        // Keyboard CLOSED → Hide view
                        viewKeyboard.setVisibility(View.GONE);
                    }
                }
            }
        });
    }


    private void initViews() {
        imageViewBack = rootView.findViewById(R.id.imageViewBack);

        relativeLayoutSave = rootView.findViewById(R.id.relativeLayoutSave);
        linearLayoutArea = rootView.findViewById(R.id.linearLayoutArea);

        checkBoxWheelChairAccess = rootView.findViewById(R.id.checkBoxWheelChairAccess);
        checkBoxChildSeating = rootView.findViewById(R.id.checkBoxChildSeating);

        spinnerDining = rootView.findViewById(R.id.spinnerDining);
        spinnerStore = rootView.findViewById(R.id.spinnerStore);
        spinnerArea = rootView.findViewById(R.id.spinnerArea);
        spinnerTable = rootView.findViewById(R.id.spinnerTable);
        spinnerAssign = rootView.findViewById(R.id.spinnerAssign);
        spinnerPromo = rootView.findViewById(R.id.spinnerPromo);
        spinnerLocation = rootView.findViewById(R.id.spinnerLocation);
        spinnerSmokingArea = rootView.findViewById(R.id.spinnerSmokingArea);

        editTextNumberOfPeople = rootView.findViewById(R.id.editTextNumberOfPeople);
        editTextNumberOfChildren = rootView.findViewById(R.id.editTextNumberOfChildren);
        editTextName = rootView.findViewById(R.id.editTextName);
        editTextPhone = rootView.findViewById(R.id.editTextPhone);
        editTextEmail = rootView.findViewById(R.id.editTextEmail);
        editTextSecondEmail = rootView.findViewById(R.id.editTextSecondEmail);
        editTextSpecialRequest = rootView.findViewById(R.id.editTextSpecialRequest);
        editTextNotes = rootView.findViewById(R.id.editTextNotes);
        editTextCustomerHistory = rootView.findViewById(R.id.editTextCustomerHistory);

        editTextStartDate = rootView.findViewById(R.id.editTextStartDate);
        editTextStartTime = rootView.findViewById(R.id.editTextStartTime);
        editTextEndTime = rootView.findViewById(R.id.editTextEndTime);

        textViewAreaLabel = rootView.findViewById(R.id.textViewAreaLabel);
        textViewStore = rootView.findViewById(R.id.textViewStore);
        switchNearMe = rootView.findViewById(R.id.switchNearMe);
        linearLayoutNearMe = rootView.findViewById(R.id.linearLayoutNearMe);
        linearLayoutStore = rootView.findViewById(R.id.linearLayoutStore);
        viewStoreDivider = rootView.findViewById(R.id.viewStoreDivider);
        viewKeyboard = rootView.findViewById(R.id.viewKeyboard);


        linearLayoutHeaderCheckin = getActivity().findViewById(R.id.view_checkin);
        linearLayoutStoreReservation = getActivity().findViewById(R.id.view_store_reservation);

        relativeLayoutSearchBarStoreFront = getActivity().findViewById(R.id.search_storefront);
        linearLayoutStoreDetailHeader = getActivity().findViewById(R.id.view_store_detail_header);
        linearLayoutHeaderStoreFront = getActivity().findViewById(R.id.header);
        linearLayoutHeaderCategory = getActivity().findViewById(R.id.header_browse_category);


        if (fromMenu) {
            linearLayoutStoreDetailHeader.setVisibility(View.GONE);
        }
        if (!ATPreferences.readBoolean(requireActivity(), Constants.HEADER_STORE)) {
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
        relativeLayoutSave.setOnClickListener(this);
        editTextStartDate.setOnClickListener(this);
        editTextStartTime.setOnClickListener(this);
        editTextEndTime.setOnClickListener(this);

        switchNearMe.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
//                callStoreNearMe(); // filter

                showNearMeBottomSheet();
            } else {
                // restore full list
                setStoreAdapter(fullStoreList, false);
            }
        });
    }

    private void callAddReservationAPI() {

        showProgress();

        smokingFlag = spinnerSmokingArea.getSelectedItemPosition() == 0;

        AddEditReservationRequest request = new AddEditReservationRequest();
        if (!reservationId.isEmpty() && !reservationId.equals("0")) {
            request.setReservationId(Integer.parseInt(reservationId));
        } else {
            request.setReservationId(null); // ✅ now possible
        }
        request.setReservationDate(Utils.getEditTextString(editTextStartDate));
        request.setStartHour(Utils.getEditTextStringWithoutSeconds(editTextStartTime));
        request.setConsumerName(Utils.getEditTextString(editTextName));
        request.setPeopleQty(Utils.getEditTextInt(editTextNumberOfPeople));
        request.setLocationId(Integer.parseInt(selectedLocationId));
        request.setAreaId(Integer.parseInt(selectedSeatingAreaId));
        request.setCompanyId(String.valueOf(ATPreferences.getInt(getActivity(), Constants.STORE_ID, 0)));
        request.setConsumerEmail(Utils.getEditTextString(editTextEmail));
        request.setConsumerPhone(Utils.getEditTextString(editTextPhone));
        request.setNote(Utils.getEditTextString(editTextNotes));


        ReservationApi api = RetrofitClient.getClient().create(ReservationApi.class);

        Call<AddEditReservationResponse> call = api.addEditReservation(request);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<AddEditReservationResponse> call, Response<AddEditReservationResponse> response) {

                hideProgress();

                if (response.isSuccessful()) {

                    if (reservationId.isEmpty())
                        baseshowFeedbackMessage(requireActivity(), rootView, "Reservation added successfully.");
                    else
                        baseshowFeedbackMessage(requireActivity(), rootView, "Reservation updated successfully.");

                    new Handler(Looper.getMainLooper()).postDelayed(() -> {
                        ((HomeActivity) requireContext()).displayViewReplace(
                                new FragmentViewReservation(),
                                Constants.TAG_VIEW_RESERVATION,
                                new Bundle());
                    }, 1000);

                }

            }

            @Override
            public void onFailure(Call<AddEditReservationResponse> call, Throwable t) {

                hideProgress();
                baseshowFeedbackMessage(requireActivity(), rootView, "API Failed: " + t.getMessage());

            }
        });
    }

    private void callStoreNearMe(double latitude, double longitude) {
        showProgress();

        NearbyStoreRequest request = new NearbyStoreRequest();
//        request.setLatitude(27.9882458);
//        request.setLongitude(-81.6917568);

        request.setLatitude(latitude);
        request.setLongitude(longitude);

        ReservationApi api = RetrofitClient.getClient().create(ReservationApi.class);
        Call<List<Integer>> call = api.getAllMerchantStoresByPointFilteredByCategory(request);

        call.enqueue(new Callback<List<Integer>>() {
            @Override
            public void onResponse(Call<List<Integer>> call, Response<List<Integer>> response) {
                hideProgress();

                if (response.isSuccessful() && response.body() != null) {

                    List<Integer> nearIds = response.body();

                    if (nearIds.isEmpty()) {
                        Utils.baseshowFeedbackMessage(requireActivity(), rootView, "No nearby store found.");
                        switchNearMe.setChecked(false);
                        gpsAddressChecked = false;
                        primaryAddressChecked = false;
                    } else {
                        filteredStoreList.clear();
                        filteredStoreList.add(new StoreItem("", "Select Store", ""));

                        Set<String> addedIds = new HashSet<>();

                        for (StoreItem item : fullStoreList) {
                            if (item.getId() != null && !item.getId().isEmpty()) {

                                if (!addedIds.contains(item.getId()) &&
                                        nearIds.contains(Integer.parseInt(item.getId()))) {

                                    filteredStoreList.add(item);
                                    addedIds.add(item.getId());
                                }
                            }
                        }
                        if (filteredStoreList.size() > 0)
                            storeId = filteredStoreList.get(0).getId();

                        setStoreAdapter(filteredStoreList, true);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Integer>> call, Throwable t) {
                hideProgress();
                t.printStackTrace();
            }
        });
    }

    private void callStoreLocation(String categoryId) {
        showProgress();

        ReservationApi api = RetrofitClient.getClient().create(ReservationApi.class);
        Call<List<ReservationLocationResponse>> call = api.getReservationLocation(Integer.parseInt(categoryId));

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<ReservationLocationResponse>> call, Response<List<ReservationLocationResponse>> response) {
                hideProgress();

                if (response.isSuccessful() && response.body() != null) {
                    Log.d("TAGs", "onResponse: " + response.body());

                    hideProgress();

                    ArrayList<String> locationList = new ArrayList<>();
                    locationIdList.clear(); // 🔥 clear old

                    locationList.add("Select Location");
                    locationIdList.add(""); // index 0 match

                    for (int i = 0; i < response.body().size(); i++) {

                        ReservationLocationResponse item = response.body().get(i);

                        String address = item.getName();
                        String id = item.getId().toString();

                        locationList.add(address);
                        locationIdList.add(id);

                    }
                    setLocationListAdapter(locationList);
                }
            }

            @Override
            public void onFailure(Call<List<ReservationLocationResponse>> call, Throwable t) {
                hideProgress();
                t.printStackTrace();
            }
        });
    }



/*
    private void callAddReservationAPI() {
        showProgress();

        smokingFlag = spinnerSmokingArea.getSelectedItemPosition() == 0;

        ModelManager.getInstance().getReservationManager().addReservationDetails(getActivity(), Operations.
                makeAddEditReservation(getActivity(),
                        reservationId,
                        Utils.getEditTextString(editTextStartDate),
                        Utils.getEditTextStringWithSeconds(editTextStartTime),
                        Utils.getEditTextStringWithSeconds(editTextEndTime),
                        Utils.getEditTextString(editTextNumberOfPeople),
                        //Utils.getEditTextString(editTextNumberOfChildren),String.valueOf(checkBoxWheelChairAccess.isChecked()),
                        //String.valueOf(checkBoxChildSeating.isChecked()), selectedTableId,
                        // selectedDiningMethodArray[spinnerDining.getSelectedItemPosition()],
                        Utils.getEditTextString(editTextName),
                        //Utils.getEditTextString(editTextPhone),Utils.getEditTextString(editTextEmail),Utils.getEditTextString(editTextSecondEmail),
                        //Utils.getEditTextString(editTextSpecialRequest),
                        Utils.getEditTextString(editTextNotes),
                        //  selectedAssignId,
                        selectedSeatingAreaId,
                        //selectedPromoId,
                        selectedLocationId,
                        smokingFlag.toString()

                ), Constants.TAG_ADD_RESERVATION);
    }

*/


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
            case R.id.editTextStartTime:
                showTime24HourPicker("Select Start Time", true);
                break;
            case R.id.editTextEndTime:
                showTimePicker("Select End Time", false);
                break;

            case R.id.editTextStartDate:
                showDatePicker();
                break;

            case R.id.relativeLayoutSave:

                if (dataValid()) {
                    callAddReservationAPI();
                }
                break;
            case R.id.imageViewBack:
                linearLayoutStoreDetailHeader.setVisibility(View.GONE);
                onBackPress();
                break;
        }
    }


    @Subscribe
    public void onEvent(final Event event) {
        switch (event.getKey()) {
            case -1:
                hideProgress();
                Utils.baseshowFeedbackMessage(requireActivity(), rootView, "Something went wrong.");
                break;
            case Constants.GET_ADDRESS_SUCCESS:

                break;
            case Constants.GET_MERCHANT_BY_CATEGORY_SUCCESS: //store listing
                hideProgress();

                ArrayList<StoreItem> storeList = new ArrayList<>();
                storeList.add(new StoreItem("", "Select Store", ""));

                MerchantByCatResponse response =
                        ModelManager.getInstance().getReservationManager().merchantByCatResponse;

                if (response != null &&
                        response.getRESULT() != null &&
                        !response.getRESULT().isEmpty()) {

                    for (int i = 0; i < response.getRESULT().get(0).getRESULT().size(); i++) {

                        List<MEItem> meList =
                                response.getRESULT().get(0)
                                        .getRESULT().get(i)
                                        .getME();

                        if (meList != null) {
                            for (int j = 0; j < meList.size(); j++) {

                                MEItem item = meList.get(j);

                                String hexName = item.getJsonMember11470(); // name
                                String id = item.getJsonMember1141();       // id ⚠️ make sure this exists

                                if (hexName != null && !hexName.isEmpty()) {
                                    storeList.add(new StoreItem(
                                            id,
                                            Utils.hexToASCII(hexName),
                                            item.getJsonMember53()
                                    ));
                                }
                            }
                        }
                    }

                    fullStoreList.clear();
                    fullStoreList.addAll(storeList);

// default show all
                    setStoreAdapter(fullStoreList, false);


                    if (fullStoreList.size() > 1) {
                        int positionOfLocationByStore = spinnerStore.getSelectedItemPosition();

                        StoreItem selectedItem = fullStoreList.get(positionOfLocationByStore);
                        if (!selectedItem.getId().isEmpty())
                            callStoreLocation(selectedItem.getId());
//                        showProgress();
                       /* ModelManager.getInstance().getMerchantManager().getMerchantDistance(
                                requireContext(),
                                Operations.makeJsonGetMerchantDistance(
                                        requireContext(),
                                        selectedItem.getMerchantId(),
                                        String.valueOf(App.latitude),
                                        String.valueOf(App.longitude)
                                ),
                                Constants.GET_MERCHANT_DISTANCE_SUCCESS
                        );*/
            /*            int positionOfLocationByStore = spinnerStore.getSelectedItemPosition();
                        if (switchNearMe.isChecked()) {
                            ModelManager.getInstance().getMerchantManager().getMerchantDistance(requireContext(),
                                    Operations.makeJsonGetMerchantDistance(requireContext(),
                                            fullStoreList.get(positionOfLocationByStore).getMerchantId(),
                                            String.valueOf(App.latitude),
                                            String.valueOf(App.longitude)), Constants.GET_MERCHANT_DISTANCE_SUCCESS);
                        }else{
                            ModelManager.getInstance().getMerchantManager().getMerchantDistance(requireContext(),
                                    Operations.makeJsonGetMerchantDistance(requireContext(),
                                    filteredStoreList.get(positionOfLocationByStore).getMerchantId(),
                                    String.valueOf(App.latitude),
                                    String.valueOf(App.longitude)), Constants.GET_MERCHANT_DISTANCE_SUCCESS);
                        }*/
                    }
                }

                break;
            case Constants.GET_RESERVATION_SUCCESS:
//                hideProgress();
                GetReservationResponse getReservationResponse = ModelManager.getInstance().getReservationManager().getReservationResponse;

                ModelManager.getInstance().getReservationManager().addReservationDetails(getActivity(),
                        Operations.makeJsonGetMerchantByCategory(requireActivity(),
                                categoryId),
                        Constants.TAG_MERCHANT_BY_CATEGORY);

                break;
            case Constants.ADD_RESERVATION_SUCCESS:
                hideProgress();
                if (reservationId.isEmpty())
                    baseshowFeedbackMessage(requireActivity(), rootView, "Reservation added successfully.");
                else
                    baseshowFeedbackMessage(requireActivity(), rootView, "Reservation updated successfully.");

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ((HomeActivity) requireContext()).displayViewReplace(new FragmentViewReservation(), Constants.TAG_VIEW_RESERVATION, new Bundle());
                    }
                }, 1000);
                break;
            case Constants.GET_ASSIGNED_TO_USER_SUCCESS:
                hideProgress();

                ArrayList<String> assignToUserList = new ArrayList<>();
                assignToUserList.add("Assign someone to this");
                AssignUserLocationResponse assignUserLocationResponse = ModelManager.getInstance().getReservationManager().assignUserLocationResponse;
                for (int i = 0; i < assignUserLocationResponse.getRESULT().get(0).getRESULT().size(); i++) {
                    assignToUserList.add(
                            Utils.hexToASCII(assignUserLocationResponse.getRESULT().get(0).getRESULT().get(i).getJsonMember1143())
                                    + " " +
                                    Utils.hexToASCII(assignUserLocationResponse.getRESULT().get(0).getRESULT().get(i).getJsonMember1144())
                                    + " " +
                                    Utils.hexToASCII(assignUserLocationResponse.getRESULT().get(0).getRESULT().get(i).getJsonMember1145()));
                }

                setUserAssignAdapter(assignToUserList);


                break;

            case Constants.GET_PROMO_BY_LOCATION_SUCCESS:
//                hideProgress();

                ModelManager.getInstance().getReservationManager().addReservationDetails(getActivity(),
                        Operations.makeJsonGetAssigns(requireActivity(),
                                ATPreferences.readString(requireContext(), Constants.MERCHANT_ID)),
                        Constants.TAG_ASSIGN_USER_BY_LOCATION);

                ArrayList<String> promoList = new ArrayList<>();
                promoList.add("Related Promo");
                PromoByLocationResponse promoByLocationResponse = ModelManager.getInstance().getReservationManager().promoByLocationResponse;
                for (int i = 0; i < promoByLocationResponse.getRESULT().get(0).getRESULT().size(); i++) {
                    promoList.add(
                            Utils.hexToASCII(promoByLocationResponse.getRESULT().get(0).getRESULT().get(i).getJsonMember12083())
                                    + " (" +
                                    Utils.hexToASCII(promoByLocationResponse.getRESULT().get(0).getRESULT().get(i).getJsonMember120157())
                                    + ")"
                    );
                }

                setPromoByLocationAdapter(promoList);

                break;

            case Constants.GET_SEATING_AREA_LOCATION_SUCCESS:
                hideProgress();
                ArrayList<String> seatingAreaLocationList = new ArrayList<>();
                seatingAreaLocationList.add("Select Area");
                SeatingAreaLocationResponse seatingAreaLocationResponse = ModelManager.getInstance().getReservationManager().seatingAreaLocationResponse;
                for (int i = 0; i < seatingAreaLocationResponse.getRESULT().get(0).getRESULT().size(); i++) {
                    seatingAreaLocationList.add(
                            Utils.hexToASCII(seatingAreaLocationResponse.getRESULT().get(0).getRESULT().get(i).getJsonMember11512()));
                }

               /* if (seatingAreaLocationList.size() == 1) {
                    textViewAreaLabel.setVisibility(View.GONE);
                    linearLayoutArea.setVisibility(View.GONE);
                } else {
                    textViewAreaLabel.setVisibility(View.VISIBLE);
                    linearLayoutArea.setVisibility(View.VISIBLE);
                }*/

                setAreaListAdapter(seatingAreaLocationList);


                break;

            case Constants.GET_TABLES_BY_SEATING_AREA_SUCCESS:
                hideProgress();
                ArrayList<String> tablesList = new ArrayList<>();
                tablesList.add("Select Table");
                TablesBySeatingAreaResponse tablesBySeatingAreaResponse = ModelManager.getInstance().getReservationManager().tablesBySeatingAreaResponse;
                for (int i = 0; i < tablesBySeatingAreaResponse.getRESULT().get(0).getRESULT().size(); i++) {
                    tablesList.add(
                            Utils.hexToASCII(tablesBySeatingAreaResponse.getRESULT().get(0).getRESULT().get(i).getJsonMember11522()));
                }
                setTableListAdapter(tablesList);

                if (!selectedPromoId.isEmpty()) {
                    ModelManager.getInstance().getReservationManager().addReservationDetails(getActivity(),
                            Operations.makeJsonGetPromos(requireActivity(),
                                    ATPreferences.readString(requireContext(), Constants.MERCHANT_ID)
                            ),
                            Constants.TAG_PROMO_BY_LOCATION);
                }

                break;

            case Constants.GET_MERCHANT_DISTANCE_SUCCESS:
                hideProgress();

                ArrayList<String> locationList = new ArrayList<>();
                locationIdList.clear(); // 🔥 clear old

                locationList.add("Select Location");
                locationIdList.add(""); // index 0 match

                List<com.apitap.app.model.Triple<String, String, Double>> tempList = new ArrayList<>();

                LocationListBean locationListBean =
                        ModelManager.getInstance().getMerchantManager().locationListBean;

                if (locationListBean.getRESULT().get(0).getRESULT().size() == 1 &&
                        locationListBean.getRESULT().get(0).getRESULT().get(0).get478() == null) {

                   /* baseshowFeedbackMessage(requireActivity(), rootView,
                            "No locations found for this store.");*/

                } else {

                    // 🔥 temp list to hold address + distance
                    tempList = new ArrayList<>();

                    double myLatitude = App.latitude;
                    double myLongitude = App.longitude;

/*                    double myLatitude = 27.9882458;
                    double myLongitude = -81.6917568;*/

                    for (int i = 0; i < locationListBean.getRESULT().get(0).getRESULT().size(); i++) {

                        LocationListBean.RESULT_ item =
                                locationListBean.getRESULT().get(0).getRESULT().get(i);

                        String address = item.get11470();
                        String id = item.get11447();

                        LocationListBean.AD ad = item.getAD();

                        if (ad == null || ad.get12038() == null || ad.get12039() == null)
                            continue;

                        try {
                            double apiLat = Double.parseDouble(ad.get12038());
                            double apiLong = Double.parseDouble(ad.get12039());

                            float[] results = new float[1];

                            android.location.Location.distanceBetween(
                                    myLatitude, myLongitude,
                                    apiLat, apiLong,
                                    results
                            );

                            double distanceKm = results[0] / 1000;

                            // 🔥 store address + id + distance
                            tempList.add(new Triple<>(address, id, distanceKm));

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    // 🔥 SORT by distance
                    Collections.sort(tempList, Comparator.comparingDouble(t -> t.third));

                    // 🔥 add sorted addresses to final list
                    for (com.apitap.app.model.Triple<String, String, Double> t : tempList) {
                        locationList.add(t.first);
                        locationIdList.add(t.second);
                    }
                }

                setLocationListAdapter(locationList);
                break;
        }
    }

    private void setData(ViewReservationResponseItem result) {
        editTextStartDate.setText(result.getReservationDate());
        editTextStartTime.setText(result.getStartHour());
        textViewStore.setText(result.getCompanyName());
//        editTextEndTime.setText(Utils.removeSecondsFromTime(result.getJsonMember116203()));
        editTextNumberOfPeople.setText(result.getPeopleQty() + "");
//        editTextNumberOfChildren.setText(result.getJsonMember116205());
        editTextEmail.setText(result.getConsumerEmail());
//        editTextSecondEmail.setText(result.getJsonMember11451());
        editTextPhone.setText(result.getConsumerPhone());

        editTextName.setText(result.getConsumerName());
//        editTextSpecialRequest.setText(Utils.hexToASCII(result.getNote()));
        editTextNotes.setText(result.getNote());

//        checkBoxWheelChairAccess.setChecked(Boolean.parseBoolean(result.getJsonMember116206()));
//        checkBoxChildSeating.setChecked(Boolean.parseBoolean(result.getJsonMember116207()));

/*
        for (int i = 0; i < selectedDiningMethodArray.length; i++) {
            if (selectedDiningMethodArray[i].equals(result.getJsonMember11571())) {
                spinnerDining.setSelection(i);
                break;
            }
        }
*/

//        selectedAssignId = result.getJsonMember114179();
        //selectedAssignId = "124";
//        selectedPromoId = result.getJsonMember114144();
        selectedSeatingAreaId = String.valueOf(result.getAreaId());
//        selectedTableId = result.getJsonMember11521();
        selectedLocationId = String.valueOf(result.getLocationId());
        storeId = String.valueOf(result.getCompanyId());

/*        if (result.getJsonMember12772() != null) {
            smokingFlag = Boolean.valueOf(result.getJsonMember12772());

            if (smokingFlag)
                spinnerSmokingArea.setSelection(1);
            else
                spinnerSmokingArea.setSelection(2);

        }*/
    }

    private void setStoreAdapter(ArrayList<StoreItem> storeList, boolean fromNearby) {

        ArrayAdapter<StoreItem> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                storeList
        );

        adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        spinnerStore.setAdapter(adapter);

        // ✅ Pre-select
        if (storeId != null && !storeId.isEmpty()) {
            for (int i = 0; i < storeList.size(); i++) {
                if (storeList.get(i).getId().equals(storeId)) {
                    spinnerStore.setSelection(i);
                    break;
                }
            }
        }

        // ✅ Selection listener
        spinnerStore.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                StoreItem selectedItem = storeList.get(position);

                if (fromNearby) {
                    storeId = selectedItem.getId(); // ✅ correct mapping
                    merchantId = selectedItem.getMerchantId(); // ✅ correct mapping
                    showProgress();

                /*    ModelManager.getInstance().getMerchantManager().getMerchantDistance(requireContext(),
                            Operations.makeJsonGetMerchantDistance(requireContext(),
                                    merchantId,
                                    String.valueOf(App.latitude),
                                    String.valueOf(App.longitude)), Constants.GET_MERCHANT_DISTANCE_SUCCESS);*/
                    callStoreLocation(storeId);

                } else {
                    if (position != 0) {
                        storeId = selectedItem.getId(); // ✅ correct mapping
                        merchantId = selectedItem.getMerchantId(); // ✅ correct mapping
                        showProgress();

                       /* ModelManager.getInstance().getMerchantManager().getMerchantDistance(requireContext(),
                                Operations.makeJsonGetMerchantDistance(requireContext(),
                                        merchantId,
                                        String.valueOf(App.latitude),
                                        String.valueOf(App.longitude)), Constants.GET_MERCHANT_DISTANCE_SUCCESS);*/
                        callStoreLocation(storeId);
                    } else {
                        storeId = "";
                        merchantId = "";
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // ✅ AUTO SELECT FIRST STORE (only for new reservation)
        if (reservationId.isEmpty() && storeList.size() > 1) {
            spinnerStore.setSelection(1); // skip "Select Store"
        }
    }

    private void setUserAssignAdapter(ArrayList<String> userAssignList) {
        ArrayAdapter<String> locationAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item,
                userAssignList);
        locationAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        spinnerAssign.setAdapter(locationAdapter);

        if (!selectedAssignId.isEmpty())
            for (int i = 0; i < ModelManager.getInstance().getReservationManager().
                    assignUserLocationResponse.getRESULT().get(0).getRESULT().size(); i++) {
                if (ModelManager.getInstance().getReservationManager().
                        assignUserLocationResponse.getRESULT().get(0).getRESULT().get(i).
                        getJsonMember1141().equals(selectedAssignId)) {

                    spinnerAssign.setSelection(i + 1);
                    break;
                }
            }

        spinnerAssign.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {
                    selectedAssignId = ModelManager.getInstance().getReservationManager().assignUserLocationResponse.getRESULT().get(0).getRESULT().get(i - 1).getJsonMember1141();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setPromoByLocationAdapter(ArrayList<String> tablesList) {
        ArrayAdapter<String> locationAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item,
                tablesList);
        locationAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        spinnerPromo.setAdapter(locationAdapter);

        if (!selectedPromoId.isEmpty())
            for (int i = 0; i < ModelManager.getInstance().getReservationManager().
                    promoByLocationResponse.getRESULT().get(0).getRESULT().size(); i++) {
                if (ModelManager.getInstance().getReservationManager().
                        promoByLocationResponse.getRESULT().get(0).getRESULT().get(i).
                        getJsonMember114144().equals(selectedPromoId)) {

                    spinnerPromo.setSelection(i + 1);
                    break;

                }
            }

        spinnerPromo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {
                    selectedPromoId = ModelManager.getInstance().getReservationManager().promoByLocationResponse.getRESULT()
                            .get(0).getRESULT().get(i - 1).getJsonMember114144();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setTableListAdapter(ArrayList<String> tablesList) {
        ArrayAdapter<String> locationAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item,
                tablesList);
        locationAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        spinnerTable.setAdapter(locationAdapter);

        if (!selectedTableId.isEmpty())
            for (int i = 0; i < ModelManager.getInstance().getReservationManager().
                    tablesBySeatingAreaResponse.getRESULT().get(0).getRESULT().size(); i++) {
                if (ModelManager.getInstance().getReservationManager().
                        tablesBySeatingAreaResponse.getRESULT().get(0).getRESULT().get(i).
                        getJsonMember11521().equals(selectedTableId)) {

                    spinnerTable.setSelection(i + 1);
                    break;

                }
            }

        spinnerTable.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {
                    selectedTableId = ModelManager.getInstance().getReservationManager().
                            tablesBySeatingAreaResponse.getRESULT().get(0).getRESULT().get(i - 1).getJsonMember11521();
/*
                    ModelManager.getInstance().getReservationManager().addReservationDetails(getActivity(),
                            Operations.makeJsonGetTablesBySeatingAreaId(requireActivity(), selectedTableId),
                            Constants.TAG_TABLES_BY_SEATING_AREA);
*/
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setAreaListAdapter(ArrayList<String> seatingAreaLocationList) {
        ArrayAdapter<String> locationAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item,
                seatingAreaLocationList);
        locationAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        spinnerArea.setAdapter(locationAdapter);

        if (!selectedSeatingAreaId.isEmpty())
            for (int i = 0; i < ModelManager.getInstance().getReservationManager().
                    seatingAreaLocationResponse.getRESULT().get(0).getRESULT().size(); i++) {
                if (ModelManager.getInstance().getReservationManager().
                        seatingAreaLocationResponse.getRESULT().get(0).getRESULT().get(i).
                        getJsonMember11511().equals(selectedSeatingAreaId)) {

                    spinnerArea.setSelection(i + 1);
                    break;

                }
            }

        spinnerArea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {
                    selectedSeatingAreaId = ModelManager.getInstance().getReservationManager().
                            seatingAreaLocationResponse.getRESULT().get(0).getRESULT().get(i - 1).getJsonMember11511();

//                    ModelManager.getInstance().getReservationManager().addReservationDetails(getActivity(),
//                            Operations.makeJsonGetTablesBySeatingAreaId(requireActivity(), selectedSeatingAreaId),
//                            Constants.TAG_TABLES_BY_SEATING_AREA);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // ✅ AUTO SELECT FIRST AREA
        if (reservationId.isEmpty() && seatingAreaLocationList.size() > 1) {
            spinnerArea.setSelection(1);
        }
    }

    private void setLocationListAdapter(ArrayList<String> locationList) {
        ArrayAdapter<String> locationAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item,
                locationList);
        locationAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        spinnerLocation.setAdapter(locationAdapter);

        if (selectedLocationId != null && !selectedLocationId.isEmpty()) {
            for (int i = 0; i < locationIdList.size(); i++) {
                if (locationIdList.get(i).equals(selectedLocationId)) {
                    spinnerLocation.setSelection(i);
                    break;
                }
            }
        }


        spinnerLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position != 0) {
                    showProgress();
                    selectedLocationId = locationIdList.get(position); // 🔥 FIXED

                    ModelManager.getInstance().getReservationManager().addReservationDetails(
                            getActivity(),
                            Operations.makeJsonSeatingAreasByLocation(requireActivity(), selectedLocationId),
                            Constants.TAG_SEATING_AREA_BY_LOCATION
                    );

                } else {
                    selectedLocationId = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // ✅ AUTO SELECT FIRST LOCATION
        if (reservationId.isEmpty() && locationList.size() > 1) {
            spinnerLocation.setSelection(1);
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

    private void showTimePicker(String title, Boolean isStartTime) {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(requireContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                String hourValue = String.valueOf(selectedHour);
                String minuteValue = String.valueOf(selectedMinute);


                if (String.valueOf(selectedHour).length() == 1)
                    hourValue = "0" + selectedHour;

                if (String.valueOf(selectedMinute).length() == 1)
                    minuteValue = "0" + selectedMinute;

                if (isStartTime)
                    editTextStartTime.setText(hourValue + ":" + minuteValue);
                else
                    editTextEndTime.setText(hourValue + ":" + minuteValue);

            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle(title);
        mTimePicker.show();


    }

    private void showTime24HourPicker(String title, Boolean isStartTime) {

        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);

        TimePickerDialog mTimePicker = new TimePickerDialog(
                requireContext(),
                (timePicker, selectedHour, selectedMinute) -> {

                    String formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute);

                    if (isStartTime)
                        editTextStartTime.setText(formattedTime);
                    else
                        editTextEndTime.setText(formattedTime);

                },
                hour,
                minute,
                true // force 24 hour
        );

        mTimePicker.setTitle(title);
        mTimePicker.show();
    }

    private void showDatePicker() {

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(
                requireContext(),
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                this,
                year, month, day
        );
        // dialog.getDatePicker().setMinDate(new Date().getTime());
        dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();


    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        month = month + 1;
        String monthValue = String.valueOf(month);
        String dayValue = String.valueOf(day);

        if (String.valueOf(month).length() == 1)
            monthValue = "0" + month;

        if (String.valueOf(day).length() == 1)
            dayValue = "0" + day;

        Log.d("onDateSet", month + "/" + day + "/" + year);
        editTextStartDate.setText(year + "-" + monthValue + "-" + dayValue);

    }

    private boolean dataValid() {
        if (Utils.checkIfEditextEmpty(editTextStartDate)) {
            baseshowFeedbackMessage(requireActivity(), rootView, "Please select start date.");
            return false;
        } else if (Utils.checkIfEditextEmpty(editTextStartTime)) {
            baseshowFeedbackMessage(requireActivity(), rootView, "Please " + editTextStartTime.getText().toString());
            return false;
        }
//        else if (Utils.checkIfEditextEmpty(editTextEndTime)) {
//            baseshowFeedbackMessage(requireActivity(), rootView, "Please " + editTextEndTime.getText().toString());
//            return false;
//        }
        else if (!Utils.isFutureDateTime(
                Utils.getEditTextString(editTextStartDate),
                Utils.getEditTextString(editTextStartTime))) {

            baseshowFeedbackMessage(requireActivity(), rootView,
                    "Selected time must be after current time.");
            return false;
        }
//        else if (spinnerDining.getSelectedItemPosition() == 0) {
//            baseshowFeedbackMessage(requireActivity(), rootView, "Please select dining method");
//            return false;
//        }
        else if (spinnerArea.getVisibility() == View.VISIBLE && spinnerArea.getSelectedItemPosition() == 0 && spinnerArea.getAdapter().getCount() > 1) {
            baseshowFeedbackMessage(requireActivity(), rootView, "Please select area");
            return false;
        }
//        else if (spinnerTable.getSelectedItemPosition() == 0) {
//            baseshowFeedbackMessage(requireActivity(), rootView, "Please select seating");
//            return false;
//        }
        else if (Utils.checkIfEditextEmpty(editTextNumberOfPeople)) {
            baseshowFeedbackMessage(requireActivity(), rootView, "Please " + editTextNumberOfPeople.getText().toString());
            return false;
        }
//        else if (Utils.checkIfEditextEmpty(editTextNumberOfChildren)) {
//            baseshowFeedbackMessage(requireActivity(), rootView, "Please " + editTextNumberOfChildren.getText().toString());
//            return false;
//        }
        else if (Utils.checkIfEditextEmpty(editTextName)) {
            baseshowFeedbackMessage(requireActivity(), rootView, "Please " + editTextName.getText().toString());
            return false;
        } else if (Utils.checkIfEditextEmpty(editTextEmail)) {
            baseshowFeedbackMessage(requireActivity(), rootView, "Please " + editTextEmail.getText().toString());
            return false;
        } else if (Utils.checkIfEditextEmpty(editTextPhone)) {
            baseshowFeedbackMessage(requireActivity(), rootView, "Please " + editTextPhone.getText().toString());
            return false;
        }
//        else if (spinnerAssign.getSelectedItemPosition() == 0) {
//            baseshowFeedbackMessage(requireActivity(), rootView, "Please select assign");
//            return false;
//        }
//        else if (spinnerPromo.getSelectedItemPosition() == 0) {
//                baseshowFeedbackMessage(requireActivity(), rootView, "Please select assign");
//            return false;
//        }

//        else if (spinnerSmokingArea.getSelectedItemPosition() == 0 && spinnerSmokingArea.getAdapter().getCount() > 1) {
//            baseshowFeedbackMessage(requireActivity(), rootView, "Please select Smoking Area Preference");
//            return false;
//        }

        return true;
    }

    private void disableViews() {
        relativeLayoutSave.setVisibility(View.GONE); // cannot edit reservation

        Utils.disableEnableClickable(editTextName, false);
        Utils.disableEnableClickable(editTextStartDate, false);
        Utils.disableEnableClickable(editTextStartTime, false);
        Utils.disableEnableClickable(editTextEndTime, false);
        Utils.disableEnableClickable(editTextNumberOfPeople, false);
        Utils.disableEnableClickable(editTextNotes, false);

        Utils.disableEnableClickable(spinnerLocation, false);
        Utils.disableEnableClickable(spinnerArea, false);
        Utils.disableEnableClickable(spinnerSmokingArea, false);

    }

    private void showNearMeBottomSheet() {

        View view = LayoutInflater.from(requireContext())
                .inflate(R.layout.bottom_sheet_near_me, null);

        com.google.android.material.bottomsheet.BottomSheetDialog dialog =
                new com.google.android.material.bottomsheet.BottomSheetDialog(requireContext());

        dialog.setContentView(view);
        dialog.setCancelable(true);

        TextView tvUseGps = view.findViewById(R.id.tvUseGps);
        CheckBox checkBoxPrimaryAddress = view.findViewById(R.id.checkBoxPrimaryAddress);
        CheckBox checkBoxGps = view.findViewById(R.id.checkBoxGps);
        TextView tvUseAddress = view.findViewById(R.id.tvUseAddress);

        checkBoxPrimaryAddress.setOnClickListener(view2 -> tvUseAddress.performClick());

        checkBoxGps.setOnClickListener(view1 -> tvUseGps.performClick());

        if (primaryAddressChecked)
            checkBoxPrimaryAddress.setChecked(true);

        if (gpsAddressChecked)
            checkBoxGps.setChecked(true);


        // 👉 GPS OPTION
        tvUseGps.setOnClickListener(v -> {
            checkBoxPrimaryAddress.setChecked(false);
            checkBoxGps.setChecked(true);


            if (App.latitude == 0 || App.longitude == 0) {
                baseshowFeedbackMessage(requireActivity(), rootView, "GPS location not available");
                switchNearMe.setChecked(false);
                return;
            }
            dialog.dismiss();

            gpsAddressChecked = true;
            primaryAddressChecked = false;

            callStoreNearMe(App.latitude, App.longitude);
        });

        // 👉 PRIMARY ADDRESS OPTION
        tvUseAddress.setOnClickListener(v -> {
            try {
                GetAddressResponse response = ModelManager.getInstance()
                        .getReservationManager()
                        .getAddressResponse;

                if (response == null ||
                        response.getRESULT() == null ||
                        response.getRESULT().isEmpty() ||
                        response.getRESULT().get(0).getRESULT() == null ||
                        response.getRESULT().get(0).getRESULT().isEmpty() ||
                        response.getRESULT().get(0).getRESULT().get(0).getAD() == null ||
                        response.getRESULT().get(0).getRESULT().get(0).getAD().isEmpty()) {

                    baseshowFeedbackMessage(requireActivity(), rootView, "Primary address not available");
                    return;
                }

                List<ADItem> adList = response.getRESULT().get(0)
                        .getRESULT().get(0)
                        .getAD();

                ADItem selectedAd = null;

                // 🔹 Step 1: Try last item
                ADItem lastAd = adList.get(adList.size() - 1);

                if (isValidLatLng(lastAd)) {
                    selectedAd = lastAd;
                }
                // 🔹 Step 2: Try second last (size - 2)
                else if (adList.size() >= 2) {
                    ADItem secondLastAd = adList.get(adList.size() - 2);
                    if (isValidLatLng(secondLastAd)) {
                        selectedAd = secondLastAd;
                    }
                }

                // 🔹 Step 3: If still null → fail
                if (selectedAd == null) {
                    baseshowFeedbackMessage(requireActivity(), rootView, "No valid address found");
                    return;
                }

                double latitude = Double.parseDouble(selectedAd.getJsonMember12038());
                double longitude = Double.parseDouble(selectedAd.getJsonMember12039());

                checkBoxPrimaryAddress.setChecked(true);
                checkBoxGps.setChecked(false);

                primaryAddressChecked = true;
                gpsAddressChecked = false;

                dialog.dismiss();
                callStoreNearMe(latitude, longitude);

            } catch (Exception e) {
                e.printStackTrace();
                baseshowFeedbackMessage(requireActivity(), rootView, "Something went wrong");
            }

        });

        dialog.setOnDismissListener(d -> {
            // If user closes without selection → turn OFF
            if (primaryAddressChecked == false && gpsAddressChecked == false) {
                switchNearMe.setChecked(false);
            }
        });

        dialog.show();
    }

    private boolean isValidLatLng(ADItem ad) {
        if (ad == null) return false;

        String latStr = ad.getJsonMember12038();
        String lngStr = ad.getJsonMember12039();

        if (latStr == null || lngStr == null || latStr.isEmpty() || lngStr.isEmpty()) {
            return false;
        }

        try {
            double lat = Double.parseDouble(latStr);
            double lng = Double.parseDouble(lngStr);
            return lat != 0 && lng != 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}


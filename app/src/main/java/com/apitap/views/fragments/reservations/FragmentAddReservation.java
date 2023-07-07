package com.apitap.views.fragments.reservations;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.apitap.App;
import com.apitap.R;
import com.apitap.controller.ModelManager;
import com.apitap.model.Constants;
import com.apitap.model.Operations;
import com.apitap.model.Utils;
import com.apitap.model.assignedToUser.AssignUserLocationResponse;
import com.apitap.model.bean.LocationListBean;
import com.apitap.model.customclasses.Event;
import com.apitap.model.getReservation.GetReservationResponse;
import com.apitap.model.getReservation.RESULTItem;
import com.apitap.model.preferences.ATPreferences;
import com.apitap.model.promoByLocation.PromoByLocationResponse;
import com.apitap.model.seatingAreaByLocation.SeatingAreaLocationResponse;
import com.apitap.model.tablesBySeatingArea.TablesBySeatingAreaResponse;
import com.apitap.views.HomeActivity;
import com.apitap.views.fragments.BaseFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Calendar;

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
    private LinearLayout linearLayoutHeaderCategory;
    private RelativeLayout relativeLayoutSave;
    private Spinner spinnerDining, spinnerArea, spinnerTable, spinnerAssign, spinnerPromo, spinnerLocation, spinnerSmokingArea;
    private EditText editTextStartDate, editTextStartTime, editTextEndTime, editTextNumberOfPeople;
    private EditText editTextNumberOfChildren, editTextName, editTextPhone, editTextEmail, editTextSecondEmail;
    private EditText editTextSpecialRequest, editTextNotes, editTextCustomerHistory;
    private TextView textViewAreaLabel;
    private String reservationId = "";
    private String selectedLocationId = "";
    private String selectedSeatingAreaId = "";
    private String selectedTableId = "";
    private String selectedPromoId = "";
    private String selectedAssignId = "";
    private Boolean smokingFlag = null;

    private String[] selectedDiningMethodArray;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_add_reservation, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        selectedDiningMethodArray = getResources().getStringArray(R.array.dining_method_id);

        if (getArguments() != null && getArguments().containsKey(Constants.RESERVATION_ID))
            reservationId = getArguments().getString(Constants.RESERVATION_ID);

        initViews();
        clickListeners();


        if (reservationId.isEmpty()) {
            ModelManager.getInstance().getMerchantManager().getMerchantDistance(requireContext(),
                    Operations.makeJsonGetMerchantDistance(requireContext(),
                            ATPreferences.readString(requireContext(), Constants.MERCHANT_ID),
                            String.valueOf(App.latitude),
                            String.valueOf(App.longitude)), Constants.GET_MERCHANT_DISTANCE_SUCCESS);
        } else {
            showProgress();
            ModelManager.getInstance().getReservationManager().addReservationDetails(getActivity(),
                    Operations.makeJsonGetReservationById(requireActivity(),
                            reservationId),
                    Constants.TAG_GET_RESERVATION);

            disableViews();

        }


    }



    private void initViews() {
        imageViewBack = rootView.findViewById(R.id.imageViewBack);

        relativeLayoutSave = rootView.findViewById(R.id.relativeLayoutSave);

        checkBoxWheelChairAccess = rootView.findViewById(R.id.checkBoxWheelChairAccess);
        checkBoxChildSeating = rootView.findViewById(R.id.checkBoxChildSeating);

        spinnerDining = rootView.findViewById(R.id.spinnerDining);
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
        relativeLayoutSave.setOnClickListener(this);
        editTextStartDate.setOnClickListener(this);
        editTextStartTime.setOnClickListener(this);
        editTextEndTime.setOnClickListener(this);
    }

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
                showTimePicker("Select Start Time", true);
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
                setData(getReservationResponse.getRESULT().get(0).getRESULT().get(0));

                ModelManager.getInstance().getMerchantManager().getMerchantDistance(requireContext(),
                        Operations.makeJsonGetMerchantDistance(requireContext(),
                                ATPreferences.readString(requireContext(), Constants.MERCHANT_ID),
                                String.valueOf(App.latitude),
                                String.valueOf(App.longitude)), Constants.GET_MERCHANT_DISTANCE_SUCCESS);
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
                }, 700);
                break;
            case Constants.GET_ASSIGNED_TO_USER_SUCCESS:

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
                ArrayList<String> seatingAreaLocationList = new ArrayList<>();
                seatingAreaLocationList.add("Select Area");
                SeatingAreaLocationResponse seatingAreaLocationResponse = ModelManager.getInstance().getReservationManager().seatingAreaLocationResponse;
                for (int i = 0; i < seatingAreaLocationResponse.getRESULT().get(0).getRESULT().size(); i++) {
                    seatingAreaLocationList.add(
                            Utils.hexToASCII(seatingAreaLocationResponse.getRESULT().get(0).getRESULT().get(i).getJsonMember11512()));
                }
                setAreaListAdapter(seatingAreaLocationList);

                if (seatingAreaLocationResponse.getRESULT().get(0).getRESULT().size() == 0) {
                    textViewAreaLabel.setVisibility(View.GONE);
                    spinnerArea.setVisibility(View.GONE);
                } else {
                    textViewAreaLabel.setVisibility(View.VISIBLE);
                    spinnerArea.setVisibility(View.VISIBLE);
                }

                break;

            case Constants.GET_TABLES_BY_SEATING_AREA_SUCCESS:
                ArrayList<String> tablesList = new ArrayList<>();
                tablesList.add("Select Table");
                TablesBySeatingAreaResponse tablesBySeatingAreaResponse = ModelManager.getInstance().getReservationManager().tablesBySeatingAreaResponse;
                for (int i = 0; i < tablesBySeatingAreaResponse.getRESULT().get(0).getRESULT().size(); i++) {
                    tablesList.add(
                            Utils.hexToASCII(tablesBySeatingAreaResponse.getRESULT().get(0).getRESULT().get(i).getJsonMember11522()));
                }
                setTableListAdapter(tablesList);

                if (!selectedPromoId.isEmpty())
                    ModelManager.getInstance().getReservationManager().addReservationDetails(getActivity(),
                            Operations.makeJsonGetPromos(requireActivity(),
                                    ATPreferences.readString(requireContext(), Constants.MERCHANT_ID)
                            ),
                            Constants.TAG_PROMO_BY_LOCATION);

                break;

            case Constants.GET_MERCHANT_DISTANCE_SUCCESS:
                ArrayList<String> locationList = new ArrayList<>();
                locationList.add("Select Location");
                LocationListBean locationListBean = ModelManager.getInstance().getMerchantManager().locationListBean;


                if (locationListBean.getRESULT().get(0).getRESULT().size() == 1 && locationListBean.getRESULT().get(0).getRESULT().get(0).get478() == null)
                    baseshowFeedbackMessage(requireActivity(), rootView, "No locations found for this store.");
                else {
                    for (int i = 0; i < locationListBean.getRESULT().get(0).getRESULT().size(); i++) {
                        String addressTwo = "";
                        if (locationListBean.getRESULT().get(0).getRESULT().get(i).getAD().get11413().isEmpty())
                            addressTwo =
                                    Utils.hexToASCII(locationListBean.getRESULT().get(0).getRESULT().get(i).getAD().get11413()).trim()
                                            +
                                            " " + locationListBean.getRESULT().get(0).getRESULT().get(i).getAD().getZP().get4717();
                        else
                            addressTwo = (locationListBean.getRESULT().get(0).getRESULT().get(i).getAD().getCI().get4715().trim()
                                    + " " + locationListBean.getRESULT().get(0).getRESULT().get(i).getAD().getCO().get11417() +
                                    " " + locationListBean.getRESULT().get(0).getRESULT().get(i).getAD().getST().get_1234() +
                                    " " + locationListBean.getRESULT().get(0).getRESULT().get(i).getAD().getZP().get4717());

                        locationList.add(Utils.hexToASCII(
                                locationListBean.getRESULT().get(0).getRESULT().get(i).getAD().get11412().trim())
                                + "  " + addressTwo + " " +
                                locationListBean.getRESULT().get(0).getRESULT().get(i).getAD().getCO().get4718().trim());
                    }
                    setLocationListAdapter(locationList);


//                    if (selectedPromoId.isEmpty())
//                        ModelManager.getInstance().getReservationManager().addReservationDetails(getActivity(),
//                                Operations.makeJsonGetPromos(requireActivity(),
//                                        ATPreferences.readString(requireContext(), Constants.MERCHANT_ID)
//                                ),
//                                Constants.TAG_PROMO_BY_LOCATION);


                }
                break;

        }
    }

    private void setData(RESULTItem result) {
        editTextStartDate.setText(result.getJsonMember116201());
        editTextStartTime.setText(Utils.removeSecondsFromTime(result.getJsonMember116202()));
        editTextEndTime.setText(Utils.removeSecondsFromTime(result.getJsonMember116203()));
        editTextNumberOfPeople.setText(result.getJsonMember116204());
        editTextNumberOfChildren.setText(result.getJsonMember116205());
        editTextEmail.setText(result.getJsonMember1147());
        editTextSecondEmail.setText(result.getJsonMember11451());
        editTextPhone.setText(result.getJsonMember4828());

        editTextName.setText(Utils.hexToASCII(result.getJsonMember11453()));
        editTextSpecialRequest.setText(Utils.hexToASCII(result.getJsonMember12155()));
        editTextNotes.setText(Utils.hexToASCII(result.getJsonMember11716()));

        checkBoxWheelChairAccess.setChecked(Boolean.parseBoolean(result.getJsonMember116206()));
        checkBoxChildSeating.setChecked(Boolean.parseBoolean(result.getJsonMember116207()));

        for (int i = 0; i < selectedDiningMethodArray.length; i++) {
            if (selectedDiningMethodArray[i].equals(result.getJsonMember11571())) {
                spinnerDining.setSelection(i);
                break;
            }
        }

        selectedAssignId = result.getJsonMember114179();
        //selectedAssignId = "124";
        selectedPromoId = result.getJsonMember114144();
        selectedSeatingAreaId = result.getJsonMember11713();
        selectedTableId = result.getJsonMember11521();
        selectedLocationId = result.getJsonMember11447();

        if (result.getJsonMember12772() != null) {
            smokingFlag = Boolean.valueOf(result.getJsonMember12772());

            if (smokingFlag)
                spinnerSmokingArea.setSelection(1);
            else
                spinnerSmokingArea.setSelection(2);

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
    }

    private void setLocationListAdapter(ArrayList<String> locationList) {
        ArrayAdapter<String> locationAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item,
                locationList);
        locationAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        spinnerLocation.setAdapter(locationAdapter);

        if (!selectedLocationId.isEmpty())
            for (int i = 0; i <
                    ModelManager.getInstance().getMerchantManager().locationListBean.getRESULT().get(0).getRESULT().size(); i++) {
                if (ModelManager.getInstance().getMerchantManager().locationListBean.getRESULT().get(0).getRESULT()
                        .get(i).get11447().equals(selectedLocationId)) {
                    spinnerLocation.setSelection(i + 1);
                    break;
                }
            }


        spinnerLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {
                    selectedLocationId = ModelManager.getInstance().getMerchantManager().locationListBean.getRESULT().get(0).getRESULT().get(i - 1).get11447();
                    ModelManager.getInstance().getReservationManager().addReservationDetails(getActivity(),
                            Operations.makeJsonSeatingAreasByLocation(requireActivity(), selectedLocationId), Constants.TAG_SEATING_AREA_BY_LOCATION);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
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
            baseshowFeedbackMessage(requireActivity(), rootView, "Please " + editTextStartDate.getText().toString());
            return false;
        } else if (Utils.checkIfEditextEmpty(editTextStartTime)) {
            baseshowFeedbackMessage(requireActivity(), rootView, "Please " + editTextStartTime.getText().toString());
            return false;
        } else if (Utils.checkIfEditextEmpty(editTextEndTime)) {
            baseshowFeedbackMessage(requireActivity(), rootView, "Please " + editTextEndTime.getText().toString());
            return false;
        } else if (!Utils.isDateAfter(Utils.getEditTextString(editTextStartTime), Utils.getEditTextString(editTextEndTime), "HH:mm")) {
            baseshowFeedbackMessage(requireActivity(), rootView, "End time should be after start time.");
            return false;
        }
//        else if (spinnerDining.getSelectedItemPosition() == 0) {
//            baseshowFeedbackMessage(requireActivity(), rootView, "Please select dining method");
//            return false;
//        }
        else if (spinnerArea.getVisibility() == View.VISIBLE && spinnerArea.getSelectedItemPosition() == 0) {
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
        }
//        else if (Utils.checkIfEditextEmpty(editTextEmail)) {
//            baseshowFeedbackMessage(requireActivity(), rootView, "Please " + editTextEmail.getText().toString());
//            return false;
//        }
//        else if (spinnerAssign.getSelectedItemPosition() == 0) {
//            baseshowFeedbackMessage(requireActivity(), rootView, "Please select assign");
//            return false;
//        }
//        else if (spinnerPromo.getSelectedItemPosition() == 0) {
//                baseshowFeedbackMessage(requireActivity(), rootView, "Please select assign");
//            return false;
//        }

        else if (spinnerSmokingArea.getSelectedItemPosition() == 0) {
            baseshowFeedbackMessage(requireActivity(), rootView, "Please select Smoking Area Preference");
            return false;
        }

        return true;
    }

    private void disableViews() {
        relativeLayoutSave.setVisibility(View.GONE); // cannot edit reservation

        Utils.disableEnableClickable(editTextName,false);
        Utils.disableEnableClickable(editTextStartDate,false);
        Utils.disableEnableClickable(editTextStartTime,false);
        Utils.disableEnableClickable(editTextEndTime,false);
        Utils.disableEnableClickable(editTextNumberOfPeople,false);
        Utils.disableEnableClickable(editTextNotes,false);

        Utils.disableEnableClickable(spinnerLocation,false);
        Utils.disableEnableClickable(spinnerArea,false);
        Utils.disableEnableClickable(spinnerSmokingArea,false);

    }


}
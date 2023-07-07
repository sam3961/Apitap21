package com.apitap.views.fragments;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.apitap.R;
import com.apitap.controller.ModelManager;
import com.apitap.model.Constants;
import com.apitap.model.Operations;
import com.apitap.model.Utils;
import com.apitap.model.customclasses.Event;
import com.apitap.views.HistoryDetailActivity;
import com.apitap.views.HomeActivity;
import com.apitap.views.MerchantStoreDetails;
import com.apitap.views.MerchantStoreMap;
import com.apitap.views.NavigationMenu.FragmentDrawer;
import com.apitap.views.fragments.messages.FragmentMessages;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;

import java.util.ArrayList;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBarUtils;
import fr.castorflex.android.smoothprogressbar.SmoothProgressDrawable;

/**
 * Created by Shami on 6/10/2017.
 */

public class SendMessage extends Fragment implements View.OnClickListener {

    ArrayList<String> type_list = new ArrayList<String>();
    EditText subject;
    Spinner spinnerLocation;
    String merchantId = "";
    String locationId = "";
    EditText message;
    Spinner spinner;
    TextView merchantName;
    String merchantNamestr;
    private ArrayList<String> locationList = new ArrayList<>();
    private ArrayList<String> locationIdList = new ArrayList<>();
    String productId = "12", className = "", adName = "";
    private LinearLayout backll, rootLayout, llLocation,llTo;
    String type = "92";
    Button sendMessage;
    private SmoothProgressBar mPocketBar;
    private FragmentDrawer drawerFragment;
    private boolean isMessageSent = false;
    private Bundle bundle;
    private Dialog mDialog;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //   getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        View v = inflater.inflate(R.layout.send_message, container, false);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        bundle = getArguments();
        if (bundle != null) {
            merchantId = bundle.getString("merchantId");
            merchantNamestr = bundle.getString("storeName");
            if (bundle.containsKey("productId"))
                productId = bundle.getString("productId");
            if (bundle.containsKey("className"))
                className = bundle.getString("className");
            if (bundle.containsKey("adName"))
                adName = bundle.getString("adName");
            if (bundle.containsKey("locationList")) {
                locationList = bundle.getStringArrayList("locationList");
                locationIdList = bundle.getStringArrayList("locationIdList");

            }
        }

        initViews(v);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (locationList.size() > 0)
            setSpinnerAdapter();
    }

    private void setSpinnerAdapter() {
        locationList.add(0, "Any Location");
        locationIdList.add(0, "");
        ArrayAdapter<String> adapterLocation = new ArrayAdapter<String>(getActivity(), R.layout.spinner_cat_store,
                R.id.text, locationList);

//        adapterLocation.setDropDownViewResource(R.layout.spinner_dropdown);
        spinnerLocation.setAdapter(adapterLocation);

        spinnerLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                locationId = locationIdList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        // getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
    }

    private void initViews(View view) {

        spinnerLocation = view.findViewById(R.id.spinnerLocation);
        subject = view.findViewById(R.id.editTextSubject2);
        sendMessage = view.findViewById(R.id.buttonSend);
        message = view.findViewById(R.id.editTextMessage);
        merchantName = view.findViewById(R.id.editTextTo);
        merchantName.setText(merchantNamestr);
        spinner = view.findViewById(R.id.editTextSubject);
        mPocketBar = view.findViewById(R.id.pocket);
        backll = view.findViewById(R.id.back_ll);
        llLocation = view.findViewById(R.id.llLocation);
        llTo = view.findViewById(R.id.llTo);
        rootLayout = view.findViewById(R.id.rootLayout);

        if (locationList.size() > 0)
            llLocation.setVisibility(View.VISIBLE);
        if (merchantNamestr.isEmpty())
            llTo.setVisibility(View.GONE);


        backll.setOnClickListener(view1 -> {
            EventBus.getDefault().post(new Event(Constants.BACK_PRESSED, ""));
            if (getFragmentManager().getBackStackEntryCount() > 0) {
                getFragmentManager().popBackStack();
                if (!isMessageSent)
                    getFragmentManager().popBackStack();
            } else {
                getActivity().onBackPressed();
            }
        });

        mPocketBar.setSmoothProgressDrawableBackgroundDrawable(
                SmoothProgressBarUtils.generateDrawableWithColors(
                        getResources().getIntArray(R.array.pocket_background_colors),
                        ((SmoothProgressDrawable) mPocketBar.getIndeterminateDrawable()).getStrokeWidth()));

        type_list.add("Contact Form");
        type_list.add("Message");
        type_list.add("Help Ticket");
        type_list.add("Note");
        type_list.add("Share offer");
        type_list.add("Consultation");
        type_list.add("Report Abuse");
        type_list.add("Notification");

        ArrayAdapter<String> arrayadpter = new ArrayAdapter<String>(getActivity(), R.layout.address_row2, R.id.txt_type, type_list);
        spinner.setAdapter(arrayadpter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0)
                    type = "91";
                else if (i == 1)
                    type = "92";
                else if (i == 2)
                    type = "93";
                else if (i == 3)
                    type = "94";
                else if (i == 4)
                    type = "95";
                else if (i == 5)
                    type = "96";
                else if (i == 6)
                    type = "97";
                else if (i == 7)
                    type = "98";

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sendMessage.setOnClickListener(this);


    }

    @Subscribe
    public void onEvent(Event event) {

        //      Utils.stopDialog();
        if (event.getKey() == Constants.MESSAGE_SEND_SUCCESS) {
            isMessageSent = true;
            hideProgress();
            Utils.baseshowFeedbackMessage(getActivity(), rootLayout, "Message Sent");
            message.setText("");
            Bundle bundle = new Bundle();
            bundle.putString("merchantId", merchantId);
            bundle.putString("storeName", merchantNamestr);
            checkForClassName(bundle);
        } else if (event.getKey() == Constants.GET_SERVER_ERROR) {
            hideProgress();
            Utils.baseshowFeedbackMessage(getActivity(), rootLayout, "Something went wrong.");

        }
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
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.buttonSend:

                String subject_str = subject.getText().toString();
                String message_str = message.getText().toString();
                if (subject_str.isEmpty())
                    Utils.baseshowFeedbackMessage(getActivity(), rootLayout, "Please enter subject.");
                else if (message_str.isEmpty())
                    Utils.baseshowFeedbackMessage(getActivity(), rootLayout, "Please enter message.");
                else {
                    showProgress();
                    if (adName.isEmpty())


//                        ModelManager.getInstance().getMessageManager().sendMessage(mActivity,
//                                Operations.sendMessageReply(mActivity, list.get(0).getParentId(),
//                                        "2", merchantId,
//                                        Utils.getElevenDigitId(list.get(0).getType()), list.get(0).getSubject(),
//                                        hexmsg, "", list.get(0).getId(), productId,
//                                        id, jsonArrayImages,locationId), Constants.MESSAGE_SEND_SUCCESS);


                    ModelManager.getInstance().getMessageManager().sendMessage(getActivity(),
                                Operations.sendMessage(getActivity(),
                                        merchantId, Utils.getElevenDigitId(type),
                                        subject_str, message_str,
                                        Utils.getElevenDigitId(productId), locationId),
                                Constants.MESSAGE_SEND_SUCCESS);
                    else
                        ModelManager.getInstance().getMessageManager().sendMessage(getActivity(),
                                Operations.sendMessageForAd(getActivity(),
                                        merchantId, Utils.getElevenDigitId(type),
                                        adName, message_str,
                                        Utils.getElevenDigitId(productId)
                                        , new JSONArray(), locationId), Constants.MESSAGE_SEND_SUCCESS);

                }
                break;

        }
    }

    private void checkForClassName(Bundle bundle) {
        switch (className) {
            case "Home":
                ((HomeActivity) getActivity()).displayView(new FragmentMessages(), Constants.TAG_MESSAGEPAGE, bundle);
                break;
            case "StoreDetails":
                ((MerchantStoreDetails) getActivity()).displayView(new FragmentMessages(), Constants.TAG_MESSAGEPAGE, bundle);
                break;
            case "StoreMap":
                ((MerchantStoreMap) getActivity()).displayView(new FragmentMessages(), Constants.TAG_MESSAGEPAGE, bundle);
                break;
            case "InvoiceDetail":
                EventBus.getDefault().post(new Event(Constants.BACK_PRESSED, ""));
                ((HistoryDetailActivity) getActivity()).displayView(new FragmentMessages(), Constants.TAG_MESSAGEPAGE, bundle);
                break;
            default:
                ((HomeActivity) getActivity()).displayView(new FragmentMessages(), Constants.TAG_MESSAGEPAGE, bundle);
                break;

        }
    }


    public void showProgress() {
        mDialog = new Dialog(getActivity());
        // no tile for the dialog
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.prograss_bar_dialog);
        ProgressBar mProgressBar = mDialog.findViewById(R.id.progress_bar);
        TextView progress_text = mDialog.findViewById(R.id.progress_text);
        progress_text.setText("Sending...");
        //  mProgressBar.getIndeterminateDrawable().setColorFilter(context.getResources()
        // .getColor(R.color.material_blue_gray_500), PorterDuff.Mode.SRC_IN);
        mProgressBar.setVisibility(View.VISIBLE);
        // you can change or add this line according to your need
        mProgressBar.setIndeterminate(true);
        mDialog.setCancelable(false);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();
    }

    public void hideProgress() {
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
    }
}

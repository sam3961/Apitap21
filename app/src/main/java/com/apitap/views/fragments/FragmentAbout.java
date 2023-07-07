package com.apitap.views.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;

import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.apitap.R;
import com.apitap.controller.ContactUsManger;
import com.apitap.controller.ModelManager;
import com.apitap.model.Constants;
import com.apitap.model.Operations;
import com.apitap.model.Utils;
import com.apitap.model.customclasses.Event;
import com.apitap.model.preferences.ATPreferences;
import com.apitap.views.HomeActivity;
import com.apitap.views.TermsAndConditionsActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class FragmentAbout extends BaseFragment {

    private ViewHolder holder;
    private boolean isTermsClicked;
    private boolean isPrivacyClicked;
    private final ArrayList<String> reasonsList = new ArrayList<>();

    public FragmentAbout() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_about, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        holder = new ViewHolder(view);
        tabContainer2Visible();
        hitApiForListOfReasons();
        checkForTermsConditions();
    }

    private class ViewHolder implements View.OnClickListener {
        LinearLayout ll_back;
        Button app_info, privacy, terms_conditions, buttonSubmit;
        Spinner options_Spinner;
        EditText clarifyReason_ed;
        ImageView img_fb, img_twitter, imageViewVisitApitap;
        ScrollView rootLayout;

        public ViewHolder(View view) {
            ll_back = view.findViewById(R.id.back_ll);
            app_info = view.findViewById(R.id.app_info);
            options_Spinner = view.findViewById(R.id.choices);
            clarifyReason_ed = view.findViewById(R.id.reason);
            privacy = view.findViewById(R.id.privacy);
            terms_conditions = view.findViewById(R.id.terms);
            img_fb = view.findViewById(R.id.fb);
            img_twitter = view.findViewById(R.id.twitter);
            buttonSubmit = view.findViewById(R.id.buttonSubmit);
            rootLayout = view.findViewById(R.id.rootLayout);
            imageViewVisitApitap = view.findViewById(R.id.imageViewVisitApitap);

            clarifyReason_ed.setMovementMethod(new ScrollingMovementMethod());

            ll_back.setOnClickListener(this);
            app_info.setOnClickListener(this);
            buttonSubmit.setOnClickListener(this);
            img_fb.setOnClickListener(this);
            img_twitter.setOnClickListener(this);
            privacy.setOnClickListener(this);
            terms_conditions.setOnClickListener(this);
            imageViewVisitApitap.setOnClickListener(this);

            try {
                Field popup = Spinner.class.getDeclaredField("mPopup");
                popup.setAccessible(true);
                android.widget.ListPopupWindow popupWindow = (ListPopupWindow) popup.get(options_Spinner);
                popupWindow.setHeight(600);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.back_ll:
                    getActivity().startActivity(new Intent(getActivity(), HomeActivity.class));
                    break;
                case R.id.app_info:
                    Utils.whatsNewDialog(getActivity());
                    break;
                case R.id.privacy:
                    if (!ATPreferences.readString(getActivity(), Constants.TERMS).isEmpty())
                        startActivity(new Intent(getActivity(), TermsAndConditionsActivity.class).putExtra(Constants.KEY, Constants.PRIVACY_KEY));
                    else {
                        showProgress();
                        isPrivacyClicked = true;
                    }
                    break;
                case R.id.terms:
                    if (!ATPreferences.readString(getActivity(), Constants.TERMS).isEmpty())
                        startActivity(new Intent(getActivity(), TermsAndConditionsActivity.class).putExtra(Constants.KEY, Constants.TERMS_KEY));
                    else {
                        showProgress();
                        isTermsClicked = true;
                    }
                    break;
                case R.id.fb:
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/aiodigitalmarketing"));
                    startActivity(intent);
                    break;
                case R.id.twitter:
                    Intent intent1 = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/aiodm1"));
                    startActivity(intent1);
                    break;
                case R.id.imageViewVisitApitap:
                    Intent intent2 = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.apitap.com/"));
                    startActivity(intent2);
                    break;
                case R.id.buttonSubmit:
                    if (clarifyReason_ed.getText().toString().isEmpty()) {
                        Utils.baseshowFeedbackMessage(getActivity(), rootLayout, "Please write something.");
                    } else {
                        showProgress();
                        ModelManager.getInstance().getMessageManager().sendMessage(getActivity(), Operations.sendMessageAboutUs(getActivity(),
                                reasonsList.get(options_Spinner.getSelectedItemPosition()), clarifyReason_ed.getText().toString(),
                                ContactUsManger.aboutUsBean.getRESULT().get(0).getRESULT().get(options_Spinner.getSelectedItemPosition()).get_122_25())
                                , Constants.MESSAGE_SEND_SUCCESS);
                    }
                    break;
            }
        }
    }


    private void checkForTermsConditions() {
        if (ATPreferences.readString(getActivity(), Constants.TERMS).isEmpty()) {
            ModelManager.getInstance().getLoginManager().getTermsAndConditions(getActivity(),
                    Operations.makeJsonGetTermsConditions(getActivity()));
        }
    }

    private void hitApiForListOfReasons() {
        showProgress();
        ModelManager.getInstance().getContactUsManger().fetchReasonsListing(getActivity(),
                Operations.fetchAboutUsListing(getActivity()));
    }

    private void listInflate() {
        reasonsList.add("I have a question about my account.");
        reasonsList.add("I need help doing something.");
        reasonsList.add("I want to suggest adding one of my favorite merchants to ApiTap.");
        reasonsList.add("I have a suggestion for how to improve ApiTap.");
        //  reasonsList.add("-----------------------------------");
        reasonsList.add("I'm having problems with merchant.");
        reasonsList.add("I want to report an offensive item, service or merchant.");
        reasonsList.add("I want to report suspicious activity in ApiTap.");
        //reasonsList.add("-----------------------------------");
        reasonsList.add("I need something else.");


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

    @Subscribe
    public void onEvent(final Event event) {
        switch (event.getKey()) {

            case Constants.ABOUT_LIST_SUCCESS:
                hideProgress();
                for (int i = 0; i < ContactUsManger.aboutUsBean.getRESULT().get(0).getRESULT().size(); i++) {
                    reasonsList.add(Utils.hexToASCII(ContactUsManger.aboutUsBean.getRESULT().get(0).getRESULT().get(i).get_122_26()));
                }
                holder.options_Spinner.setAdapter(new SpinnerAdapter(getActivity(), R.layout.row_spinner_divider));
                holder.options_Spinner.measure(0, 0);
                holder.options_Spinner.setDropDownWidth(holder.options_Spinner.getMeasuredWidth() + 80);
                break;
            case Constants.MESSAGE_SEND_SUCCESS:
                hideProgress();
                Utils.baseshowFeedbackMessage(getActivity(), holder.rootLayout, "Submit Successfully");
                break;
            case Constants.TERMS_CONDITIONS:
                hideProgress();
                if (isPrivacyClicked)
                    startActivity(new Intent(getActivity(), TermsAndConditionsActivity.class).putExtra(Constants.KEY, Constants.PRIVACY_KEY));
                else if (isTermsClicked)
                    startActivity(new Intent(getActivity(), TermsAndConditionsActivity.class).putExtra(Constants.KEY, Constants.TERMS_KEY));
                break;

        }
    }


    public void tabContainer2Visible() {
        HomeActivity.tabContainer2.setVisibility(View.GONE);
        HomeActivity.tabContainer2.setVisibility(View.GONE);
    }

    public class SpinnerAdapter extends ArrayAdapter<String> {
        Context context;
        int quantity = 1;
        com.apitap.views.adapters.SpinnerAdapter.AdapterClick adapterClick;

        public SpinnerAdapter(Context context, int resource) {
            super(context, resource);
            this.context = context;
        }

        @Override
        public int getCount() {
            return reasonsList.size();
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.row_spinner_divider, null);
            TextView label = rowView.findViewById(R.id.text);
            LinearLayout divider = rowView.findViewById(R.id.divider);
            divider.setVisibility(View.VISIBLE);
            label.setText(reasonsList.get(position));
            return rowView;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.row_spinner_divider, null);
            TextView label = rowView.findViewById(R.id.text);
            LinearLayout divider = rowView.findViewById(R.id.divider);
            label.setText(reasonsList.get(position));
            divider.setVisibility(View.VISIBLE);
            return rowView;
        }
    }

}
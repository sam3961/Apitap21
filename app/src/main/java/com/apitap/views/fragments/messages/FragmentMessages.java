package com.apitap.views.fragments.messages;


import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.apitap.R;
import com.apitap.controller.ModelManager;
import com.apitap.model.Constants;
import com.apitap.model.Operations;
import com.apitap.model.Utils;
import com.apitap.model.bean.MessageListBean;
import com.apitap.model.customclasses.Event;
import com.apitap.model.preferences.ATPreferences;
import com.apitap.views.HistoryDetailActivity;
import com.apitap.views.HomeActivity;
import com.apitap.views.MerchantStoreDetails;
import com.apitap.views.MerchantStoreMap;
import com.apitap.views.fragments.messages.adapter.MessageListAdapter;
import com.apitap.views.customviews.DividerItemDecoration;
import com.apitap.views.fragments.BaseFragment;
import com.apitap.views.fragments.messageDetails.FragmentMessageDetail;
import com.apitap.views.fragments.SendMessage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentMessages extends BaseFragment implements View.OnClickListener {


    private ViewHolder holder;
    MessageListAdapter adp;
    List<MessageListBean.RESULT.MessageData> list;
    int unreadCount = 0;
    int state = 0;
    private LinearLayout ll_msgCount;
    private LinearLayout backll;
    private RelativeLayout rootLayout;
    public static Button view_msg, btn_newMsg;
    private Dialog reloadDialog;
    private String merchantId = "", storeName = "", className = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.activity_apitap_message, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        holder = new ViewHolder(view);
        view_msg = view.findViewById(R.id.view_msg);
        rootLayout = view.findViewById(R.id.rootLayout);
        backll = view.findViewById(R.id.back_ll);
        ll_msgCount = getActivity().findViewById(R.id.new_msgsll);
        btn_newMsg = getActivity().findViewById(R.id.new_msg);

        reloadDialog = Utils.showReloadDialog(getActivity());

        backll.setOnClickListener(this);
        btn_newMsg.setOnClickListener(this);

        ll_msgCount.setVisibility(View.GONE);
        view_msg.setVisibility(View.GONE);
        tabContainer2Visible();
        holder.recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        holder.recycler.addItemDecoration(new DividerItemDecoration(getActivity(), R.drawable.divider_grey));
        showProgress();


        holder.find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                state = 1;
                if (holder.search_msg.getText().toString().length() == 0) {
                    Utils.baseshowFeedbackMessage(getActivity(), rootLayout, "Please input something to search");
                } else {
                    String query = holder.search_msg.getText().toString();
                    filter(query);
                }
            }
        });


        holder.search_msg.addTextChangedListener(txwatcher);
    }


    @Override
    public void onResume() {
        super.onResume();
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey("merchantId")) {
            merchantId = bundle.getString("merchantId");
            storeName = bundle.getString("storeName");
            className = bundle.getString("className");
            ModelManager.getInstance().getMessageManager().getAllMessages(getActivity(),
                    Operations.makeJsonAllMerchantMessages(getActivity(), ATPreferences.readString(
                            getActivity(), Constants.MERCHANT_ID)), Constants.ALL_MESSAGES_SUCCESS);
            btn_newMsg.setVisibility(View.VISIBLE);

        } else
            ModelManager.getInstance().getMessageManager().getAllMessages(getActivity(),
                    Operations.makeJsonAllMessages(getActivity()), Constants.ALL_MESSAGES_SUCCESS);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_ll:
                onBackPress();
                break;
            case R.id.new_msg:
                Bundle bundle = new Bundle();
                bundle.putString("merchantId", merchantId);
                bundle.putString("storeName", storeName);
                checkForClassName(bundle);
                break;
        }

    }

    private void checkForClassName(Bundle bundle) {
        switch (className) {
            case "StoreDetails":
                ((MerchantStoreDetails) getActivity()).displayView(new SendMessage(), Constants.TAG_MESSAGEPAGE, bundle);
                break;
            case "InvoiceDetail":
                EventBus.getDefault().post(new Event(Constants.BACK_PRESSED, ""));
                ((HistoryDetailActivity) getActivity()).displayView(new FragmentMessages(), Constants.TAG_MESSAGEPAGE, bundle);
                break;
            case "StoreMap":
                ((MerchantStoreMap) getActivity()).displayView(new SendMessage(), Constants.TAG_MESSAGEPAGE, bundle);
                break;
            default:
                ((HomeActivity) getActivity()).displayView(new SendMessage(), Constants.TAG_MESSAGEPAGE, bundle);
                break;
        }
    }

    private class ViewHolder {

        private final RecyclerView recycler;
        private final TextView no_messages;
        private final EditText search_msg;
        private final Button find;
        private final TextView currentTime;
        private final TextView tv_unreadCount;


        public ViewHolder(View view) {
            recycler = view.findViewById(R.id.recycler);
            no_messages = view.findViewById(R.id.nomsgs);
            search_msg = view.findViewById(R.id.searchmsg);
            find = view.findViewById(R.id.find);
            currentTime = view.findViewById(R.id.currentDate);
            tv_unreadCount = view.findViewById(R.id.new_msgs);


        }
    }

    void filter(String text) {
        List<MessageListBean.RESULT.MessageData> temp = new ArrayList();
        for (MessageListBean.RESULT.MessageData d : list) {

            if (d.getSubject().contains(text) ||
                    Utils.hexToASCII(d.getContextData()).contains(text)) {
                temp.add(d);
            }
        }
        //update recyclerview
        adp.updateList(temp);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    final TextWatcher txwatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {

            if (state == 1 && holder.search_msg.length() == 0) {
                adp = new MessageListAdapter(getActivity(), list, unreadCount);
                holder.recycler.setAdapter(adp);
            }
        }

        public void afterTextChanged(Editable s) {
        }
    };


    @Subscribe
    public void onEvent(final Event event) {
        switch (event.getKey()) {
            case Constants.ALL_MESSAGES_SUCCESS:
                hideProgress();
                list = ModelManager.getInstance().getMessageManager().messageListBean.getRESULT().get(0).getRESULT();
                if (list.get(0).getIsSeen() == null) {
                    if (merchantId.isEmpty()) {
                        holder.no_messages.setVisibility(View.VISIBLE);
                        holder.recycler.setVisibility(View.GONE);
                        rootLayout.setVisibility(View.VISIBLE);
                    } else {
                        Bundle bundle = new Bundle();
                        bundle.putString("merchantId", merchantId);
                        bundle.putString("storeName", storeName);
                        checkForClassName(bundle);
                    }
                    return;
                }
                rootLayout.setVisibility(View.VISIBLE);
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getIsSeen().equals("false")) {
                        unreadCount++;
                    }
                }
                if (unreadCount > 0)
                    holder.tv_unreadCount.setText("You Have " + (unreadCount) + " New Or Unread Messages");
                else
                    holder.tv_unreadCount.setText("You Have No New Or Unread Messages");

                holder.currentTime.setText(Utils.getCurrentTimeUsingDate());
                adp = new MessageListAdapter(getActivity(), list, unreadCount);
                holder.recycler.setAdapter(adp);
                Log.d("ListSizess", list.size() + "");
                if (list.size() == 0) {
                    holder.no_messages.setVisibility(View.VISIBLE);
                    holder.recycler.setVisibility(View.GONE);
                }
                adp.setOnItemClickListner((v, position) -> {
                    //114.144 product id
                    //123.21 ad id
                    //121.75 invoice id

                    Bundle bundle = new Bundle();
                    bundle.putString("merchantName", list.get(position).getSeventy());
                    bundle.putString("adName", "");
                    bundle.putString("productName", "");

                    if (ATPreferences.readString(getActivity(), Constants.KEY_USERID).equals(list.get(position).getUserId()))
                        bundle.putString("merchantId", list.get(position).getMerchantReceiver());
                    else if (ATPreferences.readString(getActivity(), Constants.KEY_USERID).equals(list.get(position).getMerchantReceiver()))
                        bundle.putString("merchantId", list.get(position).getUserId());
                    if (!list.get(position).getProductId().isEmpty())
                        bundle.putString("productId", Utils.getElevenDigitId(list.get(position).getProductId()));
                    else if (!list.get(position).getAdId().isEmpty())
                        bundle.putString("adID", Utils.getElevenDigitId(list.get(position).getAdId()));
                    else if (!list.get(position).getInvoiceId().isEmpty())
                        bundle.putString("invoice", list.get(position).getInvoiceId());
                    else if (!list.get(position).getId().isEmpty())
                        bundle.putString("generalId", list.get(position).getId());
                    ((HomeActivity) getActivity()).displayView(new FragmentMessageDetail(),
                            Constants.MessageDetailPage, bundle);

                });

                break;
            case -1:
                hideProgress();
                relaodDialogShow();
                break;

            case Constants.GET_SERVER_ERROR:
                Utils.baseshowFeedbackMessage(getActivity(), rootLayout, "Something Went Wrong...");
                break;

        }
    }

    private void relaodDialogShow() {
        TextView textView_yes = reloadDialog.findViewById(R.id.txtok);
        TextView textView_no = reloadDialog.findViewById(R.id.txtcancel);
        TextView textView_title = reloadDialog.findViewById(R.id.txtmessage);

        textView_title.setText("We're sorry but there seems to be some network issues connecting to the server. Please try again.");
        textView_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reloadFragment();
                reloadDialog.dismiss();
            }
        });

        textView_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reloadDialog.dismiss();

            }
        });

        reloadDialog.show();
    }

    public void reloadFragment() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
    }

    public void tabContainer2Visible() {
        if (HomeActivity.tabContainer2 == null)
            return;
        HomeActivity.tabContainer2.setVisibility(View.GONE);
        HomeActivity.tabContainer2.setVisibility(View.GONE);
    }
}

package com.apitap.app.views.fragments.messages;


import static android.view.View.VISIBLE;

import android.app.Dialog;
import android.app.Activity;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.apitap.app.R;
import com.apitap.app.controller.ModelManager;
import com.apitap.app.model.Constants;
import com.apitap.app.model.Operations;
import com.apitap.app.model.Utils;
import com.apitap.app.model.bean.MessageListBean;
import com.apitap.app.model.customclasses.Event;
import com.apitap.app.model.preferences.ATPreferences;
import com.apitap.app.model.unreadMessageMerchant.RESULTItem;
import com.apitap.app.model.unreadMessageMerchant.UnreadMerchantMessages;
import com.apitap.app.views.HistoryDetailActivity;
import com.apitap.app.views.HomeActivity;
import com.apitap.app.views.MessageDetailActivity;
import com.apitap.app.views.MerchantStoreDetails;
import com.apitap.app.views.MerchantStoreMap;
import com.apitap.app.views.fragments.messages.adapter.MessageListAdapter;
import com.apitap.app.views.customviews.DividerItemDecoration;
import com.apitap.app.views.fragments.BaseFragment;
import com.apitap.app.views.fragments.messageDetails.FragmentMessageDetail;
import com.apitap.app.views.fragments.SendMessage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentMessages extends BaseFragment implements View.OnClickListener {


    private ViewHolder holder;
    MessageListAdapter adp;
    List<MessageListBean.MessageData> list;
    int unreadCount = 0;
    int state = 0;
    private LinearLayout ll_msgCount;
    private LinearLayout backll;
    private RelativeLayout rootLayout;
    public static Button view_msg, btn_newMsg;
    private Dialog reloadDialog;
    private String merchantId = "", storeName = "", className = "";
    private LinearLayout linearLayoutStoreFrontHeader;

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
        ll_msgCount = getActivity().findViewById(R.id.new_msgsll);
        btn_newMsg = getActivity().findViewById(R.id.new_msg);
        linearLayoutStoreFrontHeader = getActivity().findViewById(R.id.header);

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
                    Utils.hideKeyboardFrom(requireContext(), view);
                }
            }
        });

        holder.clearSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                state = 0;
                holder.search_msg.setText("");
                showFullMessageList();
                Utils.hideKeyboardFrom(requireContext(), view);
            }
        });

        holder.search_msg.addTextChangedListener(txwatcher);
    }


    @Override
    public void onResume() {
        super.onResume();
        Bundle bundle = getArguments();
        if ((bundle != null && bundle.containsKey("merchantId")) || linearLayoutStoreFrontHeader.getVisibility() == VISIBLE) {

            if (bundle != null && bundle.containsKey("merchantId"))
                merchantId = bundle.getString("merchantId");

            if (bundle != null && bundle.containsKey("storeName"))
                storeName = bundle.getString("storeName");

            if (bundle != null && bundle.containsKey("className"))
                className = bundle.getString("className");

            btn_newMsg.setVisibility(VISIBLE);

            ModelManager.getInstance().getMessageManager().getAllMessages(getActivity(),
                    Operations.makeJsonAllMerchantMessages(getActivity(), ATPreferences.readString(
                            getActivity(), Constants.MERCHANT_ID)), Constants.ALL_MESSAGES_SUCCESS);

            ModelManager.getInstance().getMessageManager().getUnreadMessagesMerchant(requireContext(),
                    Operations.makeJsonUnreadMessagesMerchant(requireActivity()), Constants.UNREAD_MESSAGES_MERCHANT_SUCCESS);

            String storeName = ATPreferences.readString(
                    getActivity(),
                    Constants.STORE_NAME
            );

            if (storeName != null && storeName.contains(",")) {
                storeName = storeName.split(",")[0].trim();
            }

            holder.titleName.setText("Messages with " + storeName);
            holder.titleName.setTextSize(13);
        } else {
            ModelManager.getInstance().getMessageManager().getAllMessages(getActivity(),
                    Operations.makeJsonAllMessages(getActivity()), Constants.ALL_MESSAGES_SUCCESS);

            ModelManager.getInstance().getMessageManager().getUnreadMessages(requireContext(),
                    Operations.makeJsonUnreadMessages(requireActivity()), Constants.UNREAD_MESSAGES_SUCCESS);

        }

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
        if (className == null)
            className = "";
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
        private final TextView titleName;
        private final TextView no_messages;
        private final EditText search_msg;
        private final Button find;
        private final ImageView clearSearch;
        private final TextView currentTime;
        private final TextView tv_unreadCount;
        private final LinearLayout linearSearch;


        public ViewHolder(View view) {
            titleName = view.findViewById(R.id.titleName);
            recycler = view.findViewById(R.id.recycler);
            no_messages = view.findViewById(R.id.nomsgs);
            search_msg = view.findViewById(R.id.searchmsg);
            find = view.findViewById(R.id.find);
            clearSearch = view.findViewById(R.id.clear_search);
            currentTime = view.findViewById(R.id.currentDate);
            tv_unreadCount = view.findViewById(R.id.new_msgs);
            linearSearch = view.findViewById(R.id.linear);


        }
    }

    void filter(String text) {
        List<MessageListBean.MessageData> temp = new ArrayList<>();

        if (adp == null) {
            return;
        }

        if (list == null || list.isEmpty()) {
            adp.updateList(temp);
            updateEmptyState(0);
            return;
        }

        String query = text == null ? "" : text.trim().toLowerCase();

        for (MessageListBean.MessageData d : list) {
            if (!isRenderableMessage(d)) {
                continue;
            }

            String subject = d.getSubject() != null ? d.getSubject().toLowerCase() : "";
            String message = d.getContextData() != null
                    ? Utils.hexToASCII(d.getContextData()).toLowerCase()
                    : "";
            String merchantName = d.getSeventy() != null
                    ? Utils.hexToASCII(d.getSeventy()).toLowerCase()
                    : "";

            if (subject.contains(query)
                    || message.contains(query)
                    || merchantName.contains(query)) {
                temp.add(d);
            }
        }

        adp.updateList(temp);
        bindAdapterClicks();

        if (temp.isEmpty()) {
            holder.no_messages.setVisibility(VISIBLE);
            holder.recycler.setVisibility(View.GONE);
        } else {
            holder.no_messages.setVisibility(View.GONE);
            holder.recycler.setVisibility(VISIBLE);
        }
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
                showFullMessageList();
            }

            holder.clearSearch.setVisibility(s.length() > 0 ? VISIBLE : View.GONE);
        }

        public void afterTextChanged(Editable s) {
        }
    };


    @Subscribe
    public void onEvent(final Event event) {
        switch (event.getKey()) {
            case Constants.ALL_MESSAGES_SUCCESS:
                hideProgress();

                MessageListBean bean = ModelManager.getInstance()
                        .getMessageManager().messageListBean;

                if (bean == null || bean.getRESULT() == null || bean.getRESULT().isEmpty()) {
                    updateEmptyState(0);
                    return;
                }

                MessageListBean.ResultWrapper wrapper = bean.getRESULT().get(0);

                if (wrapper == null || wrapper.getRESULT() == null) {
                    updateEmptyState(0);
                    return;
                }

                list = sanitizeMessageList(wrapper.getRESULT());

                if (list == null || list.isEmpty()) {
                    holder.no_messages.setVisibility(VISIBLE);
                    holder.recycler.setVisibility(View.GONE);
                    return;
                }

                rootLayout.setVisibility(VISIBLE);
                holder.currentTime.setText(Utils.getCurrentTimeUsingDate());

                showFullMessageList();
                String activeQuery = holder.search_msg.getText().toString();
                if (!activeQuery.trim().isEmpty()) {
                    state = 1;
                    filter(activeQuery);
                }

                break;
            case -1:
                hideProgress();
                relaodDialogShow();
                break;

            case Constants.GET_SERVER_ERROR:
                Utils.baseshowFeedbackMessage(getActivity(), rootLayout, "Something Went Wrong...");
                break;

            case Constants.UNREAD_MESSAGES_MERCHANT_SUCCESS:
                UnreadMerchantMessages unreadMerchantMessages = event.getUnreadMerchantMessages();
                unreadCount = getMerchantUnreadCount(unreadMerchantMessages, getCurrentMerchantId());
                if (unreadCount > 0) {
                    holder.tv_unreadCount.setText("You Have " + unreadCount + " New Or Unread Messages");
                } else {
                    holder.tv_unreadCount.setText("You Have No New Or Unread Messages");
                }

                break;
            case Constants.UNREAD_MESSAGES_SUCCESS:
                unreadCount = Integer.parseInt(event.getResponse());
                if (unreadCount > 0)
                    holder.tv_unreadCount.setText("You Have " + (unreadCount) + " New Or Unread Messages");
                else
                    holder.tv_unreadCount.setText("You Have No New Or Unread Messages");

                break;

        }
    }

    private void bindAdapterClicks() {
        if (adp == null) {
            return;
        }

        adp.setOnItemClickListner((v, position) -> {
            if (position == RecyclerView.NO_POSITION) {
                return;
            }

            MessageListBean.MessageData messageData = adp.getItem(position);
            if (messageData == null) {
                return;
            }

            Bundle bundle = new Bundle();
            bundle.putString("merchantName", messageData.getSeventy());
            bundle.putString("adName", "");
            bundle.putString("messageId", messageData.getParentId());
            bundle.putString("productName", "");

            String currentUserId = ATPreferences.readString(getActivity(), Constants.KEY_USERID);
            if (currentUserId.equals(messageData.getUserId())) {
                bundle.putString("merchantId", messageData.getMerchantReceiver());
            } else if (currentUserId.equals(messageData.getMerchantReceiver())) {
                bundle.putString("merchantId", messageData.getUserId());
            }

            if (messageData.getProductId() != null && !messageData.getProductId().isEmpty()) {
                bundle.putString("productId", Utils.getElevenDigitId(messageData.getProductId()));
            } else if (messageData.getAdId() != null && !messageData.getAdId().isEmpty()) {
                bundle.putString("adID", Utils.getElevenDigitId(messageData.getAdId()));
            } else if (messageData.getInvoiceId() != null && !messageData.getInvoiceId().isEmpty()) {
                bundle.putString("invoice", messageData.getInvoiceId());
            } else if (messageData.getId() != null && !messageData.getId().isEmpty()) {
                bundle.putString("generalId", messageData.getId());
            }

            openMessageDetail(bundle);
        });
    }

    private void showFullMessageList() {
        List<MessageListBean.MessageData> fullList = list == null
                ? new ArrayList<>()
                : list;
        adp = new MessageListAdapter(getActivity(), fullList, unreadCount);
        holder.recycler.setAdapter(adp);
        bindAdapterClicks();
        updateEmptyState(fullList.size());
    }

    private void openMessageDetail(Bundle bundle) {
        Activity activity = getActivity();
        if (activity instanceof HomeActivity) {
            ((HomeActivity) activity).displayView(new FragmentMessageDetail(), Constants.MessageDetailPage, bundle);
        } else if (activity instanceof HistoryDetailActivity) {
            ((HistoryDetailActivity) activity).displayView(new FragmentMessageDetail(), Constants.MessageDetailPage, bundle);
        } else if (activity instanceof MerchantStoreDetails) {
            ((MerchantStoreDetails) activity).displayView(new FragmentMessageDetail(), Constants.MessageDetailPage, bundle);
        } else if (activity instanceof MerchantStoreMap) {
            ((MerchantStoreMap) activity).displayView(new FragmentMessageDetail(), Constants.MessageDetailPage, bundle);
        } else if (activity instanceof MessageDetailActivity) {
            ((MessageDetailActivity) activity).displayView(new FragmentMessageDetail(), Constants.MessageDetailPage, bundle);
        }
    }

    private void updateEmptyState(int resultCount) {
        if (resultCount == 0) {
            holder.no_messages.setVisibility(VISIBLE);
            holder.recycler.setVisibility(View.GONE);
        } else {
            holder.no_messages.setVisibility(View.GONE);
            holder.recycler.setVisibility(VISIBLE);
        }
    }

    private List<MessageListBean.MessageData> sanitizeMessageList(List<MessageListBean.MessageData> source) {
        List<MessageListBean.MessageData> sanitized = new ArrayList<>();
        if (source == null) {
            return sanitized;
        }

        for (MessageListBean.MessageData item : source) {
            if (isRenderableMessage(item)) {
                sanitized.add(item);
            }
        }

        sortMessagesNewestFirst(sanitized);
        return sanitized;
    }

    private void sortMessagesNewestFirst(List<MessageListBean.MessageData> messages) {
        Collections.sort(messages, new Comparator<MessageListBean.MessageData>() {
            @Override
            public int compare(MessageListBean.MessageData first, MessageListBean.MessageData second) {
                Date firstDate = getMessageSortDate(first);
                Date secondDate = getMessageSortDate(second);

                if (firstDate == null && secondDate == null) {
                    return 0;
                }
                if (firstDate == null) {
                    return 1;
                }
                if (secondDate == null) {
                    return -1;
                }

                return secondDate.compareTo(firstDate);
            }
        });
    }

    private Date getMessageSortDate(MessageListBean.MessageData item) {
        if (item == null) {
            return null;
        }

        Date updatedDate = parseMessageDate(item.getUpdatedDate());
        if (updatedDate != null) {
            return updatedDate;
        }

        return parseMessageDate(item.getCreatedDate());
    }

    private Date parseMessageDate(String value) {
        if (isBlank(value)) {
            return null;
        }

        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(value);
        } catch (ParseException e) {
            return null;
        }
    }

    private boolean isRenderableMessage(MessageListBean.MessageData item) {
        if (item == null) {
            return false;
        }

        return !isBlankSafe(item.getSeventy())
                || !isBlankSafe(item.getSubject())
                || !isBlankSafe(item.getContextData())
                || !isBlankSafe(item.getId())
                || !isBlankSafe(item.getParentId())
                || !isBlankSafe(item.getCreatedDate());
    }

    private boolean isBlankSafe(String value) {
        if (value == null || value.trim().isEmpty()) {
            return true;
        }

        try {
            // if this field is hex, try converting safely
            String decoded = Utils.hexToASCII(value);
            return decoded.trim().isEmpty();
        } catch (Exception e) {
            // if conversion fails, treat original value
            return value.trim().isEmpty();
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty() || "null".equalsIgnoreCase(value.trim());
    }

    private String getCurrentMerchantId() {
        if (!isBlank(merchantId)) {
            return merchantId;
        }

        Activity activity = getActivity();
        if (activity == null) {
            return "";
        }

        return ATPreferences.readString(activity, Constants.MERCHANT_ID);
    }

    private int getMerchantUnreadCount(UnreadMerchantMessages unreadMerchantMessages, String currentMerchantId) {
        if (unreadMerchantMessages == null || isBlank(currentMerchantId)) {
            return 0;
        }

        List<RESULTItem> outerResults = unreadMerchantMessages.getRESULT();
        if (outerResults == null || outerResults.isEmpty()) {
            return 0;
        }

        for (RESULTItem outerItem : outerResults) {
            if (outerItem == null || outerItem.getRESULT() == null) {
                continue;
            }

            for (RESULTItem merchantItem : outerItem.getRESULT()) {
                if (merchantItem == null) {
                    continue;
                }

                if (isMatchingMerchantId(currentMerchantId, merchantItem.getJsonMember114179())) {
                    try {
                        return Integer.parseInt(merchantItem.getJsonMember114121());
                    } catch (NumberFormatException e) {
                        return 0;
                    }
                }
            }
        }

        return 0;
    }

    private boolean isMatchingMerchantId(String currentMerchantId, String responseMerchantId) {
        if (isBlank(currentMerchantId) || isBlank(responseMerchantId)) {
            return false;
        }

        String localMerchantId = currentMerchantId.trim();
        String apiMerchantId = responseMerchantId.trim();

        return localMerchantId.equals(apiMerchantId)
                || localMerchantId.endsWith(apiMerchantId)
                || apiMerchantId.endsWith(localMerchantId);
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

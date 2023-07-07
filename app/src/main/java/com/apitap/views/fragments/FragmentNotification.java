package com.apitap.views.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.apitap.R;
import com.apitap.controller.ModelManager;
import com.apitap.model.Constants;
import com.apitap.model.Operations;
import com.apitap.model.Utils;
import com.apitap.model.bean.NotificationBean;
import com.apitap.model.customclasses.Event;
import com.apitap.views.HomeActivity;
import com.apitap.views.adapters.NotificationAdapter;
import com.apitap.views.customviews.DividerItemDecoration;
import com.apitap.views.fragments.itemDetails.FragmentItemDetails;
import com.apitap.views.fragments.messageDetails.FragmentMessageDetail;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class FragmentNotification extends BaseFragment implements NotificationAdapter.AdapterClick {

    private NotificationAdapter notificationAdapter;
    private ViewHolder holder;
    private List<NotificationBean.RESULTBeanX.RESULTBean> notificationList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notifications, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        holder = new ViewHolder(view);
        tabContainer2Visible();
        // getfocus();
        //
        holder.recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        holder.recycler.addItemDecoration(new DividerItemDecoration(getActivity(), R.drawable.divider_grey));

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        hitApi();
    }

    private void hitApi() {
        showProgress();
        ModelManager.getInstance().getNotificationsManager().getListOfNotifications(getActivity(),
                Operations.getListOfNotifications(getActivity()));
    }

    private void setAdapter() {
        holder.recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        notificationAdapter = new NotificationAdapter(notificationList, this);
        holder.recycler.setAdapter(notificationAdapter);
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(int position) {
        if (!notificationList.get(position).get_122_114().isEmpty()) {
            if (!notificationList.get(position).get_114_144().isEmpty()) {
                Bundle bundle = new Bundle();
                bundle.putString("merchantName", Utils.hexToASCII(notificationList.get(position).get_114_70()));
                bundle.putString("merchantId", notificationList.get(position).get_114_179());
                bundle.putString("productId", Utils.getElevenDigitId(notificationList.get(position).get_114_144()));
                bundle.putString("productName", notificationList.get(position).get_122_128());
                ((HomeActivity) getActivity()).displayView(new FragmentMessageDetail(),
                        Constants.MessageDetailPage, bundle);

            } else if (!notificationList.get(position).get_123_21().isEmpty()) {
                Bundle bundle = new Bundle();
                bundle.putString("merchantName", Utils.hexToASCII(notificationList.get(position).get_114_70()));
                bundle.putString("merchantId", notificationList.get(position).get_114_179());
                bundle.putString("adID", Utils.getElevenDigitId(notificationList.get(position).get_123_21()));
                bundle.putString("adName", notificationList.get(position).get_120_83());
                ((HomeActivity) getActivity()).displayView(new FragmentMessageDetail(),
                        Constants.MessageDetailPage, bundle);


            } else if (!notificationList.get(position).get_121_75().isEmpty()) {
                Bundle bundle = new Bundle();
                bundle.putString("invoice", notificationList.get(position).get_121_75());
                ((HomeActivity) getActivity()).displayView(new FragmentMessageDetail(),
                        Constants.MessageDetailPage, bundle);

            } else {
                Bundle bundle = new Bundle();
                bundle.putString("merchantName", Utils.hexToASCII(notificationList.get(position).get_114_70()));
                bundle.putString("merchantId", Utils.hexToASCII(notificationList.get(position).get_122_114()));
                bundle.putString("generalId", notificationList.get(position).get_122_114());
                ((HomeActivity) getActivity()).displayView(new FragmentMessageDetail(),
                        Constants.MessageDetailPage, bundle);
            }
        } else if (!notificationList.get(position).get_114_144().isEmpty()) {
            String productId = Utils.lengtT(11, notificationList.get(0).get_114_144());
            String productType = "21";
            Bundle bundle = new Bundle();
            bundle.putString("productId", productId);
            bundle.putString("productType", productType);
            FragmentItemDetails fragment = new FragmentItemDetails();
            fragment.setArguments(bundle);
            ((HomeActivity) getActivity()).displayView(fragment, Constants.TAG_DETAILSPAGE, bundle);


        }
    }

    private class ViewHolder implements View.OnClickListener {

        private final RecyclerView recycler;
        private final RelativeLayout relativeLayoutRoot;
        private final TextView tvNoNotifications;
        private LinearLayout backll;

        public ViewHolder(View view) {
            recycler = view.findViewById(R.id.rv_notifications);
            tvNoNotifications = view.findViewById(R.id.nomsgs);
            backll = view.findViewById(R.id.back_ll);
            relativeLayoutRoot = view.findViewById(R.id.rootLayout);
            backll.setOnClickListener(this);


        }

        @Override
        public void onClick(View view) {
            getActivity().startActivity(new Intent(getActivity(), HomeActivity.class));
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

    @Subscribe
    public void onEvent(final Event event) {
        switch (event.getKey()) {
            case Constants.NOTIFICATION_LIST:
                hideProgress();
                notificationList = ModelManager.getInstance().getNotificationsManager().notificationBean.getRESULT().get(0).getRESULT();
                setAdapter();
                break;
            case Constants.GET_SERVER_ERROR:
                hideProgress();
                Utils.baseshowFeedbackMessage(getActivity(), holder.relativeLayoutRoot, "Server Error, Please try again.");

                break;
            case Constants.NOTIFICATION_LIST_EMPTY:
                hideProgress();
                holder.tvNoNotifications.setVisibility(View.VISIBLE);
                break;
        }
    }


    public void tabContainer2Visible() {
        //homeTab2.setBackgroundColor(getActivity().getResources().getColor(R.color.colorWhite));
        //imageViewHomeTab2.setImageResource(R.drawable.ic_icon_home);

        HomeActivity.tabContainer2.setVisibility(View.GONE);
        HomeActivity.tabContainer2.setVisibility(View.GONE);
    }

    public void clearfocus() {
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    public void getfocus() {
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

}

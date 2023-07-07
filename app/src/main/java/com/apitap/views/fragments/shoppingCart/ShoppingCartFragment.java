package com.apitap.views.fragments.shoppingCart;

/**
 * Created by Ravi on 29/07/15.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.apitap.R;
import com.apitap.controller.ModelManager;
import com.apitap.model.Constants;
import com.apitap.model.Operations;
import com.apitap.model.customclasses.Event;
import com.apitap.views.HomeActivity;
import com.apitap.views.adapters.ShoppingAdapter;
import com.apitap.views.fragments.BaseFragment;
import com.github.rahatarmanahmed.cpv.CircularProgressView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import static com.apitap.App.isGuest;


public class ShoppingCartFragment extends BaseFragment implements View.OnClickListener {

    ListView mList;
    ViewPager mViewPager;
    private LinearLayout backll;
    private TextView textViewEmptyCart;


    public ShoppingCartFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_shopping_cart, container, false);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        showProgress();
        getfocus();
        tabContainer2Visible();
        ModelManager.getInstance().getShoppingCartManager().getShoppingCarts(getActivity(), Operations.makeJsonGetShoppingCart(getActivity()));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (isGuest) {
            ModelManager.getInstance().getLoginManager().guestLastActivity(getActivity(), Operations.makeJsonLastActivityByGuest(getActivity()));
        }
    }

    private void initViews(View v) {
        mList = (ListView) v.findViewById(R.id.list);
        backll = (LinearLayout) v.findViewById(R.id.back_ll);
        textViewEmptyCart = v.findViewById(R.id.textViewEmptyCart);
        backll.setOnClickListener(this);
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
    public void onEvent(Event event) {
        switch (event.getKey()) {
            case Constants.SHOPPING_SUCCESS:
                clearfocus();
                hideProgress();
                mList.setAdapter(new ShoppingAdapter( getActivity(), 0, ModelManager.getInstance().getShoppingCartManager().shoopingArray));
                break;

            case Constants.ITEM_DELETED:
                showProgress();
                ModelManager.getInstance().getShoppingCartManager().getShoppingCarts(getActivity(), Operations.makeJsonGetShoppingCart(getActivity()));
                break;
            case Constants.GUEST_ACTIVITY_SUCCESS:

                break;
            case Constants.GUEST_ACTIVITY_TIMEOUT:

                break;
            case -1:
                clearfocus();
                hideProgress();
                textViewEmptyCart.setVisibility(View.VISIBLE);
                mList.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
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

    @Override
    public void onClick(View view) {
        getActivity().startActivity(new Intent(getActivity(), HomeActivity.class));
    }
}

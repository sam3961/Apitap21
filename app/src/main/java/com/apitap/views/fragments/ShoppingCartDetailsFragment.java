package com.apitap.views.fragments;

/**
 * Created by Ashok on 29/07/15.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.apitap.R;
import com.apitap.controller.ModelManager;
import com.apitap.model.Constants;
import com.apitap.model.ItemClickSupport;
import com.apitap.model.Operations;
import com.apitap.model.bean.ShoppingCompBean;
import com.apitap.model.customclasses.Event;
import com.apitap.views.PaymentActivity;
import com.apitap.views.SelectAddressScreen;

import com.github.rahatarmanahmed.cpv.CircularProgressView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class ShoppingCartDetailsFragment extends Fragment {

    String companyName;
    CircularProgressView mPocketBar;
    private RecyclerView recycler;
    private Button btn_selectaddress;

    public ShoppingCartDetailsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_shopping_cart1, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        mPocketBar.setVisibility(View.VISIBLE);

        Bundle b = getArguments();
        if (b != null) {
            companyName = b.getString("companyName");
            ShoppingCompBean shoppingCompBean = (ShoppingCompBean) b.getSerializable("shoppingCart");
            ModelManager.getInstance().getShoppingCartItemManager().getShoppingCartItems(getActivity(), Operations.makeJsonGetShoppingCartItem(getActivity(), shoppingCompBean,""));
        }

        ItemClickSupport.addTo(recycler)
                .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {

                        startActivity(new Intent(getActivity(), PaymentActivity.class));
                    }
                });
    }

    private void initViews(View v) {
        btn_selectaddress = (Button) v.findViewById(R.id.btn_selectaddress);
        recycler = (RecyclerView) v.findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        // recycler.addItemDecoration(new DividerItemDecoration(getActivity(), R.drawable.divider));
        mPocketBar = (CircularProgressView) v.findViewById(R.id.pocket);

        btn_selectaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getActivity(), SelectAddressScreen.class));

            }
        });


//        RecyclerView.ItemDecoration dividerItemDecoration = new DividerItemDecoration(recycler.getContext(),
//                new LinearLayoutManager(getActivity()).getOrientation());
//        recycler.addItemDecoration(dividerItemDecoration);

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
            case Constants.SHOPPING_DETAIL_SUCCESS:
                mPocketBar.setVisibility(View.GONE);
              //  recycler.setAdapter(new ShoppingCartDetailActivity.CartItemAdapter(getActivity(), companyName, ModelManager.getInstance().getShoppingCartItemManager().detailBean.getRESULT().get(0).getRESULT()));
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

}

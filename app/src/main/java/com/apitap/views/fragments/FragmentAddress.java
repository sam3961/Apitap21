package com.apitap.views.fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apitap.R;
import com.apitap.controller.ModelManager;
import com.apitap.model.Constants;
import com.apitap.model.Operations;
import com.apitap.model.customclasses.Event_Address;
import com.apitap.views.AddressDetailActivity;
import com.apitap.views.adapters.AddressAdapter;
import com.apitap.views.customviews.VerticalSpaceItemDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBarUtils;
import fr.castorflex.android.smoothprogressbar.SmoothProgressDrawable;


public class FragmentAddress extends Fragment implements View.OnClickListener {


    private ViewHolder holder;
    private LinearLayout llCurrent;
    private CheckBox cbCurrent;

    public FragmentAddress() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_address, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        holder = new ViewHolder(view);
        llCurrent = (LinearLayout) view.findViewById(R.id.ll_current);
        cbCurrent = (CheckBox) view.findViewById(R.id.cb_current);

        llCurrent.setOnClickListener(this);
        cbCurrent.setOnClickListener(this);

        holder.recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        holder.recycler.setNestedScrollingEnabled(false);
        holder.recycler.addItemDecoration(new VerticalSpaceItemDecoration(20));
        holder.txt_addnew.setOnClickListener(this);
        holder.mPocketBar.progressiveStart();
        ModelManager.getInstance().getAddressManager().getAddresses(getActivity(), Operations.makeJsonGetAddress(getActivity()), Constants.GET_ADDRESS_SUCCESS);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtaddnew:
                startActivityForResult(new Intent(getActivity(), AddressDetailActivity.class).putExtra("ISNew", true), 1);
                break;

            case R.id.ll_current:
                if (cbCurrent.isChecked()) {
                    cbCurrent.setChecked(false);
                } else
                    cbCurrent.setChecked(true);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == getActivity().RESULT_OK) {
            holder.mPocketBar.progressiveStart();
            holder.mPocketBar.setVisibility(View.VISIBLE);
            ModelManager.getInstance().getAddressManager().getAddresses(getActivity(), Operations.makeJsonGetAddress(getActivity()), Constants.GET_ADDRESS_SUCCESS);

        }
    }

    private class ViewHolder {

        private final TextView txt_addnew;
        private final RecyclerView recycler;
        private final SmoothProgressBar mPocketBar;

        public ViewHolder(View view) {
            txt_addnew = (TextView) view.findViewById(R.id.txtaddnew);
            recycler = (RecyclerView) view.findViewById(R.id.recycler);
            mPocketBar = (SmoothProgressBar) view.findViewById(R.id.pocket);

            mPocketBar.setSmoothProgressDrawableBackgroundDrawable(
                    SmoothProgressBarUtils.generateDrawableWithColors(
                            getResources().getIntArray(R.array.pocket_background_colors),
                            ((SmoothProgressDrawable) mPocketBar.getIndeterminateDrawable()).getStrokeWidth()));
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
    public void onEvent(final Event_Address event) {
        switch (event.getKey()) {
            case Constants.GET_ADDRESS_SUCCESS:

                if (event.getResponse() != null) {
                    AddressAdapter adp = new AddressAdapter(event.getResponse());
                    holder.recycler.setAdapter(adp);
                    adp.setOnEditClickListner(new AddressAdapter.AdapterClick() {
                        @Override
                        public void onEditClick(View v, int position) {
                            startActivityForResult(new Intent(getActivity(), AddressDetailActivity.class).putExtra("ISNew", false)
                                    .putExtra("data", event.getResponse().get(position)), 1);

                        }
                    });
                }
                holder.mPocketBar.progressiveStop();
                holder.mPocketBar.setVisibility(View.INVISIBLE);


                break;
        }
    }

}

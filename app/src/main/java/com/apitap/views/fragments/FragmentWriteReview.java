package com.apitap.views.fragments;


import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import com.apitap.R;
import com.apitap.controller.ModelManager;
import com.apitap.model.Constants;
import com.apitap.model.Operations;
import com.apitap.model.Utils;
import com.apitap.model.customclasses.Event;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;


public class FragmentWriteReview extends DialogFragment {


    private ViewHolder viewHolder;

    public FragmentWriteReview() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return inflater.inflate(R.layout.fragment_fragment_write_review, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewHolder = new ViewHolder(view);

        viewHolder.btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = viewHolder.edit_title.getText().toString().trim();
                String desc = viewHolder.edit_desc.getText().toString().trim();
                String rating = getRatingName(viewHolder.ratingBar.getProgress());

                ModelManager.getInstance().getMerchantManager().writeReview(getActivity(),
                        Operations.makeJsonGetWriteReview(getActivity(), "0001202A000000000858", title, desc, rating),
                        Constants.WRITE_REVIEW_SUCCESS);

            }
        });
    }

    private class ViewHolder {

        private final EditText edit_title, edit_desc;
        private final Button btn_submit;
        private final RatingBar ratingBar;

        public ViewHolder(View view) {
            edit_title = (EditText) view.findViewById(R.id.edit_title);
            edit_desc = (EditText) view.findViewById(R.id.edit_desc);
            btn_submit = (Button) view.findViewById(R.id.btn_submit);
            ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);

        }

    }

    private String getRatingName(int rating) {
        if (rating == 1) {
            return "2101";
        } else if (rating == 2) {
            return "2102";
        } else if (rating == 3) {
            return "2103";
        } else if (rating == 4) {
            return "2104";
        } else {
            return "2105";
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(Event event) {
        switch (event.getKey()) {
            case Constants.WRITE_REVIEW_SUCCESS:
                Utils.showToast(getActivity(), "Send");
                dismiss();
                break;

        }
    }

}

package com.apitap.views.fragments.checkinTv;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.apitap.R;
import com.apitap.model.Constants;
import com.apitap.views.HomeActivity;
import com.apitap.views.fragments.BaseFragment;
import com.apitap.views.fragments.checkinTv.adapter.AdapterStreamDirectory;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentStreamDirectory extends BaseFragment implements AdapterStreamDirectory.OnItemClickListener {

    private RecyclerView recyclerViewDirectory;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stream_directory, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerViewDirectory = view.findViewById(R.id.rvStreamDirectory);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setAdapter();
    }

    private void setAdapter() {
        AdapterStreamDirectory adapterStreamDirectory = new AdapterStreamDirectory(requireContext(),
                new ArrayList(),this);
        recyclerViewDirectory.setAdapter(adapterStreamDirectory);
    }

    @Override
    public void onItemClick(int position) {
        ((HomeActivity) getActivity()).displayView(new FragmentStreamDetail(), Constants.TAG_STREAM_DIRECTORY_DETAIL, new Bundle());
    }
}

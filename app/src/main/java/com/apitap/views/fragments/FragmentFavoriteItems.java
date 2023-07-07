package com.apitap.views.fragments;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apitap.R;
import com.apitap.controller.ModelManager;
import com.apitap.model.bean.FavBeanOld;
import com.apitap.model.bean.Favdetailsbean;
import com.apitap.model.bean.ImagesBean;
import com.apitap.views.adapters.FavouriteItemGroupAdapter;
import com.github.rahatarmanahmed.cpv.CircularProgressView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by apple on 11/08/16.
 */
public class FragmentFavoriteItems extends Fragment implements View.OnClickListener {

    private RecyclerView recyclerView;
    TextView mHeader;
    LinearLayout  back_ll;
    ArrayList<ImagesBean> allImages;
    int position;
    ArrayList<String> arraylist;
    private CircularProgressView mPocketBar;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_all_favs, container, false);

        initViews(v);
        setListener();


        setAdapter();
        return v;
    }


    private void initViews(View v) {
        recyclerView = v.findViewById(R.id.recyclerView);
        mHeader = v.findViewById(R.id.header);
        back_ll = v.findViewById(R.id.back_ll);
        mPocketBar = v.findViewById(R.id.pocket);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        mHeader.setText("Favorite Products");

    }

    private void setListener() {
        back_ll.setOnClickListener(this);
    }


    private void setAdapter() {
        List<FavBeanOld.RESULT> response = ModelManager.getInstance().getFavouriteManager().favBean.getRESULT();
        HashMap<Integer, ArrayList<Favdetailsbean>> map = ModelManager.getInstance().getFavouriteManager().itemsData;
        FavouriteItemGroupAdapter favouriteItemGroupAdapter = new FavouriteItemGroupAdapter(getActivity(), map, response,"");
        recyclerView.setAdapter(favouriteItemGroupAdapter);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_ll:
                if (getFragmentManager().getBackStackEntryCount() > 0)
                    getFragmentManager().popBackStack();
                break;

        }
    }




}

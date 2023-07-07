package com.apitap.views.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apitap.R;
import com.apitap.controller.ModelManager;
import com.apitap.model.bean.FavBeanSpecial;
import com.apitap.model.bean.FavMerchantBean;
import com.apitap.model.bean.Favspecialbean;
import com.apitap.model.bean.ImagesBean;
import com.apitap.views.adapters.FavouriteSpecialGroupAdapter;
import com.apitap.views.adapters.FavouriteStoresGroupAdapter;
import com.github.rahatarmanahmed.cpv.CircularProgressView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by apple on 11/08/16.
 */
public class FragmentFavoriteStores extends Fragment implements View.OnClickListener {

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

        mHeader.setText("Favorite Stores");
    }

    private void setListener() {
        back_ll.setOnClickListener(this);
    }


    private void setAdapter() {
//        List<FavBeanSpecial.RESULT> response_special = ModelManager.getInstance().getFavouriteManager().favBeanSpecial.getRESULT();
  //      HashMap<Integer, ArrayList<Favspecialbean>> map_special = ModelManager.getInstance().getFavouriteManager().itemsDataSpecial;
        List<FavMerchantBean.RESULT> result = ModelManager.getInstance().getFavouriteManager().favMerchantBean.getRESULT();

        FavouriteStoresGroupAdapter favouriteStoresGroupAdapter = new FavouriteStoresGroupAdapter(getActivity(),result);
        recyclerView.setAdapter(favouriteStoresGroupAdapter);

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

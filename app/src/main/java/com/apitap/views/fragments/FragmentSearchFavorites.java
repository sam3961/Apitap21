package com.apitap.views.fragments;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.apitap.R;
import com.apitap.controller.ModelManager;
import com.apitap.model.bean.SearchFavoritesBean;
import com.apitap.views.adapters.SearchFavoritesAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sourcefuse on 15/12/16.
 */

public class FragmentSearchFavorites extends Fragment {
    ListView list;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search_favorites, container, false);
        initViews(v);
        setAdapter();
        return v;
    }

    private void initViews(View v) {
        list = (ListView) v.findViewById(R.id.list);
    }

    private void setAdapter(){
        List<SearchFavoritesBean.RESULT.RESULTDATA> searchFavoritesBeen =new ArrayList<>();
        searchFavoritesBeen = ModelManager.getInstance().getSearchFavoritesManager().searchFavoritesBean.getResult().get(0).getResult();
        if (searchFavoritesBeen.size()>0){
        SearchFavoritesAdapter favoritesAdapter=new SearchFavoritesAdapter(getActivity(),0, ModelManager.getInstance().getSearchFavoritesManager().searchFavoritesBean.getResult().get(0).getResult());
        list.setAdapter(favoritesAdapter);}
    }
}

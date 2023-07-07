package com.apitap.views.fragments.favourite;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;

import com.apitap.views.fragments.BaseFragment;
import com.apitap.views.fragments.FragmentFavoriteItems;
import com.apitap.views.fragments.FragmentFavoriteSpecials;

import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.apitap.R;
import com.apitap.controller.ModelManager;
import com.apitap.model.Constants;
import com.apitap.model.Operations;
import com.apitap.model.Utils;
import com.apitap.model.ViewPagerCustomDuration;
import com.apitap.model.bean.AdsDetailWithMerchant;
import com.apitap.model.bean.FavBeanOld;
import com.apitap.model.bean.FavBeanSpecial;
import com.apitap.model.bean.FavMerchantBean;
import com.apitap.model.bean.Favdetailsbean;
import com.apitap.model.bean.FavouriteItemBean;
import com.apitap.model.bean.FavouriteSpecialBean;
import com.apitap.model.bean.Favspecialbean;
import com.apitap.model.customclasses.Event;
import com.apitap.views.HomeActivity;
import com.apitap.views.fragments.FragmentFavoriteStores;
import com.apitap.views.fragments.favourite.adapter.FavouriteItemAdapter;
import com.apitap.views.fragments.favourite.adapter.FavouriteMerchantAdapter;
import com.apitap.views.fragments.favourite.adapter.FavouriteSpecialAdapter;
import com.apitap.views.adapters.SamplePagerAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;

public class FragmentFavourite extends BaseFragment {

    private ViewHolder holder;
    FavouriteItemAdapter adp;
    FavouriteSpecialAdapter adp2;
    FavouriteMerchantAdapter fav_adp;
    int state_Ads = 0, state_Items = 0, state_Store = 0, state_Special = 0;
    ArrayList<String> spinnerArrayList;
    private String selected_sort = "";
    private boolean isSpecialsItemsEmpty;
    private boolean isMerchantsEmpty;
    private boolean isAdsEmpty;
    private boolean isSpecialEmpty;
    private boolean isItemsEmpty;
    private Dialog reloadDialog;
    private boolean firstTimeLoad;
    private Dialog businessSelectionDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_fragment_favourite, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        holder = new ViewHolder(view);

        spinnerArrayList = new ArrayList<>();
        spinnerArrayList.add("All");

        ModelManager.getInstance().getFavouriteManager().getFavourites(getActivity(),
                Operations.makeJsonGetFavourite(getActivity(), ""), Constants.GET_FAVOURITE_SUCCESS);

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        firstTimeLoad = true;
        showProgress();
    }

    private class ViewHolder implements View.OnClickListener {

        TextView mTabAds, mTabSpecial, mTabItems, mTabSaved, textViewNoFav;
        private final RecyclerView recyclerItems, recycler_Store, recycler_Special;
        private ViewPagerCustomDuration viewPager;
        private CircleIndicator circleIndicator;
        private LinearLayout linearLayout_Ads, linearLayout_stores, linearLayout_items, linearLayout_special;
        private LinearLayout main_ll;
        ImageView img_Ads, img_Store, img_Items, img_Specials;
        CardView ads_Card;
        Spinner spinner_sort;
        ScrollView rootLayout;
        private TextView tvBusinessTitle, textViewShowAllItems, textViewShowAllSpecials, textViewShowAllStores;
        private CardView cardViewAds;

        public ViewHolder(View view) {
            storeFrontTabsView();
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
            LinearLayoutManager layoutManager1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
            recyclerItems = view.findViewById(R.id.recycler);
            recycler_Store = view.findViewById(R.id.recyclerStore);
            recycler_Special = view.findViewById(R.id.recyclerSpecial);
            mTabAds = view.findViewById(R.id.tab_ads);
            tvBusinessTitle = view.findViewById(R.id.title);
            mTabSpecial = view.findViewById(R.id.tab_special);
            mTabItems = view.findViewById(R.id.tab_items);
            mTabSaved = view.findViewById(R.id.tab_saved);
            circleIndicator = view.findViewById(R.id.indicator_default);
            viewPager = view.findViewById(R.id.viewpager);
            linearLayout_Ads = view.findViewById(R.id.lin_Ads);
            linearLayout_items = view.findViewById(R.id.lin_Items);
            linearLayout_stores = view.findViewById(R.id.title_lin_merchant);
            linearLayout_special = view.findViewById(R.id.lin_special);
            textViewShowAllItems = view.findViewById(R.id.showallitems);
            textViewShowAllSpecials = view.findViewById(R.id.showallspecials);
            textViewShowAllStores = view.findViewById(R.id.showallstores);
            main_ll = view.findViewById(R.id.linear_main);
            img_Ads = view.findViewById(R.id.img_Ads);
            img_Store = view.findViewById(R.id.img_Stores);
            img_Specials = view.findViewById(R.id.img_Special);
            img_Items = view.findViewById(R.id.img_Items);
            ads_Card = view.findViewById(R.id.ll_ads);
            spinner_sort = view.findViewById(R.id.spinner_sort);
            cardViewAds = view.findViewById(R.id.ll_ads);
            rootLayout = view.findViewById(R.id.rootLayout);
            textViewNoFav = view.findViewById(R.id.textViewNoFav);

            reloadDialog = Utils.showReloadDialog(getActivity());

            linearLayout_Ads.setOnClickListener(this);
            linearLayout_items.setOnClickListener(this);
            linearLayout_stores.setOnClickListener(this);
            textViewShowAllStores.setOnClickListener(this);
            linearLayout_special.setOnClickListener(this);
            textViewShowAllSpecials.setOnClickListener(this);
            textViewShowAllItems.setOnClickListener(this);

            recyclerItems.setNestedScrollingEnabled(false);
            recyclerItems.setLayoutManager(layoutManager);

            recycler_Special.setNestedScrollingEnabled(false);
            recycler_Special.setLayoutManager(layoutManager1);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {

                case R.id.lin_Ads:

                    if (state_Ads == 0) {
                        state_Ads = 1;
                        img_Ads.setImageResource(R.drawable.ic_icon_downarrow);
                        ads_Card.setVisibility(View.GONE);
                    } else if (state_Ads == 1) {
                        state_Ads = 0;
                        img_Ads.setImageResource(R.drawable.ic_icon_uparrow);
                        ads_Card.setVisibility(View.VISIBLE);
                    }
                    break;
                case R.id.lin_Items:
                    if (state_Items == 0) {
                        state_Items = 1;
                        recyclerItems.setVisibility(View.GONE);
                        img_Items.setImageResource(R.drawable.ic_icon_downarrow);
                    } else if (state_Items == 1) {
                        state_Items = 0;
                        recyclerItems.setVisibility(View.VISIBLE);
                        img_Items.setImageResource(R.drawable.ic_icon_uparrow);
                    }
                    break;
                case R.id.lin_special:
                    if (state_Special == 0) {
                        state_Special = 1;
                        recycler_Special.setVisibility(View.GONE);
                        img_Specials.setImageResource(R.drawable.ic_icon_downarrow);
                    } else if (state_Special == 1) {
                        state_Special = 0;
                        recycler_Special.setVisibility(View.VISIBLE);
                        img_Specials.setImageResource(R.drawable.ic_icon_uparrow);
                    }
                    break;
                case R.id.title_lin_merchant:
                    if (state_Store == 0) {
                        state_Store = 1;
                        recycler_Store.setVisibility(View.GONE);
                        img_Store.setImageResource(R.drawable.ic_icon_downarrow);
                    } else if (state_Store == 1) {
                        state_Store = 0;
                        recycler_Store.setVisibility(View.VISIBLE);
                        img_Store.setImageResource(R.drawable.ic_icon_uparrow);
                    }
                    break;

                case R.id.showallitems:
                    ((HomeActivity) getActivity()).displayView(new FragmentFavoriteItems(), "FAV_ITEMS", new Bundle());
                    break;

                case R.id.showallspecials:
                    ((HomeActivity) getActivity()).displayView(new FragmentFavoriteSpecials(), "FAV_SPECIALS", new Bundle());
                    break;
                case R.id.showallstores:
                    ((HomeActivity) getActivity()).displayView(new FragmentFavoriteStores(), "FAV_STORES", new Bundle());
                    break;
            }
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

            case Constants.GET_FAVOURITE_SUCCESS:
                HashMap<Integer, ArrayList<Favdetailsbean>> map = ModelManager.getInstance().getFavouriteManager().itemsData;
//Gson Data which is set On Server

                for (int i = 0; i < map.size(); i++) {
                    if (!spinnerArrayList.contains(map.get(i).get(0).getBusiness_type())) {
                        spinnerArrayList.add(map.get(i).get(0).getBusiness_type());
                    }
                }

                hideProgress();
                holder.main_ll.setVisibility(View.VISIBLE);
                clearfocus();
                break;
            case Constants.REMOVE_FAVOURITE_SUCCESS:

                Utils.showToast(getActivity(), "Removed");
                ModelManager.getInstance().getFavouriteManager().getFavourites(getActivity(),
                        Operations.makeJsonGetFavourite(getActivity(), ""), Constants.GET_FAVOURITE_SUCCESS);

                break;
            case Constants.GET_FAVOURITE_MERCHNAT_SUCCESS:
                try {
                    List<FavMerchantBean.RESULT> result = ModelManager.getInstance().getFavouriteManager().favMerchantBean.getRESULT();
                    for (int i = 0; i < result.size(); i++) {
                        for (int j = 0; j < result.get(i).getCU().size(); j++) {
                            if (!spinnerArrayList.contains(result.get(i).getCU().get(j).get_12083())) {
                                spinnerArrayList.add(result.get(i).getCU().get(j).get_12083());
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                setSpinnerAdapter();
                break;
            case Constants.ADS_LISTING_SUCCESS:
                showAdsWithAnimation();
                break;
            case Constants.GET_FAVOURITE_MERCHNAT_EMPTY:
                isMerchantsEmpty = true;
                HashMap<Integer, ArrayList<Favdetailsbean>> itemsDatas = ModelManager.getInstance().getFavouriteManager().itemsData;
                HashMap<Integer, ArrayList<Favspecialbean>> itemsDataSpecial = ModelManager.getInstance().getFavouriteManager().itemsDataSpecial;

                if (itemsDatas.size() > 0 || itemsDataSpecial.size() > 0)
                    setSpinnerAdapter();

                break;
            case Constants.NO_SPECIAL_ITEMS_FAVOURITES:
                isSpecialsItemsEmpty = true;
                break;
            case Constants.ADS_FAVOURITES_EMPTY:
                HashMap<Integer, ArrayList<Favdetailsbean>> itemsData = ModelManager.getInstance().getFavouriteManager().itemsData;
                HashMap<Integer, ArrayList<Favspecialbean>> specialData = ModelManager.getInstance().getFavouriteManager().itemsDataSpecial;
                isAdsEmpty = true;
                hideProgress();
                clearfocus();
                if (isSpecialsItemsEmpty && isMerchantsEmpty && itemsData.size() == 0 && specialData.size() == 0) {
                    holder.textViewNoFav.setVisibility(View.VISIBLE);
                }
                break;
            case Constants.GET_FAVOURITE_ITEM_EMPTY:
                isItemsEmpty = true;
                break;
            case Constants.GET_FAVOURITE_SPECIAL_EMPTY:
                isSpecialEmpty = true;
                break;
            case -1:
                hideProgress();
                clearfocus();
                relaodDialogShow();
                break;

        }

    }

    private void relaodDialogShow() {
        TextView textView_yes = reloadDialog.findViewById(R.id.txtok);
        TextView textView_no = reloadDialog.findViewById(R.id.txtcancel);
        TextView textView_title = reloadDialog.findViewById(R.id.txtmessage);

        textView_title.setText("We're sorry but there seems to be some network issues connecting to the server. Please try again.");
        textView_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reloadDialog.dismiss();
                reloadFragment();

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

    private void setItemsFavoriteAdapter() {
        List<FavouriteItemBean> favouriteItemBeans = new ArrayList<>();
        if (ModelManager.getInstance().getFavouriteManager().favBean != null
                && ModelManager.getInstance().getFavouriteManager().favBean.getRESULT() != null) {
            List<FavBeanOld.RESULT> response = ModelManager.getInstance().getFavouriteManager().favBean.getRESULT();
            HashMap<Integer, ArrayList<Favdetailsbean>> map = ModelManager.getInstance().getFavouriteManager().itemsData;
//Gson Data which is set On Server

            for (int i = 0; i < response.size(); i++) {
                for (int j = 0; j < ModelManager.getInstance().getFavouriteManager().favBean.getRESULT().get(i).getPC().size(); j++) {
                    FavouriteItemBean favouriteItemBean = new FavouriteItemBean();
                    favouriteItemBean.setDescription(Utils.hexToASCII(ModelManager.getInstance().getFavouriteManager().favBean.getRESULT().get(i).
                            getPC().get(j).get12083()));
                    favouriteItemBean.setId(Utils.lengtT(11, ModelManager.getInstance().getFavouriteManager().favBean.getRESULT().get(i).
                            getPC().get(j).get114144()));
                    favouriteItemBean.setproduct_type(ModelManager.getInstance().getFavouriteManager().favBean.getRESULT().get(i).
                            getPC().get(j).get114112());
                    favouriteItemBean.setActualPrice(ModelManager.getInstance().getFavouriteManager().favBean.getRESULT().get(i).
                            getPC().get(j).get11498());
                    favouriteItemBean.setDiscountedPrice(ModelManager.getInstance().getFavouriteManager().favBean.getRESULT().get(i).
                            getPC().get(j).get122158());
                    favouriteItemBean.setSeen(ModelManager.getInstance().getFavouriteManager().favBean.getRESULT().get(i).
                            getPC().get(j).get1149());
                    favouriteItemBean.setImageUrl(ModelManager.getInstance().getFavouriteManager().favBean.getRESULT().get(i).
                            getPC().get(j).get121170());
                    favouriteItemBeans.add(favouriteItemBean);

                }
            }
            if (map != null && map.size() > 0) {
                holder.main_ll.setVisibility(View.VISIBLE);
                adp = new FavouriteItemAdapter(getActivity(), favouriteItemBeans, selected_sort);
                holder.recyclerItems.setAdapter(adp);
            }
        } else {
            holder.linearLayout_items.setVisibility(View.GONE);
            holder.textViewShowAllItems.setVisibility(View.GONE);
            holder.recyclerItems.setVisibility(View.GONE);

        }
    }

    private void setSpecialFavoriteAdapter() {

        if (ModelManager.getInstance().getFavouriteManager().favBeanSpecial != null &&
                ModelManager.getInstance().getFavouriteManager().favBeanSpecial.getRESULT() != null) {
            List<FavBeanSpecial.RESULT> response_special = ModelManager.getInstance().getFavouriteManager().favBeanSpecial.getRESULT();

            HashMap<Integer, ArrayList<Favspecialbean>> map_special = ModelManager.getInstance().getFavouriteManager().itemsDataSpecial;

            ArrayList<FavouriteSpecialBean> favouriteSpecialBeans = new ArrayList<>();

            for (int i = 0; i < response_special.size(); i++) {
                for (int j = 0; j < ModelManager.getInstance().getFavouriteManager().favBeanSpecial.getRESULT().get(i).getPC().size(); j++) {
                    FavouriteSpecialBean favouriteItemBean = new FavouriteSpecialBean();
                    favouriteItemBean.setDescription(Utils.hexToASCII(ModelManager.getInstance().getFavouriteManager().favBeanSpecial.getRESULT().get(i).
                            getPC().get(j).get12083()));
                    favouriteItemBean.setId(Utils.lengtT(11, ModelManager.getInstance().getFavouriteManager().favBeanSpecial.getRESULT().get(i).
                            getPC().get(j).get114144()));
                    favouriteItemBean.setproduct_type(ModelManager.getInstance().getFavouriteManager().favBeanSpecial.getRESULT().get(i).
                            getPC().get(j).get114112());
                    favouriteItemBean.setActualPrice(ModelManager.getInstance().getFavouriteManager().favBeanSpecial.getRESULT().get(i).
                            getPC().get(j).get11498());
                    favouriteItemBean.setDiscountedPrice(ModelManager.getInstance().getFavouriteManager().favBeanSpecial.getRESULT().get(i).
                            getPC().get(j).get122162());
                    favouriteItemBean.setSeen(ModelManager.getInstance().getFavouriteManager().favBeanSpecial.getRESULT().get(i).
                            getPC().get(j).get1149());
                    favouriteItemBean.setImageUrl(ModelManager.getInstance().getFavouriteManager().favBeanSpecial.getRESULT().get(i).
                            getPC().get(j).get121170());
                    favouriteSpecialBeans.add(favouriteItemBean);

                }
            }

            if (map_special != null && map_special.size() > 0) {
                holder.main_ll.setVisibility(View.VISIBLE);
                adp2 = new FavouriteSpecialAdapter(getActivity(), favouriteSpecialBeans, selected_sort);
                holder.recycler_Special.setAdapter(adp2);
            }
        } else {
            holder.linearLayout_special.setVisibility(View.GONE);
            holder.textViewShowAllSpecials.setVisibility(View.GONE);
        }
    }


    private void setMerchantFavoriteAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        holder.recycler_Store.setLayoutManager(layoutManager);
        List<FavMerchantBean.RESULT> result = ModelManager.getInstance().getFavouriteManager().favMerchantBean.getRESULT();

        if (result.size() > 0) {
            holder.main_ll.setVisibility(View.VISIBLE);

            fav_adp = new FavouriteMerchantAdapter(getActivity(), result, selected_sort);
            holder.recycler_Store.setAdapter(fav_adp);
        } else {
            holder.linearLayout_stores.setVisibility(View.GONE);
            holder.textViewShowAllStores.setVisibility(View.GONE);
        }
    }

    private void setSpinnerAdapter() {
        ArrayAdapter<String> arrayadap = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item_fav, R.id.text, spinnerArrayList);
        holder.spinner_sort.setAdapter(arrayadap);

        holder.spinner_sort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selected_sort = spinnerArrayList.get(i);
                holder.tvBusinessTitle.setText(selected_sort);
                setMerchantFavoriteAdapter();
                setItemsFavoriteAdapter();
                setSpecialFavoriteAdapter();
                showAdsWithAnimation();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void checkFirstTimeLoad(ArrayList<String> businessArraylist) {
        ArrayList<String> tempBusinessList = new ArrayList<>();
        tempBusinessList.addAll(businessArraylist);
        if (firstTimeLoad) {
            tempBusinessList.remove(0);
            firstTimeLoad = false;
            businessSelectionDialog = showBusinessSelectionDialog(tempBusinessList);
            TextView textViewDone = businessSelectionDialog.findViewById(R.id.tvDone);
            textViewDone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.spinner_sort.setSelection(businessSelectedPosition + 1);
                    businessSelectionDialog.dismiss();
                }
            });
        }

    }

    @Override
    public void onBusinessSelect(int position) {
        holder.spinner_sort.setSelection(position + 1);
        businessSelectionDialog.dismiss();
    }

    public void clearfocus() {
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    public void getfocus() {
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }


    Runnable r;
    Handler h;
    int count;

    private void showAdsWithAnimation() {

        if (ModelManager.getInstance().getFavouriteManager().ads != null && ModelManager.getInstance().getFavouriteManager().ads.size() > 0) {
            holder.main_ll.setVisibility(View.VISIBLE);

            count = 0;
            final ArrayList<AdsDetailWithMerchant> adsDetailWithMerchants = ModelManager.getInstance().getFavouriteManager().url_maps1;
            holder.viewPager.setAdapter(new SamplePagerAdapter(getActivity(), ModelManager.getInstance().getFavouriteManager().ads, adsDetailWithMerchants, false, "fav", selected_sort));
            holder.circleIndicator.setViewPager(holder.viewPager);
            holder.viewPager.setCurrentItem(count);
            // viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
            h = new Handler();
            r = new Runnable() {
                @Override
                public void run() {
                    h.removeMessages(0);
                    ++count;
                    if ((count + 1) > ModelManager.getInstance().getFavouriteManager().ads.size())
                        count = 0;

                    holder.viewPager.setCurrentItem(count);
                    h.postDelayed(r, 10000);
                }
            };

            holder.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    count = position;
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
            h.postDelayed(r, 1000);
        } else {
            holder.linearLayout_Ads.setVisibility(View.GONE);
            holder.cardViewAds.setVisibility(View.GONE);
        }
    }
}
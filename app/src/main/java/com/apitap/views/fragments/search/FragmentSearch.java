package com.apitap.views.fragments.search;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.apitap.R;
import com.apitap.controller.ModelManager;
import com.apitap.model.Constants;
import com.apitap.model.Operations;
import com.apitap.model.Utils;
import com.apitap.model.ViewPagerCustomDuration;
import com.apitap.model.bean.AdsDetailWithMerchant;
import com.apitap.model.bean.SearchBean;
import com.apitap.model.bean.SearchSpecialBean;
import com.apitap.model.customclasses.Event;
import com.apitap.model.preferences.ATPreferences;
import com.apitap.views.HomeActivity;
import com.apitap.views.adapters.SamplePagerAdapter;
import com.apitap.views.fragments.itemDetails.FragmentItemDetails;
import com.apitap.views.fragments.search.adapter.SearchItemAdapter;
import com.apitap.views.customviews.HorizontalSpaceItemDecoration;
import com.apitap.views.fragments.BaseFragment;
import com.apitap.views.fragments.messages.FragmentMessages;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.linearlistview.LinearListView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;

/**
 * Created by sourcefuse on 25/11/16.
 */

public class FragmentSearch extends BaseFragment {
    private RecyclerView listView;
    private LinearListView listView2;
    private ArrayList<String> mernchantfavlist = new ArrayList<>();
    private ViewPagerCustomDuration viewPager;
    private CircleIndicator circleIndicator;
    private CardView listlayout1, listlayout2, noItemFound, noSpecialFound, adLayout, noadLayout;
    private LinearLayout viewMain;
    private RelativeLayout relativeLayoutSearchBarStoreFront;
    private List<SearchSpecialBean.RESULT> allImagesItems;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private Activity mActivity;
    private ScrollView scrollView;
    private ArrayList<String> removeDuplicacy = new ArrayList<>();
    private SamplePagerAdapter SamplePagerAdapter;
    private String searchKey = "", merchantId = "", zip = "", brandName = "", rating = "", sort_by = Constants.Alphabetical, isFav = "false";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search, container, false);

        mActivity = getActivity();
        viewPager = v.findViewById(R.id.viewpager);
        circleIndicator = v.findViewById(R.id.indicator_default);
        listlayout1 = v.findViewById(R.id.lin1);
        listlayout2 = v.findViewById(R.id.lin2);
        noItemFound = v.findViewById(R.id.noItemll);
        noSpecialFound = v.findViewById(R.id.noSpecial_ll);
        adLayout = v.findViewById(R.id.ad_ll);
        noadLayout = v.findViewById(R.id.no_Ads);
        viewMain = v.findViewById(R.id.mainview);
        scrollView = v.findViewById(R.id.scroll_view);
        relativeLayoutSearchBarStoreFront = getActivity().findViewById(R.id.search_storefront);

        searchKey = getArguments().getString("key");
        sort_by = getArguments().getString("sortby");
        zip = getArguments().getString("zip");
        brandName = getArguments().getString("brandName");
        rating = getArguments().getString("rating");

        if (getArguments().containsKey("merchantId"))
            merchantId = ATPreferences.readString(getActivity(), Constants.MERCHANT_ID);


        showProgress();
        ModelManager.getInstance().getSearchItemsManager().getAllSearchProduct(getActivity(),
                Operations.makeJsonSearchProduct(getActivity(),
                        Utils.convertStringToHex(searchKey), sort_by,
                        merchantId, zip
                        , brandName, rating));


        listView = v.findViewById(R.id.list);
        listView2 = v.findViewById(R.id.list2);
        listView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        listView.addItemDecoration(new HorizontalSpaceItemDecoration(20));


        return v;
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
            case Constants.SEARCH_ITEM_SUCCESS:
                boolean setAdap = false;
                //   final List<SearchItemBean.Result.ResultData> arrayList = ModelManager.getInstance().getSearchItemsManager().searchItemBean.getResult().get(0).getResult();
                final HashMap<Integer, ArrayList<SearchBean>> map = ModelManager.getInstance().getSearchItemsManager().itemsData;

                if (isFav.equals("true")) {
                    for (int i = 0; i < map.size(); i++) {
                        if (mernchantfavlist.contains(map.get(i).get(0).getSellerName())) {
                            setAdap = true;
                        }
                    }
                }

                if (setAdap || !isFav.equals("true") && map.size() > 0) {
                    SearchItemAdapter searchItemAdapter = new SearchItemAdapter(getActivity(), map);
                    listView.setAdapter(searchItemAdapter);
                    searchItemAdapter.setOnItemClickListner(new SearchItemAdapter.AdapterClick() {
                        @Override
                        public void onItemClick(View v, int position) {
                            String productId = Utils.lengtT(11, map.get(position).get(0).getProductId());
                            String productType = map.get(position).get(0).getProdcutType();
                            Bundle bundle = new Bundle();
                            bundle.putString("productId", productId);
                            bundle.putString("productType", productType);
                            bundle.putString("flag", "search");
                            ((HomeActivity) getActivity()).displayView(new FragmentItemDetails(), Constants.TAG_DETAILSPAGE,
                                    bundle);


                        }
                    });

                } else {
                    noItemFound.setVisibility(View.GONE);
                    listlayout1.setVisibility(View.GONE);
                }


                //circularProgressView.setVisibility(View.GONE);

                break;
            case Constants.ADDRESS_NEARBY_SUCCESS:
                Utils.setRecyclerNearByAdapter(getActivity());
                break;
            case Constants.SEARCH_ITEM_SUCCESS_Empty:

                hideProgress();
                noItemFound.setVisibility(View.GONE);
                listlayout1.setVisibility(View.GONE);
                break;

            case Constants.SEARCH_ITEM_SUCCESS_List2:
                //   circularProgressView.setVisibility(View.GONE);
                setDataSpecial();
                break;
            case Constants.SEARCH_ITEM_SUCCESS_List2_Empty:
                //   circularProgressView.setVisibility(View.GONE);
                listlayout2.setVisibility(View.GONE);
                noSpecialFound.setVisibility(View.VISIBLE);
                break;

            case Constants.GET_MERCHANT_FAVORITES:
                ModelManager.getInstance().getSearchItemsManager().getAllSearchProduct(getActivity(),
                        Operations.makeJsonSearchProduct(getActivity(), searchKey, sort_by, merchantId, zip, brandName, rating));
                mernchantfavlist = ModelManager.getInstance().getMerchantFavouriteManager().mernchantfavlist;
                break;
            case Constants.ADS_LISTING_SUCCESS:
                hideProgress();
                scrollView.setVisibility(View.VISIBLE);
                showAdsWithAnimation();
                break;
            case Constants.ADS_LISTING_EMPTY:
                hideProgress();
                scrollView.setVisibility(View.VISIBLE);
                adLayout.setVisibility(View.GONE);
                noadLayout.setVisibility(View.GONE);
                break;
            case -1:
                hideProgress();
                Toast.makeText(getContext(), "Problem occurred, please try again.", Toast.LENGTH_SHORT).show();
                break;
        }
    }


    Runnable r;
    Handler h;
    int count;

    private void showAdsWithAnimation() {

        if (ModelManager.getInstance().getSearchItemsManager().ads != null)
            if (ModelManager.getInstance().getSearchItemsManager().ads.size() > 0) {

                count = 0;
                final ArrayList<AdsDetailWithMerchant> adsDetailWithMerchants = ModelManager.getInstance().getSearchItemsManager().url_maps1;

                SamplePagerAdapter = new SamplePagerAdapter(getActivity(), ModelManager.getInstance()
                        .getSearchItemsManager().ads, adsDetailWithMerchants, false, "search");
                viewPager.setAdapter(SamplePagerAdapter);
                circleIndicator.setViewPager(viewPager);
                viewPager.setCurrentItem(count);
                // viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
                h = new Handler();
                r = new Runnable() {
                    @Override
                    public void run() {
                        h.removeMessages(0);
                        ++count;
                        if ((count + 1) > ModelManager.getInstance().getSearchItemsManager().ads.size())
                            count = 0;

                        if (SamplePagerAdapter != null) {
                            SamplePagerAdapter.notifyDataSetChanged();
                        }
                        viewPager.setCurrentItem(count);
                        h.postDelayed(r, 10000);
                    }
                };

                viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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

                //  mLlAds.setVisibility(View.GONE);
                //noAdsll.setVisibility(View.VISIBLE);
            }
    }

    private void setDataSpecial() {
        removeDuplicacy = new ArrayList<>();
        List<SearchSpecialBean.RESULT> specialBeanArrayList = ModelManager.getInstance().getSearchItemsManager().searchSpecialBean.getRESULT();
        this.allImagesItems = specialBeanArrayList;
        Log.d("SizeSpecial", specialBeanArrayList.size() + "");
        if (allImagesItems.size() > 0)
            listView2.setAdapter(mAdapterItems);
        else {
            listlayout2.setVisibility(View.GONE);
            noSpecialFound.setVisibility(View.GONE);
        }

    }

    @Override
    public void onPause() {
        if (SamplePagerAdapter != null) {
            SamplePagerAdapter.notifyDataSetChanged();
        }
        super.onPause();
    }

    private BaseAdapter mAdapterItems = new BaseAdapter() {

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.row_horizontal_test, parent, false);
            }
            //   pcList = allImagesItems.get(position).getPC();

            ImageView imageView = convertView.findViewById(R.id.image);
            CardView cardViewParent = convertView.findViewById(R.id.parentCard);
            TextView description = convertView.findViewById(R.id.description);
            ImageView eye = convertView.findViewById(R.id.eye);
            String isSeen = allImagesItems.get(0).getPC().get(position).get1149();
            LinearLayout rlSinglePrice = convertView.findViewById(R.id.rel_single_price);
            LinearLayout rlTwoPrice = convertView.findViewById(R.id.rl_two_price);
            TextView actualPrice = convertView.findViewById(R.id.actual_price);

            if ((mernchantfavlist != null && mernchantfavlist.size() > 0 &&
                    mernchantfavlist.contains(Utils.hexToASCII(allImagesItems.get(0).getPC().get(position).get_114170())))
                    || (!isFav.equals("true"))) {
                if (!removeDuplicacy.contains(Utils.hexToASCII(allImagesItems.get(0).getPC().get(position).get120157()))) {
                    removeDuplicacy.add(Utils.hexToASCII(allImagesItems.get(0).getPC().get(position).get120157()));
                    if (isSeen.equalsIgnoreCase("false")) {
                        eye.setBackgroundResource(R.drawable.green_seen);
                    } else {
                        eye.setVisibility(View.GONE);
                    }

                    description.setText(Utils.hexToASCII(allImagesItems.get(0).getPC().get(position).get120157()));
                    rlSinglePrice.setVisibility(View.VISIBLE);
                    rlTwoPrice.setVisibility(View.GONE);
                    actualPrice.setText(allImagesItems.get(0).getPC().get(position).get122162());
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String productId = Utils.lengtT(11, allImagesItems.get(0).getPC().get(position).get114144());
                            String productType = allImagesItems.get(0).getPC().get(position).get114112();
                            Bundle bundle = new Bundle();
                            bundle.putString("productId", productId);
                            bundle.putString("productType", productType);
                            bundle.putString("flag", "search");

                            ((HomeActivity) getActivity()).displayView(new FragmentItemDetails(), Constants.TAG_DETAILSPAGE,
                                    bundle);

                        }
                    });
                    Glide.with(mActivity).load(ATPreferences.readString(mActivity, Constants.KEY_IMAGE_URL)
                            + allImagesItems.get(0).getPC().get(position).get121170())
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(imageView);

                } else
                    cardViewParent.setVisibility(View.GONE);
            } else {
                cardViewParent.setVisibility(View.GONE);
            }
            return convertView;

        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public int getCount() {
            return allImagesItems.get(0).getPC().size();
        }
    };


    public void onResume() {
        super.onResume();
        showAdsWithAnimation();
    }


}
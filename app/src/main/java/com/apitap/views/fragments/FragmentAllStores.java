package com.apitap.views.fragments;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.apitap.R;
import com.apitap.controller.ModelManager;
import com.apitap.model.Constants;
import com.apitap.model.Operations;
import com.apitap.model.Utils;
import com.apitap.model.bean.Storebean;
import com.apitap.model.customclasses.Event;
import com.apitap.model.preferences.ATPreferences;
import com.apitap.views.HomeActivity;
import com.apitap.views.adapters.SortAdapter;
import com.apitap.views.fragments.home.FragmentHome;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.linearlistview.LinearListView;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import static com.apitap.views.fragments.home.FragmentHome.setFavouriteMerchantView;

/**
 * Created by apple on 11/08/16.
 */
public class FragmentAllStores extends Fragment implements View.OnClickListener {

    private GridView mGrid;
    TextView mTextViewHeader;
    LinearLayout sortby, back_ll,linearLayoutHeader;
    int position;
    private String sort_by = Constants.NewToOld;
    SortAdapter sortAdapter;
    ArrayList<String> arraylist;
    private CircularProgressView mPocketBar;
    private LinearLayout rootLayout;
    TextView tvStoreName;
    boolean header = false;
    ImageView ivStoreImage;
    List<Storebean> allImages;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_all_specials, container, false);

        initViews(v);
        setListener();
        arrayListInitialize();


        position = getArguments().getInt("position");
        allImages = (List<Storebean>) getArguments().getSerializable("list");
        mTextViewHeader.setText(getArguments().getString("header"));

        Log.d("Listsizess",allImages.size()+"  ");
        setAdapter();
        return v;
    }


    private void arrayListInitialize() {
        arraylist = new ArrayList<>();
        arraylist.add("Low to High");
        arraylist.add("High to Low");
        arraylist.add("Newest Arrivals");
        arraylist.add("Most Popular");
        //arraylist.add("Expiring Soon");
        arraylist.add("Sort A-Z");
        arraylist.add("Sort Z-A");
        arraylist.add("Sort Nearby");
    }

    private void initViews(View v) {
        mGrid = v.findViewById(R.id.listView);
        mTextViewHeader = v.findViewById(R.id.textViewHeader);
        sortby = v.findViewById(R.id.sortby);
        back_ll = v.findViewById(R.id.back_ll);
        mPocketBar = v.findViewById(R.id.pocket);
        rootLayout =  v.findViewById(R.id.rootLayout);
        tvStoreName = v.findViewById(R.id.storeName);
        linearLayoutHeader = v.findViewById(R.id.header);
        ivStoreImage = v.findViewById(R.id.adstoreImg);


        header = ATPreferences.readBoolean(getActivity(), Constants.HEADER_STORE);
        if (header) {
            linearLayoutHeader.setVisibility(View.VISIBLE);
            //tabContainer2Visible();
            tvStoreName.setText(ATPreferences.readString(getActivity(), Constants.HEADER_TITLE));
            Picasso.get().load(ATPreferences.readString(getActivity(), Constants.HEADER_IMG))
                    .placeholder(R.drawable.loading).into(ivStoreImage);
            setFavouriteMerchantView(tvStoreName);
        }

        }

    private void setListener() {
        sortby.setOnClickListener(this);
        back_ll.setOnClickListener(this);
    }


    private void setAdapter() {
            MyAdapter adapter = new MyAdapter(allImages);
            mGrid.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sortby:
                sort_dialog();
                break;
            case R.id.back_ll:
                if (getFragmentManager().getBackStackEntryCount() > 0)
                    getFragmentManager().popBackStack();
                break;

        }
    }

    private void sort_dialog() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_list);

        final ListView listview = dialog.findViewById(R.id.list);
        sortAdapter = new SortAdapter(getActivity(), arraylist, 0);
        listview.setAdapter(sortAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                LinearLayout item_view = view.findViewById(R.id.root_view);
                item_view.setBackgroundColor(getResources().getColor(R.color.colorGrey));
                final CheckBox itemcheck = view.findViewById(R.id.checkimg);
                sortBy(position);
                ATPreferences.putString(getActivity(), Constants.SAVED_SORT, position + "");
                ModelManager.getInstance().getHomeManager().getHome(getActivity(), Operations.makeJsonGetAds(getActivity(), /*ATPreferences.readString(getActivity(), Constants.MERCHANT_ID)*/""
                        , sort_by));
                mPocketBar.setVisibility(View.VISIBLE);
                dialog.dismiss();

            }
        });
        dialog.show();
    }

    public void sortBy(int position) {
        if (position == 0)
            sort_by = Constants.LowtoPriceHigh;
        else if (position == 1)
            sort_by = Constants.PriceHightoLow;
        else if (position == 2)
            sort_by = Constants.NewToOld;
        else if (position == 3)
            sort_by = Constants.MostPopular;
               /* else if (position == 4)
                    sort_by = Constants.EXPIRING_SOON;*/
        else if (position == 4)
            sort_by = Constants.Alphabetical;
        else if (position == 5)
            sort_by = Constants.REVERSE_Alphabetical;
        else if (position == 6)
            sort_by = Constants.NEAR_ME;
    }

    @Subscribe
    public void onEvent(Event event) {
        switch (event.getKey()) {
            case Constants.ALL_IMAGES_SUCCESS:
                mPocketBar.setVisibility(View.GONE);
                setAdapter();
                break;
            case Constants.GET_SERVER_ERROR:
                mPocketBar.setVisibility(View.GONE);
                //Toast.makeText(getActivity(), "Server Error, Please try again.", Toast.LENGTH_SHORT).show();
                Utils.baseshowFeedbackMessage(getActivity(),rootLayout,"Server Error");
                break;
            case -1:
                mPocketBar.setVisibility(View.GONE);
               // Toast.makeText(getActivity(), "Server Error, Please try again.", Toast.LENGTH_SHORT).show();
                Utils.baseshowFeedbackMessage(getActivity(),rootLayout,"Server Error");

                break;


        }
    }


    class MyAdapter extends BaseAdapter {
        LayoutInflater inflater;
        List<Storebean> allImages;

        public MyAdapter(List<Storebean> array) {
            inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.allImages = array;
        }

        @Override
        public int getCount() {
            return allImages.size();
        }

        @Override
        public Object getItem(int position) {
            return allImages.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        class ViewHolder {
            TextView mCategoryName;
            LinearListView mTwoWayView;
            //  MyTwoWayAdapter adapter;
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {

            ViewHolder holder;
            // if (convertView == null) {

            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.row_horizontal_test, parent, false);
            ImageView imageView = (ImageView) convertView.findViewById(R.id.image);
            TextView description = (TextView) convertView.findViewById(R.id.description);
            ImageView eye = (ImageView) convertView.findViewById(R.id.eye);

            LinearLayout rlSinglePrice = (LinearLayout) convertView.findViewById(R.id.rel_single_price);
            LinearLayout rlTwoPrice = (LinearLayout) convertView.findViewById(R.id.rl_two_price);
            TextView price = (TextView) convertView.findViewById(R.id.price);
            TextView actualPrice = (TextView) convertView.findViewById(R.id.actual_price);
            TextView priceAfterDiscount = (TextView) convertView.findViewById(R.id.price_after_discount);

            eye.setVisibility(View.GONE);

            imageView.setTag(position);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String merchantID = allImages.get(position).getId();
                    ATPreferences.putString(getActivity(),Constants.MERCHANT_ID, merchantID);
                    Bundle b = new Bundle();
                    b.putBoolean(Constants.HEADER_STORE, true);
                    b.putString(Constants.MERCHANT_ID, merchantID);
                    ATPreferences.putBoolean(getActivity(), Constants.HEADER_STORE, true);


                    ((HomeActivity) getActivity()).displayAddView(new FragmentHome(), Constants.TAG_STORESPAGE, Constants.TAG_HOMEPAGE, b);
                }
            });
            Picasso.get().load(ATPreferences.readString(getActivity(), Constants.KEY_IMAGE_URL) + "_t_" +
                    allImages.get(position).getImageUrl()).placeholder(R.drawable.splash_screen_new)
                    .error(R.drawable.splash_screen_new).into(imageView);
            description.setText(allImages.get(position).getName());

            return convertView;
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
}

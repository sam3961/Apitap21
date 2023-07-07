package com.apitap.views.fragments;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
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
import com.apitap.controller.HomeManager;
import com.apitap.controller.ModelManager;
import com.apitap.model.Constants;
import com.apitap.model.Operations;
import com.apitap.model.Utils;
import com.apitap.model.bean.ImagesBean;
import com.apitap.model.bean.SpecialsBean;
import com.apitap.model.customclasses.Event;
import com.apitap.model.preferences.ATPreferences;
import com.apitap.views.HomeActivity;
import com.apitap.views.adapters.SortAdapter;
import com.apitap.views.fragments.itemDetails.FragmentItemDetails;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.linearlistview.LinearListView;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.apitap.views.fragments.home.FragmentHome.setFavouriteMerchantView;

/**
 * Created by apple on 11/08/16.
 */
public class FragmentAllSpecial extends Fragment implements View.OnClickListener{

    private GridView mGrid;
    TextView mTextViewHeader;
    List<SpecialsBean.PC> allImages;
    int position;
    private String sort_by = Constants.NewToOld;
    LinearLayout sortby,back_ll;
    LinearLayout rootLayout,linearLayoutHeader;
    TextView tvStoreName;
    ImageView ivStoreImage;
    ArrayList<String> arraylist;

    private CircularProgressView mPocketBar;
    private boolean header=false;
    private String currentCategoryId="";

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_all_specials, container, false);

        initViews(v);
        setListener();
        arrayListInitialize();
        position=getArguments().getInt("position");
        mTextViewHeader.setText(getArguments().getString("header"));

        currentCategoryId = getArguments().getString("currentCategoryId");
        allImages = (List<SpecialsBean.PC>) getArguments().getSerializable("list");


        setAdapter();
        return v;
    }

    private void initViews(View v) {
        mGrid = v.findViewById(R.id.listView);
        mTextViewHeader = v.findViewById(R.id.textViewHeader);
        sortby = v.findViewById(R.id.sortby);
        back_ll = v.findViewById(R.id.back_ll);
        rootLayout = v.findViewById(R.id.rootLayout);
        tvStoreName = v.findViewById(R.id.storeName);
        linearLayoutHeader = v.findViewById(R.id.header);
        ivStoreImage = v.findViewById(R.id.adstoreImg);
        mPocketBar = v.findViewById(R.id.pocket);


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
        HashMap<Integer, ArrayList<ImagesBean>> map = ModelManager.getInstance().getHomeManager().specialData;

        ArrayList<ImagesBean> array=map.get(position);
        if (array != null && array.size() > 0) {
            MyAdapter adapter = new MyAdapter(allImages);
            mGrid.setAdapter(adapter);
        }
    }

    private void arrayListInitialize() {
        arraylist = new ArrayList<>();
        arraylist.add("Newest Arrivals");
        arraylist.add("Expiring Soon");
        arraylist.add("Most Popular");
        arraylist.add("Sort A-Z");
        arraylist.add("Sort Z-A");
        arraylist.add("Sort Nearby");
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.sortby:
                sort_dialog();
                break;
            case R.id.back_ll:
                if (getFragmentManager().getBackStackEntryCount() > 0)
                    getFragmentManager().popBackStack();
                break;
        }
    }

    class MyAdapter extends BaseAdapter {
        LayoutInflater inflater;
        ArrayList<ImagesBean> array;
        List<SpecialsBean.PC> allImages;

        public MyAdapter(List<SpecialsBean.PC> allImages) {
            inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.allImages = allImages;
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
        public View getView(final int position, View convertView, ViewGroup parent) {

            ViewHolder holder;
            // if (convertView == null) {

            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.row_horizontal_test, parent, false);
            ImageView imageView = convertView.findViewById(R.id.image);
            TextView actualPrice = convertView.findViewById(R.id.actual_price);
            TextView priceAfterDiscount = convertView.findViewById(R.id.price_after_discount);
            TextView price = convertView.findViewById(R.id.price);

            ImageView eye = convertView.findViewById(R.id.eye);
            LinearLayout rlSinglePrice = convertView.findViewById(R.id.rel_single_price);
            LinearLayout rlTwoPrice = convertView.findViewById(R.id.rl_two_price);
            TextView description = convertView.findViewById(R.id.description);
            String isSeen = allImages.get(position).get1149();
            if (isSeen.equalsIgnoreCase("false")) {
                eye.setBackgroundResource(R.drawable.green_seen);
            } else {
                eye.setVisibility(View.GONE);
            }

            if (allImages.get(position).get11498().equals(allImages.get(position).get122162()) ||
                    allImages.get(position).get122162().equals("") ||
                    allImages.get(position).get122162().equals("0") ||
                    allImages.get(position).get122162().equals("0.00")) {

                rlSinglePrice.setVisibility(View.VISIBLE);
                rlTwoPrice.setVisibility(View.GONE);
                actualPrice.setText(/*"$"+*/allImages.get(position).get11498());

            } else {
                rlSinglePrice.setVisibility(View.GONE);
                rlTwoPrice.setVisibility(View.VISIBLE);
                actualPrice.setTextColor(Color.GRAY);
                actualPrice.setText(/*"$" + */allImages.get(position).get11498());
                priceAfterDiscount.setText(/*"$" +*/ allImages.get(position).get122162());
                actualPrice.setPaintFlags(actualPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            }

//            if (array.get(position).getActualPrice().equals(array.get(position).getPriceAfterDiscount()) || array.get(position).getPriceAfterDiscount().equals("") || array.get(position).getPriceAfterDiscount().equals("0") || array.get(position).getPriceAfterDiscount().equals("0.00")) {
//                rlSinglePrice.setVisibility(View.VISIBLE);
//                rlTwoPrice.setVisibility(View.GONE);
//                actualPrice.setText(array.get(position).getActualPrice());
//            } else {
//                rlSinglePrice.setVisibility(View.GONE);
//                rlTwoPrice.setVisibility(View.VISIBLE);
//                actualPrice.setText(array.get(position).getActualPrice());
//                priceAfterDiscount.setText(array.get(position).getPriceAfterDiscount());
//                actualPrice.setPaintFlags(actualPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//            }

            description.setText(Utils.hexToASCII(allImages.get(position).get12083()));

            convertView.setTag(holder);

            Picasso.get().load(ATPreferences.readString(getActivity(),
                    Constants.KEY_IMAGE_URL) + "_t_" + allImages.get(position).get121170()).into(imageView);


            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String productId = Utils.lengtT(11, allImages.get(position).get114144());
                    String productType = array.get(position).getProdcutType();
                    Bundle bundle = new Bundle();
                    bundle.putString("productId", productId);
                    bundle.putString("productType", productType);
                    bundle.putString("actualprice", allImages.get(position).get11498());
                    bundle.putString("discountprice", allImages.get(position).get122162());
                    FragmentItemDetails fragment = new FragmentItemDetails();
                    fragment.setArguments(bundle);
                    ((HomeActivity) getActivity()).displayView(fragment, Constants.TAG_DETAILSPAGE, bundle);
                }
            });

            return convertView;
        }

    }

    private void sort_dialog() {

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.dialog_list);
        final ListView listview = dialog.findViewById(R.id.list);


        SortAdapter sortAdapter = new SortAdapter(getActivity(), arraylist, 1);
        listview.setAdapter(sortAdapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LinearLayout item_view = (LinearLayout) view;
                item_view.setBackgroundColor(getResources().getColor(R.color.colorGrey));
                final CheckBox itemcheck = item_view.findViewById(R.id.checkimg);

                if (position == 0)
                    sort_by = Constants.NewToOld;
                else if (position == 1)
                    sort_by = Constants.EXPIRING_SOON;
                else if (position == 2)
                    sort_by = Constants.MostPopular;
                else if (position == 3)
                    sort_by = Constants.Alphabetical;
                else if (position == 4)
                    sort_by = Constants.REVERSE_Alphabetical;
                else if (position == 5)
                    sort_by = Constants.NEAR_ME;

                if (itemcheck.isChecked()) {
                    itemcheck.setChecked(true);
                } else {
                    itemcheck.setChecked(false);
                }
                //ATPreferences.putString(getActivity(), Constants.SPECIAL_SORT, position + "");
                ModelManager.getInstance().getHomeManager().getSpecials(getActivity(),
                        Operations.makeJsonSpecialsCategory(getActivity(), "", sort_by,
                                currentCategoryId));

                mPocketBar.setVisibility(View.VISIBLE);
                dialog.dismiss();


            }
        });
        dialog.show();

    }

    @Subscribe
    public void onEvent(Event event) {
        switch (event.getKey()) {
            case Constants.ALL_IMAGES_SUCCESS:
                mPocketBar.setVisibility(View.GONE);
                allImages= HomeManager.specialsBean.getRESULT().get(0).getRESULT().get(0).getPC();
                setAdapter();
                break;

            case Constants.GET_SERVER_ERROR:
                mPocketBar.setVisibility(View.GONE);
                ///Toast.makeText(getActivity(), "Server Error, Please try again.", Toast.LENGTH_SHORT).show();
                Utils.baseshowFeedbackMessage(getActivity(),rootLayout,"Server Error, Please try again");

                break;
            case -1:
                mPocketBar.setVisibility(View.GONE);
              //  Toast.makeText(getActivity(), "Problem occurred, please try again.", Toast.LENGTH_SHORT).show();
                Utils.baseshowFeedbackMessage(getActivity(),rootLayout,"Server Error, Please try again");

                break;

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

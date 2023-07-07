package com.apitap.views.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.apitap.R;
import com.apitap.model.AddTabBar;
import com.apitap.model.Constants;
import com.apitap.model.Utils;
import com.apitap.model.bean.ProductDetailsBean;
import com.apitap.model.customclasses.Event;
import com.apitap.model.preferences.ATPreferences;
import com.apitap.views.FullScreenImage;
import com.apitap.views.HomeActivity;
import com.apitap.views.NavigationMenu.FragmentDrawer;
import com.apitap.views.fragments.ads.FragmentAds;
import com.apitap.views.fragments.items.FragmentItems;
import com.apitap.views.fragments.specials.FragmentSpecial;
import com.google.android.material.tabs.TabLayout;
import com.jsibbold.zoomage.ZoomageView;
import com.linearlistview.LinearListView;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import static com.apitap.App.isGuest;


/**
 * Created by sourcefuse on 15/9/16.
 */

public class FragmentZoomImage extends BaseFragment implements View.OnClickListener {
    ZoomageView touch;
    private FragmentDrawer drawerFragment;
    private Toolbar mToolbar;
    TextView productName, textViewStoreName;
    HorizontalScrollView scrollView;
    ArrayList<ProductDetailsBean> detailsArray;
    LinearLayout scan_tool;
    LinearLayout msg_tool;
    LinearLayout search_tool;
    ImageView resize;
    LinearLayout viewMain;
    FrameLayout frameLayout;
    private static int toolint = 0;
    String searchkey = "";
    String imageUrl = "";
    int rel_position = 99;
    ImageView iv_back;
    public static TabLayout tabLayout;
    private ImageView  homeTab2;
    private TextView tabOne, tabTwo, tabThree, tabFour, tabFive, tabSix;
    LinearLayout tabConatiner;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_zoom_image, container, false);
        
        initViews();

        imageUrl = getArguments().getString("image");
        rel_position = getArguments().getInt("position");
        String name = getArguments().getString("name");
        String storeName = getArguments().getString("storeName");
        detailsArray = (ArrayList<ProductDetailsBean>) getArguments().getSerializable("detailsArray");

        productName.setText(name);
        textViewStoreName.setText(storeName);
        Picasso.get().load(imageUrl).into(touch);
        if (rel_position != 99) {
            Picasso.get().load(ATPreferences.readString(getActivity(), Constants.KEY_IMAGE_URL) +
                    detailsArray.get(rel_position).getProductImage()).into(touch);
            imageUrl = ATPreferences.readString(getActivity(), Constants.KEY_IMAGE_URL) + detailsArray.get(rel_position).getProductImage();
        }


        rootView.findViewById(R.id.ll_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPress();
            }
        });
        setImages(detailsArray);
        return rootView;
    }


    private void initViews() {
        touch = rootView.findViewById(R.id.touch);
        homeTab2 = rootView.findViewById(R.id.tab_one_image);
        textViewStoreName =  rootView.findViewById(R.id.textViewStoreName);
        productName = rootView.findViewById(R.id.product_name);
        iv_back = rootView.findViewById(R.id.back);
        scrollView = rootView.findViewById(R.id.scroll_view);
        scan_tool = rootView.findViewById(R.id.ll_scan);
        viewMain = rootView.findViewById(R.id.linear);
        frameLayout = rootView.findViewById(R.id.container_body);
        resize = rootView.findViewById(R.id.resize);
        msg_tool = rootView.findViewById(R.id.ll_message);
        search_tool = rootView.findViewById(R.id.ll_search);
        tabConatiner = rootView.findViewById(R.id.tab_container);
        tabLayout = rootView.findViewById(R.id.tabs);


        iv_back.setOnClickListener(this);
        scan_tool.setOnClickListener(this);
        resize.setOnClickListener(this);
        msg_tool.setOnClickListener(this);
        search_tool.setOnClickListener(this);
        
    }

    private void setImages(ArrayList<ProductDetailsBean> detailsArray) {
        final LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearListView mTwoWayView = rootView.findViewById(R.id.my_gallery);
        mTwoWayView.setAdapter(mAdapter);
    }


    private BaseAdapter mAdapter = new BaseAdapter() {

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.row_zoom, parent, false);
            }

            ImageView imageView = convertView.findViewById(R.id.image);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    rel_position=position;
                    touch.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    Picasso.get().load(ATPreferences.readString(getActivity(), Constants.KEY_IMAGE_URL) +
                            detailsArray.get(position).getProductImage()).into(touch);

                    imageUrl = ATPreferences.readString(getActivity(),
                            Constants.KEY_IMAGE_URL) + detailsArray.get(position).getProductImage();

                    Log.d("imageUrla", imageUrl + "  " + ATPreferences.readString(getActivity(),
                            Constants.KEY_IMAGE_URL) + detailsArray.get(position).getProductImage());
                }
            });

            Picasso.get().load(ATPreferences.readString(getActivity(),
                    Constants.KEY_IMAGE_URL) + detailsArray.get(position).getProductImage()).into(imageView);
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
            return detailsArray.size();
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.back:
                onBackPress();
                break;

            case R.id.resize:
                Intent intent = new Intent(getActivity(), FullScreenImage.class);

                Bundle extras = new Bundle();
                extras.putString("imagebitmap", imageUrl);
                extras.putString("video", "");
                extras.putString("previousClass", "ZoomImage");
                extras.putInt("img_position", rel_position);
                extras.putSerializable("detailsArray", detailsArray);
                intent.putExtras(extras);
                startActivity(intent);
               // ((HomeActivity) getActivity()).displayView(new FragmentFullScreenImage(), Constants.TAG_FULL_SCREEN, extras);
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

    @Subscribe
    public void onEvent(Event event) {
        switch (event.getKey()) {
            case Constants.ADDRESS_NEARBY_SUCCESS:
               Utils.setRecyclerNearByAdapter(getActivity());
                break;
        }
    }

}

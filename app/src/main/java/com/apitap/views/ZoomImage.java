package com.apitap.views;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;

import com.apitap.views.fragments.favourite.FragmentFavourite;
import com.apitap.views.fragments.specials.FragmentSpecial;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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

import com.apitap.R;
import com.apitap.model.AddTabBar;
import com.apitap.model.Constants;
import com.apitap.model.Utils;
import com.apitap.model.bean.ProductDetailsBean;
import com.apitap.model.customclasses.Event;
import com.apitap.model.preferences.ATPreferences;
import com.apitap.views.NavigationMenu.FragmentDrawer;
import com.apitap.views.fragments.items.FragmentItems;
import com.apitap.views.fragments.messages.FragmentMessages;
import com.apitap.views.fragments.FragmentScanner;
import com.apitap.views.fragments.ads.FragmentAds;
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

public class ZoomImage extends AppCompatActivity implements View.OnClickListener, FragmentDrawer.FragmentDrawerListener {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom_image);

        initViews();

        imageUrl = getIntent().getExtras().getString("image");
        rel_position = getIntent().getExtras().getInt("position");
        String name = getIntent().getExtras().getString("name");
        String storeName = getIntent().getExtras().getString("storeName");
        detailsArray = (ArrayList<ProductDetailsBean>) getIntent().getSerializableExtra("detailsArray");

        productName.setText(name);
        textViewStoreName.setText(storeName);
        Picasso.get().load(imageUrl).into(touch);
        if (rel_position != 99) {
            Picasso.get().load(ATPreferences.readString(ZoomImage.this, Constants.KEY_IMAGE_URL) +
                    detailsArray.get(rel_position).getProductImage()).into(touch);
            imageUrl = ATPreferences.readString(ZoomImage.this, Constants.KEY_IMAGE_URL) + detailsArray.get(rel_position).getProductImage();
        }


        findViewById(R.id.ll_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //  cross.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });

        setImages(detailsArray);

    }


    private void initViews() {
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // cross=(TextView)findViewById(R.id.cross);
        touch = findViewById(R.id.touch);
        homeTab2 = findViewById(R.id.tab_one_image);
        textViewStoreName =  findViewById(R.id.textViewStoreName);
        productName = findViewById(R.id.product_name);
        iv_back = findViewById(R.id.back);
        scrollView = findViewById(R.id.scroll_view);
        scan_tool = findViewById(R.id.ll_scan);
        viewMain = findViewById(R.id.linear);
        frameLayout = findViewById(R.id.container_body);
        resize = findViewById(R.id.resize);
        msg_tool = findViewById(R.id.ll_message);
        search_tool = findViewById(R.id.ll_search);
        tabConatiner = findViewById(R.id.tab_container);
        tabLayout = findViewById(R.id.tabs);


        iv_back.setOnClickListener(this);
        scan_tool.setOnClickListener(this);
        resize.setOnClickListener(this);
        msg_tool.setOnClickListener(this);
        search_tool.setOnClickListener(this);

        AddTabBar.getmInstance().setupViewPager(tabLayout);
        AddTabBar.getmInstance().setupTabIcons(tabLayout, ZoomImage.this, tabOne, tabTwo, tabThree,
                tabFour, tabFive, tabSix,homeTab2);
        AddTabBar.getmInstance().bindWidgetsWithAnEvent(tabConatiner, tabLayout, ZoomImage.this, R.id.container_body2);

    }

    private void setImages(ArrayList<ProductDetailsBean> detailsArray) {
        final LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearListView mTwoWayView = findViewById(R.id.my_gallery);
        mTwoWayView.setAdapter(mAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
                    Picasso.get().load(ATPreferences.readString(ZoomImage.this, Constants.KEY_IMAGE_URL) +
                            detailsArray.get(position).getProductImage()).into(touch);

                    imageUrl = ATPreferences.readString(ZoomImage.this, Constants.KEY_IMAGE_URL) + detailsArray.get(position).getProductImage();

                    Log.d("imageUrla", imageUrl + "  " + ATPreferences.readString(ZoomImage.this, Constants.KEY_IMAGE_URL) + detailsArray.get(position).getProductImage());
                }
            });

            Picasso.get().load(ATPreferences.readString(ZoomImage.this, Constants.KEY_IMAGE_URL) + detailsArray.get(position).getProductImage()).into(imageView);
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
                finish();
                break;
            case R.id.ll_message:
                displayView(new FragmentMessages(), Constants.TAG_MESSAGEPAGE, new Bundle());
                break;

            case R.id.ll_scan:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.CAMERA},
                                MY_CAMERA_REQUEST_CODE);
                    } else {
                        //mTxtHeading.setText("Scan");
                        //mlogo.setVisibility(View.GONE);
                        //mTxtHeading.setVisibility(View.VISIBLE);
                        displayView(new FragmentScanner(), Constants.TAG_SCANNER, new Bundle());
                    }
                }

                break;
            case R.id.ll_search:
                //Utils.showSearchDialog(this,"ZoomImage");
                if (isGuest)
                    Utils.showGuestDialog(this);
                else {
                    displayView(new FragmentFavourite(), Constants.TAG_FAVOURITEPAGE, null);
                }
                break;
            case R.id.resize:
                Intent intent = new Intent(this, FullScreenImage.class);

                Bundle extras = new Bundle();
                extras.putString("imagebitmap", imageUrl);
                extras.putString("video", "");
                extras.putString("previousClass", "ZoomImage");
                extras.putInt("img_position", rel_position);
                extras.putSerializable("detailsArray", detailsArray);
                intent.putExtras(extras);
                startActivity(intent);
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
               Utils.setRecyclerNearByAdapter(getApplicationContext());
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_CAMERA_REQUEST_CODE) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (toolint == 0) {

                    //  Toast.makeText(this, "Camera permission granted", Toast.LENGTH_LONG).show();
                    // mTxtHeading.setText("Scan");
                    //mlogo.setVisibility(View.GONE);
                    //mTxtHeading.setVisibility(View.VISIBLE);
                    displayView(new FragmentScanner(), Constants.TAG_SCANNER, new Bundle());

                } else {
                    displayView(new FragmentScanner(), Constants.TAG_SCANNER, null);
                }
            } else {

                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_LONG).show();

            }

        }
    }

    public void displayView(Fragment fragment, String tag, Bundle bundle) {
        frameLayout.setVisibility(View.VISIBLE);
        viewMain.setVisibility(View.GONE);
        //  if (fragment != null) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragB = fragmentManager.findFragmentByTag(tag);
        if (bundle != null)
            fragment.setArguments(bundle);
        //  if (fragB == null) {
        fragmentTransaction.replace(R.id.container_body, fragment);
        if (fragment instanceof FragmentAds || fragment instanceof FragmentSpecial || fragment instanceof FragmentItems) {

        } else
            fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.commit();
//            } else
//                fragmentTransaction.show(fragB);
        //  getSupportActionBar().setTitle(tag);
        // }
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        startActivity(new Intent(this, HomeActivity.class).putExtra("Drawer", position));
    }
}

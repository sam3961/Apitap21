package com.apitap.views;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.apitap.R;
import com.apitap.model.Constants;
import com.apitap.model.Utils;
import com.apitap.model.preferences.ATPreferences;
import com.apitap.views.fragments.favourite.FragmentFavourite;
import com.apitap.views.fragments.items.FragmentItems;
import com.apitap.views.fragments.messages.FragmentMessages;
import com.apitap.views.fragments.FragmentScanner;
import com.apitap.views.fragments.specials.FragmentSpecial;
import com.apitap.views.fragments.ads.FragmentAds;
import com.jsibbold.zoomage.ZoomageView;
import com.squareup.picasso.Picasso;

import static com.apitap.App.isGuest;

public class ZoomMessageImage extends AppCompatActivity implements View.OnClickListener {

    private ZoomageView zoomImageView;
    ImageView collapse_img;
    LinearLayout collapse_white;
    LinearLayout scan_tool;
    LinearLayout msg_tool;
    RelativeLayout viewMain;
    FrameLayout frameLayout;
    LinearLayout search_tool;
    private static final int MY_CAMERA_REQUEST_CODE = 100;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_message_image);

        zoomImageView =  findViewById(R.id.imgDisplay);
        collapse_img =  findViewById(R.id.collapse_img);
        collapse_white =  findViewById(R.id.collapse_white);
        scan_tool = findViewById(R.id.ll_scan);
        msg_tool = findViewById(R.id.ll_message);
        search_tool = findViewById(R.id.ll_search);
        viewMain = findViewById(R.id.linear);
        frameLayout = findViewById(R.id.container_body);

        msg_tool.setOnClickListener(this);
        search_tool.setOnClickListener(this);
        scan_tool.setOnClickListener(this);


        Picasso.get().load(ATPreferences.readString(this, Constants.KEY_MESSAGE_IMAGE_URL) +getIntent().getStringExtra("image")
                ).fit().centerInside().into(zoomImageView);

        collapse_white.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        collapse_img.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                    finish();
            }
        });




    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_message:

                displayView(new FragmentMessages(), Constants.TAG_MESSAGEPAGE, new Bundle());
                break;

            case R.id.ll_scan:

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

                break;
            case R.id.ll_search:
                //Utils.showSearchDialog(this, "ZoomScreenImage");
                if (isGuest)
                    Utils.showGuestDialog(this);
                else {
                    displayView(new FragmentFavourite(), Constants.TAG_FAVOURITEPAGE, null);
                }
                break;
        }
    }

    public void displayView(Fragment fragment, String tag, Bundle bundle) {
        //  if (fragment != null) {
        frameLayout.setVisibility(View.VISIBLE);
        viewMain.setVisibility(View.GONE);
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

}

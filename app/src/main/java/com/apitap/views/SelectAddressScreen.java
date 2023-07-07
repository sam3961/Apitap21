package com.apitap.views;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apitap.R;
import com.apitap.model.GPSService;

public class SelectAddressScreen extends AppCompatActivity {

    private LinearLayout lay_floating;
    private TextView txt_done;
    CheckBox current;
    TextView AddressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_address_screen);

        initViews();

    }

    private void initViews() {
        lay_floating = (LinearLayout) findViewById(R.id.lay_floating);
        txt_done = (TextView) findViewById(R.id.txt_done);
        current = (CheckBox) findViewById(R.id.cb_current);
        AddressView = (TextView) findViewById(R.id.txtaddnew);


        current.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (current.isChecked()){
                    current.setChecked(false);}
                else {
                    String address = getLocations();
                    current.setChecked(true);
                    AddressView.setText(address);
                }
            }
        });

        lay_floating.setAnimation(AnimationUtils.loadAnimation(this, R.anim.bottom_top));

        txt_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public String  getLocations() {
        String address = "";
        GPSService mGPSService = new GPSService(this);
        mGPSService.getLocation();

        if (mGPSService.isLocationAvailable == false) {

            // Here you can ask the user to try again, using return; for that
            Toast.makeText(this, "Your location is not available, please try again.", Toast.LENGTH_SHORT).show();
            return "";

            // Or you can continue without getting the location, remove the return; above and uncomment the line given below
            // address = "Location not available";
        } else {

            // Getting location co-ordinates
            double latitude = mGPSService.getLatitude();
            double longitude = mGPSService.getLongitude();


            address = mGPSService.getLocationAddress();

            // tvLocation.setText("Latitude: " + latitude + " \nLongitude: " + longitude);
            //tvAddress.setText("Address: " + address);
        }


        // make sure you close the gps after using it. Save user's battery power
        mGPSService.closeGPS();
        return address;
    }

    @Override
    public void onBackPressed() {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.top_bottom);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                finish();
                overridePendingTransition(0, 0);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        lay_floating.startAnimation(anim);
    }
}

package com.apitap.views;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.apitap.R;
import com.apitap.controller.ModelManager;
import com.apitap.model.Constants;
import com.apitap.model.Operations;
import com.apitap.model.Utils;
import com.apitap.model.bean.getaddress.AddressData;
import com.apitap.model.customclasses.Event_Add_Address;
import com.apitap.model.customclasses.Event_Address;
import com.apitap.views.adapters.CodeAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBarUtils;
import fr.castorflex.android.smoothprogressbar.SmoothProgressDrawable;

public class AddressDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et_type, et_line1, et_line2, et_mob;
    private TextView txt_pin, txt_country, txt_state, txt_city;
    private RecyclerView recyclerView;
    private Button btn_save;
    private boolean ISNew;
    private AddressData addressData;
    private SmoothProgressBar mPocketBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        ISNew = getIntent().getBooleanExtra("ISNew", true);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (ISNew) {
            toolbar.setTitle("Add New Address");
        } else {
            toolbar.setTitle("Edit Address");
            addressData = (AddressData) getIntent().getSerializableExtra("data");
        }
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initViews();


    }

    private void initViews() {
        et_type = (EditText) findViewById(R.id.et_type);
        et_line1 = (EditText) findViewById(R.id.et_line1);
        et_line2 = (EditText) findViewById(R.id.et_line2);
        et_mob = (EditText) findViewById(R.id.et_mob);
        txt_pin = (TextView) findViewById(R.id.txt_pin);
        txt_country = (TextView) findViewById(R.id.txt_country);
        txt_state = (TextView) findViewById(R.id.txt_state);
        txt_city = (TextView) findViewById(R.id.txt_city);
        btn_save = (Button) findViewById(R.id.btn_save);
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        mPocketBar = (SmoothProgressBar) findViewById(R.id.pocket);

        mPocketBar.setSmoothProgressDrawableBackgroundDrawable(
                SmoothProgressBarUtils.generateDrawableWithColors(
                        getResources().getIntArray(R.array.pocket_background_colors),
                        ((SmoothProgressDrawable) mPocketBar.getIndeterminateDrawable()).getStrokeWidth()));

        if (ISNew) {
            btn_save.setText("Save");
        } else {
            btn_save.setText("Update");
            et_type.setText(addressData.getAddress_type());
            et_line1.setText(addressData.getLine1());
            et_line2.setText(addressData.getLine2());
            et_mob.setText(addressData.getPhone());
            txt_country.setText(addressData.getcO().getCountry());
            txt_state.setText(addressData.getsT().getState());
            txt_city.setText(addressData.getcI().getCity());
            txt_pin.setText(addressData.getzP().getZipcode());

            txt_country.setTag(Utils.getElevenDigitId(addressData.getcO().getCountryId()));
            txt_state.setTag(Utils.getElevenDigitId(addressData.getsT().getStateId()));
            txt_city.setTag(Utils.getElevenDigitId(addressData.getcI().getCityId()));
            txt_pin.setTag(Utils.getElevenDigitId(addressData.getzP().getZipcodeId()));

        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        btn_save.setOnClickListener(this);
        txt_country.setOnClickListener(this);
        txt_state.setOnClickListener(this);
        txt_city.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save:
                saveData();

                break;
            case R.id.txt_country:
                doProgress(true);
                //  Utils.startDialog(this);
                ModelManager.getInstance().getCountryManager().getCode(this, Operations.makeJsonGetCountry(this),
                        Constants.GET_COUNTRY_CODE);
                break;
            case R.id.txt_state:
                if (txt_country.getTag() != null) {
                    doProgress(true);
                    //       Utils.startDialog(this);
                    String id = String.valueOf(txt_country.getTag());

                    ModelManager.getInstance().getCountryManager().getCode(this, Operations.makeJsonGetState(this, id),
                            Constants.GET_STATE_CODE);
                }
                break;
            case R.id.txt_city:
                if (txt_state.getTag() != null) {
                    doProgress(true);
                    //        Utils.startDialog(this);
                    String id = String.valueOf(txt_state.getTag());

                    ModelManager.getInstance().getCountryManager().getCode(this, Operations.makeJsonGetCity(this, id),
                            Constants.GET_CITY_CODE);
                }
                break;
        }
    }

    private void saveData() {
        String type = et_type.getText().toString().trim();
        String line1 = et_line1.getText().toString().trim();
        String line2 = et_line2.getText().toString().trim();
        String mob = et_mob.getText().toString().trim();
        String country = txt_country.getText().toString().trim();
        String state = txt_state.getText().toString().trim();
        String city = txt_city.getText().toString().trim();
        String pin = txt_pin.getText().toString().trim();

        if (TextUtils.isEmpty(type) || TextUtils.isEmpty(line1) || TextUtils.isEmpty(line2) || TextUtils.isEmpty(mob)
                || TextUtils.equals(country, getString(R.string.country)) || TextUtils.equals(state, getString(R.string.state))
                || TextUtils.equals(city, getString(R.string.city)) || TextUtils.equals(pin, getString(R.string.pincode))) {
            Utils.showToast(this, "Please fill all details");
        } else {
            doProgress(true);
            //     Utils.startDialog(this);
            if (ISNew) {
                ModelManager.getInstance().getAddressManager().addAddresse(this, Operations.makeJsonAddAddress(true, this, type, line1,
                        line2, mob, String.valueOf(txt_country.getTag()), String.valueOf(txt_state.getTag()), String.valueOf(txt_city.getTag()),
                        String.valueOf(txt_pin.getTag()), ""), Constants.ADD_ADDRESS_SUCCESS);
            } else {
                ModelManager.getInstance().getAddressManager().addAddresse(this, Operations.makeJsonAddAddress(false, this, type, line1,
                        line2, mob, String.valueOf(txt_country.getTag()), String.valueOf(txt_state.getTag()), String.valueOf(txt_city.getTag()),
                        String.valueOf(txt_pin.getTag()), Utils.getElevenDigitId(addressData.getAddressId())), Constants.ADD_ADDRESS_SUCCESS);
            }

        }


    }

    @Subscribe
    public void onEvent(final Event_Address event_address) {
        if (event_address.isCode()) {
            doProgress(false);
            //      Utils.stopDialog();
            switch (event_address.getKey()) {
                case Constants.GET_COUNTRY_CODE:
                    if (event_address.getResponse_code().getCountries().size() == 0) {
                        Utils.showToast(this, "No Data");
                        txt_country.setText(getString(R.string.country));
                        return;
                    }
                    break;
                case Constants.GET_STATE_CODE:
                    if (event_address.getResponse_code().getStates().size() == 0) {
                        Utils.showToast(this, "No Data");
                        txt_state.setText(getString(R.string.state));
                        return;
                    }
                    break;
                case Constants.GET_CITY_CODE:
                    if (event_address.getResponse_code().getCities().size() == 0) {
                        Utils.showToast(this, "No Data");
                        txt_city.setText(getString(R.string.city));
                        return;
                    }
                    break;
                case Constants.GET_PIN_CODE:
                    if (event_address.getResponse_code().getPincodes().size() == 0) {
                        Utils.showToast(this, "No Data");
                        txt_pin.setText(getString(R.string.pincode));
                        return;
                    } else {
                        String id = Utils.getElevenDigitId(event_address.getResponse_code().getPincodes().get(0).getPinId());

                        txt_pin.setText(event_address.getResponse_code().getPincodes().get(0).getPinCode());
                        txt_pin.setTag(id);

                        return;
                    }
            }

            CodeAdapter adp = new CodeAdapter(event_address);
            recyclerView.setAdapter(adp);
            recyclerView.setVisibility(View.VISIBLE);
            adp.setonItemClickListner(new CodeAdapter.AdapterClick() {
                @Override
                public void onClick(View view, int position) {
                    String id = "";
                    switch (event_address.getKey()) {

                        case Constants.GET_COUNTRY_CODE:
                            txt_country.setText(event_address.getResponse_code().getCountries().get(position).getCountryName());
                            id = Utils.getElevenDigitId(String.valueOf(event_address.getResponse_code().getCountries().get(position).getCountryId()));

                            txt_country.setTag(id);
                            break;
                        case Constants.GET_STATE_CODE:
                            txt_state.setText(event_address.getResponse_code().getStates().get(position).getStateName());
                            id = Utils.getElevenDigitId(String.valueOf(event_address.getResponse_code().getStates().get(position).getStateId()));

                            txt_state.setTag(id);
                            break;
                        case Constants.GET_CITY_CODE:
                            txt_city.setText(event_address.getResponse_code().getCities().get(position).getCityName());
                            id = Utils.getElevenDigitId(String.valueOf(event_address.getResponse_code().getCities().get(position).getCityId()));

                            txt_city.setTag(id);
                            doProgress(true);

                            //          Utils.startDialog(AddressDetailActivity.this);
                            if (txt_city.getTag() != null) {
                                id = Utils.getElevenDigitId(String.valueOf(txt_city.getTag()));

                                ModelManager.getInstance().getCountryManager().getCode(AddressDetailActivity.this,
                                        Operations.makeJsonGetPINCode(AddressDetailActivity.this, id), Constants.GET_PIN_CODE);
                            }

                            break;

                    }
                    recyclerView.setVisibility(View.GONE);
                    recyclerView.removeAllViews();
                }
            });
        }

    }

    @Subscribe
    public void onEvent(final Event_Add_Address event_add_address) {
        doProgress(false);
        //      Utils.stopDialog();
        switch (event_add_address.getKey()) {
            case Constants.ADD_ADDRESS_SUCCESS:
                if (event_add_address.isSuccess()) {
                    Utils.showToast(this, "Added SuccessFully");
                    setResult(RESULT_OK);
                    finish();
                }
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    private void doProgress(boolean b) {
        if (b) {
            mPocketBar.setVisibility(View.VISIBLE);
            mPocketBar.progressiveStart();
        } else {
            mPocketBar.progressiveStop();
            mPocketBar.setVisibility(View.INVISIBLE);
        }
    }

}

package com.apitap.views;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.apitap.R;
import com.apitap.controller.ModelManager;
import com.apitap.model.Constants;
import com.apitap.model.Operations;
import com.apitap.model.Utils;
import com.apitap.model.bean.getaddress.AddressData;
import com.apitap.model.customclasses.Event_Address;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sourcefuse on 25/11/16.
 */

public class PaymentActivity extends Activity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    Activity mActivity;
    Spinner spinner,spinner_type;
    ArrayList<String> address_Ids = new ArrayList<String>();
    ArrayList<String> card_type = new ArrayList<String>();
    EditText nick_nameTv,card_holdernameTv,cvc_tv,card_noTv,expiry_tv;
    String addressid,nick_name,holdername,cvc,card_no,expiry_date,card_typestr;
    Button Submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        mActivity = this;
        initViews();
        setListener();
        ModelManager.getInstance().getAddressManager().getAddresses(mActivity, Operations.makeJsonGetAddress(mActivity), Constants.GET_ADDRESS_SUCCESS);

    }

    private void initViews() {
        spinner  = (Spinner) findViewById(R.id.spinners);
        Submit  = (Button) findViewById(R.id.submit);
        spinner_type  = (Spinner) findViewById(R.id.spinner_type);
        nick_nameTv  = (EditText) findViewById(R.id.nick_name);
        card_holdernameTv  = (EditText) findViewById(R.id.card_holdername);
        cvc_tv  = (EditText) findViewById(R.id.cvc);
        card_noTv  = (EditText) findViewById(R.id.card_number);
        expiry_tv  = (EditText) findViewById(R.id.exp_date);
        card_type.add("MasterCard");
        card_type.add("VISA");
        card_type.add("AMERICAN EXPRESS");

        Submit.setOnClickListener(this);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,R.layout.address_row,R.id.txt_type,card_type);
        spinner_type.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(this);
     /*   recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(20));*/
    }

    private void setListener() {
        findViewById(R.id.back).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.submit:
                addCard();
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
    public void onEvent( final Event_Address event) {
        switch (event.getKey()) {
            case Constants.GET_ADDRESS_SUCCESS:

                if (event.getResponse() != null) {
                    AddCardAddressAdapter adp = new AddCardAddressAdapter(mActivity,event.getResponse());
                    spinner.setAdapter(adp);
                }
                break;
        }
    }

    public void addCard(){
        String cardtypeId ="";
        String card_hex = "";
        card_typestr = spinner_type.getSelectedItem().toString();
        card_no = card_noTv.getText().toString();
        holdername = card_holdernameTv.getText().toString();
        nick_name = nick_nameTv.getText().toString();
        expiry_date = expiry_tv.getText().toString();
        cvc = cvc_tv.getText().toString();

        if (card_typestr.equals("MasterCard")){
            cardtypeId = "00000000062";
        }else if(card_typestr.equals("VISA")){
            cardtypeId = "00000000061";
        }else{
            cardtypeId = "00000000063";
        }


        if (card_typestr.isEmpty()||card_no.isEmpty()||holdername.isEmpty()||nick_name.isEmpty()||expiry_date.isEmpty()||cvc.isEmpty()){
            Toast.makeText(getApplicationContext(),"All Fields neccessary",Toast.LENGTH_SHORT).show();
        }
        else{
            ModelManager.getInstance().getCardManager().addCardDetails(this,
                    Operations.makeJsonAddCreditCard(this,cardtypeId,card_no,nick_name,holdername,cvc,expiry_date,addressid));
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
       String normal_id =address_Ids.get(i);
        addressid = Utils.getElevenDigitId(normal_id);

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public class AddCardAddressAdapter extends BaseAdapter {

        private List<AddressData> response;
        private  LayoutInflater inflater = null;

        private Activity activity;

        public AddCardAddressAdapter( Activity a, List<AddressData> response) {
            this.response = response;
            activity = a;
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }


        @Override
        public int getCount() {
            return response.size();
        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            View vi = view;
            vi = inflater.inflate(R.layout.address_row, null);
            TextView txt_type = (TextView)vi.findViewById(R.id.txt_type);
            address_Ids.add(response.get(position).getAddressId());
            Log.d("rescponseId",response.get(position).getAddressId());

            txt_type.setText(response.get(position).getAddress_type()+" "+response.get(position).getLine1()+" "+response.get(position).getLine2());

            return vi;
        }

    }

}

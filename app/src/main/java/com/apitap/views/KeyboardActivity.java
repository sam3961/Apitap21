package com.apitap.views;

import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.apitap.R;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by apple on 08/08/16.
 */
public class KeyboardActivity extends FragmentActivity implements View.OnClickListener {

    Button btnCount1,btnCount2,btnCount3,btnCount4,btnCount5,btnCount6,
            btnCount7,btnCount8,btnCount9,btnCount10;
    Button[] btnCount = {btnCount1,btnCount2,btnCount3,btnCount4,btnCount5,btnCount6,btnCount7,btnCount8,btnCount9,btnCount10};
    EditText editNumber;

    String number = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_keypad);

        InitView();

        GenerateRandomNumber();

    }

    private void GenerateRandomNumber() {
        ArrayList<Integer> number = new ArrayList<>();
        for (int i = 0; i <= 9; ++i) number.add(i);
        Collections.shuffle(number);

        for(int j = 0; j <=9; j++){
            btnCount[j].setText(""+number.get(j));
        }
    }

    private void InitView() {

        btnCount[0] = (Button)findViewById(R.id.btnCount1);
        btnCount[1] = (Button)findViewById(R.id.btnCount2);
        btnCount[2] = (Button)findViewById(R.id.btnCount3);
        btnCount[3] = (Button)findViewById(R.id.btnCount4);
        btnCount[4] = (Button)findViewById(R.id.btnCount5);
        btnCount[5] = (Button)findViewById(R.id.btnCount6);
        btnCount[6] = (Button)findViewById(R.id.btnCount7);
        btnCount[7] = (Button)findViewById(R.id.btnCount8);
        btnCount[8] = (Button)findViewById(R.id.btnCount9);
        btnCount[9] = (Button)findViewById(R.id.btnCount10);

        for(int i = 0; i <= 9; i++){
            btnCount[i].setOnClickListener(this);
        }

        editNumber = (EditText)findViewById(R.id.editNumber);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnCount1:
                if(number.equals("")){
                    number = btnCount[0].getText().toString();
                }else{
                    number = number+btnCount[0].getText().toString();
                }
                editNumber.setText(number);
                break;
            case R.id.btnCount2:
                if(number.equals("")){
                    number = btnCount[1].getText().toString();
                }else{
                    number = number+btnCount[1].getText().toString();
                }
                editNumber.setText(number);
                break;
            case R.id.btnCount3:
                if(number.equals("")){
                    number = btnCount[2].getText().toString();
                }else{
                    number = number+btnCount[2].getText().toString();
                }
                editNumber.setText(number);
                break;
            case R.id.btnCount4:
                if(number.equals("")){
                    number = btnCount[3].getText().toString();
                }else{
                    number = number+btnCount[3].getText().toString();
                }
                editNumber.setText(number);
                break;
            case R.id.btnCount5:
                if(number.equals("")){
                    number = btnCount[4].getText().toString();
                }else{
                    number = number+btnCount[4].getText().toString();
                }
                editNumber.setText(number);
                break;
            case R.id.btnCount6:
                if(number.equals("")){
                    number = btnCount[5].getText().toString();
                }else{
                    number = number+btnCount[5].getText().toString();
                }
                editNumber.setText(number);
                break;
            case R.id.btnCount7:
                if(number.equals("")){
                    number = btnCount[6].getText().toString();
                }else{
                    number = number+btnCount[6].getText().toString();
                }
                editNumber.setText(number);
                break;
            case R.id.btnCount8:
                if(number.equals("")){
                    number = btnCount[7].getText().toString();
                }else{
                    number = number+btnCount[7].getText().toString();
                }
                editNumber.setText(number);
                break;
            case R.id.btnCount9:
                if(number.equals("")){
                    number = btnCount[8].getText().toString();
                }else{
                    number = number+btnCount[8].getText().toString();
                }
                editNumber.setText(number);
                break;
            case R.id.btnCount10:
                if(number.equals("")){
                    number = btnCount[9].getText().toString();
                }else{
                    number = number+btnCount[9].getText().toString();
                }
                editNumber.setText(number);
                break;
            default:
                break;

        }
    }
}

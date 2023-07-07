package com.apitap.model;

import android.app.ProgressDialog;
import android.content.Context;


/**
 * Created by vijay on 23/1/18.
 */

public class ProgressDialogLoading {

    ProgressDialog progressBar;
    public static ProgressDialogLoading mInstance;

    public ProgressDialogLoading(){
        mInstance=this;
    }
    public static ProgressDialogLoading  getmInstance(){
        return mInstance;
    }

    public void hideProgress(){
        if (progressBar!=null&&progressBar.isShowing())
            progressBar.dismiss();
    }

    public void showProgress(Context context){
        progressBar = new ProgressDialog(context);
        progressBar.setMessage("Loading...");
        progressBar.setCancelable(false);
        progressBar.show();


    }
}

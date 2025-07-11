package com.apitap.views;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.apitap.R;
import com.apitap.controller.ModelManager;
import com.apitap.model.Operations;
import com.apitap.model.Utils;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import static com.apitap.views.HomeActivity.tabLayout;


public class BaseActivity extends AppCompatActivity {
    public TextView tvTitle;
    public EditText editTextSearch;
    public ImageView imageViewBack;
    public ImageView imageViewSearch;
    private Dialog dialogProgressBar;
    private Dialog progressBarAsync;
    Dialog mAlertDialog;
    private int permissionNeeded;
    private ArrayList<String> arrayListSortByName = new ArrayList<>();
    private ArrayList<String> arrayListRatingName = new ArrayList<>();
    private ArrayList<String> arrayListSortById = new ArrayList<>();
    private ArrayList<String> arrayListRatingId = new ArrayList<>();
    private AlertDialog alertDialog;
    public Dialog dialogTempPassword;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    //<editor-fold desc="run time permission check here">
    public int checkPermission(String[] permissions) {
        permissionNeeded = 0;
        if (Build.VERSION.SDK_INT >= 23) {
            for (int i = 0; i < permissions.length; i++) {
                int result = ContextCompat.checkSelfPermission(this, permissions[i]);
                if (result != PackageManager.PERMISSION_GRANTED) {
                    permissionNeeded++;
                }
            }
        }
        return permissionNeeded;
    }
    //</editor-fold>


    public void showLoadingDialog() {

    }

    public void showProgress() {
        if (dialogProgressBar != null && dialogProgressBar.isShowing())
            dialogProgressBar.dismiss();
        dialogProgressBar = Utils.createLoadingDialog(this,true);
        dialogProgressBar.show();
    }

    public void hideProgress() {
        if (dialogProgressBar != null && dialogProgressBar.isShowing())
            dialogProgressBar.dismiss();
    }

    public void showProgressAsync() {
        if (progressBarAsync != null && progressBarAsync.isShowing())
            progressBarAsync.dismiss();
        progressBarAsync = Utils.createLoadingDialog(this,true);
        progressBarAsync.show();
    }

    public void hideProgressAsync() {
        if (progressBarAsync != null && progressBarAsync.isShowing())
            progressBarAsync.dismiss();
    }


    public void startAlphaAnimation(View v, long duration, int visibility) {
        AlphaAnimation alphaAnimation = visibility == 0 ? new AlphaAnimation(0.0f, 1.0f) : new AlphaAnimation(1.0f, 0.0f);
        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
        v.startAnimation(alphaAnimation);
    }

    public void baseshowFeedbackMessage(Activity activity, View view, String message) {
        Snackbar snakbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
//        TextView tv = snakbar.getView().findViewById(R.id.snackbar_text);
        TextView tv = snakbar.getView().findViewById(com.google.android.material.R.id.snackbar_text);
        tv.setTextColor(ContextCompat.getColor(activity, R.color.colorWhite));
        snakbar.getView().setBackgroundColor(ContextCompat.getColor(activity, R.color.colorGreenLogo));
        if (snakbar.isShown()) {
            snakbar.dismiss();
        }
        snakbar.show();
    }

    public ArrayList<String> sortByListNames() {
        arrayListSortByName = new ArrayList<>();
        arrayListSortByName.add("A - Z");
        arrayListSortByName.add("Z - A");
        arrayListSortByName.add("Price - Low to High");
        arrayListSortByName.add("Price - High to Low");
        arrayListSortByName.add("Newest");
        arrayListSortByName.add("Nearest");
        arrayListSortByName.add("Popularity - Most viewed");
        return arrayListSortByName;
    }

    public ArrayList<String> ratingListNames() {
        arrayListRatingName = new ArrayList<>();
        arrayListRatingName.add("1 Star");
        arrayListRatingName.add("2 Star");
        arrayListRatingName.add("3 Star");
        arrayListRatingName.add("4 Star");
        arrayListRatingName.add("5 Star");
        return arrayListRatingName;
    }

    public ArrayList<String> sortByListId() {
        arrayListSortById = new ArrayList<>();
        arrayListSortById.add("120.83-ASC");
        arrayListSortById.add("120.83-DESC");
        arrayListSortById.add("114.98-ASC");
        arrayListSortById.add("114.98-DESC");
        arrayListSortById.add("114.144-DESC");
        arrayListSortById.add("120.11-ASC");
        arrayListSortById.add("114.132-DESC");
        return arrayListSortById;
    }

    public ArrayList<String> ratingListId() {
        arrayListRatingId = new ArrayList<>();
        arrayListRatingId.add("2101");
        arrayListRatingId.add("2102");
        arrayListRatingId.add("2103");
        arrayListRatingId.add("2104");
        arrayListRatingId.add("2105");
        return arrayListRatingId;
    }

    public void setBusinessTabText(String text) {
        View view1 = tabLayout.getTabAt(0).getCustomView();
        TextView textView = view1.findViewById(R.id.tab);
        textView.setText(" Businesses\n  " + text);
    }

    public void setBusinessDefaultTabText() {
        View view1 = tabLayout.getTabAt(0).getCustomView();
        TextView textView = view1.findViewById(R.id.tab);
        textView.setText(" Businesses");
    }


    public void removeLastFrament() {
        getFragmentManager().popBackStack();
    }

    protected void showAlertDialog(String message) {
        alertDialog = new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .setCancelable(false)
                .show();
    }

    public void showConfirmTempPassDialog(final Activity activity) {

        final Dialog dialog = new Dialog(activity,R.style.AppTheme_Dialog_MyDialogTheme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_temp_password_confirm);

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        Button buttonYes = dialog.findViewById(R.id.buttonYes);
        Button buttonNo = dialog.findViewById(R.id.buttonNo);

        buttonYes.setOnClickListener(view -> {
            changeTempPassDialog(activity);
            dialog.dismiss();
        });
        buttonNo.setOnClickListener(view -> dialog.dismiss());
        dialog.show();
    }

    public void changeTempPassDialog(final Activity activity) {
        dialogTempPassword = new Dialog(activity, R.style.AppTheme_Dialog_MyDialogTheme);
        dialogTempPassword.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogTempPassword.setContentView(R.layout.dialog_change_temp_pass);

        dialogTempPassword.setCancelable(false);
        dialogTempPassword.setCanceledOnTouchOutside(false);

        Button buttonSubmit = dialogTempPassword.findViewById(R.id.buttonSubmit);
        EditText editTextNewPassword = dialogTempPassword.findViewById(R.id.editTextNewPassword);
        EditText editTextConfirmPassword = dialogTempPassword.findViewById(R.id.editTextConfirmPassword);


        buttonSubmit.setOnClickListener(view -> {
            Utils.hideKeyboardFrom(activity, view);
            if (editTextNewPassword.getText().toString().isEmpty()) {
                Utils.baseshowFeedbackMessage(activity, view, "Please enter new password.");
            } else if (editTextConfirmPassword.getText().toString().isEmpty()) {
                Utils.baseshowFeedbackMessage(activity, view, "Please enter confirm password.");
            } else if (!Utils.isValidPassword(editTextNewPassword.getText().toString(),
                    editTextNewPassword)) {
                Utils.baseshowFeedbackMessage(activity, view, "Password must be 8 digits and contain at least: 1 CAP letter,a number,a special character");
            } else if (!editTextNewPassword.getText().toString().equals(editTextConfirmPassword.getText().toString())) {
                Utils.baseshowFeedbackMessage(activity, view, "New and Confirm password must be same.");
            } else {
                showProgress();
                ModelManager.getInstance().getforgotManager().updatePassword(
                        BaseActivity.this, Operations.updatePassword(
                                activity, Utils.convertStringToHex(editTextNewPassword.getText().toString())
                        )
                );
            }
        });
        dialogTempPassword.show();
    }


}

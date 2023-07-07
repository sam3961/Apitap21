package com.apitap.views.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.SystemClock;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.apitap.R;
import com.apitap.model.Utils;
import com.apitap.views.HomeActivity;
import com.apitap.views.adapters.AdapterBusinessSelect;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import static com.apitap.views.HomeActivity.tabLayout;

public class BaseFragment extends Fragment implements AdapterBusinessSelect.BusinessClick {

    private AdapterBusinessSelect.BusinessClick businessClick;
    protected int businessSelectedPosition;
    protected String storeName="";
    protected Boolean isBroadcasting=false;
    protected String storeImageUrl="";
    protected boolean isFavouriteStore;
    private static long mLastClickTime = 0;
    public Dialog dialogProgressBar;
    private ArrayList<String> arrayListSortByStoreName = new ArrayList<>();
    private ArrayList<String> arrayListSortByStoreId = new ArrayList<>();
    private ArrayList<String> arrayListSortByName = new ArrayList<>();
    private ArrayList<String> arrayListSortById = new ArrayList<>();

    private ArrayList<String> arrayListRatingName = new ArrayList<>();
    private ArrayList<String> arrayListRatingId = new ArrayList<>();
    private AlertDialog alertDialog;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        businessClick = this;
    }

    public Dialog showBusinessSelectionDialog(ArrayList<String> arrayList) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), getResources().getColor(R.color.transparent));
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_select_business, null);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transparent)));
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        lp.height = ViewGroup.LayoutParams.MATCH_PARENT;
        alertDialog.getWindow().setAttributes(lp);
        alertDialog.setCancelable(true);

        TextView textViewClose = view.findViewById(R.id.tvClose);
        RecyclerView recyclerViewBusiness = view.findViewById(R.id.recyclerViewBusiness);

        recyclerViewBusiness.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL));

        final AdapterBusinessSelect adapterBusinessSelect = new AdapterBusinessSelect(arrayList, businessClick);
        recyclerViewBusiness.setAdapter(adapterBusinessSelect);

        textViewClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });


        alertDialog.show();
        return alertDialog;

    }

    @Override
    public void onBusinessSelect(int position) {
        businessSelectedPosition = position;
    }

    public static boolean waitCode() {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 100) {
            return false;
        } else {
            mLastClickTime = SystemClock.elapsedRealtime();
            return true;

        }
        //   return true;

    }

    public void showProgress() {
        if (dialogProgressBar != null && dialogProgressBar.isShowing())
            dialogProgressBar.dismiss();
        dialogProgressBar = Utils.createLoadingDialog(getActivity(),true);
        dialogProgressBar.show();
    }

    public void hideProgress() {
        if (dialogProgressBar != null && dialogProgressBar.isShowing()) {
            dialogProgressBar.dismiss();
            dialogProgressBar=null;
        }
    }
    public boolean isProgressShowing() {
        boolean progress = false;
        if (dialogProgressBar != null && dialogProgressBar.isShowing()) {
            progress=true;
        }
        return progress;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        hideProgress();
        setBusinessDefaultTabText();
    }

    public ArrayList<String> sortByListStoresNames() {
        arrayListSortByStoreName = new ArrayList<>();
        arrayListSortByStoreName.add("A - Z");
        arrayListSortByStoreName.add("Near to Far");
        arrayListSortByStoreName.add("Far to Near");
        arrayListSortByStoreName.add("Z - A");
        return arrayListSortByStoreName;
    }

    public ArrayList<String> sortByListStoresId() {
        arrayListSortByStoreId = new ArrayList<>();
        arrayListSortByStoreId.add("114.70-ASC");
        arrayListSortByStoreId.add("120.11-ASC");
        arrayListSortByStoreId.add("120.11-DESC");
        arrayListSortByStoreId.add("114.70-DESC");
        return arrayListSortByStoreId;
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

    public ArrayList<String> ratingListId() {
        arrayListRatingId = new ArrayList<>();
        arrayListRatingId.add("2101");
        arrayListRatingId.add("2102");
        arrayListRatingId.add("2103");
        arrayListRatingId.add("2104");
        arrayListRatingId.add("2105");
        return arrayListRatingId;
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

    public void storeFrontTabsView() {
      //  HomeActivity.homeTab2.setBackgroundColor(getActivity().getResources().getColor(R.color.colorWhite));
        //HomeActivity.imageViewHomeTab2.setImageResource(R.drawable.ic_icon_home);
       // HomeActivity.tabContainer2.setVisibility(View.GONE);
       // HomeActivity.tabContainer2.setVisibility(View.GONE);
    }
    public void homeTabsView() {
        //HomeActivity.homeTab2.setBackgroundColor(getActivity().getResources().getColor(R.color.darkBlue));
        //HomeActivity.imageViewHomeTab2.setImageResource(R.drawable.ic_icon_home_white);
       // HomeActivity.tabContainer2.setVisibility(View.GONE);
       // HomeActivity.tabContainer2.setVisibility(View.GONE);
    }


    public void defaultTabsView() {
       // HomeActivity.tabContainer2.setVisibility(View.GONE);
       // HomeActivity.tabContainer1.setVisibility(View.VISIBLE);
    }
    public void hideTabsView() {
       // HomeActivity.tabContainer2.setVisibility(View.GONE);
       // HomeActivity.tabContainer2.setVisibility(View.GONE);
    }

    public void baseshowFeedbackMessage(Activity activity, View view, String message) {
        Snackbar snakbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        TextView tv = snakbar.getView().findViewById(R.id.snackbar_text);
        tv.setTextColor(ContextCompat.getColor(activity, R.color.colorWhite));
        snakbar.getView().setBackgroundColor(ContextCompat.getColor(activity, R.color.colorPrimary));
        if (snakbar.isShown()) {
            snakbar.dismiss();
        }
        snakbar.show();
    }

    public void setBusinessTabText(String text){
       // View view1 = tabLayout.getTabAt(0).getCustomView();
       // TextView textView = view1.findViewById(R.id.tab);
       // textView.setText(" Businesses\n  "+text);
       // tabLayout.getTabAt(0).setText(" Businesses ("+text+")");
    }
    public void setBusinessDefaultTabText(){
     /*   View view1 = tabLayout.getTabAt(0).getCustomView();
        TextView textView = view1.findViewById(R.id.tab);
        textView.setText(" Businesses");*/
       // tabLayout.getTabAt(0).setText(" Businesses");
    }
    public void onBackPress(){
        getFragmentManager().popBackStack();
    }

    protected void showAlertDialog(String message) {
        alertDialog = new AlertDialog.Builder(getActivity())
                .setMessage(message)
                .setPositiveButton("OK", null)
                .setCancelable(false)
                .show();
    }


}

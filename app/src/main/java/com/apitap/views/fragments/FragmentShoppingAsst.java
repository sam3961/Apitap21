package com.apitap.views.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.apitap.R;
import com.apitap.controller.ModelManager;
import com.apitap.model.Constants;
import com.apitap.model.GPSService;
import com.apitap.model.Operations;
import com.apitap.model.Utils;
import com.apitap.model.bean.SearchFavoritesBean;
import com.apitap.model.bean.ShoppingAsstListBean;
import com.apitap.model.customclasses.Event;
import com.apitap.model.preferences.ATPreferences;
import com.apitap.views.HomeActivity;
import com.apitap.views.SearchItemActivity;
import com.apitap.views.adapters.SavedAdapter;
import com.apitap.views.adapters.SavedCatAdapter;
import com.github.rahatarmanahmed.cpv.CircularProgressView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FragmentShoppingAsst extends BaseFragment implements View.OnClickListener {
    ListView mList, mListSubCategory;
    String selectedItem = "";
    Activity mActivity;
    double latitude = 0.0;
    double longitude = 0.0;
    LinearLayout linearLayoutFavoriteStore, linearLayoutNearByStore, linearLayoutSearchAllStore;
    ImageView add_cat, add_subcat;
    TextView tv_itemName, introduction, introduction2;
    EditText name, ed_sub_cat;
    public HashMap<String, ArrayList<String>> mHashMap = new HashMap<>();
    private LinearLayout list_cont, list_cont_items, back_layout;
    FrameLayout container;
    SavedAdapter savedAdapter;
    SavedCatAdapter savedCatAdapter;
    ArrayList<String> mainArrayList = new ArrayList<String>();
    public int defaultPosition = 404;
    int defaultCatPosition = 404;
    RelativeLayout ll_main_view, rl_bottomLayout;
    LinearLayout ll_back, ll_slideBack;

    List<ShoppingAsstListBean.RESULT_> shoppingAsstListBeen = new ArrayList<>();
    String shopping_listId;
    String shopping_itemId;
    boolean isMainListCliked = false;
    boolean custom_notify = false;
    boolean item_Edit = false;
    private ScrollView rootLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.shoping2_assist, container, false);
        // Utils.checkPermission(getActivity());
        mActivity = getActivity();
        initViews(v);
        setListener(v);
        setListenerOnList();
        showProgress();
        ModelManager.getInstance().getAssistantManager().hitApi(mActivity, Operations.getShoppingAssistantList(mActivity), Constants.GET_ASSISTANT_LIST);
        disableViews();
        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void initViews(View v) {
        list_cont = (LinearLayout) v.findViewById(R.id.list_cont);
        ll_back = (LinearLayout) v.findViewById(R.id.back_ll);
        ll_slideBack = (LinearLayout) v.findViewById(R.id.layout_slide);
        list_cont_items = (LinearLayout) v.findViewById(R.id.list_cont_items);
        back_layout = (LinearLayout) v.findViewById(R.id.back_layout);
        mList = (ListView) v.findViewById(R.id.list);
        mListSubCategory = (ListView) v.findViewById(R.id.list_subcategory);
        tv_itemName = (TextView) v.findViewById(R.id.tvitemName);
        introduction = (TextView) v.findViewById(R.id.introduction);
        introduction2 = (TextView) v.findViewById(R.id.introduction2);
        name = (EditText) v.findViewById(R.id.name);
        ed_sub_cat = (EditText) v.findViewById(R.id.ed_sub_cat);
        container = (FrameLayout) v.findViewById(R.id.container);
        ll_main_view = (RelativeLayout) v.findViewById(R.id.ll_main_view);
        rl_bottomLayout = (RelativeLayout) v.findViewById(R.id.bottom_bar);
        linearLayoutFavoriteStore =  v.findViewById(R.id.linearLayoutFavoriteStore);
        add_cat = (ImageView) v.findViewById(R.id.add_category);
        add_subcat = (ImageView) v.findViewById(R.id.sub_category);
        linearLayoutNearByStore =  v.findViewById(R.id.linearLayoutNearByStore);
        linearLayoutSearchAllStore =  v.findViewById(R.id.linearLayoutSearchAllStore);
        rootLayout =  v.findViewById(R.id.rootLayout);
        tabContainer2Visible();
    }


    private void setListener(View v) {
        v.findViewById(R.id.add_category).setOnClickListener(this);
        v.findViewById(R.id.sub_category).setOnClickListener(this);
        v.findViewById(R.id.linearLayoutFavoriteStore).setOnClickListener(this);
        v.findViewById(R.id.linearLayoutNearByStore).setOnClickListener(this);
        v.findViewById(R.id.linearLayoutSearchAllStore).setOnClickListener(this);
        v.findViewById(R.id.back_layout).setOnClickListener(this);
        v.findViewById(R.id.back_ll).setOnClickListener(this);
        mList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                isMainListCliked = true;
                shopping_listId = shoppingAsstListBeen.get(position).get12231();
                showDialog(position);
                return true;
            }
        });
        mListSubCategory.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                isMainListCliked = false;
                shopping_itemId = shoppingAsstListBeen.get(defaultPosition).getIL().get(position).get12217();
                showDialog(position);
                return true;
            }
        });
    }

    View v;
    int position;

    private void setListenerOnList() {
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                isMainListCliked = true;

                shopping_listId = shoppingAsstListBeen.get(i).get12231();
                //  selectedItem = ((TextView) view.findViewById(R.id.name)).getText().toString();
                tv_itemName.setText("List: "+Utils.hexToASCII(shoppingAsstListBeen.get(i).get120157()));
                v = view;
                position = i;
                String pos = String.valueOf(i);
                ATPreferences.putString(mActivity, "shop_asst", pos);
                defaultPosition = i;
                Log.e("defaultPosition", defaultPosition + "");
                searchWord = "";
                itemListVisible();
//                } else {
//                    mListSubCategory.setVisibility(View.GONE);
//                    introduction2.setVisibility(View.VISIBLE);
//                }
                //savedAdapter.customNotify(categoryArray,defaultPosition, defaultCatPosition,true);
            }
        });
        mListSubCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedItem = Utils.hexToASCII(shoppingAsstListBeen.get(defaultPosition).getIL().get(i).get12083());
                defaultCatPosition = i;
                isMainListCliked = false;
                shopping_itemId = shoppingAsstListBeen.get(defaultPosition).getIL().get(i).get12217();
                savedCatAdapter.customNotify(shoppingAsstListBeen, defaultPosition, defaultCatPosition, false);
                //savedAdapter.customNotifyPostion( defaultPosition, defaultCatPosition, false);
            }
        });
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
            case Constants.SEARCH_FAVORITES_SUCCESS:
                //         circularProgressView.setVisibility(View.GONE);
                if (ModelManager.getInstance().getSearchFavoritesManager().searchFavoritesBean == null)
             //       Utils.showToast(getActivity(), "Cart Saved Successfuly");
                Utils.baseshowFeedbackMessage(getActivity(),rootLayout,"Cart Saved Successfuly");


                List<SearchFavoritesBean.RESULT.RESULTDATA> list = ModelManager.getInstance().getSearchFavoritesManager().searchFavoritesBean.getResult().get(0).getResult();
                if (list != null && list.size() > 0) {
                    ll_back.setVisibility(View.VISIBLE);
                    //   displayView(new Fragment_Search_Store(), "SearchFav", new Bundle());
                } else {
                    //Utils.showToast(getActivity(), "No data found");
                    Utils.baseshowFeedbackMessage(getActivity(),rootLayout,"No data found");
                }
                break;

            case -1:
                Utils.baseshowFeedbackMessage(getActivity(),rootLayout,"No data found");
                break;
            case Constants.GET_ASSISTANT_LIST:
                hideProgress();
                shoppingAsstListBeen = ModelManager.getInstance().getAssistantManager().shoppingAsstListBean.getRESULT().get(0).getRESULT();
                if (custom_notify) {
                    if (isMainListCliked)
                        savedAdapter.customNotify(shoppingAsstListBeen, defaultPosition, defaultCatPosition, true);
                    else
                        savedCatAdapter.customNotify(shoppingAsstListBeen, defaultPosition, defaultCatPosition, false);
                } else {
                    savedAdapter = new SavedAdapter(getActivity(), shoppingAsstListBeen, defaultPosition, defaultCatPosition, true);
                    mList.setAdapter(savedAdapter);
                }
                break;

            case Constants.REMOVE_ITEM_IASSISTANT_LIST:
                custom_notify = true;
                ModelManager.getInstance().getAssistantManager().hitApi(mActivity, Operations.getShoppingAssistantList(mActivity), Constants.GET_ASSISTANT_LIST);
           //     Toast.makeText(getActivity(), "Item Deleted", Toast.LENGTH_SHORT).show();
                Utils.baseshowFeedbackMessage(getActivity(),rootLayout,"Item Deleted");
                break;
            case Constants.ADD_ITEM_ASSISTANT_LIST:
                custom_notify = true;
                if (item_Edit)
              //      Toast.makeText(getActivity(), "Item Renamed", Toast.LENGTH_SHORT).show();
                Utils.baseshowFeedbackMessage(getActivity(),rootLayout,"Item Renamed");
                else
                  //  Toast.makeText(getActivity(), "New Item Added", Toast.LENGTH_SHORT).show();
                    Utils.baseshowFeedbackMessage(getActivity(),rootLayout,"New Item Added");

                ModelManager.getInstance().getAssistantManager().hitApi(mActivity, Operations.getShoppingAssistantList(mActivity), Constants.GET_ASSISTANT_LIST);
                break;
            case Constants.EDIT_ASSISTANT_LIST:
                custom_notify = true;
                //Toast.makeText(getActivity(), "List Renamed", Toast.LENGTH_SHORT).show();
                Utils.baseshowFeedbackMessage(getActivity(),rootLayout,"List Renamed");
                ModelManager.getInstance().getAssistantManager().hitApi(mActivity, Operations.getShoppingAssistantList(mActivity), Constants.GET_ASSISTANT_LIST);
                break;
            case Constants.REMOVE_ASSISTANT_LIST:
                custom_notify = true;
               // Toast.makeText(getActivity(), "List Removed", Toast.LENGTH_SHORT).show();
                Utils.baseshowFeedbackMessage(getActivity(),rootLayout,"List Removed");
                ModelManager.getInstance().getAssistantManager().hitApi(mActivity, Operations.getShoppingAssistantList(mActivity), Constants.GET_ASSISTANT_LIST);
                break;
            case Constants.ADD_ASSISTANT_LIST:
                custom_notify = true;
                ModelManager.getInstance().getAssistantManager().hitApi(mActivity, Operations.getShoppingAssistantList(mActivity), Constants.GET_ASSISTANT_LIST);
              ///  Toast.makeText(getActivity(), "List Added", Toast.LENGTH_SHORT).show();
                Utils.baseshowFeedbackMessage(getActivity(),rootLayout,"List Added");
                break;
        }
    }

    String searchWord = "";

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.linearLayoutFavoriteStore:
                if (selectedItem == null || selectedItem.equals("")) {
                   // Toast.makeText(getActivity(), "Please select item", Toast.LENGTH_SHORT).show();
                    Utils.baseshowFeedbackMessage(getActivity(),rootLayout,"Please select item");

                } else {

                    ArrayList<String> listBeen = new ArrayList<>();
                    for (int i = 0; i > shoppingAsstListBeen.get(defaultPosition).getIL().size(); i++) {
                        listBeen.add(shoppingAsstListBeen.get(defaultPosition).getIL().get(i).get12083());
                    }
                    //    circularProgressView.setVisibility(View.VISIBLE);
                    //ll_main_view.setVisibility(View.GONE);
                    //container.setVisibility(View.VISIBLE);
                    Bundle bundle = new Bundle();
                    bundle.putString("asst", selectedItem);
                    bundle.putString("isFav", "true");
                    Log.e("asst", selectedItem + "");
                    startActivity(new Intent(getActivity(), SearchItemActivity.class)
                            .putExtra("key", selectedItem)
                            .putExtra("isFav", "true"));

                    // displayView(new Fragment_Search_Store(), "", bundle);
                    // ((HomeActivity) getActivity()).displayView(new Fragment_Search_Store(), "", bundle);
                    //ll_back.setVisibility(View.VISIBLE);
                    //ModelManager.getInstance().getSearchFavoritesManager().searchFavorites(getActivity(), Operations.makeJsonGetFavourite(getActivity(), selectedItem), 100);
                }
                break;

            case R.id.linearLayoutNearByStore:
                //getLocations();
                if (selectedItem == null || selectedItem.equals("")) {
                    //Toast.makeText(getActivity(), "Please select item", Toast.LENGTH_SHORT).show();
                    Utils.baseshowFeedbackMessage(getActivity(),rootLayout,"Please select item");

                } else {
                    linearLayoutSearchAllStore.performClick();
                    // Toast.makeText(getActivity(), "Currently Not Available", Toast.LENGTH_SHORT).show();
                    //    circularProgressView.setVisibility(View.VISIBLE);
                    //ModelManager.getInstance().getSearchNearByManager().searchNearBy(getActivity(), Operations.makeJsonSearchNearBy(getActivity(), selectedItem, String.valueOf(latitude), String.valueOf(longitude)), 100);
                    //ModelManager.getInstance().getSearchNearByManager().searchNearBy(getActivity(), Operations.makeJsonSearchNearBy(getActivity(), selectedItem, String.valueOf(latitude), String.valueOf(longitude)), 100);
                }
                break;

            case R.id.linearLayoutSearchAllStore:
                if (selectedItem == null || selectedItem.equals("")) {
                    Toast.makeText(getActivity(), "Please select item", Toast.LENGTH_SHORT).show();
                } else {

                    ArrayList<String> listBeen = new ArrayList<>();
                    for (int i = 0; i > shoppingAsstListBeen.get(defaultPosition).getIL().size(); i++) {
                        listBeen.add(shoppingAsstListBeen.get(defaultPosition).getIL().get(i).get12083());
                    }
                    //    circularProgressView.setVisibility(View.VISIBLE);
                    //ll_main_view.setVisibility(View.GONE);
                    //container.setVisibility(View.VISIBLE);
                    Bundle bundle = new Bundle();
                    bundle.putString("asst", selectedItem);
                    bundle.putString("isFav", "false");
                    Log.e("asst", selectedItem + "");
                    // displayView(new Fragment_Search_Store(), "", bundle);
                    //((HomeActivity) getActivity()).displayView(new Fragment_Search_Store(), "", bundle);
                    startActivity(new Intent(getActivity(), SearchItemActivity.class)
                            .putExtra("key", selectedItem));

                    ll_back.setVisibility(View.VISIBLE);
                }
                break;

            case R.id.back_ll:
                if (container.getVisibility()==View.VISIBLE) {
                    ll_main_view.setVisibility(View.VISIBLE);
                    container.setVisibility(View.GONE);
                    ll_back.setVisibility(View.GONE);
                }else {
                    getActivity().startActivity(new Intent(getActivity(), HomeActivity.class));
                }
                break;

            case R.id.add_category:
                //addToList(false,"cat");
                if (!name.getText().toString().trim().isEmpty()) {
                    String str_name = name.getText().toString();
                    //  addCatgory(false, name);
                    ModelManager.getInstance().getAssistantManager().hitApi(mActivity, Operations.addshoppingList(mActivity, Utils.convertStringToHex(str_name)), Constants.ADD_ASSISTANT_LIST);
                    showProgress();
                    name.setText("");
                    isMainListCliked = true;
                    hideKeyboard(name);
                } else
                    //Toast.makeText(getActivity(), "Please input some title", Toast.LENGTH_SHORT).show();
                    Utils.baseshowFeedbackMessage(getActivity(),rootLayout,"Please input some title");

                break;

            case R.id.sub_category:
                if (v == null) {
                  //  Toast.makeText(getActivity(), "Please select category", Toast.LENGTH_SHORT).show();
                    Utils.baseshowFeedbackMessage(getActivity(),rootLayout,"Please select category");

                } else {
                    // addCatgory(true, ed_sub_cat);
                    showProgress();
                    isMainListCliked = false;
                    item_Edit = false;
                    String item_name = ed_sub_cat.getText().toString();
                    ModelManager.getInstance().getAssistantManager().hitApi(mActivity, Operations.addItemToShoppingList(mActivity, shopping_listId, Utils.convertStringToHex(item_name), "", false), Constants.ADD_ITEM_ASSISTANT_LIST);
                    ed_sub_cat.setText("");
                    hideKeyboard(ed_sub_cat);
                }
                break;

            case R.id.back_layout:
                savedListVisible();
                defaultCatPosition = 404;
                break;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utils.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                }
                break;
        }
    }

    public String getLocations() {
        String address = "";
        GPSService mGPSService = new GPSService(mActivity);
        mGPSService.getLocation();
        boolean b = Utils.checkLocationPermission(mActivity);
        if (!b) {

            // Here you can ask the user to try again, using return; for that
            //Toast.makeText(mActivity, "Your location is not available, please try again.", Toast.LENGTH_SHORT).show();
            return "";

            // Or you can continue without getting the location, remove the return; above and uncomment the line given below
            // address = "Location not available";
        } else {

            // Getting location co-ordinates
            latitude = mGPSService.getLatitude();
            longitude = mGPSService.getLongitude();
            // Toast.makeText(getActivity(), "Latitude:" + latitude + " | Longitude: " + longitude, Toast.LENGTH_LONG).show();

            address = mGPSService.getLocationAddress();

        }

        //Toast.makeText(context, "Your address is: " + address, Toast.LENGTH_SHORT).show();
        // make sure you close the gps after using it. Save user's battery power
        mGPSService.closeGPS();
        return address;
    }

    public void tabContainer2Visible() {
        //homeTab2.setBackgroundColor(getActivity().getResources().getColor(R.color.colorWhite));
        //imageViewHomeTab2.setImageResource(R.drawable.ic_icon_home);

        HomeActivity.tabContainer2.setVisibility(View.GONE);
        HomeActivity.tabContainer2.setVisibility(View.GONE);
    }

    public void itemListVisible() {

        savedCatAdapter = new SavedCatAdapter(getActivity(), shoppingAsstListBeen, defaultPosition, defaultCatPosition, false);
        mListSubCategory.setAdapter(savedCatAdapter);
        ll_slideBack.setVisibility(View.VISIBLE);
        rl_bottomLayout.setVisibility(View.VISIBLE);
        enableViews();
        list_cont.setVisibility(View.GONE);
        list_cont_items.setVisibility(View.VISIBLE);
        Animation animation1 =
                AnimationUtils.loadAnimation(getActivity(),
                        R.anim.slide_left);
        list_cont_items.setAnimation(animation1);

        if (shoppingAsstListBeen.get(defaultPosition).getIL().size() == 0) {
            mListSubCategory.setVisibility(View.GONE);
            introduction2.setVisibility(View.VISIBLE);
        } else {
            mListSubCategory.setVisibility(View.VISIBLE);
            introduction2.setVisibility(View.GONE);
        }
        //  list_cont.setAnimation(animation2);

    }

    public void savedListVisible() {
        ll_slideBack.setVisibility(View.GONE);
       // rl_bottomLayout.setVisibility(View.GONE);
        disableViews();
        list_cont.setVisibility(View.VISIBLE);
        Animation animation2 =
                AnimationUtils.loadAnimation(getActivity(),
                        R.anim.slide_right);
        list_cont_items.setAnimation(animation2);
        list_cont_items.setVisibility(View.GONE);


        //savedAdapter = new SavedAdapter(getActivity(), shoppingAsstListBeen, defaultPosition, defaultCatPosition, true);
        // mList.setAdapter(savedAdapter);
        savedAdapter.customNotify(shoppingAsstListBeen, defaultPosition, defaultCatPosition, true);

        if (shoppingAsstListBeen.size() == 0) {
            mList.setVisibility(View.GONE);
            introduction.setVisibility(View.VISIBLE);
        } else {
            mList.setVisibility(View.VISIBLE);
            introduction.setVisibility(View.GONE);
        }

    }

    private void disableViews() {
        linearLayoutFavoriteStore.setAlpha(0.5f);
        linearLayoutNearByStore.setAlpha(0.5f);
        linearLayoutSearchAllStore.setAlpha(0.5f);
        linearLayoutNearByStore.setEnabled(false);
        linearLayoutSearchAllStore.setEnabled(false);
        linearLayoutFavoriteStore.setEnabled(false);

    }
    private void enableViews() {
        linearLayoutFavoriteStore.setAlpha(1.0f);
        linearLayoutNearByStore.setAlpha(1.0f);
        linearLayoutSearchAllStore.setAlpha(1.0f);
        linearLayoutNearByStore.setEnabled(true);
        linearLayoutSearchAllStore.setEnabled(true);
        linearLayoutFavoriteStore.setEnabled(true);
    }

    public void showDialog(final int position) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.setContentView(R.layout.delete_item_dialog_fs_assit);

        // set the custom dialog components - text, image and button
        final TextView titleName = (TextView) dialog.findViewById(R.id.delet_btn);
        final ImageView img_edit = (ImageView) dialog.findViewById(R.id.img_edit_name);
        final EditText txt_editt_name = (EditText) dialog.findViewById(R.id.txt_edit_name);
        if (!isMainListCliked)
            titleName.setText("Delete Item");
        titleName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgress();
                if (isMainListCliked)
                    ModelManager.getInstance().getAssistantManager().hitApi(mActivity, Operations.deleteShoppingList(mActivity, shopping_listId), Constants.REMOVE_ASSISTANT_LIST);
                else
                    ModelManager.getInstance().getAssistantManager().hitApi(mActivity, Operations.removeItemToShoppingList(mActivity, shopping_itemId), Constants.REMOVE_ITEM_IASSISTANT_LIST);

                dialog.dismiss();
            }
        });
        img_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name_edit = txt_editt_name.getText().toString();
                //name.setText(name_edit);
                item_Edit = true;
                showProgress();
                if (isMainListCliked)
                    ModelManager.getInstance().getAssistantManager().hitApi(mActivity, Operations.editshoppingList(mActivity, Utils.convertStringToHex(name_edit), shopping_listId), Constants.EDIT_ASSISTANT_LIST);
                else
                    ModelManager.getInstance().getAssistantManager().hitApi(mActivity, Operations.addItemToShoppingList(mActivity, shopping_listId, Utils.convertStringToHex(name_edit), shopping_itemId, true), Constants.EDIT_ASSISTANT_LIST);
                hideKeyboard(txt_editt_name);
                //  Toast.makeText(getActivity(), "Name Edited", Toast.LENGTH_SHORT).show();
                dialog.dismiss();

            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

    }

    public void hideKeyboard(EditText editText) {
        InputMethodManager inputMgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMgr.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

}

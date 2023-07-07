package com.apitap.views;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.apitap.R;
import com.apitap.controller.ModelManager;
import com.apitap.model.AddTabBar;
import com.apitap.model.Constants;
import com.apitap.model.Operations;
import com.apitap.model.Utils;
import com.apitap.model.bean.DetailsBean;
import com.apitap.model.bean.GetCardBean;
import com.apitap.model.bean.GetDeliveryMerchantBean;
import com.apitap.model.bean.ProductOptions2Bean;
import com.apitap.model.bean.ProductOptionsBean;
import com.apitap.model.bean.ShoppingCartDetailBean;
import com.apitap.model.bean.ShoppingCompBean;
import com.apitap.model.bean.getaddress.AddressData;
import com.apitap.model.customclasses.Event;
import com.apitap.model.customclasses.Event_Address;
import com.apitap.model.preferences.ATPreferences;
import com.apitap.views.NavigationMenu.FragmentDrawer;
import com.apitap.views.adapters.SpinnerAdapter;
import com.apitap.views.fragments.items.FragmentItems;
import com.apitap.views.fragments.messages.FragmentMessages;
import com.apitap.views.fragments.FragmentScanner;
import com.apitap.views.fragments.ads.FragmentAds;
import com.github.gcacace.signaturepad.views.SignaturePad;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shami on 2/12/16.
 */

public class ShoppingCartDetailActivity extends AppCompatActivity implements View.OnClickListener, FragmentDrawer.FragmentDrawerListener {
    String companyName, totalAmount, companyLogo;
    double totalAmountDouble;
    TextView header, txt_time, txt_date;
    public static JSONArray arrchoice = new JSONArray();
    public static JSONObject objchoice = new JSONObject();
    public static JSONObject objchoice2 = new JSONObject();
    RelativeLayout layout;
    private FragmentDrawer drawerFragment;
    private Toolbar mToolbar;
    Double tip_price = 0.00;
    double total_int = 0.0;
    String merchantId = "";
    public CircularProgressView mPocketBar;
    public static String specialInstr;
    private boolean isGuest = false;
    private RecyclerView recycler;
    LinearLayout tipadd, tipcancel;
    Button storeBrowse;
    private Button btn_remove, btn_edit, btn_submit, btn_save_Later;
    static TextView sub_total, taxtv, tip, total;
    private ShoppingCompBean shoppingCompBean;
    ImageView logo_merchant;
    private List<ShoppingCartDetailBean.RESULT.DetailData> response;
    public static String deleteitemposition = "";
    public static Activity mActivity;
    String isTip = "";
    boolean card_Available = false;
    Spinner spinner_delivery, spinner_shippingad;
    ArrayList<String> address_Ids = new ArrayList<String>();
    ArrayList<String> deliver_ids = new ArrayList<String>();
    ArrayList<String> deliver_costs = new ArrayList<String>();
    String deliver_Id = "";
    LinearLayout back;
    String shipping_id = "";
    String card_token = "";
    String card_Id = "";
    EditText specialInstructions;
    Spinner payment_method;
    public static String Choice_Id;
    public static String Choice_Price;
    ArrayList<ProductOptionsBean> productOptionsArrayList = new ArrayList<ProductOptionsBean>();
    ArrayList<ProductOptions2Bean> productOptionsArrayList2 = new ArrayList<ProductOptions2Bean>();
    ArrayList<String> productOptionsArrayList2Str = new ArrayList<String>();
    ArrayList<String> productOptionsArrayList3 = new ArrayList<String>();
    GetOption1 adp1;
    GetOption2 adp2;
    LinearLayout search_tool;
    LinearLayout scan_tool;
    LinearLayout msg_tool;
    ArrayList<GetCardBean> getCardBeen = new ArrayList<GetCardBean>();
    ArrayList<GetCardBean> addCards = new ArrayList<GetCardBean>();
    ArrayList<String> card_String = new ArrayList<String>();
    ArrayList<String> card_Ids = new ArrayList<String>();
    ArrayList<String> option_Ids = new ArrayList<String>();
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private static int toolint = 0;
    public static String productIdiS = "";
    public static TabLayout tabLayout;
    private TextView tabOne, tabTwo, tabThree, tabFour, tabFive, tabSix;
    private ImageView  homeTab2;
    LinearLayout tabConatiner;
    private Spinner option1, option2;
    JSONArray jsonArray = new JSONArray();
    int state = 0;
    String option_Id, option_Id2;
    String str_option_Id = "", str_option_Id2 = "";
    LinearLayout viewMain;
    FrameLayout frameLayout;
    ImageView storeImage;
    TextView storeName, cartId, delivery_txt;
    public double sub_Total = 0.00;
    public double grand_Total;
    ImageView iv_back;
    private int itemPosition = 404;
    double options_Price;
    public boolean isCurrentMerchantFav=false;
    private DrawerLayout rootLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkout_screen);
        mActivity = this;
        initViews();
        getBundles();

    }

    private void initViews() {

        isGuest = ATPreferences.readBoolean(this, Constants.GUEST);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        storeImage = (ImageView) findViewById(R.id.adstoreImg);
        rootLayout=findViewById(R.id.drawer_layout);
        iv_back = (ImageView) findViewById(R.id.tv_back);
        storeName = (TextView) findViewById(R.id.storeName);
        delivery_txt = (TextView) findViewById(R.id.delivery_cost);
        sub_total = (TextView) findViewById(R.id.sub_total);
        back = (LinearLayout) findViewById(R.id.iv_back);
        payment_method = (Spinner) findViewById(R.id.payment_method);
        logo_merchant = (ImageView) findViewById(R.id.logocmp);
        taxtv = (TextView) findViewById(R.id.taxtv);
        total = (TextView) findViewById(R.id.total);
        cartId = (TextView) findViewById(R.id.txt_cartId);
        tip = (TextView) findViewById(R.id.tip);
        btn_remove = (Button) findViewById(R.id.remove);
        btn_edit = (Button) findViewById(R.id.edit);
        recycler = (RecyclerView) findViewById(R.id.recycler);
        storeBrowse = (Button) findViewById(R.id.details_store);
        header = (TextView) findViewById(R.id.header_txt);
        btn_submit = (Button) findViewById(R.id.submit);
        txt_time = (TextView) findViewById(R.id.time);
        txt_date = (TextView) findViewById(R.id.txt_date);
        btn_save_Later = (Button) findViewById(R.id.save_later);
        mPocketBar = (CircularProgressView) findViewById(R.id.pocket);
        spinner_delivery = (Spinner) findViewById(R.id.delivery);
        tabConatiner = (LinearLayout) findViewById(R.id.tab_container);
        homeTab2 = (ImageView) findViewById(R.id.tab_one_image);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        search_tool = (LinearLayout) findViewById(R.id.ll_search);
        scan_tool = (LinearLayout) findViewById(R.id.ll_scan);
        msg_tool = (LinearLayout) findViewById(R.id.ll_message);
        viewMain = (LinearLayout) findViewById(R.id.linear);
        specialInstructions = (EditText) findViewById(R.id.specialInstructions);
        frameLayout = (FrameLayout) findViewById(R.id.container_body);


        spinner_shippingad = (Spinner) findViewById(R.id.shipping);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.addItemDecoration(new com.apitap.views.customviews.DividerItemDecoration(this, R.drawable.divider_grey));


        findViewById(R.id.storeName).setOnClickListener(this);

        spinner_delivery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String deliver = deliver_ids.get(i);
                deliver_Id = Utils.getElevenDigitId(deliver);
                delivery_txt.setText("Delivery:$" + deliver_costs.get(i));
                double totalPrice = Double.parseDouble(deliver_costs.get(i)) + total_int;
                total.setText("Total: $" + (String.format("%.2f", totalPrice)));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinner_shippingad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String shipping = address_Ids.get(i);
                shipping_id = Utils.getElevenDigitId(shipping);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        tip.setOnClickListener(this);
        btn_remove.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
        btn_edit.setOnClickListener(this);
        search_tool.setOnClickListener(this);
        scan_tool.setOnClickListener(this);
        msg_tool.setOnClickListener(this);
        storeBrowse.setOnClickListener(this);
        btn_save_Later.setOnClickListener(this);
        iv_back.setOnClickListener(this);


        AddTabBar.getmInstance().setupViewPager(tabLayout);
        AddTabBar.getmInstance().setupTabIcons(tabLayout, mActivity,
                tabOne, tabTwo, tabThree, tabFour, tabFive, tabSix,homeTab2);
        AddTabBar.getmInstance().bindWidgetsWithAnEvent(tabConatiner, tabLayout, ShoppingCartDetailActivity.this, R.id.container_body2);

    }

    private void getBundles() {
        Bundle b = getIntent().getExtras();
        if (b != null) {
            companyName = b.getString("companyName");
            totalAmount = b.getString("totalprice");
            companyLogo = b.getString("compnyimg");
            merchantId = b.getString("compnyID");
            String DateTime = b.getString("dateTime");
            String CartId = b.getString("cartId");
            cartId.setText("Shopping Cart No.: " + CartId);
            total_int = 10.00;
            taxtv.setText("Tax: $10.00");
            //  total.setText("Total: $" + (String.format("%.2f", total_int)));
            //tip.setText("Tip: $");
            //  sub_total.setText("$" + totalAmount);
            storeName.setText(companyName);
            txt_date.setText("Date: " + Utils.changeInvoiceDateFormat(DateTime));

            txt_time.setText("Time: " + Utils.getTimeFromInvoice(DateTime));
            Picasso.get().load(ATPreferences.readString(mActivity, Constants.KEY_IMAGE_URL) + companyLogo).into(logo_merchant);
            Picasso.get().load(ATPreferences.readString(mActivity, Constants.KEY_IMAGE_URL) + companyLogo).into(storeImage);
            Log.d("cmplogo", ATPreferences.readString(mActivity, Constants.KEY_IMAGE_URL) + companyLogo + "");
            shoppingCompBean = (ShoppingCompBean) b.getSerializable("shoppingCart");
            mPocketBar.setVisibility(View.VISIBLE);

            ModelManager.getInstance().getMerchantManager().getMerchantDetail(this,
                    Operations.makeJsonGetMerchantDetail(this, merchantId), Constants.GET_MERCHANT_SUCCESS);

            ModelManager.getInstance().getShoppingCartItemManager().getShoppingCartItems(ShoppingCartDetailActivity.this, Operations.makeJsonGetShoppingCartItem(ShoppingCartDetailActivity.this, shoppingCompBean, ""));

            // ModelManager.getInstance().getShoppingManager().getShoppingCart(ShoppingCartDetailActivity.this, Operations.makeJsonGetShoppingCart(ShoppingCartDetailActivity.this, shoppingCompBean));
            ModelManager.getInstance().getAddressManager().getAddresses(mActivity, Operations.makeJsonGetAddress(mActivity), Constants.GET_ADDRESS_SUCCESS);
            //!above one for purchase items
            ModelManager.getInstance().getCardManager().getCardDetails(ShoppingCartDetailActivity.this, Operations.getJsonCard(ShoppingCartDetailActivity.this));

            ModelManager.getInstance().getMerchantManager().merchantDelivery(this,
                    Operations.makeJsonGetMerchantDelivery(this, merchantId), Constants.GET_Delivery_Merchant);

            ModelManager.getInstance().getMerchantFavouriteManager().getFavourites(this,
                    Operations.makeJsonGetMerchantFavourite(this));

        }
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
    public void onEvent(final Event_Address event) {
        switch (event.getKey()) {
            case Constants.GET_ADDRESS_SUCCESS:

                if (event.getResponse() != null) {
                    GetAddressAdapter adp = new GetAddressAdapter(mActivity, event.getResponse());
                    spinner_shippingad.setAdapter(adp);
                }
                break;
        }
    }

    @Subscribe
    public void onEvent(Event event) {
        switch (event.getKey()) {
            case Constants.SHOPPING_DETAIL_SUCCESS:
                if (recycler.getVisibility() == View.INVISIBLE) {
                    mPocketBar.setVisibility(View.GONE);
               //     Toast.makeText(mActivity, "Cart Edit Successfuly", Toast.LENGTH_SHORT).show();
                    Utils.baseshowFeedbackMessage(ShoppingCartDetailActivity.this,rootLayout,"Cart Edit Successfuly");

                }
                sub_Total=0;
                response = ModelManager.getInstance().getShoppingCartItemManager().detailBean.getRESULT().get(0).getRESULT();
                recycler.setAdapter(new CartItemAdapter(this, companyName, response));
                recycler.setVisibility(View.VISIBLE);
                break;

            case Constants.SAVE_CART_SUCCESS:
                mPocketBar.setVisibility(View.GONE);
               // Toast.makeText(mActivity, "Cart Saved Successfuly", Toast.LENGTH_SHORT).show();
                Utils.baseshowFeedbackMessage(ShoppingCartDetailActivity.this,rootLayout,"Cart Saved Successfuly");

                break;

            case Constants.GET_MERCHANT_SUCCESS:
                isTip = ModelManager.getInstance().getMerchantManager().merchantDetailBean.getRESULT().get(0).getRESULT().get(0).getIsTipAvail();
                Log.d("istips", isTip);
                if (isTip.equals("false")) {
                    tip.setVisibility(View.GONE);
                    taxtv.setGravity(Gravity.END);
                     LinearLayout.LayoutParams  layoutParams=  (LinearLayout.LayoutParams)total.getLayoutParams();
                    layoutParams.weight= (float) 0.40;
                    total.setLayoutParams(layoutParams);

                }

                break;

            case Constants.ITEM_DELETED:
                ModelManager.getInstance().getShoppingCartItemManager().getShoppingCartItems(ShoppingCartDetailActivity.this, Operations.makeJsonGetShoppingCartItem(ShoppingCartDetailActivity.this, shoppingCompBean, ""));
                //PocketBar.setVisibility(View.GONE);
                mPocketBar.setVisibility(View.GONE);
                break;

            case Constants.REMOVE_CARD_SUCCESS:
                ModelManager.getInstance().getCardManager().getCardDetails(ShoppingCartDetailActivity.this, Operations.getJsonCard(ShoppingCartDetailActivity.this));

                break;
            case Constants.GET_CARD_SUCCESS:
                card_Available = true;

                getCardBeen = ModelManager.getInstance().getCardManager().getCardBeen;
                addCards = ModelManager.getInstance().getCardManager().getCardAdd;
                SetDeliveryMethod addCardAddressAdapter = new SetDeliveryMethod(mActivity, getCardBeen);
                payment_method.setAdapter(addCardAddressAdapter);
                payment_method.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        card_token = getCardBeen.get(i).getCard_number();
                        card_Id = card_Ids.get(i);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });


                break;
            case Constants.GET_CARD_SUCCESS_Empty:

                ArrayList<String> arrayListnew = new ArrayList<String>();
                arrayListnew.add("Add Payment Method");
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.address_row2, R.id.txt_type, arrayListnew);
                payment_method.setAdapter(arrayAdapter);
                payment_method.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        addCard();
                    }
                });

                card_Available = false;

                break;
            case Constants.GET_Delivery_Merchant:
                ArrayList<GetDeliveryMerchantBean> arrayList = new ArrayList<GetDeliveryMerchantBean>();
                arrayList = ModelManager.getInstance().getMerchantManager().getDeliveryMerchantBeanArrayList;

                GetDeliveryMerchant adp = new GetDeliveryMerchant(mActivity, arrayList);
                spinner_delivery.setAdapter(adp);
                mPocketBar.setVisibility(View.GONE);
                total_int = total_int + sub_Total;
                break;

            case Constants.GET_SHOPPING_SUCCESS:
                signatureDialog();
                mPocketBar.setVisibility(View.GONE);
                break;
            case Constants.GET_SHOPPING_FAILED:
               // Toast.makeText(this, "Try Again", Toast.LENGTH_SHORT).show();
                Utils.baseshowFeedbackMessage(ShoppingCartDetailActivity.this,rootLayout,"Try Again");

                break;

            case Constants.GET_OPTIONS1_SUCCESS:
                productOptionsArrayList = ModelManager.getInstance().getProductOptions().arrayOptions1;
                adp1 = new GetOption1(mActivity, productOptionsArrayList);
                break;

            case Constants.GET_OPTIONS2_SUCCESS:
                state++;
                productOptionsArrayList2 = ModelManager.getInstance().getProductOptions().arrayOptions2;
                productOptionsArrayList2Str = ModelManager.getInstance().getProductOptions().arrayOptionsStr;
                adp2 = new GetOption2(mActivity, productOptionsArrayList2);
                //option2_spinner.setAdapter(adp2);

                if (productOptionsArrayList.size() == 1) {
                    option1.setAdapter(adp2);
                    for (int i = 0; i < productOptionsArrayList.size(); i++) {
                        Log.d("SpinnerName", response.get(itemPosition).getcH().get(0).get_12716() + "  " + productOptionsArrayList2.get(i).getChoice_name());
                        if (response.get(itemPosition).getcH().get(0).get_12716().equals(productOptionsArrayList2.get(i).getChoice_name())) {
                            option1.setSelection(i);
                        }
                    }
                }
                if (layout.getVisibility() == View.VISIBLE && state == 1) {
                    ModelManager.getInstance().getProductOptions().getOption2(this, Operations.makeJsonGetOptions2(this, option_Id2), 1);
                }
                if (state > 1) {
                    option2.setAdapter(adp2);
                    productOptionsArrayList3 = ModelManager.getInstance().getProductOptions().arrayOptionsStr2;
                    for (int i = 0; i < productOptionsArrayList2.size(); i++) {
                        Log.d("SpinnerName2", response.get(itemPosition).getcH().get(1).get_12716() + "  " + productOptionsArrayList2.get(i).getChoice_name());
                        if (response.get(itemPosition).getcH().get(1).get_12716().equals(productOptionsArrayList2.get(i).getChoice_name())) {
                            option2.setSelection(i);
                        }
                    }

                } else {
                    option1.setAdapter(adp2);
                    for (int i = 0; i < productOptionsArrayList.size(); i++) {
                        Log.d("SpinnerName", response.get(itemPosition).getcH().get(0).get_12716() + "  " + productOptionsArrayList2.get(i).getChoice_name());
                        if (response.get(itemPosition).getcH().get(0).get_12716().equals(productOptionsArrayList2.get(i).getChoice_name())) {
                            option1.setSelection(i);
                        }
                    }

                }
                break;
            case Constants.SHOPPING_SUCCESS:
                String id = "";
                if (!event.getResponse().isEmpty()) {
                    id = event.getResponse();
                }
                //mPocketBar.setVisibility(View.GONE);
                ModelManager.getInstance().getShoppingCartItemManager().getShoppingCartItems(ShoppingCartDetailActivity.this, Operations.makeJsonGetShoppingCartItem(ShoppingCartDetailActivity.this, shoppingCompBean, id));
                recycler.setVisibility(View.INVISIBLE);
                break;

            case Constants.GET_MERCHANT_FAVORITES:
                //ArrayList<String> merchantFavList = ModelManager.getInstance().getMerchantFavouriteManager().mernchantfavlist;
                ArrayList<String> merchantFavList = ModelManager.getInstance().getMerchantFavouriteManager().mernchantfavlist;
                if (merchantFavList.contains(companyName)) {
                    isCurrentMerchantFav = true;
                    setFavouriteMerchantView(storeName);
                }
                break;

            case Constants.ADD_MERCHANT_FAVORITE_SUCCESS:
                //    if (isFavorite.equals("1"))
                mPocketBar.setVisibility(View.GONE);
                isCurrentMerchantFav = true;
                setFavouriteMerchantView(storeName);
                break;
            case Constants.REMOVE_MERCHANT_FAVORITES:
                mPocketBar.setVisibility(View.GONE);
                isCurrentMerchantFav = false;
                setFavouriteMerchantView(storeName);
                break;

        }
    }


    public void setFavouriteMerchantView(TextView tvStoreName) {
        if (isCurrentMerchantFav) {
            tvStoreName.setBackground(mActivity.getResources().getDrawable(R.drawable.back_round_green_border));
            tvStoreName.setTextColor(mActivity.getResources().getColor(R.color.colorGreen));
            tvStoreName.setCompoundDrawablesWithIntrinsicBounds(R.drawable.rate_star_green, 0, 0, 0);
        } else {
            tvStoreName.setBackground(mActivity.getResources().getDrawable(R.drawable.back_round_blue_border));
            tvStoreName.setTextColor(mActivity.getResources().getColor(R.color.colorBlue));
            tvStoreName.setCompoundDrawablesWithIntrinsicBounds(R.drawable.rate_star_blue, 0, 0, 0);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tip:
//                if (isTip.equals("false"))
//                    Toast.makeText(mActivity, "Sorry this merchant does'nt support Tip", Toast.LENGTH_SHORT).show();
//                else
                tipdialog();
                break;
            case R.id.submit:
                if (!card_Available)
                    addCard();
                else
                    // payDialog();
                    afterSubmit();
                break;

            case R.id.storeName:
                mPocketBar.setVisibility(View.VISIBLE);
                if (!isCurrentMerchantFav) {
                    ModelManager.getInstance().getAddMerchantFavorite().addMerchantToFavorite(mActivity, Operations.makeJsonMerchantAddToFavorite(mActivity, merchantId));
                } else
                    ModelManager.getInstance().getMerchantFavouriteManager().removeFavourite(mActivity,
                            Operations.makeJsonRemoveMerchantFavourite(mActivity, merchantId));
                break;

            case R.id.ll_message:
                if (isGuest) {
                   // Toast.makeText(mActivity, "Sorry! Please register account first.", Toast.LENGTH_SHORT).show();
                    Utils.baseshowFeedbackMessage(ShoppingCartDetailActivity.this,rootLayout,"Sorry! Please register account first.");

                } else {
                    frameLayout.setVisibility(View.VISIBLE);
                    viewMain.setVisibility(View.GONE);
                    displayView(new FragmentMessages(), Constants.TAG_MESSAGEPAGE, new Bundle());
                }
                break;

            case R.id.ll_scan:
                frameLayout.setVisibility(View.VISIBLE);
                viewMain.setVisibility(View.GONE);
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
            case R.id.remove:
                //removeItem();
                ModelManager.getInstance().getDeleteItemManager().deleteItem(mActivity, Operations.makeJsonDeleteItemFromCart(mActivity, deleteitemposition));
                mPocketBar.setVisibility(View.VISIBLE);
                break;
            case R.id.edit:
//                editItemDialog();
                if (itemPosition != 404)
                    showdialog();
                else
                    //Toast.makeText(mActivity, "Please Select Item for Edit", Toast.LENGTH_SHORT).show();
                    Utils.baseshowFeedbackMessage(ShoppingCartDetailActivity.this,rootLayout,"Please Select Item for Edit");

                break;
            case R.id.details_store:
                ATPreferences.putBoolean(mActivity, Constants.HEADER_STORE, true);
                ATPreferences.putString(mActivity, Constants.MERCHANT_ID, merchantId);
                startActivity(new Intent(mActivity, HomeActivity.class));
                break;
            case R.id.ll_search:
                //Utils.showSearchDialog(this,"ShoppingCart");
                if (isGuest)
                    Utils.showGuestDialog(this);
                else {
                    displayView(new FragmentFavourite(), Constants.TAG_FAVOURITEPAGE, null);
                }
                break;
            case R.id.tv_back:
                backPressed();
                break;
            case R.id.save_later:
                ModelManager.getInstance().getShoppingCartItemManager().saveShoppingCart(ShoppingCartDetailActivity.this, Operations.makeSaveCart(ShoppingCartDetailActivity.this, Utils.getElevenDigitId(shoppingCompBean.getShoppingCartId())));
                mPocketBar.setVisibility(View.VISIBLE);
                break;
        }
    }

    public void afterSubmit() {
        specialInstr = specialInstructions.getText().toString();
        mPocketBar.setVisibility(View.VISIBLE);
        String tip_is = "";
        String total_amount = total.getText().toString();
        String total_Amount_Is = total_amount.substring(8);
        String tax = "10.00";
        if (tip.getText().toString().equals("Add Tip"))
            tip_is = "0.00";
        else
            tip_is = tip.getText().toString();

        if (objchoice2.has("114.98")) {
            arrchoice.put(objchoice);
            arrchoice.put(objchoice2);
            Log.d("arrch", arrchoice.toString());

        } else
            arrchoice.put(objchoice);

        if (!specialInstr.isEmpty())
            specialInstr = Utils.convertStringToHex(specialInstr);

        ModelManager.getInstance().getShoppingManager().getShoppingCart(ShoppingCartDetailActivity.this, Operations.
                makeJsonPayShoppingCart(ShoppingCartDetailActivity.this, merchantId, shoppingCompBean.getShoppingCartId(),
                        total_Amount_Is, tip_is, shipping_id, deliver_Id, card_token, total_Amount_Is, tax, shoppingCompBean,
                        Choice_Id, Choice_Price, arrchoice, specialInstr));


    }

/*
    public void payDialog() {
        final Dialog dialog = new Dialog(ShoppingCartDetailActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.setContentView(R.layout.pay_with_card);

        final Spinner spinner = (Spinner) dialog.findViewById(R.id.spinner);
        final Button btnpay = (Button) dialog.findViewById(R.id.pay);
        final ImageView card_add = (ImageView) dialog.findViewById(R.id.card_add);
        final ImageView card_remove = (ImageView) dialog.findViewById(R.id.card_remove);

        card_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ModelManager.getInstance().getCardManager().deleteCard(mActivity,
                        Operations.makeJsonGetDeleteCard(mActivity, card_Id));
                dialog.dismiss();
            }
        });
        card_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCard();
                dialog.dismiss();
            }
        });


        btnpay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tip_is = "";
                String total_amount = total.getText().toString();
                String total_Amount_Is = total_amount.substring(8);
                String tax = "10.00";
                if (tip.getText().toString().equals("Add Tip"))
                    tip_is = "0.00";
                else
                    tip_is = tip.getText().toString();


                ModelManager.getInstance().getShoppingManager().getShoppingCart(ShoppingCartDetailActivity.this, Operations.makeJsonPayShoppingCart
                        (ShoppingCartDetailActivity.this, merchantId, shoppingCompBean.getShoppingCartId(), total_Amount_Is, tip_is, shipping_id,
                                deliver_Id, card_token, total_Amount_Is, tax, shoppingCompBean, Choice_Id, Choice_Price, arrchoice));

                dialog.dismiss();
            }
        });


        setDialogAdapter(spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                card_token = getCardBeen.get(i).getCard_number();
                card_Id = card_Ids.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }
*/

    public void setDialogAdapter(Spinner spinner) {

        AddCardAddressAdapter addCardAddressAdapter = new AddCardAddressAdapter(mActivity, getCardBeen);

        spinner.setAdapter(addCardAddressAdapter);
    }

    public void tipdialog() {
        final Dialog dialog = new Dialog(ShoppingCartDetailActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.tip_dialog);

        final EditText tiped = (EditText) dialog.findViewById(R.id.tipprice);
        tipadd = (LinearLayout) dialog.findViewById(R.id.addtip);
        tipcancel = (LinearLayout) dialog.findViewById(R.id.cancel);

        tipadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Tip = tiped.getText().toString();
                tip_price = Double.parseDouble(Tip);
                dialog.dismiss();
                if (tip_price != 0) {

                    double totaltip = tip_price + total_int;
                    total.setText("Total: $" + String.format("%.2f", totaltip));
                    tip.setText("Tip: $" + String.format("%.2f", tip_price));
                } else {
                    tip.setText("Add Tip");

                    total.setText("Total: $" + (String.format("%.2f", Double.parseDouble(totalAmount))));
                }
            }
        });

        tipcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void removeItem() {
        final Dialog dialog = new Dialog(ShoppingCartDetailActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        dialog.setContentView(R.layout.remove_item);

        LinearLayout save = (LinearLayout) dialog.findViewById(R.id.save);
        final LinearLayout delete = (LinearLayout) dialog.findViewById(R.id.remove);
        final TextView remove_tv = (TextView) dialog.findViewById(R.id.txt_remove);
        final TextView save_tv1 = (TextView) dialog.findViewById(R.id.txt_save);
        final CheckBox deletecb = (CheckBox) dialog.findViewById(R.id.completely);
        final CheckBox savecb = (CheckBox) dialog.findViewById(R.id.savelater);
        Button submit = (Button) dialog.findViewById(R.id.submit);

        remove_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (savecb.isChecked()) {
                    savecb.setChecked(false);
                    deletecb.setChecked(true);
                } else {
                    savecb.setChecked(false);
                    deletecb.setChecked(true);
                }
            }
        });

        save_tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (deletecb.isChecked()) {
                    deletecb.setChecked(false);
                    savecb.setChecked(true);
                } else {
                    savecb.setChecked(true);
                    deletecb.setChecked(false);
                }
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (savecb.isChecked()) {
                    Log.d("hellosavw", "save");
                } else if (deletecb.isChecked()) {
                    ModelManager.getInstance().getDeleteItemManager().deleteItem(mActivity, Operations.makeJsonDeleteItemFromCart(mActivity, deleteitemposition));
                    mPocketBar.setVisibility(View.VISIBLE);
                } else {
                    Log.d("donothing", "nope");
                }
                dialog.dismiss();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (deletecb.isChecked()) {
                    deletecb.setChecked(false);
                    savecb.setChecked(true);
                } else {
                    savecb.setChecked(true);
                    deletecb.setChecked(false);
                }
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (savecb.isChecked()) {
                    savecb.setChecked(false);
                    deletecb.setChecked(true);
                } else {
                    savecb.setChecked(false);
                    deletecb.setChecked(true);
                }
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);

    }

    public void signatureDialog() {
        Dialog dialog = new Dialog(ShoppingCartDetailActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.signature_screen);
        SignaturePad mSignaturePad = (SignaturePad) dialog.findViewById(R.id.signature_pad);
        mSignaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {

            @Override
            public void onStartSigning() {
                //Event triggered when the pad is touched
            }

            @Override
            public void onSigned() {
                //Event triggered when the pad is signed
            }

            @Override
            public void onClear() {
                //Event triggered when the pad is cleared
            }
        });

        LinearLayout linearLayout = (LinearLayout) dialog.findViewById(R.id.submit);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Toast.makeText(getApplicationContext(), "Thanks for Purchase", Toast.LENGTH_SHORT).show();
                Utils.baseshowFeedbackMessage(ShoppingCartDetailActivity.this,rootLayout,"Thanks for Purchase");

                Intent i = new Intent(mActivity, HomeActivity.class);
                startActivity(i);
            }
        });


        dialog.show();
        dialog.getWindow().setAttributes(lp);

    }

/*
    public void editItemDialog() {
        final Dialog dialog = new Dialog(ShoppingCartDetailActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.customdialog_checkout);
        TextView minusd = (TextView) dialog.findViewById(R.id.tv_minus);
        TextView plusd = (TextView) dialog.findViewById(R.id.tv_plus);
        final TextView tv_quantityd = (TextView) dialog.findViewById(R.id.tv_quantity);
        final Button submit = (Button) dialog.findViewById(R.id.submitdailog);
        final Button cancel = (Button) dialog.findViewById(R.id.cancel);
        option1_spinner = (Spinner) dialog.findViewById(R.id.editoption);
        option2_spinner = (Spinner) dialog.findViewById(R.id.editoption2);
        option1_spinner.setAdapter(adp1);

        option1_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String optionId = productOptionsArrayList.get(i).getOption_id();
                String options_Id = Utils.getElevenDigitId(optionId);
                option_Id = options_Id;
                ModelManager.getInstance().getProductOptions().getOption2(mActivity, Operations.makeJsonGetOptions2(mActivity, options_Id), 0);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        minusd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quan = Integer.parseInt(tv_quantityd.getText().toString());
                if (quan > 1)
                    quan--;
                tv_quantityd.setText(String.valueOf(quan));
            }
        });
        plusd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quan = Integer.parseInt(tv_quantityd.getText().toString());
                quan++;
                tv_quantityd.setText(String.valueOf(quan));
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPocketBar.setVisibility(View.VISIBLE);
                DetailsBean detailsBean = new DetailsBean();
                Log.d("UserDrais", detailsBean.getMerchantID() + "");
                ModelManager.getInstance().getShoppingCartManager().addItemTOCart(mActivity, Operations.makeJsonAddToCartItems(mActivity, tv_quantityd.getText().toString(), Utils.getElevenDigitId(productIdiS), merchantId, option_Id, option_Id));
                dialog.dismiss();
            }
        });

        dialog.show();
    }
*/

    @Override
    public void onDrawerItemSelected(View view, int position) {
        startActivity(new Intent(this, HomeActivity.class).putExtra("Drawer", position));
    }


    public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.ViewHolder> {

        private final Activity activity;
        private List<ShoppingCartDetailBean.RESULT.DetailData> response;
        private String companyName;
        double price;
        private float totalPrice;
        int selected_position = -1;
        ArrayList<CheckBox> mCheckBoxes = new ArrayList<CheckBox>();

        public CartItemAdapter(Activity activity, String companyName, List<ShoppingCartDetailBean.RESULT.DetailData> response) {
            this.response = response;
            this.activity = activity;
            this.companyName = companyName;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.checkout_row_new, parent, false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {


            holder.txt_name.setText(Utils.hexToASCII(response.get(position).getName()));

            //  holder.txt_desc.setText(Utils.hexToASCII(response.get(position).getDescription()));
            // holder.txt_dealername.setText("By " + companyName);

            if (response.get(position).getcH().size() != 0) {
                holder.op1layout.setVisibility(View.VISIBLE);
                Choice_Id = response.get(position).getcH().get(0).get_122112();
                Choice_Price = response.get(position).getcH().get(0).get_12084();
                try {
                    objchoice.put("121.104", Utils.getElevenDigitId(Choice_Id));
                    objchoice.put("114.98", Choice_Price);
                    objchoice.put("121.30", Utils.getElevenDigitId(response.get(0).get_12130()));
                    objchoice.put("114.144", Utils.getElevenDigitId(response.get(position).get_114144()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                holder.tvOption1.setText(response.get(position).getcH().get(0).get_12716());
                holder.option1Price.setText("$" + response.get(position).getcH().get(0).get_12084());
                options_Price = Double.parseDouble(response.get(position).getcH().get(0).get_12084());
                if (response.get(position).getcH().size() > 1) {
                    holder.op2layout.setVisibility(View.VISIBLE);
                    try {
                        Choice_Id = response.get(position).getcH().get(1).get_122112();
                        Choice_Price = response.get(position).getcH().get(1).get_12084();
                        objchoice2.put("121.104", Utils.getElevenDigitId(Choice_Id));
                        objchoice2.put("114.98", Choice_Price);
                        objchoice2.put("121.30", Utils.getElevenDigitId(response.get(0).get_12130()));
                        objchoice2.put("114.144", Utils.getElevenDigitId(response.get(position).get_114144()));
                        Log.d("JsonCreate", objchoice.toString() + "," + objchoice2.toString());
                    } catch (JSONException | IndexOutOfBoundsException e) {
                        e.printStackTrace();
                    }
                    holder.tvOption2.setText(response.get(position).getcH().get(1).get_12716());
                    holder.option2price.setText("$" + response.get(position).getcH().get(1).get_12084());
                    options_Price =options_Price+ Double.parseDouble(response.get(position).getcH().get(1).get_12084());
                }
            }
            holder.txt_quantity.setText(/*"Quantity: " + */response.get(position).getQuantity());
            Log.d("getPrices", response.get(position).getPrice() + " get114112" + response.get(position).get_114112());
            if (!response.get(position).getPrice().equals("0.00")) {
                price = Double.parseDouble(response.get(position).getPrice());
                holder.txt_price.setText("$ " + String.format("%.2f", Double.parseDouble(response.get(position).getPrice())));
            } else {
                price = Double.parseDouble(response.get(position).get_114112());
                holder.txt_price.setText("$ " + String.format("%.2f", Double.parseDouble(response.get(position).get_114112())));
            }
            SpinnerAdapter spinnerAdapter = new SpinnerAdapter(activity, R.layout.row_spinner, response.get(position).getdE());
            //holder.spinner.setAdapter(spinnerAdapter);
            Picasso.get().load(ATPreferences.readString(activity, Constants.KEY_IMAGE_URL) + response.get(position).getImage()).into(holder.img);

            int quantity = Integer.parseInt(response.get(position).getQuantity());
            sub_Total = sub_Total + price * quantity;
            sub_Total = sub_Total + options_Price;

            sub_total.setText("$" + String.format("%.2f", sub_Total));
            holder.more_info.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    moreInfoDialog();
                }
            });

            mCheckBoxes.add(holder.checkbox);
            holder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        itemPosition = position;
                        deleteitemposition = response.get(position).get_12130();
                        productIdiS = response.get(position).get_114144();
                        ModelManager.getInstance().getProductOptions().getOption1(activity, Operations.makeJsonGetOptions(activity, Utils.getElevenDigitId(productIdiS)));


                        for (int i = 0; i < mCheckBoxes.size(); i++) {
                            if (mCheckBoxes.get(i) == buttonView)
                                selected_position = i;
                            else
                                mCheckBoxes.get(i).setChecked(false);
                        }

                    } else {
                        //itemPosition = 404;
                        selected_position = -1;
                    }

                }
            });

            totalPrice = totalPrice + Float.parseFloat(response.get(position).getPrice()) * Integer.parseInt(response.get(position).getQuantity());

        }


        @Override
        public int getItemCount() {
            return response.size();
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            private final ImageView img, more_info;
            private final Spinner spinner;
            private final RelativeLayout op1layout, op2layout;
            private final LinearLayout ll_delete;
            private TextView tvOption1, tvOption2, option1Price, option2price;
            private final CheckBox checkbox;
            private final TextView txt_name, txt_dealername, txt_quantity, txt_price, txt_addtocart, txt_remove, txt_desc, txt_deliverydays, txt_shippingfee;

            public ViewHolder(View itemView) {
                super(itemView);
                tvOption1 = (TextView) itemView.findViewById(R.id.tv_option1);
                tvOption2 = (TextView) itemView.findViewById(R.id.tv_option2);
                option1Price = (TextView) itemView.findViewById(R.id.option1_price);
                option2price = (TextView) itemView.findViewById(R.id.option2_price);
                txt_name = (TextView) itemView.findViewById(R.id.txt_name);
                txt_dealername = (TextView) itemView.findViewById(R.id.txt_dealername);
                txt_price = (TextView) itemView.findViewById(R.id.txt_price);
                txt_addtocart = (TextView) itemView.findViewById(R.id.txt_addtocart);
                txt_remove = (TextView) itemView.findViewById(R.id.txt_remove);
                txt_quantity = (TextView) itemView.findViewById(R.id.txt_quantity);
                txt_desc = (TextView) itemView.findViewById(R.id.txt_desc);
                txt_deliverydays = (TextView) itemView.findViewById(R.id.delivery_days);
                txt_shippingfee = (TextView) itemView.findViewById(R.id.shipping_fee);
                img = (ImageView) itemView.findViewById(R.id.img);
                ll_delete = (LinearLayout) itemView.findViewById(R.id.delete);
                op1layout = (RelativeLayout) itemView.findViewById(R.id.opy1layout);
                op2layout = (RelativeLayout) itemView.findViewById(R.id.opy2layout);
                spinner = (Spinner) itemView.findViewById(R.id.spinner);
                checkbox = (CheckBox) itemView.findViewById(R.id.checkimg);
                more_info = (ImageView) itemView.findViewById(R.id.more_info);
            }
        }
    }

    private static void moreInfoDialog() {
        final Dialog dialog = new Dialog(mActivity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.setContentView(R.layout.more_info_checkout);
        Button done = (Button) dialog.findViewById(R.id.close);
        TextView specialInstructions = (TextView) dialog.findViewById(R.id.specs);
        TextView specialInstructions_edt = (TextView) dialog.findViewById(R.id.specs_ed);

        specialInstructions.setVisibility(View.GONE);
        specialInstructions_edt.setVisibility(View.VISIBLE);
        if (!specialInstructions_edt.getText().toString().isEmpty())
            specialInstr = specialInstructions.getText().toString();

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    public class AddCardAddressAdapter extends BaseAdapter {

        private List<GetCardBean> response;
        private LayoutInflater inflater = null;

        private Activity activity;

        public AddCardAddressAdapter(Activity a, List<GetCardBean> response) {
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
            vi = inflater.inflate(R.layout.pay_with_card_row, null);
            TextView txt_type = (TextView) vi.findViewById(R.id.txt_type);
            TextView carddigits = (TextView) vi.findViewById(R.id.card_number);

            String Card_Number = response.get(position).getCard_number();
            card_Ids.add(response.get(position).getCard_token());
            String first_four = Card_Number.substring(0, 4);
            String last_four = Card_Number.substring(12, 16);
            Log.d("first_four", first_four + " d");
            Log.d("last_four", last_four + " d");

            carddigits.setText("XXXX-" + last_four);

            String response_type = response.get(position).getCard_type();

            if (response_type.equals("61"))
                txt_type.setText("VISA");
            else if (response_type.equals("62"))
                txt_type.setText("MASTERCARD");
            else
                txt_type.setText("AMERICAN EXPRESS");
            // txt_type.setText(response.get(position).getAddress_type()+" "+response.get(position).getLine1()+" "+response.get(position).getLine2());

            return vi;
        }

    }

    public class SetDeliveryMethod extends BaseAdapter {

        private List<GetCardBean> response;
        private LayoutInflater inflater = null;
        private ArrayList<String> addCards = new ArrayList<String>();
        private Activity activity;

        public SetDeliveryMethod(Activity a, List<GetCardBean> response) {
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
            vi = inflater.inflate(R.layout.address_row2, null);
            TextView txt_type = (TextView) vi.findViewById(R.id.txt_type);


            String response_type = response.get(position).getCard_type();
            String Card_Number = response.get(position).getCard_number();
            card_Ids.add(response.get(position).getCard_token());

            String first_four = Card_Number.substring(0, 4);
            String last_four = Card_Number.substring(12, 16);
            card_String.add(response_type);
            if (response_type.equals("61"))
                txt_type.setText("VISA   " +/* first_four + "-XXXX-XXXX*/"XXXX-" + last_four);
            else if (response_type.equals("62"))
                txt_type.setText("MASTERCARD   " + /*first_four + "-XXXX-XXXX-*/"XXXX-" + last_four);
            else if (response_type.equals("63"))
                txt_type.setText("AMERICAN EXPRESS   " + /*first_four + "-XXXX-XXXX-*/"XXXX-" + last_four);


            // txt_type.setText(response.get(position).getAddress_type()+" "+response.get(position).getLine1()+" "+response.get(position).getLine2());

            return vi;
        }

    }

    public class GetAddressAdapter extends BaseAdapter {
        private List<AddressData> response;
        private LayoutInflater inflater = null;
        private Activity activity;

        public GetAddressAdapter(Activity a, List<AddressData> response) {
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
            vi = inflater.inflate(R.layout.address_row2, null);
            TextView txt_type = (TextView) vi.findViewById(R.id.txt_type);
            address_Ids.add(response.get(position).getAddressId());
            Log.d("rescponseId", response.get(position).getAddressId());
            txt_type.setText(response.get(position).getAddress_type() + " " + response.get(position).getLine1() + " " + response.get(position).getLine2());
            return vi;
        }
    }

    public class GetDeliveryMerchant extends BaseAdapter {
        private List<GetDeliveryMerchantBean> response;
        private LayoutInflater inflater = null;
        private Activity activity;

        public GetDeliveryMerchant(Activity a, List<GetDeliveryMerchantBean> response) {
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
            vi = inflater.inflate(R.layout.deliver_row, null);
            TextView txt_type = (TextView) vi.findViewById(R.id.txt_type);
            TextView txt_price = (TextView) vi.findViewById(R.id.txt_price);

            if (!deliver_ids.contains(response.get(position).getId())) {
                deliver_ids.add(response.get(position).getId());
                deliver_costs.add(response.get(position).getDelivery_price());
            }

            txt_type.setText(response.get(position).getDelviery_options() + " " + response.get(position).getPickup());
            txt_price.setText("$ " + response.get(position).getDelivery_price());
            return vi;
        }
    }

    public class GetOption1 extends BaseAdapter {
        private List<ProductOptionsBean> response;
        private LayoutInflater inflater = null;
        private Activity activity;

        public GetOption1(Activity a, List<ProductOptionsBean> response) {
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
            vi = inflater.inflate(R.layout.spinner_row, null);
            TextView txt_type = (TextView) vi.findViewById(R.id.txt_type);
            option_Ids.add(response.get(position).getOption_id());
            Log.d("addingIds", response.get(position).getOption_id());
            txt_type.setText(response.get(position).getName_option());
            return vi;
        }
    }

    public class GetOption2 extends BaseAdapter {
        private List<ProductOptions2Bean> response;
        private LayoutInflater inflater = null;
        private Activity activity;

        public GetOption2(Activity a, List<ProductOptions2Bean> response) {
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
            vi = inflater.inflate(R.layout.spinner_row, null);
            TextView txt_type = (TextView) vi.findViewById(R.id.txt_type);
            TextView txt_price = (TextView) vi.findViewById(R.id.txt_price);
            Log.d("responseas", response.get(position).getChoice_name() + "wdk");

            txt_type.setText(response.get(position).getChoice_name());
            txt_price.setVisibility(View.VISIBLE);
            double price = Double.parseDouble(response.get(position).getChoice_price());
            txt_price.setText("$" + (String.format("%.2f", price)));

            return vi;
        }
    }

    public void addCard() {

        Intent i = new Intent(mActivity, PaymentActivity.class);
        startActivity(i);

    }

    public void showdialog() {
        final Dialog dialog = new Dialog(mActivity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.setContentView(R.layout.customdialog_checkout);
        TextView minusd = (TextView) dialog.findViewById(R.id.tv_minus);
        TextView plusd = (TextView) dialog.findViewById(R.id.tv_plus);
        final TextView tv_quantityd = (TextView) dialog.findViewById(R.id.tv_quantity);
        final TextView tv_option = (TextView) dialog.findViewById(R.id.option);
        final TextView tv_option1 = (TextView) dialog.findViewById(R.id.option1);
        final Button submit = (Button) dialog.findViewById(R.id.submitdailog);
        final Button cancel = (Button) dialog.findViewById(R.id.cancel);
        layout = (RelativeLayout) dialog.findViewById(R.id.realativesecond);
        option1 = (Spinner) dialog.findViewById(R.id.editoption);
        option2 = (Spinner) dialog.findViewById(R.id.editoption2);


        option1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String optionId = productOptionsArrayList2Str.get(i);
                String options_Id = Utils.getElevenDigitId(optionId);
                str_option_Id = options_Id;

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        option2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (layout.getVisibility() == View.VISIBLE) {
                    String optionId = productOptionsArrayList3.get(i);
                    String options_Id = Utils.getElevenDigitId(optionId);
                    str_option_Id2 = options_Id;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        //option1.setAdapter(adp1);
        Log.e("LIst", productOptionsArrayList.size() + "");
        if (productOptionsArrayList.size() == 1) {
            tv_option.setText(productOptionsArrayList.get(0).getName_option().toString() + "");

            String optionId = productOptionsArrayList.get(0).getOption_id();
            String options_Id = Utils.getElevenDigitId(optionId);
            option_Id = options_Id;
            ModelManager.getInstance().getProductOptions().getOption2(this, Operations.makeJsonGetOptions2(this, options_Id), 0);


        } else if (productOptionsArrayList.size() == 2) {
            layout.setVisibility(View.VISIBLE);
            tv_option.setText(productOptionsArrayList.get(0).getName_option().toString() + "");
            String optionId = productOptionsArrayList.get(0).getOption_id();
            String options_Id = Utils.getElevenDigitId(optionId);
            option_Id = options_Id;
            ModelManager.getInstance().getProductOptions().getOption2(this, Operations.makeJsonGetOptions2(this, options_Id), 0);


            tv_option1.setText(productOptionsArrayList.get(1).getName_option().toString() + "");
            String optionId2 = productOptionsArrayList.get(1).getOption_id();
            String options_Id2 = Utils.getElevenDigitId(optionId2);
            option_Id2 = options_Id2;
        }

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                state = 0;
                dialog.dismiss();
            }
        });

        minusd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quan = Integer.parseInt(tv_quantityd.getText().toString());
                if (quan > 1)
                    quan--;
                tv_quantityd.setText(String.valueOf(quan));
            }
        });

        plusd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quan = Integer.parseInt(tv_quantityd.getText().toString());
                quan++;
                tv_quantityd.setText(String.valueOf(quan));
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPocketBar.setVisibility(View.VISIBLE);
                DetailsBean detailsBean = new DetailsBean();
                Log.d("UserDrais", detailsBean.getMerchantID() + "");
                ModelManager.getInstance().getDeleteItemManager().deleteItem(mActivity, Operations.makeJsonDeleteItemFromCart(mActivity, deleteitemposition));
/*
                ModelManager.getInstance().getShoppingCartManager().addItemTOCart(mActivity, Operations.
                        makeJsonAddToCartItems(mActivity, tv_quantityd.getText().toString(),
                                Utils.getElevenDigitId(productIdiS), merchantId, str_option_Id, str_option_Id2));
*/
                //ModelManager.getInstance().getShoppingCartManager().addItemTOCart(mActivity, Operations.makeJsonAddToCartItems(mActivity, tv_quantityd.getText().toString(), Utils.getElevenDigitId(productIdiS), merchantId, str_option_Id,str_option_Id2));
                state = 0;
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    public void displayView(Fragment fragment, String tag, Bundle bundle) {
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        backPressed();
    }

    public void showGuestDialog() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.setContentView(R.layout.guest_login);

        TextView title = (TextView) dialog.findViewById(R.id.text);
        Button login = (Button) dialog.findViewById(R.id.continueshoping);
        Button cancel = (Button) dialog.findViewById(R.id.cancel);

        title.setText("Your Cart is gonna be erased unless you create an account");
        login.setText("Register");
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mActivity, LoginActivity.class));
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        dialog.show();
        dialog.getWindow().setAttributes(lp);
        //getDialogView(dialog);
        //viewsVisibility(dialog);

    }

    public void backPressed() {
        if (isGuest) {
            showGuestDialog();
        } else if (viewMain.getVisibility() == View.VISIBLE) {
            finish();
        } else {
            viewMain.setVisibility(View.VISIBLE);
            frameLayout.setVisibility(View.GONE);
        }
    }
}
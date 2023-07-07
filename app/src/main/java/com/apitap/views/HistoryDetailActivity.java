package com.apitap.views;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;

import com.apitap.views.fragments.favourite.FragmentFavourite;
import com.apitap.views.fragments.specials.FragmentSpecial;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.apitap.R;
import com.apitap.controller.HistoryManager;
import com.apitap.controller.ModelManager;
import com.apitap.model.AddTabBar;
import com.apitap.model.Constants;
import com.apitap.model.Operations;
import com.apitap.model.Utils;
import com.apitap.model.bean.HistoryInvoiceBean;
import com.apitap.model.bean.InvoiceDetailBean;
import com.apitap.model.bean.InvoiceItemsBean;
import com.apitap.model.bean.MerchantDetailBean;
import com.apitap.model.customclasses.Event;
import com.apitap.model.preferences.ATPreferences;
import com.apitap.views.NavigationMenu.FragmentDrawer;
import com.apitap.views.adapters.History2Adapter;
import com.apitap.views.adapters.InvoiceItemAdapter;
import com.apitap.views.customviews.DividerItemDecoration;
import com.apitap.views.fragments.items.FragmentItems;
import com.apitap.views.fragments.messages.FragmentMessages;
import com.apitap.views.fragments.FragmentScanner;
import com.apitap.views.fragments.ads.FragmentAds;
import com.apitap.views.fragments.SendMessage;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.apitap.App.isGuest;

public class HistoryDetailActivity extends BaseActivity implements View.OnClickListener, FragmentDrawer.FragmentDrawerListener, InvoiceItemAdapter.ReturnItemClick {

    private RecyclerView recycler, recyclerViewReturn;
    History2Adapter history2Adapter;
    private InvoiceItemAdapter invoiceItemAdapter;
    private HistoryInvoiceBean.RESULT.Invoicedata data;
    private String status;
    private ImageView logo_merchant;
    private Button rate_merchant, reorder_btn;
    private List<InvoiceDetailBean.RESULT.DetailData> list;
    private RelativeLayout rlHedaerPrice;
    private ImageView iv_back, imageViewReturnBack;
    private TextView close;
    int selected_position = -1;
    ArrayList<CheckBox> mCheckBoxes = new ArrayList<CheckBox>();
    private List<InvoiceItemsBean.RESULT.Invoicedata> listInvoiceItems;
    private TextView txt_account, txt_time, txt_deleiveryMethod, txt_deleiveryAddress, txt_transNo, txt_tip2, txt_auth, txt_subTotal,
            txt_approval, txt_taxes, txt_total, txt_date, txt_no, tvStatus, tvTotal, tvReturnTotal, textViewDelIns;
    private Activity mActivity;
    private TextView sendMessage;
    LinearLayout scan_tool;
    LinearLayout msg_tool;
    LinearLayout search_tool, submitReturn, storePolicies;
    RelativeLayout viewMain;
    JSONObject jsonObject = new JSONObject();
    FrameLayout frameLayout;
    private static int toolint = 0;
    double returnTotal = 0;
    double quantityAmount = 0;
    String searchkey = "";
    ImageView storeImage;
    TextView storeName;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private MerchantDetailBean.RESULT.DetailData merchant_data;
    private FragmentDrawer drawerFragment;
    private Toolbar mToolbar;
    public static TabLayout tabLayout;
    private TextView tabOne, tabTwo, tabThree, tabFour, tabFive, tabSix;
    private ImageView  homeTab2;

    LinearLayout tabConatiner;
    Button storeBrowse;
    public double sub_Total = 0.00;
    public double grand_Total = 0.00;
    public boolean isCurrentMerchantFav = false;
    public MerchantDetailBean.RESULT.DetailData data_fav;
    private DrawerLayout rootLayout;
    private ScrollView scrollViewInvoice, scrollViewReturn;
    public InvoiceItemAdapter.ReturnItemClick returnItemClick;
    private JSONArray jsonArray = new JSONArray();
    private int noOfDays;
    private int apiNoOfDays;
    private Dialog sorryReturnDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_detail2);
        status = getIntent().getExtras().getString("status");
        mActivity = this;
        returnItemClick = this;
        data = (HistoryInvoiceBean.RESULT.Invoicedata) getIntent().getSerializableExtra("data");
        initViews();
        setListener();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("Tags", "Resttart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        ModelManager.getInstance().getAddMerchantRating().getMerchantRating(HistoryDetailActivity.this,
                Operations.GetMerchantRating(mActivity, ATPreferences.readString(this, Constants.KEY_USERID), data.getMerchantId()));

    }

    private void initViews() {
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        tabConatiner = findViewById(R.id.tab_container);
        scrollViewInvoice = findViewById(R.id.invoiceView);
        homeTab2 = findViewById(R.id.tab_one_image);
        scrollViewReturn = findViewById(R.id.returnView);
        submitReturn = findViewById(R.id.submitReturn);
        storePolicies = findViewById(R.id.storePolicies);
        rootLayout = findViewById(R.id.drawer_layout);
        tabLayout = findViewById(R.id.tabs);
        storeImage = findViewById(R.id.adstoreImg);
        storeName = findViewById(R.id.storeName);
        tvTotal = findViewById(R.id.tv_total);
        tvReturnTotal = findViewById(R.id.returnTotal);
        storeBrowse = findViewById(R.id.details_store);
        textViewDelIns = findViewById(R.id.textViewDelIns);
        tvStatus = findViewById(R.id.tv_status);
        txt_account = findViewById(R.id.txt_account);
        sendMessage = findViewById(R.id.send_message);
        iv_back = findViewById(R.id.tv_back);
        imageViewReturnBack = findViewById(R.id.imageViewReturnBack);
        close = findViewById(R.id.close);
        txt_deleiveryMethod = findViewById(R.id.txt_deleiveryMethod);
        txt_time = findViewById(R.id.time);
        txt_date = findViewById(R.id.txt_date);

        txt_deleiveryAddress = findViewById(R.id.txt_deleiveryAddress);
        //   txt_transNo = (TextView) findViewById(R.id.transaction);
        txt_tip2 = findViewById(R.id.txt_tip2);
        txt_subTotal = findViewById(R.id.txt_subTotal);
        txt_auth = findViewById(R.id.tv_auth);
        txt_approval = findViewById(R.id.approval);
        txt_taxes = findViewById(R.id.txt_taxes);
        txt_total = findViewById(R.id.txt_total);
        txt_no = findViewById(R.id.txt_no);
        //   logo_merchant = (ImageView) findViewById(R.id.logoimg);
        rate_merchant = findViewById(R.id.add_rating);
        reorder_btn = findViewById(R.id.reorder);
        //  rlHedaerPrice = (RelativeLayout) findViewById(R.id.rl_hedaer_price);
        recycler = findViewById(R.id.recycler);
        recyclerViewReturn = findViewById(R.id.returnList);
        scan_tool = findViewById(R.id.ll_scan);
        viewMain = findViewById(R.id.linear);
        frameLayout = findViewById(R.id.container_body);
        msg_tool = findViewById(R.id.ll_message);
        search_tool = findViewById(R.id.ll_search);

        showProgress();

        returnDialogInitialize();

        recycler.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewReturn.setLayoutManager(new LinearLayoutManager(this));
        recycler.addItemDecoration(new DividerItemDecoration(this, R.drawable.divider_grey));

        setInitialData();

        ModelManager.getInstance().getHistoryManager().getInvoiceDetail(this,
                Operations.makeJsonGetInvoiceDetail(this, Utils.getElevenDigitId(data.getInvoiceId())),
                Constants.INVOICE_DETAIL_SUCCESS);
        ModelManager.getInstance().getMerchantManager().getMerchantDetail(this,
                Operations.makeJsonGetMerchantDetail(this, data.getMerchantId()), Constants.GET_MERCHANT_SUCCESS);

/*
        ModelManager.getInstance().getInvoiceManager().getListOfInvoice(this, Operations.makeJsonGetInvoiceItems(this,
                Utils.getElevenDigitId(data.getInvoiceId())));
*/
        ModelManager.getInstance().getMerchantFavouriteManager().getFavourites(this,
                Operations.makeJsonGetMerchantFavourite(this));

        AddTabBar.getmInstance().setupViewPager(tabLayout);
        AddTabBar.getmInstance().setupTabIcons(tabLayout, mActivity, tabOne, tabTwo, tabThree, tabFour,
                tabFive, tabSix,homeTab2);
        AddTabBar.getmInstance().bindWidgetsWithAnEvent(tabConatiner, tabLayout, HistoryDetailActivity.this, R.id.container_body2);
        ATPreferences.putBoolean(mActivity, "isCheckedH", false);

    }

    private void returnDialogInitialize() {
        sorryReturnDialog = Utils.showReturnDialog(this);
        TextView txtok = sorryReturnDialog.findViewById(R.id.txtok);
        TextView txtcancel = sorryReturnDialog.findViewById(R.id.txtcancel);

        txtok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sorryReturnDialog.dismiss();
            }
        });
        txtcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                frameLayout.setVisibility(View.VISIBLE);
                viewMain.setVisibility(View.GONE);
                Bundle bundle = new Bundle();
                bundle.putString("merchantId", data.getMerchantId());
                bundle.putString("storeName", storeName.getText().toString());
                displayView(new SendMessage(), "Send Message", bundle);
                sorryReturnDialog.dismiss();
            }
        });
    }

    private void setListener() {
        findViewById(R.id.return_item).setOnClickListener(this);
        findViewById(R.id.add_rating).setOnClickListener(this);
        // findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.close).setOnClickListener(this);
        findViewById(R.id.send_message).setOnClickListener(this);
        findViewById(R.id.ll_message).setOnClickListener(this);
        findViewById(R.id.ll_search).setOnClickListener(this);
        findViewById(R.id.ll_scan).setOnClickListener(this);
        findViewById(R.id.reorder).setOnClickListener(this);
        findViewById(R.id.details_store).setOnClickListener(this);
        findViewById(R.id.tv_back).setOnClickListener(this);
        findViewById(R.id.imageViewReturnBack).setOnClickListener(this);
        findViewById(R.id.storeName).setOnClickListener(this);
        findViewById(R.id.submitReturn).setOnClickListener(this);
        findViewById(R.id.storePolicies).setOnClickListener(this);
        findViewById(R.id.cancelReturn).setOnClickListener(this);
    }

    private void setInitialData() {
        txt_no.setText("Invoice No.: " + data.getInvoiceNumber());
        if (!data.getApproval_number().isEmpty())
            txt_approval.setText("Approval No.: " + data.getApproval_Code());
        else
            txt_approval.setText("Approval No.: N/A");

        if (data.getStatus().equals("102")) {
            tvStatus.setText("Status: " + "Return");
            txt_auth.setText("Auth: " + "Return");

        } else  if (data.getStatus().equals("105")) {
            tvStatus.setText("Status: " + "Refund");
            txt_auth.setText("Auth: " + "Refund");

        } else {
            tvStatus.setText("Status: " + "Delivered");
            txt_auth.setText("Auth: " + "Approved");
        }

        // txt_transNo.setText("Transaction No.: " + data.getTransactionNo());
        txt_date.setText("Date: " + Utils.changeInvoiceDateFormat(data.getInvoiceDate()));

        try {
            Date apiDate = new SimpleDateFormat("dd MMM yyyy").parse(Utils.changeInvoiceDateFormat(
                    data.getInvoiceDate()));
            Date currentDate = new SimpleDateFormat("dd MMM yyyy").parse(Utils.getDate());
            long days = Utils.daysBetween(apiDate, currentDate);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                noOfDays = Math.toIntExact(days);
            }
            Log.d("No_of_Dates", Utils.daysBetween(apiDate, currentDate) + "  ");
        } catch (ParseException e) {
            e.printStackTrace();
        }


        txt_time.setText("Time: " + Utils.getTimeFromInvoice(data.getInvoiceDate()));
        double totals = Float.parseFloat(data.getAmount()) + 10.00;
        Log.d("Total1", totals + "");
        Log.d("Total2", (new DecimalFormat("##.##").format(totals) + ""));
        txt_taxes.setText("Tax: $" + data.getInvoiceTax());
//      txt_tip1.setText("$" + data.getInvoiceTip());
        txt_tip2.setText("Tip: $" + data.getInvoiceTip());

        txt_deleiveryMethod.setText(data.getdE().get(0).getDeleiveryCompany());
        try {
            txt_deleiveryAddress.setText(data.getdE().get(0).getAddressLine());
        } catch (Exception e) {
            e.printStackTrace();
        }

        String card_typestr = data.getkT().get(0).getCardName();
        switch (card_typestr) {
            case "62":
                txt_account.setText("MasterCard");
                break;
            case "61":

                txt_account.setText("VISA");
                break;
            default:
                txt_account.setText("AMERICAN EXPRESS");
                break;
        }


        double subTotal = Double.parseDouble(data.getAmount()) - Double.parseDouble(data.getInvoiceTax()) - Double.parseDouble(data.getInvoiceTip());

        //txt_subTotal.setText("Sub Total: $" + String.format("%.2f", subTotal));
        txt_subTotal.setText("Delivery: $" + data.getdE().get(0).getDeleiveryPrice());
        //tvTotal.setText("$ " + data.getAmount());
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
    public void onEvent(final Event event) {
        switch (event.getKey()) {
            case Constants.INVOICE_DETAIL_SUCCESS:
                double price = 0.0;
                int quantity = 0;
                list = ModelManager.getInstance().getHistoryManager().invoiceDetailBean.getRESULT().get(0).getRESULT();
                if (list == null || list.size() == 0)
                    Utils.baseshowFeedbackMessage(this, rootLayout, "Something Went Wrong.");
                else {
                    for (int i = 0; i < list.size(); i++) {
                        price = Double.parseDouble(list.get(i).getRegularPrice());
                        quantity = Integer.parseInt(list.get(i).getProductQty());
                        sub_Total = sub_Total + price * quantity;
                    }
                    tvTotal.setText("$" + String.format("%.2f", sub_Total));

                    grand_Total = sub_Total +
                            Double.parseDouble(data.getdE().get(0).getDeleiveryPrice()) +
                            Double.parseDouble(data.getInvoiceTax()) + Double.parseDouble(data.getInvoiceTip());

                    txt_total.setText("Total: $ " + String.format("%.2f", grand_Total));

                    history2Adapter = new History2Adapter(this, list, data);
                    recycler.setAdapter(history2Adapter);
                    String strSpecial = data.getDeliveryInstructions();
                    if (!strSpecial.isEmpty())
                        textViewDelIns.setText(Utils.hexToASCII(data.getDeliveryInstructions()));
                    else
                        textViewDelIns.setText("No special Instructions");


                    if (list.get(0).getTimeFrameValue() != null && !list.get(0).getTimeFrameValue().isEmpty())
                        apiNoOfDays = Integer.parseInt(list.get(0).getTimeFrameValue());
                    Log.d("apiNoOfDays", apiNoOfDays + "");

                }


                hideProgress();
                break;

            case Constants.INVOICE_ITEMS:
                listInvoiceItems = ModelManager.getInstance().getInvoiceManager().invoiceItemsBean.getRESULT().get(0).getRESULT();
                break;
            case Constants.BACK_PRESSED:
                frameLayout.setVisibility(View.GONE);
                viewMain.setVisibility(View.VISIBLE);
                break;

            case Constants.RETURN_INVOICE_ITEMS:
                scrollViewReturn.setVisibility(View.GONE);
                scrollViewInvoice.setVisibility(View.VISIBLE);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        invoiceItemAdapter.notifyDataSetChanged();
                        History2Adapter.returnBeanArrayList = new ArrayList<>();

                        jsonArray = new JSONArray();
                        hideProgress();
                        //  history2Adapter.clearCheckbox();
                        //Toast.makeText(HistoryDetailActivity.this, "Items submitted for return successfully", Toast.LENGTH_SHORT).show();
                        Utils.baseshowFeedbackMessage(HistoryDetailActivity.this, rootLayout, "Products submitted for return successfully");
                        ModelManager.getInstance().getHistoryManager().getInvoiceDetail(mActivity,
                                Operations.makeJsonGetInvoiceDetail(mActivity, Utils.getElevenDigitId(data.getInvoiceId())),
                                Constants.INVOICE_DETAIL_SUCCESS);


                    }
                });
                break;
            case Constants.GET_MERCHANT_SUCCESS:
                merchant_data = ModelManager.getInstance().getMerchantManager().merchantDetailBean.getRESULT().get(0).getRESULT().get(0);
                Picasso.get().load(ATPreferences.readString(mActivity, Constants.KEY_IMAGE_URL) + merchant_data.getImage()).into(storeImage);
                storeName.setText(merchant_data.getName());
                break;

            case Constants.GET_MERCHANT_FAVORITES:
                //ArrayList<String> merchantFavList = ModelManager.getInstance().getMerchantFavouriteManager().mernchantfavlist;
                ArrayList<String> merchantFavList = ModelManager.getInstance().getMerchantFavouriteManager().mernchantfavlist;
                Log.d("sizeandfav  ", merchant_data.getName() + "  " + storeName.getText().toString() + "   " + merchantFavList.size());
                if (merchantFavList.contains(merchant_data.getName())) {
                    isCurrentMerchantFav = true;
                    setFavouriteMerchantView(storeName);
                }
                break;

            case Constants.ADD_MERCHANT_FAVORITE_SUCCESS:
                //    if (isFavorite.equals("1"))
                hideProgress();
                isCurrentMerchantFav = true;
                setFavouriteMerchantView(storeName);
                break;
            case Constants.REMOVE_MERCHANT_FAVORITES:
                hideProgress();
                isCurrentMerchantFav = false;
                setFavouriteMerchantView(storeName);
                break;
            case Constants.SHOPPING_SUCCESS:
                hideProgress();
                Utils.baseshowFeedbackMessage(mActivity, rootLayout, "Products Added to Cart.");
                break;
            case -1:
                hideProgress();
                Utils.baseshowFeedbackMessage(mActivity, rootLayout, "Something Went Wrong.");
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
            case R.id.return_item:
                int size = History2Adapter.returnBeanArrayList.size();
                boolean isReturnable = true;
                if (size > 0) {
                    for (int i = 0; i < size; i++) {
                        if (list.get(i).getReturnStatus().equalsIgnoreCase("80001")) {
                            Utils.baseshowFeedbackMessage(HistoryDetailActivity.this, rootLayout, "Your return request of product is already done.");
                            isReturnable = false;
                            break;
                        } else if (list.get(i).getReturnStatus().equalsIgnoreCase("80002")) {
                            Utils.baseshowFeedbackMessage(HistoryDetailActivity.this, rootLayout, "Your return request of product can't be processed.");
                            isReturnable = false;
                            break;
                        } else if (list.get(i).getReturnStatus().equalsIgnoreCase("80003")) {
                            Utils.baseshowFeedbackMessage(HistoryDetailActivity.this, rootLayout, "Your return request of product is pending.");
                            isReturnable = false;
                            break;
                        }
                    }

                    if (!isReturnable)
                        return;
                    if (apiNoOfDays >= noOfDays)
                        showReturnDialog();
                    else
                        sorryReturnDialog.show();

                } else
                    Utils.baseshowFeedbackMessage(HistoryDetailActivity.this, rootLayout, "Please Select an Item");

                break;
            case R.id.add_rating:

                Intent i = new Intent(mActivity, RateMerchant.class);
                i.putExtra("merchantId", data.getMerchantId());
                i.putExtra("storeName", merchant_data.getName());
                i.putExtra("storeImage", merchant_data.getImage());
                startActivity(i);

                break;
            case R.id.cancelReturn:
                scrollViewInvoice.setVisibility(View.VISIBLE);
                scrollViewReturn.setVisibility(View.GONE);
                break;
            case R.id.imageViewReturnBack:
                scrollViewInvoice.setVisibility(View.VISIBLE);
                scrollViewReturn.setVisibility(View.GONE);
                break;

            case R.id.storePolicies:
                startActivity(new Intent(mActivity, MerchantStoreDetails.class)
                        .putExtra("merchantId", data.merchantId));

                break;
            case R.id.submitReturn:
                Utils.hideKeyboardFrom(mActivity, scrollViewReturn);

                jsonArray = new JSONArray();
                for (int j = 0; j < History2Adapter.returnBeanArrayList.size(); j++) {
                    jsonObject = new JSONObject();
                    Log.d("ifff", InvoiceItemAdapter.returnBean.getReasonId());
                    try {
                        jsonObject.put("114.144", History2Adapter.returnBeanArrayList.get(j).productId);
                        jsonObject.put("114.121", History2Adapter.returnBeanArrayList.get(j).stringQuantity);
                        jsonObject.put("114.98", History2Adapter.returnBeanArrayList.get(j).amount);
                        if (History2Adapter.returnBeanArrayList.get(j).getReasonId().isEmpty())
                            jsonObject.put("123.65", InvoiceItemAdapter.reasonIdList.get(j));
                        else
                            jsonObject.put("123.65", History2Adapter.returnBeanArrayList.get(j).getReasonId());
                        if (History2Adapter.returnBeanArrayList.get(j).getComments().isEmpty())
                            jsonObject.put("120.157", Utils.convertStringToHex(InvoiceItemAdapter.commentsList.get(j)));
                        else
                            jsonObject.put("120.157", Utils.convertStringToHex(History2Adapter.returnBeanArrayList.get(j).getComments()));
                        jsonArray.put(jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Log.d("JsonArrayOfReturn", jsonObject + "");

                showProgress();
                ModelManager.getInstance().getReturnItemManager().returnItems(
                        HistoryDetailActivity.this, Operations.makeJsonreturnOrder(mActivity,
                                Utils.getElevenDigitId(data.getInvoiceId()), jsonArray));

                break;

            case R.id.storeName:
                showProgress();
                if (!isCurrentMerchantFav) {
                    ModelManager.getInstance().getAddMerchantFavorite().addMerchantToFavorite(mActivity, Operations.makeJsonMerchantAddToFavorite(mActivity, data.getMerchantId()));
                } else
                    ModelManager.getInstance().getMerchantFavouriteManager().removeFavourite(mActivity,
                            Operations.makeJsonRemoveMerchantFavourite(mActivity, data.merchantId));
                break;

            case R.id.details_store:
                ATPreferences.putBoolean(mActivity, Constants.HEADER_STORE, true);
                ATPreferences.putString(mActivity, Constants.MERCHANT_ID, data.getMerchantId());
                startActivity(new Intent(mActivity, HomeActivity.class));
                break;

            case R.id.close:
                History2Adapter.returnBeanArrayList = new ArrayList<>();
                finish();
                break;
            case R.id.tv_back:
                backPressed();
                break;
            case R.id.reorder:
                showProgress();
/*
                ModelManager.getInstance().getShoppingManager().getShoppingCart(HistoryDetailActivity.this,
                Operations.makeJsonPayShoppingCart
                        (HistoryDetailActivity.this, data.getMerchantId(), shoppingCompBean.getShoppingCartId(), total_Amount_Is, tip_is, shipping_id,
                                deliver_Id, card_token, total_Amount_Is, tax, shoppingCompBean, Choice_Id, Choice_Price));
*/
                ModelManager.getInstance().getShoppingCartManager().reOrderCart(mActivity,
                        Operations.makeJsonReorder(mActivity, data.shoppingCartId));


                break;
            case R.id.send_message:
//                frameLayout.setVisibility(View.VISIBLE);
//                viewMain.setVisibility(View.GONE);
//                displayView(new FragmentMessages(), Constants.TAG_MESSAGEPAGE, new Bundle());

                // Toast.makeText(this, "Currently there is no messages for this invoice", Toast.LENGTH_SHORT).show();
                Intent obji = new Intent(this, MessageDetailActivity.class);
                obji.putExtra("invoice", data.getInvoiceId());
                startActivity(obji);
               /* Intent intent = new Intent(mActivity, SendMessage.class);

                intent.putExtra("merchantId", data.getMerchantId());
                intent.putExtra("storeName", merchant_data.getName());
                intent.putExtra("invoiceId", data.getInvoiceId());

                startActivity(intent);*/
                break;
            case R.id.ll_message:
                frameLayout.setVisibility(View.VISIBLE);
                viewMain.setVisibility(View.GONE);
                displayView(new FragmentMessages(), Constants.TAG_MESSAGEPAGE, new Bundle());
                break;

            case R.id.ll_scan:
                frameLayout.setVisibility(View.VISIBLE);
                viewMain.setVisibility(View.GONE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
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
                }

                break;
            case R.id.ll_search:
             //   Utils.showSearchDialog(this, "HistoryDetails");
                if (isGuest)
                    Utils.showGuestDialog(this);
                else {
                    displayView(new FragmentFavourite(), Constants.TAG_FAVOURITEPAGE, null);
                }
                break;
        }
    }

    public void showSearchDialog() {
        final ArrayList<String> list = ModelManager.getInstance().getSearchManager().listAddresses;
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.setContentView(R.layout.quick_search_test);

        Button submit = dialog.findViewById(R.id.submit);
        Button cancel = dialog.findViewById(R.id.cancel);
        final EditText search = dialog.findViewById(R.id.search);
        Spinner spinner = dialog.findViewById(R.id.spinner);
        ArrayAdapter arrayAdapter = new ArrayAdapter(getApplicationContext(), R.layout.spinner_item, list);
        spinner.setAdapter(arrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchkey = search.getText().toString();
                startActivity(new Intent(mActivity, SearchItemActivity.class).putExtra("key", searchkey));
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
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        //getDialogView(dialog);
        //viewsVisibility(dialog);

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
    public void onBackPressed() {
        super.onBackPressed();
        backPressed();
    }

    public void backPressed() {
        if (scrollViewReturn.getVisibility() == View.VISIBLE) {
            scrollViewInvoice.setVisibility(View.VISIBLE);
            scrollViewReturn.setVisibility(View.GONE);
        } else if (viewMain.getVisibility() == View.VISIBLE) {
            History2Adapter.returnBeanArrayList = new ArrayList<>();
            finish();
        } else {
            viewMain.setVisibility(View.VISIBLE);
            frameLayout.setVisibility(View.GONE);
        }
    }

    private void showReturnDialog() {

        List<String> reasonsList = new ArrayList<>();
        List<String> reasonsIdList = new ArrayList<>();
        for (int i = 0; i < HistoryManager.returnReasonsBean.getRESULT().get(0).getRESULT().size(); i++) {
            reasonsList.add(HistoryManager.returnReasonsBean.getRESULT().get(0).getRESULT().get(i).get_122_5());
            reasonsIdList.add(HistoryManager.returnReasonsBean.getRESULT().get(0).getRESULT().get(i).get_114_154());
        }
        scrollViewReturn.setVisibility(View.VISIBLE);
        scrollViewInvoice.setVisibility(View.GONE);
        if (invoiceItemAdapter != null)
            invoiceItemAdapter.notifyDataSetChanged();
        else {
            invoiceItemAdapter = new InvoiceItemAdapter(this, History2Adapter.returnBeanArrayList, returnItemClick,
                    reasonsList, reasonsIdList);
            recyclerViewReturn.setAdapter(invoiceItemAdapter);
        }
        for (int i = 0; i < History2Adapter.returnBeanArrayList.size(); i++) {
            int quantity = Integer.parseInt(History2Adapter.returnBeanArrayList.get(i).stringQuantity);
            double price = Double.parseDouble(History2Adapter.returnBeanArrayList.get(i).amount);
            quantityAmount = quantityAmount + (quantity * price);
            tvReturnTotal.setText("$" + Utils.roundOffTo2DecPlaces(quantityAmount));

        }
        // custom dialog
     /*   final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.setContentView(R.layout.dialog_invoice_item);
        dialog.setTitle("Title...");

//        // set the custom dialog components - text, image and button
//        TextView text = (TextView) dialog.findViewById(R.id.text);
        final String[] arraySpinner = new String[]{"Choose a reason",
                "Bought by mistake", "Better price available", "Product damaged, but shipping box OK", "Item arrived too late",
                "Missing parts or accessories", "Product and shipping box both damaged", "Wrong item was sent",
                "Item defective or doesn't work", "Received extra item I didn't buy(no refund needed)",
                "No longer needed", "Didn't approve purchase", "Inaccurate website description"
        };
        Spinner s = dialog.findViewById(R.id.spinner);
        ListView listView = dialog.findViewById(R.id.returnList);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);

        InvoiceItemAdapter invoiceItemAdapter = new InvoiceItemAdapter(this, 0, listInvoiceItems, status, dialog);
        listView.setAdapter(invoiceItemAdapter);
//        text.setText("Android custom dialog example!");
        LinearLayout cancel = dialog.findViewById(R.id.cancel);
        LinearLayout submit = dialog.findViewById(R.id.submit);
//        image.setImageResource(R.drawable.ic_launcher);
//
//        Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
//        // if button is clicked, close the custom dialog
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                mPocketBar.setVisibility(View.VISIBLE);
                ModelManager.getInstance().getReturnItemManager().returnItems(
                        HistoryDetailActivity.this, Operations.makeJsonreturnOrder(mActivity,
                                status, Utils.getElevenDigitId(data.getInvoiceId()), data.getStatus(),History2Adapter.jsonArray));


            }
        });


        dialog.show();
        dialog.getWindow().setAttributes(lp);*/
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        startActivity(new Intent(this, HomeActivity.class).putExtra("Drawer", position));
    }

    @Override
    public void onDontReturnClick(int position) {
        History2Adapter.returnBeanArrayList.remove(position);
        invoiceItemAdapter.notifyDataSetChanged();
    }

    @Override
    public void onQuantityDecrease(String amount) {
        quantityAmount = quantityAmount - Double.parseDouble(amount);
        tvReturnTotal.setText("$" + Utils.roundOffTo2DecPlaces(quantityAmount));
    }

    @Override
    public void onQuantityIncrease(String amount) {
        quantityAmount = quantityAmount + Double.parseDouble(amount);
        tvReturnTotal.setText("$" + Utils.roundOffTo2DecPlaces(quantityAmount));
    }
}

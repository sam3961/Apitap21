package com.apitap.views.fragments;

import static com.apitap.views.fragments.home.FragmentHome.setFavouriteMerchantView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apitap.R;
import com.apitap.controller.HistoryManager;
import com.apitap.controller.ModelManager;
import com.apitap.model.Constants;
import com.apitap.model.Operations;
import com.apitap.model.Utils;
import com.apitap.model.bean.HistoryInvoiceBean;
import com.apitap.model.bean.InvoiceDetailBean;
import com.apitap.model.bean.InvoiceItemsBean;
import com.apitap.model.bean.MerchantDetailBean;
import com.apitap.model.customclasses.Event;
import com.apitap.model.preferences.ATPreferences;
import com.apitap.views.HomeActivity;
import com.apitap.views.NavigationMenu.FragmentDrawer;
import com.apitap.views.adapters.History2Adapter;
import com.apitap.views.adapters.InvoiceItemAdapter;
import com.apitap.views.customviews.DividerItemDecoration;
import com.apitap.views.fragments.home.FragmentHome;
import com.apitap.views.fragments.messageDetails.FragmentMessageDetail;
import com.google.android.material.tabs.TabLayout;
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

public class FragmentHistoryDetail extends BaseFragment implements
        View.OnClickListener, InvoiceItemAdapter.ReturnItemClick {

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
    private LinearLayout rootLayout;
    private ScrollView scrollViewInvoice, scrollViewReturn;
    public InvoiceItemAdapter.ReturnItemClick returnItemClick;
    private JSONArray jsonArray = new JSONArray();
    private int noOfDays;
    private int apiNoOfDays;
    private Dialog sorryReturnDialog;
    private View rootView;
    private LinearLayout linearLayoutHeaderStoreFront;
    private RelativeLayout relativeLayoutSearchBarStoreFront;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_history_detail2, container, false);
        status = getArguments().getString("status");
        mActivity = getActivity();
        returnItemClick = this;
        data = (HistoryInvoiceBean.RESULT.Invoicedata) getArguments().getSerializable("data");
        initViews();
        setListener();

        return rootView;
    }
    
    @Override
    public void onResume() {
        super.onResume();
        ModelManager.getInstance().getAddMerchantRating().getMerchantRating(getActivity(),
                Operations.GetMerchantRating(mActivity, ATPreferences.readString(getActivity(),
                        Constants.KEY_USERID), data.getMerchantId()));

    }

    private void initViews() {
        relativeLayoutSearchBarStoreFront = getActivity().findViewById(R.id.search_storefront);
        linearLayoutHeaderStoreFront = getActivity().findViewById(R.id.header);
        tabConatiner = rootView.findViewById(R.id.tab_container);
        scrollViewInvoice = rootView.findViewById(R.id.invoiceView);
        homeTab2 = rootView.findViewById(R.id.tab_one_image);
        scrollViewReturn = rootView.findViewById(R.id.returnView);
        submitReturn = rootView.findViewById(R.id.submitReturn);
        storePolicies = rootView.findViewById(R.id.storePolicies);
        rootLayout = rootView.findViewById(R.id.rootLayout);
        tabLayout = rootView.findViewById(R.id.tabs);
        storeImage = rootView.findViewById(R.id.adstoreImg);
        storeName = rootView.findViewById(R.id.storeName);
        tvTotal = rootView.findViewById(R.id.tv_total);
        tvReturnTotal = rootView.findViewById(R.id.returnTotal);
        storeBrowse = rootView.findViewById(R.id.details_store);
        textViewDelIns = rootView.findViewById(R.id.textViewDelIns);
        tvStatus = rootView.findViewById(R.id.tv_status);
        txt_account = rootView.findViewById(R.id.txt_account);
        sendMessage = rootView.findViewById(R.id.send_message);
        iv_back = rootView.findViewById(R.id.tv_back);
        imageViewReturnBack = rootView.findViewById(R.id.imageViewReturnBack);
        close = rootView.findViewById(R.id.close);
        txt_deleiveryMethod = rootView.findViewById(R.id.txt_deleiveryMethod);
        txt_time = rootView.findViewById(R.id.time);
        txt_date = rootView.findViewById(R.id.txt_date);

        txt_deleiveryAddress = rootView.findViewById(R.id.txt_deleiveryAddress);
        //   txt_transNo = (TextView) rootView.findViewById(R.id.transaction);
        txt_tip2 = rootView.findViewById(R.id.txt_tip2);
        txt_subTotal = rootView.findViewById(R.id.txt_subTotal);
        txt_auth = rootView.findViewById(R.id.tv_auth);
        txt_approval = rootView.findViewById(R.id.approval);
        txt_taxes = rootView.findViewById(R.id.txt_taxes);
        txt_total = rootView.findViewById(R.id.txt_total);
        txt_no = rootView.findViewById(R.id.txt_no);
        //   logo_merchant = (ImageView) rootView.findViewById(R.id.logoimg);
        rate_merchant = rootView.findViewById(R.id.add_rating);
        reorder_btn = rootView.findViewById(R.id.reorder);
        //  rlHedaerPrice = (RelativeLayout) rootView.findViewById(R.id.rl_hedaer_price);
        recycler = rootView.findViewById(R.id.recycler);
        recyclerViewReturn = rootView.findViewById(R.id.returnList);
        scan_tool = rootView.findViewById(R.id.ll_scan);
        viewMain = rootView.findViewById(R.id.linear);
        frameLayout = rootView.findViewById(R.id.container_body);
        msg_tool = rootView.findViewById(R.id.ll_message);
        search_tool = rootView.findViewById(R.id.ll_search);

        showProgress();

        returnDialogInitialize();

        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewReturn.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler.addItemDecoration(new DividerItemDecoration(mActivity, R.drawable.divider_grey));

        setInitialData();


        ModelManager.getInstance().getHistoryManager().getInvoiceDetail(mActivity,
                Operations.makeJsonGetInvoiceDetail(mActivity, Utils.getElevenDigitId(data.getInvoiceId())),
                Constants.INVOICE_DETAIL_SUCCESS);
        ModelManager.getInstance().getMerchantManager().getMerchantDetail(mActivity,
                Operations.makeJsonGetMerchantDetail(mActivity, data.getMerchantId()), Constants.GET_MERCHANT_SUCCESS);

/*
        ModelManager.getInstance().getInvoiceManager().getListOfInvoice(this, Operations.makeJsonGetInvoiceItems(this,
                Utils.getElevenDigitId(data.getInvoiceId())));
*/
        ModelManager.getInstance().getMerchantFavouriteManager().getFavourites(getActivity(),
                Operations.makeJsonGetMerchantFavourite(mActivity));

           ATPreferences.putBoolean(mActivity, "isCheckedH", false);

    }

    private void returnDialogInitialize() {
        sorryReturnDialog = Utils.showReturnDialog(getActivity());
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
                ((HomeActivity) getActivity()).displayView(new SendMessage(), "Send Message", bundle);
                sorryReturnDialog.dismiss();
            }
        });
    }

    private void setListener() {
        rootView.findViewById(R.id.return_item).setOnClickListener(this);
        rootView.findViewById(R.id.add_rating).setOnClickListener(this);
        // rootView.findViewById(R.id.iv_back).setOnClickListener(this);
        rootView.findViewById(R.id.close).setOnClickListener(this);
        rootView.findViewById(R.id.send_message).setOnClickListener(this);
        rootView.findViewById(R.id.ll_message).setOnClickListener(this);
        rootView.findViewById(R.id.ll_search).setOnClickListener(this);
        rootView.findViewById(R.id.ll_scan).setOnClickListener(this);
        rootView.findViewById(R.id.reorder).setOnClickListener(this);
        rootView.findViewById(R.id.details_store).setOnClickListener(this);
        rootView.findViewById(R.id.tv_back).setOnClickListener(this);
        rootView.findViewById(R.id.imageViewReturnBack).setOnClickListener(this);
        rootView.findViewById(R.id.storeName).setOnClickListener(this);
        rootView.findViewById(R.id.submitReturn).setOnClickListener(this);
        rootView.findViewById(R.id.storePolicies).setOnClickListener(this);
        rootView.findViewById(R.id.cancelReturn).setOnClickListener(this);
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
            txt_deleiveryAddress.setText(Utils.hexToASCII(data.getdE().get(0).getAddressLine()));
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
        } catch (Exception e) {
            e.printStackTrace();
        }




        double subTotal = Double.parseDouble(data.getAmount()) - Double.parseDouble(data.getInvoiceTax()) - Double.parseDouble(data.getInvoiceTip());

        //txt_subTotal.setText("Sub Total: $" + String.format("%.2f", subTotal));
        txt_subTotal.setText("Delivery: $" + data.getdE().get(0).getDeleiveryPrice());
        //tvTotal.setText("$ " + data.getAmount());
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
            case Constants.HISTORY_INVOICE_SUCCESS:

                break;

            case Constants.INVOICE_DETAIL_SUCCESS:
                double price = 0.0;
                int quantity = 0;
                list = ModelManager.getInstance().getHistoryManager().invoiceDetailBean.getRESULT().get(0).getRESULT();
                if (list == null || list.size() == 0)
                    Utils.baseshowFeedbackMessage(getActivity(), rootLayout, "Something Went Wrong.");
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

                    history2Adapter = new History2Adapter(getActivity(), list, data);
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
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        invoiceItemAdapter.notifyDataSetChanged();
                        History2Adapter.returnBeanArrayList = new ArrayList<>();

                        jsonArray = new JSONArray();
                        hideProgress();
                        //  history2Adapter.clearCheckbox();
                        //Toast.makeText(HistoryDetailActivity.this, "Items submitted for return successfully", Toast.LENGTH_SHORT).show();
                        Utils.baseshowFeedbackMessage(mActivity, rootLayout, "Products submitted for return successfully");
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

//    public void setFavouriteMerchantView(TextView tvStoreName) {
//        if (isCurrentMerchantFav) {
//            tvStoreName.setBackground(mActivity.getResources().getDrawable(R.drawable.back_round_green_border));
//            tvStoreName.setTextColor(mActivity.getResources().getColor(R.color.colorGreen));
//            tvStoreName.setCompoundDrawablesWithIntrinsicBounds(R.drawable.rate_star_green, 0, 0, 0);
//        } else {
//            tvStoreName.setBackground(mActivity.getResources().getDrawable(R.drawable.back_round_blue_border));
//            tvStoreName.setTextColor(mActivity.getResources().getColor(R.color.colorBlue));
//            tvStoreName.setCompoundDrawablesWithIntrinsicBounds(R.drawable.rate_star_blue, 0, 0, 0);
//        }
//    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.return_item:
                int size = History2Adapter.returnBeanArrayList.size();
                boolean isReturnable = true;
                if (size > 0) {
                    for (int i = 0; i < size; i++) {
                        if (list.get(i).getReturnStatus().equalsIgnoreCase("80001")) {
                            Utils.baseshowFeedbackMessage(mActivity, rootLayout, "Your return request of product is already done.");
                            isReturnable = false;
                            break;
                        } else if (list.get(i).getReturnStatus().equalsIgnoreCase("80002")) {
                            Utils.baseshowFeedbackMessage(mActivity, rootLayout, "Your return request of product can't be processed.");
                            isReturnable = false;
                            break;
                        } else if (list.get(i).getReturnStatus().equalsIgnoreCase("80003")) {
                            Utils.baseshowFeedbackMessage(mActivity, rootLayout, "Your return request of product is pending.");
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
                    Utils.baseshowFeedbackMessage(mActivity, rootLayout, "Please Select an Item");

                break;
            case R.id.add_rating:

                Bundle bundle = new Bundle();
                bundle.putString("merchantId", data.getMerchantId());
                bundle.putString("storeName",merchant_data.getName());
                bundle.putString("storeImage",merchant_data.getImage());
                ((HomeActivity) getActivity()).displayView(new FragmentStoreRate(), Constants.TAG_STORE_RATE, bundle);


                break;
            case R.id.cancelReturn:
            case R.id.imageViewReturnBack:
                scrollViewInvoice.setVisibility(View.VISIBLE);
                scrollViewReturn.setVisibility(View.GONE);
                break;

            case R.id.storePolicies:

                linearLayoutHeaderStoreFront.setVisibility(View.GONE);
                relativeLayoutSearchBarStoreFront.setVisibility(View.GONE);
                Bundle bundle1 = new Bundle();
                bundle1.putString("merchantId", data.merchantId);
                ((HomeActivity) getActivity()).displayView(new FragmentStoreDetails(), Constants.TAG_STORE_DETAILS, bundle1);


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
                        mActivity, Operations.makeJsonreturnOrder(mActivity,
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
                onBackPress();
                break;
            case R.id.tv_back:
                onBackPress();
                break;
            case R.id.reorder:
                showProgress();
                ModelManager.getInstance().getShoppingCartManager().reOrderCart(mActivity,
                        Operations.makeJsonReorder(mActivity, data.shoppingCartId));


                break;
            case R.id.send_message:
                Bundle bundle2 = new Bundle();
                bundle2.putString("invoice", data.getInvoiceId());
                bundle2.putString("locationId", data.getLocationId());
                ((HomeActivity) getActivity()).displayView(new FragmentMessageDetail(),
                        Constants.MessageDetailPage, bundle2);

                break;
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
            invoiceItemAdapter = new InvoiceItemAdapter(mActivity, History2Adapter.returnBeanArrayList, returnItemClick,
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

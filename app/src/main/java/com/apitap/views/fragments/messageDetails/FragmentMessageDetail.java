package com.apitap.views.fragments.messageDetails;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apitap.R;
import com.apitap.controller.ModelManager;
import com.apitap.model.Constants;
import com.apitap.model.MyFirebaseMessagingService;
import com.apitap.model.Operations;
import com.apitap.model.PermissionFile;
import com.apitap.model.Utils;
import com.apitap.model.bean.MessageListBean;
import com.apitap.model.customclasses.Event;
import com.apitap.model.preferences.ATPreferences;
import com.apitap.views.HomeActivity;
import com.apitap.views.NavigationMenu.FragmentDrawer;
import com.apitap.views.ZoomMessageImage;
import com.apitap.views.fragments.BaseFragment;
import com.apitap.views.fragments.adDetails.FragmentAdDetail;
import com.apitap.views.fragments.itemDetails.FragmentItemDetails;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;

import net.alhazmy13.mediapicker.Image.ImagePicker;

import org.apache.commons.lang.StringEscapeUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class FragmentMessageDetail extends BaseFragment implements View.OnClickListener {

    public RecyclerView recycler;
    public static EditText edit_msg;
    public static String mesgStr;
    private TextView invoice, date, time, status, tv_Status;
    private RelativeLayout top_header;
    private static Button btn_send;
    private Button btn_report;
    public static LinearLayout iv_back;
    public static MessageListBean.RESULT.MessageData data;
    //private static CircularProgressView mPocketBar;
    List<MessageListBean.RESULT.MessageData> list = new ArrayList<>();
    public static Activity mActivity;
    int scrollDist = 0;
    LinearLayout reply_invoice;
    boolean isVisible = true;
    public static boolean isItemDetails = false;
    private FragmentDrawer drawerFragment;
    private Toolbar mToolbar;
    public static TabLayout tabLayout;
    private TextView tabOne, tabTwo, tabThree, tabFour, tabFive, tabSix, title;
    private ImageView homeTab2;
    LinearLayout tabConatiner;
    LinearLayout search_tool;
    LinearLayout scan_tool;
    LinearLayout msg_tool;
    LinearLayout ll_back;
    LinearLayout linearLayoutSelected;
    RelativeLayout viewMain;
    FrameLayout frameLayout;
    private static int toolint = 0;
    private String invoiceStr = "";
    private String merchantId = "";
    private String merchantName = "";
    private String locationId = "";
    private String productId = "", adID = "", imageUrl = "", videoUrl = "", generalID = "", productName = "";
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    TextView tv_noMsg;
    private Dialog reloadDialog;
    public boolean isAdapterSet = false;
    private MessageDetailAdapter messageDetailAdapter;
    private boolean loadMessages = false;
    private RelativeLayout rootLayout;
    private PermissionFile permissionFile;
    private File destination;
    private Uri outputFileUri;
    private Dialog dialog;
    private ArrayList<String> stringImage64 = new ArrayList<>();
    private JSONArray jsonArrayImages = new JSONArray();
    private JSONObject jsonObject = new JSONObject();
    private Uri selectedImage;
    private ImageView imageViewSelectedImage1, imageViewSelectedImage2, imageViewSelectedImage3;
    private ImageView imageViewAttachment;
    private RelativeLayout relativeLayoutClose;
    private int selectedImageState = 0;
    private View rootView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_message_detail, container, false);
        mActivity = getActivity();
        permissionFile = new PermissionFile(getActivity());
//merchantName
        if (getArguments() != null && getArguments().containsKey("locationId")) {
            locationId = getArguments().getString("locationId");
        }

        if (getArguments().containsKey("productId")) {
            productId = getArguments().getString("productId");
            productName = getArguments().getString("productName");
            merchantId = getArguments().getString("merchantId");
            merchantName = getArguments().getString("merchantName");
        } else if (getArguments().containsKey("invoice")) {
            invoiceStr = getArguments().getString("invoice");
        } else if (getArguments().containsKey("adID")) {
            adID = getArguments().getString("adID");
            imageUrl = getArguments().getString("imageUrl");
            productName = getArguments().getString("adName");
            merchantName = getArguments().getString("merchantName");
            merchantId = getArguments().getString("merchantId");
        } else if (getArguments().containsKey("generalId")) {
            generalID = getArguments().getString("generalId");
            merchantName = getArguments().getString("merchantName");
            merchantId = getArguments().getString("merchantId");
        } else
            data = (MessageListBean.RESULT.MessageData) getArguments().getSerializable("data");

        initViews();

        return rootView;
    }

    private void initViews() {
        imageViewAttachment = rootView.findViewById(R.id.imageViewAttachment);
        rootLayout = rootView.findViewById(R.id.drawer_layout);
        imageViewSelectedImage1 = rootView.findViewById(R.id.imageViewSelectedImage1);
        imageViewSelectedImage2 = rootView.findViewById(R.id.imageViewSelectedImage2);
        imageViewSelectedImage3 = rootView.findViewById(R.id.imageViewSelectedImage3);
        recycler = rootView.findViewById(R.id.recycler);
        homeTab2 = rootView.findViewById(R.id.tab_one_image);
        edit_msg = rootView.findViewById(R.id.edit_msg);
        //edit_msg.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        iv_back = rootView.findViewById(R.id.iv_back);
        reply_invoice = rootView.findViewById(R.id.reply_invoice);
        top_header = rootView.findViewById(R.id.top_header);
        linearLayoutSelected = rootView.findViewById(R.id.linearLayoutSelected);
        btn_send = rootView.findViewById(R.id.btn_send);
        btn_report = rootView.findViewById(R.id.btn_report);
        invoice = rootView.findViewById(R.id.invoice);
        tv_noMsg = rootView.findViewById(R.id.no_msgTxt);
        date = rootView.findViewById(R.id.datetxt);
        title = rootView.findViewById(R.id.title_reply);
        time = rootView.findViewById(R.id.timetxt);
        viewMain = rootView.findViewById(R.id.linear);
        frameLayout = rootView.findViewById(R.id.container_body);
        msg_tool = rootView.findViewById(R.id.ll_message);
        ll_back = rootView.findViewById(R.id.back_ll);
        scan_tool = rootView.findViewById(R.id.ll_scan);
        search_tool = rootView.findViewById(R.id.ll_search);
        status = rootView.findViewById(R.id.status);
        tabConatiner = rootView.findViewById(R.id.tab_container);
        tabLayout = rootView.findViewById(R.id.tabs);
        tv_Status = rootView.findViewById(R.id.subjectname);
        relativeLayoutClose = rootView.findViewById(R.id.relativeLayoutClose);

        reloadDialog = Utils.showReloadDialog(mActivity);

        //tv_Status.setText(merchantName);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false);
        layoutManager.setStackFromEnd(true);
        recycler.setLayoutManager(layoutManager);

        search_tool.setOnClickListener(this);
        scan_tool.setOnClickListener(this);
        relativeLayoutClose.setOnClickListener(this);
        msg_tool.setOnClickListener(this);
        title.setOnClickListener(this);
        tv_Status.setOnClickListener(this);
        ll_back.setOnClickListener(this);
        btn_report.setOnClickListener(this);
        imageViewAttachment.setOnClickListener(this);
        imageViewSelectedImage1.setOnClickListener(this);
        imageViewSelectedImage2.setOnClickListener(this);
        imageViewSelectedImage3.setOnClickListener(this);
        sendButtonClick();

//        recycler.setOnScrollListener(new MyRecyclerScroll() {
//            @Override
//            public void show() {
//                top_header.setVisibility(View.VISIBLE);
//            }
//
//            @Override
//            public void hide() {
//                top_header.setVisibility(View.GONE);
//            }
//        });


        recycler.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && top_header.getVisibility() == View.VISIBLE) {
                    //   top_header.setVisibility(View.GONE);
                } else if (dy < 0 && top_header.getVisibility() != View.VISIBLE) {
                    // top_header.setVisibility(View.VISIBLE);
                } else {
                    //   top_header.setVisibility(View.VISIBLE);
                }
            }
        });

        showProgress();
        // if (!data.getInvoiceId().isEmpty()||(invoiceStr != null && !invoiceStr.isEmpty())){
        if (invoiceStr != null && !invoiceStr.isEmpty()) {
            ModelManager.getInstance().getMessageManager().getMessageDetail(mActivity,
                    Operations.makeJsonGetMessagesInvoice(mActivity, invoiceStr), Constants.MESSAGE_DETAIL_SUCCESS);
            title.setText("Re. Invoice:");
        } else if (productId != null && !productId.isEmpty()) {
            // else if (!data.getProductId().isEmpty()||(productId != null && !productId.isEmpty())) {
            ModelManager.getInstance().getMessageManager().getMessageDetail(mActivity,
                    Operations.getMessageProduct(mActivity, productId), Constants.MESSAGE_DETAIL_SUCCESS);
            title.setText("Re. " + productName);
            title.setPaintFlags(invoice.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        } else if (!adID.isEmpty()) {
            ModelManager.getInstance().getMessageManager().getMessageDetail(mActivity,
                    Operations.getMessageAd(mActivity, adID), Constants.MESSAGE_DETAIL_SUCCESS);
            title.setText("Re. " + productName);
            title.setPaintFlags(invoice.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        } else if (!generalID.isEmpty()) {
            ModelManager.getInstance().getMessageManager().getMessageDetail(mActivity,
                    Operations.makeJsonGetMessagesDetail(mActivity, generalID), Constants.MESSAGE_DETAIL_SUCCESS);
            title.setText("Re. " + productName);
            title.setPaintFlags(invoice.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        } else {
/*
            if (data == null) {
                displayView(new FragmentMessages(), Constants.TAG_MESSAGEPAGE, new Bundle());
                return;
            }
*/
            title.setText("Re. General Question");
            title.setPaintFlags(invoice.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            ModelManager.getInstance().getMessageManager().getMessageDetail(mActivity,
                    Operations.makeJsonGetMessagesDetail(mActivity, data.getParentId()), Constants.MESSAGE_DETAIL_SUCCESS);
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
    public void onEvent(final Event event) {
        switch (event.getKey()) {
            case Constants.MESSAGE_DETAIL_SUCCESS:
                enableViews();
                //  mPocketBar.setVisibility(View.GONE);
                hideProgress();
                list = ModelManager.getInstance().getMessageManager().messageDetailBean.getRESULT().get(0).getRESULT();

                if (list.get(0).getId() != null) {
                    btn_report.setEnabled(true);
                    Log.d("listMessage", list.size() + " l" + list.get(list.size() - 1).getMerchantReceiver());

                    if (list.get(list.size() - 1).getMerchantReceiver().equals(ATPreferences.readString(mActivity, Constants.KEY_USERID)))
                        ModelManager.getInstance().getMessageManager().readMessage(mActivity,
                                Operations.makeMessageRead(mActivity, list.get(0).getId()));

                    setAdapter();
                    if (invoiceStr != null && !invoiceStr.isEmpty()) {
                        invoice.setText("#" + list.get(0).getInvoiceId());
                        invoice.setPaintFlags(invoice.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                    }

                    if (!list.get(0).getProductId().isEmpty()) {
                        title.setText("Re. " + Utils.hexToASCII(list.get(0).getProductName()));
                        title.setPaintFlags(invoice.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                    } else if (!list.get(0).getAdId().isEmpty()) {
                        title.setText("Re. " + list.get(0).getSubject());
                        title.setPaintFlags(invoice.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                    } else {
                        title.setText("Re. " + list.get(0).getSubject());
                        title.setPaintFlags(invoice.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                    }
                    date.setText("Date: " + Utils.getDateFromMsg(list.get(0).getDate()));
                    time.setText("Time: " + Utils.getTimeFromInvoice(list.get(0).getDate()));
                    if (list.get(0).getStatus().equals("1501"))
                        status.setText("Status: " + "Open");
                    else
                        status.setText("Status: " + "Close");

                    if (!list.get(0).getUserId().equals(ATPreferences.readString(mActivity, Constants.KEY_USERID))) {
                        merchantId = list.get(0).getUserId();
                        if (merchantId.isEmpty())
                            merchantId = ATPreferences.readString(mActivity, Constants.KEY_USERID);
                    } else
                        merchantId = list.get(0).getMerchantReceiver();


                    if (list.get(0).getSeventy() != null && (!list.get(0).getSeventy().isEmpty()))
                        tv_Status.setText(Utils.hexToASCII(list.get(0).getSeventy()));
                    else
                        tv_Status.setText(merchantName);

                } else {
                    date.setText("Date: " + "N/A");
                    time.setText("Time: " + "N/A");
                    status.setText("Status: " + "N/A");
                    //reply_invoice.setVisibility(View.GONE);
                    tv_noMsg.setVisibility(View.VISIBLE);
                    btn_report.setEnabled(false);
                    recycler.setVisibility(View.GONE);
                    if (merchantId.isEmpty()) {
                        //  edit_msg.setEnabled(false);
                    } else
                        isItemDetails = true;

                   /* btn_send.setVisibility(View.GONE);
                    edit_msg.setVisibility(View.GONE);
                    Toast.makeText(mActivity, "Message Retrieving Failed", Toast.LENGTH_LONG).show();*/
                }
                //  mPocketBar.progressiveStop();
                // mPocketBar.setVisibility(View.INVISIBLE);

                break;
            case Constants.ADDRESS_NEARBY_SUCCESS:
                Utils.setRecyclerNearByAdapter(getActivity());
                break;
            case Constants.MESSAGE_SEND_ABUSE_SUCCESS:
                // mPocketBar.setVisibility(View.GONE);
                hideProgress();
                Utils.baseshowFeedbackMessage(mActivity, rootView, "Report Sent Successfully.");
                break;
            case Constants.MESSAGE_SEND_SUCCESS:
                if (linearLayoutSelected.getVisibility() == View.VISIBLE) {
                    linearLayoutSelected.setVisibility(View.GONE);
                }
                stringImage64 = new ArrayList<>();
                jsonArrayImages = new JSONArray();
                jsonObject = new JSONObject();
                imageViewSelectedImage1.setImageResource(R.drawable.ic_add_box_black_24dp);
                imageViewSelectedImage2.setImageResource(R.drawable.ic_add_box_black_24dp);
                imageViewSelectedImage3.setImageResource(R.drawable.ic_add_box_black_24dp);

                hideProgress();
                //mPocketBar.setVisibility(View.GONE);
                if (invoiceStr != null && !invoiceStr.isEmpty())
                    ModelManager.getInstance().getMessageManager().getMessageDetail(mActivity,
                            Operations.makeJsonGetMessagesInvoice(mActivity, invoiceStr), Constants.MESSAGE_DETAIL_SUCCESS);

                else if (!adID.isEmpty()) {
                    ModelManager.getInstance().getMessageManager().getMessageDetail(mActivity,
                            Operations.getMessageAd(mActivity, adID), Constants.MESSAGE_DETAIL_SUCCESS);
                    title.setText("Re. " + productName);
                    title.setPaintFlags(invoice.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                } else if (productId != null && !productId.isEmpty())
                    ModelManager.getInstance().getMessageManager().getMessageDetail(mActivity,
                            Operations.getMessageProduct(mActivity, productId), Constants.MESSAGE_DETAIL_SUCCESS);
                else if (!generalID.isEmpty()) {
                    ModelManager.getInstance().getMessageManager().getMessageDetail(mActivity,
                            Operations.makeJsonGetMessagesDetail(mActivity, generalID), Constants.MESSAGE_DETAIL_SUCCESS);
                    title.setText("Re. " + productName);
                    title.setPaintFlags(invoice.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                } else
                    ModelManager.getInstance().getMessageManager().getMessageDetail(mActivity,
                            Operations.makeJsonGetMessagesDetail(mActivity, data.getParentId()), Constants.MESSAGE_DETAIL_SUCCESS);

                Log.d("Message Success", "Messages");
                break;
            case -1:
                // mPocketBar.setVisibility(View.GONE);
                hideProgress();
                disableViews();
                relaodDialogShow();
                // edit_msg.setEnabled(false);
                // Toast.makeText(mActivity, "Message Retrieving Failed", Toast.LENGTH_LONG).show();
                break;
            case Constants.GET_SERVER_ERROR:
                // mPocketBar.setVisibility(View.GONE);
                hideProgress();
                Utils.baseshowFeedbackMessage(mActivity, rootView, "Something Went Wrong...");

                break;

            case Constants.FCM_MSG_NOTIFICATION:
                if (productId.equals(MyFirebaseMessagingService.productId))
                    loadMessages = true;
                else if (adID.equals(MyFirebaseMessagingService.adId))
                    loadMessages = true;
                else if (invoiceStr.equals(MyFirebaseMessagingService.invoiceId))
                    loadMessages = true;
                else if (generalID.equals(MyFirebaseMessagingService.generalMessageId))
                    loadMessages = true;

                if (!loadMessages)
                    return;

                if (!MyFirebaseMessagingService.productId.isEmpty()) {
                    ModelManager.getInstance().getMessageManager().getMessageDetail(mActivity,
                            Operations.getMessageProduct(mActivity, MyFirebaseMessagingService.productId),
                            Constants.MESSAGE_DETAIL_SUCCESS);
                    title.setText("Re. " + MyFirebaseMessagingService.productName);
                    title.setPaintFlags(invoice.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                } else if (!MyFirebaseMessagingService.adId.isEmpty()) {
                    ModelManager.getInstance().getMessageManager().getMessageDetail(mActivity,
                            Operations.getMessageAd(mActivity, MyFirebaseMessagingService.adId), Constants.MESSAGE_DETAIL_SUCCESS);
                    title.setText("Re. " + productName);
                    title.setPaintFlags(invoice.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                } else if (!MyFirebaseMessagingService.invoiceId.isEmpty()) {
                    ModelManager.getInstance().getMessageManager().getMessageDetail(mActivity,
                            Operations.makeJsonGetMessagesInvoice(mActivity, MyFirebaseMessagingService.invoiceId),
                            Constants.MESSAGE_DETAIL_SUCCESS);
                    title.setText("Re. Invoice:");

                } else if (!MyFirebaseMessagingService.generalMessageId.isEmpty()) {
                    ModelManager.getInstance().getMessageManager().getMessageDetail(mActivity,
                            Operations.makeJsonGetMessagesDetail(mActivity, MyFirebaseMessagingService.generalMessageId),
                            Constants.MESSAGE_DETAIL_SUCCESS);


                }
                break;

        }
    }

    private void setAdapter() {
        if (!isAdapterSet) {
            messageDetailAdapter = new MessageDetailAdapter(mActivity, list);
            recycler.setAdapter(messageDetailAdapter);
            isAdapterSet = true;
        } else {
            messageDetailAdapter.customNotify(list);
            // recycler.scrollToPosition(list.size() - 1);
            //recycler.getLayoutManager().scrollToPosition(messageDetailAdapter.getItemCount() - 1);
            recycler.smoothScrollToPosition(list.size() - 1);
            messageDetailAdapter.notifyItemInserted(list.size() - 1);
        }
    }


    private void relaodDialogShow() {
        TextView textView_yes = reloadDialog.findViewById(R.id.txtok);
        TextView textView_no = reloadDialog.findViewById(R.id.txtcancel);
        TextView textView_title = reloadDialog.findViewById(R.id.txtmessage);

        textView_title.setText("We're sorry but there seems to be some network issues connecting to the server. Please try again.");
        textView_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reloadActivity();
                reloadDialog.dismiss();
            }
        });

        textView_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reloadDialog.dismiss();

            }
        });

        reloadDialog.show();
    }


    private void reloadActivity() {
       /* Intent intent = getIntent();
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);*/
    }


    private void enableViews() {
        top_header.setVisibility(View.VISIBLE);
        //mPocketBar.setVisibility(View.VISIBLE);
        reply_invoice.setVisibility(View.VISIBLE);
        tv_noMsg.setVisibility(View.GONE);
        recycler.setVisibility(View.VISIBLE);
    }

    private void disableViews() {
        // top_header.setVisibility(View.GONE);
        // mPocketBar.setVisibility(View.GONE);
        date.setText("Date: " + "N/A");
        time.setText("Time: " + "N/A");
        status.setText("Status: " + "N/A");
        //  reply_invoice.setVisibility(View.GONE);
        tv_noMsg.setVisibility(View.VISIBLE);
        recycler.setVisibility(View.GONE);
        btn_report.setEnabled(false);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_ll:
                //backpressed();
                onBackPress();
                break;
            case R.id.imageViewAttachment:
                selectedImageState = 0;
                showBottomSheetDialog();
                break;
            case R.id.imageViewSelectedImage1:
                selectedImageState = 0;
                showBottomSheetDialog();
                break;
            case R.id.imageViewSelectedImage2:
                selectedImageState = 1;
                showBottomSheetDialog();
                break;
            case R.id.imageViewSelectedImage3:
                selectedImageState = 2;
                showBottomSheetDialog();
                break;
            case R.id.relativeLayoutClose:
                selectedImage = null;
                stringImage64 = new ArrayList<>();
                jsonArrayImages = new JSONArray();
                jsonObject = new JSONObject();
                linearLayoutSelected.setVisibility(View.GONE);
                imageViewSelectedImage1.setImageResource(R.drawable.ic_add_box_black_24dp);
                imageViewSelectedImage2.setImageResource(R.drawable.ic_add_box_black_24dp);
                imageViewSelectedImage3.setImageResource(R.drawable.ic_add_box_black_24dp);

                break;
            case R.id.title_reply:
                if (!productId.isEmpty()) {
                    FragmentItemDetails fragment = new FragmentItemDetails();
                    Bundle bundle = new Bundle();
                    bundle.putString("productId", productId);
                    bundle.putString("productType", "21");
                    fragment.setArguments(bundle);
                    ((HomeActivity) getActivity()).displayView(fragment, Constants.TAG_DETAILSPAGE, bundle);
                } else if (!adID.isEmpty()) {
                    Bundle bundle = new Bundle();
                    bundle.putString("videoUrl", videoUrl);
                    bundle.putString("image", imageUrl);
                    bundle.putString("merchant", status.getText().toString());
                    bundle.putString("adName", productName);
                    bundle.putString("desc", "");
                    bundle.putString("id", adID);
                    bundle.putString("ad_id", adID);
                    bundle.putString("merchantid", merchantId);
                    bundle.putInt("adpos", 0);
                    ((HomeActivity) getActivity()).displayView(new FragmentAdDetail(), Constants.TAG_AD_DETAIL, bundle);
                }
                break;

            case R.id.subjectname:
                ATPreferences.putBoolean(mActivity, Constants.HEADER_STORE, true);
                ATPreferences.putString(mActivity, Constants.MERCHANT_ID, merchantId);
                startActivity(new Intent(mActivity, HomeActivity.class));
                break;

            case R.id.btn_report:
                final Dialog dialog = Utils.reportMessage(mActivity);
                final EditText editText = dialog.findViewById(R.id.editTextMessage);
                dialog.findViewById(R.id.txtok).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (editText.getText().toString().isEmpty())
                            //Toast.makeText(MessageDetailActivity.this, "Please write something about Report.", Toast.LENGTH_SHORT).show();
                            Utils.baseshowFeedbackMessage(mActivity, rootView, "Please write something about Report.");
                        else {
                            dialog.dismiss();
                            //mPocketBar.setVisibility(View.VISIBLE);
                            showProgress();
                            ModelManager.getInstance().getMessageManager().sendMessage(mActivity,
                                    Operations.sendMessageReportAbuse(mActivity, "abuse report",
                                            editText.getText().toString(), list.get(0).getId(), merchantId), Constants.MESSAGE_SEND_ABUSE_SUCCESS);
                        }
                    }
                });
                dialog.findViewById(R.id.txtcancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                break;

        }
    }


    public static class MessageDetailAdapter extends RecyclerView.Adapter<MessageDetailAdapter.ViewHolder> {

        private AdapterClick adapterClick;
        List<MessageListBean.RESULT.MessageData> list;
        Activity activity;

        public MessageDetailAdapter(Activity activity, List<MessageListBean.RESULT.MessageData> list) {
            this.activity = activity;
            this.list = list;
        }

        public void customNotify(List<MessageListBean.RESULT.MessageData> list) {
            this.list = list;
            // notifyItemInserted(list.size() - 1);
            notifyDataSetChanged();

        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_row, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {

            //  holder.txt_title.setText(list.get(position).getName());
            SimpleDateFormat sdf_old = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date old = null;
            try {
                old = sdf_old.parse(list.get(position).getDate());
                String userDateTime = Utils.getLosAngelesLocalDateTime(list.get(position).getDate());
                old = sdf_old.parse(userDateTime);

            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (list.get(position).getUserId().equals(ATPreferences.readString(activity, Constants.KEY_USERID))) {

                if (list.get(position).getMiList().size() > 0) {
                    Log.d("IMAGEURLS684", ATPreferences.readString(activity, Constants.KEY_MESSAGE_IMAGE_URL) +
                            list.get(position).getMiList().get(0).get_119_17());

                    holder.linearLayoutImagesParentSender.setVisibility(View.VISIBLE);
                    holder.linearLayoutImagesParentReceiver.setVisibility(View.GONE);
                    holder.cardViewReceiver1.setVisibility(View.GONE);
                    holder.cardViewReceiver2.setVisibility(View.GONE);
                    holder.cardViewReceiver3.setVisibility(View.GONE);

                    if (list.get(position).getMiList().size() == 1) {
                        holder.cardViewSender1.setVisibility(View.VISIBLE);
                        holder.cardViewSender2.setVisibility(View.GONE);
                        holder.cardViewSender3.setVisibility(View.GONE);
                        Picasso.get().load(ATPreferences.readString(activity, Constants.KEY_MESSAGE_IMAGE_URL) +
                                list.get(position).getMiList().get(0).get_119_17()).into(holder.imageViewSender1);

                    } else if (list.get(position).getMiList().size() == 2) {
                        holder.cardViewSender1.setVisibility(View.VISIBLE);
                        holder.cardViewSender2.setVisibility(View.VISIBLE);
                        holder.cardViewSender3.setVisibility(View.GONE);
                        Picasso.get().load(ATPreferences.readString(activity, Constants.KEY_MESSAGE_IMAGE_URL) +
                                list.get(position).getMiList().get(0).get_119_17()).into(holder.imageViewSender1);
                        Picasso.get().load(ATPreferences.readString(activity, Constants.KEY_MESSAGE_IMAGE_URL) +
                                list.get(position).getMiList().get(1).get_119_17()).into(holder.imageViewSender2);

                    } else /*if (list.get(position).getMiList().size() == 3)*/ {
                        holder.cardViewSender1.setVisibility(View.VISIBLE);
                        holder.cardViewSender2.setVisibility(View.VISIBLE);
                        holder.cardViewSender3.setVisibility(View.VISIBLE);
                        Picasso.get().load(ATPreferences.readString(activity, Constants.KEY_MESSAGE_IMAGE_URL) +
                                list.get(position).getMiList().get(0).get_119_17()).into(holder.imageViewSender1);
                        Picasso.get().load(ATPreferences.readString(activity, Constants.KEY_MESSAGE_IMAGE_URL) +
                                list.get(position).getMiList().get(1).get_119_17()).into(holder.imageViewSender2);
                        Picasso.get().load(ATPreferences.readString(activity, Constants.KEY_MESSAGE_IMAGE_URL) +
                                list.get(position).getMiList().get(2).get_119_17()).into(holder.imageViewSender3);

                    }
                } else {
                    holder.linearLayoutImagesParentSender.setVisibility(View.GONE);
                    holder.linearLayoutImagesParentReceiver.setVisibility(View.GONE);
                    holder.cardViewSender1.setVisibility(View.GONE);
                    holder.cardViewSender2.setVisibility(View.GONE);
                    holder.cardViewSender3.setVisibility(View.GONE);
                    holder.cardViewReceiver1.setVisibility(View.GONE);
                    holder.cardViewReceiver2.setVisibility(View.GONE);
                    holder.cardViewReceiver3.setVisibility(View.GONE);

                }

                holder.merchantMessagell.setVisibility(View.GONE);
                holder.getReplyView.setVisibility(View.VISIBLE);

                holder.txt_date.setText(Utils.getTimeAgo(old.getTime(), activity));

                if (holder.txt_date.getText().toString().isEmpty())
                    holder.txt_date.setText("today");

                holder.txt_msg.setText(Html.fromHtml(Utils.getStringHexaDecimal(StringEscapeUtils.unescapeHtml(list.get(position).getContextData()))));

                holder.txt_time.setText(Utils.getLosAngelesLocalTime(list.get(position).getDate()));


                Picasso.get().load(ATPreferences.readString(activity, Constants.KEY_MESSAGE_IMAGE_URL) +
                        list.get(position).getLogoImage()).into(holder.img_main);
            } else {

                if (list.get(position).getMiList().size() > 0) {
                    Log.d("IMAGEURLS747", ATPreferences.readString(activity, Constants.KEY_MESSAGE_IMAGE_URL) +
                            list.get(position).getMiList().get(0).get_119_17());
                    holder.linearLayoutImagesParentReceiver.setVisibility(View.VISIBLE);
                    holder.linearLayoutImagesParentSender.setVisibility(View.GONE);
                    holder.cardViewSender1.setVisibility(View.GONE);
                    holder.cardViewSender2.setVisibility(View.GONE);
                    holder.cardViewSender3.setVisibility(View.GONE);

                    if (list.get(position).getMiList().size() == 1) {
                        holder.cardViewReceiver1.setVisibility(View.VISIBLE);
                        holder.cardViewReceiver2.setVisibility(View.GONE);
                        holder.cardViewReceiver3.setVisibility(View.GONE);
                        Picasso.get().load(ATPreferences.readString(activity, Constants.KEY_MESSAGE_IMAGE_URL) +
                                list.get(position).getMiList().get(0).get_119_17()).into(holder.imageViewReceiver1);

                    } else if (list.get(position).getMiList().size() == 2) {
                        holder.cardViewReceiver1.setVisibility(View.VISIBLE);
                        holder.cardViewReceiver2.setVisibility(View.VISIBLE);
                        holder.cardViewReceiver3.setVisibility(View.GONE);
                        Picasso.get().load(ATPreferences.readString(activity, Constants.KEY_MESSAGE_IMAGE_URL) +
                                list.get(position).getMiList().get(0).get_119_17()).into(holder.imageViewReceiver1);
                        Picasso.get().load(ATPreferences.readString(activity, Constants.KEY_MESSAGE_IMAGE_URL) +
                                list.get(position).getMiList().get(1).get_119_17()).into(holder.imageViewReceiver2);

                    } else if (list.get(position).getMiList().size() == 3) {
                        holder.cardViewReceiver1.setVisibility(View.VISIBLE);
                        holder.cardViewReceiver2.setVisibility(View.VISIBLE);
                        holder.cardViewReceiver3.setVisibility(View.VISIBLE);
                        Picasso.get().load(ATPreferences.readString(activity, Constants.KEY_MESSAGE_IMAGE_URL) +
                                list.get(position).getMiList().get(0).get_119_17()).into(holder.imageViewReceiver1);
                        Picasso.get().load(ATPreferences.readString(activity, Constants.KEY_MESSAGE_IMAGE_URL) +
                                list.get(position).getMiList().get(1).get_119_17()).into(holder.imageViewReceiver2);
                        Picasso.get().load(ATPreferences.readString(activity, Constants.KEY_MESSAGE_IMAGE_URL) +
                                list.get(position).getMiList().get(2).get_119_17()).into(holder.imageViewReceiver3);

                    } else {
                        holder.linearLayoutImagesParentSender.setVisibility(View.GONE);
                        holder.linearLayoutImagesParentReceiver.setVisibility(View.GONE);
                        holder.cardViewReceiver1.setVisibility(View.GONE);
                        holder.cardViewReceiver2.setVisibility(View.GONE);
                        holder.cardViewReceiver3.setVisibility(View.GONE);
                    }
                }

                holder.merchantMessagell.setVisibility(View.VISIBLE);
                holder.getReplyView.setVisibility(View.GONE);
                holder.tv_storeName.setText(Utils.hexToASCII(list.get(position).getName()));

                holder.txt_store_time.setText(" " + Utils.getLosAngelesLocalTime(list.get(position).getDate()));

                holder.txt_store_date.setText(Utils.getTimeAgo(old.getTime(), activity));
                if (holder.txt_store_date.getText().toString().isEmpty())
                    holder.txt_store_date.setText("today");

                holder.txt_sending.setText(Html.fromHtml(Utils.getStringHexaDecimal(StringEscapeUtils.unescapeHtml(list.get(position).getContextData()))));
            }

            holder.cardViewSender1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startZoomActivity(list.get(position).getMiList().get(0).get_119_17());
                }
            });
            holder.cardViewSender2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startZoomActivity(list.get(position).getMiList().get(1).get_119_17());
                }
            });
            holder.cardViewSender3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startZoomActivity(list.get(position).getMiList().get(2).get_119_17());
                }
            });

            holder.cardViewReceiver1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startZoomActivity(list.get(position).getMiList().get(0).get_119_17());
                }
            });
            holder.cardViewReceiver2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startZoomActivity(list.get(position).getMiList().get(1).get_119_17());
                }
            });
            holder.cardViewReceiver3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startZoomActivity(list.get(position).getMiList().get(2).get_119_17());
                }
            });


        }

        public String getEmoticon(int originalUnicode) {
            return new String(Character.toChars(originalUnicode));
        }

        private String convertEmoji(String content) {
            content = content.replaceAll("U\\+", "0x");
            String keyword = "0x";

            int index = content.indexOf(keyword);
            int spaceIndex;

            while (index >= 0) {
                spaceIndex = content.indexOf(" ", index);

                if (spaceIndex > index) {
                    String emoji = content.substring(index, spaceIndex);
                    content = content.replaceAll(emoji, getEmoticon(Integer.decode(emoji)));
                }
                index = content.indexOf(keyword, index + keyword.length());
            }

            return content;
        }

        public void startZoomActivity(String path) {

            activity.startActivity(new Intent(activity, ZoomMessageImage.class).putExtra("image", path));
        }

        @Override
        public int getItemCount() {
            return list.size();
        }


        public class ViewHolder extends RecyclerView.ViewHolder {

            private final TextView txt_title, txt_date, txt_store_date, txt_time, tv_storeName, txt_store_time;
            private final ImageView img_main;
            private final TextView txt_msg, txt_sending;
            private final LinearLayout merchantMessagell;
            private final LinearLayout getReplyView, linearLayoutImagesParentSender, linearLayoutImagesParentReceiver;
            private final CardView cardViewSender1, cardViewSender2, cardViewSender3;
            private final ImageView imageViewSender1, imageViewSender2, imageViewSender3;
            private final CardView cardViewReceiver1, cardViewReceiver2, cardViewReceiver3;
            private final ImageView imageViewReceiver1, imageViewReceiver2, imageViewReceiver3;

            public ViewHolder(View itemView) {
                super(itemView);
                txt_title = itemView.findViewById(R.id.txt_title);
                txt_date = itemView.findViewById(R.id.txt_date);
                txt_time = itemView.findViewById(R.id.txt_time);
                txt_store_time = itemView.findViewById(R.id.txt_time_merchant);
                txt_store_date = itemView.findViewById(R.id.txt_date_merchant);
                txt_sending = itemView.findViewById(R.id.sending);
                txt_msg = itemView.findViewById(R.id.txt_msg);
                img_main = itemView.findViewById(R.id.img_main);
                merchantMessagell = itemView.findViewById(R.id.replysent);
                getReplyView = itemView.findViewById(R.id.mymsg);
                tv_storeName = itemView.findViewById(R.id.store_name);
                linearLayoutImagesParentSender = itemView.findViewById(R.id.senderViewImagesRoot);
                linearLayoutImagesParentReceiver = itemView.findViewById(R.id.receiverViewImagesRoot);

                cardViewSender2 = itemView.findViewById(R.id.cardView2Sender);
                cardViewSender1 = itemView.findViewById(R.id.cardView1Sender);
                cardViewSender3 = itemView.findViewById(R.id.cardView3Sender);
                imageViewSender1 = itemView.findViewById(R.id.imageView1Sender);
                imageViewSender2 = itemView.findViewById(R.id.imageView2Sender);
                imageViewSender3 = itemView.findViewById(R.id.imageView3Sender);
                cardViewReceiver1 = itemView.findViewById(R.id.cardView1Receiver);
                cardViewReceiver2 = itemView.findViewById(R.id.cardView2Receiver);
                cardViewReceiver3 = itemView.findViewById(R.id.cardView3Receiver);
                imageViewReceiver1 = itemView.findViewById(R.id.imageView1Receiver);
                imageViewReceiver2 = itemView.findViewById(R.id.imageView2Receiver);
                imageViewReceiver3 = itemView.findViewById(R.id.imageView3Receiver);


            }
        }

        public void setOnItemClickListner(AdapterClick adapterClick) {
            this.adapterClick = adapterClick;
        }

        public interface AdapterClick {
            void onItemClick(View v, int position);
        }

    }


    public String toHex(String arg) {
        char[] digital = "0123456789ABCDEF".toCharArray();
        StringBuffer sb = new StringBuffer("");
        byte[] bs = arg.getBytes();
        int bit;
        for (int i = 0; i < bs.length; i++) {
            bit = (bs[i] & 0x0f0) >> 4;
            sb.append(digital[bit]);
            bit = bs[i] & 0x0f;
            sb.append(digital[bit]);
        }
        return sb.toString();
    }

    public String hextoString(String hex) {
        String digital = "0123456789ABCDEF";
        char[] hex2char = hex.toCharArray();
        byte[] bytes = new byte[hex.length() / 2];
        int temp;
        for (int i = 0; i < bytes.length; i++) {
            temp = digital.indexOf(hex2char[2 * i]) * 16;
            temp += digital.indexOf(hex2char[2 * i + 1]);
            bytes[i] = (byte) (temp & 0xff);
        }
        return new String(bytes);
    }

    public static String convertUTF8ToString(String s) {
        String out = null;
        try {
            out = new String(s.getBytes("ISO-8859-1"), "UTF-8");
        } catch (java.io.UnsupportedEncodingException e) {
            return null;
        }
        return out;
    }


    // convert internal Java String format to UTF-8
    public static String convertStringToUTF8(String s) {
        String out = null;
        try {
            out = new String(s.getBytes("UTF-8"), "ISO-8859-1");
        } catch (java.io.UnsupportedEncodingException e) {
            return null;
        }
        return out;
    }

    private void sendButtonClick() {
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message_str = edit_msg.getText().toString();
                String hexmsg = Utils.convertStringToHex(StringEscapeUtils.escapeHtml(message_str));
                Log.d("HEXXMES", Utils.convertStringToHex(StringEscapeUtils.escapeHtml(message_str)));

                /*try {
                    byte[] data = edit_msg.getText().toString().getBytes("UTF-8");
                    String base64String = Base64.encodeToString(data, Base64.DEFAULT);
                    Log.d("HEXXMES",hexmsg+"  "+message_str+"  "+base64String);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                if (!hexmsg.isEmpty())
                    return;*/
                if (!message_str.isEmpty()) {
                    //mPocketBar.setVisibility(View.VISIBLE);
                    showProgress();

                    if (invoiceStr != null && !invoiceStr.isEmpty())
                        ModelManager.getInstance().getMessageManager().sendMessage(mActivity,
                                Operations.sendMessageReply(mActivity, list.get(0).getParentId(),
                                        "1", merchantId,
                                        Utils.getElevenDigitId(list.get(0).getType()), list.get(0).getSubject(), hexmsg,
                                        Utils.getElevenDigitId(invoiceStr), list.get(0).getId(), "",
                                        list.get(list.size() - 1).getId(),
                                        jsonArrayImages, locationId), Constants.MESSAGE_SEND_SUCCESS);
                    else if (adID != null && !adID.isEmpty()) {
                        if (list.get(0).getProductName() == null) {
                            ModelManager.getInstance().getMessageManager().sendMessage(mActivity,
                                    Operations.sendMessageForAd(mActivity,
                                            merchantId, Utils.getElevenDigitId("92"),
                                            productName, message_str,
                                            Utils.getElevenDigitId(adID), jsonArrayImages, locationId), Constants.MESSAGE_SEND_SUCCESS);

                        } else {
                            String id = list.get(list.size() - 1).getId();
                            ModelManager.getInstance().getMessageManager().sendMessage(mActivity,
                                    Operations.sendMessageReply(mActivity, list.get(0).getParentId(),
                                            "4", merchantId,
                                            Utils.getElevenDigitId(list.get(0).getType()), list.get(0).getSubject(), hexmsg,
                                            "", list.get(0).getId(), adID,
                                            id, jsonArrayImages, locationId), Constants.MESSAGE_SEND_SUCCESS);

                        }
                    } else if (productId != null && !productId.isEmpty()) {

                        if (/*isItemDetails &&*/ list.get(0).getProductName() == null)
                            ModelManager.getInstance().getMessageManager().sendMessage(mActivity,
                                    Operations.sendMessageForProduct(mActivity,
                                            merchantId, Utils.getElevenDigitId("92"),
                                            productName, message_str,
                                            Utils.getElevenDigitId(productId), jsonArrayImages, locationId), Constants.MESSAGE_SEND_SUCCESS);
                        else {
                            String id = list.get(list.size() - 1).getId();
                            ModelManager.getInstance().getMessageManager().sendMessage(mActivity,
                                    Operations.sendMessageReply(mActivity, list.get(0).getParentId(),
                                            "2", merchantId,
                                            Utils.getElevenDigitId(list.get(0).getType()), list.get(0).getSubject(),
                                            hexmsg, "", list.get(0).getId(), productId,
                                            id, jsonArrayImages, locationId), Constants.MESSAGE_SEND_SUCCESS);
                        }
                    } else {
                        String id = list.get(list.size() - 1).getId();
                        ModelManager.getInstance().getMessageManager().sendMessage(mActivity,
                                Operations.sendMessageReply(mActivity, list.get(list.size() - 1).getParentId(),
                                        "3", merchantId,
                                        Utils.getElevenDigitId(list.get(list.size() - 1).getType()), list.get(list.size() - 1).getSubject(),
                                        hexmsg, "",
                                        list.get(list.size() - 1).getId(), "", id, jsonArrayImages,
                                        locationId), Constants.MESSAGE_SEND_SUCCESS);
                    }
                    edit_msg.setText("");
                    Utils.dismissKeyboard(mActivity, edit_msg);
                } else
                    baseshowFeedbackMessage(getActivity(), rootView, "Please enter some text");
            }
        });

    }


    public void showBottomSheetDialog() {
        LayoutInflater layoutInflater = (LayoutInflater) mActivity.getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.view_choose_attachment, null);
        dialog = new BottomSheetDialog(mActivity);
        dialog.setContentView(view);
        dialog.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundResource(android.R.color.transparent);
        TextView textViewGallery = dialog.findViewById(R.id.textViewPhoto);
        TextView textViewCamera = dialog.findViewById(R.id.textViewCamera);
        TextView textViewCancel = dialog.findViewById(R.id.textViewCancel);
        textViewCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePickerIntent(ImagePicker.Mode.CAMERA);

            }
        });
        textViewGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePickerIntent(ImagePicker.Mode.GALLERY);
            }
        });
        textViewCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);
    }

    public void imagePickerIntent(ImagePicker.Mode mode) {
        new ImagePicker.Builder(mActivity)
                .mode(mode)
                .compressLevel(ImagePicker.ComperesLevel.MEDIUM)
                .directory(ImagePicker.Directory.DEFAULT)
                .extension(ImagePicker.Extension.JPG)
                .allowMultipleImages(true)
                .enableDebuggingMode(true)
                .build();

    }


    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        if (reqCode == ImagePicker.IMAGE_PICKER_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> mPaths = data.getStringArrayListExtra(ImagePicker.EXTRA_IMAGE_PATH);

            jsonObject = new JSONObject();
            // for (int j = 0; j < mPaths.size(); j++) {
            Bitmap myBitmap = BitmapFactory.decodeFile(new File(mPaths.get(0)).getAbsolutePath());
            if (selectedImageState == 0)
                imageViewSelectedImage1.setImageBitmap(myBitmap);
            else if (selectedImageState == 1)
                imageViewSelectedImage2.setImageBitmap(myBitmap);
            else if (selectedImageState == 2)
                imageViewSelectedImage3.setImageBitmap(myBitmap);
            linearLayoutSelected.setVisibility(View.VISIBLE);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
            byte[] b = baos.toByteArray();
            stringImage64.add(Base64.encodeToString(b, Base64.DEFAULT));
            try {
                jsonObject.put("119.17", Base64.encodeToString(b, Base64.URL_SAFE));
            } catch (JSONException e) {
                e.printStackTrace();
                //   }
            }
            if (jsonObject.has("119.17"))
                jsonArrayImages.put(jsonObject);
        }
    }

}

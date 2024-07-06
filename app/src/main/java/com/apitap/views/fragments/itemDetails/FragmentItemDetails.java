package com.apitap.views.fragments.itemDetails;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;

import androidx.annotation.Nullable;

import com.apitap.views.adapters.AdapterQtySpinner;
import com.apitap.views.fragments.BaseFragment;
import com.apitap.views.fragments.FragmentStoreDetails;
import com.apitap.views.fragments.FragmentZoomImage;
import com.apitap.views.fragments.SendMessage;
import com.apitap.views.fragments.adDetails.FragmentAdDetail;
import com.apitap.views.fragments.shoppingCart.ShoppingCartFragment;
import com.apitap.views.fragments.storefront.FragmentStoreFront;
import com.google.android.gms.vision.text.Line;
import com.google.android.material.tabs.TabLayout;

import androidx.fragment.app.FragmentTransaction;
import androidx.cardview.widget.CardView;

import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.apitap.R;
import com.apitap.controller.ModelManager;
import com.apitap.model.Constants;
import com.apitap.model.Operations;
import com.apitap.model.Utils;
import com.apitap.model.bean.CategoryDetailsBean;
import com.apitap.model.bean.DetailsBean;
import com.apitap.model.bean.ProductDetailsBean;
import com.apitap.model.bean.ProductOptionsBean;
import com.apitap.model.bean.RelatedDetailsBean;
import com.apitap.model.bean.SizeBean;
import com.apitap.model.bean.Sizedata;
import com.apitap.model.bean.SpecialItemBean;
import com.apitap.model.customclasses.Event;
import com.apitap.model.customclasses.NestedListView;
import com.apitap.model.preferences.ATPreferences;
import com.apitap.views.ForgotPasswordActivity;
import com.apitap.views.HomeActivity;
import com.apitap.views.LoginActivity;
import com.apitap.views.adapters.SubImagesAdapter;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.linearlistview.LinearListView;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ashok-kumar on 9/6/16.
 */

public class FragmentItemDetails extends BaseFragment implements View.OnClickListener, AdapterView.OnItemClickListener {
    EditText editEmail, editPassword;
    TextView actual_price, price_afterdiscount, storeName, more_details;
    Activity mActivity;
    LinearLayout llAddToCart;
    private LinearLayout linearLayoutHeaderStoreFront;
    private RelativeLayout relativeLayoutSearchBarStoreFront;
    private TabLayout tabLayout;
    ScrollView scrollView;
    ImageView icImage, ivFav, ivChat, imageViewZoomIn, imageViewZoomOut;
    NestedListView listSubInages;
    GridView list;
    RelativeLayout layout;
    private TextView productDesc;
    TextView sellerName, txtRelatedItems, txtCategorySpecialItems, txtCategoryItems, txtTitle, desc, minus, plus, tv_quantity;
    LinearLayout layoutFavorite, llQuantity;
    ImageView back_tv;
    ArrayList<String> sampleImagesArrayList = new ArrayList<>();
    CircularProgressView mPocketBar;
    String productImage, productId, productName, productType;
    String merchantID = "";
    LinearListView listRelatedItems, listCategoryItems;
    LinearListView listSpecialRelatedItems;
    CardView layAttributes, layRelatedItems, layCategoryItems, layCategoryspecialItems;
    List<RelatedDetailsBean.RESULT_> relatedArray;
    List<SpecialItemBean.RESULT> specialArrayRelated;
    List<CategoryDetailsBean.RESULT> catArray;
    Spinner spinnerSize, spinnerColor;
    LinearLayout linearLayoutZoomIn, linearLayoutZoomOut, llListImages;
    TextView thumbNa;
    TextView tvAvailability;
    String actualPrice, priceAfterDiscount;
    ArrayList<ProductOptionsBean> productOptionsArrayList = new ArrayList<ProductOptionsBean>();
    ArrayList<ProductOptionsBean> productOptionChoices1ArrayList = new ArrayList<ProductOptionsBean>();
    ArrayList<ProductOptionsBean> productOptionChoices2ArrayList = new ArrayList<ProductOptionsBean>();
    GetOption1 getOptionChoicesOneAdapter;
    GetOption2 getOptionChoicesTwoAdapter;
    Spinner spinnerCartOption1, spinnerCartOption2;
    Spinner spinnerInfoOption1, spinnerInfoOption2;
    EditText editTextSpecialInstructions;
    Button detailStore;
    TextView textViewVideo;
    ImageView inbox;
    LinearListView listView, listViewItems, listViewSaved;
    String option_Id, option_Id2, videoUrl = "";
    String str_option_Id = "", str_option_Id2 = "";
    int state = 0;
    boolean isFavouriteboolean = false;
    private boolean isGuest = false;
    private int related_pos = 0;
    private String barcode = "", imageUrl = "";
    private String specs;
    private String sku = "";
    private String brandNames = "";
    private String model;
    private String age21;
    private String age18;
    private String limit_quantity = "";
    private String flag = "";
    private String limit = "0";
    private Dialog reloadDialog;
    private RelativeLayout rootLayout;
    private SubImagesAdapter adapterSubImages;
    private float fontSize = 14;
    private Dialog moreDetailDialog;
    private Dialog addToCartDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_item_details, container, false);
        return v;
    }

    @Override
    public void onViewCreated(View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        mActivity = getActivity();
        initViews(v);
        setListeners(v);


        Bundle b = getArguments();
        if (b != null) {
            isGuest = ATPreferences.readBoolean(getActivity(), Constants.GUEST);
            productId = b.getString("productId");
            productType = b.getString("productType");
            if (productType == null) {
                productType = "21";
            }
            flag = b.getString("flag");


            mPocketBar.setVisibility(View.VISIBLE);
            getfocus();


            ModelManager.getInstance().getDetailsManager().getDetails(getActivity(), Operations.makeJsonGetItemDetails(getActivity(), productId, ATPreferences.readString(mActivity, Constants.KEY_USERID), productType), false);

            //21 for items
            if (productType.equals("21")) {
                ModelManager.getInstance().getDetailsManager().getDetails(getActivity(), Operations.makeJsonGetRelatedItems(getActivity(), productId), true);
                txtCategorySpecialItems.setVisibility(View.GONE);
                layCategoryspecialItems.setVisibility(View.GONE);
                more_details.setVisibility(View.VISIBLE);
                llAddToCart.setVisibility(View.VISIBLE);
            } else {
                txtRelatedItems.setVisibility(View.GONE);
                layCategoryItems.setVisibility(View.GONE);
                more_details.setVisibility(View.INVISIBLE);
                llAddToCart.setVisibility(View.INVISIBLE);
                ModelManager.getInstance().getDetailsManager().getSpecialDetails(getActivity(), Operations.makeJsonSpecialItemrequired(getActivity(), productId));
            }
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (isGuest) {
            ModelManager.getInstance().getLoginManager().guestLastActivity(getActivity(), Operations.makeJsonLastActivityByGuest(getActivity()));
        }
    }

    private void setListeners(View v) {
        inbox.setOnClickListener(this);
        back_tv.setOnClickListener(this);
        detailStore.setOnClickListener(this);
        icImage.setOnClickListener(this);
        storeName.setOnClickListener(this);
        more_details.setOnClickListener(this);
        listSubInages.setOnItemClickListener(this);
        llAddToCart.setOnClickListener(this);
        minus.setOnClickListener(this);
        plus.setOnClickListener(this);
        ivFav.setOnClickListener(this);
        ivChat.setOnClickListener(this);
        linearLayoutZoomOut.setOnClickListener(this);
        linearLayoutZoomIn.setOnClickListener(this);
        imageViewZoomIn.setOnClickListener(this);
        imageViewZoomOut.setOnClickListener(this);


        // v.findViewById(R.id.ll_back).setOnClickListener(this);
        v.findViewById(R.id.ic_back).setOnClickListener(this);
        v.findViewById(R.id.ll_add_to_cart).setOnClickListener(this);
        v.findViewById(R.id.store_name).setOnClickListener(this);
        v.findViewById(R.id.textViewVideo).setOnClickListener(this);
    }

    private void initViews(View v) {
        tabLayout = getActivity().findViewById(R.id.tabs);
        if (tabLayout != null)
            //tabLayout.setVisibility(View.VISIBLE);

            relativeLayoutSearchBarStoreFront = getActivity().findViewById(R.id.search_storefront);
        linearLayoutHeaderStoreFront = getActivity().findViewById(R.id.header);
        llListImages = v.findViewById(R.id.ll_list_images);
        rootLayout = v.findViewById(R.id.rootLayout);
        thumbNa = v.findViewById(R.id.tvthumb);
        tvAvailability = v.findViewById(R.id.tvAvailability);
        spinnerSize = v.findViewById(R.id.spinner);
        actual_price = v.findViewById(R.id.actual_price);
        price_afterdiscount = v.findViewById(R.id.price_after_discount);
        textViewVideo = v.findViewById(R.id.textViewVideo);
        storeName = v.findViewById(R.id.storeName);
        more_details = v.findViewById(R.id.moreDetails);
        spinnerColor = v.findViewById(R.id.spinner_color);
        icImage = v.findViewById(R.id.ic_image);
        ivFav = v.findViewById(R.id.iv_fav);
        ivChat = v.findViewById(R.id.iv_chat);
        detailStore = v.findViewById(R.id.details_stor);
        sellerName = v.findViewById(R.id.seller_name);
        listRelatedItems = v.findViewById(R.id.list_related_items);
        listSpecialRelatedItems = v.findViewById(R.id.list_special_related_items);
        listCategoryItems = v.findViewById(R.id.list_category_items);
        txtRelatedItems = v.findViewById(R.id.txt_related);
        txtCategoryItems = v.findViewById(R.id.txt_category);
        txtCategorySpecialItems = v.findViewById(R.id.txt_category_Special);
        productDesc = v.findViewById(R.id.product_desc);
        back_tv = v.findViewById(R.id.ic_back);
        desc = v.findViewById(R.id.desc);
        inbox = v.findViewById(R.id.iv_inbox);
        txtTitle = v.findViewById(R.id.title);
        layAttributes = v.findViewById(R.id.lay_attributes);
        layRelatedItems = v.findViewById(R.id.lay_related_items);
        layCategoryItems = v.findViewById(R.id.lay_cat_items);
        layCategoryspecialItems = v.findViewById(R.id.lay_specialitems);
        llAddToCart = v.findViewById(R.id.ll_add_to_cart);
        //  icProductImage = (ImageView) v.findViewById(R.id.product_image_first);
        list = v.findViewById(R.id.list);
        listSubInages = v.findViewById(R.id.list_subimages);
        mPocketBar = v.findViewById(R.id.pocket);
        layoutFavorite = v.findViewById(R.id.layout_favorite);
        scrollView = v.findViewById(R.id.scrollView);
        llQuantity = v.findViewById(R.id.ll_quantity);
        minus = v.findViewById(R.id.tv_minus);
        plus = v.findViewById(R.id.tv_plus);
        tv_quantity = v.findViewById(R.id.tv_quantity);
        imageViewZoomIn = v.findViewById(R.id.imageViewZoomIn);
        imageViewZoomOut = v.findViewById(R.id.imageViewZoomOut);
        linearLayoutZoomIn = v.findViewById(R.id.linearLayoutZoomIn);
        linearLayoutZoomOut = v.findViewById(R.id.linearLayoutZoomOut);
        v.findViewById(R.id.iv_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareImage();
            }
        });

        reloadDialog = Utils.showReloadDialog(getActivity());

        setListViewScrollable(listSubInages);
    }

    private void shareImage() {
        showProgress();
        new Thread(() -> {
            Bitmap loadedImage = getBitmapFromURL(imageUrl);
            // After getting the result
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    hideProgress();
                    String path =
                            MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), loadedImage, "", null);
                    Uri screenshotUri = Uri.parse(path);
                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_TEXT, "Nice item on ApiTap\n" + Utils.hexToASCII(sellerName.getText().toString())
                            + "\n" + productDesc.getText().toString()
                            + "\n" + "Open in the ApiTap"
                            + "\n" + "http://itemaiodc.com/" + productType + "/" + productId);
                    shareIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
                    shareIntent.setType("image/*");
                    //shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(Intent.createChooser(shareIntent, "Share Via"));

                }
            });
        }).start();

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

            case Constants.PRODUCT_AVAILBLE_MERCHANT:
                hideProgress();
                if (event.getResponse().equals("true")) {
                    state = 0;
                    showAddToCartDialog();
                } else {
                    dialogProductNotAvailable();
                }
                break;

            case Constants.GUEST_ACTIVITY_SUCCESS:

                break;

            case Constants.GUEST_ACTIVITY_TIMEOUT:

                break;

            case Constants.DETAILS_SUCCESS:
                ArrayList<DetailsBean> arrayDetails = ModelManager.getInstance().getDetailsManager().arrayDetails;

                imageUrl = ATPreferences.readString(getActivity(), Constants.KEY_IMAGE_URL)
                        + "banner_" + arrayDetails.get(0).getImage();

                Log.d("TAG", "detailsUrl: " + imageUrl);

                Picasso.get().load(imageUrl).fit()
                        .centerInside()
                        .into(icImage);

                barcode = arrayDetails.get(0).getBarcode();
                limit = arrayDetails.get(0).getLimit();
                specs = Utils.hexToASCII(arrayDetails.get(0).getSpecs());
                sku = arrayDetails.get(0).getSku();
                brandNames = arrayDetails.get(0).getBrand();
                model = arrayDetails.get(0).getModel();
                age21 = arrayDetails.get(0).getAge21();
                age18 = arrayDetails.get(0).getAge18();
                limit_quantity = arrayDetails.get(0).getQuantity();
                productImage = ATPreferences.readString(getActivity(), Constants.KEY_IMAGE_URL) + arrayDetails.get(0).getArrayProductDetails().get(0).getProductImage();
                productName = arrayDetails.get(0).getName();
                storeName.setText(arrayDetails.get(0).getSellerName());
                merchantID = arrayDetails.get(0).getMerchantID();
                txtTitle.setText(/*"By " +*/ arrayDetails.get(0).getName());
                tvAvailability.setText(Utils.checkAvailability(arrayDetails.get(0).getAvailability()));

                priceAfterDiscount = (String.format("%.2f", Double.parseDouble(arrayDetails.get(0).getPrice_AfterDiscount())));
                actualPrice = String.format("%.2f", Double.parseDouble(arrayDetails.get(0).getPrice()));
                String special_price = arrayDetails.get(0).getSpecial_price();

                Log.d("PriceDetail", priceAfterDiscount + "  " + actualPrice);

                if (!special_price.isEmpty()) {
                    actual_price.setText(special_price);
                    actual_price.setTextColor(Color.RED);
                    price_afterdiscount.setVisibility(View.GONE);
                } else if (Double.parseDouble(actualPrice) == Double.parseDouble(priceAfterDiscount) || priceAfterDiscount.equals("0") || priceAfterDiscount.equals("0.00") || Double.parseDouble(priceAfterDiscount) > Double.parseDouble(actualPrice)) {
                    actual_price.setText("$" + Utils.getFormatAmount(actualPrice));
                    price_afterdiscount.setVisibility(View.GONE);
                } else if (actualPrice.equals("0") || actualPrice.equals("0.00")) {
                    price_afterdiscount.setText("$" + Utils.getFormatAmount(priceAfterDiscount));
                    actual_price.setVisibility(View.GONE);
                } else if (Double.parseDouble(actualPrice) > Double.parseDouble(priceAfterDiscount)) {
                    price_afterdiscount.setText("$" + Utils.getFormatAmount(priceAfterDiscount));
                    actual_price.setText("$" + Utils.getFormatAmount(actualPrice));
                    actual_price.setTextColor(getResources().getColor(R.color.colorRed));
                    actual_price.setPaintFlags(actual_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                }

                String resultString = Utils.hexToASCII(arrayDetails.get(0).getProductDesc());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    productDesc.setText(Html.fromHtml(resultString, Html.FROM_HTML_MODE_LEGACY));
                    desc.setText(Html.fromHtml(resultString, Html.FROM_HTML_MODE_LEGACY));
                } else {
                    productDesc.setText(Html.fromHtml(resultString));
                    desc.setText(Html.fromHtml(resultString));
                }

                sellerName.setText(arrayDetails.get(0).getTitle());

                //Picasso.get().load(productImage).into(icProductImage);
                ArrayList<Sizedata> arraySize = arrayDetails.get(0).getSizedata();
                if (arraySize != null && arraySize.size() > 0) {
                    llQuantity.setVisibility(View.VISIBLE);
                    for (int i = 0; i < arraySize.size(); i++) {
                        ArrayList<SizeBean> arrData = arraySize.get(i).getSizeArray();
                        ArrayList<String> nameArray = new ArrayList<>();
                        int size = arrData.size();
                        for (int j = 0; j < size; j++) {
                            nameArray.add(arrData.get(j).getSize());
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, nameArray);
                        if (i == 0) {
                            spinnerColor.setAdapter(adapter); // this will set list of values to spinner
                        } else if (i == 1) {
                            spinnerSize.setAdapter(adapter); // this will set list of values to spinner
                        }
                    }

                } else {
                    spinnerSize.setVisibility(View.GONE);
                    spinnerColor.setVisibility(View.GONE);
                    llQuantity.setVisibility(View.GONE);
                    layAttributes.setVisibility(View.GONE);
                }
                ArrayList<ProductDetailsBean> arrayProducts = arrayDetails.get(0).getArrayProductDetails();

                if (sampleImagesArrayList.size() > 0) {
                    if (adapterSubImages != null)
                        adapterSubImages.notifyDataSetChanged();
                }

                if (arrayProducts != null && arrayProducts.size() > 0) {
                    if (arrayProducts.size() == 1) {
                        thumbNa.setVisibility(View.VISIBLE);
                        listSubInages.setVisibility(View.GONE);
                    } else {
                        sampleImagesArrayList = new ArrayList<>();
                        for (int k = 0; k < arrayProducts.size(); k++) {
                            sampleImagesArrayList.add(ATPreferences.readString(getActivity(), Constants.KEY_IMAGE_URL) +
                                    arrayDetails.get(0).getArrayProductDetails().get(k).getProductImage());
                        }

                        if (adapterSubImages != null)
                            adapterSubImages.notifyDataSetChanged();
                        else {
                            adapterSubImages = new SubImagesAdapter(getActivity(),
                                    0, sampleImagesArrayList, productImage);
                            listSubInages.setAdapter(adapterSubImages);
                            listSubInages.setVisibility(View.VISIBLE);
                            thumbNa.setVisibility(View.GONE);
                        }
                    }
                }

                initMoreDetailsDialog();
                initAddToCartDialog();

                break;


            case Constants.RELATED_DETAILS:

                relatedArray = ModelManager.getInstance().getDetailsManager().relatedDetailsBean.getRESULT().get(0).getRESULT();

                if (relatedArray != null && relatedArray.size() > 0) {
                    listRelatedItems.setAdapter(mAdapterRelatedItems);
                    txtRelatedItems.setVisibility(View.VISIBLE);
                    layRelatedItems.setVisibility(View.VISIBLE);
                } else {
                    // layRelatedItems.setVisibility(View.GONE);
                    //  txtRelatedItems.setText("People Also View");
                    //txtCategorySpecialItems.setText("People Also View");
                    //  txtCategoryItems.setText("People Also View");

                    layRelatedItems.setVisibility(View.GONE);
                }

                break;

            case Constants.RELATED_SPECIAL_DETAILS:

                specialArrayRelated = ModelManager.getInstance().getDetailsManager().specialItemBean.getRESULT();
//                if (specialArrayRelated.size() > 0)
//                    Log.d("relatedArraySizej", specialArrayRelated.get(0).getRESULT().get(0).getIA().get(0).get_114_9() + "  ");

                if (specialArrayRelated != null && specialArrayRelated.get(0).getRESULT().get(0).getIA().size() > 0) {
                    listSpecialRelatedItems.setAdapter(mAdapterSpecialRealted);
                    //  txtRelatedItems.setVisibility(View.VISIBLE);
                    txtCategorySpecialItems.setVisibility(View.VISIBLE);
                    layCategoryspecialItems.setVisibility(View.VISIBLE);

                    layRelatedItems.setVisibility(View.GONE);
                } else {
                    // layRelatedItems.setVisibility(View.GONE);
                    // layRelatedItems.setVisibility(View.GONE);
                    // layCategoryspecialItems.setVisibility(View.GONE);
                }

                break;

            case Constants.CATEGORY_DETAILS:
                clearfocus();
                mPocketBar.setVisibility(View.GONE);
                catArray = ModelManager.getInstance().getDetailsManager().categoryDetailsBean.getRESULT();
                if (catArray.get(0).get12083() != null && catArray.size() > 0) {
                    listCategoryItems.setAdapter(mPeopleAlsoView);
                    //txtCategoryItems.setVisibility(View.VISIBLE);
                    txtCategoryItems.setVisibility(View.VISIBLE);

                } else {
                    //  layCategoryItems.setVisibility(View.GONE);
                    layCategoryItems.setVisibility(View.GONE);
                    // layRelatedItems.setVisibility(View.GONE);
                }
                break;

            case Constants.ADD_TO_FAVORITE_SUCCESS:
                //    if (isFavorite.equals("1"))
                clearfocus();
                mPocketBar.setVisibility(View.GONE);
                isFavouriteboolean = true;
                ivFav.setBackgroundResource(R.drawable.ic_icon_fav);
//                else
//                    ivFav.setBackgroundResource(R.drawable.ic_icon_fav_gray);
                break;


            case Constants.SHOPPING_SUCCESS:
                clearfocus();
                mPocketBar.setVisibility(View.GONE);
                // Toast.makeText(getActivity(), "Item added to cart", Toast.LENGTH_SHORT).show();
                showSuccessdialog();
                break;
            case Constants.PRODUCT_VIDEO_EXISTS:
                videoUrl = event.getResponse();
                sampleImagesArrayList.add(videoUrl);
                if (adapterSubImages != null)
                    adapterSubImages.notifyDataSetChanged();
                else {
                    adapterSubImages = new SubImagesAdapter(getActivity(),
                            0, sampleImagesArrayList, productImage);
                    listSubInages.setAdapter(adapterSubImages);
                    listSubInages.setVisibility(View.VISIBLE);
                    thumbNa.setVisibility(View.GONE);

                }
                //  textViewVideo.setVisibility(View.VISIBLE);
                break;
            case -1:
                clearfocus();
                mPocketBar.setVisibility(View.GONE);
                relaodDialogShow();
                break;

            case Constants.GET_FAVOURITE_SUCCESS:

                final ArrayList<String> favdetailsbeanArrayList = ModelManager.getInstance().getDetailsManager().hexIds;

                if (favdetailsbeanArrayList.contains(productId)) {
                    ivFav.setBackgroundResource(R.drawable.ic_icon_fav);
                    isFavouriteboolean = true;
                } else {
                    isFavouriteboolean = false;
                    ivFav.setBackgroundResource(R.drawable.ic_icon_fav_gray);
                }
                break;
            case Constants.GET_ITEM_OPTIONS_SUCCESS:
                productOptionsArrayList = ModelManager.getInstance().getDetailsManager().arrayOptions1;

                initMoreDetailsDialog();
                initAddToCartDialog();

                if (!productOptionsArrayList.isEmpty()) {
                    showProgress();
                    getItemOptionChoices();
                }
                break;
            case Constants.GET_OPTIONS_CHOICES_1_SUCCESS:
                productOptionChoices1ArrayList = ModelManager.getInstance().getProductOptions().productOptionsBeans1;
                getOptionChoicesOneAdapter = new GetOption1(mActivity, productOptionChoices1ArrayList);

                spinnerCartOption1.setAdapter(getOptionChoicesOneAdapter);
                spinnerInfoOption1.setAdapter(getOptionChoicesOneAdapter);

                if (productOptionsArrayList.size() == 1)
                    hideProgress();
                break;
            case Constants.GET_OPTIONS_CHOICES_2_SUCCESS:
                productOptionChoices2ArrayList = ModelManager.getInstance().getProductOptions().productOptionsBeans2;
                getOptionChoicesTwoAdapter = new GetOption2(mActivity, productOptionChoices2ArrayList);

                spinnerCartOption2.setAdapter(getOptionChoicesTwoAdapter);
                spinnerInfoOption2.setAdapter(getOptionChoicesTwoAdapter);

                hideProgress();
                break;

            case Constants.REMOVE_FAVOURITE_SUCCESS:
                ivFav.setBackgroundResource(R.drawable.ic_icon_fav_gray);
                isFavouriteboolean = false;
                //  ModelManager.getInstance().getDetailsManager().getDetails(getActivity(), Operations.makeJsonGetItems(getActivity(), productId, ATPreferences.readString(mActivity, Constants.KEY_USERID)), false);

                break;

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
                reloadDialog.dismiss();
                reloadFragment();

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

    public void reloadFragment() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
    }


    private void setListViewScrollable(final ListView list) {
        list.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login:
                String email = editEmail.getText().toString();
                String password = editPassword.getText().toString();
                if (Utils.isEmpty(email) || Utils.isEmpty(password)) {
                    //   Toast.makeText(mActivity, "Please fill all details", Toast.LENGTH_SHORT).show();
                    Utils.baseshowFeedbackMessage(mActivity, rootLayout, "Please fill all details");
                } else {
                    ModelManager.getInstance().getLoginManager().getLogin(mActivity, Operations.makeJsonUserLogin(mActivity, email, password));
                }
                break;
            case R.id.iv_inbox:
                if (isGuest) {
                    showGuestDialog();
                } else {
//                    displayView(new FragmentMessages(), Constants.TAG_MESSAGEPAGE, new Bundle());
                    Bundle bundle = new Bundle();
                    bundle.putString("productId", productId);
                    bundle.putString("merchantId", merchantID);
                    bundle.putString("productName", productName);
                    bundle.putString("merchantName", storeName.getText().toString());
                    bundle.putString("storeName", storeName.getText().toString());
                    bundle.putStringArrayList("locationList", ModelManager.getInstance().getDetailsManager().arrayListLocation);
                    bundle.putStringArrayList("locationIdList", ModelManager.getInstance().getDetailsManager().arrayListLocationId);
//                    ((HomeActivity) getActivity()).displayView(new FragmentMessageDetail(),
//                            Constants.MessageDetailPage, bundle);

                    ((HomeActivity) getActivity()).displayView(new SendMessage(), "Send Message", bundle);
                    break;

                }
                break;

            case R.id.moreDetails:
                // moreDetailsdialog();
                if (!moreDetailDialog.isShowing()) {
                    moreDetailDialog.show();
                    moreDetailDialog.getWindow().setDimAmount(0.5f);
                    moreDetailDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                }
                break;
            case R.id.imageViewZoomIn:
            case R.id.linearLayoutZoomIn:
                if (fontSize < 20) {
                    fontSize++;
                    productDesc.setTextSize(fontSize);
                }
                break;
            case R.id.imageViewZoomOut:
            case R.id.linearLayoutZoomOut:
                if (fontSize > 14) {
                    fontSize--;
                    productDesc.setTextSize(fontSize);
                }
                break;
            case R.id.txtForgotPassword:
                Intent i = new Intent(getActivity(), ForgotPasswordActivity.class);
                startActivity(i);
                break;
            case R.id.textViewVideo:
                Bundle extras = new Bundle();
                extras.putString("imagebitmap", "");
                extras.putString("previousClass", "ProductDetail");
                extras.putString("video", videoUrl);
                extras.putString("merchant", storeName.getText().toString());
                extras.putString("adName", productName);
                extras.putString("desc", productDesc.getText().toString());
                extras.putString("id", "");
                extras.putString("ad_id", "");
                extras.putInt("adpos", 0);
                extras.putLong("vidpos", 0);
                ((HomeActivity) getActivity()).displayView(new FragmentAdDetail(), Constants.TAG_AD_DETAIL, extras);

                break;
            case R.id.iv_chat:
                Bundle bundle = new Bundle();
                bundle.putString("productId", productId);
                bundle.putString("merchantId", merchantID);
                bundle.putString("storeName", storeName.getText().toString());
                ((HomeActivity) getActivity()).displayView(new SendMessage(), "Send Message", bundle);
                break;

            case R.id.iv_fav:
                if (isGuest) {
                    showGuestDialog();
                } else if (!isFavouriteboolean) {
                    mPocketBar.setVisibility(View.VISIBLE);
                    getfocus();
                    ModelManager.getInstance().getAddToFavoriteManager().addToFavorite(mActivity, Operations.makeJsonAddToFavorite(mActivity, productId));
                } else {
                    ModelManager.getInstance().getFavouriteManager().removeFavourites(mActivity, Operations.makeJsonRemoveFavourite(mActivity, productId), Constants.REMOVE_FAVOURITE_SUCCESS);
                }
                break;

            case R.id.ic_image:
                if (productImage != null) {
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("image", productImage);
                    bundle1.putString("storeName", storeName.getText().toString());
                    bundle1.putInt("position", related_pos);
                    bundle1.putString("name", txtTitle.getText().toString());
                    bundle1.putSerializable("detailsArray", ModelManager.getInstance().getDetailsManager().arrayDetails.get(0).getArrayProductDetails());
                    ((HomeActivity) getActivity()).displayAddView(new FragmentZoomImage(), Constants.TAG_DETAILSPAGE, Constants.TAG_ZOOM_IMAGE, bundle1);
                }
                break;

            case R.id.storeName:
                //   ATPreferences.putBoolean(mActivity, Constants.HEADER_STORE, true);
                // ATPreferences.putString(mActivity, Constants.MERCHANT_ID, merchantID);
                //startActivity(new Intent(mActivity, HomeActivity.class));
//                Bundle bundle = new Bundle();
//                bundle.putBoolean("header", true);
//                displayView(new FragmentHome(), "Home", bundle);

                break;
            case R.id.details_stor:
                //    ATPreferences.putBoolean(mActivity, Constants.HEADER_STORE, true);
                //  ATPreferences.putString(mActivity, Constants.MERCHANT_ID, merchantID);
                //startActivity(new Intent(mActivity, HomeActivity.class));
//                Bundle bundle = new Bundle();
//                bundle.putBoolean("header", true);
//                displayView(new FragmentHome(), "Home", bundle);

                Log.d("merchantIddd", merchantID);
                Bundle b = new Bundle();
                b.putBoolean(Constants.HEADER_STORE, true);
                b.putString(Constants.MERCHANT_ID, merchantID);

                ATPreferences.putBoolean(getActivity(), Constants.HEADER_STORE, true);
                ATPreferences.putString(getActivity(), Constants.MERCHANT_ID, merchantID);
                ATPreferences.putString(getActivity(), Constants.MERCHANT_CATEGORY, "");

                ((HomeActivity) getActivity()).displayView(new FragmentStoreFront(), Constants.TAG_STORESFRONTPAGE, b);


                break;


            case R.id.store_name:
           /*     Intent intents = new Intent(getActivity(), MerchantStoreDetails.class);
                intents.putExtra("merchantId", merchantID);
                startActivity(intents);*/
                linearLayoutHeaderStoreFront.setVisibility(View.GONE);
                relativeLayoutSearchBarStoreFront.setVisibility(View.GONE);
                Bundle bundle1 = new Bundle();
                bundle1.putString("merchantId", merchantID);
                ((HomeActivity) getActivity()).displayView(new FragmentStoreDetails(), Constants.TAG_STORE_DETAILS, bundle1);

                break;


//            case R.id.ic_back:
//                Log.d("fragmentcount", getFragmentManager().getBackStackEntryCount() + "");
//                if (getFragmentManager().getBackStackEntryCount() > 0) {
//                    getFragmentManager().popBackStack();
//                } else {
//                    getActivity().onBackPressed();
//                }
//
//                break;

            case R.id.ic_back:
                //((HomeActivity) getActivity()).onBackPressed();
                Log.d("fragmentcount", getFragmentManager().getBackStackEntryCount() + "");
                if (getFragmentManager().getBackStackEntryCount() > 0) {
                    getFragmentManager().popBackStack();
                    return;
                } else {
                    getActivity().onBackPressed();
                }
//                if (getFragmentManager().getBackStackEntryCount() == 1) {
//                    if (flag != null) {
//                        if (flag.equals("adDetail"))
//                            AdDetailActivity.viewFrame();
//                        else if (flag.equals("search"))
//                            SearchItemActivity.viewFrame();
//                    } else
//                        getActivity().onBackPressed();
//
//                }
                break;

            case R.id.tv_minus:
                int quantity = Integer.parseInt(tv_quantity.getText().toString());
                if (quantity > 1)
                    quantity--;

                tv_quantity.setText(String.valueOf(quantity));
                break;

            case R.id.tv_plus:
                int quan = Integer.parseInt(tv_quantity.getText().toString());
                quan++;
                tv_quantity.setText(String.valueOf(quan));
                break;

            case R.id.ll_add_to_cart:
//                if (isGuest) {
//                    showGuestDialog();
//                } else {
                if (dateOfBirthValidate())
                    callValidateMerchantProduct();
                //showdialog();
                //}
                // mPocketBar.setVisibility(View.VISIBLE);
                //ModelManager.getInstance().getShoppingCartManager().addItemTOCart(mActivity, Operations.makeJsonAddToCartItems(mActivity, productQuantity, productId));

                break;
        }
    }

    private boolean dateOfBirthValidate() {
        //  age18 = "true";
        if (age18.equals("true") && !Utils.isOlder(getActivity(), 18)) {
            Utils.ageFilterDialog(getActivity(), 18);
            return false;
        } else if (age21.equals("true") && !Utils.isOlder(getActivity(), 21)) {
            Utils.ageFilterDialog(getActivity(), 21);
            return false;
        }
        return true;
    }

    private void callValidateMerchantProduct() {
        showProgress();
        ModelManager.getInstance().getMerchantManager().validMerchantByProduct(getActivity(), Operations.
                validMerchantByProduct(getActivity(), productId));
    }

    public void dialogProductNotAvailable() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.gravity = Gravity.CENTER;
        dialog.setContentView(R.layout.dialog_invalid_product);
        Button done = (Button) dialog.findViewById(R.id.btn_ok);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setDimAmount(0.5f);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

    }


    public void initAddToCartDialog() {
        ArrayList<String> quantityList = new ArrayList<String>();
        addToCartDialog = new Dialog(mActivity, R.style.AppTheme_Dialog_MyDialogTheme);
        addToCartDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(addToCartDialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
//        lp.dimAmount=0.5f; // Dim level. 0.0 - no dim, 1.0 - completely opaque
        addToCartDialog.setContentView(R.layout.customdialog_checkout);
        TextView minusd = addToCartDialog.findViewById(R.id.tv_minus);
        TextView plusd = addToCartDialog.findViewById(R.id.tv_plus);
        final TextView tv_quantityd = addToCartDialog.findViewById(R.id.tv_quantity);
        final TextView tv_option = addToCartDialog.findViewById(R.id.option);
        final TextView tv_option1 = addToCartDialog.findViewById(R.id.option1);
        editTextSpecialInstructions = addToCartDialog.findViewById(R.id.editTextSpecialInstructions);
        final Button submit = addToCartDialog.findViewById(R.id.submitdailog);
        final Button cancel = addToCartDialog.findViewById(R.id.cancel);
        RelativeLayout relativeLayoutChoiceOne = addToCartDialog.findViewById(R.id.realative);
        RelativeLayout relativeLayoutChoiceSecond = addToCartDialog.findViewById(R.id.realativesecond);
        spinnerCartOption1 = addToCartDialog.findViewById(R.id.editoption);
        spinnerCartOption2 = addToCartDialog.findViewById(R.id.editoption2);
        Spinner spinnerQty = addToCartDialog.findViewById(R.id.spinnerQty);
        RelativeLayout rlSpinnerQty = addToCartDialog.findViewById(R.id.rlSpinnerQty);
        LinearLayout llQuantity = addToCartDialog.findViewById(R.id.ll_quantity);

        if (productOptionsArrayList.isEmpty())
            relativeLayoutChoiceOne.setVisibility(View.GONE);
        else
            relativeLayoutChoiceOne.setVisibility(View.VISIBLE);


        if (limit.equals("0") || limit.isEmpty()) {
            rlSpinnerQty.setVisibility(View.GONE);
            llQuantity.setVisibility(View.VISIBLE);
        } else {
            rlSpinnerQty.setVisibility(View.VISIBLE);
            llQuantity.setVisibility(View.GONE);

            for (int i = 1; i <= Integer.parseInt(limit); i++) {
                quantityList.add(String.valueOf(i));
            }

            spinnerQty.setAdapter(new AdapterQtySpinner(mActivity, quantityList));
        }


        spinnerCartOption1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String optionId = productOptionChoices1ArrayList.get(i).getChoice_id();
                str_option_Id = Utils.getElevenDigitId(optionId);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        spinnerCartOption2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (relativeLayoutChoiceSecond.getVisibility() == View.VISIBLE) {
                    String optionId = productOptionChoices2ArrayList.get(i).getChoice_id();
                    str_option_Id2 = Utils.getElevenDigitId(optionId);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinnerQty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                tv_quantityd.setText(quantityList.get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        if (productOptionsArrayList.size() == 1) {
            tv_option.setText(productOptionsArrayList.get(0).getName_option() + "");
            option_Id = Utils.getElevenDigitId(productOptionsArrayList.get(0).getOption_id());

        } else if (productOptionsArrayList.size() >= 2) {
            relativeLayoutChoiceSecond.setVisibility(View.VISIBLE);

            tv_option.setText(productOptionsArrayList.get(0).getName_option() + "");
            option_Id = Utils.getElevenDigitId(productOptionsArrayList.get(0).getOption_id());

            tv_option1.setText(productOptionsArrayList.get(1).getName_option() + "");
            option_Id2 = Utils.getElevenDigitId(productOptionsArrayList.get(1).getOption_id());
        }


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                state = 0;
                addToCartDialog.dismiss();
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
                getfocus();
                ModelManager.getInstance().getShoppingCartManager().addItemTOCart(mActivity,
                        Operations.makeJsonAddToCartItems(mActivity, tv_quantityd.getText().toString(), productId,
                                merchantID, str_option_Id, str_option_Id2, editTextSpecialInstructions.getText().toString()));

                addToCartDialog.dismiss();
            }
        });

//        addToCartDialog.show();
//        //dialog.getWindow().setAttributes(lp);
//        addToCartDialog.getWindow().setDimAmount(0.5f);
//        addToCartDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

    }

    public void showAddToCartDialog() {
        if (addToCartDialog != null && !addToCartDialog.isShowing()) {
            addToCartDialog.show();
            addToCartDialog.getWindow().setDimAmount(0.5f);
            addToCartDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        }
    }

    public void initMoreDetailsDialog() {
        boolean isNoneGreen = true;
        moreDetailDialog = new Dialog(mActivity, R.style.AppTheme_Dialog_MyDialogTheme);
        moreDetailDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(moreDetailDialog.getWindow().getAttributes());
        lp.gravity = Gravity.CENTER;
        moreDetailDialog.setContentView(R.layout.more_details_dialog);
        Button done = moreDetailDialog.findViewById(R.id.close);
        TextView modelNumber = moreDetailDialog.findViewById(R.id.model_number);
        LinearLayout ll_model = moreDetailDialog.findViewById(R.id.model_ll);
        LinearLayout ll_brand = moreDetailDialog.findViewById(R.id.brand_ll);
        LinearLayout linearLayoutChoiceOne = moreDetailDialog.findViewById(R.id.relativeLayoutSelectType);
        RelativeLayout relativeLayoutChoiceSecond = moreDetailDialog.findViewById(R.id.realativesecond);
        TextView skuId = moreDetailDialog.findViewById(R.id.pos_sku);
        LinearLayout ll_sku = moreDetailDialog.findViewById(R.id.sku_ll);
        TextView barCode = moreDetailDialog.findViewById(R.id.bar_code_number);
        TextView quantity = moreDetailDialog.findViewById(R.id.quantity);
        TextView specification = moreDetailDialog.findViewById(R.id.specs);
        TextView brandName = moreDetailDialog.findViewById(R.id.brand_name);
        WebView webView = moreDetailDialog.findViewById(R.id.webView);
        CheckBox checkBox18 = moreDetailDialog.findViewById(R.id.eightyears);
        CheckBox checkBox21 = moreDetailDialog.findViewById(R.id.twoyears);
        final TextView textViewOptionOne = moreDetailDialog.findViewById(R.id.option);
        final TextView textViewOptionTwo = moreDetailDialog.findViewById(R.id.option1);
        spinnerInfoOption1 = moreDetailDialog.findViewById(R.id.editoption);
        spinnerInfoOption2 = moreDetailDialog.findViewById(R.id.editoption2);

        if (productOptionsArrayList.isEmpty())
            linearLayoutChoiceOne.setVisibility(View.GONE);
        else
            linearLayoutChoiceOne.setVisibility(View.VISIBLE);


        LinearLayout linear_layoutQty = moreDetailDialog.findViewById(R.id.limited_qty_ll);
        LinearLayout linear_layout18 = moreDetailDialog.findViewById(R.id.eighteenplus_ll);
        LinearLayout linear_layout21 = moreDetailDialog.findViewById(R.id.twentyOneplus_ll);
        LinearLayout linear_layoutnone = moreDetailDialog.findViewById(R.id.none_ll);

        if (productOptionsArrayList.size() == 1) {
            textViewOptionOne.setText(productOptionsArrayList.get(0).getName_option() + "");
            option_Id = Utils.getElevenDigitId(productOptionsArrayList.get(0).getOption_id());
        } else if (productOptionsArrayList.size() >= 2) {
            relativeLayoutChoiceSecond.setVisibility(View.VISIBLE);

            textViewOptionOne.setText(productOptionsArrayList.get(0).getName_option() + "");
            option_Id = Utils.getElevenDigitId(productOptionsArrayList.get(0).getOption_id());

            textViewOptionTwo.setText(productOptionsArrayList.get(1).getName_option() + "");
            option_Id2 = Utils.getElevenDigitId(productOptionsArrayList.get(1).getOption_id());
        }

        spinnerInfoOption1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String optionId = productOptionChoices1ArrayList.get(i).getChoice_id();
                str_option_Id = Utils.getElevenDigitId(optionId);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        spinnerInfoOption2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (relativeLayoutChoiceSecond.getVisibility() == View.VISIBLE) {
                    String optionId = productOptionChoices2ArrayList.get(i).getChoice_id();
                    str_option_Id2 = Utils.getElevenDigitId(optionId);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        //specification.setMovementMethod(new ScrollingMovementMethod());
        checkBox21.setClickable(false);
        checkBox18.setClickable(false);

        if (limit_quantity == null || limit_quantity.equals("0"))
            quantity.setText("No");
        else {
            quantity.setText(limit_quantity);
            isNoneGreen = false;
            linear_layoutQty.setBackground(getResources().getDrawable(R.drawable.bg_red_radius_low));
        }
        if (sku == null || sku.isEmpty())
            ll_sku.setVisibility(View.GONE);
            //skuId.setText("N/A");
        else
            skuId.setText(sku);

        if (brandNames == null || brandNames.isEmpty())
            ll_brand.setVisibility(View.GONE);
            //skuId.setText("N/A");
        else
            brandName.setText(brandNames);

        if (barcode == null && barcode.isEmpty())
            barCode.setText("N/A");
        else
            barCode.setText(barcode);

        if (specs == null || specs.isEmpty())
            //  specification.setText("N/A");
            Log.d("dsd", "dsd");
        else {
            String sepcifications = String.valueOf(Html.fromHtml(specs));
            //specification.setText(sepcifications);
//            sepcifications  = specification.getText().toString().replace("Â","");
            // specification.setText(Html.fromHtml(specs,Html.FROM_HTML_MODE_COMPACT).toString());
            //  webView.loadDataWithBaseURL(null, specs, "text/html", "utf-8", null);
            webView.loadData(specs, "text/html", null);

        }
        if (model == null || model.isEmpty())
            //modelNumber.setText("N/A");
            ll_model.setVisibility(View.GONE);
        else
            modelNumber.setText(model);

        if (age18 != null && age18.equals("true")) {
            checkBox18.setChecked(true);
            isNoneGreen = false;
            linear_layout18.setBackground(getResources().getDrawable(R.drawable.bg_red_radius_low));
        } else
            checkBox18.setVisibility(View.GONE);

        if (age21 != null && age21.equals("true")) {
            isNoneGreen = false;
            checkBox21.setChecked(true);
            linear_layout21.setBackground(getResources().getDrawable(R.drawable.bg_red_radius_low));
        } else
            checkBox21.setVisibility(View.GONE);
        //  checkBox21.setText("The product is not available to people over 21 years of age");

        if (isNoneGreen)
            linear_layoutnone.setBackground(getResources().getDrawable(R.drawable.back_blue_border_green_));

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moreDetailDialog.dismiss();
            }
        });

        //  moreDetailDialog.show();
        //dialog.getWindow().setAttributes(lp);
        moreDetailDialog.getWindow().setDimAmount(0.5f);
        moreDetailDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

    }

    private void getItemOptionChoices() {
        if (productOptionsArrayList.size() == 1) {
            option_Id = Utils.getElevenDigitId(productOptionsArrayList.get(0).getOption_id());
            ModelManager.getInstance().getProductOptions().getOption1(getActivity(), Operations.makeJsonGetOptions2(getActivity(), option_Id), 0);


        } else if (productOptionsArrayList.size() >= 2) {
            option_Id = Utils.getElevenDigitId(productOptionsArrayList.get(0).getOption_id());
            ModelManager.getInstance().getProductOptions().getOption1(getActivity(), Operations.makeJsonGetOptions2(getActivity(), option_Id), 0);

            option_Id2 = Utils.getElevenDigitId(productOptionsArrayList.get(1).getOption_id());
            ModelManager.getInstance().getProductOptions().getOption2(getActivity(), Operations.makeJsonGetOptions2(getActivity(), option_Id2), 0);

        }

    }

    public void showSuccessdialog() {
        final Dialog dialog = new Dialog(mActivity, R.style.AppTheme_Dialog_MyDialogTheme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.setContentView(R.layout.customdialogcart);
        Button btncontinue = dialog.findViewById(R.id.continueshoping);
        Button btncheckout = dialog.findViewById(R.id.checkout);

        btncontinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btncheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((HomeActivity) getActivity()).displayView(new ShoppingCartFragment(), Constants.TAG_SHOPPING, new Bundle());
                dialog.dismiss();

            }
        });

        dialog.show();
        // dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setDimAmount(0.5f);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        ArrayList<ProductDetailsBean> arrayDetails = ModelManager.getInstance().getDetailsManager().arrayDetails.get(0).getArrayProductDetails();
        if (sampleImagesArrayList.get(i).endsWith("mp4") ||
                sampleImagesArrayList.get(i).endsWith("mpeg")
                || sampleImagesArrayList.get(i).endsWith("avi") || sampleImagesArrayList.get(i).endsWith("mkv")) {

            textViewVideo.performClick();
            return;
        }
        if (arrayDetails.size() > 0) {
            thumbNa.setVisibility(View.GONE);
        }

        imageUrl = ATPreferences.readString(getActivity(), Constants.KEY_IMAGE_URL) + arrayDetails.get(i).getProductImage();
        //String id = arrayDetails.get(i).getProductId();
        related_pos = i;
        // ModelManager.getInstance().getProductOptions().getOption1(getActivity(), Operations.makeJsonGetOptions(getActivity(), id));

        Log.d("TAG", "onItemClick: " + imageUrl);
        Picasso.get().load(imageUrl).into(icImage);

    }


    public void clearfocus() {
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    public void getfocus() {
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    public void showGuestDialog() {

        final Dialog dialog = new Dialog(getActivity(), R.style.AppTheme_Dialog_MyDialogTheme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.setContentView(R.layout.guest_login);

        Button login = dialog.findViewById(R.id.continueshoping);
        Button cancel = dialog.findViewById(R.id.cancel);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        dialog.show();
        //   dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setDimAmount(0.5f);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        //getDialogView(dialog);
        //viewsVisibility(dialog);

    }

    public void DisplayFragment(String flag, Bundle bundle) {
        if (flag == null) {
            flag = "home";
        }
//        switch (flag) {
//            case "search":
//                ((SearchItemActivity) getActivity()).displayView(new FragmentItemDetails(), Constants.TAG_DETAILSPAGE, bundle);
//                break;
//            case "adDetail":
//                ((AdDetailActivity) getActivity()).displayView(new FragmentItemDetails(), Constants.TAG_DETAILSPAGE, bundle);
//                break;
//            default:
//                ((HomeActivity) getActivity()).displayView(new FragmentItemDetails(), Constants.TAG_DETAILSPAGE, bundle);
//                break;
//        }
        ((HomeActivity) getActivity()).displayView(new FragmentItemDetails(), Constants.TAG_DETAILSPAGE, bundle);
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
            TextView txt_type = vi.findViewById(R.id.txt_type);
            txt_type.setText(Utils.hexToASCII(response.get(position).getChoice_name()));
            return vi;
        }
    }

    public class GetOption2 extends BaseAdapter {
        private List<ProductOptionsBean> response;
        private LayoutInflater inflater = null;
        private Activity activity;

        public GetOption2(Activity a, List<ProductOptionsBean> response) {
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
            TextView txt_type = vi.findViewById(R.id.txt_type);
            TextView txt_price = vi.findViewById(R.id.txt_price);
            txt_price.setVisibility(View.VISIBLE);
            double price = Double.parseDouble(response.get(position).getChoice_price());
            txt_price.setText("$" + (String.format("%.2f", price)));


            txt_type.setText(Utils.hexToASCII(response.get(position).getChoice_name()));
            return vi;
        }

    }

    private BaseAdapter mPeopleAlsoView = new BaseAdapter() {

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.row_horizontal_test, parent, false);
            }

            ImageView imageView = convertView.findViewById(R.id.image);
            TextView description = convertView.findViewById(R.id.description);
            ImageView eye = convertView.findViewById(R.id.eye);
            String isSeen = catArray.get(position).get1149();
            LinearLayout rlSinglePrice = convertView.findViewById(R.id.rel_single_price);
            LinearLayout rlTwoPrice = convertView.findViewById(R.id.rl_two_price);
            TextView price = convertView.findViewById(R.id.price);
            TextView actualPrice = convertView.findViewById(R.id.actual_price);
            TextView priceAfterDiscount = convertView.findViewById(R.id.price_after_discount);

            if (isSeen != null && isSeen.equalsIgnoreCase("false")) {
                eye.setBackgroundResource(R.drawable.green_seen);
            } else {
                eye.setVisibility(View.GONE);
            }
            try {
                description.setText(Utils.hexToASCII(catArray.get(position).get12083()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            String ActualPrice = String.format("%.2f", Double.parseDouble(catArray.get(position).get11498()));
            String DiscountPrice = String.format("%.2f", Double.parseDouble(catArray.get(position).get122158()));


            if (Double.parseDouble(ActualPrice) == Double.parseDouble(DiscountPrice) || DiscountPrice.equals("0") || DiscountPrice.equals("0.00")) {
                rlSinglePrice.setVisibility(View.VISIBLE);
                rlTwoPrice.setVisibility(View.GONE);
                actualPrice.setText("$" + Utils.getFormatAmount(ActualPrice));
            } else if (ActualPrice.equals("0") || ActualPrice.equals("0.00")) {
                rlTwoPrice.setVisibility(View.VISIBLE);
                rlSinglePrice.setVisibility(View.GONE);
                priceAfterDiscount.setText("$" + Utils.getFormatAmount(DiscountPrice));
                actualPrice.setVisibility(View.GONE);
            } else if (Double.parseDouble(ActualPrice) > Double.parseDouble(DiscountPrice)) {
                rlTwoPrice.setVisibility(View.VISIBLE);
                rlSinglePrice.setVisibility(View.VISIBLE);
                priceAfterDiscount.setText("$" + Utils.getFormatAmount(DiscountPrice));
                priceAfterDiscount.setGravity(Gravity.END);
                actualPrice.setGravity(Gravity.START);
                actualPrice.setText("$" + Utils.getFormatAmount(ActualPrice) + "   ");
                actualPrice.setTextColor(getResources().getColor(R.color.colorRed));
                actualPrice.setPaintFlags(actualPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getfocus();
                    productId = Utils.lengtT(11, catArray.get(position).get114144());
                    ModelManager.getInstance().setProductSeen().setProductSeen(getActivity(), Operations.makeProductSeen(getActivity(), productId));
                    mPocketBar.setVisibility(View.VISIBLE);

                    String productType = catArray.get(position).get114112();
                    Bundle bundle = new Bundle();
                    bundle.putString("productId", productId);
                    bundle.putString("productType", productType);
                    bundle.putString("flag", flag);
                    DisplayFragment(flag, bundle);
                }
            });

            // Picasso.get().load(ATPreferences.readString(getActivity(), Constants.KEY_IMAGE_URL) + catArray.get(position).get121170()).into(imageView);
//            Glide.with(getActivity()).load(ATPreferences.readString(getActivity(), Constants.KEY_IMAGE_URL) + catArray.get(position).get121170())
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .placeholder(R.drawable.splash_screen_new)
//                    .into(imageView);

            Picasso.get()
                    .load(ATPreferences.readString(mActivity, Constants.KEY_IMAGE_URL) + "_t_" + catArray.get(position)
                            .get121170())
                    .into(imageView);


            return convertView;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public int getCount() {
            return catArray.size();
        }
    };
    private BaseAdapter mAdapterRelatedItems = new BaseAdapter() {

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.row_horizontal_test, parent, false);
            }

            ImageView imageView = convertView.findViewById(R.id.image);
            TextView description = convertView.findViewById(R.id.description);
            ImageView eye = convertView.findViewById(R.id.eye);
            String isSeen = relatedArray.get(position).get_1149();
            LinearLayout rlSinglePrice = convertView.findViewById(R.id.rel_single_price);
            LinearLayout rlTwoPrice = convertView.findViewById(R.id.rl_two_price);
            TextView price = convertView.findViewById(R.id.price);
            TextView actualPrice = convertView.findViewById(R.id.actual_price);
            TextView priceAfterDiscount = convertView.findViewById(R.id.price_after_discount);

            if (isSeen.equalsIgnoreCase("false")) {
                eye.setBackgroundResource(R.drawable.green_seen);
            } else {
                eye.setVisibility(View.GONE);
            }
            try {
                description.setText(Utils.hexToASCII(relatedArray.get(position).get12083()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            String ActualPrice = String.format("%.2f", Double.parseDouble(relatedArray.get(position).get11498()));
            String DiscountPrice = String.format("%.2f", Double.parseDouble(relatedArray.get(position).get122158()));


            Log.d("acyial2", ActualPrice + "Dsax" + DiscountPrice);

            if (Double.parseDouble(ActualPrice) == Double.parseDouble(DiscountPrice) || DiscountPrice.equals("0") || DiscountPrice.equals("0.00")) {
                rlSinglePrice.setVisibility(View.VISIBLE);
                rlTwoPrice.setVisibility(View.GONE);
                actualPrice.setText("$" + Utils.getFormatAmount(ActualPrice));
            } else if (ActualPrice.equals("0") || ActualPrice.equals("0.00")) {
                rlTwoPrice.setVisibility(View.VISIBLE);
                rlSinglePrice.setVisibility(View.GONE);
                priceAfterDiscount.setText("$" + Utils.getFormatAmount(DiscountPrice));
                actualPrice.setVisibility(View.GONE);
            } else if (Double.parseDouble(ActualPrice) > Double.parseDouble(DiscountPrice)) {
                rlTwoPrice.setVisibility(View.VISIBLE);
                rlSinglePrice.setVisibility(View.VISIBLE);
                priceAfterDiscount.setText("$" + Utils.getFormatAmount(DiscountPrice));
                priceAfterDiscount.setGravity(Gravity.END);
                actualPrice.setGravity(Gravity.START);
                actualPrice.setText("$" + Utils.getFormatAmount(ActualPrice) + "   ");
                actualPrice.setTextColor(getResources().getColor(R.color.colorRed));
                actualPrice.setPaintFlags(actualPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mPocketBar.setVisibility(View.VISIBLE);
                    productId = Utils.lengtT(11, relatedArray.get(position).get114144());
                    String productType = relatedArray.get(position).get114112();
                    ModelManager.getInstance().setProductSeen().setProductSeen(getActivity(), Operations.makeProductSeen(getActivity(), productId));

                    Bundle bundle = new Bundle();
                    bundle.putString("productId", productId);
                    bundle.putString("productType", productType);
                    bundle.putString("flag", flag);

                    DisplayFragment(flag, bundle);
                }
            });

//            Glide.with(getActivity()).load(ATPreferences.readString(getActivity(), Constants.KEY_IMAGE_URL) + relatedArray.get(position).get121170())
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .placeholder(R.drawable.splash_screen_new)
//                    .into(imageView);
            Picasso.get().load(ATPreferences.readString(mActivity, Constants.KEY_IMAGE_URL)
                            + "_t_" + relatedArray.get(position).get121170()).
                    into(imageView);


            return convertView;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public int getCount() {
            return relatedArray.size();
        }
    };
    private BaseAdapter mAdapterSpecialRealted = new BaseAdapter() {

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.row_horizontal_test, parent, false);
            }

            ImageView imageView = convertView.findViewById(R.id.image);
            TextView description = convertView.findViewById(R.id.description);
            ImageView eye = convertView.findViewById(R.id.eye);
            String isSeen = specialArrayRelated.get(0).getRESULT().get(0).getIA().get(position).get_114_9();
            LinearLayout rlSinglePrice = convertView.findViewById(R.id.rel_single_price);
            LinearLayout rlTwoPrice = convertView.findViewById(R.id.rl_two_price);
            TextView price = convertView.findViewById(R.id.price);
            TextView actualPrice = convertView.findViewById(R.id.actual_price);
            TextView priceAfterDiscount = convertView.findViewById(R.id.price_after_discount);

            if (isSeen.equalsIgnoreCase("false")) {
                eye.setBackgroundResource(R.drawable.green_seen);
            } else {
                eye.setVisibility(View.GONE);
            }
            try {
                description.setText(Utils.hexToASCII(specialArrayRelated.get(0).getRESULT().get(0).getIA().get(position).get12083()));
            } catch (Exception e) {
                e.printStackTrace();
            }

            String ActualPrice = String.format("%.2f", Double.parseDouble(specialArrayRelated.get(0).getRESULT().get(0).getIA().get(position).get11498()));
            String DiscountPrice = String.format("%.2f", Double.parseDouble(specialArrayRelated.get(0).getRESULT().get(0).getIA().get(position).get122158()));
            if (Double.parseDouble(ActualPrice) == Double.parseDouble(DiscountPrice) || DiscountPrice.equals("0") || DiscountPrice.equals("0.00")) {
                rlSinglePrice.setVisibility(View.VISIBLE);
                rlTwoPrice.setVisibility(View.GONE);
                actualPrice.setText("$" + Utils.getFormatAmount(ActualPrice));
            } else if (ActualPrice.equals("0") || ActualPrice.equals("0.00")) {
                rlTwoPrice.setVisibility(View.VISIBLE);
                rlSinglePrice.setVisibility(View.GONE);
                priceAfterDiscount.setText("$" + Utils.getFormatAmount(DiscountPrice));
                actualPrice.setVisibility(View.GONE);
            } else if (Double.parseDouble(ActualPrice) > Double.parseDouble(DiscountPrice)) {
                rlTwoPrice.setVisibility(View.VISIBLE);
                rlSinglePrice.setVisibility(View.VISIBLE);
                priceAfterDiscount.setText("$" + Utils.getFormatAmount(DiscountPrice));
                actualPrice.setText("$" + Utils.getFormatAmount(ActualPrice) + "   ");
                priceAfterDiscount.setGravity(Gravity.END);
                actualPrice.setGravity(Gravity.START);
                actualPrice.setTextColor(getResources().getColor(R.color.colorRed));
                actualPrice.setPaintFlags(actualPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mPocketBar.setVisibility(View.VISIBLE);
                    productId = Utils.lengtT(11, specialArrayRelated.get(0).getRESULT().get(0).getIA().get(position).get114144());
                    ModelManager.getInstance().setProductSeen().setProductSeen(getActivity(), Operations.makeProductSeen(getActivity(), productId));
                    String productType = specialArrayRelated.get(0).getRESULT().get(0).getIA().get(position).get114112();
                    Bundle bundle = new Bundle();
                    bundle.putString("productId", productId);
                    bundle.putString("productType", productType);
                    bundle.putString("flag", flag);

                    DisplayFragment(flag, bundle);

                }
            });

//            Glide.with(getActivity()).load(ATPreferences.readString(getActivity(), Constants.KEY_IMAGE_URL) + specialArrayRelated.get(0).getRESULT().get(0).getIA().
//                    get(position).getIM().get(0).get4742())
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .placeholder(R.drawable.splash_screen_new)
//                    .into(imageView);

            Picasso.get().load(ATPreferences.readString(mActivity, Constants.KEY_IMAGE_URL) + "_t_" +
                            specialArrayRelated.get(0).getRESULT().get(0).getIA().
                                    get(position).getIM().get(0).get4742())
                    .into(imageView);


            return convertView;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public int getCount() {
            return specialArrayRelated.get(0).getRESULT().get(0).getIA().size();
        }
    };


    int getDistance(MotionEvent event) {
        int dx = (int) (event.getX(0) - event.getX(1));
        int dy = (int) (event.getY(0) - event.getY(1));
        return (int) (Math.sqrt(dx * dx + dy * dy));
    }

    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    // Returns the URI path to the Bitmap displayed in specified ImageView
    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        adapterSubImages = null;
        if (getArguments().containsKey(Constants.TAB_SELECTED)) {
            TabLayout.Tab tab = tabLayout.getTabAt(2);
            ((HomeActivity) getActivity()).removeTabListener();
            tab.select();
            ((HomeActivity) getActivity()).addTabListener();
        }

    }
}

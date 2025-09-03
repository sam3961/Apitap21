package com.apitap.views.fragments.specials.utils;

public class Constant {
    public static final int TRANSACTION_SUCCESSED = 100;  //transaction success

    public static final int TRANSACTION_FAILURE = 101; //transaction failure

    public static final int TRANSACTION_TIMEOOUT = 102; //transaction timeout

    public static final int TRANSACTION_STATUS = 103; //transaction timeout

    public static final int PAYMENT_EXTDATA_RESULT = 301;

    public static final int PAYMENT_EXTDATA_ADD_PASSTHRU_RESULT = 302;

    public static final int MANAGE_UPLOAD_IMAGE_RESULT = 501;

    public static final int MANAGE_SAVE_IMAGE_RESULT = 502;

    public static final int MANAGE_VAS_SPECIAL_DATA_RESULT = 503;

    public static final int CONVERGE_ADD_REQUEST_RESULT = 504;

    public static final int CONVERGE_UPDATE_REQUEST_RESULT = 505;
    public static final int UNAUTHORIZED = 401;

    public static final int MANAGE_VAS_CAP = 506;

    public static final int MANAGE_HMAC_INFORMATION = 507;

    public static final int PAYMENT_TAXDETAIL_RESULT = 303;

    public static final int PAYMENT_COMMERCIAL_RESULT = 304;

    public static final int PAYMENT_LINEIETMDETAIL_RESULT = 305;

    public static final int RESTAURANT_RESULT = 306;

    public static final int PAYMENT_HOST_GATEWAY_RESULT = 307;

    public static final int PAYMENT_TRANSACTION_BEHAVIOR_RESULT = 308;

    public static final int PAYMENT_ORIGINAL_RESULT = 309;

    public static final int PAYMENT_FLEET_CARD_RESULT = 310;

    public static final int PAYMENT_MULTI_MERCHANT_RESULT = 311;

    public static final int PAYMENT_LODGING_RESULT = 312;

    public static final int PAYMENT_LODGING_ROOMS_RESULT = 313;

    public static final int PAYMENT_LODGING_ITEMS_RESULT = 314;

    public static final int PAYMENT_AUTO_RENTAL_ITEMS_RESULT = 314;

    public static final int PAYMENT_HOST_CREDENTIAL_INFORMATION_RESULT = 315;


    public static final String DIALOG_TITLE = "Title"; //dialog title

    public static final String DIALOG_MESSAGE = "Message"; //dialog message

    public static final String BROADCAST_COMMAND = "com.pax.poslink.command";

    public static final String COMMAND_NAME = "commandname";

    public static final String BROADCAST_PASSTHRU = "com.pax.poslink.passthru";

    public static final String COMMAND_NAME_PASSTHRU = "commandname_passthru";

    public static final String BUNDLE_KEY_EXTDATA = "Payment_ExtData";

    public static final String BUNDLE_KEY_PAYMENT_COMMERCIAL = "Payment_Commercial";

    public static final String BUNDLE_KEY_PAYMENT_TAXDETAIL_DISPLAY = "Payment_TaxDetail_Display";

    public static final String BUNDLE_KEY_PAYMENT_LINEITEM_DISPLAY = "Payment_LineItemDetail_Display";

    public static final String BUNDLE_KEY_PAYMENT_RESTAURANT = "Payment_Restaurant";

    public static final String BUNDLE_KEY_PAYMENT_HOST_GATEWAY = "Payment_HostGateWay";

    public static final String BUNDLE_KEY_PAYMENT_TRANSACTION = "Payment_Transaction_Behavior";

    public static final String BUNDLE_KEY_PAYMENT_ORIGINAL = "Payment_Original";

    public static final String BUNDLE_KEY_PAYMENT_FLEET_CARD = "Payment_FleetCard";

    public static final String BUNDLE_KEY_PAYMENT_MULTI_MERCHANT = "Payment_MultiMerchant";

    public static final String BUNDLE_KEY_PAYMENT_LODGING = "Payment_LODGING";

    public static final String BUNDLE_KEY_PAYMENT_LODGING_ROOMS_DISPLAY = "Payment_RoomsRates_Display";

    public static final String BUNDLE_KEY_PAYMENT_LODGING_ITEMS_DISPLAY = "Payment_Lodging_Items_Display";

    public static final String BROADCAST_SCAN_RESULT_MODE = "com.barcode.sendBroadcast";

    public static final String BUNDLE_KEY_PAYMENT_AUTO_RENTAL = "Payment_Auto_Rental";

    public static final String HOME_PAGE_URL = "Home_Page_Url";
    public static final String TABLE_ITEM = "TABLE_ITEM";
    public static final String TABLE_ORDER_CREATOR_ID_SMALL = "tableOrderCreatorId";

    public static final String BUNDLE_KEY_PAYMENT_AUTO_RENTAL_ITEMS_DISPLAY = "Payment_Auto_Rental_Items_Display";

    public static final String BUNDLE_KEY_PAYMENT_HOSTCREDENTIALINFORMATION = "Payment_HOSTCREDENTIALINFORMATION";


    public static final String SELECTED_LOCATION_ID = "SELECTED_LOCATION_ID";
    public static final String FROM_PICKUP = "FROM_PICKUP";
    public static final String FROM_DELIVERY = "FROM_DELIVERY";
    public static final String FROM_PICKUP_LISTING = "FROM_PICKUP_LISTING";
    public static final String ORDER_FROM_APITAP = "ORDER_FROM_APITAP";
    public static final String TABLE_ID = "TABLE_ID";
    public static final String TABLE_NAME = "TABLE_NAME";
    public static final String TABLE_LOCATION_NAME = "TABLE_LOCATION_NAME";
    public static final String LOCATION_SEATING_ID = "LOCATION_SEATING_ID";
    public static final String TABLE_ORDER_CREATOR_ID = "TABLE_ORDER_CREATOR_ID";
    public static final String ORDER_STATUS_ID = "ORDER_STATUS_ID";
    public static final String TABLE_ORDER_ITEM = "TABLE_ORDER_ITEM";
    public static final String TABLE_ORDER_LOCATION_ID = "TABLE_ORDER_LOCATION_ID";
    public static final String TABLE_ORDER_TYPE_ID = "TABLE_ORDER_TYPE_ID";
    public static final String TABLE_ORDER_FIRST_NAME = "TABLE_ORDER_FIRST_NAME";
    public static final String TABLE_ORDER_LAST_NAME = "TABLE_ORDER_LAST_NAME";
    public static final String TABLE_ORDER_PHONE = "TABLE_ORDER_PHONE";
    public static final String ORDER_ID = "ORDER_ID";
    public static final String TAX_AMOUNT = "TAX_AMOUNT";
    public static final String FROM_DELIVERY_LISTING = "FROM_DELIVERY_LISTING";
    public static final String TABLE_ORDER = "TABLE_ORDER";
    public static final String IMAGE_URL = "IMAGE_URL";
    public static final String RESERVATION_DATA = "RESERVATION_DATA";
    public static final String HISTORY_DETAIL = "HISTORY_DETAIL";
    public static final String MERCHANT_COPY = "MERCHANT COPY";
    public static final String CUSTOMER_COPY = "CUSTOMER COPY";


    public static final String PRIVACY_TERMS_DATA = "PRIVACY_TERMS_DATA";
    public static final String PRIVACY_TERMS_TITLE = "PRIVACY_TERMS_TITLE";


    public static final Integer PRODUCT_DISABLE = 1002;
    public static final Integer PRODUCT_ENABLE = 1001;

    public static final Integer STATUS_OPEN = 92001;
    public static final Integer STATUS_DELIVERED = 92002;
    public static final Integer STATUS_CLOSED = 92003;
    public static final Integer STATUS_CANCEL = 92004;
    public static final Integer ORDER_TYPE_ID = 94001;


    public static class OrderTypeConstants {
        public static final int DINE_IN_ID = 94001;
        public static final String DINE_IN_NAME = "Dine-in";
        public static final String DINE_IN_BUNDLE = "4076";

        public static final int DELIVERY_ID = 94002;
            public static final String DELIVERY_NAME = "Delivery";
        public static final String DELIVERY_BUNDLE = "4077";

        public static final int PICKUP_ID = 94003;
        public static final String PICKUP_NAME = "Pickup";
        public static final String PICKUP_BUNDLE = "3874";
    }

    public static class ReservationStatus {
        public static final int SCHEDULED = 1;
        public static final int CHECKED_IN = 2;
        public static final int NO_SHOW = 3;
        public static final int SEATED = 4;
        public static final int CANCELED = 5;
    }


}
package com.apitap.model.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sahil on 9/28/2016.
 */
public class InvoiceDetailBean {

    @SerializedName("RESULT")
    @Expose
    private List<RESULT> rESULT = new ArrayList<RESULT>();

    public List<RESULT> getRESULT() {
        return rESULT;
    }

    public class RESULT {
        @SerializedName("_44")
        @Expose
        private String status;

        @SerializedName("RESULT")
        @Expose
        private List<DetailData> rESULT = new ArrayList<DetailData>();

        public List<DetailData> getRESULT() {
            return rESULT;
        }

        public String getStatus() {
            return status;
        }

        public class DetailData {

            @SerializedName("_121_75")
            @Expose
            public String invoiceId;
            @SerializedName("_114_132")
            @Expose
            public String returnQty;

            @SerializedName("_121_7")
            @Expose
            public String returnPrice;

            @SerializedName("_123_28")
            @Expose
            public String timeframeId;

            @SerializedName("_123_35")
            @Expose
            public String timeFrameName;

            @SerializedName("_121_10")
            @Expose
            public String returnStatus;

   @SerializedName("_127_66")
            @Expose
            public String specialInstructions;


            @SerializedName("_123_30")
            @Expose
            public String timeFrameValue;

            @SerializedName("_123_29")
            @Expose
            public String returnPolicy;


            @SerializedName("_121_170")
            @Expose
            public String invoiceImage;

            @SerializedName("_114_144")
            @Expose
            public String productId;
            @SerializedName("_114_112")
            @Expose
            public String productType;
            @SerializedName("_53")
            @Expose
            public String merchantId;
            @SerializedName("_114_143")
            @Expose
            public String status;
            @SerializedName("_120_83")
            @Expose
            public String productName;
            @SerializedName("_120_157")
            @Expose
            public String desc;
            @SerializedName("IM")
            @Expose
            public List<IM> image = new ArrayList<IM>();
            @SerializedName("_122_148")
            @Expose
            public String productQty;
            @SerializedName("_122_158")
            @Expose
            public String salePrice;
            @SerializedName("_114_98")
            @Expose
            public String regularPrice;
            @SerializedName("CH")
            @Expose
            public List<Choicesdata> cH = new ArrayList<Choicesdata>();

            public String getInvoiceId() {
                return invoiceId;
            }

            public String getInvoiceImage() {
                return invoiceImage;
            }

            public String getTimeframeId() {
                return timeframeId;
            }

            public String getTimeFrameName() {
                return timeFrameName;
            }

            public String getTimeFrameValue() {
                return timeFrameValue;
            }

            public String getReturnStatus() {
                return returnStatus;
            }

            public String getSpecialInstructions() {
                return specialInstructions;
            }

            public String getReturnPolicy() {
                return returnPolicy;
            }

            public String getReturnQty() {
                return returnQty;
            }

            public void setReturnQty(String returnQty) {
                this.returnQty = returnQty;
            }

            public String getReturnPrice() {
                return returnPrice;
            }

            public void setReturnPrice(String returnPrice) {
                this.returnPrice = returnPrice;
            }

            public String getProductId() {
                return productId;
            }

            public String getProductType() {
                return productType;
            }

            public String getMerchantId() {
                return merchantId;
            }

            public String getStatus() {
                return status;
            }

            public String getProductName() {
                return productName;
            }

            public String getDesc() {
                return desc;
            }

            public List<IM> getImage() {
                return image;
            }

            public String getProductQty() {
                return productQty;
            }

            public String getSalePrice() {
                return salePrice;
            }

            public String getRegularPrice() {
                return regularPrice;
            }

            public List<Choicesdata> getcH() {
                return cH;
            }

            public List<IM> getIm() {
                return image;
            }

            public class IM {

                @SerializedName("_120_86")
                @Expose
                public String _12086;
                @SerializedName("_47_42")
                @Expose
                public String path;
                @SerializedName("_120_33")
                @Expose
                public String _12033;

                public String getPath() {
                    return path;
                }
            }

            public class Choicesdata {

                @SerializedName("_122_134")
                @Expose
                public String optionName;
                @SerializedName("_122_135")
                @Expose
                public String choiceName;
                @SerializedName("_120_84")
                @Expose
                public String choicePrice;

                public String getOptionName() {
                    return optionName;
                }

                public String getChoiceName() {
                    return choiceName;
                }

                public String getChoicePrice() {
                    return choicePrice;
                }
            }
        }

    }
}

package com.apitap.model.bean;

import com.apitap.model.Utils;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sahil on 9/14/2016.
 */
public class MessageListBean {

    @SerializedName("RESULT")
    @Expose
    private List<RESULT> rESULT = new ArrayList<RESULT>();

    public List<RESULT> getRESULT() {
        return rESULT;
    }


    public class MI {
        @SerializedName("_120_86")
        @Expose
        private String _120_86;

        @SerializedName("_119_17")
        @Expose
        private String _119_17;

        public String get_120_86() {
            return _120_86;
        }


        public String get_119_17() {
            return _119_17;
        }

    }
    public class RESULT {
        @SerializedName("_44")
        @Expose
        private String status;

        @SerializedName("RESULT")
        @Expose
        private List<MessageData> rESULT = new ArrayList<MessageData>();

        public List<MessageData> getRESULT() {
            return rESULT;
        }

        public String getStatus() {
            return status;
        }

        public class MessageData implements Serializable {

            @SerializedName("_122_114")
            @Expose
            public String id;
            @SerializedName("_122_128")
            @Expose
            public String subject;
            @SerializedName("_114_143")
            @Expose
            public String status;
            @SerializedName("_53")
            @Expose
            public String userId;

            @SerializedName("_127_87")
            @Expose
            public String replied;

            @SerializedName("_114_53")
            @Expose
            public String merchantName;


            @SerializedName("_114_179")
            @Expose

            public String merchantReceiver;
            @SerializedName("_122_181")
            @Expose
            public String name;
            @SerializedName("_114_150")
            @Expose
            public String parentId;
            @SerializedName("_120_31")
            @Expose
            public String date;
            @SerializedName("_120_16")
            @Expose
            public String type;
            @SerializedName("_120_157")
            @Expose
            public String contextData;

            @SerializedName("_114_70")
            @Expose
            public String seventy;

            @SerializedName("_120_138")
            @Expose
            public String isSeen;

            @SerializedName("_121_75")
            @Expose
            public String invoiceId;

            @SerializedName("_114_144")
            @Expose
            public String productId;

            @SerializedName("_123_21")
            @Expose
            public String adId;

            @SerializedName("MI")
            @Expose
            public List<MI> miList= new ArrayList<>();

            @SerializedName("_120_83")
            @Expose
            public String productName;
            @SerializedName("_121_170")
            @Expose
            public String logoImage;

            public List<MI> getMiList() {
                return miList;
            }

            public String getAdId() {
                return adId;
            }

            public void setAdId(String adId) {
                this.adId = adId;
            }
            public String getIsSeen() {
                return isSeen;
            }

            public void setIsSeen(String isSeen) {
                this.isSeen = isSeen;
            }

            public String getProductId() {
                return productId;
            }

            public void setProductId(String productId) {
                this.productId = productId;
            }

            public String getId() {
                return id;
            }

            public String getProductName() {
                return productName;
            }

            public String getSubject() {
                return Utils.hexToASCII(subject);
            }

            public String getStatus() {
                return status;
            }

            public String getUserId() {
                return userId;
            }

            public String getMerchantName() {
                return merchantName;
            }

            public String getReplied() {
                return replied;
            }


            public String getMerchantReceiver() {
                return merchantReceiver;
            }

            public String getName() {
                return name;
            }

            public String getParentId() {
                return parentId;
            }

            public String getDate() {
                return date;
            }

            public String getType() {
                return type;
            }

            public String getContextData() {
                return contextData;
            }

            public String getSeventy() {
                return seventy;
            }

            public String getInvoiceId() {
                return invoiceId;
            }

            public String getLogoImage() {
                return logoImage;
            }
        }

    }
}

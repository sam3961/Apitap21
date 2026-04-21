package com.apitap.app.model.bean;

import com.apitap.app.model.Utils;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MessageListBean implements Serializable {

    @SerializedName(value = "192", alternate = {"_192"})
    @Expose
    private String key;

    @SerializedName(value = "11", alternate = {"_11"})
    @Expose
    private String timeStamp;

    @SerializedName(value = "122.17", alternate = {"_122_17"})
    @Expose
    private boolean hasError;

    @SerializedName(value = "122.18", alternate = {"_122_18"})
    @Expose
    private String errorMessage;

    @SerializedName("RESULT")
    @Expose
    private List<ResultWrapper> RESULT = new ArrayList<>();

    public String getKey() {
        return key;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public boolean isHasError() {
        return hasError;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public List<ResultWrapper> getRESULT() {
        return RESULT;
    }

    public static class MI implements Serializable {

        @SerializedName(value = "120.86", alternate = {"_120_86"})
        @Expose
        private String imageId;

        @SerializedName(value = "119.17", alternate = {"_119_17"})
        @Expose
        private String imageName;

        public String getImageId() {
            return imageId;
        }

        public String getImageName() {
            return imageName;
        }
    }

    public static class CN implements Serializable {

        @SerializedName(value = "122.114", alternate = {"_122_114"})
        @Expose
        private String id;

        @SerializedName(value = "120.16", alternate = {"_120_16"})
        @Expose
        private String type;

        @SerializedName(value = "53", alternate = {"_53"})
        @Expose
        private String userId;

        @SerializedName(value = "114.179", alternate = {"_114_179"})
        @Expose
        private String merchantReceiver;

        @SerializedName(value = "122.128", alternate = {"_122_128"})
        @Expose
        private String subject;

        @SerializedName(value = "120.157", alternate = {"_120_157"})
        @Expose
        private String contextData;

        @SerializedName(value = "121.141", alternate = {"_121_141"})
        @Expose
        private String seenDate;

        @SerializedName(value = "120.31", alternate = {"_120_31"})
        @Expose
        private String date;

        @SerializedName("MI")
        @Expose
        private List<MI> miList = new ArrayList<>();

        @SerializedName(value = "122.181", alternate = {"_122_181"})
        @Expose
        private String name;

        @SerializedName(value = "122.133", alternate = {"_122_133"})
        @Expose
        private String senderName;

        @SerializedName(value = "114.53", alternate = {"_114_53"})
        @Expose
        private String merchantName;

        public String getId() {
            return id;
        }

        public String getType() {
            return type;
        }

        public String getUserId() {
            return userId;
        }

        public String getMerchantReceiver() {
            return merchantReceiver;
        }

        public String getSubject() {
            return subject;
        }

        public String getContextData() {
            return contextData;
        }

        public String getSeenDate() {
            return seenDate;
        }

        public String getDate() {
            return date;
        }

        public List<MI> getMiList() {
            return miList;
        }

        public String getName() {
            return name;
        }

        public String getSenderName() {
            return senderName;
        }

        public String getMerchantName() {
            return merchantName;
        }
    }

    public static class ResultWrapper implements Serializable {

        @SerializedName(value = "101", alternate = {"_101"})
        @Expose
        private String apiCode;

        @SerializedName(value = "39", alternate = {"_39"})
        @Expose
        private String responseCode;

        @SerializedName(value = "44", alternate = {"_44"})
        @Expose
        private String status;

        @SerializedName(value = "127.41", alternate = {"_127_41"})
        @Expose
        private int count;

        @SerializedName("RESULT")
        @Expose
        private List<MessageData> RESULT = new ArrayList<>();

        public String getApiCode() {
            return apiCode;
        }

        public String getResponseCode() {
            return responseCode;
        }

        public String getStatus() {
            return status;
        }

        public int getCount() {
            return count;
        }

        public List<MessageData> getRESULT() {
            return RESULT;
        }
    }

    public static class MessageData implements Serializable {

        @SerializedName(value = "122.114", alternate = {"_122_114"})
        @Expose
        private String id;

        @SerializedName(value = "122.128", alternate = {"_122_128"})
        @Expose
        private String subject;

        @SerializedName(value = "120.157", alternate = {"_120_157"})
        @Expose
        private String contextData;

        @SerializedName(value = "114.143", alternate = {"_114_143"})
        @Expose
        private String status;

        @SerializedName(value = "114.47", alternate = {"_114_47"})
        @Expose
        private String subStatus;

        @SerializedName(value = "53", alternate = {"_53"})
        @Expose
        private String userId;

        @SerializedName(value = "114.179", alternate = {"_114_179"})
        @Expose
        private String merchantReceiver;

        @SerializedName(value = "114.53", alternate = {"_114_53"})
        @Expose
        private String merchantName;

        @SerializedName(value = "122.181", alternate = {"_122_181"})
        @Expose
        private String name;

        @SerializedName(value = "114.150", alternate = {"_114_150"})
        @Expose
        private String parentId;

        @SerializedName(value = "114.138", alternate = {"_114_138"})
        @Expose
        private String createdDate;

        @SerializedName(value = "114.139", alternate = {"_114_139"})
        @Expose
        private String updatedDate;

        @SerializedName(value = "120.16", alternate = {"_120_16"})
        @Expose
        private String type;

        @SerializedName(value = "120.83", alternate = {"_120_83"})
        @Expose
        private String productName;

        @SerializedName(value = "122.25", alternate = {"_122_25"})
        @Expose
        private String extraField12225;

        @SerializedName(value = "121.170", alternate = {"_121_170"})
        @Expose
        private String logoImage;

        @SerializedName(value = "114.70", alternate = {"_114_70"})
        @Expose
        private String seventy;

        @SerializedName(value = "121.141", alternate = {"_121_141"})
        @Expose
        private String seenDate;

        @SerializedName(value = "127.87", alternate = {"_127_87"})
        @Expose
        private String replied;

        @SerializedName(value = "120.138", alternate = {"_120_138"})
        @Expose
        private String isSeen;

        @SerializedName(value = "121.75", alternate = {"_121_75"})
        @Expose
        private String invoiceId;

        @SerializedName(value = "123.21", alternate = {"_123_21"})
        @Expose
        private String adId;

        @SerializedName(value = "114.144", alternate = {"_114_144"})
        @Expose
        private String productId;

        @SerializedName(value = "114.112", alternate = {"_114_112"})
        @Expose
        private String productTypeId;

        @SerializedName(value = "120.21", alternate = {"_120_21"})
        @Expose
        private String messageDirection;

        @SerializedName(value = "122.41", alternate = {"_122_41"})
        @Expose
        private String messageTypeName;

        @SerializedName(value = "114.121", alternate = {"_114_121"})
        @Expose
        private String totalReplies;

        @SerializedName(value = "114.132", alternate = {"_114_132"})
        @Expose
        private String unreadReplies;

        @SerializedName("CN")
        @Expose
        private List<CN> cnList = new ArrayList<>();

        @SerializedName("MI")
        @Expose
        private List<MI> miList = new ArrayList<>();

        public String getId() {
            return id;
        }

        public String getSubject() {
            return Utils.hexToASCII(subject);
        }

        public String getContextData() {
            return contextData;
        }

        public String getStatus() {
            return status;
        }

        public String getSubStatus() {
            return subStatus;
        }

        public String getUserId() {
            return userId;
        }

        public String getMerchantReceiver() {
            return merchantReceiver;
        }

        public String getMerchantName() {
            return merchantName;
        }

        public String getName() {
            return name;
        }

        public String getParentId() {
            return parentId;
        }

        public String getCreatedDate() {
            return createdDate;
        }

        public String getUpdatedDate() {
            return updatedDate;
        }

        public String getType() {
            return type;
        }
       public String getProductName() {
            return productName;
        }

        public String getExtraField12225() {
            return extraField12225;
        }

        public String getLogoImage() {
            return logoImage;
        }

        public String getSeventy() {
            return seventy;
        }

        public String getSeenDate() {
            return seenDate;
        }

        public String getReplied() {
            return replied;
        }

        public String getIsSeen() {
            return isSeen;
        }

        public String getInvoiceId() {
            return invoiceId;
        }

        public String getAdId() {
            return adId;
        }

        public String getProductId() {
            return productId;
        }

        public String getProductTypeId() {
            return productTypeId;
        }

        public String getMessageDirection() {
            return messageDirection;
        }

        public String getMessageTypeName() {
            return messageTypeName;
        }

        public String getTotalReplies() {
            return totalReplies;
        }

        public String getUnreadReplies() {
            return unreadReplies;
        }

        public List<CN> getCnList() {
            return cnList;
        }

        public List<MI> getMiList() {
            return miList;
        }
    }
}
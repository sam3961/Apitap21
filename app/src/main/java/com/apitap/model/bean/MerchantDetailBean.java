package com.apitap.model.bean;

import com.apitap.model.Utils;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sahil on 9/14/2016.
 */
public class MerchantDetailBean {

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

            @SerializedName("_114_70")
            @Expose
            public String name;


            @SerializedName("_120_157")
            @Expose
            public String about;
            @SerializedName("_114_9")
            @Expose
            public String isTipAvail;

            @SerializedName("_114_12")
            @Expose
            public String address;
            @SerializedName("_114_13")
            @Expose
            public String address_second;
            @SerializedName("_48_28")
            @Expose
            public String phone;
            @SerializedName("_123_20")
            @Expose
            public String website;
            @SerializedName("_121_170")
            @Expose
            public String image;
            @SerializedName("_119_15")
            @Expose
            public String terms;
            @SerializedName("_119_16")
            @Expose
            public String policy;
            @SerializedName("_122_19")
            @Expose
            public String rating;
            @SerializedName("_121_80")
            @Expose
            public String review_count;

            public String getName() {
                return Utils.hexToASCII(name);
            }

            public String getAbout() {
                return about;
            }

            public String getIsTipAvail() {
                return isTipAvail;
            }

            public String getAddress() {
                return address;
            }

            public String getAddress_second() {
                return address_second;
            }

            public String getPhone() {
                return phone;
            }

            public String getWebsite() {
                return website;
            }

            public String getImage() {
                return image;
            }

            public String getTerms() {
                return terms;
            }

            public String getPolicy() {
                return policy;
            }

            public String getRating() {
                return rating;
            }

            public String getReview_count() {
                return review_count;
            }
        }

    }
}

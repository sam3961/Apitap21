package com.apitap.model.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sourcefuse on 25/11/16.
 */

public class SearchItemBean {

    @SerializedName("RESULT")
    @Expose
    private List<Result> result = new ArrayList<Result>();

    public List<Result> getResult() {
        return result;
    }

    public class Result{

        @SerializedName("_44")
        @Expose
        private String status;

        public String getStatus() {
            return status;
        }

        @SerializedName("RESULT")
        @Expose
        public List<ResultData> result = new ArrayList<ResultData>();

        public List<ResultData> getResult() {
            return result;
        }

        public class ResultData{

            @SerializedName("_114_144")
            @Expose
            private String id;

            @SerializedName("_114_70")
            @Expose
            private String _11470;

            @SerializedName("_114_112")
            @Expose
            private String product_type;

            @SerializedName("_120_83")
            @Expose
            private String name;

            @SerializedName("_121_170")
            @Expose
            private String image;

            @SerializedName("_114_9")
            @Expose
            private String seen;

            @SerializedName("_114_98")
            @Expose
            private String actualPrice;

            @SerializedName("_122_158")
            @Expose
            private String priceAfterDiscount;

            public String getImage() {
                return image;
            }

            public String getName() {
                return name;
            }

            public String getActualPrice() {
                return actualPrice;
            }

            public String getPriceAfterDiscount() {
                return priceAfterDiscount;
            }

            public String getSeen() {
                return seen;
            }

            public String getId() {
                return id;
            }

            public String getProduct_type() {
                return product_type;
            }

            public String get_11470() {
                return _11470;
            }
        }

    }



}

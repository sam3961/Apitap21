package com.apitap.model.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by sourcefuse on 14/12/16.
 */

public class SearchFavoritesBean {

    @SerializedName("RESULT")
    @Expose
    private List<RESULT> result;

    public List<RESULT> getResult() {
        return result;
    }

    public class RESULT {
        @SerializedName("_101")
        @Expose
        private String operationCode;

        @SerializedName("_44")
        @Expose
        private String transaction;

        @SerializedName("RESULT")
        @Expose
        private List<RESULTDATA> result;

        public List<RESULTDATA> getResult() {
            return result;
        }

        public String getOperationCode() {
            return operationCode;
        }

        public String getTransaction() {
            return transaction;
        }

        public class RESULTDATA {

            @SerializedName("_120_83")
            @Expose
            private String name;

            @SerializedName("_114_70")
            @Expose
            private String storeName;

            @SerializedName("_114_98")
            @Expose
            private String actualPrice;

            @SerializedName("_122_158")
            @Expose
            private String priceAfterDiscount;

            @SerializedName("_114_112")
            @Expose
            private String productType;

            @SerializedName("_114_144")
            @Expose
            private String productId;

            @SerializedName("IM")
            @Expose
            private List<ImagesData> imagesData;

            public String getName() {
                return name;
            }

            public String getStoreName() {
                return storeName;
            }

            public String getActualPrice() {
                return actualPrice;
            }

            public String getPriceAfterDiscount() {
                return priceAfterDiscount;
            }

            public String getProductType() {
                return productType;
            }

            public List<ImagesData> getImagesData() {
                return imagesData;
            }

            public String getProductId() {
                return productId;
            }

            public class ImagesData{

                @SerializedName("_47_42")
                @Expose
                private String imageUrl;

                public String getImageUrl() {
                    return imageUrl;
                }
            }
        }
    }
}

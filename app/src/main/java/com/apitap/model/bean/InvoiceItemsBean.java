package com.apitap.model.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sahil on 9/14/2016.
 */
public class InvoiceItemsBean {

    @SerializedName("RESULT")
    @Expose
    private List<RESULT> rESULT = new ArrayList<RESULT>();

    public List<RESULT> getRESULT() {
        return rESULT;
    }

    public class RESULT{
        @SerializedName("_44")
        @Expose
        private String status;

        @SerializedName("RESULT")
        @Expose
        private List<Invoicedata> rESULT = new ArrayList<Invoicedata>();

        public List<Invoicedata> getRESULT() {
            return rESULT;
        }

        public String getStatus() {
            return status;
        }

        public class Invoicedata implements Serializable{

            @SerializedName("_121_75")
            @Expose
            public String invoiceId;

            @SerializedName("_114_144")
            @Expose
            public String invoiceNumber;
            @SerializedName("_114_112")
            @Expose
            public String invoiceDate;
            @SerializedName("_120_83")
            @Expose
            public String itemName;
            @SerializedName("_120_157")
            @Expose
            public String description;
            @SerializedName("_122_148")
            @Expose
            public String quantity;

            @SerializedName("_122_158")
            @Expose
            public String actualPrice;

            public String getInvoiceId() {
                return invoiceId;
            }

            public String getInvoiceNumber() {
                return invoiceNumber;
            }

            public String getInvoiceDate() {
                return invoiceDate;
            }

            public String getItemName() {
                return itemName;
            }

            public String getDescription() {
                return description;
            }

            public String getQuantity() {
                return quantity;
            }

            public String getActualPrice() {
                return actualPrice;
            }
        }

    }
}

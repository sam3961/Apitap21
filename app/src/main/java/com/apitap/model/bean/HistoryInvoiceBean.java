package com.apitap.model.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sahil on 9/14/2016.
 */
public class HistoryInvoiceBean {

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
        private List<Invoicedata> rESULT = new ArrayList<Invoicedata>();

        public List<Invoicedata> getRESULT() {
            return rESULT;
        }

        public String getStatus() {
            return status;
        }

        public class Invoicedata implements Serializable {

            @SerializedName("_121_75")
            @Expose
            public String invoiceId;

            @SerializedName("_114_47")
            @Expose
            public String locationId;

            @SerializedName("_123_28")
            @Expose
            public String timeframeId;

            @SerializedName("_123_35")
            @Expose
            public String timeFrameName;

            @SerializedName("_123_30")
            @Expose
            public String timeFrameValue;

            @SerializedName("_123_29")
            @Expose
            public String returnPolicy;

            @SerializedName("_121_10")
            @Expose
            public String returnStatus;

            @SerializedName("_127_66")
            @Expose
            public String specialInstructions;

            @SerializedName("_122_31")
            @Expose
            public String shoppingCartId;

            @SerializedName("_124_101")
            @Expose
            public String transactionNo;


            @SerializedName("_124_116")
            @Expose
            public String approval_number;

            @SerializedName("_124_111")
            @Expose
            public String approval_Code;

            @SerializedName("_121_41")
            @Expose
            public String invoiceNumber;
            @SerializedName("_120_31")
            @Expose
            public String invoiceDate;
            @SerializedName("_114_143")
            @Expose
            public String status;
            @SerializedName("_55_3")
            @Expose
            public String amount;
            @SerializedName("_114_70")
            @Expose
            public String companyName;

            @SerializedName("_114_179")
            @Expose
            public String merchantId;
            @SerializedName("_120_109")
            @Expose
            public String invoiceTip;
            @SerializedName("_121_97")
            @Expose
            public String invoiceTax;
            @SerializedName("_121_72")
            @Expose
            public String transactionType;
            @SerializedName("_121_55")
            @Expose
            public String deliveryInstructions;
            @SerializedName("_48_29")
            @Expose
            public String innvoiceType;
            @SerializedName("KT")
            @Expose
            public List<KT> kT = new ArrayList<KT>();
            @SerializedName("DE")
            @Expose
            public List<DE> dE = new ArrayList<DE>();

            public String getSpecialInstructions() {
                return specialInstructions;
            }

            public String getInvoiceId() {
                return invoiceId;
            }

            public String getLocationId() {
                return locationId;
            }

            public String getTransactionNo() {
                return transactionNo;
            }

            public String getApproval_Code() {
                return approval_Code;
            }

            public String getReturnStatus() {
                return returnStatus;
            }

            public String getApproval_number() {
                return approval_number;
            }

            public String getInvoiceNumber() {
                return invoiceNumber;
            }

            public String getInvoiceDate() {
                return invoiceDate;
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

            public String getReturnPolicy() {
                return returnPolicy;
            }

            public String getStatus() {
                return status;
            }

            public String getAmount() {
                return amount;
            }

            public String getShoppingCartId() {
                return shoppingCartId;
            }

            public String getCompanyName() {
                return companyName;
            }

            public String getMerchantId() {
                return merchantId;
            }

            public String getInvoiceTip() {
                return invoiceTip;
            }

            public String getInvoiceTax() {
                return invoiceTax;
            }

            public String getTransactionType() {
                return transactionType;
            }

            public String getDeliveryInstructions() {
                return deliveryInstructions;
            }

            public String getInnvoiceType() {
                return innvoiceType;
            }

            public List<KT> getkT() {
                return kT;
            }

            public List<DE> getdE() {
                return dE;
            }

            public class DE implements Serializable {

                @SerializedName("_114_12")
                @Expose
                public String addressLine;
                @SerializedName("_122_133")
                @Expose
                public String deleiveryCompany;
                @SerializedName("_122_161")
                @Expose
                public String deleiveryPrice;
                @SerializedName("_121_27")
                @Expose
                public String shippingStatus;
                @SerializedName("_122_188")
                @Expose
                public String routingNumber;
                @SerializedName("_121_140")
                @Expose
                public String shippingDate;

                public String getAddressLine() {
                    return addressLine;
                }

                public String getDeleiveryCompany() {
                    return deleiveryCompany;
                }

                public String getDeleiveryPrice() {
                    return deleiveryPrice;
                }

                public String getShippingStatus() {
                    return shippingStatus;
                }

                public String getRoutingNumber() {
                    return routingNumber;
                }

                public String getShippingDate() {
                    return shippingDate;
                }
            }

            public class KT implements Serializable {

                @SerializedName("_48_30")
                @Expose
                public String cardAmount;
                @SerializedName("_48_6")
                @Expose
                public String cardNickname;


                @SerializedName("_120_7")
                @Expose
                public String cardname;

                @SerializedName("_37")
                @Expose
                public String transactionNumber;
                @SerializedName("_38")
                @Expose
                public String merchantApprovalNumber;

                public String getCardAmount() {
                    return cardAmount;
                }

                public String getCardNickname() {
                    return cardNickname;
                }

                public String getCardName() {
                    return cardname;
                }

                public String getTransactionNumber() {
                    return transactionNumber;
                }

                public String getMerchantApprovalNumber() {
                    return merchantApprovalNumber;
                }
            }
        }

    }
}

package com.apitap.model.bean;

import com.apitap.model.Utils;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sahil on 9/14/2016.
 */
public class ShoppingCartDetailBean {

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
        private List<DetailData> rESULT = new ArrayList<DetailData>();

        public List<DetailData> getRESULT() {
            return rESULT;
        }

        public String getStatus() {
            return status;
        }

        public class DetailData {



            @SerializedName("_122_31")
            @Expose
            public String _12231;

            @SerializedName("_121_30")
            @Expose
            public String _12130;
            @SerializedName("_114_144")
            @Expose
            public String _114144;
            @SerializedName("_120_157")
            @Expose
            public String description;
            @SerializedName("_121_170")
            @Expose
            public String image;
            @SerializedName("_114_132")
            @Expose
            public String qunatity;
            @SerializedName("_122_75")
            @Expose
            public String price;
            @SerializedName("_114_112")
            @Expose
            public String _114112;
            @SerializedName("CH")
            @Expose
            public List<CH> cH = new ArrayList<CH>();

            @SerializedName("DE")
            @Expose
            public List<DE> dE = new ArrayList<DE>();

            @SerializedName("_122_159")
            @Expose
            public String _122159;
            @SerializedName("_114_143")
            @Expose
            public String _114143;
            @SerializedName("_123_41")
            @Expose
            public String name;

            public String get_12231() {
                return _12231;
            }



            public String get_12130() {
                return _12130;
            }

            public String get_114144() {
                return _114144;
            }

            public String getDescription() {
                return description;
            }

            public String getImage() {
                return image;
            }

            public String getQuantity() {
                return qunatity;
            }

            public String getPrice() {
                return price;
            }

            public String get_114112() {
                return _114112;
            }

            public List<CH> getcH() {
                return cH;
            }

            public List<DE> getdE() {
                return dE;
            }

            public String get_122159() {
                return _122159;
            }

            public String get_114143() {
                return _114143;
            }

            public String getName() {
                return name;
            }

            public class CH {

                @SerializedName("_122_159")
                @Expose
                public String _122159;
                @SerializedName("_114_143")
                @Expose
                public String _114143;
                @SerializedName("_122_112")
                @Expose
                public String _122112;
                @SerializedName("_127_16")
                @Expose
                public String _12716;
                @SerializedName("_120_84")
                @Expose
                public String _12084;




                public String get_122159() {

                    return _122159;
                }

                public String get_114143() {
                    return _114143;
                }

                public String get_122112() {
                    return _122112;
                }

                public String get_12716() {
                    return Utils.hexToASCII(_12716);
                }

                public String get_12084() {
                    return _12084;
                }
            }

            public class DE{
                @SerializedName("_122_39")
                @Expose
                public String expectedDays;

                @SerializedName("_122_161")
                @Expose
                public String price;

                public String getExpectedDays() {
                    return expectedDays;
                }

                public String getPrice() {
                    return price;
                }
            }

        }

    }
}

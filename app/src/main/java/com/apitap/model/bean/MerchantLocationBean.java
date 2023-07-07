package com.apitap.model.bean;

import com.apitap.model.Utils;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sahil on 9/14/2016.
 */
public class MerchantLocationBean {

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
        private List<MerchantLocationData> rESULT = new ArrayList<MerchantLocationData>();

        public List<MerchantLocationData> getRESULT() {
            return rESULT;
        }

        public String getStatus() {
            return status;
        }

        public class MerchantLocationData {

            @SerializedName("_114_47")
            @Expose
            public String locationID;
            @SerializedName("_114_70")
            @Expose
            public String storeName;
            @SerializedName("AD")
            @Expose
            public AD aD;
            @SerializedName("PH")
            @Expose
            public PH_ pH;
            @SerializedName("SL")
            @Expose
            public List<SL> sL = new ArrayList<SL>();

            public double distance;

            public double getDistance() {
                return distance;
            }

            public void setDistance(double distance) {
                this.distance = distance;
            }

            public String getLocationID() {
                return locationID;
            }

            public String getStoreName() {
                return Utils.hexToASCII(storeName);
            }

            public AD getaD() {
                return aD;
            }

            public PH_ getpH() {
                return pH;
            }

            public List<SL> getsL() {
                return sL;
            }

            public class SL {

                @SerializedName("_127_89")
                @Expose
                public String dayOfWeek;
                @SerializedName("_127_90")
                @Expose
                public String hourFlag;
                @SerializedName("_127_91")
                @Expose
                public String closeFlag;
                @SerializedName("_127_64")
                @Expose
                public String hourOpen;
                @SerializedName("_127_65")
                @Expose
                public String hourClose;

                public String getDayOfWeek() {
                    return dayOfWeek;
                }

                public String getHourFlag() {
                    return hourFlag;
                }

                public String getCloseFlag() {
                    return closeFlag;
                }

                public String getHourOpen() {
                    return hourOpen;
                }

                public String getHourClose() {
                    return hourClose;
                }
            }


            public class PH_ {

                @SerializedName("_122_88")
                @Expose
                public String _12288;
                @SerializedName("_53")
                @Expose
                public String _53;
                @SerializedName("_48_28")
                @Expose
                public String _4828;
                @SerializedName("_121_167")
                @Expose
                public String _121167;
                @SerializedName("CO")
                @Expose
                public CO__ cO;
                @SerializedName("_121_62")
                @Expose
                public String _12162;

                public class CO__ {

                    @SerializedName("_122_87")
                    @Expose
                    public String _12287;
                    @SerializedName("_47_18")
                    @Expose
                    public String _4718;
                    @SerializedName("_120_129")
                    @Expose
                    public String _120129;
                    @SerializedName("_114_17")
                    @Expose
                    public String _11417;

                }

            }


            public class AD {

                @SerializedName("_114_115")
                @Expose
                public String _114115;
                @SerializedName("_53")
                @Expose
                public String _53;
                @SerializedName("_121_45")
                @Expose
                public String _12145;
                @SerializedName("_114_53")
                @Expose
                public String _11453;
                @SerializedName("_114_9")
                @Expose
                public String _1149;
                @SerializedName("_120_39")
                @Expose
                public String _12039;
                @SerializedName("_120_38")
                @Expose
                public String _12038;
                @SerializedName("CI")
                @Expose
                public CI cI;
                @SerializedName("CO")
                @Expose
                public CO cO;
                @SerializedName("ST")
                @Expose
                public ST sT;
                @SerializedName("ZP")
                @Expose
                public ZP zP;
                @SerializedName("PH")
                @Expose
                public PH pH;
                @SerializedName("_114_12")
                @Expose
                public String _11412;
                @SerializedName("_114_13")
                @Expose
                public String _11413;

                public ZP getzP() {
                    return zP;
                }

                public class ST {

                    @SerializedName("_120_13")
                    @Expose
                    public String _12013;
                    @SerializedName("_47_16")
                    @Expose
                    public String _4716;
                    @SerializedName("_122_87")
                    @Expose
                    public String _12287;

                }

                public class PH {

                    @SerializedName("_122_88")
                    @Expose
                    public String _12288;
                    @SerializedName("_53")
                    @Expose
                    public String _53;
                    @SerializedName("_48_28")
                    @Expose
                    public String _4828;
                    @SerializedName("_121_167")
                    @Expose
                    public String _121167;
                    @SerializedName("CO")
                    @Expose
                    public CO_ cO;
                    @SerializedName("_121_62")
                    @Expose
                    public String _12162;

                    public class CO_ {

                        @SerializedName("_122_87")
                        @Expose
                        public String _12287;
                        @SerializedName("_47_18")
                        @Expose
                        public String _4718;
                        @SerializedName("_120_129")
                        @Expose
                        public String _120129;
                        @SerializedName("_114_17")
                        @Expose
                        public String _11417;

                    }


                }


                public class ZP {

                    @SerializedName("_122_107")
                    @Expose
                    public String _122107;
                    @SerializedName("_47_17")
                    @Expose
                    public String _4717;
                    @SerializedName("_114_14")
                    @Expose
                    public String _11414;
                    @SerializedName("_120_38")
                    @Expose
                    public String lat;
                    @SerializedName("_120_39")
                    @Expose
                    public String lng;

                    public String getLat() {
                        return lat;
                    }

                    public String getLng() {
                        return lng;
                    }
                }


                public class CI {

                    @SerializedName("_114_14")
                    @Expose
                    public String _11414;
                    @SerializedName("_47_15")
                    @Expose
                    public String _4715;
                    @SerializedName("_120_13")
                    @Expose
                    public String _12013;

                }

                public class CO {

                    @SerializedName("_122_87")
                    @Expose
                    public String _12287;
                    @SerializedName("_47_18")
                    @Expose
                    public String _4718;
                    @SerializedName("_120_129")
                    @Expose
                    public String _120129;
                    @SerializedName("_114_17")
                    @Expose
                    public String _11417;

                }


            }

        }

    }
}

package com.apitap.model.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Shami on 25/6/2018.
 */

public class AdsListBean2 {

    @SerializedName("_192")
    @Expose
    private String _192;
    @SerializedName("_11")
    @Expose
    private String _11;
    @SerializedName("_122_17")
    @Expose
    private Boolean _12217;
    @SerializedName("_122_18")
    @Expose
    private String _12218;
    @SerializedName("RESULT")
    @Expose
    private List<RESULT> rESULT = null;

    public String get192() {
        return _192;
    }

    public void set192(String _192) {
        this._192 = _192;
    }

    public String get11() {
        return _11;
    }

    public void set11(String _11) {
        this._11 = _11;
    }

    public Boolean get12217() {
        return _12217;
    }

    public void set12217(Boolean _12217) {
        this._12217 = _12217;
    }

    public String get12218() {
        return _12218;
    }

    public void set12218(String _12218) {
        this._12218 = _12218;
    }

    public List<RESULT> getRESULT() {
        return rESULT;
    }

    public void setRESULT(List<RESULT> rESULT) {
        this.rESULT = rESULT;
    }

    public class AD {

        @SerializedName("_123_21")
        @Expose
        private String _12321;
        @SerializedName("_53")
        @Expose
        private String _53;
        @SerializedName("_114_70")
        @Expose
        private String _11470;
        @SerializedName("_120_83")
        @Expose
        private String _12083;
        @SerializedName("_120_157")
        @Expose
        private String _120157;
        @SerializedName("_120_86")
        @Expose
        private String _12086;
        @SerializedName("_121_170")
        @Expose
        private String _121170;
        @SerializedName("_127_50")
        @Expose
        private String _12750;
        @SerializedName("_121_15")
        @Expose
        private String _12115;
        @SerializedName("IR")
        @Expose
        private List<IR> iR = null;

        public String get12321() {
            return _12321;
        }

        public void set12321(String _12321) {
            this._12321 = _12321;
        }

        public String get53() {
            return _53;
        }

        public void set53(String _53) {
            this._53 = _53;
        }

        public String get11470() {
            return _11470;
        }

        public void set11470(String _11470) {
            this._11470 = _11470;
        }

        public String get12083() {
            return _12083;
        }

        public void set12083(String _12083) {
            this._12083 = _12083;
        }

        public String get120157() {
            return _120157;
        }

        public void set120157(String _120157) {
            this._120157 = _120157;
        }

        public String get12086() {
            return _12086;
        }

        public void set12086(String _12086) {
            this._12086 = _12086;
        }

        public String get121170() {
            return _121170;
        }

        public void set121170(String _121170) {
            this._121170 = _121170;
        }

        public String get12750() {
            return _12750;
        }

        public void set12750(String _12750) {
            this._12750 = _12750;
        }

        public String get12115() {
            return _12115;
        }

        public void set12115(String _12115) {
            this._12115 = _12115;
        }


        public List<IR> getIR() {
            return iR;
        }

        public void setIR(List<IR> iR) {
            this.iR = iR;
        }

    }

    public class IR {

        @SerializedName("_114_144")
        @Expose
        private String _114144;
        @SerializedName("_114_112")
        @Expose
        private String _114112;
        @SerializedName("_120_83")
        @Expose
        private String _12083;
        @SerializedName("_114_98")
        @Expose
        private String _11498;
        @SerializedName("_122_158")
        @Expose
        private String _122158;
        @SerializedName("_121_170")
        @Expose
        private String _121170;

        public String get114144() {
            return _114144;
        }

        public void set114144(String _114144) {
            this._114144 = _114144;
        }

        public String get114112() {
            return _114112;
        }

        public void set114112(String _114112) {
            this._114112 = _114112;
        }

        public String get12083() {
            return _12083;
        }

        public void set12083(String _12083) {
            this._12083 = _12083;
        }

        public String get11498() {
            return _11498;
        }

        public void set11498(String _11498) {
            this._11498 = _11498;
        }

        public String get122158() {
            return _122158;
        }

        public void set122158(String _122158) {
            this._122158 = _122158;
        }

        public String get121170() {
            return _121170;
        }

        public void set121170(String _121170) {
            this._121170 = _121170;
        }

    }

    public class RESULT {

        @SerializedName("_101")
        @Expose
        private String _101;
        @SerializedName("_39")
        @Expose
        private String _39;
        @SerializedName("_44")
        @Expose
        private String _44;
        @SerializedName("_127_41")
        @Expose
        private Integer _12741;
        @SerializedName("RESULT")
        @Expose
        private List<RESULT_> rESULT = null;

        public String get101() {
            return _101;
        }

        public void set101(String _101) {
            this._101 = _101;
        }

        public String get39() {
            return _39;
        }

        public void set39(String _39) {
            this._39 = _39;
        }

        public String get44() {
            return _44;
        }

        public void set44(String _44) {
            this._44 = _44;
        }

        public Integer get12741() {
            return _12741;
        }

        public void set12741(Integer _12741) {
            this._12741 = _12741;
        }

        public List<RESULT_> getRESULT() {
            return rESULT;
        }

        public void setRESULT(List<RESULT_> rESULT) {
            this.rESULT = rESULT;
        }

    }


    public class RESULT_ {

        @SerializedName("_114_93")
        @Expose
        private String _11493;
        @SerializedName("_120_45")
        @Expose
        private String _12045;
        @SerializedName("_120_83")
        @Expose
        private String _12083;
        @SerializedName("_120_44")
        @Expose
        private String _12044;
        @SerializedName("_122_21")
        @Expose
        private String _12221;
        @SerializedName("AD")
        @Expose
        private List<AD> aD = null;

        public String get11493() {
            return _11493;
        }

        public void set11493(String _11493) {
            this._11493 = _11493;
        }

        public String get12045() {
            return _12045;
        }

        public void set12045(String _12045) {
            this._12045 = _12045;
        }

        public String get12083() {
            return _12083;
        }

        public void set12083(String _12083) {
            this._12083 = _12083;
        }

        public String get12044() {
            return _12044;
        }

        public void set12044(String _12044) {
            this._12044 = _12044;
        }

        public String get12221() {
            return _12221;
        }

        public void set12221(String _12221) {
            this._12221 = _12221;
        }

        public List<AD> getAD() {
            return aD;
        }

        public void setAD(List<AD> aD) {
            this.aD = aD;
        }

    }

    public class SL {

        @SerializedName("_127_89")
        @Expose
        private String _12789;
        @SerializedName("_127_90")
        @Expose
        private String _12790;
        @SerializedName("_127_91")
        @Expose
        private String _12791;
        @SerializedName("_120_12")
        @Expose
        private String _12012;
        @SerializedName("_127_64")
        @Expose
        private String _12764;
        @SerializedName("_127_65")
        @Expose
        private String _12765;

        public String get12789() {
            return _12789;
        }

        public void set12789(String _12789) {
            this._12789 = _12789;
        }

        public String get12790() {
            return _12790;
        }

        public void set12790(String _12790) {
            this._12790 = _12790;
        }

        public String get12791() {
            return _12791;
        }

        public void set12791(String _12791) {
            this._12791 = _12791;
        }

        public String get12012() {
            return _12012;
        }

        public void set12012(String _12012) {
            this._12012 = _12012;
        }

        public String get12764() {
            return _12764;
        }

        public void set12764(String _12764) {
            this._12764 = _12764;
        }

        public String get12765() {
            return _12765;
        }

        public void set12765(String _12765) {
            this._12765 = _12765;
        }

    }


}
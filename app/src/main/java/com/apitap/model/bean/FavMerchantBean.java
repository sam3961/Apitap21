package com.apitap.model.bean;

/**
 * Created by Shami on 24/4/2018.
 */

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class FavMerchantBean {

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
    private List<RESULT> rESULT = null;

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

    public List<RESULT> getRESULT() {
        return rESULT;
    }

    public void setRESULT(List<RESULT> rESULT) {
        this.rESULT = rESULT;
    }
    public class RESULT {

        @SerializedName("_53")
        @Expose
        private String _53;
        @SerializedName("_114_70")
        @Expose
        private String _11470;
        @SerializedName("_120_31")
        @Expose
        private String _12031;
        @SerializedName("CU")
        @Expose
        private List<CU> cU = null;
        @SerializedName("_121_170")
        @Expose
        private String _121170;
        @SerializedName("_127_66")
        @Expose
        private String _12766;

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

        public String get12031() {
            return _12031;
        }

        public void set12031(String _12031) {
            this._12031 = _12031;
        }

        public List<CU> getCU() {
            return cU;
        }

        public void setCU(List<CU> cU) {
            this.cU = cU;
        }

        public String get121170() {
            return _121170;
        }

        public void set121170(String _121170) {
            this._121170 = _121170;
        }

        public String get12766() {
            return _12766;
        }

        public void set12766(String _12766) {
            this._12766 = _12766;
        }

    }
    public class CU {

        @SerializedName("_114_93")
        @Expose
        private String _11493;

        @SerializedName("_120_45")
        @Expose
        private String _12045;

        public String get_12083() {
            return _12083;
        }

        public void set_12083(String _12083) {
            this._12083 = _12083;
        }

        @SerializedName("_120_83")
        @Expose
        private String _12083;

        @SerializedName("_120_44")
        @Expose
        private String _12044;


        @SerializedName("_122_21")
        @Expose
        private String _12221;
        @SerializedName("_127_10")
        @Expose
        private String _12710;
        @SerializedName("_122_183")
        @Expose
        private String _122183;
        @SerializedName("_127_86")
        @Expose
        private String _12786;
        @SerializedName("_47_42")
        @Expose
        private String _4742;

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

        public String get12710() {
            return _12710;
        }

        public void set12710(String _12710) {
            this._12710 = _12710;
        }

        public String get122183() {
            return _122183;
        }

        public void set122183(String _122183) {
            this._122183 = _122183;
        }

        public String get12786() {
            return _12786;
        }

        public void set12786(String _12786) {
            this._12786 = _12786;
        }

        public String get4742() {
            return _4742;
        }

        public void set4742(String _4742) {
            this._4742 = _4742;
        }

    }
}
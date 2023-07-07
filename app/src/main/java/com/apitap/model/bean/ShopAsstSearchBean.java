package com.apitap.model.bean;

/**
 * Created by Shami on 31/7/2018.
 */

import java.util.List;

import com.apitap.model.Utils;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class ShopAsstSearchBean {

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
        @SerializedName("_114_121")
        @Expose
        private String _114121;
        @SerializedName("PC")
        @Expose
        private List<PC> pC = null;

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

        public String get114121() {
            return _114121;
        }

        public void set114121(String _114121) {
            this._114121 = _114121;
        }

        public List<PC> getPC() {
            return pC;
        }

        public void setPC(List<PC> pC) {
            this.pC = pC;
        }

    }

    public class PC {

        @SerializedName("_114_144")
        @Expose
        private String _114144;
        @SerializedName("_114_112")
        @Expose
        private String _114112;
        @SerializedName("_120_83")
        @Expose
        private String _12083;
        @SerializedName("_120_157")
        @Expose
        private String _120157;
        @SerializedName("_114_98")
        @Expose
        private String _11498;
        @SerializedName("_122_158")
        @Expose
        private String _122158;
        @SerializedName("_121_170")
        @Expose
        private String _121170;
        @SerializedName("_120_31")
        @Expose
        private String _12031;
        @SerializedName("_114_179")
        @Expose
        private String _114179;
        @SerializedName("_114_70")
        @Expose
        private String _11470;
        @SerializedName("_121_77")
        @Expose
        private String _12177;
        @SerializedName("_122_19")
        @Expose
        private String _12219;
        @SerializedName("_121_80")
        @Expose
        private String _12180;
        @SerializedName("_114_9")
        @Expose
        private String _1149;

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

        public String get120157() {
            return _120157;
        }

        public void set120157(String _120157) {
            this._120157 = _120157;
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

        public String get12031() {
            return _12031;
        }

        public void set12031(String _12031) {
            this._12031 = _12031;
        }

        public String get114179() {
            return _114179;
        }

        public void set114179(String _114179) {
            this._114179 = _114179;
        }

        public String get11470() {
            return _11470;
        }

        public void set11470(String _11470) {
            this._11470 = _11470;
        }

        public String get12177() {
            return _12177;
        }

        public void set12177(String _12177) {
            this._12177 = _12177;
        }

        public String get12219() {
            return _12219;
        }

        public void set12219(String _12219) {
            this._12219 = _12219;
        }

        public String get12180() {
            return _12180;
        }

        public void set12180(String _12180) {
            this._12180 = _12180;
        }

        public String get1149() {
            return _1149;
        }

        public void set1149(String _1149) {
            this._1149 = _1149;
        }

    }
}
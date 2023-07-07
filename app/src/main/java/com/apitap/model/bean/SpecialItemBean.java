package com.apitap.model.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Shami on 13/2/2018.
 */


public class SpecialItemBean {

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

    public class RESULT_ {

        @SerializedName("IA")
        @Expose
        private List<Ium> iA = null;

        public List<Ium> getIA() {
            return iA;
        }

        public void setIA(List<Ium> iA) {
            this.iA = iA;
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
    public class Ium {

        @SerializedName("_114_144")
        @Expose
        private String _114144;
        @SerializedName("_114_112")
        @Expose
        private String _114112;
        @SerializedName("_114_143")
        @Expose
        private String _114143;

        @SerializedName("_114_9")
        @Expose
        private String _114_9;

        @SerializedName("_120_83")
        @Expose
        private String _12083;
        @SerializedName("_114_98")
        @Expose
        private String _11498;
        @SerializedName("_122_158")
        @Expose
        private String _122158;
        @SerializedName("IM")
        @Expose
        private List<IM> iM = null;

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

        public String get114143() {
            return _114143;
        }

        public void set114143(String _114143) {
            this._114143 = _114143;
        }


        public String get_114_9() {
            return _114_9;
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

        public List<IM> getIM() {
            return iM;
        }

        public void setIM(List<IM> iM) {
            this.iM = iM;
        }

    }
    public class IM {

        @SerializedName("_120_86")
        @Expose
        private String _12086;
        @SerializedName("_47_42")
        @Expose
        private String _4742;
        @SerializedName("_127_72")
        @Expose
        private String _12772;
        @SerializedName("_120_33")
        @Expose
        private String _12033;

        public String get12086() {
            return _12086;
        }

        public void set12086(String _12086) {
            this._12086 = _12086;
        }

        public String get4742() {
            return _4742;
        }

        public void set4742(String _4742) {
            this._4742 = _4742;
        }

        public String get12772() {
            return _12772;
        }

        public void set12772(String _12772) {
            this._12772 = _12772;
        }

        public String get12033() {
            return _12033;
        }

        public void set12033(String _12033) {
            this._12033 = _12033;
        }

    }
}
package com.apitap.model.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by sourcefuse on 12/12/16.
 */

public class ShoppingAsstListBean {

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

        @SerializedName("_122_31")
        @Expose
        private String _12231;
        @SerializedName("_120_157")
        @Expose
        private String _120157;
        @SerializedName("_114_138")
        @Expose
        private String _114138;
        @SerializedName("IL")
        @Expose
        private List<IL> iL = null;

        public String get12231() {
            return _12231;
        }

        public void set12231(String _12231) {
            this._12231 = _12231;
        }

        public String get120157() {
            return _120157;
        }

        public void set120157(String _120157) {
            this._120157 = _120157;
        }

        public String get114138() {
            return _114138;
        }

        public void set114138(String _114138) {
            this._114138 = _114138;
        }

        public List<IL> getIL() {
            return iL;
        }

        public void setIL(List<IL> iL) {
            this.iL = iL;
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
    public class IL {

        @SerializedName("_122_17")
        @Expose
        private String _12217;
        @SerializedName("_120_83")
        @Expose
        private String _12083;

        public String get12217() {
            return _12217;
        }

        public void set12217(String _12217) {
            this._12217 = _12217;
        }

        public String get12083() {
            return _12083;
        }

        public void set12083(String _12083) {
            this._12083 = _12083;
        }

    }
}

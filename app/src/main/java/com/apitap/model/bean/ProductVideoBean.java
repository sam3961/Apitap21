package com.apitap.model.bean;

import java.util.List;

public class ProductVideoBean {


    /**
     * _101 : 010100631
     * _39 : 0000
     * _44 : Transaction Approved
     * _127_41 : 0
     * RESULT : [{"_121_20":"22","_121_15":"videoplayback.mp4","_121_2":"videoplayback_7872561994647454154.mp4"}]
     */

    private String _101;
    private String _39;
    private String _44;
    private int _127_41;
    private List<RESULTBean> RESULT;

    public String get_101() {
        return _101;
    }

    public void set_101(String _101) {
        this._101 = _101;
    }

    public String get_39() {
        return _39;
    }

    public void set_39(String _39) {
        this._39 = _39;
    }

    public String get_44() {
        return _44;
    }

    public void set_44(String _44) {
        this._44 = _44;
    }

    public int get_127_41() {
        return _127_41;
    }

    public void set_127_41(int _127_41) {
        this._127_41 = _127_41;
    }

    public List<RESULTBean> getRESULT() {
        return RESULT;
    }

    public void setRESULT(List<RESULTBean> RESULT) {
        this.RESULT = RESULT;
    }

    public static class RESULTBean {
        /**
         * _121_20 : 22
         * _121_15 : videoplayback.mp4
         * _121_2 : videoplayback_7872561994647454154.mp4
         */

        private String _121_20;
        private String _121_15;
        private String _121_2;

        public String get_121_20() {
            return _121_20;
        }

        public void set_121_20(String _121_20) {
            this._121_20 = _121_20;
        }

        public String get_121_15() {
            return _121_15;
        }

        public void set_121_15(String _121_15) {
            this._121_15 = _121_15;
        }

        public String get_121_2() {
            return _121_2;
        }

        public void set_121_2(String _121_2) {
            this._121_2 = _121_2;
        }
    }
}

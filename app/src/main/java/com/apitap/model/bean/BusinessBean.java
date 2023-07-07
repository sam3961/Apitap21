package com.apitap.model.bean;

import java.util.List;

public class BusinessBean {


    /**
     * _192 : 8f09eaddb545ff7c94b3c7106eede715
     * _11 : 09-10-2019 17:34:27.776 +0000
     * _122_17 : false
     * _122_18 : 313972793934626e
     * RESULT : [{"_101":"010200802","_39":"0000","_44":"Transaction Approved","_127_41":0,"RESULT":[{"_114_93":"725","_120_45":"Retail","_120_44":"1","_122_21":"1"},{"_114_93":"726","_120_45":"Restaurant/Bar","_120_44":"1","_122_21":"1"},{"_114_93":"728","_120_45":"Professional Services","_120_44":"1","_122_21":"1"},{"_114_93":"4945","_120_45":"QATestTOB","_120_44":"1","_122_21":"1"}]}]
     */

    private String _192;
    private String _11;
    private boolean _122_17;
    private String _122_18;
    private List<RESULTBeanX> RESULT;

    public String get_192() {
        return _192;
    }

    public void set_192(String _192) {
        this._192 = _192;
    }

    public String get_11() {
        return _11;
    }

    public void set_11(String _11) {
        this._11 = _11;
    }

    public boolean is_122_17() {
        return _122_17;
    }

    public void set_122_17(boolean _122_17) {
        this._122_17 = _122_17;
    }

    public String get_122_18() {
        return _122_18;
    }

    public void set_122_18(String _122_18) {
        this._122_18 = _122_18;
    }

    public List<RESULTBeanX> getRESULT() {
        return RESULT;
    }

    public void setRESULT(List<RESULTBeanX> RESULT) {
        this.RESULT = RESULT;
    }


    public static class RESULTBeanX {
        /**
         * _101 : 010200802
         * _39 : 0000
         * _44 : Transaction Approved
         * _127_41 : 0
         * RESULT : [{"_114_93":"725","_120_45":"Retail","_120_44":"1","_122_21":"1"},{"_114_93":"726","_120_45":"Restaurant/Bar","_120_44":"1","_122_21":"1"},{"_114_93":"728","_120_45":"Professional Services","_120_44":"1","_122_21":"1"},{"_114_93":"4945","_120_45":"QATestTOB","_120_44":"1","_122_21":"1"}]
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
    }
        public static class RESULTBean {
            /**
             * _114_93 : 725
             * _120_45 : Retail
             * _120_44 : 1
             * _122_21 : 1
             */

            private String _114_93;
            private String _120_45;
            private String _120_44;
            private String _122_21;

            public String get_114_93() {
                return _114_93;
            }

            public void set_114_93(String _114_93) {
                this._114_93 = _114_93;
            }

            public String get_120_45() {
                return _120_45;
            }

            public void set_120_45(String _120_45) {
                this._120_45 = _120_45;
            }

            public String get_120_44() {
                return _120_44;
            }

            public void set_120_44(String _120_44) {
                this._120_44 = _120_44;
            }

            public String get_122_21() {
                return _122_21;
            }

            public void set_122_21(String _122_21) {
                this._122_21 = _122_21;
            }
        }
    }

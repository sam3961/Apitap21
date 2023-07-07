package com.apitap.model.bean;

import java.util.List;

public class ReturnReasonsBean {


    /**
     * _192 : 8f09eaddb545ff7c94b3c7106eede715
     * _11 : 07-15-2019 18:41:13.961 +0000
     * _122_17 : false
     * _122_18 : 39366d7836367277
     * RESULT : [{"_101":"010100576","_39":"0000","_44":"Transaction Approved","_127_41":0,"RESULT":[{"_114_154":"1","_122_5":"Bought by mistake"},{"_114_154":"2","_122_5":"Better price available"},{"_114_154":"3","_122_5":"Product damaged, but shipping box OK"},{"_114_154":"4","_122_5":"Item arrived too late"},{"_114_154":"5","_122_5":"Missing parts or accessories"},{"_114_154":"6","_122_5":"Product and shipping box both damaged"},{"_114_154":"7","_122_5":"Wrong item was sent"},{"_114_154":"8","_122_5":"Item defective or doesnÂ´t work"},{"_114_154":"9","_122_5":"Received extra item I didnÂ´t buy (no refund needed)"},{"_114_154":"10","_122_5":"No longer needed"},{"_114_154":"11","_122_5":"DidnÂ´t approve purchase"},{"_114_154":"12","_122_5":"Inaccurate website description"}]}]
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
         * _101 : 010100576
         * _39 : 0000
         * _44 : Transaction Approved
         * _127_41 : 0
         * RESULT : [{"_114_154":"1","_122_5":"Bought by mistake"},{"_114_154":"2","_122_5":"Better price available"},{"_114_154":"3","_122_5":"Product damaged, but shipping box OK"},{"_114_154":"4","_122_5":"Item arrived too late"},{"_114_154":"5","_122_5":"Missing parts or accessories"},{"_114_154":"6","_122_5":"Product and shipping box both damaged"},{"_114_154":"7","_122_5":"Wrong item was sent"},{"_114_154":"8","_122_5":"Item defective or doesnÂ´t work"},{"_114_154":"9","_122_5":"Received extra item I didnÂ´t buy (no refund needed)"},{"_114_154":"10","_122_5":"No longer needed"},{"_114_154":"11","_122_5":"DidnÂ´t approve purchase"},{"_114_154":"12","_122_5":"Inaccurate website description"}]
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
             * _114_154 : 1
             * _122_5 : Bought by mistake
             */

            private String _114_154;
            private String _122_5;

            public String get_114_154() {
                return _114_154;
            }

            public void set_114_154(String _114_154) {
                this._114_154 = _114_154;
            }

            public String get_122_5() {
                return _122_5;
            }

            public void set_122_5(String _122_5) {
                this._122_5 = _122_5;
            }
        }
    }
}

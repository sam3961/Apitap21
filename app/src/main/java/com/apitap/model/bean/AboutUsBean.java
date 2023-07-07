package com.apitap.model.bean;

import java.util.List;

public class AboutUsBean {

    /**
     * _192 : 8f09eaddb545ff7c94b3c7106eede715
     * _11 : 03-29-2019 20:37:39.688 +0000
     * _122_17 : false
     * _122_18 : 38346a6f36356369
     * RESULT : [{"_101":"010100756","_39":"0000","_44":"Transaction Approved","_127_41":0,"RESULT":[{"_122_25":"85001","_122_26":"4920686176652061207175657374696F6E2061626F7574206D79206163636F756E742E"},{"_122_25":"85002","_122_26":"49206E6565642068656C7020646F696E6720736F6D657468696E672E"},{"_122_25":"85003","_122_26":"492077616E7420746F207375676765737420616464696E67206F6E65206F66206D79206661766F72697465206D65726368616E747320746F204170695461702E"},{"_122_25":"85004","_122_26":"49206861766520612073756767657374696F6E20666F7220686F7720746F20696D70726F7665204170695461702E"},{"_122_25":"85005","_122_26":"49276D20686176696E672070726F626C656D732077697468206D65726368616E742E"},{"_122_25":"85006","_122_26":"492077616E7420746F207265706F727420616E206F6666656E73697665206974656D2C2073657276696365206F72206D65726368616E742E"},{"_122_25":"85007","_122_26":"492077616E7420746F207265706F727420737573706963696F757320616374697669747920696E204170695461702E"},{"_122_25":"85008","_122_26":"49206E65656420736F6D657468696E6720656C73652E"}]}]
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
         * _101 : 010100756
         * _39 : 0000
         * _44 : Transaction Approved
         * _127_41 : 0
         * RESULT : [{"_122_25":"85001","_122_26":"4920686176652061207175657374696F6E2061626F7574206D79206163636F756E742E"},{"_122_25":"85002","_122_26":"49206E6565642068656C7020646F696E6720736F6D657468696E672E"},{"_122_25":"85003","_122_26":"492077616E7420746F207375676765737420616464696E67206F6E65206F66206D79206661766F72697465206D65726368616E747320746F204170695461702E"},{"_122_25":"85004","_122_26":"49206861766520612073756767657374696F6E20666F7220686F7720746F20696D70726F7665204170695461702E"},{"_122_25":"85005","_122_26":"49276D20686176696E672070726F626C656D732077697468206D65726368616E742E"},{"_122_25":"85006","_122_26":"492077616E7420746F207265706F727420616E206F6666656E73697665206974656D2C2073657276696365206F72206D65726368616E742E"},{"_122_25":"85007","_122_26":"492077616E7420746F207265706F727420737573706963696F757320616374697669747920696E204170695461702E"},{"_122_25":"85008","_122_26":"49206E65656420736F6D657468696E6720656C73652E"}]
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
             * _122_25 : 85001
             * _122_26 : 4920686176652061207175657374696F6E2061626F7574206D79206163636F756E742E
             */

            private String _122_25;
            private String _122_26;

            public String get_122_25() {
                return _122_25;
            }

            public void set_122_25(String _122_25) {
                this._122_25 = _122_25;
            }

            public String get_122_26() {
                return _122_26;
            }

            public void set_122_26(String _122_26) {
                this._122_26 = _122_26;
            }
        }
    }
}

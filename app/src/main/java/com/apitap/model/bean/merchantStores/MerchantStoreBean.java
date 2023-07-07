
package com.apitap.model.bean.merchantStores;

import java.util.List;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class MerchantStoreBean {

    @SerializedName("RESULT")
    private List<RESULT> mRESULT;
    @SerializedName("_11")
    private String m_11;
    @SerializedName("_122_17")
    private Boolean m_12217;
    @SerializedName("_122_18")
    private String m_12218;
    @SerializedName("_192")
    private String m_192;

    public List<RESULT> getRESULT() {
        return mRESULT;
    }

    public void setRESULT(List<RESULT> rESULT) {
        mRESULT = rESULT;
    }

    public String get_11() {
        return m_11;
    }

    public void set_11(String _11) {
        m_11 = _11;
    }

    public Boolean get_12217() {
        return m_12217;
    }

    public void set_12217(Boolean _12217) {
        m_12217 = _12217;
    }

    public String get_12218() {
        return m_12218;
    }

    public void set_12218(String _12218) {
        m_12218 = _12218;
    }

    public String get_192() {
        return m_192;
    }

    public void set_192(String _192) {
        m_192 = _192;
    }

}

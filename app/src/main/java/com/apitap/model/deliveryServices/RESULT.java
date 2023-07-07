
package com.apitap.model.deliveryServices;

import java.util.List;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class RESULT {

    @SerializedName("RESULT")
    private List<RESULT> mRESULT;
    @SerializedName("_101")
    private String m_101;
    @SerializedName("_122_39")
    private String m_12239;
    @SerializedName("_127_41")
    private Long m_12741;
    @SerializedName("_39")
    private String m_39;
    @SerializedName("_44")
    private String m_44;

    public List<RESULT> getRESULT() {
        return mRESULT;
    }

    public void setRESULT(List<RESULT> rESULT) {
        mRESULT = rESULT;
    }

    public String get_101() {
        return m_101;
    }

    public void set_101(String _101) {
        m_101 = _101;
    }

    public String get_12239() {
        return m_12239;
    }

    public void set_12239(String _12239) {
        m_12239 = _12239;
    }

    public Long get_12741() {
        return m_12741;
    }

    public void set_12741(Long _12741) {
        m_12741 = _12741;
    }

    public String get_39() {
        return m_39;
    }

    public void set_39(String _39) {
        m_39 = _39;
    }

    public String get_44() {
        return m_44;
    }

    public void set_44(String _44) {
        m_44 = _44;
    }

}

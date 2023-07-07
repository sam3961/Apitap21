
package com.apitap.model.bean.categories;

import java.util.List;
import com.google.gson.annotations.SerializedName;


public class RESULT {

    @SerializedName("CA")
    private List<CA> mCA;
    @SerializedName("RESULT")
    private List<RESULT> mRESULT;
    @SerializedName("_101")
    private String m_101;
    @SerializedName("_114_93")
    private String m_11493;
    @SerializedName("_120_44")
    private String m_12044;
    @SerializedName("_120_45")
    private String m_12045;
    @SerializedName("_122_21")
    private String m_12221;
    @SerializedName("_127_41")
    private Long m_12741;
    @SerializedName("_39")
    private String m_39;
    @SerializedName("_44")
    private String m_44;

    public List<CA> getCA() {
        return mCA;
    }

    public void setCA(List<CA> cA) {
        mCA = cA;
    }

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

    public String get_11493() {
        return m_11493;
    }

    public void set_11493(String _11493) {
        m_11493 = _11493;
    }

    public String get_12044() {
        return m_12044;
    }

    public void set_12044(String _12044) {
        m_12044 = _12044;
    }

    public String get_12045() {
        return m_12045;
    }

    public void set_12045(String _12045) {
        m_12045 = _12045;
    }

    public String get_12221() {
        return m_12221;
    }

    public void set_12221(String _12221) {
        m_12221 = _12221;
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

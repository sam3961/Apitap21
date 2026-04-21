package com.apitap.app.model.merchantCategoryList;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

public class RESULT implements Parcelable {

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
    @SerializedName("_127_111")
    private String m_127111;
    @SerializedName("_127_41")
    private Long m_12741;
    @SerializedName("_127_66")
    private String m_12766;
    @SerializedName("_39")
    private String m_39;
    @SerializedName("_44")
    private String m_44;

    // --- Getters and Setters ---
    public List<RESULT> getRESULT() { return mRESULT; }
    public void setRESULT(List<RESULT> RESULT) { this.mRESULT = RESULT; }

    public String get_101() { return m_101; }
    public void set_101(String _101) { this.m_101 = _101; }

    public String get_11493() { return m_11493; }
    public void set_11493(String _11493) { this.m_11493 = _11493; }

    public String get_12044() { return m_12044; }
    public void set_12044(String _12044) { this.m_12044 = _12044; }

    public String get_12045() { return m_12045; }
    public void set_12045(String _12045) { this.m_12045 = _12045; }

    public String get_12221() { return m_12221; }
    public void set_12221(String _12221) { this.m_12221 = _12221; }

    public String get_127111() { return m_127111; }
    public void set_127111(String _127111) { this.m_127111 = _127111; }

    public Long get_12741() { return m_12741; }
    public void set_12741(Long _12741) { this.m_12741 = _12741; }

    public String get_12766() { return m_12766; }
    public void set_12766(String _12766) { this.m_12766 = _12766; }

    public String get_39() { return m_39; }
    public void set_39(String _39) { this.m_39 = _39; }

    public String get_44() { return m_44; }
    public void set_44(String _44) { this.m_44 = _44; }

    // --- Parcelable Implementation ---
    protected RESULT(Parcel in) {
        mRESULT = new ArrayList<>();
        in.readList(mRESULT, RESULT.class.getClassLoader());
        m_101 = in.readString();
        m_11493 = in.readString();
        m_12044 = in.readString();
        m_12045 = in.readString();
        m_12221 = in.readString();
        m_127111 = in.readString();
        if (in.readByte() == 0) {
            m_12741 = null;
        } else {
            m_12741 = in.readLong();
        }
        m_12766 = in.readString();
        m_39 = in.readString();
        m_44 = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(mRESULT);
        dest.writeString(m_101);
        dest.writeString(m_11493);
        dest.writeString(m_12044);
        dest.writeString(m_12045);
        dest.writeString(m_12221);
        dest.writeString(m_127111);
        if (m_12741 == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(m_12741);
        }
        dest.writeString(m_12766);
        dest.writeString(m_39);
        dest.writeString(m_44);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<RESULT> CREATOR = new Creator<RESULT>() {
        @Override
        public RESULT createFromParcel(Parcel in) {
            return new RESULT(in);
        }

        @Override
        public RESULT[] newArray(int size) {
            return new RESULT[size];
        }
    };
}

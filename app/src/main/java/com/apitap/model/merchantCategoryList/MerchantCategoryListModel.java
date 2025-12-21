package com.apitap.model.merchantCategoryList;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class MerchantCategoryListModel implements Parcelable {

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

    // --- Getters and Setters ---
    public List<RESULT> getRESULT() { return mRESULT; }
    public void setRESULT(List<RESULT> rESULT) { mRESULT = rESULT; }

    public String get_11() { return m_11; }
    public void set_11(String _11) { m_11 = _11; }

    public Boolean get_12217() { return m_12217; }
    public void set_12217(Boolean _12217) { m_12217 = _12217; }

    public String get_12218() { return m_12218; }
    public void set_12218(String _12218) { m_12218 = _12218; }

    public String get_192() { return m_192; }
    public void set_192(String _192) { m_192 = _192; }

    // --- Parcelable implementation ---
    protected MerchantCategoryListModel(Parcel in) {
        m_11 = in.readString();
        byte tmp = in.readByte();
        m_12217 = tmp == 0 ? null : tmp == 1;
        m_12218 = in.readString();
        m_192 = in.readString();
        mRESULT = in.createTypedArrayList(RESULT.CREATOR);
    }

    public static final Creator<MerchantCategoryListModel> CREATOR = new Creator<MerchantCategoryListModel>() {
        @Override
        public MerchantCategoryListModel createFromParcel(Parcel in) {
            return new MerchantCategoryListModel(in);
        }

        @Override
        public MerchantCategoryListModel[] newArray(int size) {
            return new MerchantCategoryListModel[size];
        }
    };

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(m_11);
        parcel.writeByte((byte) (m_12217 == null ? 0 : m_12217 ? 1 : 2));
        parcel.writeString(m_12218);
        parcel.writeString(m_192);
        parcel.writeTypedList(mRESULT);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}

package com.apitap.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sourcefuse on 12/12/16.
 */

public class GetCardBean implements Parcelable{

    private String card_id;
    private String name;
    private String card_number;
    private String card_type;
    private String card_add;
    private String merchantName;
    private String AdName;



    public GetCardBean(){

    }

    protected GetCardBean(Parcel in) {
        card_id = in.readString();
        name = in.readString();
        card_number = in.readString();
        card_type = in.readString();
        card_add = in.readString();
    }

    public static final Creator<GetCardBean> CREATOR = new Creator<GetCardBean>() {
        @Override
        public GetCardBean createFromParcel(Parcel in) {
            return new GetCardBean(in);
        }

        @Override
        public GetCardBean[] newArray(int size) {
            return new GetCardBean[size];
        }
    };

    public String getCard_add() {
        return card_add;
    }

    public void setCard_add(String card_add) {
        this.card_add = card_add;
    }

    public String getCard_token() {
        return card_id;
    }

    public void setCard_token(String id) {
        this.card_id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }





    public String getCard_number() {
        return card_number;
    }

    public void setCard_number(String actualPrice) {
        this.card_number = actualPrice;
    }

    public String getCard_type() {
        return card_type;
    }

    public void setCard_type(String priceAfterDiscount) {
        this.card_type = priceAfterDiscount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(card_id);
        parcel.writeString(name);
        parcel.writeString(card_number);
        parcel.writeString(card_type);
        parcel.writeString(card_add);
    }
}

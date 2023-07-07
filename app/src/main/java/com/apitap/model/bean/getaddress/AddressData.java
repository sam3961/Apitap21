
package com.apitap.model.bean.getaddress;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AddressData implements Serializable{

    @SerializedName("_114_115")
    @Expose
    private String addressId;
    @SerializedName("_114_53")
    @Expose
    private String address_type;
    @SerializedName("CI")
    @Expose
    private CI cI;
    @SerializedName("CO")
    @Expose
    private CO cO;
    @SerializedName("ST")
    @Expose
    private ST sT;
    @SerializedName("ZP")
    @Expose
    private ZP zP;
    @SerializedName("_114_18")
    @Expose
    private String phone;
    @SerializedName("_114_12")
    @Expose
    private String line1;
    @SerializedName("_114_13")
    @Expose
    private String line2;

    private boolean checked;

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress_type() {
        return address_type;
    }

    public void setAddress_type(String address_type) {
        this.address_type = address_type;
    }

    public CI getcI() {
        return cI;
    }

    public void setcI(CI cI) {
        this.cI = cI;
    }

    public CO getcO() {
        return cO;
    }

    public void setcO(CO cO) {
        this.cO = cO;
    }

    public ST getsT() {
        return sT;
    }

    public void setsT(ST sT) {
        this.sT = sT;
    }

    public ZP getzP() {
        return zP;
    }

    public void setzP(ZP zP) {
        this.zP = zP;
    }

    public String getLine1() {
        return line1;
    }

    public void setLine1(String line1) {
        this.line1 = line1;
    }

    public String getLine2() {
        return line2;
    }

    public void setLine2(String line2) {
        this.line2 = line2;
    }

    public class CI implements Serializable{

        @SerializedName("_47_15")
        @Expose
        private String city;

        @SerializedName("_114_14")
        @Expose
        private String cityId;

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getCityId() {
            return cityId;
        }

        public void setCityId(String cityId) {
            this.cityId = cityId;
        }
    }

    public class CO implements Serializable{

        @SerializedName("_47_18")
        @Expose
        private String country;

        @SerializedName("_122_87")
        @Expose
        private String countryId;

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getCountryId() {
            return countryId;
        }

        public void setCountryId(String countryId) {
            this.countryId = countryId;
        }
    }

    public class ST implements Serializable{


        @SerializedName("_47_16")
        @Expose
        private String state;

        @SerializedName("_120_13")
        @Expose
        private String stateId;

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getStateId() {
            return stateId;
        }

        public void setStateId(String stateId) {
            this.stateId = stateId;
        }
    }

    public class ZP implements Serializable{

        @SerializedName("_47_17")
        @Expose
        private String zipcode;

        @SerializedName("_122_107")
        @Expose
        private String zipcodeId;

        public String getZipcode() {
            return zipcode;
        }

        public void setZipcode(String zipcode) {
            this.zipcode = zipcode;
        }

        public String getZipcodeId() {
            return zipcodeId;
        }

        public void setZipcodeId(String zipcodeId) {
            this.zipcodeId = zipcodeId;
        }
    }


}
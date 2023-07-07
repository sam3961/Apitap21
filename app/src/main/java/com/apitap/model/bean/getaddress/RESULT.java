
package com.apitap.model.bean.getaddress;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class RESULT {


    @SerializedName("_44")
    @Expose
    private String status;

    @SerializedName("RESULT")
    @Expose
    private List<RESULT_> rESULT = new ArrayList<RESULT_>();


    public List<RESULT_> getRESULT() {
        return rESULT;
    }

    public void setRESULT(List<RESULT_> rESULT) {
        this.rESULT = rESULT;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public class RESULT_ {

        @SerializedName("AD")
        @Expose
        private List<AddressData> addresses = new ArrayList<AddressData>();

        public List<AddressData> getAddresses() {
            return addresses;
        }

        public void setAddresses(List<AddressData> addresses) {
            this.addresses = addresses;
        }

        @SerializedName("CO")
        @Expose
        private List<CountryData> countries = new ArrayList<>();

        public List<CountryData> getCountries() {
            return countries;
        }

        public void setCountries(List<CountryData> countries) {
            this.countries = countries;
        }

        @SerializedName("ST")
        @Expose
        private List<StateData> states = new ArrayList<>();

        public List<StateData> getStates() {
            return states;
        }

        public void setStates(List<StateData> states) {
            this.states = states;
        }

        @SerializedName("CI")
        @Expose
        private List<CityData> cities = new ArrayList<>();

        public List<CityData> getCities() {
            return cities;
        }

        public void setCities(List<CityData> cities) {
            this.cities = cities;
        }

        @SerializedName("ZP")
        @Expose
        private List<PINCodeData> pincodes = new ArrayList<>();

        public List<PINCodeData> getPincodes() {
            return pincodes;
        }

        public void setPincodes(List<PINCodeData> pincodes) {
            this.pincodes = pincodes;
        }
    }
}

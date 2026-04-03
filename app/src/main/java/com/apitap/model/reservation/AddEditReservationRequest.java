package com.apitap.model.reservation;

import com.google.gson.annotations.SerializedName;

public class AddEditReservationRequest {

    @SerializedName("note")
    private String note;

    @SerializedName("userCreatorId")
    private int userCreatorId;

    @SerializedName("smokingFlag")
    private boolean smokingFlag = false;

    @SerializedName("childFlag")
    private boolean childFlag = false;

    @SerializedName("consumerPhone")
    private String consumerPhone;

    @SerializedName("reservationDate")
    private String reservationDate;

    @SerializedName("id")
    private Integer reservationId;

    @SerializedName("endHour")
    private String endHour = "";

    @SerializedName("companyId")
    private String companyId;

    @SerializedName("areaId")
    private int areaId;

    @SerializedName("childQty")
    private int childQty = 0;

    @SerializedName("wheelchairFlag")
    private boolean wheelchairFlag = false;

    @SerializedName("statusId")
    private int statusId = 1;

    @SerializedName("startHour")
    private String startHour;

    @SerializedName("locationId")
    private int locationId;

    @SerializedName("peopleQty")
    private int peopleQty;

    @SerializedName("consumerEmail")
    private String consumerEmail;

    @SerializedName("consumerName")
    private String consumerName;

    public String getNote() {
        return note;
    }

    public int getUserCreatorId() {
        return userCreatorId;
    }

    public boolean isSmokingFlag() {
        return smokingFlag;
    }

    public boolean isChildFlag() {
        return childFlag;
    }

    public String getConsumerPhone() {
        return consumerPhone;
    }

    public String getReservationDate() {
        return reservationDate;
    }

    public String getEndHour() {
        return endHour;
    }

    public String getCompanyId() {
        return companyId;
    }

    public int getAreaId() {
        return areaId;
    }

    public int getChildQty() {
        return childQty;
    }

    public boolean isWheelchairFlag() {
        return wheelchairFlag;
    }

    public int getStatusId() {
        return statusId;
    }

    public String getStartHour() {
        return startHour;
    }

    public int getLocationId() {
        return locationId;
    }

    public int getPeopleQty() {
        return peopleQty;
    }

    public String getConsumerEmail() {
        return consumerEmail;
    }

    public String getConsumerName() {
        return consumerName;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setUserCreatorId(int userCreatorId) {
        this.userCreatorId = userCreatorId;
    }

    public void setSmokingFlag(boolean smokingFlag) {
        this.smokingFlag = smokingFlag;
    }

    public void setChildFlag(boolean childFlag) {
        this.childFlag = childFlag;
    }

    public void setConsumerPhone(String consumerPhone) {
        this.consumerPhone = consumerPhone;
    }

    public void setReservationDate(String reservationDate) {
        this.reservationDate = reservationDate;
    }

    public void setEndHour(String endHour) {
        this.endHour = endHour;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public void setAreaId(int areaId) {
        this.areaId = areaId;
    }

    public void setChildQty(int childQty) {
        this.childQty = childQty;
    }

    public void setWheelchairFlag(boolean wheelchairFlag) {
        this.wheelchairFlag = wheelchairFlag;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public void setStartHour(String startHour) {
        this.startHour = startHour;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public void setPeopleQty(int peopleQty) {
        this.peopleQty = peopleQty;
    }

    public void setConsumerEmail(String consumerEmail) {
        this.consumerEmail = consumerEmail;
    }

    public void setConsumerName(String consumerName) {
        this.consumerName = consumerName;
    }

    public void setReservationId(Integer reservationId) {
        this.reservationId = reservationId;
    }

}
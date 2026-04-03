package com.apitap.model.reservation;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ViewReservationResponseItem implements Serializable {

	@SerializedName("note")
	private String note;

	@SerializedName("addresSecond")
	private String addresSecond;

	@SerializedName("smokingFlag")
	private boolean smokingFlag;

	@SerializedName("companyName")
	private String companyName;

	@SerializedName("consumerPhone")
	private String consumerPhone;

	@SerializedName("reservationDate")
	private String reservationDate;

	@SerializedName("childQty")
	private int childQty;

	@SerializedName("reservationId")
	private int reservationId;

	@SerializedName("areaName")
	private String areaName;

	@SerializedName("locationId")
	private int locationId;

	@SerializedName("storeName")
	private String storeName;

	@SerializedName("id")
	private int id;

	@SerializedName("userCreatorId")
	private int userCreatorId;

	@SerializedName("childFlag")
	private boolean childFlag;

	@SerializedName("addresFirst")
	private String addresFirst;

	@SerializedName("zipcode")
	private String zipcode;

	@SerializedName("endHour")
	private String endHour;

	@SerializedName("companyId")
	private int companyId;

	@SerializedName("areaId")
	private int areaId;

	@SerializedName("wheelchairFlag")
	private boolean wheelchairFlag;

	@SerializedName("statusId")
	private int statusId;

	@SerializedName("startHour")
	private String startHour;

	@SerializedName("peopleQty")
	private int peopleQty;

	@SerializedName("consumerEmail")
	private String consumerEmail;

	@SerializedName("consumerName")
	private String consumerName;

	public String getNote(){
		return note;
	}

	public String getAddresSecond(){
		return addresSecond;
	}

	public boolean isSmokingFlag(){
		return smokingFlag;
	}

	public String getCompanyName(){
		return companyName;
	}

	public String getConsumerPhone(){
		return consumerPhone;
	}

	public String getReservationDate(){
		return reservationDate;
	}

	public int getChildQty(){
		return childQty;
	}

	public int getReservationId(){
		return reservationId;
	}

	public String getAreaName(){
		return areaName;
	}

	public int getLocationId(){
		return locationId;
	}

	public String getStoreName(){
		return storeName;
	}

	public int getId(){
		return id;
	}

	public int getUserCreatorId(){
		return userCreatorId;
	}

	public boolean isChildFlag(){
		return childFlag;
	}

	public String getAddresFirst(){
		return addresFirst;
	}

	public String getZipcode(){
		return zipcode;
	}

	public String getEndHour(){
		return endHour;
	}

	public int getCompanyId(){
		return companyId;
	}

	public int getAreaId(){
		return areaId;
	}

	public boolean isWheelchairFlag(){
		return wheelchairFlag;
	}

	public int getStatusId(){
		return statusId;
	}

	public String getStartHour(){
		return startHour;
	}

	public int getPeopleQty(){
		return peopleQty;
	}

	public String getConsumerEmail(){
		return consumerEmail;
	}

	public String getConsumerName(){
		return consumerName;
	}
}
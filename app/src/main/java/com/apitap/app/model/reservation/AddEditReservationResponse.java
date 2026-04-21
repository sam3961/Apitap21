package com.apitap.app.model.reservation;

import com.google.gson.annotations.SerializedName;

public class AddEditReservationResponse{

	@SerializedName("note")
	private String note;

	@SerializedName("addresSecond")
	private Object addresSecond;

	@SerializedName("smokingFlag")
	private boolean smokingFlag;

	@SerializedName("companyName")
	private Object companyName;

	@SerializedName("consumerPhone")
	private String consumerPhone;

	@SerializedName("reservationDate")
	private String reservationDate;

	@SerializedName("childQty")
	private int childQty;

	@SerializedName("reservationId")
	private int reservationId;

	@SerializedName("areaName")
	private Object areaName;

	@SerializedName("locationId")
	private int locationId;

	@SerializedName("storeName")
	private Object storeName;

	@SerializedName("id")
	private int id;

	@SerializedName("userCreatorId")
	private int userCreatorId;

	@SerializedName("childFlag")
	private boolean childFlag;

	@SerializedName("addresFirst")
	private Object addresFirst;

	@SerializedName("zipcode")
	private Object zipcode;

	@SerializedName("endHour")
	private String endHour;

	@SerializedName("companyId")
	private Object companyId;

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

	public Object getAddresSecond(){
		return addresSecond;
	}

	public boolean isSmokingFlag(){
		return smokingFlag;
	}

	public Object getCompanyName(){
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

	public Object getAreaName(){
		return areaName;
	}

	public int getLocationId(){
		return locationId;
	}

	public Object getStoreName(){
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

	public Object getAddresFirst(){
		return addresFirst;
	}

	public Object getZipcode(){
		return zipcode;
	}

	public String getEndHour(){
		return endHour;
	}

	public Object getCompanyId(){
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
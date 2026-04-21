package com.apitap.app.model.reservation;

import com.google.gson.annotations.SerializedName;

public class ViewReservationRequest{

	@SerializedName("userCreatorId")
	private String userCreatorId;

	@SerializedName("endDate")
	private String endDate;

	@SerializedName("page")
	private int page;

	@SerializedName("sort")
	private String sort;

	@SerializedName("startDate")
	private String startDate;

	public String getUserCreatorId(){
		return userCreatorId;
	}

	public String getEndDate(){
		return endDate;
	}

	public int getPage(){
		return page;
	}

	public String getSort(){
		return sort;
	}

	public String getStartDate(){
		return startDate;
	}

	public void setUserCreatorId(String userCreatorId) {
		this.userCreatorId = userCreatorId;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
}
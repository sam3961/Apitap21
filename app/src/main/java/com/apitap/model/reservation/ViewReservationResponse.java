package com.apitap.model.reservation;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ViewReservationResponse{

	@SerializedName("ViewReservationResponse")
	private List<ViewReservationResponseItem> viewReservationResponse;

	public List<ViewReservationResponseItem> getViewReservationResponse(){
		return viewReservationResponse;
	}
}
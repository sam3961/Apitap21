package com.apitap.model.home.address;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class RESULTItem{

	@SerializedName("AD")
	private List<ADItem> aD;

	public List<ADItem> getAD(){
		return aD;
	}
}
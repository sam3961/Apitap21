package com.apitap.model.storeFrontItems.browseCategory;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class RESULTItem{

	@SerializedName("_120_45")
	private String jsonMember12045;

	@SerializedName("_120_44")
	private String jsonMember12044;

	@SerializedName("_122_21")
	private String jsonMember12221;

	@SerializedName("_114_93")
	private String jsonMember11493;

	@SerializedName("CA")
	private List<CAItem> cA;

	public String getJsonMember12045(){
		return jsonMember12045;
	}

	public String getJsonMember12044(){
		return jsonMember12044;
	}

	public String getJsonMember12221(){
		return jsonMember12221;
	}

	public String getJsonMember11493(){
		return jsonMember11493;
	}

	public List<CAItem> getCA(){
		return cA;
	}
}
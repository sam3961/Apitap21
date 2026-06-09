package com.apitap.app.model.itemModel.items;

import com.google.gson.annotations.SerializedName;

public class LOItem{

	@SerializedName("_120_38")
	private String jsonMember12038;

	@SerializedName("_120_39")
	private String jsonMember12039;

	@SerializedName("_114_47")
	private String jsonMember11447;

	@SerializedName("_114_70")
	private String jsonMember11470;

	public String getJsonMember12038(){
		return jsonMember12038;
	}

	public String getJsonMember12039(){
		return jsonMember12039;
	}

	public String getJsonMember11447(){
		return jsonMember11447;
	}

	public String getJsonMember11470(){
		return jsonMember11470;
	}
}
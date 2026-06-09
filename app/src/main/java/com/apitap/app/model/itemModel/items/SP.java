package com.apitap.app.model.itemModel.items;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class SP{

	@SerializedName("SL")
	private List<SLItem> sL;

	@SerializedName("_114_139")
	private String jsonMember114139;

	@SerializedName("_114_138")
	private String jsonMember114138;

	public List<SLItem> getSL(){
		return sL;
	}

	public String getJsonMember114139(){
		return jsonMember114139;
	}

	public String getJsonMember114138(){
		return jsonMember114138;
	}
}
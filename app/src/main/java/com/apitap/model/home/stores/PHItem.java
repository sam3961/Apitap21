package com.apitap.model.home.stores;

import com.google.gson.annotations.SerializedName;

public class PHItem{

	@SerializedName("_121_167")
	private String jsonMember121167;

	@SerializedName("_48_28")
	private String jsonMember4828;

	@SerializedName("_122_88")
	private String jsonMember12288;

	@SerializedName("_53")
	private String jsonMember53;

	@SerializedName("CO")
	private CO cO;

	@SerializedName("_121_62")
	private String jsonMember12162;

	public String getJsonMember121167(){
		return jsonMember121167;
	}

	public String getJsonMember4828(){
		return jsonMember4828;
	}

	public String getJsonMember12288(){
		return jsonMember12288;
	}

	public String getJsonMember53(){
		return jsonMember53;
	}

	public CO getCO(){
		return cO;
	}

	public String getJsonMember12162(){
		return jsonMember12162;
	}
}
package com.apitap.model.storeFrontItems.favourites;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class RESULTItem{

	@SerializedName("_127_66")
	private String jsonMember12766;

	@SerializedName("_127_56")
	private String jsonMember12756;

	@SerializedName("_114_121")
	private String jsonMember114121;

	@SerializedName("CU")
	private List<CUItem> cU;

	@SerializedName("_114_70")
	private String jsonMember11470;

	@SerializedName("_121_170")
	private String jsonMember121170;

	@SerializedName("_114_179")
	private String jsonMember114179;

	@SerializedName("_121_140")
	private String jsonMember121140;

	public String getJsonMember12766(){
		return jsonMember12766;
	}

	public String getJsonMember12756(){
		return jsonMember12756;
	}

	public String getJsonMember114121(){
		return jsonMember114121;
	}

	public List<CUItem> getCU(){
		return cU;
	}

	public String getJsonMember11470(){
		return jsonMember11470;
	}

	public String getJsonMember121170(){
		return jsonMember121170;
	}

	public String getJsonMember114179(){
		return jsonMember114179;
	}

	public String getJsonMember121140(){
		return jsonMember121140;
	}
}
package com.apitap.model.storeAbout;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class StoreAboutResponse{

	@SerializedName("_192")
	private String jsonMember192;

	@SerializedName("_122_18")
	private String jsonMember12218;

	@SerializedName("_122_17")
	private boolean jsonMember12217;

	@SerializedName("RESULT")
	private List<RESULTItem> rESULT;

	@SerializedName("_11")
	private String jsonMember11;

	public String getJsonMember192(){
		return jsonMember192;
	}

	public String getJsonMember12218(){
		return jsonMember12218;
	}

	public boolean isJsonMember12217(){
		return jsonMember12217;
	}

	public List<RESULTItem> getRESULT(){
		return rESULT;
	}

	public String getJsonMember11(){
		return jsonMember11;
	}
}
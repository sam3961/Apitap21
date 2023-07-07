package com.apitap.model.headerCategory;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class RESULTItem{

	@SerializedName("_39")
	private String jsonMember39;

	@SerializedName("_101")
	private String jsonMember101;

	@SerializedName("_127_41")
	private int jsonMember12741;

	@SerializedName("RESULT")
	private List<RESULTItem> rESULT;

	@SerializedName("_44")
	private String jsonMember44;

	@SerializedName("_114_53")
	private String jsonMember11453;

	@SerializedName("_122_21")
	private String jsonMember12221;

	@SerializedName("_120_44")
	private String jsonMember12044;

	@SerializedName("_114_93")
	private String jsonMember11493;

	public String getJsonMember39(){
		return jsonMember39;
	}

	public String getJsonMember101(){
		return jsonMember101;
	}

	public int getJsonMember12741(){
		return jsonMember12741;
	}

	public List<RESULTItem> getRESULT(){
		return rESULT;
	}

	public String getJsonMember44(){
		return jsonMember44;
	}

	public String getJsonMember11453(){
		return jsonMember11453;
	}

	public String getJsonMember12221(){
		return jsonMember12221;
	}

	public String getJsonMember12044(){
		return jsonMember12044;
	}

	public String getJsonMember11493(){
		return jsonMember11493;
	}
}
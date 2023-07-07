package com.apitap.model.seatingAreaByLocation;

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

	@SerializedName("_115_11")
	private String jsonMember11511;

	@SerializedName("_115_13")
	private String jsonMember11513;

	@SerializedName("_115_12")
	private String jsonMember11512;

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

	public String getJsonMember11511(){
		return jsonMember11511;
	}

	public String getJsonMember11513(){
		return jsonMember11513;
	}

	public String getJsonMember11512(){
		return jsonMember11512;
	}
}
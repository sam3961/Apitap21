package com.apitap.controller;

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

	public void setJsonMember39(String jsonMember39){
		this.jsonMember39 = jsonMember39;
	}

	public String getJsonMember39(){
		return jsonMember39;
	}

	public void setJsonMember101(String jsonMember101){
		this.jsonMember101 = jsonMember101;
	}

	public String getJsonMember101(){
		return jsonMember101;
	}

	public void setJsonMember12741(int jsonMember12741){
		this.jsonMember12741 = jsonMember12741;
	}

	public int getJsonMember12741(){
		return jsonMember12741;
	}

	public void setRESULT(List<RESULTItem> rESULT){
		this.rESULT = rESULT;
	}

	public List<RESULTItem> getRESULT(){
		return rESULT;
	}

	public void setJsonMember44(String jsonMember44){
		this.jsonMember44 = jsonMember44;
	}

	public String getJsonMember44(){
		return jsonMember44;
	}

	public void setJsonMember11511(String jsonMember11511){
		this.jsonMember11511 = jsonMember11511;
	}

	public String getJsonMember11511(){
		return jsonMember11511;
	}

	public void setJsonMember11513(String jsonMember11513){
		this.jsonMember11513 = jsonMember11513;
	}

	public String getJsonMember11513(){
		return jsonMember11513;
	}

	public void setJsonMember11512(String jsonMember11512){
		this.jsonMember11512 = jsonMember11512;
	}

	public String getJsonMember11512(){
		return jsonMember11512;
	}
}
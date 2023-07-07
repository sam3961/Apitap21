package com.apitap.model.tablesBySeatingArea;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class TablesBySeatingAreaResponse{

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

	public void setJsonMember192(String jsonMember192){
		this.jsonMember192 = jsonMember192;
	}

	public String getJsonMember192(){
		return jsonMember192;
	}

	public void setJsonMember12218(String jsonMember12218){
		this.jsonMember12218 = jsonMember12218;
	}

	public String getJsonMember12218(){
		return jsonMember12218;
	}

	public void setJsonMember12217(boolean jsonMember12217){
		this.jsonMember12217 = jsonMember12217;
	}

	public boolean isJsonMember12217(){
		return jsonMember12217;
	}

	public void setRESULT(List<RESULTItem> rESULT){
		this.rESULT = rESULT;
	}

	public List<RESULTItem> getRESULT(){
		return rESULT;
	}

	public void setJsonMember11(String jsonMember11){
		this.jsonMember11 = jsonMember11;
	}

	public String getJsonMember11(){
		return jsonMember11;
	}
}
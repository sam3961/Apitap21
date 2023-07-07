package com.apitap.model.addReservation;

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

	@SerializedName("_115_22")
	private String jsonMember11522;

	@SerializedName("_116_200")
	private String jsonMember116200;

	@SerializedName("_114_70")
	private String jsonMember11470;

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

	public String getJsonMember11522(){
		return jsonMember11522;
	}

	public String getJsonMember116200(){
		return jsonMember116200;
	}

	public String getJsonMember11470(){
		return jsonMember11470;
	}
}
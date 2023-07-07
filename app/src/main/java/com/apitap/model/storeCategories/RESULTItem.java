package com.apitap.model.storeCategories;

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

	@SerializedName("_127_66")
	private String jsonMember12766;

	@SerializedName("_114_121")
	private String jsonMember114121;

	@SerializedName("_120_45")
	private String jsonMember12045;

	@SerializedName("ME")
	private List<MEItem> mE;

	@SerializedName("_120_44")
	private String jsonMember12044;

	@SerializedName("_122_21")
	private String jsonMember12221;

	@SerializedName("_114_93")
	private String jsonMember11493;

	@SerializedName("_127_111")
	private String jsonMember127111;

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

	public String getJsonMember12766(){
		return jsonMember12766;
	}

	public String getJsonMember114121(){
		return jsonMember114121;
	}

	public String getJsonMember12045(){
		return jsonMember12045;
	}

	public List<MEItem> getME(){
		return mE;
	}

	public String getJsonMember12044(){
		return jsonMember12044;
	}

	public String getJsonMember12221(){
		return jsonMember12221;
	}

	public String getJsonMember11493(){
		return jsonMember11493;
	}

	public String getJsonMember127111(){
		return jsonMember127111;
	}
}
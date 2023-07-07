package com.apitap.model.bean.itemStoreFront;

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

	@SerializedName("_114_53")
	private String jsonMember11453;

	@SerializedName("_114_121")
	private String jsonMember114121;

	@SerializedName("PC")
	private List<PCItem> pC;

	@SerializedName("_120_44")
	private String jsonMember12044;

	@SerializedName("_122_21")
	private String jsonMember12221;

	@SerializedName("_114_93")
	private String jsonMember11493;

	@SerializedName("_127_111")
	private String jsonMember127111;
	@SerializedName("_114_8")
	private String jsonMember1148;

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

	public String getJsonMember11453(){
		return jsonMember11453;
	}

	public String getJsonMember114121(){
		return jsonMember114121;
	}

	public List<PCItem> getPC(){
		return pC;
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

	public String getJsonMember1148() {
		return jsonMember1148;
	}
}
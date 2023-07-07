package com.apitap.model.home.stores;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class RESULTItem{

	@SerializedName("_127_66")
	private String jsonMember12766;

	@SerializedName("_114_11")
	private String jsonMember11411;

	@SerializedName("LO")
	private LO lO;

	@SerializedName("AD")
	private List<ADItem> aD;

	@SerializedName("_47_8")
	private String jsonMember478;

	@SerializedName("PH")
	private List<PHItem> pH;

	@SerializedName("_114_70")
	private String jsonMember11470;

	@SerializedName("_53")
	private String jsonMember53;

	@SerializedName("_121_170")
	private String jsonMember121170;

	@SerializedName("CA")
	private List<CAItem> cA;

	public String getJsonMember12766(){
		return jsonMember12766;
	}

	public String getJsonMember11411(){
		return jsonMember11411;
	}

	public LO getLO(){
		return lO;
	}

	public List<ADItem> getAD(){
		return aD;
	}

	public String getJsonMember478(){
		return jsonMember478;
	}

	public List<PHItem> getPH(){
		return pH;
	}

	public String getJsonMember11470(){
		return jsonMember11470;
	}

	public String getJsonMember53(){
		return jsonMember53;
	}

	public String getJsonMember121170(){
		return jsonMember121170;
	}

	public List<CAItem> getCA(){
		return cA;
	}
}
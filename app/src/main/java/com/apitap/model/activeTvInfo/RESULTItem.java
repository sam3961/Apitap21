package com.apitap.model.activeTvInfo;

import java.util.List;

import com.apitap.model.Utils;
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

	@SerializedName("_121_141")
	private String jsonMember121141;

	@SerializedName("_122_170")
	private String jsonMember122170;

	@SerializedName("_114_47")
	private String jsonMember11447;

	@SerializedName("_120_103")
	private String jsonMember120103;

	@SerializedName("_120_69")
	private String jsonMember12069;

	@SerializedName("_122_126")
	private String jsonMember122126;

	@SerializedName("_114_70")
	private String jsonMember11470;

	@SerializedName("_122_123")
	private String jsonMember122123;

	@SerializedName("_114_9")
	private String jsonMember1149;

	@SerializedName("_121_183")
	private String jsonMember121183;

	@SerializedName("_124_152")
	private String jsonMember124152;

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

	public String getJsonMember121141(){
		return jsonMember121141;
	}

	public String getJsonMember122170(){
		return jsonMember122170;
	}

	public String getJsonMember11447(){
		return jsonMember11447;
	}

	public String getJsonMember120103(){
		return jsonMember120103;
	}

	public String getJsonMember12069(){
		return jsonMember12069;
	}

	public String getTerminalName(){
		return Utils.hexToASCII(jsonMember122126);
	}

	public String getLocationName(){
		return jsonMember11470;
	}

	public String getJsonMember122123(){
		return jsonMember122123;
	}

	public String getJsonMember1149(){
		return jsonMember1149;
	}

	public String getJsonMember121183(){
		return jsonMember121183;
	}

	public String getJsonMember127111(){
		return jsonMember127111;
	}

    public String getJsonMember124152() {
        return jsonMember124152;
    }
}
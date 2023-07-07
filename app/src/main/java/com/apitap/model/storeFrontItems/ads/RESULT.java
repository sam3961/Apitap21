package com.apitap.model.storeFrontItems.ads;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RESULT {

@SerializedName("_114_93")
@Expose
private String _11493;
@SerializedName("_120_45")
@Expose
private String _12045;
@SerializedName("_120_44")
@Expose
private String _12044;
@SerializedName("_122_21")
@Expose
private String _12221;
@SerializedName("_127_66")
@Expose
private String _12766;
@SerializedName("_127_111")
@Expose
private String _127111;
@SerializedName("_120_83")
@Expose
private String _12083;
@SerializedName("AD")
@Expose
private List<AD> aD = null;

public String get11493() {
return _11493;
}

public void set11493(String _11493) {
this._11493 = _11493;
}

public String get12045() {
return _12045;
}

public void set12045(String _12045) {
this._12045 = _12045;
}

public String get12044() {
return _12044;
}

public void set12044(String _12044) {
this._12044 = _12044;
}

public String get12221() {
return _12221;
}

public void set12221(String _12221) {
this._12221 = _12221;
}

public String get12766() {
return _12766;
}

public void set12766(String _12766) {
this._12766 = _12766;
}

public String get127111() {
return _127111;
}

public void set127111(String _127111) {
this._127111 = _127111;
}

public String get12083() {
return _12083;
}

public void set12083(String _12083) {
this._12083 = _12083;
}

public List<AD> getAD() {
return aD;
}

public void setAD(List<AD> aD) {
this.aD = aD;
}

}
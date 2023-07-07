package com.apitap.model.storeFrontItems.ads;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StoreAdsResponse {

@SerializedName("_101")
@Expose
private String _101;
@SerializedName("_39")
@Expose
private String _39;
@SerializedName("_44")
@Expose
private String _44;
@SerializedName("_127_41")
@Expose
private Integer _12741;
@SerializedName("RESULT")
@Expose
private List<RESULT> rESULT = null;

public String get101() {
return _101;
}

public void set101(String _101) {
this._101 = _101;
}

public String get39() {
return _39;
}

public void set39(String _39) {
this._39 = _39;
}

public String get44() {
return _44;
}

public void set44(String _44) {
this._44 = _44;
}

public Integer get12741() {
return _12741;
}

public void set12741(Integer _12741) {
this._12741 = _12741;
}

public List<RESULT> getRESULT() {
return rESULT;
}

public void setRESULT(List<RESULT> rESULT) {
this.rESULT = rESULT;
}

}
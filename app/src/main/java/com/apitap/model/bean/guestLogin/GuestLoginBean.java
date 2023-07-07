package com.apitap.model.bean.guestLogin;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GuestLoginBean {

	@SerializedName("_192")
	@Expose
	private String _192;
	@SerializedName("_11")
	@Expose
	private String _11;
	@SerializedName("_122_17")
	@Expose
	private Boolean _12217;
	@SerializedName("_122_18")
	@Expose
	private String _12218;
	@SerializedName("RESULT")
	@Expose
	private List<RESULT> rESULT = null;

	public String get192() {
		return _192;
	}

	public void set192(String _192) {
		this._192 = _192;
	}

	public String get11() {
		return _11;
	}

	public void set11(String _11) {
		this._11 = _11;
	}

	public Boolean get12217() {
		return _12217;
	}

	public void set12217(Boolean _12217) {
		this._12217 = _12217;
	}

	public String get12218() {
		return _12218;
	}

	public void set12218(String _12218) {
		this._12218 = _12218;
	}

	public List<RESULT> getRESULT() {
		return rESULT;
	}

	public void setRESULT(List<RESULT> rESULT) {
		this.rESULT = rESULT;
	}

	public class RESULT {

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
		private List<RESULT_> rESULT = null;

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

		public List<RESULT_> getRESULT() {
			return rESULT;
		}

		public void setRESULT(List<RESULT_> rESULT) {
			this.rESULT = rESULT;
		}

	}

	public class RESULT_ {

		@SerializedName("_53")
		@Expose
		private String _53;
		@SerializedName("_127_18")
		@Expose
		private String _12718;

		public String get53() {
			return _53;
		}

		public void set53(String _53) {
			this._53 = _53;
		}

		public String get12718() {
			return _12718;
		}

		public void set12718(String _12718) {
			this._12718 = _12718;
		}

	}
}
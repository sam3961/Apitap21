package com.apitap.model.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sahil on 9/14/2016.
 */
public class AdsListBean {

    @SerializedName("RESULT")
    @Expose
    private List<RESULT> rESULT = new ArrayList<RESULT>();

    public List<RESULT> getRESULT() {
        return rESULT;
    }

    public class RESULT implements Serializable{
        @SerializedName("_44")
        @Expose
        private String status;

        @SerializedName("RESULT")
        @Expose
        private List<AdsData> rESULT = new ArrayList<AdsData>();

        public List<AdsData> getRESULT() {
            return rESULT;
        }

        public String getStatus() {
            return status;
        }

        public class AdsData implements Serializable{

            @SerializedName("_123_21")
            @Expose
            public String playlistId;

            @SerializedName("_120_45")
            @Expose
            public String _120_45;


            @SerializedName("_120_83")
            @Expose
            public String _12083;
            @SerializedName("_120_157")
            @Expose
            public String _120157;
            @SerializedName("_120_86")
            @Expose
            public String imageId;
            @SerializedName("_121_170")
            @Expose
            public String imageName;
            @SerializedName("_127_50")
            @Expose
            public String fileId;
            @SerializedName("_121_15")
            @Expose
            public String fileOriginalName;
            @SerializedName("IR")
            @Expose
            public List<IR> iR = new ArrayList<IR>();

            public String getPlaylistId() {
                return playlistId;
            }

            public String get_120_45() {
                return _120_45;
            }


            public String get_12083() {
                return _12083;
            }

            public String get_120157() {
                return _120157;
            }

            public String getImageId() {
                return imageId;
            }

            public String getImageName() {
                return imageName;
            }

            public String getFileId() {
                return fileId;
            }

            public String getFileOriginalName() {
                return fileOriginalName;
            }

            public List<IR> getiR() {
                return iR;
            }

            public class IR implements Serializable{

                @SerializedName("_114_112")
                @Expose
                public String _114_112;

                @SerializedName("_114_9")
                @Expose
                public String _114_9;


                @SerializedName("_114_144")
                @Expose
                public String _114144;

                @SerializedName("_120_83")
                @Expose
                public String _12083;
                @SerializedName("_114_98")
                @Expose
                public String _11498;
                @SerializedName("_122_158")
                @Expose
                public String _122158;
                @SerializedName("_121_170")
                @Expose
                public String imageName;

                public String get_114144() {
                    return _114144;
                }

                public String get_114_112() {
                    return _114_112;
                }

                public String get_114_9() {
                    return _114_9;
                }


                public String get_12083() {
                    return _12083;
                }

                public String get_11498() {
                    return _11498;
                }

                public String get_122158() {
                    return _122158;
                }

                public String getImageName() {
                    return imageName;
                }
            }

        }
    }
}
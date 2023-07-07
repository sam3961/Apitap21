package com.apitap.model;

public class GlobalData {

    public static String nick = "NoNick";
    public static DeviceRole deviceRole;
    public static String password;

    public enum DeviceRole {
        HOST,
        CLIENT
    }
}

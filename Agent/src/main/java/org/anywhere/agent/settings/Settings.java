package org.anywhere.agent.settings;

public class Settings {

    public static final String VERSION;
    public static final String HOST;
    public static final String PORT;
    public static final boolean AUTOSTART;
    public static final boolean OTHERFILE;
    public static final String FILEURI;
    public static final String APPLAUNCHER;
    public static final String APPID;

    static {
        VERSION = "1.0.0.0";
        APPID = "1000";
        APPLAUNCHER = "/";
        FILEURI = "";
        OTHERFILE = false;
        AUTOSTART = true;
        HOST = "default";
        PORT = "5254";
    }
}
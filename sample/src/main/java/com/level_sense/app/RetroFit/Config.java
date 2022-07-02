package com.level_sense.app.RetroFit;

/**
 * Created by hemanth on 11/3/2015.
 */
public class Config {

    //Set true for show logcat
    public static boolean DEBUG = true;

    /**
     * Fixed, it is android device
     */
    public static final String DEVICE_TYPE_ID = "3";

    public static String DEVICETOKEN_GCMID = "";
    public static String USER_TYPE_ID = "1";
    public static final boolean IS_TURBO_LINEUP_ENABLE = true;

    /**
     * Application mode development-devel or production-prod
     */
    public static final String ENVIRONMENT = "devel";

    /**
     * Message to show when request is left without completion
     */
    public static final String REQUEST_STOPPED_MSG = "Activity is not running, quitting request";

    /**
     * \
     * Base API URL
     */
    public static String FB_IMAGE_URL = "https://graph.facebook.com/";
    public static String FB_IMAGE_SUFFIX = "/picture?type=square";
    // public static String BASE_URL = "http://159.203.128.179/vsports/";
    // public static String BASE_URL = "http://192.168.0.18/vsports/";
    public static String BASE_URL = "https://dash.level-sense.com/Level-Sense-API/web/api/";
    public static String REMAINING_URL_TERMEANDCONDITION = BASE_URL + "TermsAndConditions";

    public static String TIME_ZONE_BASE_URL="https://www.php.net/manual/en/";
    //https://dash.level-sense.com/Level-Sense-API/web
    /**
     * Base API URL
     */
    public static String SERVICE_URL;

    /**
     * Base Upload data URL
     */
    public static String UPLOAD_URL = "";
    /**
     * Base URL for getting image from server, in case image path only contains
     * name
     */
    public static String IMAGE_URL;

    static {
        if ("devel".equalsIgnoreCase(ENVIRONMENT)) {
            SERVICE_URL = BASE_URL;
        } else if ("prod".equalsIgnoreCase(ENVIRONMENT)) {

        } else {
            SERVICE_URL = "SERVICE_URL not set";
            UPLOAD_URL = "UPLOAD_URL not set";
            IMAGE_URL = "IMAGE_URL not set";
        }
    }

    /**
     * Error shown when no response received or unrecognized response received
     */
    public static final String DEFAULT_SERVICE_ERROR = "Network seems to be busy right now, please try again later.";//"Can not connect to server!";

}

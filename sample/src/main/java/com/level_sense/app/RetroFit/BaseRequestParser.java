package com.level_sense.app.RetroFit;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by prashantm on 12/26/2016.
 */

public class BaseRequestParser extends ManageCacheUtils {
    public String message = "Please check your network settings.";
    public String mResponseCode = "0";
    public boolean isSuccess = false;
    public static String newtWorkMessage = "Please check your network settings.";
    private JSONObject mRespJSONObject = null;

    public boolean parseJson(String json) {
        if (!TextUtils.isEmpty(json)) {
            try {
                mRespJSONObject = new JSONObject(json);
                if (null != mRespJSONObject) {

                    isSuccess = mRespJSONObject.optBoolean("success");
                    message = mRespJSONObject.optString("message",
                            message);

                    return isSuccess;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    public JSONArray getDataArray() {
        if (null == mRespJSONObject) {
            return null;
        }
        try {
            return mRespJSONObject.optJSONArray("data");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public Object getDataObject() {
        if (null == mRespJSONObject) {
            return null;
        }
        try {
            return mRespJSONObject.optJSONObject("Data");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getValues(String key) {
        if (mRespJSONObject != null) {
            return mRespJSONObject.optString(key);
        }
        return "";
    }

}

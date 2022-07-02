package com.level_sense.app.RetroFit;

import android.content.Context;

import com.level_sense.app.Session.SessionParam;


/**
 * Created by nileshp on 9/18/2017.
 */

public class ManageCacheUtils {


    public void saveDataInCache(Context context, String key, String values) {
        SessionParam.setPrefData(context, key, values);
    }

    public String getDataFromCashe(Context context, String key) {
        return SessionParam.getPrefData(context, key);
    }
}

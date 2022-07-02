package com.level_sense.app.Session;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.level_sense.app.graph.ChartActivity;
import com.level_sense.app.model.ProfileModel;

import org.json.JSONObject;

import java.io.Serializable;

public class SessionParam implements Serializable {

    public static String PREFRENCE_NAME = "Level_sense_Profile_Prefrence";
    public static String FIRST_LOGIN = "FIRST_LOGIN";
    public String user_id;
    public String user_unique_id;
    public String first_name;
    public String last_name;
    public String user_name;
    public String email;
    public static String saveEmail = "email";
    public static String savePass = "pass";
    public static String checkbox = "checkbox";
    //public String session_key;
    public String image;
    public String dob;
    public String status;
    public String responseJson;
    public String bankJson;
    public String loginType;
    public String tagline;
    public String social_id;

    public static ProfileModel getProfileData(Context context) {

        Gson gson = new Gson();
        ProfileModel deviceParentModel = gson.fromJson(getPrefData(context, ProfileModel.class.getName()), ProfileModel.class);
        return deviceParentModel;

    }

    public SessionParam(Context context) {

        if (null != context) {

            SharedPreferences prefs = context.getSharedPreferences(PREFRENCE_NAME, Context.MODE_PRIVATE);
            user_id = prefs.getString("UserID", "");
            user_unique_id = prefs.getString("UserGUID", "");
            first_name = prefs.getString("FirstName", "");
            last_name = prefs.getString("LastName", "");
            //user_name = prefs.getString("user_name", "");
            email = prefs.getString("Email", "");
            image = prefs.getString("ProfilePicture", "");
            dob = prefs.getString("dob", "");
            status = prefs.getString("status", "");
//          responseJson = prefs.getString("responseJson", "");
//          bankJson = prefs.getString("user_bank_detail", "");
//          loginType = prefs.getString("loginType", "");
//          tagline = prefs.getString("tagline", "");
//          social_id = prefs.getString("social_id","");

        }

    }

    public SessionParam(JSONObject jsonObject) {

        if (null != jsonObject) {
            user_id = jsonObject.optString("UserID", "");
            user_unique_id = jsonObject.optString("UserGUID", "");
            first_name = jsonObject.optString("FirstName", "");
            if (first_name.equals("null")) {
                first_name = "";
            }
            last_name = jsonObject.optString("LastName", "");
            if (last_name.equals("null")) {
                last_name = "";
            }
            email = jsonObject.optString("Email", "");
            if (email.equals("null")) {
                email = "";
            }
//            user_name = jsonObject.optString("user_name", "");
            image = jsonObject.optString("ProfilePicture", "");
            if (user_name != null && user_name.equals("null")) {
                user_name = "";
            }
//            dob = jsonObject.optString("dob", "");
//            if (dob.equals("null")) {
//                dob = "";
//            }
            responseJson = jsonObject.toString();
        }

    }

    public static void setSaveSessionKey(Context activity, String session_key) {
        SharedPreferences prefs = activity.getSharedPreferences(PREFRENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("session_key", session_key);
        editor.commit();
    }

    public static void savePass(Context activity, String pass) {
        SharedPreferences prefs = activity.getSharedPreferences(PREFRENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(savePass, pass);
        editor.commit();
    }

    public static void saveEmail(Context activity, String email) {
        SharedPreferences prefs = activity.getSharedPreferences(PREFRENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(saveEmail, email);
        editor.commit();
    }
    public static void saveCheckBoxStatus(Context activity, String status) {
        SharedPreferences prefs = activity.getSharedPreferences(PREFRENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(checkbox, status);
        editor.commit();
    }

    public static void deletePrefrenceData(Context activity) {
        SharedPreferences prefs = activity.getSharedPreferences(PREFRENCE_NAME, Context.MODE_PRIVATE);
        prefs.edit().clear().commit();
    }

    public static String getSessionKey(Context activity) {
        SharedPreferences prefs = activity.getSharedPreferences(PREFRENCE_NAME, Context.MODE_PRIVATE);
        return prefs.getString("session_key", "");
    }

    public static void setPrefData(Context activity, String key, String value) {
        SharedPreferences prefs = activity.getSharedPreferences(PREFRENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static void setPrefData(Context activity, String key, int value) {
        SharedPreferences prefs = activity.getSharedPreferences(PREFRENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static String getPrefData(Context activity, String key) {
        SharedPreferences prefs = activity.getSharedPreferences(PREFRENCE_NAME, Context.MODE_PRIVATE);
        return prefs.getString(key, "");
    }

    public static Integer getPrefDataInt(Context activity, String key) {
        SharedPreferences prefs = activity.getSharedPreferences(PREFRENCE_NAME, Context.MODE_PRIVATE);
        return prefs.getInt(key, 0);
    }

    //Please verify your mobile number first.
    public void persistData(Context activity) {
        SharedPreferences prefs = activity.getSharedPreferences(PREFRENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("UserID", user_id);
        editor.putString("UserGUID", user_unique_id);
        editor.putString("FirstName", first_name);
        editor.putString("LastName", last_name);
//        editor.putString("user_name", user_name);
        editor.putString("Email", email);
        editor.putString("ProfilePicture", image);
//        editor.putString("dob", dob);
//        editor.putString("status", status);
        editor.putString("responseJson", responseJson);
        if (!TextUtils.isEmpty(bankJson))
            editor.putString("user_bank_detail", bankJson);

        if (!TextUtils.isEmpty(tagline)) {
            editor.putString("tagline", tagline);
        }
        editor.commit();
    }

    public void persistData(Context activity, String key, String value) {
        SharedPreferences prefs = activity.getSharedPreferences(PREFRENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.commit();
    }

}
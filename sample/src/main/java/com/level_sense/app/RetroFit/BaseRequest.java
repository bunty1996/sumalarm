package com.level_sense.app.RetroFit;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.level_sense.app.Session.SessionParam;
import com.level_sense.app.Auth.LoginActivity;
import com.level_sense.app.R;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Map;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BaseRequest<T> extends BaseRequestParser {

    private static final String BOUNDARY = "===" + System.currentTimeMillis() + "===";
    private static final String LINE_FEED = "\r\n";
    private static final String ACCEPT = "application/json";
    private static final String CONTENT_TYPE = "application/json";
    private static final String USER_AGENT = "";
    public int RequestCodeCache = 105;
    Fragment fragment;
    String fileName = null;
    String method = null;
    String extraParam = "";
    Handler handler = new Handler();
    Call<JsonElement> callAPI;
    Call<JsonObject> callAPI2;
    String CHARSET = "UTF-8";
    private Context mContext;
    private ApiInterface apiInterface;
    private RequestReceiver requestReciever;
    private boolean runInBackground = false;
    private Dialog dialog;
    private AVLoadingIndicatorView avi;
    private View loaderView = null;
    private View container = null;
    private int RequestCode = 1;

    Runnable r = new Runnable() {
        @Override
        public void run() {
            //hideLoader();
            hideAvi();
            if (null != requestReciever) {
                requestReciever.onNetworkFailure(RequestCode, mContext.getString(R.string.action_settings));
                Activity activity = (Activity) mContext;

                if (!activity.isDestroyed()) {

               /*Dialogs.showOkCancelDialog(
                                    mContext,
                                    mContext.getString(R.string.MSG_INTERNETERROR),
                                    new OnItemClickInAdapter() {
                                        @Override
                                        public void onClickItems(int clickID, int position, Object obje) {
                                            //showLoader();
                                            showAvi();
                                            callAPI.clone().enqueue(responseCallback);
                                        }
                                    });*/

                }

            }

        }

    };

    private boolean cacheEnabled = false;

    public Callback<JsonElement> responseCallback = new Callback<JsonElement>() {
        @Override
        public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
            String responseServer = "";
            //hideLoader();
            Log.e("stscd", response.code() + "//");
            Log.e("stscd12", response.isSuccessful() + "//");

            hideAvi();

            if (null != response.body()) {

                JsonElement jsonElement = (JsonElement) response.body();
                if (null != jsonElement) {
                    responseServer = jsonElement.toString();
                    Log.e("flags", responseServer + "//");
                }

            } else if (response.errorBody() != null) {

                responseServer = readStreamFully(response.errorBody().contentLength(), response.errorBody().byteStream());

            }

            logFullResponse(responseServer, "OUTPUT12");

            if (parseJson(responseServer)) {

                if (cacheEnabled) {
                    saveDataInCache(mContext, method + extraParam, responseServer);
                }

                if (null != requestReciever && !((Activity) mContext).isDestroyed()) {

                    if (fragment != null) {

                        if (fragment.isAdded()) {

                            if (null != getDataArray()) {
                                requestReciever.onSuccess(RequestCode, responseServer, getDataArray());
                            } else if (null != getDataObject()) {
                                requestReciever.onSuccess(RequestCode, responseServer, getDataObject());
                            } else {
                                requestReciever.onSuccess(RequestCode, responseServer, message);
                            }

                        }

                    } else {

                        if (null != getDataArray()) {
                            requestReciever.onSuccess(RequestCode, responseServer, getDataArray());
                        } else if (null != getDataObject()) {
                            requestReciever.onSuccess(RequestCode, responseServer, getDataObject());
                        } else {
                            requestReciever.onSuccess(RequestCode, responseServer, message);
                        }

                    }

                }

            } else {

                if (mResponseCode.equals("401")) {

                    Intent in = new Intent(mContext, LoginActivity.class);
                    in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    ((Activity) mContext).startActivity(in);

                } else if (mResponseCode.equalsIgnoreCase("509")) {

                } else if (mResponseCode.equalsIgnoreCase("510")) {

                } else {
                    Log.e("error45", responseServer + "//" + requestReciever);
                    int errorCode = 1;
                    if (null != requestReciever) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(responseServer);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (jsonObject != null) {
                            if (jsonObject.has("data")) {
                                JSONObject jsonObjectData = jsonObject.optJSONObject("data");
                                if (jsonObjectData != null) {
                                    if (jsonObjectData.has("not_enough_balance")) {
                                        String not_enough_balance = jsonObjectData.optString("not_enough_balance");

                                        if (not_enough_balance != null && not_enough_balance.equalsIgnoreCase("1")) {
                                            errorCode = 101;
                                        }
                                    }
                                }
                            }

                        }
                        if (jsonObject != null) {
                            JSONObject jsonObject1 = jsonObject.optJSONObject("error");
                            String error = "";
                            if (jsonObject1 != null) {
                                String k = jsonObject1.keys().next();
                                try {
                                    error = jsonObject1.getString(k);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                requestReciever.onFailure(errorCode, "" + mResponseCode, error);
                            } else {
                                requestReciever.onFailure(errorCode, "" + mResponseCode, message);
                            }
                        } else {
                            Log.e("bsrer", response.body().toString() + "//" + responseServer);
                            requestReciever.onFailure(errorCode, "" + mResponseCode, "Something went wrong.");

                        }
                    }
                }
            }
        }

        @Override
        public void onFailure(Call<JsonElement> call, Throwable t) {

            handler.removeCallbacksAndMessages(null);
            //First condition is for connection problem
            if (null != t && !TextUtils.isEmpty(t.getMessage()) && t.getMessage().startsWith("Unable to resolve") || t.getMessage().startsWith("Failed to connect")) {
                handler.postDelayed(r, 1000);
            } else {
                //hideLoader();
                hideAvi();
                requestReciever.onFailure(1, "" + mResponseCode, message);
            }
        }
    };
    // change JsonElement to JsonObject by sharan
    public Callback<JsonObject> responseCallback2 = new Callback<JsonObject>() {
        @Override
        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

            //hideLoader();
            Log.e("stscd", response.code() + "//");


            hideAvi();

            if (response.isSuccessful()) {
                String responseServer = response.body().toString();
                Log.e("stscd12", response.isSuccessful() + "//" + responseServer);
                requestReciever.onSuccess(RequestCode, responseServer, getDataArray());
            } else {
                requestReciever.onFailure(response.code(), "" + mResponseCode, response.errorBody().toString());
            }
          /*if (null != response.body()) {

                JsonObject jsonObject = (JsonObject) response.body();
                if (null != jsonObject) {
                    responseServer = jsonObject.toString();
                }

            } else if (response.errorBody() != null) {

                responseServer = readStreamFully(response.errorBody().contentLength(), response.errorBody().byteStream());

            }*//*

            logFullResponse(responseServer, "OUTPUT12");

            if (parseJson(responseServer)) {

                if (cacheEnabled) {
                    saveDataInCache(mContext, method + extraParam, responseServer);
                }

                if (null != requestReciever && !((Activity) mContext).isDestroyed()) {

                    if (fragment != null) {

                        if (fragment.isAdded()) {

                            if (null != getDataArray()) {
                                requestReciever.onSuccess(RequestCode, responseServer, getDataArray());
                            } else if (null != getDataObject()) {
                                requestReciever.onSuccess(RequestCode, responseServer, getDataObject());
                            } else {
                                requestReciever.onSuccess(RequestCode, responseServer, message);
                            }

                        }

                    } else {

                        if (null != getDataArray()) {
                            requestReciever.onSuccess(RequestCode, responseServer, getDataArray());
                        } else if (null != getDataObject()) {
                            requestReciever.onSuccess(RequestCode, responseServer, getDataObject());
                        } else {
                            requestReciever.onSuccess(RequestCode, responseServer, message);
                        }

                    }

                }

            } else {

                if (mResponseCode.equals("401")) {

                    Intent in = new Intent(mContext, LoginActivity.class);
                    in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    ((Activity) mContext).startActivity(in);

                } else if (mResponseCode.equalsIgnoreCase("509")) {

                } else if (mResponseCode.equalsIgnoreCase("510")) {

                } else {
                    Log.e("error45", responseServer + "//" + requestReciever);
                    int errorCode = 1;
                    if (null != requestReciever) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(responseServer);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (jsonObject != null) {
                            if (jsonObject.has("data")) {
                                JSONObject jsonObjectData = jsonObject.optJSONObject("data");
                                if (jsonObjectData != null) {
                                    if (jsonObjectData.has("not_enough_balance")) {
                                        String not_enough_balance = jsonObjectData.optString("not_enough_balance");

                                        if (not_enough_balance != null && not_enough_balance.equalsIgnoreCase("1")) {
                                            errorCode = 101;
                                        }
                                    }
                                }
                            }

                        }
                        if (jsonObject != null) {
                            JSONObject jsonObject1 = jsonObject.optJSONObject("error");
                            String error = "";
                            if (jsonObject1 != null) {
                                String k = jsonObject1.keys().next();
                                try {
                                    error = jsonObject1.getString(k);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                requestReciever.onFailure(errorCode, "" + mResponseCode, error);
                            } else {
                                requestReciever.onFailure(errorCode, "" + mResponseCode, message);
                            }
                        } else {
                            Log.e("bsrer", response.body().toString() + "//" + responseServer);
                            requestReciever.onFailure(errorCode, "" + mResponseCode, "Something went wrong.");

                        }
                    }
                }
            }*/
        }

        @Override
        public void onFailure(Call<JsonObject> call, Throwable t) {

            handler.removeCallbacksAndMessages(null);
            //First condition is for connection problem
            if (null != t && !TextUtils.isEmpty(t.getMessage()) && t.getMessage().startsWith("Unable to resolve") || t.getMessage().startsWith("Failed to connect")) {
                handler.postDelayed(r, 1000);
            } else {
                //hideLoader();
                hideAvi();
                requestReciever.onFailure(1, "" + mResponseCode, message);
            }
        }
    };

    private boolean isAlreadyTaken = false;
    private boolean duringCacheLoader = false;
    private HttpURLConnection mHttpURLConnection;

    public BaseRequest(Context context) {
        mContext = context;
        apiInterface =
                ApiClient.getClient(Config.BASE_URL).create(ApiInterface.class);
        dialog = getProgressesDialog(context);
    }

    public BaseRequest(Context context, Fragment fragment) {
        mContext = context;
        this.fragment = fragment;
        apiInterface = ApiClient.getClient(Config.BASE_URL).create(ApiInterface.class);
        dialog = getProgressesDialog(context);
    }

    public BaseRequest(Context context, Fragment fragment, String baseUrl) {
        mContext = context;
        this.fragment = fragment;
        apiInterface = ApiClient.getClient(baseUrl).create(ApiInterface.class);
        dialog = getProgressesDialog(context);
    }

    public String getExtraParam() {
        return extraParam;
    }

    public void setExtraParam(String extraParam) {
        this.extraParam = extraParam;
    }

    public boolean isDuringCacheLoader() {
        return duringCacheLoader;
    }

    public void setDuringCacheLoader(boolean duringCacheLoader) {
        this.duringCacheLoader = duringCacheLoader;
    }

    public boolean isRunInBackground() {
        return runInBackground;
    }

    public void setRunInBackground(boolean runInBackground) {
        this.runInBackground = runInBackground;
    }

    public boolean isAlreadyTaken() {
        return isAlreadyTaken;
    }

    public void setAlreadyTaken(boolean alreadyTaken) {
        isAlreadyTaken = alreadyTaken;
    }

    public boolean isCacheEnabled() {
        return cacheEnabled;
    }

    public void setCacheEnabled(boolean cacheEnabled) {
        this.cacheEnabled = cacheEnabled;
    }

    public void setBaseRequestListner(RequestReceiver requestListner) {
        this.requestReciever = requestListner;
    }

    public void setLoaderView(View loaderView_) {
        if (loaderView_ instanceof AVLoadingIndicatorView)
            this.avi = (AVLoadingIndicatorView) loaderView_;
    }

    public void setLoaderView(View loaderView_, String loaderTextView) {
        if (loaderView_ instanceof AVLoadingIndicatorView)
            this.avi = (AVLoadingIndicatorView) loaderView_;
    }


    public void setContainer(View avi_, View container_) {
        if (avi_ instanceof AVLoadingIndicatorView) {
            this.avi = (AVLoadingIndicatorView) avi_;
        }
        this.container = container_;
    }

    public ArrayList<Object> getDataList(JSONArray mainArray, Class<T> t) {
        Gson gsm = null;
        ArrayList<Object> list = null;
        list = new ArrayList<>();
        if (null != mainArray) {

            for (int i = 0; i < mainArray.length(); i++) {
                gsm = new Gson();
                Object object = gsm.fromJson(mainArray.optJSONObject(i).toString(), t);
                list.add(object);
            }
        }
        return list;
    }

    public ArrayList<Object> getList(JSONArray mainArray, Class<T> t) {
        Gson gsm = null;
        ArrayList<Object> list = null;
        list = new ArrayList<>();
        if (null != mainArray) {

            for (int i = 0; i < mainArray.length(); i++) {
                gsm = new Gson();
                Object object = gsm.fromJson(mainArray.optJSONObject(i).toString(), t);
                list.add(object);
            }
        }
        return list;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void callAPIPost(final int requestCode, JsonObject jsonObject, final String remainingURL) {
        isAlreadyTaken = false;
        method = remainingURL;
        RequestCode = requestCode;
        final JsonObject mJsonObject;

        if (jsonObject == null) {
            jsonObject = new JsonObject();
        }

        mJsonObject = jsonObject;
        //Log.d("BaseReq", "Input URL : " +ApiClient.getClient().baseUrl() + remainingURL);
        logFullResponse(mJsonObject.toString(), "INPUT");
        //final String sess = SessionParam.getSessionKey(mContext);
        /*if(Config.DEBUG)
         Log.d("SessionParam", "Session Key : " + sess);
         fileName = cacheEnabled ? (remainingURL
                + sess + mJsonObject.toString()).hashCode()
                + ".req" : null;
        if(cacheEnabled){
            String cacheResponse = getDataFromCashe(mContext, method + extraParam);
            if (cacheResponse != null && cacheResponse.length() > 0) {
                runInBackground = true;
                handleResponseCache(RequestCodeCache, cacheResponse);
            }
        }*///showLoader();

        showAvi();
        Log.e("sessionKey", SessionParam.getSessionKey(mContext) + "//");
        if (cacheEnabled) {
            callAPI = apiInterface.postData(remainingURL, mJsonObject, SessionParam.getSessionKey(mContext));
            callAPI.enqueue(responseCallback);
        } else {
            callAPI = apiInterface.postData(remainingURL, mJsonObject, SessionParam.getSessionKey(mContext));
            callAPI.enqueue(responseCallback);
        }

    }

    public void handleResponseCache(int requestCode, String response) {
        if (parseJson(response)) {
            if (null != requestReciever && !((Activity) mContext).isDestroyed()) {
                if (fragment != null) {
                    if (fragment.isAdded()) {
                        if (null != getDataArray()) {
                            requestReciever.onSuccess(requestCode, response, getDataArray());
                        } else if (null != getDataObject()) {
                            requestReciever.onSuccess(requestCode, response, getDataObject());
                        } else {
                            requestReciever.onSuccess(requestCode, response, message);
                        }
                    }
                } else {
                    if (null != getDataArray()) {
                        requestReciever.onSuccess(requestCode, response, getDataArray());
                    } else if (null != getDataObject()) {
                        requestReciever.onSuccess(requestCode, response, getDataObject());
                    } else {
                        requestReciever.onSuccess(requestCode, response, message);
                    }
                }
            }
        }
    }

    public void callAPIPost(final int requestCode, JsonObject jsonObject, String remainingURL, String sessionKey) {
        RequestCode = requestCode;
        method = remainingURL;
        //showLoader();
        showAvi();

        if (jsonObject == null) {
            jsonObject = new JsonObject();
        }
        PackageInfo pInfo = null;
        String version = "";
        try {
            pInfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
            version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        Log.d("BaseReq", "Input URL : " + remainingURL);
        logFullResponse(jsonObject.toString(), "INPUT");
        callAPI = apiInterface.postData(remainingURL, jsonObject, SessionParam.getSessionKey(mContext));

        callAPI.enqueue(responseCallback);
    }

    public void callAPIPostImage(final int requestCode, MultipartBody.Part image, String remainingURL, String mSessionKey) {
        RequestCode = requestCode;
        // showLoader();
        showAvi();
        Log.d("BaseReq", "Input URL : " + remainingURL);
        if (TextUtils.isEmpty(mSessionKey)) {
            //        mSessionKey = SessionParam.getSessionKey(mContext);
        }

        callAPI = apiInterface.uploadImage(remainingURL, image, mSessionKey);
        callAPI.enqueue(responseCallback);
    }

    public void callAPIGET(final int requestCode, Map<String, String> map, String remainingURL) {
        RequestCode = requestCode;
        method = remainingURL;
        showAvi();
        callAPI = apiInterface.postDataGET(remainingURL, map, SessionParam.getSessionKey(mContext));
        callAPI.enqueue(responseCallback);

    }

    public void callArticleAPIGET(final int requestCode, Map<String, String> map, String remainingURL) {
        RequestCode = requestCode;
        method = remainingURL;
        showAvi();
        callAPI2 = apiInterface.getArticleDataGET(remainingURL);
        callAPI2.enqueue(responseCallback2);

    }

    public void callAPIGETWithBaseUrl(final int requestCode, Map<String, String> map, String fullUrl) {
        RequestCode = requestCode;

        method = fullUrl;
        //showLoader();
        showAvi();
        //String baseURL = ApiClient.getClient().baseUrl().toString() + remainingURL;
        String baseURL = fullUrl;
      /*if(!baseURL.endsWith("?")){
            baseURL = baseURL + "?";
        }*/
        if (map != null) {

            for (Map.Entry<String, String> entry : map.entrySet()) {
                baseURL = baseURL + entry.getKey() + "=" + entry.getValue() + "&";
            }

        }

        Log.e("BaseReqINPUT URL : ", baseURL + "//" + SessionParam.getSessionKey(mContext));
        //callAPI = apiInterface.postDataGET(remainingURL, map, SessionParam.getSessionKey(mContext));
        callAPI = apiInterface.postDataGET(baseURL, map, SessionParam.getSessionKey(mContext));
        callAPI.enqueue(responseCallback);

    }

    public void logFullResponse(String response, String inout) {
        final int chunkSize = 2000;

        if (null != response && response.length() > chunkSize) {
            int chunks = (int) Math.ceil((double) response.length() / (double) chunkSize);

            for (int i = 1; i <= chunks; i++) {

                if (i != chunks) {

                    Log.i("BaseReq", inout + " : " + response.substring((i - 1) * chunkSize, i * chunkSize));

                } else {

                    Log.i("BaseReq", inout + " : " + response.substring((i - 1) * chunkSize, response.length()));

                }

            }

        } else {

            try {

                JSONObject jsonObject = new JSONObject(response);
                Log.d("BaseReq", inout + " : " + jsonObject.toString(jsonObject.length()));

            } catch (JSONException e) {

                e.printStackTrace();
                Log.d("BaseReq", " logFullResponse=>  " + response);

            }

        }

    }

    private String readStreamFully(long len, InputStream inputStream) {

        if (inputStream == null) {
            return null;
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {

            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }

        } catch (IOException e) {

            e.printStackTrace();

        } finally {

            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return sb.toString();

    }

    public Dialog getProgressesDialog(Context ct) {

        Dialog dialog = new Dialog(ct);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.progress_dialog_loader);
        //dialog.setCanceledOnTouchOutside(false);
        dialog.setCanceledOnTouchOutside(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);
        return dialog;

    }

    public void showLoader() {
        if (mContext != null && !((Activity) mContext).isDestroyed()) {

            if (!runInBackground) {
                if (null != loaderView) {
                    loaderView.setVisibility(View.VISIBLE);
                } else if (null != dialog) {

                    dialog.show();
                }
            }
        }
    }

    public void hideLoader() {
        if (mContext != null && !((Activity) mContext).isDestroyed()) {

            if (!runInBackground) {
                if (null != loaderView && null != container) {
                    loaderView.setVisibility(View.GONE);
                    container.setVisibility(View.VISIBLE);
                } else if (null != dialog) {
                    dialog.dismiss();
                    dialog.cancel();
                }
            }
        }
    }

    public void showAvi() {

        if (mContext != null && !((Activity) mContext).isDestroyed()) {
            if (!runInBackground) {
                if (null != avi) {
                    avi.setVisibility(View.VISIBLE);
                    avi.show();
                } else if (null != dialog) {
                    dialog.show();
                }
            } else {
                //avi.hide();
            }
        }
    }

    public void hideAvi() {

        if (mContext != null && !((Activity) mContext).isDestroyed()) {

            if (!runInBackground) {
                if (null != avi && null != container) {
                    avi.hide();
                    container.setVisibility(View.VISIBLE);
                } else if (null != avi) {
                    avi.hide();
                } else if (null != dialog) {
                    dialog.dismiss();
                    dialog.cancel();
                }
            } else {
                //avi.hide();
            }
        }
    }

    public void uploadImage(final String finalUrl, final File file, final String key) {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    MultipartConn multipartConn = new MultipartConn(
                            finalUrl);

                    multipartConn.addFilePart(key, file);

                    mHttpURLConnection = multipartConn.prepareConnection();

                    String response = readStreamFully(
                            mHttpURLConnection.getContentLength(),
                            mHttpURLConnection.getInputStream());

                    requestReciever.onSuccess(RequestCode, response, null);
                } catch (IOException e) {
                    e.printStackTrace();
                    requestReciever.onNetworkFailure(1, BaseRequestParser.newtWorkMessage);
                }
            }
        };
        new Thread(runnable).start();
    }

    public class MultipartConn {
        private HttpURLConnection httpURLConnection;
        private OutputStream outputStream;
        private PrintWriter printWriter;

        /**
         * This constructor initializes a new HTTP POST request with content
         * type is set to multipart/form-data
         *
         * @param requestURL
         */
        public MultipartConn(String requestURL) throws IOException {
            URL url = new URL(requestURL);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setDoOutput(true); // indicates POST method
            httpURLConnection.setDoInput(true);
            httpURLConnection.setRequestProperty("Content-Type",
                    "multipart/form-data; boundary=" + BOUNDARY);
//          httpURLConnection.setRequestProperty("ProfileUserModel-Agent",
//          HttpConnector.USER_AGENT);

            outputStream = httpURLConnection.getOutputStream();
            printWriter = new PrintWriter(new OutputStreamWriter(outputStream, CHARSET), true);
        }

        /**
         * Adds a form field to the request
         *
         * @param name  field name
         * @param value field value
         */
        public void addFormField(String name, String value) {
            printWriter.append("--" + BOUNDARY).append(LINE_FEED);
            printWriter.append(
                    "Content-Disposition: form-data; name=\"" + name + "\"")
                    .append(LINE_FEED);
            printWriter.append("Content-Type: text/plain; charset=" + CHARSET)
                    .append(LINE_FEED);

            printWriter.append(LINE_FEED);
            printWriter.append(value).append(LINE_FEED);
            printWriter.flush();
        }

        /**
         * Adds a upload file section to the request
         *
         * @param fieldName  name attribute in <input type="file" name="..." />
         * @param uploadFile a File to be uploaded
         * @throws IOException
         */
        public void addFilePart(String fieldName, File uploadFile)
                throws IOException {
            String fileName = uploadFile.getName();
            printWriter.append("--" + BOUNDARY).append(LINE_FEED);
            printWriter.append(
                    "Content-Disposition: form-data; name=\"" + fieldName
                            + "\"; filename=\"" + fileName + "\"").append(
                    LINE_FEED);
            printWriter.append(
                    "Content-Type: "
                            + URLConnection.guessContentTypeFromName(fileName))
                    .append(LINE_FEED);

            printWriter.append("Content-Transfer-Encoding: binary").append(
                    LINE_FEED);
            printWriter.append(LINE_FEED);
            printWriter.flush();

            FileInputStream inputStream = new FileInputStream(uploadFile);
            byte[] buffer = new byte[4096];
            int bytesRead = -1;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
//                System.out.println(" bytesRead "+bytesRead);
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.flush();
            inputStream.close();

            printWriter.append(LINE_FEED);
            printWriter.flush();
        }

        /**
         * Adds a header field to the request.
         *
         * @param name  - name of the header field
         * @param value - value of the header field
         */
        public void addHeaderField(String name, String value) {
            printWriter.append(name + ": " + value).append(LINE_FEED);
            printWriter.flush();
        }

        /**
         * Completes the request and receives response from the com.server.
         *
         * @return a list of Strings as response in case the com.server returned
         * status OK, otherwise an exception is thrown.
         * @throws IOException
         */
        public HttpURLConnection prepareConnection() {
            printWriter.append(LINE_FEED).flush();
            printWriter.append("--" + BOUNDARY + "--").append(LINE_FEED);
            printWriter.close();

            return httpURLConnection;
        }
    }

}


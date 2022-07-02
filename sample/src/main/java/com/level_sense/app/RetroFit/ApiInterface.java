package com.level_sense.app.RetroFit;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.Map;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public interface ApiInterface {

    @Headers({"Content-Type: application/json"})
    @POST
    Call<JsonElement> postData(@Url String remainingURL, @Body JsonObject jsonObject, @Header("SESSIONKEY") String session_key);

    @GET
    Call<JsonElement> postDataGET(@Url String remainingURL, @QueryMap Map<String, String> map, @Header("SESSIONKEY") String session_key);


    @GET
    Call<JsonObject> getArticleDataGET(@Url String remainingURL);


    @Multipart
    @POST
    Call<JsonElement> uploadImage(@Url String remainingURL, @Part MultipartBody.Part file, @Body ProgressRequestBody body, @Header("LoginSessionKey") String session_key);

    @Multipart
    @POST
    Call<JsonElement> uploadImage(@Url String remainingURL, @Part MultipartBody.Part file, @Header("LoginSessionKey") String session_key);
}



package com.level_sense.app.graph;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Process;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.level_sense.app.R;
import com.level_sense.app.RetroFit.BaseRequest;
import com.level_sense.app.RetroFit.RequestReceiver;
import com.level_sense.app.RoomDatabase.Converter;
import com.level_sense.app.RoomDatabase.Note;
import com.level_sense.app.RoomDatabase.RoomDB;
import com.level_sense.app.Utility.Functions;
import com.level_sense.app.Utility.OnItemClickAdapter;
import com.level_sense.app.custom.ChartLayout;
import com.level_sense.app.model.DeviceDataModel;
import com.level_sense.app.model.DeviceModel;
import com.level_sense.app.model.GraphModel;
import com.level_sense.app.roomDB.model.DeviceDataModelDB;
import com.level_sense.app.roomDB.model.GraphModelDB;
import com.level_sense.app.roomDB.view_model.GraphDataViewModel;
import com.level_sense.app.roomDB.view_model.GraphValueViewModel;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.os.Process.THREAD_PRIORITY_BACKGROUND;
import static android.os.Process.THREAD_PRIORITY_MORE_FAVORABLE;

public class ChartActivity extends AppCompatActivity implements View.OnClickListener {

    int limit = 10000;
    int currentPage = 1;
    DeviceModel deviceModel;
    @BindView(R.id.custom_pb)
    AVLoadingIndicatorView customPb;
    @BindView(R.id.custom_text)
    TextView custom_text;
    @BindView(R.id.message_tv)
    TextView messageTv;
    @BindView(R.id.loader_view_ll)
    LinearLayout loaderViewLl;
    @BindView(R.id.container)
    ScrollView container;
    @BindView(R.id.chartLayout)
    ChartLayout chartLayout;
    @BindView(R.id.tVToday)
    TextView tVToday;
    @BindView(R.id.twoHrChart)
    TextView twoHrChart;
    @BindView(R.id.twfourHrChart)
    TextView twentyFourHrChart;
    @BindView(R.id.fourHrChart)
    TextView fourHrChart;
    @BindView(R.id.tVWeek)
    TextView tVWeek;
    @BindView(R.id.tVdropDown)
    TextView tVdropDown;
    @BindView(R.id.lLTemp)
    LinearLayout lLTemp;
    @BindView(R.id.chartTimeLayout)
    LinearLayout chartTimeLayout;
    @BindView(R.id.grow_btn)
    Button grow_btn;
    @BindView(R.id.shrink_btn)
    Button shrink_btn;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbar_title;
    @BindView(R.id.txtGraphAdjustable)
    TextView txtGraphAdjustable;

    @BindView(R.id.toolbarBackImg)
    ImageView toolbarBackImg;

    @BindView(R.id.toolbarRefresh)
    ImageView toolbarRefresh;

    String value;
    String apiCallValue = "0";
    private GraphDataViewModel graphDataViewModel;
    private GraphValueViewModel graphValueViewModel;

    private ArrayList<DeviceDataModelDB> dbDataList = new ArrayList<>();
    private List<GraphModelDB> dbValueList = new ArrayList<>();

    public static Intent getIntent(Context mContext, DeviceModel deviceModel) {
        Intent intent = new Intent(mContext, ChartActivity.class);
        intent.putExtra(DeviceModel.class.getName(), deviceModel);
        return intent;
    }

    private void getIntentData() {
        deviceModel = (DeviceModel) getIntent().getExtras().get(DeviceModel.class.getName());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        apiCallValue = "0";
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_chart);
        ButterKnife.bind(this);
        RoomDB.getDatabase(ChartActivity.this);
     clearPrefrence();

        apiCallValue = "0";
        toolbar.setPadding(0, 0, 0, 0);
        toolbar.setContentInsetsAbsolute(0, 0);
        toolbar_title.setText(R.string.graph);
        toolbarBackImg.setImageResource(R.drawable.ic_back_icon);
        toolbarRefresh.setImageResource(R.drawable.ic_refresh);
        toolbarBackImg.setOnClickListener(this);
        toolbarRefresh.setOnClickListener(this);
        SpannableString ss=new SpannableString(getResources().getString(R.string.graphs_adjustable));
        ClickableSpan clickableSpan=new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                String url = "https://www.level-sense.com/account";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        };
        ForegroundColorSpan fg=new ForegroundColorSpan(getResources().getColor(R.color.black));
        ss.setSpan(clickableSpan,30,37, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(fg,30,37,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        txtGraphAdjustable.setText(ss);
        txtGraphAdjustable.setMovementMethod(LinkMovementMethod.getInstance());
        getIntentData();
        tVdropDown.setText("10");
        value = "10";
        chartLayout.setVisibility(View.GONE);
        graphDataViewModel = ViewModelProviders.of(ChartActivity.this).get(GraphDataViewModel.class);
        graphValueViewModel = ViewModelProviders.of(ChartActivity.this).get(GraphValueViewModel.class);

        changeTime();
        Log.e( "devicetype ",deviceModel.getDeviceType());

        dbDataList.clear();
        dbValueList.clear();


        //  getGraphDataLocal();
        if(PreferenceManager.getDefaultSharedPreferences(ChartActivity.this).getString("theJsonToday","").equalsIgnoreCase("")){
            apiCallValue = "0";
            callAPI();
        }
        else{
            Log.e("today", PreferenceManager.getDefaultSharedPreferences(ChartActivity.this).getString("theJsonToday", ""));
            getWeekLocalData(PreferenceManager.getDefaultSharedPreferences(ChartActivity.this).getString("theJsonToday", ""));

        }

    }

    private void getGraphDataLocal() {

        if (dbDataList.size() < 11 && apiCallValue.equalsIgnoreCase("1")) {

            dbDataList.addAll(graphDataViewModel.getAllData());
            Log.e("getdata", dbDataList.size() + "//");
            ArrayList<DeviceDataModel> deviceDataList = new ArrayList<>();

            for (int i = 0; i < dbDataList.size(); i++) {

                graphValueViewModel.getValueDB(dbDataList.get(i).getSensorId());

                DeviceDataModel model = new DeviceDataModel();

                model.setMax(dbDataList.get(i).getMax());
                model.setMin(dbDataList.get(i).getMin());
                model.setSensorId(dbDataList.get(i).getSensorId());
                model.setSensorDisplayName(dbDataList.get(i).getSensorDisplayName());
                model.setSensorDisplayUnits(dbDataList.get(i).getSensorDisplayUnits());
                model.setSensorSlug(dbDataList.get(i).getSensorSlug());
                ArrayList<GraphModel> list = new ArrayList();
                list.clear();
                dbValueList.clear();

                dbValueList.addAll(graphValueViewModel.getValueDB(dbDataList.get(i).getSensorId()));

                for (int j = 0; j < dbValueList.size(); j++) {
                    GraphModel graphModel = new GraphModel();
                    graphModel.setTimeStamp(dbValueList.get(j).getTimeStamp());
                    graphModel.setValue(dbValueList.get(j).getValue());
                    list.add(graphModel);

                }

                model.setData(list);
                deviceDataList.add(model);

                Log.e("model_full", model.toString());
                Log.e("model_list", list.toString() + "//" + list.size());

            }

            chartLayout.setVisibility(View.VISIBLE);
            custom_text.setVisibility(View.GONE);
            JSONObject object = new JSONObject();


            chartLayout.AddViews(deviceDataList, shedule, object,deviceModel.getDeviceType());

            callAPI();

        } else {

            callAPI();

        }
    }

    private void callAPI() {

        if (dbDataList.size() > 0) {

            long time = System.currentTimeMillis() - dbDataList.get(0).getDb_time();
            Log.e("dbdata", time + "//" + System.currentTimeMillis() + "//" + dbDataList.get(0).getDb_time() + "//" + apiCallValue);

            if ((time / 60000) >= 3 && apiCallValue.equalsIgnoreCase("0")) {

                callApitDeviceLogDataList(deviceModel.getId());

            } else {

            }

        } else if (apiCallValue.equalsIgnoreCase("0")){
            callApitDeviceLogDataList(deviceModel.getId());
        }
        else if(apiCallValue.equalsIgnoreCase("1")){
            if(shedule==Shedule.Week){
                getWeekLocalData(PreferenceManager.getDefaultSharedPreferences(ChartActivity.this).getString("theJsonWeek", ""));
            }
            else if(shedule==Shedule.Today){
                getWeekLocalData(PreferenceManager.getDefaultSharedPreferences(ChartActivity.this).getString("theJsonToday", ""));
            }
            else if(shedule==Shedule.TwentyFour){
                getWeekLocalData(PreferenceManager.getDefaultSharedPreferences(ChartActivity.this).getString("theJsonTwFourhr", ""));
            }
            else if(shedule==Shedule.twoHr){
                getWeekLocalData(PreferenceManager.getDefaultSharedPreferences(ChartActivity.this).getString("theJsonTwoHr", ""));
            }
            else if(shedule==Shedule.fourHr){
                getWeekLocalData(PreferenceManager.getDefaultSharedPreferences(ChartActivity.this).getString("theJsonFourHr", ""));
            }
            else{
                getWeekLocalData(PreferenceManager.getDefaultSharedPreferences(ChartActivity.this).getString("theJsonToday", ""));

            }

        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();
        //noinspection SimplifiableIfStatement

        if (id == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);

    }

    private void callApitDeviceLogDataList(String id) {
//      Log.e("callApitDevice",id+"");
        apiCallValue = "1";
        BaseRequest baseRequest = new BaseRequest(ChartActivity.this);
        baseRequest.setRunInBackground(false);
        baseRequest.setLoaderView(customPb);
        custom_text.setVisibility(View.VISIBLE);
        custom_text.setText("One moment, we are drawing your graphs for you. Please don't leave the page.");
        baseRequest.setBaseRequestListner(new RequestReceiver() {
            @Override
            public void onSuccess(int requestCode, String fullResponse, Object dataObject) {
//                PreferenceManager.getDefaultSharedPreferences(ChartActivity.this).edit().clear().commit();
                chartLayout.setVisibility(View.VISIBLE);
                custom_text.setVisibility(View.GONE);
                Log.e("callApitDevice", id + "");
                ArrayList<DeviceDataModel> deviceDataList = new ArrayList<>();
                JSONObject jsonObject;
                JSONObject devicejsonObject = null;

                        if(shedule==Shedule.Week){
                            PreferenceManager.getDefaultSharedPreferences(ChartActivity.this).edit()
                                    .putString("theJsonWeek", fullResponse).apply();
                        }
                        else if(shedule==Shedule.Today){
                            PreferenceManager.getDefaultSharedPreferences(ChartActivity.this).edit()
                                    .putString("theJsonToday", fullResponse).apply();
                        }
                        else if(shedule==Shedule.TwentyFour){
                            PreferenceManager.getDefaultSharedPreferences(ChartActivity.this).edit()
                                    .putString("theJsonTwFourhr", fullResponse).apply();
                        }


                        else if(shedule==Shedule.twoHr){
                            PreferenceManager.getDefaultSharedPreferences(ChartActivity.this).edit()
                                    .putString("theJsonTwoHr", fullResponse).apply();
                        }
                        else if(shedule==Shedule.fourHr){
                            PreferenceManager.getDefaultSharedPreferences(ChartActivity.this).edit()
                                    .putString("theJsonFourHr", fullResponse).apply();
                        }

                try {
                    jsonObject = new JSONObject(fullResponse);
                    JSONObject metaobj = jsonObject.optJSONObject("metaData");
                    Log.e("metadataval", metaobj.optString("pumpCycle") + "//");


                    grow_btn.setBackground(getResources().getDrawable(R.drawable.blue_button_selector));
                    shrink_btn.setBackground(getResources().getDrawable(R.drawable.blue_button_selector));
                    devicejsonObject = jsonObject.optJSONObject("deviceDataList");
                    Iterator<String> iter = devicejsonObject.keys();
                    Gson gson = new Gson();
                    Log.e("graphdata", devicejsonObject.toString() + "//");

                    graphDataViewModel.deleteAll();
                    long db_time = System.currentTimeMillis();
                    Log.e("Current_time", db_time + "");
                    //E/Current_time: 1582093821500

                    while (iter.hasNext()) {
                        String key = iter.next();
                        Log.e( "keys ",key);
                        JSONObject value = devicejsonObject.optJSONObject(key);
                        Log.e("value", value.toString());
                        DeviceDataModel deviceDataModel = gson.fromJson(value.toString(), DeviceDataModel.class);
                        Log.e("deviceDataModel", deviceDataModel.toString());


                        deviceDataList.add(deviceDataModel);



                    }

                    chartLayout.AddViews(deviceDataList, shedule, metaobj,deviceModel.getDeviceType());

                } catch (JSONException e) {

                }

            }

            @Override
            public void onFailure(int requestCode, String errorCode, String message) {
                custom_text.setVisibility(View.GONE);
                Log.e("callApitDevice", message + "");
                Functions.Alert(ChartActivity.this, message, Functions.AlertType.Error);

            }

            @Override
            public void onNetworkFailure(int requestCode, String message) {
                custom_text.setVisibility(View.GONE);
                Log.e("callApitDevice", message + "");

            }

        });

        JsonObject object = null;
        object = Functions.getInstance().getJsonObject("deviceId", id);
        Calendar calendar = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();

        if (shedule == Shedule.Today) {
            // calendar2.add(Calendar.DAY_OF_MONTH, -1);
            object.addProperty("dateRange", "today");
            Log.e("shedule", "today");
            // object.addProperty("dateRange", "today");

        } else if (shedule == Shedule.twoHr) {
            //  calendar2.add(Calendar.DAY_OF_MONTH, -1);
            object.addProperty("dateRange", "xhour-2");
            Log.e("shedule", "xhour-2");
            // object.addProperty("dateRange", "today");

        } else if (shedule == Shedule.fourHr) {
            //calendar2.add(Calendar.DAY_OF_MONTH, -1);
            object.addProperty("dateRange", "xhour-4");
            Log.e("shedule", "xhour-4");
            // object.addProperty("dateRange", "today");

        } else if (shedule == Shedule.Week) {
            object.addProperty("dateRange", "xhour-168");
          object.addProperty("avgBy", "hour");
        //    object.addProperty("dateRange", "week");
//            ["limit": 100000, "dateRange": "xhour-168", "avgConversion": true, "wifiConversion": true, "leackConversion": true, "battoryConversion": true, "floatConversion": true, "deviceId": "5972", "avgBy": "hour"]
            Log.e("shedule", "xhour-168");
        } else if (shedule == Shedule.TwentyFour) {
            object.addProperty("dateRange", "xhour-24");
        } else {
            calendar2.add(Calendar.WEEK_OF_MONTH, -1);
            //todo for weekly graph on request
            object.addProperty("avgConversion", true);


        }

        object.addProperty("limit", limit);
        long fromtimestamp = calendar2.getTimeInMillis() / 1000l;
        long Totimestamp = calendar.getTimeInMillis() / 1000l;
        //object.addProperty("fromTimestamp", "" + fromtimestamp);
        //object.addProperty("toTimestamp", "" + Totimestamp);
        /*object.addProperty("dateRange", "" + shedule);*/
        object.addProperty("relayConversion", true);
        object.addProperty("leackConversion", true);
        object.addProperty("floatConversion", true);
        object.addProperty("battoryConversion", false);
        object.addProperty("wifiConversion", false);

        Log.e("timestamp", fromtimestamp + "//" + Totimestamp + "//" + limit + "obj//" + object.toString());

        baseRequest.callAPIPost(1, object, getResources().getString(R.string.api_getDeviceLogList));

    }


    private void getWeekLocalData(String response) {

        chartLayout.setVisibility(View.VISIBLE);
        custom_text.setVisibility(View.GONE);
        // Log.e("callApitDevice", id + "");


        try {
            ArrayList<DeviceDataModel> deviceDataList = new ArrayList<>();
            JSONObject jsonObject;
            JSONObject devicejsonObject = null;
//        Log.e( "getWeekLocalData: ", );
            jsonObject = new JSONObject(response);

            JSONObject metaobj = jsonObject.optJSONObject("metaData");
            Log.e("metadataval", metaobj.optString("pumpCycle") + "//");

//            if (metaobj.has("pumpCycle") && Float.valueOf(metaobj.optString("pumpCycle")) >= 100) {
//
//                grow_btn.setClickable(true);
//                shrink_btn.setClickable(true);
//                grow_btn.setTextColor(getResources().getColor(R.color.white));
//                shrink_btn.setTextColor(getResources().getColor(R.color.white));
//
//            } else {
//                grow_btn.setClickable(false);
//                shrink_btn.setClickable(false);
//                grow_btn.setTextColor(getResources().getColor(R.color.black_overlay));
//                shrink_btn.setTextColor(getResources().getColor(R.color.black_overlay));
//                shrink_btn.setBackground(getResources().getDrawable(R.drawable.white_button_selector));
//                grow_btn.setBackground(getResources().getDrawable(R.drawable.white_button_selector));
//            }
//
//
//            grow_btn.setBackground(getResources().getDrawable(R.drawable.blue_button_selector));
//            shrink_btn.setBackground(getResources().getDrawable(R.drawable.blue_button_selector));
            devicejsonObject = jsonObject.optJSONObject("deviceDataList");
            Iterator<String> iter = devicejsonObject.keys();
            Gson gson = new Gson();
            Log.e("graphdata", devicejsonObject.toString() + "//");

            graphDataViewModel.deleteAll();
            long db_time = System.currentTimeMillis();
            Log.e("Current_time", db_time + "");

            while (iter.hasNext()) {
                String key = iter.next();
                JSONObject value = devicejsonObject.optJSONObject(key);
                DeviceDataModel deviceDataModel = gson.fromJson(value.toString(), DeviceDataModel.class);
                Log.e("deviceDataModel", deviceDataModel.toString());
                deviceDataList.add(deviceDataModel);

//                //final String note_id = UUID.randomUUID().toString();
//                DeviceDataModelDB deviceDataModelDB = new DeviceDataModelDB(db_time, deviceDataModel);
//
//                //delete
//                // graphDataViewModel.delete(deviceDataModelDB);
//                //update
//                // graphDataViewModel.update(deviceDataModelDB);
//                //insert
//                graphDataViewModel.insert(deviceDataModelDB);
//
//                for (int i = 0; i < deviceDataModel.getData().size(); i++) {
//                    GraphModelDB graphModelDB = new GraphModelDB(deviceDataModel.getData().get(i).getValue(), deviceDataModel.getData().get(i).getTimeStamp(), deviceDataModel.getSensorId());
//                    //update
//                    // graphValueViewModel.update(graphModelDB);
//                    //insert
//                    graphValueViewModel.insert(graphModelDB);
//
//                }
                // fetch db_time
//                Log.e("Current_time_fetch ", deviceDataModelDB.getDb_time().toString());
//
//                Log.e("deviceDataModelDB", deviceDataModelDB + "//");
            }

            chartLayout.AddViews(deviceDataList, shedule, metaobj, deviceModel.getDeviceType());

        } catch (JSONException e) {



    }

    }

    @OnClick({R.id.tVToday, R.id.twoHrChart, R.id.fourHrChart, R.id.tVWeek, R.id.tVdropDown, R.id.grow_btn, R.id.shrink_btn, R.id.reinstall_btn, R.id.twfourHrChart})
    public void onViewClicked(View view) {

        switch (view.getId()) {

            case R.id.twoHrChart:
                Log.e("twoHr", "twoHr_ok");
                shedule = Shedule.twoHr;
                changeTime();
                chartLayout.setVisibility(View.GONE);
                // callApitDeviceLogDataList(deviceModel.getId());
                if(PreferenceManager.getDefaultSharedPreferences(ChartActivity.this).getString("theJsonTwoHr","").equalsIgnoreCase("")){
                    apiCallValue = "0";
                    callAPI();
                }
                else{

                    Log.e("today", PreferenceManager.getDefaultSharedPreferences(ChartActivity.this).getString("theJsonTwoHr", ""));
                    getWeekLocalData(PreferenceManager.getDefaultSharedPreferences(ChartActivity.this).getString("theJsonTwoHr", ""));

                }

                break;

            case R.id.fourHrChart:
                Log.e("fourHr", "fourHr_ok");
                shedule = Shedule.fourHr;
                changeTime();
                chartLayout.setVisibility(View.GONE);
                //callApitDeviceLogDataList(deviceModel.getId());
                if(PreferenceManager.getDefaultSharedPreferences(ChartActivity.this).getString("theJsonFourHr","").equalsIgnoreCase("")){
                    apiCallValue = "0";
                    callAPI();
                }
                else{

                    Log.e("today", PreferenceManager.getDefaultSharedPreferences(ChartActivity.this).getString("theJsonFourHr", ""));
                    getWeekLocalData(PreferenceManager.getDefaultSharedPreferences(ChartActivity.this).getString("theJsonFourHr", ""));

                }

                break;

            case R.id.tVToday:
                shedule = Shedule.Today;
                changeTime();
                chartLayout.setVisibility(View.GONE);
                if(PreferenceManager.getDefaultSharedPreferences(ChartActivity.this).getString("theJsonToday","").equalsIgnoreCase("")){
                    apiCallValue = "0";
                    callAPI();
                }
                else{

                    Log.e("today", PreferenceManager.getDefaultSharedPreferences(ChartActivity.this).getString("theJsonToday", ""));
                    getWeekLocalData(PreferenceManager.getDefaultSharedPreferences(ChartActivity.this).getString("theJsonToday", ""));

                }


                Log.e("today", "ok");

                //callApitDeviceLogDataList(deviceModel.getId());

                //getGraphDataLocal();

                break;

            case R.id.tVWeek:

                shedule = Shedule.Week;
                changeTime();
                chartLayout.setVisibility(View.GONE);
//                callApitDeviceLogDataList(deviceModel.getId());
                if(PreferenceManager.getDefaultSharedPreferences(ChartActivity.this).getString("theJsonWeek","").equalsIgnoreCase("")){
                    apiCallValue = "0";
                    callAPI();
                }
                else{

                    Log.e("today", PreferenceManager.getDefaultSharedPreferences(ChartActivity.this).getString("theJsonWeek", ""));


                         //   Converter.fromArrayList(RoomDB.getDatabase(ChartActivity.this).noteDao().getNotes("theJsonWeek"))
                          getWeekLocalData(PreferenceManager.getDefaultSharedPreferences(ChartActivity.this).getString("theJsonWeek", ""));




                }

                //getGraphDataLocal();
                break;

            case R.id.twfourHrChart:
                shedule = Shedule.TwentyFour;
                changeTime();
                chartLayout.setVisibility(View.GONE);
                if(PreferenceManager.getDefaultSharedPreferences(ChartActivity.this).getString("theJsonTwFourhr","").equalsIgnoreCase("")){
                    apiCallValue = "0";
                    callAPI();
                }
                else{
                    Log.e("today", PreferenceManager.getDefaultSharedPreferences(ChartActivity.this).getString("theJsonTwFourhr", ""));
                    getWeekLocalData(PreferenceManager.getDefaultSharedPreferences(ChartActivity.this).getString("theJsonTwFourhr", ""));

                }

                break;

            case R.id.tVdropDown:

                Functions.showListSelection(this, 0, new OnItemClickAdapter() {
                    @Override
                    public void onClick(int i, int position, String item) {

                        tVdropDown.setText(item);
                        value = item;

                    }
                }, Functions.getShrink());

                break;

            case R.id.grow_btn:

                Log.e("vlas", value + "//");
                if (value != null)
                    callApiUpDown(true, deviceModel.getId(), value);
                else
                    Functions.Alert(ChartActivity.this, "Please select values.", Functions.AlertType.Error);

                break;

            case R.id.shrink_btn:

                Log.e("shrink", value + "//");
                if (value != null)
                    callApiUpDown(false, deviceModel.getId(), value);
                else
                    Functions.Alert(ChartActivity.this, "Please select values.", Functions.AlertType.Error);

                break;

            case R.id.reinstall_btn:

                tVdropDown.setText("");
                callApiReset(deviceModel.getId());

                break;
        }

    }

    private void clearPrefrence(){
        PreferenceManager.getDefaultSharedPreferences(ChartActivity.this).edit().remove("theJsonToday").clear().commit();
        PreferenceManager.getDefaultSharedPreferences(ChartActivity.this).edit().remove("theJsonWeek").clear().commit();
        PreferenceManager.getDefaultSharedPreferences(ChartActivity.this).edit().remove("theJsonTwFourhr").clear().commit();
        PreferenceManager.getDefaultSharedPreferences(ChartActivity.this).edit().remove("theJsonTwoHr").clear().commit();
        PreferenceManager.getDefaultSharedPreferences(ChartActivity.this).edit().remove("theJsonFourHr").clear().commit();

    }
    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.toolbarBackImg:
                onBackPressed();
                break;

            case R.id.toolbarRefresh:
                apiCallValue = "0";
                chartLayout.setVisibility(View.GONE);
                //getGraphDataLocal();

//                if(shedule==Shedule.Week){
//                    getWeekLocalData(PreferenceManager.getDefaultSharedPreferences(ChartActivity.this).getString("theJsonWeek", ""));
//                }
//                else if(shedule==Shedule.Today){
//                    getWeekLocalData(PreferenceManager.getDefaultSharedPreferences(ChartActivity.this).getString("theJsonToday", ""));
//                }
//                else if(shedule==Shedule.TwentyFour){
//                    getWeekLocalData(PreferenceManager.getDefaultSharedPreferences(ChartActivity.this).getString("theJsonTwFourhr", ""));
//                }
//                else if(shedule==Shedule.twoHr){
//                    getWeekLo
//                    calData(PreferenceManager.getDefaultSharedPreferences(ChartActivity.this).getString("theJsonTwoHr", ""));
//                }
//                else if(shedule==Shedule.fourHr){
//                    getWeekLocalData(PreferenceManager.getDefaultSharedPreferences(ChartActivity.this).getString("theJsonFourHr", ""));
//                }

                //  callApitDeviceLogDataList(deviceModel.getId());
                callAPI();
                break;
        }

    }
    public enum Shedule {
        twoHr, fourHr, Today, Week, TwentyFour
    }

    Shedule shedule = Shedule.Today;

    void changeTime() {

        if (Shedule.Today == shedule) {

            tVToday.setTextColor(getResources().getColor(R.color.white));
            tVToday.setBackgroundResource(R.drawable.blue_left);
            twoHrChart.setTextColor(getResources().getColor(R.color.blue));
            twoHrChart.setBackgroundResource(R.drawable.white_with_blue_border_left);
            fourHrChart.setTextColor(getResources().getColor(R.color.blue));
            fourHrChart.setBackgroundResource(R.drawable.white_button_selecter);
            tVWeek.setTextColor(getResources().getColor(R.color.blue));
            tVWeek.setBackgroundResource(R.drawable.white_with_blue_border_right);
            twentyFourHrChart.setTextColor(getResources().getColor(R.color.blue));
            twentyFourHrChart.setBackgroundResource(R.drawable.white_with_blue_border_right);
        } else if (Shedule.twoHr == shedule) {
            twoHrChart.setTextColor(getResources().getColor(R.color.white));
            twoHrChart.setBackgroundResource(R.drawable.blue_left);
            fourHrChart.setTextColor(getResources().getColor(R.color.blue));
            fourHrChart.setBackgroundResource(R.drawable.white_button_selecter);
            tVToday.setTextColor(getResources().getColor(R.color.blue));
            tVToday.setBackgroundResource(R.drawable.white_with_blue_border_left);
            tVWeek.setTextColor(getResources().getColor(R.color.blue));
            tVWeek.setBackgroundResource(R.drawable.white_with_blue_border_right);
            twentyFourHrChart.setTextColor(getResources().getColor(R.color.blue));
            twentyFourHrChart.setBackgroundResource(R.drawable.white_with_blue_border_right);

        } else if (Shedule.fourHr == shedule) {
            fourHrChart.setTextColor(getResources().getColor(R.color.white));
            fourHrChart.setBackgroundResource(R.drawable.blue_button_selecter);
            twoHrChart.setTextColor(getResources().getColor(R.color.blue));
            twoHrChart.setBackgroundResource(R.drawable.white_with_blue_border_left);
            tVToday.setTextColor(getResources().getColor(R.color.blue));
            tVToday.setBackgroundResource(R.drawable.white_with_blue_border_left);
            tVWeek.setTextColor(getResources().getColor(R.color.blue));
            tVWeek.setBackgroundResource(R.drawable.white_with_blue_border_right);
            twentyFourHrChart.setTextColor(getResources().getColor(R.color.blue));
            twentyFourHrChart.setBackgroundResource(R.drawable.white_with_blue_border_right);
        } else if (Shedule.Week == shedule) {
            tVWeek.setTextColor(getResources().getColor(R.color.white));
            tVWeek.setBackgroundResource(R.drawable.blue_right);
            fourHrChart.setTextColor(getResources().getColor(R.color.blue));
            fourHrChart.setBackgroundResource(R.drawable.white_button_selecter);
            twoHrChart.setTextColor(getResources().getColor(R.color.blue));
            twoHrChart.setBackgroundResource(R.drawable.white_with_blue_border_left);
            tVToday.setTextColor(getResources().getColor(R.color.blue));
            tVToday.setBackgroundResource(R.drawable.white_with_blue_border_left);
            twentyFourHrChart.setTextColor(getResources().getColor(R.color.blue));
            twentyFourHrChart.setBackgroundResource(R.drawable.white_with_blue_border_right);
        } else if (Shedule.TwentyFour == shedule) {
            twentyFourHrChart.setTextColor(getResources().getColor(R.color.white));
            twentyFourHrChart.setBackgroundResource(R.drawable.blue_right);
            fourHrChart.setTextColor(getResources().getColor(R.color.blue));
            fourHrChart.setBackgroundResource(R.drawable.white_button_selecter);
            twoHrChart.setTextColor(getResources().getColor(R.color.blue));
            twoHrChart.setBackgroundResource(R.drawable.white_with_blue_border_left);
            tVToday.setTextColor(getResources().getColor(R.color.blue));
            tVToday.setBackgroundResource(R.drawable.white_with_blue_border_left);
            tVWeek.setTextColor(getResources().getColor(R.color.blue));
            tVWeek.setBackgroundResource(R.drawable.white_with_blue_border_right);
        } else {
//            fourHrChart.setBackgroundResource(R.drawable.blue_right);
//            twoHrChart.setTextColor(getResources().getColor(R.color.blue));
//            twoHrChart.setBackgroundResource(R.drawable.white_with_blue_border_left);
//            tVWeek.setTextColor(getResources().getColor(R.color.white));
//            tVWeek.setBackgroundResource(R.drawable.blue_right);
//            tVToday.setTextColor(getResources().getColor(R.color.blue));
//            tVToday.setBackgroundResource(R.drawable.white_with_blue_border_left);


            tVToday.setTextColor(getResources().getColor(R.color.white));
            tVToday.setBackgroundResource(R.drawable.blue_left);
            twoHrChart.setTextColor(getResources().getColor(R.color.blue));
            twoHrChart.setBackgroundResource(R.drawable.white_with_blue_border_left);
            fourHrChart.setTextColor(getResources().getColor(R.color.blue));
            fourHrChart.setBackgroundResource(R.drawable.white_button_selecter);
            tVWeek.setTextColor(getResources().getColor(R.color.blue));
            tVWeek.setBackgroundResource(R.drawable.white_with_blue_border_right);
            twentyFourHrChart.setTextColor(getResources().getColor(R.color.blue));
            twentyFourHrChart.setBackgroundResource(R.drawable.white_with_blue_border_right);

        }

    }

    private void callApiUpDown(boolean isUDown, String id, String value) {

        Log.e("downupstrttime", System.currentTimeMillis() + "//");
        Log.e("value", value + "//" + isUDown + "//" + value);
        BaseRequest baseRequest = new BaseRequest(this);
        baseRequest.setRunInBackground(true);
        JsonObject object = new JsonObject();
        object.addProperty("id", id);
        object.addProperty("value", value);
        //object = Functions.getInstance().getJsonObject("id", id, "value", value);

        baseRequest.setBaseRequestListner(new RequestReceiver() {

            @Override
            public void onSuccess(int requestCode, String fullResponse, Object dataObject) {
                Log.e("downupcomplttime", System.currentTimeMillis() + "//");
                Log.e("updownres", fullResponse + "//");
                chartLayout.setVisibility(View.GONE);
                callApitDeviceLogDataList(deviceModel.getId());

            }

            @Override
            public void onFailure(int requestCode, String errorCode, String message) {
                Functions.Alert(ChartActivity.this, message, Functions.AlertType.Error);
            }

            @Override
            public void onNetworkFailure(int requestCode, String message) {
                Log.e("updownerror", message + "//");
            }

        });

        Log.e("dfsd10", getResources().getString(isUDown ? R.string.api_calUp : R.string.api_calDown) + "");
        //baseRequest.callAPIPost(1, object, getResources().getString(isUDown ? R.string.api_calDown : R.string.api_calUp));
        baseRequest.callAPIPost(1, object, getResources().getString(isUDown ? R.string.api_calUp : R.string.api_calDown));

    }

    private void callApiReset(String id) {

        BaseRequest baseRequest = new BaseRequest(ChartActivity.this);
        baseRequest.setRunInBackground(true);
        JsonObject object = null;
        object = Functions.getInstance().getJsonObject("id", id);
        baseRequest.setBaseRequestListner(new RequestReceiver() {
            @Override
            public void onSuccess(int requestCode, String fullResponse, Object dataObject) {
                chartLayout.setVisibility(View.VISIBLE);
                callApitDeviceLogDataList(deviceModel.getId());
            }

            @Override
            public void onFailure(int requestCode, String errorCode, String message) {
                Functions.Alert(ChartActivity.this, message, Functions.AlertType.Error);

            }

            @Override
            public void onNetworkFailure(int requestCode, String message) {

            }

        });

        baseRequest.callAPIPost(1, object, getResources().getString(R.string.api_resetCalibration));

    }


@Override
    public void onDestroy() {
        Log.e("onDistory","ondist");
        // Must always call the super method at the end.
        super.onDestroy();
    }
}



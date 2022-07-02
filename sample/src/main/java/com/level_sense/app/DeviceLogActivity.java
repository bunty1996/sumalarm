package com.level_sense.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.appbar.AppBarLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.level_sense.app.Fragment.MyDeviceFragment;
import com.level_sense.app.RetroFit.BaseRequest;
import com.level_sense.app.RetroFit.RequestReceiver;
import com.level_sense.app.Utility.Functions;
import com.level_sense.app.Utility.LoadMore;
import com.level_sense.app.customScroll.CustomGridLayoutManager;
import com.level_sense.app.model.DeviceEventModel;
import com.level_sense.app.model.DeviceModel;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.level_sense.app.Utility.Functions.noData;

public class DeviceLogActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.appBar)
    AppBarLayout appBar;
    @BindView(R.id.custom_pb)
    AVLoadingIndicatorView customPb;
    @BindView(R.id.list)
    RecyclerView list;
    @BindView(R.id.TvNoData)
    TextView TvNoData;

    @BindView(R.id.toolbar_title)
    TextView toolbar_title;

    @BindView(R.id.toolbarBackImg)
    ImageView toolbarBackImg;

    @BindView(R.id.toolbarRefresh)
    ImageView toolbarRefresh;

    LoadMore loadMore;
    private DeviceLogAdapter myItemRecyclerViewAdapter;
    private CustomGridLayoutManager manager;
    DeviceModel deviceModel;

    public static Intent getIntent(Context context, DeviceModel deviceModel) {
        Intent intent = new Intent(context, DeviceLogActivity.class);
        intent.putExtra(DeviceModel.class.getName(), deviceModel);
        return intent;
    }

    public void getIntentData() {
        deviceModel = (DeviceModel) getIntent().getExtras().get(DeviceModel.class.getName());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_log);
        ButterKnife.bind(this);
     /*   Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_icon);
        getSupportActionBar().setTitle(R.string.alarm_log);*/

        toolbar_title.setText(R.string.alarm_log);
        toolbarBackImg.setImageResource(R.drawable.ic_back_icon);
        toolbarRefresh.setImageResource(R.drawable.ic_refresh);
        toolbarBackImg.setOnClickListener(this);
        toolbarRefresh.setOnClickListener(this);
        manager = new CustomGridLayoutManager(DeviceLogActivity.this);
        getIntentData();
        loadMore = new LoadMore(list);
        myItemRecyclerViewAdapter = new DeviceLogAdapter(new MyDeviceFragment.OnListFragmentInteractionListener() {
            @Override
            public void onListFragmentInteraction(DeviceModel item) {

            }
        });

        myItemRecyclerViewAdapter.onDeviceAdapterTouch(new DeviceLogAdapter.DeviceLogAdapterTouch() {
            @Override
            public void onMessageTextTouch() {
                manager.setScrollEnabled(false);
            }

            @Override
            public void onOutsideTouch() {
                manager.setScrollEnabled(true);
            }

        });

        list.setLayoutManager(manager);

      /*//todo for make textview scrollable
        list.setOnTouchListener(new View.OnTouchListener(){
            public boolean onTouch(View v, MotionEvent event){
               v.findViewById(R.id.tVMessage).getParent().requestDisallowInterceptTouchEvent(true);
                return false;
        }});*/
      /*tVMessage.setOnTouchListener(new View.OnTouchListener(){
            public boolean onTouch(View v, MotionEvent event){
              //Disallow the touch request for parent scroll on touch of child view
                  v.getParent().requestDisallowInterceptTouchEvent(true);
                  return false;
        }});*/

        loadMore.setLoadingMore(false);
        loadMore.setOnLoadMoreListener(new LoadMore.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                callApitDeviceLogDataList(deviceModel.getId());
            }
        });

        list.setAdapter(myItemRecyclerViewAdapter);
        callApitDeviceLogDataList(deviceModel.getId());

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dash_board, menu);
        return true;
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
        BaseRequest baseRequest = new BaseRequest(DeviceLogActivity.this);
        baseRequest.setRunInBackground(false);
        baseRequest.setLoaderView(customPb);
        baseRequest.setBaseRequestListner(new RequestReceiver() {
            @Override
            public void onSuccess(int requestCode, String fullResponse, Object dataObject) {
                // ArrayList<DeviceDataModelDB> deviceDataList = new ArrayList<>();
                JSONObject jsonObject;
                JSONObject devicejsonObject = null;
                Log.e("devicelogres", fullResponse + "//");

                try {
                    jsonObject = new JSONObject(fullResponse);
                    //devicejsonObject = jsonObject.optJSONObject("deviceDataList");
                    devicejsonObject = jsonObject.optJSONObject("deviceLogList");
                    Log.e("devicelogres12", devicejsonObject.toString() + "//");

                } catch (JSONException e) {

                }

              /*Iterator<String> iter= devicejsonObject.keys();
                Gson gson = new Gson();
                while (iter.hasNext()){
                    String key = iter.next();
                    JSONObject value = devicejsonObject.optJSONObject(key);
                    DeviceDataModelDB deviceDataModel = gson.fromJson(value.toString(), DeviceDataModelDB.class);
                    deviceDataList.add(deviceDataModel);
                }*/

                Log.e("listarray", devicejsonObject.optJSONArray("LIST").toString() + "//");
                ArrayList<DeviceEventModel> deviceEventArrayList = baseRequest.getList(devicejsonObject.optJSONArray("LIST"), DeviceEventModel.class);
                DeviceLogActivity.this.deviceEventModelArrayList.addAll(deviceEventArrayList);
                myItemRecyclerViewAdapter.setmValues(DeviceLogActivity.this.deviceEventModelArrayList);
                if (deviceEventArrayList.size() < 10) {
                    loadMore.setLoadingMore(false);
                } else {
                    currentPage++;
                    loadMore.setLoadingMore(true);
                }
                noData(TvNoData, deviceEventArrayList);

            }

            @Override
            public void onFailure(int requestCode, String errorCode, String message) {

                Functions.Alert(DeviceLogActivity.this, message, Functions.AlertType.Error);
            }

            @Override
            public void onNetworkFailure(int requestCode, String message) {

            }
        });

        JsonObject object = null;
        object = Functions.getInstance().getJsonObject("deviceId", id);
        object.addProperty("currentPage", currentPage);
        object.addProperty("limit", 20);

        //baseRequest.callAPIPost(1, object, getResources().getString(R.string.api_getDeviceLogList));
        //change api url from above to below on 1 may 2019
        baseRequest.callAPIPost(1, object, getResources().getString(R.string.api_getDeviceLogListV1));
    }

    //"id": "3", "limit": 10, "currentPage": 1
    ArrayList<DeviceEventModel> deviceEventModelArrayList = new ArrayList<>();
    int currentPage = 1;

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.toolbarBackImg:

                onBackPressed();

                break;

            case R.id.toolbarRefresh:

                callApitDeviceLogDataList(deviceModel.getId());

                break;

        }

    }

}

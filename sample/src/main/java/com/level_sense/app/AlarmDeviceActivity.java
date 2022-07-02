package com.level_sense.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.level_sense.app.Utility.Functions;
import com.level_sense.app.graph.ChartActivity;
import com.level_sense.app.model.AlarmDeviceModel;
import com.level_sense.app.RetroFit.BaseRequest;
import com.level_sense.app.RetroFit.RequestReceiver;
import com.level_sense.app.custom.AlarmDeviceAdapter;
import com.level_sense.app.model.DeviceModel;
import com.tapadoo.alerter.OnHideAlertListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AlarmDeviceActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.submit_btn)
    Button submitBtn;
    @BindView(R.id.custom_pb)
    View custom_pb;
    @BindView(R.id.alarmAdapter)
    AlarmDeviceAdapter alarmAdapter;
  /*@BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;*/

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbar_title;

    @BindView(R.id.toolbarBackImg)
    ImageView toolbarBackImg;

    @BindView(R.id.toolbarRefresh)
    ImageView toolbarRefresh;
    DeviceModel deviceModel;
    Fragment fragment;

    public static Intent getIntent(Context mContext, DeviceModel deviceModel) {
        Intent intent = new Intent(mContext, AlarmDeviceActivity.class);
        intent.putExtra(DeviceModel.class.getName(), deviceModel);
        return intent;
    }

    private void getIntentData() {
        deviceModel = (DeviceModel) getIntent().getExtras().get(DeviceModel.class.getName());

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_device);
        ButterKnife.bind(this);
      /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_back_icon);
        actionbar.setTitle(R.string.alarm_configuration);*/

        toolbar.setPadding(0, 0, 0, 0);
        toolbar.setContentInsetsAbsolute(0, 0);
        toolbar_title.setText(R.string.alarm_configuration);
        toolbarBackImg.setImageResource(R.drawable.ic_back_icon);
        toolbarRefresh.setImageResource(R.drawable.ic_refresh);
        toolbarBackImg.setOnClickListener(this);
        toolbarRefresh.setOnClickListener(this);

        getIntentData();
        CallGetAlarmAPI(deviceModel.getId());

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflate the menu; this adds items to the action bar if it is present.
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

    public void CallGetAlarmAPI(String id) {
        BaseRequest baseRequest = new BaseRequest(AlarmDeviceActivity.this);
        baseRequest.setRunInBackground(false);
        baseRequest.setContainer(custom_pb, null);
        //baseRequest.setLoaderView(mLoader);

        baseRequest.setBaseRequestListner(new RequestReceiver() {
            @Override
            public void onSuccess(int requestCode, String fullResponse, Object dataObject) {
                try {

                    Gson gson = new Gson();
                    Log.e("alrmres", fullResponse + "//");
                    AlarmDeviceModel alarmDeviceModel = gson.fromJson(fullResponse, AlarmDeviceModel.class);

                    if (alarmDeviceModel != null)
                        alarmAdapter.AddViews(AlarmDeviceActivity.this, deviceModel.getId(), deviceModel.getDeviceType(), alarmDeviceModel, submitBtn, custom_pb);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int requestCode, String errorCode, String message) {
                Functions.Alert(AlarmDeviceActivity.this, message, Functions.AlertType.Error, new OnHideAlertListener() {
                    @Override
                    public void onHide() {
                        finish();
                    }
                });
            }

            @Override
            public void onNetworkFailure(int requestCode, String message) {
            }
        });

        JsonObject object = null;
        object = Functions.getInstance().getJsonObject("id", id);
        baseRequest.callAPIPost(1, object, getResources().getString(R.string.api_getAlarmConfig));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbarBackImg:
                onBackPressed();
                break;
            case R.id.toolbarRefresh:
                CallGetAlarmAPI(deviceModel.getId());
                break;
        }
    }
}

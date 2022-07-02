package com.level_sense.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.appbar.AppBarLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.level_sense.app.Utility.Functions;
import com.level_sense.app.Utility.OnItemClickAdapter;
import com.level_sense.app.model.DeviceModel;
import com.level_sense.app.RetroFit.BaseRequest;
import com.level_sense.app.RetroFit.RequestReceiver;
import com.level_sense.app.model.DeviceConfigModel;
import com.level_sense.app.model.DeviceDataModel;
import org.json.JSONException;
import org.json.JSONObject;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DeviceDetailActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.deviceDetailTopLinear)
    LinearLayout deviceDetailTopLinear;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.appBar)
    AppBarLayout appBar;
    @BindView(R.id.tVDeviceName)
    EditText tVDeviceName;
    @BindView(R.id.tVHaveMyDevice)
    TextView tVHaveMyDevice;
    @BindView(R.id.tVcontactMe)
    TextView tVcontactMe;
    @BindView(R.id.tVFirm)
    TextView tVFirm;
    @BindView(R.id.tVsubcriptionExpiration)
    TextView tVsubcriptionExpiration;
    @BindView(R.id.tVMacAddress)
    TextView tVMacAddress;
    @BindView(R.id.tVLastCheckin)
    TextView tVLastCheckin;
    @BindView(R.id.tVCurrentSirenState)
    TextView tVCurrentSirenState;
    @BindView(R.id.tVCurrentRelayState)
    TextView tVCurrentRelayState;
    @BindView(R.id.tVWirelessSignalStrength)
    TextView tVWirelessSignalStrength;
    @BindView(R.id.submit_btn)
    Button submitBtn;
    @BindView(R.id.custom_pb)
    View custom_pb;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.message_tv)
    TextView messageTv;
    @BindView(R.id.loader_view_ll)
    LinearLayout loaderViewLl;
    String updateInterval = "", checkInInterval = "";
    DeviceModel deviceModel;
    @BindView(R.id.toolbarBackImg)
    ImageView toolbarBackImg;
    @BindView(R.id.toolbar_title)
    TextView toolbar_title;

    @BindView(R.id.toolbarRefresh)
    ImageView toolbarRefresh;
    public static Intent getIntent(Context context, DeviceModel deviceModel) {
        Intent intent = new Intent(context, DeviceDetailActivity.class);
        intent.putExtra(DeviceModel.class.getName(), deviceModel);
        return intent;
    }

    public void getIntentData() {
        deviceModel = (DeviceModel) getIntent().getExtras().get(DeviceModel.class.getName());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_detail);
        ButterKnife.bind(this);
        toolbar.setPadding(0,0,0,0);
        toolbar.setContentInsetsAbsolute(0,0);
        toolbar_title.setText(R.string.device_detail);
        toolbarBackImg.setImageResource(R.drawable.ic_back_icon);
        toolbarRefresh.setImageResource(R.drawable.ic_refresh);
        submitBtn.setOnClickListener(this);
        toolbarBackImg.setOnClickListener(this);
        toolbarRefresh.setOnClickListener(this);
        getIntentData();
        callDeviceApi(deviceModel.getId());

        deviceDetailTopLinear.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                //Find the currently focused view, so we can grab the correct window token from it.
                View view =getCurrentFocus();
                //If no view currently has focus, create a new one, just so we can grab a window token from it
                if (view == null) {
                    view = new View( DeviceDetailActivity.this);
                }
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                return false;
            }
        });
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

    public void callDeviceApi(String id) {

        BaseRequest baseRequest = new BaseRequest(this);
        baseRequest.setRunInBackground(false);
        baseRequest.setContainer(custom_pb, null);
        baseRequest.setBaseRequestListner(new RequestReceiver() {
            @Override
            public void onSuccess(int requestCode, String fullResponse, Object dataObject) {

                Log.e("devicedetail", "fullResponse:: " + fullResponse);

                try {

                    JSONObject jsonObject = new JSONObject(fullResponse);
                    Gson gson = new Gson();
                    deviceModel = gson.fromJson(jsonObject.optJSONObject("device").toString(), DeviceModel.class);

                } catch (JSONException e) {

                }

                setDeviceData();

               /*try{
                   JSONObject jsonObject=new JSONObject(fullResponse);
                   SessionParam.setSaveSessionKey(DeviceDetailActivity.this,jsonObject.getString("sessionKey"));
                 }catch(JSONException e){
                 }
                 startActivity(DashBoardActivity.getIntent(DeviceDetailActivity.this));
                 finish();
                 */
            }

            @Override
            public void onFailure(int requestCode, String errorCode, String message) {

                Functions.Alert(DeviceDetailActivity.this, message, Functions.AlertType.Error);
            }

            @Override
            public void onNetworkFailure(int requestCode, String message) {

            }
        });

        JsonObject object = null;
        object = Functions.getInstance().getJsonObject("id", id);

        baseRequest.callAPIPost(1, object, getResources().getString(R.string.api_get_device));
    }

    public void callUpdateDeviceApi(String id) {

        BaseRequest baseRequest = new BaseRequest(this);
        baseRequest.setRunInBackground(false);
        baseRequest.setContainer(custom_pb, null);

        baseRequest.setBaseRequestListner(new RequestReceiver() {
            @Override
            public void onSuccess(int requestCode, String fullResponse, Object dataObject) {

                Functions.Alert(DeviceDetailActivity.this, "Device detail updated", Functions.AlertType.Success);

            }

            @Override
            public void onFailure(int requestCode, String errorCode, String message) {

                Functions.Alert(DeviceDetailActivity.this, message, Functions.AlertType.Error);
            }

            @Override
            public void onNetworkFailure(int requestCode, String message) {

            }

        });

        JsonObject object = null;

        object = Functions.getInstance().getJsonObject("id", id,
                "displayName", tVDeviceName.getText().toString().trim());
        JsonArray jsonArray = new JsonArray();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("configKey", "update_interval");
        jsonObject.addProperty("configVal", updateInterval);

        JsonObject jsonObject2 = new JsonObject();
        jsonObject2.addProperty("configKey", "checkin_interval");
        jsonObject2.addProperty("configVal", checkInInterval);
        jsonArray.add(jsonObject);
        jsonArray.add(jsonObject2);
        object.add("deviceConfig", jsonArray);
        baseRequest.callAPIPost(1, object, getResources().getString(R.string.api_edit_device));

    }

    void setDeviceData() {

        tVCurrentRelayState.setText(deviceModel.getRelayState() == 1 ? "On" : "Off");
        tVCurrentSirenState.setText(deviceModel.getSirenState() == 1 ? "On" : "Off");

        for (DeviceDataModel deviceDataModel : deviceModel.getDeviceDatList()) {

            if (deviceDataModel.getSensorId().equalsIgnoreCase("11")) {

               //tVWirelessSignalStrength.setText(deviceDataModel.getValue() + "dBm" + signalStrenth(deviceDataModel.getValue()));
               //tVWirelessSignalStrength.setText(Integer.parseInt(deviceDataModel.getValue()) + "dBm" + signalStrenth(deviceDataModel.getValue()));
                tVWirelessSignalStrength.setText((int)Math.round(Double.valueOf(deviceDataModel.getValue())) + "dBm" + signalStrenth(deviceDataModel.getValue()));
                break;

            }

        }

        tVLastCheckin.setText(Functions.formateDate(deviceModel.getLastCheckinTime(), "yyyy-MM-dd HH:mm:ss", "dd MMM yyyy  h:mm a"));
        tVsubcriptionExpiration.setText(Functions.formateDate(deviceModel.getDeviceSubscriptionDate(), "yyyy-MM-dd HH:mm:ss", "dd MMM yyyy  h:mm a"));
        tVDeviceName.setText(deviceModel.getDisplayName());
        tVMacAddress.setText(deviceModel.getMac());
        tVFirm.setText(deviceModel.getDeviceFirmware());

        if (deviceModel.getDeviceConfigList() != null && deviceModel.getDeviceConfigList().size() > 0) {

            for (DeviceConfigModel deviceConfigModel : deviceModel.getDeviceConfigList()) {

                int ui = 0, ci = 0;

                if (deviceConfigModel.getConfigKey().equalsIgnoreCase("update_interval")) {

                    try {

                        ui = Integer.parseInt(deviceConfigModel.getConfigVal());

                    } catch (Exception e) {

                    }

                    tVHaveMyDevice.setText("" + Functions.getIntervalInMINHD(ui));
                    updateInterval = deviceConfigModel.getConfigVal();

                } else if (deviceConfigModel.getConfigKey().equalsIgnoreCase("checkin_interval")) {

                    try {

                        ci = Integer.parseInt(deviceConfigModel.getConfigVal());

                    } catch (Exception e) {

                    }

                    tVcontactMe.setText("" + Functions.getIntervalInMINHD(ci));
                    checkInInterval = deviceConfigModel.getConfigVal();

                }

            }

        }

    }

    private boolean isValidate() {
        if (tVDeviceName.getText().toString().trim().length() <= 0) {
            Functions.Alert(this, "Please enter device name.", Functions.AlertType.Error);
            return false;
        } else return true;
    }

//    @OnClick({R.id.submit_btn})
//    void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.submit_btn:
//                if (isValidate())
//                    callUpdateDeviceApi(deviceModel.getId());
//                break;
//        }
//    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.toolbarBackImg:
                onBackPressed();
                break;
            case R.id.toolbarRefresh:
                callDeviceApi(deviceModel.getId());
                break;
            case R.id.submit_btn:
                if (isValidate())
                    callUpdateDeviceApi(deviceModel.getId());
                break;
        }
    }

    @OnClick({R.id.tVHaveMyDevice, R.id.tVcontactMe})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.tVHaveMyDevice:

                Functions.showListSelection(this, 0, new OnItemClickAdapter() {
                    @Override
                    public void onClick(int i, int position, String item) {
                        tVHaveMyDevice.setText(item);
                        updateInterval = "" + Functions.getIntervalInSecond(DeviceDetailActivity.this, item);
                    }
                }, Functions.getInterval(this, 0));

                break;

            case R.id.tVcontactMe:

                Functions.showListSelection(this, 0, new OnItemClickAdapter() {
                    @Override
                    public void onClick(int i, int position, String item) {
                        tVcontactMe.setText(item);
                        checkInInterval = "" + Functions.getIntervalInSecond(DeviceDetailActivity.this, item);

                    }
                }, Functions.getInterval(this, 1));

                break;

        }

    }

    public String signalStrenth(String sv) {
        double signalValue = Double.parseDouble(sv);
        if (signalValue >= -50) {
            return "(Excellent)";
        } else if (signalValue <= -50 && signalValue > -60) {
            return "(Good)";
        } else if (signalValue <= -60 && signalValue > -70) {
            return "(Fair)";
        } else {
            return "(Poor)";
        }

    }

}



package com.level_sense.app.Fragment;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.level_sense.app.Auth.DashBoardActivity;
import com.level_sense.app.Utility.Functions;
import com.level_sense.app.Utility.OnItemClickAdapter;
import com.level_sense.app.R;
import com.level_sense.app.RetroFit.BaseRequest;
import com.level_sense.app.RetroFit.RequestReceiver;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ClaimDeviceFragment extends Fragment {
    @BindView(R.id.tVPower)
    TextView tVPower;
    @BindView(R.id.tVCloud)
    TextView tVCloud;
    @BindView(R.id.tVCalibrate)
    TextView tVCalibrate;
    @BindView(R.id.tVAlarm)
    TextView tVAlarm;
    @BindView(R.id.claim_btn)
    Button claimBtn;
    @BindView(R.id.parent_rl)
    LinearLayout parentRl;
    @BindView(R.id.custom_pb)
    AVLoadingIndicatorView customPb;
    @BindView(R.id.claim_device)
    ImageView claim_device;

    public ClaimDeviceFragment() {

    }

    //TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ClaimDeviceFragment newInstance() {
        ClaimDeviceFragment fragment = new ClaimDeviceFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_claim_device, container, false);
        ButterKnife.bind(this, view);
        claim_device.setImageResource(R.drawable.ic_device);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    int powerColorCode = 0, cloudColorCode = 0, calibrateColorCode = 0, alarmColorCode = 0;

    @OnClick({R.id.tVPower, R.id.tVCloud, R.id.tVCalibrate, R.id.tVAlarm, R.id.claim_btn})
    public void onViewClicked(View view) {

        switch (view.getId()) {

            case R.id.tVPower:

                Functions.showColorListSelection(getActivity(), R.string.select_power, new OnItemClickAdapter() {
                    @Override
                    public void onClick(int i, int position, String item) {

                        powerColorCode = (position + 1);
                        tVPower.setText(item);

                    }
                });

                break;

            case R.id.tVCloud:

                Functions.showColorListSelection(getActivity(), R.string.select_cloud, new OnItemClickAdapter() {
                    @Override
                    public void onClick(int i, int position, String item) {

                        cloudColorCode = (position + 1);
                        tVCloud.setText(item);

                    }
                });

                break;

            case R.id.tVCalibrate:

                Functions.showColorListSelection(getActivity(), R.string.select_calibrate, new OnItemClickAdapter() {
                    @Override
                    public void onClick(int i, int position, String item) {

                        calibrateColorCode = (position + 1);
                        tVCalibrate.setText(item);

                    }
                });

                break;

            case R.id.tVAlarm:

                Functions.showColorListSelection(getActivity(), R.string.select_alarm, new OnItemClickAdapter() {
                    @Override
                    public void onClick(int i, int position, String item) {

                        alarmColorCode = (position + 1);
                        tVAlarm.setText(item);

                    }

                });

                break;

            case R.id.claim_btn:

                callApiClaimDevice();

                break;

        }

    }

    private void callApiClaimDevice() {
        BaseRequest baseRequest = new BaseRequest(getActivity());
        baseRequest.setRunInBackground(false);
        baseRequest.setContainer(customPb, null);
        //baseRequest.setLoaderView(mLoader);
        baseRequest.setBaseRequestListner(new RequestReceiver() {
            @Override
            public void onSuccess(int requestCode, String fullResponse, Object dataObject) {

                //{"success":true,"device":{"deviceSerialNumber":"9hnhrptjof5ijas3o7a0","id":"4254"}}

                Log.e("deviceclain=m", fullResponse + "//");

                try {

                    JSONObject object = new JSONObject(fullResponse);

                    if (object.has("success") && object.optBoolean("success")) {
                        Functions.Alert(getActivity(), getString(R.string.device_claimed_message), Functions.AlertType.Success);
                        //Fragment fragment = MyDeviceFragment.newInstance();
                        MyDeviceFragment fragment=new MyDeviceFragment();
                        Bundle b=new Bundle();
                        b.putString("deviceSerialNumber",object.optJSONObject("device").optString("deviceSerialNumber"));
                        fragment.setArguments(b);
                        ((DashBoardActivity) getActivity()).setFragment(fragment);
                        ((DashBoardActivity) getActivity()).getSupportActionBar().setTitle(R.string.my_devices);
                    }

                } catch (JSONException e) {

                    e.printStackTrace();

                }

            }

            @Override
            public void onFailure(int requestCode, String errorCode, String message) {
                Functions.Alert(getActivity(), message, Functions.AlertType.Error);

            }

            @Override
            public void onNetworkFailure(int requestCode, String message) {

            }

        });

        JsonObject object = null;
        JsonArray jsonArray = new JsonArray();
        jsonArray.add("" + powerColorCode);
        jsonArray.add("" + cloudColorCode);
        jsonArray.add("" + calibrateColorCode);

        jsonArray.add("" + alarmColorCode);
        object = new JsonObject();
        object.add("codes", jsonArray);
        Log.e("obj", object.toString());
        baseRequest.callAPIPost(1, object, getResources().getString(R.string.api_claimDevice));


    }

}



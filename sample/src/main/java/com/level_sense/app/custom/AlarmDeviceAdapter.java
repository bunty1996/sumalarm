package com.level_sense.app.custom;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.level_sense.app.R;
import com.level_sense.app.RetroFit.BaseRequest;
import com.level_sense.app.RetroFit.RequestReceiver;
import com.level_sense.app.Utility.CSPreferences;
import com.level_sense.app.Utility.Functions;
import com.level_sense.app.Utility.OnItemClickAdapter;
import com.level_sense.app.model.AlarmDeviceModel;
import com.level_sense.app.model.Delay;
import com.level_sense.app.model.DeviceConfigMeta;
import com.level_sense.app.model.DeviceConfigModel;
import com.level_sense.app.model.DeviceConfigurationModel;
import com.level_sense.app.model.Input1;
import com.level_sense.app.model.SensorLimit;
import com.level_sense.app.model.SensorLimitMeta;
import com.level_sense.app.util.Function;
import com.shopify.graphql.support.Nullable;
import com.tapadoo.alerter.OnHideAlertListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

//import static com.level_sense.app.R2.id.edt_humidity1;

public class AlarmDeviceAdapter extends LinearLayout {

    private String fullResponse;

    private List<Delay> delayList;
    private Context activity;

    DeviceConfigurationModel deviceModel = new DeviceConfigurationModel();


    public AlarmDeviceAdapter(Context context) {
        super(context);
    }

    public AlarmDeviceAdapter(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AlarmDeviceAdapter(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void AddViews(final Context mContext, final String id, final String devicetype, AlarmDeviceModel deviceConfigModel, Button button, View pBview) {
        this.activity = mContext;

        ArrayList<String> operationlist = new ArrayList<>();
        operationlist.add("+");
        operationlist.add("-");


        if (devicetype.equalsIgnoreCase("LS_PRO")) {
            //Log.e("myviews", "2 ");
            Log.e("myviews", "2 ");
            final ArrayList<SensorLimit> sensorLimits = deviceConfigModel.getDevice().getSensorLimit();
            final ArrayList<DeviceConfigurationModel> deviceConfig = new ArrayList<>();


            Collections.sort(sensorLimits, new Comparator<SensorLimit>() {

                public int compare(SensorLimit o1, SensorLimit o2) {
                    if (o1.getSensorIntId() == o2.getSensorIntId()) {
                        return 0;
                    } else if (o1.getSensorIntId() < o2.getSensorIntId()) {
                        return -1;
                    }
                    return 1;
                }

            });

            SensorLimitMeta sensorLimitMeta = deviceConfigModel.getDevice().getSensorLimitMeta();

            if (deviceConfigModel != null && sensorLimits != null && sensorLimits.size() > 0)
                button.setVisibility(VISIBLE);

            removeAllViews();

            for (int i = 0; i < sensorLimits.size(); i++) {
                final View view = LayoutInflater.from(getContext()).inflate(R.layout.content_alam_device, null);
                TextView tvDisplayName = (TextView) view.findViewById(R.id.tvDisplayName);
                TextView tVtemperature = (TextView) view.findViewById(R.id.tVtemperature);
                TextView tVAlarmBellow = (TextView) view.findViewById(R.id.tVAlarmBellow);
                TextView tVAlarmAbove = (TextView) view.findViewById(R.id.tVAlarmAbove);
                TextView tVAlarmInput = (TextView) view.findViewById(R.id.tVAlarmInput);
                TextView tVSensorName = (TextView) view.findViewById(R.id.tVSensorName);
                CheckBox cBRelay = (CheckBox) view.findViewById(R.id.cBRelay);
                CheckBox cBSiren = (CheckBox) view.findViewById(R.id.cBSiren);
                CheckBox cBEmailAndText = (CheckBox) view.findViewById(R.id.cBEmailAndText);
                CheckBox cBVoice = (CheckBox) view.findViewById(R.id.cBVoice);
                final TextView tVCelciou = (TextView) view.findViewById(R.id.tVCelcious);
                final TextView tVFehrenheit = (TextView) view.findViewById(R.id.tVFehrenheit);
                LinearLayout lLTempUnit = (LinearLayout) view.findViewById(R.id.lLTempUnit);
                TextView tVLevelSensor = (TextView) view.findViewById(R.id.tVLevelSensor);
                LinearLayout lLSensor = (LinearLayout) view.findViewById(R.id.lLsensor);
                LinearLayout alarmLabel = (LinearLayout) view.findViewById(R.id.alarmLabel);
                cBRelay.setTag(i);
                cBSiren.setTag(i);
                cBEmailAndText.setTag(i);
                cBVoice.setTag(i);
                tVAlarmBellow.setTag(i);
                tVAlarmAbove.setTag(i);
                tVAlarmInput.setTag(i);
                Log.e("getSensorId", sensorLimits.get(i).getSensorId() + "");

                if (sensorLimits.get(i).getLcl() != null) {

                    if (sensorLimits.get(i).getSensorId().equalsIgnoreCase("7") || sensorLimits.get(i).getSensorId().equalsIgnoreCase("8")) {

                        tVAlarmBellow.setVisibility(GONE);
                        tVAlarmAbove.setVisibility(GONE);
                        tVAlarmInput.setVisibility(VISIBLE);
                        lLSensor.setVisibility(VISIBLE);
                        alarmLabel.setVisibility(GONE);
                        if (sensorLimits.get(i).getCurrentValue().equalsIgnoreCase("open")) {
                            tVtemperature.setTextColor(getResources().getColor(R.color.green));
                        } else {
                            tVtemperature.setTextColor(getResources().getColor(R.color.red_));
                        }

                        if (Integer.valueOf(sensorLimits.get(i).getLcl()) == 65535 && Integer.valueOf(sensorLimits.get(i).getUcl()) == 700) {
                            for (Input1 input : sensorLimitMeta.getInput1()) {
                                if (input.getValue().toLowerCase().equalsIgnoreCase("open")) {
                                    tVAlarmInput.setText(input.getLabel());

                                    break;
                                }
                            }
                        } else {
                            for (Input1 input : sensorLimitMeta.getInput1()) {
                                if (input.getValue().toLowerCase().equalsIgnoreCase("closed")) {
                                    tVAlarmInput.setText(input.getLabel());
                                    break;
                                }
                            }
                        }

                    } else if (sensorLimits.get(i).getSensorId().equalsIgnoreCase("2") || sensorLimits.get(i).getSensorId().equalsIgnoreCase("3")) {

                        lLSensor.setVisibility(VISIBLE);
                        tVAlarmBellow.setVisibility(VISIBLE);
                        tVAlarmAbove.setVisibility(VISIBLE);
                        tVAlarmInput.setVisibility(GONE);
                        lLSensor.setVisibility(VISIBLE);
                        alarmLabel.setVisibility(VISIBLE);
                        Log.e("vallsll", sensorLimits.get(i).getLcl() + "//" + sensorLimits.get(i).getCurrentValue() + "//" + sensorLimits.get(i).getUcl() + "//" + sensorLimits.get(i).getCurrentValue());
                        Log.e("vallsll123", (((Double.valueOf(sensorLimits.get(i).getLcl())) > (Double.valueOf(sensorLimits.get(i).getCurrentValue()))) || ((Double.valueOf(sensorLimits.get(i).getUcl())) < (Double.valueOf(sensorLimits.get(i).getCurrentValue())))) + "//");
                        Log.e("sensorid", sensorLimits.get(i).getSensorId() + "//" + sensorLimits.get(i).getSensorDisplayName());
                        if (sensorLimits.get(i).getSensorId().equalsIgnoreCase("3")) {
                            if (((Double.valueOf(sensorLimits.get(i).getLcl())) > (Double.valueOf(sensorLimits.get(i).getCurrentValue()))) || ((Double.valueOf(sensorLimits.get(i).getUcl())) < (Double.valueOf(sensorLimits.get(i).getCurrentValue()))))
                                tVtemperature.setTextColor(getResources().getColor(R.color.red_));
                        }
                        if (sensorLimits.get(i).getSensorId().equalsIgnoreCase("2")) {
                            if (((Double.valueOf(sensorLimits.get(i).getLcl())) > (Double.valueOf(sensorLimits.get(i).getCurrentValue()))) || ((Double.valueOf(sensorLimits.get(i).getUcl())) < (Double.valueOf(sensorLimits.get(i).getCurrentValue()))))
                                tVtemperature.setTextColor(getResources().getColor(R.color.red_));
                        }

                    /*else{
                        tVtemperature.setTextColor(getResources().getColor(R.color.black_overlay));
                    }*/

                    } else if (sensorLimits.get(i).getSensorId().equalsIgnoreCase("1")) {

                        tVLevelSensor.setVisibility(GONE);
                        //tVLevelSensor.setText(Html.fromHtml(sensorLimitMeta.getCapSense()));
                        lLSensor.setVisibility(GONE);
                        //lLSensor.setVisibility(VISIBLE);
                        alarmLabel.setVisibility(GONE);
                        tVAlarmBellow.setVisibility(GONE);
                        tVAlarmAbove.setVisibility(GONE);

                        cBRelay.setVisibility(GONE);
                        cBSiren.setVisibility(GONE);
                        cBEmailAndText.setVisibility(GONE);
                        cBVoice.setVisibility(GONE);


                        if (deviceConfigModel.getDevice().getPumpCycle() != null && !deviceConfigModel.getDevice().getPumpCycle().equalsIgnoreCase("")) {

                            //if(!deviceConfigModel.getDevice().getPumpCycle().equalsIgnoreCase("100")){
                            if (Integer.valueOf(deviceConfigModel.getDevice().getPumpCycle()) < 100) {

                                tVSensorName.setText("Calibrating ... " + deviceConfigModel.getDevice().getPumpCycle() + "%");

                            } else {

                                tVSensorName.setText("Calibrated : " + deviceConfigModel.getDevice().getPumpCycle() + "%");

                            }

                            //tVSensorName.setText("Pump Cycles: "+deviceConfigModel.getDevice().getPumpCycle()+"%");
                            tVSensorName.setVisibility(VISIBLE);

                        } else {

                            tVSensorName.setVisibility(GONE);

                        }

                    } else {
                        if (sensorLimits.get(i).getSensorId().equalsIgnoreCase("5")) {
                            if (sensorLimits.get(i).getCurrentValue().equalsIgnoreCase("Fault")) {
                                tVtemperature.setTextColor(getResources().getColor(R.color.red_));
                            } else {
                                tVtemperature.setTextColor(getResources().getColor(R.color.green));
                            }
                        }

                        tVSensorName.setVisibility(GONE);
                        lLSensor.setVisibility(VISIBLE);
                        tVLevelSensor.setVisibility(GONE);
                        tVAlarmBellow.setVisibility(GONE);
                        tVAlarmAbove.setVisibility(GONE);
                        tVAlarmInput.setVisibility(GONE);
                        alarmLabel.setVisibility(GONE);
                    }

                }

                cBRelay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                        int position = Integer.parseInt(compoundButton.getTag().toString());
                        sensorLimits.get(position).setRelay("" + (b == true ? 2 : 0));
                        //sensorLimits.get(position).setRelay((long) (b == true ? 2 : 0));

                    }

                });

                cBSiren.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                        sensorLimits.get(Integer.parseInt(compoundButton.getTag().toString())).setSiren("" + (b == true ? 1 : 0));
                        //sensorLimits.get(Integer.parseInt(compoundButton.getTag().toString())).setSiren((long) (b == true ? 1 : 0));

                    }

                });

                cBEmailAndText.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                   /*sensorLimits.get(Integer.parseInt(compoundButton.getTag().toString())).setEmail((long) (b == true ? 1 : 0));
                    sensorLimits.get(Integer.parseInt(compoundButton.getTag().toString())).setText((long) (b == true ? 1 : 0));*/

                        sensorLimits.get(Integer.parseInt(compoundButton.getTag().toString())).setEmail("" + (b == true ? 1 : 0));
                        //sensorLimits.get(Integer.parseInt(compoundButton.getTag().toString())).setText("" + (b == true ? 1 : 0));

                    }

                });

                cBVoice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                        //sensorLimits.get(Integer.parseInt(compoundButton.getTag().toString())).setVoice((long) (b == true ? 1 : 0));
                        //sensorLimits.get(Integer.parseInt(compoundButton.getTag().toString())).setVoice("" + (b == true ? 1 : 0));

                        sensorLimits.get(Integer.parseInt(compoundButton.getTag().toString())).setText("" + (b == true ? 1 : 0));

                    }
                });

                final SensorLimit sensorLimit = sensorLimits.get(i);
                if (Integer.valueOf(sensorLimit.getEnabled()) == 0) {
                    tVLevelSensor.setVisibility(VISIBLE);
                    lLSensor.setVisibility(GONE);
                } else {
                    tVLevelSensor.setVisibility(GONE);
                    lLSensor.setVisibility(VISIBLE);
                }

                cBRelay.setChecked(Integer.valueOf(sensorLimit.getRelay()) == 2);
                cBEmailAndText.setChecked(Integer.valueOf(sensorLimit.getEmail()) == 1);
                //  cBEmailAndText.setChecked(Integer.valueOf(sensorLimit.getEmail()) == 1 && Integer.valueOf(sensorLimit.getText()) == 1);

                //todo for hide on request on Sam on 19June 2019 in xml file
                //cBVoice.setChecked(Integer.valueOf(sensorLimit.getVoice()) == 1);
                cBVoice.setChecked(Integer.valueOf(sensorLimit.getText()) == 1);
                cBSiren.setChecked(Integer.valueOf(sensorLimit.getSiren()) == 1);

                //todo for hide on request on Sam on 19June 2019
            /*if(sensorLimit.getSensorSlug().equalsIgnoreCase("tempc")){
                lLTempUnit.setVisibility(VISIBLE);
             }else{
                lLTempUnit.setVisibility(GONE);
             }*/

                tvDisplayName.setText(sensorLimit.getSensorDisplayName() + ": ");
                tVtemperature.setText(sensorLimit.getCurrentValue() + sensorLimit.getSensorDisplayUnits());

                tVCelciou.setTag(sensorLimit);
                tVFehrenheit.setTag(sensorLimit);

                if (sensorLimit.getSensorDisplayUnits().equalsIgnoreCase("C")) {

                    tVCelciou.setTextColor(getResources().getColor(R.color.white));
                    tVFehrenheit.setTextColor(getResources().getColor(R.color.blue));
                    tVCelciou.setBackgroundResource(R.drawable.blue_left);
                    tVFehrenheit.setBackgroundResource(R.drawable.white_with_blue_border_right);

                } else {

                    tVFehrenheit.setTextColor(getResources().getColor(R.color.white));
                    tVCelciou.setTextColor(getResources().getColor(R.color.blue));
                    tVFehrenheit.setBackgroundResource(R.drawable.blue_right);
                    tVCelciou.setBackgroundResource(R.drawable.white_with_blue_border_left);

                }

                tVCelciou.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        sensorLimit.setSensorDisplayUnits("C");

                        if (sensorLimit.getSensorDisplayUnits().equalsIgnoreCase("C")) {

                            tVCelciou.setTextColor(getResources().getColor(R.color.white));
                            tVFehrenheit.setTextColor(getResources().getColor(R.color.blue));
                            tVCelciou.setBackgroundResource(R.drawable.blue_left);
                            tVFehrenheit.setBackgroundResource(R.drawable.white_with_blue_border_right);
                            //tVtemperature.setText(Functions.convertToCelcious(sensorLimit.getCurrentValue()));
                            //sensorLimit.setCurrentValue(Functions.convertToCelcious(sensorLimit.getCurrentValue()) + "C");

                        } else {

                            tVFehrenheit.setTextColor(getResources().getColor(R.color.white));
                            tVCelciou.setTextColor(getResources().getColor(R.color.blue));
                            tVFehrenheit.setBackgroundResource(R.drawable.blue_right);
                            tVCelciou.setBackgroundResource(R.drawable.white_with_blue_border_left);
                            //tVtemperature.setText(Functions.convertToFarenhit(sensorLimit.getCurrentValue()));
                            //sensorLimit.setCurrentValue(Functions.convertToFarenhit(sensorLimit.getCurrentValue()) + "F");

                        }

                    }

                });

                tVFehrenheit.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        sensorLimit.setSensorDisplayUnits("F");

                        if (sensorLimit.getSensorDisplayUnits().equalsIgnoreCase("F")) {

                            tVFehrenheit.setTextColor(getResources().getColor(R.color.white));
                            tVCelciou.setTextColor(getResources().getColor(R.color.blue));
                            tVFehrenheit.setBackgroundResource(R.drawable.blue_right);
                            tVCelciou.setBackgroundResource(R.drawable.white_with_blue_border_left);
                            //tVtemperature.setText(Functions.convertToFarenhit(sensorLimit.getCurrentValue()));
                            //sensorLimit.setCurrentVafflue(Functions.convertToFarenhit(sensorLimit.getCurrentValue()) + "F");

                        } else {

                            tVCelciou.setTextColor(getResources().getColor(R.color.white));
                            tVFehrenheit.setTextColor(getResources().getColor(R.color.blue));
                            tVCelciou.setBackgroundResource(R.drawable.blue_left);
                            tVFehrenheit.setBackgroundResource(R.drawable.white_with_blue_border_right);
                            //tVtemperature.setText(Functions.convertToCelcious(sensorLimit.getCurrentValue()));
                            //sensorLimit.setCurrentValue(Functions.convertToCelcious(sensorLimit.getCurrentValue()) + "C");

                        }

                    }

                });

                tVAlarmBellow.setText("" + sensorLimit.getLcl());
                tVAlarmAbove.setText("" + sensorLimit.getUcl());

                tVAlarmBellow.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Functions.showListSelection(mContext, 0, new OnItemClickAdapter() {
                            @Override
                            public void onClick(int i, int position, String item) {

                                int pos = Integer.parseInt(tVAlarmBellow.getTag().toString());
                                //sensorLimits.get(pos).setLcl(Long.parseLong(item));
                                sensorLimits.get(pos).setLcl(item);
                                tVAlarmBellow.setText(item);

//                                sdbvjkvbk

                            }

                        }, Functions.getStringList(sensorLimitMeta, sensorLimit, true));

                    }

                });

                tVAlarmAbove.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Functions.showListSelection(mContext, 0, new OnItemClickAdapter() {
                            @Override
                            public void onClick(int i, int position, String item) {

                                int pos = Integer.parseInt(view.getTag().toString());
                                //sensorLimits.get(pos).setUcl(Long.parseLong(item));
                                sensorLimits.get(pos).setUcl(item);
                                tVAlarmAbove.setText(item);

                            }

                        }, Functions.getStringList(sensorLimitMeta, sensorLimit, false));

                    }

                });

                tVAlarmInput.setTag(i);

                tVAlarmInput.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Functions.showListSelection(mContext, 0, new OnItemClickAdapter() {
                            @Override
                            public void onClick(int i, int position, String item) {

                                int pos = Integer.parseInt(view.getTag().toString());

                                if (sensorLimitMeta.getInput1().get(position).getValue().toLowerCase().equalsIgnoreCase("open")) {

                                    sensorLimits.get(pos).setLcl("" + 65535);
                                    sensorLimits.get(pos).setUcl("" + 700);

                              /*sensorLimits.get(pos).setLcl((long) 65535);
                                sensorLimits.get(pos).setUcl((long) 700);*/

                                } else {

                                    sensorLimits.get(pos).setLcl("" + 1000);
                                    sensorLimits.get(pos).setUcl("" + 65535);

                              /*sensorLimits.get(pos).setLcl((long) 1000);
                                sensorLimits.get(pos).setUcl((long) 65535);*/

                                }

                                tVAlarmInput.setText(item);

                            }

                        }, Functions.getInputsString(sensorLimitMeta));

                    }

                });

                addView(view);

            }

            button.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {

                    callUpdateDeviceApi(mContext, id, sensorLimits, deviceConfig, pBview);

                }

            });

        } else {
            final ArrayList<SensorLimit> sensorLimits = deviceConfigModel.getDevice().getSensorLimit();
            final List<Delay> deviceConfigMetas = deviceConfigModel.getDevice().getDeviceConfigMeta().getDelayList();
            final ArrayList<DeviceConfigurationModel> deviceConfig = new ArrayList<>();
            DeviceConfigurationModel deviceConfigurationModel = new DeviceConfigurationModel();


            Collections.sort(sensorLimits, new Comparator<SensorLimit>() {

                public int compare(SensorLimit o1, SensorLimit o2) {
                    if (o1.getSensorIntId() == o2.getSensorIntId()) {
                        return 0;
                    } else if (o1.getSensorIntId() < o2.getSensorIntId()) {
                        return -1;
                    }
                    return 1;
                }

            });

            SensorLimitMeta sensorLimitMeta = deviceConfigModel.getDevice().getSensorLimitMeta();


            if (deviceConfigModel != null && sensorLimits != null && sensorLimits.size() > 0)
                button.setVisibility(VISIBLE);

            removeAllViews();

            for (int i = 0; i < sensorLimits.size(); i++) {

                final View view = LayoutInflater.from(getContext()).inflate(R.layout.content_alam_device, null);
                TextView tvDisplayName = (TextView) view.findViewById(R.id.tvDisplayName);
                TextView tVtemperature = (TextView) view.findViewById(R.id.tVtemperature);
                TextView tVAlarmBellow = (TextView) view.findViewById(R.id.tVAlarmBellow);
                TextView tVAlarmAbove = (TextView) view.findViewById(R.id.tVAlarmAbove);
                TextView tVAlarmInput = (TextView) view.findViewById(R.id.tVAlarmInput);
                TextView tVSensorName = (TextView) view.findViewById(R.id.tVSensorName);
                CheckBox cBRelay = (CheckBox) view.findViewById(R.id.cBRelay);
                CheckBox cBSiren = (CheckBox) view.findViewById(R.id.cBSiren);
                CheckBox cBEmailAndText = (CheckBox) view.findViewById(R.id.cBEmailAndText);
                CheckBox cBVoice = (CheckBox) view.findViewById(R.id.cBVoice);
                final TextView tVCelciou = (TextView) view.findViewById(R.id.tVCelcious);
                final TextView tVFehrenheit = (TextView) view.findViewById(R.id.tVFehrenheit);
                LinearLayout lLTempUnit = (LinearLayout) view.findViewById(R.id.lLTempUnit);
                TextView tVLevelSensor = (TextView) view.findViewById(R.id.tVLevelSensor);
                LinearLayout lLSensor = (LinearLayout) view.findViewById(R.id.lLsensor);
                LinearLayout alarmLabel = (LinearLayout) view.findViewById(R.id.alarmLabel);

                ////////////
                LinearLayout linear_delayTemp = view.findViewById(R.id.linear_delayTemp);
                LinearLayout linear_offsetEquation = view.findViewById(R.id.linear_offsetEquation);
                LinearLayout linear_temp_operation = view.findViewById(R.id.linear_temp_operation);
                LinearLayout linear_relativeHumidity = view.findViewById(R.id.linear_relativeHumidity);
                LinearLayout linear_delay_humidityAlarm = view.findViewById(R.id.linear_delay_humidityAlarm);

                TextView txt_delay_temprature = view.findViewById(R.id.txt_delay_temprature);
                TextView txt_offset_equation = view.findViewById(R.id.txt_offset_equation);

                EditText edt_temprature = view.findViewById(R.id.edt_temprature);
                TextView txt_spin_operation = view.findViewById(R.id.txt_spin_operation);

                Spinner spinner2 = view.findViewById(R.id.spinner2);

                View line_view = view.findViewById(R.id.line_view);
                line_view.setVisibility(GONE);

                TextView txt_relativeHumidity = view.findViewById(R.id.txt_relativeHumidity);
                txt_relativeHumidity.setVisibility(VISIBLE);

                EditText edt_humidity1 = view.findViewById(R.id.edt_humidity1);
                EditText edt_humidity2 = view.findViewById(R.id.edt_humidity2);

                TextView txt_spin_HumidityAlarm = view.findViewById(R.id.txt_spin_HumidityAlarm);

                //////////

                cBRelay.setVisibility(INVISIBLE);
                cBRelay.setTag(i);
                cBSiren.setTag(i);
                cBEmailAndText.setTag(i);
                cBVoice.setTag(i);
                tVAlarmBellow.setTag(i);
                tVAlarmAbove.setTag(i);
                tVAlarmInput.setTag(i);
                Log.e("getSensorId", sensorLimits.get(i).getSensorId() + "");

                if (sensorLimits.get(i).getLcl() != null) {

                    if (sensorLimits.get(i).getSensorId().equalsIgnoreCase("7") || sensorLimits.get(i).getSensorId().equalsIgnoreCase("8")) {

                        tVAlarmBellow.setVisibility(GONE);
                        tVAlarmAbove.setVisibility(GONE);
                        tVAlarmInput.setVisibility(VISIBLE);
                        lLSensor.setVisibility(VISIBLE);
                        alarmLabel.setVisibility(GONE);
                        if (sensorLimits.get(i).getCurrentValue().equalsIgnoreCase("open")) {
                            tVtemperature.setTextColor(getResources().getColor(R.color.green));
                        } else {
                            tVtemperature.setTextColor(getResources().getColor(R.color.red_));
                        }
                        if (Integer.valueOf(sensorLimits.get(i).getLcl()) == 65535 && Integer.valueOf(sensorLimits.get(i).getUcl()) == 700) {
                            for (Input1 input : sensorLimitMeta.getInput1()) {
                                if (input.getValue().toLowerCase().equalsIgnoreCase("open")) {
                                    tVAlarmInput.setText(input.getLabel());
                                    break;
                                }
                            }
                        } else {
                            for (Input1 input : sensorLimitMeta.getInput1()) {
                                if (input.getValue().toLowerCase().equalsIgnoreCase("closed")) {
                                    tVAlarmInput.setText(input.getLabel());
                                    break;
                                }
                            }
                        }

                    } else if (sensorLimits.get(i).getSensorId().equalsIgnoreCase("2")) {
//                        lLSensor.setVisibility(VISIBLE);
                        tVAlarmBellow.setVisibility(VISIBLE);
                        tVAlarmAbove.setVisibility(VISIBLE);
                        tVAlarmInput.setVisibility(GONE);
                        lLSensor.setVisibility(VISIBLE);
                        alarmLabel.setVisibility(VISIBLE);
                        linear_delayTemp.setVisibility(VISIBLE);
                        linear_offsetEquation.setVisibility(VISIBLE);
                        linear_temp_operation.setVisibility(VISIBLE);
                        linear_relativeHumidity.setVisibility(VISIBLE);
                        linear_delay_humidityAlarm.setVisibility(VISIBLE);
//                        sensorLimits.get(i).setLcl(edt_humidity1.getText().toString());
//                        sensorLimits.get(i).setUcl(edt_humidity2.getText().toString());

//                        Log.e("vallsll", sensorLimits.get(i).getLcl() + "//" + sensorLimits.get(i).getCurrentValue() + "//" + sensorLimits.get(i).getUcl() + "//" + sensorLimits.get(i).getCurrentValue());
//                        Log.e("vallsll123", (((Double.valueOf(sensorLimits.get(i).getLcl())) > (Double.valueOf(sensorLimits.get(i).getCurrentValue()))) || ((Double.valueOf(sensorLimits.get(i).getUcl())) < (Double.valueOf(sensorLimits.get(i).getCurrentValue())))) + "//");
//                        Log.e("sensorid", sensorLimits.get(i).getSensorId() + "//" + sensorLimits.get(i).getSensorDisplayName());
                        if (!sensorLimits.get(i).getCurrentValue().equals(null) && !sensorLimits.get(i).getLcl().equals(null) && !sensorLimits.get(i).getCurrentValue().isEmpty() && !sensorLimits.get(i).getLcl().isEmpty()) {
                            if (((Double.valueOf(sensorLimits.get(i).getLcl())) > (Double.valueOf(sensorLimits.get(i).getCurrentValue()))) || ((Double.valueOf(sensorLimits.get(i).getUcl())) < (Double.valueOf(sensorLimits.get(i).getCurrentValue())))) {
                                tVtemperature.setTextColor(getResources().getColor(R.color.red_));
                            } else {
                                tVtemperature.setTextColor(getResources().getColor(R.color.text_grey));
                            }

                        }

//                        sensorLimits.get(3).setLcl(edt_humidity1.getText().toString());
//                        sensorLimits.get(3).setUcl(edt_humidity2.getText().toString());


//                        sensorLimits.get(4).setLcl(edt_humidity1.getText().toString());
//                        sensorLimits.get(4).setUcl(edt_humidity2.getText().toString());

//                    } else if (deviceConfigModel.getDevice().getSensorLimit().get(i).getSensorId().equalsIgnoreCase("3")) {

                    }
//                    else if (sensorLimits.get(2).getSensorId().equalsIgnoreCase("3")) {

                    for (int k = 0; k < deviceConfigModel.getDevice().getSensorLimit().size(); k++) {
//                        if (deviceConfigModel.getDevice().getSensorLimit().get(k).getSensorDisplayName().equalsIgnoreCase("Relative humidity")) {
                        if (sensorLimits.get(k).getSensorId().equalsIgnoreCase("3")) {
//                            line_view.setVisibility(VISIBLE);

                            Log.e("cdcmdm1", sensorLimits.get(k).getCurrentValue() + "%");
                            Log.e("cdcmdm2", sensorLimits.get(k).getLcl());
                            Log.e("cdcmdm3", sensorLimits.get(k).getUcl());


                            edt_humidity1.setText(sensorLimits.get(k).getLcl());
                            edt_humidity2.setText(sensorLimits.get(k).getUcl());



                            //                        sensorLimits.get(i).setLcl(edt_humidity1.getText().toString());
//                        sensorLimits.get(i).setUcl(edt_humidity2.getText().toString());

                            String humidity = deviceConfigModel.getDevice().getSensorLimit().get(k).getCurrentValue() + "%";
                            txt_relativeHumidity.setText(humidity);
                        }
                    }


                }
//                }

                int finalI = 2;

                edt_humidity1.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                        sensorLimits.get(finalI).setLcl(edt_humidity1.getText().toString());
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
//                        sensorLimits.get(finalI).setLcl(s.toString());
//                        sensorLimits.get(pos).setUcl(item);
                    }
                });

                edt_humidity2.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                        sensorLimits.get(finalI).setUcl(edt_humidity2.getText().toString());
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
//                        sensorLimits.get(finalI).setUcl(s.toString());
                    }
                });

                //////

                edt_temprature.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

//                        sensorLimits.get(finalI).setLcl(edt_humidity1.getText().toString());

                        deviceConfigModel.getDevice().getDeviceConfig().get(finalI).setConfigVal(edt_temprature.getText().toString());
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                        deviceModel.setSentry_offset(edt_temprature.getText().toString());
//                        sensorLimits.get(finalI).setLcl(s.toString());
//                        sensorLimits.get(pos).setUcl(item);
                    }
                });

                /////

                cBRelay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                        int position = Integer.parseInt(compoundButton.getTag().toString());
                        sensorLimits.get(position).setRelay("" + (b == true ? 2 : 0));
                        //sensorLimits.get(position).setRelay((long) (b == true ? 2 : 0));

                    }

                });

                cBSiren.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                        sensorLimits.get(Integer.parseInt(compoundButton.getTag().toString())).setSiren("" + (b == true ? 1 : 0));
                        //sensorLimits.get(Integer.parseInt(compoundButton.getTag().toString())).setSiren((long) (b == true ? 1 : 0));

                    }

                });

                cBEmailAndText.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                   /*sensorLimits.get(Integer.parseInt(compoundButton.getTag().toString())).setEmail((long) (b == true ? 1 : 0));
                    sensorLimits.get(Integer.parseInt(compoundButton.getTag().toString())).setText((long) (b == true ? 1 : 0));*/

                        sensorLimits.get(Integer.parseInt(compoundButton.getTag().toString())).setEmail("" + (b == true ? 1 : 0));
                        //sensorLimits.get(Integer.parseInt(compoundButton.getTag().toString())).setText("" + (b == true ? 1 : 0));

                    }

                });

                cBVoice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                        //sensorLimits.get(Integer.parseInt(compoundButton.getTag().toString())).setVoice((long) (b == true ? 1 : 0));
                        //sensorLimits.get(Integer.parseInt(compoundButton.getTag().toString())).setVoice("" + (b == true ? 1 : 0));

                        sensorLimits.get(Integer.parseInt(compoundButton.getTag().toString())).setText("" + (b == true ? 1 : 0));

                    }
                });

                final SensorLimit sensorLimit = sensorLimits.get(i);
                final Delay delay = deviceConfigMetas.get(i);
                if (Integer.valueOf(sensorLimit.getEnabled()) == 0) {
                    Log.e("alarmadater", "working");
                    tVLevelSensor.setVisibility(VISIBLE);
                    lLSensor.setVisibility(GONE);
                } else {
                    Log.e("alarmadater", "working2");
                    tVLevelSensor.setVisibility(GONE);
                    lLSensor.setVisibility(VISIBLE);
                }

                cBRelay.setChecked(Integer.valueOf(sensorLimit.getRelay()) == 2);
                cBEmailAndText.setChecked(Integer.valueOf(sensorLimit.getEmail()) == 1);
                //  cBEmailAndText.setChecked(Integer.valueOf(sensorLimit.getEmail()) == 1 && Integer.valueOf(sensorLimit.getText()) == 1);

                //todo for hide on request on Sam on 19June 2019 in xml file
                //cBVoice.setChecked(Integer.valueOf(sensorLimit.getVoice()) == 1);
                cBVoice.setChecked(Integer.valueOf(sensorLimit.getText()) == 1);
                cBSiren.setChecked(Integer.valueOf(sensorLimit.getSiren()) == 1);

                //todo for hide on request on Sam on 19June 2019
//             if(sensorLimit.getSensorSlug().equalsIgnoreCase("tempc")){
//                lLTempUnit.setVisibility(VISIBLE);
//             }else{
//                lLTempUnit.setVisibility(GONE);
//             }

                tvDisplayName.setText(sensorLimit.getSensorDisplayName() + ": ");
                tVtemperature.setText(sensorLimit.getCurrentValue() + sensorLimit.getSensorDisplayUnits());
                Log.e("sxnklsnc", sensorLimit.getCurrentValue() + sensorLimit.getSensorDisplayUnits());
                tVCelciou.setTag(sensorLimit);
                tVFehrenheit.setTag(sensorLimit);

                if (sensorLimit.getSensorDisplayUnits().equalsIgnoreCase("C")) {

                    tVCelciou.setTextColor(getResources().getColor(R.color.white));
                    tVFehrenheit.setTextColor(getResources().getColor(R.color.blue));
                    tVCelciou.setBackgroundResource(R.drawable.blue_left);
                    tVFehrenheit.setBackgroundResource(R.drawable.white_with_blue_border_right);

                } else {

                    tVFehrenheit.setTextColor(getResources().getColor(R.color.white));
                    tVCelciou.setTextColor(getResources().getColor(R.color.blue));
                    tVFehrenheit.setBackgroundResource(R.drawable.blue_right);
                    tVCelciou.setBackgroundResource(R.drawable.white_with_blue_border_left);

                }

                tVCelciou.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        sensorLimit.setSensorDisplayUnits("C");

                        if (sensorLimit.getSensorDisplayUnits().equalsIgnoreCase("C")) {

                            tVCelciou.setTextColor(getResources().getColor(R.color.white));
                            tVFehrenheit.setTextColor(getResources().getColor(R.color.blue));
                            tVCelciou.setBackgroundResource(R.drawable.blue_left);
                            tVFehrenheit.setBackgroundResource(R.drawable.white_with_blue_border_right);
                            //tVtemperature.setText(Functions.convertToCelcious(sensorLimit.getCurrentValue()));
                            //sensorLimit.setCurrentValue(Functions.convertToCelcious(sensorLimit.getCurrentValue()) + "C");

                        } else {

                            tVFehrenheit.setTextColor(getResources().getColor(R.color.white));
                            tVCelciou.setTextColor(getResources().getColor(R.color.blue));
                            tVFehrenheit.setBackgroundResource(R.drawable.blue_right);
                            tVCelciou.setBackgroundResource(R.drawable.white_with_blue_border_left);
                            //tVtemperature.setText(Functions.convertToFarenhit(sensorLimit.getCurrentValue()));
                            //sensorLimit.setCurrentValue(Functions.convertToFarenhit(sensorLimit.getCurrentValue()) + "F");

                        }

                    }

                });

                tVFehrenheit.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        sensorLimit.setSensorDisplayUnits("F");

                        if (sensorLimit.getSensorDisplayUnits().equalsIgnoreCase("F")) {

                            tVFehrenheit.setTextColor(getResources().getColor(R.color.white));
                            tVCelciou.setTextColor(getResources().getColor(R.color.blue));
                            tVFehrenheit.setBackgroundResource(R.drawable.blue_right);
                            tVCelciou.setBackgroundResource(R.drawable.white_with_blue_border_left);
                            //tVtemperature.setText(Functions.convertToFarenhit(sensorLimit.getCurrentValue()));
                            //sensorLimit.setCurrentValue(Functions.convertToFarenhit(sensorLimit.getCurrentValue()) + "F");

                        } else {

                            tVCelciou.setTextColor(getResources().getColor(R.color.white));
                            tVFehrenheit.setTextColor(getResources().getColor(R.color.blue));
                            tVCelciou.setBackgroundResource(R.drawable.blue_left);
                            tVFehrenheit.setBackgroundResource(R.drawable.white_with_blue_border_right);
                            //tVtemperature.setText(Functions.convertToCelcious(sensorLimit.getCurrentValue()));
                            //sensorLimit.setCurrentValue(Functions.convertToCelcious(sensorLimit.getCurrentValue()) + "C");

                        }

                    }

                });

                tVAlarmBellow.setText("" + sensorLimit.getLcl());
                tVAlarmAbove.setText("" + sensorLimit.getUcl());

                txt_spin_operation.setText(deviceConfigModel.getDevice().getDeviceConfig().get(1).getConfigVal());

                for (int j = 0; j < deviceConfigModel.getDevice().getDeviceConfig().size(); j++) {

                    if (deviceConfigModel.getDevice().getDeviceConfig().get(j).getConfigKey().equalsIgnoreCase("temp_delay")) {

                        int value = Integer.parseInt(deviceConfigModel.getDevice().getDeviceConfig().get(j).getConfigVal());
                        int b = 60;
                        int quotient = value / b;
                        Log.e("quotient", String.valueOf(quotient));

                        txt_delay_temprature.setText("" + quotient + " " + "Minute");
                    }

                }

                for (int j = 0; j < deviceConfigModel.getDevice().getDeviceConfig().size(); j++) {

                    if (deviceConfigModel.getDevice().getDeviceConfig().get(j).getConfigKey().equalsIgnoreCase("rh_delay")) {

                        int value = Integer.parseInt(deviceConfigModel.getDevice().getDeviceConfig().get(j).getConfigVal());
                        int b = 60;
                        int quotient = value / b;
                        Log.e("quotient", String.valueOf(quotient));

                        txt_spin_HumidityAlarm.setText("" + quotient + " " + "Minute");
                    }

                }


//                String tempValue = sensorLimit.getCurrentValue() + sensorLimit.getSensorDisplayUnits();
//                String tempValue = sensorLimit.getCurrentValue();


                String tempValue = null;
                String sanatryOffsetvalue = null;
                String totalValue = null;

                for (int j = 0; j < sensorLimits.size(); j++) {
//                    Temperature
                    if (sensorLimits.get(j).getSensorDisplayName().equalsIgnoreCase("Temperature")) {

                        tempValue = sensorLimits.get(j).getRawValue();

                    }

                }


//                for (int j = 0; j < deviceConfigModel.getDevice().getDeviceConfig().size(); j++) {

                if (deviceConfigModel.getDevice().getDeviceConfig().get(0).getConfigKey().equalsIgnoreCase("sentry_offset")) {

                    sanatryOffsetvalue = deviceConfigModel.getDevice().getDeviceConfig().get(0).getConfigVal();

//                    float a = Float.parseFloat(tempValue);
//                    float b = Float.parseFloat(sanatryOffsetvalue);

//                        txt_offset_equation.setText(tempValue +" + " + deviceConfigModel.getDevice().getDeviceConfig().get(j).getConfigVal());
                }


                if (deviceConfigModel.getDevice().getDeviceConfig().get(1).getConfigKey().equalsIgnoreCase("sentry_offset_op")) {

                    if (deviceConfigModel.getDevice().getDeviceConfig().get(1).getConfigVal().equalsIgnoreCase("+")) {


                        //  Double num = (double)Integer.parseInt(tempValue.toString())+Integer.parseInt(sanatryOffsetvalue.toString());
                        Float num = Float.valueOf(tempValue) + Float.valueOf(sanatryOffsetvalue);
//                        int a = Integer.parseInt(tempValue);
//                        int b = Integer.parseInt(sanatryOffsetvalue);
//                        totalValue = String.valueOf(a + b);

                        double value = num;
                        value = Double.parseDouble(new DecimalFormat("##.####").format(value));

//                        totalValue = tempValue.toString() + sanatryOffsetvalue.toString();

                        String val = deviceConfigModel.getDevice().getDeviceConfig().get(1).getConfigVal();

//                        txt_offset_equation.setText("(" + "(" + tempValue + ")" + " "+val +" " +"(" + sanatryOffsetvalue + ")" + " = " + value + ")");
                        txt_offset_equation.setText("(" + tempValue  + " "+val +" " + sanatryOffsetvalue  + " = " + value + ")");

                        Log.e("dvfdv", String.valueOf(value));
                        edt_temprature.setText(sanatryOffsetvalue);
                        deviceConfigurationModel.setSentry_offset(String.valueOf(value));

                    } else {
                        String val = deviceConfigModel.getDevice().getDeviceConfig().get(1).getConfigVal();

                        Float num = Float.valueOf(tempValue) - Float.valueOf(sanatryOffsetvalue);

                        double value = num;
                        value = Double.parseDouble(new DecimalFormat("##.####").format(value));
//                            int totalValue11 = Integer.parseInt(tempValue) - Integer.parseInt(sanatryOffsetvalue);
//                        txt_offset_equation.setText(tempValue + val + sanatryOffsetvalue + " = " + value);
                        Log.e("dvfdv", String.valueOf(value));
//                        txt_offset_equation.setText("(" + "(" + tempValue + ")" + " "+val +" " + "(" + sanatryOffsetvalue + ")" + " = " + String.valueOf(value) + ")");
                        txt_offset_equation.setText("(" + tempValue  + " "+val +" " + sanatryOffsetvalue  + " = " + value + ")");
                        edt_temprature.setText(sanatryOffsetvalue);
                        deviceConfigurationModel.setSentry_offset(String.valueOf(value));
                    }
                }

//                }


//                }


                ArrayList<String> arrayList = new ArrayList<>();
//                arrayList.add(deviceConfigModel.getDevice().getDeviceConfig().get(1).getConfigVal());
                arrayList.add("+");
                arrayList.add("-");

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, arrayList);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner2.setAdapter(arrayAdapter);
                spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String item = parent.getItemAtPosition(position).toString();
//                        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();

                        txt_spin_operation.setText("  " + item);

//                        deviceModel.setSentry_offset_op(deviceConfigModel.getDevice().getDeviceConfigMeta().getDelayList().get(1).getValue().toString());
                        deviceModel.setSentry_offset_op(item);

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });


//                deviceConfigurationModel.setSentry_offset_op("+");

                tVAlarmBellow.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Functions.showListSelection(mContext, 0, new OnItemClickAdapter() {
                            @Override
                            public void onClick(int i, int position, String item) {
                                int pos = Integer.parseInt(tVAlarmBellow.getTag().toString());
                                //sensorLimits.get(pos).setLcl(Long.parseLong(item));
                                sensorLimits.get(pos).setLcl(item);
                                tVAlarmBellow.setText(item);
                            }
                        }, Functions.getStringList(sensorLimitMeta, sensorLimit, true));
                    }
                });

                tVAlarmAbove.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Functions.showListSelection(mContext, 0, new OnItemClickAdapter() {
                            @Override
                            public void onClick(int i, int position, String item) {
                                int pos = Integer.parseInt(view.getTag().toString());
                                //sensorLimits.get(pos).setUcl(Long.parseLong(item));
                                sensorLimits.get(pos).setUcl(item);
                                tVAlarmAbove.setText(item);
                            }
                        }, Functions.getStringList(sensorLimitMeta, sensorLimit, false));
                    }
                });

//                for (int j = 0; j < deviceConfigModel.getDevice().getDeviceConfigMeta().getDelayList().size(); j++) {
//                    if (txt_delay_temprature.getText().toString().equalsIgnoreCase(deviceConfigModel.getDevice().getDeviceConfigMeta().getDelayList().get(j).getLabel())) {
//
//                        device.setTemp_delay(deviceConfigModel.getDevice().getDeviceConfigMeta().getDelayList().get(j).getValue().toString());
//                      //  deviceConfig.add(device);
//                    }
//
//                }

                txt_delay_temprature.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Functions.showListSelection2(mContext, 0, new OnItemClickAdapter() {
                            @Override
                            public void onClick(int i, int position, String item) {

//                                int pos = Integer.parseInt(txt_delay_temprature.getTag().toString());
//                                deviceConfigMetas.get(pos).getLabel();
                                txt_delay_temprature.setText(item);
                                for (int j = 0; j < deviceConfigModel.getDevice().getDeviceConfigMeta().getDelayList().size(); j++) {
                                    if (txt_delay_temprature.getText().toString().equalsIgnoreCase(deviceConfigModel.getDevice().getDeviceConfigMeta().getDelayList().get(j).getLabel())) {
//                                        deviceConfigurationModel.setTemp_delay(deviceConfigModel.getDevice().getDeviceConfigMeta().getDelayList().get(j).getValue().toString());
                                        //  DeviceConfigurationModel  device=new DeviceConfigurationModel();
                                        deviceModel.setTemp_delay(deviceConfigModel.getDevice().getDeviceConfigMeta().getDelayList().get(j).getValue().toString());
                                        //   deviceConfig.add(device);

                                    }

                                }

                            }
                        }, deviceConfigMetas);

                    }

                });

//                ArrayList<String> list = new ArrayList<String>();

//                list.add(txt_spin_operation.getText().toString());
//                list.add("+");
//                list.add("-");


//                txt_spin_operation.setOnClickListener(new OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Functions.configValue(mContext, 0, new OnItemClickAdapter() {
//                            @Override
//                            public void onClick(int i, int position, String item) {
//
////                                deviceConfigMetas.get(pos).getLabel();
////                                txt_spin_operation.setText(list.get(position));
//                                txt_spin_operation.setText(item);
//
//                            }
//                        }, deviceConfigModel.getDevice().getDeviceConfig());
//
//                    }
//
//                });

//                for (int j = 0; j < deviceConfigModel.getDevice().getDeviceConfigMeta().getDelayList().size(); j++) {
//                    if (txt_spin_HumidityAlarm.getText().toString().equalsIgnoreCase(deviceConfigModel.getDevice().getDeviceConfigMeta().getDelayList().get(j).getLabel())) {
////                        deviceConfigurationModel.setRh_delay(deviceConfigModel.getDevice().getDeviceConfigMeta().getDelayList().get(j).getValue().toString());
//
//                        DeviceConfigurationModel  device=new DeviceConfigurationModel();
//                        device.setRh_delay(deviceConfigModel.getDevice().getDeviceConfigMeta().getDelayList().get(j).getValue().toString());
//                        deviceConfig.add(device);
//                    }
//
//                }

                txt_spin_HumidityAlarm.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Functions.showListSelection2(mContext, 0, new OnItemClickAdapter() {
                            @Override
                            public void onClick(int i, int position, String item) {

//                                int pos = Integer.parseInt(txt_spin_HumidityAlarm.getTag().toString());
//                                deviceConfigMetas.get(pos).getLabel();
                                txt_spin_HumidityAlarm.setText(item);

                                for (int j = 0; j < deviceConfigModel.getDevice().getDeviceConfigMeta().getDelayList().size(); j++) {
                                    if (txt_spin_HumidityAlarm.getText().toString().equalsIgnoreCase(deviceConfigModel.getDevice().getDeviceConfigMeta().getDelayList().get(j).getLabel())) {
//                                        deviceConfigurationModel.setRh_delay(deviceConfigModel.getDevice().getDeviceConfigMeta().getDelayList().get(j).getValue().toString());
//                                        DeviceConfigurationModel  device=new DeviceConfigurationModel();
                                        deviceModel.setRh_delay(deviceConfigModel.getDevice().getDeviceConfigMeta().getDelayList().get(j).getValue().toString());
//                                        deviceConfig.add(device);

                                    }

                                }
                            }
                        }, deviceConfigMetas);
                    }
                });

                tVAlarmInput.setTag(i);

                tVAlarmInput.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Functions.showListSelection(mContext, 0, new OnItemClickAdapter() {
                            @Override
                            public void onClick(int i, int position, String item) {

                                int pos = Integer.parseInt(view.getTag().toString());
                                if (sensorLimitMeta.getInput1().get(position).getValue().toLowerCase().equalsIgnoreCase("open")) {

                                    sensorLimits.get(pos).setLcl("" + 65535);
                                    sensorLimits.get(pos).setUcl("" + 700);

                              /*sensorLimits.get(pos).setLcl((long) 65535);
                                sensorLimits.get(pos).setUcl((long) 700);*/

                                } else {

                                    sensorLimits.get(pos).setLcl("" + 1000);
                                    sensorLimits.get(pos).setUcl("" + 65535);

                              /*sensorLimits.get(pos).setLcl((long) 1000);
                                sensorLimits.get(pos).setUcl((long) 65535);*/

                                }

                                tVAlarmInput.setText(item);

                            }

                        }, Functions.getInputsString(sensorLimitMeta));

                    }

                });

                if (sensorLimits.get(i).getSensorId().equalsIgnoreCase("7") || sensorLimits.get(i).getSensorId().equalsIgnoreCase("8")) {
                    addView(view);

                }
                if (sensorLimits.get(i).getSensorId().equalsIgnoreCase("2") && sensorLimits.get(i).getCurrentValue() != "") {

                    addView(view);
                }

            }
//&& sensorLimits.get(i).getLcl() != null&&
            button.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    deviceConfig.add(deviceConfigurationModel);
                    callUpdateDeviceApi(mContext, id, sensorLimits, deviceConfig, pBview);

                }

            });
        }

    }


    public void callUpdateDeviceApi(final Context mContext, String id, ArrayList<SensorLimit> sensorLimits, ArrayList<DeviceConfigurationModel> deviceConfig, View view) {

        BaseRequest baseRequest = new BaseRequest(mContext);
        baseRequest.setRunInBackground(false);
        baseRequest.setLoaderView(view);
        //baseRequest.setContainer(custom_pb, null);

        baseRequest.setBaseRequestListner(new RequestReceiver() {
            @Override
            public void onSuccess(int requestCode, String fullResponse, Object dataObject) {

                Functions.Alert(((Activity) mContext), "AlarmConfig updated", Functions.AlertType.Success, new OnHideAlertListener() {
                    @Override
                    public void onHide() {
                        ((Activity) mContext).finish();
                    }
                });

            }

            @Override
            public void onFailure(int requestCode, String errorCode, String message) {

                Functions.Alert(((Activity) mContext), message, Functions.AlertType.Error);
            }

            @Override
            public void onNetworkFailure(int requestCode, String message) {

            }

        });

        JsonObject object = null;
        object = Functions.getInstance().getJsonObject("id", id);

        JsonArray jsonArray2 = new JsonArray();
        JsonArray jsonArray3 = new JsonArray();

        Log.e("Sensor_limit", sensorLimits.get(3).toString());
        for (SensorLimit sensorLimit : sensorLimits) {

            JsonObject jsonObject3 = new JsonObject();

            jsonObject3.addProperty("id", sensorLimit.getId());
            jsonObject3.addProperty("sensorId", sensorLimit.getSensorId());
            jsonObject3.addProperty("sensorSlug", sensorLimit.getSensorSlug());
            jsonObject3.addProperty("sensorDisplayName", sensorLimit.getSensorDisplayName());
            jsonObject3.addProperty("sensorDisplayUnits", sensorLimit.getSensorDisplayUnits());
            jsonObject3.addProperty("lcl", sensorLimit.getLcl());
            jsonObject3.addProperty("ucl", sensorLimit.getUcl());
            jsonObject3.addProperty("enabled", sensorLimit.getEnabled());
            jsonObject3.addProperty("relay", sensorLimit.getRelay());
            jsonObject3.addProperty("siren", sensorLimit.getSiren());
            jsonObject3.addProperty("email", sensorLimit.getEmail());
            jsonObject3.addProperty("text", sensorLimit.getText());
            jsonObject3.addProperty("voice", sensorLimit.getVoice());
            jsonObject3.addProperty("isAlarm", sensorLimit.getIsAlarm());
            jsonObject3.addProperty("currentValue", sensorLimit.getCurrentValue());
            jsonArray2.add(jsonObject3);

        }


      /*  for (SensorLimit sensorLimit : sensorLimits) {

            JsonObject jsonObject4 = new JsonObject();

//            jsonObject4.addProperty("id", sensorLimit.getId());
//            jsonObject4.addProperty("sensorId", sensorLimit.getSensorId());
//            jsonObject4.addProperty("sensorSlug", sensorLimit.getSensorSlug());
//            jsonObject4.addProperty("sensorDisplayName", sensorLimit.getSensorDisplayName());
//            jsonObject4.addProperty("sensorDisplayUnits", sensorLimit.getSensorDisplayUnits());

            if (sensorLimit.getSensorId().equalsIgnoreCase("3")) {
                jsonObject4.addProperty("lcl", sensorLimit.getLcl());
                jsonObject4.addProperty("ucl", sensorLimit.getUcl());
//            jsonObject4.addProperty("enabled", sensorLimit.getEnabled());
//            jsonObject4.addProperty("relay", sensorLimit.getRelay());
//            jsonObject4.addProperty("siren", sensorLimit.getSiren());
//            jsonObject4.addProperty("email", sensorLimit.getEmail());
//            jsonObject4.addProperty("text", sensorLimit.getText());
//            jsonObject4.addProperty("voice", sensorLimit.getVoice());
//            jsonObject4.addProperty("isAlarm", sensorLimit.getIsAlarm());
//            jsonObject4.addProperty("currentValue", sensorLimit.getCurrentValue());
            }
            jsonArray2.add(jsonObject4);

        }
*/
        //  for (DeviceConfigurationModel deviceConfigurationModel : deviceConfig) {
        // for (int idx=0;idx<deviceConfig.size();idx++) {
        JsonObject deviceConfigObj1 = new JsonObject();
        JsonObject deviceConfigObj2 = new JsonObject();
        JsonObject deviceConfigObj3 = new JsonObject();
        JsonObject deviceConfigObj4 = new JsonObject();
        //  if(idx==0) {
        deviceConfigObj1.addProperty("configKey", "sentry_offset");
        deviceConfigObj1.addProperty("configVal", deviceModel.getSentry_offset());
        //  }
        // if(idx==1) {
        deviceConfigObj2.addProperty("configKey", "sentry_offset_op");
        deviceConfigObj2.addProperty("configVal", deviceModel.getSentry_offset_op());
        //  }
        //  if(idx==4) {
        deviceConfigObj3.addProperty("configKey", "temp_delay");
        deviceConfigObj3.addProperty("configVal", deviceModel.getTemp_delay());
        //  }
        //  if(idx==6) {
        deviceConfigObj4.addProperty("configKey", "rh_delay");
        deviceConfigObj4.addProperty("configVal", deviceModel.getRh_delay());
        //  }
        jsonArray3.add(deviceConfigObj1);
        jsonArray3.add(deviceConfigObj2);
        jsonArray3.add(deviceConfigObj3);
        jsonArray3.add(deviceConfigObj4);
        // }

        Log.e("arrayobj", deviceConfig.toArray().toString());

        object.add("sensorLimit", jsonArray2);
        object.add("deviceConfig", jsonArray3);
        baseRequest.callAPIPost(1, object, getResources().getString(R.string.api_edit_device));

    }

  /*  public class MainActivity extends Activity {
        ArrayList<String> StringArray = new ArrayList<String>();

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            ArrayValueAddFunction();
            LinearLayout LinearLayoutView = new LinearLayout(this);
            TextView DisplayStringArray = new TextView(this);
            DisplayStringArray.setTextSize(25);
            LinearLayoutView.addView(DisplayStringArray);
            for (int i = 0; i < StringArray.size(); i++) {
                DisplayStringArray.append(StringArray.get(i));
                DisplayStringArray.append("\n");
            }
            setContentView(LinearLayoutView);
        }

        private void ArrayValueAddFunction() {
            StringArray.add("ONE");
            StringArray.add("TWO");
            StringArray.add("THREE");
            StringArray.add("FOUR");
            StringArray.add("FIVE");
            StringArray.add("SIX");
        }
    }*/

}
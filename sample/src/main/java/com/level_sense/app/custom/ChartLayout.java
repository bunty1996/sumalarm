package com.level_sense.app.custom;

import android.app.AlertDialog;

import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.DialogInterface;

import androidx.annotation.Nullable;

import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;
import com.level_sense.app.R;
import com.level_sense.app.graph.ChartActivity;
import com.level_sense.app.model.DeviceDataModel;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;


public class ChartLayout extends LinearLayout {
    Context context;

    public ChartLayout(Context context) {
        super(context);
        this.context = context;
    }

    public ChartLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public ChartLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    public void AddViews(ArrayList<DeviceDataModel> sensorLimits, ChartActivity.Shedule shedule, JSONObject metaData, String devicetype) {
        removeAllViews();

        Log.e("schedule", shedule.toString() + "//");
        Log.e("meta", metaData.toString() + "//");

        for (int i = 0; i < sensorLimits.size(); i++) {

                if ((!sensorLimits.get(i).getSensorId().equalsIgnoreCase("9") && !sensorLimits.get(i).getSensorId().equalsIgnoreCase("10")) && !sensorLimits.get(i).getSensorId().equalsIgnoreCase("7") && !sensorLimits.get(i).getSensorId().equalsIgnoreCase("8") /*&& sensorLimits.get(i).getData().size() > 0*/) {

                    final View view = LayoutInflater.from(getContext()).inflate(R.layout.content_chat, null);
                    TextView tVDisplayName = (TextView) view.findViewById(R.id.tVDisplayName);
                    TextView noGraphdata = (TextView) view.findViewById(R.id.noGraphdata);
                    ImageView graph_icon = view.findViewById(R.id.graph_icon);
                    LinearLayout graphlayout = view.findViewById(R.id.graphlayout);
                    new DeviceDataModel();
                    Log.e("graphname", sensorLimits.get(i).getSensorDisplayName());
                    if (sensorLimits.get(i).getSensorId().equalsIgnoreCase("11"))
                        tVDisplayName.setText("Wi-Fi Signal Strength in dBm");
                    else if (sensorLimits.get(i).getSensorId().equalsIgnoreCase("3")) {
                        tVDisplayName.setText("Humidity");
                    }
                    else if (sensorLimits.get(i).getSensorId().equalsIgnoreCase("5")) {
                        tVDisplayName.setText("Incoming Power Status");
                    }
                    else if (sensorLimits.get(i).getSensorId().equalsIgnoreCase("1")) {
                        if (metaData.has("pumpCycle") && Integer.valueOf(metaData.optString("pumpCycle")) == 100) {
                            tVDisplayName.setText(sensorLimits.get(i).getSensorDisplayName() + "( Calibrated )" );
                        } else if (metaData.has("pumpCycle")) {
                            tVDisplayName.setText(sensorLimits.get(i).getSensorDisplayName() + "( Calibrating..." + metaData.optString("pumpCycle") + "%)");
                        } else
                            tVDisplayName.setText(sensorLimits.get(i).getSensorDisplayName() + "( Calibrated )");
                    } else {
                        tVDisplayName.setText(sensorLimits.get(i).getSensorDisplayName());
                    }

                    //todo for testing for understading the code
                    ArrayList<DataPoint> dataPointArrayList = new ArrayList<>();

                    Log.e("sensorname", sensorLimits.get(i).getSensorDisplayName() + "//" + sensorLimits.get(i).getData().size());



                    for (int j = 0; j < sensorLimits.get(i).getData().size(); j++) {
                        DataPoint point ;
                        if (!sensorLimits.get(i).getData().get(j).getValue().equalsIgnoreCase("null")) {

                                point = new DataPoint(sensorLimits.get(i).getData().get(j).getTimeStamp(), Double.parseDouble(sensorLimits.get(i).getData().get(j).getValue()));

                            dataPointArrayList.add(point);
                        }
                    }


                    GraphView graph = (GraphView) view.findViewById(R.id.graph);

                    DataPoint[] dataPoint = new DataPoint[dataPointArrayList.size()];

                    for (int j = 0; j < dataPointArrayList.size(); j++) {

                        if (sensorLimits.get(i).getSensorId().equalsIgnoreCase("1")) {

                            dataPoint[j] = dataPointArrayList.get(j);
                            graph.getViewport().setMinY(0);
                            graph.getViewport().setMaxY(sensorLimits.get(i).getMax());
                            graph.getViewport().setYAxisBoundsManual(true);
                        }
                        else if (sensorLimits.get(i).getSensorId().equalsIgnoreCase("6")) {

                            graph.getViewport().setYAxisBoundsManual(true);
                            dataPoint[j] = dataPointArrayList.get(j);
                            graph.getViewport().setMinY(0);
                            graph.getViewport().setMaxY(1);

                        }
                        else if (sensorLimits.get(i).getSensorId().equalsIgnoreCase("2")) {
                            graph.getViewport().setYAxisBoundsManual(true);
                            dataPoint[j] = dataPointArrayList.get(j);
                            graph.getViewport().setMaxY(Double.valueOf(sensorLimits.get(i).getMax()));
                            graph.getViewport().setMinY(Double.valueOf(sensorLimits.get(i).getMin()));


                        }
                        else if (sensorLimits.get(i).getSensorId().equalsIgnoreCase("3")) {
                            graph.getViewport().setYAxisBoundsManual(true);
                            dataPoint[j] = dataPointArrayList.get(j);
                           graph.getViewport().setMaxY(100);
                            graph.getViewport().setMinY(30);


                        }
                        else if (sensorLimits.get(i).getSensorId().equalsIgnoreCase("11")) {
                            graph.getViewport().setYAxisBoundsManual(true);
                            dataPoint[j] = dataPointArrayList.get(j);
                            graph.getViewport().setMaxY(Double.valueOf(sensorLimits.get(i).getMax()));
                            graph.getViewport().setMinY(Double.valueOf(sensorLimits.get(i).getMin()));

                        }
                        else if (sensorLimits.get(i).getSensorId().equalsIgnoreCase("4")) {
                            dataPoint[j] = dataPointArrayList.get(j);
                            graph.getViewport().setYAxisBoundsManual(true);
                            graph.getViewport().setMinY(0);
                            graph.getViewport().setMaxY(100);

                        }
                        else {
                            dataPoint[j] = dataPointArrayList.get(j);
                        }
                        if (j == 1) {
                          graph.getViewport().setMinX(dataPointArrayList.get(1).getX());
                        }
                    }
                    noGraphdata.setVisibility(VISIBLE);
                    graph_icon.setVisibility(VISIBLE);
                    graph_icon.setAlpha(0.1f);
                    Log.e("sesnr", sensorLimits.get(i).getData().size() + "//" + sensorLimits.get(i).getSensorId());
                    if (sensorLimits.get(i).getData().size() == 0) {
                        Log.e("sesnr123", sensorLimits.get(i).getData().size() + "//" + sensorLimits.get(i).getSensorId());
                        noGraphdata.setText("No " + sensorLimits.get(i).getSensorDisplayName() + " data available ");
                        graph.setVisibility(GONE);
                        view.setVisibility(GONE);
                    } else {
                        graph.setVisibility(VISIBLE);
                    }
                    if (dataPointArrayList.size() > 0)
                        graph.getViewport().setMaxX(dataPointArrayList.get(dataPointArrayList.size() - 1).getX());
//                    graph.getGridLabelRenderer().setPadding(40);//40
//                    //todo for padding
//                    graph.getGridLabelRenderer().setLabelsSpace(10);//10
//                    graph.getGridLabelRenderer().setHorizontalLabelsAngle(90);//90
//                    graph.getGridLabelRenderer().setLabelVerticalWidth(100);//100
                    graph.getViewport().setXAxisBoundsManual(true);

                    LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(dataPoint);
                    series.setAnimated(true);

                    if (sensorLimits.get(i).getSensorId().equalsIgnoreCase("2") || sensorLimits.get(i).getSensorId().equalsIgnoreCase("1"))
                        series.setDrawBackground(true);

                    if (sensorLimits.get(i).getSensorId().equalsIgnoreCase("2")) {

                        series.setColor(getResources().getColor(R.color.blue));

                    } else if (sensorLimits.get(i).getSensorId().equalsIgnoreCase("11")) {

                        series.setColor(getResources().getColor(R.color.blue));

                    } else if (sensorLimits.get(i).getSensorId().equalsIgnoreCase("4")) {

                        series.setColor(getResources().getColor(R.color.skyblue));

                    } else {

                        series.setColor(getResources().getColor(R.color.blue));

                    }
                    //graph.setGraphType(shedule == ChartActivity.Shedule.Today ? GraphView.GraphType.Today : GraphView.GraphType.Week);
                    graph.setGraphType(shedule == ChartActivity.Shedule.Week ? GraphView.GraphType.Week : GraphView.GraphType.Today);

                    Log.e("myiddd ", sensorLimits.get(i)+"//"+sensorLimits.get(i).getSensorId());
                    graph.setSensorId(sensorLimits.get(i).getSensorId());
                    graph.invalidate();
                    series.setDrawAsPath(false);
                    series.setDrawDataPoints(true);
                    series.setDataPointsRadius(5f);
                    series.setAnimated(true);

                    series.setOnDataPointTapListener(new OnDataPointTapListener() {
                        @Override
                        public void onTap(Series series, DataPointInterface dataPoint) {

                            String message = "Point Value = " + dataPoint.getY() + "\n\n" + "Time = " + label("" + dataPoint.getX(), shedule);
                            showDialog(getContext(), message);

                        }

                    });

                    graph.addSeries(series);
                    if (devicetype.equalsIgnoreCase("LS_PRO")) {
                    if(sensorLimits.get(i).getSensorId().equalsIgnoreCase("1")) {
                        addView(view);
                    }
                    else if(sensorLimits.get(i).getSensorId().equalsIgnoreCase("6")){
                        addView(view,1);
                    }
                    else if(sensorLimits.get(i).getSensorId().equalsIgnoreCase("2")){
                        addView(view);
                    }
                    else if(sensorLimits.get(i).getSensorId().equalsIgnoreCase("3")){
                        addView(view);
                    }
                    else if(sensorLimits.get(i).getSensorId().equalsIgnoreCase("11")){
                        addView(view,4);
                    }
                    else if(sensorLimits.get(i).getSensorId().equalsIgnoreCase("4")){
                        addView(view);
                    }}
                    else{
                        if(sensorLimits.get(i).getSensorId().equalsIgnoreCase("11")){
                            addView(view);
                        }
                           else if(sensorLimits.get(i).getSensorId().equalsIgnoreCase("6")){
                                addView(view);
                           }
                        else if(sensorLimits.get(i).getSensorId().equalsIgnoreCase("2")){
                            addView(view);
                        }
                    }

                }
            }


    }



    public static void showDialog(Context mContext, String message) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext, R.style.ThemeDialogCustom);
        dialogBuilder.setTitle("Graph Points");
        dialogBuilder.setMessage(message);
        dialogBuilder.setPositiveButton(R.string.ok,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    protected String label(String data, ChartActivity.Shedule graphType) {
        Log.e("datetimestamps ", data.toString());
        String split[] = data.split(",");
        StringBuilder stringBuilder = new StringBuilder();
        for (String s : split) {
            stringBuilder.append(s);
        }
        Date date = new Date((long) (Double.valueOf(stringBuilder.toString()) * 1000));
        String newString;
        if (graphType == ChartActivity.Shedule.Week)
            newString = new SimpleDateFormat("dd MMM yy").format(date); // 9:00
        else
            newString = new SimpleDateFormat("hh:mm a").format(date); // 9:00

        Log.e("date", newString + "//");
        return newString;
    }

}
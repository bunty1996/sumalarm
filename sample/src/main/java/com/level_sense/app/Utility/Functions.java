package com.level_sense.app.Utility;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.gson.JsonObject;

import com.jjoe64.graphview.series.DataPoint;
import com.level_sense.app.model.Delay;
import com.level_sense.app.model.DeviceConfigMeta;
import com.level_sense.app.model.DeviceConfigModel;
import com.level_sense.app.model.Input1;
import com.level_sense.app.model.Max;
import com.level_sense.app.model.Min;
import com.level_sense.app.model.SensorLimit;
import com.shopify.buy3.GraphClient;
import com.level_sense.app.R;
import com.level_sense.app.model.SensorLimitMeta;
import com.tapadoo.alerter.Alerter;
import com.tapadoo.alerter.OnHideAlertListener;


import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;


public class Functions extends Application {

    public static Functions instance = null;
    private static String ActiveLeagueID;
    private boolean isCoinSystemEnabled = false;

    public static Functions getInstance() {

        if (instance == null) {
            instance = new Functions();
        }
        return instance;
    }

    public static String formatDecimal(double d) {
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH);
        DecimalFormat formatter = (DecimalFormat) nf;
        return formatter.format(d);
    }

  /*  public static void configSanetoryOffsetOP(Context context, int titleResId, final OnItemClickAdapter OnItemClickAdapter, ArrayList<String> deviceConfigMetas) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.ThemeDialogCustom);
        if (0 != titleResId) {
            builder.setTitle(titleResId);
        }
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, R.layout.list_item);
        List<String> list = new ArrayList<>();
        list.clear();

//        if (deviceConfigMetas.get(1).getConfigKey().equalsIgnoreCase("sentry_offset_op")) {
////            for (int i = 0; i < deviceConfigMetas.size(); i++) {
//            list.add(deviceConfigMetas.get(1).getConfigVal());
////            }
//        }
        arrayAdapter.addAll(list
        );

        builder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int position) {
                String item = arrayAdapter.getItem(position);
                if (null != OnItemClickAdapter) {
                    OnItemClickAdapter.onClick(0, position, item);
                }
            }
        });

        AlertDialog dialog = builder.create();
        //dialog.getWindow().setBackgroundDrawableResource(R.drawable.transparent);
        dialog.show();

    }*/

    public static void configValue(Context context, int titleResId, final OnItemClickAdapter OnItemClickAdapter, ArrayList<DeviceConfigModel> deviceConfigMetas) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.ThemeDialogCustom);
        if (0 != titleResId) {
            builder.setTitle(titleResId);
        }
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, R.layout.list_item);
        List<String> list = new ArrayList<>();
        list.clear();

        if (deviceConfigMetas.get(1).getConfigKey().equalsIgnoreCase("sentry_offset_op")) {
//            for (int i = 0; i < deviceConfigMetas.size(); i++) {
                list.add(deviceConfigMetas.get(1).getConfigVal());
//            }
        }
        arrayAdapter.addAll(list
        );

        builder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int position) {
                String item = arrayAdapter.getItem(position);
                if (null != OnItemClickAdapter) {
                    OnItemClickAdapter.onClick(0, position, item);
                }
            }
        });

        AlertDialog dialog = builder.create();
        //dialog.getWindow().setBackgroundDrawableResource(R.drawable.transparent);
        dialog.show();

    }

    public static void showListSelection2(Context context, int titleResId, final OnItemClickAdapter OnItemClickAdapter, List<Delay> deviceConfigMetas) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.ThemeDialogCustom);
        if (0 != titleResId) {
            builder.setTitle(titleResId);
        }
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, R.layout.list_item);
        List<String> list = new ArrayList<>();
        list.clear();
        for (int i = 0; i < deviceConfigMetas.size(); i++) {
            list.add(deviceConfigMetas.get(i).getLabel());
        }
        arrayAdapter.addAll(list
        );

        builder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int position) {
                String item = arrayAdapter.getItem(position);
                if (null != OnItemClickAdapter) {
                    OnItemClickAdapter.onClick(0, position, item);
                }
            }
        });

        AlertDialog dialog = builder.create();
        //dialog.getWindow().setBackgroundDrawableResource(R.drawable.transparent);
        dialog.show();

    }

    public boolean isCoinSystemEnabled() {
        return isCoinSystemEnabled;
    }

    public void setCoinSystemEnabled(boolean coinSystemEnabled) {
        isCoinSystemEnabled = coinSystemEnabled;
    }

    public String getActiveLeagueID() {
        return this.ActiveLeagueID;
    }

    public void setActiveLeagueID(String LeagueID) {
        this.ActiveLeagueID = LeagueID;
    }

    public String getImageNameFromFormation(String formation) {
        int DF = Integer.parseInt(Character.toString(formation.charAt(0)));
        int MF = Integer.parseInt(Character.toString(formation.charAt(2)));
        int FW = Integer.parseInt(Character.toString(formation.charAt(4)));

        String image_name = "ic_" + DF + MF + FW;
        return image_name;
    }

    public GraphClient graphClient;

    @Override
    public void onCreate() {
        super.onCreate();

    }

    public JsonObject getJsonObject(String... nameValuePair) {
        JsonObject HashMap = null;
        if (null != nameValuePair && nameValuePair.length % 2 == 0) {
            HashMap = new JsonObject();
            int i = 0;
            while (i < nameValuePair.length) {
                HashMap.addProperty(nameValuePair[i], nameValuePair[i + 1]);
                i += 2;
            }
        }
        return HashMap;
    }


    public String toTitleCase(String str) {
        if (str == null) {
            return "";
        }
        boolean space = true;
        StringBuilder builder = new StringBuilder(str);
        final int len = builder.length();
        for (int i = 0; i < len; ++i) {
            char c = builder.charAt(i);
            if (space) {
                if (!Character.isWhitespace(c)) {
                    builder.setCharAt(i, Character.toTitleCase(c));
                    space = false;
                }
            } else if (Character.isWhitespace(c)) {
                space = true;
            } else {
                builder.setCharAt(i, Character.toLowerCase(c));
            }
        }
        return builder.toString();
    }

    public static void Alert(Activity activity, String message, AlertType alertType, OnHideAlertListener onHideAlertListener) {
        if (alertType == AlertType.Error) {
            Alerter.create(activity).setText(message)
                    .setIcon(R.drawable.ic_error_outline_white_24dp)
                    .setBackgroundColor(R.color.red_500).setOnHideListener(onHideAlertListener).show();

        } else if (alertType == AlertType.Success)
            Alerter.create(activity).setText(message).setBackgroundColor(R.color.blue).setIcon(R.drawable.ic_done_white_24dp).setOnHideListener(onHideAlertListener).show();
    }

    public static void Alert(Activity activity, String message, AlertType alertType) {
        if (alertType == AlertType.Error) {
            Alerter.create(activity).setText(message)
                    .setIcon(R.drawable.ic_error_outline_white_24dp)
                    .setBackgroundColor(R.color.red_500).show();

        } else if (alertType == AlertType.Success)
            Alerter.create(activity).setText(message).setBackgroundColor(R.color.blue).setIcon(R.drawable.ic_done_white_24dp).show();
    }

    public enum AlertType {
        Error,
        Success
    }

    public String getAppString(int id) {
        String str = "";
        if (!TextUtils.isEmpty(this.getResources().getString(id))) {
            str = this.getResources().getString(id);
        } else {
            str = "";
        }
        return str;
    }

    public static void showDefaultTwoButonYesNo(Context mContext, String message, int title, final
    OnDialogButtonClickListener
            onItemClickCommen) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext, R.style.ThemeDialogCustom);
        if (0 != title) {
            dialogBuilder.setTitle(title);
        }
        if (!message.equals("")) {
            dialogBuilder.setMessage(message);
        } else {
            dialogBuilder.setMessage("Urnknown Error");
        }
        dialogBuilder.setCancelable(false);
        dialogBuilder.setPositiveButton(R.string.yes,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onItemClickCommen.onClick(1);
                        dialog.cancel();
                    }
                });
        dialogBuilder.setNegativeButton(R.string.no,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onItemClickCommen.onClick(0);
                        dialog.cancel();
                    }
                });
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

    }

    public static void showDefaultTwoButonYesNoDelete(Context mContext, String message, int title, final
    OnDialogButtonClickListener
            onItemClickCommen) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext, R.style.ThemeDialogCustom);
        if (0 != title) {
            dialogBuilder.setTitle(title);
        }
        if (!message.equals("")) {
            dialogBuilder.setMessage(message);
        } else {
            dialogBuilder.setMessage("Urnknown Error");
        }
        dialogBuilder.setCancelable(false);
        dialogBuilder.setPositiveButton(R.string.yes,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onItemClickCommen.onClick(1);
                        dialog.cancel();
                    }
                });
        dialogBuilder.setNegativeButton(R.string.no,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onItemClickCommen.onClick(0);
                        dialog.cancel();
                    }
                });
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

    }

    public interface OnDialogButtonClickListener {
        void onClick(int type);
    }

    public static void showListSelection(Context context, int titleResId, final OnItemClickAdapter OnItemClickAdapter, final List<String> items) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.ThemeDialogCustom);

        if (0 != titleResId) {
            builder.setTitle(titleResId);
        }

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, R.layout.list_item);
        arrayAdapter.addAll(items);
        builder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int position) {
                String item = arrayAdapter.getItem(position);
                if (null != OnItemClickAdapter) {
                    OnItemClickAdapter.onClick(0, position, item);
                }
            }
        });

        AlertDialog dialog = builder.create();
        //dialog.getWindow().setBackgroundDrawableResource(R.drawable.transparent);
        dialog.show();

    }

    public static String convertToFarenhit(String Celsius) {
        double v = Double.parseDouble(Celsius);
        double v1 = (9.0 / 5.0) * v + 32;

        return "" + v1;

    }

    public static String convertToCelcious(String Celsius) {
        double v = Double.parseDouble(Celsius);
        double celsius = (5.0 / 9.0) * (v - 32);
        return "" + celsius;

    }

    public static void showColorListSelection(final Context context, int titleResId, final OnItemClickAdapter OnItemClickAdapter) {
        final int[] colorCodes = new int[]{R.color.red_, R.color.snow_white, R.color.yellow_, R.color.green_, R.color.blue_, R.color.purple};
        final int[] colorNames = new int[]{R.string.red, R.string.white, R.string.yellow, R.string.green, R.string.blue, R.string.purple};
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.ThemeColorSelectionDialogCustom);

        if (0 != titleResId) {
            TextView textView = new TextView(context);
            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(context.getResources().getColor(R.color.blue));
            textView.setTextSize(20);
            textView.setPadding(5, 5, 5, 5);
            textView.setText(titleResId);
            builder.setCustomTitle(textView);
//            builder.setTitle(titleResId);
        }

        final BaseAdapter arrayAdapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return colorNames.length;
            }

            @Override
            public String getItem(int i) {
                return context.getString(colorNames[i]);
            }

            @Override
            public long getItemId(int i) {
                return i;

            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                view = LayoutInflater.from(context).inflate(R.layout.list_item, null);
                view.setBackgroundColor(context.getResources().getColor(R.color.gray_color_background));
                TextView textView = (TextView) view.findViewById(android.R.id.text1);
                textView.setText(colorNames[i]);
                textView.setAllCaps(true);
                textView.setGravity(Gravity.CENTER);
                textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
                textView.setTextColor(context.getResources().getColor(colorCodes[i]));
                return view;
            }
        };
        builder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int position) {
                String item = context.getString(colorNames[position]);
                if (null != OnItemClickAdapter) {
                    OnItemClickAdapter.onClick(0, position, item);

                }

            }
        });
        AlertDialog dialog = builder.create();
        // dialog.getWindow().setBackgroundDrawableResource(R.drawable.transparent);
        dialog.show();
    }

    public static boolean isValidEmaillId(String email) {

        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }

    public static String formateDate(String dateInString, String... format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format[0], Locale.ENGLISH);
        String formateDate = "";
        try {
            Date date = formatter.parse(dateInString);
            SimpleDateFormat formatter2 = new SimpleDateFormat(format[1], Locale.ENGLISH);
            formateDate = formatter2.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formateDate;
    }

    public static int getIntervalInSecond(Context mContext, String time) {
        String[] strings = time.split(" ");
        int i = Integer.parseInt(strings[0]);
        int duration = 0;
        if (strings[1].equalsIgnoreCase("Min")) {
            duration = i * 60;
        } else if (strings[1].equalsIgnoreCase("Hour")) {
            duration = i * 60 * 60;
        } else if (strings[1].equalsIgnoreCase("Day")) {
            duration = i * 60 * 60 * 60;
        }
        return duration;
    }

    public static String getIntervalInMINHD(int time) {
        int min = time / 60;
        int hour = min / 60;
        int day = hour / 60;
        String str = "";
        if (day > 0) {
            str = day + " Day";
        } else if (hour > 0) {
            str = hour + " Hour";
        } else if (min > 0) {
            str = min + " Min";
        }
        return str;
    }

    public static ArrayList<String> getStringList(SensorLimitMeta sensorLimitMeta, SensorLimit sensorLimit, boolean isMin) {
        ArrayList<String> list = new ArrayList<>();
        ArrayList<Min> mins = new ArrayList<>();
        ArrayList<Max> maxS = new ArrayList<>();

        if (sensorLimit.getSensorId().equalsIgnoreCase("2")) {
            mins = sensorLimitMeta.getTempc().getMin();
            maxS = sensorLimitMeta.getTempc().getMax();

        } else if (sensorLimit.getSensorId().equalsIgnoreCase("3")) {
            mins = sensorLimitMeta.getRh().getMin();
            maxS = sensorLimitMeta.getRh().getMax();

        }
        if (isMin) {
            for (Min min : mins) {
                list.add("" + min.getValue());
            }
        } else {
            for (Max max : maxS) {
                list.add("" + max.getValue());
            }
        }
        return list;
    }

    /*public static ArrayList<String> getStringList2(List<Delay> deviceConfigMetas, Delay sensorLimit, boolean isMin) {
        ArrayList<String> list = new ArrayList<>();
        ArrayList<Min> mins = new ArrayList<>();
        ArrayList<Max> maxS = new ArrayList<>();

        if (sensorLimit.getSensorId().equalsIgnoreCase("2")) {
            mins = deviceConfigMetas.getTempc().getMin();
            maxS = deviceConfigMetas.getTempc().getMax();

        } else if (sensorLimit.getSensorId().equalsIgnoreCase("3")) {
            mins = deviceConfigMetas.getRh().getMin();
            maxS = deviceConfigMetas.getRh().getMax();

        }
        if (isMin) {
            for (Min min : mins) {
                list.add("" + min.getValue());
            }
        } else {
            for (Max max : maxS) {
                list.add("" + max.getValue());
            }
        }
        return list;
    }*/

    public static ArrayList<String> getInputsString(SensorLimitMeta sensorLimitMeta) {
        ArrayList<String> list = new ArrayList<>();

        for (Input1 input : sensorLimitMeta.getInput1()) {
            list.add(input.getLabel());
        }
        return list;
    }

    public static ArrayList<String> getShrink() {
        ArrayList<String> list = new ArrayList<>();
        list.add("10");
        list.add("100");
        list.add("200");

        return list;
    }

    public static ArrayList<String> getInterval(Context mCtxt, int intervalType) {
        ArrayList<String> interval = new ArrayList<>();
        if (intervalType == 0) {
            interval.add(mCtxt.getString(R.string.min_2));
            interval.add(mCtxt.getString(R.string.min_3));
            interval.add(mCtxt.getString(R.string.min_4));
            interval.add(mCtxt.getString(R.string.min_5));
        } else {
            interval.add(mCtxt.getString(R.string.min_5));
            interval.add(mCtxt.getString(R.string.min_10));
            interval.add(mCtxt.getString(R.string.min_20));

            interval.add(mCtxt.getString(R.string.min_30));
            interval.add(mCtxt.getString(R.string.hr_1));
            interval.add(mCtxt.getString(R.string.hr_2));
            interval.add(mCtxt.getString(R.string.hr_4));
            interval.add(mCtxt.getString(R.string.hr_8));
            interval.add(mCtxt.getString(R.string.d_1));
            interval.add(mCtxt.getString(R.string.d_2));

        }
        return interval;
    }


    public static void noData(TextView textView, ArrayList arrayList) {
        if (arrayList.size() > 0) {
            textView.setVisibility(View.GONE);
        } else {
            textView.setVisibility(View.VISIBLE);
        }
    }

    public static void iteraterList(ArrayList<DataPoint> arrayList) {
        for (DataPoint temp : arrayList) {

        }

    }

}
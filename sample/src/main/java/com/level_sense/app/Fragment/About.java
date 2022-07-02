package com.level_sense.app.Fragment;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.TrafficStats;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.level_sense.app.R;
import com.level_sense.app.view.ScreenRouter;
import com.level_sense.app.view.cart.CartClickActionEvent;

import static com.facebook.common.util.ByteConstants.MB;

public class About extends Fragment {

    private TextView aboutText;
    private TextView RX;
    private TextView TX;
    private Handler mHandler = new Handler();
    private long mStartRX = 0;
    private long mStartTX = 0;
    private static final long  MB = 1024L * 1024L;
    public About() {

    }

    //TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static About newInstance() {
        About fragment = new About();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.about, container, false);
        aboutText = (TextView) v.findViewById(R.id.aboutText);
         RX = (TextView)v.findViewById(R.id.RX);
         TX = (TextView)v.findViewById(R.id.TX);
        PackageManager manager = getActivity().getPackageManager();
        PackageInfo info = null;

        try {

            info = manager.getPackageInfo(getActivity().getPackageName(), 0);

        } catch (PackageManager.NameNotFoundException e) {

            e.printStackTrace();

        }
//        mStartRX = TrafficStats.getTotalRxBytes();
//        mStartTX = TrafficStats.getTotalTxBytes();
//        if (mStartRX == TrafficStats.UNSUPPORTED || mStartTX == TrafficStats.UNSUPPORTED) {
//            Toast.makeText(getActivity(),"your device doesn't support TrafficStatus",Toast.LENGTH_SHORT).show();
//        } else {
//            mHandler.postDelayed(mRunnable, 1000);
//        }
//        int uid = android.os.Process.myUid();
//      Log.e("txBytesInitial",TrafficStats.getUidTxBytes(uid)+"MB");
//      Log.e("rxBytesInitial",TrafficStats.getUidRxBytes(uid)+"MB");
//        TX.setText("Transmit - "+KBToMB(TrafficStats.getUidTxBytes(uid))+"MB");
//        RX.setText("Recieve - "+KBToMB(TrafficStats.getUidRxBytes(uid))+"MB");
        String version = info.versionName;
        String aboutStr = "App platform : Android \n App Version : " + version + "\n\n Copyright © 2019 Sump Alarm Inc. All Rights Reserved.";
        aboutText.setText(aboutStr);
        return v;
    }
    public static long KBToMB(long KB)
    {
        return KB / MB;
    }
//    private final Runnable mRunnable = new Runnable() {
//        public void run() {
//
//            long rxBytes = TrafficStats.getTotalRxBytes()- mStartRX;
//            RX.setText("Transmit Bytes-"+Long.toString(rxBytes));
//            long txBytes = TrafficStats.getTotalTxBytes()- mStartTX;
//            TX.setText("Recieve Bytes-"+Long.toString(txBytes));
//            mHandler.postDelayed(mRunnable, 1000);
//        }
//    };
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);

        menu.findItem(R.id.cart).getActionView().setOnClickListener(v -> {
            ScreenRouter.route(getActivity(), new CartClickActionEvent());
        });

    }

}

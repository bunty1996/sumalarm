package com.level_sense.app.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.level_sense.app.AlarmDeviceActivity;
import com.level_sense.app.DeviceDetailActivity;
import com.level_sense.app.DeviceLogActivity;
import com.level_sense.app.SwipeToDeleteCallback;
import com.level_sense.app.Utility.Functions;
import com.level_sense.app.graph.ChartActivity;
import com.level_sense.app.model.DeviceModel;
import com.level_sense.app.model.DeviceParentModel;
import com.level_sense.app.R;
import com.level_sense.app.RetroFit.BaseRequest;
import com.level_sense.app.RetroFit.RequestReceiver;
import com.level_sense.app.slideup.SlideUp;
import com.level_sense.app.slideup.SlideUpBuilder;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */

public class MyDeviceFragment extends Fragment {
    //TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    @BindView(R.id.list)
    RecyclerView list;
    @BindView(R.id.iVdeviceEdit)
    LinearLayout iVdeviceEdit;
    //ImageButton iVdeviceEdit;
    @BindView(R.id.iVDeviceNotification)
    LinearLayout iVDeviceNotification;
    //ImageButton iVDeviceNotification;
    @BindView(R.id.iVDeviceAlarm)
    LinearLayout iVDeviceAlarm;
    //ImageButton iVDeviceAlarm;
    @BindView(R.id.hidden_panel)
    LinearLayout hiddenPanel;
    @BindView(R.id.iVDeviceDelete)
    LinearLayout iVDeviceDelete;
    //ImageButton iVDeviceDelete;
    @BindView(R.id.iVDeviceGraph)
    LinearLayout iVDeviceGraph;
    //ImageButton iVDeviceGraph;
    @BindView(R.id.parent_rl)
    RelativeLayout parentRl;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.noDevicesFound)
    TextView noDevicesFound;
    Unbinder unbinder;
    //TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    @BindView(R.id.custom_pb)
    View custom_pb;
    MyItemRecyclerViewAdapter myItemRecyclerViewAdapter;

    private String deviceSerialNumber = "";
    MenuItem menuItem;

    ArrayList<DeviceModel> deviceModelArrayList = new ArrayList<>();

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MyDeviceFragment() {

    }

    //TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static MyDeviceFragment newInstance() {
        MyDeviceFragment fragment = new MyDeviceFragment();
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
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);
        ButterKnife.bind(this, view);
        //Set the adapter
        setHasOptionsMenu(true);
        hiddenPanel.setVisibility(View.INVISIBLE);
        getActivity().setTitle(getActivity().getResources().getString(R.string.my_devices));
        if (getArguments() != null) {

            if (getArguments().containsKey("deviceSerialNumber")) {

                deviceSerialNumber = getArguments().getString("deviceSerialNumber");

            }
        }


        //deviceSerialNumber

        list.setLayoutManager(new LinearLayoutManager(getActivity()));
        myItemRecyclerViewAdapter = new MyItemRecyclerViewAdapter(new OnListFragmentInteractionListener() {
            @Override
            public void onListFragmentInteraction(DeviceModel item) {

                if (!slideUp.isVisible())
                    slideUpDown();

            }

        });

        list.setAdapter(myItemRecyclerViewAdapter);

        unbinder = ButterKnife.bind(this, view);
        initSlider();

        callApiGetDeviceList();

        enableSwipeToDeleteAndUndo();

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (!swipeRefresh.isRefreshing()) {

                    swipeRefresh.setRefreshing(true);

                }
                callApiGetDeviceList();

            }

        });
        //startActivityForResult(ChartActivity.getIntent(getActivity(), null), 100);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.dash_board, menu);
        MenuItem menuItem = menu.findItem(R.id.refresh);
        menuItem.setVisible(true);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.refresh) {
            callApiGetDeviceList();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.iVdeviceEdit, R.id.iVDeviceNotification, R.id.iVDeviceDelete, R.id.iVDeviceGraph, R.id.iVDeviceAlarm, R.id.tVactionItem})
    public void onViewClicked(View view) {

        switch (view.getId()) {

            case R.id.iVdeviceEdit:

                startActivityForResult(DeviceDetailActivity.getIntent(getActivity(), myItemRecyclerViewAdapter.getSelectedItems()), 100);

                break;

            case R.id.iVDeviceNotification:

                startActivityForResult(AlarmDeviceActivity.getIntent(getActivity(), deviceModelArrayList.get(myItemRecyclerViewAdapter.getSelectedPosition())), 100);

                break;

            case R.id.iVDeviceDelete:

//                enableSwipeToDeleteAndUndo();

             /*   Functions.showDefaultTwoButonYesNo(getActivity(), getString(R.string.sure_to_delete) + " " + deviceModelArrayList.get(myItemRecyclerViewAdapter.getSelectedPosition()).getDisplayName() + " ?"
                        , R.string.deleteDevice, new Functions.OnDialogButtonClickListener() {
                            @Override
                            public void onClick(int type) {

                                if (type == 1) {
                                    callDeleteDevice(deviceModelArrayList.get(myItemRecyclerViewAdapter.getSelectedPosition()).getId());

                                }

                            }

                        });*/

                break;

            case R.id.iVDeviceGraph:

                startActivityForResult(ChartActivity.getIntent(getActivity(), deviceModelArrayList.get(myItemRecyclerViewAdapter.getSelectedPosition())), 100);

                break;

            case R.id.iVDeviceAlarm:

                startActivityForResult(DeviceLogActivity.getIntent(getActivity(), myItemRecyclerViewAdapter.getSelectedItems()), 100);

                break;

            case R.id.tVactionItem:

                slideUpDown();

                break;

        }

    }


    public interface OnListFragmentInteractionListener {
        //TODO: Update argument type and name
        void onListFragmentInteraction(DeviceModel item);
    }

    SlideUp slideUp;

    public void slideUpDown() {

        if (!slideUp.isVisible()) {
            slideUp.show();
        } else {
            slideUp.hide();
        }

    }

    void initSlider() {

        slideUp = new SlideUpBuilder(hiddenPanel).withListeners(new SlideUp.Listener.Events() {
            public void onSlide(float percent) {
            }

            @Override
            public void onVisibilityChanged(int visibility) {
                if (visibility == View.GONE) {
                }
            }
        }).withStartGravity(Gravity.BOTTOM).withLoggingEnabled(true).withGesturesEnabled(true).withStartState(SlideUp.State.HIDDEN).build();

    }



    private void callApiGetDeviceList() {

        BaseRequest baseRequest = new BaseRequest(getActivity(), this);
        baseRequest.setLoaderView(custom_pb);
        baseRequest.setBaseRequestListner(new RequestReceiver() {
            @Override
            public void onSuccess(int requestCode, String fullResponse, Object dataObject) {

                if (swipeRefresh != null && swipeRefresh.isRefreshing()) {

                    swipeRefresh.setRefreshing(false);

                }

                Log.e("devclst", fullResponse + "//");

                if (fullResponse != null) {

                    Gson gson = new Gson();
                    DeviceParentModel deviceParentModel = gson.fromJson(fullResponse, DeviceParentModel.class);
                    deviceModelArrayList = deviceParentModel.getDeviceList();
                    myItemRecyclerViewAdapter.setmValues(deviceModelArrayList, deviceSerialNumber);

                }

                if (deviceModelArrayList.isEmpty()) {

                    noDevicesFound.setVisibility(View.VISIBLE);
                    list.setVisibility(View.GONE);

                } else {

                    noDevicesFound.setVisibility(View.GONE);
                    list.setVisibility(View.VISIBLE);

                }

            }

            @Override
            public void onFailure(int requestCode, String errorCode, String message) {

                if (swipeRefresh != null && swipeRefresh.isRefreshing()) {

                    swipeRefresh.setRefreshing(false);
                }

            }

            @Override
            public void onNetworkFailure(int requestCode, String message) {

                if (swipeRefresh != null && swipeRefresh.isRefreshing()) {

                    swipeRefresh.setRefreshing(false);

                }

            }

        });

        baseRequest.callAPIGET(1, new HashMap<String, String>(), getResources().getString(R.string.api_get_device_list));

    }

    private void callDeleteDevice(String id) {

        BaseRequest baseRequest = new BaseRequest(getActivity(), this);
        baseRequest.setRunInBackground(true);
        JsonObject object = null;
        object = Functions.getInstance().getJsonObject("id", id
        );
        baseRequest.setBaseRequestListner(new RequestReceiver() {
            @Override
            public void onSuccess(int requestCode, String fullResponse, Object dataObject) {
                callApiGetDeviceList();
            }

            @Override
            public void onFailure(int requestCode, String errorCode, String message) {
                Functions.Alert(getActivity(), message, Functions.AlertType.Error);

            }

            @Override
            public void onNetworkFailure(int requestCode, String message) {

            }
        });
        baseRequest.callAPIPost(1, object, getResources().getString(R.string.api_deleteDevice));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callApiGetDeviceList();
    }

    private void enableSwipeToDeleteAndUndo() {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(getActivity()) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

                final int position = viewHolder.getAdapterPosition();
//                final String item = myItemRecyclerViewAdapter.getData().get(position);

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity(), R.style.ThemeDialogCustom);
//                dialogBuilder.setMessage(getString(R.string.sure_to_delete) + " " + deviceModelArrayList.get(position).getDisplayName());
                //Setting message manually and performing action on button click
                dialogBuilder.setMessage(getString(R.string.sure_to_delete) + " " + deviceModelArrayList.get(position).getDisplayName())
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
//                                finish();
//                                callDeleteDevice(deviceModelArrayList.get(myItemRecyclerViewAdapter.getSelectedPosition()).getId());
                                Toast.makeText(getActivity(), "Success",
                                        Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //  Action for 'NO' Button
                                Toast.makeText(getActivity(), "Close",
                                        Toast.LENGTH_SHORT).show();
                                dialog.cancel();

                            }
                        });
                //Creating dialog box
                AlertDialog alert = dialogBuilder.create();
                //Setting the title manually
                alert.setTitle(R.string.deleteDevice);
                alert.show();

                myItemRecyclerViewAdapter.notifyDataSetChanged();
            }


               /* Functions.showDefaultTwoButonYesNoDelete(getActivity(), getString(R.string.sure_to_delete) + " " + deviceModelArrayList.get(myItemRecyclerViewAdapter.getSelectedPosition()).getDisplayName() + " ?"
                        , R.string.deleteDevice, new Functions.OnDialogButtonClickListener() {
                            @Override
                            public void onClick(int type) {

                                if (type == 1) {

                                    Toast.makeText(getActivity(), "Successfull", Toast.LENGTH_SHORT).show();
//                                    callDeleteDevice(deviceModelArrayList.get(myItemRecyclerViewAdapter.getSelectedPosition()).getId());

                                }

                            }

                        });*/


//                Functions.showDefaultTwoButonYesNo(getActivity(), getString(R.string.are_you_sure_want_to_delete)
//                        , R.string.deleteContect, new Functions.OnDialogButtonClickListener() {
//                            @Override
//                            public void onClick(int type) {
//
//                                Toast.makeText(getActivity(), "Successfull", Toast.LENGTH_SHORT).show();
//
////                                    myItemRecyclerViewAdapter.removeItem(position);
//
//                            }
//
//                        });

//                myItemRecyclerViewAdapter.removeItem(position);


//                Snackbar snackbar = Snackbar
//                        .make(noDevicesFound, "Item was removed from the list.", Snackbar.LENGTH_LONG);
                /*snackbar.setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        myItemRecyclerViewAdapter.restoreItem(item, position);
                        list.scrollToPosition(position);
                    }
                });*/

//                snackbar.setActionTextColor(Color.YELLOW);
//                snackbar.show();


        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(list);
    }

}



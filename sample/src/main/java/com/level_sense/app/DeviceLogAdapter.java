package com.level_sense.app;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.level_sense.app.Fragment.MyDeviceFragment;
import com.level_sense.app.model.DeviceEventModel;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DeviceLogAdapter extends RecyclerView.Adapter<DeviceLogAdapter.ViewHolder> {
    private List<DeviceEventModel> mValues = new ArrayList<>();
    private final MyDeviceFragment.OnListFragmentInteractionListener mListener;
    DeviceLogAdapterTouch touch;

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public DeviceEventModel getSelectedItems() {
        return mValues.get(selectedPosition);
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }

    int selectedPosition = -1;

    public void setmValues(List<DeviceEventModel> mValues) {
        this.mValues = mValues;
        notifyDataSetChanged();
    }

    public DeviceLogAdapter(MyDeviceFragment.OnListFragmentInteractionListener listener) {
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_device_log, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);
        holder.setDeviceData();
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.listItemDeviceLogRelative)
        RelativeLayout listItemDeviceLogRelative;
        @BindView(R.id.tVLBLevent)
        TextView tVLBLevent;
        @BindView(R.id.tVEvent)
        TextView tVEvent;
        @BindView(R.id.tVLBLeventTime)
        TextView tVLBLeventTime;
        @BindView(R.id.tVEventTime)
        TextView tVEventTime;
        @BindView(R.id.tVLBLLogType)
        TextView tVLBLLogType;
        @BindView(R.id.tVLogType)
        TextView tVLogType;
        @BindView(R.id.tVLBMessage)
        TextView tVLBMessage;
        @BindView(R.id.tVMessage)
        TextView tVMessage;
        @BindView(R.id.tVLBTo)
        TextView tVLBTo;
        @BindView(R.id.tVTo)
        TextView tVTo;
        public DeviceEventModel mItem;

        void setDeviceData() {
            tVEventTime.setText(mItem.getEventTime());
            tVEvent.setText(mItem.getEvent());
            tVMessage.setText(mItem.getMessage());
            tVTo.setText(mItem.getTo());
        }

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
      //tVMessage.setMovementMethod(new ScrollingMovementMethod());

    /*//todo for make textview scrollable
        list.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
               v.findViewById(R.id.tVMessage).getParent().requestDisallowInterceptTouchEvent(false);
               return false;
            }
        });*/

      /*tVMessage.setOnTouchListener(new View.OnTouchListener(){
            public boolean onTouch(View v, MotionEvent event){
              //Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
        }});*/

            tVMessage.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    touch.onMessageTextTouch();
                    return false;
                }
            });

            listItemDeviceLogRelative.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    touch.onOutsideTouch();
                    return false;
                }
            });
        }
    }

    public void onDeviceAdapterTouch(DeviceLogAdapterTouch touch){
        this.touch=touch;
    }

    public interface DeviceLogAdapterTouch{
        public void onMessageTextTouch();
        public void onOutsideTouch();
    }

}

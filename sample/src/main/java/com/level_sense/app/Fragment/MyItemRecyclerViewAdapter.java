package com.level_sense.app.Fragment;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.level_sense.app.R;
import com.level_sense.app.model.DeviceModel;

import java.util.ArrayList;
import java.util.List;


public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {

    private List<DeviceModel> mValues = new ArrayList<>();
    private String deviceSerialNumber = "";
    private final MyDeviceFragment.OnListFragmentInteractionListener mListener;
    int selectedPosition = -1;

    public int getSelectedPosition() {
        return selectedPosition;
    }
    public DeviceModel getSelectedItems() {
        return mValues.get(selectedPosition);
    }


    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }

    public void setmValues(List<DeviceModel> mValues, String deviceSerialNumber) {
        this.mValues = mValues;
        this.deviceSerialNumber = deviceSerialNumber;
        notifyDataSetChanged();
    }

    public MyItemRecyclerViewAdapter(MyDeviceFragment.OnListFragmentInteractionListener listener) {
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_my_device, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.mItem = mValues.get(position);
        holder.tVDevice.setText(mValues.get(position).getDisplayName());

        if (!deviceSerialNumber.equalsIgnoreCase("") && deviceSerialNumber.equalsIgnoreCase(mValues.get(position).getDeviceSerialNumber())) {
            selectedPosition = position;
        }

      /*if(mValues.get(position).getCheckinFailCount()==0){
            holder.tVOnline.setText(holder.tVOnline.getContext().getString(R.string.online));
            holder.tVOnline.setTextColor(holder.tVOnline.getContext().getResources().getColor(R.color.green));
        }else{
            holder.tVOnline.setText(holder.tVOnline.getContext().getString(R.string.offline));
            holder.tVOnline.setTextColor(holder.tVOnline.getContext().getResources().getColor(R.color.red_500));
        }*/

        if (mValues.get(position).getOnline().equalsIgnoreCase("1")) {
            holder.tVOnline.setText(holder.tVOnline.getContext().getString(R.string.online));
            holder.tVOnline.setTextColor(holder.tVOnline.getContext().getResources().getColor(R.color.green));
        } else {
            holder.tVOnline.setText(holder.tVOnline.getContext().getString(R.string.offline));
            holder.tVOnline.setTextColor(holder.tVOnline.getContext().getResources().getColor(R.color.red_500));
        }

        if (mValues.get(position).getDeviceState() == 0) {
            holder.tVStatus.setText(holder.tVStatus.getContext().getString(R.string.normal));
            holder.tVStatus.setTextColor(holder.tVOnline.getContext().getResources().getColor(R.color.black));
        } else {
            holder.tVStatus.setTextColor(holder.tVOnline.getContext().getResources().getColor(R.color.red_500));
            holder.tVStatus.setText(
                    holder.tVStatus.getContext().getString(R.string.alarm_));
        }

        if (selectedPosition == position) {
            holder.mainRl.setBackgroundColor(holder.mainRl.getContext().getResources().getColor(R.color.selected_color));
        } else {
            holder.mainRl.setBackgroundColor(holder.mainRl.getContext().getResources().getColor(R.color.white));
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (null != mListener) {

                    //Notify the active callbacks interface (the activity, if the
                    //fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                    selectedPosition = position;
                    notifyDataSetChanged();

                }

            }

        });

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView tVDevice;
        public final TextView tVStatus;
        public final TextView tVOnline;
        public final RelativeLayout mainRl;
        public DeviceModel mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            tVDevice = (TextView) view.findViewById(R.id.tVDevice);
            tVStatus = (TextView) view.findViewById(R.id.tVStatus);
            mainRl = (RelativeLayout) view.findViewById(R.id.main_rl);
            tVOnline = (TextView) view.findViewById(R.id.tVOnline);

        }

        @Override
        public String toString() {
            return super.toString() + " '" + tVDevice.getText() + "'";
        }

    }

    public void removeItem(int position) {
        mValues.remove(position);
        notifyItemRemoved(position);
    }

//    public void restoreItem(String item, int position) {
//        mValues.add(position, item);
//        notifyItemInserted(position);
//    }
//
//    public ArrayList<String> getData() {
//        return data;
//    }

}

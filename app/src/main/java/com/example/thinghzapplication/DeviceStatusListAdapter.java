package com.example.thinghzapplication;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thinghzapplication.deviceModel.DataItem;

import java.util.ArrayList;
import java.util.List;

public class DeviceStatusListAdapter extends RecyclerView.Adapter<DeviceStatusListAdapter.MyViewHolder> {
    private ArrayList<DeviceStatusModel> deviceStatusModels;
    private Context context;
    private int lastCheckedPosition = -1;
    private RadioDeviceStatusClickListener mlistener;


    public DeviceStatusListAdapter(ArrayList<DeviceStatusModel> deviceStatusModels, Context context,RadioDeviceStatusClickListener mlistener) {
        this.deviceStatusModels = deviceStatusModels;
        this.context = context;
        this.mlistener = mlistener;
    }



    @NonNull
    @Override
    public DeviceStatusListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View deviceStatusView = LayoutInflater.from(context).inflate(R.layout.rv_device_status_layout,parent,false);
        return new DeviceStatusListAdapter.MyViewHolder(deviceStatusView);
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceStatusListAdapter.MyViewHolder holder, int position) {
        DeviceStatusModel deviceStatusModel = deviceStatusModels.get(position);
        holder.tv_device_status.setText(deviceStatusModel.getDeviceStatus());
        holder.rb_device_status.setChecked(lastCheckedPosition == position);
        if(position == lastCheckedPosition){
            mlistener.onRadioDeviceStatusClick(deviceStatusModels.get(position).getDeviceStatus());
            Toast.makeText(context,"Selected:"+deviceStatusModels.get(lastCheckedPosition).getDeviceStatus(),Toast.LENGTH_SHORT).show();
            Log.i("DeviceStatusAdapter","Selected:"+deviceStatusModels.get(lastCheckedPosition).getDeviceStatus());
        }

    }

    @Override
    public int getItemCount() {
        return deviceStatusModels.size();
    }

    public void setDeviceStatusList(ArrayList<DeviceStatusModel> deviceStatusModels) {
        this.deviceStatusModels = new ArrayList<>();
        this.deviceStatusModels = deviceStatusModels;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_device_status;
        public RadioButton rb_device_status;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_device_status = itemView.findViewById(R.id.tv_device_status_value);
            rb_device_status = itemView.findViewById(R.id.rb_device_status);
            rb_device_status.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int copyOfLastCheckedPosition = lastCheckedPosition;
                    lastCheckedPosition = getAdapterPosition();
                    notifyItemChanged(copyOfLastCheckedPosition);
                    notifyItemChanged(lastCheckedPosition);
                }
            });
        }
    }

    public interface RadioDeviceStatusClickListener{
        void onRadioDeviceStatusClick(String selected);
    }

}
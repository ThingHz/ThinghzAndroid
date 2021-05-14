package com.example.thinghzapplication;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thinghzapplication.deviceModel.DataItem;

import java.util.List;

public class AddedDeviceListAdapter extends RecyclerView.Adapter<AddedDeviceListAdapter.MyViewHolder> {
    private List<DataItem> deviceList;
    private static final String TAG = "AddedDeviceListAdapter";
    Context context;

    public AddedDeviceListAdapter(List<DataItem> deviceList, Context context) {
        this.deviceList = deviceList;
        this.context = context;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View deviceCardView = LayoutInflater.from(parent.getContext()).inflate(R.layout.added_device_card_layout,parent,false);
        return new MyViewHolder(deviceCardView);    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        DataItem dataItem = deviceList.get(position);
        holder.deviceId.setText(dataItem.getDeviceId());
        Log.i(TAG,"DeviceId:"+dataItem.getDeviceId());
        holder.deviceName.setText(dataItem.getDeviceName());
        Log.i(TAG,"DeviceName:"+dataItem.getDeviceName());
        switch(dataItem.getSensorProfile()){
            case 1:
                Log.i(TAG,"Sensor None");
                holder.deviceImage.setImageResource(R.drawable.ic_temp_online_svg);
                break;
            case 2:
                Log.i(TAG,"Sensor Temp");
                holder.deviceImage.setImageResource(R.drawable.ic_temp_online_svg);
                break;
            case 3:
                Log.i(TAG,"Sensor Temp and Humid");
                holder.deviceImage.setImageResource(R.drawable.ic_temp_online_svg);
                break;
            case 4:
                Log.i(TAG,"Sensor Gas");
                holder.deviceImage.setImageResource(R.drawable.ic_temp_online_svg);
                break;
            case 5:
                Log.i(TAG,"Sensor Gyro");
                holder.deviceImage.setImageResource(R.drawable.ic_temp_online_svg);
                break;
            case 6:
                Log.i(TAG,"Sensor THM");
                holder.deviceImage.setImageResource(R.drawable.ic_temp_online_svg);
                break;
            case 7:
                Log.i(TAG,"Sensor Ctrl");
                holder.deviceImage.setImageResource(R.drawable.ic_temp_online_svg);
                break;
            case 8:
                Log.i(TAG,"Sensor THC");
                holder.deviceImage.setImageResource(R.drawable.ic_temp_online_svg);
                break;
            default:
                Log.i(TAG,"Sensor Error");
                holder.deviceImage.setImageResource(R.drawable.ic_temp_online_svg);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return deviceList.size();
    }

    public void setDeviceList(List<DataItem> deviceList) {
        this.deviceList = deviceList;
        notifyDataSetChanged();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView deviceName,deviceId;
        public ImageView deviceImage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            deviceName = itemView.findViewById(R.id.tv_add_device_name);
            deviceId = itemView.findViewById(R.id.tv_add_device_id);
            deviceImage = itemView.findViewById(R.id.iv_add_icon_temp);

        }

    }
}

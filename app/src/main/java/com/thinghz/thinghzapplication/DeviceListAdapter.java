package com.thinghz.thinghzapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.google.gson.Gson;
import com.thinghz.thinghzapplication.Utils.KeysUtils;
import com.thinghz.thinghzapplication.deviceModel.DataItem;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class DeviceListAdapter extends RecyclerView.Adapter<DeviceListAdapter.MyViewHolder> {
    private List<DataItem> deviceList;
    private static final String TAG = "DeviceListAdapter";
    Context context;
    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();
    private DeleteClickListener mlistener;


    public DeviceListAdapter(Context context, List<DataItem> deviceList,DeleteClickListener mlistener) {
        this.deviceList = deviceList;
        this.context = context;
        this.mlistener = mlistener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View deviceCardView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
        return new MyViewHolder(deviceCardView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Animation animationCard = AnimationUtils.loadAnimation(context, R.anim.anim_card);
        DataItem dataItem = deviceList.get(position);
        boolean deviceStatus = dataItem.getDeviceStatus().equals("online");
        viewBinderHelper.setOpenOnlyOne(true);
        viewBinderHelper.bind(holder.swipeRevealLayout, String.valueOf(dataItem.getDeviceId()));
        viewBinderHelper.closeLayout(String.valueOf(dataItem.getDeviceId()));
        holder.cardViewLayout.setAnimation(animationCard);
        Log.i(TAG, "deviceStatus" + deviceStatus);
        holder.deviceHumid.setVisibility(View.GONE);
        holder.deviceHumidLabel.setVisibility(View.GONE);
        holder.light_icon.setVisibility(View.GONE);
        holder.deviceId.setText(dataItem.getDeviceId());
        Log.i(TAG, "DeviceId:" + dataItem.getDeviceId());
        holder.deviceName.setText(dataItem.getDeviceName());
        Log.i(TAG, "DeviceName:" + dataItem.getDeviceName());
        holder.deviceBattery.setText(dataItem.getBattery() + context.getResources().getString(R.string.percent));
        Log.i(TAG, "DeviceBattery:" + dataItem.getBattery());
        holder.timestampValue.setText(convertUnixTime(dataItem.getTimestamp()));
        switch (dataItem.getSensorProfile()) {
            case 1:
                Log.i(TAG, "Sensor None");
                holder.deviceImage.setImageResource(R.drawable.ic_temp_online_svg);
                if (!deviceStatus) {
                    holder.deviceImage.setImageResource(R.drawable.ic_temp_offline_svg);
                }
                holder.deviceHumidLabel.setVisibility(View.VISIBLE);
                holder.deviceHumid.setVisibility(View.VISIBLE);
                holder.deviceHumid.setText("--");
                holder.deviceTemp.setText("--");
                break;
            case 2:
                Log.i(TAG, "Sensor Temp");
                holder.deviceImage.setImageResource(R.drawable.ic_temp_online_svg);
                if (!deviceStatus) {
                    holder.deviceImage.setImageResource(R.drawable.ic_temp_offline_svg);
                }
                holder.deviceHumidLabel.setVisibility(View.GONE);
                holder.deviceHumid.setVisibility(View.GONE);
                if (dataItem.getTemp() != null) {
                    holder.deviceTemp.setText(dataItem.getTemp() + context.getResources().getString(R.string.degree_cel));
                } else {
                    holder.deviceTemp.setText("--");
                }
                break;
            case 3:
                Log.i(TAG, "Sensor Temp and Humid");
                holder.deviceImage.setImageResource(R.drawable.ic_temp_online_svg);
                if (!deviceStatus) {
                    holder.deviceImage.setImageResource(R.drawable.ic_temp_offline_svg);
                }
                holder.deviceHumidLabel.setVisibility(View.VISIBLE);
                holder.deviceHumid.setVisibility(View.VISIBLE);
                holder.deviceGas.setVisibility(View.GONE);
                holder.deviceGasLable.setVisibility(View.GONE);
                if (dataItem.getTemp() != null) {
                    holder.deviceTemp.setText(dataItem.getTemp() + context.getResources().getString(R.string.degree_cel));
                } else {
                    holder.deviceTemp.setText("--");
                }
                if (dataItem.getTemp() != null) {
                    holder.deviceHumid.setText(dataItem.getHumid() + context.getResources().getString(R.string.percent));
                } else {
                    holder.deviceHumid.setText("--");
                }
                break;
            case 4:
                Log.i(TAG, "Sensor Gas");
                holder.deviceImage.setImageResource(R.drawable.ic_temp_online_svg);
                if (!deviceStatus) {
                    holder.deviceImage.setImageResource(R.drawable.ic_temp_offline_svg);
                }
                holder.deviceHumidLabel.setVisibility(View.VISIBLE);
                holder.deviceHumid.setVisibility(View.VISIBLE);
                holder.deviceTempLable.setVisibility(View.VISIBLE);
                holder.deviceTemp.setVisibility(View.VISIBLE);
                holder.deviceGas.setVisibility(View.VISIBLE);
                holder.deviceGasLable.setVisibility(View.VISIBLE);
                holder.deviceTemp.setText(dataItem.getTemp() + context.getResources().getString(R.string.degree_cel));
                holder.deviceHumid.setText(dataItem.getHumid() + context.getResources().getString(R.string.percent));
                holder.deviceGas.setText(dataItem.getGas() + context.getResources().getString(R.string.ppm_value));
                break;
            case 5:
                Log.i(TAG, "Sensor Light");
                holder.deviceImage.setImageResource(R.drawable.ic_temp_online_svg);
                if (!deviceStatus) {
                    holder.deviceImage.setImageResource(R.drawable.ic_temp_offline_svg);
                }
                holder.deviceHumidLabel.setVisibility(View.VISIBLE);
                holder.deviceHumid.setVisibility(View.VISIBLE);
                holder.deviceGas.setVisibility(View.VISIBLE);
                holder.light_icon.setVisibility(View.VISIBLE);
                holder.deviceGasLable.setVisibility(View.VISIBLE);
                holder.deviceTempLable.setVisibility(View.VISIBLE);
                holder.deviceTemp.setVisibility(View.VISIBLE);
                holder.deviceGasLable.setText(R.string.lux_label);
                holder.deviceTemp.setText(dataItem.getTemp() + context.getResources().getString(R.string.degree_cel));
                holder.deviceHumid.setText(dataItem.getHumid() + context.getResources().getString(R.string.percent));
                holder.deviceGas.setText(dataItem.getLux() + context.getResources().getString(R.string.lux_value));
                break;
            case 6:
                Log.i(TAG, "Sensor THM");
                holder.deviceImage.setImageResource(R.drawable.ic_temp_online_svg);
                if (!deviceStatus) {
                    holder.deviceImage.setImageResource(R.drawable.ic_temp_offline_svg);
                }
                holder.deviceHumidLabel.setVisibility(View.VISIBLE);
                holder.deviceHumid.setVisibility(View.VISIBLE);
                holder.deviceTemp.setText(dataItem.getTemp() + context.getResources().getString(R.string.degree_cel));
                holder.deviceHumid.setText(dataItem.getHumid() + context.getResources().getString(R.string.percent));
                break;
            case 7:
                Log.i(TAG, "Sensor Ctrl");
                holder.deviceImage.setImageResource(R.drawable.ic_temp_online_svg);
                if (!deviceStatus) {
                    holder.deviceImage.setImageResource(R.drawable.ic_temp_offline_svg);
                }
                holder.deviceHumidLabel.setVisibility(View.GONE);
                holder.deviceHumid.setVisibility(View.GONE);
                holder.deviceTemp.setText(dataItem.getTemp() + context.getResources().getString(R.string.degree_cel));
                break;
            case 8:
                Log.i(TAG, "Sensor THC");
                holder.deviceImage.setImageResource(R.drawable.ic_temp_online_svg);
                if (!deviceStatus) {
                    holder.deviceImage.setImageResource(R.drawable.ic_temp_offline_svg);
                }
                holder.deviceHumidLabel.setVisibility(View.VISIBLE);
                holder.deviceHumid.setVisibility(View.VISIBLE);
                holder.deviceTemp.setText(dataItem.getTemp() + context.getResources().getString(R.string.degree_cel));
                holder.deviceHumid.setText(dataItem.getHumid() + context.getResources().getString(R.string.percent));
                break;
            default:
                Log.i(TAG, "Sensor Error");
                holder.deviceImage.setImageResource(R.drawable.ic_temp_online_svg);
                if (!deviceStatus) {
                    holder.deviceImage.setImageResource(R.drawable.ic_temp_offline_svg);
                }
                holder.deviceHumidLabel.setVisibility(View.VISIBLE);
                holder.deviceHumid.setVisibility(View.VISIBLE);
                holder.deviceHumid.setText("--");
                holder.deviceTemp.setText("--");
                break;
        }
        holder.escalationLevel.setText(String.valueOf(dataItem.getEscalation()));
        switch (dataItem.getEscalation()) {
            case 0:
                if (deviceStatus) {
                    holder.escalationLevel.setTextColor(ContextCompat.getColor(context, R.color.escalation_0));
                    holder.deviceImage.setColorFilter(ContextCompat.getColor(context, R.color.escalation_0));
                    break;
                }
                holder.escalationLevel.setTextColor(ContextCompat.getColor(context, R.color.icon_offline));
                break;
            case 1:
                if (deviceStatus) {
                    holder.escalationLevel.setTextColor(ContextCompat.getColor(context, R.color.escalation_1));
                    holder.deviceImage.setColorFilter(ContextCompat.getColor(context, R.color.escalation_1));
                    break;
                }
                holder.escalationLevel.setTextColor(ContextCompat.getColor(context, R.color.icon_offline));
                break;
            case 2:
                if (deviceStatus) {
                    holder.deviceImage.setColorFilter(ContextCompat.getColor(context, R.color.escalation_2));
                    holder.escalationLevel.setTextColor(ContextCompat.getColor(context, R.color.escalation_2));
                    break;
                }
                holder.escalationLevel.setTextColor(ContextCompat.getColor(context, R.color.icon_offline));
                break;
            case 3:
                if (deviceStatus) {
                    holder.deviceImage.setColorFilter(ContextCompat.getColor(context, R.color.escalation_3));
                    holder.escalationLevel.setTextColor(ContextCompat.getColor(context, R.color.escalation_3));
                    break;
                }
                holder.escalationLevel.setTextColor(ContextCompat.getColor(context, R.color.icon_offline));
                break;
            case 4:
                if (deviceStatus) {
                    holder.deviceImage.setColorFilter(ContextCompat.getColor(context, R.color.escalation_4));
                    holder.escalationLevel.setTextColor(ContextCompat.getColor(context, R.color.escalation_4));
                    break;
                }
                holder.escalationLevel.setTextColor(ContextCompat.getColor(context, R.color.icon_offline));
                break;
            case 5:
                if (deviceStatus) {
                    holder.deviceImage.setColorFilter(ContextCompat.getColor(context, R.color.escalation_5));
                    holder.escalationLevel.setTextColor(ContextCompat.getColor(context, R.color.escalation_5));
                    break;
                }
                holder.escalationLevel.setTextColor(ContextCompat.getColor(context, R.color.icon_offline));
                break;
            default:
                if (deviceStatus) {
                    holder.deviceImage.setColorFilter(ContextCompat.getColor(context, R.color.escalation_0));
                    holder.escalationLevel.setTextColor(ContextCompat.getColor(context, R.color.escalation_0));
                    break;
                }
                holder.escalationLevel.setTextColor(ContextCompat.getColor(context, R.color.icon_offline));
                break;
        }
        holder.analysisButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AnalysisActivity.class);
                intent.putExtra("DeviceData",new Gson().toJson(deviceList.get(position)));
                context.startActivity(intent);
            }
        });

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertbuilder = new AlertDialog.Builder(context);
                alertbuilder.setMessage("Do you want to Delete this device?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                int actualPosition = holder.getAdapterPosition();
                                String deviceid = deviceList.get(actualPosition).getDeviceId();
                                Integer escalation = deviceList.get(actualPosition).getEscalation();
                                removeItem(actualPosition);
                                mlistener.onDeleteClick(deviceid,escalation);
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                AlertDialog alert = alertbuilder.create();
                alert.setTitle("Delete Device");
                alert.show();
            }
        });

        holder.iv_icon_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mlistener.onSettingClick(position);
            }
        });

        holder.light_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mlistener.onLightClick(position);
            }
        });
        Log.i(TAG, "DeviceEscalation:" + dataItem.getEscalation());
    }

    private void removeItem(int adapterPosition) {
        deviceList.remove(adapterPosition);
        notifyItemRemoved(adapterPosition);
        notifyItemRangeChanged(adapterPosition, deviceList.size());
    }

    private String convertUnixTime(int timestamp) {
        Instant instant = Instant.ofEpochSecond(timestamp);
        Date date = Date.from(instant);
        SimpleDateFormat sdf = new SimpleDateFormat(KeysUtils.getPATTERN());
        sdf.setTimeZone(TimeZone.getTimeZone("IST"));
        return sdf.format(date);
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
        public TextView deviceName, deviceId, escalationLevel, deviceTemp, deviceHumid, deviceBattery,deviceGas;
        public TextView deviceHumidLabel;
        public TextView deviceTempLable;
        public TextView deviceGasLable;
        public TextView timestampValue;
        public ImageView deviceImage,iv_icon_settings,light_icon;
        public Button analysisButton;
        public ImageView deleteButton;
        public SwipeRevealLayout swipeRevealLayout;
        public CardView cardViewLayout;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            deviceName = itemView.findViewById(R.id.tv_device_name);
            deviceId = itemView.findViewById(R.id.tv_device_id);
            escalationLevel = itemView.findViewById(R.id.tv_escalation_value);
            deviceTemp = itemView.findViewById(R.id.tv_temp_value);
            deviceGas = itemView.findViewById(R.id.tv_gas_value);
            deviceHumid = itemView.findViewById(R.id.tv_humid_value);
            deviceTempLable = itemView.findViewById(R.id.tv_temp_label);
            deviceGasLable = itemView.findViewById(R.id.tv_gas_label);
            deviceBattery = itemView.findViewById(R.id.tv_battery_value);
            deviceHumidLabel = itemView.findViewById(R.id.tv_humid_label);
            timestampValue = itemView.findViewById(R.id.tv_timestamp_value);
            deviceImage = itemView.findViewById(R.id.iv_icon_temp);
            analysisButton = itemView.findViewById(R.id.analysis_button);
            swipeRevealLayout = itemView.findViewById(R.id.swipe_layout);
            deleteButton = itemView.findViewById(R.id.iv_delete_device);
            cardViewLayout = itemView.findViewById(R.id.device_card);
            iv_icon_settings = itemView.findViewById(R.id.iv_icon_settings);
            light_icon = itemView.findViewById(R.id.iv_icon_light);
        }
    }


    public interface DeleteClickListener {
        void onDeleteClick(String deviceId, Integer escalation);
        void onSettingClick(Integer position);
        void onLightClick(Integer position);
    }
}

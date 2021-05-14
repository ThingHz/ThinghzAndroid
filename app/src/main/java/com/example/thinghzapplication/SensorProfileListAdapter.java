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

import java.util.ArrayList;

public class SensorProfileListAdapter extends RecyclerView.Adapter<SensorProfileListAdapter.MyViewHolder>{
    private ArrayList<SensorProfileModel> sensorProfileModels;
    Context context;
    private int lastCheckedPosition = -1;
    private RadioSensorProfileClickListener mlistener;

    public SensorProfileListAdapter(ArrayList<SensorProfileModel> sensorProfileModels, Context context,RadioSensorProfileClickListener mlistener) {
        this.sensorProfileModels = sensorProfileModels;
        this.context = context;
        this.mlistener = mlistener;
    }

    @NonNull
    @Override
    public SensorProfileListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View sensorView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_sensor_profile_layout,parent,false);
        return new SensorProfileListAdapter.MyViewHolder(sensorView);

    }

    @Override
    public void onBindViewHolder(@NonNull SensorProfileListAdapter.MyViewHolder holder, int position) {
        SensorProfileModel sensorModel = sensorProfileModels.get(position);
        holder.tv_sensor.setText(sensorModel.getSensorProfile());
        holder.rb_sensor.setChecked(position == lastCheckedPosition);
        if(position == lastCheckedPosition){
            mlistener.onRadioSensorProfileClick(sensorProfileModels.get(lastCheckedPosition).getSensorProfile());
            Toast.makeText(context,"Selected:"+sensorProfileModels.get(lastCheckedPosition).getSensorProfile(),Toast.LENGTH_SHORT).show();
            Log.i("SensorProfileListAdapter","Selected:"+sensorProfileModels.get(lastCheckedPosition).getSensorProfile());
        }
    }

    @Override
    public int getItemCount() {
        return sensorProfileModels.size();
    }

    public void setSensorProfile(ArrayList<SensorProfileModel> sensorProfileModels) {
        this.sensorProfileModels = new ArrayList<>();
        this.sensorProfileModels = sensorProfileModels;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_sensor;
        public RadioButton rb_sensor;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_sensor = itemView.findViewById(R.id.tv_sensor_profile_value);
            rb_sensor = itemView.findViewById(R.id.rb_sensor_profile);
            rb_sensor.setOnClickListener(new View.OnClickListener() {
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

    public interface RadioSensorProfileClickListener{
        void onRadioSensorProfileClick(String selected);
    }

}

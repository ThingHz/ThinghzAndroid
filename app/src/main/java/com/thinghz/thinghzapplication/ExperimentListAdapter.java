package com.thinghz.thinghzapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.thinghz.thinghzapplication.deviceModel.DataItem;
import com.thinghz.thinghzapplication.deviceModel.GetDeviceMetaDataModel;
import com.thinghz.thinghzapplication.deviceModel.UpdateDeviceMetaRequest;
import com.thinghz.thinghzapplication.retrofitInterface.GetdeviceData;
import com.thinghz.thinghzapplication.retrofitInterface.PublishInterface;

import java.util.ArrayList;
import java.util.List;

public class ExperimentListAdapter extends RecyclerView.Adapter<ExperimentListAdapter.MyViewHolder> {

    private List<GetDeviceMetaDataModel.MetaData> deviceMetaData;

    private final UpdateDeviceMetaRequest updateDeviceMetaRequest = new UpdateDeviceMetaRequest();

    private UpdateMetaDataClickListener mlistener;
    Context context;

    public ExperimentListAdapter(Context context, List<GetDeviceMetaDataModel.MetaData> deviceMetaData, UpdateMetaDataClickListener mlistener) {
        this.deviceMetaData = deviceMetaData;
        this.context = context;
        this.mlistener = mlistener;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View metaCardView = LayoutInflater.from(parent.getContext()).inflate(R.layout.experiment_card_layout, parent, false);
        return new MyViewHolder(metaCardView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Animation animationCard = AnimationUtils.loadAnimation(context, R.anim.anim_card);
        Log.i("ExperimentListAdapter", String.valueOf(deviceMetaData.get(0).getObj4()));
        GetDeviceMetaDataModel.MetaData metaData = deviceMetaData.get(0);
        holder.cardViewLayout.setAnimation(animationCard);
        holder.edit_shelf1_lux.setVisibility(View.GONE);
        switch(position){
            case 0:
                Log.i("ExperimentListAdapter", "1"+ String.valueOf(deviceMetaData.get(0).getEx1()));
                holder.edit_experiment_shelf1.setText(metaData.getEx1());
                holder.edit_objective_shelf1.setText(metaData.getObj1());
                holder.text_label_shelf.setText("Shelf 1");
                break;
            case 1:
                Log.i("ExperimentListAdapter", "1"+ String.valueOf(deviceMetaData.get(0).getEx1()));
                holder.edit_experiment_shelf1.setText(metaData.getEx2());
                holder.edit_objective_shelf1.setText(metaData.getObj2());
                holder.text_label_shelf.setText("Shelf 2");
                break;
            case 2:
                Log.i("ExperimentListAdapter", "1"+ String.valueOf(deviceMetaData.get(0).getEx1()));
                holder.edit_experiment_shelf1.setText(metaData.getEx3());
                holder.edit_objective_shelf1.setText(metaData.getObj3());
                holder.text_label_shelf.setText("Shelf 3");
                break;
            case 3:
                Log.i("ExperimentListAdapter", "1"+ String.valueOf(deviceMetaData.get(0).getEx1()));
                holder.edit_experiment_shelf1.setText(metaData.getEx4());
                holder.edit_objective_shelf1.setText(metaData.getObj4());
                holder.text_label_shelf.setText("Shelf 4");
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + position);
        }

        holder.img_edit_shelf1_experiment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.edit_experiment_shelf1.getTag().toString().equalsIgnoreCase("disable")){
                    Log.i("Experiment List Adapter", "onClick:"+ holder.edit_experiment_shelf1.getTag());
                    holder.edit_experiment_shelf1.setTag("enable");
                    holder.edit_experiment_shelf1.setEnabled(true);
                    holder.edit_experiment_shelf1.requestFocus();
                }
                else {
                    holder.edit_experiment_shelf1.setTag("disable");
                    holder.edit_experiment_shelf1.setEnabled(false);
                }
            }
        });

        holder.img_edit_shelf1_objective.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.edit_objective_shelf1.getTag().toString().equalsIgnoreCase("disable")){
                    Log.i("Experiment List Adapter", "onClick:"+holder.edit_objective_shelf1.getTag());
                    holder.edit_objective_shelf1.setTag("enable");
                    holder.edit_objective_shelf1.setEnabled(true);
                    holder.edit_objective_shelf1.requestFocus();
                }
                else {
                    holder.edit_objective_shelf1.setTag("disable");
                    holder.edit_objective_shelf1.setEnabled(false);
                }
            }
        });
        holder.check_shelf1_lux.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    holder.edit_shelf1_lux.setVisibility(View.VISIBLE);
                } else {
                    holder.edit_shelf1_lux.setVisibility(View.GONE);
                }
            }
        });

        holder.button_update_experiment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(position){
                    case 0:
                        updateDeviceMetaRequest.setEx1(holder.edit_experiment_shelf1.getText().toString());
                        updateDeviceMetaRequest.setObj1(holder.edit_objective_shelf1.getText().toString());
                        updateDeviceMetaRequest.setEx2(metaData.getEx2());
                        updateDeviceMetaRequest.setObj2(metaData.getObj2());
                        updateDeviceMetaRequest.setEx3(metaData.getEx3());
                        updateDeviceMetaRequest.setObj3(metaData.getObj3());
                        updateDeviceMetaRequest.setEx4(metaData.getEx4());
                        updateDeviceMetaRequest.setObj4(metaData.getObj4());

                        if(!holder.edit_shelf1_lux.getText().toString().equals("")){
                            updateDeviceMetaRequest.setLux1(holder.edit_shelf1_lux.getText().toString());
                        }
                        if(!holder.check_shelf1_lux.isChecked()){
                            updateDeviceMetaRequest.setSensorAt(1);
                        }
                        break;
                    case 1:
                        updateDeviceMetaRequest.setEx2(holder.edit_experiment_shelf1.getText().toString());
                        updateDeviceMetaRequest.setObj2(holder.edit_objective_shelf1.getText().toString());
                        updateDeviceMetaRequest.setEx1(metaData.getEx1());
                        updateDeviceMetaRequest.setObj1(metaData.getObj1());
                        updateDeviceMetaRequest.setEx3(metaData.getEx3());
                        updateDeviceMetaRequest.setObj3(metaData.getObj3());
                        updateDeviceMetaRequest.setEx4(metaData.getEx4());
                        updateDeviceMetaRequest.setObj4(metaData.getObj4());
                        if(!holder.edit_shelf1_lux.getText().toString().equals("")){
                            updateDeviceMetaRequest.setLux2(holder.edit_shelf1_lux.getText().toString());
                        }
                        if(!holder.check_shelf1_lux.isChecked()){
                            updateDeviceMetaRequest.setSensorAt(2);
                        }
                        break;
                    case 2:
                        updateDeviceMetaRequest.setEx3(holder.edit_experiment_shelf1.getText().toString());
                        updateDeviceMetaRequest.setObj3(holder.edit_objective_shelf1.getText().toString());
                        updateDeviceMetaRequest.setEx1(metaData.getEx1());
                        updateDeviceMetaRequest.setObj1(metaData.getObj1());
                        updateDeviceMetaRequest.setEx2(metaData.getEx2());
                        updateDeviceMetaRequest.setObj2(metaData.getObj2());
                        updateDeviceMetaRequest.setEx4(metaData.getEx4());
                        updateDeviceMetaRequest.setObj4(metaData.getObj4());
                        if(!holder.edit_shelf1_lux.getText().toString().equals("")){
                            updateDeviceMetaRequest.setLux3(holder.edit_shelf1_lux.getText().toString());
                        }
                        if(!holder.check_shelf1_lux.isChecked()){
                            updateDeviceMetaRequest.setSensorAt(3);
                        }
                        break;
                    case 3:
                        updateDeviceMetaRequest.setEx4(holder.edit_experiment_shelf1.getText().toString());
                        updateDeviceMetaRequest.setObj4(holder.edit_objective_shelf1.getText().toString());
                        updateDeviceMetaRequest.setEx1(metaData.getEx1());
                        updateDeviceMetaRequest.setObj1(metaData.getObj1());
                        updateDeviceMetaRequest.setEx2(metaData.getEx2());
                        updateDeviceMetaRequest.setObj2(metaData.getObj2());
                        updateDeviceMetaRequest.setEx3(metaData.getEx3());
                        updateDeviceMetaRequest.setObj3(metaData.getObj3());
                        if(!holder.edit_shelf1_lux.getText().toString().equals("")){
                            updateDeviceMetaRequest.setLux4(holder.edit_shelf1_lux.getText().toString());
                        }
                        if(!holder.check_shelf1_lux.isChecked()){
                            updateDeviceMetaRequest.setSensorAt(4);
                        }
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + position);
                }
                mlistener.metaDataClick(updateDeviceMetaRequest);
            }
        });
    }

    public void setMetaList(List<GetDeviceMetaDataModel.MetaData> deviceMetaData) {
        this.deviceMetaData = deviceMetaData;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView img_edit_shelf1_experiment;
        public ImageView img_edit_shelf1_objective;
        public TextView text_label_shelf;
        public TextInputEditText edit_experiment_shelf1;
        public TextInputEditText edit_objective_shelf1;
        public EditText edit_shelf1_lux;
        public CheckBox check_shelf1_lux;
        public CardView cardViewLayout;
        public Button button_update_experiment;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            img_edit_shelf1_experiment = itemView.findViewById(R.id.iv_edit_shelf1_experiment);
            img_edit_shelf1_objective = itemView.findViewById(R.id.iv_edit_shelf1_objective);
            edit_experiment_shelf1  = itemView.findViewById(R.id.edit_text_shelf1_experiment);
            edit_objective_shelf1 =itemView.findViewById(R.id.edit_text_shelf1_objective);
            edit_shelf1_lux = itemView.findViewById(R.id.et_shelf1_lux);
            check_shelf1_lux = itemView.findViewById(R.id.cb_shelf1_lux);
            cardViewLayout = itemView.findViewById(R.id.cl_experiment_card);
            button_update_experiment = itemView.findViewById(R.id.bt_device_experiment_update);
            text_label_shelf = itemView.findViewById(R.id.tv_shelf_label);
        }
    }

    public interface UpdateMetaDataClickListener {
        void metaDataClick(UpdateDeviceMetaRequest updateDeviceMetaRequest);
    }

}

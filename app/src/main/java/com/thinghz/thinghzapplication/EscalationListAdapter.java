package com.thinghz.thinghzapplication;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class EscalationListAdapter extends RecyclerView.Adapter<EscalationListAdapter.MyViewHolder> {
    private ArrayList<EscalationModel> escalationModels;
    Context context;
    private int lastCheckedPosition = -1;
    private RadioEscalationClickListener mlistener;


    public EscalationListAdapter(ArrayList<EscalationModel> escalationModels, Context context,RadioEscalationClickListener mlistener) {
        this.escalationModels = escalationModels;
        this.context = context;
        this.mlistener = mlistener;
    }

    @NonNull
    @Override
    public EscalationListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View escalationView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_escalation_layout,parent,false);
        return new EscalationListAdapter.MyViewHolder(escalationView);
    }

    @Override
    public void onBindViewHolder(@NonNull EscalationListAdapter.MyViewHolder holder, int position) {
        EscalationModel escalationModel = escalationModels.get(position);
        holder.tv_escalation.setText(escalationModel.getEscalation());
        holder.rb_escalation.setChecked(position == lastCheckedPosition);
        if(position == lastCheckedPosition){
            mlistener.onRadioEscalationClick(escalationModels.get(lastCheckedPosition).getEscalation());
            Toast.makeText(context,"Selected:"+escalationModels.get(lastCheckedPosition).getEscalation(),Toast.LENGTH_SHORT).show();
            Log.i("DeviceStatusAdapter","Selected:"+escalationModels.get(lastCheckedPosition).getEscalation());
        }
    }

    @Override
    public int getItemCount() {
        return escalationModels.size();
    }

    public void setEscalation(ArrayList<EscalationModel> escalationModels) {
        this.escalationModels = new ArrayList<>();
        this.escalationModels = escalationModels;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_escalation;
        public RadioButton rb_escalation;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_escalation = itemView.findViewById(R.id.tv_escalation_list_value);
            rb_escalation = itemView.findViewById(R.id.rb_escalation);
            rb_escalation.setOnClickListener(new View.OnClickListener() {
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

    public interface RadioEscalationClickListener{
        void onRadioEscalationClick(String selected);
    }

}

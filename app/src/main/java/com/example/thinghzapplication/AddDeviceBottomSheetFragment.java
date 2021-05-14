package com.example.thinghzapplication;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.thinghzapplication.Utils.KeysUtils;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;

public class AddDeviceBottomSheetFragment extends BottomSheetDialogFragment implements View.OnClickListener{

    Spinner spinner_max_temp,spinner_min_Temp;
    EditText device_name;
    TextInputLayout edit_text_device_neame_layout;
    View view;
    Button add_device_button;
    HashMap<String,String> map = new HashMap<>();
    private AddButtomClickListner mlistener;
    boolean isMaxTempSelected = false;
    boolean isMinTempSelected = false;
    private final String TAG = AddDeviceBottomSheetFragment.this.getClass().getSimpleName();

    public static AddDeviceBottomSheetFragment newInstance() {
        return new AddDeviceBottomSheetFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_add_device_bottom_sheet, container, false);
        super.onViewCreated(view, savedInstanceState);
        spinner_max_temp = view.findViewById(R.id.spinner_max_temp);
        spinner_min_Temp = view.findViewById(R.id.spinner_min_temp);
        device_name = view.findViewById(R.id.edit_text_device_name);
        edit_text_device_neame_layout = view.findViewById(R.id.edit_text_device_neame_layout);
        add_device_button = view.findViewById(R.id.button_add_device);
        device_name.setImeOptions(EditorInfo.IME_ACTION_DONE);
        ArrayAdapter adapterMaxTemp = ArrayAdapter.createFromResource(getActivity(), R.array.max_Temp_enteries, android.R.layout.simple_expandable_list_item_1);
        adapterMaxTemp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner_max_temp.setAdapter(adapterMaxTemp);
        ArrayAdapter adapterminTemp = ArrayAdapter.createFromResource(getActivity(), R.array.min_Temp_enteries, android.R.layout.simple_expandable_list_item_1);
        adapterminTemp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_min_Temp.setAdapter(adapterminTemp);
        add_device_button.setOnClickListener(this);
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof AddButtomClickListner){
            mlistener = (AddButtomClickListner) context;
        }else{
            throw new RuntimeException(context.toString()+" must implement AddButtonClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mlistener =null;
    }

    public interface AddButtomClickListner {
        void onAddButtonClick(HashMap<String,String> map);
    }

    @Override
    public void onClick(View view) {
        String deviceName = device_name.getText().toString();
        boolean isDeviceNameEntered = false;
        Log.i(TAG,"gotdeviceName: "+deviceName);
        map.put(KeysUtils.getMap_device_name(),deviceName);
        Log.i(TAG,"map: "+map);
        if(deviceName.isEmpty()){
            edit_text_device_neame_layout.setErrorEnabled(true);
            edit_text_device_neame_layout.setError("Please enter valid Device name");
            isDeviceNameEntered = false;
        }else{
            edit_text_device_neame_layout.setErrorEnabled(false);
            edit_text_device_neame_layout.setError(null);
            isDeviceNameEntered = true;
        }
        spinner_min_Temp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i(TAG,"Selected Value minTemp="+adapterView.getItemAtPosition(i).toString());
                String minTempSelection = adapterView.getItemAtPosition(i).toString();
                map.put(KeysUtils.getMap_min_temp(),minTempSelection);
                isMinTempSelected = true;
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                isMinTempSelected = false;
            }
        });
        spinner_max_temp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i(TAG,"Selected Value maxTemp="+adapterView.getItemAtPosition(i).toString());
                String maxTempSelection = adapterView.getItemAtPosition(i).toString();
                map.put(KeysUtils.getMap_max_temp(),maxTempSelection);
                isMaxTempSelected = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                isMinTempSelected = false;
            }

        });

        if (isDeviceNameEntered && isMaxTempSelected && isMinTempSelected) {
            mlistener.onAddButtonClick(map);
            dismiss();
        }else{
            if(!isMaxTempSelected || !isMinTempSelected){
                Toast.makeText(getActivity(),"MaxTemp or MinTemp not Selected",Toast.LENGTH_SHORT).show();
            }
        }
    }
}



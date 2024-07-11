package com.thinghz.thinghzapplication;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.thinghz.thinghzapplication.Utils.KeysUtils;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;

public class AddDeviceBottomSheetFragment extends BottomSheetDialogFragment implements View.OnClickListener{

    Spinner spinner_max_temp,spinner_min_Temp,spinner_max_humidity,spinner_min_humidity,spinner_max_carbon,spinner_min_carbon,spinner_max_lux,spinner_min_lux;
    EditText device_name;
    LinearLayout carbon_layout_spinner,lux_layout_spinner,carbon_layout_label,lux_layout_label;
    int sensor_profile = 0;
    TextInputLayout edit_text_device_name_layout;
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
        Bundle args = getArguments();
        if(args != null){
            sensor_profile = args.getInt("sensor_profile");
        }

        carbon_layout_label = view.findViewById(R.id.ll_carbon_label);
        lux_layout_label = view.findViewById(R.id.ll_lux_label);
        carbon_layout_spinner = view.findViewById(R.id.ll_carbon_spinner);
        lux_layout_spinner = view.findViewById(R.id.ll_lux_spinner);
        if(sensor_profile == 5){
            carbon_layout_spinner.setVisibility(View.GONE);
            carbon_layout_label.setVisibility(View.GONE);
        }
        spinner_max_temp = view.findViewById(R.id.spinner_max_temp);
        spinner_min_Temp = view.findViewById(R.id.spinner_min_temp);
        spinner_max_lux = view.findViewById(R.id.spinner_max_lux);
        spinner_min_lux = view.findViewById(R.id.spinner_min_lux);
        spinner_max_humidity = view.findViewById(R.id.spinner_max_humidity);
        spinner_min_humidity = view.findViewById(R.id.spinner_min_humidity);
        spinner_max_carbon = view.findViewById(R.id.spinner_max_carbon);
        spinner_min_carbon = view.findViewById(R.id.spinner_min_carbon);
        device_name = view.findViewById(R.id.edit_text_device_name);
        edit_text_device_name_layout = view.findViewById(R.id.edit_text_device_neame_layout);
        add_device_button = view.findViewById(R.id.button_add_device);
        device_name.setImeOptions(EditorInfo.IME_ACTION_DONE);
        ArrayAdapter adapterMaxTemp = ArrayAdapter.createFromResource(getActivity(), R.array.max_Temp_enteries, android.R.layout.simple_expandable_list_item_1);
        adapterMaxTemp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_max_temp.setAdapter(adapterMaxTemp);
        ArrayAdapter adapterminTemp = ArrayAdapter.createFromResource(getActivity(), R.array.min_Temp_enteries, android.R.layout.simple_expandable_list_item_1);
        adapterminTemp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_min_Temp.setAdapter(adapterminTemp);
        ArrayAdapter adapterMaxHumidity = ArrayAdapter.createFromResource(getActivity(), R.array.max_Humidity_enteries, android.R.layout.simple_expandable_list_item_1);
        adapterMaxHumidity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_max_humidity.setAdapter(adapterMaxHumidity);
        ArrayAdapter adapterMinHumidity = ArrayAdapter.createFromResource(getActivity(), R.array.min_Humidity_enteries, android.R.layout.simple_expandable_list_item_1);
        adapterMinHumidity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_min_humidity.setAdapter(adapterMinHumidity);
        if(sensor_profile != 5){
            ArrayAdapter adapterMaxCarbon = ArrayAdapter.createFromResource(getActivity(), R.array.max_carbon_enteries, android.R.layout.simple_expandable_list_item_1);
            adapterMaxCarbon.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_max_carbon.setAdapter(adapterMaxCarbon);
            ArrayAdapter adapterMinCarbon = ArrayAdapter.createFromResource(getActivity(), R.array.min_carbon_enteries, android.R.layout.simple_expandable_list_item_1);
            adapterMinCarbon.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_min_carbon.setAdapter(adapterMinCarbon);
        }else{
            ArrayAdapter adapterMaxLux = ArrayAdapter.createFromResource(getActivity(), R.array.max_lux_enteries, android.R.layout.simple_expandable_list_item_1);
            adapterMaxLux.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_max_carbon.setAdapter(adapterMaxLux);
            ArrayAdapter adapterMinLux = ArrayAdapter.createFromResource(getActivity(), R.array.min_lux_enteries, android.R.layout.simple_expandable_list_item_1);
            adapterMinLux.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_min_carbon.setAdapter(adapterMinLux);
        }

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
            edit_text_device_name_layout.setErrorEnabled(true);
            edit_text_device_name_layout.setError("Please enter valid Device name");
            isDeviceNameEntered = false;
        }else{
            edit_text_device_name_layout.setErrorEnabled(false);
            edit_text_device_name_layout.setError(null);
            isDeviceNameEntered = true;
        }
        spinner_min_Temp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i(TAG,"Selected Value min Temp="+adapterView.getItemAtPosition(i).toString());
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
                Log.i(TAG,"Selected Value max Temp="+adapterView.getItemAtPosition(i).toString());
                String maxTempSelection = adapterView.getItemAtPosition(i).toString();
                map.put(KeysUtils.getMap_max_temp(),maxTempSelection);
                isMaxTempSelected = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                isMinTempSelected = false;
            }

        });

        spinner_max_humidity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i(TAG,"Selected Value max Humid="+adapterView.getItemAtPosition(i).toString());
                String maxHumidSelection = adapterView.getItemAtPosition(i).toString();
                map.put(KeysUtils.getMap_max_humid(),maxHumidSelection);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner_min_humidity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i(TAG,"Selected Value min Humid="+adapterView.getItemAtPosition(i).toString());
                String minHumidSelection = adapterView.getItemAtPosition(i).toString();
                map.put(KeysUtils.getMap_min_humid(),minHumidSelection);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        if(sensor_profile != 5){
            spinner_max_carbon.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    Log.i(TAG,"Selected Value max Carbon="+adapterView.getItemAtPosition(i).toString());
                    String maxCarbonSelection = adapterView.getItemAtPosition(i).toString();
                    map.put(KeysUtils.getMap_max_carbon(),maxCarbonSelection);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            spinner_min_carbon.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    Log.i(TAG,"Selected Value min Carbon="+ adapterView.getItemAtPosition(i).toString());
                    String minCarbonSelection = adapterView.getItemAtPosition(i).toString();
                    map.put(KeysUtils.getMap_min_carbon(),minCarbonSelection);
                }
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }else {
            spinner_max_lux.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    Log.i(TAG,"Selected Value max Humid="+adapterView.getItemAtPosition(i).toString());
                    String maxLuxSelection = adapterView.getItemAtPosition(i).toString();
                    map.put(KeysUtils.getMap_max_lux(),maxLuxSelection);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            spinner_min_lux.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    Log.i(TAG,"Selected Value min Humid="+ adapterView.getItemAtPosition(i).toString());
                    String minLuxSelection = adapterView.getItemAtPosition(i).toString();
                    map.put(KeysUtils.getMap_min_lux(),minLuxSelection);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

        }


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



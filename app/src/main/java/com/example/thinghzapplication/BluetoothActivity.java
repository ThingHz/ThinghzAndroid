package com.example.thinghzapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.Collection;

public class BluetoothActivity extends AppCompatActivity implements BeaconConsumer {
    private Button button_scan;
    private BeaconManager beaconManager;
    private boolean isScanning = false;
    private final String TAG = BluetoothActivity.this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        beaconManager = BeaconManager.getInstanceForApplication(getApplicationContext());
        button_scan = findViewById(R.id.buttonScan);
        button_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startScan();
            }
        });
    }


    private void startScan() {

        if (!beaconManager.isBound(this)) {
            Log.i(TAG, "binding Manager");
            beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(BeaconParser.EDDYSTONE_UID_LAYOUT));
            beaconManager.bind(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        beaconManager = BeaconManager.getInstanceForApplication(getApplicationContext());
        if (!beaconManager.isBound(this)) {
            beaconManager.getBeaconParsers().add(new BeaconParser().
                    setBeaconLayout(BeaconParser.EDDYSTONE_UID_LAYOUT));
            beaconManager.bind(this);
        }
        isScanning = true;
    }

    @Override
    public void onBeaconServiceConnect() {
        Log.d(TAG,"beaconManager is bound, ready to start scanning");
        beaconManager.addRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                if(isScanning){
                    if(beacons.size()>0){
                       for(Beacon beacon: beacons){
                            Log.i(TAG,"Serivce UUID is"+beacon.getServiceUuid());
                            Log.i(TAG,"Id 1"+beacon.getIdentifier(0));
                            Log.i(TAG,"Id 1"+beacon.getIdentifier(1));
                            if(beacon.getServiceUuid() == 0xfeaa && beacon.getBeaconTypeCode() == 0x00){
                                Log.d(TAG, "I see a beacon transmitting a namespace: " + (beacon.getId1().toByteArray()).toString() + "and instance" +
                                        (beacon.getId2().toByteArray()).toString() + "and distance"+ beacon.getDistance()+"meters away.");
                            }
                       }
                    }
                }
            }
        });
        try {
            beaconManager.startRangingBeaconsInRegion(new Region("com.bridou_n.beaconscanner", null, null, null));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
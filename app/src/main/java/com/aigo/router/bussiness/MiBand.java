package com.aigo.router.bussiness;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.util.Log;

/**
 * Created by zhangcirui on 2017/1/20.
 */

public class MiBand{

    private static final String	TAG	= "miband-android";

    public static void startScan(ScanCallback callback)
    {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if(null == adapter)
        {
            Log.e(TAG,"BluetoothAdapter is null");
            return;
        }
        BluetoothLeScanner scanner = adapter.getBluetoothLeScanner();
        if(null == scanner){
            Log.e(TAG,"BluetoothLeScanner is null");
            return;
        }
        scanner.startScan(callback);
    }

    public static void stopScan(ScanCallback callback){
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if(null == adapter)
        {
            Log.e(TAG,"BluetoothAdapter is null");
            return;
        }
        BluetoothLeScanner scanner = adapter.getBluetoothLeScanner();
        if(null == scanner){
            Log.e(TAG,"BluetoothLeScanner is null");
            return;
        }
        scanner.stopScan(callback);
    }


}

package com.parrot.audric.parrotzik.service;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.parrot.audric.parrotzik.zikapi.ZikBluetoothHelper;

public class ZikBluetoothReceiver extends BroadcastReceiver {
    private static final String TAG = "ZikBluetoothReceiver";


    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
            final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
            switch(state) {
                case BluetoothAdapter.STATE_OFF:
                    Log.i(TAG, "state: off: " + state );
                    LocalBroadcastManager.getInstance(context).sendBroadcast(
                            new Intent(ZikIntent.ACTION_ZIK_DISCONNECTED.toString())
                    );
                    break;
                default:
            }
        }
        else if(action.equals(BluetoothDevice.ACTION_ACL_CONNECTED)) {
            if(ZikBluetoothHelper.getZikDevice() != null) {
                Log.i(TAG, "A zik has been found in the bounded device.. starting zik service !");
                Intent zikService = new Intent(context, ZikService.class);
                context.startService(zikService);
            }
        }
        else if(action.equals(BluetoothDevice.ACTION_ACL_DISCONNECTED)) {
            if(ZikBluetoothHelper.getZikDevice() == null) {
                Log.i(TAG, "No zik found, we stop the service");
                Intent zikService = new Intent(context, ZikService.class);
                context.stopService(zikService);
                LocalBroadcastManager.getInstance(context).sendBroadcast(
                        new Intent(ZikIntent.ACTION_ZIK_DISCONNECTED.toString()));

            }
        }
    }
}

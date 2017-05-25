package com.parrot.audric.parrotzik.service;

import android.app.Service;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.parrot.audric.parrotzik.ui.Intents;
import com.parrot.audric.parrotzik.zikapi.ZikBluetoothHelper;
import com.parrot.audric.parrotzik.zikapi.ZikConnection;

public class ZikService extends Service {
    private static final String TAG = "ZikService";

    private LocalBroadcastManager localBroadcastManager;

    private ZikBluetoothHelper zikBluetoothHelperConnector;
    private ZikConnection zikConnection;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                BluetoothDevice zik = zikBluetoothHelperConnector.getZikDevice();
                if (zik != null) {
                    if(zikConnection != null) {
                        if(zikConnection.isConnected()) {
                            zikConnection.close();
                        }
                        zikConnection = null;
                    }

                    zikConnection = new ZikConnection(zik, getApplicationContext());
                    if(zikConnection.connect()) {
                        localBroadcastManager.sendBroadcast(
                                new Intent(ZikBroadcastIntent.ACTION_ZIK_CONNECTED.toString()));

                        zikConnection.refreshZikState();

                        Intent toSend = new Intent(ZikBroadcastIntent.ACTION_ZIK_STATE_REFRESH.toString());
                        toSend.putExtra(Intents.INTENT_STATE, zikConnection.getState());
                        localBroadcastManager.sendBroadcast(toSend);

                    }
                    else {
                        localBroadcastManager.sendBroadcast(
                                new Intent(ZikBroadcastIntent.ACTION_ZIK_DISCONNECTED.toString()));

                    }
                }
            }
        }).start();

        return START_STICKY;
    }

    @Override
    public void onCreate() {
        Log.i(TAG, "Zik service starting");
        Toast.makeText(this, "zik service started", Toast.LENGTH_LONG).show();

        zikBluetoothHelperConnector = new ZikBluetoothHelper();
        localBroadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());
    }




    @Override
    public void onDestroy() {
        Toast.makeText(this, "zik service destroyed", Toast.LENGTH_LONG).show();
        if(zikConnection != null && zikConnection.isConnected())
            zikConnection.close();
    }
}

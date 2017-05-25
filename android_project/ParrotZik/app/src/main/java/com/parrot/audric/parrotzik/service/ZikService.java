package com.parrot.audric.parrotzik.service;

import android.app.Service;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.parrot.audric.parrotzik.zikapi.ZikBluetoothHelper;
import com.parrot.audric.parrotzik.zikapi.ZikConnection;

public class ZikService extends Service {


    private static final String TAG = "ZikService";

    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;
    private LocalBroadcastManager localBroadcastManager;



    private ZikBluetoothHelper zikBluetoothHelperConnector;
    private ZikConnection zikConnection;

    @Nullable
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
                                new Intent(ZikIntent.ACTION_ZIK_CONNECTED.toString()));
                    }
                    else {
                        localBroadcastManager.sendBroadcast(
                                new Intent(ZikIntent.ACTION_ZIK_DISCONNECTED.toString()));

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


        localBroadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());

        HandlerThread thread = new HandlerThread("ZikHandlerThread",
                Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);
    }




    // Handler that receives messages from the thread
    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                // Restore interrupt status.
                Thread.currentThread().interrupt();
            }
        }
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "zik service destroyed", Toast.LENGTH_LONG).show();

        if(zikConnection != null && zikConnection.isConnected())
            zikConnection.close();
    }
}

package com.parrot.audric.parrotzik.zikapi;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;
import java.util.UUID;

public class ZikBluetoothHelper {
    private  static final String TAG = "ZikBluetoothHelper";
    // Parrot Zik headphones MAC address regex (90:03:B7 is Parrot vendor id)
    private static final String MAC = "90:03:B7:[0-9A-Fa-f]{2}:[0-9A-Fa-f]{2}:[0-9A-Fa-f]{2}";

    // UUID to communicate with Parrot Zik devices
    public static final java.util.UUID uuid = UUID.fromString("0ef0f502-f0ee-46c9-986c-54ed027807fb");
    private BluetoothSocket socket;


    private BluetoothAdapter getDefaultAdapter() {
        return BluetoothAdapter.getDefaultAdapter();
    }

    private Set<BluetoothDevice> getBoundedDevice() {
        BluetoothAdapter adapter = getDefaultAdapter();

        if(adapter == null) {
            Log.e(TAG, "No bluetooth adapter found.");
            return null;
            //TODO better handle of case
        }
        else {
            return adapter.getBondedDevices();
        }
    }


    public BluetoothDevice getZikDevice() {
        Set<BluetoothDevice> devices = getBoundedDevice();

        if(devices != null) {
            for (BluetoothDevice device : devices) {
                if (device.getAddress().matches(MAC))
                    return device;
            }
        }
        return null;
    }


    public BluetoothSocket createAndConnectSocket(BluetoothDevice device) {

        BluetoothSocket socket = null;

        if(BluetoothAdapter.getDefaultAdapter().isDiscovering())
            BluetoothAdapter.getDefaultAdapter().cancelDiscovery();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            socket = device.createRfcommSocketToServiceRecord(uuid);
        } catch (Exception e) {Log.e("","Error creating socket");}

        try {
            socket.connect();
            Log.e(TAG,"Connected");
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
            try {
                Log.e(TAG, "trying fallback...");

                socket = (BluetoothSocket) device.getClass().getMethod("createRfcommSocket", new Class[]{int.class}).invoke(device, 1);
                Log.i(TAG,"socket already connected? " + socket.isConnected());
                if(!socket.isConnected())
                    socket.connect();

                Log.e(TAG, "Connected");
            } catch (Exception e2) {
                Log.e(TAG, "Couldn't establish ZikBluetoothHelper connection!");
                e2.printStackTrace();
            }

        }
        return socket;
    }


    public BluetoothSocket connect(BluetoothDevice device) throws IOException {
        socket = createAndConnectSocket(device);
        return socket;
    }

}

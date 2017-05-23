package com.parrot.audric.parrotzik.zikapi;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by audric on 21/05/17.
 */

public class Connection {
    private static final String TAG = "Connection";


    private final Context context;
    private final BluetoothDevice device;

    private BluetoothSocket socket;
    private State state = new State();
    private BluetoothAdapter bluetoothAdapter;
    private InputStream inputStream;
    private OutputStream outputStream;

    private Parser parser;

    public Connection(BluetoothDevice device, Context context) {
        this.device = device;
        this.context  = context;

        this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        parser = new Parser(state);
    }


    public boolean connect() {
        Bluetooth bluetooth = new Bluetooth();
        try {
            socket = bluetooth.connect(device);

            Log.w(TAG,"Connected ?: " + socket.isConnected());
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
            if( write(new byte[] {0, 3, 0}))
           //     skip(1024);
            Log.i(TAG, " wrote init");

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


    private void read() {
        Log.e(TAG, "skip before read... ");

        skip(10);
        byte[] data = new byte[1024];
        try {
            Log.e(TAG, "starting read: ");

            int size = inputStream.read(data);
            String s = new String(data, 0 , size);

            Log.e(TAG, "read ret: " + s);
            parser.parse(s);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private boolean skip(long i)  {
        try {
            Log.e(TAG, "skipping " + i + " available: " + inputStream.available() + " skipped: " + inputStream.skip(i));

            Log.e(TAG, "done skipping " + i);

            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    private boolean write(byte[] data) {
        try {
            Log.e(TAG, "write");
            outputStream.write(data);
            Log.e(TAG, "flush");
            outputStream.flush();
            Log.e(TAG, "end");

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


    public void getBattery() {
        Log.e(TAG, "getbattery");
        write(Protocol.getRequest(Constants.BatteryGet));
        read();
    }


    public boolean isConnected() {
        return  socket != null && socket.isConnected();
    }

    public void close() {
        if(isConnected()) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            socket = null;
        }
    }


    public State getState() {
        return state;//TODO make a copy
    }
}

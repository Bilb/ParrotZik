package com.parrot.audric.parrotzik.zikapi;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.parrot.audric.parrotzik.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;


public class ZikConnection {
    private static final String TAG = "ZikConnection";


    private final BluetoothDevice device;

    private BluetoothSocket socket;
    private State state = new State();
    private InputStream inputStream;
    private OutputStream outputStream;
    private Parser parser;

    public ZikConnection(BluetoothDevice device, Context context) {
        this.device = device;
        parser = new Parser(state);
    }


    public boolean connect() {
        ZikBluetoothHelper zikBluetoothHelper = new ZikBluetoothHelper();
        try {
            socket = zikBluetoothHelper.connect(device);

            Log.w(TAG,"Connected ?: " + socket.isConnected());
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
            if(write(new byte[] {0, 3, 0}))
                skip(3);

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


    private void read() {
        byte[] data = new byte[1024];
        try {
            int size = inputStream.read(data);
            String s = new String(data, 0 , size);
            int start = s.indexOf('<');
            s = s.substring(start);
            Log.d(TAG, "after filter: " + s);
            parser.parse(s);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private boolean skip(long i)  {
        try {
            inputStream.skip(i);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    private boolean write(byte[] data) {
        try {
            outputStream.write(data);
            outputStream.flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


    public State.Battery getBattery() {
        write(Protocol.getRequest(Constants.BatteryGet));
        read();
        return state.getBattery();
    }


    public boolean getNoiseCancellationStatus() {
        write(Protocol.getRequest(Constants.ANCEnableGet));
        read();
        return state.getNoiseCancellation();
    }

    public boolean setNoiseCancellationStatus(boolean enabled) {
        if(write(Protocol.setRequest(Constants.ANCEnableSet, String.valueOf(enabled))))
            state.setNoiseCancellation(enabled);

        return state.getNoiseCancellation();
    }

    public void getEqualizerStatus() {
        write(Protocol.getRequest(Constants.EqualizerEnabledGet));
        read();
    }

    public boolean setEqualizerStatus(boolean enabled) {
        if(write(Protocol.setRequest(Constants.EqualizerEnabledSet, String.valueOf(enabled)))) {
            state.setEqualizer(enabled);
        }
        return state.getEqualizer();
    }

    public void getConcertHallStatus() {
        write(Protocol.getRequest(Constants.SoundEffectEnabledGet));
        read();
    }

    public boolean setConcertHallStatus(boolean enabled) {
        if(write(Protocol.setRequest(Constants.SoundEffectEnabledSet, String.valueOf(enabled)))) {
            state.setConcertHall(enabled);
        }
        return state.getConcertHall();
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
        return state;
    }




    public void refreshZikState(final ZiKStateRefreshListnener listener) {
        final Handler handler = new Handler();

        new Thread(new Runnable() {
            @Override
            public void run() {
                getBattery();
                getNoiseCancellationStatus();
                getEqualizerStatus();
                getConcertHallStatus();

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onZikStateRefreshed(state);
                    }
                });
            }
        }).start();
    }


    public interface ZiKStateRefreshListnener {
        void onZikStateRefreshed(State state);
    }


    public boolean toggleAnc() {
        boolean currentAnc = getState().getNoiseCancellation();
        currentAnc = setNoiseCancellationStatus(!currentAnc);

        return currentAnc;
    }

    public boolean toggleEqualizer() {
        boolean current = getState().getEqualizer();
        current = setEqualizerStatus(!current);

        return current;
    }

    public boolean toggleConcertHall() {
        boolean current = getState().getConcertHall();
        current = setConcertHallStatus(!current);

        return current;
    }


}

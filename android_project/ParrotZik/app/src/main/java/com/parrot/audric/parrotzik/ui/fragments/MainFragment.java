package com.parrot.audric.parrotzik.ui.fragments;

/**
 * Created by audric on 06/05/17.
 */

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHeadset;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import com.parrot.audric.parrotzik.R;
import com.parrot.audric.parrotzik.databinding.FragmentMainBinding;
import com.parrot.audric.parrotzik.zikapi.Bluetooth;
import com.parrot.audric.parrotzik.zikapi.BluetoothConnector;
import com.parrot.audric.parrotzik.zikapi.Connection;
import com.vstechlab.easyfonts.EasyFonts;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainFragment extends Fragment {

    private static final String TAG = "MainFragment";

    private FragmentMainBinding binding;


    private Bluetooth bluetoothConnector;

    private Connection connection;
    public MainFragment() {
        bluetoothConnector = new Bluetooth();
    }

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_main, container, false);
        binding.titleAnc.setTypeface(EasyFonts.robotoLight(getContext()));
        binding.titleConcertHall.setTypeface(EasyFonts.robotoLight(getContext()));
        binding.titleEq.setTypeface(EasyFonts.robotoLight(getContext()));



        return binding.getRoot();

    }

    @Override
    public void onAttach(final Context context) {
        super.onAttach(context);

        new Thread(new Runnable() {
            @Override
            public void run() {
                BluetoothDevice zik = bluetoothConnector.getZikDevice();

                Log.e(TAG, "zik: " + zik);
                if(zik != null) {
                    connection = new Connection(zik, context);
                    connection.connect();

                    connection.getBattery();
                    Log.i(TAG, "batterlyevel: " + connection.getState().getBatteryLevel());

                    if(connection.getState().getBatteryLevel() != -1) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                binding.customProgressBar.setProgressWithAnimationx(connection.getState().getBatteryLevel());
                                animateTextView(0, connection.getState().getBatteryLevel(), binding.percentageBatteryTv);

                            }
                        });
                    }
                }
            }
        }).start();
    }


    @Override
    public void onDetach() {
        super.onDetach();
        if(connection != null && connection.isConnected())
            connection.close();
    }

    public void animateTextView(int initialValue, int finalValue, final TextView textview) {
        DecelerateInterpolator interpolator = new DecelerateInterpolator(0.8f);

        int start = Math.min(initialValue, finalValue);
        int end = Math.max(initialValue, finalValue);
        int difference = Math.abs(finalValue - initialValue);
        Handler handler = new Handler();
        for (int count = start; count <= end; count++) {
            int time = Math.round(interpolator.getInterpolation((((float) count) / difference)) * 1500);
            final int finalCount = ((initialValue > finalValue) ? initialValue - count : count);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    textview.setText(String.valueOf(finalCount));
                }
            }, time);
        }
    }


}

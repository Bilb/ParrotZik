package com.parrot.audric.parrotzik.ui.fragments;

/**
 * Created by audric on 06/05/17.
 */

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHeadset;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import com.parrot.audric.parrotzik.R;
import com.parrot.audric.parrotzik.databinding.FragmentMainBinding;
import com.parrot.audric.parrotzik.ui.ViewUtils;
import com.parrot.audric.parrotzik.zikapi.State;
import com.parrot.audric.parrotzik.zikapi.ZikBluetoothHelper;
import com.parrot.audric.parrotzik.zikapi.ZikConnection;
import com.vstechlab.easyfonts.EasyFonts;

public class MainFragment extends Fragment {

    private static final String TAG = "MainFragment";

    private FragmentMainBinding binding;


    private ZikBluetoothHelper zikBluetoothHelperConnector;

    private ZikConnection zikConnection;

    public MainFragment() {
        zikBluetoothHelperConnector = new ZikBluetoothHelper();
    }

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    BluetoothHeadset mBluetoothHeadset;



    private BluetoothProfile.ServiceListener mProfileListener = new BluetoothProfile.ServiceListener() {
        public void onServiceConnected(int profile, BluetoothProfile proxy) {
            if (profile == BluetoothProfile.HEADSET) {
                mBluetoothHeadset = (BluetoothHeadset) proxy;
                Log.e(TAG, "got it");
            }
        }
        public void onServiceDisconnected(int profile) {
            if (profile == BluetoothProfile.HEADSET) {
                mBluetoothHeadset = null;
                Log.e(TAG, "lost it");
            }
        }
    };

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
        BluetoothAdapter.getDefaultAdapter().getProfileProxy(context, mProfileListener, BluetoothProfile.HEADSET);



        new Thread(new Runnable() {
            @Override
            public void run() {
                BluetoothDevice zik = zikBluetoothHelperConnector.getZikDevice();

                Log.e(TAG, "zik: " + zik);
                if(zik != null) {
                    zikConnection = new ZikConnection(zik, context);
                    zikConnection.connect();

                    zikConnection.getBattery();

                    final boolean noiseCancellation = zikConnection.getNoiseCancellationStatus();

                    Integer tagColor = Color.rgb(Color.red(Color.WHITE),Color.green(Color.WHITE),
                            Color.blue(Color.WHITE));
                    binding.ancButton.setTag(tagColor);
                    binding.titleAnc.setTag(tagColor);


                    binding.ancButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            toggleAnc();
                        }
                    });


                    final State.Battery zikBattery = zikConnection.getBattery();
                    if(zikBattery != null ) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                switch (zikBattery.state) {
                                    case CHARGING:
                                        binding.chargingIv.setVisibility(View.VISIBLE);
                                        break;
                                    default:
                                        binding.chargingIv.setVisibility(View.INVISIBLE);
                                        binding.customProgressBar.setProgressWithAnimation(zikBattery.level);
                                        ViewUtils.animateTextView(0, zikBattery.level, binding.percentageBatteryTv);
                                }

                                if(noiseCancellation)
                                    animateNoiseCancellationChanges(getResources().getColor(R.color.orangeParrot));
                                else
                                    animateNoiseCancellationChanges(getResources().getColor(R.color.colorPrimary));
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
        BluetoothAdapter.getDefaultAdapter().closeProfileProxy(BluetoothProfile.HEADSET, mBluetoothHeadset);
        if(zikConnection != null && zikConnection.isConnected())
            zikConnection.close();
    }



    private void toggleAnc() {
        boolean currentAnc = zikConnection.getState().getNoiseCancellation();
        currentAnc = zikConnection.setNoiseCancellationStatus(!currentAnc);

        if(currentAnc)
            animateNoiseCancellationChanges(getResources().getColor(R.color.orangeParrot));
        else
            animateNoiseCancellationChanges(getResources().getColor(R.color.colorPrimary));

    }



    private void animateNoiseCancellationChanges(int endColor) {
        ViewUtils.animateViewColorChange(binding.ancButton,
                ((Integer) (binding.ancButton.getTag())),
                endColor);

        Integer tagColor = Color.rgb(Color.red(endColor),Color.green(endColor),
                Color.blue(endColor));
        binding.ancButton.setTag(tagColor);

        ViewUtils.animateViewColorChange(binding.titleAnc,
                ((Integer) (binding.titleAnc.getTag())),
                endColor);
        binding.titleAnc.setTag(tagColor);
    }


    public class BluetoothConnectionReceiver extends BroadcastReceiver {
        public BluetoothConnectionReceiver(){
            //No initialisation code needed
        }

        @Override
        public void onReceive(Context context, Intent intent){

            Toast.makeText(context, "ACTION : " + intent.getAction(), Toast.LENGTH_SHORT).show();
            if(BluetoothDevice.ACTION_ACL_CONNECTED.equals(intent.getAction())){
                Snackbar.make(getView(), "connected",Snackbar.LENGTH_LONG).show();
            } else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(intent.getAction())){
                Snackbar.make(getView(), "connected",Snackbar.LENGTH_LONG).show();
            }
        }
    }

}

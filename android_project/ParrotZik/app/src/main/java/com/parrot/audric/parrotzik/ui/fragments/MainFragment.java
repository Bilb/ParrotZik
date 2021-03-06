package com.parrot.audric.parrotzik.ui.fragments;

/**
 * Created by audric on 06/05/17.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

import com.parrot.audric.parrotzik.R;
import com.parrot.audric.parrotzik.databinding.FragmentMainBinding;
import com.parrot.audric.parrotzik.service.ZikBroadcastIntent;
import com.parrot.audric.parrotzik.ui.Intents;
import com.parrot.audric.parrotzik.ui.activity.MainActivity;
import com.parrot.audric.parrotzik.ui.view.ColorUtils;
import com.parrot.audric.parrotzik.ui.view.ViewUtils;
import com.parrot.audric.parrotzik.zikapi.State;
import com.vstechlab.easyfonts.EasyFonts;

public class MainFragment extends Fragment {

    private static final String TAG = "MainFragment";

    private FragmentMainBinding binding;


    private ZikBroadcastReceiver zikBroadcastReceiver;


    private boolean isZikConnected = false;

    private State zikState;


    public MainFragment() {
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


    private void setTagColor(View view, int color) {
        Integer tagColor = Color.rgb(Color.red(color),Color.green(color), Color.blue(color));
        view.setTag(tagColor);
    }

    @Override
    public void onAttach(final Context context) {
        super.onAttach(context);

        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(getActivity());
        if(zikBroadcastReceiver == null)
            zikBroadcastReceiver = new ZikBroadcastReceiver();
        localBroadcastManager.registerReceiver(zikBroadcastReceiver, ZikBroadcastIntent.getGlobalFilter());
    }


    private class ZikBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            ZikBroadcastIntent zikBroadcastIntent = ZikBroadcastIntent.valueOf(intent.getAction());


            Log.w(TAG, "localbroadcatreceiver action:  " + zikBroadcastIntent);

            switch (zikBroadcastIntent) {
                case ACTION_ZIK_CONNECTED:
                    break;
                case ACTION_ZIK_DISCONNECTED:
                    isZikConnected = false;
                    break;
                case ACTION_ZIK_STATE_REFRESH:
                    isZikConnected = true;
                    zikState = intent.getParcelableExtra(Intents.INTENT_STATE);
                    Log.i(TAG, "zikState: " + zikState);
            }
        }
    }



    @Override
    public void onDetach() {
        super.onDetach();
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(getActivity());
        localBroadcastManager.unregisterReceiver(zikBroadcastReceiver);
    }



    private void toggleAnc() {
        MainActivity mainActivity = (MainActivity) getActivity();
        boolean currentAnc = mainActivity.getZikService().toggleAnc();

        if(currentAnc)
            animateNoiseCancellationChanges(getResources().getColor(R.color.tintEnabled));
        else
            animateNoiseCancellationChanges(getResources().getColor(R.color.tintDisabled));
    }

    private void toggleEqualizer() {
        MainActivity mainActivity = (MainActivity) getActivity();

        boolean currentEqualizer = mainActivity.getZikService().toggleEqualizer();

        if(currentEqualizer)
            animateEqualizerChanges(getResources().getColor(R.color.tintEnabled));
        else
            animateEqualizerChanges(getResources().getColor(R.color.tintDisabled));
    }

    private void toggleConcertHall() {
        MainActivity mainActivity = (MainActivity) getActivity();

        boolean currentConcertHall = mainActivity.getZikService().toggleConcertHall();

        if(currentConcertHall)
            animateConcertHallChanges(getResources().getColor(R.color.tintEnabled));
        else
            animateConcertHallChanges(getResources().getColor(R.color.tintDisabled));
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


    private void animateEqualizerChanges(int endColor) {
        ViewUtils.animateViewColorChange(binding.equalizerButton,
                ((Integer) (binding.equalizerButton.getTag())),
                endColor);

        Integer tagColor = Color.rgb(Color.red(endColor),Color.green(endColor),
                Color.blue(endColor));
        binding.equalizerButton.setTag(tagColor);

        ViewUtils.animateViewColorChange(binding.titleEq,
                ((Integer) (binding.titleEq.getTag())),
                endColor);
        binding.titleEq.setTag(tagColor);
    }

    private void animateConcertHallChanges(int endColor) {
        ViewUtils.animateViewColorChange(binding.concertHallButton,
                ((Integer) (binding.concertHallButton.getTag())),
                endColor);

        Integer tagColor = Color.rgb(Color.red(endColor),Color.green(endColor),
                Color.blue(endColor));
        binding.concertHallButton.setTag(tagColor);

        ViewUtils.animateViewColorChange(binding.titleConcertHall,
                ((Integer) (binding.titleConcertHall.getTag())),
                endColor);
        binding.titleConcertHall.setTag(tagColor);
    }


    @Override
    public void onStart() {
        super.onStart();

        int colorNotConnected = ColorUtils.getColor(getActivity(), R.color.tintNotConnected);


        binding.titleEq.setTextColor(colorNotConnected);
        binding.titleConcertHall.setTextColor(colorNotConnected);
        binding.titleAnc.setTextColor(colorNotConnected);


        binding.ancButton.setColorFilter(colorNotConnected);
        binding.concertHallButton.setColorFilter(colorNotConnected);
        binding.equalizerButton.setColorFilter(colorNotConnected);


        setTagColor(binding.ancButton, colorNotConnected);
        setTagColor(binding.concertHallButton, colorNotConnected);
        setTagColor(binding.equalizerButton, colorNotConnected);

        setTagColor(binding.titleAnc, colorNotConnected);
        setTagColor(binding.titleConcertHall, colorNotConnected);
        setTagColor(binding.titleEq, colorNotConnected);

        binding.customProgressBar.setProgress(0);
        binding.percentageBatteryTv.setText("0");


        binding.ancButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isZikConnected)
                    toggleAnc();
            }
        });

        binding.concertHallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isZikConnected)
                    toggleConcertHall();
            }
        });

        binding.equalizerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isZikConnected)
                    toggleEqualizer();
            }
        });


        final RotateAnimation animRotate = new RotateAnimation(0.0f, -358.0f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);

        animRotate.setDuration(1500);
        animRotate.setRepeatCount(-1);
        animRotate.setFillAfter(true);
        animRotate.setInterpolator(new LinearInterpolator());

        binding.customProgressBar.setProgress(1);
        binding.customProgressBar.startAnimation(animRotate);

        animRotate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) { }

            @Override
            public void onAnimationRepeat(Animation animation) {
                if(isZikConnected) {
                    animation.cancel();
                    binding.customProgressBar.setProgress(0);
                    updateUI(zikState);
                }
            }
        });
    }



    private void updateUI(State state) {
        final State.Battery zikBattery = state.getBattery();
        if (zikBattery != null) {
            switch (zikBattery.state) {
                case CHARGING:
                    binding.chargingIv.setVisibility(View.VISIBLE);
                    break;
                default:
                    binding.chargingIv.setVisibility(View.INVISIBLE);
                    binding.customProgressBar.setProgressWithAnimation(zikBattery.level);
                    ViewUtils.animateTextView(0, zikBattery.level, binding.percentageBatteryTv);
            }

            if (state.getNoiseCancellation())
                animateNoiseCancellationChanges(getResources().getColor(R.color.tintEnabled));
            else
                animateNoiseCancellationChanges(getResources().getColor(R.color.tintDisabled));

            if (state.getEqualizer())
                animateEqualizerChanges(getResources().getColor(R.color.tintEnabled));
            else
                animateEqualizerChanges(getResources().getColor(R.color.tintDisabled));

            if (state.getConcertHall())
                animateConcertHallChanges(getResources().getColor(R.color.tintEnabled));
            else
                animateConcertHallChanges(getResources().getColor(R.color.tintDisabled));
        }
    }
}

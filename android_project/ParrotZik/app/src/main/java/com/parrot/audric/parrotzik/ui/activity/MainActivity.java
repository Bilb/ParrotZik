package com.parrot.audric.parrotzik.ui.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;

import com.parrot.audric.parrotzik.R;
import com.parrot.audric.parrotzik.databinding.ActivityMainBinding;
import com.parrot.audric.parrotzik.service.ZikService;
import com.parrot.audric.parrotzik.ui.fragments.MainFragment;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private SectionsPagerAdapter sectionsPagerAdapter;

    private ActivityMainBinding binding;


    private String parrotZikTwoColors;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        binding.container.setAdapter(sectionsPagerAdapter);

        binding.pagerIndicator.setViewPager(binding.container);

        binding.titleTextSwitcher.setInAnimation(this, R.anim.fade_in_fast);
        binding.titleTextSwitcher.setOutAnimation(this, R.anim.fade_out_fast);
        parrotZikTwoColors = "<font color=" + getResources().getColor(R.color.darkTextColor) +
                ">Parrot</font> <font color=" + getResources().getColor(R.color.orangeParrot)
                + ">Zik</font>";

        binding.titleTextSwitcher.setText(Html.fromHtml(parrotZikTwoColors));



        binding.container.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //TODO better animation of title switch viewpager using positionOffset to get percentage
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 1:
                        binding.titleTextSwitcher.setText("Contrôle du bruit");
                        break;
                    case 2:
                        binding.titleTextSwitcher.setText("Egaliseur");
                        break;
                    case 3:
                        binding.titleTextSwitcher.setText("Parrot Concert Hall");
                        break;
                    case 4:
                        binding.titleTextSwitcher.setText("Carte du bruit");
                        break;
                    default:

                        binding.titleTextSwitcher.setText(Html.fromHtml(parrotZikTwoColors));
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) { }
        });

        Intent intent = new Intent(MainActivity.this, ZikService.class);
        startService(intent);
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {


            if(position == 0)
                return MainFragment.newInstance();
            else
                return null;
        }

        @Override
        public int getCount() {
            return 1;
            //5

        }


        @Override
        public CharSequence getPageTitle(int position) {
            return null;
        }
    }
}

package br.com.indirim.quickphoto;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;

public class AboutActivity extends FragmentActivity {
    FragmentPagerAdapter adapterViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_about);

        Toolbar bar = (Toolbar) findViewById(R.id.toolbar);
        bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            ViewPager view_pager = (ViewPager) findViewById(R.id.view_pager);
            adapterViewPager = new AboutActivityPagerAdapter(getSupportFragmentManager());
            view_pager.setAdapter(adapterViewPager);
        }
    }

    public static class AboutActivityPagerAdapter extends FragmentPagerAdapter {
        private static int NUM_ITEMS = 5;

        public AboutActivityPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            return AboutPagerFragment.newInstance(position);
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position)
        {
            ArrayList<String> integrantes = new ArrayList<String>();

            integrantes.add("Paulo");
            integrantes.add("Giani");
            integrantes.add("Julio");
            integrantes.add("Ben");
            integrantes.add("Jorge");

            return integrantes.get(position);
        }
    }
}
package br.com.indirim.quickphoto;

import br.com.indirim.quickphoto.util.SystemUiHider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class TutorialActivity extends FragmentActivity {
    FragmentPagerAdapter adapterViewPager;
    private Button skipButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_tutorial);

        skipButton =  (Button) findViewById(R.id.skipButton);

        Activity activity = this;

        skipButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(TutorialActivity.this, CameraActivity.class);
                //intent.putExtra(CameraActivity.SKIP_TUTORIAL, true);
                startActivity(intent);
            }
        });

        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            ViewPager view_pager = (ViewPager) findViewById(R.id.view_pager);
            adapterViewPager = new TutorialActivityPagerAdapter(getSupportFragmentManager());
            view_pager.setAdapter(adapterViewPager);
        }
    }

    public static class TutorialActivityPagerAdapter extends FragmentPagerAdapter {
        private static int NUM_ITEMS = 8;

        public TutorialActivityPagerAdapter(FragmentManager fragmentManager) {
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
            return TutorialPagerFragment.newInstance(position);
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            return "Page " + position;
        }

    }
}

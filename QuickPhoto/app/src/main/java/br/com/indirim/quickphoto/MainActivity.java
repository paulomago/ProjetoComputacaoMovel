package br.com.indirim.quickphoto;

import android.content.Intent;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;

import java.io.File;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        String imagePath = getIntent().getStringExtra(MainActivity.getCurrentImageName());
        FragmentManager fragmentManager = getSupportFragmentManager();

        if (imagePath != null)
        {
            PreviewPhotoFragment previewFragment = PreviewPhotoFragment.newInstance(imagePath);

            fragmentManager.beginTransaction()
                    .replace(R.id.container, previewFragment)
                    .commit();
        } else {
            fragmentManager.beginTransaction()
                    .replace(R.id.container, new GalleryFragment())
                    .commit();
        }
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        Intent intent;
        FragmentManager fragmentManager = getSupportFragmentManager();

        switch(position)
        {
            case 0:
                intent = new Intent(this, CameraActivity.class);
                intent.putExtra(CameraActivity.SKIP_TUTORIAL, true);
                startActivity(intent);
                break;
            case 1:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new GalleryFragment())
                        .commit();
                break;
            case 2:
                intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;
            case 3:
                intent = new Intent(this, TutorialActivity.class);
                startActivity(intent);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    public static File getPhotoDirectory()
    {
        return new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "QuickPhoto");
    }

    public static String getCurrentImageName()
    {
        return "br.com.indirim.quickphoto.IMAGE";
    }
}

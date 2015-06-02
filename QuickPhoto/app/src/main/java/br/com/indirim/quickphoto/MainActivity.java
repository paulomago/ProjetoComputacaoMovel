package br.com.indirim.quickphoto;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
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

    public static final String SKIP_TUTORIAL_CHECKBOX = "skip_tutorial_checkbox";
    public static final String SKIP_CAMERA_CHECKBOX = "skip_camera_checkbox";
    public static final String IMAGE_PATH = "IMAGE_PATH";
    public static final String IMAGE_ROTATE = "IMAGE_ROTATE";

    private static final int SELECT_PICTURE = 1;

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

        String imagePath = getIntent().getStringExtra(MainActivity.IMAGE_PATH);
        FragmentManager fragmentManager = getSupportFragmentManager();

        if (imagePath == null) {
            SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(this);

            boolean skipTutorialConfig = preference.getBoolean(MainActivity.SKIP_TUTORIAL_CHECKBOX, false);

            if (!skipTutorialConfig) {
                Intent intent = new Intent(this, TutorialActivity.class);
                startActivity(intent);
            } else {
                boolean skipCameraConfig = preference.getBoolean(MainActivity.SKIP_CAMERA_CHECKBOX, false);

                if (!skipCameraConfig) {
                    Intent intent = new Intent(this, CameraActivity.class);
                    startActivity(intent);
                } else {
                    carregarGaleria();
                }
            }
        }
        else
        {
            PreviewPhotoFragment previewFragment = PreviewPhotoFragment.newInstance(imagePath, 90);

            fragmentManager.beginTransaction()
                    .replace(R.id.container, previewFragment)
                    .commit();
        }
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        Intent intent;

        switch(position)
        {
            case 0:
                intent = new Intent(this, CameraActivity.class);
                startActivity(intent);
                break;
            case 1:
                carregarGaleria();
                break;
            case 2:
                intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;
            case 3:
                intent = new Intent(this, TutorialActivity.class);
                startActivity(intent);
                break;
            case 4:
                intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                break;
        }
    }

    public void carregarGaleria(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        //intent.setData(Uri.parse(MainActivity.getPhotoDirectory().getPath()));
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, 1);
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

    public static File getPhotoDirectory()
    {
        return new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "QuickPhoto");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                String selectedImagePath = getPath(selectedImageUri);

                Activity activity = getParent();

                PreviewPhotoFragment previewFragment = PreviewPhotoFragment.newInstance(selectedImagePath, -90);
                FragmentManager fragmentManager = getSupportFragmentManager();

                fragmentManager.beginTransaction()
                        .replace(R.id.container, previewFragment)
                        .commit();
            }
        }
    }

    /**
     * auxiliar para saber o caminho de uma imagem URI
     */
    protected String getPath(Uri uri) {

        if( uri == null ) {
            // TODO realizar algum log ou feedback do utilizador
            return null;
        }

        // Tenta recuperar a imagem da media store primeiro
        // Isto só irá funcionar para as imagens selecionadas da galeria

        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);

        if( cursor != null ){
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }

        return uri.getPath();
    }
}

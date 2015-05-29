package br.com.indirim.quickphoto;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;


public class CameraActivity extends Activity {

    public static final String SKIP_TUTORIAL = "SKIP_TUTORIAL";
    public static final String SKIP_TUTORIAL_CHECKBOX = "skip_tutorial_checkbox";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(this);

        boolean skipTutorialConfig = preference.getBoolean(CameraActivity.SKIP_TUTORIAL_CHECKBOX, false);
        boolean skipTutorialButton = getIntent().getBooleanExtra(CameraActivity.SKIP_TUTORIAL, false);

        if (!skipTutorialConfig && !skipTutorialButton) {
            Intent intent = new Intent(this, TutorialActivity.class);
            startActivity(intent);
        } else {
            setContentView(R.layout.activity_camera);

            getFragmentManager().beginTransaction()
                    .replace(R.id.main_container, CameraFragment.newInstance())
                    .commit();
        }
    }
}

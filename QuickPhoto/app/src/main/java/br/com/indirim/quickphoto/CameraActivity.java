package br.com.indirim.quickphoto;

import android.app.Activity;
import android.os.Bundle;

public class CameraActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        getFragmentManager().beginTransaction()
                .replace(R.id.main_container, CameraFragment.newInstance())
                .commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}

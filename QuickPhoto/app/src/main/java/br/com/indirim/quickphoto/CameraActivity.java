package br.com.indirim.quickphoto;

import android.app.Activity;
import android.os.Bundle;


public class CameraActivity extends Activity {
    // chamado quando a atividade e criada pela primira vez
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        if (null == savedInstanceState) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.main_container, CameraFragment.newInstance())
                    .commit();
        }
    }

}

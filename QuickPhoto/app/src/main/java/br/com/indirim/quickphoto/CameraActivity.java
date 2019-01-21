package br.com.indirim.quickphoto;

import android.app.Activity;
import android.os.Bundle;

/**
 * Tela principal de captura da c&acirc;mera
 * @link CameraFragment
 */
public class CameraActivity extends Activity {
    // chamado quando a atividade e criada pela primira vez
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        getFragmentManager().beginTransaction()
                .replace(R.id.main_container, CameraFragment.newInstance())
                .commit();
    }
}

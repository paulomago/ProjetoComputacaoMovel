package br.com.indirim.quickphoto;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.hardware.Camera;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Environment;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CameraFragment extends Fragment implements View.OnClickListener, SurfaceHolder.Callback {

    /**
     * Conversion from screen rotation to JPEG orientation.
     */
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();

    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    /**
     * Objeto que representa a câmera
     */
    private Camera camera;

    /**
     * Local de armazenamento da foto tirada
     */
    private File imageFile;

    /**
     * View para exibição do preview da imagem
     */
    private SurfaceView surfaceView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_camera, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        File picsDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "QuickPhoto");

        if (!picsDir.mkdirs()) {
            Log.e("CameraFragment", "Directory not created");
        }

        DateFormat df = new SimpleDateFormat("yyyyMMdd_hhmmss");
        Date dt = new Date();

        imageFile = new File(picsDir, "IMG_" + df.format(dt) + ".jpg");
        camera = Camera.open();

        surfaceView = (SurfaceView) view.findViewById(R.id.cameraPreview);
        surfaceView.getHolder().addCallback(this);

        view.findViewById(R.id.photoButton).setOnClickListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (camera != null) {
            camera.release();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (camera != null) {
            camera.release();
        }
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.photoButton:
                takePicture(v);
                break;
        }
    }

    /**
     * Método que tira uma foto
     * @param v
     */
    public void takePicture(View v) {
        // Cria uma classe que implementa a interface PictureCallback, que será usada como callback ao tirar uma foto
        Camera.PictureCallback jpeg = new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                FileOutputStream fos = null;
                try {
                    try {
                        // Grava os bytes da imagem no arquivo onde a foto deve ser armazenada
                        fos = new FileOutputStream(imageFile);
                        fos.write(data);
                    } finally {
                        if (fos != null) {
                            fos.close();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                ajustaOrientacaoDaCamera();

                // Inicia novamente o preview
                camera.startPreview();
            }
        };

        // Tira uma foto. O callback fornecido é chamado assim que a imagem JPEG estiver disponível
        camera.takePicture(null, null, jpeg);

        Activity activity = getActivity();

        // Mostra uma mensagem indicando que a foto foi tirada
        Toast.makeText(activity, "Foto gravada em " + imageFile.getPath(), Toast.LENGTH_LONG).show();
    }

    public static CameraFragment newInstance() {
        CameraFragment fragment = new CameraFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }

    /**
     * This is called immediately after the surface is first created.
     * Implementations of this should start up whatever rendering code
     * they desire.  Note that only one thread can ever draw into
     * a {@link android.view.Surface}, so you should not draw into the Surface here
     * if your normal rendering will be in another thread.
     *
     * @param holder The SurfaceHolder whose surface is being created.
     */
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            ajustaOrientacaoDaCamera();
            camera.setPreviewDisplay(holder);
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This is called immediately after any structural changes (format or
     * size) have been made to the surface.  You should at this point update
     * the imagery in the surface.  This method is always called at least
     * once, after {@link #surfaceCreated}.
     *
     * @param holder The SurfaceHolder whose surface has changed.
     * @param format The new PixelFormat of the surface.
     * @param width  The new width of the surface.
     * @param height The new height of the surface.
     */
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (holder.getSurface() != null) {
            try {
                camera.stopPreview();
            } catch (Exception e) {
            }

            try {
                ajustaOrientacaoDaCamera();
                camera.setPreviewDisplay(holder);
                camera.startPreview();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * This is called immediately before a surface is being destroyed. After
     * returning from this call, you should no longer try to access this
     * surface.  If you have a rendering thread that directly accesses
     * the surface, you must ensure that thread is no longer touching the
     * Surface before returning from this function.
     *
     * @param holder The SurfaceHolder whose surface is being destroyed.
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    private void ajustaOrientacaoDaCamera() {
        Activity activity = getActivity();
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        Camera.Parameters parameters = camera.getParameters();

        camera.setDisplayOrientation(ORIENTATIONS.get(rotation));
        parameters.setRotation(ORIENTATIONS.get(rotation));

        List<Camera.Size> previewSizes = parameters.getSupportedPreviewSizes();

        Camera.Size previewSize = previewSizes.get(0);
        parameters.setPreviewSize(previewSize.width, previewSize.height);

        List<String> focus = parameters.getSupportedFocusModes();
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_INFINITY);

        camera.setParameters(parameters);
    }
}
